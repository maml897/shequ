package com.utils;

import static java.util.Map.Entry.comparingByKey;
import static java.util.stream.Collectors.toMap;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;

public class ComputeUtils {

	/**
	 * 平均分
	 * @param list
	 * @param mapper
	 * @return
	 */
	public static <T> double average(Collection<T> list, ToDoubleFunction<T> mapper) {
		return list.stream().mapToDouble(mapper).average().orElse(0);
	}

	/**
	 * 最高分
	 * @param list
	 * @param mapper
	 * @return
	 */
	public static <T> double top(Collection<T> list, ToDoubleFunction<T> mapper) {
		return list.stream().mapToDouble(mapper).max().orElse(0);
	}

	/**
	 * 最低分
	 * @param list
	 * @param mapper
	 * @return
	 */
	public static <T> double min(Collection<T> list, ToDoubleFunction<T> mapper) {
		return list.stream().mapToDouble(mapper).min().orElse(0);
	}

	/**
	 * 中位数
	 * @param list
	 * @param orderd
	 *            列表是否已经进行了排序，如果没有需要false
	 * @return
	 */
	public static double median(List<Double> list, boolean... orderd) {
		if (list.size() == 0) {
			return 0;
		}
		if (orderd == null || orderd.length == 0 || orderd[0]) {
			int number = list.size();
			double median = 0f;
			int x = number / 2;
			if (number % 2 == 0) {
				return (list.get(x - 1) + list.get(x)) / 2;
			} else {
				median = list.get(x);
			}
			return median;
		}

		// 无序列表快速得到中位数https://www.cnblogs.com/shizhh/p/5746151.html
		int heapSize = list.size() / 2 + 1;
		PriorityQueue<Double> heap = new PriorityQueue<>(heapSize);
		for (int i = 0; i < heapSize; i++) {
			heap.add(list.get(i));
		}

		for (int i = heapSize; i < list.size(); i++) {
			if (heap.peek() < list.get(i)) {
				heap.poll();
				heap.add(list.get(i));
			}
		}

		if (list.size() % 2 == 1) {
			return (double) heap.peek();
		} else {
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
	public static <T> double varience(Collection<T> list, ToDoubleFunction<T> mapper, double... averages) {
		double average = (averages == null || averages.length == 0) ? average(list, mapper) : averages[0];
		return list.stream().mapToDouble(item -> Math.pow(mapper.applyAsDouble(item) - average, 2)).average().orElse(0);
	}

	/***
	 * 标准差
	 * @param list
	 * @param mapper
	 * @param variences
	 * @return
	 */
	public static <T> double standard(Collection<T> list, ToDoubleFunction<T> mapper, double... variences) {
		if (variences != null && variences.length == 1) {
			return Math.sqrt(variences[0]);
		}
		double varience = varience(list, mapper);
		return Math.sqrt(varience);
	}

	/**
	 * 满分人数
	 * @param list
	 * @param predicate
	 * @return
	 */
	public static <T> int full(Collection<T> list, Predicate<T> predicate) {
		long count = list.stream().filter(predicate).count();
		return Convert.toInt(count);
	}

	/**
	 * 最简单的计算排序
	 * @param map 分数-数量,需要排序
	 * @return 分数-排名
	 */
	public static <T> Map<Double, Integer> computeOrderFromScoreCount(Map<Double, Long> map,boolean...ordereds) {
		boolean ordered = ordereds != null && ordereds[0];
		if(!ordered) {
			map = map.entrySet().stream().sorted(Collections.reverseOrder(comparingByKey()))
					.collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
		}
		
		Map<Double, Integer> studentIDOrder = new HashMap<>();
		long order = 1;
		for (Entry<Double, Long> entry : map.entrySet()) {
			studentIDOrder.put(entry.getKey(), Convert.toInt(order));
			order = order + entry.getValue();
		}
		return studentIDOrder;
	}

	/**
	 * 最简单的计算排序
	 * @param list 分数集合
	 * @return 分数-排名
	 */
	public static <T> Map<Double, Integer> computeOrder(List<Double> list) {
		Map<Double, Long> map = LambdaUtils.groupby(list, x -> x, Collectors.counting());// 每个分对应多少个学生
		return computeOrderFromScoreCount(map);
	}

	/**
	 * 个面具具体业务计算排名
	 * @param list 带分数的业务列表数据
	 * @param scoreFun 获取分数的逻辑
	 * @param orderCon 设置排名的逻辑
	 */
	public static <T> void computeOrder(List<T> list, Function<T, Double> scoreFun, Consumer<Integer> orderCon) {
		List<Double> scores = LambdaUtils.list2list(list, scoreFun);
		Map<Double, Integer> studentIDOrder = computeOrder(scores);
		list.forEach(item -> orderCon.accept(studentIDOrder.get(scoreFun.apply(item))));
	}

	/**
	 *  差异系数，变异系数，最终保留默认2位
	 * @param standard 保留下属位数之后的,4位
	 * @param average 保留小数位数之后的，2位
	 * @return
	 */
	public static double cv(double standard, double average, int... scales) {
		int scale = (scales == null || scales.length == 0) ? 2 : scales[0];
		standard = MathUtils.round(standard, 4);
		average = MathUtils.round(average);
		return MathUtils.round(MathUtils.div(standard, average), scale);
	}

	/**
	 *  全距，最终保留2位
	 * @param max 保留下属位数之后的2位
	 * @param min 保留小数位数之后的，2位
	 * @return
	 */
	public static double range(double max, double min) {
		max = MathUtils.round(max);
		min = MathUtils.round(min);
		return MathUtils.round(MathUtils.sub(max, min));
	}

	/**
	 *  难度,默认保留4位
	 * @param average 保留下属位数之后的2位
	 * @param fullScore 保留小数位数之后的，2位
	 * @return
	 */
	public static double difficulty(double average, double fullScore, int... scales) {
		int scale = (scales == null || scales.length == 0) ? 4 : scales[0];
		average = MathUtils.round(average);
		fullScore = MathUtils.round(fullScore);
		return MathUtils.round(MathUtils.div(average, fullScore), scale);
	}

	/**
	 * 离均差,默认保留4位
	 * @param average1 保留下属位数之后的2位
	 * @param average2 保留小数位数之后的，2位
	 * @return
	 */
	public static double dfa(double average1, double average2, int... scales) {
		int scale = (scales == null || scales.length == 0) ? 4 : scales[0];
		average1 = MathUtils.round(average1);
		average2 = MathUtils.round(average2);
		return MathUtils.round(MathUtils.sub(average1, average2), scale);
	}

	/**
	 * 离均差率,默认保留4位
	 * @param average1 保留下属位数之后的2位
	 * @param average2 保留小数位数之后的，2位
	 * @return
	 */
	public static double dfaL(double dfa, double average2, int... scales) {
		return MathUtils.round(MathUtils.div(dfa, average2), scales);
	}

	/**
	 * 获取一个小题的区分度
	 * @param orderedStudentIds 排序好的的待计算的学生列表，一般是按照总分或者单科总分排序
	 * @param ts 一般是指 questionStudent
	 * @param t2studentID  
	 * @param t2questionID
	 * @param t2score
	 * @param score 小题满分
	 * @return  奇葩，statistics是去除0分的人数，orderedStudentIds是包含0分的
	 */
	public static <T> double[] discrimination(List<Long> orderedStudentIds, List<T> ts, Function<T, Long> t2studentID, Function<T, Float> t2score, float score) {
		int number = orderedStudentIds.size();
		if (number == 0) {
			return new double[] { 0, 0, 0, 0, 0 };
		}

		// 这里实现的有问题，hnum和lnum应该是一样的，比如100个人，那么前27% 和后27%是一样的都是 27个人。
		int hnum = number * 27 / 100;
		int lnum = number - number * 73 / 100;
		if (hnum + lnum > 3 && lnum > 0 && hnum > 0) {
			// 前27%
			List<Long> hstudentList = CollUtil.sub(orderedStudentIds, 0, hnum);
			Set<Long> sets = new HashSet<>(hstudentList);
			List<T> hs = LambdaUtils.filter(ts, x -> sets.contains(t2studentID.apply(x)));
			double havg = hs.stream().mapToDouble(x -> t2score.apply(x)).average().orElse(0);

			// 后27%
			List<Long> lstudentList = CollUtil.sub(orderedStudentIds, number - lnum, number);
			Set<Long> sets1 = new HashSet<>(lstudentList);
			List<T> ls = LambdaUtils.filter(ts, x -> sets1.contains(t2studentID.apply(x)));
			double lavg = ls.stream().mapToDouble(x -> t2score.apply(x)).average().orElse(0);

			double result = (havg - lavg) / score;
			return new double[] { hnum, havg, lnum, lavg, result };
		}

		return new double[] { 0, 0, 0, 0, 0 };
	}

	// 难度指数的计算
	public static <T> double difficultyIndex(List<T> list, Function<T, Double> t2totalscore, Function<T, Double> t2score, double full, double max, double min) {
		if (full == 0) {
			return 0;
		}
		List<T> result = LambdaUtils.filter(list, x -> {
			double totalscore = t2totalscore.apply(x);
			if (totalscore <= max && totalscore > min) {
				return true;
			}
			return false;
		});

		double difficulty = 0;
		try {
			double average = result.stream().mapToDouble(map -> t2score.apply(map)).average().orElse(0);
			difficulty = (float) average / full;
		} catch (Exception e) {

		}
		return difficulty;
	}

	//信度
	void reliability(){
		
	}
	
	public static void main(String[] args) {
		List<Double> list = Arrays.asList(70d, 70d, 80d, 80d, 50d, 50d, 50d);
		System.out.println(computeOrder(list));
		
		Map<String,String> map =new HashMap<>();
		
		System.out.println(map.getClass().isAssignableFrom(LinkedHashMap.class));
		
		System.out.println(map instanceof LinkedHashMap);
	}
}
