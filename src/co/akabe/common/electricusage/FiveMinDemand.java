package co.akabe.common.electricusage;

/**
 * 5分ごと需要を示すクラス。
 * @author Hirochika Yuda, shinkai.sdpl@gmail.com
 */
public class FiveMinDemand extends HourlyDemand {

	/**
	 * コンストラクタ：九州電力以外
	 * @param d 日付
	 * @param t 時刻
	 * @param dm 需要
	 */
	public FiveMinDemand(String d, String t, String dm) {
		super(d, t, dm, "-1048576");
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * コンストラクタ：九州電力
	 * @param d 日付
	 * @param t 時刻
	 * @param dm 需要
	 * @param y 前日比
	 */
	public FiveMinDemand(String d, String t, String dm, String y) {
		super(d, t, dm, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return this.getTime() + "の需要実績は" + getDemandToday() + "万kWでした。";
	}
	
	@Override
	public String toStringWithDiff() {
		return toString() + appendDiff();
	}
	
	@Override
	public String toStringWithPercentage(PeakSupply pe) {
		return toString() + appendPercentage(pe);
	}
	
	@Override
	public String toStringWithDiffandPercentage(PeakSupply pe) {
		return toString() + appendDiff() + appendPercentage(pe);
	}
	
}
