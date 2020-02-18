/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.provvisoriocassa;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.provvisoriocassa.RisultatiRicercaSinteticaProvvisorioDiCassaModel;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;

/**
 * Action per i risultati di ricerca del provvisorio di cassa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 11/03/2016
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaSinteticaProvvisorioDiCassaAction extends GenericBilancioAction<RisultatiRicercaSinteticaProvvisorioDiCassaModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5954701485309106312L;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		Integer startPosition = Integer.valueOf(0);
		Integer startPositionInSessione = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		log.debug(methodName, "Start position in sessione: " + startPositionInSessione);
		if (startPositionInSessione != null) {
			startPosition = startPositionInSessione;
		}
		model.setSavedDisplayStart(startPosition);
		
		log.debug(methodName, "StartPosition = " + startPosition);
		
		TipoProvvisorioDiCassa tipoProvvisorio = sessionHandler.getParametro(BilSessionParameter.TIPO_PROVVISORIO);
		impostaNomeAzione(tipoProvvisorio);
		
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		leggiEventualiErroriAzionePrecedente();
		
		return SUCCESS;
	}
	
	private void impostaNomeAzione(TipoProvvisorioDiCassa tipoProvvisorio) {
		if(TipoProvvisorioDiCassa.E.equals(tipoProvvisorio)){
			model.setNomeAzioneSuccessiva("risultatiRicercaSinteticaProvvisorioDiCassa_inserisciDocumentoEntrataPerProvvisori.do");
		}else{
			model.setNomeAzioneSuccessiva("risultatiRicercaSinteticaProvvisorioDiCassa_inserisciDocumentoSpesaPerProvvisori.do");
		}
		
	}

	/**
	 * Redirezione all'associazione dellle quote di entrata
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String associaDocumentoEntrata(){
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Redirezione all'associazione dellle quote di spesa
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String associaDocumentoSpesa(){
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, Boolean.TRUE);
		return SUCCESS;
	}
	
	/**
	 * Redirezione all'inserimento del documento di entrata dopo aver impostato in sessione i provvisori selezionati
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciDocumentoEntrataPerProvvisori(){
		if( model.getListaUidProvvisorio() == null ||  model.getListaUidProvvisorio().isEmpty()){
			addErrore(ErroreCore.NESSUN_ELEMENTO_SELEZIONATO.getErrore());
			return INPUT;
		}
		sessionHandler.setParametro(BilSessionParameter.LISTA_UID_PROVVISORI, model.getListaUidProvvisorio());
		sessionHandler.setParametro(BilSessionParameter.TOTALE_PROVVISORI_SELEZIONATI, model.getTotaleProvvisoriSelezionati());
		return SUCCESS;
	}
	
	/**
	 * Redirezione all'inserimento del documento di spesa dopo aver impostato in sessione i provvisori selezionati
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserisciDocumentoSpesaPerProvvisori(){
		if( model.getListaUidProvvisorio() == null ||  model.getListaUidProvvisorio().isEmpty()){
			addErrore(ErroreCore.NESSUN_ELEMENTO_SELEZIONATO.getErrore());
			return INPUT;
		}
		sessionHandler.setParametro(BilSessionParameter.LISTA_UID_PROVVISORI, model.getListaUidProvvisorio());
		sessionHandler.setParametro(BilSessionParameter.TOTALE_PROVVISORI_SELEZIONATI, model.getTotaleProvvisoriSelezionati());
		return SUCCESS;
	}
	
}
