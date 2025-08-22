package com.hk47bot.rotp_stfn.container;

import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.hk47bot.rotp_stfn.capability.BlockZipperStorage;
import com.hk47bot.rotp_stfn.init.InitContainers;
import com.hk47bot.rotp_stfn.init.InitStands;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;

public class BlockZipperContainer extends Container {
    private final BlockZipperStorage storage;
    private static final int PLAYER_INV_SLOT_COUNT = 36;
    private static final int ZIPPER_INV_SLOT_COUNT = 18;

    public BlockZipperContainer(int id, PlayerInventory inventory, PacketBuffer dataFromServer) {
        this(id, inventory, new BlockZipperStorage(new BlockPos(0, 0, 0), StringTextComponent.EMPTY));
    }

    public BlockZipperContainer(int id, PlayerInventory inventory, BlockZipperStorage itemCap) {
        super(InitContainers.STICKY_FINGERS_BLOCK_CONTAINER.get(), id);
        this.storage = itemCap;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, i * 18 + 67));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(inventory, k, 8 + k * 18, 125));
        }

        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(itemCap, j + i * 9, 8 + j * 18, (i + 1) * 18));
            }
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        IStandPower power = IStandPower.getPlayerStandPower(player);
        ServerWorld world = (ServerWorld) player.level;
        return power.getType() == InitStands.STAND_STICKY_FINGERS.getStandType() && !world.getBlockState(storage.getPos()).isAir();
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
}