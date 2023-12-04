/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.documento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.documento.InserisciPrimaNotaIntegrataDocumentoBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.selector.CausaleEPSelector;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoQuotaRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrata;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrataFactory;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinDocumentoHelper;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.business.utility.BilUtilities;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoIva;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClasseDiConciliazione;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

/**
 * Classe base di action per l'inserimento della prima integrata per il subddocumento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/05/2015
 * @version 1.1.0 - 14/10/2015 - gestione GEN/GSA
 * 
 * @param <D>  la tipizzazione del documento
 * @param <S>  la tipizzazione del subdocumento
 * @param <SI> la tipizzazione del subdocumento iva
 * @param <E>  la tipizzazione del wrapper
 * @param <H>  la tipizzazione dell'helper
 * @param <M>  la tipizzazione del model
 *
 */
public abstract class InserisciPrimaNotaIntegrataSubdocumentoBaseAction <D extends Documento<S, SI>, S extends Subdocumento<D, SI>, SI extends SubdocumentoIva<D, S, SI>,
	E extends ElementoQuotaRegistrazioneMovFin<D, S>, H extends ConsultaRegistrazioneMovFinDocumentoHelper<D, S, SI>, M extends InserisciPrimaNotaIntegrataDocumentoBaseModel<D, S, SI, E, D, S, SI, H>>
		extends InserisciPrimaNotaIntegrataDocumentoBaseAction<D, S, SI, E, H, M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 9174159852005414039L;
	
	@Autowired private transient ContoService contoService;

	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		model.getClass();
		checkCasoDUsoApplicabile();
		
		// In inserimento arrivo da registrazione
		try {
			ricercaDettaglioRegistrazioneMovFin();
			log.debug(methodName, "registrazione ottenuta");
			
			// calcolo del documento
			impostazioneDocumento();
			log.debug(methodName, "subdocumento ottenuto");
		} catch (WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			// Metto in sessione gli errori ed esco
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		
		sessionHandler.setParametro(BilSessionParameter.NON_PULIRE_MODEL, Boolean.TRUE);
		return SUCCESS;
	}

	/**
	 * Impostazione del documento
	 * 
	 * @throws WebServiceInvocationFailureException  in caso di fallimento nell'invocazione del servizio
	 */
	protected void impostazioneDocumento() throws WebServiceInvocationFailureException {
		// To implement
	}
	
	/**
	 * Preparazione per il metodo {@link #enterDettaglio()}.
	 */
	public void prepareEnterDettaglio() {
		model.setSubdocumento(null);
		model.setMovimentoEP(null);
		
		model.setCausaleEP(null);
		model.setListaConto(null);
		model.setTotaleDare(BigDecimal.ZERO);
		model.setTotaleAvere(BigDecimal.ZERO);
	}
	
	/**
	 * Caricamento dati per ingresso nel dettaglio
	 * 
	 * @return una stringa corrispondente dal risultato dell'invocazione
	 */
	public String enterDettaglio() {
		final String methodName = "enterDettaglio";
		try {
			caricaDettaglioRegistrazioneMovFin();
			ottieniDettaglioSubdocumento();
			sessionHandler.setParametro(BilSessionParameter.ULTIMO_TIPO_EVENTO_RICERCATO, null);
			caricaListaCausaleEP(true);
			ottieniDatiMovimentoEP();
			calcolaTotaleDareAvere();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		
		
		return SUCCESS;
	}
	
	/**
	 * Carica il dettaglio della registrazione movimento fin.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	protected void caricaDettaglioRegistrazioneMovFin() throws WebServiceInvocationFailureException {
		RegistrazioneMovFin registrazioneMovFin = sessionHandler.getParametro(BilSessionParameter.REGISTRAZIONEMOVFIN);
		if(registrazioneMovFin == null || registrazioneMovFin.getUid() != model.getUidRegistrazione()){
			registrazioneMovFin = ricaricaDettaglioRegistrazioneMovFin();
			sessionHandler.setParametro(BilSessionParameter.REGISTRAZIONEMOVFIN, registrazioneMovFin);
		}
		if(registrazioneMovFin == null) {
			throw new GenericFrontEndMessagesException("Nessuna registrazione caricata dal sistema");
		}
		model.setRegistrazioneMovFin(registrazioneMovFin);
	}
	
	
	private RegistrazioneMovFin ricaricaDettaglioRegistrazioneMovFin() throws WebServiceInvocationFailureException{
		String methodName = "ricaricaDettaglioRegistrazioneMovFin";
		RicercaDettaglioRegistrazioneMovFin request = model.creaRequestRicercaDettaglioRegistrazioneMovFin();
		logServiceRequest(request);
		
		RicercaDettaglioRegistrazioneMovFinResponse response = registrazioneMovFinService.ricercaDettaglioRegistrazioneMovFin(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettaglioRegistrazioneMovFin.class, response));
			addErrori(response);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioRegistrazioneMovFin.class, response));
		}
		if (response.getRegistrazioneMovFin() == null) {
			log.info(methodName, "RegistrazioneMovFin con uid " + model.getUidRegistrazione() + " non presente");
			addErrore(ErroreCore.ENTITA_INESISTENTE.getErrore("Registrazione movimento finanziario", model.getUidRegistrazione()));
			throw new WebServiceInvocationFailureException("Registrazione movimento finanziario con uid " + model.getUidRegistrazione() + " non presente");
		}
		
		return response.getRegistrazioneMovFin();
	}

	/**
	 * Ottiene il dettaglio del subdocumento.
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void ottieniDettaglioSubdocumento() throws WebServiceInvocationFailureException {
		// To implement
	}

	/**
	 * Ottiene i dati del movimento EP.
	 */
	private void ottieniDatiMovimentoEP() {
		E wrapper = findWrapperByUidSubdocumento();
		if(wrapper == null || wrapper.getRegistrazioneMovFin() == null
				|| wrapper.getRegistrazioneMovFin().getListaMovimentiEP().isEmpty()
				|| wrapper.getRegistrazioneMovFin().getListaMovimentiEP().get(0).getCausaleEP() == null
				|| wrapper.getRegistrazioneMovFin().getListaMovimentiEP().get(0).getCausaleEP().getUid() == 0) {
			// Non ho il dato. Inizializzo a nuovo ed esco
			model.setMovimentoEP(new MovimentoEP());
			model.setCausaleEPOriginaria(null);
			impostaValoriDefaultMovimentoEP();
			return;
		}
		MovimentoEP movimentoEP = ReflectionUtil.deepClone(wrapper.getRegistrazioneMovFin().getListaMovimentiEP().get(0));
		model.setMovimentoEP(movimentoEP);
		// SIAC-6062
		model.setCausaleEPOriginaria(wrapper.getRegistrazioneMovFin().getListaMovimentiEP().get(0).getCausaleEP());
	}

	/**
	 * Impostazione dei valori di default.
	 */
	private void impostaValoriDefaultMovimentoEP() {
		MovimentoEP movimentoEP = model.getMovimentoEP();
		movimentoEP.setDescrizione("MOVIMENTO ASSOCIATO A QUOTA DOCUMENTO");
		CausaleEP causaleEP = findCausaleEP();
		movimentoEP.setCausaleEP(causaleEP);
	}
	
	/**
	 * Trova la causale di integrazione.
	 * 
	 * @return la causale trovata
	 */
	private CausaleEP findCausaleEP() {
		CausaleEPSelector selector = istanziaSelettoreCausale();
		return selector.selezionaCausaleEP(model.getListaCausaleEP());
	}
	
	/**
	 * Calcolo dei totali dare ed avere
	 */
	protected void calcolaTotaleDareAvere() {
		BigDecimal totaleDare = BigDecimal.ZERO;
		BigDecimal totaleAvere = BigDecimal.ZERO;
		
		for (ElementoScritturaPrimaNotaIntegrata datoScrittura : model.getListaElementoScritturaPerElaborazione()) {
			if(datoScrittura.getMovimentoDettaglio().getImporto() != null) {
				if(datoScrittura.isSegnoAvere()) {
					totaleAvere = totaleAvere.add(datoScrittura.getMovimentoDettaglio().getImporto());
				} else if(datoScrittura.isSegnoDare()) {
					totaleDare = totaleDare.add(datoScrittura.getMovimentoDettaglio().getImporto());
				}
			}
		}
		
		model.setTotaleAvere(totaleAvere);
		model.setTotaleDare(totaleDare);
		model.setDaRegistrare(totaleAvere.subtract(totaleDare));
	}

	/**
	 * Validazione per il metodo {@link #enterDettaglio()}.
	 */
	public void validateEnterDettaglio() {
		checkNotNullNorInvalidUid(model.getSubdocumento(), "Subdocumento");
	}
	
	/**
	 * Ingresso nella pagina di dettaglio.
	 * 
	 * @return una stringa corrispondente dal risultato dell'invocazione
	 */
	public String dettaglio() {
		return SUCCESS;
	}
	
	/**
	 * Reindirizzamento verso lo step 1.
	 * 
	 * @return una stringa corrispondente dal risultato dell'invocazione
	 */
	public String backToStep1() {
		return SUCCESS;
	}
	
	/**
	 * Ottiene la lista dei conti collegati al movimento.
	 * 
	 * @return una stringa corrispondente dal risultato dell'invocazione
	 */
	public String ottieniListaConti() {
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #proponiContiDaCausaleEP()}.
	 */
	public void prepareProponiContiDaCausaleEP() {
		model.setCausaleEP(null);
		model.setListaElementoScritturaPerElaborazione(new ArrayList<ElementoScritturaPrimaNotaIntegrata>());
	}
	
	/**
	 * Proposta dei conti per il movimento EP a partire dalla causale EP selezionata.
	 * 
	 * @return una stringa corrispondente dal risultato dell'invocazione
	 */
	public String proponiContiDaCausaleEP() {
		final String methodName = "proponiContiDaCausaleEP";
		getDatiCausaleDaLista();
		CausaleEP causaleEP = model.getCausaleEP();
		
		if(model.getCausaleEP() == null || model.getCausaleEP().getUid() == 0) {
			// Se non ho caricato la causale, svuoto i conti
			log.debug(methodName, "Nessuna causale EP selezionata");
			model.setListaElementoScrittura(new ArrayList<ElementoScritturaPrimaNotaIntegrata>());
			model.setListaElementoScritturaPerElaborazione(new ArrayList<ElementoScritturaPrimaNotaIntegrata>());
			calcolaTotaleDareAvere();
			return SUCCESS;
		}
		
		List<ElementoScritturaPrimaNotaIntegrata> listaElementoScrittura = ElementoScritturaPrimaNotaIntegrataFactory.creaListaScrittureDaCausaleEP(causaleEP, BigDecimal.ZERO);
		List<ElementoScritturaPrimaNotaIntegrata> listaElementoScritturaPerElaborazione = ReflectionUtil.deepClone(listaElementoScrittura);
		
		impostaImportiScritture(listaElementoScritturaPerElaborazione);
		
		model.setListaElementoScrittura(listaElementoScrittura);
		model.setListaElementoScritturaPerElaborazione(listaElementoScritturaPerElaborazione);
		
		// Ricalcolo Dare e avere
		calcolaTotaleDareAvere();
		
		return SUCCESS;
	}
	
	/**
	 * Ottiene i dati della causale dalla lista
	 */
	protected void getDatiCausaleDaLista() {
		if(model.getCausaleEP() == null) {
			return;
		}
		int uidCausaleEP = model.getCausaleEP().getUid();
		for (CausaleEP causaleEP : model.getListaCausaleEP()) {
			if (uidCausaleEP == causaleEP.getUid()) {
				CausaleEP clone = ReflectionUtil.deepClone(causaleEP);
				model.setCausaleEP(clone);
				return;
			}
		}
	}

	/**
	 * Impostazione degli importi delle scritture.
	 * 
	 * @param listaElementoScrittura la lista delle scritture
	 */
	protected void impostaImportiScritture(List<ElementoScritturaPrimaNotaIntegrata> listaElementoScrittura) {
		final String methodName = "impostaImportiScritture";
		// Trovo il wrapper corretto
		E wrapper = null;
		for(E eqsrmf : model.getListaElementoQuota()) {
			if(eqsrmf.getSubdocumento() != null && eqsrmf.getSubdocumento().getUid() == model.getSubdocumento().getUid()) {
				wrapper = eqsrmf;
				break;
			}
		}
		if(wrapper == null) {
			// C'e' un errore?
			log.debug(methodName, "Wrapper non trovato per subdocumento " + model.getSubdocumento().getUid());
			return;
		}
		//JIRA 3286 se il documento e' NCD
		boolean isNCD = model.getDocumento() !=null && model.getDocumento().getTipoDocumento() !=null && model.getDocumento().getTipoDocumento().isNotaCredito();
		if(model.getSubdocumento().getSubdocumentoIva() != null) {
			// Ho i dati iva: effettuo il calcolo
			calcolaDatiScrittureIva(isNCD, listaElementoScrittura, model.getSubdocumento().getSubdocumentoIva());
			return;
		}
		
		if(!wrapper.isRilevanteIva()) {
			// Copio semplicemente i valori
			calcolaDatiScritturaNoIvaNoRilevante(listaElementoScrittura, wrapper);
			return;
		}
		
		// Il wrapper e' rilevante IVA. Effettuo il calcolo
		calcolaDatiScritturaNoIvaRilevante(isNCD,listaElementoScrittura, wrapper);
	}

	/**
	 * Calcola i dati di scrittura per i dati IVA.
	 * 
	 * @param isNCD                  se sia un NCD
	 * @param listaElementoScrittura la lista delle scritture
	 * @param subdocumentoIva        il subdocumento iva
	 */
	private void calcolaDatiScrittureIva(boolean isNCD, List<ElementoScritturaPrimaNotaIntegrata> listaElementoScrittura, SI subdocumentoIva) {
		
		//Pre SIAC-4734 
//		BigDecimal imponibile = subdocumentoIva.getTotaleImponibileMovimentiIva();
//		BigDecimal impostaDetraibile = subdocumentoIva.getTotaleImpostaDetraibileMovimentiIva(); //Solo imposta detraibile (vedi SIAC-4604)
		
		//Post SIAC-4734
		BigDecimal imponibile = subdocumentoIva.getTotaleImponibileMovimentiIva();
		BigDecimal impostaIndetraibile = subdocumentoIva.getTotaleImpostaIndetraibileMovimentiIva();
		imponibile = imponibile.add(impostaIndetraibile).setScale(BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getPrecision(), BilUtilities.MATH_CONTEXT_TWO_HALF_DOWN.getRoundingMode());
		
		BigDecimal imposta = subdocumentoIva.getTotaleImpostaDetraibileMovimentiIva();
		
		
		// Se non corrispondono al totale della quota, lascio perdere
		
		for(ElementoScritturaPrimaNotaIntegrata elementoScrittura : listaElementoScrittura) {
			setImportoInElementoScrittura(isNCD,imponibile, imposta, elementoScrittura);
		}
	}

	/**
	 * Imposta l'importo nell'elemento scrittura.
	 * 
	 * @param isNCD             indica se il documento &eacute; di tipo NCD
	 * @param imponibile        l'imponibile
	 * @param impostaDetraibile l'imposta detraibile
	 * @param elementoScrittura il wrapper della scrittura
	 */
	protected abstract void setImportoInElementoScrittura(boolean isNCD, BigDecimal imponibile, BigDecimal impostaDetraibile, ElementoScritturaPrimaNotaIntegrata elementoScrittura);

	/**
	 * Calcola i dati di scrittura per i dati senza IVA e non rilevanti.
	 * 
	 * @param listaElementoScrittura la lista delle scritture
	 * @param wrapper                il wrapper
	 */
	private void calcolaDatiScritturaNoIvaNoRilevante(List<ElementoScritturaPrimaNotaIntegrata> listaElementoScrittura, E wrapper) {
		for(ElementoScritturaPrimaNotaIntegrata elementoScrittura : listaElementoScrittura) {
			if(!elementoScrittura.isTipoImportoImposta()) {
				// Imposto l'imponibile come importo
				elementoScrittura.getMovimentoDettaglio().setImporto(wrapper.getImponibileDaImpostare());
			}
		}
	}
	
	/**
	 * Calcola i dati di scrittura per i dati senza IVA e rilevanti.
	 * 
	 * @param listaElementoScrittura la lista delle scritture
	 * @param wrapper                il wrapper
	 */
	private void calcolaDatiScritturaNoIvaRilevante(boolean isNCD,List<ElementoScritturaPrimaNotaIntegrata> listaElementoScrittura, E wrapper) {
		for(ElementoScritturaPrimaNotaIntegrata elementoScrittura : listaElementoScrittura) {
			setImportoInElementoScrittura(isNCD,wrapper.getImponibileDaImpostare(), wrapper.getImpostaDaImpostare(), elementoScrittura);
		}
	}
	
	/**
	 * Preparazione per il metodo {@link #aggiornaConto()}.
	 */
	public void prepareAggiornaConto() {
		model.setImporto(null);
		model.setOperazioneSegnoConto(null);
		model.setIndiceConto(null);
	}
	
	/**
	 * Aggiorna la singolaRiga
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaConto() {
		int idx = model.getIndiceConto().intValue();
		
		ElementoScritturaPrimaNotaIntegrata elementoScrittura = model.getListaElementoScritturaPerElaborazione().get(idx);
		elementoScrittura.getMovimentoDettaglio().setImporto(model.getImporto());
		if (!model.isContiCausale()) {
			elementoScrittura.getMovimentoDettaglio().setSegno(model.getOperazioneSegnoConto());
			elementoScrittura.getContoTipoOperazione().setOperazioneSegnoConto(model.getOperazioneSegnoConto());
		}
		elementoScrittura.setAggiornamentoImportoManuale(true);

		model.getListaElementoScritturaPerElaborazione().set(idx, elementoScrittura);
		calcolaTotaleDareAvere();
		model.setIndiceConto(null);
		impostaInformazioneSuccesso();
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #aggiornaConto()}.
	 */
	public void validateAggiornaConto() {
		checkNotNull(model.getImporto(), "Importo");
		checkCondition(model.getImporto() == null || model.getImporto().signum() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Importo", "deve essere positivo"));
		checkNotNull(model.getOperazioneSegnoConto(), "Segno");
		checkNotNull(model.getIndiceConto(), "Indice", true);
		
		int idx =  model.getIndiceConto().intValue();
		int size = model.getListaElementoScritturaPerElaborazione().size();
		checkCondition(idx >= 0 && idx < size, ErroreCore.FORMATO_NON_VALIDO.getErrore("Indice", "deve essere compreso tra 0 e " + size), true);
		
		ElementoScritturaPrimaNotaIntegrata elementoScrittura = model.getListaElementoScritturaPerElaborazione().get(idx);
		checkNotNull(elementoScrittura, "Scrittura", true);
		checkCondition(!elementoScrittura.isUtilizzoImportoNonModificabile(), ErroreGEN.MOVIMENTO_CONTABILE_NON_MODIFICABILE.getErrore("l'utilizzo dell'importo e' non modificabile"));
	}
	
	/**
	 * Preparazione per il metodo {@link #eliminaConto()}.
	 */
	public void prepareEliminaConto() {
		model.setIndiceConto(null);
	}
	
	/**
	 * Elimina la singolaRiga
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String eliminaConto() {
		int idx = model.getIndiceConto().intValue();
		model.getListaElementoScritturaPerElaborazione().remove(idx);
		
		calcolaTotaleDareAvere();
		model.setIndiceConto(null);
		impostaInformazioneSuccesso();
		return SUCCESS;
	}
	
	/**
	 * validate per Elimina la singolaRiga
	 */
	public void validateEliminaConto() {
		checkCondition(!model.isContiCausale(), ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Il conto deriva da una causale"), true);
		checkNotNull(model.getIndiceConto(), "Indice");
		
		int idx =  model.getIndiceConto().intValue();
		int size = model.getListaElementoScritturaPerElaborazione().size();
		checkCondition(idx >= 0 && idx < size, ErroreCore.FORMATO_NON_VALIDO.getErrore("Indice", "deve essere compreso tra 0 e " + size), true);
		
		ElementoScritturaPrimaNotaIntegrata elementoScrittura = model.getListaElementoScritturaPerElaborazione().get(idx);
		checkNotNull(elementoScrittura, "Scrittura", true);
		checkCondition(!elementoScrittura.isUtilizzoContoObbligatorio(), ErroreGEN.MOVIMENTO_CONTABILE_NON_ELIMINABILE.getErrore("l'utilizzo del conto e' obbligatorio"));
	}
	
	/**
	 * Aggiornamento del conto dalla classe di conciliazione
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaContoDaClasseDiConciliazione() {
		int idx = model.getIndiceConto().intValue(); 
		//SIAC-8199
		Conto contoSelezionato = impostaDatiConto(); // 
		ElementoScritturaPrimaNotaIntegrata elementoScrittura = model.getListaElementoScritturaPerElaborazione().get(idx);
//		elementoScrittura.getContoTipoOperazione().setConto(model.getContoDaSostituire());
		elementoScrittura.getMovimentoDettaglio().setConto(contoSelezionato);
		model.getListaElementoScritturaPerElaborazione().set(idx, elementoScrittura);
		calcolaTotaleDareAvere();
		impostaInformazioneSuccesso();
		model.setIndiceConto(null);
		return SUCCESS;
	}
	
	/**
	 * valida il metodo aggiornaContoDaClasseDiConciliazioneConDigitazione
	 */
	public void validateAggiornaContoDaClasseDiConciliazioneConDigitazione(){
		Conto conto = checkAndObtainContoFogliaEsistenteUnivoco();
		
		//SIAC-7388
		checkContoInListaContiClasse(conto);
		// Imposto il conto nel model
		model.setContoDaSostituire(conto);
	}

	private Conto checkAndObtainContoFogliaEsistenteUnivoco() {
		RicercaSinteticaConto request = model.creaRequestRicercaSinteticaConto(model.getConto());
		logServiceRequest(request);
	
		RicercaSinteticaContoResponse response = contoService.ricercaSinteticaConto(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			// Se ho errori esco
			addErrori(response);
			throw new ParamValidationException(createErrorInServiceInvocationString(RicercaSinteticaConto.class, response));
		}
		checkCondition(!response.getConti().isEmpty(), ErroreCore.ENTITA_NON_TROVATA.getErrore("Conto", model.getConto().getCodice()), true);
		checkCondition(response.getConti().size() < 2, ErroreFin.OGGETTO_NON_UNIVOCO.getErrore("Conto"), true);
		
		Conto conto = response.getConti().get(0);
		checkCondition(Boolean.TRUE.equals(conto.getContoFoglia()), ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto " + conto.getCodice(), "non e' un Conto foglia"), true);
		return conto;
	}
	
	private void checkContoInListaContiClasse(Conto conto) {
		int idx = model.getIndiceConto().intValue();
		ElementoScritturaPrimaNotaIntegrata elementoScrittura = model.getListaElementoScritturaPerElaborazione().get(idx);
		ClasseDiConciliazione cl = elementoScrittura.getClasseDiConcilazione();
		
		if(cl != null && ClasseDiConciliazione.CONTI.equals(cl)) {
			//TODO: valutare con analista
			return;
		}
		checkCondition(cl != null && elementoScrittura.getContiSelezionabiliDaClasseDiConciliazione() != null && !elementoScrittura.getContiSelezionabiliDaClasseDiConciliazione().isEmpty(), 
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("La classe di conciliazione non presenta conti con cui confrontare il conto digitato."), true);
		
		checkCondition(isContoAppartenenteAClasseConciliazione(conto, elementoScrittura.getContiSelezionabiliDaClasseDiConciliazione()), 
				ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("Il conto digitato (" + StringUtils.defaultIfBlank(conto.getCodice(), "null") 
				+ " ) non risulta essere tra i conti della classe " + StringUtils.defaultIfBlank(model.getClasseDiConciliazioneContoDigitato(), ""), true));
	}

	private boolean  isContoAppartenenteAClasseConciliazione(Conto conto, List<Conto> contiClasse) {
		for (Conto ct : contiClasse) {
			if(ct.getCodice().equalsIgnoreCase(conto.getCodice())) {
				return true;
			}
		}
		return false;
	}

	
	/**
	 * Aggiornamento del conto dalla classe di conciliazione di tipo Conti
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String aggiornaContoDaClasseDiConciliazioneConDigitazione() {
		return aggiornaContoDaClasseDiConciliazione();
	}
	
	
	private Conto impostaDatiConto() {
		if(model.getContoDaSostituire() != null && model.getContoDaSostituire().getUid() != 0){
			log.debug("impostaDatiConto", "ho il conto!");
			RicercaDettaglioConto req = model.creaRequestRicercaDettaglioConto(model.getContoDaSostituire());
			RicercaDettaglioContoResponse res = contoService.ricercaDettaglioConto(req);
//			model.setContoDaSostituire(res.getConto());
			return res.getConto();
		}else{
			log.debug("impostaDatiConto", "non ho il conto!");
//			model.setContoDaSostituire(null);
			return null;
		}
	}
	
	/**
	 * Trova il wrapper a partire dall'uid del subdocumento.
	 * 
	 * @return il wrapper corrispondente all'uid del subdocumento, se presente; <code>null</code> in caso contrario
	 */
	protected E findWrapperByUidSubdocumento() {
		List<E> listaElementoQuota = model.getListaElementoQuota();
		S subdocumento = model.getSubdocumento();
		
		for(E eqrmf : listaElementoQuota) {
			if(eqrmf.getSubdocumento() != null && eqrmf.getSubdocumento().getUid() == subdocumento.getUid()) {
				return eqrmf;
			}
		}
		return null;
	}
	
	@Override
	public void prepareInserisciPrimaNota() {
		model.setMovimentoEP(null);
	}
	
	@Override
	public String inserisciPrimaNota() {
		// Aggiungo i dati ma non valido la nota
		return registraPrimaNota(false);
	}
	
	@Override
	public void validateInserisciPrimaNota() {
		super.validateInserisciPrimaNota();
		checkAggiuntiviSubdocumento();
	}
	
	@Override
	public void prepareAggiornaPrimaNota() {
		model.setMovimentoEP(null);
	}
	
	@Override
	public String aggiornaPrimaNota() {
		// Aggiungo i dati ma non valido la nota
		return registraPrimaNota(false);
	}
	
	@Override
	public void validateAggiornaPrimaNota() {
		super.validateAggiornaPrimaNota();
		checkAggiuntiviSubdocumento();
	}

	/**
	 * I check aggiuntivi da effettuare per il subdocumento.
	 */
	private void checkAggiuntiviSubdocumento() {
		checkNotNull(model.getMovimentoEP(), "Movimento", true);
		checkNotNullNorInvalidUid(model.getMovimentoEP().getCausaleEP(), "Causale");
		checkNotNullNorEmpty(model.getMovimentoEP().getDescrizione(), "Descrizione");
		
		// Controllo la correttezza delle scritture
		List<MovimentoDettaglio> listaMovimentoDettaglio = checkScrittureCorrette();
		model.getMovimentoEP().setListaMovimentoDettaglio(listaMovimentoDettaglio);
		
		if(model.getMovimentoEP().getCausaleEP() != null && model.getMovimentoEP().getCausaleEP().getUid() != 0) {
			CausaleEP causaleEP = ComparatorUtils.searchByUidEventuallyNull(model.getListaCausaleEP(), model.getMovimentoEP().getCausaleEP());
			checkCondition(causaleEP != null && causaleEP.getUid() != 0, ErroreCore.ENTITA_NON_TROVATA.getErrore("Causale", "uid " + model.getMovimentoEP().getCausaleEP().getUid()), true);
			model.getMovimentoEP().setCausaleEP(causaleEP);
		}
	}
	
	/**
	 * Ottiene il soggetto.
	 * 
	 * @return il soggetto del documento
	 */
	protected Soggetto ottieniSoggetto() {
		return model.getDocumento() != null ? model.getDocumento().getSoggetto() : null;
	}
	
	/**
	 * Aggiornamento del conto dalla classe di conciliazione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniContiDaClasseDiConciliazione() {
		int idx = model.getIndiceConto().intValue();
		ElementoScritturaPrimaNotaIntegrata elementoScrittura = model.getListaElementoScritturaPerElaborazione().get(idx);
		model.setListaContoDaClasseConciliazione(elementoScrittura.getContiSelezionabiliDaClasseDiConciliazione());
		model.setIndiceConto(null);
		return SUCCESS;
	}
}

