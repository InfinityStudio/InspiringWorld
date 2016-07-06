package api.simplelib.registry;

public interface ModelHandler<Type>
{
	boolean handle(Type target);
}
