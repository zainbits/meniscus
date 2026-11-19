package io.github.zainbits.meniscus

import android.graphics.RuntimeShader
import androidx.compose.runtime.Immutable
import io.github.zainbits.liquid.GlassBackdrop
import io.github.zainbits.liquid.GlassMode
import io.github.zainbits.liquid.GlassScene
import io.github.zainbits.liquid.GlassStyle

/**
 * Draws [MeniscusNavBar] as liquid glass over [backdrop] instead of solid colours: the bar's own
 * outline (pills, necks and the pinching drop) bends, blurs and tints what scrolls behind it, and the
 * selected pill is tinted with the selected container colour.
 *
 * Mark the content behind the bar with `Modifier.glassSource(backdrop)` (liquid-core), and place the
 * bar outside that content, drawn after it.
 *
 * @param style the glass look; null = `LocalGlassStyle` with the accent taken from the bar's colours.
 * @param mode null = `LocalGlassMode` (Solid in battery saver or heat when using `ProvideGlass`).
 */
@Immutable
public class MeniscusGlass(
    public val backdrop: GlassBackdrop,
    public val style: GlassStyle? = null,
    public val mode: GlassMode? = null,
)

/**
 * The bar's outline for the glass engine: the same signed distance as the solid renderer (rounded slot
 * boxes plus capillary bridges), with the normal taken from the distance's gradient and the accent
 * blend as the scene accent. The version bumps only when the shape actually moved.
 */
internal class MeniscusScene : GlassScene() {
    private val slots = FloatArray(MeniscusMotion.MAX_SLOTS * 4)
    private val bridges = FloatArray((MeniscusMotion.MAX_SLOTS - 1) * 4)
    private val lastSlots = FloatArray(slots.size)
    private val lastBridges = FloatArray(bridges.size)
    private var count = 0
    private var cy = 0f
    private var radius = 0f
    private var outerBL = 0f
    private var outerBR = 0f
    private var lastKey = FloatArray(5) { Float.NaN }

    override val hasAccent: Boolean get() = true

    fun update(model: MeniscusModel, centerY: Float, outerBottomLeft: Float, outerBottomRight: Float) {
        model.fillSlots(slots)
        model.fillBridges(bridges)
        count = model.count
        cy = centerY
        radius = model.radius
        outerBL = outerBottomLeft.coerceIn(0f, radius)
        outerBR = outerBottomRight.coerceIn(0f, radius)
        val changed = !slots.contentEquals(lastSlots) || !bridges.contentEquals(lastBridges) ||
            lastKey[0] != count.toFloat() || lastKey[1] != cy || lastKey[2] != radius ||
            lastKey[3] != outerBL || lastKey[4] != outerBR
        if (changed) {
            slots.copyInto(lastSlots)
            bridges.copyInto(lastBridges)
            lastKey[0] = count.toFloat(); lastKey[1] = cy; lastKey[2] = radius; lastKey[3] = outerBL; lastKey[4] = outerBR
            version++
        }
    }

    override fun applyUniforms(shader: RuntimeShader) {
        shader.setFloatUniform("uMCount", count.toFloat())
        shader.setFloatUniform("uMCy", cy)
        shader.setFloatUniform("uMR", radius)
        shader.setFloatUniform("uMOuterBL", outerBL)
        shader.setFloatUniform("uMOuterBR", outerBR)
        shader.setFloatUniform("uMSlots", slots)
        shader.setFloatUniform("uMBridges", bridges)
    }

    override val agsl: String = """
uniform float uMCount;
uniform float uMCy;
uniform float uMR;
uniform float uMOuterBL;
uniform float uMOuterBR;
uniform float uMSlots[${SLOTS * 4}];
uniform float uMBridges[${(SLOTS - 1) * 4}];

float mSlot(float2 p, float l, float r, float rbl, float rbr) {
    float2 q = float2(p.x - 0.5 * (l + r), p.y - uMCy);
    float rad = q.y > 0.0 ? (q.x > 0.0 ? rbr : rbl) : uMR;
    float2 d = abs(q) - float2(0.5 * (r - l), uMR) + rad;
    return min(max(d.x, d.y), 0.0) + length(max(d, 0.0)) - rad;
}

float mBridge(float2 p, float xm, float hw, float k, float n) {
    float2 q = float2(p.x - xm, abs(p.y - uMCy));
    float hh = n + k;
    float2 d = float2(abs(q.x) - hw, q.y - hh);
    float box = min(max(d.x, d.y), 0.0) + length(max(d, 0.0));
    return max(box, k - length(float2(q.x, q.y - hh)));
}

float mDist(float2 p) {
    float d = 10000.0;
    for (int i = 0; i < $SLOTS; i++) {
        if (float(i) < uMCount) {
            float rbl = i == 0 ? uMOuterBL : uMR;
            float rbr = float(i) == uMCount - 1.0 ? uMOuterBR : uMR;
            d = min(d, mSlot(p, uMSlots[i * 4], uMSlots[i * 4 + 1], rbl, rbr));
        }
    }
    for (int j = 0; j < ${SLOTS - 1}; j++) {
        if (float(j) < uMCount - 1.0) {
            float n = uMBridges[j * 4 + 3];
            if (n > 0.0) {
                d = min(d, mBridge(p, uMBridges[j * 4], uMBridges[j * 4 + 1], uMBridges[j * 4 + 2], n));
            }
        }
    }
    return d;
}

float3 scene(float2 p) {
    float d = mDist(p);
    // Outward normal from the distance's gradient (two extra samples, forward differences).
    float2 g = float2(mDist(p + float2(1.0, 0.0)) - d, mDist(p + float2(0.0, 1.0)) - d);
    return float3(d, g / max(length(g), 0.0001));
}

half sceneAccent(float2 p) {
    float a = uMSlots[2];
    for (int j = 0; j < ${SLOTS - 1}; j++) {
        if (float(j) < uMCount - 1.0) {
            float xm = uMBridges[j * 4];
            float t = uMSlots[j * 4 + 3];
            a += (uMSlots[j * 4 + 6] - uMSlots[j * 4 + 2]) * smoothstep(xm - t, xm + t, p.x);
        }
    }
    return half(clamp(a, 0.0, 1.0));
}
"""

    private companion object {
        const val SLOTS = MeniscusMotion.MAX_SLOTS
    }
}
