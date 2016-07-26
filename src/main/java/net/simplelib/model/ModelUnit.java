package net.simplelib.model;

import com.google.common.collect.Lists;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

/**
 * @author ci010
 */
public class ModelUnit
{
	public final String groupName;

	private int textureWidth, textureHeight;
	private Vector3f rotation, rotationPoint, offset;

	private boolean compiled;
	private int displayList;
	private boolean showModel;

	private List<Cube> cubeList;
	private List<ModelUnit> childModels;

	public ModelUnit(ModelGroupEntity model, String name)
	{
		this.textureWidth = 64;
		this.textureHeight = 32;
		this.showModel = true;
		this.cubeList = Lists.newArrayList();
		rotation = new Vector3f();
		rotationPoint = new Vector3f();
		offset = new Vector3f();
//		model.boxList.add(this);

		this.groupName = name;
		this.setTextureSize(model.textureWidth, model.textureHeight);
	}

	public ModelUnit(ModelGroupEntity model)
	{
		this(model, null);
	}

	public void setInvisible(boolean b)
	{
		this.showModel = b;
	}

	/**
	 * Returns the model renderer with the new texture parameters.
	 */
	public ModelUnit setTextureSize(int textureWidthIn, int textureHeightIn)
	{
		this.textureWidth = textureWidthIn;
		this.textureHeight = textureHeightIn;
		return this;
	}

	/**
	 * Sets the current box's rotation points and rotation angles to another box.
	 */
	public void addChild(ModelUnit renderer)
	{
		if (this.childModels == null)
			this.childModels = Lists.newArrayList();
		this.childModels.add(renderer);
	}

	public ModelUnit addUnit(UVMapper mapper, Vector3f pos, Vector3f size, float scale)
	{
		Cube modelUnit = new Cube(this.textureHeight, this.textureWidth, mapper, pos, size, scale);
		this.cubeList.add(modelUnit);
		return this;
	}

	public ModelUnit addUnit(UVMapper mapper, Vector3f pos, Vector3f size)
	{
		Cube modelUnit = new Cube(this.textureHeight, this.textureWidth, mapper, pos, size, 0);
		this.cubeList.add(modelUnit);
		return this;
	}

	public void setRotationPoint(float rotationPointX, float rotationPointY, float rotationPointZ)
	{
		this.rotationPoint.x = rotationPointX;
		this.rotationPoint.y = rotationPointY;
		this.rotationPoint.z = rotationPointZ;
	}

	public void rotate(int degree, float x, float y, float z)
	{
		double radians = Math.toRadians(degree);
		rotate((float) radians, x, y, z);
	}

	public void rotate(float radian, float x, float y, float z)
	{
		this.rotation.translate(x * radian, y * radian, z * radian);
	}

	public Vector3f getRotation()
	{
		return this.rotation;
	}

