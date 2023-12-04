/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.categoriacespiti;

import org.apache.commons.lang3.StringUtils;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccespapp.frontend.ui.model.categoriacespiti.RicercaCategoriaCespitiModel;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespitiResponse;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class GenericTipoBeneAction.
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaCategoriaCespitiAction extends GenericCategoriaCespitiAction<RicercaCategoriaCespitiModel> {

	/** Per la serializzazione*/
	private static final long serialVersionUID = -184989908425366102L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		cleanErroriMessaggiInformazioni();
		caricaListaTipoCalcolo();
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		model.setCategoriaCespiti(null);
		return SUCCESS;		
	}
	
	/**
	 * Validate effettua ricerca.
	 */
	public void validateEffettuaRicerca() {
		CategoriaCespiti categoriaCespiti = model.getCategoriaCespiti();
		boolean indicatoUnCriterioDiRicerca = categoriaCespiti != null &&
				(StringUtils.isNotBlank(categoriaCespiti.getCodice()) ||
				 StringUtils.isNotBlank(categoriaCespiti.getDescrizione()) ||
				 categoriaCespiti.getAliquotaAnnua() != null ||
				 model.idEntitaPresente(categoriaCespiti.getCategoriaCalcoloTipoCespite())
				 );
		checkCondition(indicatoUnCriterioDiRicerca, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
	}
	
	/**
	 * Effettua ricerca.
	 *
	 * @return the string
	 */
	public String effettuaRicerca(){
		final String methodName = "effettuaRicerca";
		RicercaSinteticaCategoriaCespiti request = model.creaRequestRicercaSinteticaCategoriaCespiti();
		RicercaSinteticaCategoriaCespitiResponse response = classificazioneCespiteService.ricercaSinteticaCategoriaCespiti(request);
		if(response.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(response);
			return INPUT;
		}
		
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo");

		// Imposto in sessione i dati
		impostaParametriInSessione(request, response);
		
		return SUCCESS;
	}
	
	/**
	 * Impostazione dei parametri in sessione
	 * @param request  la request da impostare
	 * @param response la response con i parametri da impostare
	 */
	private void impostaParametriInSessione(RicercaSinteticaCategoriaCespiti request, RicercaSinteticaCategoriaCespitiResponse response) {
				
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CATEGORIA_CESPITI, request);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CATEGORIA_CESPITI, response.getListaCategoriaCespiti());
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
	}

	

	

}
