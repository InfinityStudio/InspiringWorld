package net.infstudio.inspiringworld.tech.common.energy.network;

import javax.annotation.Nullable;
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
    private static int temp;

    /**
     @param source The source node to update from
     */
    public static void updateFromSource(INetworkGraphSource source) {
        if (!source.toUpdate()) {
            return;
        }

        source.setPathPrevious(null);
        bfsQueue.add(source);

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

                bfsQueue.clear();
                return;
            }
            if (v instanceof INetworkGraphVertexIn) {
                for (INetworkGraphEdge e : ((INetworkGraphVertexIn)v).getEdgesIn()) {
                    INetworkGraphVertexBase next = e.getStart();
                    if (!onPath(v, next)) {
                        next.setPathPrevious(e);
                        bfsQueue.add(next);
                    }
                }
            }
            if (v instanceof INetworkGraphVertexOut) {
                for (INetworkGraphEdge e : ((INetworkGraphVertexOut)v).getEdgesOut()) {
                    INetworkGraphVertexBase next = e.getEnd();
                    if (!onPath(v, next)) {
                        next.setPathPrevious(e);
                        bfsQueue.add(next);
                    }
                }
            }
        }
        source.setToUpdate(false);

        bfsQueue.clear();
    }

    private static boolean onPath(INetworkGraphVertexBase pathEnd, INetworkGraphVertexBase toCheck) {
        while (pathEnd != toCheck) {
            INetworkGraphEdge prev = pathEnd.getPathPrevious();
            if (prev == null) {
                return false;
            }
            pathEnd = prev.getStart() == pathEnd ? prev.getEnd() : prev.getStart();
        }
        return true;
    }

    /**
     Remove an edge from an energy network.
     @param edge The edge to remove. Must be connected in a network when call this.
     */
    public static void removeEdge(INetworkGraphEdge edge) {
        setToUpdateFromVertex(edge.getStart());

        edge.getStart().getEdgesOut().remove(edge);
        edge.getEnd().getEdgesIn().remove(edge);

        temp = edge.getCurrent();

        bfsForEachVertex(edge.getStart(), new Predicate<INetworkGraphVertexBase>() {
            @Override
            public boolean apply(@Nullable INetworkGraphVertexBase input) {
                if (!(input instanceof INetworkGraphSource)) {
                    return true;
                }
                for (INetworkGraphEdge prev = input.getPathPrevious();
                     prev != null;
                     prev = prev.getEnd().getPathPrevious()) {
                    prev.setCurrent(prev.getCurrent() - temp);
                }
                return false;
            }
        }, NetworkIterateType.BACKWARD);

        bfsForEachVertex(edge.getEnd(), new Predicate<INetworkGraphVertexBase>() {
            @Override
            public boolean apply(@Nullable INetworkGraphVertexBase input) {
                if (!(input instanceof INetworkGraphAbyss)) {
                    return true;
                }
                for (INetworkGraphEdge prev = input.getPathPrevious();
                     prev != null;
                     prev = prev.getStart().getPathPrevious()) {
                    prev.setCurrent(prev.getCurrent() - temp);
                }
                return false;
            }
        }, NetworkIterateType.FORWARD);
    }

    /**
     Update network start from specified vertex.
     @param vertex Specified vertex in the network.
     */
    public static void setToUpdateFromVertex(INetworkGraphVertexBase vertex) {
        bfsForEachVertex(vertex, new Predicate<INetworkGraphVertexBase>() {
            @Override
            public boolean apply(@Nullable INetworkGraphVertexBase input) {
                if (input instanceof INetworkGraphSource) {
                    ((INetworkGraphSource) input).setToUpdate(true);
                }
                return true;
            }
        }, NetworkIterateType.TWO_WAY);
    }

    public static void bfsForEachVertex(INetworkGraphVertexBase vertex, Predicate<? super INetworkGraphVertexBase> function,
                                        NetworkIterateType type) {
        bfsQueue.add(vertex);
        bfsVisited.add(vertex);
        vertex.setPathPrevious(null);

        while (!bfsQueue.isEmpty()) {
            INetworkGraphVertexBase v = bfsQueue.poll();
            if (!function.apply(v)) {
                break;
            }
            if ((type == NetworkIterateType.TWO_WAY || type == NetworkIterateType.BACKWARD)
                && v instanceof INetworkGraphVertexIn) {
                for (INetworkGraphEdge e : ((INetworkGraphVertexIn) v).getEdgesIn()) {
                    INetworkGraphVertexBase next = e.getStart();
                    if (!bfsVisited.contains(next)) {
                        next.setPathPrevious(e);
                        bfsQueue.add(next);
                        bfsVisited.add(next);
                    }
                }
            }
            if ((type == NetworkIterateType.TWO_WAY || type == NetworkIterateType.FORWARD)
                && v instanceof INetworkGraphVertexOut) {
                for (INetworkGraphEdge e : ((INetworkGraphVertexOut) v).getEdgesOut()) {
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

    public enum NetworkIterateType {
        TWO_WAY,
        FORWARD,
        BACKWARD
    }
}
