package io.gr1d.commons.components.documentation.config;

import lombok.Getter;
import lombok.Setter;
import springfox.documentation.service.Contact;

@Getter @Setter
public class InfoContact {

	private String name;
	
	private String email;
	
	private String url;
	
	public Contact getSwaggerContact() {
		return new Contact(name, url, email);
	}
}