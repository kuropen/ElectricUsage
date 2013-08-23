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
 * 5分ごと需要を示すクラス。
 * @author Hirochika Yuda, shinkai.sdpl@gmail.com
 */
public class FiveMinDemand extends HourlyDemand {

	/**
	 * コンストラクタ：九州電力以外
	 * @param d 日付
	 * @param t 時刻
	 * @param dm 需要
	 */
	public FiveMinDemand(String d, String t, String dm) {
		super(d, t, dm, "-1048576");
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * コンストラクタ：九州電力
	 * @param d 日付
	 * @param t 時刻
	 * @param dm 需要
	 * @param y 前日比
	 */
	public FiveMinDemand(String d, String t, String dm, String y) {
		super(d, t, dm, y);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return this.getTime() + "の需要実績は" + getDemandToday() + "万kWでした。";
	}
	
//	@Override
//	public String toStringWithDiff() {
//		return toString() + appendDiff();
//	}
//	
//	@Override
//	public String toStringWithPercentage(PeakSupply pe) {
//		return toString() + appendPercentage(pe);
//	}
//	
//	@Override
//	public String toStringWithDiffandPercentage(PeakSupply pe) {
//		return toString() + appendDiff() + appendPercentage(pe);
//	}
	
}
