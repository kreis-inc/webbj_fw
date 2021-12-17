package jp.bj_one.re.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jp.bj_one.re.PrintStatus;
import jp.bj_one.re.ReportId;
import jp.bj_one.re.ReportStatus;
import jp.bj_one.re.database.ManagementEntity;
import jp.bj_one.re.database.ManagementRepository;

@Service
@Transactional(readOnly = true)
class GetStatus {
	@Autowired
	private ManagementRepository repository;
	
	@PersistenceContext
	private EntityManager em;

	ReportStatus getStatus(ReportId reportId) {
		if (reportId.isError()) {
			ReportStatus result = new ReportStatus();
			result.setId(reportId);
			result.setStatus(PrintStatus.POST_ERROR);
			return result;
		}
		ManagementEntity entity = repository.getOne(reportId.getValue());
		if (entity.getId() != null)
			return new ReportStatus(entity);
		else {
			ReportStatus result = new ReportStatus();
			result.setId(reportId);
			result.setStatus(PrintStatus.GET_STATUS_ERROR);
			return result;
		}
	}

	/**
	 * status を JPQL で取得して帳票作成中かどうか調べる.<br>
	 * getStatus と違い、キャッシュされないので context 内で
	 * 繰り返し調べてもちゃんと更新される.
	 * @param idList
	 * @return
	 */
	boolean isRunning(ReportId[] idList) {
		if (idList == null || idList.length == 0)
			return false;
		for (ReportId reportId : idList) {
			if (reportId.isError())
				return false;
		}
		
		Long[] values = new Long[idList.length];
		for (int i = 0; i < idList.length; i++)
			values[i] = idList[i].getValue();
		PrintStatus[] statusList = repository.getSratus(values);
		return PrintStatus.isRunning(statusList);
	}

	ReportStatus[] getStatus(ReportId[] idList) {
		for (ReportId reportId : idList) {
			if (reportId.isError()) {
				ReportStatus[] result = new ReportStatus[1];
				result[0].setId(reportId);
				result[0].setStatus(PrintStatus.POST_ERROR);
				return result;
			}
		}
		
		Long[] lList = new Long[idList.length];
		for (int i = 0; i < idList.length; i++)
			lList[i] = idList[i].getValue();
		repository.flush();
		List<ManagementEntity> entList = repository.findByIdIn(lList);
		ReportStatus[] result = new ReportStatus[entList.size()];
		for (int i = 0; i < entList.size(); i++) {
			result[i] = new ReportStatus(entList.get(i));
//			if (em != null)
//				em.refresh(entList.get(i));
		}
		return result;
	}
}
