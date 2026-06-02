package com.librarys.ferreira.core.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.librarys.ferreira.core.ui.account_plan.detail.AccountDetailScreen
import com.librarys.ferreira.core.ui.account_plan.insert.InsertAccountPlanScreen
import com.librarys.ferreira.core.ui.account_plan.list.ListAccountScreen
import com.librarys.ferreira.core.ui.trades.insert.InsertTradeScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "account_list"
    ) {
        composable("account_list") {
            ListAccountScreen(
                onAddAccountClick = { navController.navigate("insert_account") },
                onAccountClick = { account ->
                    navController.navigate("account_detail/${account.id}")
                }
            )
        }

        composable("insert_account") {
            InsertAccountPlanScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = "account_detail/{accountId}",
            arguments = listOf(navArgument("accountId") { type = NavType.StringType })
        ) {
            AccountDetailScreen(
                onBackClick = { navController.popBackStack() },
                onAddTradeClick = { navController.navigate("insert_trade") }
            )
        }

        composable("insert_trade") {
            InsertTradeScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
