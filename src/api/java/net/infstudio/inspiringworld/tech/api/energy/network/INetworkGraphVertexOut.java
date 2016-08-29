package net.infstudio.inspiringworld.tech.api.energy.network;

import java.util.Set;

/**
 * @author Blealtan
 */
public interface INetworkGraphVertexOut extends INetworkGraphVertexBase {
    /**
     @return Get edges starts at this vertex.
     */
    Set<INetworkGraphEdge> getEdgesOut();
}
