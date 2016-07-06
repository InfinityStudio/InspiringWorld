package api.simplelib.registry;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import api.simplelib.utils.ASMDataUtil;
import api.simplelib.utils.TypeUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Set;

/**
 * @author ci010
 */
public abstract class ASMRegistryDelegate<T extends Annotation>
{
	protected Set<ASMCache> cache = Sets.newHashSet();
	private Iterator<ASMCache> itr;
	private ASMCache current;

	private class ASMCache
	{
		Class<?> clz;
		String modid;
		T annotation;
		Optional<Object> target;
		Optional<Field> field;

		public ASMCache(Class<?> clz, String modid, T annotation, Optional<Object> target, Optional<Field> field)
		{
			this.clz = clz;
			this.modid = modid;
			this.annotation = annotation;
			this.target = target;
			this.field = field;
		}
	}

	public boolean hasNext()
	{
		if (this.itr == null)
			this.itr = cache.iterator();
		return itr.hasNext();
	}

	public void next()
	{
		if (this.itr == null)
			this.itr = cache.iterator();
		this.current = itr.next();
	}

	protected Class<?> getAnnotatedClass()
	{
		return current.clz;
	}

	protected String getModid()
	{
		return current.modid;
	}

	protected T getAnnotation()
	{
		return this.current.annotation;
	}

	protected Optional<Object> getObject()
	{
		return this.current.target;
	}

	protected Optional<Field> getField() {return this.current.field;}

	public final void addCache(String modid, ASMDataTable.ASMData data)
	{
		if (modid == null)
			modid = ASMDataUtil.getModId(data);
		Class<?> clz = ASMDataUtil.getClass(data);
		Class<? extends T> type = TypeUtils.getGenericTypeTo(this);
		T annotation = ASMDataUtil.getAnnotation(data, type);
		Optional<Field> f = Optional.fromNullable(ASMDataUtil.getField(data));
		Optional<Object> o = ASMDataUtil.getObject(data);
		cache.add(new ASMCache(clz, modid, annotation, o, f));
	}
}
