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

import java.util.List;

public class ListBlueprint<T, N extends Blueprint<T>> extends Blueprint<List<T>> {
	private final N n;
	
	public ListBlueprint(N n) {
		this.n = n;
	}
	
	@Override
	public <F, I> List<T> decode(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		return set(deserializer.readList(n, key, object), instance);
	}
	
	@Override
	public <F, O> void encode(Serializer<F> serializer, @Nullable String key, O value, F object) {
		serializer.writeList(n, key, get(value), object);
	}
	
	@Override
	public String toString() {
		return "ListNode[" + (key == null ? "None" : key) + ", " + n + "]";
	}
}
