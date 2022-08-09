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
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class OptionalBlueprint<T, N extends Blueprint<T>> extends Blueprint<Optional<T>> {
	private final N n;
	
	public OptionalBlueprint(N n) {
		this.n = n;
	}
	
	@Override
	public <F, I> Optional<T> decode(Decoder<F> decoder, @Nullable String key, F object, I instance) {
		if (decoder.readBoolean(metaDataKey(key) + "Flag", object)) {
			try {
				return setter(Optional.of(n.decode(decoder, key, object, instance)), instance);
			} catch (Exception exception) {
				return setter(Optional.empty(), instance);
			}
		} else {
			return setter(Optional.empty(), instance);
		}
	}
	
	@Override
	public <F, V> void encode(Encoder<F> encoder, @Nullable String key, V value, F object) {
		var result = getter(value);
		
		if (result.isPresent()) {
			encoder.writeBoolean(metaDataKey(key) + "Flag", true, object);
			n.encode(encoder, key, result, object);
		} else {
			encoder.writeBoolean(metaDataKey(key) + "Flag", false, object);
		}
	}
	
	private String metaDataKey(String key) {
		return key == null ? "MetaData" : key + "$MetaData";
	}
	
	@Override
	public String toString() {
		return "OptionalBlueprint[" + (key == null ? "None" : key) + ", " + n + "]";
	}
}
