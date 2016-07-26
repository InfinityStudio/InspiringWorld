package net.simplelib.common.registry.component;

import api.simplelib.registry.components.ComponentStruct;
import api.simplelib.utils.NameFormatter;
import net.minecraft.block.Block;
import net.minecraft.item.Item;


/**
 * @author CI010
 */
public abstract class RegComponentBase<T>
{
	private T component;
	private String name, oreName, regName;
	private int creativeTabId;

	public RegComponentBase(String name, T wrap)
	{
		this.name = name;
		this.regName = handleRegisterName(name);
		this.component = wrap;
	}

	protected String handleRegisterName(String name)
	{
		return NameFormatter.upperTo_(name);
	}

	public RegComponentBase<T> setOreName(String name)
	{
		this.oreName = name;
		return this;
	}

	public int getCreativeTabId()
	{
		return creativeTabId;
	}

	public void setCreativeTabId(int id)
	{
		this.creativeTabId = id;
	}

	public T getComponent()
	{
		return component;
	}

	public String getRegisterName()
	{
		return regName;
	}

	public String getOreName()
	{
		return oreName;
	}

	public String getBaseName() {return name;}

	public abstract RegComponentBase<T> register();

	public abstract Runnable getRegClient();

	public static RegComponentBase<?> of(String name, Object o)
	{
		if (o instanceof Block)
			return new RegBlock(name, (Block) o);
		if (o instanceof Item)
			return new RegItem(name, (Item) o);
		if (o.getClass().isAnnotationPresent(ComponentStruct.class))
			return new RegStructReflect<Object>(name, o);
		else return new RegComponentComplete(name, o);
	}
}
