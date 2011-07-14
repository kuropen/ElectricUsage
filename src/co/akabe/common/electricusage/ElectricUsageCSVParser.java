package co.akabe.common.electricusage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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
		new SupplyDataFormat("http://www.kepco.co.jp/yamasou/juyo_kansai.csv", 5, 2, 11);
	
	public static final SupplyDataFormat Format_Chubu = 
		new SupplyDataFormat("http://denki-yoho.chuden.jp/denki_yoho_content_data/juyo_cepco.csv", 5, 2, 8);
	
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
	public PeakElectricity getPeakDemand () {
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
		PeakElectricity ret = new PeakElectricity(PeakElectricityType.DEMAND, baseDataArray[1], baseDataArray[0]);
		return ret;
	}
	
	/**
	 * ピーク時の予想最大電力供給を得る。
	 * @return ピーク時の最大電力供給
	 */
	public PeakElectricity getPeakSupply () {
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
		PeakElectricity ret = new PeakElectricity(PeakElectricityType.SUPPLY, baseDataArray[1], baseDataArray[0]);
		return ret;
	}
	
	/**
	 * 時間ごとの需要実績データを得る。
	 * @return 時間ごとの需要実績
	 */
	public Vector<HourlyDemand> getHourlyDemand () {
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
			HourlyDemand hd = new HourlyDemand(baseDataArray[0], baseDataArray[1], 
					baseDataArray[2], diffStr);
			ret.add(hd);
		}
		return ret;
	}
	
	/**
	 * 5分ごとの需要実績データを得る。
	 * @return 時間ごとの需要実績
	 */
	public Vector<HourlyDemand> get5MinDemand () {
		if (!df.isNewFormat) return null;
		Vector<HourlyDemand> ret = new Vector<HourlyDemand>();
		if (buff == null)
			try {
				buff = readFromURL();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		final int startLine = df.fiveMinDemand_Line;
		for (int i = startLine; i < startLine + 287; i++) {
			String basedata = buff.get(i);
			String[] baseDataArray = basedata.split(",");
			String diffStr = "-1048576"; //最終フィールドが予測(東電)ならば前日比を表示させないため-1048576を送る
			String demandStr;
			if (baseDataArray.length == 3)
				demandStr = baseDataArray[2];
			else
				demandStr = "0";
			HourlyDemand hd = new HourlyDemand(baseDataArray[0], baseDataArray[1], 
					demandStr, diffStr, true);
			ret.add(hd);
		}
		return ret;
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
		PeakElectricity pe = p.getPeakSupply();
		System.out.println(pe.toString());
		System.out.println(HourlyDemand.seekNearestHistory(p.get5MinDemand()).toStringWithDiffandPercentage(pe));
		
		System.out.println("\n東京電力管内");	
		ElectricUsageCSVParser pt = new ElectricUsageCSVParser(Format_Tokyo);
		//System.out.println(pt.getReadText());
		System.out.println(pt.getPeakDemand().toString());
		PeakElectricity pet = pt.getPeakSupply();
		System.out.println(pet.toString());
		System.out.println(HourlyDemand.seekNearestHistory(pt.get5MinDemand()).toStringWithDiffandPercentage(pet));
		
		System.out.println("\n関西電力管内");	
		ElectricUsageCSVParser pk = new ElectricUsageCSVParser(Format_Kansai);
		//System.out.println(pt.getReadText());
		System.out.println(pk.getPeakDemand().toString());
		PeakElectricity pek = pk.getPeakSupply();
		System.out.println(pek.toString());
		System.out.println(HourlyDemand.seekNearestHistory(pk.getHourlyDemand()).toStringWithDiffandPercentage(pek));
		
		System.out.println("\n中部電力管内");	
		ElectricUsageCSVParser pc = new ElectricUsageCSVParser(Format_Chubu);
		//System.out.println(pt.getReadText());
		System.out.println(pc.getPeakDemand().toString());
		PeakElectricity pec = pc.getPeakSupply();
		System.out.println(pec.toString());
		System.out.println(HourlyDemand.seekNearestHistory(pc.getHourlyDemand()).toStringWithDiffandPercentage(pec));
	}
	
}
