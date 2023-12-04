/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.conti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti.RicercaContoModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;

/**
 * Classe di action per la ricerca di un conto da compilazione guidata
 * 
 * @version 1.0.0 - 09/03/2015
 *
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaContoAction extends GenericBilancioAction<RicercaContoModel> {

	/** per serializzazione **/
	private static final long serialVersionUID = -1838784065087876202L;


	@Autowired private transient ContoService contoService;
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	
	@Override
	public String execute() {
		checkCasoDUsoApplicabile(model.getTitolo());
		return SUCCESS;
	}
	

	
	/** Ricerca del conto secondo i parametri passati in input
	 * 
	 * @return SUCCESS
	 */
	public String effettuaRicercaConto(){
		String methodName = "effettuaRicercaConto";
		validaRicercaConto();
		if(hasErrori()){
			log.debug(methodName, "Validazione fallita");
			return SUCCESS;
		}
//		ricerca del conto
		RicercaSinteticaConto reqRSC = model.creaRequestRicercaSinteticaConto();
		
		
		RicercaSinteticaContoResponse resRSC = contoService.ricercaSinteticaConto(reqRSC);
		if(resRSC.hasErrori()){
			log.info(methodName, "Errori nella ricerca sintetica del conto");
			addErrori(resRSC);
			return SUCCESS;
		}
		if(resRSC.getConti().isEmpty()){
			log.info(methodName, "nessun dato trovato nella ricerca sintetica del conto");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CONTO_COMP, reqRSC);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CONTO_COMP, resRSC.getConti());
		
		return SUCCESS;
	}
	
	
	/**
	 * Validazione della ricerca del conto.
	 */
	public void validaRicercaConto(){
		if(model.getConto().getPianoDeiConti()== null || model.getConto().getPianoDeiConti().getClassePiano()== null 
				||  model.getConto().getPianoDeiConti().getClassePiano().getUid() == 0){
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("classe piano dei conti"));
		}
		
	}
	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una request per il servizio di {@link LeggiClassificatoriByTipoElementoBilResponse}.
	 * 
	 * @param codice il codice definente il classificatore
	 * @return la response corrispondente
	 */
	protected LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(String codice) {
		LeggiClassificatoriByTipoElementoBil req = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice);
		logServiceRequest(req);
		LeggiClassificatoriByTipoElementoBilResponse res = classificatoreBilService.leggiClassificatoriByTipoElementoBil(req);
		logServiceResponse(res);
		return res;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio req = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse res = bilancioService.ricercaDettaglioBilancio(req);
		
		FaseBilancio faseBilancio = res.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		sessionHandler.setParametro(BilSessionParameter.FASE_BILANCIO, faseBilancio);
		
		if(FaseBilancio.PREVISIONE.equals(faseBilancio) || FaseBilancio.PLURIENNALE.equals(faseBilancio)) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	

}
