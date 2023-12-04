/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capitolo.helper;


import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.TipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

public class CapitoloEntrataGestioneActionHelper extends CapitoloGestioneActionHelper {

	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	@Autowired protected transient TipoComponenteImportiCapitoloService tipoComponenteImportiCapitoloService;

	public CapitoloEntrataGestioneActionHelper(GenericBilancioAction<? extends GenericBilancioModel> action) {
		super(action);
	}
	
	public void findCapitoloEntrataGestione(Capitolo<? extends ImportiCapitolo, ? extends ImportiCapitolo> capitolo) {		
		final String methodName = "checkCapitolo";

		if (capitolo == null || capitolo.getUid() > 0) {
			return;
		}

		if (capitolo.getAnnoCapitolo() == null || capitolo.getNumeroCapitolo() == null || capitolo.getNumeroArticolo() == null) {
			return;
		}
		
		action.checkCondition(capitolo.getAnnoCapitolo() != 0 && capitolo.getNumeroCapitolo() != 0 && capitolo.getNumeroArticolo() != null, 
			ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno capitolo, capitolo o articolo"), true);

		RicercaSinteticaCapitoloEntrataGestione request = creaRequestRicercaSinteticaCapitoloEntrataGestione(capitolo);
		action.logServiceRequest(request);
		
		RicercaSinteticaCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService.ricercaSinteticaCapitoloEntrataGestione(request);
		action.logServiceResponse(response);
		
		if(response.hasErrori()) {
			log.info(methodName, action.createErrorInServiceInvocationString(RicercaSinteticaCapitoloEntrataGestione.class, response));
			action.addErrori(response);
			throw new ParamValidationException("Error found in checkCapitoloEntrataGestione");
		}
		
		List<CapitoloEntrataGestione> list = response.getCapitoli();
		action.checkCondition(!list.isEmpty(), ErroreCore.NESSUN_DATO_REPERITO.getErrore(), true);
		action.checkUnicoCapitolo(response.getCapitoli(), true);

		capitolo.setUid(list.get(0).getUid());
	}
	
	public RicercaSinteticaCapitoloEntrataGestione
		creaRequestRicercaSinteticaCapitoloEntrataGestione(Capitolo<? extends ImportiCapitolo, ? extends ImportiCapitolo> capitolo) {
		
		RicercaSinteticaCapitoloEntrataGestione request = action.getModel().creaRequest(RicercaSinteticaCapitoloEntrataGestione.class);
		request.setEnte(action.getModel().getEnte());
		
		RicercaSinteticaCapitoloEGest ricercaSinteticaCapitoloEGest = new RicercaSinteticaCapitoloEGest();
		ricercaSinteticaCapitoloEGest.setAnnoCapitolo(capitolo.getAnnoCapitolo());
		ricercaSinteticaCapitoloEGest.setNumeroCapitolo(capitolo.getNumeroCapitolo());
		ricercaSinteticaCapitoloEGest.setNumeroArticolo(capitolo.getNumeroArticolo());
		ricercaSinteticaCapitoloEGest.setAnnoEsercizio(action.getModel().getAnnoEsercizioInt());
		
		ricercaSinteticaCapitoloEGest.setStatoOperativo(StatoOperativoElementoDiBilancio.VALIDO);
		
		request.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		request.setRicercaSinteticaCapitoloEntrata(ricercaSinteticaCapitoloEGest);
		request.setCalcolaTotaleImporti(Boolean.TRUE);
		request.setParametriPaginazione(new ParametriPaginazione());
		request.getParametriPaginazione().setElementiPerPagina(1);
		request.getParametriPaginazione().setNumeroPagina(0);		
		
		return request;
			
	}
}
