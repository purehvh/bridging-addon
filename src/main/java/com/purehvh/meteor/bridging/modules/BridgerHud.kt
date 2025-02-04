package com.purehvh.meteor.bridging.modules

import com.purehvh.meteor.bridging.BridgingAddon
import meteordevelopment.meteorclient.systems.hud.HudElement
import meteordevelopment.meteorclient.systems.hud.HudElementInfo
import meteordevelopment.meteorclient.systems.hud.HudRenderer
import meteordevelopment.meteorclient.utils.render.color.Color

class BridgerHud : HudElement(INFO) {
    companion object {
        val INFO = HudElementInfo(
            BridgingAddon.HUD_GROUP,
            "bridger",
            "Certified bridger badge",
            ::BridgerHud
        )
    }

    override fun render(renderer: HudRenderer) {
        val text = "Certified Pro Bridger"
        setSize(renderer.textWidth(text, true), renderer.textHeight(true))
        renderer.quad(x.toDouble(), y.toDouble(), width.toDouble(), height.toDouble(), Color.BLACK)
        renderer.text(text, x.toDouble(), y.toDouble(), Color.YELLOW, true)
    }
}
