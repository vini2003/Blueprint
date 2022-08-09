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

import java.util.Collection;

public class CollectionBlueprint<T, N extends Blueprint<T>, C extends Collection<T>> extends Blueprint<C> {
	private final N n;
	
	private final Supplier1<C> collection;
	
	public CollectionBlueprint(N n, Supplier1<C> collection) {
		this.n = n;
		
		this.collection = collection;
	}
	
	@Override
	public <F, I> C decode(Decoder<F> decoder, @Nullable String key, F object, I instance) {
		var collection = this.collection.get();
		
		decoder.readCollection(n, key, object, collection::add);
		
		return setter(collection, instance);
	}
	
	@Override
	public <F, O> void encode(Encoder<F> encoder, @Nullable String key, O value, F object) {
		encoder.writeCollection(n, key, getter(value), object);
	}
	
	@Override
	public String toString() {
		return "ListBlueprint[" + (key == null ? "None" : key) + ", " + n + "]";
	}
}
