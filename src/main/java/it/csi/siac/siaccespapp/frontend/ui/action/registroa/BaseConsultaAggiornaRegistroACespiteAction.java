/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.registroa;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.registroa.BaseConsultaAggiornaRegistroACespiteModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.cespite.ElementoCespite;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.PrimaNotaCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoDettaglioRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoDettaglioRegistroACespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoEPRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoEPRegistroACespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * Action per la consultazione del registro A(prime note verso inventario contabile)
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/10/2018
 * @param <M> la tipizzazione del model
 */
public abstract class BaseConsultaAggiornaRegistroACespiteAction<M extends BaseConsultaAggiornaRegistroACespiteModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2478362497314167578L;
	
	/** Il servizio per la prima nota cespite */
	@Autowired protected PrimaNotaCespiteService primaNotaCespiteService;
	@Autowired private CespiteService cespiteService;
	
	/**
	 * Carica movimenti EP.
	 *
	 * @return true, if successful
	 */
	protected boolean caricaMovimentiEP() {
		boolean movimentiCaricatiCorrettamente = false;
		RicercaSinteticaMovimentoEPRegistroACespite req = model.creaRicercaSinteticaMovimentoEPRegistroACespite();
		RicercaSinteticaMovimentoEPRegistroACespiteResponse response = primaNotaCespiteService.ricercaSinteticaMovimentoEPRegistroACespite(req);
		if(response.hasErrori()) {
			addErrori(response);
			return movimentiCaricatiCorrettamente;
		}
		if(response.getTotaleElementi() == 0) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("consultazione non possibile", "nessuna prima nota trovata"));
			return movimentiCaricatiCorrettamente;
		}
		movimentiCaricatiCorrettamente = true;
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_MOVIMENTI_EP_REGISTRO_A_CESPITE, response.getMovimentiEP());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_MOVIMENTI_EP_REGISTRO_A_CESPITE, req);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		return movimentiCaricatiCorrettamente;
	}
	
	/**
	 * Caricamento del movimento di dettaglio
	 * @return se il caricamento &eacute; avvenuto correttamente
	 */
	protected boolean caricaMovimentiDettaglio() {
		boolean movimentiCaricatiCorrettamente = false;
		RicercaSinteticaMovimentoDettaglioRegistroACespite req = model.creaRicercaSinteticaMovimentoDettaglioRegistroACespite();
		RicercaSinteticaMovimentoDettaglioRegistroACespiteResponse response = primaNotaCespiteService.ricercaSinteticaMovimentoDettaglioRegistroACespite(req);
		if(response.hasErrori()) {
			addErrori(response);
			return movimentiCaricatiCorrettamente;
		}
		if(response.getTotaleElementi() == 0) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("consultazione non possibile", "nessuna prima nota trovata"));
			return movimentiCaricatiCorrettamente;
		}
		movimentiCaricatiCorrettamente = true;
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_MOVIMENTI_DETTAGLIO_REGISTRO_A_CESPITE, response.getMovimentiDettaglio());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_MOVIMENTI_DETTAGLIO_REGISTRO_A_CESPITE, req);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		model.setTipoCausale(extractTipoCausale(response.getMovimentiDettaglio()));
		return movimentiCaricatiCorrettamente;
	}
	
	/**
	 * Extract tipo causale.
	 *
	 * @param movimentoDettaglio the movimento dettaglio
	 * @return the tipo causale
	 */
	private TipoCausale extractTipoCausale(List<MovimentoDettaglio> movimentoDettaglioLista) {
		MovimentoDettaglio movimentoDettaglio = movimentoDettaglioLista != null && !movimentoDettaglioLista.isEmpty()?  movimentoDettaglioLista.get(0) : null;
		if(movimentoDettaglio == null || movimentoDettaglio.getMovimentoEP() == null || movimentoDettaglio.getMovimentoEP().getPrimaNota() == null) {
			return null;
		}
		return movimentoDettaglio.getMovimentoEP().getPrimaNota().getTipoCausale();
	}
	
	/**
	 * Ottieni tabella cespiti.
	 *
	 * @return the string
	 */
	public abstract String ottieniTabellaCespiti();
	
	/**
	 * Ottieni lista cespiti collegati.
	 *
	 * @return the string
	 */
	public String ottieniListaCespitiCollegati() {
		RicercaSinteticaCespite req = model.creaRequestRicercaSinteticaCespite();
		RicercaSinteticaCespiteResponse responseRicerca = cespiteService.ricercaSinteticaCespite(req);
		if(responseRicerca.hasErrori()) {
			addErrori(responseRicerca);
			return INPUT;
		}
		impostaNelModelCespitiTrovati(responseRicerca.getListaCespite());
		return SUCCESS;
	}
	
	/**
	 * Imposta nel model cespiti trovati.
	 *
	 * @param listaCespite the lista cespite
	 */
	private void impostaNelModelCespitiTrovati(ListaPaginata<Cespite> listaCespite) {
		model.setListaCespitiCollegatiAMovimentoEP(new ArrayList<ElementoCespite>());
		if(listaCespite == null || listaCespite.isEmpty()) {
			return;
		}
		for (Cespite cespite : listaCespite) {
			ElementoCespite e = new ElementoCespite(cespite);
			model.getListaCespitiCollegatiAMovimentoEP().add(e);
		}
	}
}
