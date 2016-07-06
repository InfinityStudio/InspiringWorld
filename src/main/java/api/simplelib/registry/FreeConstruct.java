package api.simplelib.registry;

import java.lang.annotation.*;

/**
 * This annotation indicates that this class/interface should have a public non-parameter constructor.
 *
 * @author ci010
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.SOURCE)
@Inherited
public @interface FreeConstruct
{
}
