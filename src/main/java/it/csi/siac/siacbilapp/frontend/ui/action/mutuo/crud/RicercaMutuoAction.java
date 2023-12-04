/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.mutuo.crud;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.mutuo.base.BaseMutuoAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.mutuo.crud.RicercaMutuoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaSinteticaMutuo;
import it.csi.siac.siacbilser.frontend.webservice.msg.mutuo.RicercaSinteticaMutuoResponse;
import it.csi.siac.siacbilser.model.mutuo.Mutuo;
import it.csi.siac.siaccommon.util.number.NumberUtil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaMutuoAction extends BaseMutuoAction<RicercaMutuoModel> {


	private static final long serialVersionUID = 5407969745524143565L;
	
	@Override
	public void prepareEnterPage() {
		super.prepareEnterPage();
		try {
			model.setListaTipoAtto(provvedimentoActionHelper.caricaListaTipoAtto());
			model.setListaClasseSoggetto(soggettoActionHelper.caricaListaClasseSoggetto());
			model.setListaPeriodoMutuo(mutuoActionHelper.caricaListaPeriodoMutuo());
			model.setListaStatoMutuo(mutuoActionHelper.caricaListaStatoMutuo());
		}
		catch (WebServiceInvocationFailureException e) {
			throwSystemExceptionErroreDiSistema(e);
		}
	}

	public String ricerca() {
		final String methodName = "ricerca";
		
		RicercaSinteticaMutuo req = model.creaRequestRicercaSinteticaMutuo();
		logServiceRequest(req);
		
		RicercaSinteticaMutuoResponse res = mutuoService.ricercaSinteticaMutuo(req);
		logServiceResponse(res);

		if (res.hasErrori()) {
			addErrori(res);
			log.info(methodName, "Si sono verificati errori nell'invocazione del servizio");
			super.callPrepareEnterPageOnErrors();
			return INPUT;
		}
		
		if(res.getMutui().getTotaleElementi() == 0) {
			log.info(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			prepareEnterPage();
			return INPUT;
		}
		
		log.debug(methodName, "Totale: "+res.getMutui().getTotaleElementi());
		log.debug(methodName, "Ricerca effettuata con successo");
		
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_MUTUO, req);
					
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_MUTUO, res.getMutui());
		
		return SUCCESS;
	}
	
	public void validateRicerca() {
		Mutuo mutuo = model.getMutuo();
		
		try {
			provvedimentoActionHelper.validateAttoAmministrativo(mutuo.getAttoAmministrativo());
	
			soggettoActionHelper.findSoggetto(mutuo.getSoggetto());
			
			checkCondition(
					NumberUtil.isValidAndGreaterThanZero(mutuo.getNumero()) ||
					mutuo.getTipoTasso() != null ||
					mutuo.getStatoMutuo() != null || 
					StringUtils.isNotEmpty(mutuo.getOggetto()) ||
					provvedimentoActionHelper.attoAmministrativoValorizzato(mutuo.getAttoAmministrativo()) ||
					mutuo.getSoggetto().getUid() > 0 ||
					mutuo.getPeriodoRimborso().getUid() > 0, 
				ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		} finally {
			callPrepareEnterPageOnErrors();
		}
	}

}
