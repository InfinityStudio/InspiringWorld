package net.infstudio.inspiringworld.magic.repackage.net.simplelib.inventory;

import com.google.common.base.Optional;
import com.google.gson.*;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.inventory.*;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.Vector2i;

import java.lang.reflect.Type;

/**
 * @author ci010
 */
public class InventoryToJson
{
//	public static String inventoryToJson(final Inventory inv)
//	{
//		GsonBuilder builder = new GsonBuilder().registerTypeAdapter(Layout.class, new JsonSerializer<Layout>()
//		{
//			@Override
//			public JsonElement serialize(Layout src, PrimitiveType typeOfSrc, JsonSerializationContext context)
//			{
//				JsonArray array = new JsonArray();
//				for (InventoryElement element : inv)
//				{
//					JsonObject object = new JsonObject();
//					object.addProperty("id", element.id());
//					Optional<String> optional = element.name();
//					if (optional.isPresent())
//						object.addProperty("name", optional.get());
//					object.addProperty("type", element instanceof InventorySpace ? "space" : "slot");
//					if (element instanceof InventorySpace)
//					{
//						object.addProperty("xSize", ((InventorySpace) element).xSize());
//						object.addProperty("ySize", ((InventorySpace) element).ySize());
//					}
//					JsonArray pos = new JsonArray();
//					Vector2i srcPos = src.getPos(element.id());
//					pos.add(new JsonPrimitive(srcPos.getX()));
//					pos.add(new JsonPrimitive(srcPos.getY()));
//					object.add("pos", pos);
//					array.add(object);
//				}
//				return array;
//			}
//		});
//		return builder.create().toJson(inv.getLayout());
//	}
//
//	public static void allocLayoutFromJson(final InventoryBuilder builder, String json)
//	{
//		final GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(Layout.class, new JsonDeserializer<Layout>()
//		{
//			@Override
//			public Layout deserialize(JsonElement json, PrimitiveType typeOfT, JsonDeserializationContext context) throws JsonParseException
//			{
//				JsonArray arr = json.getAsJsonArray();
//				for (int i = 0; i < arr.size(); i++)
//				{
//					JsonObject obj = arr.get(i).getAsJsonObject();
//					int id = -1, x, y;
//					String name = null;
//					if (obj.has("id"))
//						id = obj.get("id").getAsInt();
//					if (obj.has("name"))
//						name = obj.get("name").getAsString();
//					if (obj.has("pos"))
//					{
//						JsonArray posArr = obj.get("pos").getAsJsonArray();
//						x = posArr.get(0).getAsInt();
//						y = posArr.get(1).getAsInt();
//					}
//					else
//						throw new IllegalArgumentException();
//					if (id == -1 && name == null)
//						throw new IllegalArgumentException();
//					if (id == -1)
//					{
//						boolean find = false;
//						for (int j = 0; j < builder.currentSize(); j++)
//						{
//							InventoryElement element = builder.getElement(j);
//							Optional<String> stringOptional = element.name();
//							if (stringOptional.isPresent())
//								if (name.equals(stringOptional.get()))
//								{
//									find = true;
//									builder.allocPos(element, x, y);
//								}
//						}
//						if (!find)
//						{
//							// TODO: 2016/5/5  log fatal
//						}
//					}
//					else
//					{
//						InventoryElement element = builder.getElement(id);
//						builder.allocPos(element, x, y);
//						if (name != null)
//							builder.allocName(element, name);
//					}
//				}
//				return null;
//			}
//		});
//		gsonBuilder.create().fromJson(json, Layout.class);
//	}

}
