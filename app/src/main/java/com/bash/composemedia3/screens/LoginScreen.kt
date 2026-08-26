package com.bash.composemedia3.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bash.composemedia3.data.User
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(modifier: Modifier, onLoggedIn: (user: User) -> Unit) {

    val isLoggedIn = remember { mutableStateOf(false) }

    val name = rememberSaveable { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    val SnackbarHostState = remember { SnackbarHostState() }

    Surface(modifier) {




        Column(
            modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            TextField(name.value, onValueChange = { name.value = it }, label = { Text("Name") })

            Spacer(modifier = Modifier.size(16.dp))

            Button(onClick = {
                if (name.value.isNotEmpty()) {
                    isLoggedIn.value = true
                    onLoggedIn(User(name = name.value))
                } else {
                    coroutineScope.launch {
                        SnackbarHostState.showSnackbar("enter valid data")
                    }
                }
            }) {
                Text(
                    text = "Logged In",
                    style = TextStyle.Default,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

        }

        SnackbarHost(hostState = SnackbarHostState, modifier = Modifier.fillMaxWidth())


    }

}


@Composable
@Preview
fun PreviewLoginScreen() {
    LoginScreen(modifier = Modifier.fillMaxSize(), onLoggedIn = {

    })

}