package com.troila.dataStrutcture.linear;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Title		:	普通單鏈表實現類
 * 					我在設計之初，沒有考慮多線程及一致性的問題。
 * 					好再後來將size設置為了原子性的。
 * 					但是後續需要考慮對鏈錶內的元素做增刪操作的時候，線程間的安全性問題。
 * 
 * 					當然，還可以考慮實現一個線程安全的單向鏈錶。這個鏈錶就做一些不考慮線程安全的操作
 * 						也還可以參照之前看的主動失敗的思路，來設計單向鏈錶。
 * 						抑或使用volatile、synchronized的方式也好。
 * 
 * 					問題：	線程安全問題
 * 							重複壓入同一個元素，會造成鏈錶的斷裂。是否在加入元素前做校驗？
 * 								為了避免這個問題，可以只放開頭入頭出這個口子加入數據，其他的釦子關閉即可。
 * 								還可以在構造的時候，顯示指明元素的後面元素。但是有個問題是，除非我們僅從頭部或中間加入元素
 * 									否則從尾部加入元素的話，我們仍然不知道其後續元素是誰。
 * 					反思：	發現我在設計之初，沒有考慮到重複數據以及相關的一些問題。僅僅把功能設計的很多很全
 * 							但是忽略了性能與產生bug的點。
 * 					前提：	頭和尾不可以是同一個節點
 * @Description: 
 * @author 		: liwei
 * @date 2019年1月8日
 */
public class SingleLinkedList {

	//定義頭
	private NodeOfSingle<String> head;
	//定義總元素數量
	private AtomicInteger size = new AtomicInteger(0);
	
	/**
	 * @Title		:	功能：	這個類，我們會讓它實現諸如一些頭部插入，頭部刪除、普通插入，普通刪除等操作。
	 * 							還會實現一些向後遍歷、判斷是否還有下個節點、判斷是否頭部、尾部等操作
	 * 
	 * 					作用：	使用單向鏈錶，更多的是用於類似創建棧（雙向適合創建隊列）這種頻繁增刪的場景
	 * 							因此，不要考慮為鏈錶的每個元素設置下標這種屬性。因為每次增刪後的維護成本過高
	 * 								所有會造成高維護的功能，直接砍掉就是了。
	 * 					問題：	高並發下，我如何保證size數量的正確呢？
	 * @Description:	構造函數，得到一個空的單向鏈錶
	 */
	public SingleLinkedList() {
		head = null;
		size = new AtomicInteger(0);
	}
	
	/**
	 * @Title		:	初始化頭結點的構造函數
	 * @Description:	
	 * @param 		:	node
	 */
	public SingleLinkedList(NodeOfSingle<String> node) {
		node.next = null;
		head = node;
		size = new AtomicInteger(1);
	}
	
	/**
	 * @Description:	頭部添加元素
	 * 					令新元素指向原有頭部對應元素
	 * 					令“頭”這個指針指向新元素
	 * 
	 * 					突然發現一個問題，本來head是個“指針”的概念。但是在java這裡直接用對象代替了。
	 * 					那麼，我想要在jvm或jmm中找到某個對象的地址，或通過地址找到某個對象，該如何操作呢？
	 * @param		:	
	 * @return		:	Object
	 */
	public void addHead(NodeOfSingle<String> node) {
		node.next = head;
		head = node;
		size.getAndIncrement();
	}
	
