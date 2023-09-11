@file:Suppress("FunctionName")

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlin.random.Random

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        MontyHallSimulatorApp()
    }
}

class MontyHallSimulator {
    private var numberOfSimulations = 0
    private var stayWins = 0
    private var switchWins = 0

    fun runSimulations(numberOfSimulations: Int, doorCount: Int) {
        this.numberOfSimulations = numberOfSimulations
        stayWins = 0
        switchWins = 0

        repeat(numberOfSimulations) {
            val prizeDoor = Random.nextInt(doorCount)
            val chosenDoor = Random.nextInt(doorCount)

            val hostDoor = (0 until doorCount).first { it != chosenDoor && it != prizeDoor }
            val switchDoor = (0 until doorCount).first { it != chosenDoor && it != hostDoor }

            when {
                chosenDoor == prizeDoor -> stayWins++
                switchDoor == prizeDoor -> switchWins++
            }
        }
    }

    fun getStayWinPercentage(): Double = stayWins.toDouble() / numberOfSimulations * 100

    fun getSwitchWinPercentage(): Double = switchWins.toDouble() / numberOfSimulations * 100
}

@Composable
fun MontyHallSimulatorApp() {
    val simulator = remember { MontyHallSimulator() }
    var numberOfSimulations by remember { mutableStateOf(1000) }
    var numberOfDoors by remember { mutableStateOf(3) }
    var stayWinPercentage by remember { mutableStateOf(0.0) }
    var switchWinPercentage by remember { mutableStateOf(0.0) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Парадокс Монти Холла", style = MaterialTheme.typography.h4)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = numberOfSimulations.toString(),
                onValueChange = { numberOfSimulations = it.toIntOrNull() ?: 0 },
                label = { Text("Количество симуляций:") }
            )

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = numberOfDoors.toString(),
                onValueChange = { numberOfDoors = it.toIntOrNull() ?: 0 },
                label = { Text("Количество дверей:") }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            simulator.runSimulations(numberOfSimulations, numberOfDoors)
            stayWinPercentage = simulator.getStayWinPercentage()
            switchWinPercentage = simulator.getSwitchWinPercentage()
        }) {
            Text("Запустить симуляции")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Результаты:")

        Spacer(modifier = Modifier.height(8.dp))

        Text("Выигрыши при оставлении двери: $stayWinPercentage%", style = MaterialTheme.typography.body1)
        Text("Выигрыши при смене двери: $switchWinPercentage%", style = MaterialTheme.typography.body1)
    }
}