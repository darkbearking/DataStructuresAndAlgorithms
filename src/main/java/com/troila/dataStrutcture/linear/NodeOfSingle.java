package com.troila.dataStrutcture.linear;

/**
 * @Title		:	單向鏈表的基礎數據結構
 * 					為了不會在鏈錶中產生重複數據，因此創建一個帶指針的構造函數
 * @Description:	
 * @author 		:	liwei
 * @date		:	2019年1月7日
 */
public class NodeOfSingle<T> {

	//每個節點的數據
	private T data;
	//給單向鏈表封裝類使用的，指向後一個元素
	public NodeOfSingle<T> next;
	
	public NodeOfSingle(T data) {
		this.data = data;
	}
	
	/**
	 * @Title		:	
	 * @Description:	這個的存在，能在一定程度上避免重複節點的存在。
	 * @param data
	 * @param next
	 */
	public NodeOfSingle(T data, NodeOfSingle<T> next) {
		this.data = data;
		this.next = next;
	}

	public T getData() {
		return data;
	}
}
