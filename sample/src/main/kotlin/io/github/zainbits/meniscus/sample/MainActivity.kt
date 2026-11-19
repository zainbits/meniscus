package io.github.zainbits.meniscus.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

/** Showcase for Meniscus: a small app shell with realistic screens behind the floating bar, plus live controls. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val systemDark = isSystemInDarkTheme()
            var state by rememberSaveable(stateSaver = ShowcaseState.Saver) { mutableStateOf(ShowcaseState(dark = systemDark)) }
            ShowcaseTheme(palette = state.palette, dark = state.dark) {
                CompositionLocalProvider(LocalLayoutDirection provides if (state.rtl) LayoutDirection.Rtl else LayoutDirection.Ltr) {
                    ShowcaseApp(state = state, onStateChange = { state = it })
                }
            }
        }
    }
}

@Composable
private fun ShowcaseApp(state: ShowcaseState, onStateChange: (ShowcaseState) -> Unit) {
    // Track the destination, not the index, so changing the tab count keeps you on the same screen.
    var current by rememberSaveable { mutableStateOf(Destination.Home) }
    val destinations = Destination.forTabCount(state.tabCount)
    val selected = destinations.indexOf(current).takeIf { it >= 0 } ?: destinations.lastIndex

    ShowcaseScaffold(
        destinations = destinations,
        selected = selected,
        onSelect = { current = destinations[it] },
        glass = state.glass,
        clarity = state.clarity,
        dark = state.dark,
    ) { contentPadding ->
        AnimatedContent(
            targetState = destinations[selected],
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "screen",
        ) { destination ->
            when (destination) {
                Destination.Home -> HomeScreen(contentPadding)
                Destination.Explore -> ExploreScreen(contentPadding)
                Destination.Inbox -> InboxScreen(contentPadding)
                Destination.Saved -> SavedScreen(contentPadding)
                Destination.Profile -> ProfileScreen(contentPadding)
                Destination.Settings -> ControlsScreen(contentPadding, state, onStateChange)
            }
        }
    }
}
