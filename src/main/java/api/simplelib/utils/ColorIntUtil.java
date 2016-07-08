package api.simplelib.utils;

import org.lwjgl.util.vector.Vector4f;

import java.awt.*;
import java.util.Iterator;

import static java.lang.Math.abs;
import static java.lang.Math.max;

/**
 * @author ci010
 * @author Mickey
 */
public class ColorIntUtil
{
	public static int setAlpha(int argb, int alpha)
	{
		return ((alpha & 0xFF) << 24) | (argb & 0xFFFFFF);
	}

	public static int getAlpha(int argb)
	{
		return (argb >> 24) & 0xFF;
	}

	public static int setRed(int argb, int red)
	{
		return ((red & 0xFF) << 16) | (argb & 0xFF00FFFF);
	}

	public static int getRed(int argb)
	{
		return (argb >> 16) & 0xFF;
	}

	public static int setGreen(int argb, int green)
	{
		return ((green & 0xFF) << 8) | (argb & 0xFFFF00FF);
	}

	public static int getGreen(int argb)
	{
		return (argb >> 8) & 0xFF;
	}

	public static int setBlue(int argb, int blue)
	{
		return (blue & 0xFF) | (argb & 0xFFFFFF00);
	}

	public static int getBlue(int argb)
	{
		return (argb) & 0xFF;
	}

	public static int newARGB(int a, int r, int g, int b)
	{
		return ((a & 0xFF) << 24) |
				((r & 0xFF) << 16) |
				((g & 0xFF) << 8) |
				(b & 0xFF);
	}

	public static Vector4f getColorInGLFloat(int rgba)
	{
		return new Vector4f(
				(float) (rgba >> 16 & 255) / 255.0F,
				(float) (rgba >> 8 & 255) / 255.0F,
				(float) (rgba & 255) / 255.0F,
				(float) (rgba >> 24 & 255) / 255.0F);
	}

	public static Color[] argbGradient(final Color start, final Color end, final int steps, final boolean keepA)
	{
		int[] ints = argbIntGradient(start, end, steps, keepA);
		Color[] colors = new Color[ints.length];
		for (int i = 0; i < ints.length; i++)
			colors[i] = new Color(ints[i], true);
		return colors;
	}

	public static int[] argbIntGradient(final Color start, final Color end, final int steps, final boolean keepAlpha)
	{
		return argbIntGradient(start.getRGB(), end.getRed(), steps, keepAlpha);
	}

	public static int[] argbIntGradient(final int start, int end, int steps, boolean keepAlpha)
	{
		int r = getRed(end) - getRed(start);
		int g = getGreen(end) - getGreen(start);
		int b = getBlue(end) - getBlue(start);
		int a = keepAlpha ? 0 : getAlpha(end) - getAlpha(start);

		int nSteps = steps > 0 ? steps : findMax(abs(a), abs(r), abs(g), abs(b), 1), i = 0;

		float rStep = r / (float) nSteps;
		float gStep = g / (float) nSteps;
		float bStep = b / (float) nSteps;
		float aStep = a / (float) nSteps;

		float fr = getRed(start), fg = getGreen(start), fb = getBlue(start), fa = getAlpha(start);

		int[] colors = new int[nSteps];

		while (i++ < nSteps)
		{
			fr += rStep;
			fg += gStep;
			fb += bStep;
			fa += aStep;
			colors[i] = newARGB((int) fa, (int) fr, (int) fg, (int) fb);
		}

		return colors;
	}

	public static int[] argbIntGradient(final int start, int end, boolean keepAlpha)
	{
		return argbIntGradient(start, end, -1, keepAlpha);
	}

	public static int[] argbIntGradient(final int start, int end)
	{
		return argbIntGradient(start, end, -1, true);
	}

	private static int findMax(int... values)
	{
		int i = values[0];
		for (int j = i; j < values.length; j++)
			if (j > i)
				i = j;
		return i;
	}


	public static Iterator<Color> argbStep(final Color start, final Color end, final int steps, final boolean keepA)
	{
		return new Iterator<Color>()
		{
			int r = end.getRed() - start.getRed();
			int g = end.getGreen() - start.getGreen();
			int b = end.getBlue() - start.getBlue();
			int a = keepA ? 0 : end.getAlpha() - start.getAlpha();

			int nSteps = steps > 0 ? steps : max(max(max(abs(r), max(abs(g), abs(b))), abs(a)), 1),
					i = nSteps, directions = -1;

			float rStep = r / (float) nSteps;
			float gStep = g / (float) nSteps;
			float bStep = b / (float) nSteps;
			float aStep = a / (float) nSteps;

			float fr, fg, fb, fa;

			@Override
			public boolean hasNext()
			{
				return true;
			}

			@Override
			public Color next()
			{
				if (++i > nSteps)
				{
					directions *= -1;
					i = 0;
					Color now = directions > 0 ? start : end;
					fr = now.getRed();
					fg = now.getGreen();
					fb = now.getBlue();
					fa = (keepA ? start : now).getAlpha();
				}
				else
				{
					fr += rStep * directions;
					fg += gStep * directions;
					fb += bStep * directions;
					fa += aStep * directions;
				}

				return new Color((int) fr, (int) fg, (int) fb, (int) fa);
			}

			@Override
			public void remove()
			{

			}
		};
	}

	public static Iterator<Color> ahsbStep(final Color start, final Color end, final int steps, final boolean keepS, final boolean keepB, final boolean keepA)
	{
		return new Iterator<Color>()
		{
			float start_hsb[] = new float[4], end_hsb[] = new float[4];

			{
				Color.RGBtoHSB(start.getRed(), start.getGreen(), start.getBlue(), start_hsb);
				Color.RGBtoHSB(end.getRed(), end.getGreen(), end.getBlue(), end_hsb);
				start_hsb[3] = start.getAlpha();
				end_hsb[3] = end.getAlpha();
				if (keepS)
					end_hsb[1] = start_hsb[1];
				if (keepB)
					end_hsb[2] = start_hsb[2];
				if (keepA)
					end_hsb[3] = start_hsb[3];
			}

			float h = end_hsb[0] - start_hsb[0];
			float s = end_hsb[1] - start_hsb[1];
			float b = end_hsb[2] - start_hsb[2];
			float a = end_hsb[3] - start_hsb[3];

			int nSteps = (int) (steps > 0 ? steps : max(max(max(abs(h * 360), max(abs(s * 100), abs(b * 100))), abs(a)), 1)), i = nSteps, directions = -1;

			float hStep = h / (float) nSteps;
			float sStep = s / (float) nSteps;
			float bStep = b / (float) nSteps;
			float aStep = a / (float) nSteps;

			float fh, fs, fb, fa;

			@Override
			public boolean hasNext()
			{
				return true;
			}

			@Override
			public Color next()
			{
				if (++i > nSteps)
				{
					directions *= -1;
					i = 0;
					float now[] = directions > 0 ? start_hsb : end_hsb;
					fh = now[0];
					fs = now[1];
					fb = now[2];
					fa = (directions > 0 ? start : end).getAlpha();
				}
				else
				{
					fh += hStep * directions;
					fs += sStep * directions;
					fb += bStep * directions;
					fa += aStep * directions;
				}

				int rgb = Color.HSBtoRGB(fh, fs, fb);
				return new Color(rgb >> 16 & 0xFF, rgb >> 8 & 0xFF, rgb & 0xFF, (int) fa);
			}

			@Override
			public void remove()
			{

			}
		};
	}
}
