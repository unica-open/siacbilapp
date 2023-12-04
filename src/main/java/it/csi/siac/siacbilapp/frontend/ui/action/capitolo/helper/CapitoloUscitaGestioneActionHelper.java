/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capitolo.helper;


import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.TipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

public class CapitoloUscitaGestioneActionHelper extends CapitoloGestioneActionHelper {
	
	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	@Autowired protected transient TipoComponenteImportiCapitoloService tipoComponenteImportiCapitoloService;

	public CapitoloUscitaGestioneActionHelper(GenericBilancioAction<? extends GenericBilancioModel> action) {
		super(action);
		// TODO Auto-generated constructor stub
	}

	public void findCapitoloUscitaGestione(Capitolo<? extends ImportiCapitolo, ? extends ImportiCapitolo> capitolo) {		
		final String methodName = "checkCapitolo";

		if (capitolo == null || capitolo.getUid() > 0) {
			return;
		}

		if (capitolo.getAnnoCapitolo() == null || capitolo.getNumeroCapitolo() == null || capitolo.getNumeroArticolo() == null) {
			return;
		}
		
		action.checkCondition(capitolo.getAnnoCapitolo() != 0 && capitolo.getNumeroCapitolo() != 0 && capitolo.getNumeroArticolo() != null, 
			ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno capitolo, capitolo o articolo"), true);

		RicercaSinteticaCapitoloUscitaGestione request = creaRequestRicercaSinteticaCapitoloUscitaGestione(capitolo);
		action.logServiceRequest(request);
		
		RicercaSinteticaCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(request);
		action.logServiceResponse(response);
		
		if(response.hasErrori()) {
			log.info(methodName, action.createErrorInServiceInvocationString(RicercaSinteticaCapitoloUscitaGestione.class, response));
			action.addErrori(response);
			throw new ParamValidationException("Error found in checkCapitoloUscitaGestione");
		}
		
		List<CapitoloUscitaGestione> list = response.getCapitoli();
		action.checkCondition(!list.isEmpty(), ErroreCore.NESSUN_DATO_REPERITO.getErrore(), true);
		action.checkUnicoCapitolo(response.getCapitoli(), true);

		capitolo.setUid(list.get(0).getUid());
	}
	
	public RicercaSinteticaCapitoloUscitaGestione 
		creaRequestRicercaSinteticaCapitoloUscitaGestione(Capitolo<? extends ImportiCapitolo, ? extends ImportiCapitolo> capitolo) {
		
		RicercaSinteticaCapitoloUscitaGestione request = action.getModel().creaRequest(RicercaSinteticaCapitoloUscitaGestione.class);
		request.setEnte(action.getModel().getEnte());
		
		RicercaSinteticaCapitoloUGest ricercaSinteticaCapitoloUGest = new RicercaSinteticaCapitoloUGest();
		ricercaSinteticaCapitoloUGest.setAnnoCapitolo(capitolo.getAnnoCapitolo());
		ricercaSinteticaCapitoloUGest.setNumeroCapitolo(capitolo.getNumeroCapitolo());
		ricercaSinteticaCapitoloUGest.setNumeroArticolo(capitolo.getNumeroArticolo());
		ricercaSinteticaCapitoloUGest.setAnnoEsercizio(action.getModel().getAnnoEsercizioInt());
		ricercaSinteticaCapitoloUGest.setStatoOperativo(StatoOperativoElementoDiBilancio.VALIDO);
		
		request.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		request.setRicercaSinteticaCapitoloUGest(ricercaSinteticaCapitoloUGest);
		request.setCalcolaTotaleImporti(Boolean.TRUE);
		request.setParametriPaginazione(new ParametriPaginazione());
		request.getParametriPaginazione().setElementiPerPagina(1);
		request.getParametriPaginazione().setNumeroPagina(0);
		
		return request;
	}
	
	public List<TipoComponenteImportiCapitolo> caricaListaTipoComponentiImportiCapitolo() throws WebServiceInvocationFailureException{
		List<TipoComponenteImportiCapitolo> list = action.getSessionHandler().getParametro(BilSessionParameter.LISTA_COMPONENTE_CAPITOLO);
		
		if(list == null) {
			RicercaSinteticaTipoComponenteImportiCapitolo request = action.getModel().creaRequestRicercaSinteticaTipoComponentiImportoCapitolo();
			action.logServiceRequest(request);
			RicercaSinteticaTipoComponenteImportiCapitoloResponse response = tipoComponenteImportiCapitoloService.ricercaSinteticaTipoComponenteImportiCapitolo(request);
			action.logServiceResponse(response);

			if(response.hasErrori()) {
				action.addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaTipoComponentiImportiCapitolo");
			}
			
			list = response.getListaTipoComponenteImportiCapitolo();
			action.getSessionHandler().setParametro(BilSessionParameter.LISTA_COMPONENTE_CAPITOLO, list);
		}

		return list;
	}
}
