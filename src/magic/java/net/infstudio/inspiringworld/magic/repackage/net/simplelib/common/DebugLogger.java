package net.infstudio.inspiringworld.magic.repackage.net.simplelib.common;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.spi.LoggerContext;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.Environment;

import java.util.Map;

/**
 * @author ci010
 */
public class DebugLogger
{
	private static Logger logger = LogManager.getLogger();
	private static MessageFactory factory = logger.getMessageFactory();
//	private static final String FQCN = AbstractLogger.class.getName();

	static
	{
		LoggerContext context = LogManager.getContext(DebugLogger.class.getClassLoader(), true);
		org.apache.logging.log4j.core.LoggerContext real = (org.apache.logging.log4j.core.LoggerContext) context;
//		real.getConfiguration()
		Configuration cfg = real.getConfiguration();
		System.out.println(real.getConfigLocation());
		System.out.println(cfg);
		RollingRandomAccessFileAppender file = (RollingRandomAccessFileAppender) cfg.getAppenders().get("File");
		for (Map.Entry<String, Appender> stringAppenderEntry : cfg.getAppenders().entrySet())
		{
			System.out.println(stringAppenderEntry.getKey()+ " " + stringAppenderEntry.getValue().getClass());
		}
//		FileAppender.createAppender("test","","","test",null,null.);
//		FileAppender.createAppender("logs/test.log","true")
		System.out.println(context);
	}

	public static void info(String message, Object... obj)
	{
		String msg = factory.newMessage(message, obj).getFormattedMessage();
		if (Environment.debug())
			logger.log(Level.INFO, msg);
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

}
