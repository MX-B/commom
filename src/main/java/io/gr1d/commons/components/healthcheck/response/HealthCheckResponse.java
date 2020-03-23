package io.gr1d.commons.components.healthcheck.response;

import java.util.Collection;
import java.util.LinkedList;

import io.gr1d.commons.components.healthcheck.response.enums.ServiceStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HealthCheckResponse {

	private String version;
	
	private String name;
	
	private ServiceStatus status;
	
	private Collection<ServiceInfo> services;

	public HealthCheckResponse(String name, String version, ServiceStatus status) {
		this.name = name;
		this.version = version;
		this.status = status;
		this.services = new LinkedList<ServiceInfo>();
	}
}
