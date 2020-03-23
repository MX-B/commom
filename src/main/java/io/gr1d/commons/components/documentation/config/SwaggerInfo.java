package io.gr1d.commons.components.documentation.config;

import java.util.Collections;
import java.util.Optional;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.service.ApiInfo;

@Component
@ConfigurationProperties(prefix="swagger.info")
@Getter @Setter
@Slf4j
public class SwaggerInfo {
	
	private String title;
	
	private String description;
	
	private String version;
	
	private String termsOfServiceUrl;
	
	private InfoContact contact;
	
	private String license;
	
	private String licenseUrl;

	/**
	 * API Information
	 * @return @link ApiInfo
	 */
	public ApiInfo getApiInfo() {
		if (log.isTraceEnabled()) {
			log.trace("function=getApiInfo status=init");
		}
		ApiInfo apiInfo = new ApiInfo(
			title,
			description,
			version,
			termsOfServiceUrl,
			Optional.ofNullable(contact).orElse(new InfoContact()).getSwaggerContact(),
			license,
			licenseUrl,
			Collections.emptyList());
		if (log.isTraceEnabled()) {
			log.trace("function=getApiInfo status=done");
		}
		return apiInfo;
	}
}
