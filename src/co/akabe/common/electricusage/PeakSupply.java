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

/**
 * ピーク時の電力 (需要または供給) を表すクラス
 */
public class PeakSupply extends TimeAmountData {

    private int peakAmount;

    /**
     * コンストラクタ
     *
     * @param tm 明記されているピーク時刻
     * @param am ピーク時電力量
     */
    public PeakSupply(String tm, int am) {
        time = tm;
        peakAmount = am;
    }

    /**
     * コンストラクタ
     *
     * @param tm  明記されているピーク時刻
     * @param ams ピーク時電力量
     */
    public PeakSupply(String tm, String ams) {
        this(tm, Integer.parseInt(ams));
    }

    /**
     * ピーク時刻を返す。
     *
     * @return ピーク時刻。フォーマットは元データ依存（変換しません）
     */
    public String getTime() {
        return time;
    }

    /**
     * ピーク時電力量を返す。
     *
     * @return ピーク時電力量
     */
    public int getAmount() {
        return peakAmount;
    }

    @Override
    public String toString() {
        String ret = "最大電力供給は" + this.getHour() + "時台において" + peakAmount
                + "万kWです。";
        return ret;
    }

}
