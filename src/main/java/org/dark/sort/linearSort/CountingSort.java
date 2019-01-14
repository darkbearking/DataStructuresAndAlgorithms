package org.dark.sort.linearSort;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Title		: 計數排序算法
 * 				  原始數據容器為list。其實使用數組也可以
 * 				  但是真正存放結果的集合應該是數組。如果不怕麻煩就用list也咩問題。
 * 				  原理見cs方法的說明。
 * 				  
 * @Description: 
 * @author 		: liwei
 * @date 2018年12月14日
 */
public class CountingSort {
	/**
	 * @Description: 初始化數據
	 * 				  並將初始化後的數據傳入記排核心方法。
	 * @param		:
	 * @return		: Object
	 */
	public void initData() {
		//一個是打算造出的數組的長度，一個是隨機數的範圍。
		Integer length = 60 , randomRange = 20;
		List<Integer> list = null;
		
		try {
			list = new ArrayList<Integer>();
			for(int i=0; i<length; i++) {
				Integer num = new Random().nextInt(randomRange);
				list.add(num);
			}
			for(int j=0; j<length; j++) {
				System.out.println(list.get(j));
			}
			System.out.println("=========================華麗麗的分割線=========================");
			cs(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 計數排序的核心方法
	 * 					1：	通用思路：計數排序其實是線性排序的一種，因為它的時間複雜度突破了選擇排序的O(nLogn)的下限
	 * 							所以應該是一種比較快捷的排序算法。
	 * 					2：	適用場景
	 * 							2.1：	不適用：如果排序對象中存在極端數據，比如1,2,4,5,12,100000這種矬子裡出迪拜塔的行為就不合適
	 * 										鑒定方法：1	比如，兩最值數據的差距較大，但是數組的length又較小的時候，就不適合
	 * 												  2	再比如，以原始數組中的位置作為橫坐標，以其值作為縱坐標
	 * 														如果某位置的數據的斜率有暴增，則不適用（斜率求法如下
	 * 														以其值為y值，以其橫向步長(橫坐標減去它前一個元素的坐標)為x，斜率=y/x
	 * 														斜率的閾值可以根據自己的實際情況去具體設置，在此不做建議。）
	 *							2.2：	適用：	如果有較多重複數據，兩端最值差距不大，
	 *											待排序數組中數據量較大，最好是總數據量遠大於兩最值之差的時候
	 *											個人感覺如果數組中都為整數可能效果更好，如果是浮點數，那麼桶排序會更適合
	 * 					3：	邏輯算法：以數組中的兩個最值之差作為新數組的length，以兩個最值作為新數組的起點與終點。
	 * 							然後將原始數組的每個元素的值與起點的坐標值（注意這兩個）做減法
	 * 							得出的差值其實就是原數組的元素在新數組中的位置，然後在這個位置上加一。
	 * 					4：	操作方法：
	 * 						2.1：	通過一輪循環，找到兩個最值
	 * 						2.2：	求最值的差，根據差值創造兩個新數組，分別為表頭數組和表格數組
	 * 						2.3：	循環原數組，獲取每位對應的數據與最小值的差，差值就是新數組的位置
	 * 						2.4：	在新數組的對應數位上++
	 * 						2.5：	全部循環完畢後，就能知道哪個數在哪個位置了。也就相當於排序完畢了。
	 * 								
	 */
	public void cs(List<Integer> list) {
		//原數組的長度與新數組的長度
		Integer sizeOriginal = 0 , sizeNew = 0;;
		Integer min = 0 ,max = 0;
		Integer[] arrHead = null ,arrBody = null;
		
		try {
			sizeOriginal = list.size();
			//第一輪循環，獲取最值
			for(Integer tmp : list) {
				if(tmp > max)
					max = tmp;
				if(tmp < min)
					min = tmp;
			}
			
			//創建表頭與表體數組，以最值為上下界。
			sizeNew = max - min + 1;
			arrHead = new Integer[sizeNew];
			arrBody = new Integer[sizeNew];
			
			//第二輪循環新數組，做兩件事
			//1：為表頭數組賦初值
			//2：為表體數組所有元素賦初值0。
			for(int i=0; i<sizeNew; i++) {
				arrHead[i] = min + i;
				arrBody[i] = 0;
			}
			
			//第三輪循環，獲取原值與最小值的差，然後在表體數組的對應位上+1，意為找到了一個某某數字
			for(Integer tmp : list) {
				arrBody[tmp - min] ++;
			}
			
			//最後一輪循環，遍歷新數組，同時打印出表頭和表體中各個數據的個數。
			for(int i=0; i<sizeNew; i++) {
				System.out.println(arrHead[i] + " : " + arrBody[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		CountingSort cs = new CountingSort();
		cs.initData();
	}
}
