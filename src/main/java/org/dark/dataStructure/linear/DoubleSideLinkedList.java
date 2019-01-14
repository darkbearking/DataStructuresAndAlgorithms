package org.dark.dataStructure.linear;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Title		:	雙端鏈錶
 * 					核心：	就是在單向鏈錶的基礎上，添加了一個指向尾部的指針。
 * 							但並未實現逆向遍歷或訪問鏈錶的功能。稱之為雙端鏈錶。
 * 					前提：	頭和尾不可以是同一個節點
 * @Description:	
 * @author 		:	liwei
 * @date		:	2019年1月9日
 */
public class DoubleSideLinkedList {

	//定義頭部和尾部的指針
	public NodeOfSingle<String> head;
	public NodeOfSingle<String> tail;
	
	//定義總元素數量
	private AtomicInteger size = new AtomicInteger(0);
	
	/**
	 * @Title		:	沒有指向頭尾指針的構造
	 * @Description:
	 */
	public DoubleSideLinkedList() {
		head = null;
		tail = null;
		size = new AtomicInteger(0);
	}
	
	/**
	 * @Title		:	有且僅有指向頭部指針的構造
	 * @Description:
	 */
	public DoubleSideLinkedList(NodeOfSingle<String> node) {
		head = node;
		head.next = null;
		tail = null;
		size = new AtomicInteger(1);
	}
	
	/**
	 * @Title		:	有指向頭尾指針的構造
	 * @Description:
	 */
	public DoubleSideLinkedList(NodeOfSingle<String> nodeHead, NodeOfSingle<String> nodeTail) {
		head = nodeHead;
		tail = nodeTail;
		head.next = tail;
		tail.next = null;
		size = new AtomicInteger(2);
	}

