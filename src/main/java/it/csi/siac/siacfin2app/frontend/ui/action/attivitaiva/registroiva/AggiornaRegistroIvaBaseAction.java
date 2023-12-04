/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.registroiva;

import xyz.timedrain.arianna.plugin.BreadCrumb;

import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.registroiva.AggiornaRegistroIvaBaseModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioRegistroIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioRegistroIvaResponse;
import it.csi.siac.siacfin2ser.model.RegistroIva;

/**
 * Classe di action per l'aggiornamento del Registro Iva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 29/05/2014
 * @param <M> la tipizzazione del model
 *
 */
public class AggiornaRegistroIvaBaseAction<M extends AggiornaRegistroIvaBaseModel> extends GenericRegistroIvaAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2153699020319289795L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListaTipoRegistro();
		caricaListaGruppoAttivitaIva();
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		// Controllo se il CDU sia applicabile
		checkCasoDUsoApplicabile(model.getTitolo());
		
		RicercaDettaglioRegistroIva request = model.creaRequestRicercaDettaglioRegistroIva();
		logServiceRequest(request);
		RicercaDettaglioRegistroIvaResponse response = registroIvaService.ricercaDettaglioRegistroIva(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione del servizio RicercaDettaglioRegistroIva");
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Ricercato gruppo con uid " + model.getUidRegistroIva());
		model.impostaDati(response.getRegistroIva());
		
		return SUCCESS;
	}
	

	/**
	 * Validazione di base
	 */
	protected void baseValidazione() {
		RegistroIva ri = model.getRegistroIva();
		
		checkNotNullNorEmpty(ri.getCodice(), "Codice");
		checkNotNullNorEmpty(ri.getDescrizione(), "Descrizione");
		
		// Il gruppo deve essere selezionato
		checkNotNullNorInvalidUid(model.getGruppoAttivitaIva(), "Gruppo Attivita Iva");
		// Il tipo registro deve essere indicato
		checkNotNull(model.getTipoRegistroIva(), "Tipo Registro Iva");
	}
	
}
