/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.anticipospesepermissione;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione.BaseInserisciAggiornaRendicontoAnticipoSpesePerMissioneCassaEconomaleModel;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Classe di action per l'inserimento del rendiconto per l'anticipo spese per missione, azioni sui giustificativi.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/02/2015
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseInserisciAggiornaRendicontoAnticipoSpesePerMissioneCassaEconomaleGiustificativiAction<M extends BaseInserisciAggiornaRendicontoAnticipoSpesePerMissioneCassaEconomaleModel>
		extends BaseInserisciAggiornaAnticipoSpesePerMissioneCassaEconomaleGiustificativiAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5482371599401992876L;

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
	 * Imposta la restituzione totale.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String restituzioneAltroUfficio() {
		model.setRestituzioneAltroUfficio(Boolean.TRUE);
		model.getListaGiustificativo().clear();
		return SUCCESS;
	}
	
	@Override
	public String addGiustificativo() {
		model.setRestituzioneTotale(Boolean.FALSE);
		model.setRestituzioneAltroUfficio(Boolean.FALSE);
		return super.addGiustificativo();
	}
}
