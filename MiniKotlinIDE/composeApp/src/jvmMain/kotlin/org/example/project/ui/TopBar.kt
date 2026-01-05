package org.example.project.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import minikotlinide.composeapp.generated.resources.Kotlin
import minikotlinide.composeapp.generated.resources.Res
import org.example.project.model.ScriptStatus
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    status: ScriptStatus,
    onRun: () -> Unit,
    onStop: () -> Unit,
    lastRunDuration: String?
) {
    Column {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF1E1E1E),
                titleContentColor = Color.White
            ),
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // logo and title
                    TopBarLogoAndTitle(status)

                    Spacer(modifier = Modifier.weight(1f))

                    // actions buttons
                    TopBarActionsButtons(
                        onRun = onRun,
                        onStop = onStop,
                        isRunning = status is ScriptStatus.Running
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    // execution history
                    TopBarExecutionHistory(duration = lastRunDuration)
                }
            }
        )
        HorizontalDivider(
            color = Color.White.copy(alpha = 0.15f),
            thickness = 1.dp
        )
    }
}

@Composable
private fun TopBarLogoAndTitle(status: ScriptStatus){
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(Res.drawable.Kotlin),
            contentDescription = "Kotlin icon",
            modifier = Modifier.size(48.dp),
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "Mini Kotlin IDE",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            StatusIndicator(status)
        }
    }
}

@Composable
private fun StatusIndicator(status: ScriptStatus) {
    val (color, label) = when (status) {
        is ScriptStatus.Idle -> Color.Magenta to "Idle"
        is ScriptStatus.Running -> Color.Green to "Running..."
        is ScriptStatus.Finished -> {
            val c = if (status.exitCode == 0) Color.Cyan else Color.Red
            c to "Last Exit: ${status.exitCode}"
        }
        is ScriptStatus.Error -> Color.Red to "Error"
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = color.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun TopBarActionsButtons(onRun: () -> Unit, onStop: () -> Unit, isRunning: Boolean){
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Run Button
        Button(
            onClick = onRun,
            enabled = !isRunning,
            shape = MaterialTheme.shapes.small
        ) {
            Text("Run")
        }

        Spacer(modifier = Modifier.width(8.dp))

        //stop button
        Button(
            onClick =  onStop,
            enabled = isRunning,
            shape = MaterialTheme.shapes.small,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red.copy(alpha = 0.8f),
                contentColor = Color.White,
                disabledContainerColor = Color.Gray.copy(alpha = 0.2f)
            )
        ) {
            Text("Stop")
        }
    }
}

@Composable
private fun TopBarExecutionHistory(duration: String?){
    if (duration != null) {
        Text(
            text = "Last: ${duration}ms",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
    Spacer(modifier = Modifier.width(8.dp))
}