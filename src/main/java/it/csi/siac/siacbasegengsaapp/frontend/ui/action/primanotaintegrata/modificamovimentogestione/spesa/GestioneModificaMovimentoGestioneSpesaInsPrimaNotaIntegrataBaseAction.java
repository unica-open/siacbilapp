/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.modificamovimentogestione.spesa;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.modificamovimentogestione.spesa.GestioneModificaMovimentoGestioneSpesaPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaHelper;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommonapp.util.exception.FrontEndUncheckedException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, modifica del movimento gestione di spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/11/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneModificaMovimentoGestioneSpesaInsPrimaNotaIntegrataBaseAction<M extends GestioneModificaMovimentoGestioneSpesaPrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaPrimaNotaIntegrataBaseAction<Impegno, ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaHelper, M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8985890954785351729L;

	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	
	/** Nome del model del completamento e validazione dell'accertamento. Modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_MODIFICA_MOVIMENTO_GESTIONE_SPESA_FIN = "CompletaValidaModificaMovimentoGestioneSpesaInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento e validazione dell'accertamento. Modulo GSA */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_MODIFICA_MOVIMENTO_GESTIONE_SPESA_GSA = "CompletaValidaModificaMovimentoGestioneSpesaInsPrimaNotaIntegrataGSAModel";
	/** Nome del model del completamento dell'accertamento, modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_MODIFICA_MOVIMENTO_GESTIONE_SPESA_FIN = "CompletaModificaMovimentoGestioneSpesaInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento dell'accertamento, modulo GSA */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_MODIFICA_MOVIMENTO_GESTIONE_SPESA_GSA = "CompletaModificaMovimentoGestioneSpesaInsPrimaNotaIntegrataGSAModel";
	
	@Override
	public String execute() throws Exception {
		final String methodName = "execute";
		
		checkCasoDUsoApplicabile();
		
		// Recupero registrazione dalla sessione
		RegistrazioneMovFin registrazioneMovFin = sessionHandler.getParametro(BilSessionParameter.REGISTRAZIONEMOVFIN);
		model.setRegistrazioneMovFin(registrazioneMovFin);
		log.debug(methodName, "registrazione ottenuta");
		
		if(inEsecuzioneRegistrazioneMovFin()) {
			return INPUT;
		}
		
		sessionHandler.setParametro(BilSessionParameter.MODIFICA_MOVIMENTO_GESTIONE_SPESA, null);
		
		ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa = (ModificaMovimentoGestioneSpesa) model.getRegistrazioneMovFin().getMovimento();
		model.setModificaMovimentoGestioneSpesa(modificaMovimentoGestioneSpesa);
		
		if(model.getPrimaNota() == null){
			model.setPrimaNota(new PrimaNota());
			model.impostaDatiNelModel();
		}
		try {
			computaStringheDescrizioneDaRegistrazione(model.getRegistrazioneMovFin());
		} catch (FrontEndUncheckedException febe) {
			log.info(methodName, febe.getMessage());
			// Metto in sessione gli errori ed esco
			model.addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore(febe.getMessage()));
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}

		try{
			caricaListaCausaleEP(false);
			caricaListaClassi();
			caricaListaTitoli();
		}catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			// Metto in sessione gli errori ed esco
			setErroriInSessionePerActionSuccessiva();
			return INPUT;
		}
		
		getDatiDaModificaMovimentoGestioneSpesa(modificaMovimentoGestioneSpesa);
		Soggetto soggetto = ricavaSoggettoDaMovimento(modificaMovimentoGestioneSpesa, model.getSubImpegno(), model.getImpegno());
		model.setSoggettoMovimentoFIN(soggetto);
		return SUCCESS;
	}

	/**
	 * Ottiene i dati della modifica del movimento di gestione di spesa
	 * @param modificaMovimentoGestioneSpesa il subimpegno da cui ottenere i dati
	 */
	private void getDatiDaModificaMovimentoGestioneSpesa(ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa){
		RicercaImpegnoPerChiaveOttimizzato req = model.creaRequestRicercaImpegnoPerChiaveOttimizzato(modificaMovimentoGestioneSpesa);
		logServiceRequest(req);
		
		RicercaImpegnoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			return;
		}
		
		Impegno impegnoServizio = res.getImpegno();
		SubImpegno subImpegnoServizio = findSubImpegno(res.getImpegno(), modificaMovimentoGestioneSpesa);
		ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesaServizio = findModificaMovimentoGestioneSpesa(subImpegnoServizio != null ? subImpegnoServizio : impegnoServizio, modificaMovimentoGestioneSpesa);
		
		sessionHandler.setParametro(BilSessionParameter.MODIFICA_MOVIMENTO_GESTIONE_SPESA, modificaMovimentoGestioneSpesaServizio);
		sessionHandler.setParametro(BilSessionParameter.IMPEGNO, impegnoServizio);
		sessionHandler.setParametro(BilSessionParameter.SUBIMPEGNO, subImpegnoServizio);
		
		model.setModificaMovimentoGestioneSpesa(modificaMovimentoGestioneSpesaServizio);
		model.setImpegno(impegnoServizio);
		model.setSubImpegno(subImpegnoServizio);
		model.setClassificatoreGerarchico(model.getImpegno().getCapitoloUscitaGestione().getMacroaggregato());
		
		// SIAC-5294
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinModificaMovimentoGestioneSpesaHelper(impegnoServizio, subImpegnoServizio, modificaMovimentoGestioneSpesaServizio, model.isGestioneUEB()));
	}
	
	/**
	 * Trova la modifica dall'impegno restituito dal servizio di ricerca.
	 * 
	 * @param impegno                        l'impegno del servizio di ricerca
	 * @param modificaMovimentoGestioneSpesa la modifica con uid da trovare
	 * @return la modifica
	 */
	private ModificaMovimentoGestioneSpesa findModificaMovimentoGestioneSpesa(Impegno impegno, ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa) {
		final String methodName = "findModificaMovimentoGestioneSpesa";
		if(impegno == null || impegno.getListaModificheMovimentoGestioneSpesa() == null) {
			log.warn(methodName, "ModificaMovimentoGestioneSpesa non reperibile dalla response per la registrazione " + model.getRegistrazioneMovFin().getUid());
			return null;
		}
		for(ModificaMovimentoGestioneSpesa mmgs : impegno.getListaModificheMovimentoGestioneSpesa()) {
			if(mmgs != null && mmgs.getUid() == modificaMovimentoGestioneSpesa.getUid()) {
				return mmgs;
			}
		}
		log.warn(methodName, "Nessuna modifica con uid " + modificaMovimentoGestioneSpesa.getUid() + " reperibile nell'impegno con uid " + impegno.getUid()
				+ " ottenuto per la registrazione " + model.getRegistrazioneMovFin().getUid());
		return null;
	}

	/**
	 * Ottiene l'eventuale subimpegno collegato alla modifica del movimento di gestione di spesa.
	 * 
	 * @param impegno                        l'impegno del servizio di ricerca
	 * @param modificaMovimentoGestioneSpesa la modifica con uid da trovare
	 * @return il subimpegno
	 */
	private SubImpegno findSubImpegno(Impegno impegno, ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa) {
		final String methodName = "findSubImpegno";
		if(impegno == null || impegno.getElencoSubImpegni() == null) {
			log.warn(methodName, "SubImpegno non reperibile dalla response per la registrazione " + model.getRegistrazioneMovFin().getUid());
			return null;
		}
		if(modificaMovimentoGestioneSpesa.getSubImpegno() == null) {
			log.debug(methodName, "Modifica non relativa al subimpegno: non tiro su il dato");
			return null;
		}
		SubImpegno subImpegno = modificaMovimentoGestioneSpesa.getSubImpegno();
		for(SubImpegno si : impegno.getElencoSubImpegni()) {
			if(si != null && si.getUid() == subImpegno.getUid()) {
				return si;
			}
		}
		log.warn(methodName, "Nessuna subimpegno con uid " + subImpegno.getUid() + " reperibile nell'impegno con uid " + impegno.getUid()
				+ " ottenuto per la registrazione " + model.getRegistrazioneMovFin().getUid() + " e compatibile con la modifica " + modificaMovimentoGestioneSpesa.getUid());
		return null;
	}

	/**
	 * Ottiene il soggetto dal movimento.
	 * 
	 * @param modificaMovimentoGestioneSpesa la modifica del movimento di gestione di spesa
	 * @param subImpegno il subimpegno da cui ottenere il soggetto (fallback 1)
	 * @param impegno    l'impegno da cui ottenere il soggetto (fallback 2)
	 * @return il soggetto del movimento
	 */
	private Soggetto ricavaSoggettoDaMovimento(ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa, SubImpegno subImpegno, Impegno impegno) {
		final String methodName = "ricavaSoggettoDaMovimento";
		if(modificaMovimentoGestioneSpesa != null && modificaMovimentoGestioneSpesa.getSoggettoNewMovimentoGestione() != null) {
			log.debug(methodName, "Ottenuto soggetto da modifica: " + modificaMovimentoGestioneSpesa.getSoggettoNewMovimentoGestione().getUid());
			return modificaMovimentoGestioneSpesa.getSoggettoNewMovimentoGestione();
		}
		if (subImpegno != null && subImpegno.getSoggetto() != null) {
			log.debug(methodName, "Fallback 1: ottenuto soggetto da subimpegno: " + subImpegno.getSoggetto().getUid());
			return subImpegno.getSoggetto();
		}
		if(impegno != null && impegno.getSoggetto() != null) {
			log.debug(methodName, "Fallback 2: ottenuto soggetto da impegno: " + impegno.getSoggetto().getUid());
			return impegno.getSoggetto();
		}
		log.info(methodName, "Nessun soggetto ottenuto per la modifica " + (modificaMovimentoGestioneSpesa != null ? modificaMovimentoGestioneSpesa.getUid() : "null"));
		return null;
	}
	
	@Override
	protected void computaStringheDescrizioneDaRegistrazione(RegistrazioneMovFin registrazioneMovFin) {
		StringBuilder movimento = new StringBuilder();
		StringBuilder descrizione = new StringBuilder();
		ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa = (ModificaMovimentoGestioneSpesa) registrazioneMovFin.getMovimento();
		
		// Recuper anno e numero da impegno o subimpegno
		int anno = ottieniAnno(modificaMovimentoGestioneSpesa);
		BigDecimal numero = ottieniNumero(modificaMovimentoGestioneSpesa);
		BigDecimal numeroSub = ottieniNumeroSub(modificaMovimentoGestioneSpesa);
		
		descrizione.append("ModificaMovimentoGestioneSpesa ")
			.append(anno)
			.append("/")
			.append(numero.toPlainString());
		if(numeroSub != null) {
			descrizione.append("-")
				.append(numeroSub.toPlainString());
		}
		if(StringUtils.isNotBlank(modificaMovimentoGestioneSpesa.getDescrizione())) {
			descrizione.append(" ")
				.append(modificaMovimentoGestioneSpesa.getDescrizione());
		}
		movimento.append(anno)
			.append("/")
			.append(numero)
			.append(" (modmovgestspe)");

		model.setDescrMovimentoFinanziario(movimento.toString());
		model.getPrimaNota().setDescrizione(descrizione.toString());
		model.getPrimaNota().setDataRegistrazione(new Date());
	}

	/**
	 * Ottiene l'anno del movimento a partire dalla modifica.
	 *
	 * @param modificaMovimentoGestioneSpesa la modifica da cui ottenere i dati
	 * @return l'anno del movimento
	 * @throws FrontEndUncheckedException nel caso in cui i dati non siano conformi alle specifiche
	 */
	private int ottieniAnno(ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa) {
		if(modificaMovimentoGestioneSpesa.getSubImpegno() != null) {
			return modificaMovimentoGestioneSpesa.getSubImpegno().getAnnoImpegnoPadre();
		} else if (modificaMovimentoGestioneSpesa.getImpegno() != null) {
			return modificaMovimentoGestioneSpesa.getImpegno().getAnnoMovimento();
		}
		throw new FrontEndUncheckedException("Modifica del movimento di gestione senza impegno ne' subimpegno associato");
	}

	/**
	 * Ottiene il numero del movimento a partire dalla modifica.
	 *
	 * @param modificaMovimentoGestioneSpesa la modifica da cui ottenere i dati
	 * @return il numero del movimento
	 * @throws FrontEndUncheckedException nel caso in cui i dati non siano conformi alle specifiche
	 */
	private BigDecimal ottieniNumero(ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa) {
		if(modificaMovimentoGestioneSpesa.getSubImpegno() != null) {
			return modificaMovimentoGestioneSpesa.getSubImpegno().getNumeroImpegnoPadre();
		} else if (modificaMovimentoGestioneSpesa.getImpegno() != null) {
			return modificaMovimentoGestioneSpesa.getImpegno().getNumero();
		}
		throw new FrontEndUncheckedException("Modifica del movimento di gestione senza impegno ne' subimpegno associato");
	}

	/**
	 * Ottiene il numero del submovimento a partire dalla modifica.
	 *
	 * @param modificaMovimentoGestioneSpesa la modifica da cui ottenere i dati
	 * @return il numero del submovimento
	 */
	private BigDecimal ottieniNumeroSub(ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa) {
		if(modificaMovimentoGestioneSpesa.getSubImpegno() != null) {
			return modificaMovimentoGestioneSpesa.getSubImpegno().getNumero();
		}
		return null;
	}

	/**
	 * Validazione per il metodo {@link #completeSalva()}.
	 */
	public void validateCompleteSalva() {
		// Se non ho la prima nota, qualcosa e' andato storto: esco subito
		checkNotNull(model.getPrimaNota(), "Prima Nota Integrata ", true);
		checkNotNullNorInvalidUid(model.getCausaleEP(), "CausaleEP");
		checkNotNull(model.getPrimaNota().getDataRegistrazione(), "Data registrazione ");
		checkNotNullNorEmpty(model.getPrimaNota().getDescrizione(), "Descrizione ");

		List<MovimentoDettaglio> listaMovimentiDettaglioFinal = checkScrittureCorrette();
		
		// Esco se ho errori
		if(hasErrori()) {
			return;
		}
		
		MovimentoEP movEP = new MovimentoEP();
		movEP.setCausaleEP(model.getCausaleEP());
		movEP.setRegistrazioneMovFin(model.getRegistrazioneMovFin());
		model.setListaMovimentoDettaglio(listaMovimentiDettaglioFinal);
		movEP.setListaMovimentoDettaglio(listaMovimentiDettaglioFinal);
		
		model.getListaMovimentoEP().add(movEP);
	}

	/**
	 * Completamento per lo step 2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	
	public String completeSalva() {
		final String methodName = "completeSalva";
		
		aggiornaNumeroRiga();
		// L'inserisci e' sempre presente da registrazione quindi chiamo il completa
		
		// Inserimento della causale
		RegistraPrimaNotaIntegrata req = model.creaRequestRegistraPrimaNotaIntegrata();
		logServiceRequest(req);
		RegistraPrimaNotaIntegrataResponse res = primaNotaService.registraPrimaNotaIntegrata(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'inserimento della Prima Nota integrata");
			addErrori(res);
			return INPUT;
		}
		
		log.info(methodName, "registrata correttamente Prima Nota integrata con uid " + res.getPrimaNota().getUid());
		if (model.isValidazione()) {
			log.info(methodName, "validata correttamente Prima Nota integrata con uid " + res.getPrimaNota().getUid());
		}
		
		// Imposto i dati della causale restituitimi dal servizio
		model.setPrimaNota(res.getPrimaNota());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}

	@Override
	protected ElementoPianoDeiConti ottieniElementoPianoDeiContiDaMovimento() {
		return model.getImpegno() != null
			&& model.getImpegno().getCapitoloUscitaGestione() != null
				? model.getImpegno().getCapitoloUscitaGestione().getElementoPianoDeiConti()
				: null;
	}
	
	@Override
	protected Soggetto ottieniSoggettoDaMovimento() {
		return ricavaSoggettoDaMovimento(model.getModificaMovimentoGestioneSpesa(), model.getSubImpegno(), model.getImpegno());
	}
}

