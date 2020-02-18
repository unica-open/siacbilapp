/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.BaseInserisciAggiornaCausaleEPBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.CodificheService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiElementoPianoDeiContiByCodiceAndAnnoResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodificheResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriEP;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaClassificatoriEPResponse;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacgenser.frontend.webservice.CausaleService;
import it.csi.siac.siacgenser.frontend.webservice.ContoService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaEventiPerTipo;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaEventiPerTipoResponse;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.ClassificatoreEP;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.OperazioneTipoImporto;
import it.csi.siac.siacgenser.model.OperazioneUtilizzoConto;
import it.csi.siac.siacgenser.model.OperazioneUtilizzoImporto;
import it.csi.siac.siacgenser.model.TipoCausale;
import it.csi.siac.siacgenser.model.TipoEvento;
import it.csi.siac.siacgenser.model.errore.ErroreGEN;

/**
 * Classe base di action per l'inserimento e l'aggiornamento della causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 27/03/2015
 * 
 * @param <M> la tipizzazione del model
 *
 */
public abstract class BaseInserisciAggiornaCausaleEPBaseAction<M extends BaseInserisciAggiornaCausaleEPBaseModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3955333394357185896L;
	
	/** Serviz&icirc; della causale */
	@Autowired protected transient CausaleService causaleService;
	/** Serviz&icirc; dei classificatori bil */
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	/** Serviz&icirc; delle codifiche */
	@Autowired private transient CodificheService codificheService;
	/** Serviz&icirc; del conto */
	@Autowired protected transient ContoService contoService;
	/** Serviz&icirc; del soggetto */
	@Autowired protected transient SoggettoService soggettoService;
	
	
	/** Nome del model dell'aggiornamento per la sessione */
	public static final String MODEL_SESSION_NAME_AGGIORNAMENTO_FIN = "AggiornaCausaleEPFINModel";
	/** Nome del model dell'inserimento per la sessione */
	public static final String MODEL_SESSION_NAME_INSERIMENTO_FIN = "InserisciCausaleEPFINModel";
	/** Nome del model dell'aggiornamento per la sessione */
	public static final String MODEL_SESSION_NAME_AGGIORNAMENTO_GSA = "AggiornaCausaleEPGSAModel";
	/** Nome del model dell'inserimento per la sessione */
	public static final String MODEL_SESSION_NAME_INSERIMENTO_GSA = "InserisciCausaleEPGSAModel";
	
	/**
	 * @return il parametro di sessione corrispondente alla lista classi piano
	 */
	protected abstract BilSessionParameter getBilSessionParameterListaClassiPiano();

	
	@Override
	public void prepare() throws Exception {
		cleanErroriMessaggiInformazioni();
	}
	
	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		
		if (getFaseDiBilancioNonCompatibile(faseBilancio)) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
	/**
	 * Controlla la fase di bilancio non compatibile
	 * @param faseBilancio la fase di bilancio
	 * @return se la fase non &eacute; compatibile
	 */
	protected boolean getFaseDiBilancioNonCompatibile(FaseBilancio faseBilancio) {
		return 
		FaseBilancio.PLURIENNALE.equals(faseBilancio) ||
		FaseBilancio.PREVISIONE.equals(faseBilancio) ||
		FaseBilancio.CHIUSO.equals(faseBilancio);
	}
	
	/**
	 * Caricamento delle liste per l'interazione utente.
	 * 
	 * @throws GenericFrontEndMessagesException in caso di eccezione nel caricamento delle liste
	 */
	protected void caricaListe() {
		try {
			caricaListaTipoCausale();
			caricaListaClassificatoriEP();
			caricaListaTipoEventoAndClassePiano();
			caricaListaEvento();
			caricaListeConto();
			caricaListaClassi();
			caricaListaTitoli();
			caricaListaClasseSoggetto();
		} catch(WebServiceInvocationFailureException wsife) {
			throw new GenericFrontEndMessagesException(wsife);
		}
	}
	
	/**
	 * Caricamento della lista del Tipo causale.
	 */
	private void caricaListaTipoCausale() {
		model.setListaTipoCausale(Arrays.asList(TipoCausale.values()));
	}
	
	/**
	 * Caricamento della lista dei classificatori EP.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione dei serviz&icirc;
	 */
	protected void caricaListaClassificatoriEP() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaClassificatoriEP";
		// TODO: possono essere fino a 6
		List<ClassificatoreEP> listaClassificatoreEP1 = sessionHandler.getParametro(BilSessionParameter.LISTA_VALORE_BENE);
		List<ClassificatoreEP> listaClassificatoreEP2 = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_AQUISIZIONE_BENE);
		List<ClassificatoreEP> listaClassificatoreEP3 = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO_COLLEGATO);
		List<ClassificatoreEP> listaClassificatoreEP4 = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ONERE_FISCALE);
		List<ClassificatoreEP> listaClassificatoreEP5 = sessionHandler.getParametro(BilSessionParameter.LISTA_RILEVANTE_IVA);
		
		boolean isListaPresente = listaClassificatoreEP1 != null && listaClassificatoreEP2 != null && listaClassificatoreEP3 != null
				&& listaClassificatoreEP4 != null && listaClassificatoreEP5 != null;
		if(!isListaPresente) {
			log.debug(methodName, "Almeno una lista di classificatori EP per la causale EP non presente in sessione. Caricamento da servizio");
			// Devo caricare le liste
			RicercaClassificatoriEP request = model.creaRequestRicercaClassificatoriEP();
			logServiceRequest(request);
			RicercaClassificatoriEPResponse response = causaleService.ricercaClassificatoriEP(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				String errorMsg = createErrorInServiceInvocationString(request, response);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			
			listaClassificatoreEP1 = response.getListaClassificatori(TipologiaClassificatore.VALORE_BENE);
			listaClassificatoreEP2 = response.getListaClassificatori(TipologiaClassificatore.MODALITA_AQUISIZIONE_BENE);
			listaClassificatoreEP3 = response.getListaClassificatori(TipologiaClassificatore.TIPO_DOCUMENTO_COLLEGATO);
			listaClassificatoreEP4 = response.getListaClassificatori(TipologiaClassificatore.TIPO_ONERE_FISCALE);
			listaClassificatoreEP5 = response.getListaClassificatori(TipologiaClassificatore.RILEVANTE_IVA);
			
			sessionHandler.setParametro(BilSessionParameter.LISTA_VALORE_BENE, listaClassificatoreEP1);
			sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_AQUISIZIONE_BENE, listaClassificatoreEP2);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_DOCUMENTO_COLLEGATO, listaClassificatoreEP3);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ONERE_FISCALE, listaClassificatoreEP4);
			sessionHandler.setParametro(BilSessionParameter.LISTA_RILEVANTE_IVA, listaClassificatoreEP5);
		}
		
		log.debug(methodName, "Estrazione dei label dei classificatori");
		String labelClassificatoreEP1 = estraiLabel(listaClassificatoreEP1);
		String labelClassificatoreEP2 = estraiLabel(listaClassificatoreEP2);
		String labelClassificatoreEP3 = estraiLabel(listaClassificatoreEP3);
		String labelClassificatoreEP4 = estraiLabel(listaClassificatoreEP4);
		String labelClassificatoreEP5 = estraiLabel(listaClassificatoreEP5);
		
		model.setLabelClassificatoreEP1(labelClassificatoreEP1);
		model.setLabelClassificatoreEP2(labelClassificatoreEP2);
		model.setLabelClassificatoreEP3(labelClassificatoreEP3);
		model.setLabelClassificatoreEP4(labelClassificatoreEP4);
		model.setLabelClassificatoreEP5(labelClassificatoreEP5);
		
		model.setListaClassificatoreEP1(listaClassificatoreEP1);
		model.setListaClassificatoreEP2(listaClassificatoreEP2);
		model.setListaClassificatoreEP3(listaClassificatoreEP3);
		model.setListaClassificatoreEP4(listaClassificatoreEP4);
		model.setListaClassificatoreEP5(listaClassificatoreEP5);
	}
	
	/**
	 * Estrae il label per la lista di codifiche fornita.
	 * @param <C> la tipizzazione del classificatore
	 * 
	 * @param list la lista di codifiche da cui estrarre il label
	 * 
	 * @return il label corrispondente alla lista
	 */
	protected <C extends ClassificatoreGenerico> String estraiLabel(List<C> list) {
		if(list != null && !list.isEmpty()) {
			return list.get(0).getTipoClassificatore().getDescrizione();
		}
		return null;
	}

	/**
	 * Caricamento delle liste del tipo evento e della classe piano.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private void caricaListaTipoEventoAndClassePiano() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaTipoEventoAndClassePiano";
		
		// Le carico insieme per evitare di duplicare la chiamata al servizio
		List<TipoEvento> listaTipoEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_EVENTO);
		List<ClassePiano> listaClassePiano = sessionHandler.getParametro(getBilSessionParameterListaClassiPiano());
		if(listaTipoEvento == null || listaClassePiano == null) {
			RicercaCodifiche request = model.creaRequestRicercaCodifiche(TipoEvento.class, "ClassePiano" + "_" + model.getAmbito().getSuffix());
			logServiceRequest(request);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorMsg = createErrorInServiceInvocationString(request, response);
				log.warn(methodName, errorMsg);
				addErrori(response);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			//TODO filtra classePiano
			listaTipoEvento = response.getCodifiche(TipoEvento.class);
			listaClassePiano = response.getCodifiche(ClassePiano.class);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_EVENTO, listaTipoEvento);
			sessionHandler.setParametro(getBilSessionParameterListaClassiPiano(), listaClassePiano);
		}
		
		model.setListaTipoEvento(listaTipoEvento);
		model.setListaTipoEventoFiltrata(listaTipoEvento);
		if (model.getCausaleEP() != null && model.getCausaleEP().getTipoCausale()!=null) {
			List<TipoEvento> listaTipoEventoTipoCausaleEPFiltrata = new ArrayList<TipoEvento> ();
			
			// separo i tipiEvento per TipoCausale
			if (listaTipoEvento!=null) {
				//verifica tipo causale  tipo evento 
				for (TipoEvento e : listaTipoEvento) {
					if (e!=null && !e.getListaTipoCausaleEP().isEmpty() ) {
						for (TipoCausale tc : e.getListaTipoCausaleEP()) {
							if (model.getCausaleEP().getTipoCausale().equals(tc)) {
								listaTipoEventoTipoCausaleEPFiltrata.add(e);
								break;
							}
						}
					}
				}
			}
			model.setListaTipoEventoFiltrata(listaTipoEventoTipoCausaleEPFiltrata);
		} 
		model.setListaClassePiano(listaClassePiano);
	}
	
	/**
	 * Caricamento della lista del Tipo evento filtrata in base al tipo causale se presente.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione 
	 */
	public String ottieniListaTipoEventoFiltrata() {
		final String methodName = "ottieniListaTipoEventoFiltrata";
		
		List<TipoEvento> listaTipoEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_EVENTO);
		if(listaTipoEvento == null) {
			RicercaCodifiche request = model.creaRequestRicercaCodifiche(TipoEvento.class);
			logServiceRequest(request);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorMsg = createErrorInServiceInvocationString(request, response);
				log.warn(methodName, errorMsg);
				addErrori(response);
				return INPUT;
			}
			
			listaTipoEvento = response.getCodifiche(TipoEvento.class);
			
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_EVENTO, listaTipoEvento);
		}
		model.setListaTipoEvento(listaTipoEvento);
		
		if(model.getCausaleEP() == null || model.getCausaleEP().getTipoCausale() == null) {
			model.setListaTipoEventoFiltrata(listaTipoEvento);
			return SUCCESS;
		}
			
		List<TipoEvento> listaTipoEventoTipoCausaleEPFiltrata = new ArrayList<TipoEvento> ();
		
		// separo i tipiEvento per TipoCausale
		if (listaTipoEvento!=null) {
			//verifica tipo causale  tipo evento 
			for (TipoEvento e : listaTipoEvento) {
				if (e!=null && !e.getListaTipoCausaleEP().isEmpty() ) {
					for (TipoCausale tc : e.getListaTipoCausaleEP()) {
						if (model.getCausaleEP().getTipoCausale().equals(tc)) {
							listaTipoEventoTipoCausaleEPFiltrata.add(e);
							break;
						}
					}
				}
			}
		}
		model.setListaTipoEventoFiltrata(listaTipoEventoTipoCausaleEPFiltrata);
		
		return SUCCESS;
	}
	
	/**
	 * Caricamento delle liste del tipo evento.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di errore nell'invocazione del servizio
	 */
	private void caricaListaEvento() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaEvento";
		
		if(!idEntitaPresente(model.getTipoEvento())) {
			// Se non ho il tipo di evento, non ho nemmeno modo di caricare l'evento
			return;
		}
		
		Integer uidTipoEvento = sessionHandler.getParametro(BilSessionParameter.ULTIMO_UID_TIPO_EVENTO_RICERCATO);
		List<Evento> listaEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_EVENTO);
		// Se non ho i dati, effettuo la ricerca
		if(uidTipoEvento == null || (uidTipoEvento.intValue() != model.getTipoEvento().getUid()) || listaEvento == null) {
			RicercaEventiPerTipo request = model.creaRequestRicercaEventiPerTipo();
			logServiceRequest(request);
			RicercaEventiPerTipoResponse response = causaleService.ricercaEventiPerTipo(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorMsg = createErrorInServiceInvocationString(request, response);
				log.warn(methodName, errorMsg);
				addErrori(response);
				throw new WebServiceInvocationFailureException(errorMsg);
			}
			
			listaEvento = response.getEventi();
			
			// Aggiungo il risultato in sessione
			sessionHandler.setParametro(BilSessionParameter.LISTA_EVENTO, listaEvento);
			sessionHandler.setParametro(BilSessionParameter.ULTIMO_UID_TIPO_EVENTO_RICERCATO, model.getTipoEvento().getUid());
		}
		
		model.setListaEvento(listaEvento);
	}
	
	/**
	 * Carica le liste relative al conto.
	 */
	private void caricaListeConto() {
		model.setListaOperazioneSegnoConto(Arrays.asList(OperazioneSegnoConto.values()));
		model.setListaOperazioneUtilizzoConto(Arrays.asList(OperazioneUtilizzoConto.values()));
		model.setListaOperazioneUtilizzoImporto(Arrays.asList(OperazioneUtilizzoImporto.values()));
		model.setListaOperazioneTipoImporto(Arrays.asList(OperazioneTipoImporto.values()));
	}

	
	/**
	 * Caricamento della lista delle classi.
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected void caricaListaClassi() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaClassi";
		List<ClassePiano> listaClassePiano = sessionHandler.getParametro(getBilSessionParameterListaClassiPiano());
		if(listaClassePiano == null) {
			RicercaCodifiche request = model.creaRequestRicercaCodifiche("ClassePiano" + "_" + model.getAmbito().getSuffix());
			logServiceRequest(request);
			RicercaCodificheResponse response = codificheService.ricercaCodifiche(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorMsg = createErrorInServiceInvocationString(request, response);
				log.warn(methodName, errorMsg);
				addErrori(response);
				throw new WebServiceInvocationFailureException(errorMsg);
			}

			listaClassePiano = response.getCodifiche(ClassePiano.class);
			sessionHandler.setParametro(getBilSessionParameterListaClassiPiano(), listaClassePiano);
		}
		model.setListaClassi(listaClassePiano);
		
	}
	
	/**
	 * Metodo di utilit&agrave; per l'ottenimento di una request per il servizio di {@link LeggiClassificatoriByTipoElementoBilResponse}.
	 * 
	 * @param codice il codice definente il classificatore
	 * @return la response corrispondente
	 */
	protected LeggiClassificatoriByTipoElementoBilResponse ottieniResponseLeggiClassificatoriByTipoElementoBil(String codice) {
		LeggiClassificatoriByTipoElementoBil request = model.creaRequestLeggiClassificatoriByTipoElementoBil(codice);
		logServiceRequest(request);
		LeggiClassificatoriByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriByTipoElementoBil(request);
		logServiceResponse(response);
		return response;
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
		
		
		model.setListaTitoloSpesa(listaTS);
	
	}
	
	
	/**
	 * Controlla se la lista delle Classi Soggetto sia presente valorizzata in sessione.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi di documento.
	 */
	protected void caricaListaClasseSoggetto() {
		List<CodificaFin> listaClassiSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaClassiSoggetto == null) {
			ListeGestioneSoggetto request = model.creaRequestListeGestioneSoggetto();
			ListeGestioneSoggettoResponse response = soggettoService.listeGestioneSoggetto(request);
			logServiceResponse(response);
			if(!response.hasErrori()) {
				listaClassiSoggetto = response.getListaClasseSoggetto();
				ComparatorUtils.sortByCodiceFin(listaClassiSoggetto);
				sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaClassiSoggetto);
			}
		}
		model.setListaClasseSoggetto(listaClassiSoggetto);
	}
	/**
	 * Ingresso nello step 1.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String step1() {
		return SUCCESS;
	}
	
	/**
	 * Annullamento dei dati relativi allo step 1.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaStep1() {
		return SUCCESS;
	}
	
	/**
	 * Preparazione per il metodo {@link #completeStep1()}.
	 */
	public void prepareCompleteStep1() {
		// Pulisco la causale EP
		model.setCausaleEP(null);
		
		// Pulisco i dati esterni
		model.setSoggetto(null);
		model.setClassificatoreEP1(null);
		model.setClassificatoreEP2(null);
		model.setClassificatoreEP3(null);
		model.setClassificatoreEP4(null);
		model.setClassificatoreEP5(null);
		model.setClassificatoreEP6(null);
		
		// Imposto la lista degli eventi
		List<Evento> listaEvento = sessionHandler.getParametro(BilSessionParameter.LISTA_EVENTO);
		model.setListaEvento(listaEvento);
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep1()}.
	 */
	public void validateCompleteStep1() {
		final String methodName = "validateCompleteStep1";
		// Se non ho la causale, qualcosa e' andato storto: esco subito
		checkNotNull(model.getCausaleEP(), "Causale", true);
		
		checkNotNull(model.getCausaleEP().getTipoCausale(), "Tipo");
		checkNotNullNorEmpty(model.getCausaleEP().getCodice(), "Codice");
		checkNotNullNorEmpty(model.getCausaleEP().getDescrizione(), "Descrizione");
		
		if(TipoCausale.Integrata.equals(model.getCausaleEP().getTipoCausale())) {
			//controllo da effettuare solo per ambito FIN
			if (isAmbitoFIN(model.getAmbito())) {
				try {
					checkContoQuintoLivello();
				} catch(ParamValidationException pve) {
					log.debug(methodName, "Errore di validazione per le causali di raccordo: " + pve.getMessage());
				}
			}
			try {
				checkSoggetto();
			} catch(ParamValidationException pve) {
				log.debug(methodName, "Errore di validazione per le causali di raccordo: " + pve.getMessage());
			}
		}
		
		checkNotNullNorInvalidUid(model.getTipoEvento(), "Tipo evento");
		checkCondition(!model.getCausaleEP().getEventi().isEmpty(), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Lista eventi"));
	}
	
	/**
	 * Il conto deve essere  nelle seguenti condizioni:
	 * <ul>
	 *     <li>
	 *         Esistere. In caso non sia rispettato un controllo inviare il messaggio <code>lt;COR_ERR_0012 Entit&agrave; inesistente
	 *         (&lt;nome entit&agrave;&gt;: 'Il conto finanziario',  &lt;chiave ricerca&gt;: indicare il codice conto con cui si &eacute; effettuata  la ricerca)&gt;</code>
	 *     </li>
	 *     <li>
	 *         Essere valido nell'anno di bilancio. In caso non sia rispettato un controllo inviare il messaggio <code>&lt;COR_ERR_043 Entit&agrave; non completa
	 *         (&lt;nome entit&agrave;&gt;: 'Il conto finanziario ****',  &lt;motivo&gt; : 'non ha un'istanza valida nell'anno di bilancio')&gt;</code>
	 *     </li>
	 *     <li>
	 *         Essere di V livello. In caso non sia rispettato un controllo inviare il messaggio <code>&lt;COR_ERR_043 Entit&agrave; non completa
	 *         (&lt;nome entit&agrave;&gt;: 'Il conto finanziario ****', &lt;motivo&gt;:  '&eacute; di livello ****')&gt;</code>
	 *     </li>
	 * </ul>
	 */
	private void checkContoQuintoLivello() {
		// 0. Devo aver impostato il campo
		checkCondition(model.getElementoPianoDeiConti() != null && StringUtils.isNotBlank(model.getElementoPianoDeiConti().getCodice()),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Codice conto finanziario "), true);
		
		// Ricerca del conto
		LeggiElementoPianoDeiContiByCodiceAndAnno request = model.creaRequestLeggiElementoPianoDeiContiByCodiceAndAnno(model.getElementoPianoDeiConti());
		logServiceRequest(request);
		LeggiElementoPianoDeiContiByCodiceAndAnnoResponse response = classificatoreBilService.leggiElementoPianoDeiContiByCodiceAndAnno(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Se ho errori esco
			addErrori(response);
			throw new ParamValidationException(createErrorInServiceInvocationString(request, response));
		}
		
		// 1. Il conto deve esistere
		checkCondition(response.getElementoPianoDeiConti() != null, ErroreCore.ENTITA_INESISTENTE.getErrore("Il conto finanziario", model.getElementoPianoDeiConti().getCodice()), true);
		
		ElementoPianoDeiConti epdc = response.getElementoPianoDeiConti();
		// 2. Deve essere valido nell'anno di bilancio
		checkCondition(ValidationUtil.isEntitaValidaPerAnnoEsercizio(epdc, model.getAnnoEsercizioInt()),
			ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto finanziario " + epdc.getCodice(), " non ha un'istanza valida nell'anno di bilancio"),
			true);
		// 3. Deve essere di quinto livello
		checkCondition(epdc.getLivello() == 5, ErroreCore.ENTITA_NON_COMPLETA.getErrore("Il conto finanziario " + epdc.getCodice(), " e' di livello " + epdc.getLivello()), true);
		
		// Imposto il piano dei conti reperito
		model.setElementoPianoDeiConti(epdc);
	}
	
	/**
	 * Controlla la validit&agrave; del soggetto.
	 */
	private void checkSoggetto() {
		final String methodName = "checkSoggetto";
		if(model.getSoggetto() == null || StringUtils.isBlank(model.getSoggetto().getCodiceSoggetto())) {
			// Tutto a posto. esco
			return;
		}
		
		Soggetto soggetto = sessionHandler.getParametro(BilSessionParameter.SOGGETTO);
		
		if(soggetto == null || !model.getSoggetto().getCodiceSoggetto().equals(soggetto.getCodiceSoggetto())) {
			log.debug(methodName, "Caricamento dei dati da servizio");
			RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave();
			logServiceRequest(request);
			RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				String errorMsg = createErrorInServiceInvocationString(request, response);
				log.debug(methodName, errorMsg);
				addErrori(response);
				throw new ParamValidationException(errorMsg);
			}
			soggetto = response.getSoggetto();
			
			// Le liste possono tranquillamente essere null. Pertanto, per sicurezza fornisco come default una lista vuota
			List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = defaultingList(response.getListaSecondariaSoggetto());
			List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
			listaModalitaPagamentoSoggetto = impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
			
			// Imposto in sessione
			sessionHandler.setParametro(BilSessionParameter.SOGGETTO, soggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO, listaSedeSecondariaSoggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO, listaModalitaPagamentoSoggetto);
		}
		
		// Controllo l'esistenza del soggetto
		checkCondition(soggetto != null, ErroreCore.ENTITA_INESISTENTE.getErrore("Soggetto", model.getSoggetto().getCodiceSoggetto()), true);
		checkCondition(!StatoOperativoAnagrafica.PROVVISORIO.equals(soggetto.getStatoOperativo()) && !StatoOperativoAnagrafica.ANNULLATO.equals(soggetto.getStatoOperativo()),
			ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("il soggetto", soggetto.getStatoOperativo()));
		model.setSoggetto(soggetto);
	}

	/**
	 * Completamento per lo step 1.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep1() {
		// Cerco il tipo evento nella lista
		TipoEvento tipoEvento = ComparatorUtils.searchByUid(model.getListaTipoEvento(), model.getTipoEvento());
		model.setTipoEvento(tipoEvento);
		// Cerco gli eventi
		for (ListIterator<Evento> li = model.getCausaleEP().getEventi().listIterator(); li.hasNext();) {
			Evento ev = li.next();
			Evento evPopolato = ComparatorUtils.searchByUid(model.getListaEvento(), ev);
			li.set(evPopolato);
		}
		
		popolaCodiceDescrizioneClassificatoriEP();
		
		return SUCCESS;
	}
	
	/**
	 * Popolamento del codice e della descrizione dei classificatori EP
	 */
	private void popolaCodiceDescrizioneClassificatoriEP() {
		ClassificatoreEP classificatoreEP1 = popolaCodiceDescrizioneClassificatoriEP(model.getListaClassificatoreEP1(), model.getClassificatoreEP1());
		ClassificatoreEP classificatoreEP2 = popolaCodiceDescrizioneClassificatoriEP(model.getListaClassificatoreEP2(), model.getClassificatoreEP2());
		ClassificatoreEP classificatoreEP3 = popolaCodiceDescrizioneClassificatoriEP(model.getListaClassificatoreEP3(), model.getClassificatoreEP3());
		ClassificatoreEP classificatoreEP4 = popolaCodiceDescrizioneClassificatoriEP(model.getListaClassificatoreEP4(), model.getClassificatoreEP4());
		ClassificatoreEP classificatoreEP5 = popolaCodiceDescrizioneClassificatoriEP(model.getListaClassificatoreEP5(), model.getClassificatoreEP5());
		ClassificatoreEP classificatoreEP6 = popolaCodiceDescrizioneClassificatoriEP(model.getListaClassificatoreEP6(), model.getClassificatoreEP6());
		
		model.setClassificatoreEP1(classificatoreEP1);
		model.setClassificatoreEP2(classificatoreEP2);
		model.setClassificatoreEP3(classificatoreEP3);
		model.setClassificatoreEP4(classificatoreEP4);
		model.setClassificatoreEP5(classificatoreEP5);
		model.setClassificatoreEP6(classificatoreEP6);
	}

	/**
	 * Popolamento del codice e della descrizione dei classificatori EP.
	 * 
	 * @param list             la lista nella quale effettuare la ricerca
	 * @param classificatoreEP il classificatore da popolare
	 * 
	 * @return il classificatore trovato
	 */
	private ClassificatoreEP popolaCodiceDescrizioneClassificatoriEP(List<ClassificatoreEP> list, ClassificatoreEP classificatoreEP) {
		return ComparatorUtils.searchByUidEventuallyNull(list, classificatoreEP);
	}

	/**
	 * Ritorno allo step 1.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String backToStep1() {
		return SUCCESS;
	}
	
	/**
	 * Annullamento dei dati relativi allo step 2.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String annullaStep2() {
		return SUCCESS;
	}
	
	/**
	 * Ingresso nello step 2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String step2() {
		return SUCCESS;
	}
	
	/**
	 * Validazione per il metodo {@link #completeStep2()}.
	 */
	public void validateCompleteStep2() {
		final String methodName = "validateCompleteStep2";
		checkCausaleIntegrata();
		
		if(BilConstants.TIPO_EVENTO_SCRITTURA_EPILOGATIVA.getConstant().equals(model.getTipoEvento().getCodice())) {
			// Deve essere presente almeno un conto appartenente alla ClasseConto = EP
			int numContiEpilogativa = 0;
			for(ContoTipoOperazione cto : model.getListaContoTipoOperazione()) {
				if(isContoEP(cto.getConto())) {
					numContiEpilogativa++;
				}
			}
			log.debug(methodName, "Numero conti di classe epilogativa: " + numContiEpilogativa);
			checkCondition(numContiEpilogativa >= 1, ErroreCore.ENTITA_NON_COMPLETA.getErrore("La causale", "deve essere presente un conto epilogativo"));
		}
	}


	
	/**
	 * effettua i controlli sulla causale integrata
	 */
	private void checkCausaleIntegrata() {
		final String methodName = "checkCausaleIntegrata";
		
		if(!TipoCausale.Integrata.equals(model.getCausaleEP().getTipoCausale())) {
			//esco se la causale non e' di tipo INT
			log.debug(methodName, "La causale non e' integrata. Salto il controllo.");
			return;
		}
		checkContiCausaleIntegrata();
	}


	/**
	 * Per le le causali DI RACCORDO effettuare  i seguenti controlli.
	 * <br/>
	 * Se l'ambito &egrave; FIN
	 * 	<ul>
	 * 	 <li>Ci siano almeno due conti, uno con TipoOperazione-segnoConto = DARE e uno con TipoOperazione-segnoConto=AVERE; </li>
	 * 	 <li>Se la causale &egrave; associata ad un Soggetto deve essere presente un Conto collegato allo stesso Soggetto </li>
	 * 	 <li>Ci deve essere almeno un conto collegato allo stesso V livello del Piano dei Conti Finanziario della causale,</li>
	 * 	 <li>Occorre controllare che non sia presente nel sistema una causale con lo stesso V livello del PdC finanziario e gli stessi conti economici inseriti a parit&agrave; di eventi </li>
	 * 	</ul>
	 * Se l'ambito &egrave; GSA
	 * 	<ul>
	 * 	 <li>Possono essere presenti n conti </li>
	 * 	 <li>Deve essere possibile inserire n conti ed n classi di conciliazione </li>
	 *   <li>Deve essere possibile inserire n classi di conciliazione </li>
	 * 	 <li>
	 * 	</ul>
	 * 
	 */
	protected void checkContiCausaleIntegrata() {
		final String methodName = "checkContiCausaleIntegrata";
		// E' obbligatorio associare almeno un conto della Classe CE oppure SP (SIAC-2095)
		int numContiCESP = 0;
		// Ci siano almeno due conti, uno con TipoOperazione-segnoConto = DARE e uno con TipoOperazione-segnoConto = AVERE
		int numConti = 0;
		int numContiSegnoDare = 0;
		int numContiSegnoAvere = 0;
		// Se la causale e' associata ad un Soggetto deve essere presente un Conto collegato allo stesso Soggetto
		int numContiStessoSoggetto = 0;
		
		boolean isPresentSoggetto = model.getSoggetto() != null && model.getSoggetto().getUid() != 0;
		int uidSoggetto = model.getSoggetto() != null ? model.getSoggetto().getUid() : -1;
		
		for(ContoTipoOperazione cto : model.getListaContoTipoOperazione()) {
			numConti++;

			if(isContoCE(cto.getConto()) || isContoSP(cto.getConto())) {
				numContiCESP++;
			}
			// Conti DARE
			if(OperazioneSegnoConto.DARE.equals(cto.getOperazioneSegnoConto())) {
				numContiSegnoDare++;
			}
			// Conti AVERE
			if(OperazioneSegnoConto.AVERE.equals(cto.getOperazioneSegnoConto())) {
				numContiSegnoAvere++;
			}
			// Stesso soggetto
			if(cto.getConto() != null && isPresentSoggetto && cto.getConto().getSoggetto() != null && uidSoggetto == cto.getConto().getSoggetto().getUid()) {
				numContiStessoSoggetto++;
			}
		}
		
		log.debug(methodName, "Numero di conti di classe CE oppure SP : " + numContiCESP);
		checkCondition(numContiCESP > 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("conto di classe CE oppure SP "));
		
		log.debug(methodName, "Numero conti: " + numConti + " -- Numero conti con segno DARE: " + numContiSegnoDare + " -- Numero conti con segno AVERE: " + numContiSegnoAvere);
		checkCondition(numConti >= 2 && numContiSegnoDare >= 1 && numContiSegnoAvere >= 1, ErroreGEN.ASSENZA_CONTI_OBBLIGATORI_CAUSALI_DI_RACCORDO.getErrore());
		//Tolto controllo  siac-2095 
		log.debug(methodName, "La causale era collegata a un soggetto? " + isPresentSoggetto + " -- numero conti collegati allo stesso soggetto: " + numContiStessoSoggetto);
		checkCondition(!isPresentSoggetto || numContiStessoSoggetto >= 1, ErroreCore.ENTITA_NON_COMPLETA.getErrore("La causale", "deve essere presente un conto collegato al soggetto della causale"));
	}
	
	/**
	 * Controlla se il conto sia di tipo CE.
	 * 
	 * @param conto il conto da controllare
	 * 
	 * @return <code>true</code> se il conto &eacute; di tipo CO; <code>false</code> altrimenti
	 */
	protected boolean isContoCE(Conto conto) {
		if(conto == null){
			return false;
		}
		return conto.getPianoDeiConti() != null && conto.getPianoDeiConti().getClassePiano() != null
			&& BilConstants.CLASSE_CONTO_COSTI_DI_ESERCIZIO.getConstant().equals(conto.getPianoDeiConti().getClassePiano().getCodice());
	}

	/**
	 * Controlla se il conto sia di tipo SP (ovvero AP oppure PP).
	 * 
	 * @param conto il conto da controllare
	 * 
	 * @return <code>true</code> se il conto &eacute; di tipo SP; <code>false</code> altrimenti
	 */
	protected boolean isContoSP(Conto conto) {
		if(conto == null){
			return false;
		}
		return conto.getPianoDeiConti() != null && conto.getPianoDeiConti().getClassePiano() != null
			&& (
				BilConstants.CLASSE_CONTO_ATTIVO_PATRIMONIALE.getConstant().equals(conto.getPianoDeiConti().getClassePiano().getCodice())
				|| BilConstants.CLASSE_CONTO_PASSIVO_PATRIMONIALE.getConstant().equals(conto.getPianoDeiConti().getClassePiano().getCodice())
			);
	}
	
	/**
	 * Controlla se il conto sia di tipo EP.
	 * 
	 * @param conto il conto da controllare
	 * 
	 * @return <code>true</code> se il conto &eacute; di tipo EP; <code>false</code> altrimenti
	 */
	private boolean isContoEP(Conto conto) {
		return conto.getPianoDeiConti() != null && conto.getPianoDeiConti().getClassePiano() != null
			&& BilConstants.CLASSE_CONTO_EPILOGATIVA.getConstant().equals(conto.getPianoDeiConti().getClassePiano().getCodice());
	}
	
	/**
	 * Completamento per lo step 2.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String completeStep2() {
		// Da implementare correttamente nelle sottoclassi
		return INPUT;
	}
	
	/**
	 * Ingresso nello step 3.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String step3() {
		leggiEventualiInformazioniAzionePrecedente();
		return SUCCESS;
	}
	
	/**
	 * Controlla se l'ambito fornito sia FIN.
	 * 
	 * @param ambito l'ambito da controllare
	 * @return <code>true</code> se l'ambito &eacutE; FIN; <code>false</code> altrimenti
	 */
	private boolean isAmbitoFIN(Ambito ambito) {
		return Ambito.AMBITO_FIN.equals(ambito);
	}
}
