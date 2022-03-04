package KJcompany.KSAS.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("kjcompany.ksas")
public class ShopProperties {

	private String uploadPath;
	
}
