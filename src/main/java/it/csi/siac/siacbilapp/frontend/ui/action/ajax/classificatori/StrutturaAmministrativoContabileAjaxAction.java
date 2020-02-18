/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.classificatori;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericClassificatoriAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.codifica.ElementoCodificaFactory;
import it.csi.siac.siaccorser.frontend.webservice.ClassificatoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabile;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabileResponse;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;


/**
 * Classe per il caricamento <em>AJAX</em> della Struttura Amministrativo Contabile.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 22/01/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class StrutturaAmministrativoContabileAjaxAction extends GenericClassificatoriAjaxAction<LeggiStrutturaAmminstrativoContabile, LeggiStrutturaAmminstrativoContabileResponse> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6906150985170454009L;
	
	private List<StrutturaAmministrativoContabile> listInSession;
	
	/** Service per i Classificatori del modulo BIL */
	@Autowired private transient ClassificatoreService classificatoreService;
	
	@Override
	protected LeggiStrutturaAmminstrativoContabile definisciRequest(Integer id) {
		return model.creaRequestLeggiStrutturaAmminstrativoContabile();
	}

	@Override
	protected LeggiStrutturaAmminstrativoContabileResponse ottieniResponse(LeggiStrutturaAmminstrativoContabile request) {
		if(StringUtils.isNotBlank(model.getNomeAzioneDecentrata())) {
			return gestisciEventualeAzioneDecentrata(request);
		}
		return ottieniResponseDaServizio(request);
	}

	/**
	 * Gestisce l'eventuale azione decentrata: ottiene le sole SAC relative all'azione, se effettivamente decentrata, o
	 * riconduce al caso base.
	 * 
	 * @param request la request del servizio
	 * 
	 * @return la response
	 */
	private LeggiStrutturaAmminstrativoContabileResponse gestisciEventualeAzioneDecentrata(LeggiStrutturaAmminstrativoContabile request) {
		// Controllo se l'azione sia decentrata: in tal caso mostro solo le SAC corrette
		AzioneConsentita azioneConsentita =
			ComparatorUtils.findAzioneConsentitaByNomeAzione(sessionHandler.getAzioniConsentite(),
				model.getNomeAzioneDecentrata());
		if(azioneConsentita != null) {
			// Sono decentrato: mostro solo le SAC che mi competono
			LeggiStrutturaAmminstrativoContabileResponse response = new LeggiStrutturaAmminstrativoContabileResponse();
			response.setListaStrutturaAmmContabile(ottieniListaStruttureAmministrativoContabiliDaSessione());
			return response;
		}
		return ottieniResponseDaServizio(request);
	}
	
	/**
	 * Ottiene la response invocando il servizio.
	 * 
	 * @param request la request tramite cui effettuare l'invocazione
	 * 
	 * @return la response
	 */
	private LeggiStrutturaAmminstrativoContabileResponse ottieniResponseDaServizio(LeggiStrutturaAmminstrativoContabile request) {
		if(listInSession != null) {
			LeggiStrutturaAmminstrativoContabileResponse response = new LeggiStrutturaAmminstrativoContabileResponse();
			response.setListaStrutturaAmmContabile(listInSession);
			return response;
		} 
		
		return classificatoreService.leggiStrutturaAmminstrativoContabile(request);
	}

	@Override
	protected void impostaLaResponseInSessione(LeggiStrutturaAmminstrativoContabileResponse response) {
		sessionHandler.setParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE,
				listInSession != null ? listInSession : response.getListaStrutturaAmmContabile());
	}

	@Override
	protected void impostaPerInjezioneInVariazione(LeggiStrutturaAmminstrativoContabileResponse response) {
		injettaInVariazione(TipologiaClassificatore.CDC, response.getListaStrutturaAmmContabile());
	}

	@Override
	protected void injettaResponseNelModel(LeggiStrutturaAmminstrativoContabileResponse response) {
		model.setListaElementoCodifica(ElementoCodificaFactory.getInstances(response.getListaStrutturaAmmContabile()));
	}
	
	@Override
	protected void sortDeiRisultati(LeggiStrutturaAmminstrativoContabileResponse response) {
		final String methodName = "sortDeiRisultati";
		try {
			ComparatorUtils.sortDeepByCodice(response.getListaStrutturaAmmContabile());
		} catch (Exception e) {
			log.debug(methodName, "Errore nel sorting dei dati: " + e.getMessage());
		}
	}
	
	@Override
	protected boolean gestisciEventualeIdVuoto(Integer id) {
		// Controllo subito se ho la lista in sessione
		listInSession = sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		return false;
	}
	
}
