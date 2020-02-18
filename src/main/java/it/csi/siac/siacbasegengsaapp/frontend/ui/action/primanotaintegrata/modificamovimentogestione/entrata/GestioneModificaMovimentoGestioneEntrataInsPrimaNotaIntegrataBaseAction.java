/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.modificamovimentogestione.entrata;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.BaseInserisciAggiornaPrimaNotaIntegrataBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.modificamovimentogestione.entrata.GestioneModificaMovimentoGestioneEntrataPrimaNotaIntegrataBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataHelper;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccommonapp.util.exception.FrontEndUncheckedException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.RegistraPrimaNotaIntegrataResponse;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;

/**
 * Classe base di action per l'inserimento della prima integrata, sezione dei movimenti dettaglio, modifica del movimento gestione di entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/11/2015
 * @param <M> la tipizzazione del model
 */
public abstract class GestioneModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataBaseAction<M extends GestioneModificaMovimentoGestioneEntrataPrimaNotaIntegrataBaseModel>
		extends BaseInserisciAggiornaPrimaNotaIntegrataBaseAction<Accertamento, ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataHelper, M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8985890954785351729L;

	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	
	/** Nome del model del completamento e validazione dell'accertamento. Modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_MODIFICA_MOVIMENTO_GESTIONE_ENTRATA_FIN = "CompletaValidaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento e validazione dell'accertamento. Modulo GSA */
	public static final String MODEL_SESSION_NAME_COMPLETA_VALIDA_INS_MODIFICA_MOVIMENTO_GESTIONE_ENTRATA_GSA = "CompletaValidaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataGSAModel";
	/** Nome del model del completamento dell'accertamento, modulo GEN */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_MODIFICA_MOVIMENTO_GESTIONE_ENTRATA_FIN = "CompletaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataFINModel";
	/** Nome del model del completamento dell'accertamento, modulo GSA */
	public static final String MODEL_SESSION_NAME_COMPLETA_INS_MODIFICA_MOVIMENTO_GESTIONE_ENTRATA_GSA = "CompletaModificaMovimentoGestioneEntrataInsPrimaNotaIntegrataGSAModel";
	
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
		
		sessionHandler.setParametro(BilSessionParameter.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA, null);
		
		ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = (ModificaMovimentoGestioneEntrata) model.getRegistrazioneMovFin().getMovimento();
		model.setModificaMovimentoGestioneEntrata(modificaMovimentoGestioneEntrata);
		
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
		
		getDatiDaModificaMovimentoGestioneEntrata(modificaMovimentoGestioneEntrata);
		Soggetto soggetto = ricavaSoggettoDaMovimento(modificaMovimentoGestioneEntrata, model.getSubAccertamento(), model.getAccertamento());
		model.setSoggettoMovimentoFIN(soggetto);
		return SUCCESS;
	}

	/**
	 * Ottiene i dati della modifica del movimento di gestione di entrata
	 * @param modificaMovimentoGestioneEntrata il subimpegno da cui ottenere i dati
	 */
	private void getDatiDaModificaMovimentoGestioneEntrata(ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata){

		// Carico da servizio
		RicercaAccertamentoPerChiaveOttimizzato req = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato(modificaMovimentoGestioneEntrata);
		logServiceRequest(req);
		
		RicercaAccertamentoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(req);

		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			return;
		}
		
		Accertamento accertamentoServizio = res.getAccertamento();
		SubAccertamento subAccertamentoServizio = findSubAccertamento(res.getAccertamento(), modificaMovimentoGestioneEntrata);
		ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrataServizio = findModificaMovimentoGestioneEntrata(subAccertamentoServizio != null ? subAccertamentoServizio : accertamentoServizio, modificaMovimentoGestioneEntrata);
		
		sessionHandler.setParametro(BilSessionParameter.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA, modificaMovimentoGestioneEntrataServizio);
		sessionHandler.setParametro(BilSessionParameter.ACCERTAMENTO, accertamentoServizio);
		sessionHandler.setParametro(BilSessionParameter.SUBACCERTAMENTO, subAccertamentoServizio);
		
		model.setModificaMovimentoGestioneEntrata(modificaMovimentoGestioneEntrataServizio);
		model.setAccertamento(accertamentoServizio);
		model.setSubAccertamento(subAccertamentoServizio);
		model.setClassificatoreGerarchico(model.getAccertamento().getCapitoloEntrataGestione().getCategoriaTipologiaTitolo());
		
		// SIAC-5294
		model.setConsultazioneHelper(new ConsultaRegistrazioneMovFinModificaMovimentoGestioneEntrataHelper(accertamentoServizio, subAccertamentoServizio, modificaMovimentoGestioneEntrataServizio, model.isGestioneUEB()));
	}
	
	/**
	 * Trova la modifica dall'impegno restituito dal servizio di ricerca.
	 * 
	 * @param impegno                        l'impegno del servizio di ricerca
	 * @param modificaMovimentoGestioneEntrata la modifica con uid da trovare
	 * @return la modifica
	 */
	private ModificaMovimentoGestioneEntrata findModificaMovimentoGestioneEntrata(Accertamento impegno, ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata) {
		final String methodName = "findModificaMovimentoGestioneEntrata";
		if(impegno == null || impegno.getListaModificheMovimentoGestioneEntrata() == null) {
			log.warn(methodName, "ModificaMovimentoGestioneEntrata non reperibile dalla response per la registrazione " + model.getRegistrazioneMovFin().getUid());
			return null;
		}
		for(ModificaMovimentoGestioneEntrata mmgs : impegno.getListaModificheMovimentoGestioneEntrata()) {
			if(mmgs != null && mmgs.getUid() == modificaMovimentoGestioneEntrata.getUid()) {
				return mmgs;
			}
		}
		log.warn(methodName, "Nessuna modifica con uid " + modificaMovimentoGestioneEntrata.getUid() + " reperibile nell'impegno con uid " + impegno.getUid()
				+ " ottenuto per la registrazione " + model.getRegistrazioneMovFin().getUid());
		return null;
	}

	/**
	 * Ottiene l'eventuale subimpegno collegato alla modifica del movimento di gestione di entrata.
	 * 
	 * @param impegno                        l'impegno del servizio di ricerca
	 * @param modificaMovimentoGestioneEntrata la modifica con uid da trovare
	 * @return il subimpegno
	 */
	private SubAccertamento findSubAccertamento(Accertamento impegno, ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata) {
		final String methodName = "findSubAccertamento";
		if(impegno == null || impegno.getElencoSubAccertamenti() == null) {
			log.warn(methodName, "SubAccertamento non reperibile dalla response per la registrazione " + model.getRegistrazioneMovFin().getUid());
			return null;
		}
		if(modificaMovimentoGestioneEntrata.getSubAccertamento() == null) {
			log.debug(methodName, "Modifica non relativa al subimpegno: non tiro su il dato");
			return null;
		}
		SubAccertamento subAccertamento = modificaMovimentoGestioneEntrata.getSubAccertamento();
		for(SubAccertamento sa : impegno.getElencoSubAccertamenti()) {
			if(sa != null && sa.getUid() == subAccertamento.getUid()) {
				return sa;
			}
		}
		log.warn(methodName, "Nessuna subimpegno con uid " + subAccertamento.getUid() + " reperibile nell'impegno con uid " + impegno.getUid()
				+ " ottenuto per la registrazione " + model.getRegistrazioneMovFin().getUid() + " e compatibile con la modifica " + modificaMovimentoGestioneEntrata.getUid());
		return null;
	}

	/**
	 * Ottiene il soggetto dal movimento.
	 * 
	 * @param modificaMovimentoGestioneEntrata la modifica del movimento di gestione di entrata
	 * @param subAccertamento il subimpegno da cui ottenere il soggetto (fallback 1)
	 * @param impegno    l'impegno da cui ottenere il soggetto (fallback 2)
	 * @return il soggetto del movimento
	 */
	private Soggetto ricavaSoggettoDaMovimento(ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata, SubAccertamento subAccertamento, Accertamento impegno) {
		final String methodName = "ricavaSoggettoDaMovimento";
		if(modificaMovimentoGestioneEntrata != null && modificaMovimentoGestioneEntrata.getSoggettoNewMovimentoGestione() != null) {
			log.debug(methodName, "Ottenuto soggetto da modifica: " + modificaMovimentoGestioneEntrata.getSoggettoNewMovimentoGestione().getUid());
			return modificaMovimentoGestioneEntrata.getSoggettoNewMovimentoGestione();
		}
		if (subAccertamento != null && subAccertamento.getSoggetto() != null) {
			log.debug(methodName, "Fallback 1: ottenuto soggetto da subimpegno: " + subAccertamento.getSoggetto().getUid());
			return subAccertamento.getSoggetto();
		}
		if(impegno != null && impegno.getSoggetto() != null) {
			log.debug(methodName, "Fallback 2: ottenuto soggetto da impegno: " + impegno.getSoggetto().getUid());
			return impegno.getSoggetto();
		}
		log.info(methodName, "Nessun soggetto ottenuto per la modifica " + (modificaMovimentoGestioneEntrata != null ? modificaMovimentoGestioneEntrata.getUid() : "null"));
		return null;
	}
	
	@Override
	protected void computaStringheDescrizioneDaRegistrazione(RegistrazioneMovFin registrazioneMovFin) {
		StringBuilder movimento = new StringBuilder();
		StringBuilder descrizione = new StringBuilder();
		ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata = (ModificaMovimentoGestioneEntrata) registrazioneMovFin.getMovimento();
		
		// Recuper anno e numero da impegno o subimpegno
		int anno = ottieniAnno(modificaMovimentoGestioneEntrata);
		BigDecimal numero = ottieniNumero(modificaMovimentoGestioneEntrata);
		BigDecimal numeroSub = ottieniNumeroSub(modificaMovimentoGestioneEntrata);
		
		descrizione.append("ModificaMovimentoGestioneEntrata ")
			.append(anno)
			.append("/")
			.append(numero.toPlainString());
		if(numeroSub != null) {
			descrizione.append("-")
				.append(numeroSub.toPlainString());
		}
		if(StringUtils.isNotBlank(modificaMovimentoGestioneEntrata.getDescrizione())) {
			descrizione.append(" ")
				.append(modificaMovimentoGestioneEntrata.getDescrizione());
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
	 * @param modificaMovimentoGestioneEntrata la modifica da cui ottenere i dati
	 * @return l'anno del movimento
	 * @throws FrontEndUncheckedException nel caso in cui i dati non siano conformi alle specifiche
	 */
	private int ottieniAnno(ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata) {
		if(modificaMovimentoGestioneEntrata.getSubAccertamento() != null) {
			return modificaMovimentoGestioneEntrata.getSubAccertamento().getAnnoAccertamentoPadre();
		} else if (modificaMovimentoGestioneEntrata.getAccertamento() != null) {
			return modificaMovimentoGestioneEntrata.getAccertamento().getAnnoMovimento();
		}
		throw new FrontEndUncheckedException("Modifica del movimento di gestione senza impegno ne' subimpegno associato");
	}

	/**
	 * Ottiene il numero del movimento a partire dalla modifica.
	 *
	 * @param modificaMovimentoGestioneEntrata la modifica da cui ottenere i dati
	 * @return il numero del movimento
	 * @throws FrontEndUncheckedException nel caso in cui i dati non siano conformi alle specifiche
	 */
	private BigDecimal ottieniNumero(ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata) {
		if(modificaMovimentoGestioneEntrata.getSubAccertamento() != null) {
			return modificaMovimentoGestioneEntrata.getSubAccertamento().getNumeroAccertamentoPadre();
		} else if (modificaMovimentoGestioneEntrata.getAccertamento() != null) {
			return modificaMovimentoGestioneEntrata.getAccertamento().getNumero();
		}
		throw new FrontEndUncheckedException("Modifica del movimento di gestione senza impegno ne' subimpegno associato");
	}

	/**
	 * Ottiene il numero del submovimento a partire dalla modifica.
	 *
	 * @param modificaMovimentoGestioneEntrata la modifica da cui ottenere i dati
	 * @return il numero del submovimento
	 */
	private BigDecimal ottieniNumeroSub(ModificaMovimentoGestioneEntrata modificaMovimentoGestioneEntrata) {
		if(modificaMovimentoGestioneEntrata.getSubAccertamento() != null) {
			return modificaMovimentoGestioneEntrata.getSubAccertamento().getNumero();
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
		return model.getAccertamento() != null
			&& model.getAccertamento().getCapitoloEntrataGestione() != null
				? model.getAccertamento().getCapitoloEntrataGestione().getElementoPianoDeiConti()
				: null;
	}
	
	@Override
	protected Soggetto ottieniSoggettoDaMovimento() {
		return ricavaSoggettoDaMovimento(model.getModificaMovimentoGestioneEntrata(), model.getSubAccertamento(), model.getAccertamento());
	}
}

