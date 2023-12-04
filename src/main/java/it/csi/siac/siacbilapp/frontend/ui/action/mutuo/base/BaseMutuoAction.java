/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.base;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.action.mutuo.helper.MutuoActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.provvedimento.helper.ProvvedimentoActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.soggetto.helper.SoggettoActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.tipofinanziamento.helper.TipoFinanziamentoActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.base.BaseMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.MutuoService;
import it.csi.siac.siacbilser.model.mutuo.TipoTassoMutuo;
import xyz.timedrain.arianna.plugin.BreadCrumb;

public abstract class BaseMutuoAction<MM extends BaseMutuoModel> extends GenericBilancioAction<MM> {

	private static final long serialVersionUID = -4803429860291622841L;
	
	@Autowired protected transient MutuoService mutuoService;
	
	protected transient MutuoActionHelper mutuoActionHelper;
	protected transient SoggettoActionHelper soggettoActionHelper;
	protected transient ProvvedimentoActionHelper provvedimentoActionHelper;
	protected transient TipoFinanziamentoActionHelper tipoFinanziamentoActionHelper;
	
	@Override
	@BreadCrumb(BaseMutuoAction.MODEL_TITOLO)
	public String execute() throws Exception {
		return super.execute();
	}

	@PostConstruct
	protected void postConstruct() {
		mutuoActionHelper = new MutuoActionHelper(this);
		soggettoActionHelper = new SoggettoActionHelper(this);
		provvedimentoActionHelper = new ProvvedimentoActionHelper(this);
	}
	
	public void prepareEnterPage() {
		try {
			super.prepare();
		}
		catch (Exception e) {
			throwSystemExceptionErroreDiSistema(e);
		}
	}

	public String enterPage() throws Exception {
		return SUCCESS;
	}

	public void setTipoTassoStr(String tipoTasso) {
		model.getMutuo().setTipoTasso(TipoTassoMutuo.fromCodice(tipoTasso));
	}

	
	public String getTipoTassoStr() {
		return model.getMutuo().getTipoTasso().getCodice();
	}


	public void saveIdMutuoIntoSession(Integer idMutuo) {
		sessionHandler.setParametro(BilSessionParameter.UID_MUTUO, idMutuo);
	}

	public Integer getIdMutuoFromSession() {
		return sessionHandler.getParametro(BilSessionParameter.UID_MUTUO);
	}
	
	public void callPrepareEnterPageOnErrors() {
		if (hasErrori()) {
			prepareEnterPage();
		}
	}
}

