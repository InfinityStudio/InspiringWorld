package net.infstudio.inspiringworld.tech.common.tileentity;

import java.util.Set;

import com.google.common.collect.Sets;

import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphEdge;
import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphVertex;
import net.infstudio.inspiringworld.tech.common.capability.IWTechCapablilties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityAntenna extends TileEntity implements ITickable {

    protected NetworkVertex graphVertex = new NetworkVertex();

    protected Set<INetworkGraphEdge> edgesIn = Sets.newLinkedHashSet();
    protected Set<INetworkGraphEdge> edgesOut = Sets.newLinkedHashSet();

    protected int pass = 0;

    public class NetworkVertex implements INetworkGraphVertex {

        private INetworkGraphEdge pathPrevious = null;

        @Override
        public Set<INetworkGraphEdge> getEdgesIn() {
            return TileEntityAntenna.this.edgesIn;
        }

        @Override
        public int getConsumeRatio() {
            return 4;
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
        public Set<INetworkGraphEdge> getEdgesOut() {
            return TileEntityAntenna.this.edgesOut;
        }

        @Override
        public void appendPass(int passing) {
            TileEntityAntenna.this.pass += passing;
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
            return IWTechCapablilties.NETWORK_GRAPH_VERTEX.cast(this.graphVertex);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        // TODO Auto-generated method stub
    }
}
