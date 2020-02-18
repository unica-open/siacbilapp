/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.allegatoatto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.allegatoatto.DettaglioElencoDocumentiAllegatoModel;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegato;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegatoEntrata;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegatoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.AllegatoAttoService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElencoResponse;
import it.csi.siac.siacfin2ser.model.DatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di Action per l'ottenimento del dettaglio dell'ElencoDocumentiAllegato.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/set/2014
 * @version 1.0.1 - 21/ott/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class DettaglioElencoDocumentiAllegatoAction extends GenericBilancioAction<DettaglioElencoDocumentiAllegatoModel>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1983715183787345140L;
	
	@Autowired private transient AllegatoAttoService allegatoAttoService;
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		RicercaSinteticaQuoteElenco request = model.creaRequestRicercaSinteticaQuoteElenco();
		logServiceRequest(request);
		RicercaSinteticaQuoteElencoResponse response = allegatoAttoService.ricercaSinteticaQuoteElenco(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return SUCCESS;
		}
		
		ElencoDocumentiAllegato eda = response.getElencoDocumentiAllegato();
		eda.setSubdocumenti(response.getSubdocumenti());
		
		// Ho l'elenco. Lo spezzo e lo wrappo
		List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaWrapper = ottieniWrapperPerElenco(eda);
		model.setListaElementoElencoDocumentiAllegato(listaWrapper);
		
		log.debug(methodName, "Totale: "+response.getTotaleElementi());
		
		
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_DETTAGLIO_QUOTE_ELENCO, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_DETTAGLIO_QUOTE_ELENCO, response.getSubdocumenti());
		sessionHandler.setParametro(BilSessionParameter.ELENCO_DOCUMENTI_ALLEGATO_LIGHT, response.getElencoDocumentiAllegato());
		return SUCCESS;
	}


	/**
	 * Calcola il wrapper per l'elenco indicato.
	 * 
	 * @param eda l'elenco da wrappare
	 * @return il wrapper
	 */
	private List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> ottieniWrapperPerElenco(ElencoDocumentiAllegato eda) {
		List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> result = new ArrayList<ElementoElencoDocumentiAllegato<?,?,?,?,?>>();
		List<DatiSoggettoAllegato> listaDatiSoggettoAllegato = sessionHandler.getParametro(BilSessionParameter.LISTA_DATI_SOGGETTO_ALLEGATO_ALLEGATO_ATTO);
		
		if(listaDatiSoggettoAllegato == null) {
			// Inizializzo la lista come vuota
			listaDatiSoggettoAllegato = new ArrayList<DatiSoggettoAllegato>();
		}
		
		List<Subdocumento<?, ?>> subdocumenti = eda.getSubdocumenti();
		for(Subdocumento<?, ?> sub : subdocumenti) {
			Documento<?, ?> doc = sub.getDocumento();
			if(doc == null || doc.getSoggetto() == null) {
				// Non ho i dati del documento. Strano. Continuo
				continue;
			}
			DatiSoggettoAllegato dsa = ottieniDatiSoggettoAllegatoViaSoggetto(listaDatiSoggettoAllegato, doc.getSoggetto());
			ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?> eeda = null;
			if(sub instanceof SubdocumentoSpesa) {
				eeda = new ElementoElencoDocumentiAllegatoSpesa(eda, dsa, model.isGestioneUEB(), (SubdocumentoSpesa) sub);
			} else if (sub instanceof SubdocumentoEntrata) {
				eeda = new ElementoElencoDocumentiAllegatoEntrata(eda, dsa, model.isGestioneUEB(), (SubdocumentoEntrata) sub);
			}
			result.add(eeda);
		}
		return result;
	}

	/**
	 * Ottiene i datiSoggettoAllegato a partire dal soggetto corrispondente.
	 * 
	 * @param listaDatiSoggettoAllegato la lista dei dati
	 * @param soggetto                  il soggetto da ricercare
	 * 
	 * @return i dati relativi al soggetto, se presenti
	 */
	private DatiSoggettoAllegato ottieniDatiSoggettoAllegatoViaSoggetto(List<DatiSoggettoAllegato> listaDatiSoggettoAllegato, Soggetto soggetto) {
		for (DatiSoggettoAllegato dsa : listaDatiSoggettoAllegato) {
			if (soggetto.getUid() != 0 && dsa.getSoggetto() != null && soggetto.getUid() == dsa.getSoggetto().getUid()) {
				return dsa;
			}
		}
		return null;
	}
	
	/**
	 * chiama il servizio per ottenere le quote di un elenco filtrate.
	 *
	 * @return the string
	 */
	public String caricaQuoteElencoFiltrate() {
		final String methodName = "caricaQuoteElencoFiltrate";
		RicercaSinteticaQuoteElenco request = model.creaRequestRicercaSinteticaQuoteElenco();
		logServiceRequest(request);
		RicercaSinteticaQuoteElencoResponse response = allegatoAttoService.ricercaSinteticaQuoteElenco(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return SUCCESS;
		}
		
		ElencoDocumentiAllegato eda = response.getElencoDocumentiAllegato();
		eda.setSubdocumenti(response.getSubdocumenti());
		
		// Ho l'elenco. Lo spezzo e lo wrappo
		List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaWrapper = ottieniWrapperPerElenco(eda);
		model.setListaElementoElencoDocumentiAllegatoFiltrato(listaWrapper);
		
		log.debug(methodName, "Totale: "+response.getTotaleElementi());
		
		
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_DETTAGLIO_QUOTE_ELENCO_FILTRATE, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_DETTAGLIO_QUOTE_ELENCO_FILTRATE, response.getSubdocumenti());
		sessionHandler.setParametro(BilSessionParameter.ELENCO_DOCUMENTI_ALLEGATO_LIGHT, response.getElencoDocumentiAllegato());
		return SUCCESS;
	}
	
}
