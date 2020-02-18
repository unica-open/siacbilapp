/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ajax.causale;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.ajax.causale.CausalePreDocumentoEntrataAjaxModel;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;

/**
 * Classe di actoin per il caricamento della causale di entrata per il PreDocumento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/04/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class CausalePreDocumentoEntrataAjaxAction extends GenericBilancioAction<CausalePreDocumentoEntrataAjaxModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7062074207750685611L;
	
	@Autowired private transient PreDocumentoEntrataService preDocumentoEntrataService;

	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		Integer uidTipoCausale = model.getUidTipoCausale();
		Integer uidSAC = model.getUidStrutturaAmministrativoContabile();
		log.debug(methodName, "id tipo: " + uidTipoCausale);
		log.debug(methodName, "id sac: " + uidSAC);
		
		if(uidTipoCausale == null || uidTipoCausale.intValue() == 0) {
			log.debug(methodName, "Id non fornito");
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("tipo causale"));
			return SUCCESS;
		}
		
		// Creazione della request
		RicercaSinteticaCausaleEntrata request = model.creaRequestRicercaSinteticaCausaleEntrata(null);
		
		log.debug(methodName, "Richiamo il WebService");
		RicercaSinteticaCausaleEntrataResponse response = preDocumentoEntrataService.ricercaSinteticaCausaleEntrata(request);
		log.debug(methodName, "Richiamato il WebService");
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Errori nella response");
			addErrori(response);
			return SUCCESS;
		}
		
		model.setListaCausale(response.getCausaliEntrata());
		sessionHandler.setParametro(BilSessionParameter.LISTA_CAUSALE_ENTRATA, response.getCausaliEntrata());
		
		return SUCCESS;
	}
	
	/**
	 * Ricerca di dettaglio per la causale di entrata.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaDettaglio() {
		final String methodName = "ricercaDettaglio";
		
		// Creazione della request
		RicercaDettaglioCausaleEntrata request = model.creaRequestRicercaDettaglioCausaleEntrata();
		logServiceRequest(request);
		RicercaDettaglioCausaleEntrataResponse response = preDocumentoEntrataService.ricercaDettaglioCausaleEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return SUCCESS;
		}
		// Imposto il dato nel model
		model.setCausaleEntrata(response.getCausaleEntrata());
		
		return SUCCESS;
	}
	
	/**
	 * Ricerca delle causali non annullate
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String nonAnnullate() {
		final String methodName = "execute";
		
		// Creazione della request
		RicercaSinteticaCausaleEntrata req = model.creaRequestRicercaSinteticaCausaleEntrata(StatoOperativoCausale.VALIDA);
		RicercaSinteticaCausaleEntrataResponse res = preDocumentoEntrataService.ricercaSinteticaCausaleEntrata(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return SUCCESS;
		}
		
		List<CausaleEntrata> list = res.getCausaliEntrata();
		model.setListaCausale(list);
		sessionHandler.setParametro(BilSessionParameter.LISTA_CAUSALE_ENTRATA_NON_ANNULLATE, list);
		addCausaleAnnullataPredoc();
		
		return SUCCESS;
	}
	
	/**
	 * Aggiunge la causale annullata del predocumento
	 */
	private void addCausaleAnnullataPredoc() {
		final String methodName = "addCausaleAnnullataPredoc";
		if(model.getCausaleOriginale() == null || model.getCausaleOriginale().getUid() == 0) {
			log.debug(methodName, "Causale originale non valorizzata: non aggiungo nulla");
			return;
		}
		CausaleEntrata ce = model.getCausaleOriginale();
		if(!StatoOperativoCausale.ANNULLATA.equals(ce.getStatoOperativoCausale())) {
			log.debug(methodName, "Causale non annullata: non aggiungo nella lista");
			return;
		}
		// Controllo sul tipo
		if(ce.getTipoCausale() == null || ce.getTipoCausale().getUid() != model.getUidTipoCausale().intValue()) {
			log.debug(methodName, "Causale di tipo differente da quello della ricerca: non aggiungo nella lista");
			return;
		}
		List<CausaleEntrata> list = model.getListaCausale();
		CausaleEntrata ceInList = ComparatorUtils.searchByUidEventuallyNull(list, ce);
		if(ceInList != null) {
			log.debug(methodName, "Causale gia' in lista: non aggiungo");
			return;
		}
		log.debug(methodName, "Devo aggiungere la causale nella lista");
		list.add(ce);
	}
	
	/**
	 * Validazione per il metodo {@link #nonAnnullate()}.
	 */
	public void validateNonAnnullate() {
		checkCondition(model.getUidTipoCausale() != null && model.getUidTipoCausale().intValue() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("tipo causale"));
	}
}
