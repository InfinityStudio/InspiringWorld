package api.simplelib.seril;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

/**
 * @author ci010
 */
public interface ISerializer<T> extends ITagSerializer<T>, JsonSerializer<T>, JsonDeserializer<T>
{}
