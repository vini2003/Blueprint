package dev.vini2003.blueprint.util;

import dev.vini2003.blueprint.supplier.Supplier1;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CollectionUtil {
	@Nullable
	@SuppressWarnings("rawtypes")
	public static Supplier1<Collection> findDefaultConstructor(Class<?> clazz) {
		if (List.class.isAssignableFrom(clazz)) {
			return ArrayList::new;
		} else if (Set.class.isAssignableFrom(clazz)) {
			return HashSet::new;
		} else if (Queue.class.isAssignableFrom(clazz)) {
			return ArrayDeque::new;
		} else {
			return null;
		}
	}
}
