package com.troila.dataStrutcture.linear;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.poi.hssf.record.formula.functions.T;

/**
 * @Title		:	雙向鏈錶
 * 					核心：	就是使用帶有前後指針的雙向節點，構建鏈錶
 * 					約定：	size表示鏈錶中元素總數，從1開始
 * 							index表示元素在鏈錶中的位置，從0開始
 * 					實現：	1：	這次我參照了別人的思路。
 * 								也就是雙向鏈錶的每個方法的入參，大部分是對象元素，而非鏈錶節點
 * 								節點的創建、與前後節點的掛載操作交給鏈錶的方法來實現
 * 								（之前的思路是，入參基本都是節點元素，然後鏈錶的方法只做掛載操作）
 * 								其實，感覺從設計模式的角度來說，我都可以弄一個工廠類來專門做“車廂”的生產工作。
 * 							2：	所有添加操作的底層，都是addByIndex。但是這個方法默認是添加在第index元素的前面
 * 								因此對於添加到尾部的操作相當於將元素添加到 鏈錶size+1的位置上
 * 					前提：	頭和尾節點可以是同一個，這點與單向以及雙端的設定不同
 * 					問題：	頭與尾是否應該指向自己？
 * 								首先，你想要的是否是一個閉環的雙向鏈錶？如果不是就不要考慮這些
 * 								若需要，那麼在刪除頭尾節點的時候，都需要對兩端的節點的前後指針做特殊處理。
 * 							
 * @Description:	
 * @author 		:	liwei
 * @date		:	2019年1月10日
 */
public class DoubleLinkedList<T> {
	//定義頭與尾
	private NodeOfDouble<T> head,tail;
	//定義總元素數量
	private AtomicInteger size = new AtomicInteger(0);

	/**
	 * @Title		:	無參構造
	 * @Description:
	 */
	public DoubleLinkedList() {
		head = null;
		tail = null;
		size = new AtomicInteger(0);
	}
	
	/**
	 * @Title		:	帶頭指針的構造
	 * 					沒有讓頭尾自關聯，從設計之初就沒有打算做成一個閉環雙向鏈錶
	 * @Description:
	 */
	public DoubleLinkedList(T data) {
		NodeOfDouble<T> node = new NodeOfDouble<T>(data, null, null);
		head = tail = node;
		size = new AtomicInteger(1);
	}
	
	/**
	 * @Description:	頭部添加元素
	 * 					如果，原本鏈錶為空，那麼頭與尾都使用這個新節點
	 * 					否則，用新節點作為頭，原有的頭指針向後移動
	 * 							在這個過程中，我們不需要關注尾節點是誰。因為尾部絕不會為空
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean addHead(T data) {
		return addByIndex(0,data);
	}
	
	/**
	 * @Description:	尾部添加元素
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean addTail(T data) {
//		boolean flag = false;
//		NodeOfDouble<T> nodeNew = null;
//		
//		try {
//			nodeNew = new NodeOfDouble<T>(data);
//			if(size.get() == 0) {
//				head = tail = nodeNew;
//				size = new AtomicInteger(1);
//				return true;
//			}else {
//				tail.next = nodeNew;
//				nodeNew.pre = tail;
//				tail = nodeNew;
//				size.getAndIncrement();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return flag;
		return addByIndex(getSize(),data);
	}
	
	/**
	 * @Description:	在目標節點前添加元素
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean addBeforeNode(T node, T data) {
		boolean flag = false;
		int index = 0;
		
		try {
			if((index = get(node)) < 0)
				return flag;
			flag = addByIndex(index, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	/**
	 * @Description:	在指定位置前添加元素
	 * 					0：	校驗鏈錶是否為空，為空則將元素封裝為頭結點插入鏈錶
	 * 					1：	首先調用getByIndex找到對應位置的元素，如果位置越界，會有底層函數控制
	 * 					2：	如果返回的結果不為空
	 * 						否則，說明鏈錶為空
	 * 					3：	node就是原來處於index位置的那個節點，但是我們找不到index節點的前一個節點，也沒必要找到它
	 * 						因此我們需要先把新節點的前驅指向node的前驅，新節點的後繼指向node。這套掛載關係建立後
	 * 						再用新節點替換node的前驅的後繼節點，用新節點充當node節點的前驅，邏輯完美
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean addByIndex(int index, T t) {
		boolean flag = false;
		NodeOfDouble<T> node = null ,nodeNew = null;
		
		try {
			nodeNew = new NodeOfDouble<T>(t);
			
			if(size.get() == 0) {
				head = tail = nodeNew;
				//這裡不用自增，以免多線程造成size大小與實際節點數量不符
				//因為兩個線程同時創造一個新head，但size會加加兩次。
				size = new AtomicInteger(1);
				return true;
			}
			else if(index == getSize()) {
				tail.next = nodeNew;
				nodeNew.pre = tail;
				tail = nodeNew;
				size.getAndIncrement();
				return true;
			}
			else {
				node = getByIndex(index);
				if(null != node) {
					nodeNew.pre = node.pre;
					nodeNew.next = node;
					if(index == 0) {
						head = nodeNew;
					}else {
						node.pre.next = nodeNew;
					}
					node.pre = nodeNew;
					size.getAndIncrement();
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	/**
	 * @Description:	頭部刪除元素
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean delHead() {
		return delByIndex(0);
	}
	
	/**
	 * @Description:	尾部刪除元素
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean delTail() {
		return delByIndex(size.get());
	}
	
	/**
	 * @Description:	任意位置刪除元素
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean del(T data) {
		boolean flag = false;
		int index = 0;
		
		try {
			if((index = get(data)) < 0)
				return flag;
			flag = delByIndex(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	/**
	 * @Description:	根據index刪除元素
	 * 					1：	首先調用方法校驗元素是否存在
	 * 						如果存在，則當前節點的前驅的後繼節點是當前節點的後繼節點
	 * 									當前節點的後繼的前驅節點是當前節點的前驅節點
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean delByIndex(int index) {
		boolean flag = false;
		NodeOfDouble<T> node = null;
		
		try {
			node = getByIndex(index);
			if(null != node) {
				if(index == getSize()) {
					tail = node.pre;
				}else {
					node.next.pre = node.pre;
				}
				if(index == 0) {
					head = node.next;
				}else {
					node.pre.next = node.next;
				}
				node.next = node.pre = null;
				size.getAndDecrement();
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}

	/**
	 * @Description:	頭部獲取元素
	 * @param		:	
	 * @return		:	boolean
	 */
	public NodeOfDouble<T> getHead() {
		return getByIndex(0);
	}
	
