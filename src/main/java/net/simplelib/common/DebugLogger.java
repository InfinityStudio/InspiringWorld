package net.simplelib.common;

import api.simplelib.utils.FileReference;
import api.simplelib.utils.Environment;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.MessageFactory;

import java.io.*;

/**
 * @author ci010
 */
public class DebugLogger
{
	private static FileOutputStream stream;
	private static Logger logger = LogManager.getLogger();
	private static MessageFactory factory = logger.getMessageFactory();
//	private static final String FQCN = AbstractLogger.class.getName();

	public static void info(String message, Object... obj)
	{
		String msg = factory.newMessage(message, obj).getFormattedMessage();
		if (Environment.debug())
			logger.log(Level.INFO, msg);
		try
		{
			stream.write(msg.concat("\n").getBytes());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void trace(String message, Object... obj)
	{
		logger.trace(message, obj);
	}

	public static void fatal(String message, Object... obj)
	{
		logger.fatal(message, obj);
	}

	public static void error(String message, Object... obj)
	{
		logger.error(message, obj);
	}

	public static void warn(String message, Object... obj)
	{
		logger.warn(message, obj);
	}

	static
	{
		try
		{
			File log = new File(FileReference.getLog(), "debug.log");
			if (!log.exists())
				log.createNewFile();
			stream = new FileOutputStream(log);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
