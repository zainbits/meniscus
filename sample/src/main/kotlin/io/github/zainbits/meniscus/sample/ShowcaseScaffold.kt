package io.github.zainbits.meniscus.sample

import io.github.zainbits.liquid.ProvideGlass
import io.github.zainbits.liquid.glassSource
import io.github.zainbits.liquid.rememberGlassBackdrop
import io.github.zainbits.meniscus.MeniscusGlass
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.zainbits.meniscus.MeniscusNavBar
import io.github.zainbits.meniscus.MeniscusNavBarDefaults
import io.github.zainbits.meniscus.MeniscusNavItem

/** Edge-to-edge shell: content scrolls under the status bar and behind the floating Meniscus bar. */
@Composable
fun ShowcaseScaffold(
    destinations: List<Destination>,
    selected: Int,
    onSelect: (Int) -> Unit,
    glass: Boolean = false,
    clarity: Float = 0.35f,
    dark: Boolean = true,
    content: @Composable (PaddingValues) -> Unit,
) {
    val items = remember(destinations) { destinations.map { MeniscusNavItem(it.label, it.icon) } }
    val top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val contentPadding = PaddingValues(
        start = 16.dp,
        end = 16.dp,
        top = top + 16.dp,
        bottom = bottom + MeniscusNavBarDefaults.BottomGap + MeniscusNavBarDefaults.Height + 24.dp,
    )

    val backdrop = rememberGlassBackdrop()
    ProvideGlass(clarity = clarity, dark = dark) {
    Box(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        // What the glass bar shows through.
        Box(Modifier.fillMaxSize().glassSource(backdrop).background(MaterialTheme.colorScheme.background)) {
            content(contentPadding)
        }
        MeniscusNavBar(
            items = items,
            selectedIndex = selected,
            onSelect = onSelect,
            glass = if (glass) remember(backdrop) { MeniscusGlass(backdrop) } else null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal))
                .padding(
                    start = MeniscusNavBarDefaults.SideMargin,
                    end = MeniscusNavBarDefaults.SideMargin,
                    bottom = MeniscusNavBarDefaults.BottomGap,
                ),
        )
    }
    }
}
