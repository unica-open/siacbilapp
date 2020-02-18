/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.RicercaPrimeNoteModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaPrimeNote;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaPrimeNoteResponse;

/**
 * Classe di action per la ricerca della prima nota libera
 * 
 * @author Valentina
 * @version 1.0.0 - 20/06/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaPrimeNoteAction extends GenericBilancioAction<RicercaPrimeNoteModel> {
	
	private static final long serialVersionUID = 1849764163870162966L;
	
	@Autowired private transient PrimaNotaService primaNotaService;
	@Autowired private transient ClassificatoreBilService classificatoreBilService;

	/** Ricerca del conto secondo i parametri passati in input
	 * 
	 * @return SUCCESS
	 */
	public String effettuaRicercaPrimeNote(){
		String methodName = "effettuaRicercaPrimeNote";
		checkRicercaValida();
		if(hasErrori()){
			log.debug(methodName, "Validazione fallita");
			return SUCCESS;
		}
		RicercaPrimeNote reqRPN = model.creaRequestRicercaPrimeNote();
		RicercaPrimeNoteResponse resRPN = primaNotaService.ricercaPrimeNote(reqRPN);
		if(resRPN.hasErrori()){
			log.info(methodName, "Errori nella ricerca delle prime note");
			addErrori(resRPN);
			return SUCCESS;
		}
		if(resRPN.getPrimeNote().isEmpty()){
			log.info(methodName, "nessun dato trovato nella ricerca delle prime note");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return SUCCESS;
		}
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_PRIMANOTA_GEN, reqRPN);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_PRIMANOTA_GEN, resRPN.getPrimeNote());
		
		return SUCCESS;
	}

	/**
	 * Controlla che la ricerca sia valida
	 */
	public void checkRicercaValida(){
		checkElementoPianoDeiConti();
		checkCondition( (StringUtils.isBlank(model.getNumeroMovimento()) && model.getAnnoMovimento() == null) || (StringUtils.isNotBlank(model.getNumeroMovimento()) && model.getAnnoMovimento() != null),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("movimento", ": anno e numero non possono essere valorizzati singolarmente"));
	}

	/**
	 * Controlla l'elemento del piano dei conti
	 */
	private void checkElementoPianoDeiConti() {
		if(model.getElementoPianoDeiConti() == null || StringUtils.isBlank(model.getElementoPianoDeiConti().getCodice())){
			return;
		}
		LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno request = model.creaRequestLeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno();
		logServiceRequest(request);
		LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnnoResponse response = classificatoreBilService.leggiClassificatoreGerarchicoByCodiceAndTipoAndAnno(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return;
		}
		if(response.getClassificatore() == null ){
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("V livello del piano dei conti", ""+model.getElementoPianoDeiConti().getCodice()));
		}
	}
	
	
	

}
