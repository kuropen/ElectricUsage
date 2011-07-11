package co.akabe.common.electricusage;

/**
 * 供給データのフォーマットを指定するクラス
 */
public class SupplyDataFormat {

	/**
	 * 供給データのURL
	 */
	public final String dataURL;
	
	/**
	 * 供給データの文字コード
	 */
	public final String charset;
	
	/**
	 * ピーク需要の開始行数
	 */
	public final int peakDemand_Line;
	
	/**
	 * ピーク供給の開始行数
	 */
	public final int peakSupply_Line;
	
	/**
	 * 時間別需要の開始行数
	 */
	public final int hourlyDemand_Line;
	
	/**
	 * 5分ごと要の開始行数
	 */
	public final int fiveMinDemand_Line;
	
	/**
	 * 時間別需要の第4フィールドが予測である (東電のみtrue)
	 */
	public final boolean isNewFormat;
	
	/**
	 * データフォーマット変数の初期化
	 * @param url URL
	 * @param pdl ピーク需要行
	 * @param psl ピーク供給行
	 * @param hdl 時間別需要行
	 */
	public SupplyDataFormat (final String url, final int pdl, final int psl, final int hdl) {
		dataURL = url;
		peakDemand_Line = pdl;
		peakSupply_Line = psl;
		hourlyDemand_Line = hdl;
		charset = "Shift_JIS";
		isNewFormat = false;
		fiveMinDemand_Line = 0;
	}
	
	/**
	 * データフォーマット変数の初期化
	 * @param url URL
	 * @param pdl ピーク需要行
	 * @param psl ピーク供給行
	 * @param hdl 時間別需要行
	 * @param fivemin 5分ごと需要行
	 */
	public SupplyDataFormat (final String url, final int pdl, final int psl, final int hdl, final int fivemin) {
		dataURL = url;
		peakDemand_Line = pdl;
		peakSupply_Line = psl;
		hourlyDemand_Line = hdl;
		charset = "Shift_JIS";
		fiveMinDemand_Line = fivemin;
		isNewFormat = true;
	}
	
	/**
	 * データフォーマット変数の初期化
	 * @param url URL
	 * @param pdl ピーク需要行
	 * @param psl ピーク供給行
	 * @param hdl 時間別需要行
	 * @param cs 文字コード
	 */
	public SupplyDataFormat (final String url, final int pdl, final int psl, final int hdl, final String cs) {
		dataURL = url;
		peakDemand_Line = pdl;
		peakSupply_Line = psl;
		hourlyDemand_Line = hdl;
		charset = cs;
		isNewFormat = false;
		fiveMinDemand_Line = 0;
	}
		
}
