package com.example.tictactoe.ui.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.example.tictactoe.R
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tictactoe.ui.navigation.Screen


@Composable
fun AboutBox(navController: NavController)
{
    Card(
        modifier = Modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )

    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ){
            Text(
                text = stringResource(R.string.about),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.about_text),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.popBackStack() }
            ) {
                Text(
                    text = stringResource(R.string.ok),
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        }
    }

@Composable
fun AboutScreen(navController: NavController){
    AboutBox(navController)
}
@Preview(showBackground = true)
@Composable
fun AboutBoxPreview()
{
    val navController = rememberNavController()
    AboutBox(navController)
}

