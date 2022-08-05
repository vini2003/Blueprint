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

package dev.vini2003.blueprint.generic;

import dev.vini2003.blueprint.Blueprint;
import dev.vini2003.blueprint.deserializer.Deserializer;
import dev.vini2003.blueprint.deserializer.Serializer;
import dev.vini2003.blueprint.supplier.Supplier1;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class GenericMapBlueprint<M extends Map<Object, Object>> extends Blueprint<M> {
	private final Supplier1<M> map;
	
	public GenericMapBlueprint(Supplier1<M> map) {
		this.map = map;
	}
	
	@Override
	public <F, I> M decode(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		var map = this.map.get();
		
		var exists = deserializer.readBoolean(metaDataKey(key) + "$Flag", object);
		
		if (exists) {
			try {
				var keyClass = Class.forName(deserializer.readString(metaDataKey(key) + "$Key", object));
				var valueClass = Class.forName(deserializer.readString(metaDataKey(key) + "$Value", object));
				
				var keyBlueprint = Blueprint.ofClass((Class<Object>) keyClass);
				var valueBlueprint = Blueprint.ofClass((Class<Object>) valueClass);
				
				deserializer.readMap(keyBlueprint, valueBlueprint, key, object, map::put);
				
				return map;
			} catch (Exception e) {
				return map;
			}
		} else {
			return map;
		}
	}
	
	@Override
	public <F, V> void encode(Serializer<F> serializer, @Nullable String key, V value, F object) {
		var map = get(value);
		
		if (!map.isEmpty()) {
			var entry = map.entrySet().stream().findFirst().orElseThrow();
			
			var keyBlueprint = Blueprint.ofValue(entry.getKey());
			var valueBlueprint = Blueprint.ofValue(entry.getValue());
			
			serializer.writeBoolean(metaDataKey(key) + "$Flag", true, object);
			
			serializer.writeString(metaDataKey(key) + "$Key", entry.getKey().getClass().getName(), object);
			serializer.writeString(metaDataKey(key) + "$Value", entry.getValue().getClass().getName(), object);
			
			serializer.writeMap(keyBlueprint, valueBlueprint, key, map, object);
		} else {
			serializer.writeBoolean(metaDataKey(key) + "$Flag", false, object);
		}
	}
	
	private String metaDataKey(String key) {
		return key == null ? "MetaData" : key + "$MetaData";
	}
	
	@Override
	public String toString() {
		return "GenericMapNode[" + (key == null ? "None" : key) + "]";
	}
}
