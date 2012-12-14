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
 * 電気事業者による電力需給CSVデータをパースするクラス。<br>
 * 東京電力・東北電力により公開されている電力需給状況のCSVファイルを解釈します。
 * @author Eternie (Hirochika Yuda), eternie@eternie-labs.net
 * @version 1.0
 * 
 */
public class ElectricUsageCSVParser {

	/**
	 * 東京電力管内のデータURL
	 * @deprecated Use {@link #Format_Tokyo} instead. NOW THIS VARIABLE DOES NOT WORK WELL.
	 */
	public static final String UsageDataURL_Tokyo =
		"http://www.tepco.co.jp/forecast/html/images/juyo-j.csv";
	
	/**
	 * 東北電力管内のデータURL
	 * @deprecated Use {@link #Format_Tohoku} instead
	 */
	public static final String UsageDataURL_Tohoku = 
		"http://setsuden.tohoku-epco.co.jp/common/demand/juyo_tohoku.csv";
	
	/**
	 * 東北電力のデータフォーマット定義
	 */
	public static final SupplyDataFormat Format_Tohoku =
		new SupplyDataFormat("http://setsuden.tohoku-epco.co.jp/common/demand/juyo_tohoku.csv", 5, 2, 8, 44);
	
	/**
	 * 東京電力のデータフォーマット定義
	 */
	public static final SupplyDataFormat Format_Tokyo = 
		new SupplyDataFormat("http://www.tepco.co.jp/forecast/html/images/juyo-j.csv", 5, 2, 8, 44);
	
	/**
	 * 関西電力のデータフォーマット定義
	 */
	public static final SupplyDataFormat Format_Kansai = 
		new SupplyDataFormat("http://www.kepco.co.jp/yamasou/juyo1_kansai.csv", 5, 2, 11, 49);
	
	/**
	 * 中部電力のデータフォーマット定義
	 */
	public static final SupplyDataFormat Format_Chubu = 
		new SupplyDataFormat("http://denki-yoho.chuden.jp/denki_yoho_content_data/juyo_cepco003.csv", 5, 2, 24, 76);
	
	/**
	 * 九州電力のデータフォーマット定義
	 * @deprecated 日付をまたぐとデータが取得できなくなってしまうため、 {@link #buildKyushuFormat()}を使うこと。
	 */
	@Deprecated
	public static final SupplyDataFormat Format_Kyushu = 
		new SupplyDataFormat("http://www.kyuden.co.jp/power_usages/csv/juyo-hourly-"+new SimpleDateFormat("yyyyMMdd").format(new Date())+".csv", 5, 2, 8, 44);
	
	/**
	 * 中国電力のデータフォーマット定義
	 */
	public static final SupplyDataFormat Format_Chugoku =
			new SupplyDataFormat("http://www.energia.co.jp/jukyuu/sys/juyo-j.csv", 5, 2, 8, 44);
	
	/**
	 * 北海道電力のデータフォーマット定義
	 */
	public static final SupplyDataFormat Format_Hokkaido = 
			new SupplyDataFormat("http://denkiyoho.hepco.co.jp/data/juyo_hokkaidou.csv", 5, 2, 11, 44);
	static {
		Format_Hokkaido.setAsHokkaido();
	}
	
	/**
	 * 四国電力のデータフォーマット定義
	 */
	public static final SupplyDataFormat Format_Shikoku = 
			new SupplyDataFormat("http://www.yonden.co.jp/denkiyoho/juyo_yonden.csv", 5, 2, 8, 40);
	
	/**
	 * 北陸電力のデータフォーマット定義
	 */
	public static final SupplyDataFormat Format_Hokuriku =
			new SupplyDataFormat("http://www.setsuden-rikuden.jp/csv/juyo-rikuden.csv", 5, 2, 8, 44);
	
	/**
	 * 九州電力のデータフォーマット定義を作成する
	 * @return 九州電力のデータフォーマット定義
	 */
	public static final SupplyDataFormat buildKyushuFormat () {
		return new SupplyDataFormat("http://www.kyuden.co.jp/power_usages/csv/juyo-hourly-"+new SimpleDateFormat("yyyyMMdd").format(new Date())+".csv", 5, 2, 8, 44);
	}
	
	private SupplyDataFormat df;	
	private Vector<String> buff;
	
	
	public ElectricUsageCSVParser (SupplyDataFormat f) {
		df = f;
	}
	
