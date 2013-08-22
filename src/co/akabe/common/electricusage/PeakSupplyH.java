package co.akabe.common.electricusage;

public class PeakSupplyH extends PeakSupply {
	/**
     * コンストラクタ
     * @param tm 明記されているピーク時刻
     * @param am ピーク時電力量
     */
    public PeakSupplyH (String tm, float am) {
            super(tm, (int)am);
            time = tm;
    }

    /**
     * コンストラクタ
     * @param tm 明記されているピーク時刻
     * @param ams ピーク時電力量
     */
    public PeakSupplyH (String tm, String ams) {
            this(tm, Float.parseFloat(ams));
    }
}
