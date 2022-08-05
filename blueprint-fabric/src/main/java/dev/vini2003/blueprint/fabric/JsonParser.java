/*
 * MIT License
 *
 * Copyright (c) 2020 - 2022 vini2003
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.vini2003.blueprint.fabric;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.vini2003.blueprint.Blueprint;
import dev.vini2003.blueprint.deserializer.Deserializer;
import dev.vini2003.blueprint.deserializer.Serializer;
import dev.vini2003.blueprint.exception.DeserializerException;
import dev.vini2003.blueprint.exception.SerializerException;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import dev.vini2003.blueprint.consumer.Consumer2;
import dev.vini2003.blueprint.consumer.Consumer1;

public class JsonParser implements Serializer<JsonElement>, Deserializer<JsonElement> {
	public static final JsonParser INSTANCE = new JsonParser();
	
	@Override
	public JsonElement createCollection(JsonElement element) {
		return new JsonArray();
	}
	
	@Override
	public JsonElement createMap(JsonElement element) {
		return new JsonObject();
	}
	
	@Override
	public void write(@Nullable String key, JsonElement value, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				if (value instanceof JsonObject valueJsonObject) {
					for (var valueKey : valueJsonObject.keySet()) {
						jsonObject.add(valueKey, valueJsonObject.get(valueKey));
					}
				}
			} else {
				jsonObject.add(key, value);
			}
		}
	}
	
	@Override
	public void writeBoolean(@Nullable String key, boolean value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (object instanceof JsonObject jsonObject) {
				if (key == null) {
					throw new SerializerException("Cannot write non-keyed boolean to " + object.getClass().getName());
				}
				
				jsonObject.addProperty(key, value);
			} else {
				throw new SerializerException("Cannot write boolean to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeByte(@Nullable String key, byte value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (object instanceof JsonObject jsonObject) {
				if (key == null) {
					throw new SerializerException("Cannot write non-keyed byte to " + object.getClass().getName());
				}
				
				jsonObject.addProperty(key, value);
			} else {
				throw new SerializerException("Cannot write byte to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeShort(@Nullable String key, short value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (object instanceof JsonObject jsonObject) {
				if (key == null) {
					throw new SerializerException("Cannot write non-keyed short to " + object.getClass().getName());
				}
				
				jsonObject.addProperty(key, value);
			} else {
				throw new SerializerException("Cannot write short to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeChar(@Nullable String key, char value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (object instanceof JsonObject jsonObject) {
				if (key == null) {
					throw new SerializerException("Cannot write non-keyed char to " + object.getClass().getName());
				}
				
				jsonObject.addProperty(key, value);
			} else {
				throw new SerializerException("Cannot write char to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeInt(@Nullable String key, int value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (object instanceof JsonObject jsonObject) {
				if (key == null) {
					throw new SerializerException("Cannot write non-keyed int to " + object.getClass().getName());
				}
				
				jsonObject.addProperty(key, value);
			} else {
				throw new SerializerException("Cannot write int to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeLong(@Nullable String key, long value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (key == null) {
				throw new SerializerException("Cannot write non-keyed long to " + object.getClass().getName());
			}
			
			if (object instanceof JsonObject jsonObject) {
				jsonObject.addProperty(key, value);
			} else {
				throw new SerializerException("Cannot write long to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeFloat(@Nullable String key, float value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (object instanceof JsonObject jsonObject) {
				if (key == null) {
					throw new SerializerException("Cannot write non-keyed float to " + object.getClass().getName());
				}
				
				jsonObject.addProperty(key, value);
			} else {
				throw new SerializerException("Cannot write float to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeDouble(@Nullable String key, double value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (object instanceof JsonObject jsonObject) {
				if (key == null) {
					throw new SerializerException("Cannot write non-keyed double to " + object.getClass().getName());
				}
				
				jsonObject.addProperty(key, value);
			} else {
				throw new SerializerException("Cannot write double to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeString(@Nullable String key, String value, JsonElement object) {
		if (object instanceof JsonArray jsonArray) {
			jsonArray.add(new JsonPrimitive(value));
		} else {
			if (object instanceof JsonObject jsonObject) {
				if (key == null) {
					throw new SerializerException("Cannot write non-keyed String to " + object.getClass().getName());
				}
				
				jsonObject.addProperty(key, value);
			} else {
				throw new SerializerException("Cannot write string to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public <K, V, M extends Map<K, V>> void writeMap(Blueprint<K> keyBlueprint, Blueprint<V> valueBlueprint, @Nullable String key, M value, JsonElement object) {
		var mapObject = createMap(object);
		
		if (object instanceof JsonObject jsonObject && mapObject instanceof JsonObject mapJsonObject) {
			for (var mapEntry : value.entrySet()) {
				var mapKey = mapEntry.getKey();
				var mapValue = mapEntry.getValue();
				
				var mapKeyJsonArray = new JsonArray();
				var mapValueJsonArray = new JsonArray();
				
				keyBlueprint.encode(this, null, mapKey, mapKeyJsonArray);
				valueBlueprint.encode(this, null, mapValue, mapValueJsonArray);
				
				if (key != null) {
					mapJsonObject.add(mapKeyJsonArray.get(0).getAsString(), mapValueJsonArray.get(0));
				} else {
					jsonObject.add(mapKeyJsonArray.get(0).getAsString(), mapValueJsonArray.get(0));
				}
			}
			
			if (key != null) {
				jsonObject.add(key, mapJsonObject);
			}
		} else {
			throw new SerializerException("Cannot write map to " + object.getClass().getName());
		}
	}
	
	@Override
	public <V, C extends Collection<V>> void writeCollection(Blueprint<V> valueBlueprint, @Nullable String key, C value, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new SerializerException("Cannot write non-keyed Collection to " + object.getClass().getName());
			}
			
			var jsonArray = new JsonArray();
			
			for (var listValue : value) {
				valueBlueprint.encode(this, null, listValue, jsonArray);
			}
			
			jsonObject.add(key, jsonArray);
		} else {
			if (object instanceof JsonArray jsonArray) {
				for (var listValue : value) {
					var listValueJsonObject = new JsonObject();
					
					valueBlueprint.encode(this, null, listValue, listValueJsonObject);
					jsonArray.add(listValueJsonObject);
				}
			}
		}
	}
	
	@Override
	public JsonElement read(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				return object;
			}
			
			return jsonObject.get(key);
		} else {
			throw new DeserializerException("Cannot read element from " + object.getClass().getName());
		}
	}
	
	@Override
	public boolean readBoolean(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed boolean from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsBoolean();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsBoolean();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public byte readByte(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed byte from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsByte();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsByte();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public short readShort(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed short from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsShort();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsShort();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public char readChar(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed char from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsJsonPrimitive().getAsCharacter();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsCharacter();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public int readInt(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed int from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsInt();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsInt();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public long readLong(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed long from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsLong();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsLong();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public float readFloat(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed float from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsFloat();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsFloat();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public double readDouble(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed double from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsDouble();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsDouble();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public String readString(@Nullable String key, JsonElement object) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed String from " + object.getClass().getName());
			}
			
			return jsonObject.get(key).getAsString();
		} else {
			if (object instanceof JsonPrimitive jsonPrimitive) {
				return jsonPrimitive.getAsString();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public <K, V> void readMap(Blueprint<K> keyBlueprint, Blueprint<V> valueBlueprint, @Nullable String key, JsonElement object, Consumer2<K, V> mapper) {
		if (object instanceof JsonObject jsonObject) {
			var mapJsonobject = key == null ? jsonObject : jsonObject.get(key).getAsJsonObject();
			
			for (var mapKey : mapJsonobject.keySet()) {
				try {
					var mapValue = mapJsonobject.get(mapKey);
					
					var keyDeserialized = keyBlueprint.decode(this, null, new JsonPrimitive(mapKey), null);
					var valueDeserialized = valueBlueprint.decode(this, null, mapValue, null);
					
					mapper.accept(keyDeserialized, valueDeserialized);
				} catch (Exception ignored) {}
			}
		} else {
			throw new DeserializerException();
		}
	}
	
	@Override
	public <V> void readCollection(Blueprint<V> valueBlueprint, @Nullable String key, JsonElement object, Consumer1<V> collector) {
		if (object instanceof JsonObject jsonObject) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed List from " + object.getClass().getName());
			}
			
			var jsonArray = jsonObject.get(key).getAsJsonArray();
			
			for (var jsonValue : jsonArray) {
				try {
					var valueDeserialized = valueBlueprint.decode(this, null, jsonValue, null);
					
					collector.accept(valueDeserialized);
				} catch (Exception ignored) {}
			}
		} else {
			if (object instanceof JsonArray jsonArray) {
				for (var jsonValue : jsonArray) {
					var valueDeserialized = valueBlueprint.decode(this, null, jsonValue, null);
					
					collector.accept(valueDeserialized);
				}
			} else {
				throw new DeserializerException();
			}
		}
	}
}