	/**
	 * @Description:	向頭部插入元素
	 * 					若當前head是空，那麼尾部自然也是空，只需讓新來的做head，而尾部無操作
	 * 					否則，替換head，然後校驗，當前是否有tail。
	 * 						如果沒有tail，則原來的head作為tail
	 * 						否則對尾部不需操作
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean addHead(NodeOfSingle<String> node) {
		boolean flag = false;
		
		try {
			if(null == node)
				return flag;
			
			if(null == head) {
				head = node;
			}
			else {
				if(null == tail) {
					tail = head;
					head = node;
					head.next = tail;
				}else {
					node.next = head.next;
					head = node;
				}
			}
			size.getAndIncrement();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	/**
	 * @Description:	向尾部插入元素
	 * 					如果頭不存在，直接不讓這樣操作
	 * 					否則，校驗尾部是否存在
	 * 						如果尾部存在，新的替換老的
	 * 						否則，新來的做尾部，同時與頭部關聯。
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean addTail(NodeOfSingle<String> node) {
		boolean flag = false;
		
		try {
			if(null == node)
				return flag;
			
			if(null == head) {
				return flag;
			}else {
				if(null == tail) {
					node = tail;
					head.next = node;
				}else {
					tail.next = node;
					tail = node;
					node.next = null;
				}
			}
			size.getAndIncrement();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	/**
	 * @Description:	在元素node後添加新元素newNode
	 * 					0：	如果傳入的node或newNode是null，或者head是null，你他喵玩兒我啊。。。
	 * 					1：	首先校驗node是否在於鏈錶中，不存在直接返回false
	 * 						然後，分別根據原節點的角色確定新節點的添加方式
	 * 							如果node是head。同時tail為空，那麼newNode就是尾
	 * 											否則就是tail不為空，那麼newNode放在head的後面即可。
	 * 							如果node是tail。同時tail不為空（看似廢話，但是重要），那麼newNode替換原來的尾。
	 * 							如果node既不是head也不是tail，那就該怎麼弄就怎麼弄唄。
	 * 							因為執行上面邏輯的前提，是node一定存在，那麼不會出現node不符合上述任何一種的情況。
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean add(NodeOfSingle<String> node, NodeOfSingle<String> newNode) {
		boolean flag = false;
		
		try {
			if(null == node || null == newNode || null == head) {
				return flag;
			}
			if(this.ifExist(node)) {
				if(head.equals(node)) {
					if(null == tail) {
						tail = newNode;
						head.next = newNode;
					}else {
						newNode.next = head.next;
						head.next = newNode;
					}
				}
				else if(null != tail && node.equals(tail)) {
					tail.next = newNode;
					tail = newNode;
					newNode.next = null;
				}
				else if(!head.equals(node) && !node.equals(tail)) {
					newNode.next = node.next;
					node.next = newNode;
				}
				size.getAndIncrement();
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * @Description:	從頭部刪除元素
	 * 					1：如果頭部是空，你他喵逗我玩呢？
	 * 					2：否則，獲取鏈錶總元素數量。
	 * 							如果數量是1，說明鏈錶中僅有一個head，那麼將head置為null即可
	 * 							如果數量是2，說明有一head，一tail。那麼將原先的tail做新的head，tail置為null即可
	 * 							如果數量大於等於3，讓原先第二位的替代head即可。其他不變
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean delHead() {
		boolean flag = false;
		
		try {
			if(null == head) {
				return flag;
			}
			
			if(this.getSize() == 1) {
				head = null;
			}
			else if(this.getSize() == 2) {
				head = tail;
				head.next = null;
				tail = null;
			}
			else if(this.getSize() >= 3) {
				head = head.next;
			}
			size.getAndDecrement();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * @Description:	從尾部刪除元素
	 * 					0：	這裡是用單向節點實現的雙端鏈錶，因此不支持查找元素的前一個元素
	 * 						所以當前方法無法實現。
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean delTail() {
		boolean flag = false;
		return flag;
	}
	
	/**
	 * @Description:	刪除元素
	 * 					0：	校驗入參元素與鏈錶的頭是否為空
	 * 					1：	一定是從head開始遍歷，所以在初始校驗時不必考慮入參node與tail一致的情況。
	 * 						這部分調用前面的delHead方法即可
	 * 					2：	讓nodePre從等於head，並且從head之後的第一個節點開始進入輪詢。
	 * 						如果若nodePre是尾,說明沒有node，刪除失敗
	 * 						如果nodePre不是尾，同時nodePre的next就是要找的node，需要對node做判斷
	 * 							如果node是尾，那麼把nodePre變為尾即可
	 * 							否則，就是最普通的情況，讓nodePre指向node的下一個就行啦
	 * 						否則，指針向下移動，繼續判斷下一個節點即可
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean del(NodeOfSingle<String> node) {
		boolean flag = false;
		//定義前一節點
		NodeOfSingle<String> nodePre ;

		try {
			if(null == node || null == head) {
				return flag;
			}
			
			//若頭就是目標元素
			if(head.equals(node)) {
				if(this.delHead()) {
					return true;
				}
			}else {
				//從頭部開始遍歷
				nodePre = head;

				while(null != nodePre.next) {
					if(nodePre.equals(tail)) {
						return flag;
					}
					else if(nodePre.next.equals(node)) {
						if(tail == node) {
							nodePre.next = null;
							tail = nodePre;
						}
						else {
							nodePre.next = node.next;
							node.next = null;
						}
						
						flag = true;
					}
					else {
						nodePre = nodePre.next;
					}
				}
			}
			if(flag)
				size.getAndDecrement();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	/**
	 * @Description:	獲取頭部元素
	 * @param		:	
	 * @return		:	NodeOfSingle
	 */
	public NodeOfSingle<String> getHead() {
		return null == head ? null : head;
	}
	
	/**
	 * @Description:	獲取尾部元素
	 * @param		:	
	 * @return		:	NodeOfSingle
	 */
	public NodeOfSingle<String> getTail() {
		return null == tail ? null : tail;
	}
	
