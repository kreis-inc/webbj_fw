package jp.bj_one.fw.dto;

public class SearchCondition {

	/** 表示件数。*/
	private Integer perPage;

	/** ページ。*/
	private Integer page;

	/** 取得開始番号。*/
	private Integer start;

	/**
	 * perPageを取得.
	 *
	 * @return perPage
	 */
	public Integer getPerPage() {
		return perPage;
	}

	/**
	 * perPageを設定.
	 *
	 * @param perPage perPage
	 */
	public void setPerPage(Integer perPage) {
		this.perPage = perPage;
	}

	/**
	 * pageを取得.
	 *
	 * @return page
	 */
	public Integer getPage() {
		return page;
	}

	/**
	 * pageを設定.
	 *
	 * @param page page
	 */
	public void setPage(Integer page) {
		this.page = page;
	}

	/**
	 * startを取得.
	 *
	 * @return start
	 */
	public Integer getStart() {
		return start;
	}

	/**
	 * startを設定.
	 *
	 * @param start start
	 */
	public void setStart(Integer start) {
		this.start = start;
	}


}

