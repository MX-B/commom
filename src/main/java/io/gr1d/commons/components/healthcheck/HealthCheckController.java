package io.gr1d.commons.components.healthcheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.gr1d.commons.components.errorHandler.response.Gr1dError;
import io.gr1d.commons.components.healthcheck.response.HealthCheckResponse;
import io.gr1d.commons.components.healthcheck.response.enums.ServiceStatus;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class HealthCheckController {

	public static final String ROUTE_BASIC = "/hc"; 
	public static final String ROUTE_COMPLETE = ROUTE_BASIC + "/complete"; 
	
	@Autowired
	private HealthcheckService service;
	
	@GetMapping(ROUTE_BASIC)
	@ApiOperation(value = "Health Check Service", notes = "Check all system's services and responds if ok or not")
    @ApiResponses({
        @ApiResponse(code = 200, message = "All Services working fine"),
        @ApiResponse(code = 503, message = "Any Service is Down")
    })
	public ResponseEntity<ServiceStatus> simpleHealthCheck() throws Exception{
		if (log.isTraceEnabled()) {
			log.trace("function=simpleHealthCheck status=init");
		}
		HealthCheckResponse res = service.check();
		HttpStatus status = getStatus(res);
		if (log.isTraceEnabled()) {
			log.trace("function=simpleHealthCheck status=done");
		}
		return new ResponseEntity<>(res.getStatus(), status);
	}

	@GetMapping(ROUTE_COMPLETE)
	@ApiOperation(value = "Health Check Service", notes = "Check all system's services and responds a detailed info")
    @ApiResponses({
        @ApiResponse(code = 200, message = "All Services working fine", response = HealthCheckResponse.class),
        @ApiResponse(code = 401, message = "Access unauthorized", response = Gr1dError[].class),
        @ApiResponse(code = 403, message = "Access forbidden", response = Gr1dError[].class),
        @ApiResponse(code = 503, message = "Any Service is Down", response = HealthCheckResponse.class)
    })
	public ResponseEntity<HealthCheckResponse> completeHealthCheck() throws Exception{
		if (log.isTraceEnabled()) {
			log.trace("function=completeHealthCheck status=init");
		}
		HealthCheckResponse res = service.check();
		HttpStatus status = getStatus(res);
		if (log.isTraceEnabled()) {
			log.trace("function=completeHealthCheck status=done");
		}
		return new ResponseEntity<>(res, status);
	}
	
	private HttpStatus getStatus(HealthCheckResponse res) {
		return ServiceStatus.LIVE.equals(res.getStatus()) ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
	}
}
