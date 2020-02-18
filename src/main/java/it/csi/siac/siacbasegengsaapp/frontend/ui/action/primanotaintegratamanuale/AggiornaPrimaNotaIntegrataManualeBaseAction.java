/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegratamanuale;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.BaseInserisciAggiornaPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegratamanuale.AggiornaPrimaNotaIntegrataManualeBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoScritturaPrimaNotaLibera;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoScritturaPrimaNotaLiberaFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNotaIntegrataManuale;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNotaIntegrataManualeResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe di Action per l'aggiornamento della prima nota integrata manuale (comune tra ambito FIN e GSA)
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 * @param <M> la tipizzazione del model
 */
public abstract class AggiornaPrimaNotaIntegrataManualeBaseAction<M extends AggiornaPrimaNotaIntegrataManualeBaseModel> extends BaseInserisciAggiornaPrimaNotaLiberaBaseAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -343376638117270313L;
	
	@Autowired private MovimentoGestioneService movimentoGestioneService;

	@Override
	public String execute() throws Exception {
		checkCasoDUsoApplicabile("");
		caricaListe();
		caricaListaClassi();
		caricaListaTitoli();
		try {
			caricaPrimaNota();
			caricaMovimentoGestione();
		} catch(WebServiceInvocationFailureException wsife) {
			// Fallimento nell'invocazione del servizio: esco
			return INPUT;
		}
		
		return SUCCESS;
	}
	
	@Override
	protected void filtraTipiEventoExtr() {
		List<TipoEvento> listaTipiEvento = model.getListaTipiEvento();
		for(Iterator<TipoEvento> it = listaTipiEvento.iterator(); it.hasNext();) {
			TipoEvento te = it.next();
			if(te == null || !BilConstants.TIPO_EVENTO_EXTR.getConstant().equals(te.getCodice())) {
				it.remove();
			}
		}
		if(!listaTipiEvento.isEmpty()) {
			// Preseleziona il tipo evento
			model.setTipoEvento(listaTipiEvento.get(0));
		}
	}
	
	@Override
	protected void filtraEventoExtr() throws WebServiceInvocationFailureException {
		List<Evento> listaEvento = model.getListaEvento();
		for(Iterator<Evento> it = listaEvento.iterator(); it.hasNext();) {
			Evento ev = it.next();
			if (ev == null || ev.getTipoEvento() == null || !BilConstants.TIPO_EVENTO_EXTR.getConstant().equals(ev.getTipoEvento().getCodice())){
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
	protected CausaleEP ottieniCausaleEPDaPrimaNotaOriginale() {
		return getCausaleEPDaPrimaNota(model.getPrimaNotaLiberaOriginale());
	}
	
	@Override
	public String completeStep1() {
		String result = super.completeStep1();
		
		creaListaElementoScritturaDaOriginale();
		return result;
	}
	
	/**
	 * Crea la lista degli elementi di scrittura dalla prima nota originale.
	 */
	private void creaListaElementoScritturaDaOriginale() {
		model.setListaElementoScrittura(ElementoScritturaPrimaNotaLiberaFactory.creaListaScrittureDaPrimaNota(model.getPrimaNotaLiberaOriginale(), model.isContiCausale()));
		
		// Copio la lista in modo da avere un opggetto su cui lavorare ce avere la copia in originale
		List<ElementoScritturaPrimaNotaLibera> clone = ReflectionUtil.deepClone(model.getListaElementoScrittura());
		model.setListaElementoScritturaPerElaborazione(clone);
		
		if(model.getCausaleEP() != null ){
			model.setListaElementoScritturaDaCausale(ElementoScritturaPrimaNotaLiberaFactory.creaListaScrittureDaCausaleEP(model.getCausaleEP()));
		}
	}

	@Override
	public String annullaStep1() {
		// Riprendo l'originale
		PrimaNota primaNotaLiberaOriginale = model.getPrimaNotaLiberaOriginale();
		// Clono per effettuare l'aggiornamento
		PrimaNota primaNotaLibera = ReflectionUtil.deepClone(primaNotaLiberaOriginale);
		// Imposto i dati nel model
		impostaDatiNelModel(primaNotaLiberaOriginale, primaNotaLibera);
		
		return SUCCESS;
	}
	
	
	@Override
	public String annullaStep2() {
		// Ricarico la lista
		creaListaElementoScritturaDaOriginale();
		
		List<PrimaNota> clone = ReflectionUtil.deepClone(model.getPrimaNotaLiberaOriginale().getListaPrimaNotaFiglia());
		model.setListaPrimeNoteDaCollegare(clone);
		
		return SUCCESS;
	}
	
	/**
	 * Caricamento della causale EP.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaPrimaNota() throws WebServiceInvocationFailureException {
		final String methodName = "caricaPrimaNota";
		
		log.debug(methodName, "Caricamento della causale");
		RicercaDettaglioPrimaNota req = model.creaRequestRicercaDettaglioPrimaNotaLibera();
		logServiceRequest(req);
		RicercaDettaglioPrimaNotaResponse res = primaNotaService.ricercaDettaglioPrimaNota(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMsg = createErrorInServiceInvocationString(req, res);
			log.info(methodName, errorMsg);
			addErrori(res);
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		if(res.getPrimaNota() == null) {
			String errorMsg = "Nessuna causale corrispondente all'uid " + model.getPrimaNotaLibera().getUid();
			log.info(methodName, errorMsg);
			addErrore(ErroreCore.ENTITA_INESISTENTE.getErrore("Causale", model.getPrimaNotaLibera().getUid()));
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		
		// Ho la causale
		PrimaNota primaNotaDaServizio = res.getPrimaNota();
		
		// Clono per effettuare l'aggiornamento
		PrimaNota primaNota = ReflectionUtil.deepClone(primaNotaDaServizio);
		// Imposto i dati nel model
		impostaDatiNelModel(primaNotaDaServizio, primaNota);
	}
	
	/**
	 * Impostazione dei dati nel model.
	 * 
	 * @param primaNotaDaServizio la causale originale
	 * @param primaNota           la causale da aggiornare
	 */
	private void impostaDatiNelModel(PrimaNota primaNotaDaServizio, PrimaNota primaNota) {
		model.setPrimaNotaLiberaOriginale(primaNotaDaServizio);
		model.setPrimaNotaLibera(primaNota);
		CausaleEP causaleEPDAPNL = getCausaleEPDaPrimaNota(primaNota);
		
		// Ho tutti i dati nella lista inutile ricercare il dettaglio x avere i conti
		CausaleEP causaleEP = ComparatorUtils.searchByUid(model.getListaCausaleEP(), causaleEPDAPNL);
		
		model.setCausaleEP(causaleEP);
		impostaEvento(causaleEP, causaleEPDAPNL);
		
		model.setListaPrimeNoteDaCollegare(primaNota.getListaPrimaNotaFiglia());
	}
	

	/**
	 * Caricamento del movimento di gestione
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	private void caricaMovimentoGestione() throws WebServiceInvocationFailureException {
		PrimaNota primaNota = model.getPrimaNotaLibera();
		if(primaNota.getListaMovimentiEP() == null || primaNota.getListaMovimentiEP().isEmpty()) {
			Errore errore = ErroreCore.ERRORE_DI_SISTEMA.getErrore("La prima nota " + primaNota.getNumero() + " non presenta movimento EP");
			addErrore(errore);
			throw new WebServiceInvocationFailureException(errore.getTesto());
		}
		MovimentoEP movimentoEP = primaNota.getListaMovimentiEP().get(0);
		if(movimentoEP == null || movimentoEP.getRegistrazioneMovFin() == null) {
			Errore errore = ErroreCore.ERRORE_DI_SISTEMA.getErrore("La prima nota " + primaNota.getNumero() + " non presenta registrazione");
			addErrore(errore);
			throw new WebServiceInvocationFailureException(errore.getTesto());
		}
		Entita entita = movimentoEP.getRegistrazioneMovFin().getMovimento();
		if(entita instanceof Impegno) {
			caricaImpegno((Impegno) entita);
			return;
		}
		if(entita instanceof Accertamento) {
			caricaAccertamento((Accertamento)entita);
			return;
		}
		Errore errore = ErroreCore.ERRORE_DI_SISTEMA.getErrore("La prima nota " + primaNota.getNumero() + " è collegata a un movimento di tipo "
				+ entita.getClass().getSimpleName() + " che non risulta gestibile con le prime note integrate manuali");
		addErrore(errore);
		throw new WebServiceInvocationFailureException(errore.getTesto());
	}
	
	/**
	 * Caricamento dell'accertamento
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	private void caricaAccertamento(Accertamento accertamento) throws WebServiceInvocationFailureException {
		final String methodName = "caricaAccertamento";
		model.setTipoMovimento("Accertamento");
		RicercaAccertamentoPerChiaveOttimizzato req = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato(accertamento);
		RicercaAccertamentoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(req);
		
		if(res.hasErrori()) {
			log.info(methodName, "Errori nella ricerca dell'accertamento");
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		if(res.getAccertamento() == null) {
			log.info(methodName, "Accertamento non presente");
			Errore errore = ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", req.getpRicercaAccertamentoK().getAnnoAccertamento()
					+ "/" + req.getpRicercaAccertamentoK().getNumeroAccertamento().toPlainString());
			addErrore(errore);
			throw new WebServiceInvocationFailureException(errore.getTesto());
		}
		model.setAccertamento(res.getAccertamento());
		model.setAnnoMovimentoGestione(Integer.valueOf(res.getAccertamento().getAnnoMovimento()));
		model.setNumeroMovimentoGestione(Integer.valueOf(res.getAccertamento().getNumero().intValue()));
		if(accertamento instanceof SubAccertamento) {
			// Cerca sub
			SubAccertamento sa = ComparatorUtils.findByNumeroMovimentoGestione(res.getAccertamento().getElencoSubAccertamenti(), (SubAccertamento) accertamento);
			
			if(sa == null) {
				log.info(methodName, "SubAccertamento non presente");
				Errore errore = ErroreCore.ENTITA_NON_TROVATA.getErrore("SubAccertamento", req.getpRicercaAccertamentoK().getAnnoAccertamento()
						+ "/" + req.getpRicercaAccertamentoK().getNumeroAccertamento().toPlainString()
						+ "-" + req.getpRicercaAccertamentoK().getNumeroSubDaCercare().toPlainString());
				addErrore(errore);
				throw new WebServiceInvocationFailureException(errore.getTesto());
			}
			model.setNumeroSubmovimentoGestione(Integer.valueOf(sa.getNumero().intValue()));
			model.setSubAccertamento(sa);
		}
	}

	/**
	 * Caricamento dell'impegno
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	private void caricaImpegno(Impegno impegno) throws WebServiceInvocationFailureException {
		final String methodName = "caricaImpegno";
		model.setTipoMovimento("Impegno");
		RicercaImpegnoPerChiaveOttimizzato req = model.creaRequestRicercaImpegnoPerChiaveOttimizzato(impegno);
		RicercaImpegnoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(req);
		
		if(res.hasErrori()) {
			log.info(methodName, "Errori nella ricerca dell'impegno");
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		if(res.getImpegno() == null) {
			log.info(methodName, "Impegno non presente");
			Errore errore = ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", req.getpRicercaImpegnoK().getAnnoImpegno()
					+ "/" + req.getpRicercaImpegnoK().getNumeroImpegno().toPlainString());
			addErrore(errore);
			throw new WebServiceInvocationFailureException(errore.getTesto());
		}
		model.setImpegno(res.getImpegno());
		model.setAnnoMovimentoGestione(Integer.valueOf(res.getImpegno().getAnnoMovimento()));
		model.setNumeroMovimentoGestione(Integer.valueOf(res.getImpegno().getNumero().intValue()));
		if(impegno instanceof SubImpegno) {
			// Cerca sub
			SubImpegno si = ComparatorUtils.findByNumeroMovimentoGestione(res.getImpegno().getElencoSubImpegni(), (SubImpegno) impegno);
			
			if(si == null) {
				log.info(methodName, "SubImpegno non presente");
				Errore errore = ErroreCore.ENTITA_NON_TROVATA.getErrore("SubImpegno", req.getpRicercaImpegnoK().getAnnoImpegno()
						+ "/" + req.getpRicercaImpegnoK().getNumeroImpegno().toPlainString()
						+ "-" + req.getpRicercaImpegnoK().getNumeroSubDaCercare().toPlainString());
				addErrore(errore);
				throw new WebServiceInvocationFailureException(errore.getTesto());
			}
			model.setNumeroSubmovimentoGestione(Integer.valueOf(si.getNumero().intValue()));
			model.setSubImpegno(si);
		}
	}

	/**
	 * Impostazione dell'evento nel model, utilizzando la causale EP
	 * @param causaleEP la causale da utilizzare
	 * @param causaleEPDAPNL la causale della prima nota, come fallback
	 */
	private void impostaEvento(CausaleEP causaleEP, CausaleEP causaleEPDAPNL) {
		if (causaleEP != null && causaleEP.getEventi().size() == 1) {
			model.setEvento(causaleEP.getEventi().get(0));
		} else if(causaleEPDAPNL != null && causaleEPDAPNL.getEventi().size() == 1) {
			model.setEvento(causaleEPDAPNL.getEventi().get(0));
		}
	}

	/**
	 * Validazione per il metodo {@link #completeStep2()}.
	 */
	public void validateCompleteStep2() {
		// Se non ho la prima nota, qualcosa e' andato storto: esco subito
		checkNotNull(model.getPrimaNotaLibera(), "Prima Nota Libera ", true);
		List<MovimentoDettaglio> listaMovimentiDettaglioFinal = checkScrittureCorrette();
		
		if(hasErrori()) {
			return;
		}
		
		MovimentoEP movEP = new MovimentoEP();
		movEP.setCausaleEP(model.getCausaleEP());
		model.setListaMovimentoDettaglio(listaMovimentiDettaglioFinal);
		movEP.setListaMovimentoDettaglio(listaMovimentiDettaglioFinal);
		
		model.getListaMovimentoEP().add(movEP);
	}
	
	@Override
	public String completeStep2() {
		final String methodName = "completeStep2";
		
		aggiornaNumeroRiga();
		
		// Inserimento della causale
		AggiornaPrimaNotaIntegrataManuale req = model.creaRequestAggiornaPrimaNotaIntegrataManuale();
		logServiceRequest(req);
		AggiornaPrimaNotaIntegrataManualeResponse res = primaNotaService.aggiornaPrimaNotaIntegrataManuale(req);
		logServiceResponse(res);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'inserimento della Prima Nota Libera");
			addErrori(res);
			return INPUT;
		}
		log.debug(methodName, "aggiornata correttamente Prima Nota Libera con uid " + res.getPrimaNota().getUid());
		// Imposto i dati della causale restituitimi dal servizio
		model.setPrimaNotaLibera(res.getPrimaNota());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		return SUCCESS;
	}
	
	/**
	 * Ritorno allo step 1.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@Override
	public String backToStep1() {
		List<PrimaNota> clone = ReflectionUtil.deepClone(model.getPrimaNotaLiberaOriginale().getListaPrimaNotaFiglia());
		model.setListaPrimeNoteDaCollegare(clone);
		return SUCCESS;
	}
}
