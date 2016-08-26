package net.infstudio.inspiringworld.tech.common.energy.network;

import java.util.List;

/**
 * @author Blealtan
 */
public class NetworkController {
    private List<INetworkGraphSource> sources;
    private List<INetworkGraphSink> sinks;

    public List<INetworkGraphSource> getSources() {
        return sources;
    }

    public List<INetworkGraphSink> getSinks() {
        return sinks;
    }

    public void update() {
        // TODO
    }
}
