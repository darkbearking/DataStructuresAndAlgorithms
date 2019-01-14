package org.dark.sort.linearSort;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Title		: 基數排序算法
 * 				  其原始數據的存放容器與結果的存放容器與計數排序法相似。
 * 				  原理見rs方法的說明。
 * 				  
 * @Description: 
 * @author 		: liwei
 * @date 2018年12月14日
 */
public class RadixSort {
	//數組的第一維度有兩個元素，用來切換已排與待排數組
	Integer dimension = 2;
	//假设已知待排数组的最高数位，本例中，最高数位为4.因此需要循环四次
	Integer digitPosition = 4;
	//一個是打算造出的元素的個數，一個是隨機數的範圍。
	Integer elementCount = 30 , randomRange = 9000;
	//定義一個三維數組，用其中一個維度來切換著放待排與已排數據，第二維度用來存放隨機生成的數字
	//而第三維度的每個元素對應隨機數的每一位，左側不足的位數以0補滿。
	char[][][] chars = new char[dimension][elementCount][digitPosition];
	//创建临时桶。如果是纯数字排序，那么9个桶就足够了，纯字母的话26个也差不多了。
	Integer[] buckets = new Integer[10];
	
	/**
	 * 内部类：桶
	 * 沒用到當前類，因為來回存取數據太麻煩。。。
	 */
	private class Bucket{
		//桶名称
		String bucketName ;
		//已存元素数量
		Integer totalCount;
		
		public String getBucketName() {
			return bucketName;
		}
		public void setBucketName(String bucketName) {
			this.bucketName = bucketName;
		}
		public Integer getTotalCount() {
			return totalCount;
		}
		public void setTotalCount(Integer totalCount) {
			this.totalCount = totalCount;
		}
	}
	
