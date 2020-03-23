package io.gr1d.commons.components.documentation.config;

import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import io.gr1d.commons.components.documentation.enums.SecurityType;
import lombok.Getter;
import lombok.Setter;
import springfox.documentation.service.SecurityScheme;

@Component
@ConfigurationProperties(prefix="swagger.security")
@Getter @Setter
public class SwaggerSecuritySchema {

	private SecurityType securityType;
	
	private SchemaOauth2 oauth2;

	private SchemaBasic basic;
	
	public SecurityScheme getSecuritySchema() {
		SecurityScheme securityScheme = null;
		securityType = Optional.ofNullable(securityType).orElse(SecurityType.NONE);
		switch(securityType) {
			case BASIC:
				securityScheme = basic.getSecurityScheme();
				break;
			case OAUTH2:
				securityScheme = oauth2.getSecurityScheme();
				break;
			default:
				break;
		}
		return securityScheme;
	}
}
