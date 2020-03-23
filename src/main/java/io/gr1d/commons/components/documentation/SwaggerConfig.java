package io.gr1d.commons.components.documentation;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import io.gr1d.commons.components.documentation.config.SwaggerInfo;
import io.gr1d.commons.components.documentation.config.SwaggerSecuritySchema;
import io.gr1d.commons.components.documentation.enums.SecurityType;
import io.gr1d.commons.components.errorHandler.Gr1dErrorController;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@Slf4j
@Profile({"dev"})
public class SwaggerConfig {

	public static final String ROUTE_WEBJARS = "/webjars/**";
	public static final String ROUTE_SWAGGER_RESOURCES = "/swagger-resources/**";
	public static final String ROUTE_SWAGER_DOCS = "/v2/api-docs**";
	
	@Autowired
	private SwaggerInfo info;	
	
	@Autowired
	private SwaggerSecuritySchema security;
	/**
	 * Configures the Servlet Bean do Swagger
	 * @return {@link Docket}
	 */
	@Bean
	public Docket createSwaggerApi() {
		if (log.isTraceEnabled()) {
			log.trace("function=createSwaggerApi status=init");
		}
		Docket docket = new Docket(DocumentationType.SWAGGER_2)  
				.select()                                  
				.apis(getAPIs())              
				.paths(PathSelectors.any())
				.build()
				.apiInfo(info.getApiInfo())
				.useDefaultResponseMessages(false);

		if (!SecurityType.NONE.equals(security.getSecurityType())) {
			docket.securitySchemes(Collections.singletonList(security.getSecuritySchema()));
		}
		if (log.isTraceEnabled()) {
			log.trace("function=createSwaggerApi status=done");
		}
		return docket;
	}

	/**
	 * Ignore Spring Error Controller
	 * @return {@link Predicate}
	 */
	private Predicate<RequestHandler> getAPIs(){
		if (log.isTraceEnabled()) {
			log.trace("function=getAPIs status=init");
		}
		String pack = Gr1dErrorController.class.getPackage().toString().split(" ")[1];
		Predicate<RequestHandler> predicate = Predicates.not(RequestHandlerSelectors.basePackage(pack)); 
		if (log.isTraceEnabled()) {
			log.trace("function=getAPIs status=done");
		}
		return predicate;
	}
}
