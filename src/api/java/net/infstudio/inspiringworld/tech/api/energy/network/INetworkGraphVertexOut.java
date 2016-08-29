package net.infstudio.inspiringworld.tech.api.energy.network;

import java.util.List;

/**
 * @author Blealtan
 */
public interface INetworkGraphVertexOut extends INetworkGraphVertexBase {
    /**
     @return Get edges starts at this vertex.
     */
    List<INetworkGraphEdge> getEdgesOut();
}
