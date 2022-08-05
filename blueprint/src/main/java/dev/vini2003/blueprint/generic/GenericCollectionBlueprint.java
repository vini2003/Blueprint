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

public class GenericCollectionBlueprint<C extends Collection<Object>> extends Blueprint<C> {
	private final Supplier1<C> collection;
	
	public GenericCollectionBlueprint(Supplier1<C> collection) {
		this.collection = collection;
	}
	
	@Override
	public <F, I> C decode(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		var collection = this.collection.get();
		
		var exists = deserializer.readBoolean(metaDataKey(key) + "$Flag", object);
		
		if (exists) {
			try {
				var valueClass = Class.forName(deserializer.readString(metaDataKey(key) + "$Value", object));
				
				var valueBlueprint = Blueprint.ofClass((Class<Object>) valueClass);
				
				deserializer.readCollection(valueBlueprint, key, object, collection::add);
				
				return set(collection, instance);
			} catch (Exception e) {
				return set(collection, instance);
			}
		} else {
			return set(collection, instance);
		}
	}
	
	@Override
	public <F, V> void encode(Serializer<F> serializer, @Nullable String key, V value, F object) {
		var collection = get(value);
		
		if (!collection.isEmpty()) {
			var entry = collection.stream().findFirst().orElseThrow();
			
			var valueBlueprint = Blueprint.ofValue(entry);
			
			serializer.writeBoolean(metaDataKey(key) + "$Flag", true, object);
			
			serializer.writeString(metaDataKey(key) + "$Value", entry.getClass().getName(), object);
			
			serializer.writeCollection(valueBlueprint, key, collection, object);
		} else {
			serializer.writeBoolean(metaDataKey(key) + "$Flag", false, object);
		}
	}
	
	private String metaDataKey(String key) {
		return key == null ? "MetaData" : key + "$MetaData";
	}
	
	@Override
	public String toString() {
		return "GenericCollectionNode[" + (key == null ? "None" : key) + "]";
	}
}
