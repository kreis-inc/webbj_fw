package jp.bj_one.fw.dto;

import jp.bj_one.fw.entity.EntityInterface;

public class CrudParamDto {

	private EntityInterface viewEntity;

	/**
	 * viewEntityを取得.
	 *
	 * @return viewEntity
	 */
	public EntityInterface getViewEntity() {
		return viewEntity;
	}

	/**
	 * viewEntityを設定.
	 *
	 * @param viewEntity viewEntity
	 */
	public void setViewEntity(EntityInterface viewEntity) {
		this.viewEntity = viewEntity;
	}
}
