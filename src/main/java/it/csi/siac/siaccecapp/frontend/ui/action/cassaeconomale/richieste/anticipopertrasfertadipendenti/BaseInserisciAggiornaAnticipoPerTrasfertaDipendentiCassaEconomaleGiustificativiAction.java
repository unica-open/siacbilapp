/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipopertrasfertadipendenti;

import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipopertrasfertadipendenti.BaseInserisciAggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleModel;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.StatoOperativoGiustificativi;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Classe di action base per l'inserimento o aggiornamento dell'anticipo spese per trasferta dipendenti, azioni sui giustificativi.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 16/02/2015
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseInserisciAggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleGiustificativiAction<M extends BaseInserisciAggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleModel>
		extends BaseInserisciAggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3205854731058801029L;

	/**
	 * Ottiene la lista dei giustificativi correntemente legata all'anticipo.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaGiustificativi() {
		// Segnaposto per l'invocazione
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #addGiustificativo()}.
	 */
	public void prepareAddGiustificativo() {
		model.setGiustificativo(null);
	}
	
	/**
	 * Aggiunge un giustificativo.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String addGiustificativo() {
		model.getGiustificativo().setStatoOperativoGiustificativi(StatoOperativoGiustificativi.VALIDO);
		model.getListaGiustificativo().add(model.getGiustificativo());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #addGiustificativo()}.
	 */
	public void validateAddGiustificativo() {
		validazioneCampiGiustificativo();
	}
	
	/**
	 * Validazione dei campi del giustificativo.
	 */
	protected void validazioneCampiGiustificativo() {
		// Se non ho il giustificativo, e' inutile prosegire con la validazione
		checkNotNull(model.getGiustificativo(), "Giustificativo", true);
		
		Giustificativo giustificativo = model.getGiustificativo();
		checkNotNullNorInvalidUid(giustificativo.getTipoGiustificativo(), "Tipo");
		
		Valuta valutaEuro = getValutaEuro();
		giustificativo.setValuta(valutaEuro);
		
		// Caricamento del tipo giustificativo
		caricaTipoGiustificativo(giustificativo);
		// Imposto la data
		giustificativo.setDataEmissione(new Date());
	}
	
	/**
	 * Carica il tipo di giustificativo.
	 * 
	 * @param giustificativo il giustificativo da caricare
	 */
	protected void caricaTipoGiustificativo(Giustificativo giustificativo) {
		List<TipoGiustificativo> listaTipoGiustificativo = model.getListaTipoGiustificativo();
		TipoGiustificativo foundTipoGiustificativo = ComparatorUtils.searchByUidEventuallyNull(listaTipoGiustificativo, giustificativo.getTipoGiustificativo());
		giustificativo.setTipoGiustificativo(foundTipoGiustificativo);
	}

	/**
	 * Ottiene la valuta euro
	 * 
	 * @return la valuta euro, se presente; <code>null</code> in caso contrario
	 */
	private Valuta getValutaEuro() {
		List<Valuta> listaValutaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_VALUTA);
		for(Valuta valuta : listaValutaInSessione){
			if(BilConstants.VALUTA_CODICE_EURO.getConstant().equals(valuta.getCodice())){
				return valuta;
			}
		}
		addActionError("");
		return null;
	}

	/**
	 * Preparazione per il metodo {@link #removeGiustificativo()}.
	 */
	public void prepareRemoveGiustificativo() {
		model.setRowNumber(null);
	}
	
	/**
	 * Rimuove un giustificativo.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String removeGiustificativo() {
		int row = model.getRowNumber().intValue();
		model.getListaGiustificativo().remove(row);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #removeGiustificativo()}.
	 */
	public void validateRemoveGiustificativo() {
		checkNotNull(model.getRowNumber(), "Giustificativo da eliminare");
	}
	
	/**
	 * Preparazione per il metodo {@link #updateGiustificativo()}.
	 */
	public void prepareUpdateGiustificativo() {
		model.setGiustificativo(null);
		model.setRowNumber(null);
	}
	
	/**
	 * Aggiorna un giustificativo.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String updateGiustificativo() {
		int row = model.getRowNumber().intValue();
		model.getListaGiustificativo().set(row, model.getGiustificativo());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #updateGiustificativo()}.
	 */
	public void validateUpdateGiustificativo() {
		checkNotNull(model.getRowNumber(), "Giustificativo da aggiornare", true);
		validazioneCampiGiustificativo();
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterListaTipoGiustificativo() {
		return BilSessionParameter.LISTA_TIPO_GIUSTIFICATIVO_RIMBORSO;
	}
}
