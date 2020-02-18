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

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siaccommon.util.DataValidator;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.InserisciPreDocumentoSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;

/**
 * Classe di action per l'inserimento del PreDocumento di Spesa
 * 
 * @author Marchino Alessandro,Nazha Ahmad
 * @version 1.0.0 - 15/04/2014
 * @version 1.0.1 - 05/06/2015
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class InserisciPreDocumentoSpesaAction extends GenericPreDocumentoSpesaAction<InserisciPreDocumentoSpesaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4273219650273647172L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		
		// Caricamento liste
		try {
			caricaListaTipoCausale();
			caricaListaCausaleSpesa();
			caricaListaContoTesoreria();
			caricaListaNazioni();
			caricaListaSesso();
			caricaListaTipoAtto();
			caricaListaClasseSoggetto();
			caricaListaTipoFinanziamento();
		} catch(WebServiceInvocationFailureException e) {
			log.error("prepare", "Errore nell'invocazione del caricamento di una lista: " + e.getMessage());
		} finally {
			checkMetodoConclusoSenzaErrori();
		}
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Caricamento valori default
		checkCasoDUsoApplicabile("Inserimento preDocumento di spesa");
		// Carico sede secondaria e modalità pagamento
		caricaListaSedeSecondariaSoggettoEModalitaPagamentoSoggetto();
		
		// Inizializzo l'anagrafica
		DatiAnagraficiPreDocumentoSpesa datiAnagraficiPreDocumentoSpesa = new DatiAnagraficiPreDocumentoSpesa();
		datiAnagraficiPreDocumentoSpesa.setNazioneNascita(BilConstants.DESCRIZIONE_ITALIA.getConstant());
		datiAnagraficiPreDocumentoSpesa.setNazioneIndirizzo(BilConstants.DESCRIZIONE_ITALIA.getConstant());
		
		model.setDatiAnagraficiPreDocumento(datiAnagraficiPreDocumentoSpesa);
		
		return SUCCESS;
	}
	
	/**
	 * Inserisce il preDocumento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inserimento() {
		final String methodName = "inserimento";
		
		log.debug(methodName, "Inserimento del PreDocumento di Spesa");
		InseriscePreDocumentoSpesa request = model.creaRequestInseriscePreDocumentoSpesa();
		logServiceRequest(request);
		InseriscePreDocumentoSpesaResponse response = preDocumentoSpesaService.inseriscePreDocumentoSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Fallimento nell'invocazione del servizio");
			addErrori(response);
			return INPUT;
		}
		
		PreDocumentoSpesa preDocumentoSpesa = response.getPreDocumentoSpesa();
		
		log.debug(methodName, "Inserito il PreDocumento numero " + preDocumentoSpesa.getNumero() + " con uid " + preDocumentoSpesa.getUid());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		
		model.setPreDocumento(preDocumentoSpesa);
		
		// Redirigo verso ricerca o aggiornamento a seconda che sia arrivato da ricerca o da cruscotto
		String result = AGGIORNA;
		
		if(Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.INSERIMENTO_DA_RICERCA, Boolean.class))) {
			sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_DA_RICERCA, Boolean.FALSE);
			result = RICERCA;
		}
		setMessaggiInSessionePerActionSuccessiva();
		
		return result;
	}
	
	/**
	 * Validazione per l'inserimento del preDocumento.
	 */
	public void validateInserimento() {
		// Check campi obbligatori
		PreDocumentoSpesa preDocumentoSpesa = model.getPreDocumento();
		checkNotNull(preDocumentoSpesa.getDataCompetenza(), "Data");
		checkNotNullNorEmpty(preDocumentoSpesa.getPeriodoCompetenza(), "Periodo competenza");
		checkNotNull(preDocumentoSpesa.getImporto(), "Importo");
		
		checkNotNullNorInvalidUid(model.getTipoCausale(), "Causale tipo");
		checkNotNullNorInvalidUid(model.getCausaleSpesa(), "Causale");
		checkNotNullNorInvalidUid(model.getContoTesoreria(), "Conto del tesoriere");
		
		// Validazione logica
		checkCondition(preDocumentoSpesa.getImporto() == null || preDocumentoSpesa.getImporto().signum()>0,
				ErroreCore.VALORE_NON_VALIDO.getErrore("importo",": l'importo deve essere positivo"));
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
