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

import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.causale.InserisciCausaleSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;

/**
 * Classe di Action per la gestione dell'inserimento della Causale.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 04/02/2013
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciCausaleSpesaAction extends GenericCausaleSpesaAction<InserisciCausaleSpesaModel>{
	
	/** Per la serialiazzazione */
	private static final long serialVersionUID = 7033998987854718167L;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		
		caricaListeCodifiche();
		
		checkPrepareConclusoSenzaErrori();
		
		log.debugEnd(methodName, "");
	}
	
	/**
	 * Metodo di utilit&agrave; per il caricamento delle liste delle codifiche.
	 */
	@Override
	@BreadCrumb("%{model.titolo}")
	@SkipValidation
	public String execute() throws Exception {
		// Caricamento valori default
		checkCasoDUsoApplicabile("Inserimento causale di spesa");
		// Carico sede secondaria e modalità pagamento
		caricaListaSedeSecondariaSoggettoEModalitaPagamentoSoggetto();
				
		if(model.getCausale() == null) {
			model.setCausale(new CausaleSpesa());
		}
		
		CapitoloUscitaGestione capitoloUscitaGestione = new CapitoloUscitaGestione();
		capitoloUscitaGestione.setNumeroArticolo(0);
		capitoloUscitaGestione.setNumeroUEB(1);
		model.setCapitolo(capitoloUscitaGestione);
		
		return SUCCESS;
	}
	
	/**
	 * Salva la causale
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String salvaCausaleSpesa() {
		final String methodName = "salvaCausaleSpesa";
		
		InserisceCausaleSpesa requestInserimento = model.creaRequestInserimentoCausaleSpesa();
		logServiceRequest(requestInserimento);
		
		InserisceCausaleSpesaResponse responseInserimento = preDocumentoSpesaService.inserisceCausaleSpesa(requestInserimento);
		logServiceResponse(responseInserimento);
		
		if(responseInserimento.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(requestInserimento, responseInserimento));
			addErrori(responseInserimento);
			return INPUT;
		}
		
		CausaleSpesa causale = responseInserimento.getCausaleSpesa();
		
		// Injetto la causale ottenuto dalla response nel model
		model.setCausale(causale);
		model.setIdCausale(causale.getUid());
		
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il salvataggio della causale di Spesa.
	 */
	public void validateSalvaCausaleSpesa() {
		CausaleSpesa causale = model.getCausale();
		
		if(causale == null) {
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice"));
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Descrizione"));
		} else {
			checkNotNullNorEmpty(causale.getCodice(), "Codice");
			checkNotNullNorEmpty(causale.getDescrizione(), "Descrizione");
		}
		
		if(model.getTipoCausale() != null) {
			causale.setTipoCausale(model.getTipoCausale());
			checkCondition(causale.getTipoCausale().getUid() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo Causale"));
		}
		
		validazioneSoggetto();
		validazioneCapitolo();
		validazioneImpegnoSubImpegno();
		validazioneAttoAmministrativo();
		
		controlloConguenzaSoggettoImpegno();
		controlloCongruenzaCapitoloImpegno();
	}

}
