package net.infstudio.inspiringworld.magic.repackage.api.simplelib.sync;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.seril.ITagSerializable;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.vars.VarNotify;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.vars.VarRef;

/**
 * @author ci010
 */
public interface VarSync<T> extends VarNotify<T>, VarRef<T>, ITagSerializable
{
}
