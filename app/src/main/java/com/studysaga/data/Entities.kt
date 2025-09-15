package com.studysaga.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.OffsetDateTime
import androidx.room.TypeConverters


@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val username: String = "Guest",
    val totalMinutes: Int = 0,
    val level: Int = 1,
    val exp: Int = 0,
    val crystals: Int = 0,
    val streak: Int = 0,
    val lastStudyDate: LocalDate? = null,
    val expBoost: Int = 0,       // percent
    val crystalBoost: Int = 0,   // percent
    val equippedSkinId: Long? = null
)

@Entity(tableName = "study_sessions")
data class StudySessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val startIso: String,
    val endIso: String? = null,
    val minutes: Int = 0,
    val completed: Boolean = false,
    val expGained: Int = 0,
    val crystalsGained: Int = 0
)

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val rarity: String,     // Common, Rare, Epic, Legendary
    val type: String,       // SKIN or BOOST
    val description: String = "",
    val boostPercent: Int = 0, // if BOOST
    val boostTarget: String = "" // EXP or CRYSTALS
)

@Entity(
    tableName = "inventory",
    foreignKeys = [ForeignKey(entity = ItemEntity::class,
        parentColumns = ["id"], childColumns = ["itemId"], onDelete = ForeignKey.CASCADE)]
)
data class InventoryItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemId: Long,
    val obtainedIso: String = OffsetDateTime.now().toString(),
    val equipped: Boolean = false
)

@Entity(tableName = "gacha_results")
data class GachaResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemId: Long,
    val crystalsSpent: Int,
    val pulledIso: String = OffsetDateTime.now().toString()
)

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = 1")
    fun observeUser(): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = 1")
    suspend fun getUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: UserEntity)
}

@Dao
interface StudyDao {
    @Insert
    suspend fun insert(session: StudySessionEntity): Long

    @Update
    suspend fun update(session: StudySessionEntity)

    @Query("SELECT * FROM study_sessions ORDER BY id DESC LIMIT 50")
    fun latest(): Flow<List<StudySessionEntity>>
}

@Dao
interface ItemDao {
    @Insert
    suspend fun insertItem(item: ItemEntity): Long

    @Insert
    suspend fun insertInventory(inv: InventoryItemEntity): Long

    @Query("SELECT i.id, i.name, i.rarity, i.type, i.description, i.boostPercent, i.boostTarget, inv.id as invId, inv.equipped as equipped FROM items i JOIN inventory inv ON i.id=inv.itemId ORDER BY inv.id DESC")
    fun inventoryWithItems(): Flow<List<InventoryRow>>

    @Query("UPDATE inventory SET equipped = 0")
    suspend fun unequipAll()

    @Query("UPDATE inventory SET equipped = 1 WHERE id = :invId")
    suspend fun equip(invId: Long)
}

data class InventoryRow(
    val id: Long? = null, // not used
    val name: String,
    val rarity: String,
    val type: String,
    val description: String,
    val boostPercent: Int,
    val boostTarget: String,
    val invId: Long,
    val equipped: Boolean
)

@Dao
interface GachaDao {
    @Insert
    suspend fun insertResult(res: GachaResultEntity): Long
}

@Database(
    entities = [UserEntity::class, StudySessionEntity::class, ItemEntity::class, InventoryItemEntity::class, GachaResultEntity::class],
    version = 1
)
@TypeConverters(Converters::class) // ⬅️ 추가
abstract class StudyDb : RoomDatabase() {
    abstract fun user(): UserDao
    abstract fun study(): StudyDao
    abstract fun item(): ItemDao
    abstract fun gacha(): GachaDao
}

