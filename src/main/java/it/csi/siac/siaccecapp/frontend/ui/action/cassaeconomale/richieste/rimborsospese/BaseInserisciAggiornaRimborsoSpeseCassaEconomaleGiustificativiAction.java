/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.rimborsospese;

import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.rimborsospese.BaseInserisciAggiornaRimborsoSpeseCassaEconomaleModel;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.StatoOperativoGiustificativi;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Classe di action base per l'inserimento o aggiornamento del rimborso spese, azioni sui giustificativi.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/02/2015
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseInserisciAggiornaRimborsoSpeseCassaEconomaleGiustificativiAction<M extends BaseInserisciAggiornaRimborsoSpeseCassaEconomaleModel>
		extends BaseInserisciAggiornaRimborsoSpeseCassaEconomaleAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8671289149803983686L;

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
	private void validazioneCampiGiustificativo() {
		// Se non ho il giustificativo, e' inutile prosegire con la validazione
		checkNotNull(model.getGiustificativo(), "Giustificativo", true);
		
		Giustificativo giustificativo = model.getGiustificativo();
		checkNotNullNorInvalidUid(giustificativo.getTipoGiustificativo(), "Tipo");
		checkNotNullNorInvalidUid(giustificativo.getValuta(), "Valuta", true);
		
		// Ho la valuta. Controllo se ho selezionato l'EURO
		boolean isEuro = isEuro(giustificativo.getValuta());
		checkCondition(isEuro || giustificativo.getImportoGiustificativoInValuta() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo valuta estera"));
		checkCondition(isEuro || giustificativo.getImportoGiustificativoInValuta() == null || giustificativo.getImportoGiustificativoInValuta().signum() >= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo valuta estera", ": non puo' essere negativo"));
		
		checkCondition(!isEuro || giustificativo.getImportoGiustificativo() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo"));
		checkCondition(!isEuro || giustificativo.getImportoGiustificativo() == null || giustificativo.getImportoGiustificativo().signum() >= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": non puo' essere negativo"));
		
		// Caricamento del tipo giustificativo
		caricaTipoGiustificativo(giustificativo);
	}
	
	@Override
	protected void caricaTipoGiustificativo(Giustificativo giustificativo) {
		List<TipoGiustificativo> listaTipoGiustificativo = model.getListaTipoGiustificativo();
		TipoGiustificativo foundTipoGiustificativo = ComparatorUtils.searchByUidEventuallyNull(listaTipoGiustificativo, giustificativo.getTipoGiustificativo());
		giustificativo.setTipoGiustificativo(foundTipoGiustificativo);
	}
	
	/**
	 * Controlla se la valuta fornita sia l'EURO.
	 * 
	 * @param valuta la valuta da controllare, con il campo uid valorizzato
	 * 
	 * @return <code>true</code> se la valuta e' l'EURO; <code>false</code> in caso contrario
	 */
	private boolean isEuro(Valuta valuta) {
		List<Valuta> listaValutaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_VALUTA);
		Valuta foundValuta = ComparatorUtils.searchByUidEventuallyNull(listaValutaInSessione, valuta);
		return foundValuta != null && BilConstants.VALUTA_CODICE_EURO.getConstant().equals(foundValuta.getCodice());
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
	
}
