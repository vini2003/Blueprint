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

package dev.vini2003.blueprint;

import dev.vini2003.blueprint.deserializer.Deserializer;
import dev.vini2003.blueprint.deserializer.Serializer;
import org.jetbrains.annotations.Nullable;

public class NullableBlueprint<T, N extends Blueprint<T>> extends Blueprint<T> {
	private final N n;
	
	public NullableBlueprint(N n) {
		this.n = n;
	}
	
	@Override
	public <F, I> T decode(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		if (deserializer.readBoolean(key + "Flag", object)) {
			try {
				return set(n.decode(deserializer, key, object, instance), instance);
			} catch (Exception exception) {
				return set(null, instance);
			}
		} else {
			return set(null, instance);
		}
	}
	
	@Override
	public <F, V> void encode(Serializer<F> serializer, @Nullable String key, V value, F object) {
		var result = get(value);
		
		if (result != null) {
			serializer.writeBoolean(key + "Flag", true, object);
			n.encode(serializer, key, result, object);
		} else {
			serializer.writeBoolean(key + "Flag", false, object);
		}
	}
	
	@Override
	public String toString() {
		return "NullableNode[" + (key == null ? "None" : key) + ", " + n + "]";
	}
}
