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

import dev.vini2003.blueprint.annotation.Blueprintable;
import dev.vini2003.blueprint.annotation.DefaultBlueprint;
import dev.vini2003.blueprint.compound.*;
import dev.vini2003.blueprint.consumer.Consumer2;
import dev.vini2003.blueprint.encoding.Decoder;
import dev.vini2003.blueprint.encoding.Encoder;
import dev.vini2003.blueprint.exception.BlueprintException;
import dev.vini2003.blueprint.function.*;
import dev.vini2003.blueprint.generic.GenericCompoundBlueprint;
import dev.vini2003.blueprint.generic.GenericCollectionBlueprint;
import dev.vini2003.blueprint.generic.GenericMapBlueprint;
import dev.vini2003.blueprint.generic.GenericOptionalBlueprint;
import dev.vini2003.blueprint.pair.Pair;
import dev.vini2003.blueprint.util.CollectionUtil;
import dev.vini2003.blueprint.util.FunctionUtil;
import dev.vini2003.blueprint.util.MapUtil;
import dev.vini2003.blueprint.util.ReflectionUtil;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Predicate;

public abstract class Blueprint<T> {
	private static final Map<Class<?>, Blueprint<?>> BLUEPRINTS = new HashMap<>();
	
	protected Function1<?, T> getter;
	protected Consumer2<?, ?> setter;
	protected Predicate<?> setterPredicate;
	
	protected String key = null;
	
	public Blueprint() {
	
	}
	
	public Blueprint(Function1<?, T> getter, Consumer2<?, ?> setter, Predicate<?> setterPredicate, String key) {
		this.getter = getter;
		this.setter = setter;
		this.setterPredicate = setterPredicate;
		
		this.key = key;
	}
	
	public <O> Blueprint<T> get(Function1<O, T> getter) {
		return new WrappedBlueprint<>(getter, setter, setterPredicate, key, this);
	}
	
	public <E, J> Blueprint<T> set(Consumer2<E, J> setter) {
		return new WrappedBlueprint<>(getter, setter, setterPredicate, key, this);
	}
	
	public <E> Blueprint<T> when(Predicate<E> setterPredicate) {
		return new WrappedBlueprint<>(getter, setter, setterPredicate, key, this);
	}
	
	public Blueprint<T> key(String key) {
		return new WrappedBlueprint<>(getter, setter, setterPredicate, key, this);
	}
	
	public <U> Blueprint<U> xmap(Function1<T, U> decodeMapper, Function1<U, T> encodeMapper) {
		return new MappedBlueprint<>(getter, setter, setterPredicate, key, this, decodeMapper, encodeMapper);
	}
	
	public Blueprint<List<T>> list() {
		return new CollectionBlueprint<>(this, ArrayList::new);
	}
	
	public Blueprint<T[]> array() {
			return new CollectionBlueprint<>(this, ArrayList::new).xmap(list -> (T[]) list.toArray(), array -> (ArrayList<T>) Arrays.asList(array));
	}
	
	public Blueprint<Set<T>> setter() {
		return new CollectionBlueprint<>(this, HashSet::new);
	}
	
	public Blueprint<Queue<T>> queue() {
		return new CollectionBlueprint<>(this, ArrayDeque::new);
	}
	
	public Blueprint<Optional<T>> optional() {
		return new OptionalBlueprint<>(this);
	}
	
	public static Blueprint<Boolean> BOOLEAN = new Blueprint<>() {
		@Override
		public <F, I> Boolean decode(Decoder<F> decoder, @Nullable String key, F object, I instance) {
			return setter(decoder.readBoolean(key, object), instance);
		}
		
		@Override
		public <F, V> void encode(Encoder<F> encoder, @Nullable String key, V value, F object) {
			encoder.writeBoolean(key, getter(value), object);
		}
	};
	
	public static Blueprint<Byte> BYTE = new Blueprint<>() {
		@Override
		public <F, I> Byte decode(Decoder<F> decoder, @Nullable String key, F object, I instance) {
			return setter(decoder.readByte(key, object), instance);
		}
		
		@Override
		public <F, V> void encode(Encoder<F> encoder, @Nullable String key, V value, F object) {
			encoder.writeByte(key, getter(value), object);
		}
	};
	
