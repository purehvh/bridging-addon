package com.purehvh.meteor.bridging.modules

import com.purehvh.meteor.bridging.BridgingAddon
import meteordevelopment.meteorclient.events.world.TickEvent
import meteordevelopment.meteorclient.systems.modules.Module
import meteordevelopment.orbit.EventHandler
import net.minecraft.item.BlockItem
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.MathHelper
import kotlin.math.abs

/*
WIP
 */
class GodBridge : Module(BridgingAddon.CATEGORY, "god-bridge", "Auto com.purehvh.meteor.bridging.modules.GodBridge!") {
    private var canResetShift = false
    private val bridgeAngle = 82.0f // The pitch angle to target
    private var bridgeDirection: BridgeDirection? = null
    private var prevTime = 0L
    var doRightClick = false
    var doShift = false

    @EventHandler
    fun onTick(event: TickEvent.Pre) {
        if (mc.player == null || mc.world == null) {
            return
        }

        this.canResetShift = true

        // Determine the cardinal direction to face
        bridgeDirection = getPlayerDirection()

        // Adjust yaw & pitch gradually
        setToBridgePosition()

        // Attempt to right-click if pitch is around target
        val pitch: Float = mc.player!!.pitch
        val lower = bridgeAngle - 7.0f // 75
        val upper = bridgeAngle + 7.0f // 89
        val currentTime = System.nanoTime() / 1000000L // ms

        val mainHandStack = mc.player!!.inventory.mainHandStack
        if (mainHandStack.item !is BlockItem) {
            mc.player!!.sendMessage(Text.literal("You must hold a block in your hand"), true)
            doShift = true
            return
        }

        if (pitch in lower..upper && (currentTime - prevTime) >= 31) {
            doRightClick = true
            prevTime = currentTime
        } else {
            doRightClick = false
        }

        // If air below, stop horizontal movement
        val posBelow: BlockPos = mc.player!!.blockPos.down()
        if (mc.world!!.getBlockState(posBelow).isAir) {
            mc.player!!.setVelocity(0.0, mc.player!!.velocity.y, 0.0)
        }
    }

    override fun onDeactivate() {
        this.doRightClick = false
        this.doShift = false
    }


    private fun setToBridgePosition() {
        var yaw: Float = mc.player!!.yaw
        // Wrap angle to [-180, 180]
        yaw = MathHelper.wrapDegrees(yaw)

        // Gradually rotate yaw to the cardinal angle
        if (bridgeDirection != null) {
            val targetYaw = bridgeDirection!!.cardinalAngle

            // Decide which direction to move yaw
            if (bridgeDirection!!.side == BridgeDirection.Side.NEGATIVE && yaw < targetYaw) {
                val diff = (abs((yaw - targetYaw).toDouble()) / 30.0f).toFloat()
                mc.player!!.yaw = yaw + diff
            } else if (bridgeDirection!!.side == BridgeDirection.Side.POSITIVE && yaw > targetYaw) {
                val diff = (abs((yaw - targetYaw).toDouble()) / 30.0f).toFloat()
                mc.player!!.yaw = yaw - diff
            }
        }

        // Gradually move pitch to ~82
        val pitch: Float = mc.player!!.pitch
        if (pitch < this.bridgeAngle) {
            val diff = (abs((pitch - this.bridgeAngle).toDouble()) / 25.0f).toFloat()
            mc.player!!.pitch = pitch + diff
        } else if (pitch > this.bridgeAngle) {
            val diff = (abs((pitch - this.bridgeAngle).toDouble()) / 70.0f).toFloat()
            mc.player!!.pitch = pitch - diff
        }
    }

    private fun getPlayerDirection(): BridgeDirection {
        // "Facing" from player's orientation
        val facing: Direction = mc.player!!.horizontalFacing
        val yaw: Float = MathHelper.wrapDegrees(mc.player!!.yaw)

        val direction: BridgeDirection

        when (facing) {
            Direction.NORTH -> {
                direction = BridgeDirection.NORTH
                if (yaw > 0.0f) {
                    direction.side = BridgeDirection.Side.NEGATIVE
                } else {
                    direction.side = BridgeDirection.Side.POSITIVE
                }
            }

            Direction.SOUTH -> {
                direction = BridgeDirection.SOUTH
                if (yaw < 0.0f) {
                    direction.side = BridgeDirection.Side.NEGATIVE
                } else {
                    direction.side = BridgeDirection.Side.POSITIVE
                }
            }

            Direction.EAST -> {
                direction = BridgeDirection.EAST
                if (yaw < direction.cardinalAngle) {
                    direction.side = BridgeDirection.Side.NEGATIVE
                } else {
                    direction.side = BridgeDirection.Side.POSITIVE
                }
            }

            Direction.WEST -> {
                direction = BridgeDirection.WEST
                if (yaw < direction.cardinalAngle) {
                    direction.side = BridgeDirection.Side.NEGATIVE
                } else {
                    direction.side = BridgeDirection.Side.POSITIVE
                }
            }

            else -> {
                direction = BridgeDirection.WEST
                if (yaw < direction.cardinalAngle) {
                    direction.side = BridgeDirection.Side.NEGATIVE
                } else {
                    direction.side = BridgeDirection.Side.POSITIVE
                }
            }
        }

        return direction
    }

    /**
     * Simple enum to store cardinal angles
     * Renamed to "BridgeDirection" to avoid confusion
     * with net.minecraft.util.math.Direction
     */
    private enum class BridgeDirection(var cardinalAngle: Float) {
        NORTH(180.0f),
        SOUTH(0.0f),
        EAST(-90.0f),
        WEST(90.0f);

        var side: Side? = null // Whether the player is rotating negative or positive to get to that angle

        enum class Side {
            NEGATIVE,
            POSITIVE
        }
    }


}
