package co.akabe.common.electricusage;

import java.util.Vector;

/**
 * 時間ごとの需要を格納するクラス
 */
public class HourlyDemand extends ElecCSVHandler {

	/** 日付 */
	private String dt;
	/** 当日実績 */
	private int td;
	/** 前日実績 */
	private int yd;
	
	/**
	 * 5分ごと需要か？
	 */
	private boolean is5Min;
	
	/**
	 * コンストラクタ
	 * @param d 日付
	 * @param t 時刻
	 * @param dm 当日実績
	 * @param y 前日実績
	 */
	public HourlyDemand (String d, String t, String dm, String y) {
		dt = d;
		time = t;
		td = Integer.parseInt(dm);
		yd = Integer.parseInt(y);
		is5Min = false;
	}
	
	/**
	 * コンストラクタ
	 * @param d 日付
	 * @param t 時刻
	 * @param dm 当日実績
	 * @param five 5分需要フラグ
	 */
	public HourlyDemand (String d, String t, String dm, String y, boolean five) {
		dt = d;
		time = t;
		td = Integer.parseInt(dm);
		yd = Integer.parseInt(y);
		is5Min = five;
	}
	
	/**
	 * 日付を返す。
	 * @return 実績データの日付
	 */
	public String getDate () {
		return dt;
	}
	
	/**
	 * 時刻を返す。
	 * @return 日付データの時刻
	 */
	public String getTime () {
		return time;
	}
	
	/**
	 * 時刻から「:00」を取り、数字だけを返す。
	 * @return 日付データの時刻(n時台)
	 */
	public int getHour () {
		String[] arr = time.split(":");
		int ret = Integer.parseInt(arr[0]);
		return ret;
	}
	
	/**
	 * 当日実績を返す。
	 * @return 当日実績
	 */
	public int getDemandToday () {
		return td;
	}
	
	/**
	 * 前日実績を返す。
	 * @return 前日実績
	 */
	public int getDemandYesterday () {
		return yd;
	}
	
	/**
	 * ピーク供給に対する使用率を返す。
	 * @param pe 使用率の算定基準となるピーク供給 (需要を入れても通じますが意味がないです)
	 * @return 使用率 (パーセント単位)
	 */
	public float getUsePercentage (PeakElectricity pe) {
		int peakSupply = pe.getPeakAmount();
		float ret = (float)td / peakSupply * 100;
		return ret;
	}
	
	/**
	 * 前日との実績差を返す。
	 * @return 前日との実績差
	 */
	public int getDifference () {
		if (yd == -1048576) return 0;
		return td - yd;
	}
	
	@Override
	public String toString() {
		if (!is5Min)
			return this.getHour() + "時台の需要実績は" + td + "万kWでした。";
		else
			return this.getTime() + "の需要実績は" + td + "万kWでした。";
	}
	
	/**
	 * 前日差を付け加えたtoString
	 * @return メッセージ文字列 + 前日差
	 */
	public String toStringWithDiff() {
		return this.toString() + this.appendDiff();
	}
	
	/**
	 * 使用率を付け加えたtoString
	 * @return メッセージ文字列 + 使用率
	 */
	public String toStringWithPercentage(PeakElectricity pe) {
		return this.toString() + this.appendPercentage(pe);
	}
	
	/**
	 * 前日差・使用率を付け加えたtoString
	 * @return メッセージ文字列 + 前日差 + 使用率
	 */
	public String toStringWithDiffandPercentage(PeakElectricity pe) {
		return this.toString() + this.appendDiff() + this.appendPercentage(pe);
	}
	
	/**
	 * toStringにおける前日差増分
	 * @return 前日差を表す文字列
	 */
	private String appendDiff () {
		if (yd == -1048576) return "";
		int d = this.getDifference();
		return "(前日比 " + ((d > 0) ? "+" : "") + d + " 万kW)";
	}
	
	/**
	 * toStringにおけるパーセント増分
	 * @return 電力使用率を表す文字列
	 */
	private String appendPercentage (PeakElectricity pe) {
		return "(ピーク供給力に対する使用率は " + String.format("%.2f", this.getUsePercentage(pe)) + "%)";
	}
	
	/**
	 * 最新データを探す
	 * @param v 時間帯ごとのデータのセット
	 * @return 最新需要データ
	 */
	public static HourlyDemand seekNearestHistory (Vector<? extends HourlyDemand> v) {
		if (v == null) return null;
		for (int i = (v.size() - 1); i >= 0; i--) { //後ろから探索
			HourlyDemand tmp = v.get(i);
			if (tmp.getDemandToday() > 0) return tmp; //0でないデータが発見されたら得られる最新データ
		}
		return null;
	}
	
}
