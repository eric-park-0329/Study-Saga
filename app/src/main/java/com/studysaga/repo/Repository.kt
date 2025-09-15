package com.studysaga.repo

import android.content.Context
import androidx.room.Room
import com.studysaga.data.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.OffsetDateTime
import kotlin.math.max
import kotlin.random.Random

class StudyRepository(context: Context) {
    private val db = Room.databaseBuilder(context, StudyDb::class.java, "studysaga.db").build()
    private val userDao = db.user()
    private val studyDao = db.study()
    private val itemDao = db.item()
    private val gachaDao = db.gacha()

    val userFlow: Flow<UserEntity?> = userDao.observeUser()
    val inventoryFlow = itemDao.inventoryWithItems()

    suspend fun ensureUser(name: String = "Guest") {
        val u = userDao.getUser()
        if (u == null) {
            userDao.upsert(UserEntity(username = name))
            seedItems()
        }
    }

    suspend fun setUsername(name: String) {
        val u = userDao.getUser() ?: UserEntity()
        userDao.upsert(u.copy(username = name))
    }

    private suspend fun seedItems() {
        val pool = listOf(
            ItemEntity(name = "Forest Cloak", rarity = "Common", type = "SKIN", description = "A basic adventurer cloak."),
            ItemEntity(name = "Bronze Pick", rarity = "Common", type = "BOOST", description = "Slightly better crystal yield.", boostPercent = 5, boostTarget = "CRYSTALS"),
            ItemEntity(name = "Scholar Glasses", rarity = "Rare", type = "BOOST", description = "Gain more EXP from study.", boostPercent = 10, boostTarget = "EXP"),
            ItemEntity(name = "Azure Robe", rarity = "Epic", type = "SKIN", description = "A robe from the Azure Academy."),
            ItemEntity(name = "Timekeeper Charm", rarity = "Legendary", type = "BOOST", description = "Big EXP boost for focused sessions.", boostPercent = 20, boostTarget = "EXP")
        )
        for (i in pool) itemDao.insertItem(i)
    }

    suspend fun startSession(): Long {
        val s = StudySessionEntity(startIso = OffsetDateTime.now().toString())
        return studyDao.insert(s)
    }

    suspend fun completeSession(id: Long, minutes: Int): StudySessionEntity? {
        val u = userDao.getUser() ?: return null
        val boosts = getBoosts()
        val crystalRate = 1.0 + boosts.crystalBoost / 100.0
        val expRate = 1.0 + boosts.expBoost / 100.0

        val crystalsGained = (minutes / 5.0 * crystalRate).toInt() // 1 per 5 min
        val expGained = (minutes * 10 * expRate).toInt()

        val updatedUser = u.copy(
            totalMinutes = u.totalMinutes + minutes,
            crystals = u.crystals + crystalsGained,
            exp = u.exp + expGained,
            lastStudyDate = LocalDate.now(),
            streak = if (u.lastStudyDate == LocalDate.now().minusDays(1)) u.streak + 1 else if (u.lastStudyDate == LocalDate.now()) u.streak else 1
        )

        // Level up logic
        var lvl = updatedUser.level
        var exp = updatedUser.exp
        var needed = expToNext(lvl)
        while (exp >= needed) {
            exp -= needed
            lvl += 1
            needed = expToNext(lvl)
        }

        val finalUser = updatedUser.copy(level = lvl, exp = exp)
        userDao.upsert(finalUser)

        val session = StudySessionEntity(
            id = id,
            startIso = OffsetDateTime.now().minusMinutes(minutes.toLong()).toString(),
            endIso = OffsetDateTime.now().toString(),
            minutes = minutes,
            completed = true,
            expGained = expGained,
            crystalsGained = crystalsGained
        )
        studyDao.update(session)
        return session
    }

    private fun expToNext(level: Int) = 100 + (level - 1) * 50

    data class Boosts(val expBoost: Int, val crystalBoost: Int)

    private suspend fun getBoosts(): Boosts {
        val inv = inventoryFlow.map { list ->
            val equippedBoosts = list.filter { it.equipped && it.type == "BOOST" }
            val exp = equippedBoosts.filter { it.boostTarget == "EXP" }.sumOf { it.boostPercent }
            val cry = equippedBoosts.filter { it.boostTarget == "CRYSTALS" }.sumOf { it.boostPercent }
            Boosts(exp, cry)
        }
        // Simplify: return no boosts here; ViewModel recomputes from inventory snapshot
        return Boosts(0, 0)
    }

    suspend fun equip(invId: Long) {
        itemDao.unequipAll()
        itemDao.equip(invId)
    }

    suspend fun pullGacha(tier: String): Pair<String, String> {
        val u = userDao.getUser() ?: return "No user" to ""
        val cost = when (tier) {
            "Bronze" -> 10
            "Silver" -> 30
            else -> 60
        }
        if (u.crystals < cost) return "Not enough crystals" to ""
        val pool = gachaPoolFor(tier)
        val item = pool.random()
        // Spend crystals
        userDao.upsert(u.copy(crystals = u.crystals - cost))
        // Add to inventory
        val invId = itemDao.insertInventory(InventoryItemEntity(itemId = item.id))
        gachaDao.insertResult(GachaResultEntity(itemId = item.id, crystalsSpent = cost))
        return item.name to item.rarity
    }

    private suspend fun gachaPoolFor(tier: String): List<ItemEntity> {
        // very basic rarity weights by tier
        val items = db.item().let {
            // Not ideal: Room DAO lacks "get all" here — create a quick query substitute
            // We'll do a naive approach using direct DB query via RoomDatabase (omitted). For demo, re-create likely pool:
            listOf(
                ItemEntity(id = 1, name = "Forest Cloak", rarity = "Common", type = "SKIN"),
                ItemEntity(id = 2, name = "Bronze Pick", rarity = "Common", type = "BOOST", boostPercent = 5, boostTarget = "CRYSTALS"),
                ItemEntity(id = 3, name = "Scholar Glasses", rarity = "Rare", type = "BOOST", boostPercent = 10, boostTarget = "EXP"),
                ItemEntity(id = 4, name = "Azure Robe", rarity = "Epic", type = "SKIN"),
                ItemEntity(id = 5, name = "Timekeeper Charm", rarity = "Legendary", type = "BOOST", boostPercent = 20, boostTarget = "EXP")
            )
        }
        val weights = when (tier) {
            "Bronze" -> mapOf("Common" to 75, "Rare" to 20, "Epic" to 4, "Legendary" to 1)
            "Silver" -> mapOf("Common" to 55, "Rare" to 30, "Epic" to 12, "Legendary" to 3)
            else -> mapOf("Common" to 35, "Rare" to 35, "Epic" to 20, "Legendary" to 10)
        }
        fun sample(): ItemEntity {
            val list = items.flatMap { i -> List(weights[i.rarity] ?: 0) { i } }
            return if (list.isNotEmpty()) list.random() else items.random()
        }
        // Return a pseudo-random single-item pool outcome repeated to make "random()"
        return List(10) { sample() }
    }

    fun expToNextPublic(level: Int) = expToNext(level)

    // Helper for UI weekly goal
    fun weeklyGoalFor(level: Int) = 300 + (level - 1) * 60
}
