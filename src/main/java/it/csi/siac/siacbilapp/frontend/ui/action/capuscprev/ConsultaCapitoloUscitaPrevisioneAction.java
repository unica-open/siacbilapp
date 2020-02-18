/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capuscprev;

import java.util.ArrayList;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloUscitaAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscprev.ConsultaCapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.business.utility.helper.ComponenteImportiCapitoloPerAnnoHelper;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;

/**
 * Classe di Action per la gestione della consultazione del Capitolo di Uscita
 * Previsione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 18/07/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaCapitoloUscitaPrevisioneAction
		extends CapitoloUscitaAction<ConsultaCapitoloUscitaPrevisioneModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6272778532865093503L;

	@Autowired
	private transient CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;
	@Autowired
	private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		try {
			ricercaDettaglioCapitolo();
			// SIAC-5169
			caricaLabelClassificatoriGenerici(BilConstants.CODICE_CAPITOLO_USCITA_PREVISIONE.getConstant());
		} catch (WebServiceInvocationFailureException wsife) {
			log.debug(methodName, wsife.getMessage());
			return INPUT;
		}

		/* Imposto il model in sessione */
		sessionHandler.setParametro(BilSessionParameter.MODEL_CONSULTA_CAPITOLO, model);

		return SUCCESS;
	}

	/**
	 * Ricerca del dettaglio del capitolo
	 * 
	 * @throws WebServiceInvocationFailureException
	 *             in caso di eccezione
	 */
	private void ricercaDettaglioCapitolo() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioCapitolo";
		/* Effettuo la ricerca di dettaglio del Capitolo */
		RicercaDettaglioCapitoloUscitaPrevisione req = model.creaRequestRicercaDettaglioCapitoloUscitaPrevisione();
		logServiceRequest(req);
		RicercaDettaglioCapitoloUscitaPrevisioneResponse res = capitoloUscitaPrevisioneService
				.ricercaDettaglioCapitoloUscitaPrevisione(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if (res.hasErrori()) {
			// si sono verificati degli errori: esco.
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		
		sessionHandler.setParametro(BilSessionParameter.CAPITOLO_PER_RICERCA_DETTAGLIO_VARIAZIONE, res.getCapitoloUscitaPrevisione().getUid());

		// GESC014 Servizio per ricerca componenti del capitolo
		RicercaComponenteImportiCapitolo reqComponenti = model.creaRequestRicercaComponenteImportiCapitolo();
		RicercaComponenteImportiCapitoloResponse resComponenti = componenteImportiCapitoloService
				.ricercaComponenteImportiCapitolo(reqComponenti);

		// Controllo gli errori
		if (resComponenti.hasErrori()) {
			// si sono verificati degli errori: esco.
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(reqComponenti, resComponenti));
		}

		log.debug(methodName, "Impostazione dei dati nel model");
		List<ImportiCapitoloPerComponente> importiComponentiCapitolo = new ArrayList<ImportiCapitoloPerComponente>();
		
		//FIX BUG 7132
//		for(int i=0; i<resComponenti.getListaImportiCapitolo().size(); i++){
//			resComponenti.getListaImportiCapitolo().get(i).setResiduoEffettivo(new BigDecimal(1991));
//			resComponenti.getListaImportiCapitolo().get(i).setPagatoCassaIniziale(new BigDecimal(1991));
//			resComponenti.getListaImportiCapitolo().get(i).setStanziamentoCassa(new BigDecimal(1991));
//			
//		}
		
		
		
		
		
		importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper
				.toComponentiImportiCapitoloPerAnno(resComponenti.getListaImportiCapitolo());
		
		//SIAC-7227
		//passo la nuova lista al model con il match delle varie componenti
//		if(resComponenti.getListaImportiCapitolo().get(0) != null) {
//			importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnnoPrecedente(importiComponentiCapitolo, resComponenti.getListaImportiCapitolo().get(0));
//		}
		//
		
		model.setImportiComponentiCapitolo(importiComponentiCapitolo);
		model.setImportiAnniSuccessivi(resComponenti.getImportiCapitoloAnniSuccessivi());
		model.setImportiResidui(resComponenti.getImportiCapitoloResiduo());

		model.impostaDatiDaResponse(res);

		// COMPETENZA
		List<ImportiCapitoloPerComponente> competenzaComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildCompetenzaRowUP(resComponenti.getListaImportiCapitolo(),
				resComponenti.getImportiCapitoloResiduo(), resComponenti.getImportiCapitoloAnniSuccessivi(),
				competenzaComponenti);

		model.setCompetenzaStanziamento((competenzaComponenti.get(0) != null) ? competenzaComponenti.get(0) : null);
		model.setCompetenzaImpegnato((competenzaComponenti.get(1) != null) ? competenzaComponenti.get(1) : null);
		model.setDisponibilita((competenzaComponenti.get(2) != null) ? competenzaComponenti.get(2) : null);

		/// model.setCompetenza
		// RESIDUI
		List<ImportiCapitoloPerComponente> residuiComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildResiduiRow(resComponenti.getListaImportiCapitolo(),
				resComponenti.getImportiCapitoloResiduo(), resComponenti.getImportiCapitoloAnniSuccessivi(), residuiComponenti);

		model.setResiduiPresunti((residuiComponenti.get(0) != null) ? residuiComponenti.get(0) : null);
		model.setResiduiEffettivi((residuiComponenti.get(1) != null) ? residuiComponenti.get(1) : null);

		// CASSA
		List<ImportiCapitoloPerComponente> cassaComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildCassaRow(resComponenti.getListaImportiCapitolo(),
				resComponenti.getImportiCapitoloResiduo(), resComponenti.getImportiCapitoloAnniSuccessivi(), cassaComponenti);

		model.setCassaStanziato((cassaComponenti.get(0) != null) ? cassaComponenti.get(0) : null);
		model.setCassaPagato((cassaComponenti.get(1) != null) ? cassaComponenti.get(1) : null);

		boolean isPrevisione = checkCaso();
		// chiamata al servizio per sapere in quale fase di bilancio mi trovo
		model.setCapGestioneEquivalentePresente(isPrevisione);

		// se non mi trovo in previsione e il capitolo ha un equivalente
		// capitolo di gestione...quindi uidEquivalente diverso da 0..effettuo
		// la chiamata
		if (!isPrevisione && res.getCapitoloUscitaPrevisione().getUidCapitoloEquivalente() != 0) {
			model.setCapGestioneEquivalentePresente(true);
			// Ricerco il dettaglio del capitolo di gestione equivalente,
			// utilizzando come uid quello equivalente che proviene dalla
			// response res
			RicercaDettaglioCapitoloUscitaGestione reqEq = model
					.creaRequestRicercaDettaglioCapitoloUscitaGestioneEquivalente(
							res.getCapitoloUscitaPrevisione().getUidCapitoloEquivalente());
			RicercaDettaglioCapitoloUscitaGestioneResponse resEq = capitoloUscitaGestioneService
					.ricercaDettaglioCapitoloUscitaGestione(reqEq);
			if (resEq.hasErrori()) {
				// si sono verificati degli errori: esco.
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(reqEq, resEq));
			}
			model.impostaImportiCapitoloEquivalente(resEq.getListaImportiCapitoloUG());

			// stessa cosa, viene fatta per le componenti
			RicercaComponenteImportiCapitolo requestEquiv = model
					.creaRequestRicercaComponenteImportiCapitoloEquivalente(
							res.getCapitoloUscitaPrevisione().getUidCapitoloEquivalente());
			RicercaComponenteImportiCapitoloResponse responseEquiv = componenteImportiCapitoloService
					.ricercaComponenteImportiCapitolo(requestEquiv);
			if (responseEquiv.hasErrori()) {
				// si sono verificati degli errori: esco.
				throw new WebServiceInvocationFailureException(
						createErrorInServiceInvocationString(requestEquiv, responseEquiv));
			}
			List<ImportiCapitoloPerComponente> importiComponentiCapitoloEquiv = new ArrayList<ImportiCapitoloPerComponente>();
			importiComponentiCapitoloEquiv = ComponenteImportiCapitoloPerAnnoHelper
					.toComponentiImportiCapitoloPerAnno(responseEquiv.getListaImportiCapitolo());
			model.setImportiComponentiCapitoloEquiv(importiComponentiCapitoloEquiv);
			model.setImportiAnniSuccessiviEquiv(responseEquiv.getImportiCapitoloAnniSuccessivi());
			model.setImportiResiduiEquiv(responseEquiv.getImportiCapitoloResiduo());

			// NUOVA GESTIONE
			// COMPETENZA
			List<ImportiCapitoloPerComponente> competenzaComponentiEquiv = new ArrayList<ImportiCapitoloPerComponente>();
			ComponenteImportiCapitoloPerAnnoHelper.buildCompetenzaRowUG(responseEquiv.getListaImportiCapitolo(),
					responseEquiv.getImportiCapitoloResiduo(), responseEquiv.getImportiCapitoloAnniSuccessivi(),
					competenzaComponentiEquiv);

			model.setCompetenzaStanziamentoEq((competenzaComponentiEquiv.get(0) != null) ? competenzaComponentiEquiv.get(0) : null);
			model.setCompetenzaImpegnatoEq((competenzaComponentiEquiv.get(1) != null) ? competenzaComponentiEquiv.get(1) : null);
			model.setDisponibilitaEq((competenzaComponentiEquiv.get(2) != null) ? competenzaComponentiEquiv.get(2) : null);

			/// model.setCompetenza
			// RESIDUI
			List<ImportiCapitoloPerComponente> residuiComponentiEquiv = new ArrayList<ImportiCapitoloPerComponente>();
			ComponenteImportiCapitoloPerAnnoHelper.buildResiduiRow(responseEquiv.getListaImportiCapitolo(),
					responseEquiv.getImportiCapitoloResiduo(), responseEquiv.getImportiCapitoloAnniSuccessivi(),
					residuiComponentiEquiv);

			model.setResiduiPresuntiEq((residuiComponentiEquiv.get(0) != null) ? residuiComponentiEquiv.get(0) : null);
			model.setResiduiEffettiviEq((residuiComponentiEquiv.get(1) != null) ? residuiComponentiEquiv.get(1) : null);

			// model.setResidui

			// CASSA
			List<ImportiCapitoloPerComponente> cassaComponentiEquiv = new ArrayList<ImportiCapitoloPerComponente>();
			ComponenteImportiCapitoloPerAnnoHelper.buildCassaRow(responseEquiv.getListaImportiCapitolo(),
					responseEquiv.getImportiCapitoloResiduo(), responseEquiv.getImportiCapitoloAnniSuccessivi(), cassaComponentiEquiv);

			model.setCassaStanziatoEq((cassaComponentiEquiv.get(0) != null) ? cassaComponentiEquiv.get(0) : null);
			model.setCassaPagatoEq((cassaComponentiEquiv.get(1) != null) ? cassaComponentiEquiv.get(1) : null);

		} else {// altrimenti setto il flag Capitolo Non Presente
			model.setCapGestioneEquivalentePresente(false);
			//FIXME elimina righe di codice inutile... TODO
			model.setImportiEx(new ImportiCapitoloUG());
			model.setImportiEx1(new ImportiCapitoloUG());
			model.setImportiEx2(new ImportiCapitoloUG());
			model.setImportiEx3(new ImportiCapitoloUG());

		}
		model.setImportiEx(new ImportiCapitoloUG());

		log.debug(methodName, "Dati impostati nel model");
	}

	// Funzione che mi restituisce la fase di bilancio in cui mi trovo
	private boolean checkCaso() throws WebServiceInvocationFailureException {
		RicercaDettaglioBilancio req = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse res = bilancioService.ricercaDettaglioBilancio(req);
		FaseBilancio faseBilancio = res.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioCompatibile = FaseBilancio.PREVISIONE.equals(faseBilancio);
		if (res.hasErrori()) {
			// si sono verificati degli errori: esco.
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		return faseDiBilancioCompatibile;
	}

}
