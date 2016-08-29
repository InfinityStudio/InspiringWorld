package net.infstudio.inspiringworld.tech.api.energy.network;

/**
 * @author Blealtan
 */
public interface INetworkGraphEdge {
    INetworkGraphVertexOut getStart();

    INetworkGraphVertexIn getEnd();

    int getCapacity();

    void setCapacity(int capacity);

    int getCurrent();

    void setCurrent(int current);
}
