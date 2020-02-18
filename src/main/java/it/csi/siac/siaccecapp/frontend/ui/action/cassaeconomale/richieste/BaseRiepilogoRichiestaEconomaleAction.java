/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseRiepilogoRichiestaEconomaleModel;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.errore.TipoErrore;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;

/**
 * Classe base di action per tutte le funzionalit&agrave; richiedenti il riepilogo della richiesta economale
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 22/01/2015
 * 
 * @param <M> la tipizzazione del model
 *
 */
public class BaseRiepilogoRichiestaEconomaleAction<M extends BaseRiepilogoRichiestaEconomaleModel> extends BaseRichiestaEconomaleAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1032069580832552972L;
	
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	
	/**
	 * Controlla la validit&agrave; del movimento di gestione.
	 * 
	 * @param movimentoGestione    il movimento da controllare
	 * @param subMovimentoGestione il submovimento da controllare
	 */
	protected void checkMovimentoGestione(Impegno movimentoGestione, SubImpegno subMovimentoGestione) {
		final String methodName = "checkMovimentoGestione";
		checkNotNull(movimentoGestione, "Impegno", true);
		checkCondition(movimentoGestione.getAnnoMovimento() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno impegno"));
		checkCondition(movimentoGestione.getNumero() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero impegno"));
		
		if(hasErrori()) {
			// Se ho errori, esco subito
			return;
		}
		
		// La chiave logica del movimento
		final String chiaveMovimentoGestione = movimentoGestione.getAnnoMovimento() + "/" + movimentoGestione.getNumero();
		
		Impegno impegno;
		List<SubImpegno> subImp;
		try {
			//impegno = ottieniImpegnoDaServizio(movimentoGestione);
			RicercaImpegnoPerChiaveOttimizzatoResponse res = ottieniImpegnoDaServizio(movimentoGestione);
			impegno = res.getImpegno();
			subImp = res.getElencoSubImpegniTuttiConSoloGliIds() != null ? res.getElencoSubImpegniTuttiConSoloGliIds() : new ArrayList<SubImpegno>();
		} catch(WebServiceInvocationFailureException wsife) {
			// Ho gia' fatto il log
			log.info(methodName, wsife.getMessage());
			return;
		}
		
		log.debug(methodName, "Trovato movimento di gestione " + impegno.getUid() + " da servizio corrispondente alla chiave " + chiaveMovimentoGestione);
		model.setMovimentoGestione(impegno);
		
		Impegno perValidazione = impegno;
		TipoErrore erroreDefinitivo = ErroreFin.IMPEGNO_NON_IN_STATO_DEFINITIVO;
		String sulStr = "sull'impegno";
		String delStr = "dell'impegno";
		
		checkCondition(subImp.isEmpty() || (subMovimentoGestione != null && subMovimentoGestione.getNumero() != null), ErroreFin.IMPEGNO_CON_SUBIMPEGNI.getErrore(), true);
		
		if(subMovimentoGestione != null && subMovimentoGestione.getNumero() != null && impegno.getElencoSubImpegni() != null) {
			// La chiave logica del submovimento
			final String chiaveSubMovimentoGestione = chiaveMovimentoGestione + "-" + subMovimentoGestione.getNumero();
			
			log.debug(methodName, "Ricerca del subimpegno");
			SubImpegno subImpegno = ComparatorUtils.findByNumeroMovimentoGestione(impegno.getElencoSubImpegni(), subMovimentoGestione);
			checkCondition(subImpegno != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("SubImpegno", chiaveSubMovimentoGestione), true);
			log.debug(methodName, "Trovato submovimento di gestione " + impegno.getUid() + " da servizio corrispondente alla chiave " + chiaveSubMovimentoGestione);
			model.setSubMovimentoGestione(subImpegno);
			
			perValidazione = subImpegno;
			erroreDefinitivo = ErroreFin.SUBIMPEGNO_NON_IN_STATO_DEFINITIVO;
			sulStr = "sul subimpegno";
			delStr = "del subimpegno";
		}
		
		checkMovimentoGestione(perValidazione, erroreDefinitivo, sulStr, delStr);
	}

	/**
	 * Ottiene l'impegno da servizio, corrispondente ad anno e numero forniti.
	 * 
	 * @param movimentoGestione il movimento tramite cui effettuare la ricerca
	 * @return l'impegno corrispondente
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected RicercaImpegnoPerChiaveOttimizzatoResponse ottieniImpegnoDaServizio(Impegno movimentoGestione) throws WebServiceInvocationFailureException {
		final String methodName = "ottieniImpegnoDaServizio";
		final String chiaveMovimentoGestione = movimentoGestione.getAnnoMovimento() + "/" + movimentoGestione.getNumero();
		
//		Impegno impegno = sessionHandler.getParametro(BilSessionParameter.IMPEGNO);
		
//		if(!ValidationUtil.isValidMovimentoGestioneFromSession(impegno, movimentoGestione)) {
			RicercaImpegnoPerChiaveOttimizzato request = model.creaRequestRicercaImpegnoPerChiaveOttimizzato(movimentoGestione);
			RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(request);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String msg = createErrorInServiceInvocationString(request, response);
				addErrori(response);
				
				throw new WebServiceInvocationFailureException(msg);
			}
			if(response.getImpegno() == null) {
				String msg = ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", chiaveMovimentoGestione).getTesto();
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", chiaveMovimentoGestione));
				log.info(methodName, msg);
				throw new WebServiceInvocationFailureException(msg);
			}
			
			Impegno impegno = response.getImpegno();
			inizializzazioneListe(impegno);
			
//			sessionHandler.setParametro(BilSessionParameter.IMPEGNO, impegno);
//		}
		return response;
	}
	
	/**
	 * Controllo delle condizioni di validita dell'impegno.
	 * <br/>
	 * <ul>
	 *     <li>
	 *         L'impegno o il subimpegno selezionato deve essere in stato <code>DEFINITIVO</code> (<code>D</code>), altrimenti viene visualizzato il messaggio
	 *         <code>&lt;FIN_ERR_0096, Impegno non in stato Definitivo&gt;</code> o <code>&lt;FIN_ERR_0098, SubImpegno non in stato Definitivo&gt;</code>;
	 *     </li>
	 *     <li>
	 *         La disponibilit&agrave; dell'impegno deve essere maggiore di 0, altrimenti visualizzare il seguente messaggio
	 *         <code>COR_ERR_0044 - Operazione non consentita (messaggio: 'Non vi &eacute; disponibilit&agrave; sull'impegno')&gt;</code>.
	 *         <br/>
	 *         Se il parametro definito in fase di configurazione della cassa <code>CassaEconomale.limiteImporto</code> &eacute; stato valorizzato (diverso da 0),
	 *         occorre controllare che la disponibilit&agrave; dell'impegno sia maggiore; altrimenti visualizzare il seguente messaggio
	 *         <code>COR_ERR_0044 - Operazione non consentita (messaggio: 'La disponibilit&agrave; dell'impegno &eacute; inferiore al limite stabilito in fase di configurazione')&gt;</code>;
	 *     </li>
	 *     <li>
	 *         L'importo della richiesta da pagare deve essere inferiore o uguale alla disponibilit&agrave; dell'impegno; altrimenti visualizzare il seguente messaggio
	 *         <code>&gt;COR_ERR_0044 - Operazione non consentita (messaggio: 'Non vi &eacute; disponibilit&agrave; sull'impegno per pagare la richiesta')&gt;</code>
	 *     </li>
	 * </ul>
	 * 
	 * @param impegno              l'impegno da validare
	 * @param tipoErroreDefinitivo il tipo di errore per lo statoOperativo non Definitivo
	 * @param strSul               la stringa da usare per l'articolo 'sul'
	 * @param strDel               la stringa da usare per l'articolo 'del'
	 */
	private void checkMovimentoGestione(Impegno impegno, TipoErrore tipoErroreDefinitivo, String strSul, String strDel) {
		// Stato definitivo
		checkCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(impegno.getStatoOperativoMovimentoGestioneSpesa()), tipoErroreDefinitivo.getErrore());
		
		// Disponibilita' > 0
		checkCondition(impegno.getDisponibilitaLiquidare() != null && impegno.getDisponibilitaLiquidare().signum() > 0,
				ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Non vi &eacute; disponibilit&agrave; " + strSul));
		// Se limite importo => disponibilita' > limite importo
		checkCondition(model.getCassaEconomale() == null || model.getCassaEconomale().getLimiteImporto() == null || impegno.getDisponibilitaLiquidare() == null
				|| model.getCassaEconomale().getLimiteImporto().compareTo(impegno.getDisponibilitaLiquidare()) < 0,
				ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("La disponibilit&agrave; " + strDel + " &eacute; inferiore al limite stabilito in fase di configurazione"));
		
		// Importo richiesta < disponibilita'
		BigDecimal importo = ottieniImporto();
		checkCondition(impegno.getDisponibilitaLiquidare() == null || importo.compareTo(impegno.getDisponibilitaLiquidare()) <= 0,
				ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Non vi &eacute; disponibilit&agrave; " + strSul + " per pagare la richiesta"));
	}

	/**
	 * Ottiene l'importo dal model.
	 * 
	 * @return l'importo
	 */
	protected BigDecimal ottieniImporto() {
		return model.getRichiestaEconomale() != null && model.getRichiestaEconomale().getImporto() != null ? model.getRichiestaEconomale().getImporto() : BigDecimal.ZERO;
	}

	/**
	 * Inizializzazione delle liste dell'impegno.
	 * 
	 * @param impegno l'impegno da inizializzare
	 */
	private void inizializzazioneListe(Impegno impegno) {
		if(impegno.getElencoSubImpegni() == null) {
			impegno.setElencoSubImpegni(new ArrayList<SubImpegno>());
		}
		// Inizializzazione mutui
		if(impegno.getListaVociMutuo() == null) {
			impegno.setListaVociMutuo(new ArrayList<VoceMutuo>());
		}
		// Inizializzazione mutui sui subimpegni
		for(SubImpegno si : impegno.getElencoSubImpegni()) {
			if(si.getListaVociMutuo() == null) {
				si.setListaVociMutuo(new ArrayList<VoceMutuo>());
			}
		}
	}
	
	/**
	 * rimuove l'impegno dalla sessione
	 */
	protected void cleanImpegnoFromSession() {
		sessionHandler.setParametro(BilSessionParameter.IMPEGNO, null);
	}

}
