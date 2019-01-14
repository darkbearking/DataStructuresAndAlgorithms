package org.dark.sort.compareSort;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @Title		:	實現快速排序算法
 * 				 	數據容器為list。其實使用數組也可以，這些對最終結果沒有多少影響
 * 				 	快排原理：見qs方法的說明
 * @Description: 
 * @author 		: liwei
 * @date 2018年12月14日
 */
public class QuickSort {

	List<Integer> list = null;
	
	/**
	 * @Description: 初始化數據
	 * 				  並將初始化後的數據傳入快排核心方法。
	 * @param		:
	 * @return		: Object
	 */
	public void initData() {
		Integer targetPointer, leftPointer, rightPointer;
		//一個是打算造出的數組的長度，一個是隨機數的範圍。
		Integer length = 100 , randomRange = 200;
		
		try {
			list = new ArrayList<Integer>();
			for(int i=0; i<length; i++) {
				Integer num = new Random().nextInt(randomRange);
				list.add(num);
			}
			for(int j=0; j<length; j++) {
				System.out.println(list.get(j));
			}
			
			targetPointer = list.size()-1;
			leftPointer = 0;
			rightPointer = targetPointer;
			qs(targetPointer, leftPointer, rightPointer);
			
			for(int j=0; j<length; j++) {
				System.out.println(list.get(j));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: 快速排序的核心方法
	 * 					1：	通用思路：快排其實是分治法的一種實現，需要不斷的對父數組做二分
	 * 						它的時間複雜度並不穩定，最優狀態為O(n*log(n))。
	 * 						這也是所有比較排序的時間複雜度的下限了。
	 * 						它分別對父數組的兩個子集合遞歸做同樣的操作。直到每個子數組的元素都是有序為止。
	 * 						（判斷條件之一，可以是每個子數組中僅有一個元素。）
	 * 					2：	操作方法：
	 * 						2.1：	在數組中建立三個指針。分別指向數組最左、最右、標的物。
	 * 								我們分別給它們起名叫左指針、右指針、標的物。
	 * 								其中標的物使我們隨機找的一個東西，作為二分法的那個分割物。
	 * 						2.2：	標的物的確定有三種方法，或者最左，或者最右，或者中間的隨機一個位置
	 * 								根據標的物的位置的不同，後續的代碼實現邏輯中也會略有不同。
	 * 								在本例中，我們選擇最右側的數據作為標的物。
	 * 						2.3：	
	 * 								2.3.1：開始循環，先移動左指針，移動過程中需要校驗是否與右指針重疊
	 * 									在沒有重疊的前提下，當遇到大於標的物的數據的時候移動右指針
	 * 								2.3.2：右指針移動過程中同樣需要校驗是否與做指針重疊
	 * 									若沒有重疊，同時右指針遇到了一個小於標的數據，則交換左右指針指向的數據。
	 * 								2.3.3：交換後，繼續2.3.1的步驟直到兩個指針重疊。
	 * 									當指針重疊，說明一次遍歷完結。
	 * 						2.4：以標的物為分割線，將原數組分為兩塊，分別對這兩個子部分執行2.1到2.3的步驟。
	 * 								直至每個子數組的長度不大於3.
	 * 					3：還一種思路，是類似於插入排序的方法來做快速排序。但是那樣真的不能叫快速排序。
	 * 						因為快排的最大優點，就是它不必像插入排序那樣使用臨時存儲空間。
	 * 						所以那種借用臨時存儲來實現快排的可以借鑒。在一定場景下有其存在意義（比如空間換時間的場景）
	 * 					4：補充
	 * 						4.1：為了減少開闢的臨時存儲空間，因此我們每次的入參數組都是這個數組本身，區別在於左右指針的位置不同
	 * 						4.2：最初設計這個方法的時候，我考慮將數組作為入參傳入，但是結合上面的4.1所說
	 * 							感覺真的沒有必要。因為第一形參要分配內存單元、第二我們始終在對同一個數組做操作，傳來傳去多白癡
	 * 					
	 * @param		: Integer 		targetPointer
	 * @param		: Integer		leftPointer
	 * @param		: Integer 		rightPointer
	 */
	public void qs(Integer targetPointer, Integer leftPointer, Integer rightPointer) {
		Integer pointer = list.get(targetPointer);
		Integer temp = -1;
		Integer leftTmp = leftPointer , righTemp = rightPointer;	//留個備份，因為在下面的循環中，這個左右指針被修改了
		
		
		/**
		 * 找到本次數組的標的物（作為分割物存在的那個數據）
		 * 循環的次數，取決於起點與終點的距離
		 * 循環的目的，是要在起點與終點指針限定的範圍內逐步移動指針
		 * 移動過程需要將大於標的物的數放到
		 * 
		 * 本來我的寫法是for(int i=0;i<size;i++,leftPointer++)
		 * 同時在for循環中有一個if判斷 if(rightPointer != leftPointer)
		 * 內層對右指針的循環和校驗與上面類似就不再重複了
		 * 但是按照現在的新循環寫法，for(; leftPointer<rightPointer; leftPointer++)
		 * 那麼這個if判斷就不需要了，因為循環的邊界是動態的。
		 * 邊界會幫我們做這一步校驗的。
		 * 同時，i和j也不需要了。因為我們直接對左右指針做移動，還要i和j幹嘛呢？
		 */
		for(; leftPointer<rightPointer; leftPointer++) {
//			System.out.println("隨時看一眼rightPointer，以便觀察循環的邊界是否縮短了 ： " + rightPointer);
			if(list.get(leftPointer) > pointer ) {
				for(; rightPointer>leftPointer; rightPointer--) {
					//走到這裡，說明左指針指向的大而右的小，需要調換左右的這兩個數了
					//其實這裡也可以判斷如果右指針對應的元素小於等於標的物，但是那樣一來，無非是多做一次交換，沒有任何意義
					
					//但是我假設了一個場景，就是本輪循環只有兩個元素，分別為2和1，那麼2比1大會走到這裡，但是1不比1小
					//所以這裡不會進入交換步驟，同時跳出這兩層循環之後，rightPointer == targetPointer，也不會對這兩個元素作交換
					//這時就會有問題了。所以這裡還是需要寫成list.get(rightPointer) <= pointer，而不能僅僅是小於了。
					//這個情況是以數組最右側作為標的物的時候的操作。如果是以數組最左側為標的物，那麼外層循環的if條件應該加上一個等號
					if(list.get(rightPointer) < pointer) {
						temp = list.get(leftPointer);
						list.set(leftPointer, list.get(rightPointer));
						list.set(rightPointer, temp);
//						System.out.println("跳出後，記得看一下新的rightPointer是幾，外層循環的上限是否變了 :" + rightPointer);
						break;
					}
				}
			}
		}
		/**
		 * 從循環出來後看一看，右指針是否動了
		 * 	同時以標的物的位置作為分割物，把原數組分為兩個分別遞歸
		 * 否則說明當前這一段都比標的物小，需要從數組中去掉標的物，
		 * 	再將新數組中最右側的元素作為新的標的物。
		 * 	（最初遇到這種有一側指針未曾移動過的情況的時候，我對選擇極端位置作為標的物的想法動搖了，我想換成中間數
		 * 	但轉念一想，我的下一次遞歸的時候，其實是在一個新數組的基礎上去遞歸了，這個新數組已经不包含老的標的物了
		 * 	那麼，我就算仍然選極端位置也是可以的。因為雖然都是極端位置，但那數字本身已經是新的標的物了
		 * 	最壞情況下，也無非是當前數組已是有序，只是它的有序與你期望的順序是相反的而已。算自己倒霉罷了
		 * 	如果想迴避上述這種情況，那麼就每次用隨機數的方法來選擇標的物。這樣的話，最後仍然是以某個指針是否移動過為標準
		 * 	因為啊，標的物只是作為下次分割的標準，與本次是否交換左右指針是無關的。與下次有關，與本次無關
		 * 	所以綜合考慮，還不如現在這樣，以極端位置作為標的物更簡單呢）
		 */
		if(rightPointer != targetPointer) {
			temp = list.get(rightPointer);
			list.set(rightPointer, list.get(targetPointer));
			list.set(targetPointer, temp);
			targetPointer = rightPointer;
		}else {
			targetPointer--;
		}
		//分別對標的物的左側和右側數組分別進行遞歸
		//說明當前這輪遞歸其實只有兩個或三個元素，就不必再繼續遞歸或處理了，因為絕對已經是有序的了
		if(righTemp - leftTmp <= 2) {
		}
		//如果標的物僅比左指針大1，則僅遞歸右側
		else if(targetPointer - leftPointer == 1)
			qs(righTemp, targetPointer, righTemp);
		//如果標的物僅比右指針小1，則僅遞歸左側
		else if(rightPointer - targetPointer == 1)
			qs(targetPointer, leftTmp, targetPointer);
		//否則都做遞歸
		else {
			qs(targetPointer, leftTmp, targetPointer);
			qs(righTemp, targetPointer, righTemp);
		}
	}
	
	public static void main(String[] args) {
		QuickSort qs = new QuickSort();
		qs.initData();
	}
}
