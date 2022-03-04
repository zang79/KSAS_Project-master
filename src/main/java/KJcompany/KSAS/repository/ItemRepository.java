package KJcompany.KSAS.repository;

import KJcompany.KSAS.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
	
}
