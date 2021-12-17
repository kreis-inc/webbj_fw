package jp.bj_one.re;

public enum PrintStatus {
	/** -4 システムエラー */
	SYSTEM_ERROR(-4, "システムエラー"),
	
	/** -3 POST時エラー */
	POST_ERROR(-3, "帳票投入時エラー"),
	
	/** -2 ステータス取得時エラー */
	GET_STATUS_ERROR(-2, "ステータス取得エラー"),
	
	/** -1 実行失敗 */
	FAILURE(-1, "実行失敗"),
	
	/** 0 実行待ち */
	AWAITING(0, "実行待ち"),
	
	/** 1 実行中 */
	RUNNING(1, "実行中"),
	
	/** 2 実行完了 */
	COMPLETE(2, "実行完了");
	
	private final int value;
	private final String msg;
	
	private PrintStatus(int v, String msg) {
		this.value = v;
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return this.msg;
	}
	
	/**
	 * 実行中のステータスかどうか.
	 * @return true:実行中 (not 実行完了)
	 */
	public boolean isRunning() {
		switch (this) {
		case GET_STATUS_ERROR:
		case AWAITING:
		case RUNNING:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean isRunning(PrintStatus[] statusList) {
		for (PrintStatus printStatus : statusList) {
			if (printStatus.isRunning())
				return true;
		}
		return false;
	}
	
	/**
	 * 実行完了したステータスかどうか.
	 * @return true:実行完了.（正常終了・エラー終了両方を含む）
	 */
	public boolean isEnded() {
		return !isRunning();
	}
	
	/**
	 * 正常終了したステータスかどうか.
	 * @return true:正常終了している.
	 */
	public boolean isComplete() {
		return this == COMPLETE;
	}
	
	/**
	 * エラー終了したステータスかどうか.
	 * @return true:エラー終了している
	 */
	public boolean isError() {
		return this.value < 0;
	}
}
