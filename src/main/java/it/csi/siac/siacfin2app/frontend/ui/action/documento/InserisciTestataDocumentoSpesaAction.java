/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.documento;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.documento.InserisciTestataDocumentoSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipo;
import it.csi.siac.siacfinser.model.siopeplus.SiopeDocumentoTipoAnalogico;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di action per l'inserimento della testata del Documento (Iva) di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/03/2014
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class InserisciTestataDocumentoSpesaAction extends GenericDocumentoSpesaAction<InserisciTestataDocumentoSpesaModel> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1919984150384201989L;
	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		// Pulisco il model
		setModel(null);
		super.prepare();
	}
	
	@Override
	@BreadCrumb("%{model.titolo}")
	@AnchorAnnotation(value = "OP-SPE-insDocIvaSpe", name = "Documento Testata STEP 1")
	public String execute() throws Exception {
		checkCasoDUsoApplicabile("Inserimento documento di spesa");
		
		// Caricamento delle liste
		checkAndObtainListaTipoDocumento(TipoFamigliaDocumento.IVA_SPESA, Boolean.FALSE, Boolean.FALSE);
		checkAndObtainListaClassiSoggetto();
		// SIAC-5311 SIOPE+
		checkAndObtainListeSIOPE();
		// Impostazione default
		impostaDefaultDocumento();
		
		return SUCCESS;
	}
	
	/**
	 * Impostazione dei default per il documento
	 */
	private void impostaDefaultDocumento() {
		// SIAC-5437: il tipo documento deve essere di default ANALOGICO
		if(model.getDocumento() == null) {
			// Inizializzo il documento
			model.setDocumento(new DocumentoSpesa());
		}
		
		SiopeDocumentoTipo siopeDocumentoTipo = ComparatorUtils.findByCodice(model.getListaSiopeDocumentoTipo(), BilConstants.CODICE_SIOPE_DOCUMENTO_TIPO_ANALOGICO.getConstant());
		model.getDocumento().setSiopeDocumentoTipo(siopeDocumentoTipo);
	}
	/**
	 * Controlla se le lista delSIOPE siano presenti valorizzate nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi.
	 */
	private void checkAndObtainListeSIOPE() {
		if(!model.getListaSiopeDocumentoTipo().isEmpty()
				&& !model.getListaSiopeDocumentoTipoAnalogico().isEmpty()) {
			// Ho gia' le liste nel model
			return;
		}
		
		// Ricerca la causale come codifica generica
		RicercaCodifiche req = model.creaRequestRicercaCodifiche(SiopeDocumentoTipo.class, SiopeDocumentoTipoAnalogico.class);
		RicercaCodificheResponse res = codificheService.ricercaCodifiche(req);
		
		// Se ho errori ignoro la response
		// TODO: gestire gli errori?
		if(!res.hasErrori()) {
			model.setListaSiopeDocumentoTipo(res.getCodifiche(SiopeDocumentoTipo.class));
			model.setListaSiopeDocumentoTipoAnalogico(res.getCodifiche(SiopeDocumentoTipoAnalogico.class));
		}
	}
	
	/**
	 * Metodo per l'ingresso nello step2 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enterStep2() {
		// Il metodo è presente per evitare problemi di redirezione. Equivalente ad un Redirect-after-post
		return SUCCESS;
	}
	
	/**
	 * Metodo per l'ingresso nello step2 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@AnchorAnnotation(value = "OP-SPE-insDocIvaSpe", name = "Documento Testata STEP 2")
	public String step2() {
		return SUCCESS;
	}
	
	/**
	 * Metodo per l'ingresso nello step3 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enterStep3() {
		// Il metodo è presente per evitare problemi di redirezione. Equivalente ad un Redirect-after-post
		return salvataggioDocumento();
	}
	
	/**
	 * Metodo per l'ingresso nello step3 dell'inserimento.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@AnchorAnnotation(value = "OP-SPE-insDocIvaSpe", name = "Documento Testata STEP 3")
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
	@AnchorAnnotation(value = "OP-SPE-insDocIvaSpe", name = "Documento Testata STEP 1")
	public String ripeti() {
		DocumentoSpesa documentoSalvato = model.getDocumento();
		Soggetto soggettoAssociato = model.getSoggetto();
		cleanModel();
		model.impostaDatiRipeti(documentoSalvato, soggettoAssociato);
		
		checkAndObtainListaClassiSoggetto();
		checkAndObtainListaTipoDocumento(TipoFamigliaDocumento.IVA_SPESA, Boolean.FALSE, Boolean.FALSE);
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
	@AnchorAnnotation(value = "OP-SPE-insDocIvaSpe", name = "Documento Testata STEP 3")
	public String ripetiStep3() {
		return SUCCESS;
	}
	
	/**
	 * Validazione per l'ingresso nello step2.
	 */
	public void validateEnterStep2() {
		final String methodName = "validateEnterStep2";
		log.debug(methodName, "Validazione campi obbligatori");
		
		DocumentoSpesa documento = model.getDocumento();
		Soggetto soggetto = model.getSoggetto();
		
		TipoDocumento td = documento.getTipoDocumento();
		
		checkNotNullNorInvalidUid(td, "Tipo");
		checkNotNull(documento.getAnno(), "Anno");
		checkNotNullNorEmpty(documento.getNumero(), "Numero");
		checkNotNull(documento.getDataEmissione(), "Data");
		
		boolean controlloSoggetto = soggetto != null && StringUtils.isNotBlank(soggetto.getCodiceSoggetto());
		checkCondition(controlloSoggetto, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice"));
		
		// Carico il tipo documento dalla lista in sessione
		td = ComparatorUtils.searchByUid(model.getListaTipoDocumento(), td);
		documento.setTipoDocumento(td);
		
		log.debug(methodName, "Validazione logica campi");
		if(documento.getAnno() != null) {
			checkCondition(model.getAnnoEsercizioInt().compareTo(documento.getAnno()) >= 0, ErroreFin.ANNO_DOCUMENTO_ERRATO.getErrore());
			if(documento.getDataEmissione() != null) {
				Integer annoEmissione = Integer.valueOf(FormatUtils.formatDateYear(documento.getDataEmissione()));
				// Possibile refuso di analisi?
				checkCondition(documento.getAnno().compareTo(annoEmissione) == 0, ErroreFin.LA_DATA_DEVE_ESSERE_COERENTE_CON_L_ANNO_DEL_DOCUMENTO.getErrore());
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
		
		DocumentoSpesa documento = model.getDocumento();
		checkNotNull(documento.getImporto(), "Importo");
		checkNotNullNorEmpty(documento.getDescrizione(), "Descrizione");
		
		log.debug(methodName, "Validazione logica campi");
		// L'importo deve essere maggiore di zero
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
		InserisceDocumentoSpesa request = model.creaRequestInserisceDocumentoSpesa();
		logServiceRequest(request);
		
		log.debug(methodName, "Invocazione del servizio");
		InserisceDocumentoSpesaResponse response = documentoSpesaService.inserisceTestataDocumentoSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return INPUT;
		}
		
		DocumentoSpesa documentoSpesa = response.getDocumentoSpesa();
		log.debug(methodName, "Nessun errore nell'invocazione del servizio - inserito documento con uid: " + documentoSpesa.getUid());
		model.popolaModel(documentoSpesa);
		caricaClassificatori();
		
		// Fornisci messaggio successo
		impostaInformazioneSuccesso();
		return SUCCESS;
	}

	/**
	 * Carica i classificatori dalla sessione.
	 */
	private void caricaClassificatori() {
		DocumentoSpesa documento = model.getDocumento();
		
		TipoDocumento tipoDocumento = documento.getTipoDocumento();
		List<TipoDocumento> listaTipoDocumento = model.getListaTipoDocumento();
		tipoDocumento = ComparatorUtils.searchByUid(listaTipoDocumento, tipoDocumento);
		
		documento.setTipoDocumento(tipoDocumento);
	}
	
	@Override
	protected void cleanModel() {
		super.cleanModel();
		model.setUidDocumento(null);
	}

}
