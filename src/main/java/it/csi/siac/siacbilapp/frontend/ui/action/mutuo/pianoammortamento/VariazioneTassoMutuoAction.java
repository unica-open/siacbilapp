/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.pianoammortamento;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.pianoammortamento.VariazioneTassoMutuoModel;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class VariazioneTassoMutuoAction extends BaseVariazioneMutuoAction<VariazioneTassoMutuoModel> {


	private static final long serialVersionUID = -506512517381413029L;


}
