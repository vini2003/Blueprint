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
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.*;
import dev.vini2003.blueprint.consumer.Consumer2;
import dev.vini2003.blueprint.consumer.Consumer1;

public class BufParser implements Serializer<FriendlyByteBuf>, Deserializer<FriendlyByteBuf> {
	public static final BufParser INSTANCE = new BufParser();
	
	@Override
	public FriendlyByteBuf createRoot() {
		return new FriendlyByteBuf(Unpooled.buffer());
	}
	
	@Override
	public FriendlyByteBuf createCollection(FriendlyByteBuf object) {
		return object;
	}
	
	@Override
	public FriendlyByteBuf createMap(FriendlyByteBuf object) {
		return object;
	}
	
	@Override
	public void write(@Nullable String key, FriendlyByteBuf value, FriendlyByteBuf object) {
		return;
	}
	
	@Override
	public void writeBoolean(@Nullable String key, boolean value, FriendlyByteBuf object) {
		object.writeBoolean(value);
	}
	
	@Override
	public void writeByte(@Nullable String key, byte value, FriendlyByteBuf object) {
		object.writeByte(value);
	}
	
	@Override
	public void writeShort(@Nullable String key, short value, FriendlyByteBuf object) {
		object.writeShort(value);
	}
	
	@Override
	public void writeChar(@Nullable String key, char value, FriendlyByteBuf object) {
		object.writeChar(value);
	}
	
	@Override
	public void writeInt(@Nullable String key, int value, FriendlyByteBuf object) {
		object.writeInt(value);
	}
	
	@Override
	public void writeLong(@Nullable String key, long value, FriendlyByteBuf object) {
		object.writeLong(value);
	}
	
	@Override
	public void writeFloat(@Nullable String key, float value, FriendlyByteBuf object) {
		object.writeFloat(value);
	}
	
	@Override
	public void writeDouble(@Nullable String key, double value, FriendlyByteBuf object) {
		object.writeDouble(value);
	}
	
	@Override
	public void writeString(@Nullable String key, String value, FriendlyByteBuf object) {
		object.writeInt(value.length());
		object.writeBytes(value.getBytes(StandardCharsets.UTF_8));
	}
	
	@Override
	public <K, V, M extends Map<K, V>> void writeMap(Blueprint<K> keyBlueprint, Blueprint<V> valueBlueprint, @Nullable String key, M value, FriendlyByteBuf object) {
		var mapObject = createMap(object);
		
		mapObject.writeInt(value.size());
		
		for (var entry : value.entrySet()) {
			keyBlueprint.encode(this, null, entry.getKey(), mapObject);
			valueBlueprint.encode(this, null, entry.getValue(), mapObject);
		}
	}
	
	@Override
	public <V, C extends Collection<V>> void writeCollection(Blueprint<V> valueBlueprint, @Nullable String key, C value, FriendlyByteBuf object) {
		var listObject = createCollection(object);
		
		listObject.writeInt(value.size());
		
		for (var entry : value) {
			valueBlueprint.encode(this, null, entry, listObject);
		}
	}
	
	@Override
	public FriendlyByteBuf read(@Nullable String key, FriendlyByteBuf object) {
		return object;
	}
	
	@Override
	public boolean readBoolean(@Nullable String key, FriendlyByteBuf object) {
		return object.readBoolean();
	}
	
	@Override
	public byte readByte(@Nullable String key, FriendlyByteBuf object) {
		return object.readByte();
	}
	
	@Override
	public short readShort(@Nullable String key, FriendlyByteBuf object) {
		return object.readShort();
	}
	
	@Override
	public char readChar(@Nullable String key, FriendlyByteBuf object) {
		return object.readChar();
	}
	
	@Override
	public int readInt(@Nullable String key, FriendlyByteBuf object) {
		return object.readInt();
	}
	
	@Override
	public long readLong(@Nullable String key, FriendlyByteBuf object) {
		return object.readLong();
	}
	
	@Override
	public float readFloat(@Nullable String key, FriendlyByteBuf object) {
		return object.readFloat();
	}
	
	@Override
	public double readDouble(@Nullable String key, FriendlyByteBuf object) {
		return object.readDouble();
	}
	
	@Override
	public String readString(@Nullable String key, FriendlyByteBuf object) {
		return object.toString(object.readerIndex(), object.readInt(), StandardCharsets.UTF_8);
	}
	
	@Override
	public <K, V> void readMap(Blueprint<K> keyBlueprint, Blueprint<V> valueBlueprint, @Nullable String key, FriendlyByteBuf object, Consumer2<K, V> mapper) {
		var size = object.readInt();
		
		for (var i = 0; i < size; i++) {
			var mapKey = keyBlueprint.decode(this, null, object, null);
			var mapValue = valueBlueprint.decode(this, null, object, null);
			
			mapper.accept(mapKey, mapValue);
		}
	}
	
	@Override
	public <V> void readCollection(Blueprint<V> valueBlueprint, @Nullable String key, FriendlyByteBuf object, Consumer1<V> collector) {
		var size = object.readInt();
		
		for (var i = 0; i < size; i++) {
			var collectionValue = valueBlueprint.decode(this, null, object, null);
			
			collector.accept(collectionValue);
		}
	}
}
