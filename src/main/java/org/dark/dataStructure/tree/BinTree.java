package org.dark.dataStructure.tree;

import java.util.Random;

/**
 * @Title		:	二叉查找樹的實現類
 * 					主要實現的功能包括：前序、後序、中序三序遍歷
 * 										增刪節點、查找、最值、找到節點的前驅和後繼等操作
 * @Description:	二叉查找樹僅涉及根據大小決定儲存的路徑，而不涉及平衡與自旋的問題。
 * 					二叉查找樹根據二分法思想，將小於當前節點的節點置於左子樹，反之置於其右子樹。
 * @author liwei
 * @date		:	2019年1月28日
 */
public class BinTree<T extends Comparable<T>> {
	
	//定義指向根節點的指針。初始的時候，根節點的指針就是根節點本身，不分彼此
	//完全沒有必要與單雙向鏈錶那樣，創建個獨立於所有節點之外的head或tail指針。
	NodeOfTree<T> root ;
	//定義樹
	NodeOfTree<T> tree;
	//一個是打算造出的元素的個數，一個是隨機數的範圍。
	Integer length = 30 , randomRange = 300;
	
	/**
	 * @Description:	有參數的構造，其目的就是把入參作為root即可。
	 * @param		：	root
	 */
	public BinTree(NodeOfTree<T> root) {
		this.root = root;
	}
	
	/**
	 * @Description:	無參的構造，僅僅是創造一顆空樹。
	 * 					或者說，為root初始化
	 */
	public BinTree() {
		this.root = null;
	}
	
	/**
	 * @Description：	獲取根節點
	 * @return		：	NodeOfTree<T>
	 */
	public NodeOfTree<T> getRoot(){
		return root;
	}
	
	/**
	 * @Description:	從任意節點開始的前序遍歷
	 * 					前中後三序遍歷的思想見readme.txt
	 * 					邏輯：	由於每層的每個節點都需要按照前序思想進行遍歷
	 * 							因此，抽象出一個通用方法，給每層的每個節點使用
	 * 							又因為當前是前序遍歷，執行順序是“自己--左孩子--右孩子”這樣的
	 * 							因此，當遇到角色為“自己”的時候，執行system.out.print。
	 * @param		:	
	 * @return		:	Object
	 */
	public void preOrderTraversal(NodeOfTree<T> tmp) {
		System.out.println(tmp.getData());
		if(null != tmp.getLeft())
			preOrderTraversal(tmp.getLeft());
		if(null != tmp.getRight())
			preOrderTraversal(tmp.getRight());
	}

	/**
	 * @Description:	從任意節點開始的中序遍歷
	 * 					邏輯：	本方法的執行順序是“左孩子--自己--右孩子”這樣的
	 * 							當遇到角色為“自己”的時候，執行system.out.print。
	 * @param		:	
	 * @return		:	Object
	 */
	public void midOrderTraversal(NodeOfTree<T> tmp) {
		if(null != tmp.getLeft())
			midOrderTraversal(tmp.getLeft());
		System.out.println(tmp.getData());
		if(null != tmp.getRight())
			midOrderTraversal(tmp.getRight());
	}
	
	/**
	 * @Description:	從任意節點開始的後序遍歷
	 * 					邏輯：	本方法的執行順序是“左孩子--右孩子--自己”這樣的
	 * 							當遇到角色為“自己”的時候，執行system.out.print。
	 * @param		:	
	 * @return		:	Object
	 */
	public void postOrderTraversal(NodeOfTree<T> tmp) {
		if(null != tmp.getLeft())
			postOrderTraversal(tmp.getLeft());
		if(null != tmp.getRight())
			postOrderTraversal(tmp.getRight());
		System.out.println(tmp.getData());
	}
	
