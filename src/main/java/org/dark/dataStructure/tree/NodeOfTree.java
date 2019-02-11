package org.dark.dataStructure.tree;

import org.apache.poi.hssf.record.formula.functions.T;

/**
 * @Title		:	所有樹型數據結構使用到的基礎數據單元
 * 					它保存的是一個個的未知類型的數據
 * 					由於與節點關聯的也一定是一個個的節點
 * 					同時這個節點類只用於二叉樹
 * 					因此僅定義了指向左右子節點與父節點的指針
 * @Description:	
 * @author 		：	liwei
 * @date		:	2019年1月28日
 */
public class NodeOfTree<T> {

	//存儲的數據
	private T data ;
	//下面三個分別代表左右節點和父節點
	private NodeOfTree<T> left;
	private NodeOfTree<T> right;
	private NodeOfTree<T> parent;
	
	public NodeOfTree(T data) {
		this.data = data;
	}
	
	public NodeOfTree(T data, NodeOfTree<T> left, NodeOfTree<T> right, NodeOfTree<T> parent) {
		this.data = data;
		this.left = left;
		this.right = right;
		this.parent = parent;
	}

	/**
	 * 判斷是否有左子樹
	 * @return
	 */
	public boolean hasLeft() {
		return null == this.getLeft() ? false : true; 
	}
	
	/**
	 * 判斷是否有右子樹
	 * @return
	 */
	public boolean hasRight() {
		return null == this.getRight() ? false : true; 
	}
	
	public NodeOfTree<T> getLeft() {
		return left;
	}
	public void setLeft(NodeOfTree<T> left) {
		this.left = left;
	}
	public NodeOfTree<T> getRight() {
		return right;
	}
	public void setRight(NodeOfTree<T> right) {
		this.right = right;
	}
	public NodeOfTree<T> getParent() {
		return parent;
	}
	public void setParent(NodeOfTree<T> parent) {
		this.parent = parent;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
}
