package com.hk47bot.rotp_stfn.capability;

import com.hk47bot.rotp_stfn.container.BlockZipperContainer;
import com.hk47bot.rotp_stfn.container.EntityZipperContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public class BlockZipperStorage extends Inventory implements INamedContainerProvider {
    private final BlockPos pos;
    private final ITextComponent name;

    public BlockZipperStorage(BlockPos pos, ITextComponent name){
        super(18);
        this.pos = pos;
        this.name = name;
    }

    public BlockPos getPos() {
        return pos;
    }

    @Override
    public ITextComponent getDisplayName() {
        return name;
    }

    public ListNBT createTag() {
        ListNBT listnbt = new ListNBT();
        for(int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack itemstack = this.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundNBT compoundnbt = new CompoundNBT();
                compoundnbt.putByte("Slot", (byte)i);
                itemstack.save(compoundnbt);
                listnbt.add(compoundnbt);
            }
        }

        return listnbt;
    }
    public void fromTag(ListNBT nbt) {
        for(int i = 0; i < this.getContainerSize(); ++i) {
            this.setItem(i, ItemStack.EMPTY);
        }

        for(int k = 0; k < nbt.size(); ++k) {
            CompoundNBT compoundnbt = nbt.getCompound(k);
            int j = compoundnbt.getByte("Slot") & 255;
            if (j >= 0 && j < this.getContainerSize()) {
                this.setItem(j, ItemStack.of(compoundnbt));
            }
        }

    }

    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new BlockZipperContainer(id, inventory, this);
    }
}
