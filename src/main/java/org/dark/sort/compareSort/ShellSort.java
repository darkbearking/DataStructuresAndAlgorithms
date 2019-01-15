package org.dark.sort.compareSort;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Title		:	實現希爾排序算法
 * 				 	數據容器為數組，因為中間會多次重新選取步長並根據步長獲取分組後的數據，數組的下標可以很好地滿足這點
 * 				 	希排原理：見ss方法的說明
 * @Description: 
 * @author 		: liwei
 * @date 2019年01月14日
 */
public class ShellSort {

	//定義數據元素容器
	Integer[] arr = null;
	//一個是打算造出的數組的長度，一個是隨機數的範圍。
	int length = 200 , randomRange = 6000;
	
	/**
	 * @Description: 初始化數據
	 * 				  並將初始化後的數據傳入希排核心方法。
	 * @param		:
	 * @return		: Object
	 */
	public void initData() {
		try {
			arr = new Integer[length];
			for(int i=0; i<length; i++) {
				Integer num = new Random().nextInt(randomRange);
				arr[i] = num;
			}
			for(int j=0; j<length; j++) {
				System.out.println(arr[j]);
			}

			ss();
			System.out.println("=========================華麗麗的分割線=========================");
			for(int j=0; j<length; j++) {
				System.out.println(arr[j]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 希爾排序的核心方法
	 * 					1：	通用思路：
	 * 						1.1：	先選取步長，或者叫分組數量，然後根據步長，將全量數組分為幾部分，或者叫幾組
	 * 									舉例說明，有一百條數據，第一次選取步長為50則說明分為50組。
	 * 									然後100/50=2，2說明每組中記錄條數是2條，記錄與記錄間隔50個元素。
	 * 						1.2：	在當前步長下進入三重循環
	 * 								第一層循環:遍歷所有組
	 * 								第二層循環:遍歷組中每個元素，
	 * 									若當前一個元素大於後一個元素時，交換兩個元素
	 * 										我們稱交換後較小的數據為小數，較大數據為大數，同時進入第三層循環。
	 * 										第三層循環範圍為本組中所有站位在小數之前的元素。
	 * 										第三層循環目的是用小數逐個與之比較，小數小則交換，反之則跳出第三層循環。
	 * 									否則，向前遍歷即可
	 * 						1.3：	當前步長下的所有分組遍歷完畢後，縮小步長重複上面的1.1與1.2步。直至步長=1.
	 * 					2：	優勢：	實現代碼的邏輯簡單中等規模的數據量下表現良好。
	 * 					3：	分析與心得
	 * 						3.1：	從上面的操作過程來看，貌似直接把步長設置為1不是更直接麼
	 * 								但是仔細想想，正因為前面的分組和排序，而且每次交換是在小範圍內操作，才讓數組從整體上趨於有序。
	 * 								否則，直接讓步長為1，那麼在最壞的情況下，需要進行的交換操作次數會過於龐大
	 * 					4：	補充
	 * 						4.1：	都說本算法適用於中等規模的數據量
	 * 								並且對swap空間的需求量不是很大。
	 * 						4.2：	可以考慮一套算法，根據數據總數來決定size 每次增加的量
	 * 								進而決定步長的大小，以避免過多的循環與比較次數。
	 */
	public void ss() {
		//定義步長
		int interval = 2;
		//每組元素數量，每次倍增
		int size = 2;
		//臨時變量
		int temp = 0;
		
		//每次改變步長
		while(interval > 1) {
			interval = length / size <= 0 ? 1 : length / size;
			//進入第一層循環
			for(int i = 0; i < interval; i++) {
				//進入第二層循環
				for(int j=i; j<length; j=j+interval) {
					if(j+interval < length && arr[j] > arr[j+interval]) {
						temp = arr[j];
						arr[j] = arr[j+interval];
						arr[j+interval] = temp;
						//進入第三層循環
						for(int k=j; k>0; k=k-interval) {
							if(k-1 >=0 && arr[k] < arr[k-1]) {
								temp = arr[k];
								arr[k] = arr[k-1];
								arr[k-1] = temp;
							}else {
								break;
							}
						}
					}
				}
			}
			//這種寫法，會令size兩次與2做乘法。但是沒關係，循環越往後，步子可以越大一些
			size *= 2 > length ? length : size;
		}
		
	}
	
	public static void main(String[] args) {
		ShellSort ss = new ShellSort();
		ss.initData();
	}
}
