/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.aggiorna;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.aggiorna.AggiornaPrimaNotaIntegrataDocumentoBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoQuotaPrimaNotaIntegrataFactory;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrata;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrataFactory;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNotaResponse;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
/**
 * Classe di action per l'aggiornamento della prima nota integrata.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 14/03/2017
 * @param <M> la tipizzazione del model
 *
 */
public abstract class AggiornaPrimaNotaIntegrataDocumentoBaseAction<M extends AggiornaPrimaNotaIntegrataDocumentoBaseModel> extends BaseAggiornaPrimaNotaIntegrataBaseAction<M> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 2896413243933299574L;

	/** Nome del model del completamento e validazione della reg con nuova primanota x la sessione */
	public static final String MODEL_SESSION_NAME_AGGIORNA_PRIMA_NOTA_DOCUMENTO_FIN = "AggiornaPrimaNotaIntegrataDocumentoFINModel";

	/** Nome del model del completamento della reg con nuova primanota per la sessione */
	public static final String MODEL_SESSION_NAME_AGGIORNA_PRIMA_NOTA_DOCUMENTO_GSA = "AggiornaPrimaNotaIntegrataDocumentoGSAModel";
	
	/**
	 * Ottiene il parametro di request per la ricerca dei dati finanziari associati alla prima nota integrata
	 * @return l'azione consentita
	 */
	protected abstract BilSessionParameter getParametroSessioneRequest();
	
	/**
	 * Ottiene il parametro di request per la ricerca dei dati finanziari associati alla prima nota integrata
	 * @return l'azione consentita
	 */
	protected abstract BilSessionParameter getParametroSessioneLista();
	

	@Override
	protected void popolaModel(PrimaNota primaNota) {
		model.setPrimaNota(primaNota);
		model.setListaElementoQuota(ElementoQuotaPrimaNotaIntegrataFactory.getInstancesFromPrimaNota(primaNota));
		
		Map<Integer,List<ElementoScritturaPrimaNotaIntegrata>> mappaMovimentoEPScritture = new HashMap<Integer, List<ElementoScritturaPrimaNotaIntegrata>>();
		for (MovimentoEP movEP : primaNota.getListaMovimentiEP()) {
			mappaMovimentoEPScritture.put(movEP.getUid(), ElementoScritturaPrimaNotaIntegrataFactory.creaListaScrittureDaSingoloMovimentoEP(movEP, false));
		}
		model.setMappaMovimentoEPScritture(mappaMovimentoEPScritture);
		log.debug("popolaModel", "Mappa dei movimentiEP: " + mappaMovimentoEPScritture.values());
		
	}
	
	@Override
	protected void caricaDatiUlteriori(PrimaNota primaNota) throws WebServiceInvocationFailureException{
		//ricercaRegistrazioniAssociate(primaNota);
	}
	
	/**
	 *  A partire dalla request ({@link OttieniEntitaCollegatePrimaNota}) e dalla response ({@link OttieniEntitaCollegatePrimaNotaResponse}), imposta nel model e in sessione i dati necessari alla consultazione ed, eventualmente, alla gestione paginata della tabella
	 *  
	 *   @params req la request da impostare in sessione per la tabella paginata
	 *   @params req la response da cui ottenere gli elementi da consultare
	 * */
	@Override
	protected void impostaDatiPerPaginazioneDatiFinanziari(OttieniEntitaCollegatePrimaNota req,	OttieniEntitaCollegatePrimaNotaResponse res) {
		final String methodName = "impostaDatiPerPaginazione";

		// Imposto in sessione i dati
		log.debug(methodName, "Impovto in sessione la request");
		sessionHandler.setParametroXmlType(getParametroSessioneRequest(), req);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(getParametroSessioneLista(), res.getEntitaCollegate());
	}
	

	@Override
	protected void checkScrittureCorrette() {
		int numeroScrittureDare = 0;
		int numeroScrittureAvere = 0;
		
		BigDecimal totaleDare = BigDecimal.ZERO;
		BigDecimal totaleAvere = BigDecimal.ZERO;
		
		List<MovimentoEP> listaMovimentiEPPrimaNota = model.getPrimaNota().getListaMovimentiEP();
		Map<Integer, List<ElementoScritturaPrimaNotaIntegrata>> mappaMovimentoEPScrittureElaborate = model.getMappaMovimentoEPScritture();
		for (MovimentoEP movimentoEP : listaMovimentiEPPrimaNota) {
			
			for (ElementoScritturaPrimaNotaIntegrata elementoScrittura : mappaMovimentoEPScrittureElaborate.get(movimentoEP.getUid())){
				if (elementoScrittura != null && elementoScrittura.getMovimentoDettaglio() != null){
					BigDecimal importo = elementoScrittura.getMovimentoDettaglio().getImporto();
					
					checkCondition(elementoScrittura.getMovimentoDettaglio().getConto() != null && elementoScrittura.getMovimentoDettaglio().getConto().getUid() != 0,
							ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("e' necessario assegnare i conti a tutte le scritture"), true);
					if (importo == null){
						addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo Conto " + elementoScrittura.getMovimentoDettaglio().getConto().getCodice()
								+ " " + elementoScrittura.getMovimentoDettaglio().getSegno()));
						continue;
					}
					
					if(elementoScrittura.isSegnoDare()) {
						numeroScrittureDare++;
						totaleDare = totaleDare.add(importo);
					}
					
					if(elementoScrittura.isSegnoAvere()) {
						numeroScrittureAvere++;
						totaleAvere = totaleAvere.add(importo);
					}
						
				}
			}
		
			checkCondition(numeroScrittureDare > 0 && numeroScrittureAvere > 0, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Devono essere presenti almeno due conti con segni differenti"));
			checkCondition(totaleDare.subtract(totaleAvere).signum() == 0, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il totale DARE deve essere UGUALE al totale AVERE"));
		}
	}
}
