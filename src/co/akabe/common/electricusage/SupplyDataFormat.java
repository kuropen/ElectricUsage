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
 * 供給データのフォーマットを指定するクラス
 */
public class SupplyDataFormat {

    /**
     * 供給データのURL
     */
    public String dataURL;

    /**
     * 供給データの文字コード
     */
    public final String charset;

    /**
     * ピーク需要の開始行数
     */
    public final int peakDemand_Line;

    /**
     * ピーク供給の開始行数
     */
    public final int peakSupply_Line;

    /**
     * 時間別需要の開始行数
     */
    public final int hourlyDemand_Line;

    /**
     * 5分ごと要の開始行数
     */
    public final int fiveMinDemand_Line;

    /**
     * 時間別需要の第4フィールドが予測である (東電のみtrue)
     */
    public final boolean isNewFormat;

    /**
     * 北海道フラグ (Float型を取り扱う)
     */
    public boolean isHokkaido = false;

    /**
     * データフォーマット変数の初期化
     *
     * @param url URL
     * @param pdl ピーク需要行
     * @param psl ピーク供給行
     * @param hdl 時間別需要行
     * @deprecated 現在使われていない。というのも各社が細かい時間を区切った需要データを公開しており、そちらを優先して使っているため。
     */
    public SupplyDataFormat(final String url, final int pdl, final int psl,
                            final int hdl) {
        dataURL = url;
        peakDemand_Line = pdl;
        peakSupply_Line = psl;
        hourlyDemand_Line = hdl;
        charset = "Shift_JIS";
        isNewFormat = false;
        fiveMinDemand_Line = 0;
    }

    /**
     * データフォーマット変数の初期化
     *
     * @param url     URL
     * @param pdl     ピーク需要行
     * @param psl     ピーク供給行
     * @param hdl     時間別需要行
     * @param fivemin 短時間需要行
     */
    public SupplyDataFormat(final String url, final int pdl, final int psl,
                            final int hdl, final int fivemin) {
        dataURL = url;
        peakDemand_Line = pdl;
        peakSupply_Line = psl;
        hourlyDemand_Line = hdl;
        charset = "Shift_JIS";
        fiveMinDemand_Line = fivemin;
        isNewFormat = true;
    }

    /**
     * データフォーマット変数の初期化
     *
     * @param url URL
     * @param pdl ピーク需要行
     * @param psl ピーク供給行
     * @param hdl 時間別需要行
     * @param cs  文字コード
     * @deprecated 現在使われていない。文字コードが統一されているため。
     */
    public SupplyDataFormat(final String url, final int pdl, final int psl,
                            final int hdl, final String cs) {
        dataURL = url;
        peakDemand_Line = pdl;
        peakSupply_Line = psl;
        hourlyDemand_Line = hdl;
        charset = cs;
        isNewFormat = false;
        fiveMinDemand_Line = 0;
    }

    /**
     * 北海道フラグを立てる
     *
     * @deprecated 代わりに{@link co.akabe.common.electricusage.SupplyDataFormatH}
     * クラスを使用すること。
     */
    public void setAsHokkaido() {
        isHokkaido = true;
    }

}
