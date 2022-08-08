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

package dev.vini2003.blueprint.generated;

import dev.vini2003.blueprint.Blueprint;
import dev.vini2003.blueprint.deserializer.Deserializer;
import dev.vini2003.blueprint.deserializer.Serializer;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class GeneratedBlueprint<T> extends Blueprint<T> {
	private final Class<?> clazz;
	
	private final Blueprint<?>[] fieldBlueprints;
	private final Class<?>[] fieldClazzes;
	private final String[] fieldKeys;
	
	private final int fields;
	
	public GeneratedBlueprint(Class<?> clazz, Blueprint<?>[] fieldBlueprints, Class<?>[] fieldClazzes, String[] fieldKeys) {
		this.clazz = clazz;
		
		this.fieldBlueprints = fieldBlueprints;
		this.fieldClazzes = fieldClazzes;
		this.fieldKeys = fieldKeys;
		
		this.fields = fieldBlueprints.length;
	}
	
	@Override
	public <F, I> T decode(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		var map = deserializer.read(key, object);
		
		var results = new Object[fields];
		
		for (var i = 0; i < fields; ++i) {
			var fieldBlueprint = fieldBlueprints[i];
			
			results[i] = fieldBlueprint.decode(deserializer, fieldKeys[i], map, instance);
		}
		
		try {
			return (T) setter(clazz.getConstructor(fieldClazzes).newInstance(results), instance);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public <F, V> void encode(Serializer<F> serializer, @Nullable String key, V value, F object) {
		var map = serializer.createMap(object);
		
		for (var i = 0; i < fields; ++i) {
			var fieldBlueprint = fieldBlueprints[i];

			fieldBlueprint.encode(serializer, fieldKeys[i], value, map);
		}
		
		serializer.write(key, map, object);
	}
	
	@Override
	public String toString() {
		var nodeStrings = new String[fields];
		
		for (var i = 0; i < fields; ++i) {
			nodeStrings[i] = fieldBlueprints[i].toString().replace("None", fieldKeys[i]);
		}
		
		return "GeneratedNode[" + (key == null ? "None" : key) + ", " + Arrays.toString(nodeStrings) + "]";
	}
}
