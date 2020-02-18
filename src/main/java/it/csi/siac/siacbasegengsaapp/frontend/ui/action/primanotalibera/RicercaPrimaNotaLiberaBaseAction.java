/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.RicercaPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.ClassificazioneCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespiteResponse;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespiteResponse;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.PrimaNotaService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaPrimaNotaResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * Classe di action comune FIN-GSA per la ricerca della prima nota libera
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 08/10/2015
 * @param <M> la tipizzazione del model
 */
public abstract class RicercaPrimaNotaLiberaBaseAction<M extends RicercaPrimaNotaLiberaBaseModel> extends GenericBilancioAction<M> {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 1922611835837024577L;

	@Autowired
	private transient CausaleService causaleService;

	@Autowired
	private  ClassificazioneCespiteService classificazioneCespiteService;

	@Autowired
	private transient CodificheService codificheService;
	@Autowired
	private transient PrimaNotaService primaNotaService;
	@Autowired
	private transient ContoService contoService;
	
	@Autowired
	private transient CespiteService cespiteService;

	@Autowired
	private transient ClassificatoreBilService classificatoreBilService;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Caricamento liste
		caricaListe();
	}

	/**
	 * @return il parametro di sessione corrispondente alla request
	 */
	protected abstract BilSessionParameter getBilSessionParameterRequest();

	/**
	 * @return il parametro di sessione corrispondente ai risultati della
	 *         ricerca
	 */
	protected abstract BilSessionParameter getBilSessionParameterRisultati();

	/**
	 * @return il parametro di sessione corrispondente alla lista delle causali (GSA o GEN)
	 */
	protected abstract BilSessionParameter getBilSessionParameterListeCausali();
	
	/**
	 * Caricamento delle liste per l'interazione utente.
	 * 
	 * @throws GenericFrontEndMessagesException in caso di eccezione nel caricamento delle liste
	 */
	protected void caricaListe() {
		try {
			caricaListaCausale();
			caricaListaStatoOperativoPrimaNotaLibera();
			caricaListaEvento();			
			caricaListaClassi();
			caricaListaTitoli();
			caricaListaTipoBene();
			
		} catch (WebServiceInvocationFailureException wsife) {
			throw new GenericFrontEndMessagesException(wsife);
		}
	}

	/**
	 * Caricamento della lista delle classi.
	 */
	protected void caricaListaClassi() {
		RicercaCodifiche reqRC = model.creaRequestRicercaClassi();
		logServiceRequest(reqRC);
		RicercaCodificheResponse resRC = codificheService.ricercaCodifiche(reqRC);
		logServiceResponse(resRC);
		
		if(!resRC.hasErrori()){
			model.setListaClassi(resRC.getCodifiche(ClassePiano.class));
		} else{
			addErrori(resRC);
		}
	}


	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una request per il servizio
	 * di {@link LeggiClassificatoriByTipoElementoBilResponse}.
	 * 
	 * @param codice il codice definente il classificatore
	 * @return la response corrispondente
	 */
	protected LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(String codice) {
		LeggiClassificatoriByTipoElementoBil req = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice);
		logServiceRequest(req);
		LeggiClassificatoriByTipoElementoBilResponse res = classificatoreBilService.leggiClassificatoriByTipoElementoBil(req);
		logServiceResponse(res);
		return res;
	}

	/**
	 * Caricamento della lista dei titoli.
	 */
	protected void caricaListaTitoli() {
		
		LeggiClassificatoriByTipoElementoBilResponse response = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant());
		List<TitoloEntrata> listaTE = response.getClassificatoriTitoloEntrata();
		
		model.setListaTitoloEntrata(listaTE);
		
		LeggiClassificatoriByTipoElementoBilResponse responseSpesa = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
		List<TitoloSpesa> listaTS = responseSpesa.getClassificatoriTitoloSpesa();
		List<Missione> listaMissione = responseSpesa.getClassificatoriMissione();
		model.setListaTitoloSpesa(listaTS); 
		model.setListaMissione(listaMissione);
	}


	/**
	 * Caricamento della lista delle causali.
	 * 
	 * @throws WebServiceInvocationFailureException
	 *             in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaListaCausale() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaCausale";
		List<CausaleEP> listaCausaliEp = sessionHandler.getParametro(getBilSessionParameterListeCausali());
		// Se non ho i dati, effettuo la ricerca

		if (listaCausaliEp == null) {
			// recuperare da servizio la lista causali
			RicercaSinteticaModulareCausale req = model.creaRequestRicercaSinteticaModulareCausale();
			RicercaSinteticaModulareCausaleResponse res = causaleService.ricercaSinteticaModulareCausale(req);

			if (res.hasErrori()) {
				String errorMsg = createErrorInServiceInvocationString(req, res);
				log.warn(methodName, errorMsg);
				addErrori(res);
				throw new WebServiceInvocationFailureException(errorMsg);
			}

			listaCausaliEp = res.getCausali();
			// Aggiungo il risultato in sessione
			sessionHandler.setParametro(getBilSessionParameterListeCausali(), listaCausaliEp);
		}

		model.setListaCausaleEP(listaCausaliEp);

	}

	
	/**
	 * Caricamento della lista del Tipo Bene
	 * 
	 * @throws WebServiceInvocationFailureException
	 *             in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaListaTipoBene() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaTipoBene";
		List<TipoBeneCespite> listaTipoBeneCespite = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_BENE_CESPITE);
		// Se non ho i dati, effettuo la ricerca

		if (listaTipoBeneCespite == null) {
			// recuperare da servizio la lista Tipo Bene Cespite
			RicercaSinteticaTipoBeneCespite         req = model.creaRequestRicercaSinteticaTipoBeneCespite();
			RicercaSinteticaTipoBeneCespiteResponse res = classificazioneCespiteService.ricercaSinteticaTipoBeneCespite(req);

			if (res.hasErrori()) {
				String errorMsg = createErrorInServiceInvocationString(req, res);
				log.warn(methodName, errorMsg);
				addErrori(res);
				throw new WebServiceInvocationFailureException(errorMsg);
			}

			listaTipoBeneCespite = res.getListaTipoBeneCespite();
			// Aggiungo il risultato in sessione
			sessionHandler.setParametro(getBilSessionParameterListeCausali(), listaTipoBeneCespite);
		}

		model.setListaTipoBeneCespite(listaTipoBeneCespite);

	}
	
	
	/**
	 * Caricamento della lista degli stati operativi per la prima nota libera.
	 */
	private void caricaListaStatoOperativoPrimaNotaLibera() {
		model.setListaStatoOperativoPrimaNota(Arrays.asList(StatoOperativoPrimaNota.values()));
	}

	/**
	 * Caricamento della lista degli eventi.
	 * @return la lista degli eventi
	 * 
	 * @throws WebServiceInvocationFailureException
	 *             in caso di fallimento nell'invocazione del servizio
	 */
	protected List<Evento> caricaListaEvento() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaEvento";

		List<Evento> listaEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_EVENTO);
		if (listaEvento == null) {
			log.debug(methodName, "Lista di Evento non presente in sessione. Caricamento da servizio");
			listaEvento = new ArrayList<Evento>();
			RicercaCodifiche req = model.creaRequestRicercaCodifiche(Evento.class);
			logServiceRequest(req);
			RicercaCodificheResponse res = codificheService.ricercaCodifiche(req);
			logServiceResponse(res);

			if (res.hasErrori()) {
				addErrori(res);
				String msgErrore = createErrorInServiceInvocationString(req, res);
				throw new WebServiceInvocationFailureException(msgErrore);
			}

			List<Evento> listaEventoTotale = res.getCodifiche(Evento.class);
			if (listaEventoTotale != null) {
				// verifica tipo causale tipo evento libera
				for (Evento e : listaEventoTotale) {
					if (e != null && e.getTipoEvento() != null && !e.getTipoEvento().getListaTipoCausaleEP().isEmpty()) {
						for (TipoCausale tc : e.getTipoEvento().getListaTipoCausaleEP()) {
							if (TipoCausale.Libera.equals(tc)) {
								listaEvento.add(e);
								break;
							}
						}
					}
				}
			}
		}
		model.setListaEvento(listaEvento);		
		return listaEvento;
	}

	
	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// SIAC-5242: caricamento della fase di bilancio
		caricaFaseBilancio();
		return SUCCESS;
	}

	/**
	 * Caricamento della fase di bilancio
	 */
	private void caricaFaseBilancio() {
		RicercaDettaglioBilancio req = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse res = bilancioService.ricercaDettaglioBilancio(req);
		
		if(res.hasErrori()) {
			throw new GenericFrontEndMessagesException(createErrorInServiceInvocationString(req, res));
		}
		
		FaseBilancio faseBilancio = res.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		sessionHandler.setParametro(BilSessionParameter.FASE_BILANCIO, faseBilancio);
	}

	/**
	 * Controlla la validita del codice conto.
	 * 
	 * @return <code>true</code> se il codice conto &eacute; impostato e
	 *         corretto; <code>false</code> altrimenti
	 */
	private boolean controlloCodiceConto() {
		final String methodName = "controlloCodiceConto";
		if (model.getConto() == null || StringUtils.isBlank(model.getConto().getCodice())) {
			log.debug(methodName, "Nessun conto selezionato");
			return false;
		}

		RicercaSinteticaConto req = model.creaRequestRicercaSinteticaConto(model.getConto());
		logServiceRequest(req);
		RicercaSinteticaContoResponse res = contoService.ricercaSinteticaConto(req);
		logServiceResponse(res);

		if (res.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return false;
		}

		checkCondition(res.getTotaleElementi() > 0,
		ErroreCore.ENTITA_INESISTENTE.getErrore("Conto", model.getConto().getCodice()), true);
		Conto conto = res.getConti().get(0);
		// Imposto il conto nel model
		model.setConto(conto);

		return true;
	}

	private boolean controlloCodiceCespite() {
		final String methodName = "controlloCodiceCespite";
		if (model.getCespite() == null || StringUtils.isBlank(model.getCespite().getCodice())) {
			log.debug(methodName, "Nessun cespite selezionato");
			return false;
		}

		RicercaSinteticaCespite req = model.creaRequestRicercaSinteticaCespite(model.getCespite());
		logServiceRequest(req);
		RicercaSinteticaCespiteResponse res = cespiteService.ricercaSinteticaCespite(req);
		logServiceResponse(res);

		if (res.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return false;
		}

		checkCondition(res.getTotaleElementi() > 0,
		ErroreCore.ENTITA_INESISTENTE.getErrore("cespite", model.getCespite().getCodice()), true);
		Cespite ces = res.getListaCespite().get(0);
		// Imposto il cespite nel model
		model.setCespite(ces);

		return true;
	}
	
	/**
	 * Controlla la validita dele date inserite.
	 * 
	 * @return <code>true</code> se le date sono corrette; <code>false</code>
	 *         altrimenti
	 */
	private boolean controlloDate() {
		if (model.getDataRegistrazioneDA() == null || model.getDataRegistrazioneA() == null) {
			return false;
		}

		checkCondition(!model.getDataRegistrazioneDA().after(model.getDataRegistrazioneA()),
				ErroreCore.DATE_INCONGRUENTI.getErrore("La data di registrazione DA deve essere antecedente alla data di registrazione A"),
				true);

		return true;
	}

	/**
	 * Validazione per il metodo {@link #effettuaRicerca()}.
	 */
	public void validateEffettuaRicerca() {
		checkCondition(model.getPrimaNotaLibera() != null, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore(), true);

		boolean ricercaValida = checkCampoValorizzato(model.getPrimaNotaLibera().getNumero(), "Numero Prima nota")
				|| checkCampoValorizzato(model.getPrimaNotaLibera().getNumeroRegistrazioneLibroGiornale(),"Numero Prima nota")
				|| checkPresenzaIdEntita(model.getEvento()) || checkPresenzaIdEntita(model.getCausaleEP())
				|| checkStringaValorizzata(model.getConto().getCodice(), "Codice conto")
				|| checkCampoValorizzato(model.getPrimaNotaLibera().getStatoOperativoPrimaNota(), "Stato operativo")
				|| checkStringaValorizzata(model.getPrimaNotaLibera().getDescrizione(), "Descrizione Prima nota")
				|| checkCampoValorizzato(model.getDataRegistrazioneDA(), "Data Registrazione Definitiva Prima nota DA")
				|| checkCampoValorizzato(model.getDataRegistrazioneA(), "Data Registrazione Definitiva Prima nota A")
				|| checkCampoValorizzato(model.getDataRegistrazioneProvvisoriaDA(), "Data Registrazione Provvisorio Prima nota DA")
				|| checkCampoValorizzato(model.getDataRegistrazioneProvvisoriaA(), "Data Registrazione Provvisorio Prima nota A")
				|| checkCampoValorizzato(model.getImporto(), "Importo Prima nota")
				|| checkCampoValorizzato(model.getMissione(), "Missione")
				|| checkCampoValorizzato(model.getProgramma(), "Programma")
				|| checkUlterioriCampi();

		ricercaValida = controlloCodiceConto() || ricercaValida;
		
		ricercaValida = controlloCodiceCespite() || ricercaValida;
		
		ricercaValida = controlloDate() || ricercaValida;

		checkCondition(ricercaValida, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
	}

	/**
	 * Controllo su ulteriori campi per la validit&agrave; del form
	 * @return se i campi ulteriori siano validi
	 */
	protected boolean checkUlterioriCampi() {
		// Da implementare nelle sottoclassi se necessario
		return false;
	}

	/**
	 * Ottiene la lista dei conti.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String effettuaRicerca() {
		final String methodName = "effettuaRicerca";
		RicercaSinteticaPrimaNota req = model.creaRequestRicercaSinteticaPrimaNota();
		logServiceRequest(req);

		RicercaSinteticaPrimaNotaResponse res = primaNotaService.ricercaSinteticaPrimaNota(req);
		logServiceResponse(res);
		if (res.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}

		log.debug(methodName, "Numero di risultati trovati: " + res.getTotaleElementi());
		if (res.getTotaleElementi() == 0) {
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}

		// Impostazione dati in sessione
		impostaParametriInSessione(req, res);

		return SUCCESS;

	}

	/**
	 * Impostazione dei dati in sessione
	 * @param req la request
	 * @param res la response
	 */
	private void impostaParametriInSessione(RicercaSinteticaPrimaNota req, RicercaSinteticaPrimaNotaResponse res) {

		BilSessionParameter bilSessionParameterRequest = getBilSessionParameterRequest();
		BilSessionParameter bilSessionParameterRisultati = getBilSessionParameterRisultati();

		sessionHandler.setParametro(bilSessionParameterRisultati, res.getPrimeNote());
		sessionHandler.setParametroXmlType(bilSessionParameterRequest, req);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
	}
	
	
}


