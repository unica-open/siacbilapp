/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.progetto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.progetto.GenericProgettoModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.ProgettoService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbito;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipiAmbitoResponse;
import it.csi.siac.siacbilser.model.Cronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.DettaglioUscitaCronoprogramma;
import it.csi.siac.siacbilser.model.ModalitaAffidamentoProgetto;
import it.csi.siac.siacbilser.model.Progetto;
import it.csi.siac.siacbilser.model.RiepilogoFondoPluriennaleVincolatoEntrataCronoprogramma;
import it.csi.siac.siacbilser.model.TipoAmbito;
import it.csi.siac.siacbilser.model.TipoProgetto;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;

/**
 * Action astratta per il Progetto.
 * 
 * @author Osorio Alessandra,Nazha Ahmad 
 * @version 1.0.0 - 04/02/2014
 * @version 1.0.1 - 25/03/2015
 * @param <M> la parametrizzazione del Model
 */
public class GenericProgettoAction<M extends GenericProgettoModel> extends GenericBilancioAction<M> {
	/** Per la serializzazione */
	private static final long serialVersionUID = 2251780291217335107L;
	
	/** Serviz&icirc; del progetto */
	@Autowired protected transient ProgettoService progettoService;
	/** Servizio del provvedimento */
	@Autowired protected transient ProvvedimentoService provvedimentoService;
	@Autowired private transient CodificheService codificheService;
	
	
	/**
	 * Carica le liste necessarie per il completamento delle pagine di inserimento e ricerca.
	 * 
	 */
	public void caricaListeCodifiche() {
		final String methodName = "caricaListeCodifiche";
		log.debugStart(methodName, "caricaListaTipiAtto e caricaListaAmbiti");
		caricaListaTipiAtto();
		caricaListaAmbiti();
		log.debugEnd(methodName, "caricaListaTipiAtto e caricaListaAmbiti");
	}
	
	
	/* Response */
	/**
	 * @method 		 RicercaTipiAmbitoResponse
	 * @return		 la response corrispondente
	 */
	protected RicercaTipiAmbitoResponse getListaTipiAmbito() {
		RicercaTipiAmbito request = model.creaRequestRicercaTipiAmbito();
		logServiceRequest(request);
		RicercaTipiAmbitoResponse response = progettoService.ricercaTipiAmbito(request);
		logServiceResponse(response);
		return response;
	}

	
	/* Metodi di utilita' */
	
	/**
	 * Imposta i totali per anno delle spese.
	 * 
	 * @param crono il cronoprogramma da cui ottenere gli importi
	 * @param anno  l'anno di competenza degli importi
	 * 
	 * @return il totale per l'anno
	 */
	public BigDecimal getTotaliPerAnnoSpese(Cronoprogramma crono, Integer anno) {
		BigDecimal importoRet = BigDecimal.ZERO;
		
		for (DettaglioUscitaCronoprogramma temp : crono.getCapitoliUscita()) {
			if(temp.getAnnoCompetenza().equals(anno)) {
				importoRet = importoRet.add(temp.getStanziamento());
			}
		}
		return importoRet;
		
	}
	
	/**
	 * Imposta i totali per anno delle entrate.
	 * 
	 * @param crono il cronoprogramma da cui ottenere gli importi
	 * @param anno  l'anno di competenza degli importi
	 * 
	 * @return il totale per l'anno
	 */
	public BigDecimal getTotaliPerAnnoEntrate(Cronoprogramma crono, Integer anno) {
		BigDecimal importoRet = BigDecimal.ZERO;
		
		for (DettaglioEntrataCronoprogramma temp : crono.getCapitoliEntrata()) {
			if(temp.getAnnoCompetenza().equals(anno)) {
				importoRet = importoRet.add(temp.getStanziamento());
			}
		}
		return importoRet;
		
	}
	
	//SIAC-6255
	/**
	 * Ottiene il tipo progetto dalla fase di bilancio
	 * @return il tipo di progetto
	 */
	protected TipoProgetto obtainTipoProgettoByFaseBilancio() {
		Bilancio bilancio = model.getBilancio();
		FaseBilancio fase  = null;
		if(bilancio == null || bilancio.getFaseEStatoAttualeBilancio() == null || bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio() == null) {
			fase = caricaFaseBilancio();
		} else {
			fase = bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio();
		}
		return FaseBilancio.PREVISIONE.equals(fase) || FaseBilancio.ESERCIZIO_PROVVISORIO.equals(fase)?
				TipoProgetto.PREVISIONE
				: FaseBilancio.GESTIONE.equals(fase)? 
						TipoProgetto.GESTIONE : null;
	}
	
	/**
	 * Caricamento della fase di bilancio
	 * @return la fase di bilancio
	 */
	protected FaseBilancio caricaFaseBilancio() {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		model.setBilancio(response.getBilancio());
		return response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
	}


	/**
	 * Carica la lista dei tipi di atto 
	 * 
	 */
	private void caricaListaTipiAtto() {
		final String methodName = "caricaListaTipiAtto";
		log.debug(methodName, "Caricamento della lista dei tipi di atto");
		

		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		
		if(listaTipoAtto == null) {
			TipiProvvedimento request = model.creaRequestTipiProvvedimento();
			log.debug(methodName, "Richiamo il WebService di caricamento dei Tipi di Provvedimento");
			TipiProvvedimentoResponse response = provvedimentoService.getTipiProvvedimento(request);
			log.debug(methodName, "Richiamato il WebService di caricamento dei Tipi di Provvedimento");
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del metodo");
				listaTipoAtto = new ArrayList<TipoAtto>();
			} else {
				listaTipoAtto = response.getElencoTipi();
				log.info(methodName, "listaTipoAtto.size:"+listaTipoAtto.size());
			}
			ComparatorUtils.sortByCodice(listaTipoAtto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, listaTipoAtto);
		}
		
