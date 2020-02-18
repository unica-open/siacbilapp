/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.cespite;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.cespite.RicercaCespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class RicercaTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaCespiteAction extends GenericCespiteAction<RicercaCespiteModel> {
	/**Per la serializzazione*/
	private static final long serialVersionUID = 8150064731899870323L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListe(false);
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		model.setCespite(null);
		return SUCCESS;
	}
	
	/**
	 * Validate effettua ricerca.
	 */
	public void validateEffettuaRicerca() {
		Cespite cespite = model.getCespite();
		if(cespite == null) {
			cespite = new Cespite();
			model.setCespite(cespite);
		}
		
		boolean indicatoUnCriterioDiRicerca =
			StringUtils.isNotBlank(cespite.getCodice())
			|| StringUtils.isNotBlank(cespite.getDescrizione())
			|| idEntitaPresente(cespite.getTipoBeneCespite())
			|| cespite.getClassificazioneGiuridicaCespite() != null
			
			// SIAC-6375
//			|| cespite.getFlagSoggettoTutelaBeniCulturali() != null
//			|| cespite.getFlgDonazioneRinvenimento() != null
//			|| cespite.getFlagStatoBene() != null
			
			|| cespite.getDataAccessoInventario() != null
			|| cespite.getDataCessazione() != null
			|| cespite.getNumeroInventario() != null
			// SIAC-6374
			|| model.getNumeroInventarioDa() != null
			|| model.getNumeroInventarioA() != null
			// SIAC-6375
			|| StringUtils.isNotBlank(model.getFlagSoggettoTutelaBeniCulturali())
			|| StringUtils.isNotBlank(model.getFlgDonazioneRinvenimento())
			|| StringUtils.isNotBlank(model.getFlagStatoBene())
			// SIAC-6389
			|| StringUtils.isNotBlank(cespite.getUbicazione());

		checkCondition(indicatoUnCriterioDiRicerca, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		checkCondition(model.getNumeroInventarioDa() == null || model.getNumeroInventarioA() == null
				|| model.getNumeroInventarioDa().compareTo(model.getNumeroInventarioA()) <= 0,
				ErroreCore.VALORE_NON_VALIDO.getErrore("numero inventario da/a", "il numero da non puo' essere superiore al numero a"));
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
	 * Effettua ricerca.
	 *
	 * @return the string
	 */
	public String effettuaRicercaModale(){
		return effettuaRicerca();
	}
	/**
	 * Effettua la validazione per la ricerca.
	 */
	public void validateEffettuaRicercaModale() {
		validateEffettuaRicerca();
	}
}
