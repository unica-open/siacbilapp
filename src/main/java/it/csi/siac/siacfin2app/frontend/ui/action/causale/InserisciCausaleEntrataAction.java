/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.causale;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.causale.InserisciCausaleEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;

/**
 * Classe di Action per la gestione dell'inserimento della Causale.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 12/05/2013
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciCausaleEntrataAction extends GenericCausaleEntrataAction<InserisciCausaleEntrataModel>{
	
	/** Per la serialiazzazione */
	private static final long serialVersionUID = 736618311819994668L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListeCodifiche();
		
		checkPrepareConclusoSenzaErrori();
	}
	
	/**
	 * Metodo di utilit&agrave; per il caricamento delle liste delle codifiche.
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	@SkipValidation
	public String execute() throws Exception {
		// Caricamento valori default
		checkCasoDUsoApplicabile("Inserimento causale di Entrata");
		
		if(model.getCausale() == null) {
			model.setCausale(new CausaleEntrata());
		}
		
		CapitoloEntrataGestione capitoloEntrataGestione = new CapitoloEntrataGestione();
		capitoloEntrataGestione.setNumeroArticolo(0);
		capitoloEntrataGestione.setNumeroUEB(1);
		model.setCapitolo(capitoloEntrataGestione);
		
		return SUCCESS;
	}
	
	/**
	 * Salva la causale
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String salvaCausaleEntrata() {
		final String methodName = "salvaCausaleEntrata";
		
		InserisceCausaleEntrata requestInserimento = model.creaRequestInserimentoCausaleEntrata();
		logServiceRequest(requestInserimento);
		
		InserisceCausaleEntrataResponse responseInserimento = preDocumentoEntrataService.inserisceCausaleEntrata(requestInserimento);
		logServiceResponse(responseInserimento);
		
		if(responseInserimento.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(requestInserimento, responseInserimento));
			addErrori(responseInserimento);
			return INPUT;
		}
		
		CausaleEntrata causale = responseInserimento.getCausaleEntrata();
		
		// Injetto la causale ottenuto dalla response nel model
		model.setCausale(causale);
		model.setIdCausale(causale.getUid());
		
		setMessaggiInSessionePerActionSuccessiva();
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il salvataggio della causale di Entrata.
	 */
	public void validateSalvaCausaleEntrata() {
		CausaleEntrata causale = model.getCausale();
		
		if(causale == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice"));
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Descrizione"));
		} else {
			checkNotNullNorEmpty(causale.getCodice(), "Codice");
			checkNotNullNorEmpty(causale.getDescrizione(), "Descrizione");
		}
		// Tipo causale
		checkNotNullNorInvalidUid(model.getTipoCausale(), "Tipo Causale");
		// CR-4493
		warnCondition(model.getStrutturaAmministrativoContabile() != null && model.getStrutturaAmministrativoContabile().getUid() != 0, ErroreBil.ERRORE_GENERICO.getErrore("La struttura amministrativa non e' stata selezionata"));
		
		validazioneSoggetto();
		validazioneCapitolo();
		validazioneAccertamentoSubAccertamento();
		validazioneAttoAmministrativo();
		
		controlloConguenzaSoggettoAccertamento();
		controlloCongruenzaCapitoloAccertamento();
	}

}
