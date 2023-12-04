/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.rimborsospese;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.rimborsospese.InserisciRimborsoSpeseCassaEconomaleModel;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Classe di action per l'inserimento della richiesta, azioni sui giustificativi.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 02/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaRimborsoSpeseCassaEconomaleAction.MODEL_SESSION_NAME_INSERIMENTO)
public class InserisciRimborsoSpeseCassaEconomaleGiustificativiAction extends BaseInserisciAggiornaRimborsoSpeseCassaEconomaleAction<InserisciRimborsoSpeseCassaEconomaleModel> {
	
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 335230567003308813L;

	/**
	 * Ottiene la lista dei giustificativi correntemente legata alla richiesta.
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
		
		boolean isTipoFattura = isTipoFattura(giustificativo.getTipoGiustificativo());
		if (isTipoFattura) {
			// verifico i dati che Anno e numero di protocollo siano compilati
			checkNotNull(giustificativo.getAnnoProtocollo(), "Anno");
			checkNotNull(giustificativo.getNumeroProtocollo(), "Numero protocollo");
			
		}
		checkNotNull(giustificativo.getImportoGiustificativo(), "Importo");
		checkCondition(giustificativo.getImportoGiustificativo() == null || giustificativo.getImportoGiustificativo().signum() > 0,
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("Importo",": l'importo deve essere positivo"));
		caricaTipoGiustificativo(giustificativo);
		checkValidNoMandatoryDate(giustificativo.getDataEmissione(), "Data emissione");
		
		if (giustificativo.getValuta() == null || (giustificativo.getValuta().getUid()==0)) {
			impostaValutaEuro(giustificativo);
		}
		
	}
	
	private void impostaValutaEuro(Giustificativo giustificativo) {
		Valuta valuta = new Valuta();
		valuta.setUid(model.getUidValutaEuro());
		giustificativo.setValuta(valuta);
	}

	/**
	 * Controlla se la valuta fornita sia l'EURO.
	 * 
	 * @param tipoGiustificativo il tipo giustificativo da controllare, con il campo uid valorizzato
	 * 
	 * @return <code>true</code> se la valuta e' l'EURO; <code>false</code> in caso contrario
	 */
	private boolean isTipoFattura(TipoGiustificativo tipoGiustificativo) {
		List<TipoGiustificativo> listaTipoGiustificativo = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_GIUSTIFICATIVO_RIMBORSO);
		TipoGiustificativo foundTipoGiustificativo = ComparatorUtils.searchByUidEventuallyNull(listaTipoGiustificativo, tipoGiustificativo);
		return foundTipoGiustificativo != null && BilConstants.CODICE_TIPO_GIUSTIFICATIVO_FATTURA.getConstant().equals(foundTipoGiustificativo.getCodice());
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
	protected AzioneConsentitaEnum[] retrieveAzioniConsentite() {
		return new AzioneConsentitaEnum[] {AzioneConsentitaEnum.CASSA_ECONOMALE_RIMBORSO_SPESE_INSERISCI, AzioneConsentitaEnum.CASSA_ECONOMALE_RIMBORSO_SPESE_ABILITA};
	}
}
