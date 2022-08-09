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

package dev.vini2003.blueprint.encoding;

import dev.vini2003.blueprint.Blueprint;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public interface Encoder<F> {
	F createRoot();
	
	F createCollection(F object);
	
	F createMap(F object);
	
	void write(@Nullable String key, F value, F object);
	
	void writeBoolean(@Nullable String key, boolean value, F object);
	
	void writeByte(@Nullable String key, byte value, F object);
	
	void writeShort(@Nullable String key, short value, F object);
	
	void writeChar(@Nullable String key, char value, F object);
	
	void writeInt(@Nullable String key, int value, F object);
	
	void writeLong(@Nullable String key, long value, F object);
	
	void writeFloat(@Nullable String key, float value, F object);
	
	void writeDouble(@Nullable String key, double value, F object);
	
	void writeString(@Nullable String key, String value, F object);
	
	<K, V, M extends Map<K, V>> void writeMap(Blueprint<K> keyBlueprint, Blueprint<V> valueBlueprint, @Nullable String key, M value, F object);
	
	<V, C extends Collection<V>> void writeCollection(Blueprint<V> valueBlueprint, @Nullable String key, C value, F object);
}