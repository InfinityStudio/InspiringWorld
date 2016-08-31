package net.infstudio.inspiringworld.tech.common.tileentity;

import java.util.Set;

import com.google.common.collect.Sets;

import net.infstudio.inspiringworld.tech.api.energy.IIWTechSourceEnergyProducer;
import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphEdge;
import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphSource;
import net.infstudio.inspiringworld.tech.common.block.IWTechBlocks;
import net.infstudio.inspiringworld.tech.common.capability.IWTechCapablilties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntitySourceAntenna extends TileEntity implements ITickable {

    protected NetworkSource graphSource = new NetworkSource();

    protected Set<INetworkGraphEdge> edgesOut = Sets.newLinkedHashSet();

    protected int out = 0;

    protected IIWTechSourceEnergyProducer producer;

    public class NetworkSource implements INetworkGraphSource {

        private INetworkGraphEdge pathPrevious = null;
        private boolean toUpdate = true;

        @Override
        public Set<INetworkGraphEdge> getEdgesOut() {
            return TileEntitySourceAntenna.this.edgesOut;
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
        public boolean toUpdate() {
            return this.toUpdate;
        }

        @Override
        public void setToUpdate(boolean b) {
            this.toUpdate = b;
        }

        @Override
        public void appendConsume(int extra) {
            TileEntitySourceAntenna.this.out += extra;
        }

        @Override
        public BlockPos getPosition() {
            return TileEntitySourceAntenna.this.pos;
        }
        @Override
        public int getConsume() {
            return TileEntitySourceAntenna.this.out;
        }
    }

    protected int calculateEnergy() {
        // TODO
        return 20;
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
            return IWTechCapablilties.NETWORK_GRAPH_VERTEX.cast(this.graphSource);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        if (this.worldObj.isRemote) {
            return;
        }
        // get producer
        EnumFacing facing = this.worldObj.getBlockState(this.pos).getValue(IWTechBlocks.FACING);
        TileEntity tileEntity = this.worldObj.getTileEntity(this.pos.offset(facing.getOpposite()));
        if (tileEntity == null || tileEntity.hasCapability(IWTechCapablilties.SOURCE_ENERGY_PRODUCER, facing)) {
            this.worldObj.destroyBlock(this.pos, true);
            return;
        }
        this.producer = tileEntity.getCapability(IWTechCapablilties.SOURCE_ENERGY_PRODUCER, facing);

        // receive energy
        int energy = this.calculateEnergy();
        energy -= this.producer.extractSourceEnergy(energy, false);
    }
}