	public static Blueprint<Short> SHORT = new Blueprint<>() {
		@Override
		public <F, I> Short decode(Decoder<F> decoder, @Nullable String key, F object, I instance) {
			return setter(decoder.readShort(key, object), instance);
		}
		
		@Override
		public <F, V> void encode(Encoder<F> encoder, @Nullable String key, V value, F object) {
			encoder.writeShort(key, getter(value), object);
		}
	};
	
	public static Blueprint<Character> CHARACTER = new Blueprint<>() {
		@Override
		public <F, I> Character decode(Decoder<F> decoder, @Nullable String key, F object, I instance) {
			return setter(decoder.readChar(key, object), instance);
		}
		
		@Override
		public <F, V> void encode(Encoder<F> encoder, @Nullable String key, V value, F object) {
			encoder.writeChar(key, getter(value), object);
		}
	};
	
	public static Blueprint<Integer> INTEGER = new Blueprint<>() {
		@Override
		public <F, I> Integer decode(Decoder<F> decoder, @Nullable String key, F object, I instance) {
			return setter(decoder.readInt(key, object), instance);
		}
		
		@Override
		public <F, V> void encode(Encoder<F> encoder, @Nullable String key, V value, F object) {
			encoder.writeInt(key, getter(value), object);
		}
	};
	
	public static Blueprint<Long> LONG = new Blueprint<>() {
		@Override
		public <F, I> Long decode(Decoder<F> decoder, @Nullable String key, F object, I instance) {
			return setter(decoder.readLong(key, object), instance);
		}
		
		@Override
		public <F, V> void encode(Encoder<F> encoder, @Nullable String key, V value, F object) {
			encoder.writeLong(key, getter(value), object);
		}
	};
	
	public static Blueprint<Float> FLOAT = new Blueprint<>() {
		@Override
		public <F, I> Float decode(Decoder<F> decoder, @Nullable String key, F object, I instance) {
			return setter(decoder.readFloat(key, object), instance);
		}
		
		@Override
		public <F, V> void encode(Encoder<F> encoder, @Nullable String key, V value, F object) {
			encoder.writeFloat(key, getter(value), object);
		}
	};
	
	public static Blueprint<Double> DOUBLE = new Blueprint<>() {
		@Override
		public <F, I> Double decode(Decoder<F> decoder, @Nullable String key, F object, I instance) {
			return setter(decoder.readDouble(key, object), instance);
		}
		
		@Override
		public <F, V> void encode(Encoder<F> encoder, @Nullable String key, V value, F object) {
			encoder.writeDouble(key, getter(value), object);
		}
	};
	
	public static Blueprint<String> STRING = new Blueprint<>() {
		@Override
		public <F, I> String decode(Decoder<F> decoder, @Nullable String key, F object, I instance) {
			return setter(decoder.readString(key, object), instance);
		}
		
		@Override
		public <F, V> void encode(Encoder<F> encoder, @Nullable String key, V value, F object) {
			encoder.writeString(key, getter(value), object);
		}
	};
	
	public <F, V> F encode(Encoder<F> encoder, V value) {
		var root = encoder.createRoot();
		encode(encoder, null, value, root);
		return root;
	}
	
	public <F, V> void encode(Encoder<F> encoder, V value, F object) {
		encode(encoder, null, value, object);
	}
	
	public <F, V> void encode(Encoder<F> encoder, V value, F object, Function1<V, T> getter) {
		encode(encoder, null, value, object, getter);
	}
	
	public <F, V> void encode(Encoder<F> encoder, @Nullable String key, V value, F object, Function1<V, T> getter) {
		encode(encoder, key, getter == null ? value : getter.apply(value), object);
	}
	
	public abstract <F, V> void encode(Encoder<F> encoder, @Nullable String key, V value, F object);
	
	public <F> T decode(Decoder<F> decoder, F object) {
		return decode(decoder, object, null);
	}
	
	public <F> T decode(Decoder<F> decoder, @Nullable String key, F object) {
		return decode(decoder, key, object, null);
	}
	
	public <F, I> T decode(Decoder<F> decoder, F object, I instance) {
		return decode(decoder, key, object, instance);
	}
	
	public abstract <F, I> T decode(Decoder<F> decoder, @Nullable String key, F object, I instance);
	
