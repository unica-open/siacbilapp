/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento.aggiornamento.spesa;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Classe di action per l'aggiornamento del Documento di spesa, sezione Dati Iva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/09/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaDocumentoSpesaBaseAction.FAMILY_NAME)
public class AggiornaDocumentoSpesaDatiIvaAction extends AggiornaDocumentoSpesaBaseAction {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -170470974726706278L;

	/**
	 * Ottiene le quote rilevanti ai fini IVA con tutti i dati necessar&icirc;.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniQuoteRilevantiIva() {
		final String methodName = "ottieniQuoteRilevantiIva";
		// Ottengo le quote
		List<SubdocumentoSpesa> listaQuote = model.getListaSubdocumentoSpesa();
		List<ElementoSubdocumentoIvaSpesa> listaQuoteIva = new ArrayList<ElementoSubdocumentoIvaSpesa>();
		
		// Inizio con il filtrare le quote rilevanti IVA
		for(SubdocumentoSpesa ss : listaQuote) {
			if(Boolean.TRUE.equals(ss.getFlagRilevanteIVA())) {
				//il subdocumento e' iva
				log.debug(methodName, "Subdocumento " + ss.getNumero() + " rilevante IVA");
				listaQuoteIva.add(new ElementoSubdocumentoIvaSpesa(ss, model.getAnnoEsercizioInt(), model.isGestioneUEB()));
			}
		}
		
		Iterator<ElementoSubdocumentoIvaSpesa> i = listaQuoteIva.iterator();
		
		while(!hasErrori() && i.hasNext()) {
			ElementoSubdocumentoIvaSpesa ss = i.next();
			// Popolo i dati del subdocumento rilevante iva
			ottieniDatiCapitoloSubdocumento(ss);
		}
		
		model.setListaQuoteRilevantiIva(listaQuoteIva);
		return SUCCESS;
	}
	
	/**
	 * Ottiene i dati del capitolo collegato al subdocumento
	 * 
	 * @param ss il subdocumento
	 */
	private void ottieniDatiCapitoloSubdocumento(ElementoSubdocumentoIvaSpesa ss) {
		final String methodName = "ottieniDatiCapitoloSubdocumento";
		
		// Se non ho l'impegno, esco subito
		if(ss.getImpegno() == null) {
			//non ho l'impegno, esco
			log.debug(methodName, "Subdocumento numero " + ss.getNumero() + " con uid " + ss.getUid() + " senza impegno collegato");
			return;
		}
		
		//il capitolo del subdocumento e' quello dellì'impegno
		ss.setCapitoloUscita(ss.getImpegno().getCapitoloUscitaGestione());
		// Ottengo i dati dell'attivita
		ottieniDatiIvaSubdocumento(ss);
	}

	/**
	 * Ottiene i dati dell'AttivitaIva.
	 * 
	 * @param ss il subdocumento da popolare
	 */
	private void ottieniDatiIvaSubdocumento(ElementoSubdocumentoIvaSpesa ss) {
		final String methodName = "ottieniDatiIvaSubdocumento";
		
		if(ss.getSubdocumentoIva() == null) {
			//non ho un subdocumento iva, esco
			log.debug(methodName, "Nessun subdocumentoIva collegato");
			return;
		}
		// Creo la request per il subdocumentoIva
		RicercaDettaglioSubdocumentoIvaSpesa request = model.creaRequestRicercaDettaglioSubdocumentoIvaSpesa(ss.getSubdocumentoIva());
		logServiceRequest(request);
		//chiamo il servizio per ottenere i dati iva del sub
		RicercaDettaglioSubdocumentoIvaSpesaResponse response = documentoIvaSpesaService.ricercaDettaglioSubdocumentoIvaSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nel servizio di RicercaDettaglioSubdocumentoIvaSpesa");
			addErrori(response);
			return;
		}
		//imposto nel model i dati del subbdocumento iva ottenuti dal servizio
		SubdocumentoIvaSpesa sis = response.getSubdocumentoIvaSpesa();
		ss.setSubdocumentoIva(sis);
		model.setUidSubdocumentoIva(sis.getUid());
	}
	
	/**
	 * Regidire verso l'inserimento del subdocumento iva.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String redirezioneInserimentoDocumentoIvaSpesa() {
		final String methodName = "redirezioneInserimentoDocumentoIvaSpesa";
		// Loggo la redirezione
		StringBuilder sb = new StringBuilder();
		sb.append("Redirezione inserimento SubdocumentoIvaSpesa. Uid Documento: ")
			.append(model.getUidDocumentoDaAggiornare());
		if(model.getUidQuota() != null) {
			sb.append(" Uid quota: ")
				.append(model.getUidQuota());
		}
		// Pulisco la sessione
		sessionHandler.setParametro(BilSessionParameter.MODEL_INSERISCI_DOCUMENTO_IVA_SPESA, null);
		
		log.debug(methodName, sb.toString());
		return SUCCESS;
	}
	
	/**
	 * Regidire verso l'aggiornamento del subdocumento iva.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String redirezioneAggiornamentoDocumentoIvaSpesa() {
		//metodo per utilizzare la redirezione (gestita da struts)
		log.debug("redirezioneAggiornamentoDocumentoIvaSpesa", "Redirezione aggiornamento SubdocumentoIvaSpesa. Uid subdocumentoIva: " + model.getUidSubdocumentoIva());
		return SUCCESS;
	}

}
