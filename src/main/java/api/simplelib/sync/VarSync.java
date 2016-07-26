package api.simplelib.sync;

import api.simplelib.seril.ITagSerializable;
import api.simplelib.vars.VarNotify;
import api.simplelib.vars.VarRef;

/**
 * @author ci010
 */
public interface VarSync<T> extends VarNotify<T>, VarRef<T>, ITagSerializable
{
}
