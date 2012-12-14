package co.akabe.common.electricusage;

/**
 * ピーク時の電力 (需要または供給) を表すクラス
 */
public class PeakSupply extends ElecCSVHandler implements PeakElectricity {

	private int peakAmount;
	
	/**
	 * コンストラクタ
	 * @param tm 明記されているピーク時刻
	 * @param am ピーク時電力量
	 */
	public PeakSupply (String tm, int am) {
		time = tm;
		peakAmount = am;
	}
	
	/**
	 * コンストラクタ
	 * @param tm 明記されているピーク時刻
	 * @param ams ピーク時電力量
	 */
	public PeakSupply (String tm, String ams) {
		this(tm, Integer.parseInt(ams));
	}
	
	/**
	 * ピーク時刻を返す。
	 * @return ピーク時刻。フォーマットは元データ依存（変換しません）
	 */
	public String getTime () {
		return time;
	}
	
	/**
	 * ピーク時電力量を返す。
	 * @return ピーク時電力量
	 */
	public int getAmount () {
		return peakAmount;
	}
	
	@Override
	public String toString() {
		String ret = "最大電力供給は"+
		this.getHour()+"時台において"+peakAmount+"万kWです。";
		return ret;
	}

	@Override @Deprecated
	public int getPeakAmount() {
		return getAmount();
	}
	
}
