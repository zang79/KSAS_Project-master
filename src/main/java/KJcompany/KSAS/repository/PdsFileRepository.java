package KJcompany.KSAS.repository;

import java.util.List;

import KJcompany.KSAS.domain.PdsFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PdsFileRepository extends JpaRepository<PdsFile, Long> {
	
	public List<PdsFile> findByFullName(String fullName);
	
}
