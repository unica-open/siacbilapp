/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capentgest;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloEntrataAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capentgest.ConsultaCapitoloEntrataGestioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloEntrataGestioneResponse;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.wrapper.EntitaConsultabileFilter;

/**
 * Classe di Action per la gestione della consultazione del Capitolo di Entrata Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 02/08/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaCapitoloEntrataGestioneAction extends CapitoloEntrataAction<ConsultaCapitoloEntrataGestioneModel>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1724316301273240379L;
	
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";

		try {
			ricercaDettaglioCapitolo();
			// CR-4324
			// TODO: valutare se caricarla via AJAX
			ricercaDisponibilitaCapitolo();
			// SIAC-5169
			caricaLabelClassificatoriGenerici(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant());
			// SIAC-6193
			gestisciFiltriEntitaConsultabili();
		} catch(WebServiceInvocationFailureException wsife) {
			log.debug(methodName, wsife.getMessage());
			return INPUT;
		}

		/* Imposto il model in sessione */
		sessionHandler.setParametro(BilSessionParameter.MODEL_CONSULTA_CAPITOLO, model);
		
		return SUCCESS;
	}

	/**
	 * Ricerca di dettaglio del capitolo
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void ricercaDettaglioCapitolo() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioCapitolo";
		/* Effettuo la ricerca di dettaglio del Capitolo */
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
		RicercaDettaglioCapitoloEntrataGestione req = model.creaRequestRicercaDettaglioCapitoloEntrataGestione();
		RicercaDettaglioCapitoloEntrataGestioneResponse res = capitoloEntrataGestioneService.ricercaDettaglioCapitoloEntrataGestione(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(methodName, res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		
		log.debug(methodName, "Impostazione dei dati nel model");
		model.impostaDatiDaResponse(res);
		log.debug(methodName, "Dati impostati nel model");
		sessionHandler.setParametro(BilSessionParameter.CAPITOLO_PER_RICERCA_DETTAGLIO_VARIAZIONE, res.getCapitoloEntrataGestione().getUid());
	}
	
	/**
	 * Ricerca della disponibilit&agrave; del capitolo
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void ricercaDisponibilitaCapitolo() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDisponibilitaCapitolo";
		
		RicercaDisponibilitaCapitoloEntrataGestione req = model.creaRequestRicercaDisponibilitaCapitoloEntrataGestione();
		RicercaDisponibilitaCapitoloEntrataGestioneResponse res = capitoloEntrataGestioneService.ricercaDisponibilitaCapitoloEntrataGestione(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		
		log.debug(methodName, "Impostazione delle disponibilita");
		model.setDisponibilitaCapitoloEntrataGestioneAnno0(res.getDisponibilitaCapitoloEntrataGestioneAnno0());
		model.setDisponibilitaCapitoloEntrataGestioneAnno1(res.getDisponibilitaCapitoloEntrataGestioneAnno1());
		model.setDisponibilitaCapitoloEntrataGestioneAnno2(res.getDisponibilitaCapitoloEntrataGestioneAnno2());
		model.setDisponibilitaCapitoloEntrataGestioneResiduo(res.getDisponibilitaCapitoloEntrataGestioneResiduo());
	}
	
	/**
	 * Aggiunta dei filtri pr le entit&agrave; consultabili:
	 * <ul>
	 *     <li>Impegno: COMPETENZA, RESIDUO, PLURIENNALE</li>
	 *     <li>Liquidazione: COMPETENZA, RESIDUO</li>
	 *     <li>Orfinativo: COMPETENZA, RESIDUO</li>
	 * </ul>
	 */
	
	private void gestisciFiltriEntitaConsultabili() {
		// Sempre aggiungere il filtro vuoto
		model.getListaFiltroAccertamento().add(new EntitaConsultabileFilter("", "Tutti", "checked"));
		model.getListaFiltroAccertamento().add(new EntitaConsultabileFilter("C", "Competenza",""));
		model.getListaFiltroAccertamento().add(new EntitaConsultabileFilter("R", "Residuo",""));
		model.getListaFiltroAccertamento().add(new EntitaConsultabileFilter("P", "Pluriennale",""));
		
		model.getListaFiltroOrdinativo().add(new EntitaConsultabileFilter("", "Tutti", "checked"));
		model.getListaFiltroOrdinativo().add(new EntitaConsultabileFilter("C", "Competenza",""));
		model.getListaFiltroOrdinativo().add(new EntitaConsultabileFilter("R", "Residuo",""));
	}
}
