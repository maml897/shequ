package com.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

import cn.hutool.core.convert.Convert;

public class MathUtils {
	/**
	 * 平均分
	 * @param list
	 * @param mapper
	 * @return
	 */
	public static <T> double average(Collection<Double> list) {
		return list.stream().mapToDouble(x->x).average().orElse(0);
	}
	
	/**
	 * 最高分
	 * @param list
	 * @param mapper
	 * @return
	 */
	public static <T> double top(Collection<Double> list) {
		return list.stream().mapToDouble(x->x).max().orElse(0);
	}

	/**
	 * 最低分
	 * @param list
	 * @param mapper
	 * @return
	 */
	public static <T> double min(Collection<Double> list) {
		return list.stream().mapToDouble(x->x).min().orElse(0);
	}
	
	/**
	 * 中位数
	 * @param list
	 * @param orderd
	 *            列表是否已经进行了排序，如果没有需要false
	 * @return
	 */
	public static double median(List<Double> list, boolean... orderd)
	{
		if (list.size() == 0)
		{
			return 0;
		}
		if (orderd == null || orderd.length == 0 || orderd[0])
		{
			int number = list.size();
			double median = 0f;
			int x = number / 2;
			if (number % 2 == 0)
			{
				return (list.get(x - 1) + list.get(x)) / 2;
			}
			else
			{
				median = list.get(x);
			}
			return median;
		}

		// 无序列表快速得到中位数https://www.cnblogs.com/shizhh/p/5746151.html
		int heapSize = list.size() / 2 + 1;
		PriorityQueue<Double> heap = new PriorityQueue<>(heapSize);
		for (int i = 0; i < heapSize; i++)
		{
			heap.add(list.get(i));
		}

		for (int i = heapSize; i < list.size(); i++)
		{
			if (heap.peek() < list.get(i))
			{
				heap.poll();
				heap.add(list.get(i));
			}
		}

		if (list.size() % 2 == 1)
		{
			return (double) heap.peek();
		}
		else
		{
			return (double) (heap.poll() + heap.peek()) / 2.0;
		}
	}

	/**
	 * 方差
	 * @param list
	 * @param mapper
	 * @param averages
	 * @return
	 */
	public static double varience(Collection<Double> list,double... averages) {
		double average = (averages == null || averages.length == 0) ? average(list) : averages[0];
		return list.stream().mapToDouble(item -> Math.pow(item - average, 2)).average().orElse(0);
	}

	/***
	 * 标准差
	 * @param list
	 * @param mapper
	 * @param variences
	 * @return
	 */
	public static double standard(Collection<Double> list, double... variences) {
		if (variences != null && variences.length == 1) {
			return Math.sqrt(variences[0]);
		}
		double varience = varience(list);
		return Math.sqrt(varience);
	}

	/**
	 * 满分人数
	 * @param list
	 * @param predicate
	 * @return
	 */
	public static <T> int full(Collection<Double> list, double fullScore) {
		long count = list.stream().filter(x->x==fullScore).count();
		return Convert.toInt(count);
	}
}
