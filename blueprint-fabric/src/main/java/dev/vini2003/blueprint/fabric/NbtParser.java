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

import dev.vini2003.blueprint.Blueprint;
import dev.vini2003.blueprint.deserializer.Deserializer;
import dev.vini2003.blueprint.deserializer.Serializer;
import dev.vini2003.blueprint.exception.DeserializerException;
import dev.vini2003.blueprint.exception.SerializerException;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import dev.vini2003.blueprint.consumer.Consumer2;
import dev.vini2003.blueprint.consumer.Consumer1;

public class NbtParser implements Serializer<NbtElement>, Deserializer<NbtElement> {
	public static final NbtParser INSTANCE = new NbtParser();
	
	@Override
	public NbtElement createRoot() {
		return new NbtCompound();
	}
	
	@Override
	public NbtElement createCollection(NbtElement object) {
		return new NbtList();
	}
	
	@Override
	public NbtElement createMap(NbtElement object) {
		return new NbtCompound();
	}
	
	@Override
	public void write(@Nullable String key, NbtElement value, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				if (value instanceof NbtCompound valueNbtCompound) {
					for (var valueKey : valueNbtCompound.getKeys()) {
						nbtCompound.put(valueKey, valueNbtCompound.get(valueKey));
					}
				}
			} else {
				nbtCompound.put(key, value);
			}
		}
	}
	
	@Override
	public void writeBoolean(@Nullable String key, boolean value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtByte.of(value));
		} else {
			if (object instanceof NbtCompound nbtCompound) {
				if (key == null) {
					throw new DeserializerException("Cannot write non-keyed boolean to " + object.getClass().getName());
				}
				
				nbtCompound.putBoolean(key, value);
			} else {
				throw new SerializerException("Cannot write boolean to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeByte(@Nullable String key, byte value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtByte.of(value));
		} else {
			if (object instanceof NbtCompound nbtCompound) {
				if (key == null) {
					throw new SerializerException("Cannot write non-keyed byte to " + object.getClass().getName());
				}
				
				nbtCompound.putByte(key, value);
			} else {
				throw new SerializerException("Cannot write byte to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeShort(@Nullable String key, short value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtShort.of(value));
		} else {
			if (object instanceof NbtCompound nbtCompound) {
				if (key == null) {
					throw new SerializerException("Cannot write non-keyed short to " + object.getClass().getName());
				}
				
				nbtCompound.putShort(key, value);
			} else {
				throw new SerializerException("Cannot write short to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeChar(@Nullable String key, char value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtInt.of(value));
		} else {
			if (object instanceof NbtCompound nbtCompound) {
				if (key == null) {
					throw new SerializerException("Cannot write non-keyed char to " + object.getClass().getName());
				}
				
				nbtCompound.putInt(key, value);
			} else {
				throw new SerializerException("Cannot write char to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeInt(@Nullable String key, int value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtInt.of(value));
		} else {
			if (object instanceof NbtCompound nbtCompound) {
				if (key == null) {
					throw new SerializerException("Cannot write non-keyed int to " + object.getClass().getName());
				}
				
				nbtCompound.putInt(key, value);
			} else {
				throw new SerializerException("Cannot write int to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeLong(@Nullable String key, long value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtLong.of(value));
		} else {
			if (key == null) {
				throw new SerializerException("Cannot write non-keyed long to " + object.getClass().getName());
			}
			
			if (object instanceof NbtCompound nbtCompound) {
				nbtCompound.putLong(key, value);
			} else {
				throw new SerializerException("Cannot write long to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeFloat(@Nullable String key, float value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtFloat.of(value));
		} else {
			if (object instanceof NbtCompound nbtCompound) {
				if (key == null) {
					throw new SerializerException("Cannot write non-keyed float to " + object.getClass().getName());
				}
				
				nbtCompound.putFloat(key, value);
			} else {
				throw new SerializerException("Cannot write float to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeDouble(@Nullable String key, double value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtDouble.of(value));
		} else {
			if (object instanceof NbtCompound nbtCompound) {
				if (key == null) {
					throw new SerializerException("Cannot write non-keyed double to " + object.getClass().getName());
				}
				
				nbtCompound.putDouble(key, value);
			} else {
				throw new SerializerException("Cannot write double to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public void writeString(@Nullable String key, String value, NbtElement object) {
		if (object instanceof NbtList nbtList) {
			nbtList.add(NbtString.of(value));
		} else {
			if (object instanceof NbtCompound nbtCompound) {
				if (key == null) {
					throw new SerializerException("Cannot write non-keyed String to " + object.getClass().getName());
				}
				
				nbtCompound.putString(key, value);
			} else {
				throw new SerializerException("Cannot write string to " + object.getClass().getName());
			}
		}
	}
	
	@Override
	public <K, V, M extends Map<K, V>> void writeMap(Blueprint<K> keyBlueprint, Blueprint<V> valueBlueprint, @Nullable String key, M value, NbtElement object) {
		var mapObject = createMap(object);
		
		if (object instanceof NbtCompound nbtCompound && mapObject instanceof NbtCompound mapNbtCompound) {
			for (var mapEntry : value.entrySet()) {
				var mapKey = mapEntry.getKey();
				var mapValue = mapEntry.getValue();
				
				var mapKeyNbtList = new NbtList();
				var mapValueNbtList = new NbtList();
				
				keyBlueprint.encode(this, null, mapKey, mapKeyNbtList);
				valueBlueprint.encode(this, null, mapValue, mapValueNbtList);
				
				if (key != null) {
					mapNbtCompound.put(mapKeyNbtList.getString(0), mapValueNbtList.get(0));
				} else {
					mapNbtCompound.put(mapKeyNbtList.getString(0), mapValueNbtList.get(0));
				}
			}
			
			if (key != null) {
				nbtCompound.put(key, mapNbtCompound);
			}
		} else {
			throw new SerializerException("Cannot write map to " + object.getClass().getName());
		}
	}
	
	@Override
	public <V, C extends Collection<V>> void writeCollection(Blueprint<V> valueBlueprint, @Nullable String key, C value, NbtElement object) {
		var listObject = createCollection(object);
		
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new SerializerException("Cannot write non-keyed List to " + object.getClass().getName());
			}
			
			for (var listValue : value) {
				valueBlueprint.encode(this, null, listValue, listObject);
			}
			
			nbtCompound.put(key, listObject);
		} else {
			if (object instanceof NbtList nbtList) {
				for (var listValue : value) {
					var listValueNbt = new NbtCompound();
					
					valueBlueprint.encode(this, null, listValue, listValueNbt);
					nbtList.add(listValueNbt);
				}
			}
		}
	}
	
	@Override
	public NbtElement read(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				return object;
			}
			
			return nbtCompound.get(key);
		} else {
			throw new DeserializerException("Cannot read element from " + object.getClass().getName());
		}
	}
	
	@Override
	public boolean readBoolean(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed boolean from " + object.getClass().getName());
			}
			
			return nbtCompound.getBoolean(key);
		} else {
			if (object instanceof NbtByte nbtByte) {
				return nbtByte.byteValue() != 0;
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public byte readByte(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed byte from " + object.getClass().getName());
			}
			
			return nbtCompound.getByte(key);
		} else {
			if (object instanceof NbtByte nbtByte) {
				return nbtByte.byteValue();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public short readShort(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed short from " + object.getClass().getName());
			}
			
			return nbtCompound.getShort(key);
		} else {
			if (object instanceof NbtShort nbtShort) {
				return nbtShort.shortValue();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public char readChar(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed char from " + object.getClass().getName());
			}
			
			return (char) nbtCompound.getInt(key);
		} else {
			if (object instanceof NbtInt nbtInt) {
				return (char) nbtInt.intValue();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public int readInt(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed int from " + object.getClass().getName());
			}
			
			return nbtCompound.getInt(key);
		} else {
			if (object instanceof NbtInt nbtInt) {
				return nbtInt.intValue();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public long readLong(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed long from " + object.getClass().getName());
			}
			
			return nbtCompound.getLong(key);
		} else {
			if (object instanceof NbtLong nbtLong) {
				return nbtLong.longValue();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public float readFloat(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed float from " + object.getClass().getName());
			}
			
			return nbtCompound.getFloat(key);
		} else {
			if (object instanceof NbtFloat nbtFloat) {
				return nbtFloat.floatValue();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public double readDouble(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed double from " + object.getClass().getName());
			}
			
			return nbtCompound.getDouble(key);
		} else {
			if (object instanceof NbtDouble nbtDouble) {
				return nbtDouble.doubleValue();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public String readString(@Nullable String key, NbtElement object) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed String from " + object.getClass().getName());
			}
			
			return nbtCompound.getString(key);
		} else {
			if (object instanceof NbtString nbtString) {
				return nbtString.asString();
			} else {
				throw new DeserializerException();
			}
		}
	}
	
	@Override
	public <K, V> void readMap(Blueprint<K> keyBlueprint, Blueprint<V> valueBlueprint, @Nullable String key, NbtElement object, Consumer2<K, V> mapper) {
		if (object instanceof NbtCompound nbtCompound) {
			var mapNbtCompound = key == null ? nbtCompound : nbtCompound.getCompound(key);
			
			for (var mapKey : mapNbtCompound.getKeys()) {
				var mapValue = mapNbtCompound.get(mapKey);
				
				var keyDeserialized = keyBlueprint.decode(this, null, NbtString.of(mapKey), null);
				var valueDeserialized = valueBlueprint.decode(this, null, mapValue, null);
				
				mapper.accept(keyDeserialized, valueDeserialized);
			}
		} else {
			throw new DeserializerException();
		}
	}
	
	@Override
	public <V> void readCollection(Blueprint<V> valueBlueprint, @Nullable String key, NbtElement object, Consumer1<V> collector) {
		if (object instanceof NbtCompound nbtCompound) {
			if (key == null) {
				throw new DeserializerException("Cannot read non-keyed Collection from " + object.getClass().getName());
			}
			
			var nbtList = (NbtList) nbtCompound.get(key);
			
			for (var listValue : nbtList) {
				var valueDeserialized = valueBlueprint.decode(this, null, listValue, null);
				
				collector.accept(valueDeserialized);
			}
		} else {
			if (object instanceof NbtList nbtList) {
				for (var listValue : nbtList) {
					var valueDeserialized = valueBlueprint.decode(this, null, listValue, null);
					
					collector.accept(valueDeserialized);
				}
			} else {
				throw new DeserializerException();
			}
		}
	}
}
