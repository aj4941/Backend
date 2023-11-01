package swm_nm.morandi.domain.testInfo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import swm_nm.morandi.domain.testInfo.entity.Tests;


public interface TestStatusRepository {
    Page<Tests> findAllTestRecordByCondition(String testTypename, Long bojProblemId, String bojId, Pageable pageable);
}
