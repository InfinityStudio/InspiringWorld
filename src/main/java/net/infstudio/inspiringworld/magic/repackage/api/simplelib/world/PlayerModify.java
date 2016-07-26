package net.infstudio.inspiringworld.magic.repackage.api.simplelib.world;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import static net.minecraftforge.fml.relauncher.ReflectionHelper.getPrivateValue;
import static net.minecraftforge.fml.relauncher.ReflectionHelper.setPrivateValue;

/**
 * This class contains several helper methods to modify the basic ability of player.
 *
 * @author ci010
 */
public class PlayerModify
{
	/**
	 * @param player The player will be modified.
	 * @param factor The value of factor.
	 * @param type   The type of modification.
	 */
	public static void modifyMaxHealth(EntityPlayer player, double factor, Type type)
	{
		modifyShareAttr(SharedMonsterAttributes.MAX_HEALTH, "mod_health", player, factor, type);
	}

	public static double getMaxHealth(EntityPlayer player)
	{
		return player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
	}

	/**
	 * @param player The player will be modified.
	 * @param factor The value of factor.
	 * @param type   The type of modification.
	 */
	public static void modifyAttack(EntityPlayer player, double factor, Type type)
	{
		modifyShareAttr(SharedMonsterAttributes.ATTACK_DAMAGE, "mod_attack", player, factor, type);
	}

	public static double getAttack(EntityPlayer player)
	{
		return player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
	}

	/**
	 * @param player The player will be modified.
	 * @param factor The value of factor.
	 * @param type   The type of modification.
	 */
	public static void modifyMoveSpeed(EntityPlayer player, double factor, Type type)
	{
		modifyShareAttr(SharedMonsterAttributes.MOVEMENT_SPEED, "mod_speed", player, factor, type);
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		player.capabilities.writeCapabilitiesToNBT(nbtTagCompound);
		switch (type)
		{
			case set:
				nbtTagCompound.setFloat("walkSpeed", (float) factor);
				break;
			case add:
				nbtTagCompound.setFloat("walkSpeed", (float) (factor + nbtTagCompound.getFloat("walkSpeed")));
				break;
			case multiply:
				nbtTagCompound.setFloat("walkSpeed", (float) (factor * nbtTagCompound.getFloat("walkSpeed")));
				break;
			case scaleUp:
				nbtTagCompound.setFloat("walkSpeed", (float) ((factor + 1) * nbtTagCompound.getFloat("walkSpeed")));
				break;
		}
		player.capabilities.readCapabilitiesFromNBT(nbtTagCompound);
	}

	/**
	 * @param player The player will be modified.
	 * @param factor The value of factor.
	 * @param type   The type of modification.
	 */
	public static void modifyJumpSpeed(EntityPlayer player, double factor, Type type)
	{
		switch (type)
		{
			case set:
				//TODO warn
				setPrivateValue(EntityPlayer.class, player, factor, "speedInAir");
				break;
			case add:
				setPrivateValue(EntityPlayer.class, player, (Float) getPrivateValue(EntityPlayer.class, player,
						"speedInAir")
						+ factor, "speedInAir");
				break;
			case multiply:
				setPrivateValue(EntityPlayer.class, player, (Float) getPrivateValue(EntityPlayer.class, player,
						"speedInAir")
						* factor, "speedInAir");
				break;
			case scaleUp:
				setPrivateValue(EntityPlayer.class, player, (Float) getPrivateValue(EntityPlayer.class, player,
						"speedInAir")
						* (1 + factor), "speedInAir");
				break;
		}
	}


	private static void modifyShareAttr(IAttribute attribute, String id, EntityPlayer player, double factor, Type type)
	{
		IAttributeInstance attr = player.getEntityAttribute(attribute);
		AttributeModifier mod;
		if (type == Type.set)
			mod = new AttributeModifier(id, attr.getAttributeValue() - factor, Type.add.operation);
		else
			mod = new AttributeModifier(id, factor, type.operation);
		attr.applyModifier(mod);
	}

	/**
	 * Several modification type.
	 * <p>See the enum to get more specific information.</>
	 */
	public enum Type
	{
		set(-1),
		/**
		 * Directly add to the original value.
		 * <p/>
		 * value = value + factor
		 */
		add(0),
		/**
		 * Multiply the original value by the factor.
		 * <p/>
		 * value = value * factor
		 */
		multiply(1),
		/**
		 * Scale the original value up by percentage.
		 * <p/>
		 * e.g. value = value * (1 + factor)
		 */
		scaleUp(2);

		int operation;

		Type(int operation)
		{
			this.operation = operation;
		}
	}
}
