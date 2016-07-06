package api.simplelib.io;

import com.google.common.base.Predicate;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author ci010
 */
public class MD5Test implements VerifyTest
{
	private String md5;
	private byte[] byteVersion;

	public MD5Test(String md5)
	{
		this.md5 = md5;
	}

	public MD5Test(byte[] md5)
	{
		this.byteVersion = Arrays.copyOf(md5, md5.length);
	}

	@Override
	public boolean apply(@Nullable File input)
	{
		if (input == null)
			return false;
		try
		{
			if (md5 != null)
				return DigestUtils.md5Hex(new FileInputStream(input)).equals(md5);
			if (byteVersion != null)
				return Arrays.equals(DigestUtils.md5(new FileInputStream(input)), byteVersion);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String type()
	{
		return "md5";
	}
}
