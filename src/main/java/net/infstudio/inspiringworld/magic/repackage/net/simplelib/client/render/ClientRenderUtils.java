package net.infstudio.inspiringworld.magic.repackage.net.simplelib.client.render;

import net.minecraft.client.renderer.Vector3d;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.util.vector.Vector4f;

/**
 * @author ci010
 */
public class ClientRenderUtils
{
	public static Vector3d getRealPositionX(BlockPos pos)
	{
		Vector3d vector3d = new Vector3d();
		vector3d.x = pos.getX() - TileEntityRendererDispatcher.staticPlayerX;
		vector3d.y = pos.getY() - TileEntityRendererDispatcher.staticPlayerY;
		vector3d.z = pos.getZ() - TileEntityRendererDispatcher.staticPlayerZ;
		return vector3d;
	}

	public static Vector4f getColorInGLFloat(int rgba)
	{
		return new Vector4f(
				(float) (rgba >> 16 & 255) / 255.0F,
				(float) (rgba >> 8 & 255) / 255.0F,
				(float) (rgba & 255) / 255.0F,
				(float) (rgba >> 24 & 255) / 255.0F);
	}

	public static int getColorToInt(Vector4f color)
	{
		return 0;
	}
}
