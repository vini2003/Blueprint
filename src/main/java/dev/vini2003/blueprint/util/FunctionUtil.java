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

package dev.vini2003.blueprint.util;

import dev.vini2003.blueprint.consumer.Consumer2;
import dev.vini2003.blueprint.function.Function1;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public class FunctionUtil {
    @Nullable
    public static <O, T> Function1<O, T> wrapGetter(Method getter) {
        if (getter == null) {
            return null;
        }
        
        return (o) -> {
            try {
                return (T) getter.invoke(o);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
    
    @Nullable
    public static <O, T>Consumer2<O, T> wrapSetter(Method method) {
        if (method == null) {
            return null;
        }
        
        return (o, t) -> {
            try {
                method.invoke(o, t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
