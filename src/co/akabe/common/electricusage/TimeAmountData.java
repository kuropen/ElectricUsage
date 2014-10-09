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
 * データ系クラスの一部メソッドの取り扱いを共通化するための抽象クラス
 */
public abstract class TimeAmountData {

	/**
	 * 時刻データ
	 */
	protected String time;

	private String[] getTimeArray() {
		return time.split(":");
	}

	/**
	 * 時刻から「:00」を取り、数字だけを返す。
	 * 
	 * @return 日付データの時刻(n時台)
	 */
	public int getHour() {
		int ret = Integer.parseInt(getTimeArray()[0]);
		return ret;
	}

	public int getMinute() {
		int ret = Integer.parseInt(getTimeArray()[1]);
		return ret;
	}
	
	/**
     * 供給力の基準点となる時刻を返す。
     *
     * @return 時刻（文字列）
     */
    public abstract String getTime();

    /**
     * ピーク値を示す。
     *
     * @return ピーク値(万kW)
     */
    public abstract int getAmount();
    
    /**
     * ピーク値を示す。
     *
     * @return ピーク値(万kW)
     * @deprecated Use {{@link #getAmount()} instead
     */
    public int getPeakAmount() {
    	return getAmount();
    }

}
