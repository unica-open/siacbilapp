/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.registroa.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siaccespapp.frontend.ui.action.registroa.RicercaRegistroACespiteAction;
import it.csi.siac.siaccespapp.frontend.ui.model.registroa.RicercaRegistroACespiteModel;
import it.csi.siac.siaccommon.util.log.LogUtil;
import it.csi.siac.siaccommonapp.handler.session.SessionHandler;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausaleResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaDefinitiva;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Caricamento delle liste per la ricerca registro A(prime note verso inventario contabile)
 * @author Marchino Alessandro
 * @version 1.0.0 - 18/10/2018
 *
 */
public class RicercaRegistroACespiteListLoader {

	private final LogUtil log;
	private final SessionHandler sessionHandler;
	private final RicercaRegistroACespiteAction action;
	private final RicercaRegistroACespiteModel model;
	
	private final CausaleService causaleService;
	private final CodificheService codificheService;
	private final ClassificatoreBilService classificatoreBilService;
	private final ProvvedimentoService provvedimentoService;
	private final SoggettoService soggettoService;
	
	/**
	 * Costruttore di wrap per le propriet&agrave;
	 * @param causaleService il servizio della causale
	 * @param codificheService il servizio delle codifiche
	 * @param classificatoreBilService il servizio del classificatore BIL
	 * @param provvedimentoService il servizio del provvedimento
	 * @param soggettoService il servizio del soggetto
	 * @param sessionHandler l'handler della sessione
	 * @param action la action
	 */
	public RicercaRegistroACespiteListLoader(
			CausaleService causaleService,
			CodificheService codificheService,
			ClassificatoreBilService classificatoreBilService,
			ProvvedimentoService provvedimentoService,
			SoggettoService soggettoService,
			SessionHandler sessionHandler,
			RicercaRegistroACespiteAction action) {
		this.log = new LogUtil(getClass());
		
		this.sessionHandler = sessionHandler;
		this.action = action;
		this.model = action.getModel();
		this.causaleService = causaleService;
		this.codificheService = codificheService;
		this.soggettoService = soggettoService;
		this.classificatoreBilService = classificatoreBilService;
		this.provvedimentoService = provvedimentoService;
	}
	
	/**
	 * Caricamento della lista del tipo causale
	 */
	public void caricaListaTipoCausale() {
		model.setListaTipoCausale(Arrays.asList(TipoCausale.values()));
	}
	
	/**
	 * Caricamento della lista delle causali.
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	public void caricaListaCausaleLibera() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaCausaleLibera";
		List<CausaleEP> listaCausaliEp = sessionHandler.getParametro(BilSessionParameter.LISTA_CAUSALE_EP_LIBERA_GEN);
		// Se non ho i dati, effettuo la ricerca

		if (listaCausaliEp == null) {
			// recuperare da servizio la lista causali
			RicercaSinteticaModulareCausale req = model.creaRequestRicercaSinteticaModulareCausale();
			RicercaSinteticaModulareCausaleResponse res = causaleService.ricercaSinteticaModulareCausale(req);
			handleError(methodName, RicercaSinteticaModulareCausale.class, res);

			listaCausaliEp = res.getCausali();
			// Aggiungo il risultato in sessione
			sessionHandler.setParametro(BilSessionParameter.LISTA_CAUSALE_EP_LIBERA_GEN, listaCausaliEp);
		}
		model.setListaCausaleEP(listaCausaliEp);
	}

	/**
	 * Caricamento della lista degli stati operativi per la prima nota.
	 */
	public void caricaListaStatoOperativoPrimaNota() {
		model.setListaStatoOperativoPrimaNota(Arrays.asList(
			StatoOperativoPrimaNota.ANNULLATO,
			StatoOperativoPrimaNota.DEFINITIVO
		));
	}

	/**
	 * Caricamento della lista degli stati Inventario
	 */
	public void caricaListaStatoAccettazionePrimaNotaDefinitiva() {
		model.setListaStatoAccettazionePrimaNotaDefinitiva(Arrays.asList(StatoAccettazionePrimaNotaDefinitiva.values()));
	}

	/**
	 * Caricamento della lista degli eventi per la prima nota libera.
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	public void caricaListaEventoLibera() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaEventoLibera";
		//List<String> possibleEventCodes = Arrays.asList("ACQ", "MVA", "VEN", "GIR");
		List<Evento> listaEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_EVENTO_FULL);
		if (listaEvento == null) {
			log.debug(methodName, "Lista di Evento non presente in sessione. Caricamento da servizio");
			RicercaCodifiche req = model.creaRequestRicercaCodifiche(Evento.class);
			RicercaCodificheResponse res = codificheService.ricercaCodifiche(req);
			handleError(methodName, RicercaCodifiche.class, res);
			
			listaEvento = res.getCodifiche(Evento.class);
			sessionHandler.setParametro(BilSessionParameter.LISTA_EVENTO_FULL, listaEvento);
		}
		List<Evento> listaEventoLibera = new ArrayList<Evento>();
		for (Evento e : listaEvento) {
			if (e != null
					&& e.getTipoEvento() != null
					&& (
						BilConstants.CODICE_TIPO_EVENTO_CONTABILITA_GENERALE_INVENTARIO.getConstant().equals(e.getTipoEvento().getCodice())
						|| BilConstants.CODICE_TIPO_EVENTO_GIROCONTI.getConstant().equals(e.getTipoEvento().getCodice())
					) && !e.getTipoEvento().getListaTipoCausaleEP().isEmpty()) {
				for (TipoCausale tc : e.getTipoEvento().getListaTipoCausaleEP()) {
					if (TipoCausale.Libera.equals(tc)) {
						listaEventoLibera.add(e);
						break;
					}
				}
			}
		}
		model.setListaEvento(listaEventoLibera);
	}

	/**
	 * Caricamento della lista delle classi.
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	public void caricaListaClassi() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaClassi";

		List<ClassePiano> listaClassePiano = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSE_PIANO_GEN);
		if (listaClassePiano == null) {
			RicercaCodifiche req = model.creaRequestRicercaCodifiche("ClassePiano_" + Ambito.AMBITO_FIN.getSuffix());
			RicercaCodificheResponse res = codificheService.ricercaCodifiche(req);
			handleError(methodName, RicercaCodifiche.class, res);
			
			listaClassePiano = res.getCodifiche(ClassePiano.class);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSE_PIANO_GEN, listaClassePiano);
		}
		model.setListaClassi(listaClassePiano);
	}
	
	/**
	 * Caricamento della lista dei titoli.
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione della request
	 */
	public void caricaListaTitoli() throws WebServiceInvocationFailureException {
		LeggiClassificatoriByTipoElementoBilResponse responseEntrata = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant());
		model.setListaTitoloEntrata(responseEntrata.getClassificatoriTitoloEntrata());
		
