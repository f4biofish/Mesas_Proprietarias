package com.librarys.ferreira.core

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.librarys.ferreira.core.ui.account_plan.insert.InsertAccountPlanScreen
import com.librarys.ferreira.core.ui.account_plan.list.ListAccountScreen
import com.librarys.ferreira.core.ui.theme.AppTheme
import com.librarys.ferreira.core.ui.trades.insert.InsertTradeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme() {
                InsertTradeScreen {  }
            }
        }
    }
}

