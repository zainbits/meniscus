package io.github.zainbits.meniscus.sample

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(val label: String, val icon: ImageVector) {
    Home("Home", Icons.Outlined.Home),
    Explore("Explore", Icons.Outlined.Explore),
    Inbox("Inbox", Icons.Outlined.Inbox),
    Saved("Saved", Icons.Outlined.Bookmarks),
    Profile("Profile", Icons.Outlined.Person),
    Settings("Tune", Icons.Outlined.Tune),
    ;

    companion object {
        private val content = listOf(Home, Explore, Inbox, Saved, Profile)

        /** Content tabs first, and the controls tab always last so it stays reachable. */
        fun forTabCount(count: Int): List<Destination> = content.take(count - 1) + Settings
    }
}

enum class Palette(val label: String) { Mono("Mono"), Graphite("Graphite"), Slate("Slate"), Ash("Ash"), Dynamic("Dynamic") }

data class ShowcaseState(
    val tabCount: Int = 5,
    val palette: Palette = Palette.Mono,
    val dark: Boolean = true,
    val rtl: Boolean = false,
    val glass: Boolean = true,
    val clarity: Float = 0.35f,
) {
    companion object {
        val Saver: Saver<ShowcaseState, List<Any>> = Saver(
            save = { listOf(it.tabCount, it.palette.ordinal, it.dark, it.rtl, it.glass, it.clarity) },
            restore = {
                ShowcaseState(it[0] as Int, Palette.entries[it[1] as Int], it[2] as Boolean, it[3] as Boolean, it[4] as Boolean, it[5] as Float)
            },
        )
    }
}
