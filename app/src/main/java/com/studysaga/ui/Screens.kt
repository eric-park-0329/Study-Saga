package com.studysaga.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.studysaga.vm.StudyViewModel
import androidx.compose.material3.ExperimentalMaterial3Api  // ⬅️ 없으면 추가
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@Composable
fun AppNavHost(vm: StudyViewModel) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "home") {
        composable("home") { HomeScreen(vm, nav) }
        composable("gacha") { GachaScreen(vm, nav) }
        composable("inventory") { InventoryScreen(vm, nav) }
        composable("boss") { BossScreen(vm, nav) }
        composable("settings") { SettingsScreen(vm, nav) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)                     // ⬅️ 추가
@Composable
fun TopBar(nav: NavHostController, title: String) {
    TopAppBar(
        title = { Text(title) },
        actions = {
            TextButton(onClick = { nav.navigate("settings") }) { Text("Settings") }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(vm: StudyViewModel, nav: NavHostController) {
    val ui by vm.ui.collectAsState()
    Scaffold(
        topBar = { TopBar(nav, "Study Saga") },
        bottomBar = {
            BottomAppBar {
                TextButton(onClick = { nav.navigate("gacha") }) { Text("Gacha") }
                TextButton(onClick = { nav.navigate("inventory") }) { Text("Inventory") }
                TextButton(onClick = { nav.navigate("boss") }) { Text("Boss") }
            }
        }
    ) { pad ->
        Column(
            modifier = Modifier.padding(pad).fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Welcome, ${ui.username}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Level ${ui.level} • EXP ${ui.exp}/${ui.expToNext} • Crystals ${ui.crystals}")
            Text("Today: ${ui.todayMinutes} min • Weekly: ${ui.weeklyMinutes}/${ui.weeklyGoal} min")
            Text("Daily Quest: ${if (ui.dailyCompleted) "✓ Done" else "${ui.dailyRemaining} min left"}")

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (ui.isBreak) "Break Timer" else "Study Timer", fontWeight = FontWeight.Bold)
                    Text(ui.timerText, style = MaterialTheme.typography.displaySmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (!ui.timerRunning) {
                            Button(onClick = { vm.startTimer() }) { Text(if (ui.isBreak) "Start Break" else "Start Study") }
                        } else {
                            Button(onClick = { vm.stopTimer() }) { Text("Stop") }
                        }
                        if (!ui.isBreak && !ui.timerRunning) {
                            OutlinedButton(onClick = { vm.toggleMode() }) { Text("Switch to Break") }
                        } else if (ui.isBreak && !ui.timerRunning) {
                            OutlinedButton(onClick = { vm.toggleMode() }) { Text("Back to Study") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GachaScreen(vm: StudyViewModel, nav: NavHostController) {
    val ui by vm.ui.collectAsState()
    Scaffold(topBar = { TopBar(nav, "Gacha") }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Crystals: ${ui.crystals}")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { vm.pullGacha("Bronze") }, enabled = ui.crystals >= 10) { Text("Bronze (10)") }
                Button(onClick = { vm.pullGacha("Silver") }, enabled = ui.crystals >= 30) { Text("Silver (30)") }
                Button(onClick = { vm.pullGacha("Gold") }, enabled = ui.crystals >= 60) { Text("Gold (60)") }
            }
            Spacer(Modifier.height(12.dp))
            Text("Last pull: ${ui.lastGachaText}")
        }
    }
}

@Composable
fun InventoryScreen(vm: StudyViewModel, nav: NavHostController) {
    val ui by vm.ui.collectAsState()
    Scaffold(topBar = { TopBar(nav, "Inventory") }) { pad ->
        LazyColumn(Modifier.padding(pad).padding(16.dp)) {
            items(ui.inventory) { it ->
                Card(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text("${it.name} • ${it.rarity}")
                            Text(it.description, style = MaterialTheme.typography.bodySmall)
                            if (it.type == "BOOST") Text("Boost: +${it.boostPercent}% ${it.boostTarget}")
                        }
                        if (it.type == "SKIN") {
                            TextButton(onClick = { vm.equip(it.id) }, enabled = !it.equipped) {
                                Text(if (it.equipped) "Equipped" else "Equip")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BossScreen(vm: StudyViewModel, nav: NavHostController) {
    val ui by vm.ui.collectAsState()
    Scaffold(topBar = { TopBar(nav, "Weekly Boss") }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("This Week Goal: ${ui.weeklyGoal} min")
            LinearProgressIndicator(progress = ui.weeklyMinutes.toFloat() / ui.weeklyGoal.toFloat())
            Text("${ui.weeklyMinutes} / ${ui.weeklyGoal} minutes")
            if (ui.weeklyMinutes >= ui.weeklyGoal) {
                Text("✅ Boss Cleared! Claim your bonus in Gacha rates.")
            } else {
                Text("${ui.weeklyGoal - ui.weeklyMinutes} minutes to go.")
            }
        }
    }
}

@Composable
fun SettingsScreen(vm: StudyViewModel, nav: NavHostController) {
    val ui by vm.ui.collectAsState()
    var name by remember { mutableStateOf(ui.username) }
    Scaffold(topBar = { TopBar(nav, "Settings") }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Username") })
            Button(onClick = { vm.setUsername(name) }) { Text("Save") }
            Text("Pomodoro: ${ui.studyMinutes}m / ${ui.breakMinutes}m")
        }
    }
}
