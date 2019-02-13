package org.dark.dataStructure.tree;

/**
 * @Title		:	平衡二叉樹的實現類
 * 					主要實現的功能包括：對樹平衡性的校驗
 * 										左、右翻轉
 * 										前序、後序、中序三序遍歷
 * 										增刪節點、查找、最值、找到節點的前驅和後繼等操作
 * @Description:	平衡二叉樹是在二叉查找樹的基礎上，加上了自平衡的功能。其他功能與思路與二叉查找樹完全一致。
 * 					每次增加節點後，需要校驗樹或子樹是否失衡（貌似從當前節點的父節點一直到根節點都需要校驗一次--微觀到宏觀）
 * 						判斷失衡，首先要判斷某個節點的左右子樹的樹深度。然後比較兩者之差
 * 					如果失衡，則判斷屬於哪種失衡（左左、左右、右左、右右）
 * 					左左與右右的旋轉比較簡單。若是左右，需要先旋轉為左左（右左旋轉為右右）
 * @author liwei
 * @date		:	2019年2月11日
 */
public class AVLTree<T extends Comparable<T>> {

	//定義指向根節點的指針。初始的時候，根節點的指針就是根節點本身，不分彼此
	//完全沒有必要與單雙向鏈錶那樣，創建個獨立於所有節點之外的head或tail指針。
	NodeOfTree<T> root ;
	//定義樹
	NodeOfTree<T> tree;
	//一個是打算造出的元素的個數，一個是隨機數的範圍。
	Integer length = 30 , randomRange = 300;
	
	/**
	 * @Description：	判斷某個節點左右子樹的深度差
	 * 					以左子樹的深度減右子樹的深度差為結果
	 * 					（正負說明左子樹更深抑或右子樹更深）
	 * @param		：	treeNode
	 * @return		：	int
	 */
	public int getDepthDifference(NodeOfTree<T> treeNode) {
		DepthObj depthLeft, depthRight;
		int depthDifference = 0;
		
		try {
			depthLeft = new DepthObj();
			depthLeft = digDig(treeNode,  depthLeft);
			
			depthRight = new DepthObj();
			depthRight = digDig(treeNode,  depthRight);
			
			depthDifference = depthLeft.getMaxDeepth() - depthRight.getMaxDeepth();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return depthDifference;
	}
	
	/**
	 * @Description：	獲取子樹的深度
	 * 					不區分傳入的節點是左抑或右子節點，只需要對傳入節點繼續校驗是否有子節點即可
	 * 					只要有子節點，就繼續挖。
	 * 					只要進入當前方法。說明來源一定是高級節點，，但是同時需要nowaDeepth大於它，才需要maxDeepth自增
	 * 					nowaDeepth也是同樣的自增操作。所不同的是，當從子級返回到當前層，nowaDeepth需要自減
	 * @param		：	treeNode			當前節點（隨著挖掘的深入和返回，當前節點在不斷變化）
	 * @param		：	maxDeepth			頂層傳入節點子樹的最深深度，當nowaDeepth大於它才對其自增。且僅增不減
	 * @param		：	nowaDeepth			當前處於頂層傳入節點的第幾層深度（隨著挖掘的深入和返回而變化，
	 * 											當頂層傳入節點的右子樹的當前值為0，說明挖掘完畢）
	 * @return
	 */
	public DepthObj digDig(NodeOfTree<T> treeNode, DepthObj depth) {
		depth.setNowaDeepth(depth.getNowaDeepth() + 1);
		if(depth.getNowaDeepth() > depth.getMaxDeepth() )
			depth.setMaxDeepth(depth.getMaxDeepth() + 1);;
		
		//挖挖左子樹
		if(treeNode.hasLeft()) {
			digDig(treeNode.getLeft(), depth);
			depth.setNowaDeepth(depth.getNowaDeepth() - 1); 
		}
		//挖挖右子樹
		if(treeNode.hasLeft()) {
			digDig(treeNode.getLeft(), depth);
			depth.setNowaDeepth(depth.getNowaDeepth() - 1); 
		}

		return depth;
	}
	
	/**
	 * @Description：	判斷旋轉方式
	 * 					首先由當前節點出發，根據其左右子樹的深度差，來確定旋轉的方向
	 * 					進而調用對應方向的旋轉方法
	 * 					若子樹屬於左右或右左類型，則需要兩次旋轉才能獲得最終期望結果
	 * @param		：	treeNode
	 * @return		：	AVLTree
	 */
	public void judgeRotate(NodeOfTree<T> treeNode) {
		int depthDifference = 0;
		
		try {
			depthDifference = getDepthDifference(treeNode);
			if(depthDifference >= 2) {
				depthDifference = getDepthDifference(treeNode.getLeft());
				if(depthDifference < 0) {
					rotateLeft(treeNode.getLeft());
				}
				rotateRight(treeNode);
			}
			else if(depthDifference <= -2) {
				depthDifference = getDepthDifference(treeNode.getRight());
				if(depthDifference > 0) {
					rotateRight(treeNode.getRight());
				}
				rotateLeft(treeNode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description：	對某個節點M進行右旋（將其左子節點升級為父節點的一系列操作）
	 * 					右旋可能由两种场景，左左和左右。對於後者，需要先旋轉為左左再旋轉。
	 * 					如何判斷子樹是左左抑或左右呢？繼續對M的左子節點調用getDepthDifference方法即可
	 * 					若返回值大於0，則為左左，小於0為左右（等於0的可能性你說會存在麼？？）
	 * 
	 * 					旋轉時，我們只對目標節點M，M的左子樹L以及L的右子樹R感興趣。
	 * 					旋轉後，L變為M'，M變為L的R'，R變為M的L'
	 * 					而這三個特征節點的子樹則完全不受影響，打醬油即可。
	 * 					圖示說明見“rotateRight.png”，“rotateRight_.png”以及“rotateLeftRight.png”
	 * @param		：	treeNode
	 * @return		：	AVLTree				將反轉後的所有節點視為一棵樹，用這個樹替換原有的樹
	 */
	public AVLTree<T> rotateRight(NodeOfTree<T> treeNode) {
		
		
		
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * @Description：	對某個節點M進行左旋（將其右子節點升級為父節點的一系列操作）
	 * 					右旋可能由两种场景，右右和右左。對於後者，需要先旋轉為右右再旋轉。
	 * 					其他都與右旋的思想類似
	 * 
	 * 					圖示參照右旋相關內容
	 * @param		：	treeNode
	 * @return		：	AVLTree				將反轉後的所有節點視為一棵樹，用這個樹替換原有的樹
	 */
	public AVLTree<T> rotateLeft(NodeOfTree<T> treeNode) {
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) {

	}

}
