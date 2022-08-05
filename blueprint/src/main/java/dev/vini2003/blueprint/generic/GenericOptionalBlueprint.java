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

import java.util.Collection;
import java.util.Optional;

public class GenericOptionalBlueprint<O extends Optional<Object>> extends Blueprint<O> {
	@Override
	public <F, I> O decode(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		var map = deserializer.read(key, object);
		
		var exists = deserializer.readBoolean("Exists", map);
		
		if (exists) {
			try {
				var valueClass = Class.forName(deserializer.readString("Class", map));
				
				var valueBlueprint = Blueprint.ofClass((Class<Object>) valueClass);
				
				return (O) set(Optional.of(valueBlueprint.decode(deserializer, "Value", map)), instance);
			} catch (Exception e) {
				return (O) set(Optional.empty(), instance);
			}
		} else {
			return (O) set(Optional.empty(), instance);
		}
	}
	
	@Override
	public <F, V> void encode(Serializer<F> serializer, @Nullable String key, V value, F object) {
		var map = serializer.createMap(object);
		
		var valueOptional = get(value);
		
		if (valueOptional.isPresent()) {
			var entry = valueOptional.get();
			
			var blueprint = Blueprint.ofValue(entry);
			
			serializer.writeBoolean("Exists", true, map);
			
			serializer.writeString("Class", entry.getClass().getName(), map);
			
			blueprint.encode(serializer, "Value", entry, map);
		} else {
			serializer.writeBoolean("Exists", false, map);
		}
		
		serializer.write(key, map, object);
	}

	@Override
	public String toString() {
		return "GenericOptionalNode[" + (key == null ? "None" : key) + "]";
	}
}