/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.action.causali;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali.BaseInserisciAggiornaCausaleEPBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali.InserisciCausaleEPBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;
import it.csi.siac.siacgsaapp.frontend.ui.model.causali.InserisciCausaleEPGSAModel;

/**
 * Classe di action per l'inserimento della causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/03/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaCausaleEPBaseAction.MODEL_SESSION_NAME_INSERIMENTO_GSA)
public class InserisciCausaleEPGSAAction extends InserisciCausaleEPBaseAction <InserisciCausaleEPGSAModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 4469636595935362556L;
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		checkCasoDUsoApplicabile("");
		caricaListe();
		return SUCCESS;
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterListaClassiPiano() {
		return BilSessionParameter.LISTA_CLASSE_PIANO_GSA;
	}

	@Override
	protected boolean getFaseDiBilancioNonCompatibile(FaseBilancio faseBilancio) {
	    return 
		FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
		FaseBilancio.PREVISIONE.equals(faseBilancio);
	}	
	
	@Override
	protected void checkContiCausaleIntegrata() {
		final String methodName = "checkCausaleIntegrata";
		int numContiSegnoDare = 0;
		int numContiSegnoAvere = 0;
		
		for(ContoTipoOperazione cto : model.getListaContoTipoOperazione()) {

			// Conti DARE
			if(OperazioneSegnoConto.DARE.equals(cto.getOperazioneSegnoConto())) {
				numContiSegnoDare++;
			}
			// Conti AVERE
			if(OperazioneSegnoConto.AVERE.equals(cto.getOperazioneSegnoConto())) {
				numContiSegnoAvere++;
			}
		}

		int numContiTotale = numContiSegnoDare + numContiSegnoAvere;
		log.debug(methodName, "Numero conti: " + numContiTotale + " -- Numero conti con segno DARE: " + numContiSegnoDare + " -- Numero conti con segno AVERE: " + numContiSegnoAvere);
		checkCondition(numContiSegnoDare >= 1 && numContiSegnoAvere >= 1, ErroreGEN.ASSENZA_CONTI_OBBLIGATORI_CAUSALI_DI_RACCORDO.getErrore());
	}
	
}
