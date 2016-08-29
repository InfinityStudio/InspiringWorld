package net.infstudio.inspiringworld.tech.common.energy.network;

import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphEdge;
import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphVertexIn;
import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphVertexOut;

/**
 * @author Blealtan
 */
public class NetworkGraphEdge implements INetworkGraphEdge {
    /**
     The vertex that this edge starts at.
     */
    private final INetworkGraphVertexOut start;
    /**
     The vertex that this edge ends at.
     */
    private final INetworkGraphVertexIn end;
    /**
     The capacity of this edge.
     */
    private int capacity;

    /**
     Current flow of this edge.
     */
    private int current;

    public NetworkGraphEdge(INetworkGraphVertexOut start, INetworkGraphVertexIn end, int capacity) {
        this.start = start;
        this.end = end;
        this.capacity = capacity;
    }

    @Override
    public INetworkGraphVertexOut getStart() {
        return start;
    }

    @Override
    public INetworkGraphVertexIn getEnd() {
        return end;
    }

    @Override
    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public int getCurrent() {
        return this.current;
    }

    @Override
    public void setCurrent(int current) {
        this.current = current;
    }
}
