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
import dev.vini2003.blueprint.function.Function9;
import org.jetbrains.annotations.Nullable;


public class CompoundBlueprint9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>, N6 extends Blueprint<T6>, N7 extends Blueprint<T7>, N8 extends Blueprint<T8>, N9 extends Blueprint<T9>> extends Blueprint<R> {
	private final Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> mapper;
	
	private final N1 n1;
	private final N2 n2;
	private final N3 n3;
	private final N4 n4;
	private final N5 n5;
	private final N6 n6;
	private final N7 n7;
	private final N8 n8;
	private final N9 n9;
	
	public CompoundBlueprint9(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, N8 n8, N9 n9, Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> mapper) {
		this.n1 = n1;
		this.n2 = n2;
		this.n3 = n3;
		this.n4 = n4;
		this.n5 = n5;
		this.n6 = n6;
		this.n7 = n7;
		this.n8 = n8;
		this.n9 = n9;
		
		this.mapper = mapper;
	}
	
	@Override
	public <F, I> R decode(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		var map = deserializer.read(key, object);
		
		return set(mapper.apply(n1.decode(deserializer, key, map, instance), n2.decode(deserializer, key, map, instance), n3.decode(deserializer, key, map, instance), n4.decode(deserializer, key, map, instance), n5.decode(deserializer, key, map, instance), n6.decode(deserializer, key, map, instance), n7.decode(deserializer, key, map, instance), n8.decode(deserializer, key, map, instance), n9.decode(deserializer, key, map, instance)), instance);
	}
	
	@Override
	public <F, O> void encode(Serializer<F> serializer, @Nullable String key, O value, F object) {
		var map = serializer.createMap(object);
		
		n1.encode(serializer, key, get(value), map);
		n2.encode(serializer, key, get(value), map);
		n3.encode(serializer, key, get(value), map);
		n4.encode(serializer, key, get(value), map);
		n5.encode(serializer, key, get(value), map);
		n6.encode(serializer, key, get(value), map);
		n7.encode(serializer, key, get(value), map);
		n8.encode(serializer, key, get(value), map);
		n9.encode(serializer, key, get(value), map);
		
		serializer.write(key, map, object);
	}
	
	@Override
	public String toString() {
		return "CompoundNode[" + (key == null ? "None" : key) + ", " + n1 + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + n8 + ", " + n9 + "]";
	}
}
