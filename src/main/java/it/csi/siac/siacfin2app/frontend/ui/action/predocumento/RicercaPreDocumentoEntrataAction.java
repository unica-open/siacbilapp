/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.RicercaPreDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.OrdinamentoPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di action per la ricerca del PreDocumento di Entrata
 * 
 * @author Marchino Alessandro,Nazha Ahmad
 * @version 1.0.0 - 15/04/2014
 * @version 1.0.1 - 11/06/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaPreDocumentoEntrataAction extends GenericPreDocumentoEntrataAction<RicercaPreDocumentoEntrataModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8459559205236649255L;
	
	@Autowired private transient DocumentoEntrataService documentoEntrataService;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		
		// Caricamento liste
		try {
			caricaListaTipoCausale();
			caricaListaCausaleEntrata();
			caricaListaContoCorrente();
			caricaListaNazioni();
			caricaListaSesso();
			caricaListaTipoAtto();
			caricaListaClasseSoggetto();
			caricaListaTipoFinanziamento();
			caricaListaStatoOperativoPreDocumento();
			caricaListaTipoDocumento();
			// SIAC-5250
			caricaListaOrdinamentoPreDocumentoEntrata();
		} catch(WebServiceInvocationFailureException e) {
			log.error("prepare", "Errore nell'invocazione del caricamento di una lista: " + e.getMessage());
		} finally {
			checkMetodoConclusoSenzaErrori();
		}
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Imposto i valori di default
		//model.setStatoOperativoPreDocumento(StatoOperativoPreDocumento.INCOMPLETO);
		model.setFlagNonAnnullati(Boolean.TRUE);
		model.setOrdinamentoPreDocumentoEntrata(OrdinamentoPreDocumentoEntrata.NUMERO_PREDOCUMENTO);
		return SUCCESS;
	}
	
	/**
	 * Ricerca il preDocumento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ricerca() {
		final String methodName = "ricerca";
		
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
		
		List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile = 
				sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		String stringaRiepilogo = model.componiStringaRiepilogo(listaStrutturaAmministrativoContabile);
		
		sessionHandler.setParametro(BilSessionParameter.RIEPILOGO_RICERCA_PREDOCUMENTO, stringaRiepilogo);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		sessionHandler.setParametro(BilSessionParameter.IMPORTO_TOTALE_RICERCA_PREDOCUMENTO, response.getImportoTotale());
		
		// Impostazione in sessione di alcuni parametri per l'abilitazione delle operazioni
		sessionHandler.setParametro(BilSessionParameter.PREDOCUMENTO_CAUSALE_MANCANTE, model.getFlagCausaleEntrataMancante());
		sessionHandler.setParametro(BilSessionParameter.PREDOCUMENTO_STATO_OPERATIVO, model.getStatoOperativoPreDocumento());
		
		// SIAC-4280
		sessionHandler.setParametro(BilSessionParameter.ABILITATA_MODIFICA_ASSOCIAZIONE_IMPUTAZIONI_CONTABILI_PREDOCUMENTO_ENTRATA, isAbilitataModificaImputazioniContabili());
		sessionHandler.setParametro(BilSessionParameter.CAUSALE_SELEZIONATA_PREDOCUMENTO_ENTRATA, findCausale());
		
		return SUCCESS;
	}

	/**
	 * Controlla se la modifica delle imputazioni contabili sia abilitato
	 * @return true se la modifica &eacute; abilitata; false altrimenti
	 */
	private Boolean isAbilitataModificaImputazioniContabili() {
		final String methodName = "isAbilitataModificaImputazioniContabili";
		// La modifica delle imputazioni e' abilitata solo se sono selezionati tipo e codice causale
		boolean tipoCausaleSelezionato = model.getTipoCausale() != null && model.getTipoCausale().getUid() != 0;
		boolean causaleSelezionata = model.getCausaleEntrata() != null && model.getCausaleEntrata().getUid() != 0;
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

	/**
	 * Validazione per la ricerca del preDocumento.
	 */
	public void validateRicerca() {

		PreDocumentoEntrata pds = model.getPreDocumento();
		DatiAnagraficiPreDocumento dapd = model.getDatiAnagraficiPreDocumento();
		
		boolean formValido = checkStringaValorizzata(pds.getPeriodoCompetenza(), "Periodo competenza")
				|| checkStringaValorizzata(model.getPreDocumento().getNumero(), "Numero predocumento")
				|| checkCampoValorizzato(model.getDataCompetenzaDa(), "Data da")
				|| checkCampoValorizzato(model.getDataCompetenzaA(), "Data a")
				|| checkCampoValorizzato(model.getDataTrasmissioneDa(), "Data trasmissione da")
				|| checkCampoValorizzato(model.getDataTrasmissioneA(), "Data trasmissione a")
				|| checkPresenzaIdEntita(model.getStrutturaAmministrativoContabile())
				|| checkPresenzaIdEntita(model.getTipoCausale()) || checkPresenzaIdEntita(model.getCausaleEntrata())
				|| checkPresenzaIdEntita(model.getContoCorrente())
				|| checkCampoValorizzato(model.getStatoOperativoPreDocumento(), "Stato operativo")
				|| checkCampoValorizzato(model.getPreDocumento().getImporto(), "Importo")
				|| checkStringaValorizzata(dapd.getRagioneSociale(), "Ragione sociale")
				|| checkStringaValorizzata(dapd.getCognome(), "Cognome")
				|| checkStringaValorizzata(dapd.getNome(), "Nome")
				|| checkStringaValorizzata(dapd.getCodiceFiscale(), "Codice fiscale")
				|| checkStringaValorizzata(dapd.getPartitaIva(), "Partita IVA");
		
		formValido = validazioneCapitolo() || formValido;
		formValido = validazioneAccertamentoSubAccertamento(null) || formValido;
		formValido = validazioneSoggetto() || formValido;
		formValido = validazioneAttoAmministrativo() || formValido;
		formValido = validazioneDocumento() || formValido;
		formValido = validazioneOrdinativo() || formValido;
		// SIAC-5001
		formValido = validazioneElencoDocumentiAllegato() || formValido;
		// Flags
		formValido = formValido || Boolean.TRUE.equals(model.getFlagCausaleEntrataMancante())
				|| Boolean.TRUE.equals(model.getFlagContoCorrenteMancante())
				|| Boolean.TRUE.equals(model.getFlagAttoAmministrativoMancante())
				|| Boolean.TRUE.equals(model.getFlagSoggettoMancante())
				|| Boolean.TRUE.equals(model.getFlagEstraiNonIncassato());
		if (!formValido) {
			addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		}
		// Il default come ordinamento e' il numero del documento. Nel caso non sia settato lo forzo
		if (model.getOrdinamentoPreDocumentoEntrata() == null) {
			model.setOrdinamentoPreDocumentoEntrata(OrdinamentoPreDocumentoEntrata.NUMERO_PREDOCUMENTO);
		} 
			
	}
	
	/**
	 * Carica la lista degli Stati Operativi del PreDocumento.
	 */
	private void caricaListaStatoOperativoPreDocumento() {
		model.setListaStatoOperativoPreDocumento(Arrays.asList(StatoOperativoPreDocumento.values()));
	}
	
//	/**
//	 * Carica la lista dei tipi di documento.
//	 */
//	private void caricaListaTipoDocumento() {
//		List<TipoDocumento> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO);
//		if(listaInSessione == null) {
//			RicercaTipoDocumento request = model.creaRequestRicercaTipoDocumento(TipoFamigliaDocumento.ENTRATA);
//			logServiceRequest(request);
//			RicercaTipoDocumentoResponse response = documentoService.ricercaTipoDocumento(request);
//			logServiceResponse(response);
//			
//			// Controllo gli errori
//			if(response.hasErrori()) {
//				//si sono verificati degli errori: esco.
//				addErrori(response);
//				return;
//			}
//			
//			listaInSessione = response.getElencoTipiDocumento();
//			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO, listaInSessione);
//			
//		}
//		model.setListaTipoDocumento(listaInSessione);
//	}
	
	/**
	 * Caricamento della lista degli ordinamenti del predocumento entrata
	 */
	private void caricaListaOrdinamentoPreDocumentoEntrata() {
		model.setListaOrdinamentoPreDocumentoEntrata(Arrays.asList(OrdinamentoPreDocumentoEntrata.values()));
	}
	
	/**
	 * Effettua una validazione del Capitolo fornito in input.
	 * 
	 * @return <code>true</code> se la validazione &eacute; andata a buon fine; <code>false</code> in caso contrario
	 */
	private boolean validazioneDocumento() {
		final String methodName = "validazioneDocumento";
		DocumentoEntrata doc = model.getDocumento();
		TipoDocumento tipoDoc = model.getTipoDocumento();
		
		if(doc.getAnno() == null || StringUtils.isBlank(doc.getNumero()) || tipoDoc == null || tipoDoc.getUid() == 0) {
			return false;
		}
		
		List<TipoDocumento> listaTipoDocumento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO);
		tipoDoc = ComparatorUtils.searchByUid(listaTipoDocumento, tipoDoc);
		model.setTipoDocumento(tipoDoc);
		
		RicercaSinteticaDocumentoEntrata request = model.creaRequestRicercaSinteticaDocumentoEntrata();
		logServiceRequest(request);
		RicercaSinteticaDocumentoEntrataResponse response = documentoEntrataService.ricercaSinteticaDocumentoEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nella ricerca del Documento");
			addErrori(response);
			return false;
		}
		
		String classificazioneDocumento = doc.getAnno() + "/" + doc.getNumero() + "/" + tipoDoc.getCodice();
		
		int totaleElementi = response.getTotaleElementi();
		checkCondition(totaleElementi > 0, ErroreCore.ENTITA_NON_TROVATA.getErrore("Documento", classificazioneDocumento));
		checkCondition(totaleElementi < 2, ErroreFin.OGGETTO_NON_UNIVOCO.getErrore("Documento"));
		
		if(totaleElementi == 1) {
			// Imposto i dati del capitolo
			model.setDocumento(doc);
		}
		return true;
	}
	
	/**
	 * Validazione dell'ordinativo
	 * @return se l'ordinativo sia valido
	 */
	private boolean validazioneOrdinativo() {
		final String methodName = "validazioneOrdinativo";
		if(model.getOrdinativo() == null || (model.getOrdinativo().getAnno() == null && model.getOrdinativo().getNumero() == null)) {
			log.debug(methodName, "Ordinativo non presente");
			return false;
		}
		if(model.getOrdinativo().getAnno() == null || model.getOrdinativo().getNumero() == null) {
			log.debug(methodName, "Anno o numero non valorizzati");
			addErrore(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Ordinativo", ": anno e numero devono essere entrambi valorizzati"));
			return false;
		}
		return true;
	}
	
	/**
	 * Validazione dell'elenco
	 * @return se l'ordinativo sia valido
	 */
	private boolean validazioneElencoDocumentiAllegato() {
		final String methodName = "validazioneElencoDocumentiAllegato";
		// Se non c'e' l'elenco esco
		if(model.getPreDocumento().getElencoDocumentiAllegato() == null) {
			log.debug(methodName, "Elenco documenti allegato non presente");
			return false;
		}
		ElencoDocumentiAllegato eda = model.getPreDocumento().getElencoDocumentiAllegato();
		// Se non ci sonoi dati esco
		if(eda.getAnno() == null && eda.getNumero() == null) {
			log.debug(methodName, "Anne e numero dell'elenco non presenti");
			return false;
		}
		// Se anno e numero sono presenti, devono essere contemporaneamente valorizzati
		if(eda.getAnno() == null || eda.getNumero() == null) {
			log.debug(methodName, "Anno o numero non valorizzato");
			addErrore(ErroreCore.VALORE_NON_CONSENTITO.getErrore("Elenco", ": anno e numero devono essere entrambi valorizzati"));
			return false;
		}
		// Tutto valido
		return true;
	}
	
	@Override
	protected void checkDisponibilitaAccertamentoSubaccertamento() {
		// Empty
	}
}
