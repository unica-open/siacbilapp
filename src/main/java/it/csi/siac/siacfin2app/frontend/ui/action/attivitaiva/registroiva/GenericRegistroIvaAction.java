/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.attivitaiva.registroiva;

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
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siacfin2app.frontend.ui.model.attivitaiva.registroiva.GenericRegistroIvaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.GruppoAttivitaIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.RegistroIvaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaGruppoAttivitaIva;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaGruppoAttivitaIvaResponse;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;

/**
 * Classe di action generica per il Registro Iva.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 29/05/2014
 * 
 * @param <M> la tipizzazione del model, estendente {@link GenericRegistroIvaModel}
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GenericRegistroIvaAction<M extends GenericRegistroIvaModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4652076665759277367L;
	
	/** Serviz&icirc; del registro iva */
	@Autowired protected transient RegistroIvaService registroIvaService;
	/** Serviz&icirc; del documento iva */
	@Autowired protected transient DocumentoIvaService documentoIvaService;
	/** Serviz&icirc; del gruppo attivit&agrave; iva */
	@Autowired protected transient GruppoAttivitaIvaService gruppoAttivitaIvaService;

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
		
		super.checkCasoDUsoApplicabile(cdu);
	}
	
	/**
	 * Carica la lista dei tipi di registro nel Model.
	 */
	protected void caricaListaTipoRegistro() {
		List<TipoRegistroIva> listaTipoRegistroIva = Arrays.asList(TipoRegistroIva.values());
		model.setListaTipoRegistroIva(listaTipoRegistroIva);
	}
	
	/**
	 * Carica la lista dei gruppi attivita iva nel Model.
	 */
	protected void caricaListaGruppoAttivitaIva() {
		List<GruppoAttivitaIva> listaGruppoAttivitaIva = sessionHandler.getParametro(BilSessionParameter.LISTA_GRUPPO_ATTIVITA_IVA);
		if(listaGruppoAttivitaIva == null) {
			RicercaGruppoAttivitaIva request = model.creaRequestRicercaGruppoAttivitaIva();
			logServiceRequest(request);
			RicercaGruppoAttivitaIvaResponse response = gruppoAttivitaIvaService.ricercaGruppoAttivitaIva(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				return;
			}
			listaGruppoAttivitaIva = response.getListaGruppoAttivitaIva();
		}
		model.setListaGruppoAttivitaIva(listaGruppoAttivitaIva);
	}
	
}