package com.purehvh.meteor.bridging

import com.mojang.logging.LogUtils
import com.purehvh.meteor.bridging.modules.SneakBridge
import meteordevelopment.meteorclient.addons.GithubRepo
import meteordevelopment.meteorclient.addons.MeteorAddon
import meteordevelopment.meteorclient.systems.hud.HudGroup
import meteordevelopment.meteorclient.systems.modules.Category
import meteordevelopment.meteorclient.systems.modules.Modules
import org.slf4j.Logger

class BridgingAddon : MeteorAddon() {
    override fun onInitialize() {
        LOG.info("Initializing Meteor Addon Template")

        // Modules
        Modules.get().add(SneakBridge())
    }

    override fun onRegisterCategories() {
        Modules.registerCategory(CATEGORY)
    }

    override fun getPackage(): String {
        return "com.purehvh.meteor.bridging"
    }

    override fun getRepo(): GithubRepo {
        return GithubRepo("purehvh", "bridging-addon")
    }

    companion object {
        @JvmField
        val LOG: Logger = LogUtils.getLogger()
        @JvmField
        val CATEGORY: Category = Category("bridging")
        val HUD_GROUP: HudGroup = HudGroup("bridging")
    }
}
