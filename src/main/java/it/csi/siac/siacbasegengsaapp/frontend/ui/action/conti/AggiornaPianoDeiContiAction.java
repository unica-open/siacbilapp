/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.conti;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti.AggiornaPianoDeiContiModel;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioContoResponse;
import it.csi.siac.siacgenser.model.Conto;

/**
 * Classe di action per la ricerca di un conto
 * 
 * @version 1.0.0 - 09/03/2015
 * @param <M> la tipizzazione del model
 *
 */

public abstract class AggiornaPianoDeiContiAction<M extends AggiornaPianoDeiContiModel> extends BaseInserisciAggiornaPianoDeiContiAction<M> {


	/** Per la serializzazione */
	private static final long serialVersionUID = 3420174692292561587L;
	
	/**
	 * Impostazione degli altri dati
	 */
	protected void impostaAltriDati() {
		if(model.getConto() == null) {
			return;
		}
		model.setCodiceContoEditato(costruisciCodice(model.getConto()));
		List<Conto> contiFigli = sessionHandler.getParametro(BilSessionParameter.RISULTATI_RICERCA_FIGLI_CONTO);
		model.setFigliPresenti(contiFigli != null && !contiFigli.isEmpty());
		GregorianCalendar cg = new GregorianCalendar();
		cg.setTime(model.getConto().getDataInizioValidita());
		int anno = cg.get(Calendar.YEAR);
		log.debug("impostaAltriDati", "data inizio validita: " +  model.getConto().getDataInizioValidita());
		log.debug("impostaAltriDati", "anno: " + anno);
		model.setValiditaNellAnnoCorrente(anno == model.getBilancio().getAnno());
		model.setContoFinGuidataDisabled(isContoFinGuidataDisabled(model.getConto().getPianoDeiConti().getClassePiano()));
	}

	/**
	 * Aggiornamento del conto con i dati passati in input
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public abstract String aggiornamento();
	
	/**
	 * Validazione per il metodo {@link #aggiornamento()}
	 */
	protected void validazioneAggiornamento(){
		Conto conto = model.getConto();
		checkNotNull(conto, "conto");
		checkCondition(!StringUtils.isEmpty(model.getCodiceContoEditato()), 
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("codice conto"));
		checkCondition(!StringUtils.isEmpty(conto.getDescrizione()), 
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("descrizione conto"));
		checkCondition(conto.getTipoConto() != null && conto.getTipoConto().getUid() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("tipo conto"));
		
		ricercaEValidazioneSoggetto();
		ricercaEValidazioneContoCollegato();
		ricercaEValidazionePDC();
	}
	
	/**
	 * Caricamento del dettaglio del conto.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	protected void caricaDettaglioConto() throws WebServiceInvocationFailureException{
		RicercaDettaglioConto request = model.creaRequestRicercaDettaglioConto();
		logServiceRequest(request);
		RicercaDettaglioContoResponse response = contoService.ricercaDettaglioConto(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioConto.class, response));
		}
		model.setConto(response.getConto());
		model.setContiFiglioSenzaFigli(response.getContiFiglioSenzaFigli());
		model.setCodiceBilancio(model.getConto().getCodiceBilancio());
		
		model.setFiglioNonDiLegge(model.getConto().getPianoDeiConti().getClassePiano().getLivelloDiLegge() != null &&
				model.getConto().getLivello() > model.getConto().getPianoDeiConti().getClassePiano().getLivelloDiLegge().intValue());
		sessionHandler.setParametro(BilSessionParameter.UID_CLASSE, model.getConto().getPianoDeiConti().getClassePiano().getUid());
	}
	
	/**
	 * Ricostruisce la stringa inserita dall'utente eliminando la dal codice la radice che corrisponde al codice del padre.
	 * 
	 * @param conto il conto la cui stringa &eacute; da comporre
	 * 
	 * @return il codice del conto figlio
	 */
	protected String costruisciCodice(Conto conto) {
		if(conto.getContoPadre() == null || conto.getLivello() == 1){
			return conto.getCodice();
		}
		String codicePadre = conto.getContoPadre().getCodice();
		String codiceFiglio = conto.getCodice();
		return codiceFiglio.substring(codicePadre.length() + 1);
	}

}
