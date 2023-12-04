/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ComponentTagSupport;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * Tag for handling numeric properties
 * @author interlogic
 * @version 1.0.0 - 07/07/2021
 */
public class NumericPropertyTag extends ComponentTagSupport {
	
	/** For serialization */
	private static final long serialVersionUID = -2792857448750998322L;

	private String value;
	private Integer decimalPlaces = Integer.valueOf(2);
	private Boolean formatted = Boolean.TRUE;

	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
		return new NumericProperty(stack);
	}

	@Override
	protected void populateParams() {
		super.populateParams();

		NumericProperty tag = (NumericProperty) component;
		tag.setDecimalPlaces(decimalPlaces);
		tag.setValue(value);
		tag.setFormatted(formatted);
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @param decimalPlaces the decimalPlaces to set
	 */
	public void setDecimalPlaces(Integer decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

	public void setFormatted(Boolean formatted) {
		this.formatted = formatted;
	}
}
