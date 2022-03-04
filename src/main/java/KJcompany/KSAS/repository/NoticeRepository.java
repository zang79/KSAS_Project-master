package KJcompany.KSAS.repository;

import KJcompany.KSAS.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
	
}
