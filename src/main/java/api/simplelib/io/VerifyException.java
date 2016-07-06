package api.simplelib.io;

/**
 * @author ci010
 */
public class VerifyException extends Exception
{
	public final String type;

	public VerifyException(VerifyTest test)
	{
		super("Cannot pass the verify test " + test.type());
		this.type = test.type();
	}

	public VerifyException(String type, String message)
	{
		super(message);
		this.type = type;
	}
}
