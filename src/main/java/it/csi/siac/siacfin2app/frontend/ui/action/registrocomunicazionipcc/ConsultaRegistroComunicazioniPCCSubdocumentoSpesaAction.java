/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.registrocomunicazionipcc;

import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.registrocomunicazionipcc.ConsultaRegistroComunicazioniPCCSubdocumentoSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.RegistroComunicazioniPCCService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaRegistroComunicazioniPCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaRegistroComunicazioniPCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisciRegistroComunicazioniPCCResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistriComunicazioniPCCSubdocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaRegistriComunicazioniPCCSubdocumentoResponse;
import it.csi.siac.siacfin2ser.model.CausalePCC;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;
import it.csi.siac.siacfin2ser.model.StatoDebito;
import it.csi.siac.siacfin2ser.model.TipoOperazionePCC;

/**
 * Classe di action per la consultazione del registro comunicazioni PCC per il subdocumento di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 19/giu/2015
 * 
 * @author Elisa Chiari
 * @version 1.0.1 - 20/11/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class ConsultaRegistroComunicazioniPCCSubdocumentoSpesaAction extends GenericBilancioAction<ConsultaRegistroComunicazioniPCCSubdocumentoSpesaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -499547844230395383L;
	
	@Autowired private transient CodificheService codificheService;
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	@Autowired private transient RegistroComunicazioniPCCService registroComunicazioniPCCService;
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName = "execute";
		leggiEventualiErroriAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiInformazioniAzionePrecedente();
		
		// Caricamento dati
		try {
			caricaListe();
			caricaRegistri();
			caricaDocumento();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nell'invocazione dei servizi di caricamento: " + wsife.getMessage());
			throw new GenericFrontEndMessagesException(wsife.getMessage());
		}
		return SUCCESS;
	}
	
	/**
	 * Caricamento delle liste.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazine del servizio
	 */
	private void caricaListe() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListe";
		
		RicercaCodifiche request = model.creaRequestRicercaCodifiche(TipoOperazionePCC.class, StatoDebito.class);
		logServiceRequest(request);
		RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		
		log.debug(methodName, "Caricamento codifiche per TipoOperazionePCC, StatoDebito, CausalePCC completata");
		model.setListaStatoDebito(response.getCodifiche(StatoDebito.class));
		model.setListaTipoOperazionePCC(response.getCodifiche(TipoOperazionePCC.class));
		
		sessionHandler.setParametro(BilSessionParameter.LISTA_STATO_DEBITO_PCC, response.getCodifiche(StatoDebito.class));
		impostaTipoOperazioneContabilizzazione();
	}

	/**
	 * Impostazione del tipo di operazione pari a <code>CO - CONTABILIZZAZIONE</code>.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di assenza del tipo di operazione tra quelle caricate dal servizio
	 */
	private void impostaTipoOperazioneContabilizzazione() throws WebServiceInvocationFailureException {
		final String methodName = "impostaTipoOperazioneContabilizzazione";
		for(TipoOperazionePCC tipoOperazionePCC : model.getListaTipoOperazionePCC()) {
			if(tipoOperazionePCC != null && BilConstants.TIPO_OPERAZIONE_PCC_CONTABILIZZAZIONE.getConstant().equals(tipoOperazionePCC.getCodice())) {
				// Trovata: imposto ed esco
				log.debug(methodName, "Trovata codifica con uid " + tipoOperazionePCC.getUid() + " corrispondente al codice " + tipoOperazionePCC.getCodice());
				model.setTipoOperazionePCC(tipoOperazionePCC);
				return;
			}
		}
		// Non ho trovato alcunche'
		throw new WebServiceInvocationFailureException("Nessuna codifica corrispondente al codice " + BilConstants.TIPO_OPERAZIONE_PCC_CONTABILIZZAZIONE.getConstant() + " reperita dal servizio");
	}

	/**
	 * Caricamento dei registri.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazine del servizio
	 */
	private void caricaRegistri() throws WebServiceInvocationFailureException {
		final String methodName = "caricaRegistri";
		
		RicercaRegistriComunicazioniPCCSubdocumento request = model.creaRequestRicercaRegistriComunicazioniPCCSubdocumento();
		logServiceRequest(request);
		RicercaRegistriComunicazioniPCCSubdocumentoResponse response = registroComunicazioniPCCService.ricercaRegistriComunicazioniPCCSubdocumento(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		
		log.debug(methodName, "Registri collegati al subdocumento " + model.getSubdocumentoSpesa().getUid() + " caricati: presenti #" + response.getCardinalitaComplessiva());
		model.setListaRegistroComunicazioniPCC(response.getRegistriComunicazioniPCC());
	}
	
	/**
	 * Caricamento del documento.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazine del servizio
	 */
	private void caricaDocumento() throws WebServiceInvocationFailureException {
		final String methodName = "caricaDocumento";
		
		RicercaDettaglioQuotaSpesa request = model.creaRequestRicercaDettaglioQuotaSpesa();
		logServiceRequest(request);
		RicercaDettaglioQuotaSpesaResponse response = documentoSpesaService.ricercaDettaglioQuotaSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
		}
		
		log.debug(methodName, "Ottenuto dettaglio subdocumento " + model.getSubdocumentoSpesa().getUid());
		DocumentoSpesa documentoSpesa = response.getSubdocumentoSpesa().getDocumento();
		log.debug(methodName, "Ottenuto dettaglio documento " + (documentoSpesa != null ? documentoSpesa.getUid() : "null")
				+ " - Stato: " + (documentoSpesa != null ? documentoSpesa.getStatoOperativoDocumento() : "null"));
		model.setDocumentoSpesa(documentoSpesa);
	}

	/**
	 * Ingresso nella pagina.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enter() {
		// Segnaposto per permettere il refresh
		return SUCCESS;
	}
	
	/**
	 * Ottiene la lista delle contabilizzazioni.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaContabilizzazioni() {
		// Segnaposto
		return SUCCESS;
	}
	
	/**
	 * Ottiene la lista delle causali associate ad un dato stato del debito
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	
	public String ottieniCausaliPCCByStatoDebito () {
		final String methodName = "ottieniCausaliPCCByStatoDebito ";
		List<StatoDebito> listaStatoDebitoPCC = sessionHandler.getParametro(BilSessionParameter.LISTA_STATO_DEBITO_PCC);
		if(listaStatoDebitoPCC == null) {
			// Caricamento da servizio
			log.debug(methodName, "Lista Stato debito non presente in sessione");
			RicercaCodifiche request = model.creaRequestRicercaCodifiche(StatoDebito.class);
			logServiceRequest(request);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.debug(methodName, createErrorInServiceInvocationString(request, response));
				addErrori(response);
				return INPUT;
			}
			
			log.debug(methodName, "Caricamento codifiche per StatoDebito completata");
			listaStatoDebitoPCC = response.getCodifiche(StatoDebito.class);
			sessionHandler.setParametro(BilSessionParameter.LISTA_STATO_DEBITO_PCC, response.getCodifiche(StatoDebito.class));
		}
		
		StatoDebito statoDebitoPCC = ComparatorUtils.searchByUid(listaStatoDebitoPCC, model.getStatoDebito());
		if(statoDebitoPCC == null) {
			log.debug(methodName, "Nessuno stato debito corrispondente all'uid " + model.getStatoDebito().getUid());
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Stato Debito", "per uid " + model.getStatoDebito().getUid()));
			return INPUT;
		}
		
		model.setListaCausalePCC(statoDebitoPCC.getCausaliPCC());
		log.debug(methodName, "Ottenuta lista causali collegate allo stato del debito " + statoDebitoPCC.getUid() + ": " + statoDebitoPCC.getCodice());
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #inserisciContabilizzazione()}.
	 */
	public void prepareInserisciContabilizzazione() {
		model.setRegistroComunicazioniPCC(null);
	}
	
	/**
	 * Inserimento della contabilizzazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciContabilizzazione() {
		final String methodName = "inserisciContabilizzazione";
		
		InserisciRegistroComunicazioniPCC request = model.creaRequestInserisciRegistroComunicazioniPCC();
		logServiceRequest(request);
		InserisciRegistroComunicazioniPCCResponse response = registroComunicazioniPCCService.inserisciRegistroComunicazioniPCC(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		RegistroComunicazioniPCC registroComunicazioniPCC = response.getRegistroComunicazioniPCC();
		log.debug(methodName, "Inserita contabilizzazione con uid " + registroComunicazioniPCC.getUid());
		
		// Popolamento codifiche
		popolaCodificheRegistro(registroComunicazioniPCC);
		
		// Aggiungo al model
		model.getListaRegistroComunicazioniPCC().add(0, registroComunicazioniPCC);
		return SUCCESS;
	}
	
	/**
	 * Ripopolamento delle codifiche del registro.
	 * 
	 * @param registroComunicazioniPCC il registro da ripopolare
	 */
	private void popolaCodificheRegistro(RegistroComunicazioniPCC registroComunicazioniPCC) {
		// Dettaglio TipoOperazionePCC
		TipoOperazionePCC tipoOperazionePCC = ComparatorUtils.searchByUid(model.getListaTipoOperazionePCC(), registroComunicazioniPCC.getTipoOperazionePCC());
		registroComunicazioniPCC.setTipoOperazionePCC(tipoOperazionePCC);

		// Dettaglio StatoDebito
		StatoDebito statoDebito = ComparatorUtils.searchByUid(model.getListaStatoDebito(), registroComunicazioniPCC.getStatoDebito());
		registroComunicazioniPCC.setStatoDebito(statoDebito);
		
		// Dettaglio CausalePCC
		CausalePCC causalePCC = ComparatorUtils.searchByUid(model.getListaCausalePCC(), registroComunicazioniPCC.getCausalePCC());
		registroComunicazioniPCC.setCausalePCC(causalePCC);
	}

	/**
	 * Validazione per il metodo {@link #inserisciContabilizzazione()}.
	 */
	public void validateInserisciContabilizzazione() {
		checkNotNull(model.getRegistroComunicazioniPCC(), "Contabilizzazione");
	}
	
	/**
	 * Preparazione per il metodo {@link #aggiornaContabilizzazione()}.
	 */
	public void prepareAggiornaContabilizzazione() {
		model.setRegistroComunicazioniPCC(null);
		model.setRiga(null);
	}
	
	/**
	 * Aggiornamento della contabilizzazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaContabilizzazione() {
		final String methodName = "aggiornaContabilizzazione";
		
		AggiornaRegistroComunicazioniPCC request = model.creaRequestAggiornaRegistroComunicazioniPCC();
		logServiceRequest(request);
		AggiornaRegistroComunicazioniPCCResponse response = registroComunicazioniPCCService.aggiornaRegistroComunicazioniPCC(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		RegistroComunicazioniPCC registroComunicazioniPCC = response.getRegistroComunicazioniPCC();
		log.debug(methodName, "Aggiornata contabilizzazione con uid " + response.getRegistroComunicazioniPCC().getUid());
		
		// Popolamento codifiche
		popolaCodificheRegistro(registroComunicazioniPCC);
		
		// Aggiorno sul model
		int riga = model.getRiga().intValue();
		model.getListaRegistroComunicazioniPCC().set(riga, response.getRegistroComunicazioniPCC());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #aggiornaContabilizzazione()}.
	 */
	public void validateAggiornaContabilizzazione() {
		checkNotNullNorInvalidUid(model.getRegistroComunicazioniPCC(), "Contabilizzazione");
		checkNotNull(model.getRiga(), "Riga", true);
		
		int riga = model.getRiga().intValue();
		int numeroRegistri = model.getListaRegistroComunicazioniPCC().size();
		checkCondition(riga > -1 && riga < numeroRegistri, ErroreCore.FORMATO_NON_VALIDO.getErrore("riga da aggiornare", ": deve essere compresa tra 0 e " + numeroRegistri));
	}
	
	/**
	 * Preparazione per il metodo {@link #eliminaContabilizzazione()}.
	 */
	public void prepareEliminaContabilizzazione() {
		model.setRiga(null);
	}
	
	/**
	 * Eliminazione della contabilizzazione.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaContabilizzazione() {
		final String methodName = "eliminaContabilizzazione";
		
		EliminaRegistroComunicazioniPCC request = model.creaRequestEliminaRegistroComunicazioniPCC();
		logServiceRequest(request);
		EliminaRegistroComunicazioniPCCResponse response = registroComunicazioniPCCService.eliminaRegistroComunicazioniPCC(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Eliminata contabilizzazione con uid " + response.getRegistroComunicazioniPCC().getUid());
		
		// Tolgo al model
		int riga = model.getRiga().intValue();
		model.getListaRegistroComunicazioniPCC().remove(riga);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #eliminaContabilizzazione()}.
	 */
	public void validateEliminaContabilizzazione() {
		checkNotNullNorInvalidUid(model.getRegistroComunicazioniPCC(), "Contabilizzazione");
		checkNotNull(model.getRiga(), "Riga", true);
		
		int riga = model.getRiga().intValue();
		int numeroRegistri = model.getListaRegistroComunicazioniPCC().size();
		checkCondition(riga > -1 && riga < numeroRegistri, ErroreCore.FORMATO_NON_VALIDO.getErrore("riga da eliminare", ": deve essere compresa tra 0 e " + numeroRegistri));
	}
}
