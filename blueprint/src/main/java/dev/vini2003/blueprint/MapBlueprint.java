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

import dev.vini2003.blueprint.encoding.Decoder;
import dev.vini2003.blueprint.encoding.Encoder;
import dev.vini2003.blueprint.supplier.Supplier1;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class MapBlueprint<T1, T2, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, M extends Map<T1, T2>> extends Blueprint<M> {
	private final N1 n1;
	private final N2 n2;
	
	private final Supplier1<M> map;
	
	public MapBlueprint(N1 n1, N2 n2, Supplier1<M> map) {
		this.n1 = n1;
		this.n2 = n2;
		
		this.map = map;
	}
	
	@Override
	public <F, I> M decode(Decoder<F> decoder, @Nullable String key, F object, I instance) {
		var map = this.map.get();
		
		decoder.readMap(n1, n2, key, object, map::put);
		
		return setter(map, instance);
	}
	
	@Override
	public <F, V> void encode(Encoder<F> encoder, @Nullable String key, V value, F object) {
		encoder.writeMap(n1, n2, key, getter(value), object);
	}
	
	@Override
	public String toString() {
		return "MapBlueprint[" + (key == null ? "None" : key) + ", " + n1 + ", " + n2 + "]";
	}
}
