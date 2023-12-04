/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.richieste.pagamentofatture;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.pagamentofatture.InserisciPagamentoFattureCassaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.InserisceRichiestaEconomaleResponse;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDocumentiCollegatiByDocumentoSpesaResponse;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.Valuta;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * Classe di action per l'inserimento del pagamento Fatture.
 * 
 * @author Paggio Simona
 * @author Nazha Ahmad
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/02/2015
 * @version 1.0.1 - 31/03/2015
 * @version 1.1.0 - 16/09/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession(BaseInserisciAggiornaPagamentoFattureCassaEconomaleAction.MODEL_SESSION_NAME_INSERIMENTO)
public class InserisciPagamentoFattureCassaEconomaleAction extends BaseInserisciAggiornaPagamentoFattureCassaEconomaleAction<InserisciPagamentoFattureCassaEconomaleModel> {


	/** Per la serializzazione */
	private static final long serialVersionUID = -8214244927429872240L;

	
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Richiamo semplicemente il super. Aggiungo il breadcrumb
		return super.execute();
	}
	
	
	/**
	 * Preparazione per il metodo {@link #associaFattura()}.
	 */
	public void prepareAssociaFattura() {
		model.setDocumentoSpesa(null);
		model.getListaSubdocumentoSpesa().clear();
	}
	
	@Override
	public String completeStep1() {
		String superResult = super.completeStep1();
		if(INPUT.equals(superResult)) {
			return INPUT;
		}
		// SIAC-5905: Caricamento impegno a partire dal primo possibile
		popolaImpegnoSubImpegno();
		
		return superResult;
	}
	
	/**
	 * Popolamento dell'impegno e del subimpegno a partire dalle quote
	 */
	private void popolaImpegnoSubImpegno() {
		for(SubdocumentoSpesa subdocumentoSpesa : model.getListaSubdocumentoSpesa()) {
			if(hasDatiPopolati(false, subdocumentoSpesa.getImpegno())) {
				model.setMovimentoGestione(subdocumentoSpesa.getImpegno());
				if(hasDatiPopolati(true, subdocumentoSpesa.getSubImpegno())) {
					model.setSubMovimentoGestione(subdocumentoSpesa.getSubImpegno());
				}
				return;
			}
		}
	}
	
	/**
	 * Controlla se il movimento di gestione ha i dati popolati correttamente
	 * @param isSub se sia il submovimento
	 * @param mg il movimento di gestione
	 * @return se i dati sono correttamente popolati
	 */
	private boolean hasDatiPopolati(boolean isSub, MovimentoGestione mg) {
		return mg != null && (isSub || mg.getAnnoMovimento() != 0) && mg.getNumeroBigDecimal() != null;
	}
	
	/**
	 * Associa la fattura alla richiesta
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String associaFattura() {
		// Segnaposto per l'invocazione
		//cerco il dettaglio doc per reperire i correlatirecuperato da ricercasintetica
		if (model.getDocumentoSpesa().getTipoDocumento().isNotaCredito()) {
			caricaDettaglioNotaCredito();
		}
		// Imposto il documento nelle quote
		for(SubdocumentoSpesa subdocumentoSpesa : model.getListaSubdocumentoSpesa()) {
			subdocumentoSpesa.setDocumento(model.getDocumentoSpesa());
		}
		
		return SUCCESS;
	}
	
	private void caricaDettaglioNotaCredito () {
		RicercaDocumentiCollegatiByDocumentoSpesa requestCollegati = model.creaRequestRicercaDocumentiCollegatiByDocumentoSpesa(model.getDocumentoSpesa().getUid());
		logServiceRequest(requestCollegati);
		RicercaDocumentiCollegatiByDocumentoSpesaResponse responseCollegati = documentoSpesaService.ricercaDocumentiCollegatiByDocumentoSpesa(requestCollegati);
		logServiceResponse(responseCollegati);
		
		model.getDocumentoSpesa().setListaDocumentiSpesaPadre(responseCollegati.getDocumentiSpesaPadre());
		

		
	}

	/**
	 * Validazione per il metodo {@link #associaFattura()}.
	 */
	public void validateAssociaFattura() {
		controlloImpegnoSubImpegnoModalitaPagamentoQuote();
		controlloPagato();
	}

	/**
	 * Effettua il controllo sull'impegno e il subimpegno delle quote, in aggiunta anche la modalita pagamento (da associare in automatico per cassa mista/CC).
	 * <br/>
	 * Gli impegni e i subimpegni devono essere coerenti, se presenti
	 */
	private void controlloImpegnoSubImpegnoModalitaPagamentoQuote() {
		Impegno impegno = null;
		SubImpegno subImpegno = null;
		boolean valid = true;
		ModalitaPagamentoSoggetto mps = null;
		boolean validModPagamento = true;
		
		for(SubdocumentoSpesa ss : model.getListaSubdocumentoSpesa()) {
			// Controllo che impegno e subimpegno siano coerenti, se presenti
			if(ss.getImpegno() != null && ss.getImpegno().getUid() != 0) {
				if(impegno == null) {
					impegno = ss.getImpegno();
				}
				valid = valid && (impegno.getUid() == ss.getImpegno().getUid());
			}
			if(ss.getSubImpegno() != null && ss.getSubImpegno().getUid() != 0) {
				if(subImpegno == null) {
					subImpegno = ss.getSubImpegno();
				}
				valid = valid && (subImpegno.getUid() == ss.getSubImpegno().getUid());
			}
			//controllo la mod pagamento
			
			if (!TipoDiCassa.CONTANTI.equals(model.getCassaEconomale().getTipoDiCassa()) && ss.getModalitaPagamentoSoggetto()!= null && ss.getModalitaPagamentoSoggetto().getUid()!=0) {
				if(mps == null) {
					mps = ss.getModalitaPagamentoSoggetto();
				}
				validModPagamento = validModPagamento && (mps.getUid() == ss.getModalitaPagamentoSoggetto().getUid());
				
			}
		}
		checkCondition(valid && validModPagamento, ErroreCore.VALORE_NON_CONSENTITO.getErrore("quote da associare", ": sono collegate a impegni o subimpegni differenti o differisce la  modalit&agrave; pagamento"));
	}
	
	
	/**
	 * Controllo che le quote non siano pagate in CEC.
	 */
	private void controlloPagato() {
		int i = 1;
		for(SubdocumentoSpesa ss : model.getListaSubdocumentoSpesa()) {
			checkCondition(!Boolean.TRUE.equals(ss.getPagatoInCEC()), ErroreCore.VALORE_NON_CONSENTITO.getErrore("quota " + i + " da associare", ": e' gia' stata pagata in CEC"));
			i++;
		}
	}

	/**
	 * Asspocia la fattura alla richiesta
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String selezionaDati() {
		// Segnaposto per l'invocazione
		return SUCCESS;
	}
	
	@Override
	protected void impostazioneValoriDefaultStep1() {
		final String methodName = "impostazioneValoriDefaultStep1";
		RichiestaEconomale richiestaEconomale = model.getRichiestaEconomale();
		if(richiestaEconomale == null) {
			log.debug(methodName, "Inizializzazione della richiesta economale");
			richiestaEconomale = new RichiestaEconomale();
			model.setRichiestaEconomale(richiestaEconomale);
		}
		
		Valuta valutaEuro = ComparatorUtils.findByCodice(model.getListaValuta(), BilConstants.VALUTA_CODICE_EURO.getConstant());
		if(valutaEuro != null) {
			log.debug(methodName, "Impostazione del default per il le valute: EURO, uid " + valutaEuro.getUid());
			model.setUidValutaEuro(valutaEuro.getUid());
		}

	}
	

	/**
	 * Preparazione per il metodo {@link #completeStep1()}.
	 */
	public void prepareCompleteStep1() {
		// Pulisco i campi. Basta pulirne uno
		model.setRichiestaEconomale(null);
		model.setMovimentoGestione(null);
		model.setSubMovimentoGestione(null);
	}
	

	
	/**
	 * Preparazione sul metodo {@link #completeStep2()}.
	 */
	public void prepareCompleteStep2() {
		model.setMovimentoGestione(null);
		model.setSubMovimentoGestione(null);
		model.getRichiestaEconomale().setMovimento(null);
	}
	
	/**
	 * Completa il secondo step dell'inserimento della richiesta.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep2() {
		final String methodName = "completeStep2";
		InserisceRichiestaEconomale request = model.creaRequestInserisceRichiestaEconomale();
		logServiceRequest(request);
		InserisceRichiestaEconomaleResponse response = richiestaEconomaleService.inserisceRichiestaEconomalePagamentoFatture(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(InserisceRichiestaEconomale.class, response));
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Inserita richiesta economale con uid " + response.getRichiestaEconomale().getUid());
		
		// Imposto i dati nel model
		model.setRichiestaEconomale(response.getRichiestaEconomale());
		impostaImportiRichiesta(response.getRichiestaEconomale());
		
		caricaClassificatoriDaLista();
		
		impostaMessaggioSuccessoPerStep3();
		cleanImpegnoFromSession();

		return SUCCESS;
	}

	/**
	 * Validazione per il metodo {@link #completeStep2()}.
	 */
	public void validateCompleteStep2() {
		checkNotNull(model.getRichiestaEconomale(), "Movimento", true);
		checkNotNull(model.getRichiestaEconomale().getMovimento(), "Movimento", true);
		// nel caso sia una cssa non mista il select non passerebbe il valore perche' disabilitato quindi metto il valore forzato
		
		if (!TipoDiCassa.MISTA.equals(model.getCassaEconomale().getTipoDiCassa())){
			for (ModalitaPagamentoCassa mpc : model.getListaModalitaPagamentoCassa()) {
				if (model.getCassaEconomale().getTipoDiCassa().equals(mpc.getTipoDiCassa())) {
					model.getRichiestaEconomale().getMovimento().setModalitaPagamentoCassa(mpc);
				}
			}
			
		}
		Movimento m = model.getRichiestaEconomale().getMovimento();
		
		checkNotNull(m.getDataMovimento(), "Data operazione");
		checkNotNullNorInvalidUid(m.getModalitaPagamentoCassa(), "Modalita' di pagamento");
		
		checkMovimentoGestione(model.getMovimentoGestione(), model.getSubMovimentoGestione());
	}
	
	/**
	 * Pulisce il form dello step 1 riportandolo allo stato iniziale
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaStep1(){
		model.setRichiestaEconomale(null);
		return SUCCESS;
	}
	
	/**
	 * Pulisce il form dello step 2 riportandolo allo stato iniziale
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaStep2(){
		model.setMovimentoGestione(null);
		model.setSubMovimentoGestione(null);
		model.getRichiestaEconomale().getMovimento().setModalitaPagamentoCassa(null);
		model.getRichiestaEconomale().getMovimento().setModalitaPagamentoDipendente(null);
		model.getRichiestaEconomale().getMovimento().setDettaglioPagamento(null);
		model.getRichiestaEconomale().getMovimento().setBic(null);
		model.getRichiestaEconomale().getMovimento().setContoCorrente(null);
		return SUCCESS;
	}
	
	// Lotto M
	
	@Override
	protected void impostazioneValoriDefaultStep2() {
		super.impostazioneValoriDefaultStep2();
		
		if (model.getDocumentoSpesa().getTipoDocumento().isNotaCredito() && model.getDocumentoSpesa().getListaDocumentiSpesaPadre()!=null && !model.getDocumentoSpesa().getListaDocumentiSpesaPadre().isEmpty()) {
			
			impostaImpegnoNCD();
		} else {
			impostaImpegnoQuota();
		}
	}
	
	/**
	 * Impostazione dell'impegno collegato alla ncd.
	 */
	private void impostaImpegnoNCD() {
		final String methodName = "impostaImpegnoNCD";
		DocumentoSpesa fatPadre = model.getDocumentoSpesa().getListaDocumentiSpesaPadre().get(0);
		ricercaQuoteDocumentoSpesa(fatPadre);
		//per la nota di credico collegata prendo il prmo impegno che mi viene dato dai sub doc della fattura padre
		for(SubdocumentoSpesa ss : fatPadre.getListaSubdocumenti()) {
			if(isMovimentoGestioneValorizzato(ss.getImpegno()) || isMovimentoGestioneValorizzato(ss.getSubImpegno())) {
				try {
					calcolaMovimentoGestione(ss.getImpegno(), ss.getSubImpegno());
					return;
				} catch (WebServiceInvocationFailureException wsife) {
					log.debug(methodName, "Ignoro la quota del subdocumento " + ss.getNumero() + ": " + wsife.getMessage());
				}
			}
		}
	}
	

	/**
	 * Impostazione dell'impegno collegato alla quota.
	 */
	private void impostaImpegnoQuota() {
		final String methodName = "impostaImpegnoQuota";
		for(SubdocumentoSpesa ss : model.getListaSubdocumentoSpesa()) {
			if(isMovimentoGestioneValorizzato(ss.getImpegno()) || isMovimentoGestioneValorizzato(ss.getSubImpegno())) {
				try {
					calcolaMovimentoGestione(ss.getImpegno(), ss.getSubImpegno());
					return;
				} catch (WebServiceInvocationFailureException wsife) {
					log.debug(methodName, "Ignoro la quota del subdocumento " + ss.getNumero() + ": " + wsife.getMessage());
				}
			}
		}
	}
	
	/**
	 * Calcola il movimento di gestione 
	 * 
	 * @param impegno    l'impegno da cui far partire la ricerca
	 * @param subImpegno il subImpegno da cui far partire la ricerca
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void calcolaMovimentoGestione(Impegno impegno, SubImpegno subImpegno) throws WebServiceInvocationFailureException {
		Impegno impegnoService = ottieniImpegnoDaServizio(impegno).getImpegno();
		
		// Calcolo i dati del subimpegno se presente
		if(subImpegno != null && subImpegno.getUid() != 0) {
			SubImpegno si = calcolaSubmovimentoGestione(impegnoService, subImpegno);
			model.setSubMovimentoGestione(si);
		}
		
		model.setMovimentoGestione(impegnoService);
	}

	/**
	 * Calcola il submovimento di gestione 
	 * 
	 * @param impegno    l'impegno ottenuto dal servizio
	 * @param subImpegno il subImpegno da cui far partire la ricerca
	 * @return il subimpengo corrispondente
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private SubImpegno calcolaSubmovimentoGestione(Impegno impegno, SubImpegno subImpegno) throws WebServiceInvocationFailureException {
		if(impegno.getElencoSubImpegni() == null) {
			throw new WebServiceInvocationFailureException("Impegno senza subImpegni");
		}
		for(SubImpegno si : impegno.getElencoSubImpegni()) {
			if(si != null && si.getUid() == subImpegno.getUid()) {
				return si;
			}
		}
		throw new WebServiceInvocationFailureException("Impegno con subImpegni, ma nessun subImpegno presente con uid " + subImpegno.getUid());
	}

	/**
	 * Controlla se il movimento di gestione fornito &eacute; valorizzato correttamente.
	 * 
	 * @param movimentoGestione l'entita da controllare
	 * @return <code>true</code> se l'entita &eacute; non-<code>null</code> e con uid diverso da 0; <code>false</code> altrimenti
	 */
	private boolean isMovimentoGestioneValorizzato(MovimentoGestione movimentoGestione) {
		return movimentoGestione != null && movimentoGestione.getNumeroBigDecimal() != null && movimentoGestione.getAnnoMovimento() != 0;
	}

	@Override
	protected AzioneConsentitaEnum[] retrieveAzioniConsentite() {
		return new AzioneConsentitaEnum[] {AzioneConsentitaEnum.CASSA_ECONOMALE_PAGAMENTO_FATTURE_INSERISCI, AzioneConsentitaEnum.CASSA_ECONOMALE_PAGAMENTO_FATTURE_ABILITA};
	}
}
