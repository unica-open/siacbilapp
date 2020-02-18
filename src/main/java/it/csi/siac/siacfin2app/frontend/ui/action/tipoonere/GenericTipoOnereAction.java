/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.tipoonere;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.tipoonere.GenericTipoOnereModel;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.TipoOnereService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnereResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770Response;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNaturaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNaturaOnereResponse;
import it.csi.siac.siacfin2ser.model.AttivitaOnere;
import it.csi.siac.siacfin2ser.model.Causale770;
import it.csi.siac.siacfin2ser.model.CodiceSommaNonSoggetta;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfin2ser.model.TipoOnere;

/**
 * Classe di action generica per il TipoOnere.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/11/2014
 * @param <M> la tipizzazione del Model
 *
 */
public class GenericTipoOnereAction<M extends GenericTipoOnereModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8930304202505694078L;

	/** Serviz&icirc; del documento */
	@Autowired protected transient DocumentoService documentoService;
	/** Serviz&icirc; del tipo onere */
	@Autowired protected transient TipoOnereService tipoOnereService;
	@Autowired private transient CodificheService codificheService;
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		/* 
		 * L'esercizio deve essere in una delle seguenti FASI:
		 *     Esercizio Provvisorio
		 *     Gestione
		 *     Assestamento
		 *     Predisposizione Consuntivo
		*/
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioCompatibile = 
				FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio)
				|| FaseBilancio.GESTIONE.equals(faseBilancio)
				|| FaseBilancio.ASSESTAMENTO.equals(faseBilancio)
				|| FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio);
		
		if(!faseDiBilancioCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
	/**
	 * Carica la lista delle NaturaOnere.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaNaturaOnere() throws WebServiceInvocationFailureException {
		final String methodName = "caricaNaturaOnere";
		
		List<NaturaOnere> listaNaturaOnere = sessionHandler.getParametro(BilSessionParameter.LISTA_NATURA_ONERE);
		if(listaNaturaOnere == null) {
			log.debug(methodName, "Lista non in sessione. Caricamento da servizio");
			// Controllo di avere i dati in sessione
			RicercaNaturaOnere request = model.creaRequestRicercaNaturaOnere();
			logServiceRequest(request);
			RicercaNaturaOnereResponse response = documentoService.ricercaNaturaOnere(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
			}
			listaNaturaOnere = response.getElencoNatureOnere();
			log.debug(methodName, "Lista NaturaOnere caricata. Numero dei risultati: " + listaNaturaOnere.size());
			sessionHandler.setParametro(BilSessionParameter.LISTA_NATURA_ONERE, listaNaturaOnere);
		}
		
		model.setListaNaturaOnere(listaNaturaOnere);
	}

	/**
	 * Carica la lista delle Causale770.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaCausale770() throws WebServiceInvocationFailureException {
		final String methodName = "caricaCausale770";
		
		// Controllo di avere i dati in sessione
		List<Causale770> listaCausale770 = sessionHandler.getParametro(BilSessionParameter.LISTA_CAUSALE_770);
		if(listaCausale770 == null) {
			log.debug(methodName, "Lista causale 770 non presente in sessione. Caricamento da servizio");
			RicercaCausale770 request = model.creaRequestRicercaCausale770();
			logServiceRequest(request);
			RicercaCausale770Response response = documentoService.ricercaCausale770(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
			}
			listaCausale770 = response.getElencoCausali();
			sessionHandler.setParametro(BilSessionParameter.LISTA_CAUSALE_770, listaCausale770);
		}
		log.debug(methodName, "Lista Causale770 caricata. Numero dei risultati: " + listaCausale770.size());
		model.setListaCausale770(listaCausale770);
	}
	
	/**
	 * Carica la lista di CodiceSommaNonSoggetta
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaCodiceSommaNonSoggetta() throws WebServiceInvocationFailureException {
		final String methodName = "caricaCodiceSommaNonSoggetta";
		
		// Controllo di avere i dati in sessione
		List<CodiceSommaNonSoggetta> listaCodiceSommaNonSoggetta = sessionHandler.getParametro(BilSessionParameter.LISTA_SOMME_NON_SOGGETTE);
		if(listaCodiceSommaNonSoggetta == null) {
			log.debug(methodName, "Lista CodiceSommaNonSoggetta non presente in sessione. Caricamento da servizio");
			RicercaCodifiche request = model.creaRequestRicercaCodifiche(CodiceSommaNonSoggetta.class);
			logServiceRequest(request);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
			}
			listaCodiceSommaNonSoggetta = response.getCodifiche(CodiceSommaNonSoggetta.class);
			sessionHandler.setParametro(BilSessionParameter.LISTA_SOMME_NON_SOGGETTE, listaCodiceSommaNonSoggetta);
		}
		log.debug(methodName, "Lista CodiceSommaNonSoggetta caricata. Numero dei risultati: " + listaCodiceSommaNonSoggetta.size());
		model.setListaSommeNonSoggette(listaCodiceSommaNonSoggetta);
	}

	/**
	 * Carica la lista delle AttivitaOnere.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaAttivitaOnere() throws WebServiceInvocationFailureException {
		final String methodName = "caricaAttivitaOnere";
		
		// Controllo di avere i dati in sessione
		List<AttivitaOnere> listaAttivitaOnere = sessionHandler.getParametro(BilSessionParameter.LISTA_ATTIVITA_ONERE);
		if(listaAttivitaOnere == null) {
			log.debug(methodName, "Lista attivita onere non presente in sessione. Caricamento da servizio");
			RicercaAttivitaOnere request = model.creaRequestRicercaAttivitaOnere();
			logServiceRequest(request);
			RicercaAttivitaOnereResponse response = documentoService.ricercaAttivitaOnere(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(request, response));
			}
			listaAttivitaOnere = response.getElencoAttivitaOnere();
			sessionHandler.setParametro(BilSessionParameter.LISTA_ATTIVITA_ONERE, listaAttivitaOnere);
		}
		log.debug(methodName, "Lista AttivitaOnere caricata. Numero dei risultati: " + listaAttivitaOnere.size());
		model.setListaAttivitaOnere(listaAttivitaOnere);
	}
	
	/**
	 * Carica la lista dei TipoIvaSplitReverse.
	 */
	protected void caricaListaTipoIvaSplitReverse() {
		model.setListaTipoIvaSplitReverse(Arrays.asList(TipoIvaSplitReverse.values()));
	}
	
	/**
	 * Effettua il controllo di Split / Reverse sul tipo onere.
	 */
	protected void controlloSplitReverse() {
		TipoOnere to = model.getTipoOnere();
		caricaNaturaOnere(to);
		checkCondition(to.getNaturaOnere() == null
				// Se la natura non e' ESENZIONE o SPLIT/REVERSE, non controllo il campo
				|| (!BilConstants.CODICE_NATURA_ONERE_ESENZIONE.getConstant().equals(to.getNaturaOnere().getCodice()) && !BilConstants.CODICE_NATURA_ONERE_SPLIT_REVERSE.getConstant().equals(to.getNaturaOnere().getCodice()))
				|| (BilConstants.CODICE_NATURA_ONERE_ESENZIONE.getConstant().equals(to.getNaturaOnere().getCodice()) && TipoIvaSplitReverse.ESENZIONE.equals(to.getTipoIvaSplitReverse()))
				|| (BilConstants.CODICE_NATURA_ONERE_SPLIT_REVERSE.getConstant().equals(to.getNaturaOnere().getCodice()) && to.getTipoIvaSplitReverse() != null),
			ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Split / Reverse"));
	}

	/**
	 * Caricamento della natura onere dalla lista del servizio
	 * 
	 * @param to il tipo onere la cui natura &eacute; da caricare
	 */
	private void caricaNaturaOnere(TipoOnere to) {
		if(to.getNaturaOnere() == null || to.getNaturaOnere().getUid() == 0) {
			return;
		}
		NaturaOnere naturaOnere = ComparatorUtils.searchByUidEventuallyNull(model.getListaNaturaOnere(), to.getNaturaOnere());
		checkNotNullNorInvalidUid(naturaOnere, "Natura onere", true);
		to.setNaturaOnere(naturaOnere);
	}
}
