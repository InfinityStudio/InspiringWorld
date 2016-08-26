package net.infstudio.inspiringworld.tech.logic.energynetwork;

import java.util.List;

/**
 * @author Blealtan
 */
public interface INetworkGraphVertexIn {
    /**
     @return Get edges ends at this vertex.
     */
    List<NetworkGraphEdge> getEdgesIn();
}
