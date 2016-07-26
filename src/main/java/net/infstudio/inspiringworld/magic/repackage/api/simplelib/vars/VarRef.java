package net.infstudio.inspiringworld.magic.repackage.api.simplelib.vars;

/**
 * @author ci010
 */
public interface VarRef<T> extends Var<T>
{
	boolean isPresent();

	String name();
}
