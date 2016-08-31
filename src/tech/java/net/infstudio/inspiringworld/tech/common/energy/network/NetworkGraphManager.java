package net.infstudio.inspiringworld.tech.common.energy.network;

import javax.annotation.Nullable;
import java.util.Queue;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
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
    private static boolean toUpdate;

    /**
     @param source The source node to update from
     */
    public static void updateFromSource(final INetworkGraphSource source) {
        if (!source.toUpdate()) {
            return;
        }

        toUpdate = false;

        bfsForEachVertex(source,
            new Predicate<INetworkGraphVertexBase>() {
                @Override
                public boolean apply(@Nullable INetworkGraphVertexBase input) {
                    if (!(input instanceof INetworkGraphAbyss)) return false;
                    int consume = 0;
                    INetworkGraphEdge prev;
                    boolean flag; // Last edge: Forward true, Backward false
                    int maxExtend = Integer.MAX_VALUE;

                    // Get max extending size
                    INetworkGraphVertexBase v1 = input;
                    flag = true;
                    prev = v1.getPathPrevious();
                    while (prev != null) {
                        if (prev.getEnd() == v1) {
                            // Append Consume Ratio
                            consume += flag ? v1.getConsumeRatio() : 0;
                            // Update Max Extend
                            maxExtend = Math.min(maxExtend, prev.getCapacity() - prev.getCurrent());
                            flag = true;
                            v1 = prev.getStart();
                        } else {
                            // Append Consume Ratio
                            consume -= flag ? 0 : v1.getConsumeRatio();
                            // Update Max Extend
                            maxExtend = Math.min(maxExtend, prev.getCurrent());
                            flag = false;
                            v1 = prev.getEnd();
                        }
                        prev = v1.getPathPrevious();
                    }

                    // Apply it to the path
                    INetworkGraphVertexBase v2 = input;
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

                    // Apply it to the source and abyss vertex
                    consume += source.getConsumeRatio();
                    consume *= maxExtend;
                    source.appendConsume(consume);
                    ((INetworkGraphAbyss) input).appendConsume(consume);
                    toUpdate = true;
                    return true;
                }
            },
            new Predicate<INetworkGraphEdge>() {
                @Override
                public boolean apply(@Nullable INetworkGraphEdge input) {
                    // Ignore this edge if it's empty and it's of the opposite direction of the path
                    return input == null || input.getCurrent() == 0;
                }
            },
            new Predicate<INetworkGraphEdge>() {
                @Override
                public boolean apply(@Nullable INetworkGraphEdge input) {
                    // Ignore this edge if it's full and it's of the same direction with the path
                    return input == null || input.getCurrent() == input.getCapacity();
                }
            });

        source.setToUpdate(toUpdate);
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
        while (temp > 0) {
            bfsForEachVertex(edge.getStart(),
                new Predicate<INetworkGraphVertexBase>() {
                    @Override
                    public boolean apply(@Nullable INetworkGraphVertexBase input) {
                        if (!(input instanceof INetworkGraphSource)) {
                            return false;
                        }
                        int maxReduce = temp;
                        for (INetworkGraphEdge prev = input.getPathPrevious();
                             prev != null;
                             prev = prev.getEnd().getPathPrevious()) {
                            maxReduce = Math.min(prev.getCurrent(), maxReduce);
                        }
                        for (INetworkGraphEdge prev = input.getPathPrevious();
                             prev != null;
                             prev = prev.getEnd().getPathPrevious()) {
                            prev.setCurrent(prev.getCurrent() - maxReduce);
                        }
                        temp -= maxReduce;
                        return true;
                    }
                },
                new Predicate<INetworkGraphEdge>() {
                    @Override
                    public boolean apply(@Nullable INetworkGraphEdge input) {
                        return input == null || input.getCurrent() == 0;
                    }
                },
                Predicates.<INetworkGraphEdge>alwaysTrue());
        }

        temp = edge.getCurrent();
        while (temp > 0) {
            bfsForEachVertex(edge.getEnd(),
                new Predicate<INetworkGraphVertexBase>() {
                    @Override
                    public boolean apply(@Nullable INetworkGraphVertexBase input) {
                        if (!(input instanceof INetworkGraphAbyss)) {
                            return false;
                        }
                        int maxReduce = temp;
                        for (INetworkGraphEdge prev = input.getPathPrevious();
                             prev != null;
                             prev = prev.getStart().getPathPrevious()) {
                            maxReduce = Math.min(prev.getCurrent(), maxReduce);
                        }
                        for (INetworkGraphEdge prev = input.getPathPrevious();
                             prev != null;
                             prev = prev.getStart().getPathPrevious()) {
                            prev.setCurrent(prev.getCurrent() - maxReduce);
                        }
                        temp -= maxReduce;
                        return true;
                    }
                },
                Predicates.<INetworkGraphEdge>alwaysTrue(),
                new Predicate<INetworkGraphEdge>() {
                    @Override
                    public boolean apply(@Nullable INetworkGraphEdge input) {
                        return input == null || input.getCurrent() == 0;
                    }
                });
        }
    }

    /**
     Update network start from specified vertex.
     @param vertex Specified vertex in the network.
     */
    public static void setToUpdateFromVertex(INetworkGraphVertexBase vertex) {
        bfsForEachVertex(vertex,
            new Predicate<INetworkGraphVertexBase>() {
                @Override
                public boolean apply(@Nullable INetworkGraphVertexBase input) {
                    if (input instanceof INetworkGraphSource) {
                        ((INetworkGraphSource) input).setToUpdate(true);
                    }
                    return true;
                }
            },
            Predicates.<INetworkGraphEdge>alwaysFalse(),
            Predicates.<INetworkGraphEdge>alwaysFalse());
    }

    public static void bfsForEachVertex(final INetworkGraphVertexBase vertex,
                                        final Predicate<INetworkGraphVertexBase> exit,
                                        final Predicate<INetworkGraphEdge> ignoreBackward,
                                        final Predicate<INetworkGraphEdge> ignoreForward) {
        bfsQueue.add(vertex);
        bfsVisited.add(vertex);
        vertex.setPathPrevious(null);

        while (!bfsQueue.isEmpty()) {
            INetworkGraphVertexBase v = bfsQueue.poll();
            if (exit.apply(v)) {
                break;
            }
            if (!Predicates.<INetworkGraphEdge>alwaysTrue().equals(ignoreBackward)
                && (v instanceof INetworkGraphVertexIn)) {
                for (INetworkGraphEdge e : ((INetworkGraphVertexIn) v).getEdgesIn()) {
                    INetworkGraphVertexBase next = e.getStart();
                    if (!bfsVisited.contains(next) && !ignoreBackward.apply(e)) {
                        next.setPathPrevious(e);
                        bfsQueue.add(next);
                        bfsVisited.add(next);
                    }
                }
            }
            if (!Predicates.<INetworkGraphEdge>alwaysTrue().equals(ignoreForward)
                && (v instanceof INetworkGraphVertexOut)) {
                for (INetworkGraphEdge e : ((INetworkGraphVertexOut) v).getEdgesOut()) {
                    INetworkGraphVertexBase next = e.getEnd();
                    if (!bfsVisited.contains(next) && !ignoreForward.apply(e)) {
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
}
