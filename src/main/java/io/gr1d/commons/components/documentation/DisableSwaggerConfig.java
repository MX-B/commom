package io.gr1d.commons.components.documentation;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Profile("!dev")
public class DisableSwaggerConfig {

	public static final String ROUTE = "swagger-ui.html";
	
	@RequestMapping(value = ROUTE, method = RequestMethod.GET)
    public void getSwagger(HttpServletResponse httpResponse) throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("function=getSwagger msg=[Attempt to found Swagger UI");
		}
        httpResponse.setStatus(HttpStatus.NOT_FOUND.value());
    }
}
