/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento;

import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.InserisciTestataDocumentoEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoEntrataResponse;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di action per l'inserimento della testata del Documento di entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 09/07/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class InserisciTestataDocumentoEntrataAction extends GenericDocumentoAction<InserisciTestataDocumentoEntrataModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1919984150384201989L;
	
	@Autowired private transient DocumentoEntrataService documentoEntrataService;
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	@AnchorAnnotation(value = "OP-ENT-insDocIvaEnt", name = "Documento Testata STEP 1")
	public String execute() throws Exception {
		checkCasoDUsoApplicabile("Inserimento documento di entrata");
		
		// Caricamento delle liste
		checkAndObtainListaTipoDocumento(TipoFamigliaDocumento.IVA_ENTRATA, Boolean.FALSE, Boolean.FALSE);
		checkAndObtainListaClassiSoggetto();
		
		return SUCCESS;
	}
	
	/**
	 * Metodo per l'ingresso nello step2 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enterStep2() {
		// Caricamento liste
		checkAndObtainListaCodiceBollo();
		return SUCCESS;
	}
	
	/**
	 * Metodo per l'ingresso nello step2 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@AnchorAnnotation(value = "OP-ENT-insDocIvaEnt", name = "Documento Testata STEP 2")
	public String step2() {
		return SUCCESS;
	}
	
	/**
	 * Metodo per l'ingresso nello step3 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enterStep3() {
		return salvataggioDocumento();
	}
	
	/**
	 * Metodo per l'ingresso nello step3 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@AnchorAnnotation(value = "OP-ENT-insDocIvaEnt", name = "Documento Testata STEP 3")
	public String step3() {
		return SUCCESS;
	}

	/**
	 * Metodo per la redirezione verso l'aggiornamento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiorna() {
		model.setUidDocumento(model.getDocumento().getUid());
		return SUCCESS;
	}
	
	/**
	 * Metodo per la redirezione verso la ripetizione dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@AnchorAnnotation(value = "OP-ENT-insDocIvaEnt", name = "Documento Testata STEP 1")
	public String ripeti() {
		DocumentoEntrata documentoSalvato = model.getDocumento();
		Soggetto soggettoAssociato = model.getSoggetto();
		cleanModel();
		model.impostaDatiRipeti(documentoSalvato, soggettoAssociato);
		
		checkAndObtainListaClassiSoggetto();
		checkAndObtainListaTipoDocumento(TipoFamigliaDocumento.IVA_ENTRATA, Boolean.FALSE, Boolean.FALSE);
		return SUCCESS;
	}
	
	/**
	 * Metodo per la redirezione verso l'aggiornamento, con il tab delle quote.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String quote() {
		model.setUidDocumento(model.getDocumento().getUid());
		return SUCCESS;
	}
	
	/**
	 * Metodo per l'ingresso nello step3 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ripetiSalva() {
		return salvataggioDocumento();
	}
	
	/**
	 * Metodo per l'ingresso nello step3 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@AnchorAnnotation(value = "OP-ENT-insDocIvaEnt", name = "Documento Testata STEP 3")
	public String ripetiStep3() {
		return SUCCESS;
	}
	
	/**
	 * Validazione per l'ingresso nello step2.
	 */
	public void validateEnterStep2() {
		final String methodName = "validateEnterStep2";
		log.debug(methodName, "Validazione campi obbligatori");
		
		DocumentoEntrata documento = model.getDocumento();
		Soggetto soggetto = model.getSoggetto();
		
		checkNotNullNorInvalidUid(documento.getTipoDocumento(), "Tipo");
		checkNotNull(documento.getAnno(), "Anno");
		checkNotNullNorEmpty(documento.getNumero(), "Numero");
		checkNotNull(documento.getDataEmissione(), "Data");
		
		// Controlla se il soggetto sia valido
		boolean controlloSoggetto = soggetto != null && StringUtils.isNotBlank(soggetto.getCodiceSoggetto());
		checkCondition(controlloSoggetto, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice"));
		
		log.debug(methodName, "Validazione logica campi");
		if(documento.getAnno() != null) {
			checkCondition(model.getAnnoEsercizioInt().compareTo(documento.getAnno()) >= 0, ErroreFin.ANNO_DOCUMENTO_ERRATO.getErrore());
			if(documento.getDataEmissione() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(documento.getDataEmissione());
				checkCondition(documento.getAnno().compareTo(cal.get(Calendar.YEAR)) == 0, ErroreFin.LA_DATA_DEVE_ESSERE_COERENTE_CON_L_ANNO_DEL_DOCUMENTO.getErrore());
			}
		}
		
		if(controlloSoggetto) {
			validaSoggetto(true);
		}
	}
	
	/**
	 * Validazione per l'ingresso nello step3.
	 */
	public void validateEnterStep3() {
		final String methodName = "validateEnterStep3";
		log.debug(methodName, "Validazione campi obbligatori");
		
		DocumentoEntrata documento = model.getDocumento();
		checkNotNull(documento.getImporto(), "Importo");
		checkNotNullNorEmpty(documento.getDescrizione(), "Descrizione");
		
		log.debug(methodName, "Validazione logica campi");
		// L'importo deve essere non negativo
		checkCondition(documento.getImporto() == null || documento.getImporto().signum() > 0,
			ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", ": deve essere maggiore di zero"));
		// Se la data di scadenza è presente, deve essere maggiore o uguale la data del documento
		checkCondition(documento.getDataEmissione() == null || documento.getDataScadenza() == null ||
			documento.getDataScadenza().compareTo(documento.getDataEmissione()) >= 0,
			ErroreFin.DATA_SCADENZA_ANTECEDENTE_ALLA_DATA_DEL_DOCUMENTO.getErrore());
	}
	
	/**
	 * Validazione per la ripetizione del salvataggio.
	 */
	public void validateRipetiSalva() {
		validateEnterStep2();
		validateEnterStep3();
	}
	
	/**
	 * Effettua il salvataggio del documento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	private String salvataggioDocumento() {
		final String methodName = "salvataggioDocumento";
		log.debug(methodName, "Creazione della request");
		InserisceDocumentoEntrata request = model.creaRequestInserisceDocumentoEntrata();
		logServiceRequest(request);
		
		log.debug(methodName, "Invocazione del servizio");
		InserisceDocumentoEntrataResponse response = documentoEntrataService.inserisceTestataDocumentoEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return INPUT;
		}
		
		DocumentoEntrata documentoEntrata = response.getDocumentoEntrata();
		log.debug(methodName, "Nessun errore nell'invocazione del servizio - inserito documento con uid: " + documentoEntrata.getUid());
		model.popolaModel(documentoEntrata);
		caricaClassificatori();
		
		// Fornisci messaggio successo
		impostaInformazioneSuccesso();
		return SUCCESS;
	}

	/**
	 * Carica i classificatori dalla sessione.
	 */
	private void caricaClassificatori() {
		DocumentoEntrata documento = model.getDocumento();
		
		TipoDocumento tipoDocumento = documento.getTipoDocumento();
		List<TipoDocumento> listaTipoDocumento = model.getListaTipoDocumento();
		tipoDocumento = ComparatorUtils.searchByUid(listaTipoDocumento, tipoDocumento);
		
		documento.setTipoDocumento(tipoDocumento);
	}
	
}
