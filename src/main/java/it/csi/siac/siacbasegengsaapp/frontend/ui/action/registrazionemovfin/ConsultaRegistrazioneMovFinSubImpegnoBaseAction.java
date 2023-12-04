/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinSubImpegnoBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinSubImpegnoHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * Consultazione base per il subImpegno.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 20/10/2015
 * @param <M> la tipizzazione del model
 */
public class ConsultaRegistrazioneMovFinSubImpegnoBaseAction<M extends ConsultaRegistrazioneMovFinSubImpegnoBaseModel> extends GenericBilancioAction<M>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2446886489787990361L;
	
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
		
		Impegno impegno = res.getImpegno();
		SubImpegno subImpegno = findSubImpegno(impegno.getElencoSubImpegni());
		
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinSubImpegnoHelper(impegno, subImpegno, model.isGestioneUEB()));

		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_IMPEGNO_PER_CHIAVE_SUBIMPEGNI, req);
		//setto in sessione la request e la response
		return SUCCESS;
	}
	
	/**
	 * Cerca il subimpegno nella lista dell'impegno
	 * @param listaSubImpegno la lista dei sub
	 * @return il subimpegno
	 */
	private SubImpegno findSubImpegno(List<SubImpegno> listaSubImpegno) {
		if(listaSubImpegno == null) {
			return null;
		}
		for(SubImpegno si : listaSubImpegno) {
			// Imposto solo la riga del singolo subimpegno ricercato
			if(si != null && si.getNumeroBigDecimal() != null && model.getNumeroSub().compareTo(si.getNumeroBigDecimal()) == 0) {
				return si;
			}
		}
		return null;
	}

}
