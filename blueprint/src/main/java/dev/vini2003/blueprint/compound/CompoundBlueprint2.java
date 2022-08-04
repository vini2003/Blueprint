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

package dev.vini2003.blueprint.compound;

import dev.vini2003.blueprint.Blueprint;
import dev.vini2003.blueprint.deserializer.Deserializer;
import dev.vini2003.blueprint.deserializer.Serializer;
import dev.vini2003.blueprint.function.Function2;
import org.jetbrains.annotations.Nullable;


public class CompoundBlueprint2<R, T1, T2, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>> extends Blueprint<R> {
	private final Function2<T1, T2, R> mapper;
	
	private final N1 n1;
	private final N2 n2;
	
	public CompoundBlueprint2(N1 n1, N2 n2, Function2<T1, T2, R> mapper) {
		this.n1 = n1;
		this.n2 = n2;
		
		this.mapper = mapper;
	}
	
	@Override
	public <F, I> R decode(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		var map = deserializer.read(key, object);
		
		return set(mapper.apply(n1.decode(deserializer, key, map, instance), n2.decode(deserializer, key, map, instance)), instance);
	}
	
	@Override
	public <F, V> void encode(Serializer<F> serializer, @Nullable String key, V value, F object) {
		var map = serializer.createMap(object);
		
		n1.encode(serializer, key, get(value), map);
		n2.encode(serializer, key, get(value), map);
		
		serializer.write(key, map, object);
	}
	
	@Override
	public String toString() {
		return "CompoundNode[" + (key == null ? "None" : key) + ", " + n1 + ", " + n2 + "]";
	}
}
