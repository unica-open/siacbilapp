/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.BaseInserisciAggiornaCausaleEPBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * Classe base di action per l'inserimento e l'aggiornamento della causale EP, sezione dei conti operazione.
 * 
 * @author Marchino Alessandro
 * @author Simona Paggio
 * @version 1.0.0 - 30/03/2015
 * @version 1.1.0 - 07/10/2015 GSA/GEN
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseInserisciAggiornaCausaleEPContoOperazioneBaseAction<M extends BaseInserisciAggiornaCausaleEPBaseModel> extends BaseInserisciAggiornaCausaleEPBaseAction<M> {
	
	/** Per la serialiazzazione */
	private static final long serialVersionUID = -4089684253552507201L;

	/**
	 * Ottiene la lista dei conti.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaConti() {
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #inserisciConto()}.
	 */
	public void prepareInserisciConto() {
		model.setContoTipoOperazione(null);
	}
	
	/**
	 * Validazione per il metodo {@link #inserisciConto()}.
	 */
	public void validateInserisciConto() {
		checkNotNull(model.getContoTipoOperazione(), "Conto", true);
		
		checkNotNull(model.getContoTipoOperazione().getOperazioneSegnoConto(), "Segno conto");
		if(TipoCausale.Integrata.equals(model.getCausaleEP().getTipoCausale())) {
			checkNotNull(model.getContoTipoOperazione().getOperazioneUtilizzoConto(), "Utilizzo conto");
			checkNotNull(model.getContoTipoOperazione().getOperazioneUtilizzoImporto(), "Utilizzo importo");
		}
		checkNotNull(model.getContoTipoOperazione().getOperazioneTipoImporto(), "Tipo importo");
	}
	
	/**
	 * Validazione del conto.<br/>
	 * Un Conto pu&oacute; essere associato alla causale se sono rispettate tutte le condizioni seguenti.
	 * <ul>
	 *     <li>
	 *         Ha un'istanza valida nell'anno di bilancio in corso.
	 *         <ul>
	 *             <li>In caso non sa presente in archivio inviare il messaggio <code>&lt;COR_INF_0007, Nessun dato reperito a fronte di una ricerca&gt;</code></li>
	 *             <li>In caso non sia valida inviare un messaggio <code>&lt;COR_ERR_043 Entit&agrave; non completa (&lt;nome entit&agrave;&gt;: 'Il Conto ***** ',
	 *             &lt;motivo&gt;: ' non esiste un'istanza valida nell'anno di bilancio')&gt;</code></li>
	 *         </ul>
	 *     </li>
	 *     <li>
	 *         Trovata l’istanza valida si verificano queste ulteriori condizione dell’istanza.
	 *         <ul>
	 *             <li>Conto.attivo = TRUE</li>
	 *             <li>&Eacute; un Conto foglia: Conto.contoFoglia = TRUE</li>
	 *             <li>Non deve essere gi&agrave; presente nella lista dei conti collegati al momento (la verifica si fa sui soli conti che hanno una relazione in corso di validit&agrave;)
	 *         </ul>
	 *         In caso non sia rispettata una delle condizioni seguenti inviare un messaggio
	 *         <code>&lt;COR_ERR_043 Entit&agrave; non completa (&lt;nome entit&agrave;&gt;: 'Il Conto ***** ',  &lt;motivo&gt; : indicare il motivo corrispondente)&gt;.
	 *     </li>
	 * </ul>
	 * 
	 * @param contoDaValidare il conto da validare
	 * @return il conto valido
	 */
	protected Conto validaConto(Conto contoDaValidare) {
		
		// 0. Devo aver impostato il campo
		checkCondition(contoDaValidare != null && StringUtils.isNotBlank(contoDaValidare.getCodice()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice conto finanziario "), true);
		
		// Ricerca del conto
		
		RicercaSinteticaContoResponse response = ricercaSinteticaConto(contoDaValidare);
		
		// 1. Il conto deve esistere
		checkCondition(response.getTotaleElementi() > 0, ErroreCore.NESSUN_DATO_REPERITO.getErrore(""), true);
		
		// TODO: Spero sia solo uno
		Conto conto = response.getConti().get(0);
		// 2. Deve essere valido nell'anno di bilancio
		checkCondition(ValidationUtil.isEntitaValidaPerAnnoEsercizio(conto, model.getAnnoEsercizioInt()),
			ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto " + conto.getCodice(), "non esiste un'istanza valida nell'anno di bilancio"));
		
		// 3. Deve essere attivo
		checkCondition(Boolean.TRUE.equals(conto.getAttivo()), ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto " + conto.getCodice(), "non e' attivo"), true);
		// 4. Deve essere foglia
		checkCondition(Boolean.TRUE.equals(conto.getContoFoglia()), ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto " + conto.getCodice(), "non e' un Conto foglia"), true);
		// 5. Non deve essere gia' collegato
		for(ContoTipoOperazione cto : model.getListaContoTipoOperazione()) {
			Conto contoCto = cto.getConto();
			checkCondition(contoCto == null || contoCto.getUid() != conto.getUid(), ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto " + conto.getCodice(), "e' gia' collegato alla causale"), true);
		}
		
		if(model.getTipoEvento() != null && BilConstants.TIPO_EVENTO_SCRITTURA_EPILOGATIVA.getConstant().equals(model.getTipoEvento().getCodice())) {
			// Non sono ammessi conti appartenenti a ClasseConto = CE-costi di esercizio o RE-Ricavi di esercizio
			checkCondition(isContoNotCENorRE(conto), ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto", "appartiene ad una Classe Conto non compatibile con il tipo evento associato alla causale"), true);
		} else if(model.getTipoEvento() != null && BilConstants.TIPO_EVENTO_CONTI_D_ORDINE.getConstant().equals(model.getTipoEvento().getCodice())) {
			// Sono ammessi solo conti appartenenti a ClasseConto = CO-conti d'ordine
			checkCondition(isContoCO(conto), ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto", "appartiene ad una Classe Conto non compatibile con il tipo evento associato alla causale"), true);
		}
		
		return conto;
	}
	
	/**
	 * Controlla che il conto non sia CO (ovvero OA oppure OP).
	 * 
	 * @param conto il conto da controllare
	 * @return <code>true</code> se il conto &eacute; CO; <code>false</code> altrimenti
	 */
	private boolean isContoCO(Conto conto) {
		return conto.getPianoDeiConti() != null && conto.getPianoDeiConti().getClassePiano() != null
				&& (
					BilConstants.CLASSE_CONTO_CONTI_D_ORDINE_ATTIVITA.getConstant().equals(conto.getPianoDeiConti().getClassePiano().getCodice())
					||
					BilConstants.CLASSE_CONTO_CONTI_D_ORDINE_PASSIVITA.getConstant().equals(conto.getPianoDeiConti().getClassePiano().getCodice())
				);
	}

	/**
	 * Controlla che il conto non sia n&eacute; CE n&eacute; RE.
	 * 
	 * @param conto il conto da controllare
	 * @return <code>true</code> se il conto non &eacute; n&eacute; CE n&eacute; RE; <code>false</code> altrimenti
	 */
	private boolean isContoNotCENorRE(Conto conto) {
		return conto.getPianoDeiConti() != null && conto.getPianoDeiConti().getClassePiano() != null
			&& !BilConstants.CLASSE_CONTO_COSTI_DI_ESERCIZIO.getConstant().equals(conto.getPianoDeiConti().getClassePiano().getCodice())
			&& !BilConstants.CLASSE_CONTO_RICAVI_DI_ESERCIZIO.getConstant().equals(conto.getPianoDeiConti().getClassePiano().getCodice());
	}

	/**
	 * Ricerca sintetica del conto.
	 * 
	 * @param conto il conto da cercare
	 * 
	 * @return la response del servizio
	 */
	private RicercaSinteticaContoResponse ricercaSinteticaConto(Conto conto) {
		RicercaSinteticaConto request = model.creaRequestRicercaSinteticaConto(conto);
		logServiceRequest(request);
		RicercaSinteticaContoResponse response = contoService.ricercaSinteticaConto(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Se ho errori esco
			addErrori(response);
			throw new ParamValidationException(createErrorInServiceInvocationString(RicercaSinteticaConto.class, response));
		}
		return response;
	}

	/**
	 * Inserimento del conto.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciConto() {
		// Aggiungo il conto nella lista
		model.getListaContoTipoOperazione().add(model.getContoTipoOperazione());
		// Cancello il conto
		model.setContoTipoOperazione(null);
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #aggiornaConto()}.
	 */
	public void prepareAggiornaInserisciConto() {
		model.setContoTipoOperazione(null);
		model.setIndiceConto(null);
	}
	
	/**
	 * Validazione per il metodo {@link #aggiornaConto()}.
	 */
	public void validateAggiornaConto() {
		// Se non ho il conto, non posso fare alcunche'
		checkNotNull(model.getContoTipoOperazione(), "Conto", true);
		checkNotNull(model.getIndiceConto(), "Indice del conto", true);
		int idx = model.getIndiceConto().intValue();
		int listSize = model.getListaContoTipoOperazione().size();
		checkCondition(idx >= 0 && idx < listSize, ErroreCore.FORMATO_NON_VALIDO.getErrore("indice del conto", "deve essere compreso tra 0 e " + (listSize - 1)));
		
		checkNotNull(model.getContoTipoOperazione().getOperazioneSegnoConto(), "Segno conto");
		if(TipoCausale.Integrata.equals(model.getCausaleEP().getTipoCausale())) {
			checkNotNull(model.getContoTipoOperazione().getOperazioneUtilizzoConto(), "Utilizzo conto");
			checkNotNull(model.getContoTipoOperazione().getOperazioneUtilizzoImporto(), "Utilizzo importo");
		}
		checkNotNull(model.getContoTipoOperazione().getOperazioneTipoImporto(), "Tipo importo");
		Conto contoDaCop = model.getContoTipoOperazione().getConto();
		if(contoDaCop == null || contoDaCop.getPianoDeiConti() == null || contoDaCop.getPianoDeiConti().getClassePiano() == null || contoDaCop.getPianoDeiConti().getClassePiano().getUid() == 0){
			return;
		}
		contoDaCop.setAmbito(model.getAmbito());
		// Ripopolo il conto
		RicercaSinteticaContoResponse response = ricercaSinteticaConto(contoDaCop);
		// E' gia' valido
		Conto conto = response.getConti().get(0);
		
		model.getContoTipoOperazione().setConto(conto);
		
	}
	
	/**
	 * Aggiornamento del conto.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaConto() {
		int idx = model.getIndiceConto().intValue();
		// Aggiungo il conto nella lista
		model.getListaContoTipoOperazione().set(idx, model.getContoTipoOperazione());
		// Cancello il conto
		model.setContoTipoOperazione(null);
		model.setIndiceConto(null);
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #eliminaConto()}.
	 */
	public void prepareEliminaInserisciConto() {
		model.setIndiceConto(null);
	}
	
	/**
	 * Validazione per il metodo {@link #eliminaConto()}.
	 */
	public void validateEliminaConto() {
		// Se non ho il conto, non posso fare alcunche'
		checkNotNull(model.getIndiceConto(), "Indice del conto", true);
		int idx = model.getIndiceConto().intValue();
		int listSize = model.getListaContoTipoOperazione().size();
		checkCondition(idx >= 0 && idx < listSize, ErroreCore.FORMATO_NON_VALIDO.getErrore("indice del conto", "deve essere compreso tra 0 e " + (listSize - 1)));
	}
	
	/**
	 * Eliminazione del conto.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaConto() {
		int idx = model.getIndiceConto().intValue();
		// Aggiungo il conto nella lista
		model.getListaContoTipoOperazione().remove(idx);
		model.setIndiceConto(null);
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
}
