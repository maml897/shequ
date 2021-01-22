package com.utils;

import cn.hutool.core.util.NumberUtil;

public class MathUtils {
	/**
	 * 加法
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double add(double v1,double v2) {
		return NumberUtil.add(v1, v2);
	}
	
	/**
	 * 减法
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double sub(double v1,double v2) {
		return NumberUtil.sub(v1, v2);
	}
	
	/**
	 * 乘法
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double mul(double v1,double v2) {
		return NumberUtil.mul(v1, v2);
	}
	
	/**
	 * 除法
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static double div(double v1,double v2) {
		if(v2==0) {
			return 0;
		}
		return NumberUtil.div(v1, v2);
	}
	
	/**
	 * 保留几位小数
	 * @param d
	 * @param scales
	 * @return
	 */
	public static double round(double d,int...scales) {
		int scale = (scales == null || scales.length == 0) ? 2 : scales[0];
		return NumberUtil.round(d, scale).doubleValue();
	}
	
	/**
	 * 百分比展示，数值*100，保留位数，后面加%
	 * @param d
	 * @param scales
	 * @return
	 */
	public static String percent(double d,int...scales) {
		int scale = (scales == null || scales.length == 0) ? 2 : scales[0];
		double result=round(mul(d, 100), scale);
		return format(result)+"%";
	}
	
	/***
	 * 去掉数字后面的0
	 * @param f
	 * @return
	 */
	private static String format(double f,int...scales)
	{
		f = round(f, scales);
		int i = (int) f;
		if (i == f)
		{
			return i + "";
		}
		return f + "";
	}
	
	public static void main(String[] args) {
		System.out.println(round(div(10,2)));;
		System.out.println(format(100.40));;
	}
}
