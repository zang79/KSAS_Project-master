package KJcompany.KSAS.common.repository;

import KJcompany.KSAS.common.domain.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {

}
