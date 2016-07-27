package net.infstudio.inspiringworld.tech.common.inventory;

import net.infstudio.inspiringworld.tech.InspiringTech;
import net.infstudio.inspiringworld.tech.client.gui.GuiSourceLight;
import net.infstudio.inspiringworld.tech.common.tileentity.TileEntitySourceLight;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.items.CapabilityItemHandler;

public class IWTechGuiElements implements IGuiHandler {
    public static void init() {
        NetworkRegistry.INSTANCE.registerGuiHandler(InspiringTech.instance, new IWTechGuiElements());
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        // Please use hard-coded id
        switch (id) {
        case 0:
            return new ContainerSourceLight(player,
                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP),
                tileEntity instanceof TileEntitySourceLight ? (TileEntitySourceLight) tileEntity : null);
        default:
            return null;
        }
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        // Please use hard-coded id
        switch (id) {
        case 0:
            return new GuiSourceLight(player,
                tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP),
                tileEntity instanceof TileEntitySourceLight ? (TileEntitySourceLight) tileEntity : null);
        default:
            return null;
        }
    }

}
