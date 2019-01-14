package org.dark.sort.linearSort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.Vector;

/**
 * @Title		: 桶排序算法
 * 				  本例中原始數據容器為list。其實使用數組也可以
 * 				  但是真正存放結果的集合應該是數組。如果不怕麻煩就用list也咩問題。
 * 				  原理見cs方法的說明。
 * 				  
 * 					说说理解：
 * 					其实我觉得，桶排序的一个经典应用，就是hashMap。
 * 					先说说java中对hashCode的实现：
 * 					我查看了java的hashCode方法。这个方法可以获取每个对象的hash值。
 * 					java获取hashCode是用c++代码实现。其实质就是根据内存地址，通过计算转换为一个整数。
 * 					再说说java中对hashCode的使用：
 * 					桶排序是根据待排数据的范围，确定桶数量。
 * 					而java中的hashMap是预先给定桶的数量，然后仅提供桶数量之内的服务，超过了就需要扩容hashMap
 * 
 * @Description: 
 * @author 		: liwei
 * @date 2018年12月17日
 */
public class BucketSort {

	/**
	 * 內部類，實現鏈錶
	 * @Title		:	LinkNode
	 * @Description:	当前类的目的，是为了给那种桶数量不充足的情况下，计算出的key产生碰撞时存储相同key的二级容器
	 * 					与桶排序本身无关。
	 * 					具体实现见 com.troila.dataStrutcture.linear.SingleLinkedList
	 * @author 		:	liwei
	 * @date		:	2018年12月28日
	 */
	private class LinkNode{
		
	}
	
