/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento.aggiornamento.entrata;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.action.documento.GenericAggiornaDocumentoAction;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.AggiornaDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoIvaEntrataService;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoDocumento;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.CostantiFin;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;

/**
 * Classe di action base per l'aggiornamento del Documento di entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/09/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(AggiornaDocumentoEntrataBaseAction.FAMILY_NAME)
public class AggiornaDocumentoEntrataBaseAction extends GenericAggiornaDocumentoAction<AggiornaDocumentoEntrataModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 2216228847988229357L;
	
	/** Nome della famiglia */
	public static final String FAMILY_NAME = "AggiornaDocumentoEntrata";

	/** Serviz&icirc; del documento di entrata */
	@Autowired protected transient DocumentoEntrataService documentoEntrataService;
	/** Serviz&icirc; del documento iva di entrata */
	@Autowired protected transient DocumentoIvaEntrataService documentoIvaEntrataService;
	/** Serviz&icirc; dei classificatori bil */
	@Autowired protected transient ClassificatoreBilService classificatoreBilService;
	/** Serviz&icirc; del provvisorio */
	@Autowired protected transient ProvvisorioService provvisorioService;

	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	/**
	 * Inizializza la action.
	 * 
	 * @throws FrontEndBusinessException nel caso in cui l'inizializzazione non vada a buon fine
	 */
	protected void initAction() throws FrontEndBusinessException {
		try {
			super.prepare();
		} catch (Exception e) {
			log.error("initAction", "Errore nell'inizializzazione della action: " + e.getMessage());
			throw new FrontEndBusinessException("Errore nell'inizializzazione della action", e);
		}
	}

	/**
	 * Carica la lista dei tipi di finanziamento per la quota.
	 */
	protected void checkAndObtainListaTipiFinanziamento() {
		List<TipoFinanziamento> listaTipoFinanziamento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO);
		if (listaTipoFinanziamento != null && !listaTipoFinanziamento.isEmpty()) {
			return;
		}

		LeggiClassificatoriGenericiByTipoElementoBil request = model
				.creaRequestLeggiClassificatoriGenericiByTipoElementoBil(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant());
		LeggiClassificatoriGenericiByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriGenericiByTipoElementoBil(request);

		listaTipoFinanziamento = response.getClassificatoriTipoFinanziamento();

		sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO, listaTipoFinanziamento);

		model.setListaTipiFinanziamento(listaTipoFinanziamento);
	}
	
	@Override
	protected void cleanModel() {
		super.cleanModel();
		model.setMovimentoGestione(null);
	}
	
	/**
	 * Verifico che le attivazioni contabili siano possibili
	 * 
	 */
	protected void checkAttivazioneRegContabili() {
		final String methodName = "checkAttivazioneRegContabili";
		if(model.getDocumento() == null || model.getDocumento().getTipoDocumento() == null) {
			log.debug(methodName, "Dati non presenti. Non attivo alcunche'");
			model.setAttivaRegistrazioniContabiliVisible(false);
			return;
		}
		
		StatoOperativoDocumento sto = model.getDocumento().getStatoOperativoDocumento();
		TipoDocumento td = model.getDocumento().getTipoDocumento();
		log.debug(methodName, "stato operativo" + sto);
		log.debug(methodName, "contabilizza " + model.getDocumento().getContabilizzaGenPcc());
		log.debug(methodName, "flag attiva gen " + td.getFlagAttivaGEN());
		boolean condizioneVisibilitaRegistrazioniContabili = !StatoOperativoDocumento.ANNULLATO.equals(sto)
				&& !StatoOperativoDocumento.EMESSO.equals(sto)
				&& !StatoOperativoDocumento.INCOMPLETO.equals(sto)
				&& !Boolean.TRUE.equals(model.getDocumento().getContabilizzaGenPcc())
				&& Boolean.TRUE.equals(td.getFlagAttivaGEN());
		model.setAttivaRegistrazioniContabiliVisible(condizioneVisibilitaRegistrazioniContabili);
	}
	
	/**
	 * Validazione dei campi.
	 */
	protected void validaCampi() {
		final String methodName = "validaCampi";

		DocumentoEntrata documento = model.getDocumento();

		// Validazione campi obbligatori
		checkCondition(documento.getDataEmissione() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Data"), true);
		checkCondition(documento.getImporto() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Importo"), true);
		checkCondition(StringUtils.trimToNull(documento.getDescrizione()) != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Descrizione"));
		checkCondition(documento.getCodiceBollo() != null && documento.getCodiceBollo().getUid() != 0,
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Imposta di bollo"));

		if (model.getDocumentoIncompleto()) {
			// Se lo stato operativo e' INCOMPLETO, controllo il soggetto. Altrimenti posso evitarlo
			checkNotNullNorEmpty(model.getSoggetto().getCodiceSoggetto(), "Codice");
			validaSoggetto(true);
		}

		log.debug(methodName, "Campi obbligatorii: errori rilevati? " + hasErrori());

		// La data di emissione del documento deve essere coerente con l'anno
		// dello stesso
		if (documento.getDataEmissione() != null) {
			Integer annoEmissione = Integer.decode(FormatUtils.formatDateYear(documento.getDataEmissione()));
			checkCondition(documento.getAnno().compareTo(annoEmissione) == 0, ErroreFin.LA_DATA_DEVE_ESSERE_COERENTE_CON_L_ANNO_DEL_DOCUMENTO.getErrore());
		}

		//SIAC-7193 e SIAC-7208
		checkCondition(documento.getImporto().signum() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": deve essere maggiore di zero"));
		// Validazione degli importi
		validazioneImporti(documento, BigDecimal.ZERO);
		// Se la data di scadenza è presente, deve essere maggiore o uguale la
		// data del documento
		checkCondition(
				documento.getDataEmissione() == null || documento.getDataScadenza() == null
						|| documento.getDataScadenza().compareTo(documento.getDataEmissione()) >= 0,
				ErroreFin.DATA_SCADENZA_ANTECEDENTE_ALLA_DATA_DEL_DOCUMENTO.getErrore());

		checkCondition(!(documento.getDataRepertorio() != null ^ StringUtils.isNotEmpty(documento.getNumeroRepertorio())),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Repertorio", "data e numero devono essere entrambi presenti o assenti"));
		
		// SIAC-5257
		checkProtocollo(documento);
		
		// SIAC 6677
		Date today = new Date();
		Calendar todayCal = Calendar.getInstance();
		todayCal.setTime(today);
		todayCal.set(Calendar.HOUR_OF_DAY, 0);
		todayCal.set(Calendar.MINUTE, 0);
		todayCal.set(Calendar.SECOND, 0);
		checkCondition(
				documento.getDataOperazione() == null ||  documento.getDataOperazione().compareTo(todayCal.getTime()) <= 0,
						ErroreFin.DATA_OPERAZIONE_SUCCESSIVA_ALLA_DATA_ODIERNA.getErrore());
				
		//SIAC 6677 
		checkAndFillCodAvviso(documento);
		
		//SIAC-7567
		//controllo che i campi cig e cup siano formati correttamente
		checkErrorCigCupPA(model.getDocumento(), model.getSoggetto());

		log.debug(methodName, "Validazione logica: errori rilevati? " + hasErrori());
	}
	
	/**
	 * Controlla che se presente il codice Avviso pago PA non superi le 18 cifre
	 * Nel caso le cifre fossero minori di 18 applica un fill left
	 * @param documento
	 */
	private void checkAndFillCodAvviso(DocumentoEntrata documento){
		
		checkCondition(documento.getCodAvvisoPagoPA() == null || documento.getCodAvvisoPagoPA().length()==0 ||
		isNumeric(documento.getCodAvvisoPagoPA()),
		ErroreFin.COD_AVVISO_PAGO_PA_NUMERICO.getErrore());
		
		
		int maxLength = CostantiFin.CODICE_AVVISO_PAGO_PA_LENGTH;
		checkCondition(documento.getCodAvvisoPagoPA() == null || documento.getCodAvvisoPagoPA().length() <= maxLength,
				ErroreFin.COD_AVVISO_PAGO_PA_MAXLENGTH.getErrore());
		if(documento.getCodAvvisoPagoPA()!= null && documento.getCodAvvisoPagoPA().length()>0 && documento.getCodAvvisoPagoPA().length()< maxLength){
			int diff = maxLength -documento.getCodAvvisoPagoPA().length();
			StringBuilder codAvviso = new StringBuilder();
			for(int i=0; i<diff; i++){
				codAvviso.append("0");
			}
			codAvviso.append(documento.getCodAvvisoPagoPA());
			documento.setCodAvvisoPagoPA(codAvviso.toString());
		}	
	}
	
}
