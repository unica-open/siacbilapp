/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.stampa;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.stampa.StampaLiquidazioneIvaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaLiquidazioneIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.StampaLiquidazioneIvaResponse;
import it.csi.siac.siacfin2ser.model.TipoStampa;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;

/**
 * Classe di action per la stampa della liquidazione iva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/07/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class StampaLiquidazioneIvaAction extends GenericStampaIvaAction<StampaLiquidazioneIvaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 290636867681512487L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricamentoListe();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		checkCasoDUsoApplicabile(model.getTitolo());
		
		caricaValoriDefault();
		return super.execute();
	}
	
	@Override
	public String effettuaStampa() {
		final String methodName = "effettuaStampa";
		StampaLiquidazioneIva request = model.creaRequestStampaLiquidazioneIva();
		logServiceRequest(request);
		StampaLiquidazioneIvaResponse response = stampaIvaService.stampaLiquidazioneIva(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nell'invocazione del servizio StampaLiquidazioneIva");
			addErrori(response);
			addErrore(ErroreFin.IMPOSSIBILE_ELABORARE_LA_STAMPA_DELLA_LIQUIDAZIONE_IVA.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Invocazione del servizio StampaLiquidazioneIva avvenuta con successo");
		impostaMessaggioStampaPresaInCarico();
		return SUCCESS;
	}
	
	@Override
	public void validateEffettuaStampa() {
		// Tento di ricaricare la select dei periodi
		caricaListaPeriodo();
		
		TipoStampa ts = model.getTipoStampa();
		
		// Validazione logica
		checkNotNullNorInvalidUid(model.getGruppoAttivitaIva(), "Gruppo attivita' iva");
		checkNotNull(model.getTipoChiusura(), "Tipo di chiusura");
		checkNotNull(model.getPeriodo(), "Periodo");
		checkNotNull(model.getIvaACredito(), "Iva a credito");
		checkCondition(model.getIvaACredito() == null || model.getIvaACredito().signum() <= 0,
			ErroreCore.FORMATO_NON_VALIDO.getErrore("Iva a credito", ": non deve essere positivo"));
		checkNotNull(ts, "Tipo stampa");
		
		// Carico il messaggio di elaborazione non possibile
		checkCondition(!hasErrori(), ErroreFin.IMPOSSIBILE_ELABORARE_LA_STAMPA_DELLA_LIQUIDAZIONE_IVA.getErrore());
	}
	
	/**
	 * Carica le varie liste da servizio.
	 */
	private void caricamentoListe() {
		caricaListaGruppoAttivitaIva();
		caricaListaTipoChiusura();
		caricaListaTipoStampa();
	}
	
	/**
	 * Caricamento dei valori di default.
	 */
	private void caricaValoriDefault() {
		model.setTipoStampa(TipoStampa.BOZZA);
	}
	
}
