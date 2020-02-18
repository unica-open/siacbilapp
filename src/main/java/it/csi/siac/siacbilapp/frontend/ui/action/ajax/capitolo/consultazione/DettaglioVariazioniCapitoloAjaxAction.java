/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.capitolo.consultazione;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.consultazione.DettaglioVariazioniCapitoloAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.consultazione.ElementoVariazioneImportoSingoloCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaVariazioniSingoloCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaVariazioniSingoloCapitoloResponse;
import it.csi.siac.siacbilser.model.VariazioneImportoSingoloCapitolo;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;

/**
 * Action per i risultati di ricerca del vincolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 02/01/2014
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class DettaglioVariazioniCapitoloAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoVariazioneImportoSingoloCapitolo,
		DettaglioVariazioniCapitoloAjaxModel,  VariazioneImportoSingoloCapitolo, RicercaSinteticaVariazioniSingoloCapitolo, RicercaSinteticaVariazioniSingoloCapitoloResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4918152133385653858L;
	
	private int uidCapitolo;
	
	public int getUidCapitolo() {
		return uidCapitolo;
	}

	public void setUidCapitolo(int uidCapitolo) {
		this.uidCapitolo = uidCapitolo;
	}

	@Autowired private transient CapitoloService capitoloService;
	@Autowired private transient VariazioneDiBilancioService variazioneDiBilancioService;
	
	
	/** Costruttore vuoto di default */
	public DettaglioVariazioniCapitoloAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_VARIAZIONI_SINGOLO_CAPITOLO);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_SINTETICA_VARIAZIONI_SINGOLO_CAPITOLO);
		
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaVariazioniSingoloCapitolo req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaVariazioniSingoloCapitolo req, ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoVariazioneImportoSingoloCapitolo ottieniIstanza(VariazioneImportoSingoloCapitolo e) throws FrontEndBusinessException {
		RicercaDettaglioVariazioneComponenteImportoCapitolo req = model.creaRequestRicercaDettaglioVariazioneComponenteImportoCapitolo(e, sessionHandler.getParametro(BilSessionParameter.CAPITOLO_PER_RICERCA_DETTAGLIO_VARIAZIONE, Integer.class));
		RicercaDettaglioVariazioneComponenteImportoCapitoloResponse res = variazioneDiBilancioService.ricercaDettaglioVariazioneComponenteImportoCapitolo(req);
		return new ElementoVariazioneImportoSingoloCapitolo(e, model.getAnnoEsercizioInt(),
				res.getListaDettaglioVariazioneComponenteImportoCapitolo(),
				res.getListaDettaglioVariazioneComponenteImportoCapitolo1(),
				res.getListaDettaglioVariazioneComponenteImportoCapitolo2());
	}

	@Override
	protected RicercaSinteticaVariazioniSingoloCapitoloResponse ottieniResponse(RicercaSinteticaVariazioniSingoloCapitolo req) {
		return capitoloService.ricercaSinteticaVariazioniSingoloCapitolo(req);
	}

	@Override
	protected ListaPaginata<VariazioneImportoSingoloCapitolo> ottieniListaRisultati(RicercaSinteticaVariazioniSingoloCapitoloResponse response) {
		return response.getVariazioni();
	}
	
}
