package net.simplelib.common.registry;

import api.simplelib.Instance;
import api.simplelib.LoadingDelegate;
import api.simplelib.lang.Local;
import api.simplelib.registry.ASMRegistryDelegate;
import api.simplelib.registry.ModConfig;
import api.simplelib.utils.FinalFieldUtils;
import api.simplelib.utils.PrimitiveType;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author ci010
 */
@LoadingDelegate
public class ConfigDelegate extends ASMRegistryDelegate<ModConfig>
{
	@SubscribeEvent
	public void init(FMLPreInitializationEvent event)
	{
		Configuration cfg = new Configuration(event.getModConfigurationDirectory(), this.getModid());
		ModConfig modConfig = this.getAnnotation();

		Class<?> type;
		Optional<Field> f = this.getField();
		Field field;
		Object o = null;
		if (f.isPresent())
			type = (field = f.get()).getType();
		else throw new IllegalArgumentException();
		if (!Modifier.isStatic(field.getModifiers()))
		{
			Optional<?> optional = Instance.Utils.grabAll(getAnnotatedClass());
			if (optional.isPresent())
				o = optional.get();
		}
		if (type.isArray())
			handleArr(cfg, modConfig, field, type, o);
		else
			handleCommon(cfg, modConfig, field, type, o);
	}

	private void handleArr(Configuration cfg, ModConfig modConfig, Field f, Class<?> type, Object o)
	{
		type = type.getComponentType();
		PrimitiveType primitiveType = PrimitiveType.ofUnsafe(type);
		Property.Type propType = primitiveType != null ? this.parseType(primitiveType) : type == String.class ? Property.Type
				.STRING : null;
		if (propType == null)
			throw new IllegalArgumentException();
		int maxLength;
		String comment = parseComment(modConfig);
		Optional<Object> object = this.getObject();
		String[] defaultValue = null;
		if (object.isPresent())
		{
			defaultValue = (String[]) object.get();
			maxLength = defaultValue.length;
		}
		else
		{
			ModConfig.Length annotation = f.getAnnotation(ModConfig.Length.class);
			if (annotation == null)
			{
				throw new IllegalArgumentException();
			}
			maxLength = annotation.maxLength();
		}
		if (defaultValue == null)
			defaultValue = new String[maxLength];
		Property property = cfg.get(modConfig.categoryId(), modConfig.id(), defaultValue, comment, propType);
		parseMaxMin(propType, f, property);
		parseOther(property, modConfig);
		Object v = null;
		switch (primitiveType)
		{
			case BOOL:
				v = property.getBooleanList();
				break;
			case BYTE:
			case SHORT:
			case INT:
			case LONG:
				v = property.getIntList();
				break;
			case FLOAT:
			case DOUBLE:
				v = property.getDoubleList();
				break;
		}
		int modifiers = f.getModifiers();
		if (Modifier.isFinal(modifiers))
			try
			{
				FinalFieldUtils.INSTANCE.set(o, f, v);
				return;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		if (Modifier.isPrivate(modifiers))
			if (f.isAccessible())
				f.setAccessible(true);
		try
		{
			f.set(o, v);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	private void handleCommon(Configuration cfg, ModConfig modConfig, Field f, Class<?> type, Object o)
	{
		String comment = parseComment(modConfig);
		Optional<Object> object = this.getObject();
		Object defaultValue = null;
		if (object.isPresent())
			defaultValue = object.get();
		PrimitiveType primitiveType = PrimitiveType.ofUnsafe(type);
		Property.Type propType = primitiveType != null ? this.parseType(primitiveType) : type == String.class ?
				Property.Type.STRING : null;
		if (propType == null)
			throw new IllegalArgumentException();

		if (defaultValue == null)
			switch (propType)
			{
				case STRING:
					defaultValue = "";
					break;
				case MOD_ID:
				case INTEGER:
				case COLOR:
					defaultValue = 0;
					break;
				case BOOLEAN:
					defaultValue = false;
					break;
				case DOUBLE:
					defaultValue = 0.0D;
					break;
			}
		Property property = cfg.get(modConfig.categoryId(), modConfig.id(), defaultValue.toString(),
				comment, propType);
		parseMaxMin(propType, f, property);
		parseOther(property, modConfig);
		Object v = null;
		if (propType == Property.Type.STRING)
			v = property.getString();
		else
			switch (primitiveType)
			{
				case BOOL:
					v = property.getBoolean();
					break;
				case BYTE:
					v = (byte) property.getInt();
					break;
				case SHORT:
					v = (short) property.getInt();
					break;
				case INT:
					v = property.getInt();
					break;
				case LONG:
					v = (long) property.getInt();
					break;
				case FLOAT:
					v = (float) property.getDouble();
					break;
				case DOUBLE:
					v = property.getDouble();
					break;
			}
		int modifiers = f.getModifiers();
		if (Modifier.isFinal(modifiers))
			try
			{
				FinalFieldUtils.INSTANCE.set(o, f, v);
				return;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		if (Modifier.isPrivate(modifiers))
			if (f.isAccessible())
				f.setAccessible(true);
		try
		{
			f.set(o, v);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	private Property.Type parseType(PrimitiveType primitiveType)
	{
		Property.Type propType = null;
		switch (primitiveType)
		{
			case BOOL:
				propType = Property.Type.BOOLEAN;
				break;
			case BYTE:
			case SHORT:
			case INT:
			case LONG:
				propType = Property.Type.INTEGER;
				break;
			case FLOAT:
			case DOUBLE:
				propType = Property.Type.DOUBLE;
				break;
		}
		return propType;
	}

	private void parseMaxMin(Property.Type propType, Field f, Property property)
	{
		switch (propType)
		{
			case INTEGER:
				ModConfig.ValidRange annotation = f.getAnnotation(ModConfig.ValidRange.class);
				if (annotation != null)
				{
					String[] range = annotation.range();
					if (range.length < 2)
						return;
					int max = Integer.valueOf(range[0]), min = max;
					for (int i = 1, v = Integer.valueOf(range[i]); i < range.length; v = Integer.valueOf(range[i++]))
						if (v > max)
							max = v;
						else if (v < min)
							min = v;
					property.setMaxValue(max);
					property.setMinValue(min);
				}
				break;
			case DOUBLE:
				annotation = f.getAnnotation(ModConfig.ValidRange.class);
				if (annotation != null)
				{
					String[] range = annotation.range();
					if (range.length < 2)
						return;
					double max = Double.valueOf(range[0]), min = max, v;
					for (int i = 1; i < range.length; i++)
					{
						v = Double.valueOf(range[i]);
						if (v > max)
							max = v;
						else if (v < min)
							min = v;
					}
					property.setMaxValue(max);
					property.setMinValue(min);
				}
				break;
		}
	}

	private void parseOther(Property property, ModConfig modConfig)
	{
		property.setRequiresMcRestart(modConfig.requireMCRestart());
		property.setRequiresWorldRestart(modConfig.requireWorldRestart());
		property.setShowInGui(modConfig.showInGui());
	}

	private String parseComment(ModConfig modConfig)
	{
		String comment = modConfig.comment();
		if (comment.equals(""))
		{
			String unloc = Joiner.on('.').join("cfg", modConfig.categoryId(), modConfig.id());
			comment = Local.trans(unloc);
			if (comment.equals(unloc))
				comment = null;
		}
		return comment;
	}
}
