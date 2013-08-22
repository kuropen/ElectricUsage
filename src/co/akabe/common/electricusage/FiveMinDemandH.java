package co.akabe.common.electricusage;

public class FiveMinDemandH extends FiveMinDemand {
	/**
     * コンストラクタ：九州電力以外
     * @param d 日付
     * @param t 時刻
     * @param dm 需要
     */
    public FiveMinDemandH(String d, String t, String dm) {
            super(d, t, dm.split("\\.")[0], "-1048576");
            // TODO Auto-generated constructor stub
    }

    /**
     * コンストラクタ：九州電力
     * @param d 日付
     * @param t 時刻
     * @param dm 需要
     * @param y 前日比
     */
    public FiveMinDemandH(String d, String t, String dm, String y) {
            super(d, t, dm.split("\\.")[0], y);
            // TODO Auto-generated constructor stub
    }
}
