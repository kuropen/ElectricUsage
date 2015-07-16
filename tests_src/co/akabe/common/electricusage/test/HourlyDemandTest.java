package co.akabe.common.electricusage.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import co.akabe.common.electricusage.HourlyDemand;
import co.akabe.common.electricusage.PeakSupply;

public class HourlyDemandTest extends TestCase {

    private HourlyDemand mHourlyDemand;
    private String mDateString;
    private String mTimeString;
    private int mDemand;
    private Random mRandom;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d");
        mDateString = sdf.format(now);
        SimpleDateFormat stf = new SimpleDateFormat("H:m");
        mTimeString = stf.format(now);
        mRandom = new Random();
        mDemand = mRandom.nextInt(1000);
        mHourlyDemand = new HourlyDemand(mDateString, mTimeString,
                Integer.toString(mDemand), "-1048576");
    }

    @Test
    public void testGetDate() {
        assertEquals(mDateString, mHourlyDemand.getDate());
    }

    @Test
    public void testGetTime() {
        assertEquals(mTimeString, mHourlyDemand.getTime());
    }

    @Test
    public void testGetDemandToday() {
        assertEquals(mDemand, mHourlyDemand.getDemandToday());
    }

    @Test
    public void testGetDemandYesterday() {
        assertEquals(-1048576, mHourlyDemand.getDemandYesterday());
    }

    @Test
    public void testGetUsePercentage() {
        int supply = mRandom.nextInt(1000);
        PeakSupply psTest1 = new PeakSupply(mTimeString, supply);
        PeakSupply psTest2 = new PeakSupply(mTimeString, 0);
        assertEquals("通常の使用率計算", (float) mDemand / supply * 100, mHourlyDemand.getUsePercentage(psTest1));
        assertEquals("供給がゼロの場合", 0.0f, mHourlyDemand.getUsePercentage(psTest2));
    }

}