	/**
	 * @Description:	校驗node是否存在
	 * 					0：	如果入參節點或頭節點為null，你他喵玩兒我呢？
	 * 					1：	從頭節點開始，遍歷所有節點
	 * 						如果當前節點就是目標節點，跳出，返回true
	 * 						如果當前節點指向的下個節點是null，就別繼續費勁了。
	 * 						否則，移動指針指向下個節點，繼續循環，直至循環結束。
	 * 					2：	遍歷完畢，沒有節點符合要求節點，返回false
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean ifExist(NodeOfSingle<String> node) {
		boolean flag = false;
		//定義當前節點
		NodeOfSingle<String> nodeNow ;

		try {
			if(null == node || null == head) {
				return flag;
			}
			//從頭部開始遍歷
			nodeNow = head;
			
			while(true) {
				if(nodeNow.equals(node)) {
					flag = true;
					return flag;
				}
				
				if(null == nodeNow.next) {
					break;
				}
				else {
					nodeNow = nodeNow.next;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	/**
	 * @Description:	校驗當前鏈錶是否為空
	 * @param		:	
	 * @return		:	Object
	 */
	public boolean ifEmpyt() {
		return size.get() == 0 ? true : false; 
	}

	/**
	 * @Description:	獲取當前單向鏈錶中元素數量
	 * @param		:	
	 * @return		:	Object
	 */
	public int getSize() {
		return size.get();
	}
	
	public static void main(String[] args) {
		NodeOfSingle<String> n1,n2,n3,n4,n5,n6,n7,n8;
		
		n1 = new NodeOfSingle<String>("n1");
		n2 = new NodeOfSingle<String>("n2");
		n3 = new NodeOfSingle<String>("n3");
		n4 = new NodeOfSingle<String>("n4");
		n5 = new NodeOfSingle<String>("n5");
		n6 = new NodeOfSingle<String>("n6");
		n7 = new NodeOfSingle<String>("n7");
		n8 = new NodeOfSingle<String>("n8");
		
		DoubleSideLinkedList dsl = new DoubleSideLinkedList();
		System.out.println("雙端鏈錶中的元素數量： " + dsl.getSize());
		dsl = new DoubleSideLinkedList(n1);
		System.out.println("雙端鏈錶中的元素數量： " + dsl.getSize());
		System.out.println("雙端鏈錶中的頭元素是： " + dsl.getHead().getData());
		dsl.addHead(n2);
		System.out.println("雙端鏈錶中的元素數量： " + dsl.getSize());
		System.out.println("雙端鏈錶中的頭元素是： " + dsl.getHead().getData());
		System.out.println("雙端鏈錶中的尾元素是： " + dsl.getTail().getData());
		dsl.addHead(n3);
		dsl.addHead(n4);
		System.out.println("雙端鏈錶中的元素數量： " + dsl.getSize());
		System.out.println("雙端鏈錶中的頭元素是： " + dsl.getHead().getData());
		System.out.println("雙端鏈錶中的尾元素是： " + dsl.getTail().getData());
		dsl.addTail(n8);
		System.out.println("雙端鏈錶中的元素數量： " + dsl.getSize());
		System.out.println("雙端鏈錶中的頭元素是： " + dsl.getHead().getData());
		System.out.println("雙端鏈錶中的尾元素是： " + dsl.getTail().getData());
		dsl.addTail(n8);
		dsl.addTail(n8);
		dsl.addTail(n8);
		dsl.addTail(n8);
		dsl.addTail(n8);
		dsl.addTail(n8);
		System.out.println("雙端鏈錶中的元素數量： " + dsl.getSize());
		System.out.println("雙端鏈錶中的頭元素是： " + dsl.getHead().getData());
		System.out.println("雙端鏈錶中的尾元素是： " + dsl.getTail().getData());
		dsl.addTail(n7);
		System.out.println("雙端鏈錶中的元素數量： " + dsl.getSize());
		System.out.println("雙端鏈錶中的頭元素是： " + dsl.getHead().getData());
		System.out.println("雙端鏈錶中的尾元素是： " + dsl.getTail().getData());
		
		
	}
}
