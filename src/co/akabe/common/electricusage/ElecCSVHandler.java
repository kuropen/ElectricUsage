package co.akabe.common.electricusage;

/**
 * データ系クラスの一部メソッドの取り扱いを共通化するための抽象クラス
 */
public abstract class ElecCSVHandler {

	/**
	 * 時刻データ
	 */
	protected String time;
	
	/**
	 * 時刻から「:00」を取り、数字だけを返す。
	 * @return 日付データの時刻(n時台)
	 */
	public int getHour () {
		String[] arr = time.split(":");
		int ret = Integer.parseInt(arr[0]);
		return ret;
	}
	
}
