/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.tipoonere;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.tipoonere.ConsultaTipoOnereModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoTipoOnereResponse;
import it.csi.siac.siacfin2ser.model.TipoOnere;

/**
 * Classe di action per la consultazione del tipoOnere.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/11/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaTipoOnereAction extends GenericTipoOnereAction<ConsultaTipoOnereModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 327860530092546955L;

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		final String methodName = "execute";
		if(model.getUidTipoOnere() == null) {
			throw new GenericFrontEndMessagesException(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("chiave tipo onere").getTesto(),
				GenericFrontEndMessagesException.Level.ERROR);
		}
		checkCasoDUsoApplicabile(model.getTitolo());
		
		log.debug(methodName, "Caricamento dettaglio storico per TipoOnere con uid " + model.getUidTipoOnere());
		// Ricerca storico
		DettaglioStoricoTipoOnere request = model.creaRequestDettaglioStoricoTipoOnere();
		logServiceRequest(request);
		DettaglioStoricoTipoOnereResponse response = tipoOnereService.dettaglioStoricoTipoOnere(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			throwExceptionFromErrori(response.getErrori());
		}
		
		// Impostazione liste storico
		impostazioneListeStorico(response);
		checkTipoOnereSplitReverse();
		return SUCCESS;
	}

	/**
	 * Impostazione delle liste dello storico.
	 * 
	 * @param response la response del servizio di dettaglio
	 */
	private void impostazioneListeStorico(DettaglioStoricoTipoOnereResponse response) {
		model.setListaTipoOnere(response.getTipiOnere());
		
		model.setListaCausaleEntrata(response.getCausaliEntrata());
		model.setListaCausaleSpesa(response.getCausaliSpesa());
		model.setListaSoggetto(response.getSoggetti());
		
		// Injetto il primo tipo onere
		for(TipoOnere tipoOnere : response.getTipiOnereStorico()) {
			if(tipoOnere.getDataFineValidita() == null) {
				model.setTipoOnere(tipoOnere);
				break;
			}
		}
	}
	
	/**
	 * Controlla se il tipo di onere sia split-reverse, e in tal caso imposta il valore corretto nel model.
	 */
	private void checkTipoOnereSplitReverse() {
		for(TipoOnere tipoOnere : model.getListaTipoOnere()) {
			// Se ho almeno un onere di tipo split/reverse
			if(tipoOnere != null && tipoOnere.getNaturaOnere() != null && BilConstants.CODICE_NATURA_ONERE_SPLIT_REVERSE.getConstant().equals(tipoOnere.getNaturaOnere().getCodice())) {
				model.setTipoOnereSplitReverse(true);
				return;
			}
		}
	}
	
}
