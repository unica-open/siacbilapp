/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.result;

import org.apache.struts2.json.JSONResult;

/**
 * Result for working with <a href="https://www.datatables.net/">dataTables</a>. Defines standard includes.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/09/2014
 *
 */
public class DataTablesResult extends JSONResult {
	
	/** For serialization purpose */
	private static final long serialVersionUID = 5007655031236727633L;
	private static final String INCLUDE_PROPERTIES = "errori.*,sEcho,iTotalRecords,iTotalDisplayRecords,iDisplayStart,iDisplayLength,aaData.*,moreData.*";

	/** Empty default constructor */
	public DataTablesResult() {
		super();
		setIgnoreHierarchy(false);
		setEnumAsBean(true);
		setExcludeNullProperties(true);
		setIncludeProperties(INCLUDE_PROPERTIES);
	}
	
}
