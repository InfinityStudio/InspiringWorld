package api.simplelib.io;

import com.google.common.base.Predicate;

import java.io.File;

/**
 * @author ci010
 */
public interface VerifyTest extends Predicate<File>
{
	String type();
}
