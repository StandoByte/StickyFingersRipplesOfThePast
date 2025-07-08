package com.hk47bot.rotp_stfn.capability;

import com.hk47bot.rotp_stfn.container.EntityZipperContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.UUID;

public class EntityZipperStorage extends Inventory implements INamedContainerProvider {

    private final UUID mobUUID;
    private final ITextComponent name;


    public EntityZipperStorage(ITextComponent name, UUID mobUUID){
        super(9);
        this.name = name;
        this.mobUUID = mobUUID;
    }

    @Override
    public ITextComponent getDisplayName() {
        return name;
    }
    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new EntityZipperContainer(id, inventory, this);
    }
    public UUID getMobUUID() {
        return mobUUID;
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
}
