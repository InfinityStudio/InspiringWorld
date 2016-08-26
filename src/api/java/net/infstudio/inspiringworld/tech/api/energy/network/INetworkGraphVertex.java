package net.infstudio.inspiringworld.tech.logic.energynetwork;

/**
 * @author Blealtan
 */
public interface INetworkGraphVertex extends INetworkGraphVertexIn, INetworkGraphVertexOut {
    /**
     Set energy flow passing this vertex. This method is called by the controller of this network, to tell the vertex
     to deal with it's own work.
     @param passed The size of energy flow.
     */
    void setPass(int passed);
}
