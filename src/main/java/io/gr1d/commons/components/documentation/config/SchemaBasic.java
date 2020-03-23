package io.gr1d.commons.components.documentation.config;

import org.springframework.util.Assert;

import lombok.Getter;
import lombok.Setter;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityScheme;

@Getter @Setter
public class SchemaBasic implements SchemaInterface {

	private String name;

	@Override
	public SecurityScheme getSecurityScheme() {
		Assert.notNull(name, "Property 'swagger.security.basic.name' cannot be null");
		Assert.isTrue(!name.isEmpty(), "Property 'swagger.security.basic.name' cannot be empty");
		return new BasicAuth(name);
	}
}
