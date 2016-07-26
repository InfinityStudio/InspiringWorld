package net.infstudio.inspiringworld.magic.repackage.net.simplelib.client.loading;

import com.google.common.collect.Lists;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author ci010
 */
public class ImageMerger
{
	private List<BufferedImage> images = Lists.newLinkedList();
	private static int standardSize = 256;

	static Comparator<BufferedImage> comparator = new Comparator<BufferedImage>()
	{
		@Override
		public int compare(BufferedImage o1, BufferedImage o2)
		{
			return o1.getHeight() + o1.getWidth() - o2.getHeight() - o2.getWidth();
		}
	};

	public void add(BufferedImage image)
	{
		images.add(image);
	}


	private void build()
	{
		Collections.sort(images, new Comparator<BufferedImage>()
		{
			@Override
			public int compare(BufferedImage o1, BufferedImage o2)
			{
				return o1.getHeight() + o1.getWidth() - o2.getHeight() - o2.getWidth();
			}
		});
		BufferedImage newImg = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);

	}

	private class Tree
	{
		TreeNode root;

		public void put(BufferedImage image)
		{
			if (root != null)
				root.put(image);
			else
				root = new TreeNode(image);
		}
	}

	private class TreeNode
	{
		BufferedImage image;
		TreeNode left, right;

		public TreeNode(BufferedImage image)
		{
			this.image = image;
		}

		void put(BufferedImage image)
		{//TODO ....wtf totally wrong
			int compare = comparator.compare(this.image, image);
			if (compare < 0)
				if (left != null)
					left.put(image);
				else
					left = new TreeNode(image);
			else if (right != null)
				right.put(image);
			else
				right = new TreeNode(image);
		}
	}

	private void handle(BufferedImage image)
	{
		int width = image.getWidth(), height = image.getHeight();

	}

//	private void merge(BufferedImage paper, BufferedImage newImage, int x, int y)
//	{
//		paper.setRGB(x,y,newImage.getWidth(),newImage.getHeight(),newImage.getRGB(0,0,newImage.getWidth(),new ),0,0);
//	}
}
