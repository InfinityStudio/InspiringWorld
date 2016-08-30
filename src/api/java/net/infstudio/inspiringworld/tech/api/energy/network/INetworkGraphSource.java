package net.infstudio.inspiringworld.tech.api.energy.network;

/**
 * @author Blealtan
 */
public interface INetworkGraphSource extends INetworkGraphVertexOut {
    boolean toUpdate();

    void setToUpdate(boolean toUpdate);

    void appendConsume(int extra);

    int getConsume();
}
