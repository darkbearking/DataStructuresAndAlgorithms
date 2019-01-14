package org.dark.test;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @Title		:	测试数字前后补零输出的工具类
 * @Description:	
 * @author 		:	liwei
 * @date		:	2019年1月3日
 */
public class NumberFormatTest {

	public static void padZeroBefore1() {
		// 待测试数据
		int i = 1;
		// 得到一个NumberFormat的实例
		NumberFormat nf = NumberFormat.getInstance();
		// 设置是否使用分组
		nf.setGroupingUsed(false);
		// 设置最大整数位数
		nf.setMaximumIntegerDigits(4);
		// 设置最小整数位数
		nf.setMinimumIntegerDigits(4);
		// 输出测试语句
		System.out.println("前补零方法一："+nf.format(i));
	}

	public static void padZeroBefore2() {
		int youNumber = 1;
		// 0 代表前面补充0
		// 4 代表长度为4
		// d 代表参数为正数型
		String str = String.format("%04d", youNumber);
		System.out.println("前补零方法二："+str); // 0001
	}
	
	public static void padZeroBefore3() {
		int i = 1;
		DecimalFormat g1=new DecimalFormat("0000"); 
		String startZeroStr = g1.format(i); 
		System.out.println("前补零方法三："+startZeroStr);
	}
	
	public static void padZeroAfter1() {
		int i = 1;
		DecimalFormat g2=new DecimalFormat("0.000");
		String endZeroStr = g2.format(i);
		System.out.println("后补零："+endZeroStr);
		System.out.println("虽然后补零出现这种情况,带有小数点");
		System.out.println("比如你要长度要在4位以内，可以这么做");
		System.out.println("后补零转变后："+endZeroStr.replace(".","").substring(0,4));
	}

	public static void main(String[] args) {
		NumberFormatTest.padZeroBefore1();
		NumberFormatTest.padZeroBefore2();
		NumberFormatTest.padZeroBefore3();
		NumberFormatTest.padZeroAfter1();
	}
}
