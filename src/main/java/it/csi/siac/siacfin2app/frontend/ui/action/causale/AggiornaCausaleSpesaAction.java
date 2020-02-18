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
import it.csi.siac.siacfin2app.frontend.ui.model.causale.AggiornaCausaleSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di action per l'aggiornamento della Causale di Spesa
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 29/04/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaCausaleSpesaAction extends GenericCausaleSpesaAction<AggiornaCausaleSpesaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3410995502977493215L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		
		// Caricamento liste
		caricaListeCodifiche();
		caricaListaClasseSoggetto();
		caricaListaSedeSecondariaSoggettoEModalitaPagamentoSoggetto();
		
		checkPrepareConclusoSenzaErrori();
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Caricamento valori default
		checkCasoDUsoApplicabile("Aggiornamento causale di pagamento");
		
		leggiEventualiInformazioniAzionePrecedente();
		
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
	public String aggiornaCausaleSpesa() {
		final String methodName = "aggiornaCausaleSpesa";
		
		log.debug(methodName, "Aggiornamento della Causale di Spesa");
		AggiornaCausaleSpesa request = model.creaRequestAggiornaCausaleSpesa();
		logServiceRequest(request);
		AggiornaCausaleSpesaResponse response = preDocumentoSpesaService.aggiornaCausaleSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Fallimento nell'invocazione del servizio");
			addErrori(response);
			return INPUT;
		}
		
		CausaleSpesa causaleSpesa = response.getCausaleSpesa();
		
		log.debug(methodName, "Aggiornata la Causale codice " + causaleSpesa.getCodice() + " con uid " + causaleSpesa.getUid());
		

		log.debug(methodName, "Causale aggiornata");
		impostaInformazioneSuccesso();
		
		model.setCausale(causaleSpesa);
		model.setUidCausaleDaAggiornare(causaleSpesa.getUid());
		
		ricercaDettaglioCausale();
		model.setCausaleStatoDataCalcolato(model.calcolaCausaleStatoDataCalcolato());
		
		// Imposto il parametro di rientro a TRUE
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
			
		return SUCCESS;
	}
	
	/**
	 * Validazione per l'aggiornamento della causale.
	 */
	public void validateAggiornaCausaleSpesa() {
		// Check campi obbligatori
		CausaleSpesa causaleSpesa = model.getCausale();
			
		checkNotNullNorInvalidUid(model.getTipoCausale(), "Causale tipo");
		checkNotNullNorInvalidUid(model.getCausale(), "Causale");
		
		// Validazione logica
		checkCondition(ValidationUtil.checkDateInYear(causaleSpesa.getDataAnnullamento(), model.getAnnoEsercizioInt()), 
				ErroreFin.DATA_NON_VALIDA.getErrore("di competenza"));

		// Validazioni specifiche
		validazioneSoggetto();
		validazioneCapitolo();
		validazioneImpegnoSubImpegno();
		validazioneAttoAmministrativo();
		
		controlloConguenzaSoggettoImpegno();
	}
	
}
