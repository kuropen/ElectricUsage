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

public class PeakDemand extends ElecCSVHandler implements PeakElectricity {

	private int peakAmount;
	
	/**
	 * コンストラクタ
	 * @param tm 明記されているピーク時刻
	 * @param am ピーク時電力量
	 */
	public PeakDemand (String tm, int am) {
		time = tm;
		peakAmount = am;
	}
	
	/**
	 * コンストラクタ
	 * @param tm 明記されているピーク時刻
	 * @param ams ピーク時電力量
	 */
	public PeakDemand (String tm, String ams) {
		this(tm, Integer.parseInt(ams));
	}
	
	@Override
	public String getTime() {
		return time;
	}

	@Override
	public int getAmount() {
		return peakAmount;
	}

	@Override @Deprecated
	public int getPeakAmount() {
		return getAmount();
	}
	
	@Override
	public String toString() {
		String ret = "最大電力需要は"+
		this.getHour()+"時台において"+peakAmount+"万kWです。";
		return ret;
	}

}
