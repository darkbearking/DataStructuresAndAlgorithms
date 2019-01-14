package com.troila.sort.compareSort;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.troila.dataStrutcture.linear.SingleLinkedList;


/**
 * 
 * @Title		:	實現插入排序算法
 * 				 	數據容器為數組。當然也可以使用list或者使用鏈錶，但是array的查找效率更高。
 * 				 	插排原理：見is方法的說明
 * @Description:	
 * @author 		:	liwei
 * @date		:	2019年1月7日
 */
public class InsertSort {

	Integer[] array = null;
	//一個是打算造出的元素的個數，一個是隨機數的範圍。
	Integer length = 30 , randomRange = 9000;
	
	public void initArray() {
		array = new Integer[length];
		for(int i=0; i<length; i++) {
			Integer num = new Random().nextInt(randomRange);
			array[i] = num;
			System.out.println(array[i]);
		}
		System.out.println("=========================華麗麗的分割線=========================");
		this.isArray();
	}
	
	/**
	 * @Description:	插入排序的核心方法
	 * 					1：	通用思路：插排其實是比較法的一種實現，需要不斷將無序部分的元素與有序部分的最前或最後元素比較
	 * 						它的時間複雜度並不穩定，最優狀態為O(n*log(n))。最壞是N*N
	 * 					2：	操作方法：
	 * 							把待排數組劃分為兩部分：有序與無序
	 * 							最初，將原始數組的第一個元素作為有序部分，將原始數組的剩餘部分視作無序部分
	 * 							然後，逐個遍歷無序部分的所有元素。
	 * 								將無序部分的每個元素與有序部分的每個元素進行比較，並且將其置於有序部分的正確位置。
	 * 								直至無序部分的所有元素都置於有序部分的適當位置。排序結束。
	 * 					3：	思想：	當前方法使用數組作為集合來進行插排。
	 * 							數組的好處在於可以快速查找定位元素。
	 * @param		:	
	 * @return		:	Object
	 */
	public void isArray() {
		//創建有序集合，用list存放數據。因list本身就是鏈錶，裡面的數據的順序就是放入的順序。
		List<Integer> list = new ArrayList<Integer>();
		list.add(array[0]);
		
		//遍歷原始數組，逐個元素與有序數組做比較
		for(int j=1; j<length; j++) {
			int size = list.size();
			for(int k=0; k<size; k++) {
				if(array[j] < list.get(k)) {
					list.add(k, array[j]);
					break;
				}
			}
		}
		
		Iterator<Integer> i = list.iterator();
		while(i.hasNext()) {
			System.out.println(i.next());
		}
		
		LinkedList<Integer> link = null;
	}
	
	/**
	 * @Description:	以單向鏈表實現，後續考慮使用有序鏈表和雙向甚至雙端鏈表實現。
	 * 					因為單向鏈表雖然查找比較慢，但是優點是在主要向後遍歷的場景下，這個數據結構還是有點適合的。
	 * 					核心邏輯與算法不變。變的只是基礎數據結構。
	 * @param		:	
	 * @return		:	Object
	 */
	public void isLinkedList() {
		
	}
	
	public static void main(String[] args) {
		InsertSort is = new InsertSort();
		is.initArray();
	}
}
