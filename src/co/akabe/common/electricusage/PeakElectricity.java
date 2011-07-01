package co.akabe.common.electricusage;

/**
 * ピーク時の電力 (需要または供給) を表すクラス
 */
public class PeakElectricity extends ElecCSVHandler {

	private PeakElectricityType type;
	private int peakAmount;
	
	/**
	 * コンストラクタ
	 * @param t 需要ならDEMAND, 供給ならSUPPLY
	 * @param tm 明記されているピーク時刻
	 * @param am ピーク時電力量
	 */
	public PeakElectricity (PeakElectricityType t, String tm, int am) {
		type = t;
		time = tm;
		peakAmount = am;
	}
	
	/**
	 * コンストラクタ
	 * @param t 需要ならDEMAND, 供給ならSUPPLY
	 * @param tm 明記されているピーク時刻
	 * @param ams ピーク時電力量
	 */
	public PeakElectricity (PeakElectricityType t, String tm, String ams) {
		this(t, tm, Integer.parseInt(ams));
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
	public int getPeakAmount () {
		return peakAmount;
	}
	
	@Override
	public String toString() {
		String ret = "最大電力"+(type == PeakElectricityType.DEMAND ? "需要" : "供給")+"は"+
		this.getHour()+"時台において"+peakAmount+"万kWです。";
		return ret;
	}
	
}