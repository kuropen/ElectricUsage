package co.akabe.common.electricusage.test;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import co.akabe.common.electricusage.ElectricUsageCSVParser;
import co.akabe.common.electricusage.PeakDemand;
import co.akabe.common.electricusage.PeakSupply;
import co.akabe.common.electricusage.SupplyDataFormat;

public class ElectricUsageCSVParserTest extends TestCase {

    private SupplyDataFormat[] formats;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        formats = new SupplyDataFormat[]{
                ElectricUsageCSVParser.Format_Hokkaido,
                ElectricUsageCSVParser.Format_Tohoku,
                ElectricUsageCSVParser.Format_Tokyo,
                ElectricUsageCSVParser.Format_Hokuriku,
                ElectricUsageCSVParser.Format_Chubu,
                ElectricUsageCSVParser.Format_Kansai,
                ElectricUsageCSVParser.Format_Chugoku,
                ElectricUsageCSVParser.Format_Shikoku,
                ElectricUsageCSVParser.buildKyushuFormat()};
    }

    @Test
    public void testBuildKyushuFormat() {
        SupplyDataFormat kf = ElectricUsageCSVParser.buildKyushuFormat();
        assertEquals("http://www.kyuden.co.jp/power_usages/csv/juyo-hourly-"
                        + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".csv",
                kf.dataURL);
    }

    @Test
    public void testGetPeakSupply() {
        for (SupplyDataFormat format : formats) {
            ElectricUsageCSVParser parser = new ElectricUsageCSVParser(format);
            PeakSupply ps = parser.getPeakSupply();
            if (ps != null) {
                // 北陸・中国・四国で休日の場合はnullを返すのが正常なのでnullチェックを行う
                assertTrue(ps.getAmount() > 0);
            }
        }
    }

    @Test
    public void testGetPeakDemand() {
        for (SupplyDataFormat format : formats) {
            ElectricUsageCSVParser parser = new ElectricUsageCSVParser(format);
            PeakDemand pd = parser.getPeakDemand();
            if (pd != null) {
                // 北陸・中国・四国で休日の場合はnullを返すのが正常なのでnullチェックを行う
                assertTrue(pd.getAmount() > 0);
            }
        }
    }

}
