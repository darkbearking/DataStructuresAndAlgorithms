對於樹而言，遍歷分為深度與廣度兩個陣營
深度遍歷又以前中後序的遍歷最為重要
其中，	前序遍歷是指：先自己、再左節點、後右節點
		中序遍歷是指：先左節點、再自己、後右節點
		後序遍歷是指：先左節點、再右節點、後自己
有的人在表述時候，把“自己”用“父節點”代替，這個無關緊要，我們只需知道這是同一個東西即可。
因此，三序遍歷也就是以自己所處的遍歷順序命名的。
上述就是三種遍歷的最基本的指導思想。

然後詳細闡述上述三種思想的邏輯，以前序為例
		前序：	0、每個節點都會有多重角色，分別是“自己”、“左孩子”、“右孩子”。
					無論前中後任何一種遍歷方式，當且僅當節點的角色為“自己”時，執行打印。否則就繼續遍歷
					這是三序遍歷中通用的system.out.print的時機。
				1、首先從入口節點開始，查看當前節點（自己）的明細（system.out.print），
				2、然後進入到自己的左子節點中去查看左子節點以及左側更深入的明細
				3、再次當左子節點不存在時，查看左子節點的兄弟（右子節點）以及右側更深入的明細
				4、最後，由自己找到自己的父節點向上逆行直至根節點。
			所以，上面的簡化的流程中所說的“先父節點”這點說的不夠準確，應該說“先自己”

以當前文件夾下的1.png文件中的樹來舉例
前序遍歷的結果是：ABCDEFGHI
中序遍歷的結果是：BDCAEHGIF
後序遍歷的結果是：DCBHIGFEA

以中序遍歷舉例。
	1：只要左子節點非葉子，那麼就繼續向下。
	2：看到最深最左的葉子節點後，打印之，然後打印其對應的父節點
	3：若足夠深入後，發現某節點無左子節點，那麼就先打印它自己。因為它可以被視為一個父節點
	4：父節點搞定，就搞父節點的右子節點。右子節點也是以這樣的規則向下遞歸著玩兒。
	
技巧：
	據說，對二叉查找樹進行中序遍歷，就可以得到有序的數列。
	
	通過實驗可知，中序遍歷其實是從小到大的順序對樹進行遍歷。