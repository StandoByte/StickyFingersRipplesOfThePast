package com.hk47bot.rotp_stfn.capability;


import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class EntityZipperData implements INBTSerializable<CompoundNBT> {
    private final LivingEntity entity;
    Minecraft mc = Minecraft.getInstance();
    private int remainingZipperTicks = 0;
    private int remainingZipperCooldown = 0;
    @Nullable
    private Direction enterDirection;

    
    public EntityZipperData(LivingEntity entity) {
        this.entity = entity;
    }

    // Sync all the data that should be available to all players (mostly needed for rendering).
    public void syncWithAnyPlayer(ServerPlayerEntity player) {
    }
    
    // If there is data that should only be known to the player, and not to other ones, sync it here instead.
    public void syncWithEntityOnly(ServerPlayerEntity player) {
//        AddonPackets.sendToClient(new SomeDataPacket(someDataField), player);
    }

    // Save to & load from save file.
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("ZipperTicks", remainingZipperTicks);
        nbt.putInt("ZipperCooldown", remainingZipperCooldown);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.setRemainingZipperTicks(nbt.getInt("ZipperTicks"));
        this.setRemainingZipperCooldown(nbt.getInt("ZipperCooldown"));
    }
    public boolean isInZipper() {
        return this.getRemainingZipperTicks() > 0;
    }

    public int getRemainingZipperTicks() {
        return remainingZipperTicks;
    }

    public void setRemainingZipperTicks(int remainingZipperTicks) {
        this.remainingZipperTicks = remainingZipperTicks;
    }

    public void updateZipperTicks(){
        if (remainingZipperTicks > 0){
            remainingZipperTicks--;
        }
        else {
            this.setEnterDirection(null);
        }
    }

    public Direction getEnterDirection() {
        return enterDirection;
    }

    public void setEnterDirection(Direction enterDirection) {
        this.enterDirection = enterDirection;
    }

    public int getRemainingZipperCooldown() {
        return remainingZipperCooldown;
    }

    public void updateZipperCooldown(){
        if (remainingZipperCooldown > 0){
            remainingZipperCooldown--;
        }
    }

    public void setRemainingZipperCooldown(int remainingZipperCooldown) {
        this.remainingZipperCooldown = remainingZipperCooldown;
    }

    public void tick(){
        updateZipperCooldown();
        updateZipperTicks();
    }

}
