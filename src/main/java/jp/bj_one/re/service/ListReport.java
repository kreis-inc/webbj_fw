package jp.bj_one.re.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.bj_one.re.ReportId;
import jp.bj_one.re.ReportStatus;
import jp.bj_one.re.database.ManagementEntity;
import jp.bj_one.re.database.ManagementRepository;

@Service
class ListReport {
	@Autowired
	private ManagementRepository repository;

	List<ReportStatus> listReportByPostAfter(LocalDateTime start) {
		List<ReportStatus> result = new ArrayList<ReportStatus>();
		for (ManagementEntity record : repository.findByPostDateTimeAfter(start)) {
			result.add(new ReportStatus(record));
		}
		return result;
	}
}
