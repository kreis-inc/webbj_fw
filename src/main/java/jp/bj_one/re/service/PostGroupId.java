package jp.bj_one.re.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.bj_one.re.ReportId;
import jp.bj_one.re.database.ManagementRepository;

@Service
class PostGroupId {
	@Autowired
	private ManagementRepository repository;

	ReportId[] getPostGroupReports(ReportId postGroupId) {
		try {
			Long[] r = repository.getGroupReports(postGroupId.getValue());
			ReportId[] result = new ReportId[r.length];
			for (int i = 0; i < r.length; i++)
				result[i] = new ReportId(r[i]);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new ReportId[0]; 
		}
	}
	
	ReportId getPostGroup(ReportId[] reportIds) {
		for (ReportId reportId : reportIds) {
			if (reportId.isError())
				return reportId;
		}
		if (reportIds.length < 1)
			return null;
		return reportIds[0];
	}
}
