package net.infstudio.inspiringworld.magic.repackage.net.simplelib.model;

import net.minecraft.client.model.ModelRenderer;

/**
 * @author ci010
 */
public class ModelRenderers
{
	public static void rotate(ModelRenderer renderer, int degree, float x, float y, float z)
	{
		double radians = Math.toRadians(degree);
		rotate(renderer, (float) radians, x, y, z);
	}

	public static void rotate(ModelRenderer renderer, float radian, float x, float y, float z)
	{
		renderer.rotateAngleX += x * radian;
		renderer.rotateAngleY += y * radian;
		renderer.rotateAngleZ += z * radian;
	}

}
