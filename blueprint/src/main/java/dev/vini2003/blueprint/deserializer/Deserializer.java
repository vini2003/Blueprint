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

package dev.vini2003.blueprint.deserializer;

import dev.vini2003.blueprint.Blueprint;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import dev.vini2003.blueprint.consumer.Consumer2;
import dev.vini2003.blueprint.consumer.Consumer1;
import java.util.function.Supplier;

public interface Deserializer<F> {
	F read(@Nullable String key, F object);
	
	boolean readBoolean(@Nullable String key, F object);
	
	byte readByte(@Nullable String key, F object);
	
	short readShort(@Nullable String key, F object);
	
	char readChar(@Nullable String key, F object);
	
	int readInt(@Nullable String key, F object);
	
	long readLong(@Nullable String key, F object);
	
	float readFloat(@Nullable String key, F object);
	
	double readDouble(@Nullable String key, F object);
	
	String readString(@Nullable String key, F object);
	
	<K, V> void readMap(Blueprint<K> keyBlueprint, Blueprint<V> valueBlueprint, @Nullable String key, F object, Consumer2<K, V> mapper);
	
	<V> void readCollection(Blueprint<V> valueBlueprint, @Nullable String key, F object, Consumer1<V> collector);
}