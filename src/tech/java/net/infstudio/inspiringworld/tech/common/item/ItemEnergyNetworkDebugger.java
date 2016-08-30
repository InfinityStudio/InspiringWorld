package net.infstudio.inspiringworld.tech.common.item;

import net.infstudio.inspiringworld.tech.InspiringTech;
import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphAbyss;
import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphSource;
import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphVertex;
import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphVertexBase;
import net.infstudio.inspiringworld.tech.common.capability.IWTechCapablilties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

/**
 * @author Blealtan
 */
public class ItemEnergyNetworkDebugger extends Item {

    public ItemEnergyNetworkDebugger() {
        super();
        this.setUnlocalizedName(InspiringTech.MODID + "." + "energyNetworkDebugger");
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
                                      EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.getTileEntity(pos).hasCapability(IWTechCapablilties.NETWORK_GRAPH_VERTEX, null)) {
            INetworkGraphVertexBase vertex = worldIn.getTileEntity(pos)
                .getCapability(IWTechCapablilties.NETWORK_GRAPH_VERTEX, null);
            String msg = "Network component detected:\n";
            if (vertex instanceof INetworkGraphSource)
                msg +=
                    "Source Vertex:\n" +
                    "To be Updated? " + ((INetworkGraphSource) vertex).toUpdate() + "\n" +
                    "Current Consume:" + ((INetworkGraphSource) vertex).getConsume();
            else if (vertex instanceof INetworkGraphAbyss)
                msg +=
                    "Abyss Vertex:\n" +
                    "Current Consume:" + ((INetworkGraphAbyss) vertex).getConsume();
            else if (vertex instanceof INetworkGraphVertex)
                msg +=
                    "Normal Vertex:\n" +
                    "Current Passing:" + ((INetworkGraphVertex) vertex).getPass();
            playerIn.addChatComponentMessage(new TextComponentString(msg));
        }
        return EnumActionResult.PASS;
    }
}
