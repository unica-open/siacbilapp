/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documentoiva;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacfin2app.frontend.ui.model.documentoiva.ConsultaDocumentoIvaEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaEntrataResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;

/**
 * Classe di action per la consultazione del Doc Iva di Entrata.
 *
 * @author Domenico
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class ConsultaDocumentoIvaEntrataAction extends GenericDocumentoIvaEntrataAction<ConsultaDocumentoIvaEntrataModel> {
	
	/**  Per la serializzazione. */
	private static final long serialVersionUID = -3410995502977493216L;
	
	/** The documento spesa service. */
	@Autowired private transient DocumentoEntrataService documentoEntrataService;

	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		super.prepare();
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		
		// Ricerca di dettaglio del PreDocumento
		RicercaDettaglioSubdocumentoIvaEntrata request = model.creaRequestRicercaDettaglioSubdocumentoIvaEntrata();
		logServiceRequest(request);
		RicercaDettaglioSubdocumentoIvaEntrataResponse response = documentoIvaEntrataService.ricercaDettaglioSubdocumentoIvaEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.error(methodName, "Errore nell'invocazione del servizio di ricerca dettaglio doc iva entrata");
			addErrori(response);
			throwExceptionFromErrori(response.getErrori());
		}
		
		log.debug(methodName, "SubdocumentoIva caricato. Imposto i dati nel model");
		model.setSubdocumentoIva(response.getSubdocumentoIvaEntrata());
		
		if(model.getSubdocumentoIva().getDocumentoCollegato()!=null){
			model.setUidDocumentoCollegato(model.getSubdocumentoIva().getDocumentoCollegato().getUid());
		} else {
			model.setUidDocumentoCollegato(model.getSubdocumentoIva().getSubdocumentoIvaPadre().getDocumentoCollegato().getUid());
		}
		log.debug(methodName, "Uid DocumentoEntrata collegato: " + model.getUidDocumentoCollegato());
			
		caricaDettaglioDocumentoEntrata();
		
		model.setSoggetto(model.getDocumento().getSoggetto());
		if(model.getSoggetto()!=null){
			caricaSoggetto();
		}
		
		return SUCCESS;
	}
	
	/**
	 * Carica dettaglio documento spesa.
	 */
	private void caricaDettaglioDocumentoEntrata() {
		final String methodName = "caricaDettaglioDocumentoEntrata";
		
		RicercaDettaglioDocumentoEntrata requestRDD = model.creaRequestRicercaDettaglioDocumentoEntrata(model.getUidDocumentoCollegato());
		
		logServiceRequest(requestRDD);
		RicercaDettaglioDocumentoEntrataResponse responseRDD = documentoEntrataService.ricercaDettaglioDocumentoEntrata(requestRDD);
		logServiceResponse(responseRDD);
		
		if(responseRDD.hasErrori()) {
			log.error(methodName, "Errore nell'invocazione del servizio di ricerca dettaglio doc entrata");
			addErrori(responseRDD);
			throwExceptionFromErrori(responseRDD.getErrori());
		}
		
		model.setDocumento(responseRDD.getDocumento());
		
	}
	
	/**
	 * Ottiene il dettaglio della quota iva differita.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniDettaglioQuotaIvDifferita(){
		
		for(SubdocumentoIvaEntrata sis : model.getSubdocumentoIva().getListaQuoteIvaDifferita()){
			if(sis.getUid() == model.getUidQuotaIvaDifferita()){
				model.setQuotaIvaDifferita(sis);
				return SUCCESS;
			}
		}
		return SUCCESS;
	}
	
}
