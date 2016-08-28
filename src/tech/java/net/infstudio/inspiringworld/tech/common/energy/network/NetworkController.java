package net.infstudio.inspiringworld.tech.common.energy.network;

import java.util.List;

import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphSink;
import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphSource;

/**
 * @author Blealtan
 */
public class NetworkController {
    private List<INetworkGraphSource> sources;
    private List<INetworkGraphSink> sinks;

    public List<INetworkGraphSource> getSources() {
        return this.sources;
    }

    public List<INetworkGraphSink> getSinks() {
        return this.sinks;
    }

    public void update() {
        // TODO
    }
}