	/**
	 * @Description:	在元素node後添加新元素newNode
	 * 					0：	如果傳入的node或newNode是null，你他喵玩兒我啊。。。
	 * 					1：	首先校驗node是否在於鏈錶中，不存在直接返回-1
	 * 						然後，將node指向newNode，將newNode指向node的next，over
	 * @param		:	
	 * @return		:	-1/1（失敗/成功）
	 */
	public int add(NodeOfSingle<String> node, NodeOfSingle<String> newNode) {
		int result = -1;
		
		try {
			if(null == node || null == newNode )
				return result;
			if(ifExist(node)) {
				newNode.next = node.next;
				node.next = newNode;
				result = 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * @Description:	獲取頭部元素（不刪除）
	 * @param		:	
	 * @return		:	NodeOfSingle
	 */
	public NodeOfSingle<String> getHead(){
		return null == head ? null : head;
	}
	
	/**
	 * @Description:	根據條件查找節點
	 * 					貌似當前的node的設置中，就是一個object，不包含什麼key
	 * 					所以所謂的條件其實就是node本身，所以，只需要調用判斷這個node是否存在即可。
	 * 					
	 * 					當前方法廢棄
	 * @param		:	
	 * @return		:	Object
	 */
	public NodeOfSingle<String> findByKeyword(NodeOfSingle<String> node) {
		return null;
	}
	
	/**
	 * @Description:	頭部刪除
	 * 					1：	如果頭部本身不存在，或者頭部指向的下個元素不存在，那就沒什麼可以乾的活兒了
	 * 					2：	否則，就讓頭指向的下個元素作為新的頭即可
	 * @param		:	
	 * @return		:	boolean
	 */
	public boolean delHead() {
		boolean flag = false;
		
		try {
			if(null == head || null == head.next) {
				flag = true; 
			}else {
				head = head.next;
				flag = true;
				size.getAndDecrement();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * @Description:	刪除元素
	 * 					0：	校驗入參元素與鏈錶的頭是否為空
	 * 					1：	首先要找到當前元素的前驅。
	 * 					如果前驅的後繼就是當前元素，
	 * 					然後根據當前元素的後繼是否為空
	 * @param		:	
	 * @return		:	Object
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
				//校驗目標元素是否有後繼節點
				if(null == node.next) {
					head = null;
				}else {
					head.next = node.next;
					node.next = null;
				}
				flag = true;
			}else {
				//從頭部開始遍歷
				nodePre = head;

				while(null != nodePre.next) {
					if(nodePre.equals(node)) {
						//校驗目標元素是否有後繼節點
						if(null == node.next) {
							nodePre.next = null;
						}else {
							nodePre.next = node.next;
							node.next = null;
						}
						flag = true;
					}else {
						nodePre = nodePre.next;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}

	/**
	 * @Description:	是否擁有下一個元素
	 * 					其實這個方法沒啥意義，因為我們一定是判斷鏈錶中的node是否含有下個節點
	 * 					既然node本身就是來源於鏈錶，那麼直接調用node.next方法即可。
	 * @param		:	
	 * @return		:	Object
	 */
	public boolean hasNext(NodeOfSingle<String> node) {
		return null == node.next ? false : true;
	}
	
	/**
	 * @Description:	校驗node是否存在
	 * 					0：	如果入參節點為null，你他喵玩兒我呢？
	 * 						如果頭節點是null，你他喵依然玩兒我呢？
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
			if(null == node) {
				return flag;
			}
			if(null == head) {
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
		SingleLinkedList link = new SingleLinkedList();
		
		n1 = new NodeOfSingle<String>("1");
		n2 = new NodeOfSingle<String>("2");
		n3 = new NodeOfSingle<String>("3");
		n4 = new NodeOfSingle<String>("4");
		n5 = new NodeOfSingle<String>("5");
		n6 = new NodeOfSingle<String>("6");
		n7 = new NodeOfSingle<String>("7");
		n8 = new NodeOfSingle<String>("8");
		
		link.addHead(n1);
		System.out.println("當前頭元素是: " + link.getHead().getData());
		link.addHead(n2);
		link.addHead(n7);
		link.addHead(n3);
		
		System.out.println("鏈錶總元素數量是: " + link.getSize());
		System.out.println("n2的下一個元素是: " + n2.next.getData());
		System.out.println("當前頭元素是: " + link.getHead().getData());
		link.delHead();
		System.out.println("當前頭元素是: " + link.getHead().getData());
		System.out.println("n7節點是否存在: " + link.ifExist(n7));
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}

}
