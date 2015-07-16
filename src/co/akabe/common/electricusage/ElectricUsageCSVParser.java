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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * Parser class of demand CSV data
 *
 * @author Hirochika Yuda, shinkai.sdpl@gmail.com
 */
public class ElectricUsageCSVParser {

    /**
     * 東京電力管内のデータURL
     *
     * @deprecated Use {@link #Format_Tokyo} instead. NOW THIS VARIABLE DOES NOT
     * WORK WELL.
     */
    public static final String UsageDataURL_Tokyo = "http://www.tepco.co.jp/forecast/html/images/juyo-j.csv";

    /**
     * 東北電力管内のデータURL
     *
     * @deprecated Use {@link #Format_Tohoku} instead
     */
    public static final String UsageDataURL_Tohoku = "http://setsuden.tohoku-epco.co.jp/common/demand/juyo_tohoku.csv";

    /**
     * 東北電力のデータフォーマット定義
     */
    public static final SupplyDataFormat Format_Tohoku = new SupplyDataFormat(
            "http://setsuden.tohoku-epco.co.jp/common/demand/juyo_tohoku.csv",
            5, 2, 8, 44);

    /**
     * 東京電力のデータフォーマット定義
     */
    public static final SupplyDataFormat Format_Tokyo = new SupplyDataFormat(
            "http://www.tepco.co.jp/forecast/html/images/juyo-j.csv", 5, 2, 8,
            44);

    /**
     * 関西電力のデータフォーマット定義
     */
    public static final SupplyDataFormat Format_Kansai = new SupplyDataFormat(
            "http://www.kepco.co.jp/yamasou/juyo1_kansai.csv", 5, 2, 11, 49);

    /**
     * 中部電力のデータフォーマット定義
     */
    public static final SupplyDataFormat Format_Chubu = new SupplyDataFormat(
            "http://denki-yoho.chuden.jp/denki_yoho_content_data/juyo_cepco003.csv",
            5, 2, 24, 76);

    /**
     * 九州電力のデータフォーマット定義
     *
     * @deprecated 日付をまたぐとデータが取得できなくなってしまうため、 {@link #buildKyushuFormat()}を使うこと。
     */
    @Deprecated
    public static final SupplyDataFormat Format_Kyushu = new SupplyDataFormat(
            "http://www.kyuden.co.jp/power_usages/csv/juyo-hourly-"
                    + new SimpleDateFormat("yyyyMMdd").format(new Date())
                    + ".csv", 5, 2, 8, 44);

    /**
     * 中国電力のデータフォーマット定義
     */
    public static final SupplyDataFormat Format_Chugoku = new SupplyDataFormat(
            "http://www.energia.co.jp/jukyuu/sys/juyo-j.csv", 5, 2, 8, 44);

    /**
     * 北海道電力のデータフォーマット定義
     */
    public static final SupplyDataFormatH Format_Hokkaido = new SupplyDataFormatH(
            "http://denkiyoho.hepco.co.jp/data/juyo_hokkaidou.csv", 5, 2, 11,
            44);

    /**
     * 四国電力のデータフォーマット定義
     */
    public static final SupplyDataFormat Format_Shikoku = new SupplyDataFormat(
            "http://www.yonden.co.jp/denkiyoho/juyo_yonden.csv", 5, 2, 8, 40);

    /**
     * 北陸電力のデータフォーマット定義
     */
    public static final SupplyDataFormat Format_Hokuriku = new SupplyDataFormat(
            "http://www.rikuden.co.jp/denki-yoho/csv/juyo-rikuden.csv", 5, 2,
            8, 44);

    /**
     * 九州電力のデータフォーマット定義を作成する
     *
     * @return 九州電力のデータフォーマット定義
     */
    public static SupplyDataFormat buildKyushuFormat() {
        return new SupplyDataFormat(
                "http://www.kyuden.co.jp/power_usages/csv/juyo-hourly-"
                        + new SimpleDateFormat("yyyyMMdd").format(new Date())
                        + ".csv", 5, 2, 8, 44);
    }

    private SupplyDataFormat df;
    private Vector<String> buff;

    public ElectricUsageCSVParser(SupplyDataFormat f) {
        df = f;
    }

    /**
     * コンストラクタ (キャラクタセット省略型)<br>
     * データのCharsetはShift_JISであるとみなします。
     *
     * @param URL ソースURL ({@link #UsageDataURL_Tokyo},
     *            {@link #UsageDataURL_Tohoku}の利用を推奨)
     */
    @SuppressWarnings("deprecation")
    public ElectricUsageCSVParser(String URL) {
        // this(URL, "Shift_JIS");
        df = new SupplyDataFormat(URL, 2, 5, 8);
        buff = null;
    }

    /**
     * コンストラクタ<br>
     * 現在Shift_JIS以外の文字コードでのデータを公表している会社がないので、このコンストラクタはあまり使用されない。
     *
     * @param URL     ソースURL
     * @param charset 文字コード
     */
    @SuppressWarnings("deprecation")
    public ElectricUsageCSVParser(String URL, String charset) {
        // usageDataURL = URL;
        df = new SupplyDataFormat(URL, 2, 5, 8, charset);
        buff = null;
    }

