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

public class FiveMinDemandH extends FiveMinDemand {
    /**
     * コンストラクタ：九州電力以外
     *
     * @param d  日付
     * @param t  時刻
     * @param dm 需要
     */
    public FiveMinDemandH(String d, String t, String dm) {
        super(d, t, dm.split("\\.")[0], "-1048576");
        // TODO Auto-generated constructor stub
    }

    /**
     * コンストラクタ：九州電力
     *
     * @param d  日付
     * @param t  時刻
     * @param dm 需要
     * @param y  前日比
     */
    public FiveMinDemandH(String d, String t, String dm, String y) {
        super(d, t, dm.split("\\.")[0], y);
        // TODO Auto-generated constructor stub
    }
}
