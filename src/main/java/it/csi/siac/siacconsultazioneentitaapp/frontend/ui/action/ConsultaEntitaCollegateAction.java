/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.action;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.model.ConsultaEntitaCollegateModel;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.ConsultazioneEntitaService;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.OttieniNavigazioneTipoEntitaConsultabile;
import it.csi.siac.siacconsultazioneentitaser.frontend.webservice.msg.OttieniNavigazioneTipoEntitaConsultabileResponse;

/**
 * Classe di Action per la gestione della pagina principale del cruscottino!!
 * @author Elisa Chiari
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaEntitaCollegateAction extends GenericBilancioAction<ConsultaEntitaCollegateModel>  {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1195826871044530353L;
	@Autowired /* @CachedService */
	private transient ConsultazioneEntitaService consultazioneEntitaService;
	
	/** Servizi del provvedimento */
	
	@Autowired protected transient ProvvedimentoService provvedimentoService;
	
	@Override
	public void prepare() throws Exception{
		super.prepare();
		caricaEntitaDiPartenza();
						
	}


	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return SUCCESS;
	}	

	private void caricaEntitaDiPartenza() {
		final String methodName = "caricaEntitaDiPartenza";
		
		OttieniNavigazioneTipoEntitaConsultabile request = model.creaRequestOttieniFigliEntitaConsultabile();
		logServiceRequest(request);
		log.debug(methodName, "Richiamo il webservice di ricerca navizazione");
		OttieniNavigazioneTipoEntitaConsultabileResponse response = consultazioneEntitaService.ottieniNavigazioneTipoEntitaConsultabile(request); //ricercaEntitaConsultabiliFiglieFakeImpl(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione del servizio OttieniFigliEntitaConsultabileAjaxAction");
			addErrori(response);
		}
		
		model.setEntitaConsultabili(response.getEntitaConsultabili());
	}
	
	
}