    /**
     * URLからテキストを読み込む
     *
     * @return URL上にあるテキスト。エラー発生時はnull
     * @throws java.io.IOException 通信エラーまたは入力ストリームでエラーが発生した場合にスローします
     * @since 1.0
     */
    private Vector<String> readFromURL() throws IOException {
        URL url = new URL(df.dataURL);
        URLConnection connection = url.openConnection();
        connection.setDoInput(true);
        InputStream inStream = connection.getInputStream();

        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    inStream, df.charset));

            String line = "";
            Vector<String> ret = new Vector<String>();
            while ((line = input.readLine()) != null)
                ret.add(line);
            return ret;
        } finally {
            inStream.close();
        }
    }

    /**
     * ピーク時の予想最大電力（需要）を得る。
     *
     * @return ピーク時の予想最大電力
     * @throws java.io.IOException
     */
    public PeakDemand getPeakDemand() {
        if (buff == null)
            try {
                buff = readFromURL();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        String baseData = buff.get(df.peakDemand_Line);
        String[] baseDataArray = baseData.split(",");
        PeakDemand ret;
        if (df.isHokkaido)
            ret = new PeakDemandH(baseDataArray[1], baseDataArray[0]);
        else
            ret = new PeakDemand(baseDataArray[1], baseDataArray[0]);
        return ret;
    }

    /**
     * ピーク時の予想最大電力供給を得る。
     *
     * @return ピーク時の最大電力供給
     */
    public PeakSupply getPeakSupply() {
        if (buff == null)
            try {
                buff = readFromURL();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        String baseData = buff.get(df.peakSupply_Line); // 上から3行目に明記されている
        String[] baseDataArray = baseData.split(",");
        PeakSupply ret;
        if (df.isHokkaido)
            ret = new PeakSupplyH(baseDataArray[1], baseDataArray[0]);
        else
            ret = new PeakSupply(baseDataArray[1], baseDataArray[0]);
        return ret;
    }

    /**
     * 時間ごとの需要実績データを得る。
     *
     * @return 時間ごとの需要実績
     */
    public Vector<HourlyDemand> getHourlyDemand() {
        // 九州電力に対する特例：九電は5分ごとのデータしか公開していない
        if (df.hourlyDemand_Line == 0) {
            return null;
        }

        Vector<HourlyDemand> ret = new Vector<HourlyDemand>();
        if (buff == null)
            try {
                buff = readFromURL();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        final int startLine = df.hourlyDemand_Line;
        for (int i = startLine; i < startLine + 24; i++) {
            String basedata = buff.get(i);
            String[] baseDataArray = basedata.split(",");
            String diffStr = baseDataArray[3];
            if (df.isNewFormat)
                diffStr = "-1048576"; // 最終フィールドが予測(東電)ならば前日比を表示させないため-1048576を送る
            HourlyDemand hd;
            if (df.isHokkaido)
                hd = new HourlyDemandH(baseDataArray[0], baseDataArray[1],
                        baseDataArray[2], diffStr);
            else
                hd = new HourlyDemand(baseDataArray[0], baseDataArray[1],
                        baseDataArray[2], diffStr);
            ret.add(hd);
        }
        return ret;
    }

    /**
     * 5分ごとの需要実績データを得る。
     *
     * @return 時間ごとの需要実績
     */
    public Vector<FiveMinDemand> get5MinDemand() {
        if (df.fiveMinDemand_Line == 0) {
            return null;
        }
        Vector<FiveMinDemand> ret = new Vector<FiveMinDemand>();
        if (buff == null)
            try {
                buff = readFromURL();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return null;
            }
        final int startLine = df.fiveMinDemand_Line;
        for (int i = startLine; i < buff.size(); i++) {
            String basedata = buff.get(i);
            String[] baseDataArray = basedata.split(",");
            if (baseDataArray.length < 3)
                break;
            String diffStr;
            if (df.hourlyDemand_Line != 0)
                diffStr = "-1048576"; // 最終フィールドが予測(東電)ならば前日比を表示させないため-1048576を送る
            else
                diffStr = baseDataArray[3]; // 九州電力に対する特例：5分ごとの予測でも前日比を表示できる
            String demandStr;
            if (baseDataArray.length >= 3)
                demandStr = baseDataArray[2];
            else
                demandStr = df.isHokkaido ? "0.0" : "0";
            FiveMinDemand hd;
            if (df.isHokkaido)
                hd = new FiveMinDemandH(baseDataArray[0], baseDataArray[1],
                        demandStr, diffStr);
            else
                hd = new FiveMinDemand(baseDataArray[0], baseDataArray[1],
                        demandStr, diffStr);
            ret.add(hd);
        }
        return ret;
    }

    public String getDateText() {
        String baseData = buff.get(0);
        String[] baseDataArray = baseData.split(" ");
        return baseDataArray[0];
    }

    /**
     * 読み込んだソーステキストをそのまま返します。
     *
     * @return 読み込んだテキストファイルのテキスト
     */
    public String getReadText() {
        try {
            Vector<String> readLines = readFromURL();
            String ret = new String();
            for (int i = 0; i < readLines.size(); i++) {
                ret += readLines.get(i) + "\n";
            }
            return ret;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
