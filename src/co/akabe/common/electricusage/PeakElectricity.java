package co.akabe.common.electricusage;

/**
 * 需要クラスのインターフェイス
 * @author Hirochika Yuda, shinkai.sdpl@gmail.com
 */
public interface PeakElectricity {

	/**
	 * 供給力の基準点となる時刻を返す。
	 * @return 時刻（文字列）
	 */
	public String getTime();
	
	/**
	 * ピーク値を示す。
	 * @return ピーク値(万kW)
	 */
	public int getAmount();
	
	/**
	 * 互換性のためのメソッド。
	 * @return ピーク値(万kW)
	 * @deprecated Use {@link #getAmount()}.
	 */
	@Deprecated
	public int getPeakAmount();
	
}
