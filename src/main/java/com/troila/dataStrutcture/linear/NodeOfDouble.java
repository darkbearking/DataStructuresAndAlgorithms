package com.troila.dataStrutcture.linear;

/**
 * @Title		:	雙向鏈表的基礎數據結構
 * 					為了不會在鏈錶中產生重複數據，因此創建一個帶指針的構造函數
 * @Description:	
 * @author 		:	liwei
 * @date		:	2019年1月7日
 */
public class NodeOfDouble<T> {

	//每個節點的數據
	private T data;
	//給單向鏈表封裝類使用的，指向前一個和後一個元素
	public NodeOfDouble<T> pre;
	public NodeOfDouble<T> next;
	
	/**
	 * @Title		:	
	 * @Description:	沒有指針的構造
	 * @param data
	 * @param pre
	 */
	public NodeOfDouble(T data) {
		this.data = data;
	}
	
	/**
	 * @Title		:	
	 * @Description:	帶有前指針的構造
	 * @param data
	 * @param pre
	 */
	public NodeOfDouble(T data, NodeOfDouble<T> pre) {
		this.data = data;
		this.pre = pre;
	}
	
	/**
	 * @Title		:	
	 * @Description:	帶有前後指針的構造
	 * @param data
	 * @param pre
	 */
	public NodeOfDouble(T data, NodeOfDouble<T> pre, NodeOfDouble<T> next) {
		this.data = data;
		this.pre = pre;
		this.next = next;
	}
	
	public T getData() {
		return data;
	}
}
