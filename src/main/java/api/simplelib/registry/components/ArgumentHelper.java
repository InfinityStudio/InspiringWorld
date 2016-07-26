package api.simplelib.registry.components;

import net.simplelib.common.registry.component.ReflectionAnnotatedMaker;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * The interface that inject the annotation data.
 * <p/>
 * See {@link ReflectionAnnotatedMaker#apply(Field)}
 *
 * @author CI010
 */
public interface ArgumentHelper
{
	/**
	 * Get all the data needed to construct from annotation.
	 *
	 * @param annotation
	 * @return All the arguments needed to construct.
	 */
	Object[] getArguments(Annotation annotation);
}
