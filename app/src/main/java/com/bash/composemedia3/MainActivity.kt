package com.bash.composemedia3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bash.composemedia3.screens.LoginScreen
import com.bash.composemedia3.screens.PlayerScreen
import com.bash.composemedia3.ui.theme.ComposeMedia3Theme
import com.bash.composemedia3.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {

    val userViewModel : UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeMedia3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                   AppNavHost(modifier = Modifier.padding(innerPadding), userViewModel = userViewModel)
                }
            }
        }
    }
}


const val ROUTE_LOGIN = "login"

const val ROUTE_PLAYER = "player"

@Composable
fun AppNavHost(navHost: NavHostController = rememberNavController(), modifier: Modifier, userViewModel: UserViewModel) {
    NavHost(navController = navHost, startDestination = ROUTE_LOGIN) {
        composable(ROUTE_PLAYER) {
            val user by userViewModel.user.collectAsState()
            PlayerScreen(modifier,user)
        }

        composable(ROUTE_LOGIN) {
            LoginScreen(modifier.fillMaxSize() , onLoggedIn = {user ->
                userViewModel.setUser(user)
                navHost.navigate(ROUTE_PLAYER)
            })
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeMedia3Theme {
        Greeting("Android")
    }
}