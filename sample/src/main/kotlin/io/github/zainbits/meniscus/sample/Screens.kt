package io.github.zainbits.meniscus.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// All content here is made up for the demo.

@Composable
private fun ScreenTitle(title: String, subtitle: String) {
    Column(Modifier.padding(bottom = 8.dp)) {
        Text(title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
        Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun Tile(modifier: Modifier = Modifier, seed: Int) {
    val primary = MaterialTheme.colorScheme.primary
    val shift = (seed % 5) * 0.12f
    Box(
        modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    listOf(primary.copy(alpha = 0.85f - shift), MaterialTheme.colorScheme.surfaceContainerHighest),
                ),
            ),
    )
}

@Composable
private fun SurfaceCard(content: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
        modifier = Modifier.fillMaxWidth(),
    ) { Column(Modifier.padding(16.dp)) { content() } }
}

private val stories = listOf(
    "Designing motion that feels physical" to "Springs, not curves: why fixed-step physics looks the same at 60 and 120 Hz.",
    "A drop is two shapes, not one" to "Shape morphing turns one outline into another. Pinching off needs something else.",
    "Signed distance fields in AGSL" to "One shader pass draws every pill, every neck and a crisp 1px outline.",
    "Edges that follow your screen" to "The bar's outer corners share a centre with the phone's own rounded corners.",
    "Accessible by default" to "Tabs announce themselves to TalkBack and the layout mirrors for RTL languages.",
    "Interrupt it anywhere" to "Tap another tab mid-animation and the liquid continues from where it is.",
)

@Composable
fun HomeScreen(padding: PaddingValues) {
    LazyColumn(contentPadding = padding, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenTitle("Good evening", "Scroll: the content flows behind the floating bar") }
        item { Tile(Modifier.fillMaxWidth().height(180.dp), seed = 0) }
        items(stories) { (title, body) ->
            SurfaceCard {
                Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(4.dp))
                Text(body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun ExploreScreen(padding: PaddingValues) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = padding,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(span = { GridItemSpan(2) }) { ScreenTitle("Explore", "Collections picked for you") }
        items((0 until 14).toList()) { i -> Tile(Modifier.fillMaxWidth().aspectRatio(if (i % 3 == 0) 0.8f else 1f), seed = i) }
    }
}

private val threads = listOf(
    Triple("Design review", "Motion feels great on the 120 Hz device", "09:41"),
    Triple("Release train", "Build 214 is ready for testing", "09:12"),
    Triple("Ava", "Can we try the pill a bit wider?", "Yesterday"),
    Triple("Platform team", "Shader warm-up looks clean in traces", "Yesterday"),
    Triple("Leo", "RTL check passed on the Arabic build", "Mon"),
    Triple("Accessibility", "TalkBack reads tabs as tabs now", "Mon"),
    Triple("Mia", "Dark mode colours look right", "Sun"),
    Triple("QA", "No dropped frames in the scroll test", "Sat"),
    Triple("Noah", "Sharing the screen recording now", "Fri"),
)

@Composable
fun InboxScreen(padding: PaddingValues) {
    LazyColumn(contentPadding = padding) {
        item { ScreenTitle("Inbox", "${threads.size} conversations") }
        items(threads) { (from, text, time) ->
            Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(44.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center,
                ) { Text(from.take(1), color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold) }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(from, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                    Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
                }
                Text(time, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
        }
    }
}

@Composable
fun SavedScreen(padding: PaddingValues) {
    LazyColumn(contentPadding = padding, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenTitle("Saved", "Things you kept for later") }
        items((0 until 8).toList()) { i ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Tile(Modifier.size(72.dp), seed = i + 2)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(stories[i % stories.size].first, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                    Text("${4 + i} min read", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(padding: PaddingValues) {
    LazyColumn(contentPadding = padding, verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        item {
            Box(
                Modifier.size(96.dp).clip(CircleShape).background(
                    Brush.linearGradient(listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.surfaceContainerHighest)),
                ),
            )
        }
        item { Text("Sam Rivera", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                listOf("128" to "Posts", "4.2k" to "Followers", "310" to "Following").forEach { (n, l) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(n, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onBackground)
                        Text(l, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
        items((0 until 6).toList()) { i -> Tile(Modifier.fillMaxWidth().height(120.dp), seed = i + 1) }
    }
}

@Composable
fun ControlsScreen(padding: PaddingValues, state: ShowcaseState, onChange: (ShowcaseState) -> Unit) {
    LazyColumn(contentPadding = padding, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { ScreenTitle("Meniscus", "Change the bar live") }
        item {
            SurfaceCard {
                Text("Tabs", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (n in 2..6) FilterChip(selected = state.tabCount == n, onClick = { onChange(state.copy(tabCount = n)) }, label = { Text("$n") })
                }
            }
        }
        item {
            SurfaceCard {
                Text("Colors", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Palette.entries.take(3).forEach { p ->
                        FilterChip(selected = state.palette == p, onClick = { onChange(state.copy(palette = p)) }, label = { Text(p.label) })
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Palette.entries.drop(3).forEach { p ->
                        FilterChip(selected = state.palette == p, onClick = { onChange(state.copy(palette = p)) }, label = { Text(p.label) })
                    }
                }
            }
        }
        item {
            SurfaceCard {
                ToggleRow("Dark theme", state.dark) { onChange(state.copy(dark = it)) }
                ToggleRow("Right-to-left layout", state.rtl) { onChange(state.copy(rtl = it)) }
            }
        }
        item {
            SurfaceCard {
                ToggleRow("Liquid glass", state.glass) { onChange(state.copy(glass = it)) }
                Text("Clear  ←  →  Tinted", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                androidx.compose.material3.Slider(value = state.clarity, onValueChange = { onChange(state.copy(clarity = it)) }, enabled = state.glass)
            }
        }
        item {
            SurfaceCard {
                Text("io.github.zainbits:meniscus", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                Text("github.com/zainbits/meniscus", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
private fun ToggleRow(label: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onChange)
    }
}
