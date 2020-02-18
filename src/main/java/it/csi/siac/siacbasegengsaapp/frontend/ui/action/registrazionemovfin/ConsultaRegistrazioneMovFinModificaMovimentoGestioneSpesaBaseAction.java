/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;

/**
 * Consultazione base per la modifica del movimento di gestione di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 20/10/2015
 * @param <M> la tipizzazione del model
 */
public class ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaBaseAction<M extends ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaBaseModel> extends GenericBilancioAction<M>{
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 975563045921409841L;
	
	@Autowired
	private transient MovimentoGestioneService movimentoGestioneService;
	
	@Override
	public String execute() throws Exception {
		RicercaImpegnoPerChiaveOttimizzato req = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();
		logServiceRequest(req);
		RicercaImpegnoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			return INPUT;
		}
		if(res.getImpegno() == null){
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		Impegno impegno = res.getImpegno();
		SubImpegno subImpegno = findSubImpegno(res.getImpegno());
		ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa = findModificaMovimentoGestioneSpesa(subImpegno != null ? subImpegno : impegno);
		
		// SIAC-5294
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaHelper(impegno, subImpegno, modificaMovimentoGestioneSpesa, model.isGestioneUEB()));
		
		return SUCCESS;
	
	}
	
	/**
	 * Trova il subimpegno.
	 * @param impegno l'impegno
	 * @return il sub
	 */
	private SubImpegno findSubImpegno(Impegno impegno) {
		final String methodName = "findSubImpegno";
		if(impegno == null || impegno.getElencoSubImpegni() == null) {
			return null;
		}
		BigDecimal numeroSub = model.getNumeroSub();
		if(numeroSub == null) {
			log.debug(methodName, "Modifica non relativa al subimpegno: non tiro su il dato");
			return null;
		}
		for(SubImpegno si : impegno.getElencoSubImpegni()) {
			if(si != null && si.getNumero() != null && si.getNumero().compareTo(numeroSub) == 0) {
				return si;
			}
		}
		return null;
	}
	
	/**
	 * Trova la modifica dall'impegno restituito dal servizio di ricerca.
	 * 
	 * @param impegno l'impegno del servizio di ricerca
	 * @return la modifica
	 */
	private ModificaMovimentoGestioneSpesa findModificaMovimentoGestioneSpesa(Impegno impegno) {
		if(impegno == null || impegno.getListaModificheMovimentoGestioneSpesa() == null) {
			return null;
		}
		int uid = model.getUid().intValue();
		for(ModificaMovimentoGestioneSpesa mmgs : impegno.getListaModificheMovimentoGestioneSpesa()) {
			if(mmgs != null && mmgs.getUid() == uid) {
				return mmgs;
			}
		}
		return null;
	}

}
