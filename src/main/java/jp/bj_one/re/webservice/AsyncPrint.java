package jp.bj_one.re.webservice;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jp.bj_one.re.PrintStatus;
import jp.bj_one.re.ReportId;
import jp.bj_one.re.database.ManagementEntity;
import jp.bj_one.re.database.ManagementRepository;
import jp.bj_one.re.webservice.data.ReportFile;

@Async
@Component
public class AsyncPrint {
	@Autowired
	ManagementRepository repository;

	@Autowired
	ReportFile reportFile;

	public void print(jp.bj_one.re.Report target, ManagementEntity entity) {
		entity.setStatus(PrintStatus.RUNNING);
		entity.setPrintDateTime(LocalDateTime.now());
		repository.save(entity);
		repository.flush();

		ReportId reportId = new ReportId(entity.getId());
		target.info.setId(reportId);
		target.info.setReportName(entity.getReportName());
		target.info.setReportSubName(entity.getReportSubName());
		target.info.setReportMessage(entity.getReportMessage());
		target.info.setPostGroupId(entity.getPostGroupId());
		target.info.setPostGroupName(entity.getPostGroupName());
		target.info.setPostGroupMessage(entity.getPostGroupMessage());
		target.info.setInternalNumber(entity.getInternalNumber());
		target.info.setSalesOrderNo(entity.getSalesOrderNo());
		target.info.setSuccess(false);

		boolean postError = true;
		try {
			target.print(reportFile.getPath(reportId));
			postError = false;
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (postError) {
			entity.setStatus(PrintStatus.POST_ERROR);
		} else {
			if (target.info.getReportName().isEmpty())
				entity.setReportName(target.getClass().getSimpleName());
			else
				entity.setReportName(target.info.getReportName());
			entity.setReportSubName(target.info.getReportSubName());
			entity.setReportMessage(target.info.getReportMessage());
			entity.setInternalNumber(target.info.getInternalNumber());
			entity.setSalesOrderNo(target.info.getSalesOrderNo());
			if (target.info.isSuccess())
				entity.setStatus(PrintStatus.COMPLETE);
			else
				entity.setStatus(PrintStatus.FAILURE);
			if (target.info.getFilename() != null && !target.info.getFilename().isEmpty()) {
				boolean fileExists;
				long fileSize = 0;
				try {
					if (fileExists = Files.exists(reportFile.getPath(reportId), LinkOption.NOFOLLOW_LINKS))
						fileSize = Files.size(reportFile.getPath(reportId));
				} catch (IOException e) {
					fileExists = false;
				}
				if (fileExists) {
					entity.setFilename(target.info.getFilename() != null ? target.info.getFilename() : "");
					entity.setFilesize(fileSize);
				} else
					entity.setFilename("");
			} else
				entity.setFilename("");
		}
		entity.setEndedDateTime(LocalDateTime.now());

		// ファイル名にグループIDを付与
		try {
			String sal = entity.getSalesOrderNo();
			if (sal != null && !"".equals(sal) && "YAZAKI".equals(sal)) {
				// 何もしません。
			} else {
				entity.setFilename(entity.getPostGroupId() + "_" + entity.getFilename());
			}
		} catch (Exception e) {
			entity.setFilename(entity.getPostGroupId() + "_" + entity.getFilename());
		}
		try {
			repository.save(entity);
			repository.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!entity.getPostGroupName().equals(target.info.getPostGroupName())
				|| !entity.getPostGroupMessage().equals(target.info.getPostGroupMessage())) {
			try {
				repository.updatePostGroup(entity.getPostGroupId(), target.info.getPostGroupName(),
						target.info.getPostGroupMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
