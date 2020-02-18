/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.registroa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnnoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaPuntualeCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccespapp.frontend.ui.action.registroa.utils.RicercaRegistroACespiteListLoader;
import it.csi.siac.siaccespapp.frontend.ui.model.registroa.RicercaRegistroACespiteModel;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaRegistroACespiteResponse;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoResponse;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Action per la ricerca del registro A(prime note verso inventario contabile)
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/10/2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaRegistroACespiteAction extends GenericBilancioAction<RicercaRegistroACespiteModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2317567947755165211L;
	
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	@Autowired private transient CausaleService causaleService;
	@Autowired private transient CespiteService cespiteService;
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	@Autowired private transient CodificheService codificheService;
	@Autowired private transient ContoService contoService;
	@Autowired private transient MovimentoGestioneService movimentoGestioneService;
	@Autowired private transient ProvvedimentoService provvedimentoService;
	@Autowired private transient SoggettoService soggettoService;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Caricamento liste
		try {
			RicercaRegistroACespiteListLoader ll = new RicercaRegistroACespiteListLoader(causaleService, codificheService, classificatoreBilService, provvedimentoService, soggettoService, sessionHandler, this);
			caricaListePrimaNotaLibera(ll);
			caricaListePrimaNotaIntegrata(ll);
			caricaFaseBilancio();
		} catch (WebServiceInvocationFailureException wsife) {
			throw new GenericFrontEndMessagesException(wsife.getMessage(), wsife);
		}
	}
	
	/**
	 * Caricamento della fase di bilancio
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaFaseBilancio() throws WebServiceInvocationFailureException {
		FaseBilancio faseBilancio = sessionHandler.getParametro(BilSessionParameter.FASE_BILANCIO);
		if(faseBilancio != null) {
			return;
		}
		
		RicercaDettaglioBilancio req = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse res = bilancioService.ricercaDettaglioBilancio(req);
		
		if(res.hasErrori()) {
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
		}
		
		faseBilancio = res.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		// Imposto la fase in sessione per i risultati della ricerca, ma non effettuo caching su di esso
		sessionHandler.setParametro(BilSessionParameter.FASE_BILANCIO, faseBilancio);
	}
	
	/**
	 * Caricamento delle liste della prima nota libera
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nel caricamento delle liste
	 */
	private void caricaListePrimaNotaLibera(RicercaRegistroACespiteListLoader ll) throws WebServiceInvocationFailureException {
		ll.caricaListaTipoCausale();
		ll.caricaListaCausaleLibera();		
		ll.caricaListaStatoOperativoPrimaNota();
		//SIAC-6564
		ll.caricaListaStatoAccettazionePrimaNotaDefinitiva();	
		ll.caricaListaEventoLibera();
		ll.caricaListaClassi();
		ll.caricaListaTitoli();
	}

	/**
	 * Caricamento delle liste della prima nota integrata
	 * @throws WebServiceInvocationFailureException in caso di fallimento nel caricamento delle liste
	 */
	private void caricaListePrimaNotaIntegrata(RicercaRegistroACespiteListLoader ll) throws WebServiceInvocationFailureException {
		ll.caricaListaTipoEventoDaSessione();
		ll.caricaListaClassiDaSessione();
		ll.caricaListaTipoAtto();
		ll.caricaListaClasseSoggetto();
		ll.caricaListaTipoFinanziamento();
	}
	

	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		// Non faccio alcunche'
		return SUCCESS;
	}
	
	/**
	 * Effettua la ricerca sul Registro A(prime note verso inventario contabile)
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String effettuaRicerca() {
		final String methodName = "effettuaRicerca";
		RicercaSinteticaRegistroACespite req = model.creaRequestRicercaSinteticaRegistroACespite();
		RicercaSinteticaRegistroACespiteResponse res = cespiteService.ricercaSinteticaRegistroACespite(req);
		if(res.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT;
		}
		// Controllo il numero dei risultati
		log.debug(methodName, "Numero di risultati trovati: " + res.getTotaleElementi());
		if (res.getTotaleElementi() == 0) {
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		// Impostazione dati in sessione
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_REGISTRO_A_CESPITE, res.getPrimeNote());
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_REGISTRO_A_CESPITE, req);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);

		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #effettuaRicerca()}
	 */
	public void validateEffettuaRicerca() {
		checkCondition(model.getPrimaNota() != null, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore(), true);
		checkNotNull(model.getPrimaNota().getTipoCausale(), "Tipo prima nota", true);
		
		if(TipoCausale.Libera.equals(model.getPrimaNota().getTipoCausale())) {
			validatePrimaNotaLibera();
		} else {
			validatePrimaNotaIntegrata();
		}
	}
	
	/**
	 * Validazione dei campi della prima nota libera
	 */
	private void validatePrimaNotaLibera() {
		// Validita dei campi nella ricerca della prima nota libera
		boolean ricercaValida =
				checkCampoValorizzato(model.getPrimaNota().getNumero(), "Numero Provvisorio")
				|| checkCampoValorizzato(model.getPrimaNota().getNumeroRegistrazioneLibroGiornale(), "Numero Definitivo")
				|| checkPresenzaIdEntita(model.getEvento())
				|| checkPresenzaIdEntita(model.getCausaleEP())
				|| checkCondizioneValida(model.getConto() != null && StringUtils.isNotBlank(model.getConto().getCodice()), "Conto")
				|| checkCampoValorizzato(model.getPrimaNota().getStatoOperativoPrimaNota(), "Stato CoGe")
				|| checkCampoValorizzato(model.getPrimaNota().getStatoAccettazionePrimaNotaDefinitiva(), "Stato INV")
				|| checkStringaValorizzata(model.getPrimaNota().getDescrizione(), "Descrizione Prima nota")
				|| checkCampoValorizzato(model.getDataRegistrazioneDefinitivaDa(), "Data registrazione definitiva Da")
				|| checkCampoValorizzato(model.getDataRegistrazioneDefinitivaA(), "Data registrazione definitiva A")
				|| checkCampoValorizzato(model.getDataRegistrazioneProvvisoriaDa(), "Data registrazione provvisoria Da")
				|| checkCampoValorizzato(model.getDataRegistrazioneProvvisoriaA(), "Data registrazione provvisoria A");

		ricercaValida = controlloCodiceConto() || ricercaValida;
		ricercaValida = controlloDate(model.getDataRegistrazioneDefinitivaDa(), model.getDataRegistrazioneDefinitivaA(), "data registrazione definitiva") || ricercaValida;
		ricercaValida = controlloDate(model.getDataRegistrazioneProvvisoriaDa(), model.getDataRegistrazioneProvvisoriaA(), "data registrazione provvisoria") || ricercaValida;

		checkCondition(ricercaValida, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
	}
	
	/**
	 * Controlla la validit&agrave; del codice conto.
	 * @return <code>true</code> se il codice conto &eacute; impostato e corretto; <code>false</code> altrimenti
	 */
	private boolean controlloCodiceConto() {
		final String methodName = "controlloCodiceConto";
		if (model.getConto() == null || StringUtils.isBlank(model.getConto().getCodice())) {
			// Se non ho il conto, non effettuo la ricerca
			log.debug(methodName, "Nessun conto selezionato");
			return false;
		}

		RicercaSinteticaConto req = model.creaRequestRicercaSinteticaConto();
		RicercaSinteticaContoResponse res = contoService.ricercaSinteticaConto(req);

		if (res.hasErrori()) {
			log.debug(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return false;
		}

		checkCondition(res.getTotaleElementi() > 0, ErroreCore.ENTITA_INESISTENTE.getErrore("Conto", model.getConto().getCodice()), true);
		// Prendo il primo conto
		Conto conto = res.getConti().get(0);
		// Imposto il conto nel model
		model.setConto(conto);

		return true;
	}

	/**
	 * Controlla la validit&agrave; dele date inserite.
	 * @return <code>true</code> se le date sono corrette; <code>false</code> altrimenti
	 */
	private boolean controlloDate(Date dataDa, Date dataA, String dateType) {
		if(dataDa == null || dataA == null) {
			return false;
		}
		// La data A non puo' essere antecedente la data da
		checkCondition(!dataDa.after(dataA), ErroreCore.DATE_INCONGRUENTI.getErrore("La " + dateType + " deve essere antecedente alla " + dateType + " A"));

		return true;
	}
	
	/**
	 * Validazione dei campi della prima nota integrata
	 */
	private void validatePrimaNotaIntegrata() {
		boolean ricercaValida =
			checkPresenzaIdEntita(model.getTipoEvento())
			|| checkCampoValorizzato(model.getPrimaNota().getNumero(), "Numero provvisorio")
			|| checkCampoValorizzato(model.getPrimaNota().getNumeroRegistrazioneLibroGiornale(), "Numero definitivo")
			|| checkCampoValorizzato(model.getAnnoMovimento(), "anno movimento")
			|| checkCampoValorizzato(model.getNumeroMovimento(), "numero movimento")
			|| checkCampoValorizzato(model.getNumeroSubmovimento(), "numero submovimento")
			|| checkCampoValorizzato(model.getImportoDocumentoDa(), "importo da")
			|| checkCampoValorizzato(model.getImportoDocumentoA(), "importo a")
			|| checkCampoValorizzato(model.getPrimaNota().getStatoOperativoPrimaNota(), "Stato CoGe")
			|| checkCampoValorizzato(model.getPrimaNota().getStatoAccettazionePrimaNotaDefinitiva(), "Stato INV")
			|| checkStringaValorizzata(model.getPrimaNota().getDescrizione(), "descrizione")
			|| checkCampoValorizzato(model.getDataRegistrazioneDefinitivaDa(), "data registrazione da")
			|| checkCampoValorizzato(model.getDataRegistrazioneDefinitivaA(), "data registrazione a")
			|| checkCampoValorizzato(model.getDataRegistrazioneProvvisoriaDa(), "data registrazione provvisoria da")
			|| checkCampoValorizzato(model.getDataRegistrazioneProvvisoriaA(), "data registrazione provvisoria a");
		checkCondition(ricercaValida, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		
		checkCondition((model.getAnnoMovimento() == null && StringUtils.isBlank(model.getNumeroMovimento())) || (model.getAnnoMovimento() != null && StringUtils.isNotBlank(model.getNumeroMovimento())),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("anno o numero movimento", ": i campi devono essere entrambi valorizzati o entrambi non valorizzati"));
		checkCondition(model.getNumeroSubmovimento() == null || model.getNumeroSubmovimento().intValue() == 0 || (model.getAnnoMovimento() != null && StringUtils.isNotBlank(model.getNumeroMovimento())),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("numero submovimento", ": valorizzare anche anno e numero movimento"));
		checkCondition(model.getImportoDocumentoDa() == null || model.getImportoDocumentoA() == null || model.getImportoDocumentoA().compareTo(model.getImportoDocumentoDa()) >= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("importi da/a", "l'importo a non puo' essere inferiore all'importo da"));
		
		controlloCodiceConto();
		checkPianoDeiConti();
		controlloDate(model.getDataRegistrazioneDefinitivaDa(), model.getDataRegistrazioneDefinitivaA(), "data registrazione definitiva");
		controlloDate(model.getDataRegistrazioneProvvisoriaDa(), model.getDataRegistrazioneProvvisoriaA(), "data registrazione provvisoria");
		checkCapitolo();
		checkSoggetto();
		checkAttoAmministrativo();
		checkMovimentoGestione();
	}
	
	/**
	 * Controllo del piano dei conti
	 * @return se il piano dei conti sia valido
	 */
	protected boolean checkPianoDeiConti() {
		final String methodName = "checkPianoDeiConti";
		if(model.getRegistrazioneMovFin() == null
				|| model.getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato() == null
				|| StringUtils.isBlank(model.getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato().getCodice())) {
			model.setRegistrazioneMovFin(null);
			// Non ho il campo: trivialmente il controllo non e' valido
			return false;
		}
		
		// Ricerca del conto
		LeggiElementoPianoDeiContiByCodiceAndAnno req = model.creaRequestLeggiElementoPianoDeiContiByCodiceAndAnno();
		LeggiElementoPianoDeiContiByCodiceAndAnnoResponse res = classificatoreBilService.leggiElementoPianoDeiContiByCodiceAndAnno(req);
		
		if(res.hasErrori()) {
			// Se ho errori esco
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return false;
		}
		
		try {
			// 1. Il conto deve esistere
			checkCondition(res.getElementoPianoDeiConti() != null,
				ErroreCore.ENTITA_INESISTENTE.getErrore("Il conto finanziario", model.getRegistrazioneMovFin().getElementoPianoDeiContiAggiornato().getCodice()),
				true);
			
			ElementoPianoDeiConti epdc = res.getElementoPianoDeiConti();
			
			// 2. Deve essere valido nell'anno di bilancio
			checkCondition(ValidationUtil.isEntitaValidaPerAnnoEsercizio(epdc, model.getAnnoEsercizioInt()),
				ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto finanziario " + epdc.getCodice(), " non ha un'istanza valida nell'anno di bilancio"),
				true);
			
			// 3. Deve essere di quinto livello
			checkCondition(epdc.getLivello() == 5,
				ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto finanziario " + epdc.getCodice(), " e' di livello " + epdc.getLivello()),
				true);
			
			model.getRegistrazioneMovFin().setElementoPianoDeiContiAggiornato(epdc);
		} catch(ParamValidationException pve) {
			// Errore di validazione
			log.info(methodName, "Errore di validazione per il piano dei conti: " + pve.getMessage());
			return false;
		}
		
		return true;
	}
	/**
	 * Controllo di validit&agrave; del capitolo
	 * @return <code>true</code> se il capitolo &eacute; valido
	 */
	private boolean checkCapitolo() {
		final String methodName = "checkCapitolo";
		if(capitoloNonValorizzato(model.getCapitolo())) {
			log.debug(methodName, "Il capitolo non e' stato fornito correttamente");
			model.setCapitolo(null);
			return false;
		}
		if(capitoloParziamenteValorizzato(model.getCapitolo())) {
			// Il capitolo e' compilato solo in parte
			addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Capitolo", ": devono essere valorizzati i parametri Capitolo/Articolo" + (model.isGestioneUEB() ? "/UEB" : "")));
			return false;
		}
		// Il capitolo e' presente
		if(model.getTipoEvento() == null || model.getTipoEvento().getUid() == 0) {
			// Il tipo di evento non e' impostato. Esco con errore
			addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Capitolo", "non e' possibile selezionare un capitolo se non e' selezionato un tipo di evento"));
			return false;
		}
		TipoEvento tipoEvento = ComparatorUtils.searchByUid(model.getListaTipoEvento(), model.getTipoEvento());
		if(tipoEvento == null) {
			// Tipo evento non valido
			addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Tipo evento", "non e' un tipo evento censito per l'ente"));
			return false;
		}
		
		if(tipoEvento.isTipoSpesa()) {
			return checkCapitoloSpesa();
		}
		if(tipoEvento.isTipoEntrata()) {
			return checkCapitoloEntrata();
		}
		log.debug(methodName, "Evento di tipo ne' entrata ne' spesa: ignoro il capitolo");
		return false;
	}
	/**
	 * Controlla se il capitolo sia valorizzato
	 * @param cap il capitolo da controllare
	 * @return se il capitolo sia valorizzato
	 */
	private boolean capitoloNonValorizzato(Capitolo<?, ?> cap) {
		return cap == null ||
				(cap.getNumeroCapitolo() == null
				&& cap.getNumeroArticolo() == null
				// Se non gestisco le UEB il numero di UEB e' preselezionato con 1
				&& (!model.isGestioneUEB() || cap.getNumeroUEB() == null));
	}
	/**
	 * Controlla se il capitolo sia parzialmente valorizzato
	 * @param cap il capitolo da controllare
	 * @return se il capitolo sia valorizzato
	 */
	private boolean capitoloParziamenteValorizzato(Capitolo<?, ?> cap) {
		return cap.getNumeroCapitolo() == null
				|| cap.getNumeroArticolo() == null
				|| cap.getNumeroUEB() == null;
	}
	/**
	 * Check del capitolo di spesa
	 * @return se il capitolo esiste
	 */
	private boolean checkCapitoloSpesa() {
		final String methodName = "checkCapitoloSpesa";
		RicercaPuntualeCapitoloUscitaGestione req = model.creaRequestRicercaPuntualeCapitoloUscitaGestione();
		RicercaPuntualeCapitoloUscitaGestioneResponse res = capitoloUscitaGestioneService.ricercaPuntualeCapitoloUscitaGestione(req);
		
		if(res.hasErrori()) {
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			return false;
		}
		if(res.getCapitoloUscitaGestione() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo di uscita gestione", componiStringaCapitolo(model.getCapitolo())));
			return false;
		}
		model.setCapitolo(res.getCapitoloUscitaGestione());
		return true;
	}
	
	/**
	 * Check del capitolo di entrata
	 * @return se il capitolo esiste
	 */
	private boolean checkCapitoloEntrata() {
		final String methodName = "checkCapitoloEntrata";
		RicercaPuntualeCapitoloEntrataGestione req = model.creaRequestRicercaPuntualeCapitoloEntrataGestione();
		RicercaPuntualeCapitoloEntrataGestioneResponse res = capitoloEntrataGestioneService.ricercaPuntualeCapitoloEntrataGestione(req);
		
		if(res.hasErrori()) {
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			return false;
		}
		if(res.getCapitoloEntrataGestione() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo di entrata gestione", componiStringaCapitolo(model.getCapitolo())));
			return false;
		}
		model.setCapitolo(res.getCapitoloEntrataGestione());
		return true;
	}
	
	/**
	 * Compone la stringa identificativa del capitolo
	 * @param cap il capitolo
	 * @return la stringa del capitolo
	 */
	private String componiStringaCapitolo(Capitolo<?, ?> cap) {
		if(cap == null) {
			return "";
		}
		if(!model.isGestioneUEB()) {
			return cap.getAnnoNumeroArticolo();
		}
		return cap.getAnnoNumeroArticoloUEB();
	}
	/**
	 * Controlli per il soggetto
	 */
	private void checkSoggetto() {
		final String methodName = "checkSoggetto";
		if(model.getSoggetto() == null || StringUtils.isBlank(model.getSoggetto().getCodiceSoggetto())) {
			log.debug(methodName, "Soggetto non presente");
			model.setSoggetto(null);
			return;
		}
		RicercaSoggettoPerChiave req = model.creaRequestRicercaSoggettoPerChiave();
		RicercaSoggettoPerChiaveResponse res = soggettoService.ricercaSoggettoPerChiave(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			return;
		}
		if(res.getSoggetto() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", model.getSoggetto().getCodiceSoggetto()));
			return;
		}
		model.setSoggetto(res.getSoggetto());
	}
	/**
	 * Controlli per l'atto amministrativo
	 */
	private void checkAttoAmministrativo() {
		final String methodName = "checkAttoAmministrativo";
		AttoAmministrativo aa = model.getAttoAmministrativo();
		if(aa == null) {
			log.debug(methodName, "Atto amministrativo non presente");
			return;
		}
		checkCondition((aa.getAnno() != 0 && aa.getNumero() != 0 && aa.getTipoAtto() != null && aa.getTipoAtto().getUid() != 0)
				|| (aa.getAnno() == 0 && aa.getNumero() == 0 && (aa.getTipoAtto() == null || aa.getTipoAtto().getUid() == 0)),
			ErroreCore.VALORE_NON_VALIDO.getErrore("Provvedimento", ": e' necessario specificare anno, numero e tipo per effettuare la ricerca"), true);
		
		// SIAC-4644, modifica: anche il tipo di atto deve essere obbligatorio
		if(aa.getAnno() == 0 || aa.getNumero() == 0 || aa.getTipoAtto() == null || aa.getTipoAtto().getUid() == 0) {
			log.debug(methodName, "Atto amministrativo non presente");
			model.setAttoAmministrativo(null);
			return;
		}
		// Prendo il tipoAtto
		TipoAtto ta = ComparatorUtils.searchByUid(model.getListaTipoAtto(), aa.getTipoAtto());
		aa.setTipoAtto(ta);
		
		RicercaProvvedimento req = model.creaRequestRicercaProvvedimento();
		RicercaProvvedimentoResponse res = provvedimentoService.ricercaProvvedimento(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			return;
		}
		List<AttoAmministrativo> sacs = filterSac(res.getListaAttiAmministrativi(), aa.getStrutturaAmmContabile() == null || aa.getStrutturaAmmContabile().getUid() == 0);
		try {
			// Controllo le SAC: deve esserci al piu' un provvedimento senza SAC
			checkUnicoAttoAmministrativo(sacs, aa.getStrutturaAmmContabile(), true);
		} catch (ParamValidationException pve) {
			log.debug(methodName, pve.getMessage());
			return;
		}
		model.setAttoAmministrativo(sacs.get(0));
	}
	/**
	 * Filtra gli atti tramite le sac
	 * @param atti gli atti da filtrare
	 * @param sacNull se la struttura sia null
	 */
	private List<AttoAmministrativo> filterSac(List<AttoAmministrativo> atti, boolean sacNull) {
		if(!sacNull) {
			return atti;
		}
		List<AttoAmministrativo> res = new ArrayList<AttoAmministrativo>();
		for(AttoAmministrativo aa : atti) {
			if(aa != null && (aa.getStrutturaAmmContabile() == null || aa.getStrutturaAmmContabile().getUid() == 0)) {
				res.add(aa);
			}
		}
		return res;
	}
	/**
	 * Controllo del movimento di gestione
	 * @return se il movimento risulta essere valido
	 */
	protected boolean checkMovimentoGestione() {
		final String methodName = "checkMovimentoGestione";
		if(hasNoDatiMovimentoGestione(model.getAccertamento()) && hasNoDatiMovimentoGestione(model.getImpegno())) {
			log.debug(methodName, "Il movimento di gestione non e' stato fornito correttamente");
			return true;
		}
		if(model.getTipoEvento() == null || model.getTipoEvento().getUid() == 0) {
			// Il tipo di evento non e' impostato. Esco con errore
			addErrore(ErroreCore.VALORE_NON_VALIDO.getErrore("Movimento Gestione", "non e' possibile selezionare un movimento gestione se non e' selezionato un tipo di evento"));
			return false;
		}
		TipoEvento tipoEvento = ComparatorUtils.searchByUid(model.getListaTipoEvento(), model.getTipoEvento());
		
		// Il capitolo e' presente
		
		if(tipoEvento.isTipoSpesa()) {
			return checkImpegno();
		}
		if(tipoEvento.isTipoEntrata()) {
			return checkAccertamento();
		}
		log.debug(methodName, "Evento di tipo ne' entrata ne' spesa: ignoro il movimento di gestione");
		return true;
	}
	
	/**
	 * Controlla se non vi siano i dati per il movimento di gestione
	 * @param mg il movimento di gestione
	 * @return se il movimento di gestione non ha dati
	 */
	private boolean hasNoDatiMovimentoGestione(MovimentoGestione mg) {
		return mg == null || mg.getAnnoMovimento() == 0 || mg.getNumero() == null;
	}
	
	/**
	 * Controlla l'accertamento
	 * @return se l'accertamento sia valido
	 */
	protected boolean checkAccertamento() {
		final String methodName = "checkAccertamento";
		Accertamento accertamento =  model.getAccertamento();
		SubAccertamento subAccertamento = model.getSubAccertamento();
		
		if(accertamento == null || (accertamento.getAnnoMovimento() == 0 || accertamento.getNumero() == null)) {
			return false;
		}
		
		RicercaAccertamentoPerChiaveOttimizzato req = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato();
		logServiceRequest(req);
		RicercaAccertamentoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errori nell'invocazione della ricercaAccertamentoPerChiaveOttimizzato");
			addErrori(res);
			return false;
		}
		if(res.isFallimento() || res.getAccertamento() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", accertamento.getAnnoMovimento()+"/"+accertamento.getNumero()));
			return false;
		}
		
		accertamento = res.getAccertamento();
		
		model.setMovimentoGestione(accertamento);
		
		if(subAccertamento != null && subAccertamento.getNumero() != null) {
			BigDecimal numero = subAccertamento.getNumero();
			// Controlli di validità sull'impegno
			subAccertamento = findSubAccertamentoLegatoAccertamentoByNumero(res.getAccertamento(), subAccertamento);
			if(subAccertamento == null) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Subaccertamento", accertamento.getAnnoMovimento() + "/" + accertamento.getNumero() + "-" + numero));
				return false;
			}
			model.setSubMovimentoGestione(subAccertamento);
		} 
		
		return true;
	}
	
	/**
	 * Trova il subAccertamento nell'elenco degli subaccertamenti dell'accertamento, se presente.
	 * 
	 * @param accertamento    l'accertamento tra i cui subAccertamenti trovare quello fornito
	 * @param subAccertamento il subaccertamento da cercare
	 * 
	 * @return il subaccertamento legato, se presente; <code>null</code> in caso contrario
	 */
	private SubAccertamento findSubAccertamentoLegatoAccertamentoByNumero(Accertamento accertamento, SubAccertamento subAccertamento) {
		SubAccertamento result = null;
		if(accertamento.getElencoSubAccertamenti() != null) {
			for(SubAccertamento s : accertamento.getElencoSubAccertamenti()) {
				if(s.getNumero().compareTo(subAccertamento.getNumero()) == 0) {
					result = s;
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * Ricerca dell'impegno per chiave
	 * @return la response dell'impegno
	 */
	private boolean checkImpegno() {
		
		Impegno impegno = model.getImpegno();
		SubImpegno subImpegno = model.getSubImpegno();
		
		boolean impegnoValorizzato = impegno != null && impegno.getAnnoMovimento() != 0 && impegno.getNumero() != null;
		boolean subImpegnoValorizzato = subImpegno != null && subImpegno.getNumero() != null;

		
		checkCondition(impegnoValorizzato || !subImpegnoValorizzato , ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("per indicare un subimpegno e' necessario indicare anche un impegno."), true);
		
		
		
		if(!impegnoValorizzato){
			return true;
		}
		
		RicercaImpegnoPerChiaveOttimizzato req = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();
		logServiceRequest(req);
		RicercaImpegnoPerChiaveOttimizzatoResponse res = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(req);
		logServiceResponse(res);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
			return false;
		}
		
		if(res.isFallimento() || res.getImpegno() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", impegno.getAnnoMovimento()+"/"+impegno.getNumero()));
			return false;
		}
		
		impegno = res.getImpegno();
		
		model.setMovimentoGestione(impegno);
		
		if(subImpegno != null && subImpegno.getNumero() != null) {
			BigDecimal numero = subImpegno.getNumero();
			// Controlli di validità sull'impegno
			subImpegno = findSubImpegnoLegatoImpegnoByNumero(res.getImpegno(), subImpegno);
			if(subImpegno == null) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("SubImpegno", ottieniChiaveMovgest(impegno) +  "-" + numero));
				return false;
			}
			model.setSubMovimentoGestione(subImpegno);
			
		} 		
		return true;
	}
	
	/**
	 * Trova il subImpegno nell'elenco degli subimpegni dell'impegno, se presente.
	 * 
	 * @param impegno    l'impegno tra i cui subImpegni trovare quello fornito
	 * @param subImpegno il subimpegno da cercare
	 * 
	 * @return il subimpegno legato, se presente; <code>null</code> in caso contrario
	 */
	private SubImpegno findSubImpegnoLegatoImpegnoByNumero(Impegno impegno, SubImpegno subImpegno) {
		SubImpegno result = null;
		if(impegno.getElencoSubImpegni() != null) {
			for(SubImpegno s : impegno.getElencoSubImpegni()) {
				if(s.getNumero().compareTo(subImpegno.getNumero()) == 0) {
					result = s;
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * Calcola la chiave del movimento
	 * @param movimentoGestione il movimento
	 * @return la chiave del movimento
	 */
	private String ottieniChiaveMovgest(MovimentoGestione movimentoGestione) {
		StringBuilder sb = new StringBuilder();
		if(movimentoGestione != null) {
			sb.append(movimentoGestione.getAnnoMovimento())
				.append('/')
				.append(movimentoGestione.getNumero());
		}
		return sb.toString();
	}
}
