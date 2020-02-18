/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento.aggiornamento.entrata;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.documento.ElementoSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;

/**
 * Classe di action per l'aggiornamento del Documento di entrata, sezione Dati Iva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/09/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaDocumentoEntrataBaseAction.FAMILY_NAME)
public class AggiornaDocumentoEntrataDatiIvaAction extends AggiornaDocumentoEntrataBaseAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2216228847988229357L;
	
	/**
	 * Ottiene le quote rilevanti ai fini IVA con tutti i dati necessar&icirc;.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniQuoteRilevantiIva() {
		final String methodName = "ottieniQuoteRilevantiIva";
		// Ottengo le quote
		List<SubdocumentoEntrata> listaQuote = model.getListaSubdocumentoEntrata();
		List<ElementoSubdocumentoIvaEntrata> listaQuoteIva = new ArrayList<ElementoSubdocumentoIvaEntrata>();

		// Inizio con il filtrare le quote rilevanti IVA
		for (SubdocumentoEntrata se : listaQuote) {
			if (Boolean.TRUE.equals(se.getFlagRilevanteIVA())) {
				log.debug(methodName, "Subdocumento " + se.getNumero() + " rilevante IVA");
				listaQuoteIva.add(new ElementoSubdocumentoIvaEntrata(se, model.getAnnoEsercizioInt(), model.isGestioneUEB()));
			}
		}

		Iterator<ElementoSubdocumentoIvaEntrata> i = listaQuoteIva.iterator();

		while (!hasErrori() && i.hasNext()) {
			ElementoSubdocumentoIvaEntrata se = i.next();
			// Popolo i dati del subdocumento rilevante iva
			ottieniDatiCapitoloSubdocumento(se);
		}

		model.setListaQuoteRilevantiIva(listaQuoteIva);
		return SUCCESS;
	}

	/**
	 * Ottiene i dati del capitolo collegato al subdocumento
	 * 
	 * @param se il subdocumento
	 */
	private void ottieniDatiCapitoloSubdocumento(ElementoSubdocumentoIvaEntrata se) {
		final String methodName = "ottieniDatiCapitoloSubdocumento";

		// Se non ho l'accertamento, esco subito
		if (se.getAccertamento() == null) {
			log.debug(methodName, "Subdocumento numero " + se.getNumero() + " con uid " + se.getUid() + " senza accertamento collegato");
			return;
		}

		se.setCapitoloEntrata(se.getAccertamento().getCapitoloEntrataGestione());
		// Ottengo i dati dell'attivita
		ottieniDatiIvaSubdocumento(se);
	}

	/**
	 * Ottiene i dati dell'AttivitaIva.
	 * 
	 * @param se il subdocumento da popolare
	 */
	private void ottieniDatiIvaSubdocumento(ElementoSubdocumentoIvaEntrata se) {
		final String methodName = "ottieniDatiIvaSubdocumento";

		if (se.getSubdocumentoIva() == null) {
			log.debug(methodName, "Nessun subdocumentoIva collegato");
			return;
		}
		// Creo la request per il subdocumentoIva
		RicercaDettaglioSubdocumentoIvaEntrata request = model.creaRequestRicercaDettaglioSubdocumentoIvaEntrata(se.getSubdocumentoIva());
		logServiceRequest(request);
		RicercaDettaglioSubdocumentoIvaEntrataResponse response = documentoIvaEntrataService.ricercaDettaglioSubdocumentoIvaEntrata(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			log.info(methodName, "Errore nel servizio di RicercaDettaglioSubdocumentoIvaEntrata");
			addErrori(response);
			return;
		}

		se.setSubdocumentoIva(response.getSubdocumentoIvaEntrata());
	}

	/**
	 * Regidire verso l'inserimento del subdocumento iva.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String redirezioneInserimentoDocumentoIvaEntrata() {
		final String methodName = "redirezioneInserimentoDocumentoIvaEntrata";
		StringBuilder sb = new StringBuilder();
		sb.append("Redirezione inserimento SubdocumentoIvaEntrata. Uid Documento: ")
			.append(model.getUidDocumentoDaAggiornare());
		if (model.getUidQuota() != null) {
			sb.append(" Uid quota: ")
				.append(model.getUidQuota());
		}

		// Pulisco la sessione
		sessionHandler.setParametro(BilSessionParameter.MODEL_INSERISCI_DOCUMENTO_IVA_ENTRATA, null);

		log.debug(methodName, sb.toString());
		return SUCCESS;
	}

	/**
	 * Regidire verso l'aggiornamento del subdocumento iva.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String redirezioneAggiornamentoDocumentoIvaEntrata() {
		log.debug("redirezioneAggiornamentoDocumentoIvaEntrata",
				"Redirezione aggiornamento SubdocumentoIvaEntrata. Uid subdocumentoIva: " + model.getUidSubdocumentoIva());
		return SUCCESS;
	}

}
