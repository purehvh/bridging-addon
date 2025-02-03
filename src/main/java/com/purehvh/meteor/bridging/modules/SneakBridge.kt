package com.purehvh.meteor.bridging.modules

import com.purehvh.meteor.bridging.BridgingAddon
import meteordevelopment.meteorclient.events.world.TickEvent
import meteordevelopment.meteorclient.systems.modules.Module
import meteordevelopment.orbit.EventHandler
import net.minecraft.util.math.BlockPos

class SneakBridge : Module(BridgingAddon.CATEGORY, "sneak-bridge", "Auto Sneak Bridge!") {
    @EventHandler
    fun onTick(event: TickEvent.Pre?) {
        if (mc.world == null || mc.player == null) {
            return
        }
        val pos = mc.player!!.pos.add(0.0, -0.5, 0.0)
        val bPos = BlockPos.ofFloored(pos)
        mc.options.sneakKey.isPressed = mc.world!!.getBlockState(bPos).isReplaceable
    }

    override fun onDeactivate() {
        mc.options.sneakKey.isPressed = false
    }
}
