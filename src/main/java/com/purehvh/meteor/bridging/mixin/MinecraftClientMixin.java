package com.purehvh.meteor.bridging.mixin;

import com.purehvh.meteor.bridging.modules.GodBridge;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "handleInputEvents", at = @At("HEAD"))
    private void onRightClick(CallbackInfo ci) {
        if (Modules.get().get(GodBridge.class).getDoRightClick())
            KeyBinding.onKeyPressed(InputUtil.Type.MOUSE.createFromCode(1)); // Simulate right click
    }
}
