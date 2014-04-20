package co.akabe.common.electricusage.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import co.akabe.common.electricusage.ElectricUsageCSVParser;
import co.akabe.common.electricusage.SupplyDataFormat;

public class ElectricUsageCSVParserTest extends TestCase {

	@Test
	public void testBuildKyushuFormat() {
		SupplyDataFormat kf = ElectricUsageCSVParser.buildKyushuFormat();
		assertEquals("http://www.kyuden.co.jp/power_usages/csv/juyo-hourly-"
				+ new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv",
				kf.dataURL);
	}

}
