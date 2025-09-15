package com.studysaga.vm

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.studysaga.data.InventoryRow
import com.studysaga.data.UserEntity
import com.studysaga.repo.StudyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.math.max

data class UiState(
    val username: String = "Guest",
    val level: Int = 1,
    val exp: Int = 0,
    val expToNext: Int = 100,
    val crystals: Int = 0,
    val todayMinutes: Int = 0,
    val weeklyMinutes: Int = 0,
    val weeklyGoal: Int = 300,
    val dailyCompleted: Boolean = false,
    val dailyRemaining: Int = 60,
    val inventory: List<InventoryItemUi> = emptyList(),
    val lastGachaText: String = "",
    val timerText: String = "25:00",
    val timerRunning: Boolean = false,
    val isBreak: Boolean = false,
    val studyMinutes: Int = 1,  //Tempo, 원래 25
    val breakMinutes: Int = 1  //Temp 원래 5
)

data class InventoryItemUi(
    val id: Long,
    val name: String,
    val rarity: String,
    val type: String,
    val description: String,
    val boostPercent: Int,
    val boostTarget: String,
    val equipped: Boolean
)

class StudyViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = StudyRepository(app.applicationContext)

    private val _ui = MutableStateFlow(UiState())
    val ui: StateFlow<UiState> = _ui

    private var sessionId: Long? = null
    private var timer: CountDownTimer? = null

    init {
        viewModelScope.launch {
            repo.ensureUser()
            repo.userFlow.collectLatest { u ->
                if (u != null) {
                    val today = LocalDate.now()
                    val expToNext = repo.expToNextPublic(u.level)
                    val weeklyGoal = repo.weeklyGoalFor(u.level)
                    _ui.value = _ui.value.copy(
                        username = u.username,
                        level = u.level,
                        exp = u.exp,
                        expToNext = expToNext,
                        crystals = u.crystals,
                        // For demo: recompute minutes only from total is omitted—assume tracked elsewhere
                        weeklyGoal = weeklyGoal
                    )
                }
            }
        }
        viewModelScope.launch {
            repo.inventoryFlow.collectLatest { inv ->
                _ui.value = _ui.value.copy(
                    inventory = inv.map {
                        InventoryItemUi(
                            id = it.invId, name = it.name, rarity = it.rarity, type = it.type,
                            description = it.description, boostPercent = it.boostPercent,
                            boostTarget = it.boostTarget, equipped = it.equipped
                        )
                    }
                )
            }
        }
    }

    fun setUsername(name: String) {
        viewModelScope.launch { repo.setUsername(name) }
    }

    fun startTimer() {
        val minutes = if (_ui.value.isBreak) _ui.value.breakMinutes else _ui.value.studyMinutes
        _ui.value = _ui.value.copy(timerRunning = true)
        viewModelScope.launch {
            if (!_ui.value.isBreak) {
                sessionId = repo.startSession()
            }
        }
        timer?.cancel()
        timer = object : CountDownTimer(minutes * 60_000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val m = (millisUntilFinished / 1000L / 60L).toInt()
                val s = ((millisUntilFinished / 1000L) % 60).toInt()
                _ui.value = _ui.value.copy(timerText = "%02d:%02d".format(m, s))
            }
            override fun onFinish() {
                _ui.value = _ui.value.copy(timerRunning = false)
                if (_ui.value.isBreak) {
                    // switch back to study
                    _ui.value = _ui.value.copy(isBreak = false, timerText = "%02d:%02d".format(_ui.value.studyMinutes, 0))
                } else {
                    // complete study session
                    val studied = _ui.value.studyMinutes
                    viewModelScope.launch {
                        sessionId?.let { id ->
                            repo.completeSession(id, studied)
                            sessionId = null
                            _ui.value = _ui.value.copy(
                                todayMinutes = _ui.value.todayMinutes + studied
                            )
                        }
                    }
                    // switch to break
                    _ui.value = _ui.value.copy(isBreak = true, timerText = "%02d:%02d".format(_ui.value.breakMinutes, 0))
                }
            }
        }.start()
    }

    fun stopTimer() {
        timer?.cancel()
        _ui.value = _ui.value.copy(timerRunning = false)
    }

    fun toggleMode() {
        val nowBreak = !_ui.value.isBreak
        val minutes = if (nowBreak) _ui.value.breakMinutes else _ui.value.studyMinutes
        _ui.value = _ui.value.copy(isBreak = nowBreak, timerText = "%02d:%02d".format(minutes, 0))
    }

    fun equip(invId: Long) {
        viewModelScope.launch { repo.equip(invId) }
    }

    fun pullGacha(tier: String) {
        viewModelScope.launch {
            val (name, rarity) = repo.pullGacha(tier)
            _ui.value = _ui.value.copy(lastGachaText = if (rarity.isEmpty()) name else "$name ($rarity)")
        }
    }
}
