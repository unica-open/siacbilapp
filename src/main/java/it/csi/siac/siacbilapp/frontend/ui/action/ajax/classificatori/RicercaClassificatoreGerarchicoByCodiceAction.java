/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.classificatori;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.ajax.RicercaClassificatoreGerarchicoByCodiceModel;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse;
import it.csi.siac.siaccorser.model.ClassificatoreGerarchico;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Ricerca del classificatore gerarchico a partire dal codice.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/12/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaClassificatoreGerarchicoByCodiceAction extends GenericBilancioAction<RicercaClassificatoreGerarchicoByCodiceModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7304149886836728410L;
	
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	
	/**
	 * Ricerca del siope di spesa
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String siopeSpesa() {
		return findClassificatore("siopeSpesa", BilSessionParameter.SIOPE_SPESA, TipologiaClassificatore.SIOPE_SPESA_I);
	}
	
	/**
	 * Validazione per il metodo {@link #siopeSpesa()}.
	 */
	public void validateSiopeSpesa() {
		baseValidate();
	}
	
	/**
	 * Ricerca del siope di entrata
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String siopeEntrata() {
		return findClassificatore("siopeEntrata", BilSessionParameter.SIOPE_ENTRATA, TipologiaClassificatore.SIOPE_ENTRATA_I);
	}
	
	/**
	 * Validazione per il metodo {@link #siopeEntrata()}.
	 */
	public void validateSiopeEntrata() {
		baseValidate();
	}
	
	/**
	 * Ottiene il classificatore da sessione o da servizio.
	 * 
	 * @param methodName il metodo per cui effettuare l'invocazione
	 * @param sessionParameter il parametro in cui impostare il dato in sessione
	 * @param tipologiaClassificatore la tipologia di classificatore
	 * @return @return una stringa corrispondente al risultato dell'invocazione
	 */
	private String findClassificatore(final String methodName, BilSessionParameter sessionParameter, TipologiaClassificatore tipologiaClassificatore) {
		ClassificatoreGerarchico classificatoreSession = sessionHandler.getParametro(sessionParameter);
		if(classificatoreSession == null || !classificatoreSession.getCodice().equals(model.getClassificatore().getCodice())) {
			// Devo ricercare nuovamente
			log.debug(methodName, "Non utilizzo la cache");
			LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno request = model.creaRequestLeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno(tipologiaClassificatore);
			logServiceRequest(request);
			LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse response = classificatoreBilService.leggiClassificatoreGerarchicoByCodiceAndTipoAndAnno(request);
			logServiceResponse(response);
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				log.debug(methodName, createErrorInServiceInvocationString(LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno.class, response));
				return INPUT;
			}
			if(response.getClassificatore() == null) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("classificatore", model.getClassificatore().getCodice()));
				log.debug(methodName, "Nessun classificatore trovato");
				return INPUT;
			}
			classificatoreSession = response.getCastClassificatore();
			sessionHandler.setParametro(sessionParameter, classificatoreSession);
		}
		
		model.setClassificatore(classificatoreSession);
		
		return SUCCESS;
	}
	
	/**
	 * Validazione base.
	 */
	private void baseValidate() {
		checkNotNull(model.getClassificatore(), "Classificatore", true);
		checkNotNullNorEmpty(model.getClassificatore().getCodice(), "Codice");
	}
	
	/**
	 * Result per la ricerca.
	 * 
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 16/12/2015
	 *
	 */
	public static class ResultClassificatoreGerarchicoByCodice extends CustomJSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = 7386425647497841723L;
		/** Propriet&agrave; da includere nel JSON creato */
		private static final String INCLUDE_PROPERTIES = "classificatore.*";

		/** Empty default constructor */
		public ResultClassificatoreGerarchicoByCodice() {
			super();
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
}