		LeggiClassificatoriByTipoElementoBilResponse responseSpesa = ottieniResponseLeggiClassificatoriByTipoElementoBil(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
		model.setListaTitoloSpesa(responseSpesa.getClassificatoriTitoloSpesa()); 
		model.setListaMissione(responseSpesa.getClassificatoriMissione());
	}
	
	/**
	 * Caricamento della lista del tipo evento, solo da sessione.
	 */
	public void caricaListaTipoEventoDaSessione() {
		List<TipoEvento> listaTipoEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_EVENTO);
		if(listaTipoEvento != null) {
			model.setListaTipoEvento(listaTipoEvento);
		}
	}
	
	/**
	 * Caricamento della lista delle classi.
	 */
	public void caricaListaClassiDaSessione() {
		List<ClassePiano> listaClassePiano = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSE_PIANO_GEN);
		if(listaClassePiano != null) {
			model.setListaClassi(listaClassePiano);
		}
	}
	
	/**
	 * Caricamento della lista del tipo atto
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	public void caricaListaTipoAtto() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaTipoAtto";
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		if(listaTipoAtto == null) {
			TipiProvvedimento req = model.creaRequestTipiProvvedimento();
			TipiProvvedimentoResponse res = provvedimentoService.getTipiProvvedimento(req);
			handleError(methodName, TipiProvvedimento.class, res);
			
			listaTipoAtto = res.getElencoTipi();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, listaTipoAtto);
		}
		
		model.setListaTipoAtto(listaTipoAtto);
	}
	
	/**
	 * Caricamento della lista della classe soggetto
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	public void caricaListaClasseSoggetto() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaClasseSoggetto";
		List<CodificaFin> listaClasseSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaClasseSoggetto == null) {
			ListeGestioneSoggetto req = model.creaRequestListeGestioneSoggetto();
			ListeGestioneSoggettoResponse res = soggettoService.listeGestioneSoggetto(req);
			handleError(methodName, ListeGestioneSoggetto.class, res);			
			listaClasseSoggetto = res.getListaClasseSoggetto();
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaClasseSoggetto);
		}
		model.setListaClasseSoggetto(listaClasseSoggetto);
	}

	/**
	 * Caricamento della lista del tipo finanziamento
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	public void caricaListaTipoFinanziamento() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaTipoFinanziamento";
		List<TipoFinanziamento> listaTipiFinanziamento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO);
		if(listaTipiFinanziamento == null) {
			LeggiClassificatoriGenericiByTipoElementoBil req = model.creaRequestLeggiClassificatoriGenericiByTipoElementoBil(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
			LeggiClassificatoriGenericiByTipoElementoBilResponse res = classificatoreBilService.leggiClassificatoriGenericiByTipoElementoBil(req);
			handleError(methodName, LeggiClassificatoriGenericiByTipoElementoBil.class, res);
			
			listaTipiFinanziamento = res.getClassificatoriTipoFinanziamento();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO, listaTipiFinanziamento);
		}
		
		model.setListaTipiFinanziamento(listaTipiFinanziamento);
	}

	/**
	 * Gestione degli errori
	 * @param callingMethodName il metodo chiamante
	 * @param req la request
	 * @param res la response
	 * @throws WebServiceInvocationFailureException in caso di errore nella response
	 */
	private void handleError(final String callingMethodName, Class<? extends ServiceRequest> reqClass, ServiceResponse res) throws WebServiceInvocationFailureException {
		if(!res.hasErrori()) {
			return;
		}
		String errorMsg = action.createErrorInServiceInvocationString(reqClass, res);
		log.warn(callingMethodName, errorMsg);
		// Set errors
		model.addErrori(res.getErrori());
		for (Errore e : res.getErrori()) {
			action.addActionError(e.getTesto());
		}
		
		throw new WebServiceInvocationFailureException(errorMsg);
	}
	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una request per il servizio di lettura classificatori by tipo elemento BIL.
	 * @param codice il codice definente il classificatore
	 * @return la response corrispondente
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(String codice) throws WebServiceInvocationFailureException {
		final String methodName = "ottieniResponseLeggiClassificatoriByTipoElementoBil";
		LeggiClassificatoriByTipoElementoBil req = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice);
		LeggiClassificatoriByTipoElementoBilResponse res = classificatoreBilService.leggiClassificatoriByTipoElementoBil(req);
		handleError(methodName, LeggiClassificatoriByTipoElementoBil.class, res);
		return res;
	}

}
