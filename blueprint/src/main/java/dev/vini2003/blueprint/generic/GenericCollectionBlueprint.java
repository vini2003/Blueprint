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

@SuppressWarnings({"unchecked", "rawtypes"})
public class GenericCollectionBlueprint extends Blueprint<Collection> {
	private final Supplier1<Collection> collection;
	
	public GenericCollectionBlueprint(Supplier1<Collection> collection) {
		this.collection = collection;
	}
	
	@Override
	public <F, I> Collection decode(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		var newCollection = this.collection.get();
		
		var exists = deserializer.readBoolean(metaDataKey(key) + "$Flag", object);
		
		if (exists) {
			try {
				var valueClass = Class.forName(deserializer.readString(metaDataKey(key) + "$Value", object));
				
				var valueBlueprint = Blueprint.of(valueClass);
				
				deserializer.readCollection(valueBlueprint, key, object, newCollection::add);
				
				return setter(newCollection, instance);
			} catch (Exception e) {
				return setter(newCollection, instance);
			}
		} else {
			return setter(newCollection, instance);
		}
	}
	
	@Override
	public <F, V> void encode(Serializer<F> serializer, @Nullable String key, V value, F object) {
		var collection = serializer.createCollection(object);
		
		var valueCollection = getter(value);
		
		if (!valueCollection.isEmpty()) {
			var entry = valueCollection.stream().findFirst().orElseThrow();
			
			var valueBlueprint = Blueprint.of(entry);
			
			serializer.writeBoolean("Exists", true, collection);
			
			serializer.writeString("Value", entry.getClass().getName(), collection);
			
			serializer.writeCollection(valueBlueprint, key, valueCollection, collection);
		} else {
			serializer.writeBoolean("Exists", false, collection);
		}
		
		serializer.write(key, collection, object);
	}
	
	private String metaDataKey(String key) {
		return key == null ? "MetaData" : key + "$MetaData";
	}
	
	@Override
	public String toString() {
		return "GenericCollectionNode[" + (key == null ? "None" : key) + "]";
	}
}
