package dev.vini2003.blueprint.util;

import dev.vini2003.blueprint.supplier.Supplier1;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MapUtil {
	@Nullable
	public static Supplier1<? extends Map<?, ?>> findDefaultConstructor(Class<?> clazz) {
		return HashMap::new;
	}
}
