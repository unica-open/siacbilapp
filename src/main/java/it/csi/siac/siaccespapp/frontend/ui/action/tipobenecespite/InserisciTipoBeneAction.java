/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.tipobenecespite;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccespapp.frontend.ui.model.tipobenecespite.InserisciTipoBeneModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.model.TipoBeneCespite;

/**
 * The Class InserisciTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciTipoBeneAction extends GenericTipoBeneAction<InserisciTipoBeneModel> {

	/**Per la serializzazione*/
	private static final long serialVersionUID = 1219685974249875285L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		cleanErroriMessaggiInformazioni();
		caricaListe();		
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		model.setTipoBeneCespite(null);
		return SUCCESS;		
	}
	
	/**
	 * Validate salva.
	 */
	public void validateSalva() {
		TipoBeneCespite tipoBeneCespite = model.getTipoBeneCespite();
		
		checkNotNull(tipoBeneCespite, "tipo bene");
		checkNotNullNorEmpty(tipoBeneCespite.getCodice(), "codice tipo bene");
		checkNotNullNorEmpty(tipoBeneCespite.getDescrizione(), "descrizione tipo bene");
		checkNotNullNorInvalidUid(tipoBeneCespite.getCategoriaCespiti(), "categoria cespiti");
		
	}
	
	/**
	 * Salva.
	 *
	 * @return the string
	 */
	public String salva() {
		InserisciTipoBeneCespite req = model.creaRequestInserisciTipoBeneCespite();
		InserisciTipoBeneCespiteResponse res = classificazioneCespiteService.inserisciTipoBeneCespite(req);
		if(res.hasErrori()) {
			addErrori(res);
			return INPUT;
		}
		model.setTipoBeneCespite(res.getTipoBeneCespite());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}

}
