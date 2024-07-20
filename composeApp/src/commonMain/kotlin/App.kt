import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import domain.cases.GetAppSetting
import domain.model.AppSetting
import domain.model.AppSettings
import org.koin.compose.koinInject
import presentation.navigation.Navigation
import presentation.theme.TicTacToeTheme
import presentation.theme.UIColorTypes

@Composable
fun TicTacToeApp() {
  val getAppSetting = koinInject<GetAppSetting>()
  val uiColorTypeFromDb =
    getAppSetting(AppSettings.UI_COLOR_TYPE)
      .collectAsState(AppSetting(AppSettings.UI_COLOR_TYPE, UIColorTypes.Default.name))
      .value
  val uiColorType =
    UIColorTypes.entries.find { it.name == uiColorTypeFromDb?.value } ?: UIColorTypes.Default
  val dynamicColorAndroid =
    (getAppSetting(AppSettings.DYNAMIC_COLOR_ANDROID)
      .collectAsState(AppSetting(AppSettings.DYNAMIC_COLOR_ANDROID, "true"))
      .value
      ?.value ?: "true").toBoolean()
  val oledMode =
    (getAppSetting(AppSettings.OLED)
      .collectAsState(AppSetting(AppSettings.OLED, "false"))
      .value
      ?.value ?: "false").toBoolean()
  val darkMode =
    (getAppSetting(AppSettings.DARK_MODE)
      .collectAsState(AppSetting(AppSettings.DARK_MODE, isSystemInDarkTheme().toString()))
      .value
      ?.value ?: "true").toBoolean()

  TicTacToeTheme(
    dynamicColorAndroid = dynamicColorAndroid && getPlatform().name.contains("Android"),
    uiColorTypes = uiColorType,
    darkTheme = darkMode,
    oled = oledMode,
  ) {
    Navigation()
  }
}
