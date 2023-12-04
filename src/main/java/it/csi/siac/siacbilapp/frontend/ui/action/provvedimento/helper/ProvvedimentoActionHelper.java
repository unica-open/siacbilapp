/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.provvedimento.helper;


import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaPuntualeProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaPuntualeProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.action.BaseActionHelper;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

public class ProvvedimentoActionHelper extends BaseActionHelper {

	@Autowired protected transient ProvvedimentoService provvedimentoService;

	public ProvvedimentoActionHelper(GenericBilancioAction<? extends GenericBilancioModel> action) {
		super(action);
	}
	
	public void findAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		final String methodName = "checkAttoAmministrativo";
		
		if (attoAmministrativo == null || attoAmministrativo.getUid() > 0) {
			return;
		}
		
		if (! attoAmministrativoValorizzato(attoAmministrativo)) {
			return;
		}
		
		validateAttoAmministrativo(attoAmministrativo);

		RicercaPuntualeProvvedimento request = creaRequestRicercaPuntualeProvvedimento(attoAmministrativo, StatoOperativoAtti.DEFINITIVO.name());
		action.logServiceRequest(request);
		RicercaPuntualeProvvedimentoResponse response = provvedimentoService.ricercaPuntualeProvvedimento(request);
		action.logServiceResponse(response);
		
		if(response.hasErrori()) {
			log.info(methodName, action.createErrorInServiceInvocationString(RicercaProvvedimento.class, response));
			action.addErrori(response);
			action.throwExceptionFromErrori(response.getErrori());
		}
		
		AttoAmministrativo aa = response.getAttoAmministrativo();
		action.checkCondition(aa != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("Provvedimento selezionato"), true);

		attoAmministrativo.setUid(aa.getUid());
		
		action.checkCondition(StatoOperativoAtti.DEFINITIVO.name().equals(aa.getStatoOperativo()),
				ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Provvedimento", aa.getStatoOperativo()), true);
	}

	public void validateAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		action.checkCondition(
				attoAmministrativo == null || 
				attoAmministrativo.getUid() > 0 ||
				! attoAmministrativoValorizzato(attoAmministrativo) ||

				attoAmministrativo.getNumero() > 0 && 
				attoAmministrativo.getAnno() > 0 &&
				attoAmministrativo.getTipoAtto().getUid() > 0, 
				
			ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Numero, anno o tipo atto provvedimento"), true);
	}
	
	public List<TipoAtto> caricaListaTipoAtto() throws WebServiceInvocationFailureException {
		List<TipoAtto> list = action.getSessionHandler().getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		if(list == null) {
			TipiProvvedimento request = action.getModel().creaRequestTipiProvvedimento();
			action.logServiceRequest(request);
			TipiProvvedimentoResponse response = provvedimentoService.getTipiProvvedimento(request);
			action.logServiceResponse(response);
			
			if(response.hasErrori()) {
				action.addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaTipoAtto");
			}
			
			list = response.getElencoTipi();
			action.getSessionHandler().setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, list);
		}
		
		return list;
	}
	
	private RicercaPuntualeProvvedimento creaRequestRicercaPuntualeProvvedimento(AttoAmministrativo attoAmministrativo, String statoOperativo) {
		RicercaPuntualeProvvedimento request = action.getModel().creaRequest(RicercaPuntualeProvvedimento.class);
		
		request.setEnte(action.getModel().getEnte());
		
		RicercaAtti ricercaAtti = new RicercaAtti();
		
		ricercaAtti.setAnnoAtto(attoAmministrativo.getAnno());
		ricercaAtti.setNumeroAtto(attoAmministrativo.getNumero());
		ricercaAtti.setStatoOperativo(StringUtils.isBlank(statoOperativo) ? null : statoOperativo);
		ricercaAtti.setTipoAtto(action.getModel().impostaEntitaFacoltativa(attoAmministrativo.getTipoAtto()));
		ricercaAtti.setStrutturaAmministrativoContabile(action.getModel().impostaEntitaFacoltativa(attoAmministrativo.getStrutturaAmmContabile()));
		request.setRicercaAtti(ricercaAtti);
		
		return request;
	}

	public boolean attoAmministrativoValorizzato(AttoAmministrativo attoAmministrativo) {
		return 
			attoAmministrativo != null && (
				attoAmministrativo.getAnno() > 0 ||
				attoAmministrativo.getNumero() > 0 ||
				(attoAmministrativo.getTipoAtto() != null && attoAmministrativo.getTipoAtto().getUid() > 0) ||
				(attoAmministrativo.getStrutturaAmmContabile() != null && attoAmministrativo.getStrutturaAmmContabile().getUid() > 0)
		);
	}
}
