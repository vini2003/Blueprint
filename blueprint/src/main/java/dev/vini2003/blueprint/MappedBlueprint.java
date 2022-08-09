package dev.vini2003.blueprint;

import dev.vini2003.blueprint.consumer.Consumer2;
import dev.vini2003.blueprint.encoding.Decoder;
import dev.vini2003.blueprint.encoding.Encoder;
import dev.vini2003.blueprint.function.Function1;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class MappedBlueprint<T, U> extends Blueprint<U> {
	private final Blueprint<T> wrapped;
	
	private final Function1<T, U> decodeMapper;
	private final Function1<U, T> encodeMapper;
	
	@SuppressWarnings("unchecked")
	public MappedBlueprint(Function1<?, T> getter, Consumer2<?, ?> setter, Predicate<?> setterPredicate, String key, Blueprint<T> wrapped, Function1<T, U> decodeMapper, Function1<U, T> encodeMapper) {
		super((Function1<?, U>) getter, setter, setterPredicate, key);
		
		this.wrapped = wrapped;
		
		this.decodeMapper = decodeMapper;
		this.encodeMapper = encodeMapper;
	}
	
	@Override
	public <F, V> void encode(Encoder<F> encoder, @Nullable String key, V value, F object) {
		wrapped.encode(encoder, key, encodeMapper.apply(getter(value)), object);
	}
	
	@Override
	public <F, I> U decode(Decoder<F> decoder, @Nullable String key, F object, I instance) {
		return decodeMapper.apply(wrapped.decode(decoder, key, object, instance));
	}
}
