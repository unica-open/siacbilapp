/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.DettaglioMovimentiPrimaNotaIntegrataModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaResponse;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;
/**
 * Classe di action per il dettaglio dei movimenti della prima nota integrata-
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 14/05/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class DettaglioMovimentiPrimaNotaIntegrataAction extends GenericBilancioAction <DettaglioMovimentiPrimaNotaIntegrataModel> {

	/** Per la serializzazione	 **/
	private static final long serialVersionUID = -6733967364481507901L;
	
	@Autowired private transient PrimaNotaService primaNotaService;
	
	/**
	 * Caricamento dei movimenti per la prima nota libera.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaMovimenti() {
		// TODO: vedere se possa aver senso creare un servizio ad hoc
		final String methodName = "caricaMovimenti";
		
		RicercaDettaglioPrimaNota request = model.creaRequestRicercaDettaglioPrimaNota();
		logServiceRequest(request);
		RicercaDettaglioPrimaNotaResponse response = primaNotaService.ricercaDettaglioPrimaNota(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		
		log.debug(methodName, "Caricato il dettaglio per la nota con uid " + model.getPrimaNota().getUid());
		PrimaNota primaNota = response.getPrimaNota();
		
		List<MovimentoEP> listaMovimentoEP = primaNota.getListaMovimentiEP();
		popolaListaMovimentiDettaglio(listaMovimentoEP);
		
		impostaTotaleDareAvere();
		
		return SUCCESS;
	}


	/**
	 * Impostazione del totale dare e del totale avere dalla lista dei movimenti.
	 */
	private void impostaTotaleDareAvere() {
		BigDecimal totaleDare = BigDecimal.ZERO;
		BigDecimal totaleAvere = BigDecimal.ZERO;
		for(MovimentoDettaglio md : model.getListaMovimentoDettaglio()) {
			if(md.getImporto() != null) {
				if(OperazioneSegnoConto.DARE.equals(md.getSegno())) {
					totaleDare = totaleDare.add(md.getImporto());
				} else if(OperazioneSegnoConto.AVERE.equals(md.getSegno())) {
					totaleAvere = totaleAvere.add(md.getImporto());
				}
			}
		}
		
		model.setTotaleDare(totaleDare);
		model.setTotaleAvere(totaleAvere);
	}
	
	/**
	 * (JIRA 3647)
	 *  Per i documenti, nel caso in cui ci siano pi&uacute; quote e quindi potenzialmente pi&uacute; conti economici patrimoniali assegnati uguali (se hanno lo stesso impegno sulle quote),
	 *  attualmente il sistema riporta tutti i conti economici/patrimoniali per tutte le quote presenti (anche se uguali)
	 *	Occorre visualizzare una volta sola il codice del conto con accanto la somma di tutti gli importi presenti.
	 *	Esempio documento con due quote a cui &eacute; stato assegnato lo stesso impegno (visualizzazione attuale)
	 * @param listaMovimentoEP la lista di movimenti EP
	 */
	private void popolaListaMovimentiDettaglio(List<MovimentoEP> listaMovimentoEP) {

		// controllo la lista che sia diversa da null
		if (listaMovimentoEP == null || listaMovimentoEP.isEmpty()) {
			return;
		}

		Map<String, MovimentoDettaglio> result = new HashMap<String, MovimentoDettaglio>();
		// scrollo la lista con i dettagli
		for (MovimentoEP m : listaMovimentoEP ) {
			for (MovimentoDettaglio md : m.getListaMovimentoDettaglio()) {
				String key = computeGroupKeyMovimentoDettaglio(md);
				if (!result.containsKey(key)) {
					result.put(key, md);
				} else {
					// bisogna aggiornare gli importi ...
					MovimentoDettaglio objectFromMap = result.get(key);
					BigDecimal importoDaAggiornare = objectFromMap.getImporto().add(md.getImporto());
					objectFromMap.setImporto(importoDaAggiornare);
					result.put(key, objectFromMap);
				}
			}
		}
		List<MovimentoDettaglio> list = new ArrayList<MovimentoDettaglio>(result.values());
		model.setListaMovimentoDettaglio(list);
	}


	/**
	 *  key per segno e conto
	 * @param m il movimento
	 * @return la chiave di raggruppamento
	 */
	private String computeGroupKeyMovimentoDettaglio(MovimentoDettaglio m) {
		StringBuilder sb = new StringBuilder();
		sb.append(m.getConto().getUid());
		sb.append("_");
		sb.append(m.getSegno());
		return sb.toString();
	}
}
