package co.akabe.common.electricusage;

/**
 * 北海道用のデータフォーマット
 */
public class SupplyDataFormatH extends SupplyDataFormat {

	public SupplyDataFormatH(final String url, final int pdl, final int psl,
			final int hdl, final int fivemin) {
		super(url, pdl, psl, hdl, fivemin);
		isHokkaido = true;
	}

}
