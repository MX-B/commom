package io.gr1d.commons.components.errorHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.gr1d.commons.components.errorHandler.response.Gr1dError;
import lombok.extern.slf4j.Slf4j;

/**
 * A base class for an API Controller
 *
 * @author Rafael Lins
 *
 */
@Slf4j
@RestControllerAdvice
public class BaseController {

	public static final String JSON = MediaType.APPLICATION_JSON_UTF8_VALUE;
	public static final String XML = MediaType.APPLICATION_XML_VALUE;
	
	@Autowired
	private MessageSource messageSource;
	
	/**
	 * Intercepts a {@link ConstraintViolationException} and returns the violations
	 * as {@link ApiError API Errors}
	 *
	 * @param exception
	 *            The {@link Exception}
	 *
	 * @see ConstraintViolationException#getConstraintViolations()
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Collection<Gr1dError>> handleException(final ConstraintViolationException exception, final Locale locale) {
		log.error("ConstraintViolationException while invoking Controller method", exception);

		final Collection<Gr1dError> errors = exception.getConstraintViolations().stream().map(Gr1dError::new)
				.collect(Collectors.toList());
		
		errors.forEach(err -> err.translateMessage(messageSource, locale));

		return ResponseEntity.unprocessableEntity().body(errors);
	}

	/**
	 * Intercepts a {@link MethodArgumentNotValidException} and returns an API
	 * Response with the errors
	 *
	 * @param exception
	 *            The thrown exception
	 *
	 * @see MethodArgumentNotValidException#getBindingResult()
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Collection<Gr1dError>> handleException(final MethodArgumentNotValidException exception, final Locale locale) {
		log.error("MethodArgumentNotValidException while invoking Controller method", exception);

		final Collection<Gr1dError> errors = exception.getBindingResult().getAllErrors().stream().map(Gr1dError::new)
				.collect(Collectors.toList());
		
		errors.forEach(err -> err.translateMessage(messageSource, locale));

		return ResponseEntity.unprocessableEntity().body(errors);
	}

	/**
	 * Intercepts a {@link Throwable} and returns an API Response with the errors
	 *
	 * @param exception
	 *            The thrown exception
	 */
	@ExceptionHandler(Throwable.class)
	public ResponseEntity<Collection<Gr1dError>> handleException(final Throwable exception, final Locale locale) {
		log.error("Uncaught exception", exception);

		final Collection<Gr1dError> errors = new ArrayList<>(1);
		errors.add(new Gr1dError(exception));
		
		errors.forEach(err -> err.translateMessage(messageSource, locale));

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
	}
}
