package net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils;

/**
 * @author ci010
 */
public class StringSource implements CharSequence
{
	public static final Handler DEFAULT = new Handler()
	{
		@Override
		public Object[] handle(Source source)
		{
			if (source != null)
				return source.getSource();
			else
				return ArrayUtils.EMPTY_ARR;
		}
	};
	private String rawContent;
	protected Source source;
	protected Handler handler = DEFAULT;

	public StringSource(String rawContent)
	{
		this.rawContent = rawContent;
	}

	public StringSource(String rawContent, Handler handler)
	{
		this.rawContent = rawContent;
		this.handler = handler;
	}

	public StringSource setSource(Source source)
	{
		this.source = source;
		return this;
	}

	public Source source()
	{
		return source;
	}

	public interface Source
	{
		Object[] getSource();
	}

	public interface Handler
	{
		Object[] handle(Source source);
	}

	@Override
	public int length()
	{
		return rawContent.length();
	}

	@Override
	public char charAt(int index)
	{
		return rawContent.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end)
	{
		return rawContent.subSequence(start, end);
	}

	public String getRawContent()
	{
		return rawContent;
	}

	protected Object getSource()
	{
		return handler.handle(this.source);
	}

	@Override
	public String toString()
	{
		return String.format(rawContent, getSource());
	}
}
