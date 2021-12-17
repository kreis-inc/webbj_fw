package jp.bj_one.re.database;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.bj_one.re.PrintStatus;


@Repository
public interface ManagementRepository extends JpaRepository<ManagementEntity, Long> {
	/**
	 * 
	 */
	List<ManagementEntity> findByPostDateTimeAfter(LocalDateTime datetime);
	List<ManagementEntity> findByIdIn(Long[] id);
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	boolean existsById(long value);
	
	@Query("SELECT m.status FROM ManagementEntity m WHERE m.id IN :id")
	PrintStatus[] getSratus(@Param("id") Long[] id);

	@Query("SELECT m.id FROM ManagementEntity m WHERE m.postGroupId IN :postGroupId")
	Long[] getGroupReports(@Param("postGroupId") Long postGroupId);

    @Modifying
    @Query("UPDATE ManagementEntity m "
    		+ "SET "
            + "  m.postGroupName = :postGroupName,"
            + "  m.postGroupMessage = :postGroupMessage "
            + "WHERE m.postGroupId = :postGroupId")
    Integer updatePostGroup(
    		@Param("postGroupId") Long postGroupId,
    		@Param("postGroupName") String postGroupName,
    		@Param("postGroupMessage") String postGroupMessage
    );
}
