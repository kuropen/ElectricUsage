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
	 */
	public static final String UsageDataURL_Tokyo =
		"http://www.tepco.co.jp/forecast/html/images/juyo-j.csv";
	
	/**
	 * 東北電力管内のデータURL
	 */
	public static final String UsageDataURL_Tohoku = 
		"http://setsuden.tohoku-epco.co.jp/common/demand/juyo_tohoku.csv";
	
	private String usageDataURL;
	private String dataCharset;
	private Vector<String> buff;
	
	/**
	 * コンストラクタ (キャラクタセット省略型)<br>
	 * データのCharsetはShift_JISであるとみなします。
	 * @param URL ソースURL ({@link #UsageDataURL_Tokyo}, {@link #UsageDataURL_Tohoku}の利用を推奨)
	 */
	public ElectricUsageCSVParser (String URL) {
		this(URL, "Shift_JIS");
	}
	
	/**
	 * コンストラクタ<br>
	 * 現在Shift_JIS以外の文字コードでのデータを公表している会社がないので、このコンストラクタはあまり使用されない。
	 * @param URL ソースURL
	 * @param charset 文字コード
	 */
	public ElectricUsageCSVParser (String URL, String charset) {
		usageDataURL = URL;
		dataCharset = charset;
		buff = null;
	}
	
	/**
	 * URLからテキストを読み込む
	 * @return URL上にあるテキスト。エラー発生時はnull
	 * @throws IOException 通信エラーまたは入力ストリームでエラーが発生した場合にスローします
	 * @since 1.0
	 */
	private Vector<String> readFromURL() throws IOException{
		URL url = new URL(usageDataURL);
		URLConnection connection = url.openConnection();
		connection.setDoInput(true);
		InputStream inStream = connection.getInputStream();
		
		try {
			BufferedReader input =
					new BufferedReader(new InputStreamReader(inStream, dataCharset));
			
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
		String baseData = buff.get(5); //上から6行目に明記されている
		String[] baseDataArray = baseData.split(",");
		PeakElectricity ret = new PeakElectricity(PeakElectricityType.DEMAND, baseDataArray[1], baseDataArray[0]);
		return ret;
	}
	
	/**
	 * ピーク時の予想最大電力供給を得る。
	 * @return ピーク時の最大電力供給
	 * @throws IOException 
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
		String baseData = buff.get(2); //上から3行目に明記されている
		String[] baseDataArray = baseData.split(",");
		PeakElectricity ret = new PeakElectricity(PeakElectricityType.SUPPLY, baseDataArray[1], baseDataArray[0]);
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
		ElectricUsageCSVParser p = new ElectricUsageCSVParser(UsageDataURL_Tohoku);
		System.out.println(p.getReadText());
		System.out.println(p.getPeakDemand().toString());
		System.out.println(p.getPeakSupply().toString());
		
		ElectricUsageCSVParser pt = new ElectricUsageCSVParser(UsageDataURL_Tokyo);
		System.out.println(pt.getPeakDemand().toString());
		System.out.println(pt.getPeakSupply().toString());
	}
	
}