	/**
	 * コンストラクタ (キャラクタセット省略型)<br>
	 * データのCharsetはShift_JISであるとみなします。
	 * @param URL ソースURL ({@link #UsageDataURL_Tokyo}, {@link #UsageDataURL_Tohoku}の利用を推奨)
	 */
	public ElectricUsageCSVParser (String URL) {
		//this(URL, "Shift_JIS");
		df = new SupplyDataFormat (URL, 2, 5, 8);
		buff = null;
	}
	
	/**
	 * コンストラクタ<br>
	 * 現在Shift_JIS以外の文字コードでのデータを公表している会社がないので、このコンストラクタはあまり使用されない。
	 * @param URL ソースURL
	 * @param charset 文字コード
	 */
	public ElectricUsageCSVParser (String URL, String charset) {
		//usageDataURL = URL;
		df = new SupplyDataFormat (URL, 2, 5, 8, charset);
		buff = null;
	}
	
	/**
	 * URLからテキストを読み込む
	 * @return URL上にあるテキスト。エラー発生時はnull
	 * @throws IOException 通信エラーまたは入力ストリームでエラーが発生した場合にスローします
	 * @since 1.0
	 */
	private Vector<String> readFromURL() throws IOException{
		URL url = new URL(df.dataURL);
		URLConnection connection = url.openConnection();
		connection.setDoInput(true);
		InputStream inStream = connection.getInputStream();
		
		try {
			BufferedReader input =
					new BufferedReader(new InputStreamReader(inStream, df.charset));
			
			String line = "";
			Vector<String> ret = new Vector<String>();
			while ((line = input.readLine()) != null)
				ret.add(line);
			return ret;
		}finally{
			inStream.close();
		}
	}
	
	/**
	 * ピーク時の予想最大電力（需要）を得る。
	 * @return ピーク時の予想最大電力
	 * @throws IOException 
	 */
	public PeakDemand getPeakDemand () {
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
		if(df.isHokkaido)
			ret = new PeakDemandH(baseDataArray[1], baseDataArray[0]);
		else
			ret = new PeakDemand(baseDataArray[1], baseDataArray[0]);
		return ret;
	}
	
