package net.infstudio.inspiringworld.tech.common.energy.network;

import java.util.Queue;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;

import net.infstudio.inspiringworld.tech.api.energy.network.*;

/**
 * @author Blealtan
 */
public class NetworkGraphManager {

    private NetworkGraphManager() {}

    private static Queue<INetworkGraphVertexBase> bfsQueue = Queues.newArrayDeque();
    private static Set<INetworkGraphVertexBase> bfsVisited = Sets.newLinkedHashSet();

    /**
     @param source The source node to update from
     */
    public static void updateFromSource(INetworkGraphSource source) {
        if (!source.toUpdate()) {
            return;
        }

        source.setPathPrevious(null);
        bfsQueue.add(source);
        bfsVisited.add(source);

        while (!bfsQueue.isEmpty()) {
            INetworkGraphVertexBase v = bfsQueue.poll();

            if (v instanceof INetworkGraphAbyss) {
                int consume = 0;
                INetworkGraphEdge prev;
                boolean flag; // Last edge: Forward true, Backward false
                int maxExtend = Integer.MAX_VALUE;

                // Get max extending size
                INetworkGraphVertexBase v1 = v;
                flag = true;
                prev = v1.getPathPrevious();
                while (prev != null) {
                    if (prev.getEnd() == v1) {
                        // Append Consume Ratio
                        if (flag) {
                            consume += v1.getConsumeRatio();
                        }
                        // Update Max Extend
                        if (maxExtend > prev.getCapacity() - prev.getCurrent()) {
                            maxExtend = prev.getCapacity() - prev.getCurrent();
                        }
                        flag = true;
                        v1 = prev.getStart();
                    } else {
                        // Append Consume Ratio
                        if (!flag) {
                            consume -= v1.getConsumeRatio();
                        }
                        // Update Max Extend
                        if (maxExtend > prev.getCurrent()) {
                            maxExtend = prev.getCurrent();
                        }
                        flag = false;
                        v1 = prev.getEnd();
                    }
                    prev = v1.getPathPrevious();
                    if (maxExtend == 0) {
                        break;
                    }
                }

                // If no available space for larger flow, leave this path behind
                if (maxExtend == 0) {
                    continue;
                }

                // Apply it
                INetworkGraphVertexBase v2 = v;
                flag = true;
                prev = v2.getPathPrevious();
                while (prev != null) {
                    if (prev.getEnd() == v2) {
                        if (flag && v2 instanceof INetworkGraphVertex) {
                            ((INetworkGraphVertex) v2).appendPass(maxExtend);
                        }
                        prev.setCurrent(prev.getCurrent() + maxExtend);
                        flag = true;
                        v2 = prev.getStart();
                    } else {
                        if (flag && v2 instanceof INetworkGraphVertex) {
                            ((INetworkGraphVertex) v2).appendPass(-maxExtend);
                        }
                        prev.setCurrent(prev.getCurrent() - maxExtend);
                        flag = false;
                        v2 = prev.getEnd();
                    }
                    prev = v2.getPathPrevious();
                }

                consume += source.getConsumeRatio();
                consume *= maxExtend;
                source.appendConsume(consume);
                ((INetworkGraphAbyss) v).appendConsume(consume);
                source.setToUpdate(true);

                bfsVisited.clear();
                bfsQueue.clear();
                return;
            }
            if (v instanceof INetworkGraphVertexIn) {
                for (INetworkGraphEdge e : ((INetworkGraphVertexIn)v).getEdgesIn()) {
                    INetworkGraphVertexBase next = e.getStart();
                    if (!bfsVisited.contains(next)) {
                        next.setPathPrevious(e);
                        bfsQueue.add(next);
                        bfsVisited.add(next);
                    }
                }
            }
            if (v instanceof INetworkGraphVertexOut) {
                for (INetworkGraphEdge e : ((INetworkGraphVertexOut)v).getEdgesOut()) {
                    INetworkGraphVertexBase next = e.getEnd();
                    if (!bfsVisited.contains(next)) {
                        next.setPathPrevious(e);
                        bfsQueue.add(next);
                        bfsVisited.add(next);
                    }
                }
            }
        }
        source.setToUpdate(false);

        bfsVisited.clear();
        bfsQueue.clear();
    }

