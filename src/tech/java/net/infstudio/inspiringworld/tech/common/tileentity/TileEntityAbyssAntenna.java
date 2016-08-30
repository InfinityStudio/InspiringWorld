package net.infstudio.inspiringworld.tech.common.tileentity;

import java.util.Set;

import com.google.common.collect.Sets;

import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphAbyss;
import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphEdge;
import net.infstudio.inspiringworld.tech.common.capability.IWTechCapablilties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityAbyssAntenna extends TileEntity implements ITickable {

    protected NetworkAbyss graphAbyss = new NetworkAbyss();

    protected Set<INetworkGraphEdge> edgesIn = Sets.newLinkedHashSet();

    protected int in = 0;

    public class NetworkAbyss implements INetworkGraphAbyss {

        private INetworkGraphEdge pathPrevious = null;

        @Override
        public Set<INetworkGraphEdge> getEdgesIn() {
            return TileEntityAbyssAntenna.this.edgesIn;
        }

        @Override
        public int getConsumeRatio() {
            return 2;
        }

        @Override
        public INetworkGraphEdge getPathPrevious() {
            return this.pathPrevious;
        }

        @Override
        public void setPathPrevious(INetworkGraphEdge v) {
            this.pathPrevious = v;
        }

        @Override
        public void appendConsume(int extra) {
            TileEntityAbyssAntenna.this.in += extra;
        }

        @Override
        public BlockPos getPosition() {
            return TileEntityAbyssAntenna.this.pos;
        }

        @Override
        public int getConsume() {
            return TileEntityAbyssAntenna.this.in;
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (IWTechCapablilties.NETWORK_GRAPH_VERTEX.equals(capability)) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (IWTechCapablilties.NETWORK_GRAPH_VERTEX.equals(capability)) {
            return IWTechCapablilties.NETWORK_GRAPH_VERTEX.cast(this.graphAbyss);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
    }
}
