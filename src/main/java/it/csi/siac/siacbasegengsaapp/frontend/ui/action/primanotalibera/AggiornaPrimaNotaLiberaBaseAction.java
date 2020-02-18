/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera;

import java.util.List;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.AggiornaPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoScritturaPrimaNotaLibera;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoScritturaPrimaNotaLiberaFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNotaResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNotaResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Classe di Action per i risultati della ricerca della prima nota libera (comune tra ambito FIN e GSA)
 *  
 * @author Elisa Chiari
 * @version 1.0.0 - 08/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class AggiornaPrimaNotaLiberaBaseAction<M extends AggiornaPrimaNotaLiberaBaseModel> extends BaseInserisciAggiornaPrimaNotaLiberaBaseAction<M> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -343376638117270313L;

	@Override
	public String execute() throws Exception {
		checkCasoDUsoApplicabile("");
		caricaListe();
		caricaListaClassi();
		caricaListaTitoli();
		try {
			caricaPrimaNota();
		} catch(WebServiceInvocationFailureException wsife) {
			// Fallimento nell'invocazione del servizio: esco
			return INPUT;
		}
		
		return SUCCESS;
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
		RicercaDettaglioPrimaNota request = model.creaRequestRicercaDettaglioPrimaNotaLibera();
		logServiceRequest(request);
		RicercaDettaglioPrimaNotaResponse response = primaNotaService.ricercaDettaglioPrimaNota(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMsg = createErrorInServiceInvocationString(request, response);
			log.info(methodName, errorMsg);
			addErrori(response);
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		if(response.getPrimaNota() == null) {
			String errorMsg = "Nessuna causale corrispondente all'uid " + model.getPrimaNotaLibera().getUid();
			log.info(methodName, errorMsg);
			addErrore(ErroreCore.ENTITA_INESISTENTE.getErrore("Causale", model.getPrimaNotaLibera().getUid()));
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		
		// Ho la causale
		PrimaNota primaNotaDaServizio = response.getPrimaNota();
		
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
		movEP.setListaMovimentoDettaglio(model.getListaMovimentoDettaglio());
		
		model.getListaMovimentoEP().add(movEP);
	}
	
	@Override
	public String completeStep2() {
		final String methodName = "completeStep2";
		
		aggiornaNumeroRiga();
		
		// Inserimento della causale
		AggiornaPrimaNota request = model.creaRequestAggiornaPrimaNota();
		logServiceRequest(request);
		AggiornaPrimaNotaResponse response = primaNotaService.aggiornaPrimaNota(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'inserimento della Prima Nota Libera");
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "aggiornata correttamente Prima Nota Libera con uid " + response.getPrimaNota().getUid());
		// Imposto i dati della causale restituitimi dal servizio
		model.setPrimaNotaLibera(response.getPrimaNota());
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
