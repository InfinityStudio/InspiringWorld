package net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.registry.component.ReflectionAnnotatedMaker;

/**
 * The interface that inject the annotation data.
 * See {@link ReflectionAnnotatedMaker#apply(Field)}
 *
 * @author CI010
 */
public interface ArgumentHelper {
    /**
     * Get all the data needed to construct from annotation.
     *
     * @param annotation
     * @return All the arguments needed to construct.
     */
    Object[] getArguments(Annotation annotation);
}
