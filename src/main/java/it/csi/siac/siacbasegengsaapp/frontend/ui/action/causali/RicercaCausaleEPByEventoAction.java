/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.RicercaCausaleEPByEventoModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.result.CustomJSONResult;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaMinimaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaMinimaCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausaleResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Evento;

/**
 * Ricerca della causale EP tramite l'evento. Base.
 * 
 * @version 1.0.0 - 14/10/2015
 * @author Elisa Chiari
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaCausaleEPByEventoAction extends GenericBilancioAction<RicercaCausaleEPByEventoModel>{

	
	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -4036115494313002475L;
	
	@Autowired private transient CausaleService causaleService;

	/**
	 * @return il parametro di sessione in cui salvare la lista causali
	 * */
	protected BilSessionParameter getBilSessionParameterListeCausali(){
		if(Ambito.AMBITO_FIN.equals(model.getAmbito()) || Ambito.AMBITO_INV.equals(model.getAmbito()) ) {
			return BilSessionParameter.LISTA_CAUSALE_EP_LIBERA_GEN;
		}
		if(Ambito.AMBITO_INV.equals(model.getAmbito())) {
			return BilSessionParameter.LISTA_CAUSALE_EP_LIBERA_INV;
		}
		return BilSessionParameter.LISTA_CAUSALE_EP_LIBERA_GSA;
	}
	
	@Override
	public String execute() throws Exception  {
		final String methodName = "execute";

		List<CausaleEP> listaCausaleEP = sessionHandler.getParametro(getBilSessionParameterListeCausali());
		
		// Se la lista delle causali non e' valorizzata, la inizializzo
		if (listaCausaleEP == null) {
			RicercaSinteticaCausale request = model.creaRequestRicercaSinteticaCausale();
			logServiceRequest(request);
			RicercaSinteticaCausaleResponse response = causaleService.ricercaSinteticaCausale(request);
			logServiceResponse(response);

			if (response.hasErrori()) {
				log.debug(methodName, createErrorInServiceInvocationString(RicercaSinteticaCausale.class, response));
				addErrori(response);
				return SUCCESS;
			}

			listaCausaleEP = response.getCausali();
			// Aggiungo il risultato in sessione
			sessionHandler.setParametro(getBilSessionParameterListeCausali(), listaCausaleEP);
		}
		
		// Se non ho selezionato un evento, non filtro alcunche'
		if(model.getEvento() == null || model.getEvento().getUid() == 0) {
			model.setListaCausaleEP(listaCausaleEP);
			return SUCCESS;
		}

		int uidEvento = model.getEvento().getUid();
		List<CausaleEP> listaCausaleEPfiltrata = new ArrayList<CausaleEP>();
		// analizzo la lista e tiro fuori solole causali che corrispondono
		for (CausaleEP causaleEP : listaCausaleEP) {
			for (Evento eventoCausale : causaleEP.getEventi()) {
				if (eventoCausale.getUid() == uidEvento && !listaCausaleEPfiltrata.contains(causaleEP)) {
					listaCausaleEPfiltrata.add(causaleEP);
				}
			}
		}
		model.setListaCausaleEP(listaCausaleEPfiltrata);
		return SUCCESS;
	}
	
	/**
	 * Ricerca minima
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaMinima() {
		final String methodName = "ricercaMinima";

		RicercaMinimaCausale request = model.creaRequestRicercaMinimaCausale();
		logServiceRequest(request);
		RicercaMinimaCausaleResponse response = causaleService.ricercaMinimaCausale(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(RicercaMinimaCausale.class, response));
			addErrori(response);
			return SUCCESS;
		}

		model.setListaCausaleEP(response.getCausali());
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #execute()}.
	 */
	public void validateExecute() {
		checkNotNull(model.getAmbito(), "ambito", true);
		checkCondition(Ambito.AMBITO_FIN.equals(model.getAmbito()) || Ambito.AMBITO_GSA.equals(model.getAmbito())||  Ambito.AMBITO_INV.equals(model.getAmbito()),
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("Ambito", ": deve essere AMBITO_FIN o AMBITO_GSA o AMBITO_INV"));
	}
	
	/**
	 * Ricerca modulare
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaModulare() {
		final String methodName = "ricercaModulare";
		
		RicercaSinteticaModulareCausale req = model.creaRequestRicercaSinteticaModulareCausale();
		RicercaSinteticaModulareCausaleResponse res = causaleService.ricercaSinteticaModulareCausale(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, createErrorInServiceInvocationString(RicercaSinteticaModulareCausale.class, res));
			addErrori(res);
			return INPUT;
		}
		
		model.setListaCausaleEP(res.getCausali());
		return SUCCESS;
	}
	
	/**
	 * Result standard per la ricerca causale EP per evento
	 * @author Marchino Alessandro
	 */
	public static class RicercaCausaleEPByEventoResult extends CustomJSONResult {
		/** Per la serializzazione */
		private static final long serialVersionUID = -694651610185502780L;
		/** Propriet&agrave; da includere nel JSON creato */
		private static final String INCLUDE_PROPERTIES = "errori.*, listaCausaleEP.*";
		/** Empty default constructor */
		public RicercaCausaleEPByEventoResult() {
			setIncludeProperties(INCLUDE_PROPERTIES);
		}
	}
}