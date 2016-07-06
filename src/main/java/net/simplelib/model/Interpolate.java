package net.simplelib.model;

import org.lwjgl.util.vector.Vector3f;

import java.util.Arrays;

/**
 * @author ci010
 */
public class Interpolate
{
	public static float angle(float v1, float v2, float partialTick)
	{
		float diff;
		for (diff = v2 - v1; diff < -180.0F; diff += 360.0F) ;

		while (diff >= 180.0F)
			diff -= 360.0F;

		return v1 + partialTick * diff;
	}

	public static float distance(float a, float b, float partialTick)
	{
		return a + (b - a) * partialTick;
	}

	public static Vector3f distance(Vector3f a, Vector3f b, float partialTick)
	{
		return add(a, multiply(add(b.negate(null), a), partialTick));
	}

	public static Vector3f[] compileCurve(Vector3f[] control, int frame)
	{
		Vector3f[] r = new Vector3f[frame];
		int realFrame = frame - 1;
		double tick = 1F / (double) (realFrame), current = tick;
		if (control.length > 4)
			for (int i = 1; i < realFrame; i++)
			{
				r[i] = curveGeneral(control, current);
				current += tick;
			}
		else
			for (int i = 1; i < realFrame; i++)
			{
				r[i] = curve(control, current);
				current += tick;
			}
		r[0] = control[0];
		r[realFrame] = control[control.length - 1];
		return r;
	}

	public static Vector3f curveGeneral(Vector3f[] control, double time)
	{
		Vector3f[] store = Arrays.copyOf(control, control.length);
		for (int i = 1; i < control.length; i++)
			for (int j = 0; j < control.length - i; j++)
				store[j] = add(multiply(store[j], (1 - time)), multiply(store[j + 1], time));
		return store[0];
	}

	public static Vector3f curveGeneral(Vector3f[] control, float time)
	{
		Vector3f[] store = Arrays.copyOf(control, control.length);
		int n = control.length - 1;
		for (int k = 1; k < n; k++)
			for (int i = 0; i < n - k; i++)
				store[i] = add(multiply(store[i], (1 - time)), multiply(store[i + 1], time));
		return store[0];
	}

	public static Vector3f curve(Vector3f[] control, float time)
	{
		if (control == null)
			return null;
		switch (control.length)
		{
			case 2:
				return add(multiply(control[0], (1 - time)), multiply(control[1], time));
			case 3:
				float rev = 1 - time;
				return add(
						multiply(control[0], (float) Math.pow(rev, 2)),
						multiply(control[1], 2 * time * rev),
						multiply(control[2], time * time));
			case 4:
				rev = 1 - time;
				return add(
						multiply(control[0], (float) Math.pow(rev, 3)),
						multiply(control[1], (float) Math.pow(rev, 2) * time * 3),
						multiply(control[2], rev * time * time * 3),
						multiply(control[3], time * time * time)
				);
			default:
				return control[0];
		}
	}

	public static Vector3f curve(Vector3f[] control, double time)
	{
		if (control == null)
			return null;
		switch (control.length)
		{
			case 2:
				return add(multiply(control[0], (1 - time)), multiply(control[1], time));
			case 3:
				double rev = 1 - time;
				return add(
						multiply(control[0], (float) Math.pow(rev, 2)),
						multiply(control[1], 2 * time * rev),
						multiply(control[2], time * time));
			case 4:
				rev = 1 - time;
				return add(
						multiply(control[0], (float) Math.pow(rev, 3)),
						multiply(control[1], (float) Math.pow(rev, 2) * time * 3),
						multiply(control[2], rev * time * time * 3),
						multiply(control[3], time * time * time)
				);
			default:
				return control[0];
		}
	}

	public static Vector3f add(Vector3f... v)
	{
		Vector3f sum = new Vector3f();
		for (Vector3f vector3f : v)
			Vector3f.add(sum, vector3f, sum);
		return sum;
	}

	public static Vector3f multiply(Vector3f v, float scale)
	{
		return (Vector3f) new Vector3f(v).scale(scale);
	}

	public static Vector3f multiply(Vector3f v, double scale)
	{
		v = new Vector3f(v);
		v.x *= scale;
		v.y *= scale;
		v.z *= scale;
		return v;
	}
}
