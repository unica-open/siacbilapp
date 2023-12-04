/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.ordinativo;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.core.appender.rewrite.MapRewritePolicy.Mode;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ValidationUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaContoVincolatoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaDisponibilitaCassaContoVincolatoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatori;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatoriResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiSottoContiVincolatiCapitoloBySubdoc;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiSottoContiVincolatiCapitoloBySubdocResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ClassificatoreStipendi;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siacfin2app.frontend.ui.model.ordinativo.GenericEmissioneOrdinativiModel;
import it.csi.siac.siacfin2ser.frontend.webservice.AllegatoAttoService;
import it.csi.siac.siacfin2ser.frontend.webservice.ContoTesoreriaService;
import it.csi.siac.siacfin2ser.frontend.webservice.DocumentoService;
import it.csi.siac.siacfin2ser.frontend.webservice.EmissioneOrdinativiService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreriaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBollo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceBolloResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceCommissioneDocumento;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceCommissioneDocumentoResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElencoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.CodiceBollo;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Distinta;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.CommissioneDocumento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;


/**
 * Classe di action generica per l'emissione di ordinativi.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/11/2014
 * @param <M> la tipizzazione del model
 *
 */
public abstract class GenericEmissioneOrdinativiAction<M extends GenericEmissioneOrdinativiModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -9025530606130414424L;
	
	/** Il nome del Model inserito in sessione per l'incasso */
	public static final String SESSION_MODEL_NAME_INCASSO = "EmissioneOrdinativiIncasso";
	/** Il nome del Model inserito in sessione per il pagamento */
	public static final String SESSION_MODEL_NAME_PAGAMENTO = "EmissioneOrdinativiPagamento";
	
	/** Il valore dell'ancora inserita in sessione per l'incasso */
	public static final String ANCHOR_VALUE_INCASSO = "OP-ENT-emissioneOrdManuale";
	/** Il valore dell'ancora inserita in sessione per il pagamento */
	public static final String ANCHOR_VALUE_PAGAMENTO = "OP-SPE-emissioneOrdManuale";
	
	/** Serviz&icirc; dell'allegato atto */
	@Autowired protected transient AllegatoAttoService allegatoAttoService;
	/** Serviz&icirc; dei classificatori bil */
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	/** Serviz&icirc; dell'emissione ordinativi */
	@Autowired protected transient EmissioneOrdinativiService emissioneOrdinativiService;

	
	@Autowired protected transient ContoTesoreriaService contoTesoreriaService;
	/** Serviz&icirc; generici */
	@Autowired protected transient GenericService genericService;

	/** documento */
	@Autowired  private transient DocumentoService documentoService;
	/** Serviz&icirc; del provvedimento */
	@Autowired protected transient ProvvedimentoService provvedimentoService;
	/** Serviz&icirc; del soggetto */
	@Autowired private transient SoggettoService soggettoService;
	//SIAC-8017-CMTO
	@Autowired private transient CapitoloService capitoloService;
	
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
	public String execute() throws Exception {
		final String methodName = "execute";
		
		checkCasoDUsoApplicabile(model.getTitolo());
		
		// Caricamento liste
		try {
			caricamentoListe();
		} catch(WebServiceInvocationFailureException wsife) {
			log.error(methodName, "Fallimento nell'invocazione del servizio: " + wsife.getMessage());
			// Esco mostrando l'errore a video
			throw new GenericFrontEndMessagesException(wsife.getMessage(), GenericFrontEndMessagesException.Level.ERROR);
		}
		return SUCCESS;
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioCompatibile = FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio)
				|| FaseBilancio.GESTIONE.equals(faseBilancio)
				|| FaseBilancio.ASSESTAMENTO.equals(faseBilancio)
				|| FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio);
		
		if(!faseDiBilancioCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}

	/**
	 * Caricamento delle liste per la visualizzazione della pagina.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricamentoListe() throws WebServiceInvocationFailureException {
		caricaListaTipoAtto();
		caricaListaStatoOperativoElencoDocumenti();
		caricaListaTipoFinanziamento();
		caricaListaClassiSoggetto();
		caricamentoDistinta();
		//task-12
		caricaListaContoTesoreria();
	}
	
	/**
	 * Caricamento delle della distinta.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	protected abstract void caricamentoDistinta() throws WebServiceInvocationFailureException;

	/**
	 * Carica la lista dei tipi di atto.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaListaTipoAtto() throws WebServiceInvocationFailureException {
		List<TipoAtto> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		if(listaInSessione == null) {
			TipiProvvedimento request = model.creaRequestTipiProvvedimento();
			logServiceRequest(request);
			TipiProvvedimentoResponse response = provvedimentoService.getTipiProvvedimento(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(TipiProvvedimento.class, response));
			}
			
			listaInSessione = response.getElencoTipi();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, listaInSessione);
		}
		
		model.setListaTipoAtto(listaInSessione);
	}

	/**
	 * Carica la lista degli stati operativi dell'elenco documenti.
	 */
	private void caricaListaStatoOperativoElencoDocumenti() {
		model.setListaStatoOperativoElencoDocumenti(Arrays.asList(StatoOperativoElencoDocumenti.values()));
	}

	/**
	 * Carica la lista dei tipi finanziamento del capitolo.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaListaTipoFinanziamento() throws WebServiceInvocationFailureException {
		List<TipoFinanziamento> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO);
		if(listaInSessione == null) {
			LeggiClassificatoriGenericiByTipoElementoBil request = model.creaRequestLeggiClassificatoriGenericiByTipoElementoBil(BilConstants.CODICE_CAPITOLO_ENTRATA_GESTIONE.getConstant());
			logServiceRequest(request);
			LeggiClassificatoriGenericiByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriGenericiByTipoElementoBil(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(LeggiClassificatoriGenericiByTipoElementoBil.class, response));
			}
			
			listaInSessione = response.getClassificatoriTipoFinanziamento();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO, listaInSessione);
		}
		
		model.setListaTipiFinanziamento(listaInSessione);
	}

	/**
	 * Carica la lista delle classi del soggetto.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	private void caricaListaClassiSoggetto() throws WebServiceInvocationFailureException {
		List<CodificaFin> listaClassiSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaClassiSoggetto == null) {
			ListeGestioneSoggetto request = model.creaRequestListeGestioneSoggetto();
			logServiceRequest(request);
			ListeGestioneSoggettoResponse response = soggettoService.listeGestioneSoggetto(request);
			logServiceResponse(response);
			// Controllo gli errori
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(ListeGestioneSoggetto.class, response));
			}
			listaClassiSoggetto = response.getListaClasseSoggetto();
			ComparatorUtils.sortByCodiceFin(listaClassiSoggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaClassiSoggetto);
		}
		model.setListaClasseSoggetto(listaClassiSoggetto);
	}

	/**
	 * Carica la lista dei Conti Tesoreria.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	
	protected void caricaListaContoTesoreria() throws WebServiceInvocationFailureException {
		List<ContoTesoreria> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_CONTO_TESORERIA);
		if(listaInSessione == null) {
			LeggiContiTesoreria request = model.creaRequestLeggiContiTesoreria();
			logServiceRequest(request);
			LeggiContiTesoreriaResponse response = contoTesoreriaService.leggiContiTesoreria(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(LeggiContiTesoreria.class, response));
			}
			
			listaInSessione = response.getContiTesoreria();
			sessionHandler.setParametro(BilSessionParameter.LISTA_CONTO_TESORERIA, listaInSessione);
		}
		
		model.setListaContoTesoreria(listaInSessione);
	}
	
	/**
	 * Caricamento delle date di scadenza
	 */
	protected void caricaDataScadenza() {
		Date dataScadenza = null;
		
		boolean elencoPopolato = model.getElencoDocumentiAllegato() != null
				&& ValidationUtil.isIntegerValid(model.getElencoDocumentiAllegato().getAnno())
				&& (ValidationUtil.isIntegerValid(model.getElencoDocumentiAllegato().getNumero())
						|| ValidationUtil.isIntegerValid(model.getNumeroElencoDa()));
		
		boolean attoPopolato = model.getAttoAmministrativo() != null
				&& model.getAttoAmministrativo().getAnno() != 0
				&& (model.getAttoAmministrativo().getNumero() != 0 || (model.getTipoAtto() != null && model.getTipoAtto().getUid() != 0));

		if(elencoPopolato){
			dataScadenza = caricaDataScadenzaDaElenco();
		}
		//se non ho trovato una data di scadenza collegata all'atto
		if(dataScadenza == null && attoPopolato ){
			dataScadenza = caricaDataScadenzaDaProvvedimento();
		}
		
		model.setDataScadenza(dataScadenza);
	}
	
	
	private Date caricaDataScadenzaDaProvvedimento() {
		final String  methodName ="caricaDataScadenzaDaElenco";
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento(StatoOperativoAtti.DEFINITIVO.name());
		logServiceRequest(request);
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.			
			log.debug(methodName, "errore in ricercaProvvedimento");
			return null;
		}		

		List<AttoAmministrativo> listaAttoAmministrativo = response.getListaAttiAmministrativi();		
		if(listaAttoAmministrativo.isEmpty() || listaAttoAmministrativo.size() != 1 ){
			log.debug(methodName, "listaAttoAmministrativo non univoco");
			return null;			
		}
		
		// Ho un unico atto
		AttoAmministrativo attoAmministrativo = listaAttoAmministrativo.get(0);
		if (!StatoOperativoAtti.DEFINITIVO.name().equals(attoAmministrativo.getStatoOperativo())){
			log.debug(methodName, "stato atto amministrativo non definitivo");
			return null;
		}
				
		AllegatoAtto allegatoAtto = attoAmministrativo.getAllegatoAtto();
		if(allegatoAtto == null){
			log.debug(methodName, "allegato atto non esistente");
			return null;
		}
		
		if(!(StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto())|| StatoOperativoAllegatoAtto.CONVALIDATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto()))){
			log.debug(methodName, "OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA");
			return null;
		}
		return allegatoAtto.getDataScadenza();
	}

	/**
	 * 
	 * @return Date
	 */
	private Date caricaDataScadenzaDaElenco() {
		final String  methodName ="caricaDataScadenzaDaElenco";
		RicercaElenco rre = model.creaRequestRicercaElenco();
		RicercaElencoResponse rreres = allegatoAttoService.ricercaElenco(rre);
		
		
		if(rreres.hasErrori()) {
			// Ho un errore nel servizio: fornisco l'errore ed esco
			log.info(methodName, createErrorInServiceInvocationString(RicercaElenco.class, rreres));
			addErrori(rreres);
			return null;
		}

		ListaPaginata<ElencoDocumentiAllegato> listaAllegatiAtti = rreres.getElenchiDocumentiAllegato();
		if (listaAllegatiAtti == null || rreres.getTotaleElementi()!= 1){
			log.debug(methodName, "Numero di elenchi che corrispondono ai criteri di ricerca incompatibile con il settaggio della data scadenza. Numero necessario:1 , elementi trovati: " + rreres.getTotaleElementi() );
			return null;
		}
		
		ElencoDocumentiAllegato elencoAllegatoAtto = listaAllegatiAtti.get(0);
		return elencoAllegatoAtto != null && elencoAllegatoAtto.getAllegatoAtto() != null? elencoAllegatoAtto.getAllegatoAtto().getDataScadenza() : null;
	}

	/**
	 * Effettua un controllo sull'atto amministrativo.
	 */

	protected void checkAttoAmministrativo() {
		checkNotNull(model.getAttoAmministrativo(), "Provvedimento", true);
		checkCondition(model.getAttoAmministrativo().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno provvedimento"));
		checkCondition(model.getAttoAmministrativo().getNumero() != 0
				|| (model.getTipoAtto() != null && model.getTipoAtto().getUid() != 0),
			ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Numero o tipo provvedimento"));
		if(model.getTipoAtto() != null && model.getTipoAtto().getUid() != 0) {
			// Caricamento del tipo atto per uid
			List<TipoAtto> list = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
			TipoAtto tipoAtto = ComparatorUtils.searchByUid(list, model.getTipoAtto());
			model.setTipoAtto(tipoAtto);
		}
		if(model.getStrutturaAmministrativoContabile() != null && model.getStrutturaAmministrativoContabile().getUid() != 0) {
			// Caricamento della struttura per uid
			List<StrutturaAmministrativoContabile> list = sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
			StrutturaAmministrativoContabile strutturaAmministrativoContabile = ComparatorUtils.searchByUid(list, model.getStrutturaAmministrativoContabile());
			model.setStrutturaAmministrativoContabile(strutturaAmministrativoContabile);
		}		
	}

	
	/**
	 * Effettua un controllo sull'atto amministrativo.
	 * 
	 * @param statoOperativo lo stato per cui eventualmente effettuare la ricerca
	 */
	protected void checkAttoAmministrativo(String statoOperativo) {
		final String methodName = "checkAttoAmministrativo";
		
		checkNotNull(model.getAttoAmministrativo(), "Provvedimento", true);
		checkCondition(model.getAttoAmministrativo().getAnno() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Anno provvedimento"));
		checkCondition(model.getAttoAmministrativo().getNumero() != 0
				|| (model.getTipoAtto() != null && model.getTipoAtto().getUid() != 0),
			ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("Numero o tipo provvedimento"));
		
		if(hasErrori()) {
			// Esco
			return;
		}
		
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento(statoOperativo);
		logServiceRequest(request);
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaProvvedimento.class, response));
			addErrori(response);
			return;
		}
		List<AttoAmministrativo> list = response.getListaAttiAmministrativi();
		checkCondition(!list.isEmpty(), ErroreCore.NESSUN_DATO_REPERITO.getErrore(), true);
		checkUnicoAttoAmministrativo(response.getListaAttiAmministrativi(), model.getStrutturaAmministrativoContabile(), true);
		// Ho un unico atto
		AttoAmministrativo attoAmministrativo = list.get(0);
		checkCondition(StatoOperativoAtti.DEFINITIVO.name().equals(attoAmministrativo.getStatoOperativo()),
				ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Provvedimento", attoAmministrativo.getStatoOperativo()), true);
		AllegatoAtto allegatoAtto = attoAmministrativo.getAllegatoAtto();
		checkCondition(allegatoAtto != null, ErroreCore.ENTITA_INESISTENTE.getErrore("Allegato all'atto " , attoAmministrativo.getAnno() + "/" + attoAmministrativo.getNumero()), true);
		
		checkCondition(StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto())
				|| StatoOperativoAllegatoAtto.CONVALIDATO.equals(allegatoAtto.getStatoOperativoAllegatoAtto()),
			ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("Allegato all'atto", allegatoAtto.getStatoOperativoAllegatoAtto()), true);
		
		// Imposto i dati nel model
		model.setAttoAmministrativo(attoAmministrativo);
		model.setTipoAtto(attoAmministrativo.getTipoAtto());
		model.setStrutturaAmministrativoContabile(attoAmministrativo.getStrutturaAmmContabile());
		model.setAllegatoAtto(allegatoAtto);
		model.setDataScadenza(allegatoAtto.getDataScadenza());
		
	}
	
	/**
	 * Validazione per il capitolo.
	 * 
	 * @param capitolo il capitolo da validare
	 */
	protected void checkCapitolo(Capitolo<?, ?> capitolo) {
		checkCondition(capitolo != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Capitolo"), true);
		checkCondition(capitolo.getAnnoCapitolo() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Anno capitolo"));
		checkCondition(capitolo.getNumeroCapitolo() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero capitolo"));
		checkCondition(capitolo.getNumeroArticolo() != null, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Numero articolo"));
		checkCondition(capitolo.getNumeroUEB() == null || capitolo.getNumeroUEB().intValue() > 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Numero UEB", "deve essere positivo"));
	}
	
	/**
	 * Effettua un controllo sull'elenco.
	 */
	protected void checkElenco() {
		ElencoDocumentiAllegato eda = model.getElencoDocumentiAllegato();
		Integer numeroDa = model.getNumeroElencoDa();
		Integer numeroA = model.getNumeroElencoA();
		
		if(eda == null) {
			// Inizializzo l'elenco
			eda = new ElencoDocumentiAllegato();
			model.setElencoDocumentiAllegato(eda);
		}
		
		Date now = new Date();
		Integer currentYear = computeYearFromDate(now);
		
		checkCondition(eda.getAnno() == null || eda.getAnno().compareTo(currentYear) <= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno", ": non deve essere successivo all'anno corrente"));
		checkCondition(numeroDa == null || numeroDa.intValue() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Numero da", ": deve essere positivo"));
		checkCondition(numeroA == null || numeroA.intValue() > 0, ErroreCore.FORMATO_NON_VALIDO.getErrore("Numero a", ": deve essere positivo"));
		
		// Se specificato anno e' obbligatorio anche numero del relativo elenco
		checkCondition(eda.getAnno() == null || (numeroDa != null || numeroA != null),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("se specificato anno e' obbligatorio anche numero del relativo elenco"));
		
		// Se specificato intervallo numero verificare che Elenco Da <= Elenco A
		checkCondition(numeroA == null || numeroDa == null || numeroDa.compareTo(numeroA) <= 0,
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Numeri elenco", ": il numero iniziale non puo' essere successivo al numero finale"));
	}
	
	/**
	 * Validazione per il soggetto
	 */
	protected void checkSoggetto() {
		final String methodName = "checkSoggetto";
		
		checkCondition(model.getSoggetto() != null && StringUtils.isNotBlank(model.getSoggetto().getCodiceSoggetto()),
				ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("codice soggetto"));
		if(hasErrori()) {
			// Se ho errori esco
			return;
		}
		
		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave();
		logServiceRequest(request);
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaSoggettoPerChiave.class, response));
			addErrori(response);
			return;
		}
		Soggetto soggetto = response.getSoggetto();
		if(soggetto == null) {
			// Nessun soggetto trovato: loggo, segnalo ed esco
			log.debug(methodName, "Nessun soggetto disponibile per codice " + request.getParametroSoggettoK().getCodice());
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", "codice " + request.getParametroSoggettoK().getCodice()));
			return;
		}
		// Soggetto trovato
		log.debug(methodName, "Soggetto con codice " + soggetto.getCodiceSoggetto() + " trovato con uid " + soggetto.getUid()
				+ ". StatoOperativoAnagrafica: " + soggetto.getStatoOperativo());
		model.setSoggetto(soggetto);
	}
	
	/**
	 * Controllo per la distinta.
	 */
	protected void checkDistinta() {
		//distinta criterio ricerca
		if(model.getDistintaDaCercare() != null && model.getDistintaDaCercare().getUid() != 0) {
			// Creo la codifica fin di dato uid
			CodificaFin distintaAsCodificaFin = new CodificaFin();
			distintaAsCodificaFin.setUid(model.getDistintaDaCercare().getUid());
			// Caricamento della codifica per uid
			List<CodificaFin> list = sessionHandler.getParametro(BilSessionParameter.LISTA_DISTINTA);
			distintaAsCodificaFin = ComparatorUtils.searchByUid(list, distintaAsCodificaFin);
			// Creazione della codifica originale
			Distinta distinta = new Distinta();
			
			distinta.setCodice(distintaAsCodificaFin.getCodice());
			distinta.setDescrizione(distintaAsCodificaFin.getDescrizione());
			distinta.setUid(distintaAsCodificaFin.getUid());
			model.setDistintaDaCercare(distinta);
		}
	}
	
	/**
	 * Computa l'anno corrispondente alla data fornita.
	 * 
	 * @param date la data di cui calcolare l'anno
	 * 
	 * @return l'anno della data
	 */
	private Integer computeYearFromDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return Integer.valueOf(cal.get(Calendar.YEAR));
	}
	
	/**
	 * Somma i bigDecimal forniti.
	 * 
	 * @param first  il primo addendo
	 * @param second il secondo addendo
	 * 
	 * @return la somma degli addendi
	 */
	protected BigDecimal sumBigDecimals(BigDecimal first, BigDecimal second) {
		return sumBigDecimals(first, second, BigDecimal.ZERO);
	}
	
	/**
	 * Somma i bigDecimal forniti.
	 * 
	 * @param first        il primo addendo
	 * @param second       il secondo addendo
	 * @param defaultValue il valore di default nel caso gli addendi siano null
	 * 
	 * @return la somma degli addendi
	 */
	protected BigDecimal sumBigDecimals(BigDecimal first, BigDecimal second, BigDecimal defaultValue) {
		BigDecimal result = first == null ? defaultValue : first;
		return result.add(second == null ? defaultValue : second);
	}
	
	/**
	 * Caricamento della lista bollo.
	 */
	protected void caricaListaBollo() {
		List<CodiceBollo> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_CODICE_BOLLO);
		if(listaInSessione == null) {
			RicercaCodiceBollo request = model.creaRequestRicercaCodiceBollo();
			RicercaCodiceBolloResponse response = documentoService.ricercaCodiceBollo(request);
			if(!response.hasErrori()) {
				model.setListaBollo(response.getElencoCodiciBollo());
				listaInSessione = response.getElencoCodiciBollo();
				sessionHandler.setParametro(BilSessionParameter.LISTA_CODICE_BOLLO, listaInSessione);
			}  

		}
		model.setListaBollo(listaInSessione);
		//se ho soltanto un elemento lo preimposto
		if(model.getListaBollo() !=null && !model.getListaBollo().isEmpty() && model.getListaBollo().size()== 1){
			model.setCodiceBollo(model.getListaBollo().get(0));
		}
	}
	
	/**
	 * task-291
	 * caricamento della lista commissioni documento
	 */
	public void caricaListaCommissioniDocumento() {
		List<CommissioneDocumento> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_CODICE_COMMISSIONI_DOCUMENTO);
		if(listaInSessione == null) {
			RicercaCodiceCommissioneDocumento request = model.creaRequestRicercaCodiceCommissioneDocumento();
			RicercaCodiceCommissioneDocumentoResponse response = documentoService.ricercaCodiceCommissioneDocumento(request);
			if(!response.hasErrori()) {
				model.setListaCommissioniDocumento(response.getElencoCodiciCommissioneDocumento());
				listaInSessione = response.getElencoCodiciCommissioneDocumento();
				sessionHandler.setParametro(BilSessionParameter.LISTA_CODICE_COMMISSIONI_DOCUMENTO, listaInSessione);
			}  

		}
		model.setListaCommissioniDocumento(listaInSessione);
		//se ho soltanto un elemento lo preimposto
		if(model.getListaCommissioniDocumento() != null && !model.getListaCommissioniDocumento().isEmpty() && model.getListaCommissioniDocumento().size() == 1){
			model.setCommissioneDocumento(model.getListaCommissioniDocumento().get(0));
		}
	}
	
	/**
	 * Controlla la data di scadenza.
	 * @param nomeData il nome della data
	 * @param nomeNoData il nome del campo 'senza data'
	 */
	protected void checkDataScadenza(String nomeData, String nomeNoData) {
		if(!Boolean.TRUE.equals(model.isFlagNoDataScadenza())){
			if(model.getDataScadenza() == null){
				return;
			}
			
			
			Date dataOdierna = model.getDataOdierna();
			Calendar calendarDataOdierna = Calendar.getInstance();
			int annoBilancio = model.getBilancio().getAnno();
			
			calendarDataOdierna.setTime(dataOdierna);
			int year = calendarDataOdierna.get(Calendar.YEAR);
			// SIAC-5711: se l'anno di sistema e' MAGGIORE dell'anno di bilancio, lancio un errore
			if(year > annoBilancio) {
				addErrore(ErroreCore.FORMATO_NON_VALIDO.getErrore("'" + nomeData + "'", ": non puo' essere impostato in quanto l'anno di sistema e' successivo all'anno di bilancio. "
						+ "Si prega di selezionare il campo '" + nomeNoData + "'"));
				return;
			}
			Calendar calendarFineAnno = Calendar.getInstance();
			// SIAC-5711: la data deve essere fino a fine anno (31/12/<anno>, ore 23:59:59
			calendarFineAnno.set(annoBilancio, Calendar.DECEMBER, 31, 23, 59, 59);
			calendarFineAnno.set(Calendar.MILLISECOND, 999);
			Date fineAnno = calendarFineAnno.getTime();
			

			Calendar calendarDataScadenza = Calendar.getInstance();
			calendarDataScadenza.setTime(model.getDataScadenza());
			//SIAC-6970
			checkCondition((DateUtils.truncatedEquals(calendarDataOdierna, calendarDataScadenza, Calendar.DATE) || dataOdierna.before(model.getDataScadenza())) && fineAnno.after(model.getDataScadenza()), 
					ErroreCore.FORMATO_NON_VALIDO.getErrore("'" + nomeData + "'", ": deve essere compresa tra la data attuale e la fine dell'anno in corso."));
		}else{
			checkCondition(model.getDataScadenza() == null , ErroreCore.FORMATO_NON_VALIDO.getErrore("Data e Nessuna Data", ": se si valorizza il campo Nessuna Data il campo Data non va valorizzato"));			
		}
		
	}
	
	/**
	 * Popolamento della Struttura Amministrativo Contabile
	 */
	protected void popolaStrutturaAmministrativoContabile() {
		final String methodName = "popolaStrutturaAmministrativoContabile";
		if(model.getStrutturaAmministrativoContabile() == null || model.getStrutturaAmministrativoContabile().getUid() == 0) {
			log.debug(methodName, "SAC non popolata");
			return;
		}
		List<StrutturaAmministrativoContabile> listaSAC = sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		StrutturaAmministrativoContabile sac = ComparatorUtils.searchByUidWithChildren(listaSAC, model.getStrutturaAmministrativoContabile());
		checkNotNullNorInvalidUid(sac, "Struttura amministrativo contabile");
		if(sac != null) {
			model.setStrutturaAmministrativoContabile(sac);
		}
	}
	
	/**
	 * Ottieni ids elementi elaborati.
	 *
	 * @return the string
	 */
	public String ottieniIdsElementiElaborati() {
		return SUCCESS;
	}
	
	/**
	 * Prepare back to step 1.
	 */
	public void prepareBackToStep1() {
//		model.setTipoEmissioneOrdinativo(null);
		if(model.getAttoAmministrativo() != null && model.getAttoAmministrativo().getAnno() == 0 && model.getAttoAmministrativo().getNumero() == 0) {
			model.setAttoAmministrativo(null);
		}
//		model.setAttoAmministrativo(null);
//		model.setTipoAtto(null);
//		model.setStrutturaAmministrativoContabile(null);
//		model.setElencoDocumentiAllegato(null);
//		model.setNumeroElencoDa(null);
//		model.setNumeroElencoA(null);
//		model.setSoggetto(null);
		model.setDistinta(null);
		model.setCodiceBollo(null);
		
		model.setAllegatoAtto(null);
		model.setNumeroElenchi(null);
		model.setTotaleQuote(null);
		model.setNumeroQuote(null);
		model.setParametriRicerca(null);
		model.setNota(null);
		model.setContoTesoreria(null);
		model.setDataScadenza(null);
		model.setFlagNoDataScadenza(null);
		model.setCommissioneDocumento(null);
		model.setIdOperazioneAsincrona(null);
		model.setTotaleElenchi(null);
		//SIAC-6175
		model.setFlagDaTrasmettere(null);
		//SIAC-6206
		model.setClassificatoreStipendi(null);
	}
	
	/**
	 * Back to step 1.
	 *
	 * @return the string
	 */
	public String backToStep1() {
		return back();
	}
	
	//SIAC-6206
	/**
	 * Caricamento delle liste classificatori stipendi
	 * @throws WebServiceInvocationFailureException in caso di eccezione nell'invocazione del servizio
	 */
	protected void caricaListaClassificatoreStipendi() throws WebServiceInvocationFailureException {
		//CR-4483
		List<ClassificatoreStipendi> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSIFICATORE_STIPENDI);
		if(listaInSessione == null) {
			LeggiClassificatoriByTipologieClassificatori request = model.creaRequestLeggiClassificatoriByTipologieClassificatori();
			logServiceRequest(request);
			LeggiClassificatoriByTipologieClassificatoriResponse response = classificatoreBilService.leggiClassificatoriByTipologieClassificatori(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaContoCorrente");
			}
			
			listaInSessione = response.extractByClass(ClassificatoreStipendi.class);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSIFICATORE_STIPENDI, listaInSessione);
		}
		impostaClassificatoreStipendiFiltrati(listaInSessione);
	}

	/**
	 * Imposta classificatore stipendi filtrati.
	 *
	 * @param listaClassificatori the lista da filtrare
	 */
	protected void impostaClassificatoreStipendiFiltrati(List<ClassificatoreStipendi> listaClassificatori) {
		//DA IMPLEMENTARE NELLE SOTTO CLASSI
	}
	
	public void validateControllaDisponibilitaSottoConto() {
		checkNotNull(model.getContoTesoreria(), "conto");
	}
	
	//SIAC-8017-CMTO
	/**
	 * Controlla disponibilita di cassa capitoli.
	 *
	 * @return the string
	 */
	public String controllaDisponibilitaSottoConto() {
		cleanErroriMessaggiInformazioni();
		ControllaDisponibilitaCassaContoVincolatoCapitolo req = model.creaRequestControllaDisponibilitaCassaContoVincolato();
		ControllaDisponibilitaCassaContoVincolatoCapitoloResponse res= capitoloService.controllaDisponibilitaCassaContoVincolatoCapitolo(req);
		if(res.hasErrori()) {
			addErrori(res);
			return SUCCESS;
		}
		addMessaggi(res.getMessaggi());
		return SUCCESS;
	}
	
//	public void prepareOttieniUidContoDaSelezionare() {
//		model.setUidsSubdocumentiSelezionati(null);
//	}
	
	public void validateOttieniUidContoDaSelezionare() {
		checkCondition(model.getUidsSubdocumentiSelezionati() != null && !model.getUidsSubdocumentiSelezionati().isEmpty(), ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("nessun subdocumento selezionato."));
	}
	
	//SIAC-8784
	public String ottieniUidContoDaSelezionare() {
		LeggiSottoContiVincolatiCapitoloBySubdoc req = model.creaRequestCaricaSottoContiVincolatiCapitolo();
		
		LeggiSottoContiVincolatiCapitoloBySubdocResponse response = capitoloService.leggiSottoContiVincolatiCapitoloBySubdocService(req);
		if(response.hasErrori()) {
			addErrori(response);
			return SUCCESS;
		}
		if(response.getListaIdsContoTesoreria() != null && !response.getListaIdsContoTesoreria().isEmpty()) {
			model.setUidContoDaSelezionare(response.getListaIdsContoTesoreria().get(0));
		}
		return SUCCESS;
	}
}
