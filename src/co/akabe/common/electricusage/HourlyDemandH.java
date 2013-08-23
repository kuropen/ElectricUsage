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

public class HourlyDemandH extends HourlyDemand {
	/**
     * コンストラクタ
     * @param d 日付
     * コンストラクタ
     * @param d 日付
     * @param t 時刻
     * @param dm 当日実績
     * @param y 前日実績
     */
    public HourlyDemandH (String d, String t, String dm, String y) {
            super(d, t, dm.split("\\.")[0], y);
    }

    /**
     * コンストラクタ
     * @param d 日付
     * @param t 時刻
     * @param dm 当日実績
     * @param five 5分需要フラグ
     * @Deprecated Use {@link FiveMinDemand} class.
     */
    public HourlyDemandH (String d, String t, String dm, String y, boolean five) {
            super(d, t, dm.split("\\.")[0], y, five);
    }
}
