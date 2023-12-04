/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.frontend.webservice.msg.AsyncServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.CompletaDefinisciPreDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaDefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTotaliPreDocumentoEntrataPerStatoResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;


/**
 * Classe di action per la ricerca del PreDocumento di Entrata per il completamento e la definizione
 * 
 * @author Marchino Alessandro
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CompletaDefinisciPreDocumentoEntrataAction extends BaseCompletaDefinisciPreDocumentoEntrataAction<CompletaDefinisciPreDocumentoEntrataModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3049741139669487436L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		
		// Caricamento liste
		try {
			//SIAC-6780
			caricaListaContoCorrente();
			
			caricaListaTipoCausale();
			caricaListaCausaleEntrata();
			caricaListaTipoAtto();
			caricaListaClasseSoggetto();
		} catch(WebServiceInvocationFailureException e) {
			log.error("prepare", "Errore nell'invocazione del caricamento di una lista: " + e.getMessage());
		} finally {
			checkMetodoConclusoSenzaErrori();
		}
		
	}

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Imposto i valori di default
		
		return SUCCESS;
	}
	
	@Deprecated
	public String ricercaPerCompletaDefinisciMassivo() {
		final String methodName = "ricercaPerCompletaDefinisciMassivo";
		
		RicercaSinteticaPreDocumentoEntrata request = model.creaRequestRicercaSinteticaPreDocumentoEntrata();
		logServiceRequest(request);
		RicercaSinteticaPreDocumentoEntrataResponse response = preDocumentoEntrataService.ricercaSinteticaPreDocumentoEntrata(request);
		logServiceResponse(response);
		
		if (response.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio di ricerca sintetica");
			addErrori(response);
			return INPUT;
		}
		
		int totaleElementi = response.getPreDocumenti().getTotaleElementi();
		
		if(totaleElementi == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo. Totale preDocumenti: " + totaleElementi);
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PREDOCUMENTI_ENTRATA, request);
	
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_PREDOCUMENTI_ENTRATA, response.getPreDocumenti());
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA_PREDOCUMENTO, response.getImportoTotale());
		
		// Impostazione in sessione di alcuni parametri per l'abilitazione delle operazioni
		sessionHandler.setParametro(BilSessionParameter.PREDOCUMENTO_CAUSALE_MANCANTE, isCausaleEntrataMancante());
		
		// SIAC-4280
		sessionHandler.setParametro(BilSessionParameter.ABILITATA_MODIFICA_ASSOCIAZIONE_IMPUTAZIONI_CONTABILI_PREDOCUMENTO_ENTRATA, isAbilitataModificaImputazioniContabili());
		sessionHandler.setParametro(BilSessionParameter.CAUSALE_SELEZIONATA_PREDOCUMENTO_ENTRATA, findCausale());

		sessionHandler.setParametro(BilSessionParameter.FROM_COMPLETA_DEFINISCI, Boolean.TRUE);

		return SUCCESS;
	}
	
	/**
	 * Controlla se presente il tipo della causale
	 * @return boolean
	 */
	public boolean isContoCorrenteMancante() {
		return model.getContoCorrente() != null && model.getContoCorrente().getUid() != 0 ? false : true;
	}

	/**
	 * Controlla se presente il tipo della causale
	 * @return boolean
	 */
	public boolean isSoggettoMancante() {
		return model.getCausaleEntrata() != null && model.getCausaleEntrata().getUid() != 0 ? true : false;
	}
	
	/**
	 * Controlla se presente il tipo della causale
	 * @return boolean
	 */
	public boolean isCausaleEntrataMancante() {
		return model.getCausaleEntrata() != null && model.getCausaleEntrata().getUid() != 0 ? true : false;
	}
	
	/**
	 * Controlla se presente la causale
	 * @return boolean
	 */
	public boolean isTipoCusaleMancante() {
		return model.getTipoCausale() != null && model.getTipoCausale().getUid() != 0 ? true : false;
	}
	
	/**
	 * Controlla se la modifica delle imputazioni contabili sia abilitato
	 * @return true se la modifica &eacute; abilitata; false altrimenti
	 */
	private Boolean isAbilitataModificaImputazioniContabili() {
		final String methodName = "isAbilitataModificaImputazioniContabili";
		// La modifica delle imputazioni e' abilitata solo se sono selezionati tipo e codice causale
		boolean tipoCausaleSelezionato = isTipoCusaleMancante();
		boolean causaleSelezionata = isCausaleEntrataMancante();
		log.debug(methodName, "Tipo causale selezionato? " + tipoCausaleSelezionato + " - causale selezionata? " + causaleSelezionata);
		return Boolean.valueOf(tipoCausaleSelezionato && causaleSelezionata);
	}
	
	/**
	 * Ottiene la causale di entrata dalla lista in sessione
	 * @return la causale di entrata
	 */
	private CausaleEntrata findCausale() {
		if(model.getCausaleEntrata() == null || model.getCausaleEntrata().getUid() == 0) {
			return null;
		}
		return ComparatorUtils.searchByUid(model.getListaCausaleEntrata(), model.getCausaleEntrata());
	}
	
	/**-
	 * Ricerca dei totali
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String cercaTotali() {
		final String methodName = "cercaTotali";
		// Invocazione del servizio
		RicercaTotaliPreDocumentoEntrataPerStato req =  model.creaRequestRicercaTotaliPreDocumentoEntrataPerStato();
		RicercaTotaliPreDocumentoEntrataPerStatoResponse res = preDocumentoEntrataService.ricercaTotaliPreDocumentoEntrataPerStato(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			// Si sono verificati degli errori: esco.
			addErrori(res);
			log.debug(methodName, "esecuzione del servizio RicercaTotaliPreDocumentoEntrataPerStato terminata con errori.");
			return INPUT;
		}
		
		impostaTotaliElenco(res);
		impostaTotaliElencoNoCassa(res);
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il completamento e la definizione del preDocumento.
	 */
	@Override
	public void validateCompletaDefinisci() {
		boolean formValido =
				checkCampoValorizzato(model.getDataCompetenzaDa(), "Data da") ||
				checkCampoValorizzato(model.getDataCompetenzaA(), "Data a") ||
				checkPresenzaIdEntita(model.getCausaleEntrata()) ||
				checkPresenzaIdEntita(model.getContoCorrente());

		
		if(!formValido) {
			addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		}
		checkCondition(model.getDataCompetenzaDa() == null || model.getDataCompetenzaA() == null
				|| !model.getDataCompetenzaA().before(model.getDataCompetenzaDa()),
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("Data competenza", "la data di competenza da non deve essere inferiore la data di competenza a"));
		
		//SIAC-7457
		//Effettuo un controllo sulla capienza dell'importo da regolarizzare
		if(model.getImportoPreDocumentiNoCassaTotale() != null && model.getProvvisorioCassa().getImportoDaRegolarizzare() != null) {
			String keyProvvisorio = model.getProvvisorioCassa().getCodice();
			checkCondition(!(model.getImportoPreDocumentiNoCassaTotale().compareTo(model.getProvvisorioCassa().getImportoDaRegolarizzare()) > 0), 
					ErroreFin.PROVVISORIO_NON_REGOLARIZZABILE.getErrore("completa e definisci","predisposizione di incasso", keyProvvisorio,
							"L'importo delle predisposizioni supera l'importo da regolarizzare del provvisorio"));
		}
		
		super.validateCompletaDefinisci();
	}
	
	
	@Override
	public String completaDefinisci() {
		final String methodName = "ricerca";
		
		CompletaDefiniscePreDocumentoEntrata req = model.creaRequestCompletaDefiniscePreDocumentoEntrata();
		AsyncServiceResponse res = preDocumentoEntrataService.completaDefiniscePreDocumentoEntrataAsync(wrapRequestToAsync(req));
		
		if (res.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio di ricerca sintetica");
			addErrori(res);
			return INPUT;
		}
		
		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("completamento e definizione della predisposizione di incasso", ""));
		return SUCCESS;
	}
	
}
