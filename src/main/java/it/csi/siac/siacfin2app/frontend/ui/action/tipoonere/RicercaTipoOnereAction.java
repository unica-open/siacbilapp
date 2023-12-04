/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.tipoonere;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.tipoonere.RicercaTipoOnereModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaTipoOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaTipoOnereResponse;

/**
 * Classe di Action per la ricerca del TipoOnere.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/11/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaTipoOnereAction extends GenericTipoOnereAction<RicercaTipoOnereModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6470931243077959315L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		final String methodName = "prepare";
		try {
			caricamentoListe();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, "Errore nel caricamento delle liste: " + wsife.getMessage());
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("caricamento delle liste delle codifiche non possibile"));
		}
	}

	/**
	 * Caricamento delle liste.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso di eccezione nel caricamento delle listeb
	 */
	private void caricamentoListe() throws WebServiceInvocationFailureException {
		caricaNaturaOnere();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		//Impostazione dei valori di default
		checkCasoDUsoApplicabile(model.getTitolo());
		impostaValoriDefault();
		return SUCCESS;
	}

	/**
	 * Impostazione dei valori di default.
	 */
	private void impostaValoriDefault() {
		model.setCorsoDiValidita(Boolean.TRUE);
	}
	
	/**
	 * Effettua la ricerca del TipoOnere.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String effettuaRicerca() {
		final String methodName = "effettuaRicerca";
		RicercaSinteticaTipoOnere request = model.creaRequestRicercaSinteticaTipoOnere();
		logServiceRequest(request);
		RicercaSinteticaTipoOnereResponse response = tipoOnereService.ricercaSinteticaTipoOnere(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaSinteticaTipoOnere.class, response));
			addErrori(response);
			return SUCCESS;
		}
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		log.debug(methodName, "Ricerca effettuata con successo. Totale elementi trovati: " + response.getTotaleElementi());
		// Impostazione dei dati
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_TIPO_ONERE, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_TIPO_ONERE, response.getListaTipoOnere());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		// Termine dell'impostazione dei dati
		return SUCCESS;
	}
	
	/**
	 * Redirezione all'aggiornamento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		final String methodName = "aggiorna";
		log.debug(methodName, "Redirezione verso l'aggiornamento del TipoOnere con uid " + (model.getTipoOnere() != null ? model.getTipoOnere().getUid() : 0));
		return SUCCESS;
	}
	
	/**
	 * Redirezione alla consultazione.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String consulta() {
		final String methodName = "consulta";
		log.debug(methodName, "Redirezione verso la consultazione del TipoOnere con uid " + (model.getTipoOnere() != null ? model.getTipoOnere().getUid() : 0));
		return SUCCESS;
	}
	
	/**
	 * Redirezione all'inserimento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisci() {
		final String methodName = "inserisci";
		log.debug(methodName, "Redirezione verso l'inserimento di un nuovo Tipo Onere");
		return SUCCESS;
	}
	
}
