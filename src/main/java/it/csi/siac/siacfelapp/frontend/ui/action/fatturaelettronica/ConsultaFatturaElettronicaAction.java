/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfelapp.frontend.ui.action.fatturaelettronica;

import java.util.Collections;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacfelapp.frontend.ui.model.fatturaelettronica.ConsultaFatturaElettronicaModel;
import it.csi.siac.siacfelapp.frontend.ui.util.converter.RiepilogoBeniFELComparator;
import it.csi.siac.sirfelser.frontend.webservice.FatturaElettronicaService;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaDettaglioFatturaElettronicaResponse;
import it.csi.siac.sirfelser.model.CausaleFEL;
import it.csi.siac.sirfelser.model.DettaglioPagamentoFEL;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.PagamentoFEL;
/**
 * Classe di action per la consultazione della fattura elettronica.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/06/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaFatturaElettronicaAction extends GenericBilancioAction<ConsultaFatturaElettronicaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7744532981737399698L;
	
	@Autowired private transient FatturaElettronicaService fatturaElettronicaService;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		
		try {
			ricercaDettaglioFatturaElettronica();
		} catch(WebServiceInvocationFailureException wsife) {
			// Ho gia' loggato dove lancio l'errore
			throw new GenericFrontEndMessagesException(wsife.getMessage(), wsife);
		}
		
		scorporamentoDatiFattura();
		
		return SUCCESS;
	}

	/**
	 * Ricerca di dettaglio della fattura elettronica.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void ricercaDettaglioFatturaElettronica() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioFatturaElettronica";
		
		RicercaDettaglioFatturaElettronica request = model.creaRequestRicercaDettaglioFatturaElettronica();
		logServiceRequest(request);
		RicercaDettaglioFatturaElettronicaResponse response = fatturaElettronicaService.ricercaDettaglioFatturaElettronica(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMsg = createErrorInServiceInvocationString(request, response);
			log.info(methodName, errorMsg);
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		
		// Imposto la fattura nel model
		model.setFatturaFEL(response.getFatturaFEL());
	}

	/**
	 * Scorporo dei dati della fattura per la visualizzazione.
	 */
	private void scorporamentoDatiFattura() {
		FatturaFEL fatturaFEL = model.getFatturaFEL();
		
		// Dati da spezzare per ottenerne il primo
		impostaCausaleFEL(fatturaFEL);
		impostaDettaglioPagamentoFEL(fatturaFEL);
		
		// Ordinamenti
		sortElencoDatiIva(fatturaFEL);
	}

	/**
	 * Impostazione della causale della fattura FEL.
	 * 
	 * @param fatturaFEL la fattura da cui ottenere la causale
	 */
	private void impostaCausaleFEL(FatturaFEL fatturaFEL) {
		if(fatturaFEL.getCausaliFEL() != null && !fatturaFEL.getCausaliFEL().isEmpty()) {
			// TODO: controllare
			// Prendo il primo portale
			CausaleFEL causaleFEL = fatturaFEL.getCausaliFEL().get(0);
			model.setCausaleFEL(causaleFEL);
		}
	}
	
	/**
	 * Impostazione del dettaglio pagamento della fattura FEL.
	 * 
	 * @param fatturaFEL la fattura da cui ottenere il portale
	 */
	private void impostaDettaglioPagamentoFEL(FatturaFEL fatturaFEL) {
		if(fatturaFEL.getPagamenti() != null && !fatturaFEL.getPagamenti().isEmpty()) {
			DettaglioPagamentoFEL dettaglioPagamentoFEL = null;
			for(PagamentoFEL pfel : fatturaFEL.getPagamenti()) {
				for(DettaglioPagamentoFEL dpfel : pfel.getElencoDettagliPagamento()) {
					// Prendo il dettaglio con progressivo minimo
					if(dettaglioPagamentoFEL == null || dettaglioPagamentoFEL.getProgressivoDettaglio().compareTo(dpfel.getProgressivoDettaglio()) > 0) {
						dettaglioPagamentoFEL = dpfel;
					}
				}
			}
			model.setDettaglioPagamentoFEL(dettaglioPagamentoFEL);
		}
	}
	
	/**
	 * Ordinamento della lista dei dati iva.
	 * 
	 * @param fatturaFEL la fattura con i dati da ordinare
	 */
	private void sortElencoDatiIva(FatturaFEL fatturaFEL) {
		Collections.sort(fatturaFEL.getRiepiloghiBeni(), RiepilogoBeniFELComparator.INSTANCE);
	}
	
}
