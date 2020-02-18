/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegratamanuale;

import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegratamanuale.ConsultaPrimaNotaIntegrataManualeBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
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
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaResponse;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Classe di Action per i la consultazione della prima nota integrata manuale (comune tra ambito FIN e GSA)
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 * @param <M> la tipizzazione del model
 */
public class ConsultaPrimaNotaIntegrataManualeBaseAction<M extends ConsultaPrimaNotaIntegrataManualeBaseModel> extends GenericBilancioAction<M>{

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 7098826208857238649L;
	
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	@Autowired private transient PrimaNotaService primaNotaService;
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio req = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse res = bilancioService.ricercaDettaglioBilancio(req);
		
		FaseBilancio faseBilancio = res.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		// SIAC-5242: rendere la consultazione utilizzabile quando il bilancio e' in stato CHIUSO
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
				FaseBilancio.PREVISIONE.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// TODO: Davvero utile?
		checkCasoDUsoApplicabile("");
		
		try {
			caricaPrimaNotaDaServizio();
			caricaMovimentoGestione();
		} catch(WebServiceInvocationFailureException wsife) {
			setErroriInSessionePerActionSuccessiva();
			setMessaggiInSessionePerActionSuccessiva();
			setInformazioniInSessionePerActionSuccessiva();
			return INPUT;
		}
		
		calcolaTotali();
		return SUCCESS;
	}

	/**
	 * Caricamento del dettaglio della prima nota dal servizio.
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	private void caricaPrimaNotaDaServizio() throws WebServiceInvocationFailureException {
		final String methodName = "caricaPrimaNotaLiberaDaServizio";
		
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
		
		PrimaNota primaNota = res.getPrimaNota();
		if(primaNota == null) {
			String errorMsg = "Nessuna prima nota corrispondente all'uid " + model.getPrimaNotaLibera().getUid();
			log.info(methodName, errorMsg);
			addErrore(ErroreCore.ENTITA_INESISTENTE.getErrore("Causale", model.getPrimaNotaLibera().getUid()));
			throw new WebServiceInvocationFailureException(errorMsg);
		}
	
		log.debug(methodName, "Trovata primaNota corrispondente all'uid " + primaNota.getUid());
		model.setPrimaNotaLibera(primaNota);
		model.setDataRegistrazioneDefinitivaVisibile(primaNota.getDataRegistrazioneLibroGiornale() != null);
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
			model.setSubAccertamento(sa);
		}
	}

	/**
	 * Caricamento dell'impegno
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	private void caricaImpegno(Impegno impegno) throws WebServiceInvocationFailureException {
		final String methodName = "caricaImpegno";
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
			model.setSubImpegno(si);
		}
	}
	
	/**
	 * Calcolo dei totali dare e avere per la prima nota
	 */
	private void calcolaTotali(){
		PrimaNota p = model.getPrimaNotaLibera();
		for (MovimentoEP mEP :  p.getListaMovimentiEP()){
			for (MovimentoDettaglio movDett : mEP.getListaMovimentoDettaglio()){
				if (OperazioneSegnoConto.DARE.equals(movDett.getSegno())) {
					model.setTotaleDare(model.getTotaleDare().add(movDett.getImporto()));
				} else {
					model.setTotaleAvere(model.getTotaleAvere().add(movDett.getImporto()));
				}
			}
			
		}
		
	}
}
