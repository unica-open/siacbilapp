/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siaccommon.util.DataValidator;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.BaseInserimentoPreDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InseriscePreDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumento;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;

/**
 * Classe base di action per l'inserimento/ripetizione del PreDocumento di Entrata
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/04/2017
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseInserimentoPreDocumentoEntrataAction<M extends BaseInserimentoPreDocumentoEntrataModel> extends GenericPreDocumentoEntrataAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1021580050844953921L;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		
		// Caricamento liste
		try {
			caricaListaTipoCausale();
			caricaListaCausaleEntrata();
			caricaListaContoCorrente();
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
	public abstract String execute();
	
	/**
	 * Inserisce il preDocumento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String inserimento() {
		final String methodName = "inserimento";
		
		log.debug(methodName, "Inserimento del PreDocumento di Entrata");
		
		if(StringUtils.isNotEmpty(model.getMessaggioRichiestaConfermaProsecuzione())){
			
			log.debug(methodName, "Sono presenti dei messaggi. E' necessaria la conferma dell'utente. ");
			model.setRichiediConfermaUtente(true);
			
			return INPUT;
		}
		model.setRichiediConfermaUtente(false);
		
		InseriscePreDocumentoEntrata request = model.creaRequestInseriscePreDocumentoEntrata();
		logServiceRequest(request);
		InseriscePreDocumentoEntrataResponse response = preDocumentoEntrataService.inseriscePreDocumentoEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.debug(methodName, "Fallimento nell'invocazione del servizio");
			addErrori(response);
			return INPUT;
		}
		
		PreDocumentoEntrata preDocumentoEntrata = response.getPreDocumentoEntrata();
		
		log.debug(methodName, "Inserito il PreDocumento numero " + preDocumentoEntrata.getNumero() + " con uid " + preDocumentoEntrata.getUid());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		
		model.setPreDocumento(preDocumentoEntrata);
		
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
		PreDocumentoEntrata preDocumentoEntrata = model.getPreDocumento();
		checkNotNull(preDocumentoEntrata.getDataCompetenza(), "Data");
		checkNotNull(preDocumentoEntrata.getDataDocumento(),"Data esecuzione");
		checkNotNullNorEmpty(preDocumentoEntrata.getPeriodoCompetenza(), "Periodo competenza");
		checkNotNull(preDocumentoEntrata.getImporto(), "Importo");
		checkCondition(preDocumentoEntrata.getImporto() == null || preDocumentoEntrata.getImporto().signum()>0,
				ErroreCore.VALORE_NON_CONSENTITO.getErrore("importo",": l'importo deve essere positivo"));
		
		checkNotNullNorInvalidUid(model.getTipoCausale(), "Causale tipo");
		checkNotNullNorInvalidUid(model.getCausaleEntrata(), "Causale");
		
		// Validazione logica
		// SIAC-4574: non e' piu' necessario controllare la data di competenza
		DatiAnagraficiPreDocumento datiAnagraficiPreDocumento = model.getDatiAnagraficiPreDocumento();
		
		// Validazioni specifiche
		validazioneSoggetto();
		validazioneCapitolo();
		validazioneAccertamentoSubAccertamento(Integer.valueOf(1));
		validazioneAttoAmministrativo();
		
		//metodi aggiunti in data 05/06/2015
		validazioneProvvisorioDiCassaPredocumentoDiEntrata();
		
		controlloConguenzaSoggettoMovimentoGestione(model.getSoggetto(), model.getMovimentoGestione(), model.getSubMovimentoGestione(),
				"predisposizione di incasso", "accertamento");
		controlloCongruenzaCapitoloAccertamento();
		
		// Controlli su codice fiscale e partita IVA
		checkCondition(StringUtils.isBlank(datiAnagraficiPreDocumento.getCodiceFiscale()) || 
				ValidationUtil.isValidCodiceFiscaleEvenTemporary(datiAnagraficiPreDocumento.getCodiceFiscale()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("codice fiscale", ": il codice fiscale non e' sintatticamente valido"));
		checkCondition(StringUtils.isBlank(datiAnagraficiPreDocumento.getPartitaIva()) ||
				DataValidator.isValidPartitaIVA(datiAnagraficiPreDocumento.getPartitaIva()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("partita IVA", ": la partita IVA non e' sintatticamente valida"));
	}

	/**
	 * Inserimento e ripetizione del predocumento
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String inserimentoRipetizione() {
		String result = inserimento();
		// TODO: riscrivere in modo migliore?
		if(!INPUT.equals(result)) {
			result = SUCCESS;
		}
		return result;
	}
	
	/**
	 * Validazione per il metodo {@link #inserimentoRipetizione()}
	 */
	public void validateInserimentoRipetizione() {
		validateInserimento();
	}
	
	@Override
	protected BilSessionParameter getParametroListaCausale() {
		return BilSessionParameter.LISTA_CAUSALE_ENTRATA_NON_ANNULLATE;
	}
}
