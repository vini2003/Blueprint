package dev.vini2003.blueprint.util;

import dev.vini2003.blueprint.supplier.Supplier1;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
	@Nullable
	@SuppressWarnings("rawtypes")
	public static Supplier1<Map> findDefaultConstructor(Class clazz) {
		return HashMap::new;
	}
}