	@SideOnly(Side.CLIENT)
	public void render(float scale)
	{
		if (this.showModel)
		{
			if (!this.compiled)
				this.compileDisplayList(scale);

			GlStateManager.pushMatrix();
			GlStateManager.translate(this.offset.x, this.offset.y, this.offset.z);

			if (this.rotation.x == 0.0F && this.rotation.y == 0.0F && this.rotation.z == 0.0F)
				if (this.rotationPoint.x == 0.0F && this.rotationPoint.y == 0.0F && this.rotationPoint.z == 0.0F)
				{
					GlStateManager.callList(this.displayList);

					if (this.childModels != null)
						for (int k = 0; k < this.childModels.size(); ++k)
							this.childModels.get(k).render(scale);
				}
				else
				{
					GlStateManager.pushMatrix();

					GlStateManager.translate(this.rotationPoint.x * scale, this.rotationPoint.y * scale, this.rotationPoint.z * scale);
					GlStateManager.callList(this.displayList);

					if (this.childModels != null)
						for (int j = 0; j < this.childModels.size(); ++j)
							this.childModels.get(j).render(scale);

					GlStateManager.popMatrix();
				}
			else
			{
				GlStateManager.translate(this.rotationPoint.x * scale, this.rotationPoint.y * scale, this.rotationPoint.z * scale);

				if (this.rotation.z != 0.0F)
					GlStateManager.rotate(this.rotation.z * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
				if (this.rotation.y != 0.0F)
					GlStateManager.rotate(this.rotation.y * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
				if (this.rotation.x != 0.0F)
					GlStateManager.rotate(this.rotation.x * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);

				GlStateManager.callList(this.displayList);
				if (this.childModels != null)
					for (int i = 0; i < this.childModels.size(); ++i)
						this.childModels.get(i).render(scale);
			}
			GlStateManager.popMatrix();
		}
	}

	@SideOnly(Side.CLIENT)
	public void renderWithRotation(float scale)
	{
		if (this.showModel)
		{
			if (!this.compiled)
				this.compileDisplayList(scale);

			GlStateManager.pushMatrix();
			GlStateManager.translate(this.rotationPoint.x * scale, this.rotationPoint.y * scale, this.rotationPoint.z * scale);

			if (this.rotation.x != 0.0F)
				GlStateManager.rotate(this.rotation.x * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
			if (this.rotation.y != 0.0F)
				GlStateManager.rotate(this.rotation.y * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
			if (this.rotation.z != 0.0F)
				GlStateManager.rotate(this.rotation.z * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);

			GlStateManager.callList(this.displayList);
			GlStateManager.popMatrix();
		}
	}

	/**
	 * Allows the changing of Angles after a box has been rendered
	 */
	@SideOnly(Side.CLIENT)
	public void postRender(float scale)
	{
		if (this.showModel)
		{
			if (!this.compiled)
				this.compileDisplayList(scale);

			if (this.rotation.x == 0.0F && this.rotation.y == 0.0F && this.rotation.z == 0.0F)
				if (this.rotationPoint.x != 0.0F || this.rotationPoint.y != 0.0F || this.rotationPoint.z != 0.0F)
					GlStateManager.translate(this.rotationPoint.x * scale, this.rotationPoint.y * scale, this.rotationPoint.z * scale);
				else
				{
					GlStateManager.translate(this.rotationPoint.x * scale, this.rotationPoint.y * scale, this.rotationPoint.z * scale);

					if (this.rotation.z != 0.0F)
						GlStateManager.rotate(this.rotation.z * (180F / (float) Math.PI), 0.0F, 0.0F, 1.0F);
					if (this.rotation.y != 0.0F)
						GlStateManager.rotate(this.rotation.y * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
					if (this.rotation.x != 0.0F)
						GlStateManager.rotate(this.rotation.x * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
				}
		}
	}

	/**
	 * Compiles a GL display list for this model
	 */
	@SideOnly(Side.CLIENT)
	private void compileDisplayList(float scale)
	{
		this.displayList = GLAllocation.generateDisplayLists(1);
		GL11.glNewList(this.displayList, GL11.GL_COMPILE);
		VertexBuffer worldrenderer = Tessellator.getInstance().getBuffer();

		for (int i = 0; i < this.cubeList.size(); ++i)
			this.cubeList.get(i).render(worldrenderer, scale);

		GL11.glEndList();
		this.compiled = true;
	}

	public int getTextureWidth()
	{
		return textureWidth;
	}

	public int getTextureHeight()
	{
		return textureHeight;
	}

	public interface UVMapper
	{
		/**
		 * @param facing the face.
		 * @return an array with length 4 {u1, v1, u2, v2}.
		 */
		int[] getUV(EnumFacing facing);
	}

	public static class Cube
	{
		protected TexturedQuad[] quadList;

		public Cube(int textureHeight, int textureWidth, UVMapper mapper,
					Vector3f pos, Vector3f size,
					float scale)
		{
			float posX = pos.x;
			float posY = pos.y;
			float posZ = pos.z;
			float posX_2 = posX + size.x;
			float posY_2 = posY + size.y;
			float posZ_2 = posZ + size.z;
			posX -= scale;
			posY -= scale;
			posZ -= scale;
			posX_2 += scale;
			posY_2 += scale;
			posZ_2 += scale;
			PositionTextureVertex p7 = new PositionTextureVertex(posX, posY, posZ, 0.0F, 0.0F);
			PositionTextureVertex p0 = new PositionTextureVertex(posX_2, posY, posZ, 0.0F, 8.0F);
			PositionTextureVertex p1 = new PositionTextureVertex(posX_2, posY_2, posZ, 8.0F, 8.0F);
			PositionTextureVertex p2 = new PositionTextureVertex(posX, posY_2, posZ, 8.0F, 0.0F);
			PositionTextureVertex p3 = new PositionTextureVertex(posX, posY, posZ_2, 0.0F, 0.0F);
			PositionTextureVertex p4 = new PositionTextureVertex(posX_2, posY, posZ_2, 0.0F, 8.0F);
			PositionTextureVertex p5 = new PositionTextureVertex(posX_2, posY_2, posZ_2, 8.0F, 8.0F);
			PositionTextureVertex p6 = new PositionTextureVertex(posX, posY_2, posZ_2, 8.0F, 0.0F);

			PositionTextureVertex[] right = {p4, p0, p1, p5},
					left = {p7, p3, p6, p2},
					top = {p4, p3, p7, p0},
					bottom = {p1, p2, p6, p5},
					front = {p0, p7, p2, p1},
					back = {p3, p4, p5, p6};
			this.quadList = new TexturedQuad[6];
			int[] uv = mapper.getUV(EnumFacing.EAST);
			this.quadList[0] = new TexturedQuad(right, uv[0], uv[1], uv[2], uv[3], textureWidth, textureHeight);
			uv = mapper.getUV(EnumFacing.WEST);
			this.quadList[1] = new TexturedQuad(left, uv[0], uv[1], uv[2], uv[3], textureWidth, textureHeight);
			uv = mapper.getUV(EnumFacing.UP);
			this.quadList[2] = new TexturedQuad(top, uv[0], uv[1], uv[2], uv[3], textureWidth, textureHeight);
			uv = mapper.getUV(EnumFacing.DOWN);
			this.quadList[3] = new TexturedQuad(bottom, uv[0], uv[1], uv[2], uv[3], textureWidth, textureHeight);
			uv = mapper.getUV(EnumFacing.NORTH);
			this.quadList[4] = new TexturedQuad(front, uv[0], uv[1], uv[2], uv[3], textureWidth, textureHeight);
			uv = mapper.getUV(EnumFacing.WEST);
			this.quadList[5] = new TexturedQuad(back, uv[0], uv[1], uv[2], uv[3], textureWidth, textureHeight);
		}

		@SideOnly(Side.CLIENT)
		public void render(VertexBuffer renderer, float scale)
		{
			for (int i = 0; i < quadList.length; i++)
				quadList[i].draw(renderer, scale);
		}
	}
}
