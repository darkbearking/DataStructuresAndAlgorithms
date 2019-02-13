package org.dark.dataStructure.tree;
/**
 * @Title		:	僅用來記錄最大深度與當前深度的對象
 * @Description:	
 * @author		:	liwei
 * @date		:	2019年2月11日
 */
public class DepthObj {
	private int maxDeepth;
	private int nowaDeepth;
	
	public DepthObj() {
		maxDeepth = 0;
		nowaDeepth = 0;
	}
	
	public int getMaxDeepth() {
		return maxDeepth;
	}
	public void setMaxDeepth(int maxDeepth) {
		this.maxDeepth = maxDeepth;
	}
	public int getNowaDeepth() {
		return nowaDeepth;
	}
	public void setNowaDeepth(int nowaDeepth) {
		this.nowaDeepth = nowaDeepth;
	}
}
