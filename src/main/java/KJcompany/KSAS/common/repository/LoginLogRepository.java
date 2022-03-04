package KJcompany.KSAS.common.repository;

import KJcompany.KSAS.common.domain.LoginLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginLogRepository extends JpaRepository<LoginLog, Long> {

}
