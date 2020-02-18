/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospese;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.BaseInserisciAggiornaRichiestaEconomaleAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese.BaseInserisciAggiornaAnticipoSpeseCassaEconomaleModel;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe base di action per l'inserimento della richiesta.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 * 
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseInserisciAggiornaAnticipoSpeseCassaEconomaleAction<M extends BaseInserisciAggiornaAnticipoSpeseCassaEconomaleModel> extends BaseInserisciAggiornaRichiestaEconomaleAction<M> {
	

	/** Per la serializzazione */
	private static final long serialVersionUID = -6373338357696380068L;
	
	
	/** Nome del model dell'inserimento per la sessione */
	public static final String MODEL_SESSION_NAME_INSERIMENTO = "InserisciAnticipoSpeseCassaEconomaleModel";
	/** Nome del model dell'aggiornamento per la sessione */
	public static final String MODEL_SESSION_NAME_AGGIORNAMENTO= "AggiornaAnticipoSpeseCassaEconomaleModel";
	/** Nome del model dell'inserimento per la sessione */
	public static final String MODEL_SESSION_NAME_INSERIMENTO_RENDICONTO = "InserisciRendicontoAnticipoSpeseCassaEconomaleModel";
	/** Nome del model dell'aggiornamento per la sessione */
	public static final String MODEL_SESSION_NAME_AGGIORNAMENTO_RENDICONTO = "AggiornaRendicontoAnticipoSpeseCassaEconomaleModel";
	
	
	@Override
	protected void caricaListe() throws WebServiceInvocationFailureException {
		caricaListeClassificatoriGenerici();

	}
	
	@Override
	protected void impostaDatiCopia(RichiestaEconomale richiestaEconomale) {
		super.impostaDatiCopia(richiestaEconomale);
		model.getRichiestaEconomale().setNote(richiestaEconomale.getNote()!=null ? richiestaEconomale.getNote() : "");
		model.getRichiestaEconomale().setImporto(richiestaEconomale.getImporto()!=null ? richiestaEconomale.getImporto() : BigDecimal.ZERO);
		//model.setListaGiustificativo(richiestaEconomale.getGiustificativi());
	}
	
	@Override
	protected void impostaDatiAggiornamento(RichiestaEconomale richiestaEconomale) {
		super.impostaDatiAggiornamento(richiestaEconomale);
		model.getRichiestaEconomale().setNote(richiestaEconomale.getNote()!=null ? richiestaEconomale.getNote() : "");
		model.getRichiestaEconomale().setImporto(richiestaEconomale.getImporto()!=null ? richiestaEconomale.getImporto() : BigDecimal.ZERO);
		
	}
	/**
	 * Validazione per il metodo {@link #completeStep1()}.
	 */
	public void validateCompleteStep1() {
		final String methodName = "validateCompleteStep1";
		
		checkNotNull(model.getRichiestaEconomale(), "Richiesta", true);
		
		// Ho la richiesta
		RichiestaEconomale re = model.getRichiestaEconomale();
		try {
			checkMatricola(re.getSoggetto());
		} catch(ParamValidationException pve) {
			log.debug(methodName, "Errore di validazione per il soggetto: " + pve.getMessage() + ". Ma continuo con la validazione per ottenere gli altri dati");
		} catch(WebServiceInvocationFailureException wsife) {
			log.debug(methodName, "Errore di validazione per il soggetto: " + wsife.getMessage() + ". Ma continuo con la validazione per ottenere gli altri dati");
		}
		
		checkNotNullNorEmpty(re.getDescrizioneDellaRichiesta(), "Descrizione della spesa");

	}
	
	@Override
	protected boolean isValidTipoRichiestaEconomale(RichiestaEconomale richiestaEconomale) {
		return richiestaEconomale != null
			&& richiestaEconomale.getTipoRichiestaEconomale() != null
			&& BilConstants.CODICE_TIPO_RICHIESTA_ECONOMALE_ANTICIPO_SPESE.getConstant().equals(richiestaEconomale.getTipoRichiestaEconomale().getCodice());
	}
	
	@Override
	protected void checkValidDate(Date date, String nomeCampo) {
		final String methodName = "checkValidDate";
		try {
			checkNotNull(date, nomeCampo, true);
		} catch(ParamValidationException pve) {
			log.debug(methodName, "Il campo " + nomeCampo + " e' null. E' inutile proseguire con i controlli");
			return;
		}
		
		Calendar cal = Calendar.getInstance();
		int nowYear = cal.get(Calendar.YEAR);
		
		cal.setTime(date);
		int chosenYear = cal.get(Calendar.YEAR);
		
		checkCondition(chosenYear <= nowYear, ErroreCore.FORMATO_NON_VALIDO.getErrore(nomeCampo, ": non deve essere successivo all'anno corrente"));
	}
	
}
