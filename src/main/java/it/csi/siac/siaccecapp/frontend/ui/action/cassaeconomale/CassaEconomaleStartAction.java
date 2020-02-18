/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.CassaEconomaleStartModel;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.TipologiaRichiestaCassaEconomale;
import it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.tipologiarichiesta.TipologiaRichiestaCassaEconomaleFactory;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;

/**
 * Classe di action per la gestione della pagina iniziale della cassa economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/12/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class CassaEconomaleStartAction extends CassaEconomaleBaseAction<CassaEconomaleStartModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3679352540674181896L;

	/** Risultato per la presenza di un'unica cassa */
	private static final String ONLY_ONE = "onlyOne";
	
	@Override
	public void prepare() throws Exception {
		// Pulisco il model
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}
	
	@Override
	public String execute() {
		final String methodName = "execute";
		
		List<CassaEconomale> listaCasseEconomali;
		try {
			listaCasseEconomali = ottieniListaCasseEconomali();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			throw new GenericFrontEndMessagesException(wsife.getMessage(), wsife);
		}
		model.setListaCassaEconomale(listaCasseEconomali);
		
		boolean unicaCassa = listaCasseEconomali.size() == 1;
		model.setNecessarioSelezionareCassa(!unicaCassa);
		
		// Se solo una, redirigo subito allo step successivo
		if(unicaCassa) {
			log.debug(methodName, "Un'unica cassa economale e' disponibile: la seleziono subito");
			CassaEconomale cassaEconomale = listaCasseEconomali.get(0);
			// Non imposto nel model perche' tanto verrebbe cancellata subito
			sessionHandler.setParametro(BilSessionParameter.CASSA_ECONOMALE_TEMP, cassaEconomale);
			return ONLY_ONE;
		}
		// Se numero casse > 1, allora faccio scegliere all'utente
		return SUCCESS;
	}

	/**
	 * Preparazione per il metodo {@link #selectCassa()}.
	 */
	public void prepareSelectCassa() {
		model.setCassaEconomale(null);
		// Obbligo l'utente al ritorno sulla pagina a riselezionare la cassa
		model.setNecessarioSelezionareCassa(true);
		// Controllo se ce ne fosse una in sessione
		CassaEconomale cassaInSessione = sessionHandler.getParametro(BilSessionParameter.CASSA_ECONOMALE_TEMP);
		if(cassaInSessione != null) {
			// Pulisco quella che era in sessione e la reimposto nel model
			model.setCassaEconomale(cassaInSessione);
			sessionHandler.setParametro(BilSessionParameter.CASSA_ECONOMALE_TEMP, null);
		}
	}
	
	/**
	 * Selezione della cassa economale.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String selectCassa() {
		final String methodName = "selectCassa";
		try {
			// Ricerca dettaglio della cassa economale
			ricercaDettaglioCassaEconomale();
			// Ricerca delle tipologie
			ricercaTipologie();
		} catch(WebServiceInvocationFailureException wsife) {
			// Fallimento: esco
			log.info(methodName, "Errore nella selezione della cassa con uid " + model.getCassaEconomale().getUid());
			return INPUT;
		}
		
		// Ricerca delle operazioni extra
		ricercaOperazioniExtra();
		// Ho selezionato la cassa: sono a posto
		model.setNecessarioSelezionareCassa(false);
		return SUCCESS;
	}
	
	/**
	 * Ricerca delle tipologie per la cassa.
	 */
	private void ricercaTipologie() {
		List<TipologiaRichiestaCassaEconomale> listaTipologiaRichiestaCassaEconomale = TipologiaRichiestaCassaEconomaleFactory.creaTipologieOperazione(sessionHandler.getAzioniConsentite());
		model.setListaTipologiaRichiestaCassaEconomale(listaTipologiaRichiestaCassaEconomale);
	}

	/**
	 * Ricerca delle operazioni extra
	 */
	private void ricercaOperazioniExtra() {
		List<TipologiaRichiestaCassaEconomale> listaOperazioniExtra = TipologiaRichiestaCassaEconomaleFactory.creaOperazioniExtra(sessionHandler.getAzioniConsentite());
		model.setListaOperazioniExtra(listaOperazioniExtra);
	}

	/**
	 * Validazione per il metodo {@link #selectCassa()}.
	 */
	public void validateSelectCassa() {
		checkNotNullNorInvalidUid(model.getCassaEconomale(), "Cassa");
	}
	
	/**
	 * Ingresso nella pagina iniziale.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String frontPage() {
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		caricaCassaEconomaleDaSessione();
		return SUCCESS;
	}

	/**
	 * Carico la cassa economale dalla sessione. Ci&oacute; permette di aggiornare i dati della cassa e di visualizzarli immediatamente
	 */
	private void caricaCassaEconomaleDaSessione() {
		CassaEconomale cassaEconomale = sessionHandler.getParametro(BilSessionParameter.CASSA_ECONOMALE);
		model.setCassaEconomale(cassaEconomale);
		if(!hasErrori() && !TipoDiCassa.CONTANTI.equals(cassaEconomale.getTipoDiCassa()) && StringUtils.isBlank(cassaEconomale.getNumeroContoCorrente())) {
			addMessaggio(ErroreBil.ERRORE_GENERICO.getErrore("La cassa economale non ha un numero di conto corrente associato: alcune funzionalita' non saranno disponibili"));
		}
	}
	
}
