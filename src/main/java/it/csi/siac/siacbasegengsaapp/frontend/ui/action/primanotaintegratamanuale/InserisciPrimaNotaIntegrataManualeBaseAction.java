/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegratamanuale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.BaseInserisciAggiornaPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegratamanuale.InserisciPrimaNotaIntegrataManualeBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoScritturaPrimaNotaLibera;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoScritturaPrimaNotaLiberaFactory;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.model.StatoOperativoMovimentoGestione;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaIntegrataManuale;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaIntegrataManualeResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe di Action per l'inserimento della prima nota integrata manuale (comune tra ambito FIN e GSA)
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 * @param <M> la tipizzazione del model
 */

public abstract class InserisciPrimaNotaIntegrataManualeBaseAction<M extends InserisciPrimaNotaIntegrataManualeBaseModel> extends BaseInserisciAggiornaPrimaNotaLiberaBaseAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5366464964103251900L;
	/** Chiave per l'accertamento */
	private static final String CHOICE_ACCERTAMENTO = "Accertamento";
	/** Chiave per l'impegno */
	private static final String CHOICE_IMPEGNO = "Impegno";
	
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;

	@Override
	public String execute() throws Exception {
		checkCasoDUsoApplicabile("");
		model.impostaDatiNelModel();
		caricaListe();
		caricaListaClassi();
		caricaListaTitoli();
		return SUCCESS;
	}
	
	@Override
	protected void filtraTipiEventoExtr() throws WebServiceInvocationFailureException {
		List<TipoEvento> listaTipiEvento = model.getListaTipiEvento();
		for(Iterator<TipoEvento> it = listaTipiEvento.iterator(); it.hasNext();) {
			TipoEvento te = it.next();
			if(te == null || !BilConstants.TIPO_EVENTO_EXTR.getConstant().equals(te.getCodice())) {
				it.remove();
			}
		}
		if(listaTipiEvento.isEmpty()) {
			throw new WebServiceInvocationFailureException(ErroreCore.ERRORE_DI_SISTEMA
				.getErrore("Tipo evento " + BilConstants.TIPO_EVENTO_EXTR.getConstant() + " non caricato da servizio")
				.getTesto());
		}
		// Preseleziona il tipo evento
		model.setTipoEvento(listaTipiEvento.get(0));
	}
	
	@Override
	protected void filtraEventoExtr() throws WebServiceInvocationFailureException {
		List<Evento> listaEvento = model.getListaEvento();
		for(Iterator<Evento> it = listaEvento.iterator(); it.hasNext();) {
			Evento ev = it.next();
			if(ev == null || ev.getCodice() == null || !ev.getCodice().startsWith(BilConstants.CODICE_EVENTO_EXTR.getConstant())) {
				it.remove();
			}
		}
		if(listaEvento.isEmpty()) {
			throw new WebServiceInvocationFailureException(ErroreCore.ERRORE_DI_SISTEMA
				.getErrore("Evento " + BilConstants.CODICE_EVENTO_EXTR.getConstant() + " non caricato da servizio")
				.getTesto());
		}
	
		if(!listaEvento.isEmpty()) {
			// Preseleziona l'evento
			model.setEvento(listaEvento.get(0));
		}
	}
	
	@Override
	protected void filtraCausale() throws WebServiceInvocationFailureException {
		List<CausaleEP> listaCausaleEP = model.getListaCausaleEP();
		if(listaCausaleEP.isEmpty()) {
			throw new WebServiceInvocationFailureException(ErroreCore.ERRORE_DI_SISTEMA
					.getErrore("Evento " + BilConstants.CODICE_EVENTO_EXTR.getConstant() + " senza causali collegate")
					.getTesto());
		}
		CausaleEP causaleEP = null;
		for(Iterator<CausaleEP> it = listaCausaleEP.iterator(); it.hasNext() && causaleEP == null;) {
			CausaleEP cep = it.next();
			if(cep != null && BilConstants.CODICE_CAUSALE_EP_EXTR.getConstant().equals(cep.getCodice())) {
				causaleEP = cep;
			}
		}
		if(causaleEP == null) {
			throw new WebServiceInvocationFailureException(ErroreCore.ERRORE_DI_SISTEMA
					.getErrore("Evento " + BilConstants.CODICE_EVENTO_EXTR.getConstant() + " senza causale " + BilConstants.CODICE_CAUSALE_EP_EXTR.getConstant() + " collegata")
					.getTesto());
		}
		model.setCausaleEP(causaleEP);
	}
	
	@Override
	public String annullaStep1() {
		model.setCausaleEP(null);
		model.setTipoEvento(null);
		model.setPrimaNotaLibera(null);
		
		return SUCCESS;
	}
	
	@Override
	public void prepareCompleteStep1() {
		super.prepareCompleteStep1();
		model.setImpegno(null);
		model.setSubImpegno(null);
		model.setAccertamento(null);
		model.setSubAccertamento(null);
		
		model.setTipoMovimento(null);
		model.setAnnoMovimentoGestione(null);
		model.setNumeroMovimentoGestione(null);
		model.setNumeroSubmovimentoGestione(null);
	}
	
	@Override
	public String completeStep1() {
		// Aggiungo i conti
		String result = super.completeStep1();
		if(INPUT.equals(result)) {
			return INPUT;
		}
		
		// Solo al termine dello step 1 devo ricalcolarmi i campi
		if (model.isContiCausale()) {
			model.setListaElementoScrittura(ElementoScritturaPrimaNotaLiberaFactory.creaListaScrittureDaCausaleEP(model.getCausaleEP()));
			model.setListaElementoScritturaDaCausale(ElementoScritturaPrimaNotaLiberaFactory.creaListaScrittureDaCausaleEP(model.getCausaleEP()));
		}
		
		// Copio la lista in modo da avere un opggetto su cui lavorare ce avere la copia in originale
		List<ElementoScritturaPrimaNotaLibera> clone = ReflectionUtil.deepClone(model.getListaElementoScrittura());
		model.setListaElementoScritturaPerElaborazione(clone);
		
		return result;
	}
	
	@Override
	public void validateCompleteStep1() {
		super.validateCompleteStep1();
		// Validazione movimento gestione
		checkNotNullNorEmpty(model.getTipoMovimento(), "Tipo movimento di gestione", true);
		
		checkCondition(CHOICE_ACCERTAMENTO.equals(model.getTipoMovimento()) || CHOICE_IMPEGNO.equals(model.getTipoMovimento()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Tipo movimento di gestione", "deve essere pari a \"" + CHOICE_IMPEGNO + "\" ovvero \"" + CHOICE_ACCERTAMENTO + "\""));
		
		checkNotNull(model.getAnnoMovimentoGestione(), "Anno movimento");
		checkNotNull(model.getNumeroMovimentoGestione(), "Numero movimento");
		
		if(CHOICE_ACCERTAMENTO.equals(model.getTipoMovimento())) {
			// Ricerca e validazione dell'accertamento
			validateAccertamento();
		} else if(CHOICE_IMPEGNO.equals(model.getTipoMovimento())) {
			// Ricerca e validazione dell'impegno
			validateImpegno();
		}
	}
	
	/**
	 * Validazione dell'accertamento: verifica che l'accertamento/sub esista
	 */
	private void validateAccertamento() {
		final String methodName = "validateAccertamento";
		RicercaAccertamentoPerChiaveOttimizzato req = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato();
		RicercaAccertamentoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(req);
		
		if(res.hasErrori()) {
			log.info(methodName, "Errori nella ricerca dell'accertamento");
			addErrori(res);
			return;
		}
		if(res.getAccertamento() == null) {
			log.info(methodName, "Accertamento non presente");
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", model.getAnnoMovimentoGestione() + "/" + model.getNumeroMovimentoGestione()));
			return;
		}
		model.setAccertamento(res.getAccertamento());
		if(model.getNumeroSubmovimentoGestione() != null) {
			// Cerca sub
			SubAccertamento sa = new SubAccertamento();
			sa.setNumero(new BigDecimal(model.getNumeroSubmovimentoGestione().intValue()));
			sa = ComparatorUtils.findByNumeroMovimentoGestione(res.getAccertamento().getElencoSubAccertamenti(), sa);
			
			checkCondition(sa != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("SubAccertamento", model.getAnnoMovimentoGestione() + "/" + model.getNumeroMovimentoGestione() + "-" + model.getNumeroSubmovimentoGestione()));
			checkCondition(sa == null || StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(sa.getStatoOperativoMovimentoGestioneEntrata()), ErroreFin.SUBACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore());
			model.setSubAccertamento(sa);
		} else {
			checkCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(res.getAccertamento().getStatoOperativoMovimentoGestioneEntrata()), ErroreFin.ACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore());
		}
	}

	/**
	 * Validazione dell'impegno: verifica che l'impegno/sub esista
	 */
	private void validateImpegno() {
		final String methodName = "validateImpegno";
		RicercaImpegnoPerChiaveOttimizzato req = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();
		RicercaImpegnoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(req);
		
		if(res.hasErrori()) {
			log.info(methodName, "Errori nella ricerca dell'impegno");
			addErrori(res);
			return;
		}
		if(res.getImpegno() == null) {
			log.info(methodName, "Impegno non presente");
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", model.getAnnoMovimentoGestione() + "/" + model.getNumeroMovimentoGestione()));
			return;
		}
		model.setImpegno(res.getImpegno());
		if(model.getNumeroSubmovimentoGestione() != null) {
			// Cerca sub
			SubImpegno si = new SubImpegno();
			si.setNumero(new BigDecimal(model.getNumeroSubmovimentoGestione().intValue()));
			si = ComparatorUtils.findByNumeroMovimentoGestione(res.getImpegno().getElencoSubImpegni(), si);
			
			checkCondition(si != null, ErroreCore.ENTITA_NON_TROVATA.getErrore("SubImpegno", model.getAnnoMovimentoGestione() + "/" + model.getNumeroMovimentoGestione() + "-" + model.getNumeroSubmovimentoGestione()));
			// SIAC-6225
			checkCondition(si == null 
					|| StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(si.getStatoOperativoMovimentoGestioneSpesa())
					|| StatoOperativoMovimentoGestione.DEFINITIVO_NON_LIQUIDABILE.getCodice().equals(si.getStatoOperativoMovimentoGestioneSpesa()), ErroreFin.MOVIMENTO_GESTIONE_STATO_OPERATIVO_NON_AMMESSO_PER_OPERAZIONE.getErrore());
			model.setSubImpegno(si);
		} else {
			// SIAC-6225
			checkCondition(StatoOperativoMovimentoGestione.DEFINITIVO.getCodice().equals(res.getImpegno().getStatoOperativoMovimentoGestioneSpesa())
					|| StatoOperativoMovimentoGestione.DEFINITIVO_NON_LIQUIDABILE.getCodice().equals(res.getImpegno().getStatoOperativoMovimentoGestioneSpesa()), ErroreFin.MOVIMENTO_GESTIONE_STATO_OPERATIVO_NON_AMMESSO_PER_OPERAZIONE.getErrore());
		}
	}
	
	@Override
	public String annullaStep2() {
		// Pulisco la lista
		model.setListaElementoScrittura(new ArrayList<ElementoScritturaPrimaNotaLibera>());
		
		// Se la causale ha dei conti, li ripristino
		if (model.isContiCausale()) {
			CausaleEP causaleEP = model.getCausaleEP();
			model.setListaElementoScrittura(ElementoScritturaPrimaNotaLiberaFactory.creaListaScrittureDaCausaleEP(causaleEP));
		}
		
		List<ElementoScritturaPrimaNotaLibera> clone = ReflectionUtil.deepClone(model.getListaElementoScrittura());
		model.setListaElementoScritturaPerElaborazione(clone);
		model.setListaPrimeNoteDaCollegare(new ArrayList<PrimaNota>());
		
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep1()}.
	 */
	public void validateCompleteStep2() {
		// Se non ho la prima nota, qualcosa e' andato storto: esco subito
		checkNotNull(model.getPrimaNotaLibera(), "Prima Nota Libera ", true);
		
		List<MovimentoDettaglio> listaMovimentiDettaglioFinal = checkScrittureCorrette();
		
		// Esco se ho errori
		if(hasErrori()) {
			return;
		}
		
		MovimentoEP movEP = new MovimentoEP();
		movEP.setCausaleEP(model.getCausaleEP());
		movEP.setAmbito(model.getAmbito());
		model.setListaMovimentoDettaglio(listaMovimentiDettaglioFinal);
		movEP.setListaMovimentoDettaglio(listaMovimentiDettaglioFinal);
		
		model.getListaMovimentoEP().add(movEP);
	}

	/**
	 * Completamento per lo step 2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@Override
	public String completeStep2() {
		final String methodName = "completeStep2";
		
		aggiornaNumeroRiga();
		// Switch dell'evento
		changeEvento();
		
		// Inserimento della causale
		InseriscePrimaNotaIntegrataManuale req = model.creaRequestInseriscePrimaNotaIntegrataManuale();
		logServiceRequest(req);
		InseriscePrimaNotaIntegrataManualeResponse res = primaNotaService.inseriscePrimaNotaIntegrataManuale(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'inserimento della Prima Nota Integrata Manuale");
			addErrori(res);
			return INPUT;
		}
		log.debug(methodName, "Inserita correttamente Prima Nota Integrata Manuale con uid " + res.getPrimaNota().getUid());
		// Imposto i dati della causale restituitimi dal servizio
		model.setPrimaNotaLibera(res.getPrimaNota());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	
	/**
	 * Cambio dell'evento per avere il codice (e il legame) corretto
	 */
	private void changeEvento() {
		final String methodName = "changeEvento";
		MovimentoGestione mg = model.retrieveMovimentoGestione();
		// Devo calcolare l'evento corretto
		Class<?> clazz = mg.getClass();
		log.debug(methodName, "Evento con codice \"EXTR\" non valido: calcolo dell'evento corrispondente per entita' collegata di tipo " + clazz.getSimpleName());
		String newCode = "EXTR-" + clazz.getSimpleName().replaceAll("[a-z]", "");
		
		Evento evento = ComparatorUtils.findByCodice(model.getListaEvento(), newCode);
		if(evento == null || evento.getCodice() == null) {
			throw new GenericFrontEndMessagesException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Impossibile reperire l'evento con codice " + newCode).getTesto());
		}
		model.setEvento(evento);
	}

	/**
	 * Ritorno allo step 1.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@Override
	public String backToStep1() {
		model.setListaPrimeNoteDaCollegare(new ArrayList<PrimaNota>());
		return SUCCESS;
	}
}
