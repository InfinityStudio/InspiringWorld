package net.infstudio.inspiringworld.magic.repackage.net.simplelib.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

/**
 * @author ci010
 */
public abstract class ModelRenderer<T extends EntityLivingBase> extends RenderLivingBase<T>
{
	public ModelRenderer(RenderManager renderManagerIn, ModelGroupEntity modelBaseIn, float shadowSizeIn)
	{
		super(renderManagerIn, modelBaseIn, shadowSizeIn);
	}

	protected ModelGroupEntity mainModel()
	{
		return (ModelGroupEntity) this.mainModel;
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Pre(entity, this, x, y, z)))
			return;
		GlStateManager.pushMatrix();
		GlStateManager.disableCull();
		this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
		this.mainModel.isRiding = entity.isRiding();
		this.mainModel.isChild = entity.isChild();

		try
		{
			float rotYawOffset = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
			float rotYaw = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
			float diff = rotYaw - rotYawOffset;

			if (entity.isRiding() && entity.getRidingEntity() instanceof EntityLivingBase)
			{
				EntityLivingBase riding = (EntityLivingBase) entity.getRidingEntity();
				rotYawOffset = this.interpolateRotation(riding.prevRenderYawOffset, riding.renderYawOffset, partialTicks);
				diff = rotYaw - rotYawOffset;
				float realRotYaw = MathHelper.wrapDegrees(diff);

				if (realRotYaw < -85.0F)
					realRotYaw = -85.0F;

				if (realRotYaw >= 85.0F)
					realRotYaw = 85.0F;

				rotYawOffset = rotYaw - realRotYaw;

				if (realRotYaw * realRotYaw > 2500.0F)
					rotYawOffset += realRotYaw * 0.2F;
			}

			float rotatePitch = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
			this.renderLivingAt(entity, x, y, z);
			float age = this.handleRotationFloat(entity, partialTicks);
			this.rotateCorpse(entity, age, rotYawOffset, partialTicks);
			GlStateManager.enableRescaleNormal();
			GlStateManager.scale(-1.0F, -1.0F, 1.0F);
			this.preRenderCallback(entity, partialTicks);
//			float f4 = 0.0625F;

			GlStateManager.translate(0.0F, -1.5078125F, 0.0F);//????

			float limbSwingAmount = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
			float limbSwing = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);

			if (entity.isChild())
				limbSwing *= 3.0F;

			if (limbSwingAmount > 1.0F)
				limbSwingAmount = 1.0F;

			GlStateManager.enableAlpha();
			this.mainModel.setLivingAnimations(entity, limbSwing, limbSwingAmount, partialTicks);
			this.mainModel.setRotationAngles(limbSwing, limbSwingAmount, age, diff, rotatePitch, 0.0625F, entity);

			if (this.renderOutlines)
			{
				boolean flag1 = this.setScoreTeamColor(entity);
				this.renderModel(entity, limbSwing, limbSwingAmount, age, diff, rotatePitch, 0.0625F);

				if (flag1)
				{
					this.unsetScoreTeamColor();
				}
			}
			else
			{
				boolean flag = this.setDoRenderBrightness(entity, partialTicks);
				this.renderModel(entity, limbSwing, limbSwingAmount, age, diff, rotatePitch, 0.0625F);

				if (flag)
				{
					this.unsetBrightness();
				}

				GlStateManager.depthMask(true);

				if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator())
					this.renderLayers(entity, limbSwing, limbSwingAmount, partialTicks, age, diff, rotatePitch, 0.0625F);
			}

			GlStateManager.disableRescaleNormal();
		}
		catch (Exception exception)
		{
//			logger.error((String)"Couldn\'t render entity", (Throwable)exception);
		}

		GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GlStateManager.enableTexture2D();
		GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GlStateManager.enableCull();
		GlStateManager.popMatrix();

		if (!this.renderOutlines)
		{
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		}
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.client.event.RenderLivingEvent.Post(entity, this, x, y, z));
	}
}
