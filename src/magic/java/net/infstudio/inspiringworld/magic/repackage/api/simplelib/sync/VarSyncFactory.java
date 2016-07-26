package net.infstudio.inspiringworld.magic.repackage.api.simplelib.sync;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.capabilities.CapabilityBuilder;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.sync.Attributes;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.vars.Var;

/**
 * @author ci010
 */
@CapabilityBuilder(Attributes.class)
public interface VarSyncFactory
{
	Var<Integer> newInteger(String name, int i);

	Var<Integer> newInteger(String name, int i, int min, int max);

	Var<Float> newFloat(String name, float f);

	Var<Float> newFloat(String name, float f, float min, float max);

	Var<Short> newShort(String name, short s);

	Var<Short> newShort(String name, short s, short min, short max);

	Var<Byte> newByte(String name, byte b);

	Var<Byte> newByte(String name, byte b, byte min, byte max);

	Var<Double> newDouble(String name, double d);

	Var<Double> newDouble(String name, double d, double min, double max);

	Var<String> newString(String name, String s);

	<T extends Enum<T>> Var<T> newEnum(String name, T e, Class<T> enumClass);
}
