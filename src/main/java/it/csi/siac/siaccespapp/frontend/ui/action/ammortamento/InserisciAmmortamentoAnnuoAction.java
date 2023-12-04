/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ammortamento;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.ammortamento.InserisciAmmortamentoAnnuoModel;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciAnteprimaAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.InserisciPrimeNoteAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteResponse;
import it.csi.siac.siaccespser.model.DettaglioAnteprimaAmmortamentoAnnuoCespite;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * The Class InserisciAmmortamentoMassivoAction.
 * @author elisa
 * @version 1.0.0 - 21-09-2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciAmmortamentoAnnuoAction extends GenericBilancioAction<InserisciAmmortamentoAnnuoModel> {
	
	/**Per la serializzazione*/
	private static final long serialVersionUID = -4640100795259949583L;

	@Autowired private transient CespiteService cespiteService;
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * Validate inserisci anteprima ammortamento annuo.
	 */
	public void validateInserisciAnteprimaAmmortamentoAnnuo() {
		checkAnno();
	}

	/**
	 * 
	 */
	private void checkAnno() {
		checkCondition(model.getAnnoAmmortamentoAnnuo() != null, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("anno per ammortamento annuo"));
	}
	
	/**
	 * Inserisci anteprima ammortamento annuo.
	 *
	 * @return the string
	 */
	public String inserisciAnteprimaAmmortamentoAnnuo() {
		InserisciAnteprimaAmmortamentoAnnuoCespite req = model.creaRequestInserisciAnteprimaAmmortamentoAnnuoCespite();
		InserisciAnteprimaAmmortamentoAnnuoCespiteResponse res = cespiteService.inserisciAnteprimaAmmortamentoAnnuoCespite(req);
		if(res.hasErrori()) {
			addErrori(res);
			return INPUT;
		}
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
		
	
	/**
	 * Validate effettua ricerca.
	 */
	public void validateRicercaDettagliAnteprimaAmmortamentoCespite() {
		checkAnno();
	}
	
	/**
	 * Effettua ricerca.
	 *
	 * @return the string
	 */
	public String ricercaDettagliAnteprimaAmmortamentoCespite(){
		final String methodName = "effettuaRicerca";
		RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite req = model.creaRequestRicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite();
		RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteResponse res = cespiteService.ricercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite(req);
		if(res.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(res);
			return INPUT;
		}
		log.debug(methodName, "Ricerca effettuata con successo");

		// Imposto in sessione i dati
		impostaParametriInSessione(req, res);
		
		return SUCCESS;
	}
	
	private void impostaParametriInSessione(RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite req,	RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteResponse res) {
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_ANTEPRIMA_AMMORTAMENTO, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_ANTEPRIMA_AMMORTAMENTO, res.getListaDettaglioAnteprimaAmmortamentoAnnuoCespite());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
	}

	
	
	/**
	 * Validate effettua ammortamento selezionati.
	 */
	public void validateEffettuaScrittureAmmortamento() {
		checkAnno();
	}
	
	/**
	 * Effettua ammortamento selezionati.
	 *
	 * @return the string
	 */
	public String effettuaScrittureAmmortamento() {
		InserisciPrimeNoteAmmortamentoAnnuoCespite req = model.creaRequestInserisciPrimeNoteAmmortamentoAnnuoCespite();
//		AzioneRichiesta azioneRichiesta = AzioniConsentite.AMMORTAMENTO_MASSIVO_INSERISCI.creaAzioneRichiesta(sessionHandler.getAzioniConsentite());
//		AsyncServiceResponse response = cespiteService.inserisciAmmortamentoMassivoCespiteAsync(wrapRequestToAsync(req, azioneRichiesta));
		InserisciPrimeNoteAmmortamentoAnnuoCespiteResponse response = cespiteService.inserisciPrimeNoteAmmortamentoAnnuoCespite(req);
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
//		addInformazione(ErroreCore.ELABORAZIONE_ASINCRONA_AVVIATA.getErrore("Ammortamento annuo", ""));
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Validate carca anteprima precedente.
	 */
	public void validateCaricaAnteprimaPrecedente() {
		checkAnno();
	}
	
	/**
	 * Carica anteprima precedente.
	 *
	 * @return the string
	 */
	public String caricaAnteprimaPrecedente() {
		final String methodName = "caricaAnteprimaPrecedente";
		RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite req = model.creaRequestRicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespitePerAnteprima();
		RicercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespiteResponse res = cespiteService.ricercaSinteticaDettaglioAnteprimaAmmortamentoAnnuoCespite(req);
		if(res.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(res);
			return INPUT;
		}
		ListaPaginata<DettaglioAnteprimaAmmortamentoAnnuoCespite> lista = res.getListaDettaglioAnteprimaAmmortamentoAnnuoCespite();
		if(lista != null && !lista.isEmpty()) {
			model.setAnteprimaAmmortamentoAnnuoCespite(lista.get(0).getAnteprimaAmmortamentoAnnuoCespite());
		}
		
		return SUCCESS;
	}
	
	/**
	 * Validate dettaglio cespiti.
	 */
	public void validateDettaglioCespiti() {
		// TODO
	}
	
	/**
	 * Dettaglio cespiti.
	 *
	 * @return the string
	 */
	public String dettaglioCespiti() {
		return SUCCESS;
	}
}
