package net.infstudio.inspiringworld.tech.api.energy.network;

import java.util.Set;

/**
 * @author Blealtan
 */
public interface INetworkGraphVertexIn extends INetworkGraphVertexBase {
    /**
     @return Get edges ends at this vertex.
     */
    Set<INetworkGraphEdge> getEdgesIn();
}
