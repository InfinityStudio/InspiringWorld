package net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.registry.component;

import com.google.common.collect.Lists;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.ComponentStruct;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.ModCreativeTab;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.OreDic;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.DebugLogger;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author ci010
 */
public class RegStructReflect<T> extends RegComponentBase<T>
{
	private List<RegItem> items;
	private List<RegBlock> blocks;

	public RegStructReflect(String name, T wrap)
	{
		super(name, wrap);
		this.items = Lists.newArrayList();
		this.blocks = Lists.newArrayList();
		Class<?> clz = wrap.getClass();
		try
		{
			this.discover(this.getRegisterName(), wrap, clz);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}

	private void discover(String parent, Object o, Class<?> clz) throws IllegalAccessException
	{
		Field[] declaredFields = clz.getDeclaredFields();
		for (Field f : declaredFields)
		{
			Class<?> type = f.getType();
			if (!f.isAccessible())
				f.setAccessible(true);
			Object fieldObj = f.get(o);
			RegComponentBase reg = null;
			if (fieldObj == null)
			{
				DebugLogger.warn("found the field " + f.getName() + " in " + clz + " is null. So, it will not be " +
						"registered in ComponentStruct!");
				continue;
			}
			if (Block.class.isAssignableFrom(type))
				blocks.add((RegBlock) (reg = new RegBlock(parent + "." + f.getName(), (Block) fieldObj)));
			else if (Item.class.isAssignableFrom(type))
				items.add((RegItem) (reg = new RegItem(parent + "." + f.getName(), (Item) fieldObj)));
			else if (type.isAnnotationPresent(ComponentStruct.class))
				discover(parent + "." + f.getName(), fieldObj, type);
			else
			{
				DebugLogger.warn("Skipping the field {} in {} since it is not a Block/Item/ComponentStruct!",
						f.getName(), clz);
				continue;
			}
			if (f.isAnnotationPresent(OreDic.class))
				reg.setOreName(f.getAnnotation(OreDic.class).value());
			if (f.isAnnotationPresent(ModCreativeTab.class))
				reg.setCreativeTabId(f.getAnnotation(ModCreativeTab.class).value());
		}

		Class<?> superclass = clz.getSuperclass();
		if (superclass.isAnnotationPresent(ComponentStruct.class))
			discover(parent, o, superclass);
	}

	@Override
	public RegComponentBase<T> register()
	{
		for (RegItem item : items) item.register();
		for (RegBlock block : blocks) block.register();
		return this;
	}

	@Override
	public Runnable getRegClient()
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
				for (RegItem item : items) item.getRegClient();
				for (RegBlock block : blocks) block.getRegClient();
			}
		};
	}
}
