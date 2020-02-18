/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;

/**
 * Classe astratta per la gestione della definizione della variazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 09/01/2014
 * 
 * @param <M> la parametrizzazione del Model 
 *
 */
public abstract class DefinisceVariazioneBaseAction<M extends GenericBilancioModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 7939989984808772427L;
	
	/** Serviz&icirc; del provvedimento */
	@Autowired protected transient ProvvedimentoService provvedimentoService;
	/** Serviz&icirc; della variazione di bilancio */
	@Autowired protected transient VariazioneDiBilancioService variazioneDiBilancioService;
	
	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();
		log.debugEnd(methodName, "");
	}
	
	/**
	 * Controlla la coerenza della variazione con la fase del bilancio.
	 * @param applicazioneVariazione l'applicazione della variazione
	 * 
	 * @return <code>true</code> se la variazione e il bilancio sono coerenti; <code>false</code> altrimenti
	 */
	protected boolean isControllaCoerenzaApplicazioneFase(String applicazioneVariazione) {
		final String methodName = "isControllaCoerenzaApplicazioneFase";
		log.debug(methodName, "Effettuo una ricerca di dettaglio per il bilancio sì da avere i dati necessarii");
		caricaBilancio();
		
		FaseBilancio faseBilancio = model.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		log.debug(methodName, "Fase bilancio: " + faseBilancio);
		log.debug(methodName, "Applicazione variazione: " + applicazioneVariazione);
		
		// SIAC-4637: anche in PREDISPOSIZIONE_CONSUNTIVO
		return
			// Previsione si può fare solo quando il bilancio è in PREVISIONE o in ESERCIZIO_PROVVISORIO
			("PREVISIONE".equalsIgnoreCase(applicazioneVariazione) && 
					(FaseBilancio.PREVISIONE.equals(faseBilancio) ||
						FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio))) ||
				// Gestione si può fare solo quando il bilancio è in ESERCIZIO_PROVVISORIO, GESTIONE o ASSESTAMENTO			
				("GESTIONE".equalsIgnoreCase(applicazioneVariazione) &&
					(FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio) ||
						FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio) ||
						FaseBilancio.GESTIONE.equals(faseBilancio) ||
						FaseBilancio.ASSESTAMENTO.equals(faseBilancio))) ||
						// Assestamento si può fare solo quando il bilancio è in ASSESTAMENTO
				("ASSESTAMENTO".equalsIgnoreCase(applicazioneVariazione) &&
					FaseBilancio.ASSESTAMENTO.equals(faseBilancio));
	}
	
	/**
	 * Metodo per il caricamento dei dati relativi al bilancio.
	 */
	private void caricaBilancio() {
		final String methodName = "caricaBilancio";
		
		// Ricerca di dettaglio
		log.debug(methodName, "Ricerca del dettaglio del Bilancio");
		// Chiamo il servizio
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		logServiceRequest(request);
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		logServiceResponse(response);
		// Imposto il bilancio nel model
		Bilancio bilancio = response.getBilancio();
		model.setBilancio(bilancio);
		log.debug(methodName, "Caricato il dettaglio del bilancio");
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		// SIAC-4637
		boolean faseDiBilancioNonCompatibile = 
				FaseBilancio.CHIUSO.equals(faseBilancio) ||
//				FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio) ||
				FaseBilancio.PLURIENNALE.equals(faseBilancio);
		
		if(faseDiBilancioNonCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
}