	/**
	 * @Description：	從任一節點開始查找目標節點是否存在
	 * 					當前方法與插入方法、刪除方法，一起體現了“二叉查找樹”的真髓
	 * 					其左子樹中一定是小於當前節點的數據，反之亦然。
	 * 					所以根據這一思想，可以很方便的進行搜索
	 * @param 		：	treeNode		樹中的某節點
	 * 					node			目標節點
	 * @return		：	boolean
	 */
	public boolean find(NodeOfTree<T> treeNode, NodeOfTree<T> node){
		boolean flag = false;
		
		if(null == treeNode) {
			return flag;
		}
		if(treeNode.getData() .compareTo(node.getData()) == 0) {
			flag = true;
		}
		else if(treeNode.getData() .compareTo(node.getData()) < 0) {
			find(treeNode.getLeft(), node);
		}
		else if(treeNode.getData() .compareTo(node.getData()) > 0){
			find(treeNode.getRight(), node);
		}
		
		return flag;
	}

	/**
	 * @Description：	從根節點開始查找目標節點
	 * @param 		：	node			目標節點
	 * @return		：	boolean
	 */
	public boolean find(NodeOfTree<T> node) {
		boolean flag = false;
		flag = find(root, node);
		return flag;
	}
	
	/**
	 * @Description：	從任一節點插入節點
	 * 					邏輯：	1：若待插入節點已存在，則插入失敗
	 * 							2：否則，校驗待插入節點與已存在節點之間的大小關係。
	 * 								若能插入葉子位置則最輕鬆愉快（仔細想了想，二叉查找樹不會涉及到為了插入而旋轉的問題。
	 * 									因為二叉查找樹中的數據僅區分大小，卻無序。因此新增的節點一定在葉子位置）
	 * 								
	 * @param 		：	treeNode		樹中的某節點
	 * 					node			目標節點
	 * @return		：	boolean
	 */
	public boolean insert(NodeOfTree<T> treeNode, NodeOfTree<T> node){
		boolean flag = false;
		
		if(treeNode.getData().compareTo(node.getData()) > 0 ) {
			if(null != treeNode.getLeft())
				insert(treeNode.getLeft(), node);
			else {
				treeNode.setLeft(node);
				node.setParent(treeNode);
				flag = true;
			}
		}
		else if(treeNode.getData().compareTo(node.getData()) < 0) {
			if(null != treeNode.getRight())
				insert(treeNode.getRight(), node);
			else {
				treeNode.setRight(node);
				node.setParent(treeNode);
				flag = true;
			}
			
		}
		return flag;
	}
	
