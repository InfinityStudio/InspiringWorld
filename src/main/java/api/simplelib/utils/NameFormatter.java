package api.simplelib.utils;

/**
 * @author ci010
 * @author Mickey
 */
public class NameFormatter
{
	public static String upperTo_(String name)
	{
		StringBuilder builder = new StringBuilder(name);
		for (int i = 0; i < builder.length(); ++i)
			if (Character.isUpperCase(builder.charAt(i)))
			{
				builder.setCharAt(i, Character.toLowerCase(builder.charAt(i)));
				if (i != 0)
					builder.insert(i, "_");
			}
		return builder.toString();
	}

	public static String _toPoint(String name)
	{
		return name.replace("_", ".");
	}

	public static String _toUp(String str)
	{
		int i;
		while ((i = str.indexOf('_')) != -1 && str.length() > i + 2)
			str = str.substring(0, i).concat(str.substring(i + 1, i + 2).toUpperCase()).concat(str.substring(i + 2));
		return str;
	}
}
