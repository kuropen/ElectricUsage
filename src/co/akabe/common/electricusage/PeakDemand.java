package co.akabe.common.electricusage;

public class PeakDemand extends ElecCSVHandler implements PeakElectricity {

	private int peakAmount;
	
	/**
	 * コンストラクタ
	 * @param tm 明記されているピーク時刻
	 * @param am ピーク時電力量
	 */
	public PeakDemand (String tm, int am) {
		time = tm;
		peakAmount = am;
	}
	
	/**
	 * コンストラクタ
	 * @param tm 明記されているピーク時刻
	 * @param ams ピーク時電力量
	 */
	public PeakDemand (String tm, String ams) {
		this(tm, Integer.parseInt(ams));
	}
	
	@Override
	public String getTime() {
		return time;
	}

	@Override
	public int getAmount() {
		return peakAmount;
	}

	@Override @Deprecated
	public int getPeakAmount() {
		return getAmount();
	}

}
