package net.infstudio.inspiringworld.tech.api.energy.network;

import java.util.List;

import net.infstudio.inspiringworld.tech.common.energy.network.NetworkGraphEdge;

/**
 * @author Blealtan
 */
public interface INetworkGraphVertexOut {
    /**
     @return Get edges starts at this vertex.
     */
    List<NetworkGraphEdge> getEdgesOut();
}
