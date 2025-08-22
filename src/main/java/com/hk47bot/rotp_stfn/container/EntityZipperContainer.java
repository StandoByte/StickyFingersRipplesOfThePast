package com.hk47bot.rotp_stfn.container;

import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.hk47bot.rotp_stfn.capability.EntityZipperStorage;
import com.hk47bot.rotp_stfn.init.InitContainers;
import com.hk47bot.rotp_stfn.init.InitStands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

import java.util.UUID;

public class EntityZipperContainer extends Container {

    private final EntityZipperStorage storage;
    private static final int PLAYER_INV_SLOT_COUNT = 36;
    private static final int ZIPPER_INV_SLOT_COUNT = 9;

    public EntityZipperContainer(int id, PlayerInventory inventory, PacketBuffer dataFromServer) {
        this(id, inventory, new EntityZipperStorage(StringTextComponent.EMPTY, UUID.randomUUID()));
    }

    public EntityZipperContainer(int id, PlayerInventory inventory, EntityZipperStorage itemCap) {
        super(InitContainers.STICKY_FINGERS_ENTITY_CONTAINER.get(), id);
        this.storage = itemCap;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, i * 18 + 84));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 142));
        }

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(itemCap, j + i * 3, 62 + j * 18, i * 18 + 17));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack sourceStack = slot.getItem();
            itemstack = sourceStack.copy();

            if (index < PLAYER_INV_SLOT_COUNT) {
                if (!this.moveItemStackTo(sourceStack, PLAYER_INV_SLOT_COUNT, PLAYER_INV_SLOT_COUNT + ZIPPER_INV_SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index < PLAYER_INV_SLOT_COUNT + ZIPPER_INV_SLOT_COUNT) {
                if (!this.moveItemStackTo(sourceStack, 0, PLAYER_INV_SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                return ItemStack.EMPTY;
            }

            if (sourceStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (sourceStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, sourceStack);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        IStandPower power = IStandPower.getPlayerStandPower(player);
        if (player.level.isClientSide()) return true;
        ServerWorld world = (ServerWorld) player.level;
        return power.getType() == InitStands.STAND_STICKY_FINGERS.getStandType() && world.getEntity(storage.getMobUUID()) != null && world.getEntity(storage.getMobUUID()).isAlive();
    }
}