		model.setListaTipoAtto(listaTipoAtto);
	}
	
	/**
	 * Carica la lista dei tipi di ambito 
	 * 
	 */
	private void caricaListaAmbiti() {
		final String methodName = "caricaListaAmbiti";
		log.debug(methodName, "Caricamento della lista dei tipi di ambito");
		
		List<TipoAmbito> listaTipiAmbito = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_AMBITO);
		
		if (listaTipiAmbito == null) {
			log.debug(methodName, "Caricamento lista tipi di ambito");
			RicercaTipiAmbitoResponse responseAL = getListaTipiAmbito();
			listaTipiAmbito = responseAL.getTipiAmbito();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_AMBITO, listaTipiAmbito);
		}
		model.setListaTipiAmbito(listaTipiAmbito);
	}
	
	/**
	 * Carica la lista dei tipi di atto 
	 * 
	 */
	protected void caricaListaModalitaAffidamento() {
		//SIAC-6255
		final String methodName = "caricaListaModalitaAffidamento";
		log.trace(methodName, "Caricamento della lista dei tipi di atto");
		

		List<ModalitaAffidamentoProgetto> listaModalitaAffidamento = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_AFFIDAMENTO);
		
		if(listaModalitaAffidamento == null) {
			RicercaCodifiche request = model.creaRequestRicercaCodifiche(ModalitaAffidamentoProgetto.class);
			log.debug(methodName, "Richiamo il WebService di caricamento dei Tipi di Provvedimento");
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			log.debug(methodName, "Richiamato il WebService di caricamento dei Tipi di Provvedimento");
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del metodo");
				listaModalitaAffidamento = new ArrayList<ModalitaAffidamentoProgetto>();
			} else {
				listaModalitaAffidamento = response.getCodifiche(ModalitaAffidamentoProgetto.class);
				log.info(methodName, "listaTipoAtto.size:"+listaModalitaAffidamento.size());
			}
			ComparatorUtils.sortByCodice(listaModalitaAffidamento);
			sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_AFFIDAMENTO, listaModalitaAffidamento);
		}
		
		model.setListaModalitaAffidamento(listaModalitaAffidamento);
	}
	
	/**
	 * Effettua una ricerca per ottenere i cronoprogrammi del progetto.
	 * 
	 * @param progetto il progetto
	 * @return una lista di cronoprogrammi
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected List<Cronoprogramma> ricercaCronoprogrammi(Progetto progetto) throws WebServiceInvocationFailureException {
		RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio req = model.creaRequestRicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio(progetto);
		RicercaDeiCronoprogrammiCollegatiAlProgettoPerBilancioResponse res = progettoService.ricercaDeiCronoprogrammiCollegatiAlProgettoPerBilancio(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		return res.getCronoprogrami();
	}

	/**
	 * filtra la lista a partire dall'anno di bilancio
	 * 
	 * @param listaFPVEntrataDaFiltrare la lista da filtrare
	 * @return la lista filtrata
	 */
	protected List<RiepilogoFondoPluriennaleVincolatoEntrataCronoprogramma> filtraListaRiepilogoFPVEntrata(List<RiepilogoFondoPluriennaleVincolatoEntrataCronoprogramma> listaFPVEntrataDaFiltrare) {
		List<RiepilogoFondoPluriennaleVincolatoEntrataCronoprogramma> listaFiltrata = new ArrayList<RiepilogoFondoPluriennaleVincolatoEntrataCronoprogramma>();
		int annoDiBilancio = model.getBilancio().getAnno();
		int annoPrimoFPV = listaFPVEntrataDaFiltrare.get(0).getAnno().intValue();
		if (!listaFPVEntrataDaFiltrare.isEmpty() && listaFPVEntrataDaFiltrare.get(0) != null && listaFPVEntrataDaFiltrare.get(0).getAnno() != null && annoPrimoFPV > annoDiBilancio) {
			int annoIndex = annoDiBilancio;
			while (annoIndex < annoPrimoFPV) {
				RiepilogoFondoPluriennaleVincolatoEntrataCronoprogramma riepilogoFondoPluriennaleVincolatoEntrataCronoprogramma = new RiepilogoFondoPluriennaleVincolatoEntrataCronoprogramma();
				riepilogoFondoPluriennaleVincolatoEntrataCronoprogramma.setImportoEntrata(BigDecimal.ZERO);
				riepilogoFondoPluriennaleVincolatoEntrataCronoprogramma.setAnno(annoIndex);
				riepilogoFondoPluriennaleVincolatoEntrataCronoprogramma.setImportoTitolo1(BigDecimal.ZERO);
				riepilogoFondoPluriennaleVincolatoEntrataCronoprogramma.setImportoTitolo2(BigDecimal.ZERO);
				listaFiltrata.add(riepilogoFondoPluriennaleVincolatoEntrataCronoprogramma);
				annoIndex++;
			}
		}
		
		for(RiepilogoFondoPluriennaleVincolatoEntrataCronoprogramma rfpvec : listaFPVEntrataDaFiltrare) {
			if(rfpvec.getAnno() != null && rfpvec.getAnno().intValue() >= annoDiBilancio) {
				listaFiltrata.add(rfpvec);
			}
		}
		return listaFiltrata;
	}
	
}
