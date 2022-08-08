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

@SuppressWarnings({"unchecked", "rawtypes"})
public class GenericMapBlueprint extends Blueprint<Map> {
	private final Supplier1<Map> map;
	
	public GenericMapBlueprint(Supplier1<Map> map) {
		this.map = map;
	}
	
	@Override
	public <F, I> Map decode(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		var map = deserializer.read(key, object);
		
		var newMap = this.map.get();
		
		var exists = deserializer.readBoolean("Exists", map);
		
		if (exists) {
			try {
				var keyClass = Class.forName(deserializer.readString("KeyClass", map));
				var valueClass = Class.forName(deserializer.readString("ValueClass", map));
				
				var keyBlueprint = Blueprint.of(keyClass);
				var valueBlueprint = Blueprint.of(valueClass);
				
				deserializer.readMap(keyBlueprint, valueBlueprint, key, map, newMap::put);
				
				return newMap;
			} catch (Exception e) {
				return newMap;
			}
		} else {
			return newMap;
		}
	}
	
	@Override
	public <F, V> void encode(Serializer<F> serializer, @Nullable String key, V value, F object) {
		var map = serializer.createMap(object);
		
		var valueMap = getter(value);
		
		if (!valueMap.isEmpty()) {
			var entry = (Map.Entry) valueMap.entrySet().stream().findFirst().orElseThrow();
			
			var keyBlueprint = Blueprint.of(entry.getKey());
			var valueBlueprint = Blueprint.of(entry.getValue());
			
			serializer.writeBoolean("Exists", true, map);
			
			serializer.writeString("KeyClass", entry.getKey().getClass().getName(), map);
			serializer.writeString("ValueClass", entry.getValue().getClass().getName(), map);
			
			serializer.writeMap(keyBlueprint, valueBlueprint, key, valueMap, map);
		} else {
			serializer.writeBoolean("Exists", false, map);
		}
		
		serializer.write(key, map, object);
	}
	
	@Override
	public String toString() {
		return "GenericMapNode[" + (key == null ? "None" : key) + "]";
	}
}
