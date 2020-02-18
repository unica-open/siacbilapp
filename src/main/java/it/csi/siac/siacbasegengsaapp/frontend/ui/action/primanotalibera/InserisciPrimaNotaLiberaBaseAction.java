/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.InserisciPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoScritturaPrimaNotaLibera;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoScritturaPrimaNotaLiberaFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Classe di Action per l'inserimento della prima nota libera (comune tra ambito FIN e GSA)
 *  
 * @author Elisa Chiari
 * @version 1.0.0 - 09/10/2015
 * @param <M> la tipizzazione del model
 */

public abstract class InserisciPrimaNotaLiberaBaseAction<M extends InserisciPrimaNotaLiberaBaseModel> extends BaseInserisciAggiornaPrimaNotaLiberaBaseAction<M> {
	
	
	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 5366464964103251900L;

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
	public String annullaStep1() {
		model.setCausaleEP(null);
		model.setTipoEvento(null);
		model.setPrimaNotaLibera(null);
		
		return SUCCESS;
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
		
		// Inserimento della causale
		InseriscePrimaNota request = model.creaRequestInseriscePrimaNota();
		logServiceRequest(request);
		InseriscePrimaNotaResponse response = primaNotaService.inseriscePrimaNota(request);
		logServiceResponse(response);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'inserimento della Prima Nota Libera");
			addErrori(response);
			return INPUT;
		}
		log.debug(methodName, "Inserita correttamente Prima Nota Libera con uid " + response.getPrimaNota().getUid());
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
		model.setListaPrimeNoteDaCollegare(new ArrayList<PrimaNota>());
		return SUCCESS;
	}
}
