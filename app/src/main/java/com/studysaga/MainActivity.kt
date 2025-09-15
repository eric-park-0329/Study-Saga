package com.studysaga

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.studysaga.ui.AppNavHost
import com.studysaga.ui.theme.StudySagaTheme
import com.studysaga.vm.StudyViewModel

class MainActivity : ComponentActivity() {
    private val vm: StudyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudySagaTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavHost(vm = vm)
                }
            }
        }
    }
}
