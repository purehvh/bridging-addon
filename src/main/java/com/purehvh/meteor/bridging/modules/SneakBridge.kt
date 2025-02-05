package com.purehvh.meteor.bridging.modules

import com.purehvh.meteor.bridging.BridgingAddon
import com.purehvh.meteor.bridging.enums.SneakBridgeMethod
import meteordevelopment.meteorclient.events.world.TickEvent
import meteordevelopment.meteorclient.settings.BoolSetting
import meteordevelopment.meteorclient.settings.EnumSetting
import meteordevelopment.meteorclient.settings.Setting
import meteordevelopment.meteorclient.systems.modules.Module
import meteordevelopment.orbit.EventHandler
import net.minecraft.util.math.BlockPos

class SneakBridge : Module(BridgingAddon.CATEGORY, "sneak-bridge", "Auto Sneak Bridge!") {
    private val sgGeneral = settings.defaultGroup
    private val method: Setting<SneakBridgeMethod> = sgGeneral.add(
        EnumSetting.Builder<SneakBridgeMethod>()
            .name("method")
            .defaultValue(SneakBridgeMethod.Vanilla)
            .build()
    )
    private val strict: Setting<Boolean> = sgGeneral.add(
        BoolSetting.Builder()
            .name("strict")
            .defaultValue(false)
            .description("If set, sneaking cannot be overwritten by jumping, flying, etc.")
            .build()
    )
    private var shouldSneak = false

    fun doVanilla(): Boolean {
        return shouldSneak && method.get() == SneakBridgeMethod.Vanilla
    }

    @EventHandler
    fun onTick(event: TickEvent.Pre) {
        if (mc.world == null || mc.player == null) {
            return
        }
        val pos = mc.player!!.pos.add(0.0, -0.5, 0.0)
        val bPos = BlockPos.ofFloored(pos)
        val rules = (
            !mc.player!!.abilities.flying
                && mc.player!!.velocity.y < 0.01
                && mc.player!!.isOnGround
                && !mc.options.jumpKey.isPressed
            ) || strict.get()

        shouldSneak =
            mc.world!!.getBlockState(bPos).isReplaceable && rules

    }

    override fun onDeactivate() {
        mc.options.sneakKey.isPressed = false
        shouldSneak = false
    }
}
