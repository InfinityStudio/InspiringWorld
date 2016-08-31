package net.infstudio.inspiringworld.tech.common.tileentity;

import java.util.Set;

import com.google.common.collect.Sets;

import net.infstudio.inspiringworld.tech.api.energy.IIWTechEnergyConsumer;
import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphEdge;
import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphVertex;
import net.infstudio.inspiringworld.tech.common.block.IWTechBlocks;
import net.infstudio.inspiringworld.tech.common.capability.IWTechCapablilties;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

public class TileEntityAntenna extends TileEntity implements ITickable {

    protected NetworkVertex graphVertex = new NetworkVertex();

    protected Set<INetworkGraphEdge> edgesIn = Sets.newLinkedHashSet();
    protected Set<INetworkGraphEdge> edgesOut = Sets.newLinkedHashSet();

    protected int pass = 0;

    protected IIWTechEnergyConsumer consumer;

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

        @Override
        public BlockPos getPosition() {
            return TileEntityAntenna.this.pos;
        }

        @Override
        public int getPass() {
            return TileEntityAntenna.this.pass;
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
            return IWTechCapablilties.NETWORK_GRAPH_VERTEX.cast(this.graphVertex);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        if (this.worldObj.isRemote) {
            return;
        }
        // get consumer
        EnumFacing facing = this.worldObj.getBlockState(this.pos).getValue(IWTechBlocks.FACING);
        TileEntity tileEntity = this.worldObj.getTileEntity(this.pos.offset(facing.getOpposite()));
        if (tileEntity == null || tileEntity.hasCapability(IWTechCapablilties.ENERGY_CONSUMER, facing)) {
            this.worldObj.destroyBlock(this.pos, true);
            return;
        }
        this.consumer = tileEntity.getCapability(IWTechCapablilties.ENERGY_CONSUMER, facing);

        // receive energy
        int energy = this.calculateEnergy();
        energy -= this.consumer.receiveEnergy(energy, false);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        // TODO
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        // TODO
        return super.writeToNBT(compound);
    }
}
