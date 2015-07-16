/*
 * Copyright (C) 2011-2013 Kuropen.
 * 
 * This file is part of the Electricity Usage Parser Library.
 * 
 * The Electricity Usage Parser Library is free software:
 * you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * The Electricity Usage Parser Library is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with The Electricity Usage Parser Library.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package co.akabe.common.electricusage;

import java.util.Vector;

/**
 * 時間ごとの需要を格納するクラス
 */
public class HourlyDemand extends TimeAmountData {

    /**
     * 日付
     */
    private String dt;
    /**
     * 当日実績
     */
    private int td;
    /**
     * 前日実績
     */
    private int yd;

    /**
     * 5分ごと需要か？
     *
     * @Deprecated {@link FiveMinDemand} に置き換え。
     */
    @SuppressWarnings("unused")
    @Deprecated
    private boolean is5Min;

    /**
     * コンストラクタ
     *
     * @param d  日付
     * @param t  時刻
     * @param dm 当日実績
     * @param y  前日実績
     */
    public HourlyDemand(String d, String t, String dm, String y) {
        dt = d;
        time = t;
        td = Integer.parseInt(dm);
        yd = Integer.parseInt(y);
        is5Min = false;
    }

    /**
     * コンストラクタ
     *
     * @param d    日付
     * @param t    時刻
     * @param dm   当日実績
     * @param five 5分需要フラグ
     * @Deprecated Use {@link FiveMinDemand} class.
     */
    public HourlyDemand(String d, String t, String dm, String y, boolean five) {
        dt = d;
        time = t;
        td = Integer.parseInt(dm);
        yd = Integer.parseInt(y);
        is5Min = five;
    }

    /**
     * 日付を返す。
     *
     * @return 実績データの日付
     */
    public String getDate() {
        return dt;
    }

    /**
     * 時刻を返す。
     *
     * @return 日付データの時刻
     */
    public String getTime() {
        return time;
    }

    /**
     * 当日実績を返す。
     *
     * @return 当日実績
     */
    public int getDemandToday() {
        return td;
    }

    /**
     * 前日実績を返す。
     *
     * @return 前日実績
     */
    public int getDemandYesterday() {
        return yd;
    }

    /**
     * ピーク供給に対する使用率を返す。
     *
     * @param s 使用率の算定基準となるピーク供給
     * @return 使用率 (パーセント単位)
     */
    public float getUsePercentage(PeakSupply s) {
        int peakSupply = s.getAmount();
        if (peakSupply <= 0) {
            return 0;
        }
        float ret = (float) td / peakSupply * 100;
        return ret;
    }

    /**
     * 前日との実績差を返す。
     *
     * @return 前日との実績差
     */
    public int getDifference() {
        if (yd == -1048576)
            return 0;
        return td - yd;
    }

    @Override
    public String toString() {
        return this.getHour() + "時台の需要実績は" + td + "万kWでした。";
    }

    /**
     * 前日差を付け加えたtoString
     *
     * @return メッセージ文字列 + 前日差
     */
    public String toStringWithDiff() {
        return this.toString() + this.appendDiff();
    }

    /**
     * 使用率を付け加えたtoString
     *
     * @return メッセージ文字列 + 使用率
     */
    public String toStringWithPercentage(PeakSupply pe) {
        return this.toString() + this.appendPercentage(pe);
    }

    /**
     * 前日差・使用率を付け加えたtoString
     *
     * @return メッセージ文字列 + 前日差 + 使用率
     */
    public String toStringWithDiffandPercentage(PeakSupply pe) {
        return this.toString() + this.appendDiff() + this.appendPercentage(pe);
    }

    /**
     * toStringにおける前日差増分
     *
     * @return 前日差を表す文字列
     */
    protected String appendDiff() {
        if (yd == -1048576)
            return "";
        int d = this.getDifference();
        return "(前日比 " + ((d > 0) ? "+" : "") + d + " 万kW)";
    }

    /**
     * toStringにおけるパーセント増分
     *
     * @return 電力使用率を表す文字列
     */
    protected String appendPercentage(PeakSupply pe) {
        return "(ピーク供給力に対する使用率は "
                + String.format("%.2f", this.getUsePercentage(pe)) + "%)";
    }

    /**
     * 最新データを探す
     *
     * @param v 時間帯ごとのデータのセット
     * @return 最新需要データ
     */
    public static <T extends HourlyDemand> T seekNearestHistory(Vector<T> v) {
        if (v == null) {
            return null;
        }
        for (int i = (v.size() - 1); i >= 0; i--) { // 後ろから探索
            T tmp = v.get(i);
            if (tmp.getDemandToday() > 0)
                return tmp; // 0でないデータが発見されたら得られる最新データ
        }
        return null;
    }

    @Override
    public int getAmount() {
        return getDemandToday();
    }

}
