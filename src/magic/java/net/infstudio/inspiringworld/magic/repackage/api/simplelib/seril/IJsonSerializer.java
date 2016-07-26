package net.infstudio.inspiringworld.magic.repackage.api.simplelib.seril;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

/**
 * @author ci010
 */
public interface IJsonSerializer<T> extends JsonSerializer<T>, JsonDeserializer<T>
{
}
