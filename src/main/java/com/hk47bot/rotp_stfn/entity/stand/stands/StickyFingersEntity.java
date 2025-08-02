package com.hk47bot.rotp_stfn.entity.stand.stands;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.hk47bot.rotp_stfn.init.InitStands;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

import net.minecraft.world.World;

public class StickyFingersEntity extends StandEntity {
    private static final DataParameter<Boolean> HAS_FOREARM = EntityDataManager.defineId(StickyFingersEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HAS_SHORT_FOREARM = EntityDataManager.defineId(StickyFingersEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> TARGET_INSIDE_ID = EntityDataManager.defineId(StickyFingersEntity.class, DataSerializers.INT);
    public Entity targetInside;

    public StickyFingersEntity(StandEntityType<StickyFingersEntity> type, World world) {
        super(type, world);
    }

    public boolean isUsingExtendedPunch() {
        return this.getCurrentTaskAction() == InitStands.STICKY_FINGERS_EXTENDED_PUNCH.get();
    }

    public boolean hasForeArm() {
        return entityData.get(HAS_FOREARM);
    }

    public void setForeArm(boolean forearm) {
        entityData.set(HAS_FOREARM, forearm);
    }

    public boolean hasShortForeArm() {
        return entityData.get(HAS_SHORT_FOREARM);
    }

    public void setShortForeArm(boolean shortforearm) {
        entityData.set(HAS_SHORT_FOREARM, shortforearm);
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(HAS_FOREARM, true);
        entityData.define(HAS_SHORT_FOREARM, true);
        entityData.define(TARGET_INSIDE_ID, -1);
    }

    @Override
    public void onSyncedDataUpdated(DataParameter<?> parameter) {
        super.onSyncedDataUpdated(parameter);
        if (parameter == TARGET_INSIDE_ID){
            if (targetInside != null) {
                entityData.set (TARGET_INSIDE_ID, targetInside.getId());
            }
            else {
                entityData.set (TARGET_INSIDE_ID, -1);
            }
        }
    }

    @Override
    public void tick(){
        super.tick();
    }
}
