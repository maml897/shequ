package com.utils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class LambdaUtils
{
	// list 转 map，指定一个属性当key，value默认U
	public static <T, U> Map<T, U> list2map(List<U> list, Function<U, T> key)
	{
		return list2map(list, key, x -> x);
	}

	// list 转 map，指定一个属性当key，执行一个属性当value
	public static <U, T, K> Map<T, K> list2map(List<U> list, Function<U, T> key, Function<U, K> value)
	{
		return list.stream().collect(Collectors.toMap(key, value, (key1, key2) -> key2, LinkedHashMap::new));
	}

	// list抽取属性
	public static <U, T> List<T> list2list(Collection<U> list, Function<U, T> fun)
	{
		return list.stream().map(fun).collect(Collectors.toList());
	}

	// 过滤
	public static <U> List<U> filter(Collection<U> list, Predicate<U> keyExtractor)
	{
		return list.stream().filter(keyExtractor).collect(Collectors.toList());
	}

	/**
	 * 自定义分组，可以分组-数量等
	 * @param list
	 * @param groupExtractor
	 * @param c
	 * @return
	 */
	public static <T, U, K> Map<U, K> groupby(Collection<T> list, Function<T, U> groupExtractor, Collector<T, ?, K> c)
	{
		return list.stream().collect(Collectors.groupingBy(groupExtractor, LinkedHashMap::new,c));
	}
	
	/**
	 * 单层分组
	 * @param list
	 * @param keyExtractor
	 * @return
	 */
	public static <T, U> Map<U, List<T>> groupby(List<T> list, Function<T, U> keyExtractor)
	{
		return list.stream().collect(Collectors.groupingBy(keyExtractor, LinkedHashMap::new, Collectors.toList()));
	}
	
	/**
	 *  单层分组之后转map
	 * @param list
	 * @param groupExtractor
	 * @param keyExtractor
	 * @return
	 */
	public static <T, U, K> Map<U, Map<K, T>> groupby2map(List<T> list, Function<T, U> groupExtractor, Function<T, K> keyExtractor)
	{
		return list.stream().collect(Collectors.groupingBy(groupExtractor, LinkedHashMap::new, Collectors.toMap(keyExtractor, x -> x, (key1, key2) -> key2, LinkedHashMap::new)));
	}
	
	/**
	 * 二层分组
	 * @param list
	 * @param groupExtractor
	 * @param keyExtractor
	 * @return
	 */
	public static <T, U, K> Map<U, Map<K, List<T>>> groupby(List<T> list, Function<T, U> groupExtractor, Function<T, K> keyExtractor)
	{
		return list.stream().collect(Collectors.groupingBy(groupExtractor, LinkedHashMap::new, Collectors.groupingBy(keyExtractor, LinkedHashMap::new, Collectors.toList())));
	}
	
	/**
	 * 二层分组之后转map
	 * @param list
	 * @param firstKey
	 * @param secondKey
	 * @param mapKey
	 * @return
	 */
	public static <T, U, K,A> Map<U, Map<K, Map<A, T>>> groupby3(List<T> list, Function<T, U> firstKey, Function<T, K> secondKey, Function<T,A> mapKey) {
		return list.stream().collect(Collectors.groupingBy(firstKey, LinkedHashMap::new,  Collectors.groupingBy(secondKey, LinkedHashMap::new, Collectors.toMap(mapKey, x -> x, (key1, key2) -> key2, LinkedHashMap::new))));
	}
	
	
	/**
	 * boolean分组
	 * @param list
	 * @param keyExtractor
	 * @return
	 */
	public static <T> Map<Boolean, List<T>> groupbyboolean(List<T> list, Predicate<T> keyExtractor)
	{
		return list.stream().collect(Collectors.partitioningBy(keyExtractor));
	}
}