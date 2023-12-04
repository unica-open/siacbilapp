/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva;

import java.util.Arrays;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.AssociaAttivitaIvaCapitoloModel;
import it.csi.siac.siacfin2ser.frontend.webservice.AttivitaIvaCapitoloService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRelazioneAttivitaIvaCapitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRelazioneAttivitaIvaCapitoloResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRelazioneAttivitaIvaCapitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceRelazioneAttivitaIvaCapitoloResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRelazioneAttivitaIvaCapitolo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRelazioneAttivitaIvaCapitoloResponse;

/**
 * Classe di action per l'associazione tra Attivita Iva e Capitolo
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 28/05/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AssociaAttivitaIvaCapitoloAction extends GenericBilancioAction<AssociaAttivitaIvaCapitoloModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4652076665759277367L;
	
	@Autowired private transient AttivitaIvaCapitoloService attivitaIvaCapitoloService;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		checkCasoDUsoApplicabile(model.getTitolo());
		caricaListaStatoOperativoElementoDiBilancio();
		// Imposto il valore di default dello stato
		model.setStatoOperativoElementoDiBilancio(StatoOperativoElementoDiBilancio.VALIDO);
		return SUCCESS;
	}

	/**
	 * Carica la lista degli Stati Operativi del Capitolo all'interno del model.
	 */
	private void caricaListaStatoOperativoElementoDiBilancio() {
		model.setListaStatoOperativoElementoDiBilancio(Arrays.asList(StatoOperativoElementoDiBilancio.values()));
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		// Il Bilancio non deve essere in fase Pluriennale, Previsione né chiuso
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
				FaseBilancio.PREVISIONE.equals(faseBilancio) ||
				FaseBilancio.CHIUSO.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
	/**
	 * Ottiene le Attivita Iva associate al Capitolo fornito in input
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 */
	public String ottieniAttivitaIvaAssociateCapitolo() {
		final String methodName = "ottieniAttivitaIvaAssociateCapitolo";
		// Effettuo la validazione dei dati
		validateOttieniAttivitaIvaAssociateCapitolo();
		if(hasErrori()) {
			log.info(methodName, "Errore nella validazione dei dati");
			return SUCCESS;
		}
		
		RicercaRelazioneAttivitaIvaCapitolo request = model.creaRequestRicercaRelazioneAttivitaIvaCapitolo();
		logServiceRequest(request);
		RicercaRelazioneAttivitaIvaCapitoloResponse response = attivitaIvaCapitoloService.ricercaRelazioneAttivitaIvaCapitolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori nell'invocazione del servizio
		if(response.hasErrori()) {
			log.info(methodName, "Errore nell'invocazione del servizio RicercaRelazioneAttivitaIvaCapitolo");
			addErrori(response);
			return SUCCESS;
		}
		
		model.setListaAttivitaIva(response.getListaAttivitaIva());
		return SUCCESS;
	}

	/**
	 * Controlla se i dati forniti per la ricerca delle associazioni con le Attivita Iva siano validi.
	 */
	private void validateOttieniAttivitaIvaAssociateCapitolo() {
		checkCondition(model.getCapitolo() != null && model.getCapitolo().getUid() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("capitolo"));
	}
	
	/**
	 * Associa l'attivita iva al capitolo fornito.
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 */
	public String associaAttivitaIvaAlCapitolo() {
		final String methodName = "associaAttivitaIvaAlCapitolo";
		// Effettuo la validazione dei dati
		validateAssociazioneAttivitaIvaCapitolo();
		if(hasErrori()) {
			log.info(methodName, "Errore nella validazione dei dati");
			return SUCCESS;
		}
		InserisceRelazioneAttivitaIvaCapitolo request = model.creaRequestInserisceRelazioneAttivitaIvaCapitolo();
		logServiceRequest(request);
		InserisceRelazioneAttivitaIvaCapitoloResponse response = attivitaIvaCapitoloService.inserisceRelazioneAttivitaIvaCapitolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori nell'invocazione del servizio
		if(response.hasErrori()) {
			log.info(methodName, "Errore nell'invocazione del servizio InserisceRelazioneAttivitaIvaCapitolo");
			addErrori(response);
			return SUCCESS;
		}
		
		model.setListaAttivitaIva(response.getListaAttivitaIva());
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Elimina l'associazione tra attivita iva e capitolo fornito.
	 * 
	 * @return una String corrispondente al risultato dell'invocazione
	 */
	public String eliminaAssociazioneAttivitaIvaAlCapitolo() {
		final String methodName = "eliminaAssociazioneAttivitaIvaAlCapitolo";
		// Effettuo la validazione dei dati
		validateAssociazioneAttivitaIvaCapitolo();
		if(hasErrori()) {
			log.info(methodName, "Errore nella validazione dei dati");
			return SUCCESS;
		}
		EliminaRelazioneAttivitaIvaCapitolo request = model.creaRequestEliminaRelazioneAttivitaIvaCapitolo();
		logServiceRequest(request);
		EliminaRelazioneAttivitaIvaCapitoloResponse response = attivitaIvaCapitoloService.eliminaRelazioneAttivitaIvaCapitolo(request);
		logServiceResponse(response);
		
		// Controllo gli errori nell'invocazione del servizio
		if(response.hasErrori()) {
			log.info(methodName, "Errore nell'invocazione del servizio EliminaRelazioneAttivitaIvaCapitolo");
			addErrori(response);
			return SUCCESS;
		}
		
		model.setListaAttivitaIva(response.getListaAttivitaIva());
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Controlla se i dati forniti per le associazioni tra AttivitaIva e Capitolo siano validi.
	 */
	private void validateAssociazioneAttivitaIvaCapitolo() {
		checkCondition(model.getCapitolo() != null && model.getCapitolo().getUid() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("capitolo"));
		checkCondition(model.getAttivitaIva() != null && model.getAttivitaIva().getUid() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("attivita"));
	}
	
}