	/**
	 * @Description：	從根節點插入目標節點
	 * 					若根節點為空，則插入到根節點
	 * @param 		：	node			目標節點
	 * @return		：	boolean
	 */
	public boolean insert(NodeOfTree<T> node) {
		boolean flag = false;
		
		try {
			if(null == this.getRoot()) {
				this.root = node;
			}else {
				flag = insert(this.getRoot(), node);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public  boolean insertObj(T t) {
		boolean flag = false;
		
		try {
			NodeOfTree<T> node = new NodeOfTree<T>(t);
			if(null == this.getRoot()) {
				this.root = node;
			}else {
				flag = insert(this.getRoot(), node);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return flag;
	}
	
	/**
	 * @Description：	刪除任一節點
	 * 					邏輯：	1：從根節點開始遍歷，若沒有目標節點或已經遍歷到空葉子節點（null），返回失敗
	 * 							2：否則，用目標節點與當前節點作比較。
	 * 								如果小於當前節點，則與其左節點作比較
	 * 								如果大於當前節點，則與其右節點作比較
	 * 								如果等於當前節點，需要判斷當前節點的角色
	 * 									如果當前節點無左子樹，則將右子樹替換當前節點即可
	 * 									如果當前節點無右子樹，則將左子樹替換當前節點即可
	 * 									如果當前節點同時存在左右子樹，
	 * 										則可以將其左子樹的最右最深的子孫節點或其右子樹的最左最深的子孫節點替換當前節點
	 * 										然後將左右子樹再拼接到新的當前節點的下面即可
	 * 								
	 * @param 		：	treeNode		樹中的某節點
	 * 					node			目標節點
	 * @return		：	boolean
	 */
	public boolean delete(NodeOfTree<T> treeNode, NodeOfTree<T> node){
		boolean flag = false;
		NodeOfTree<T> tmp = null, parent = null;
		
		if(null != treeNode) {
			if(treeNode.getData().compareTo(node.getData()) < 0) {
				delete(treeNode.getRight(), node);
			}
			else if(treeNode.getData().compareTo(node.getData()) > 0) {
				delete(treeNode.getLeft(), node);
			}
			//上面兩個是在找目標節點，這裡表示已經找到，要準備刪了。
			else if(treeNode.getData().compareTo(node.getData()) == 0) {
//				//當前節點是不為空的根節點，同時要刪除的目標節點即根節點，則將根節點置為空即可。
				//發現如果是根節點，下面這三行代碼完全不夠看，因此將代碼合並到當前節點有雙子節點中去了。
//				if(null == treeNode.getParent()) {
//					this.root = null;
//				}
				if(treeNode.hasLeft() && !treeNode.hasRight()) {
					tmp = treeNode.getLeft();
					tmp.setParent(treeNode.getParent());
					treeNode.setLeft(null);
					treeNode.setParent(null);
				}
				else if(treeNode.hasRight() && !treeNode.hasLeft()) {
					tmp = treeNode.getRight();
					tmp.setParent(treeNode.getParent());
					treeNode.setRight(null);
					treeNode.setParent(null);
				}
				else if(treeNode.hasRight() && treeNode.hasLeft()) {
					//方法一：	如果當前節點不是根節點，且其父節點缺少一個子樹的話，那麼可以讓當前節點的所有節點直接掛在父節點上
					//			同時，既然執行到這裡，說明當前節點至少是根節點的一級子節點，那也無非是讓其子樹掛在根節點上而已
					if(null != treeNode.getParent() && (!treeNode.getParent().hasLeft() || !treeNode.getParent().hasRight())) {
						tmp = treeNode.getParent();
						tmp.setLeft(treeNode.getLeft());
						tmp.setRight(treeNode.getRight());
						treeNode.setParent(null);
						treeNode.setLeft(null);
						treeNode.setRight(null);
					}
					//方法二：找到當前節點的右側最深的左節點或左側最深的右節點，即僅僅小於當前節點或僅僅大於當前節點的節點，來替換它。
					//按下面的寫法，其實每次找到的都是當前節點的右側最深最左的節點。因為上面已經判斷了當前節點雙子俱全
					else {
						if(null != treeNode.getRight()) {
							tmp = this.findLeft(treeNode.getRight());
						}
						else if(null != treeNode.getLeft()){
							tmp = this.findRight(treeNode.getLeft());
						}
						
						//當前節點是根節點的特例只在這裡有意義
						if(null == treeNode.getParent())
							this.root = tmp;
						tmp.setParent(treeNode.getParent());
						tmp.setLeft(treeNode.getLeft());
						tmp.setRight(treeNode.getRight());
						treeNode.setParent(null);
						treeNode.setLeft(null);
						treeNode.setRight(null);
					}
				}
				else if (!treeNode.hasLeft() && !treeNode.hasRight()) {
					parent = treeNode.getParent();
					if(treeNode.equals(parent.getLeft()))
						parent.setLeft(null);
					else if(treeNode.equals(parent.getRight()))
						parent.setRight(null);
					treeNode.setParent(null);
				}
				
				flag = true;
			}
		}
		
		return flag;
	}
	
	/**
	 * @Description：	從根節點刪除目標節點
	 * 					若根節點為空，則刪除失敗
	 * @param 		：	node			目標節點
	 * @return		：	boolean
	 */
	public boolean delete(NodeOfTree<T> node) {
		boolean flag = false;
		
		try {
			if(null != this.getRoot()) {
				flag = delete(this.getRoot(), node);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	/**
	 * @Description：	找到目標節點的最左子節點
	 * @param 		：	treeNode		目標節點
	 * @return		：	NodeOfTree<T>
	 */
	public NodeOfTree<T> findLeft(NodeOfTree<T> treeNode){
		NodeOfTree<T> node = treeNode;

		if(null != treeNode.getLeft()) {
			node = findLeft(treeNode.getLeft());
		}
		
		return node;
	}
	
	/**
	 * @Description：	找到目標節點的最右子節點
	 * @param 		：	treeNode		目標節點
	 * @return		：	NodeOfTree<T>
	 */
	public NodeOfTree<T> findRight(NodeOfTree<T> treeNode){
		NodeOfTree<T> node = treeNode;

		if(null != treeNode.getRight()) {
			node = findRight(treeNode.getRight());
		}
		
		return node;
	}
	
	/**
	 * @Description：	從任意位置開始獲取最小值
	 * @param 		：	treeNode		目標節點
	 * @return		：	NodeOfTree<T>
	 */
	public NodeOfTree<T> getMin(NodeOfTree<T> treeNode){
		NodeOfTree<T> node = treeNode;

		try {
			node = findLeft(node.getLeft());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return node;
	}
	
	/**
	 * @Description：	從根節點開始獲取最小值
	 * @return		：	NodeOfTree<T>
	 */
	public NodeOfTree<T> getMin(){
		NodeOfTree<T> node = this.root;

		try {
			node = findLeft(node.getLeft());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return node;
	}
	
	/**
	 * @Description：	從任意位置開始獲取最大值
	 * @param 		：	treeNode		目標節點
	 * @return		：	NodeOfTree<T>
	 */
	public NodeOfTree<T> getMax(NodeOfTree<T> treeNode){
		NodeOfTree<T> node = treeNode;

		try {
			node = findRight(node.getRight());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return node;
	}
	
	/**
	 * @Description：	從根節點開始獲取最大值
	 * @return		：	NodeOfTree<T>
	 */
	public NodeOfTree<T> getMax(){
		NodeOfTree<T> node = this.root;

		try {
			node = findRight(node.getRight());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return node;
	}
	
	/**
	 * @Description：	獲取目標節點的前驅節點
	 * 					邏輯：	節點的前驅節點有多種可能性
	 * 							1：目標節點的左子樹的最深最右子節點
	 * 							2：若目標節點的左子樹為空，要考慮目標節點是否作為其父節點的右側子孫節點存在。
	 * 								
	 * @param 		：	treeNode		目標節點
	 * @return		：	NodeOfTree<T>
	 */
	public NodeOfTree<T> getPrecursor(NodeOfTree<T> treeNode){
		NodeOfTree<T> node = null;

		try {
			if(null == treeNode) {
				return treeNode;
			}
			//首先判斷左子樹是否為空，
			//	如果為空，判斷當前節點是否作為右側子孫節點存在
			//	否則，查找左子樹的最右最深節點。
			//		存在則以最右最深節點作為前驅
			//		否則，左子節點就是當前節點的前驅
			if(null == treeNode.getLeft()) {
				node = hasLeftParent(treeNode);
			}else {
				node = findRight(treeNode.getLeft());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("節點 " + treeNode.getData() + "的前驅是： " + node.getData());
		return node;
	}
	
	/**
	 * @Description：	獲取目標節點的後繼節點
	 * 					邏輯：	節點的後繼節點有多種可能性
	 * 							1：目標節點的右子樹的最深最左子節點
	 * 							2：若目標節點的右子樹為空，要考慮目標節點是否作為其父節點的左側子孫節點存在。
	 * @param 		：	treeNode		目標節點
	 * @return		：	NodeOfTree<T>
	 */
	public NodeOfTree<T> getSubsequent(NodeOfTree<T> treeNode){
		NodeOfTree<T> node = null;

		try {
			if(null == treeNode) {
				return treeNode;
			}
			
			//首先判斷右子樹是否為空，
			//	如果為空，判斷當前節點是否作為左側子孫節點存在
			//	否則，查找右子樹的最左最深節點。
			//		存在則以最左最深節點作為前驅
			//		否則，右子節點就是當前節點的前驅
			if(null == treeNode.getRight()) {
				node = hasRightParent(treeNode);
			}else {
				node = findLeft(treeNode.getRight());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("節點 " + treeNode.getData() + "的後繼是： " + node.getData());
		return node;
	}
	
	/**
	 * @Description：	判斷當前節點是否作為右側子孫節點存在
	 * 					換言之，也就是判斷它的是否大於自己所有的祖先節點
	 * 					若沒有期望的結果，那麼這個方法是要一直遍歷到根節點的。
	 * 
	 * 					邏輯：	若當前節點的父節點不為空（父節點不是根節點）
	 * 							而且當前節點是父節點的左子節點，那麼進入迭代。
	 * 							若找到符合的父節點（父節點比自己小），那麼返回這個父節點
	 * @param 		：	treeNode		目標節點
	 * @return		：	NodeOfTree<T>
	 */
	public NodeOfTree<T> hasLeftParent(NodeOfTree<T> treeNode){
		NodeOfTree<T> node = null;

		try {
			if(null != treeNode.getParent()) {
				if(treeNode.getParent().getLeft().getData().compareTo(treeNode.getData()) == 0) {
					node = hasLeftParent(treeNode.getParent());
				}
				else if(treeNode.getParent().getRight().getData().compareTo(treeNode.getData()) == 0) {
					node = treeNode.getParent();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return node;
	}
	
	/**
	 * @Description：	判斷當前節點是否作為左側子孫節點存在
	 * 					換言之，也就是判斷它的是否小於自己所有的祖先節點
	 * 					若沒有期望的結果，那麼這個方法是要一直遍歷到根節點的。
	 * 
	 * 					邏輯：	若當前節點的父節點不為空（父節點不是根節點）
	 * 							而且當前節點是父節點的右子節點，那麼進入迭代。
	 * 							若找到符合的父節點（父節點比自己大），那麼返回這個父節點
	 * @param 		：	treeNode		目標節點
	 * @return		：	NodeOfTree<T>
	 */
	public NodeOfTree<T> hasRightParent(NodeOfTree<T> treeNode){
		NodeOfTree<T> node = null;

		try {
			if(null != treeNode.getParent()) {
				if(treeNode.getParent().getRight().getData().compareTo(treeNode.getData()) == 0) {
					node = hasRightParent(treeNode.getParent());
				}
				else if(treeNode.getParent().getLeft().getData().compareTo(treeNode.getData()) == 0) {
					node = treeNode.getParent();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return node;
	}
	
	//銷毀

	public static void main(String[] args) {
		BinTree<Integer> bt = new BinTree<Integer>();
		
//		下面是自造的一些數據由於有規律，所以用它來校驗代碼的邏輯，閱後即焚
//		NodeOfTree<Integer> node1 = new NodeOfTree<Integer>(7);
//		NodeOfTree<Integer> node2 = new NodeOfTree<Integer>(6);
//		NodeOfTree<Integer> node3 = new NodeOfTree<Integer>(12);
//		NodeOfTree<Integer> node4 = new NodeOfTree<Integer>(3);
//		NodeOfTree<Integer> node5 = new NodeOfTree<Integer>(5);
//		NodeOfTree<Integer> node6 = new NodeOfTree<Integer>(10);
//		NodeOfTree<Integer> node7 = new NodeOfTree<Integer>(15);
//		NodeOfTree<Integer> node8 = new NodeOfTree<Integer>(9);
//		NodeOfTree<Integer> node9 = new NodeOfTree<Integer>(8);
//		
//		bt.insert(node1);
//		bt.insert(node2);
//		bt.insert(node3);
//		bt.insert(node4);
//		bt.insert(node5);
//		bt.insert(node6);
//		bt.insert(node7);
//		bt.insert(node8);
//		bt.insert(node9);
		
		NodeOfTree<Integer>[] node = new NodeOfTree[bt.length];
		for(int i=0; i<bt.length; i++) {
			node[i] = new NodeOfTree<Integer>(new Random().nextInt(bt.randomRange));
			bt.insert(node[i]);
			System.out.println(node[i].getData());
		}
		
		System.out.println("=========================開始前序=========================");
		bt.preOrderTraversal(bt.getRoot());
		System.out.println(" ");
		
		System.out.println("=========================開始中序=========================");
		bt.midOrderTraversal(bt.getRoot());
		System.out.println(" ");
		
		System.out.println("=========================開始後序=========================");
		bt.postOrderTraversal(bt.getRoot());
		
		System.out.println("=========================開始前驅後繼=========================");
		bt.getPrecursor(node[15]);
		bt.getSubsequent(node[15]);
	}
}
