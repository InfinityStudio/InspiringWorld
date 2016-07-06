package api.simplelib.seril;

import com.google.gson.JsonObject;

/**
 * @author ci010
 */
public interface IJsonSerializable
{
	void toJson(JsonObject object);

	void fromJson(JsonObject object);
}
