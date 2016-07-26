package api.simplelib.registry.components;

import java.lang.annotation.*;

/**
 * The field annotated by this
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value =
		{ElementType.FIELD})
public @interface Construct
{
	Class<?> value();

	@Retention(RetentionPolicy.RUNTIME)
	@Target(value =
			{ElementType.ANNOTATION_TYPE})
	@interface Option
	{
		Class<? extends ArgumentHelper> value();
	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(value =
			{ElementType.FIELD})
	@Construct.Option(FloatHelper.class)
	@interface Float
	{
		float value();
	}

	class FloatHelper implements ArgumentHelper
	{
		@Override
		public Object[] getArguments(Annotation annotation)
		{
			return new Object[]
					{((Float) annotation).value()};
		}
	}
}
