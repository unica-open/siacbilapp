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
import it.csi.siac.siacfin2app.frontend.ui.model.documentoiva.ConsultaDocumentoIvaSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioSubdocumentoIvaSpesaResponse;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Classe di action per la consultazione del Doc iva di Spesa.
 *
 * @author Domenico
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class ConsultaDocumentoIvaSpesaAction extends GenericDocumentoIvaSpesaAction<ConsultaDocumentoIvaSpesaModel> {
	
	/**  Per la serializzazione. */
	private static final long serialVersionUID = -3410995502977493215L;
	
	/** The documento spesa service. */
	@Autowired private transient DocumentoSpesaService documentoSpesaService;
	
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
		RicercaDettaglioSubdocumentoIvaSpesa request = model.creaRequestRicercaDettaglioSubdocumentoIvaSpesa();
		logServiceRequest(request);
		RicercaDettaglioSubdocumentoIvaSpesaResponse response = documentoIvaSpesaService.ricercaDettaglioSubdocumentoIvaSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.error(methodName, "Errore nell'invocazione del servizio di ricerca dettaglio preDocumento spesa");
			addErrori(response);
			throwExceptionFromErrori(response.getErrori());
		}
		
		log.debug(methodName, "SubdocumentoIva caricato. Imposto i dati nel model");
		model.setSubdocumentoIva(response.getSubdocumentoIvaSpesa());
		
		if(model.getSubdocumentoIva().getDocumentoCollegato()!=null){
			model.setUidDocumentoCollegato(model.getSubdocumentoIva().getDocumentoCollegato().getUid());
		} else {
			model.setUidQuotaDocumentoCollegato(model.getSubdocumentoIva().getSubdocumentoIvaPadre().getUid());
			model.setUidDocumentoCollegato(model.getSubdocumentoIva().getSubdocumentoIvaPadre().getDocumentoCollegato().getUid());
		}
		log.debug(methodName, "Uid DocumentoSpesa collegato: " + model.getUidDocumentoCollegato());
		
		caricaDettaglioDocumentoSpesa();
		
		model.setSoggetto(model.getDocumento().getSoggetto());
		if(model.getSoggetto()!=null){
			caricaSoggetto();
		}
		
		return SUCCESS;
	}

	/**
	 * Carica dettaglio documento spesa.
	 */
	private void caricaDettaglioDocumentoSpesa() {
		final String methodName = "caricaDettaglioDocumentoSpesa";
		
		RicercaDettaglioDocumentoSpesa requestRDD = model.creaRequestRicercaDettaglioDocumentoSpesa(model.getUidDocumentoCollegato());
		
		logServiceRequest(requestRDD);
		RicercaDettaglioDocumentoSpesaResponse responseRDD = documentoSpesaService.ricercaDettaglioDocumentoSpesa(requestRDD);
		logServiceResponse(responseRDD);
		
		if(responseRDD.hasErrori()) {
			log.error(methodName, "Errore nell'invocazione del servizio di ricerca dettaglio doc spesa");
			addErrori(responseRDD);
			throwExceptionFromErrori(responseRDD.getErrori());
		}
		
		model.setDocumento(responseRDD.getDocumento());
		
		if(model.getUidQuotaDocumentoCollegato() != null && model.getUidQuotaDocumentoCollegato() != 0){
			caricaDettaglioQuota(model.getUidQuotaDocumentoCollegato());
		}
		
	}

	private void caricaDettaglioQuota(Integer uidQuotaDocumentoCollegato) {
		for(SubdocumentoSpesa ss : model.getDocumento().getListaSubdocumenti()){
			if(ss.getUid() == uidQuotaDocumentoCollegato){
				model.setSubdocumento(ss);
				break;
			}
		}
	}
	
	/**
	 * Ottiene il dettaglio della quota iva differita.
	 * 
	 * @return il dettaglio della quota iva differita
	 */
	public String ottieniDettaglioQuotaIvDifferita(){
		
		for(SubdocumentoIvaSpesa sis : model.getSubdocumentoIva().getListaQuoteIvaDifferita()){
			if(sis.getUid() == model.getUidQuotaIvaDifferita()){
				model.setQuotaIvaDifferita(sis);
				return SUCCESS;
			}
		}
		return SUCCESS;
	}

	
}
