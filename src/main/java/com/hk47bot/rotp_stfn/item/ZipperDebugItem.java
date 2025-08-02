package com.hk47bot.rotp_stfn.item;

import com.github.standobyte.jojo.util.mod.JojoModUtil;
import com.hk47bot.rotp_stfn.block.StickyFingersZipperBlock2;
import com.hk47bot.rotp_stfn.block.ZipperFace;
import com.hk47bot.rotp_stfn.tileentities.StickyFingersZipperTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class ZipperDebugItem extends Item {
    public ZipperDebugItem(Properties properties) {
        super(properties);
    }
    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack debugItem = player.getItemInHand(hand);
        if (world.isClientSide()){

            Minecraft mc = Minecraft.getInstance();
            NewChatGui chat = mc.gui.getChat();
            RayTraceResult rayTraceResult = JojoModUtil.rayTrace(player, 5, null);
            if (rayTraceResult instanceof BlockRayTraceResult){
                BlockRayTraceResult blockRayTraceResult = (BlockRayTraceResult) rayTraceResult;
                if (world.getBlockState(blockRayTraceResult.getBlockPos()).getBlock() instanceof StickyFingersZipperBlock2){
                    chat.addMessage(ITextComponent.nullToEmpty(blockRayTraceResult.getBlockPos().toString()));
                    for (ZipperFace face : ((StickyFingersZipperTileEntity)world.getBlockEntity(blockRayTraceResult.getBlockPos())).FACES){
                        chat.addMessage(ITextComponent.nullToEmpty("-----------------------------------------------------"));
                        chat.addMessage(ITextComponent.nullToEmpty(face.getDirection().toString()));
                        chat.addMessage(ITextComponent.nullToEmpty(face.neighbours.toString()));
                        chat.addMessage(ITextComponent.nullToEmpty(String.valueOf(face.getRotation())));
                    }
                    chat.addMessage(ITextComponent.nullToEmpty("-----------------------------------------------------"));
                }
            }
        }
        return ActionResult.success(debugItem);
    }
}
