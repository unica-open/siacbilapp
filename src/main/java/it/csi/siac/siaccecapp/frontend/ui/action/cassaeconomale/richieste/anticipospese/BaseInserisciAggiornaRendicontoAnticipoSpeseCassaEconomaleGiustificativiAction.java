/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospese;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese.BaseInserisciAggiornaRendicontoAnticipoSpeseCassaEconomaleModel;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.StatoOperativoGiustificativi;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Classe di action per l'inserimento del rendiconto per l'anticipo spese, azioni sui giustificativi.
 * 
 * @author Marchino Alessandro - Valentina Triolo
 * @version 1.0.0 - 18/02/2015
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseInserisciAggiornaRendicontoAnticipoSpeseCassaEconomaleGiustificativiAction<M extends BaseInserisciAggiornaRendicontoAnticipoSpeseCassaEconomaleModel>
		extends BaseInserisciAggiornaAnticipoSpeseCassaEconomaleAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5482371599401992876L;
	
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
		
		model.setRestituzioneTotale(Boolean.FALSE);
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #addGiustificativo()}.
	 */
	public void validateAddGiustificativo() {
		validazioneCampiGiustificativo();
	}

	/**
	 * Validazione dei campi per il giustificativo
	 */
	protected void validazioneCampiGiustificativo() {
		// Se non ho il giustificativo, e' inutile prosegire con la validazione
		checkNotNull(model.getGiustificativo(), "Giustificativo", true);
		
		Giustificativo giustificativo = model.getGiustificativo();
		
		impostaValutaEuro(giustificativo);
		
		checkNotNull(giustificativo.getImportoGiustificativo(), "Importo");
		checkCondition(giustificativo.getImportoGiustificativo() == null || giustificativo.getImportoGiustificativo().signum() >= 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", "non deve essere negativo"));
		checkNotNullNorInvalidUid(giustificativo.getTipoGiustificativo(), "Tipo", true);
		caricaTipoGiustificativo(giustificativo);
		
		boolean isFattura = isFattura(giustificativo.getTipoGiustificativo());
		checkCondition(!isFattura || giustificativo.getAnnoProtocollo() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno"));
		checkCondition(!isFattura || StringUtils.isNotBlank(giustificativo.getNumeroProtocollo()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero protocollo"));
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

	/**
	 * Esclude il giustificativo dal pagamento.
	 * <br/>
	 * <strong>LOTTO H</strong>: Non ancora implementato.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String escludiPagamento() {
		// TODO: non ancora implementato
		addErrore(ErroreCore.TIPO_AZIONE_NON_SUPPORTATA.getErrore("Esclusione dal pagamento"));
		return SUCCESS;
	}
	
	/**
	 * Imposta la restituzione totale.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String restituzioneTotale() {
		model.setRestituzioneTotale(Boolean.TRUE);
		model.getListaGiustificativo().clear();
		return SUCCESS;
	}
	

	/**
	 * Imposta la valuta ad EURO.
	 * 
	 * @param giustificativo il giustificativo in cui impostare la valuta
	 */
	private void impostaValutaEuro(Giustificativo giustificativo) {
		if(giustificativo.getValuta() == null || giustificativo.getValuta().getUid() == 0) {
			Valuta valuta = new Valuta();
			valuta.setUid(model.getUidValutaEuro());
			
			giustificativo.setValuta(valuta);
		}
	}
	
	/**
	 * Controlla se il tipo giustificativo sia <em>FATTURA</em>.
	 * 
	 * @param tipoGiustificativo il tipo giustificativo da controllare.
	 * 
	 * @return <code>true</code> se il tipo giustificativo &eacute; fattura; <code>false</code> in caso contrario
	 */
	private boolean isFattura(TipoGiustificativo tipoGiustificativo) {
		return tipoGiustificativo != null && BilConstants.CODICE_TIPO_GIUSTIFICATIVO_FATTURA.getConstant().equals(tipoGiustificativo.getCodice());
	}

}