	@SuppressWarnings("unchecked")
	protected <V> T getter(V value) {
		return getter == null ? (T) value : ((Function1<V, T>) getter).apply(value);
	}
	
	@SuppressWarnings("unchecked")
	public <T, I> T setter(T value, I instance) {
		if (instance != null && setter != null) {
			((Consumer2<I, T>) setter).accept((I) getter(instance), value);
		}
		
		return value;
	}
	
	public String getKey() {
		return key;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "Blueprint[" + (key == null ? "None" : key) + "]";
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Blueprint<T> register(Class<?> clazz, Blueprint<?> blueprint) {
		BLUEPRINTS.put(clazz, blueprint);
		return (Blueprint<T>) blueprint;
	}
	
	@Nullable
	@SuppressWarnings("rawtypes")
	public static Blueprint of(Object t) {
		return t instanceof Class<?> clazz ? ofClass(clazz) : ofClass(t.getClass());
	}
	
	@Nullable
	@SuppressWarnings({"rawtypes", "unchecked"})
	private static Blueprint ofClass(Class clazz) {
		if (BLUEPRINTS.containsKey(clazz)) {
			return BLUEPRINTS.get(clazz);
		}
		
		if (Optional.class.isAssignableFrom(clazz)) {
			return register(clazz, new GenericOptionalBlueprint());
		} else if (Map.class.isAssignableFrom(clazz)) {
			var wrappedMapConstructor = FunctionUtil.<Map>wrapConstructor(ReflectionUtil.findConstructor(clazz));
			
			if (wrappedMapConstructor == null) {
				throw new BlueprintException("Map did not have no-argument constructor in class '" + clazz.getName() + "', cannot create blueprint");
			}
			
			return register(clazz, new GenericMapBlueprint(wrappedMapConstructor));
		} else if (Collection.class.isAssignableFrom(clazz)) {
			var wrappedCollectionConstructor = FunctionUtil.<Collection>wrapConstructor(ReflectionUtil.findConstructor(clazz));
			
			if (wrappedCollectionConstructor == null) {
				throw new BlueprintException("Collection did not have no-argument constructor in class '" + clazz.getName() + "', cannot create blueprint");
			}
			
			return register(clazz, new GenericCollectionBlueprint(wrappedCollectionConstructor));
		} else if (clazz.isArray()) {
			var wrappedCollectionConstructor = CollectionUtil.findDefaultConstructor(List.class);
			
			return register(clazz, new GenericCollectionBlueprint(wrappedCollectionConstructor).xmap(Collection::toArray, Arrays::asList));
		} else if (clazz.isAnnotationPresent(Blueprintable.class)) {
			var fields = clazz.getDeclaredFields();
			
			var fieldBlueprints = new ArrayList<Blueprint<?>>();
			var fieldTypes = new ArrayList<Class<?>>();
			var fieldKeys = new ArrayList<String>();
			
			for (var field : fields) {
				Blueprint fieldBlueprint;
				
				if (Optional.class.isAssignableFrom(field.getType())) {
					fieldBlueprint = new GenericOptionalBlueprint();
				} else if (Map.class.isAssignableFrom(field.getType())) {
					var wrappedMapConstructor = FunctionUtil.<Map>wrapConstructor(ReflectionUtil.findConstructor(field.getType()));
					
					if (wrappedMapConstructor == null) {
						wrappedMapConstructor = MapUtil.findDefaultConstructor(field.getType());
					}
					
					if (wrappedMapConstructor == null) {
						throw new BlueprintException("Map did not have no-argument constructor in field '" + field.getName() + "' of class '" + clazz.getName() + "', cannot create blueprint");
					}
					
					fieldBlueprint = new GenericMapBlueprint(wrappedMapConstructor);
				} else if (Collection.class.isAssignableFrom(field.getType())) {
					var wrappedCollectionConstructor = FunctionUtil.<Collection>wrapConstructor(ReflectionUtil.findConstructor(field.getType()));
					
					if (wrappedCollectionConstructor == null) {
						wrappedCollectionConstructor = CollectionUtil.findDefaultConstructor(field.getType());
					}
					
					if (wrappedCollectionConstructor == null) {
						throw new BlueprintException("Collection did not have no-argument constructor in field '" + field.getName() + "' of class '" + clazz.getName() + "', cannot create blueprint");
					}
				
					fieldBlueprint = new GenericCollectionBlueprint(wrappedCollectionConstructor);
				} else {
					fieldBlueprint = ofClass(field.getType());
				}
				
				if (fieldBlueprint == null) {
					continue;
				}
				
				var wrappedFieldGetter = FunctionUtil.wrapGetter(ReflectionUtil.findGetter(clazz, field.getType(), field.getName()));
				var wrappedFieldSetter = FunctionUtil.wrapSetter(ReflectionUtil.findSetter(clazz, field.getType(), field.getName()));
				
				if (wrappedFieldGetter != null) {
					fieldBlueprint = fieldBlueprint.get(wrappedFieldGetter);
				}
				
				if (wrappedFieldSetter != null) {
					fieldBlueprint = fieldBlueprint.set(wrappedFieldSetter);
				}
				
				if (wrappedFieldGetter == null || wrappedFieldSetter == null) {
					continue;
				}
				
				fieldBlueprints.add(fieldBlueprint);
				
				fieldTypes.add(field.getType());
				fieldKeys.add(field.getName());
			}
			
			return register(clazz, new GenericCompoundBlueprint(clazz, fieldBlueprints.toArray(Blueprint[]::new), fieldTypes.toArray(Class[]::new), fieldKeys.toArray(String[]::new)));
		} else {
			var fields = clazz.getDeclaredFields();
			
			for (var field : fields) {
				if (Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(DefaultBlueprint.class)) {
					try {
						return register(clazz, (Blueprint) field.get(null));
					} catch (Exception e) {
						return null;
					}
				}
			}
			
			return null;
		}
	}
	
	public static <T1, T2> Blueprint<Map<T1, T2>> map(Blueprint<T1> keyBlueprint, Blueprint<T2> valueBlueprint) {
		return new MapBlueprint<>(keyBlueprint, valueBlueprint, HashMap::new);
	}
	
	public static <T> Blueprint<List<T>> list(Blueprint<T> valueBlueprint) {
		return new CollectionBlueprint<>(valueBlueprint, ArrayList::new);
	}
	
	public static <T> Blueprint<T[]> array(Blueprint<T> valueBlueprint) {
		return new CollectionBlueprint<>(valueBlueprint, ArrayList::new).xmap(list -> (T[]) list.toArray(), array -> (ArrayList<T>) Arrays.asList(array));
	}
	
	public static <T> Blueprint<Set<T>> setter(Blueprint<T> valueBlueprint) {
		return new CollectionBlueprint<>(valueBlueprint, HashSet::new);
	}
	
	public static <T> Blueprint<Queue<T>> queue(Blueprint<T> valueBlueprint) {
		return new CollectionBlueprint<>(valueBlueprint, ArrayDeque::new);
	}
	
	public static <T> Blueprint<Optional<T>> optional(Blueprint<T> valueBlueprint) {
		return new OptionalBlueprint<>(valueBlueprint);
	}
	
	public static <T1, T2> Blueprint<Pair<T1, T2>> pair(Blueprint<T1> firstBlueprint, Blueprint<T2> secondBlueprint) {
		return new PairBlueprint<>(firstBlueprint, secondBlueprint);
	}
	
	public static <R, T1, N1 extends Blueprint<T1>> CompoundBlueprint1<R, T1, N1> compound(N1 n1, Function1<T1, R> mapper) {
		return new CompoundBlueprint1<>(n1, mapper);
	}
	
	public static <R, T1, T2, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>> CompoundBlueprint2<R, T1, T2, N1, N2> compound(N1 n1, N2 n2, Function2<T1, T2, R> mapper) {
		return new CompoundBlueprint2<>(n1, n2, mapper);
	}
	
	public static <R, T1, T2, T3, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>> CompoundBlueprint3<R, T1, T2, T3, N1, N2, N3> compound(N1 n1, N2 n2, N3 n3, Function3<T1, T2, T3, R> mapper) {
		return new CompoundBlueprint3<>(n1, n2, n3, mapper);
	}
	
	public static <R, T1, T2, T3, T4, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>> CompoundBlueprint4<R, T1, T2, T3, T4, N1, N2, N3, N4> compound(N1 n1, N2 n2, N3 n3, N4 n4, Function4<T1, T2, T3, T4, R> mapper) {
		return new CompoundBlueprint4<>(n1, n2, n3, n4, mapper);
	}
	
	public static <R, T1, T2, T3, T4, T5, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>> CompoundBlueprint5<R, T1, T2, T3, T4, T5, N1, N2, N3, N4, N5> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, Function5<T1, T2, T3, T4, T5, R> mapper) {
		return new CompoundBlueprint5<>(n1, n2, n3, n4, n5, mapper);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>, N6 extends Blueprint<T6>> CompoundBlueprint6<R, T1, T2, T3, T4, T5, T6, N1, N2, N3, N4, N5, N6> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, Function6<T1, T2, T3, T4, T5, T6, R> mapper) {
		return new CompoundBlueprint6<>(n1, n2, n3, n4, n5, n6, mapper);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>, N6 extends Blueprint<T6>, N7 extends Blueprint<T7>> CompoundBlueprint7<R, T1, T2, T3, T4, T5, T6, T7, N1, N2, N3, N4, N5, N6, N7> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, Function7<T1, T2, T3, T4, T5, T6, T7, R> mapper) {
		return new CompoundBlueprint7<>(n1, n2, n3, n4, n5, n6, n7, mapper);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>, N6 extends Blueprint<T6>, N7 extends Blueprint<T7>, N8 extends Blueprint<T8>> CompoundBlueprint8<R, T1, T2, T3, T4, T5, T6, T7, T8, N1, N2, N3, N4, N5, N6, N7, N8> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, N8 n8, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> mapper) {
		return new CompoundBlueprint8<>(n1, n2, n3, n4, n5, n6, n7, n8, mapper);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>, N6 extends Blueprint<T6>, N7 extends Blueprint<T7>, N8 extends Blueprint<T8>, N9 extends Blueprint<T9>> CompoundBlueprint9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, N1, N2, N3, N4, N5, N6, N7, N8, N9> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, N8 n8, N9 n9, Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> mapper) {
		return new CompoundBlueprint9<>(n1, n2, n3, n4, n5, n6, n7, n8, n9, mapper);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>, N6 extends Blueprint<T6>, N7 extends Blueprint<T7>, N8 extends Blueprint<T8>, N9 extends Blueprint<T9>, N10 extends Blueprint<T10>> CompoundBlueprint10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, N8 n8, N9 n9, N10 n10, Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> mapper) {
		return new CompoundBlueprint10<>(n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, mapper);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>, N6 extends Blueprint<T6>, N7 extends Blueprint<T7>, N8 extends Blueprint<T8>, N9 extends Blueprint<T9>, N10 extends Blueprint<T10>, N11 extends Blueprint<T11>> CompoundBlueprint11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, N8 n8, N9 n9, N10 n10, N11 n11, Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> mapper) {
		return new CompoundBlueprint11<>(n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, mapper);
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>, N6 extends Blueprint<T6>, N7 extends Blueprint<T7>, N8 extends Blueprint<T8>, N9 extends Blueprint<T9>, N10 extends Blueprint<T10>, N11 extends Blueprint<T11>, N12 extends Blueprint<T12>> CompoundBlueprint12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, N8 n8, N9 n9, N10 n10, N11 n11, N12 n12, Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> mapper) {
		return new CompoundBlueprint12<>(n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, mapper);
	}
	