	/**
	 * @Description: 初始化數據
	 * 				  並將初始化後的數據傳入記排核心方法。
	 * 				  由於這種算法需要把高位補零，因此把數據造大一點
	 * @param		:
	 * @return		: Object
	 */
	public void init() {
		try {
			for(int i=0; i<elementCount; i++) {
				Integer num = new Random().nextInt(randomRange);
				chars[0][i] = numberFormat(num).toCharArray();
			}
			for(int j=0; j<elementCount; j++) {
				System.out.println(chars[0][j]);
			}
			System.out.println("=========================華麗麗的分割線=========================");
//			rsLowToHigh();
			rsHighToLow(0, elementCount, 0);
			for(int j=0; j<elementCount; j++) {
				System.out.println(chars[0][j]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String numberFormat(Integer num) {
		return String.format("%0"+digitPosition+"d", num);
	}
	
	/**
	 * @Description:	从低向高
	 * 					基數排序的核心方法
	 * 					1：	通用思路：計數排序其實是線性排序的一種，因為它的時間複雜度突破了選擇排序的O(nLogn)的下限
	 * 							所以應該是一種比較快捷的排序算法。
	 * 						其核心：就是对数据，逐个数位分别进行排序。进而得到规整的排序结果的操作方式。
	 * 					2：	適用場景與優點
	 * 						適用場景就是數據量較大、數位比較規整、數位不高的場景
	 * 							數位不高，那麼總體循環次數就不會很多；（基数排序法要做到有序，需要逐数位排序）
	 * 							數據量大，决定了不利于使用类似快排或冒泡这种比较排序
	 * 							比如一組數據中最高數位是千位，那麼至少要四次執行本排序才可以
	 * 							但是基數排序的好處是，對每個數位只需要執行兩次循環，外加一個作為臨時輔助的桶即可實現
	 * 							需要注意的是，上面說的“桶”並非桶排序，亦即不需要對桶內數據排序，切切
	 * 						所以最大優點是循環次數有保障。
	 * 						其代價就是，需要一組臨時輔助空間來協助執行排序操作。
	 * 					3：逻辑算法：（先正向表述，然后说我的思路和理解）
	 * 						3.0:下面的所有操作，都是对元素的某个数位的数值进行的。
	 * 							而元素，就是那些待排序的多位数（比如84354是个5位数，92是个2位数等等）
	 * 							因此后面所说的“数值”，在没有特殊说明的情况下，都是这个含义，不再解释。
	 * 						3.1:建立临时桶，例如如果待排数组是数字型，那么从低到高（0~9）建立10个桶
	 * 							若为字母型，考虑建立26个（a~z，忽略大小写）
	 * 						3.2.0:	建议将待排数组，从数位的低位向高位进行这个操作。原因后面解释
	 * 						3.2:遍历待排数组的每个元素。根据元素的“数值”，对应的桶中的元素数量进行+1操作。
	 * 							这一步操作完成后，每个桶上仅有符合当前“数值”的个数
	 * 						3.3:遍历完待排数组后，从低到高位逐个累加桶中元素数量。
	 * 							这一步操作完成后，高一位的桶中的元素个数代表的是小于等于这个“数值”的所有元素
	 * 							其目的，是为了在排序时，让这些“数值”对应的的元素知道自己大致在整个队列的哪个范围内。
	 * 						3.4:逆向遍历待排数组，让每个元素根据对应桶中当前的数量，找到自己在新数组的位置。
	 * 						3.5:多次执行当前算法（最高元素是几位，就执行几次），全部执行完毕后，数组有序
	 * 					4：理论依据
	 * 						4.1：从低向高
	 * 							每次执行当前算法，相当于逐位分组操作。每次都是对全量数据操作
	 * 							第一次操作完毕后，最低位组间数值有序，组内最低位数值相同
	 * 							第二次操作完毕后，第二位组间数值有序，组内第二位数值相同。更关键的是，组内低位有序
	 * 							根据这个规律，我们只要继续操作下去，产生的结果一定是同数位间，组内数值相同，低位数值有序
	 * 							这样就实现了排序的目的。（逐位排序）
	 * 						4.2：从高向低
	 * 							同样是分组，每次都是对组内操作，递归进行。
	 * 							第一次操作后，最高位数值组间有序，组内相同，低位乱序
	 * 							第二次操作，就是对上述的每一组分别进行分组并排序操作
	 * 								第二次操作后，最高与次高位组间有序，组内相同，低位乱序
	 * 							可以预见的是，我们每一次操作后，都会形成由高到低位的组内数值相同，组间有序的结果
	 * 								这一操作方式与从低向高的不同就在于，我们需要对每一次分组结果都分别进行同样的操作
	 * 								当我们把每一数位都排序完毕后，需要将所有数据返回到一个结果集中
	 * 								所以这种操作方式的空间占用可能会较高。
	 * 					5：准备工作：为了后续操作简便，因此开始先将待排数组转换为二维字符型数组。
	 * 							以空间换时间，减少后续对每个数位的操作时间。
	 * 								
	 */
	public void rsLowToHigh() {
		int dimensionWait = 0 ,dimensionWork = 0;
		//从低位向高位逐层循环
		for(int i=digitPosition-1; i>=0; i--) {
			//根據當前i是單或雙數，來確定誰做待排和已排數組。wait放的是待排數據，work放已排的
			dimensionWait = (i + 1) % 2;
			dimensionWork = i % 2;
			
			//为桶初始化名称。元素数量默认0
			for(int z=0; z<10; z++) {
				buckets[z] = 0;
			}
			
			//首先統計各個數位的每個數字的數量
			for(int j=0; j<elementCount; j++) {
				Integer c = Integer.parseInt(String.valueOf(chars[dimensionWait][j][i]));
				buckets[c] ++;
			}
			System.out.println("+++++++++");
			for(int j=0; j<elementCount; j++) {
				System.out.println(buckets[j]);
			}
			System.out.println("+++++++++");
			
			//其次從第二個桶開始，逐個累加前一個桶與當前桶中數字的個數
			for(int k=1; k<10; k++) {
				buckets[k] += buckets[k-1];
			}
			
			//逆向遍歷整個待排數組，取得數組本輪遍歷數位（第一位、第三位等等）的數值。
			//根據數值找到對應序號的桶進而獲得當前桶中元素總量
			//以元素總量-1後的值作為已排數組的第二維度的坐標值，並將待排數組的當前位置的數據賦予已排數組的當前位置
			for(int m=elementCount-1; m>=0; m--) {
				Integer c = Integer.parseInt(String.valueOf(chars[dimensionWait][m][i]));
				buckets[c] --;
				chars[dimensionWork][buckets[c]] = chars[dimensionWait][m];
			}
		}
	}
	
	/**
	 * @Description:	从高向低
	 * 					邏輯思想：	第一次，入參是全量數組
	 * 										操作內容是對入參的第一位（最高位）進行排序，核心算法與從低向高的核心相同。
	 * 										出參是第一位組間有序的全量數組
	 * 								第二次，入參是部分數組，其範圍是第一次操作後的某個小組
	 * 										操作內容是是對入參的第二位（次高位）進行排序
	 * 										出參是第二位組間有序的部分數組
	 * 								因此，不管整個數組最多有幾位都無所謂，有幾位就操作幾次就好了。
	 * 									但是無論第幾次操作，其核心都是相同的，都是對當前位排序
	 * 									區別只是隨著層的深入，待排數組越來越小了。
	 * 								我們可以在入參中，通過控制數組的起止位置來達到小範圍騰挪的目的。
	 * 					重點：		一定要確定好元素的絕對位置與相對位置，因為我在這個絕對與相對位置上折騰了好久。。。
	 * 					感想：		在這種算法中，我們不需要像從低到高那種方法，將待排數組與已排數組來回做切換。
	 * 								
	 * @param		:	start				數組的起始位置（永遠保證這是在完全數組中的絕對位置即可）
	 * 					end					數組的終止位置
	 * 					level				數組的層深
	 * 					absolutePosition	當前元素在完全數組中的絕對位置
	 * @return		:	Object
	 * @param list
	 */
	public void rsHighToLow(Integer start, Integer end, Integer level) {
		//建立臨時桶，用來記錄當前桶中元素數量，進而確定start和end位置。
		Integer[] bucketTmp = new Integer[10];
		//記錄到目前為止，bucketTmp中元素累加後的數量
		int totalCount = 0;
		
		//为桶初始化名称。元素数量默认0
		for(int z=0; z<10; z++) {
			buckets[z] = 0;
		}
		
		//首先統計各個數位的每個數字的數量
		for(int i=start; i<end; i++) {
			Integer c = Integer.parseInt(String.valueOf(chars[0][i][level]));
			buckets[c] ++;
		}

		//其次將buckets的每一個原始數據賦予bucketTmp，再從buckets的第二個桶開始，逐個累加前一個桶與當前桶中數字的個數
		for(int j=0; j<10; j++) {
			bucketTmp[j] = buckets[j];
			if(j >= 1)
				buckets[j] += buckets[j-1];
		}
		
		//逆向遍歷整個待排數組，取得數組本輪遍歷數位（level）的數值。
		//根據數值找到對應序號的桶進而獲得當前桶中元素總量
		//以元素總量-1後的值作為已排數組的第二維度的坐標值，並將待排數組的當前位置的數據賦予已排數組的當前位置
		for(int m=end-1; m>=start; m--) {
			Integer c = Integer.parseInt(String.valueOf(chars[0][m][level]));
			buckets[c] --;
			//這里加上start，是為了找到當前元素在完整待排數組中的絕對位置。
			//因為buckets中的位置是本小組內的排名，而非完整數組中的絕對位置。
			chars[1][buckets[c]+start] = chars[0][m];
		}
		
		for(int m=end-1; m>=start; m--) {
			chars[0][m] = chars[1][m];
		}
		//這裡++的目的，是準備進入下一層了
		level++;
		//當執行到了最後一層，就沒必要再進入下面的操作了
		if(level < digitPosition) {
			//遍歷十個bucketTmp桶，以桶中數量作為新的起止位置
			for(int n=0; n<10; n++) {
				//只有當前桶中數量不為0時，才進行遞歸操作
				//因為我遍歷的這個桶是臨時桶，當進入深層次循環的時候，這個桶給出的位置，永遠是小組內的位置
				//而非在待排中的絕對位置。
				if(bucketTmp[n] > 0) {
					//有個問題就是，這個newStart和newEnd都是在當前數組中的絕對位置，並非在整個數組中的相對位置。
					//因此在進入到下一次遞歸時，需要加上絕對定位start
					int newStart,newEnd;
					if(n == 0)
						newStart = 0;
					else
						newStart = totalCount;
					totalCount += bucketTmp[n];
					newEnd = totalCount;
					//兩條及以上的數據才犯得上遞歸一次，否則操作個毛啊
					if(newEnd - newStart >= 2) {
						rsHighToLow(newStart+start, newEnd+start, level);
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		RadixSort rs = new RadixSort();
		rs.init();
		
//		int b = 45;
//		System.out.println(b % 10);
//		System.out.println(b % 100 / 10);
//		System.out.println(b % 10000 / 1000);
	}
}

