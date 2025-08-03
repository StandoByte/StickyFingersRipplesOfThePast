package com.hk47bot.rotp_stfn.client.ui;

import com.github.standobyte.jojo.client.ClientUtil;
import com.hk47bot.rotp_stfn.RotpStickyFingersAddon;
import com.hk47bot.rotp_stfn.container.BlockZipperContainer;
import com.hk47bot.rotp_stfn.init.InitSounds;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;

public class ZipperInventoryMenu extends ContainerScreen<BlockZipperContainer> implements IHasContainer<BlockZipperContainer> {
    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(RotpStickyFingersAddon.MOD_ID, "textures/gui/zipper_54.png");
    public ZipperInventoryMenu(BlockZipperContainer container, PlayerInventory inventory, ITextComponent name) {
        super(container, inventory, name);
    }
    public void render(MatrixStack stack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        this.renderBackground(stack);
        super.render(stack, p_230430_2_, p_230430_3_, p_230430_4_);
        this.renderTooltip(stack, p_230430_2_, p_230430_3_);
    }
    @Override
    protected void renderBg(MatrixStack stack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(CONTAINER_BACKGROUND);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(stack, i, j, 0, 0, this.imageWidth, 53);
        this.blit(stack, i, j + 53, 0, 126, this.imageWidth, 96);
    }
    protected void renderLabels(MatrixStack p_230451_1_, int p_230451_2_, int p_230451_3_) {
        this.font.draw(p_230451_1_, this.getTitle(), (float)this.titleLabelX, (float)this.titleLabelY-1, 4210752);
    }

    public void onClose() {
        super.onClose();
         if (ClientUtil.canHearStands()){
             PlayerEntity user = ClientUtil.getClientPlayer();
             ClientUtil.getClientWorld().playLocalSound(user.getX(), user.getY(), user.getZ(), InitSounds.ZIPPER_CLOSE.get(),
                     SoundCategory.BLOCKS, 1.0F, 1.0F, false);
        }
    }
}
