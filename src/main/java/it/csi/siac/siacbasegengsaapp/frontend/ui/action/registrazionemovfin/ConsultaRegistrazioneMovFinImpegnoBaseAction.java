/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinImpegnoBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinImpegnoHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.collections.CollectionUtil;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * Consultazione per l'impegno. Modulo GEN.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class ConsultaRegistrazioneMovFinImpegnoBaseAction<M extends ConsultaRegistrazioneMovFinImpegnoBaseModel> extends GenericBilancioAction<M>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1357449238179917035L;
	
	@Autowired
	private transient MovimentoGestioneService movimentoGestioneService;
	
	@Override
	public String execute() throws Exception {
		return cercaImpegnoOttimizzato();
	}
	
	/**
	 * Cerca l'impegno per chiave ottenendo i subimpegni paginati.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	private String cercaImpegnoOttimizzato() {
		final String methodName = "cercaImpegnoOttimizzato";

		// Se sono nel caso in cui metto un impegno digitandolo, metto in sessione un impegno che e' senza subimpegni 
		RicercaImpegnoPerChiaveOttimizzato req = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();

		RicercaImpegnoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(req);
		if (res.hasErrori()) {
			log.info(methodName, "Errore nell'invocazione del servizio RicercaImpegnoPerChiaveOttimizzato");
			addErrori(res);
			return SUCCESS;
		}
		if (res.isFallimento()) {
			log.info(methodName, "Risultato ottenuto dal servizio RicercaImpegnoPerChiaveOttimizzato: FALLIMENTO");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}

		Impegno impegno = popolaDatiImpegno(res);
		sessionHandler.setParametro(BilSessionParameter.IMPEGNO, impegno);
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_IMPEGNO_PER_CHIAVE_SUBIMPEGNI, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_IMPEGNO_PER_CHIAVE_SUBIMPEGNI,
				CollectionUtil.toListaPaginata(res.getImpegno().getElencoSubImpegni(), res.getNumPagina(), res.getNumeroTotaleSub()));
		
		// SIAC-5294: creo l'helper
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinImpegnoHelper(impegno, model.isGestioneUEB()));

		//setto in sessione la request e la response
		return SUCCESS;
	}
	
	/**
	 * Popolamento dei dati dell'impegno
	 * @param response la response del servizio
	 * @return l'impegno
	 */
	private Impegno popolaDatiImpegno(RicercaImpegnoPerChiaveOttimizzatoResponse response) {
		Impegno impegno = response.getImpegno();
		impegno.setElencoSubImpegni(defaultingList(impegno.getElencoSubImpegni()));
		impegno.setListaVociMutuo(defaultingList(impegno.getListaVociMutuo()));
		// Inizializzazione mutui sui subimpegni
		for (SubImpegno si : impegno.getElencoSubImpegni()) {
			si.setListaVociMutuo(defaultingList(si.getListaVociMutuo()));
		}
		if (impegno.getCapitoloUscitaGestione() == null) {
			// Se il capitolo non e' stato impostato dal servizio, lo imposto io
			impegno.setCapitoloUscitaGestione(response.getCapitoloUscitaGestione());
		}
		return impegno;
	}
}
