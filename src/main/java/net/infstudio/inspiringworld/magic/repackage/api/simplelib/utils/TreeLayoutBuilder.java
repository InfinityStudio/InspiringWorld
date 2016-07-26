package net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils;

import com.google.common.base.Function;

import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.CompiledLayout;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.LayoutBuilder;

import org.lwjgl.util.vector.Vector2f;

import java.util.HashMap;

/**
 * @author ci010
 */
public class TreeLayoutBuilder implements LayoutBuilder
{
	private int numOfNode;
	private Function<Node, Vector2f> nodeSize;
	private Vector2f containerSize, gap, rootPos;
	private boolean unify, symmetry, changed;
	private Node root;
	private HashMap<Node, Vector2f> pos;

	private Tree tree = new Tree();

	public interface Node
	{
		Node[] children();
	}

	private class Layer
	{
		Tree parent;
		TreeNode[] nodes;
		int depth;

		int width;

		float gWidth, gHeight;

		public Layer(Tree parent, int depth)
		{
			this.parent = parent;
			this.depth = depth;
			this.nodes = new TreeNode[0];
		}
	}

	private class TreeNode
	{
		private Layer layer;
		private TreeNode parent;
		private Vector2f size;
		private Node delegate;

		public TreeNode(Layer layer, Vector2f vector2f, Node node)
		{
			this.layer = layer;
			this.size = vector2f;
			this.delegate = node;
		}
	}

	private class Tree
	{
		Layer[] layers = new Layer[0];
		int treeHeight, treeWidth;

		int maxWidthLayer;
	}


	public TreeLayoutBuilder() {}

	private void make()
	{
		this.discoverBase(root, null, 0, 0);
		int maxWidth;
		for (Layer layer : tree.layers)
		{
			float height = 0;
			for (TreeNode node : layer.nodes)
			{
				layer.gWidth += node.size.getX() + gap.getX();
				if (layer.gHeight > height)
					height = layer.gHeight;
			}
			layer.gHeight = height;
		}
	}


	private void ensureTreeHeight(Tree tree, int height)
	{
		if (tree.layers.length < height)
		{
			Layer[] layers = new Layer[height * 2];
			System.arraycopy(tree.layers, 0, layers, 0, tree.layers.length);
			tree.layers = layers;
			for (int i = 0; i < tree.layers.length; i++)
				if (layers[i] == null)
					layers[i] = new Layer(tree, i);
		}
	}

	private void ensureLayerWidth(Layer layer)
	{
		if (layer.width > layer.nodes.length)
		{
			TreeNode[] nodes = new TreeNode[layer.width * 2];
			System.arraycopy(layer.nodes, 0, nodes, 0, layer.nodes.length);
			layer.nodes = nodes;
		}
	}

	private void discoverBase(Node node, TreeNode parent, int index, int depth)
	{
		ensureTreeHeight(this.tree, depth);
		Layer layer = tree.layers[depth];
		layer.width++;
		ensureLayerWidth(layer);
		TreeNode treeNode = new TreeNode(layer, nodeSize.apply(node), node);
		treeNode.parent = parent;
		layer.nodes[index] = treeNode;

		Node[] children = node.children();
		if (children != null)
		{
			if (children.length > tree.treeWidth)
				tree.treeWidth = children.length;

			for (int i = 0; i < children.length; i++)
				discoverBase(children[i], treeNode, i, depth + 1);
		}
		else if (depth > tree.treeHeight)
			tree.treeHeight = depth;
	}

	private void discoverGraphic()
	{

	}

	@Override
	public CompiledLayout build()
	{
		return null;
	}
}
