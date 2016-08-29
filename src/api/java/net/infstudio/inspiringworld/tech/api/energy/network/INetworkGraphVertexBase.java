package net.infstudio.inspiringworld.tech.api.energy.network;

import javax.annotation.Nullable;

/**
 * @author Blealtan
 */
public interface INetworkGraphVertexBase {
    /**
     @return Actual consume on ONE passing energy.
     */
    int getConsumeRatio();

    @Nullable
    INetworkGraphEdge getPathPrevious();

    void setPathPrevious(@Nullable INetworkGraphEdge v);
}
