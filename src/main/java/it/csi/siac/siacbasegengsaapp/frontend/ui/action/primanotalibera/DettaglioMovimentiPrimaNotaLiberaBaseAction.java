/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.DettaglioMovimentiPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaResponse;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Classe di Action per i dettagli della prima nota libera (comune tra ambito FIN e GSA)
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 07/10/2015
 * @param <M> la tipizzazione del model
 */
public class DettaglioMovimentiPrimaNotaLiberaBaseAction<M extends DettaglioMovimentiPrimaNotaLiberaBaseModel > extends GenericBilancioAction <M> {

	
	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 4560977723777905756L;
	
	@Autowired private transient PrimaNotaService primaNotaService;
	
	/**
	 * Caricamento dei movimenti per la prima nota libera.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaMovimentiBase() {
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
		
		log.debug(methodName, "Caricato il dettaglio per la nota con uid " + model.getPrimaNotaLibera().getUid());
		PrimaNota primaNota = response.getPrimaNota();
		
		List<MovimentoEP> listaMovimentoEP = primaNota.getListaMovimentiEP();
		List<MovimentoDettaglio> listaMovimentoDettaglio = ottieniListaMovimentiDettaglio(listaMovimentoEP);
		
		model.setListaMovimentoDettaglio(listaMovimentoDettaglio);
		
		impostaTotaleDareAvere(listaMovimentoDettaglio);
		
		return SUCCESS;
	}

	/**
	 * Ottiene la lista dei movimenti di dettaglio dalla lista dei movimenti EP.
	 * 
	 * @param listaMovimentoEP la lista dei movimenti da cui ottenere i dettagli
	 * 
	 * @return la lista dei dettagli
	 */
	private List<MovimentoDettaglio> ottieniListaMovimentiDettaglio(List<MovimentoEP> listaMovimentoEP) {
		List<MovimentoDettaglio> result = new ArrayList<MovimentoDettaglio>();
		for(MovimentoEP mep : listaMovimentoEP) {
			// TODO: spostare su ITF!!
			if(mep.getListaMovimentoDettaglio() != null) {
				result.addAll(mep.getListaMovimentoDettaglio());
			}
		}
		return result;
	}

	/**
	 * Impostazione del totale dare e del totale avere dalla lista dei movimenti.
	 * 
	 * @param listaMovimentoDettaglio la lista da cui ricavare i totali
	 */
	private void impostaTotaleDareAvere(List<MovimentoDettaglio> listaMovimentoDettaglio) {
		BigDecimal totaleDare = BigDecimal.ZERO;
		BigDecimal totaleAvere = BigDecimal.ZERO;
		for(MovimentoDettaglio md : listaMovimentoDettaglio) {
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
	
}
