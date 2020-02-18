/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.causale;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacfin2app.frontend.ui.model.causale.AggiornaCausaleEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di action per l'aggiornamento della Causale di Entrata
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 12/05/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaCausaleEntrataAction extends GenericCausaleEntrataAction<AggiornaCausaleEntrataModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4652076665759277367L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		
		// Caricamento liste
		caricaListeCodifiche();
		caricaListaClasseSoggetto();
		
		checkPrepareConclusoSenzaErrori();
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Caricamento valori default
		checkCasoDUsoApplicabile("Aggiornamento causale di incasso");
		
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		
		// Ricerca di dettaglio della Causale
		ricercaDettaglioCausale();
		model.setCausaleStatoDataCalcolato(model.calcolaCausaleStatoDataCalcolato());
		
		AttoAmministrativo attoAmministrativo = caricaAttoAmministrativoSePresente();
		if(attoAmministrativo != null) {
			model.impostaAttoAmministrativo(attoAmministrativo);
		}
		
		return SUCCESS;
	}

	/**
	 * Aggiorna la causale.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaCausaleEntrata() {
		final String methodName = "aggiornaCausaleEntrata";
		
		log.debug(methodName, "Aggiornamento della Causale di Entrata");
		AggiornaCausaleEntrata request = model.creaRequestAggiornaCausaleEntrata();
		logServiceRequest(request);
		AggiornaCausaleEntrataResponse response = preDocumentoEntrataService.aggiornaCausaleEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Fallimento nell'invocazione del servizio");
			addErrori(response);
			return INPUT;
		}
		
		CausaleEntrata causaleEntrata = response.getCausaleEntrata();
		log.debug(methodName, "Aggiornata la Causale codice " + causaleEntrata.getCodice() + " con uid " + causaleEntrata.getUid());
		
		impostaInformazioneSuccesso();
		
		model.setCausale(causaleEntrata);
		model.setUidCausaleDaAggiornare(causaleEntrata.getUid());
		
		ricercaDettaglioCausale();
		model.setCausaleStatoDataCalcolato(model.calcolaCausaleStatoDataCalcolato());
		
		// Imposto il parametro di rientro a TRUE
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
			
		return SUCCESS;
	}
	
	/**
	 * Validazione per l'aggiornamento della causale.
	 */
	public void validateAggiornaCausaleEntrata() {
		// Check campi obbligatori
		CausaleEntrata causaleEntrata = model.getCausale();
			
		checkNotNullNorInvalidUid(model.getTipoCausale(), "Causale tipo");
		checkNotNullNorInvalidUid(model.getCausale(), "Causale");
		// CR-4493
		warnCondition(model.getStrutturaAmministrativoContabile() != null && model.getStrutturaAmministrativoContabile().getUid() != 0, ErroreBil.ERRORE_GENERICO.getErrore("La struttura amministrativa non e' stata selezionata"));
		
		// Validazione logica
		checkCondition(ValidationUtil.checkDateInYear(causaleEntrata.getDataAnnullamento(), model.getAnnoEsercizioInt()), 
				ErroreFin.DATA_NON_VALIDA.getErrore("di competenza"));

		// Validazioni specifiche
		validazioneSoggetto();
		validazioneCapitolo();
		validazioneAccertamentoSubAccertamento();
		validazioneAttoAmministrativo();
		
		controlloConguenzaSoggettoAccertamento();
	}
	
}
