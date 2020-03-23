package io.gr1d.commons.components.healthcheck;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.gr1d.commons.components.healthcheck.response.HealthCheckResponse;
import io.gr1d.commons.components.healthcheck.response.ServiceInfo;
import io.gr1d.commons.components.healthcheck.response.enums.ServiceStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HealthcheckService {

	@Value("${app.name}")
	private String appName;

	@Value("${app.version}")
	private String appVersion;
	
	@Autowired(required = false)
	private Collection<CheckService> services;

	public HealthCheckResponse check() throws Exception{
		if (log.isTraceEnabled()) {
			log.trace("function=check status=init");
		}
		HealthCheckResponse res = new HealthCheckResponse(appName, appVersion, ServiceStatus.LIVE);
		Collection<ServiceInfo> serviceInfos = Optional.ofNullable(services)
			.map(Collection::stream)
			.orElseGet(Stream::empty)
			.map(service -> {
				ServiceInfo serviceInfo = service.check();
				if (ServiceStatus.DOWN.equals(serviceInfo.getStatus())){
					res.setStatus(ServiceStatus.DOWN);
				}
				return serviceInfo;
			})
			.collect(Collectors.toList());
		
		res.setServices(serviceInfos);
		for (ServiceInfo service : res.getServices()){
			if (ServiceStatus.DOWN.equals(service.getStatus())){
				res.setStatus(ServiceStatus.DOWN);
			}
		}
		if (log.isTraceEnabled()) {
			log.trace("function=check status=done");
		}
		return res;
	}
}
