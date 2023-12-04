/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;

import java.io.Serializable;

import it.csi.siac.siaccorser.model.ServiceRequest;

public abstract class DataTableAjaxModel<S extends Serializable, REQ extends ServiceRequest> extends BaseDataTableAjaxModel<S> {

	private static final long serialVersionUID = -579205906298690672L; 
	
	public abstract REQ buildRequest();
}