	/**
	 * ピーク時の予想最大電力供給を得る。
	 * @return ピーク時の最大電力供給
	 */
	public PeakSupply getPeakSupply () {
		if (buff == null)
			try {
				buff = readFromURL();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		String baseData = buff.get(df.peakSupply_Line); //上から3行目に明記されている
		String[] baseDataArray = baseData.split(",");
		PeakSupply ret;
		if(df.isHokkaido)
			ret = new PeakSupplyH(baseDataArray[1], baseDataArray[0]);
		else
			ret = new PeakSupply(baseDataArray[1], baseDataArray[0]);
		return ret;
	}
	
	/**
	 * 時間ごとの需要実績データを得る。
	 * @return 時間ごとの需要実績
	 */
	public Vector<HourlyDemand> getHourlyDemand () {
		//九州電力に対する特例：九電は5分ごとのデータしか公開していない
		if (df.hourlyDemand_Line == 0) {
			return null;
		}
		
		Vector<HourlyDemand> ret = new Vector<HourlyDemand>();
		if (buff == null)
			try {
				buff = readFromURL();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		final int startLine = df.hourlyDemand_Line;
		for (int i = startLine; i < startLine + 24; i++) {
			String basedata = buff.get(i);
			String[] baseDataArray = basedata.split(",");
			String diffStr = baseDataArray[3];
			if (df.isNewFormat) diffStr = "-1048576"; //最終フィールドが予測(東電)ならば前日比を表示させないため-1048576を送る
			HourlyDemand hd;
			if(df.isHokkaido)
				hd = new HourlyDemandH(baseDataArray[0], baseDataArray[1], baseDataArray[2], diffStr);
			else
				hd = new HourlyDemand(baseDataArray[0], baseDataArray[1], baseDataArray[2], diffStr);
			ret.add(hd);
		}
		return ret;
	}
	
	/**
	 * 5分ごとの需要実績データを得る。
	 * @return 時間ごとの需要実績
	 */
	public Vector<FiveMinDemand> get5MinDemand () {
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
			if(baseDataArray.length < 3) break;
			String diffStr;
			if (df.hourlyDemand_Line != 0)
				diffStr = "-1048576"; //最終フィールドが予測(東電)ならば前日比を表示させないため-1048576を送る
			else
				diffStr = baseDataArray[3]; //九州電力に対する特例：5分ごとの予測でも前日比を表示できる
			String demandStr;
			if (baseDataArray.length >= 3)
				demandStr = baseDataArray[2];
			else
				demandStr = df.isHokkaido ? "0.0" : "0";
			FiveMinDemand hd;
			if(df.isHokkaido)
				hd = new FiveMinDemandH(baseDataArray[0], baseDataArray[1], demandStr, diffStr);
			else
				hd = new FiveMinDemand(baseDataArray[0], baseDataArray[1], demandStr, diffStr);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main (String[] args) {
		ElectricUsageCSVParser p = new ElectricUsageCSVParser(Format_Tohoku);
		//System.out.println(p.getReadText());
		System.out.println("東北電力管内");
		System.out.println(p.getPeakDemand().toString());
		PeakSupply pe = p.getPeakSupply();
		System.out.println(pe.toString());
		System.out.println(HourlyDemand.seekNearestHistory(p.get5MinDemand()).toStringWithPercentage(pe));
		System.out.println(HourlyDemand.seekNearestHistory(p.get5MinDemand()).getDate());
		System.out.println(new SimpleDateFormat("yyyy/MM/dd").format(new Date()));
		
		System.out.println("\n東京電力管内");	
		ElectricUsageCSVParser pt = new ElectricUsageCSVParser(Format_Tokyo);
		//System.out.println(pt.getReadText());
		System.out.println(pt.getPeakDemand().toString());
		PeakSupply pet = pt.getPeakSupply();
		System.out.println(pet.toString());
		System.out.println(HourlyDemand.seekNearestHistory(pt.get5MinDemand()).toStringWithPercentage(pet));
		System.out.println(pt.getDateText());
		
		System.out.println("\n関西電力管内");	
		ElectricUsageCSVParser pk = new ElectricUsageCSVParser(Format_Kansai);
		//System.out.println(pt.getReadText());
		System.out.println(pk.getPeakDemand().toString());
		PeakSupply pek = pk.getPeakSupply();
		System.out.println(pek.toString());
		System.out.println(HourlyDemand.seekNearestHistory(pk.get5MinDemand()).toStringWithPercentage(pek));
		System.out.println(pk.getDateText());
		
		System.out.println("\n中部電力管内");	
		ElectricUsageCSVParser pc = new ElectricUsageCSVParser(Format_Chubu);
		//System.out.println(pt.getReadText());
		System.out.println(pc.getPeakDemand().toString());
		PeakSupply pec = pc.getPeakSupply();
		System.out.println(pec.toString());
		System.out.println(HourlyDemand.seekNearestHistory(pc.get5MinDemand()).toStringWithPercentage(pec));
		System.out.println(pc.getDateText());
		
		System.out.println("\n九州電力管内");	
		ElectricUsageCSVParser ps = new ElectricUsageCSVParser(Format_Kyushu);
		//System.out.println(pt.getReadText());
		System.out.println(ps.getPeakDemand().toString());
		PeakSupply pes = ps.getPeakSupply();
		System.out.println(pes.toString());
		System.out.println(HourlyDemand.seekNearestHistory(ps.get5MinDemand()).toStringWithPercentage(pes));
		System.out.println(ps.getDateText());
		
		System.out.println("\n中国電力管内");	
		ElectricUsageCSVParser pg = new ElectricUsageCSVParser(Format_Chugoku);
		//System.out.println(pt.getReadText());
		System.out.println(pg.getPeakDemand().toString());
		PeakSupply peg = pg.getPeakSupply();
		System.out.println(peg.toString());
		System.out.println(HourlyDemand.seekNearestHistory(pg.get5MinDemand()).toStringWithPercentage(peg));
		System.out.println(pg.getDateText());
		
		System.out.println("\n北海道電力管内");	
		ElectricUsageCSVParser ph = new ElectricUsageCSVParser(Format_Hokkaido);
		//System.out.println(pt.getReadText());
		System.out.println(ph.getPeakDemand().toString());
		PeakSupply peh = ph.getPeakSupply();
		System.out.println(peh.toString());
		System.out.println(HourlyDemand.seekNearestHistory(ph.get5MinDemand()).toStringWithPercentage(peh));
		System.out.println(ph.getDateText());
	}
	
}
