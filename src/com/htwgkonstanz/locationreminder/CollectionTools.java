package com.htwgkonstanz.locationreminder;

import java.util.*;

public final class CollectionTools {
	
	private CollectionTools() {
	}

	public static <T> String makeString(Collection<T> collection) {
		return makeString(collection, "");
	}

	public static <T> String makeString(Collection<T> collection, String separator) {
		if (collection == null)
			return null;
		StringBuilder result = new StringBuilder();
		int indexOfLastElement = collection.size() - 1;
		ArrayList<T> list = new ArrayList<T>(collection);
		for (int i = 0; i <= indexOfLastElement; i++) {
			result.append(list.get(i));
			if (IsNotLastElement(i, indexOfLastElement))
				result.append(separator != null ? separator : "");
		}

		return result.toString();
	}

	private static boolean IsNotLastElement(int index, int indexOfLastElement) {
		return index != indexOfLastElement;
	}

	public static <T> String makeString(Collection<T> collection, char separator) {
		return makeString(collection, String.valueOf(separator));
	}

	public static <T> String makeString(T[] collection) {
		return makeString(collection, "");
	}

	public static <T> String makeString(T[] collection, String separator) {
		if (collection == null)
			return null;
		
		ArrayList<T> list = new ArrayList<T>(collection.length);
		for (T i : collection)
			list.add(i);
		return makeString(list, separator);
	}

	public static <T> String makeString(T[] collection, char separator) {
		return makeString(collection, String.valueOf(separator));
	}

}