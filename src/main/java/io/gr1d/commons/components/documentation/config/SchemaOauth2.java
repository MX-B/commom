package io.gr1d.commons.components.documentation.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.util.Assert;

import lombok.Getter;
import lombok.Setter;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.service.AuthorizationCodeGrant;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;

@Getter @Setter
public class SchemaOauth2 implements SchemaInterface {
	
	private String name;
	
	private String clientId;
	
	private String clientSecret;
	
	private String authorizeEndpointUrl;
	
	private String tokenEndpointUrl; 
	
	@Override
	public SecurityScheme getSecurityScheme() {
		validate();
		TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpoint(authorizeEndpointUrl, clientId, clientSecret);
        TokenEndpoint tokenEndpoint = new TokenEndpoint(tokenEndpointUrl, "token");
        List<GrantType> grantTypes = Arrays.asList(new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint));
		return new OAuthBuilder()
			.name(name)
			.grantTypes(grantTypes)
			.build();
	}
	
	private void validate() {
		Assert.notNull(name, "Property 'swagger.security.oauth2.name' cannot be null");
		Assert.isTrue(!name.isEmpty(), "Property 'swagger.security.basic.name' cannot be empty");

		Assert.notNull(clientId, "Property 'swagger.security.oauth2.clientId' cannot be null");
		Assert.isTrue(!clientId.isEmpty(), "Property 'swagger.security.basic.clientId' cannot be empty");
		
		Assert.notNull(clientSecret, "Property 'swagger.security.oauth2.clientSecret' cannot be null");
		Assert.isTrue(!clientSecret.isEmpty(), "Property 'swagger.security.oauth2.clientSecret' cannot be empty");
		
		Assert.notNull(authorizeEndpointUrl, "Property 'swagger.security.oauth2.authorizeEndpointUrl' cannot be null");
		Assert.isTrue(!authorizeEndpointUrl.isEmpty(), "Property 'swagger.oauth2.authorizeEndpointUrl.name' cannot be empty");
		
		Assert.notNull(tokenEndpointUrl, "Property 'swagger.security.oauth2.tokenEndpointUrl' cannot be null");
		Assert.isTrue(!tokenEndpointUrl.isEmpty(), "Property 'swagger.security.oauth2.tokenEndpointUrl' cannot be empty");
	}
}