/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ammortamento;

import java.util.ArrayList;
import java.util.List;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.ammortamento.RicercaCespiteAnteprimaAmmortamentoModel;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.ClassificazioneCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class RicercaTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaCespiteAnteprimaAmmortamentoAction extends GenericBilancioAction<RicercaCespiteAnteprimaAmmortamentoModel> {
	
	/**Per la serializzazione*/
	private static final long serialVersionUID = 8894411622272829916L;
	
	
	@Autowired private CespiteService cespiteService;
	@Autowired private ClassificazioneCespiteService classificazioneCespiteService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListe();
	}

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		model.setCategoriaCespiti(null);
		model.setTipoBeneCespite(null);
		return SUCCESS;
	}
	
	/**
	 * Validate effettua ricerca.
	 */
	public void validateEffettuaRicerca() {
		// TODO
	}
	
	/**
	 * Effettua ricerca.
	 *
	 * @return the string
	 */
	public String effettuaRicerca(){
		final String methodName = "effettuaRicerca";
		RicercaSinteticaCespite req = model.creaRequestRicercaSinteticaCespite();
		RicercaSinteticaCespiteResponse res = cespiteService.ricercaSinteticaCespite(req);
		if(res.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(res);
			return INPUT;
		}
		
		if(res.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");

		// Imposto in sessione i dati
		impostaParametriInSessione(req, res);
		
		return SUCCESS;
	}
	
	/**
	 * Impostazione dei parametri in sessione
	 * @param request  la request da impostare
	 * @param response la response con i parametri da impostare
	 */
	private void impostaParametriInSessione(RicercaSinteticaCespite request, RicercaSinteticaCespiteResponse response) {
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CESPITE, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CESPITE, response.getListaCespite());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
	}
	
	/**
	 * Carica liste.
	 */
	private void caricaListe() {
		model.setListaTipoBene(ottieniListaTipoBene());
		model.setListaCategoriaCespite(ottieniCategoriaCespiti());
	}
	
	/**
	 * Ottieni categoria cespiti.
	 *
	 * @return the list
	 */
	private List<CategoriaCespiti> ottieniCategoriaCespiti() {
		RicercaSinteticaCategoriaCespiti req = model.creaRequestRicercaSinteticaCategoriaCespiti();
		RicercaSinteticaCategoriaCespitiResponse res = classificazioneCespiteService.ricercaSinteticaCategoriaCespiti(req);
		if(res.hasErrori()){
			addErrori(res);
			return new ArrayList<CategoriaCespiti>();
		}
		return res.getListaCategoriaCespiti();
	}

	/**
	 * Carica lista tipo bene filtrato.
	 *
	 * @return the string
	 */
	public String caricaListaTipoBeneFiltrato() {
		List<TipoBeneCespite> listaTipoBeneCespite = ottieniListaTipoBene();
		if(model.getCategoriaCespiti() == null || model.getCategoriaCespiti().getUid() == 0) {
			model.setListaTipoBeneFiltrata(listaTipoBeneCespite);
			return SUCCESS;
		}
		List<TipoBeneCespite> listaTipoBeneCespiteFiltrati = new ArrayList<TipoBeneCespite>();
		for (TipoBeneCespite tbc : listaTipoBeneCespite) {
			if(tbc.getCategoriaCespiti() != null && tbc.getCategoriaCespiti().getUid() == model.getCategoriaCespiti().getUid()) {
				listaTipoBeneCespiteFiltrati.add(tbc);
			}
		}
		model.setListaTipoBeneFiltrata(listaTipoBeneCespiteFiltrati);
		return SUCCESS;
	}
	
	private List<TipoBeneCespite> ottieniListaTipoBene() {
		final String methodName="ottieniListaTipoBene";
		List<TipoBeneCespite> listaTipoBene = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_BENE_CESPITE);
		
		if(listaTipoBene == null || listaTipoBene.isEmpty()) {
			RicercaSinteticaTipoBeneCespite req = model.creaRequestRicercaSinteticaTipoBene();
			RicercaSinteticaTipoBeneCespiteResponse res = classificazioneCespiteService.ricercaSinteticaTipoBeneCespite(req);
			if(res.hasErrori()) {
				log.info(methodName, "Fallimento nella chiamata al servizio");
				addErrori(res);
				return new ArrayList<TipoBeneCespite>();
			}
			if(res.getTotaleElementi() == 0) {
				log.debug(methodName, "Nessun tipo bene cespite");
				addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
				return new ArrayList<TipoBeneCespite>();
			}
			listaTipoBene = res.getListaTipoBeneCespite();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_BENE_CESPITE,listaTipoBene);
		}
		return listaTipoBene;
	}
	
}
