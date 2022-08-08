package dev.vini2003.blueprint;

import dev.vini2003.blueprint.consumer.Consumer2;
import dev.vini2003.blueprint.deserializer.Deserializer;
import dev.vini2003.blueprint.deserializer.Serializer;
import dev.vini2003.blueprint.function.Function1;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class WrappedBlueprint<T> extends Blueprint<T> {
	private final Blueprint<T> wrapped;
	
	public WrappedBlueprint(Function1<?, T> getter, Consumer2<?, ?> setter, Predicate<?> setterPredicate, String key, Blueprint<T> wrapped) {
		super(getter, setter, setterPredicate, key);
		
		this.wrapped = wrapped;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <F, V> void encode(Serializer<F> serializer, @Nullable String key, V value, F object) {
		wrapped.encode(serializer, this.key != null ? this.key : key, value, object, (Function1<? super V, T>) this.getter);
	}
	
	@Override
	public <F, I> T decode(Deserializer<F> deserializer, @Nullable String key, F object, I instance) {
		return wrapped.decode(deserializer, this.key != null ? this.key : key, object, instance);
	}
}
