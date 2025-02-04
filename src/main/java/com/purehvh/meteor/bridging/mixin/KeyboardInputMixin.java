package com.purehvh.meteor.bridging.mixin;

import com.purehvh.meteor.bridging.modules.SneakBridge;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.util.PlayerInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input {
    @Inject(method = "tick", at = @At("TAIL"))
    private void isPressed(CallbackInfo ci) {
        if (Modules.get().get(SneakBridge.class).doVanilla()) playerInput = new PlayerInput(
            playerInput.forward(),
            playerInput.backward(),
            playerInput.left(),
            playerInput.right(),
            playerInput.jump(),
            true,
            playerInput.sprint()
        );
    }
}
