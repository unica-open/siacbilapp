/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.gruppoattivita;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.gruppoattivita.GenericGruppoAttivitaIvaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.GruppoAttivitaIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.TipoAttivita;
import it.csi.siac.siacfin2ser.model.TipoChiusura;

/**
 * Classe di action generica per il Gruppo Attivita Iva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 29/05/2014
 * 
 * @param <M> la tipizzazione del model, estendente {@link GenericGruppoAttivitaIvaModel}
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GenericGruppoAttivitaIvaAction<M extends GenericGruppoAttivitaIvaModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4652076665759277367L;
	
	/** Serviz&icirc; del gruppo attivit&agrave; iva */
	@Autowired protected transient GruppoAttivitaIvaService gruppoAttivitaIvaService;
	@Autowired private transient DocumentoIvaService documentoIvaService;

	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		// Il Bilancio non deve essere in fase Pluriennale, Previsione né chiuso
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
				FaseBilancio.PREVISIONE.equals(faseBilancio) ||
				FaseBilancio.CHIUSO.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
	/**
	 * Carica la lista dei tipi di chiusura nel Model.
	 */
	protected void caricaListaTipoChiusura() {
		model.setListaTipoChiusura(Arrays.asList(TipoChiusura.values()));
	}
	
	/**
	 * Carica la lista dei tipi di attivita nel Model.
	 */
	protected void caricaListaTipoAttivita() {
		model.setListaTipoAttivita(Arrays.asList(TipoAttivita.values()));
	}
	
	/**
	 * Carica la lista delle attivita iva.
	 */
	protected void caricaListaAttivitaIva() {
		final String methodName = "caricaListaAttivitaIva";
		
		List<AttivitaIva> listaAttivitaIva = sessionHandler.getParametro(BilSessionParameter.LISTA_ATTIVITA_IVA_COMPLETE);
		
		if(listaAttivitaIva == null) {
			log.debug(methodName, "Caricamento da servizio");
			RicercaAttivitaIva request = model.creaRequestRicercaAttivitaIva();
			logServiceRequest(request);
			RicercaAttivitaIvaResponse response = documentoIvaService.ricercaAttivitaIva(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				// TODO: lanciare un'eccezione
				log.debug(methodName, createErrorInServiceInvocationString(RicercaAttivitaIva.class, response));
				return;
			}
			log.debug(methodName, "Caricate attivitaIva dal servizio");
			listaAttivitaIva = response.getListaAttivitaIva();
			// Imposto in sessione
			sessionHandler.setParametro(BilSessionParameter.LISTA_ATTIVITA_IVA_COMPLETE, listaAttivitaIva);
		}
		
		model.setListaAttivitaIva(listaAttivitaIva);
	}
	
}