	public static <R, T1, N1 extends Blueprint<T1>> CompoundBlueprint1<R, T1, N1> compound(N1 n1) {
		return new CompoundBlueprint1<>(n1, Function1.empty());
	}
	
	public static <R, T1, T2, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>> CompoundBlueprint2<R, T1, T2, N1, N2> compound(N1 n1, N2 n2) {
		return new CompoundBlueprint2<>(n1, n2, Function2.empty());
	}
	
	public static <R, T1, T2, T3, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>> CompoundBlueprint3<R, T1, T2, T3, N1, N2, N3> compound(N1 n1, N2 n2, N3 n3) {
		return new CompoundBlueprint3<>(n1, n2, n3, Function3.empty());
	}
	
	public static <R, T1, T2, T3, T4, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>> CompoundBlueprint4<R, T1, T2, T3, T4, N1, N2, N3, N4> compound(N1 n1, N2 n2, N3 n3, N4 n4) {
		return new CompoundBlueprint4<>(n1, n2, n3, n4, Function4.empty());
	}
	
	public static <R, T1, T2, T3, T4, T5, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>> CompoundBlueprint5<R, T1, T2, T3, T4, T5, N1, N2, N3, N4, N5> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5) {
		return new CompoundBlueprint5<>(n1, n2, n3, n4, n5, Function5.empty());
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>, N6 extends Blueprint<T6>> CompoundBlueprint6<R, T1, T2, T3, T4, T5, T6, N1, N2, N3, N4, N5, N6> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6) {
		return new CompoundBlueprint6<>(n1, n2, n3, n4, n5, n6, Function6.empty());
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>, N6 extends Blueprint<T6>, N7 extends Blueprint<T7>> CompoundBlueprint7<R, T1, T2, T3, T4, T5, T6, T7, N1, N2, N3, N4, N5, N6, N7> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7) {
		return new CompoundBlueprint7<>(n1, n2, n3, n4, n5, n6, n7, Function7.empty());
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>, N6 extends Blueprint<T6>, N7 extends Blueprint<T7>, N8 extends Blueprint<T8>> CompoundBlueprint8<R, T1, T2, T3, T4, T5, T6, T7, T8, N1, N2, N3, N4, N5, N6, N7, N8> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, N8 n8) {
		return new CompoundBlueprint8<>(n1, n2, n3, n4, n5, n6, n7, n8, Function8.empty());
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>, N6 extends Blueprint<T6>, N7 extends Blueprint<T7>, N8 extends Blueprint<T8>, N9 extends Blueprint<T9>> CompoundBlueprint9<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, N1, N2, N3, N4, N5, N6, N7, N8, N9> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, N8 n8, N9 n9) {
		return new CompoundBlueprint9<>(n1, n2, n3, n4, n5, n6, n7, n8, n9, Function9.empty());
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>, N6 extends Blueprint<T6>, N7 extends Blueprint<T7>, N8 extends Blueprint<T8>, N9 extends Blueprint<T9>, N10 extends Blueprint<T10>> CompoundBlueprint10<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, N8 n8, N9 n9, N10 n10) {
		return new CompoundBlueprint10<>(n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, Function10.empty());
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>, N6 extends Blueprint<T6>, N7 extends Blueprint<T7>, N8 extends Blueprint<T8>, N9 extends Blueprint<T9>, N10 extends Blueprint<T10>, N11 extends Blueprint<T11>> CompoundBlueprint11<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, N8 n8, N9 n9, N10 n10, N11 n11) {
		return new CompoundBlueprint11<>(n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, Function11.empty());
	}
	
	public static <R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, N1 extends Blueprint<T1>, N2 extends Blueprint<T2>, N3 extends Blueprint<T3>, N4 extends Blueprint<T4>, N5 extends Blueprint<T5>, N6 extends Blueprint<T6>, N7 extends Blueprint<T7>, N8 extends Blueprint<T8>, N9 extends Blueprint<T9>, N10 extends Blueprint<T10>, N11 extends Blueprint<T11>, N12 extends Blueprint<T12>> CompoundBlueprint12<R, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, N1, N2, N3, N4, N5, N6, N7, N8, N9, N10, N11, N12> compound(N1 n1, N2 n2, N3 n3, N4 n4, N5 n5, N6 n6, N7 n7, N8 n8, N9 n9, N10 n10, N11 n11, N12 n12) {
		return new CompoundBlueprint12<>(n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, Function12.empty());
	}
	
	static {
		register(Boolean.class, Blueprint.BOOLEAN);
		register(boolean.class, Blueprint.BOOLEAN);
		register(Byte.class, Blueprint.BYTE);
		register(byte.class, Blueprint.BYTE);
		register(Short.class, Blueprint.SHORT);
		register(short.class, Blueprint.SHORT);
		register(Character.class, Blueprint.CHARACTER);
		register(char.class, Blueprint.CHARACTER);
		register(Integer.class, Blueprint.INTEGER);
		register(int.class, Blueprint.INTEGER);
		register(Long.class, Blueprint.LONG);
		register(long.class, Blueprint.LONG);
		register(Float.class, Blueprint.FLOAT);
		register(float.class, Blueprint.FLOAT);
		register(Double.class, Blueprint.DOUBLE);
		register(double.class, Blueprint.DOUBLE);
		register(String.class, Blueprint.STRING);
	}
}