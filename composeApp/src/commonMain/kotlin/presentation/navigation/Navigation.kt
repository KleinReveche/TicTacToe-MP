package presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import presentation.localvscomputer.ScreenLocalVsComputer
import presentation.localvsplayer.ScreenLocalVsPlayer

@OptIn(KoinExperimentalAPI::class)
@Composable
fun Navigation() {
  KoinContext {
    val navController = rememberNavController()
    val vm = koinViewModel<NavigationViewModel>()
    vm.saveDeviceIdentifier()

    NavHost(navController = navController, startDestination = ScreenMain) {
      composable<ScreenMain> { ScreenMain(navController) }
      composable<ScreenLocalVsComputer> {
        val data = it.toRoute<ScreenLocalVsComputer>()
        vm.addVsComputerPlayer(data)
        ScreenLocalVsComputer(data, navController)
      }
      composable<ScreenLocalVsPlayer> {
        val data = it.toRoute<ScreenLocalVsPlayer>()
        vm.addVsPlayerPlayers(data)
        ScreenLocalVsPlayer(data, navController)
      }
      composable<ScreenMultiplayer> {}
    }
  }
}
