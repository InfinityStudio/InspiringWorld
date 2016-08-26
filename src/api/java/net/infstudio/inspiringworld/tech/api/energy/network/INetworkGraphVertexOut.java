package net.infstudio.inspiringworld.tech.logic.energynetwork;

import java.util.List;

/**
 * @author Blealtan
 */
public interface INetworkGraphVertexOut {
    /**
     @return Get edges starts at this vertex.
     */
    List<NetworkGraphEdge> getEdgesOut();
}
