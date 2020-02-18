/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.RicercaVariazioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.variazione.ElementoStatoOperativoVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.variazione.ElementoStatoOperativoVariazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.VariazioneDiBilancioService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneBaseResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioneCodifiche;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneDiBilancio;
import it.csi.siac.siacbilser.model.TipoVariazione;
import it.csi.siac.siaccorser.frontend.webservice.ClassificatoreService;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabile;
import it.csi.siac.siaccorser.frontend.webservice.msg.LeggiStrutturaAmminstrativoContabileResponse;
import it.csi.siac.siaccorser.model.Bilancio;
import it.csi.siac.siaccorser.model.Codifica;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Action per la ricerca dello variazione di bilancio.
 * 
 * @author Daniele Argiolas
 * @version 1.0.0 - 28/10/2013
 *
 */

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaVariazioneAction extends GenericBilancioAction<RicercaVariazioneModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -9096744698157930495L;

	@Autowired
	private transient VariazioneDiBilancioService variazioneDiBilancioService;
	@Autowired
	private transient ProvvedimentoService provvedimentoService;
	
	@Autowired private transient ClassificatoreService classificatoreService;
	
	private List<StrutturaAmministrativoContabile> listInSession;

	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		super.prepare();
		
		// SIAC-6884 - è stato appurato che in elenco sono presenti sia settori che
		// direzioni, ma per ogni settore è sicuramente
		// presente anche la sua direzione di riferimento, pertanto è sufficiente
		// filtrare l'elenco per tipo CDC/CDR (settore/direzione)		
		LeggiStrutturaAmminstrativoContabile prova = model.creaRequestLeggiStrutturaAmminstrativoContabile();
		impostaLaResponseInSessione(ottieniResponse(prova));
		
		List<StrutturaAmministrativoContabile> lista =
				sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		
		if (lista != null && !lista.isEmpty()) {
			List<StrutturaAmministrativoContabile> elenco = new ArrayList<StrutturaAmministrativoContabile>();
				// devo filtrare l'elenco per codice
				for (int j = 0; j < lista.size(); j++) {
					if (lista.get(j).getTipoClassificatore() != null
							&& lista.get(j).getTipoClassificatore().getCodice() != null) {
						if (lista.get(j).getTipoClassificatore().getCodice()
								.equals(TipologiaClassificatore.CDR.name())) {
							// si tratta di una Direzione
							elenco.add(lista.get(j));
						}
					}
				}
				
				ComparatorUtils.sortDeepByCodice(elenco); 
				model.setListaDirezioni(elenco);
		}

		log.debug(methodName, "Caricamento della lista dei tipi di atto");

		// Caricamento lista dei tipi di atto
		caricaListeCodifiche();
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return SUCCESS;
	}

	/**
	 * Ricerca di Variazioni di bilancio
	 * 
	 * @return la stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaVariazioni() {
		final String methodName = "ricercaVariazioni";

		// popolaCodificheDaSessione();

		/* tipoVariazione true = codifica; false = bilancio */
		Boolean tipoVariazione = "codifiche".equalsIgnoreCase(model.getTipologiaSceltaVariazione()) ? Boolean.TRUE
				: Boolean.FALSE;
		ServiceRequest request;
		RicercaVariazioneBaseResponse<?> response;

		log.debug(methodName, "Creazione della request");
		/* Se Ricerca Variazioni di Bilancio */
		// se siamo in variazioni di codifiche è TRUE, altrimenti FALSE
		if (Boolean.FALSE.equals(tipoVariazione)) {
			// caso di variazioni di IMPORTI
			RicercaVariazioneBilancio req = model.creaRequestRicercaVariazioneBilancio();
			logServiceRequest(req);
			request = req;

			log.debug(methodName, "Richiamo il WebService di ricerca");
			response = variazioneDiBilancioService.ricercaVariazioneBilancio(req);
		} else {
			// caso di variazioni di CODIFICHE
			RicercaVariazioneCodifiche req = model.creaRequestRicercaVariazioneCodifiche();
			logServiceRequest(req);
			request = req;

			log.debug(methodName, "Richiamo il WebService di ricerca");
			response = variazioneDiBilancioService.ricercaVariazioneCodifiche(req);
		}

		log.debug(methodName, "Richiamato il WebService di ricerca");
		logServiceResponse(response);

		// Controllo gli errori
		if (response.hasErrori()) {
			// si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio");
			addErrori(response);
			return INPUT;
		}

		log.debug(methodName, "Ricerca effettuata con successo");

		int totaleElementi = response.getTotaleElementi();
		if (totaleElementi == 0) {
			log.debug(methodName, "Nessun risultato ottenuto dalla ricerca");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore(""));
			return INPUT;
		}

		log.debug(methodName, "Pulisco la sessione");
		// Ricordare pulire correttamente la sessione
		sessionHandler.cleanAllSafely();

		// Mette in sessione :
		// La lista dei risultati : per poter visualizzare i dati trovati nella action
		// di risultatiRicerca
		// La request usata per chiamare il servizio di ricerca : per poterla riusare
		// allo scopo di reperire un nuovo blocco di risultati
		log.debug(methodName, "Imposto in sessione la request");
		// Workaround per sopperire al fatto che le requests non siano serializzabili
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_VARIAZIONI, request);
		sessionHandler.setParametro(BilSessionParameter.TIPO_VARIAZIONE, tipoVariazione);

		log.debug(methodName, "Imposto in sessione la lista VARIAZIONI");

		List<?> listaRisultati = response.getVariazioniDiBilancio();
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_VARIAZIONI, listaRisultati);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);

		return SUCCESS;
	}

	/**
	 * Validazione per il metodo {@link #ricercaVariazioni()}.
	 */
	public void validateRicercaVariazioni() {
		final String methodName = "validateRicercaVariazioni";
		log.debugStart(methodName, "Chiamata alla validate()");

		boolean formValido = false;

		if (model.getTipologiaSceltaVariazione() == null || model.getTipologiaSceltaVariazione().isEmpty()) {
			// Nessun criterio di ricerca è stato apposto
			addErrore(ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("TipoVariazione"));
		} else if ((model.getNumeroVariazione() != null && model.getNumeroVariazione().intValue() != 0)
				|| (StringUtils.isNotBlank(model.getApplicazioneVariazione())) || (model.getStatoVariazione() != null)
				|| (model.getTipoVariazione() != null) || (model.getDataAperturaProposta()) != null
				|| (model.getDataChiusuraProposta()) != null
				|| (model.getStrutturaAmministrativoContabileDirezioneProponente()) != null
				|| (StringUtils.isNotBlank(model.getDescrizioneVariazione()))
				|| (model.getUidProvvedimento() != null)) {
			log.debug(methodName, "Ricerca variazione valida");
			formValido = true;
		}

		// Controllo della validità del form
		if (!formValido) {
			// Nessun criterio di ricerca è stato apposto
			addErrore(ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore(""));
		}

		log.debugEnd(methodName, "Fine validate() - formValido = " + formValido);
	}

	/**
	 * Metodo di utilit&agrave; per il caricamento delle liste delle codifiche.
	 * 
	 * @throws FrontEndBusinessException
	 */
	private void caricaListeCodifiche() {
		final String methodName = "caricaListeCodifiche";
		log.debug(methodName, "Caricamento della lista dei tipi di atto");

		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);

		if (listaTipoAtto == null) {
			TipiProvvedimento request = model.creaRequestTipiProvvedimento();
			log.debug(methodName, "Richiamo il WebService di caricamento dei Tipi di Provvedimento");
			TipiProvvedimentoResponse response = provvedimentoService.getTipiProvvedimento(request);
			log.debug(methodName, "Richiamato il WebService di caricamento dei Tipi di Provvedimento");
			// Controllo gli errori
			if (response.hasErrori()) {
				// si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del metodo");
				listaTipoAtto = new ArrayList<TipoAtto>();
			} else {
				listaTipoAtto = response.getElencoTipi();
			}
			ComparatorUtils.sortByCodice(listaTipoAtto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, listaTipoAtto);
		}

		model.setListaTipoAtto(listaTipoAtto);

		List<String> listaApplicazione = Arrays.asList("Previsione", "Gestione");
		model.setListaApplicazioneVariazione(listaApplicazione);

		caricaStatoVariazione();
	}

	/**
	 * @throws FrontEndBusinessException
	 * 
	 */
	private void caricaStatoVariazione() {
		List<ElementoStatoOperativoVariazione> instances = new ArrayList<ElementoStatoOperativoVariazione>();
		instances = ElementoStatoOperativoVariazioneFactory.getInstances(model.getEnte().getGestioneLivelli(),
				Arrays.asList(StatoOperativoVariazioneDiBilancio.values()));
		model.setListaStatoVariazione(instances);
	}

	/**
	 * Carica il tipo di variazione.
	 * 
	 * @return SUCCESS
	 */
	public String caricaTipoVariazioni() {
		final String methodName = "caricaTipoVariazioni";
		log.debug(methodName, "Caricamento metodo");

		String tipo = model.getTipologiaSceltaVariazione();

		List<TipoVariazione> listaTipoVariazione = new ArrayList<TipoVariazione>();

		if ("importi".equalsIgnoreCase(tipo)) {
			FaseBilancio faseBilancio = caricaFaseBilancio();
			listaTipoVariazione.addAll(Arrays.asList(TipoVariazione.values()));
			listaTipoVariazione.remove(TipoVariazione.VARIAZIONE_CODIFICA);
			if (FaseBilancio.PREVISIONE.equals(faseBilancio)) {
				listaTipoVariazione.remove(TipoVariazione.VARIAZIONE_PER_ASSESTAMENTO);
			}
		} else {
			listaTipoVariazione.add(TipoVariazione.VARIAZIONE_CODIFICA);
		}

		model.setListaTipoVariazione(listaTipoVariazione);

		return SUCCESS;
	}

	private FaseBilancio caricaFaseBilancio() {
		// Effettuo una ricerca di dettaglio per il bilancio
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		logServiceRequest(request);
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		logServiceResponse(response);

		Bilancio bilancio = response.getBilancio();
		return bilancio.getFaseEStatoAttualeBilancio().getFaseBilancio();
	}
	
	private LeggiStrutturaAmminstrativoContabileResponse ottieniResponseDaServizio(LeggiStrutturaAmminstrativoContabile request) {
		if(listInSession != null) {
			LeggiStrutturaAmminstrativoContabileResponse response = new LeggiStrutturaAmminstrativoContabileResponse();
			response.setListaStrutturaAmmContabile(listInSession);
			return response;
		} 
		
		return classificatoreService.leggiStrutturaAmminstrativoContabile(request);
	}
	
	protected LeggiStrutturaAmminstrativoContabileResponse ottieniResponse(LeggiStrutturaAmminstrativoContabile request) {
		return ottieniResponseDaServizio(request);
	}
	
	protected void impostaLaResponseInSessione(LeggiStrutturaAmminstrativoContabileResponse response) {
		sessionHandler.setParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE,
				listInSession != null ? listInSession : response.getListaStrutturaAmmContabile());
	}
}
