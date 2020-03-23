package io.gr1d.commons.components.healthcheck;

import io.gr1d.commons.components.healthcheck.response.ServiceInfo;

public interface CheckService {

	public ServiceInfo check();
}
