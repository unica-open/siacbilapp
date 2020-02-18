/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siaccommon.util.DataValidator;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.AggiornaPreDocumentoSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaPreDocumentoDiSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioPreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;

/**
 * Classe di action per l'aggiornamento del PreDocumento di Spesa
 * 
 * @author Marchino Alessandro,Nazha Ahmad
 * @version 1.0.0 - 15/04/2014
 * @version 1.0.1 - 11/06/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaPreDocumentoSpesaAction extends GenericPreDocumentoSpesaAction<AggiornaPreDocumentoSpesaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3410995502977493215L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		
		// Caricamento liste
		try {
			caricaListaTipoCausale();
			caricaListaContoTesoreria();
			caricaListaNazioni();
			caricaListaSesso();
			caricaListaTipoAtto();
			caricaListaClasseSoggetto();
			caricaListaTipoFinanziamento();
			caricaListaSedeSecondariaSoggettoEModalitaPagamentoSoggetto();
			caricaListaCausaleSpesa();
		} catch(WebServiceInvocationFailureException e) {
			log.error("prepare", "Errore nell'invocazione del caricamento di una lista: " + e.getMessage());
		} finally {
			checkMetodoConclusoSenzaErrori();
		}
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		final String methodName = "execute";
		// Caricamento valori default
		checkCasoDUsoApplicabile("Aggiornamento preDocumento di spesa");
		
		leggiEventualiInformazioniAzionePrecedente();
		leggiEventualiMessaggiAzionePrecedente();
		
		// Ricerca di dettaglio del PreDocumento
		RicercaDettaglioPreDocumentoSpesa request = model.creaRequestRicercaDettaglioPreDocumentoSpesa();
		logServiceRequest(request);
		RicercaDettaglioPreDocumentoSpesaResponse response = preDocumentoSpesaService.ricercaDettaglioPreDocumentoSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.error(methodName, "Errore nell'invocazione del servizio di ricerca dettaglio preDocumento spesa");
			addErrori(response);
			
			throwExceptionFromErrori(response.getErrori());
		}
		
		log.debug(methodName, "PreDocumento caricato. Imposto i dati nel model");
		model.impostaPreDocumento(response.getPreDocumentoSpesa());
		
		AttoAmministrativo attoAmministrativo = caricaAttoAmministrativoSePresente();
		if(attoAmministrativo != null) {
			model.impostaAttoAmministrativo(attoAmministrativo);
		}
		if(response.getPreDocumentoSpesa().getProvvisorioDiCassa() != null && response.getPreDocumentoSpesa().getProvvisorioDiCassa().getUid() !=0){
			   model.setProvvisorioCassa(response.getPreDocumentoSpesa().getProvvisorioDiCassa());
		}
		if(response.getPreDocumentoSpesa().getVoceMutuo() != null && response.getPreDocumentoSpesa().getVoceMutuo().getNumeroMutuo() !=null){
			model.setVoceMutuo(response.getPreDocumentoSpesa().getVoceMutuo());
		}
		caricaListaCausaleSpesa();
		
		return SUCCESS;
	}
	
	/**
	 * Aggiorna il preDocumento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornamento() {
		final String methodName = "aggiornamento";
		
		log.debug(methodName, "Aggiornamento del PreDocumento di Spesa");
		AggiornaPreDocumentoDiSpesa request = model.creaRequestAggiornaPreDocumentoDiSpesa();
		logServiceRequest(request);
		AggiornaPreDocumentoDiSpesaResponse response = preDocumentoSpesaService.aggiornaPreDocumentoDiSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Fallimento nell'invocazione del servizio");
			addErrori(response);
			return INPUT;
		}
		
		PreDocumentoSpesa preDocumentoSpesa = response.getPreDocumentoSpesa();
		
		log.debug(methodName, "Aggiornato il PreDocumento numero " + preDocumentoSpesa.getNumero() + " con uid " + preDocumentoSpesa.getUid());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		setMessaggiInSessionePerActionSuccessiva();
		
		model.setPreDocumento(preDocumentoSpesa);
		model.setUidPreDocumentoDaAggiornare(preDocumentoSpesa.getUid());
		
		// Redirigo verso ricerca o aggiornamento a seconda che sia arrivato da ricerca o da cruscotto
		String result = AGGIORNA;
		
		if(Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.INSERIMENTO_DA_RICERCA, Boolean.class))) {
			sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_DA_RICERCA, Boolean.FALSE);
			result = RICERCA;
		}
		
		return result;
	}
	
	/**
	 * Validazione per l'inserimento del preDocumento.
	 */
	public void validateAggiornamento() {
		// Check campi obbligatori
		PreDocumentoSpesa preDocumentoSpesa = model.getPreDocumento();
		checkNotNull(preDocumentoSpesa.getDataCompetenza(), "Data");
		checkNotNullNorEmpty(preDocumentoSpesa.getPeriodoCompetenza(), "Periodo competenza");
		checkNotNull(preDocumentoSpesa.getImporto(), "Importo");
		checkCondition(preDocumentoSpesa.getImporto() == null || preDocumentoSpesa.getImporto().signum()>0,
				ErroreCore.VALORE_NON_VALIDO.getErrore("importo",": l'importo deve essere positivo"));
		
		checkNotNullNorInvalidUid(model.getTipoCausale(), "Causale tipo");
		checkNotNullNorInvalidUid(model.getCausaleSpesa(), "Causale");
		checkNotNullNorInvalidUid(model.getContoTesoreria(), "Conto del tesoriere");
		
		// Validazione logica
		// SIAC-4574: non e' piu' necessario controllare la data di competenza
		DatiAnagraficiPreDocumentoSpesa datiAnagraficiPreDocumentoSpesa = model.getDatiAnagraficiPreDocumento();
		
		// Validazioni specifiche
		validazioneSoggetto();
		validazioneCapitolo();
		validazioneImpegnoSubImpegno();
		validazioneAttoAmministrativo();
		
		//metodi aggiunti in data 05/06/2015
		validazioneProvvisorioDiCassaPredocumentoDiSpesa();
		validaNumeroMutuo();
		controlloConguenzaSoggettoMovimentoGestione(model.getSoggetto(), model.getMovimentoGestione(), model.getSubMovimentoGestione(),
				"predisposizione di pagamento", "impegno");
		controlloCongruenzaCapitoloImpegno();
		
		// Controlli su codice fiscale e partita IVA
		checkCondition(StringUtils.isBlank(datiAnagraficiPreDocumentoSpesa.getCodiceFiscale()) || 
				ValidationUtil.isValidCodiceFiscaleEvenTemporary(datiAnagraficiPreDocumentoSpesa.getCodiceFiscale()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("codice fiscale", ": il codice fiscale non e' sintatticamente valido"));
		checkCondition(StringUtils.isBlank(datiAnagraficiPreDocumentoSpesa.getCodiceFiscaleQuietanzante()) || 
				ValidationUtil.isValidCodiceFiscaleEvenTemporary(datiAnagraficiPreDocumentoSpesa.getCodiceFiscaleQuietanzante()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("codice fiscale quietanzante", ": il codice fiscale quietanzante non e' sintatticamente valido"));
		checkCondition(StringUtils.isBlank(datiAnagraficiPreDocumentoSpesa.getPartitaIva()) ||
				DataValidator.isValidPartitaIVA(datiAnagraficiPreDocumentoSpesa.getPartitaIva()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("partita IVA", ": la partita IVA non e' sintatticamente valida"));
	}
	
	
	
	
	
	

	

}
