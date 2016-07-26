package net.simplelib.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ci010
 */
public class ModelGroupEntity extends ModelBase
{
	private Map<String, ModelUnit> renderMap = new LinkedHashMap<String, ModelUnit>(),
			view = Collections.unmodifiableMap(renderMap);
	private DataBlock dataBlock;

	public ModelGroupEntity(DataBlock dataBlock)
	{
		this.dataBlock = dataBlock;
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		dataBlock.updateRotationAngles((EntityLivingBase) entityIn, view, netHeadYaw, headPitch,
				scaleFactor, ageInTicks);
	}

	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime)
	{
		dataBlock.updateAnimationState(entitylivingbaseIn, view, limbSwing, limbSwingAmount, partialTickTime);
	}

	public interface DataBlock
	{
		void updateAnimationState(EntityLivingBase living, Map<String, ModelUnit> renderMap,
								  float limbSwing, float limbSwingAmount, float partialTickTime);

		void updateRotationAngles(EntityLivingBase living, Map<String, ModelUnit> rendererMap,
								  float netHeadYaw, float headPitch, float scaleFactor, float ageInTicks);

		void postRender(EntityLivingBase living, Map<String, ModelUnit> rendererMap);
	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		for (ModelUnit renderer : renderMap.values())
			renderer.render(scale);
		dataBlock.postRender((EntityLivingBase) entityIn, this.view);
	}

	public void setInvisible(boolean invisible)
	{
		for (ModelUnit modelRenderer : renderMap.values())
			modelRenderer.setInvisible(invisible);
	}

}
