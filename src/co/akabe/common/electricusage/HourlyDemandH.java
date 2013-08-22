package co.akabe.common.electricusage;

public class HourlyDemandH extends HourlyDemand {
	/**
     * コンストラクタ
     * @param d 日付
     * コンストラクタ
     * @param d 日付
     * @param t 時刻
     * @param dm 当日実績
     * @param y 前日実績
     */
    public HourlyDemandH (String d, String t, String dm, String y) {
            super(d, t, dm.split("\\.")[0], y);
    }

    /**
     * コンストラクタ
     * @param d 日付
     * @param t 時刻
     * @param dm 当日実績
     * @param five 5分需要フラグ
     * @Deprecated Use {@link FiveMinDemand} class.
     */
    public HourlyDemandH (String d, String t, String dm, String y, boolean five) {
            super(d, t, dm.split("\\.")[0], y, five);
    }
}