	/**
	 * @Description:	尾部獲取元素
	 * @param		:	
	 * @return		:	boolean
	 */
	public NodeOfDouble<T> getTail() {
		return getByIndex(size.get());
	}
	
	/**
	 * @Description:	根據條件獲取元素位置
	 * 					0：	如果鏈錶為空，返回負數，說明元素不存在
	 * 					1：	從頭開始遍歷所有非空的節點（為空說明已經到了尾部之後了）
	 * 						如果元素與當前節點的內容相同，跳出遍歷。index就是元素的位置
	 * 							不滿足條件則向後移動指針，同時index數量加一。直至遍歷鏈錶尾部
	 * 						如果index == 鏈錶的size，說明從頭到尾都沒有目標元素
	 * 							（若元素存在於最後一位，則index應該比size小1）
	 * 							那麼也返回負數
	 * @param		:	
	 * @return		:	int
	 */
	public int get(T data) {
		int index = 0;
		NodeOfDouble<T> node = null;
		
		try {
			if(ifEmpty()) {
				index = -1;
			}
			else {
				node = head;
				while(null != node) {
					if(data.equals(node.getData()))
						break;
					node = node.next;
					index ++;
				}
				if(index == size.get())
					index = -2;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return index;
	}
	
	/**
	 * @Description:	根據位置獲取元素
	 * 					0：	首先校驗index是否越界
	 * 					1：	從頭開始遍歷整個鏈錶直到指定位置為止
	 * 						為了能減半工作量，所以讓index與size的一半進行比較
	 * 						小於其一半則從頭遍歷，大於其一半則從尾部遍歷
	 * @param		:	
	 * @return		:	NodeOfSingle
	 */
	public NodeOfDouble<T> getByIndex(int index) {
		NodeOfDouble<T> node = null;
		//這裡的sizeTmp相當於鏈錶中總元素數量的含義。因此在從尾端遍歷時，需要減一再用
		int sizeTmp = 0;
		
		try {
			if(index < 0 || index > size.get())
				throw new IndexOutOfBoundsException();
			
			sizeTmp = size.get();
			if(index <= sizeTmp/2) {
				node = head;
				for(int i=0; null != node && i<index; i++) {
					node = node.next;
				}
			}else {
				node = tail;
				for(int i=sizeTmp-1; null != node && i>index; i--) {
					node = tail.pre;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return node;
	}
	
	/**
	 * @Description:	獲取元素數量
	 * @param		:	
	 * @return		:	int
	 */
	public int getSize() {
		return size.get();
	}
	
	/**
	 * @Description:	校驗鏈錶是否為空
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean ifEmpty() {
		return size.get() == 0;
	}
	
	/**
	 * @Description:	校驗元素是否存在
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean ifExist(T data) {
		boolean flag = false;
		
		try {
			flag = get(data) >= 0 ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	/**
	 * @Description:	顯示明細
	 * @param		:	
	 */
	public void shouDetail() {
		NodeOfDouble<T> node = head;
		int i = 0;
		try {
			while(null != node) {
				if(i >= 1)
					System.out.print(" --> ");
				System.out.print(node.getData());
				node = node.next;
				i++;
			}
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	public static void main(String[] args) {
		String n1, n2, n3, n4, n5, n6, n7, n8;
		
		n1 = "n1";
		n2 = "n2";
		n3 = "n3";
		n4 = "n4";
		n5 = "n5";
		n6 = "n6";
		n7 = "n7";
		n8 = "n8";
		
		DoubleLinkedList<String> dll = new DoubleLinkedList<String>(n1);
		System.out.println("雙向鏈錶是否為空： "+ dll.ifEmpty());
		dll.shouDetail();
		
		dll.addHead(n6);
		System.out.println("雙向鏈錶容量： "+ dll.getSize());
		dll.shouDetail();
		
		dll.addTail(n2);
		System.out.println("雙向鏈錶容量： "+ dll.getSize());
		dll.shouDetail();
		
		dll.addTail(n3);
		dll.shouDetail();
		
		dll.addHead(n5);
		System.out.println("雙向鏈錶容量： "+ dll.getSize());
		dll.shouDetail();
		System.out.println("--------------------------");
		dll.delHead();
		dll.shouDetail();
		
		dll.delTail();
		dll.shouDetail();
		
		dll.addBeforeNode(n1, n4);
		dll.shouDetail();
		
		dll.addBeforeNode(n2, n7);
		dll.shouDetail();
		
		dll.addBeforeNode(n5, n8);
		dll.shouDetail();
		
		System.out.println(dll.ifExist(n8)); 
	}
	
}
