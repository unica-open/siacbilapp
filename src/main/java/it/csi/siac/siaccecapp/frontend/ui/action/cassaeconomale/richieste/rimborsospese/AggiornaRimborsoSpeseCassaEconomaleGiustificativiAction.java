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
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.rimborsospese.AggiornaRimborsoSpeseCassaEconomaleModel;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.TipoGiustificativo;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Classe di action per l'aggiornamento del rimborso spese, azioni sui giustificativi.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/02/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaRimborsoSpeseCassaEconomaleAction.MODEL_SESSION_NAME_AGGIORNAMENTO)
public class AggiornaRimborsoSpeseCassaEconomaleGiustificativiAction
		extends BaseInserisciAggiornaRimborsoSpeseCassaEconomaleGiustificativiAction<AggiornaRimborsoSpeseCassaEconomaleModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2618699544350005335L;
	
	@Override
	public String ottieniListaGiustificativi() {
		// Segnaposto per l'invocazione
		return SUCCESS;
	}
	
	@Override
	public void prepareAddGiustificativo() {
		model.setGiustificativo(null);
	}
	
	@Override
	public String addGiustificativo() {
		model.getListaGiustificativo().add(model.getGiustificativo());
		return SUCCESS;
	}
	
	@Override
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
		//checkNotNullNorEmpty(giustificativo.getNumeroGiustificativo(),"Numero");
		checkNotNull(giustificativo.getImportoGiustificativo(), "Importo");
		checkCondition(giustificativo.getImportoGiustificativo() == null || giustificativo.getImportoGiustificativo().signum() >= 0,
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("Importo",": l'importo deve essere positivo"));

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
		TipoGiustificativo foundTipoGiustificativo= ComparatorUtils.searchByUidEventuallyNull(listaTipoGiustificativo, tipoGiustificativo);
		return foundTipoGiustificativo != null && BilConstants.CODICE_TIPO_GIUSTIFICATIVO_FATTURA.getConstant().equals(foundTipoGiustificativo.getCodice());
	}

	@Override
	public void prepareRemoveGiustificativo() {
		model.setRowNumber(null);
	}
	
	@Override
	public String removeGiustificativo() {
		int row = model.getRowNumber().intValue();
		model.getListaGiustificativo().remove(row);
		return SUCCESS;
	}
	
	@Override
	public void validateRemoveGiustificativo() {
		checkNotNull(model.getRowNumber(), "Giustificativo da eliminare");
	}
	
	@Override
	public void prepareUpdateGiustificativo() {
		model.setGiustificativo(null);
		model.setRowNumber(null);
	}
	
	@Override
	public String updateGiustificativo() {
		int row = model.getRowNumber().intValue();
		model.getListaGiustificativo().set(row, model.getGiustificativo());
		return SUCCESS;
	}
	
	@Override
	public void validateUpdateGiustificativo() {
		checkNotNull(model.getRowNumber(), "Giustificativo da aggiornare", true);
		validazioneCampiGiustificativo();
	}
	
	@Override
	protected AzioneConsentitaEnum[] retrieveAzioniConsentite() {
		return new AzioneConsentitaEnum[] {AzioneConsentitaEnum.CASSA_ECONOMALE_RIMBORSO_SPESE_AGGIORNA, AzioneConsentitaEnum.CASSA_ECONOMALE_RIMBORSO_SPESE_ABILITA};
	}
}
