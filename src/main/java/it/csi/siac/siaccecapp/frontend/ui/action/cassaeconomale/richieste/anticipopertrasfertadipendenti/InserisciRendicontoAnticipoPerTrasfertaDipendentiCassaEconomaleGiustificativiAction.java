/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipopertrasfertadipendenti;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipopertrasfertadipendenti.InserisciRendicontoAnticipoPerTrasfertaDipendentiCassaEconomaleModel;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Classe di action per l'inserimento del rendiconto per l'anticipo spese per trasferta, azioni sui giustificativi.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 17/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleAction.MODEL_SESSION_NAME_INSERIMENTO_RENDICONTO)
public class InserisciRendicontoAnticipoPerTrasfertaDipendentiCassaEconomaleGiustificativiAction
		extends BaseInserisciAggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleGiustificativiAction<InserisciRendicontoAnticipoPerTrasfertaDipendentiCassaEconomaleModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6525732093281758913L;
	
	@Override
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
	 * Restituzione totale.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String restituzioneTotale() {
		// TODO
		return SUCCESS;
	}
	
	@Override
	public String addGiustificativo() {
		model.setRestituzioneTotale(null);
		return super.addGiustificativo();
	}
	
	@Override
	protected AzioniConsentite[] retrieveAzioniConsentite() {
		return new AzioniConsentite[] {AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_PER_TRASFERTA_DIPENDENTI_INSERISCI_RENDICONTO, AzioniConsentite.CASSA_ECONOMALE_ANTICIPO_PER_TRASFERTA_DIPENDENTI_ABILITA};
	}
}
