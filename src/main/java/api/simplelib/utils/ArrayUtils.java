package api.simplelib.utils;

/**
 * @author ci010
 */
public class ArrayUtils
{
	public static final Object[] EMPTY_ARR = new Object[0];

	public static <T> T[] emptyArray()
	{
		return TypeUtils.cast(EMPTY_ARR);
	}

	public static int[] ensureCapacity(int[] arr, int length)
	{
		if (arr.length < length)
		{
			int[] newArr = new int[length];
			System.arraycopy(arr, 0, newArr, 0, arr.length);
			return newArr;
		}
		return arr;
	}

	public static <T> T[] ensureCapacity(T[] arr, int length)
	{
		if (arr.length < length)
			return resize(arr, length);
		return arr;
	}

	public static <T> T[] resize(T[] arr, int length)
	{
		T[] newArr = newArrayWithCapacity(length);
		System.arraycopy(arr, 0, newArr, 0, length);
		return newArr;
	}

	public static int[] concat(int[] a, int[] b)
	{
		int[] newArr = new int[a.length + b.length];
		System.arraycopy(a, 0, newArr, 0, a.length);
		System.arraycopy(b, 0, newArr, a.length, b.length);
		return newArr;
	}

	/**
	 * Concat the second array to the first array.
	 *
	 * @param arr  The first array.
	 * @param arr2 The second array.
	 * @param <T>  The type of these array.
	 */
	public static <T> void concat(T[] arr, T[] arr2)
	{
		int oldLength = arr.length;
		int length = oldLength + arr.length;
		arr = TypeUtils.cast(new Object[length]);
		int i;
		for (i = 0; i < oldLength; ++i)
			arr[i] = arr[i];
		for (; i < length; ++i)
			arr[i] = arr2[i - oldLength];
	}

	public static int[] iterate(int from, int to)
	{
		int[] ints = new int[to - from];
		for (int i = 0; i < ints.length; i++)
			ints[i] = i + from;
		return ints;
	}

	/**
	 * Switch two element in an array.
	 *
	 * @param arr    The array will be handled.
	 * @param first  The first element position.
	 * @param second The second element position.
	 * @param <T>    The type of the array.
	 */
	public static <T> void switchElement(T[] arr, int first, int second)
	{
		T temp = arr[first];
		arr[first] = arr[second];
		arr[second] = temp;
	}

	public static <T> T[] newArrayWithCapacity(int size)
	{
		return TypeUtils.cast(new Object[size]);
	}

	public static <T> T[] newArray(T... obj)
	{
		if (obj != null)
		{
			T[] arr = newArrayWithCapacity(obj.length);
			System.arraycopy(obj, 0, arr, 0, obj.length);
			return arr;
		}
		return newArrayWithCapacity(0);
	}
}
