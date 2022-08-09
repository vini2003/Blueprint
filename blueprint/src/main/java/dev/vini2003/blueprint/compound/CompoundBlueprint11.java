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
import dev.vini2003.blueprint.encoding.Decoder;
import dev.vini2003.blueprint.encoding.Encoder;
import dev.vini2003.blueprint.function.Function11;
import org.jetbrains.annotations.Nullable;


public class CompoundBlueprint11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>, N6 extends Blueprint<T6>, N7 extends Blueprint<T7>, N8 extends Blueprint<T8>, N9 extends Blueprint<T9>, N10 extends Blueprint<T10>, N11 extends Blueprint<T11>> extends Blueprint<R> {
	private final Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> mapper;
	
	private final N1 n1;
	private final N2 n2;
	private final N3 n3;
	private final N4 n4;
	private final N5 n5;
	private final N6 n6;
	private final N7 n7;
	private final N8 n8;
	private final N9 n9;
	private final N10 n10;
	private final N11 n11;
	
	public CompoundBlueprint11(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, N8 n8, N9 n9, N10 n10, N11 n11, Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> mapper) {
		this.n1 = n1;
		this.n2 = n2;
		this.n3 = n3;
		this.n4 = n4;
		this.n5 = n5;
		this.n6 = n6;
		this.n7 = n7;
		this.n8 = n8;
		this.n9 = n9;
		this.n10 = n10;
		this.n11 = n11;
		
		this.mapper = mapper;
	}
	
	@Override
	public <F, I> R decode(Decoder<F> decoder, @Nullable String key, F object, I instance) {
		var map = decoder.read(key, object);
		
		return setter(mapper.apply(
				n1.setter(n1.decode(decoder, key, map, instance), instance),
				n2.setter(n2.decode(decoder, key, map, instance), instance),
				n3.setter(n3.decode(decoder, key, map, instance), instance),
				n4.setter(n4.decode(decoder, key, map, instance), instance),
				n5.setter(n5.decode(decoder, key, map, instance), instance),
				n6.setter(n6.decode(decoder, key, map, instance), instance),
				n7.setter(n7.decode(decoder, key, map, instance), instance),
				n8.setter(n8.decode(decoder, key, map, instance), instance),
				n9.setter(n9.decode(decoder, key, map, instance), instance),
				n10.setter(n10.decode(decoder, key, map, instance), instance),
				n11.setter(n11.decode(decoder, key, map, instance), instance)
		), instance);
	}
	
	@Override
	public <F, O> void encode(Encoder<F> encoder, @Nullable String key, O value, F object) {
		var map = encoder.createMap(object);
		
		n1.encode(encoder, key, getter(value), map);
		n2.encode(encoder, key, getter(value), map);
		n3.encode(encoder, key, getter(value), map);
		n4.encode(encoder, key, getter(value), map);
		n5.encode(encoder, key, getter(value), map);
		n6.encode(encoder, key, getter(value), map);
		n7.encode(encoder, key, getter(value), map);
		n8.encode(encoder, key, getter(value), map);
		n9.encode(encoder, key, getter(value), map);
		n10.encode(encoder, key, getter(value), map);
		n11.encode(encoder, key, getter(value), map);
		
		encoder.write(key, map, object);
	}
	
	@Override
	public String toString() {
		return "CompoundBlueprint[" + (key == null ? "None" : key) + ", " + n1 + ", " + n2 + ", " + n3 + ", " + n4 + ", " + n5 + ", " + n6 + ", " + n7 + ", " + n8 + ", " + n9 + ", " + n10 + ", " + n11 + "]";
	}
}
