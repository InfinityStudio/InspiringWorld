package net.infstudio.inspiringworld.tech.common.energy.network;

import java.util.Queue;

import com.google.common.collect.Queues;
import net.infstudio.inspiringworld.tech.api.energy.network.*;

/**
 * @author Blealtan
 */
public class NetworkGraphManager {

    private static Queue<INetworkGraphVertexBase> bfsQueue = Queues.newArrayDeque();

    /**
     @param source The source node to update from
     */
    public static void updateFromSource(INetworkGraphSource source) {
        if (!source.toUpdate()) return;

        bfsQueue.clear();
        source.setPathPrevious(null);
        bfsQueue.add(source);

        while (!bfsQueue.isEmpty()) {
            INetworkGraphVertexBase v = bfsQueue.poll();

            if (v instanceof INetworkGraphAbyss) {
                int consume = 0;
                INetworkGraphEdge prev = v.getPathPrevious();
                boolean flag = true; // Last edge: Forward true, Backward false
                int maxExtend = Integer.MAX_VALUE;
                while (prev != null) {
                    if (prev.getEnd() == v) {
                        // Append Consume Ratio
                        if (flag) consume += v.getConsumeRatio();
                        // Update Max Extend
                        if (maxExtend > prev.getCapacity() - prev.getCurrent())
                            maxExtend = prev.getCapacity() - prev.getCurrent();
                        flag = true;
                        v = prev.getStart();
                    } else {
                        // Append Consume Ratio
                        if (!flag) consume -= v.getConsumeRatio();
                        // Update Max Extend
                        if (maxExtend > prev.getCurrent())
                            maxExtend = prev.getCurrent();
                        flag = false;
                        v = prev.getEnd();
                    }
                    prev = v.getPathPrevious();
                    if (maxExtend == 0) break;
                }
                if (maxExtend == 0) continue;
                consume += source.getConsumeRatio();
                consume *= maxExtend;
                source.appendConsume(consume);
                ((INetworkGraphAbyss) v).appendConsume(consume);
                source.setToUpdate(true);
                return;
            }
            if (v instanceof INetworkGraphVertexIn)
                for (INetworkGraphEdge e : ((INetworkGraphVertexIn)v).getEdgesIn())
                    if (v.getPathPrevious() != e) {
                        INetworkGraphVertexBase next = e.getStart();
                        next.setPathPrevious(e);
                        bfsQueue.add(next);
                    }
            if (v instanceof INetworkGraphVertexOut)
                for (INetworkGraphEdge e : ((INetworkGraphVertexOut)v).getEdgesOut())
                    if (v.getPathPrevious() != e) {
                        INetworkGraphVertexBase next = e.getEnd();
                        next.setPathPrevious(e);
                        bfsQueue.add(next);
                    }
        }
        source.setToUpdate(false);
    }

    /**
     Remove an edge from an energy network.
     @param edge The edge to remove. Must be connected in a network when call this.
     */
    public static void removeEdge(INetworkGraphEdge edge) {
        setToUpdateFromVertex(edge.getStart());

        edge.getStart().getEdgesOut().remove(edge);
        edge.getEnd().getEdgesIn().remove(edge);

        bfsQueue.clear();
        edge.getStart().setPathPrevious(null);
        bfsQueue.add(edge.getStart());
        while (!bfsQueue.isEmpty()) {
            INetworkGraphVertexBase v = bfsQueue.poll();
            if (v instanceof INetworkGraphSource) {
                ((INetworkGraphSource) v).setToUpdate(true);
                for (INetworkGraphEdge prev = v.getPathPrevious();
                     prev != null;
                     prev = prev.getEnd().getPathPrevious())
                    prev.setCurrent(prev.getCurrent() - edge.getCurrent());
                break;
            }
            if (v instanceof INetworkGraphVertexIn)
                for (INetworkGraphEdge e : ((INetworkGraphVertexIn)v).getEdgesIn()) {
                    INetworkGraphVertexBase next = e.getStart();
                    next.setPathPrevious(e);
                    bfsQueue.add(next);
                }
        }

        bfsQueue.clear();
        edge.getEnd().setPathPrevious(null);
        bfsQueue.add(edge.getEnd());
        while (!bfsQueue.isEmpty()) {
            INetworkGraphVertexBase v = bfsQueue.poll();
            if (v instanceof INetworkGraphAbyss) {
                for (INetworkGraphEdge prev = v.getPathPrevious();
                     prev != null;
                     prev = prev.getStart().getPathPrevious())
                    prev.setCurrent(prev.getCurrent() - edge.getCurrent());
                break;
            }
            if (v instanceof INetworkGraphVertexOut)
                for (INetworkGraphEdge e : ((INetworkGraphVertexOut)v).getEdgesOut()) {
                    INetworkGraphVertexBase next = e.getEnd();
                    next.setPathPrevious(e);
                    bfsQueue.add(next);
                }
        }
    }

    /**
     Update network start from specified vertex.
     @param vertex Specified vertex in the network.
     */
    public static void setToUpdateFromVertex(INetworkGraphVertexBase vertex) {
        bfsQueue.clear();
        bfsQueue.add(vertex);
        while (!bfsQueue.isEmpty()) {
            INetworkGraphVertexBase v = bfsQueue.poll();
            if (v instanceof INetworkGraphSource) {
                ((INetworkGraphSource) v).setToUpdate(true);
            }
            if (v instanceof INetworkGraphVertexIn)
                for (INetworkGraphEdge e : ((INetworkGraphVertexIn)v).getEdgesIn())
                    if (v.getPathPrevious() != e)
                        bfsQueue.add(e.getStart());
            if (v instanceof INetworkGraphVertexOut)
                for (INetworkGraphEdge e : ((INetworkGraphVertexOut)v).getEdgesOut())
                    if (v.getPathPrevious() != e)
                        bfsQueue.add(e.getEnd());
        }
    }
}
