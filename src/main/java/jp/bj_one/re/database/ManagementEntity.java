package jp.bj_one.re.database;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import jp.bj_one.re.PrintStatus;
import lombok.Data;

@Data
@Entity
@Table(name = "bj_report_engine_autocreate")
public class ManagementEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/** Report ID (Key). */
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	Long id;
	
	/** 実行ステータス.値は jp.bj_one.re.PrintStatus 参照 
	 * @see jp.bj_one.re.PrintStatus
	 */
	@Column(nullable = false, length = 32)
	@Enumerated(EnumType.STRING)
	PrintStatus status;
	
	/** 帳票名1.(OUTPUT_ID: "PO-01" 等) */
	@Column(nullable = false, length = 20)
	String reportName = "";
	
	/** 帳票名2.(OUTPUT_TITLE: "PO-01 標準発注書" 等) */
	@Column(nullable = false, length = 100)
	String reportSubName = "";
	
	/** 帳票メッセージ.用途は特定していない. */
	@Column(nullable = false, length = 100)
	String reportMessage = "";
	
	/** 帳票投入単位毎 ID.投入した最初の帳票 ID になる. */
	@Column(nullable = true)
	Long postGroupId;
	
	/** 帳票投入単位毎 名前. */
	@Column(nullable = false, length = 20)
	String postGroupName = "";
	
	/** 帳票投入単位毎 メッセージ.用途は特定していない. */
	@Column(nullable = false, length = 100)
	String postGroupMessage = "";
	
	/** 帳票ダウンロード時のファイル名. */
	@Column(nullable = false)
	String filename = "";
	
	/** 帳票ファイルサイズ. */
	@Column(nullable = true)
	Long filesize = 0L;
	
	/** 削除フラグ. */
	@Column(nullable = true)
	Boolean deleteFlg = false;
	
	/** 【MCPVC固有】内部番号. */
	@Column(name = "seq", nullable = true)
	Long internalNumber;
	
	/** 【MCPVC固有】受注番号. */
	@Column(name = "sales_order_no", nullable = true, length = 20)
	String salesOrderNo;
	
	/** 帳票投入日時. */
	@Column(nullable = false)
	LocalDateTime postDateTime;
	
	/** 帳票作成開始日時. */
	@Column(nullable = true)
	LocalDateTime printDateTime;
	
	/** 帳票作成完了（エラー終了含む）日時. */
	@Column(nullable = true)
	LocalDateTime endedDateTime;
	
	@Column(nullable = false)
	String userId;
	
	@Column(nullable = false)
	String bjSystemId;
	
	@Column(nullable = false)
	String loginUserName;
}
