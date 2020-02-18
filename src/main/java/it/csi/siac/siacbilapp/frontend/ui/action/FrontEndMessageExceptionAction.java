/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import org.softwareforge.struts2.breadcrumb.Crumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.opensymphony.xwork2.ActionSupport;

import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;

/**
 * Action describing a page which received an exception.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 28/01/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class FrontEndMessageExceptionAction extends ActionSupport {
	
	/** For serialization purpose */
	private static final long serialVersionUID = -2477236258693465293L;
	
	private Exception exception;
	private String message;
	private String cssClassName;
	private Crumb previousCrumb;
	private Integer numeroPrecedentiAncore;
	
	/** Exception level to CSS class mapper */
	private static final Map<GenericFrontEndMessagesException.Level, String> LEVEL_TO_CSS_MAPPING;
	
	static {
		Map<GenericFrontEndMessagesException.Level, String> temp =
				new EnumMap<GenericFrontEndMessagesException.Level, String>(GenericFrontEndMessagesException.Level.class);
		temp.put(GenericFrontEndMessagesException.Level.ERROR, "error");
		temp.put(GenericFrontEndMessagesException.Level.WARNING, "warning");
		temp.put(GenericFrontEndMessagesException.Level.INFO, "success");
		// Populate the map
		LEVEL_TO_CSS_MAPPING = Collections.unmodifiableMap(temp);
	}
	
	@Override
	public String execute() throws Exception {
		GenericFrontEndMessagesException castException = getExceptionAsCorrectClass();
		setMessage(castException.getMessage());
		setCssClassName(LEVEL_TO_CSS_MAPPING.get(castException.getLevel()));
		return SUCCESS;
	}
	
	/**
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the cssClassName
	 */
	public String getCssClassName() {
		return cssClassName;
	}

	/**
	 * @param cssClassName the cssClassName to set
	 */
	public void setCssClassName(String cssClassName) {
		this.cssClassName = cssClassName;
	}
	
	/**
	 * @return the previousCrumb
	 */
	public Crumb getPreviousCrumb() {
		return previousCrumb;
	}

	/**
	 * @param previousCrumb the previousCrumb to set
	 */
	public void setPreviousCrumb(Crumb previousCrumb) {
		this.previousCrumb = previousCrumb;
	}

	/**
	 * @return the numeroPrecedentiAncore
	 */
	public Integer getNumeroPrecedentiAncore() {
		return numeroPrecedentiAncore;
	}

	/**
	 * @param numeroPrecedentiAncore the numeroPrecedentiAncore to set
	 */
	public void setNumeroPrecedentiAncore(Integer numeroPrecedentiAncore) {
		this.numeroPrecedentiAncore = numeroPrecedentiAncore;
	}
	
	/**
	 * Returns the correct exception class.
	 * 
	 * @return the exception correctly cast
	 */
	private GenericFrontEndMessagesException getExceptionAsCorrectClass() {
		return (GenericFrontEndMessagesException)exception;
	}

}
