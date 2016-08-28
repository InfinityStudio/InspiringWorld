package net.infstudio.inspiringworld.tech.api.energy.network;

import java.util.List;

import net.infstudio.inspiringworld.tech.common.energy.network.NetworkGraphEdge;

/**
 * @author Blealtan
 */
public interface INetworkGraphVertexIn {
    /**
     @return Get edges ends at this vertex.
     */
    List<NetworkGraphEdge> getEdgesIn();
}
