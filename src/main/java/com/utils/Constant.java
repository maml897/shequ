package com.utils;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Constant
{
	//coluumn
	public static int COLUMN_TYPE_TEXT = 0;// 普通文本

	public static int COLUMN_TYPE_YUNDAN = 1;// 运单号
	
	public static int COLUMN_TYPE_YUNDAN_COM = 3;// 运单公司

	public static int COLUMN_TYPE_PHONE = 2;// 手机号
	
	public static int COLUMN_TYPE_IMG = 4;// 图片

	//table
	public static int TABLT_TYPE_SYSTEM = 0;// 系统模板

	public static int TABLT_TYPE_USER = 0;// 用户创建
	
	
	public static List<String> rowNames = Arrays.asList("A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U");
	
	public static Map<Integer,String> columnType = new LinkedHashMap<>();
	
	static {
		columnType.put(COLUMN_TYPE_TEXT, "文本");
		columnType.put(COLUMN_TYPE_YUNDAN, "运单号");
		columnType.put(COLUMN_TYPE_YUNDAN_COM, "快递公司");
		columnType.put(COLUMN_TYPE_PHONE, "手机号");
	}
	
	
	//运单公司集合
	public static Map<String,String> coms = new LinkedHashMap<>();

	static {
		coms.put("天天快递", "tiantian");
		coms.put("天天", "tiantian");
		
		coms.put("申通快递", "shentong");
		coms.put("申通", "shentong");
		
		coms.put("中通快递", "zhogntong");
		coms.put("中通", "zhogntong");
		coms.put("中通快运", "zto56");
		
		coms.put("韵达快递", "yunda");
		coms.put("韵达", "yunda");
		
		coms.put("圆通快递", "yuantong");
		coms.put("圆通", "yuantong");
		
		coms.put("百世汇通", "huitong");
		
		coms.put("顺丰快递", "shunfeng");
		coms.put("顺丰", "shunfeng");
		
		coms.put("宅急送", "zjs");
		
		coms.put("德邦物流", "debang");
		coms.put("德邦", "debang");
		
		
		coms.put("京东快递", "jingdong");
		coms.put("京东", "jingdong");
		coms.put("京东物流", "jingdong");
		
		coms.put("EMS", "ems");
		coms.put("邮政EMS", "ems");
		coms.put("邮政小包", "chinapost");
		
		coms.put("中铁", "zhongtie");
		coms.put("中铁快运", "zhongtie");
		
		coms.put("佳吉快运", "jiaji");
		coms.put("亚风快运", "airfex");
		coms.put("科捷物流", "itl");
		coms.put("平安达腾飞", "padtf");
		coms.put("壹米滴答", "yimidida");
		coms.put("安能物流", "ane56");
		coms.put("加运美速递", "jym56");
		coms.put("信丰物流", "xinfengwuliu");
		coms.put("联昊通速递", "lhtex");
		coms.put("全峰", "quanfeng");
		coms.put("香港邮政", "hkpost");
		coms.put("优速", "yousu");
		coms.put("如风达", "rufengda");
		coms.put("飞鹰物流", "feiying");
		coms.put("国通快递", "guotong");
		coms.put("长江国际速递", "changjiang");
		coms.put("增益速递", "zengyisudi");
		coms.put("天地华宇", "tiandihuayu");
		coms.put("速尔快递", "suer");
		coms.put("龙邦速递", "lbex");
		coms.put("快捷快递", "kjkd");
		coms.put("跨越速运", "kuayue");
		coms.put("日日顺", "rrs");
		coms.put("快客快递", "gotoquick");
	}
}