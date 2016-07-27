package net.infstudio.inspiringworld.tech.client.gui;

import net.infstudio.inspiringworld.tech.InspiringTech;
import net.infstudio.inspiringworld.tech.common.inventory.ContainerSourceLight;
import net.infstudio.inspiringworld.tech.common.tileentity.TileEntitySourceLight;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public class GuiSourceLight extends GuiContainer {
    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation(InspiringTech.MODID,
        "textures/gui/container/source_light.png");

    public GuiSourceLight(EntityPlayer player, IItemHandler burningSlot, TileEntitySourceLight tileEntity) {
        super(new ContainerSourceLight(player, burningSlot, tileEntity));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(GuiSourceLight.GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }
}
