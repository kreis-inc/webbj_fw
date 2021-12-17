package jp.bj_one.re;

import java.io.Serializable;
import java.nio.file.Path;

import org.apache.poi.ss.usermodel.Workbook;

import jp.bj_one.re.exembed.ExEmbed;
import jp.bj_one.re.exembed.ExEmbed.ExEmbedCall;
import lombok.Data;

/**
 * 帳票クラス.<br>
 * Report Engine の基本となるクラスで、当抽象クラスの実装で帳票作成に必要なデータ
 * （インスタンス変数）と帳票作成プログラム（print インスタンスメソッド）の両方を
 * 定義・作成する.
 * 
 * <ul>
 * <li>シリアライズ用に serialVersionUID は必須.</li>
 * <li>データには全て Getter、Setter が必要なので @Data（または @Getter と @Setter）が便利.</li>
 * <li>Spring Component 化禁止.内部使用の jackson での展開が失敗してデータが欠損する.</li>
 * </ul>
 */
@Data
public abstract class Report implements Serializable, ExEmbedCall {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 帳票エンジン向けのパラメタ集.
	 */
	public ReportInfo info = new ReportInfo();
	
	/**
	 * 帳票出力メソッド.帳票サーバーで当メソッドが呼び出される.
	 * @param filePath 作成したファイルは filePath で指定されたファイルに書き込むこと.
	 * @param result 当メソッドの終了情報を詰めるためのインスタンス.
	 */
	public void print(Path filePath) {
		// デフォルト動作。
		if (info.getTemplateFilename() == null || info.getTemplateFilename().isEmpty())
			info.setTemplateFilename(this.getClass().getSimpleName());
		try (ExEmbed exEmbed = ExEmbed.readTemplate(info.getTemplateFilename())) {
			exEmbed.doEmbed(this, filePath);
			info.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			info.setSuccess(false);
		}
	}
	
	/**
	 * ExEnbed 置換後、追加処理を行うためのコールバックルーチン.<br>
	 * 追加処理を行う場合は当関数を Override する。
	 * @param workbook
	 * @param result エラーなどによりダウンロードファイルを作成しない場合、false を返す。この場合、呼び元には Exception が返る。
	 */
	public boolean exEmbedPostProcess(Workbook workbook) {
		return true;
	}
}
