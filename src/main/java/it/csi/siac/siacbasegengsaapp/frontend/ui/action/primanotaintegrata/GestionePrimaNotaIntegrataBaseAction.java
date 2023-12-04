/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.GestionePrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.RegistrazioneMovFinService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe base di action per la gestione della prima nota integrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/10/2015
 * 
 * @param <M> la tipizzazione del model
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class GestionePrimaNotaIntegrataBaseAction<M extends GestionePrimaNotaIntegrataBaseModel> extends GenericBilancioAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -3130350115511899616L;
	
//	private static final Map<TipoCollegamento, String> MAPPA_RISULTATI;
	
//	static {
//		Map<TipoCollegamento, String> temp = new HashMap<TipoCollegamento, String>();
//		temp.put(TipoCollegamento.LIQUIDAZIONE, "Liquidazione");
//		temp.put(TipoCollegamento.IMPEGNO, "Impegno");
//		temp.put(TipoCollegamento.ACCERTAMENTO, "Accertamento");
//		temp.put(TipoCollegamento.ORDINATIVO_INCASSO, "OrdinativoIncasso");
//		temp.put(TipoCollegamento.ORDINATIVO_PAGAMENTO, "OrdinativoPagamento");
//		temp.put(TipoCollegamento.DOCUMENTO_ENTRATA, "DocumentoEntrata");
//		temp.put(TipoCollegamento.DOCUMENTO_SPESA, "DocumentoSpesa");
//		temp.put(TipoCollegamento.SUBDOCUMENTO_ENTRATA, "SubdocumentoEntrata");
//		temp.put(TipoCollegamento.SUBDOCUMENTO_SPESA, "SubdocumentoSpesa");
//		// TODO: aggiungere gli altri
//		temp.put(TipoCollegamento.RICHIESTA_ECONOMALE, "RichiestaEconomale");
//		temp.put(TipoCollegamento.RENDICONTO_RICHIESTA, "RendicontoRichiesta");
//		temp.put(TipoCollegamento.SUBIMPEGNO, "SubImpegno");
//		temp.put(TipoCollegamento.SUBACCERTAMENTO, "SubAccertamento");
//		
//		temp.put(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_SPESA, "ModificaMovimentoGestioneSpesa");
//		temp.put(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA, "ModificaMovimentoGestioneEntrata");
//		
//		// La mappa dei risultati NON puo' essere modificata a runtime
//		MAPPA_RISULTATI = Collections.unmodifiableMap(temp);
//		temp = null;
//	}
	
	@Autowired private transient RegistrazioneMovFinService registrazioneMovFinService;
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
		checkCasoDUsoApplicabile();
		// TODO verifica nome uid da usare nel model
		// In inserimento arrivo da registrazione
		RicercaDettaglioRegistrazioneMovFin request = model.creaRequestRicercaDettaglioRegistrazioneMovFin(model.getUidRegistrazione());
		logServiceRequest(request);
		
		RicercaDettaglioRegistrazioneMovFinResponse response = registrazioneMovFinService.ricercaDettaglioRegistrazioneMovFin(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioRegistrazioneMovFin.class, response));
			addErrori(response);
			return INPUT;
		}
		if (response.getRegistrazioneMovFin() == null) {
			log.info(methodName, "RegistrazioneMovFin con uid " + model.getUidRegistrazione() + " non presente");
			addErrore(ErroreCore.ENTITA_INESISTENTE.getErrore("Registrazione movimento finanziario", model.getUidRegistrazione()));
			return INPUT;
		}
		
		RegistrazioneMovFin regMovFin = response.getRegistrazioneMovFin();
		sessionHandler.setParametro(BilSessionParameter.REGISTRAZIONEMOVFIN, response.getRegistrazioneMovFin());
		String nomeAzioneRedirezione = componiStringaRedirezione(regMovFin);
		log.debug(methodName, "registrazione ottenuta: " + nomeAzioneRedirezione);
		model.setNomeAzioneRedirezione(nomeAzioneRedirezione);
		return SUCCESS;
	}

	/**
	 * Compone la stringa di redirezione.
	 * <br/>
	 * Il risultato sar&agrave; una stringa del tipo <pre>completa%VALIDA%%MODALITA%%TIPOCOLLEGAMENTO%PrimaNotaIntegrata</pre>.
	 * E.g.:
	 * <ul>
	 *     <li>completaValidaLiquidazioneInsPrimaNotaIntegrata</li>
	 *     <li>completaLiquidazioneAggPrimaNotaIntegrata</li>
	 * </ul>
	 * @param registrazioneMovFin la registrazione da cui ottenere l'azione
	 * @return il nome della redirezione da effettuare
	 */
	private String componiStringaRedirezione(RegistrazioneMovFin registrazioneMovFin) {
		StringBuilder result = new StringBuilder("completa");
		if (model.isValidazione()) {
			result.append("Valida");
		}
		//result.append(getTipologiaRegistrazione(registrazioneMovFin.getEvento().getTipoCollegamento()));
		
		result.append(registrazioneMovFin.getMovimento().getClass().getSimpleName());
		
		if(registrazioneMovFin.getMovimentoCollegato()!=null){
			result.append(registrazioneMovFin.getMovimentoCollegato().getClass().getSimpleName());
		}
		
		
		// TODO verificare se aggiornamento tramite la ricerca
		result.append("Ins");
		result.append("PrimaNotaIntegrata");
		// Lotto N
		// Aggiungo l'ambito
		String ambitoSuffix = model.getAmbito().getSuffix();
		result.append(ambitoSuffix);
		if(model.isFromCDUDocumento()) {
			result.append("daCDUDocumento");
		}

		return result.toString();
	}

//	/**
//	 * Ottiene la tipologia della registrazione a partire dalla registrazione stessa.
//	 * 
//	 * @param tipoCollegamento il tipo di collegamento da cui ottenere il risultato
//	 * 
//	 * @return la tipologia di registrazione
//	 */
//	private String getTipologiaRegistrazione(TipoCollegamento tipoCollegamento) {
//		if(MAPPA_RISULTATI.containsKey(tipoCollegamento)) {
//			return MAPPA_RISULTATI.get(tipoCollegamento);
//		}
//		return "";
//	}
	
}
