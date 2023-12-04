/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.tag;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;

/**
 * Tag for handling numeric properties
 * 
 * @author interlogic
 * @version 1.0.0 - 07/07/2021
 */
@StrutsTag(name = "numeric", tldBodyContent = "empty", tldTagClass = "it.csi.siac.siacbilapp.frontend.ui.util.tag.NumericPropertyTag", description = "Print out a number as a string")
public class NumericProperty extends Component {
	private static final Logger LOG = LoggerFactory.getLogger(NumericProperty.class);

	public NumericProperty(ValueStack stack) {
		super(stack);
	}

	private String value;
	private Integer decimalPlaces;
	private Boolean formatted;

	@StrutsTagAttribute(description="Value to be displayed", type="Object", defaultValue="&lt;top of stack&gt;")
	public void setValue(String value) {
		this.value = value;
	}

	@StrutsTagAttribute(description = "The number of decimal places", type = "Integer", defaultValue = "2")
	public void setDecimalPlaces(Integer decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

	@StrutsTagAttribute(description = "Whether to format or not the number", type = "Boolean", defaultValue = "true")
	public void setFormatted(Boolean formatted) {
		this.formatted = formatted;
	}

	@Override
	public boolean start(Writer writer) {
		boolean result = super.start(writer);

		BigDecimal actualValue = null;

		if (value == null) {
			value = "top";
		} else {
			value = stripExpressionIfAltSyntax(value);
		}
		
		actualValue = (BigDecimal) getStack().findValue(value, BigDecimal.class, throwExceptionOnELFailure);

		try {
			if (actualValue != null) {
				writer.write(formatted ?
						FormatUtils.formatNumber(actualValue, decimalPlaces == null ? 2 : decimalPlaces) :
						actualValue.toString()
				);
			}
		} catch (IOException e) {
			if (LOG.isInfoEnabled()) {
				LOG.info("Could not print out value '" + value + "'", e);
			}
		}

		return result;
	}

}
