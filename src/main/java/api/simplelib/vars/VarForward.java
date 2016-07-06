package api.simplelib.vars;

/**
 * @author ci010
 */
public interface VarForward<T> extends VarRef<T>
{
	void delegate(Var<T> var);

	Var<T> delegate();
}
