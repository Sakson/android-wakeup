package com.xiaoai.wakeup.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class CollectionUtil {

	/**
	 * Get collection's size.
	 * 
	 * @param collection
	 * @return
	 */
	public static int size(Collection<?> collection) {
		if (collection == null) {
			return 0;
		}
		return collection.size();
	}

	/**
	 * Check if the collection is null or it's size is 0.
	 * 
	 * @param collection
	 * @return
	 */
	public static boolean isEmpty(Collection<?> collection) {
		if (size(collection) == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Add all of the objects in target collection to the origin collection.
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static <T> boolean addAll(Collection<T> origin, Collection<T> target) {
		if (origin == null || target == null) {
			return false;
		}
		return origin.addAll(target);
	}

	/**
	 * Add all of the objects in target collection to the origin collection and
	 * ignore the objects contain in origin collection.
	 * 
	 * @param origin
	 * @param target
	 * @return
	 */
	public static <T> Collection<T> addAllIgnoreContains(Collection<T> origin,
			Collection<T> target) {
		if (origin == null) {
			return null;
		}

		List<T> temp = new ArrayList<T>();
		if (!isEmpty(target)) {
			Iterator<T> it = target.iterator();
			while (it.hasNext()) {
				T object = it.next();
				if (!origin.contains(object)) {
					temp.add(object);
				}
			}
		}

		addAll(origin, temp);
		return origin;
	}

}