    /**
     Remove an edge from an energy network.
     @param edge The edge to remove. Must be connected in a network when call this.
     */
    public static void removeEdge(INetworkGraphEdge edge) {
        setToUpdateFromVertex(edge.getStart());

        edge.getStart().getEdgesOut().remove(edge);
        edge.getEnd().getEdgesIn().remove(edge);

        edge.getStart().setPathPrevious(null);

        bfsQueue.add(edge.getStart());
        bfsVisited.add(edge.getStart());
        while (!bfsQueue.isEmpty()) {
            INetworkGraphVertexBase v = bfsQueue.poll();
            if (v instanceof INetworkGraphSource) {
                for (INetworkGraphEdge prev = v.getPathPrevious();
                     prev != null;
                     prev = prev.getEnd().getPathPrevious()) {
                    prev.setCurrent(prev.getCurrent() - edge.getCurrent());
                }
                break;
            }
            if (v instanceof INetworkGraphVertexIn) {
                for (INetworkGraphEdge e : ((INetworkGraphVertexIn)v).getEdgesIn()) {
                    INetworkGraphVertexBase next = e.getStart();
                    if (!bfsVisited.contains(next)) {
                        next.setPathPrevious(e);
                        bfsQueue.add(next);
                        bfsVisited.add(next);
                    }
                }
            }
        }

        bfsVisited.clear();
        bfsQueue.clear();

        edge.getEnd().setPathPrevious(null);
        bfsQueue.add(edge.getEnd());
        bfsVisited.add(edge.getEnd());
        while (!bfsQueue.isEmpty()) {
            INetworkGraphVertexBase v = bfsQueue.poll();
            if (v instanceof INetworkGraphAbyss) {
                for (INetworkGraphEdge prev = v.getPathPrevious();
                     prev != null;
                     prev = prev.getStart().getPathPrevious()) {
                    prev.setCurrent(prev.getCurrent() - edge.getCurrent());
                }
                break;
            }
            if (v instanceof INetworkGraphVertexOut) {
                for (INetworkGraphEdge e : ((INetworkGraphVertexOut)v).getEdgesOut()) {
                    INetworkGraphVertexBase next = e.getEnd();
                    if (!bfsVisited.contains(next)) {
                        next.setPathPrevious(e);
                        bfsQueue.add(next);
                        bfsVisited.add(next);
                    }
                }
            }
        }

        bfsVisited.clear();
        bfsQueue.clear();
    }

    /**
     Update network start from specified vertex.
     @param vertex Specified vertex in the network.
     */
    public static void setToUpdateFromVertex(INetworkGraphVertexBase vertex) {
        bfsQueue.add(vertex);
        bfsVisited.add(vertex);
        while (!bfsQueue.isEmpty()) {
            INetworkGraphVertexBase v = bfsQueue.poll();
            if (v instanceof INetworkGraphSource) {
                ((INetworkGraphSource) v).setToUpdate(true);
            }
            if (v instanceof INetworkGraphVertexIn) {
                for (INetworkGraphEdge e : ((INetworkGraphVertexIn)v).getEdgesIn()) {
                    INetworkGraphVertexBase next = e.getStart();
                    if (!bfsVisited.contains(next)) {
                        bfsQueue.add(next);
                        bfsVisited.add(next);
                    }
                }
            }
            if (v instanceof INetworkGraphVertexOut) {
                for (INetworkGraphEdge e : ((INetworkGraphVertexOut)v).getEdgesOut()) {
                    INetworkGraphVertexBase next = e.getEnd();
                    if (!bfsVisited.contains(next)) {
                        bfsQueue.add(next);
                        bfsVisited.add(next);
                    }
                }
            }
        }

        bfsVisited.clear();
        bfsQueue.clear();
    }

    public static void bfsForEach(INetworkGraphVertexBase vertex, Predicate<? super INetworkGraphVertexBase> function) {
        bfsQueue.add(vertex);
        bfsVisited.add(vertex);

        while (!bfsQueue.isEmpty()) {
            INetworkGraphVertexBase v = bfsQueue.poll();
            if (!function.apply(v)) {
                break;
            }
            if (v instanceof INetworkGraphVertexIn) {
                for (INetworkGraphEdge e : ((INetworkGraphVertexIn) v).getEdgesIn()) {
                    INetworkGraphVertexBase next = e.getStart();
                    if (!bfsVisited.contains(next)) {
                        bfsQueue.add(next);
                        bfsVisited.add(next);
                    }
                }
            }
            if (v instanceof INetworkGraphVertexOut) {
                for (INetworkGraphEdge e : ((INetworkGraphVertexOut) v).getEdgesOut()) {
                    INetworkGraphVertexBase next = e.getEnd();
                    if (!bfsVisited.contains(next)) {
                        bfsQueue.add(next);
                        bfsVisited.add(next);
                    }
                }
            }
        }

        bfsVisited.clear();
        bfsQueue.clear();
    }
}
