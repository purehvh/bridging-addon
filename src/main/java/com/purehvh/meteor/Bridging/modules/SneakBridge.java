package com.purehvh.meteor.Bridging.modules;

import com.purehvh.meteor.Bridging.BridgingAddon;
import meteordevelopment.meteorclient.events.entity.player.ClipAtLedgeEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;

import java.util.List;

import static com.purehvh.meteor.Bridging.BridgingAddon.LOG;

public class SneakBridge extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private boolean shallSneak = false;

    private final Setting<ListMode> listMode = sgGeneral.add(new EnumSetting.Builder<ListMode>()
        .name("block-list-mode")
        .description("Block list selection mode.")
        .defaultValue(ListMode.Whitelist)
        .build()
    );

    private final Setting<Integer> ext = sgGeneral.add(new IntSetting.Builder()
        .name("extend")
        .description("How far to place in front of you.")
        .defaultValue(1)
        .range(0, 5)
        .build()
    );

    public SneakBridge() {
        super(BridgingAddon.CATEGORY, "sneak-bridge", "Auto Sneak Bridge!");
    }


    @EventHandler
    public void onTick(TickEvent.Pre event) {
        if (mc.world == null || mc.player == null) {
            return;
        }
        Vec3d pos = mc.player.getPos().add(0, -0.5, 0);
        BlockPos bPos = BlockPos.ofFloored(pos);
        LOG.warn(bPos.toString());
        mc.player.sendMessage(Text.of(bPos.toString()), false);
        this.shallSneak = mc.world.getBlockState(bPos).isReplaceable();
        mc.options.sneakKey.setPressed(this.shallSneak);
    }

    @Override
    public void onDeactivate() {
        mc.options.sneakKey.setPressed(false);
    }
}
