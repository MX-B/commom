package io.gr1d.commons.components.errorHandler.response;

import io.swagger.annotations.ApiModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

@ApiModel
public class Gr1dError {
	private static final Logger LOG = LoggerFactory.getLogger(Gr1dError.class);

	private String error;
	private String message;
	private String meta;
	private String trace;
	private LocalDateTime timestamp = LocalDateTime.now();

	public Gr1dError(final Map<String, Object> errorAttributes) {
		LOG.debug("Creating new Gr1dError from ErrorAtrributes (generic error). error={}, message={}", error, message);
		error = (String) errorAttributes.get("error");
		message = (String) errorAttributes.get("message");
		trace = (String) errorAttributes.get("trace");
	}

	public Gr1dError(final String error, final String message) {
		LOG.debug("Creating new Gr1dError. error={}, message={}", error, message);

		this.error = error;
		this.message = message;
	}

	public Gr1dError(final String error, final String message, final String meta) {
		LOG.debug("Creating new Gr1dError. error={}, message={}, meta={}", error, message, meta);

		this.error = error;
		this.message = message;
		this.meta = meta;
	}

	public Gr1dError(final Throwable throwable) {
		this(throwable.getClass().getSimpleName(), throwable.getLocalizedMessage());
		LOG.debug("Created new Gr1dError from Throwable");
	}

	public Gr1dError(final Throwable throwable, final String meta) {
		this(throwable.getClass().getSimpleName(), throwable.getLocalizedMessage(), meta);
		LOG.debug("Created new Gr1dError from Throwable + Meta");
	}

	public Gr1dError(final ConstraintViolation<?> constraintViolation) {
		this(constraintViolation.getClass().getSimpleName(), constraintViolation.getMessage(),
				constraintViolation.getPropertyPath().toString());
		LOG.debug("Created Gr1dError from ConstraintViolation");
	}

	public Gr1dError(final ObjectError objectError) {
		error = ObjectError.class.getSimpleName();
		message = objectError.getDefaultMessage();
		// meta = objectError.getCode();

		if (objectError.getArguments() != null && objectError.getArguments().length > 0) {
			final Object resolvable = objectError.getArguments()[0];

			if (resolvable instanceof DefaultMessageSourceResolvable) {
				meta = ((DefaultMessageSourceResolvable) resolvable).getDefaultMessage();
			} else {
				meta = null;
			}
		} else {
			meta = null;
		}

		LOG.debug("Creating new Gr1dError from ObjectError. error={}, message={}, meta={}", error, message, meta);
	}

	public String getError() {
		return error;
	}

	public void setError(final String error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public String getMeta() {
		return meta;
	}

	public void setMeta(final String meta) {
		this.meta = meta;
	}

	public String getTrace() {
		return trace;
	}

	public void setTrace(final String trace) {
		this.trace = trace;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public void translateMessage(final MessageSource messageSource, final Locale locale) {
		try {
			message = messageSource.getMessage(message, new Object[]{meta}, locale);
		} catch (final NoSuchMessageException nsme) {
			// Do nothing
		}
	}
}
