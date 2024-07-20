package presentation.navigation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import domain.cases.GetAppSetting
import domain.cases.UpsertAppSetting
import domain.model.AppSetting
import domain.model.AppSettings
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import presentation.common.components.DarkModeSwitch
import tictactoe.composeapp.generated.resources.Res
import tictactoe.composeapp.generated.resources.settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopAppBar(text: String, onClick: () -> Unit) {
  val coroutineScope = rememberCoroutineScope()
  var rotationAngle by remember { mutableFloatStateOf(0f) }
  val getAppSetting = koinInject<GetAppSetting>()
  val isDarkMode =
    getAppSetting(AppSettings.DARK_MODE)
      .collectAsState(AppSetting(AppSettings.DARK_MODE, "true"))
      .value
      ?.value ?: isSystemInDarkTheme().toString()
  val upsertAppSetting = koinInject<UpsertAppSetting>()
  val animatedRotationAngle: Float by
    animateFloatAsState(
      targetValue = rotationAngle,
      animationSpec = tween(durationMillis = 300),
      label = "Settings cog rotation",
    )
  CenterAlignedTopAppBar(
    title = { Text(text, color = MaterialTheme.colorScheme.primary) },
    modifier = Modifier.background(MaterialTheme.colorScheme.surfaceTint),
    navigationIcon = {
      DarkModeSwitch(
        modifier = Modifier.padding(5.dp),
        checked = isDarkMode.toBoolean(),
        onCheckedChanged = {
          coroutineScope.launch {
            upsertAppSetting(AppSetting(AppSettings.DARK_MODE, it.toString()))
          }
        },
        switchWidth = 80.dp,
        switchHeight = 32.dp,
        handleSize = 26.dp,
        handlePadding = 5.dp,
      )
    },
    actions = {
      IconButton(
        onClick = {
          rotationAngle += 45f
          onClick()
        },
        modifier = Modifier.rotate(animatedRotationAngle).padding(8.dp),
      ) {
        Icon(
          imageVector = Icons.Rounded.Settings,
          contentDescription = stringResource(Res.string.settings),
          modifier = Modifier,
          tint = MaterialTheme.colorScheme.primary,
        )
      }
    },
  )
}
