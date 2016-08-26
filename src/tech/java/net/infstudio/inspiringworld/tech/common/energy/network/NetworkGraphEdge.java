package net.infstudio.inspiringworld.tech.common.energy.network;

/**
 * @author Blealtan
 */
public class NetworkGraphEdge {
    /**
     The vertex that this edge starts at.
     */
    public final INetworkGraphVertexOut start;

    /**
     The vertex that this edge ends at.
     */
    public final INetworkGraphVertexIn end;

    /**
     The capacity of this edge.
     */
    protected int capacity;

    /**
     Current flow of this edge.
     */
    int current;

    public NetworkGraphEdge(INetworkGraphVertexOut start, INetworkGraphVertexIn end, int capacity) {
        this.start = start;
        this.end = end;
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrent() {
        return current;
    }
}