	/**
	 * @Description: 初始化數據
	 * 				  並將初始化後的數據傳入桶排核心方法。
	 * @param		:
	 * @return		: Object
	 */
	public void initData() {
		//一個是打算造出的數組的長度，一個是隨機數的範圍。
		Integer length = 500 , randomRange = 300;
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
//			System.out.println("=========================華麗麗的分割線=========================");
//			bs1(list);
			
			System.out.println("=========================華麗麗的二次分割線=========================");
			bs2(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 桶排序的核心方法
	 * 					1：	通用思路：桶排序其實是線性排序的一種，因為它的時間複雜度突破了選擇排序的O(nLogn)的下限
	 * 							所以應該是一種比較快捷的排序算法。
	 * 								桶排序需要預先提供的n個桶，相當於給定了一套模糊的順序。
	 * 								因此我們為每個數據分桶的過程，就是第一次模糊排序的過程
	 * 								然後的桶內排序，就是二次清晰排序的過程。
	 * 								那么推到极限，當你的桶足夠多的時候，也就代表了第一次的排序足夠清晰
	 * 								若达到每個桶內只有一個數據，那麼所有桶內的數據一定是有序的了。
	 * 								因此，我們只需要一次循環就能讓待排序數據變得有序，因此桶排序的時間複雜度一定是線性的
	 * 					2：	適用場景
	 * 							2.1：	不適用：如果排序對象中存在極端數據，比如1,2,4,5,12,100000這種矬子裡出迪拜塔的行為就不合適
	 * 										鑒定方法：可參照我之前說的“計數排序”的鑒定方法。
	 * 											或數據間跨度太大，比如-200,12000，-7000,556000,12，-97，-501這樣的情況
	 * 												跨度大，而且數據間無序。則會造成有大量空桶存在。造成空間浪費。
	 *							2.2：	適用：	一組數據能夠均勻平滑的存在於一個不大的範圍內
	 *											hash算法其实就是桶排序的一个极端应用场景
	 *					3：	優點、限制與代價
	 *							3.1:	優點：	由於我們創建的桶是有序的，因此我們在為數據分桶的時候已經進行了一次模糊排序
	 *											只需要再進行第二次桶內小範圍的排序即可。
	 *							3.2：	代價	當存在足夠多的桶有序的桶，可以令每個桶內只存放待排數組的一個數據的時候。
	 *											問題就由對數組的排序操作變為了對桶的定位操作
	 *											那麼分桶操作可以看做是一個hash操作，或者是一次對定長數組的下標的定位操作。
	 *											這樣，時間複雜度會大幅度下降，但是空間複雜度會大幅度上升了。
	 *							3.3：	限制	最好只用於整型數據排序。
	 *											因為當你對浮點數排序使用這一方法的時候，必定在桶內二次排序時涉及到比較
	 *											若不想引入比較，可以通過將所有數據放大10的n次方倍解決，但那樣所需要的空間就太大了。
	 * 					4：	邏輯算法：	1：創建一組有序的桶（數組），分為以下兩種情況
	 * 										1.1：桶數量充足，數量甚至是待排數組中元素個數的幾倍，目的是讓每個待排元素存在於唯一的桶
	 * 											這樣不需要桶內部的二次排序，但是通用性不強，
	 * 										1.2：桶數量小於待排元素數量。這時需要二次排序
	 * 									2：將待排數據放到對應的桶中
	 * 										2.1：桶數量充足，那麼可以將待排數據根據桶的位置（數組的下標）存入對應桶即可
	 * 										2.2：桶數量不足，需要在桶內創建鏈錶或者數組，來存放同一桶內的多條數據
	 * 									3：遍歷非空桶
	 * 										3.1：桶數量充足時，只需要把每個桶的數據按順序放到原來的待排數組的位置即完成排序。
	 * 										3.2：桶數量不足時，首先要把所有非空桶內部的所有數據排序，然後遍歷每個非空桶
	 * 												然後按 桶內順序-->桶間順序 的次序將所有數據放回到原來的待排數組的位置即可。 
	 * 					5：	操作方法：
	 * 						本例中，會提供上面邏輯算法所提到兩種極端情況的實現方式。
	 * 						一切以代碼說明，不在此再贅述文字說明了。	
	 * 					
	 * 								
	 */
	public void bs1(List<Integer> list) {
		//分別代表：待排序元素數量、兩個最值、桶數量
		Integer size = 0, min = -1, max = -1, length = 0;
		List<Integer>[] buckets = null;
		
		try {
			size = list.size();
			min = list.get(0);
			
			//一次遍歷，確定最值，確定桶數量範圍
			for(Integer tmp : list) {
				if(min > tmp)
					min = tmp;
				if(max < tmp)
					max = tmp;
			}
			
			length = max - min + 1;
			buckets = initBucket1(length);
			
			//二次遍歷，將待排數組中的每個元素放入到桶中
			for(Integer tmp : list) {
				if(null == buckets[tmp - min]) {
					buckets[tmp - min] = new ArrayList<Integer>();
				}
				buckets[tmp - min].add(tmp);
			}
			
			//三次遍歷，將所有非空的桶內數據放回到待排數組中或簡單的展示出来
			list.clear();
			for(int j=0; j<length; j++) {
				if(null != buckets[j]) {
					for(Integer tmp : buckets[j]) {
						list.add(tmp);
					}
				}
			}
			
			//四次遍歷，顯示排序後效果
			for(Integer tmp : list) {
				System.out.println(tmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 	初始化桶充足的情況
	 * 					關鍵的一步根據兩端最值確定桶的個數。（已由外層調用處理）
	 * 					這樣一定不會有不同的數字在同一個桶內，但卻會產生桶內有相同數字的情況。
	 * 					為了解決桶內重複數的問題，需要在每個桶內添加一個容器
	 * 					因此這個方法就是初始化桶，但沒有初始化桶內的各個容器
	 */
	public List<Integer>[] initBucket1(Integer length){
		//定義一組List類型的數組，每個List存放待排數組中的相同記錄
		List<Integer>[] buckets = null;
		
		buckets = new ArrayList[length];
		//為了節約空間，所以暫時不在這裡初始化每個ArrayList。等到需要向ArrayList中放數據的時候再判斷並初始化。
//		for(int i=0; i<length; i++) {
//			arr[i] = new ArrayList<Integer>();
//		}
		
		return buckets;
	}
	
	/**
	 * @Description: 	初始化桶不足的情況
	 * 					數量由我任性的隨便規定就好啦
	 */
	public void bs2(List<Integer> list) {
		//分別代表：待排序元素數量、兩個最值、桶數量
		Integer size = 0, min = -1, max = -1, length = 0;
		List<Integer>[] buckets = null;
		
		try {
			size = list.size();
			min = list.get(0);
			length = 10;
			
			//一次遍歷，計算總數據跨度
			for(Integer tmp : list) {
				if(min > tmp)
					min = tmp;
				if(max < tmp)
					max = tmp;
			}

			buckets = initBucket2(length);
			
			//二次遍歷，將待排數組中的每個元素放入到桶中
			for(Integer tmp : list) {
				if(null == buckets[tmp - min]) {
					buckets[tmp - min] = new ArrayList<Integer>();
					//這裡的規則我隨意寫的。作用是將一些數據放到某個桶裡。其目的只是為了初步排序並縮小每個桶的範圍。
					buckets[ tmp * length / (max + 1) ].add(tmp);
				}else {
					
				}
				
			}
			
			//三次遍歷，將所有非空的桶內數據放回到待排數組中或簡單的展示出来
			list.clear();
			for(int j=0; j<length; j++) {
				if(null != buckets[j]) {
					for(Integer tmp : buckets[j]) {
						list.add(tmp);
					}
				}
			}
			
			//四次遍歷，顯示排序後效果
			for(Integer tmp : list) {
				System.out.println(tmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 	初始化桶不足的情況
	 * 					數量由我任性的隨便規定就好啦（就是那個傳入的length，只是這裡的length長度是拍腦袋決定的）
	 * 					需要在每個桶內添加一個容器，但沒有初始化桶內的各個容器
	 */
	public List<Integer>[] initBucket2(Integer length){
		//定義一組List類型的數組，每個List存放待排數組中的相同記錄
		List<Integer>[] buckets = null;
		
		buckets = new ArrayList[length];
		//為了節約空間，所以暫時不在這裡初始化每個ArrayList。等到需要向ArrayList中放數據的時候再判斷並初始化。
//				for(int i=0; i<length; i++) {
//					arr[i] = new ArrayList<Integer>();
//				}
		
		return buckets;
	}
	
	public static void main(String[] args) {
		BucketSort bs = new BucketSort();
		bs.initData();
		Map map = new HashMap();
		TreeMap tm = new TreeMap<Object, Object>();
		Vector v = new Vector<Object>();
	}

}
