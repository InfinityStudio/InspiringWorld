package net.infstudio.inspiringworld.tech.api.energy.network;

/**
 * @author Blealtan
 */
public interface INetworkGraphVertex extends INetworkGraphVertexIn, INetworkGraphVertexOut {
    /**
     Append extra energy flow passing this vertex. This method is called by the source of this one extensible path, to
     tell the vertex to deal with it's own work.
     @param passing The size of energy flow.
     */
    void appendPass(int passing);

    int getPass();
}
