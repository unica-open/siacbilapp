/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.causale;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.ProvvedimentoService;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.TipiProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.GenericFrontEndMessagesException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Entita;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.causale.GenericCausaleModel;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Action astratta per la gestione della Causale.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 04/02/2014
 *
 * @param <M> la parametrizzazione del Model
 */
public abstract class GenericCausaleAction<M extends GenericCausaleModel> extends GenericBilancioAction<M> {
	/** Per la serializzazione */
	private static final long serialVersionUID = 2251780291217335107L;
	
	/** Serviz&icirc; del provvedimento */
	@Autowired protected transient ProvvedimentoService provvedimentoService;
	/** Serviz&icirc; del soggetto */
	@Autowired protected transient SoggettoService soggettoService;
	/** Serviz&icirc; dei classificatori bil */
	@Autowired private transient ClassificatoreBilService classificatoreBilService;
	
	/** Risultato corrispondente all'aggiornamento */
	protected static final String AGGIORNA = "aggiorna";
	/** Risultato corrispondente alla ricerca */
	protected static final String RICERCA = "ricerca";

	/* Metodi di utilita' */
	/**
	 * Carica la lista dei tipi di atto 
	 * 
	 */
	public void caricaListaTipiAtto() {
		final String methodName = "caricaListaTipiAtto";
		log.debug(methodName, "Caricamento della lista dei tipi di atto");
		
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		
		if(listaTipoAtto == null) {
			TipiProvvedimento request = model.creaRequestTipiProvvedimento();
			log.debug(methodName, "Richiamo il WebService di caricamento dei Tipi di Provvedimento");
			TipiProvvedimentoResponse response = provvedimentoService.getTipiProvvedimento(request);
			log.debug(methodName, "Richiamato il WebService di caricamento dei Tipi di Provvedimento");
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				log.info(methodName, "Errore nell'invocazione del metodo");
				listaTipoAtto = new ArrayList<TipoAtto>();
			} else {
				listaTipoAtto = response.getElencoTipi();
				log.info(methodName, "listaTipoAtto.size:"+listaTipoAtto.size());
			}
			ComparatorUtils.sortByCodice(listaTipoAtto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, listaTipoAtto);
		}
		
		model.setListaTipoAtto(listaTipoAtto);
	}
	
	/**
	 * Carica la lista stati operativi della causale (per ora necessaria solo alla ricerca)
	 */
	protected void caricaListaStati() {
		final String methodName = "caricaListaStati";
		log.debug(methodName, "Caricamento della lista degli stati operativi della Causale");
		
		List<StatoOperativoCausale> listaStato = Arrays.asList(StatoOperativoCausale.values());
		model.setListaStatiCausale(listaStato);
	}
	
	/**
	 * Carica le liste di Sede Secondaria e Modalita Pagamento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaSedeSecondariaEModalitaPagamento() {
		caricaListeSedeSecondariaModalitaPagamentoDaSessione();
		return SUCCESS;
	}
	
	/**
	 * Carica la lista delle sedi secondarie e delle modalit&agrave; di pagamento a partire dal soggetto selezionato, se presente.
	 */
	protected void caricaListaSedeSecondariaSoggettoEModalitaPagamentoSoggetto() {
		final String methodName = "caricaListaSedeSecondariaSoggettoEModalitaPagamentoSoggetto";
		// Se il soggetto non è presente o non ha il codice valorizzato non carico nulla
		Soggetto soggetto = model.getSoggetto();
		if(soggetto == null || StringUtils.isBlank(soggetto.getCodiceSoggetto())) {
			return;
		}
		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave(soggetto);
		logServiceRequest(request);
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		logServiceResponse(response);
		
		// Se ho errori, esco subito dopo aver caricato gli errori
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return;
		}
		
		if(response.getSoggetto() == null) {
			log.info(methodName, "Nessun soggetto fornito dal servizio");
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Il soggetto non e' presente"));
			return;
		}
		
		// Aggiorno i dati del soggetto
		model.setSoggetto(response.getSoggetto());
		
		List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = defaultingList(response.getListaSecondariaSoggetto());
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
		listaModalitaPagamentoSoggetto = impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
		
		model.setListaSedeSecondariaSoggetto(listaSedeSecondariaSoggetto);
		model.setListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
		// Carico i dati in sessione
		sessionHandler.setParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO, listaSedeSecondariaSoggetto);
		sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO, listaModalitaPagamentoSoggetto);
	}

	/**
	 * Carica dalla sessione le liste della Sede Secondaria e della Modalita Pagamento.
	 */
	private void caricaListeSedeSecondariaModalitaPagamentoDaSessione() {
		List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO);
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO);
		
		model.setListaSedeSecondariaSoggetto(listaSedeSecondariaSoggetto);
		model.setListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
	}
	
	/**
	 * Carica la lista delle classi del soggetto. VEDERE SE SERVE
	 */
	protected void caricaListaClasseSoggetto() {
		List<CodificaFin> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaInSessione == null) {
			ListeGestioneSoggetto request = model.creaRequestListeGestioneSoggetto();
			ListeGestioneSoggettoResponse response = soggettoService.listeGestioneSoggetto(request);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				return;
			}
			
			listaInSessione = response.getListaClasseSoggetto();
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaInSessione);
		}
		
		model.setListaClasseSoggetto(listaInSessione);
	}
	
	/**
	 * Effettua una validazione del Soggetto fornito in input.
	 */
	protected void validazioneSoggetto() {
		final String methodName = "validazioneSoggetto";
		Soggetto soggetto = model.getSoggetto();
		// Se non ho il soggetto, sono a posto
		if(soggetto == null || StringUtils.isBlank(soggetto.getCodiceSoggetto())) {
			return;
		}
		
		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave(soggetto);
		logServiceRequest(request);
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		logServiceResponse(response);
		
		// Se ho errori, esco subito dopo aver caricato gli errori
		if(response.hasErrori()) {
			addErrori(response);
			return;
		}
		
		if(response.isFallimento() || response.getSoggetto() == null) {
			log.info(methodName, "Nessun soggetto fornito dal servizio");
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Il soggetto non e' presente"));
			return;
		}
		
		soggetto = response.getSoggetto();
		// Aggiorno i dati del soggetto
		model.setSoggetto(soggetto);
		
		// Imposto anche le liste di MPS e SSS
		List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = defaultingList(response.getListaSecondariaSoggetto());
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
		listaModalitaPagamentoSoggetto = impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
		
		model.setListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
		model.setListaSedeSecondariaSoggetto(listaSedeSecondariaSoggetto);
		sessionHandler.setParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO, listaSedeSecondariaSoggetto);
		sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO, listaModalitaPagamentoSoggetto);
		
		checkCondition(!StatoOperativoAnagrafica.ANNULLATO.equals(soggetto.getStatoOperativo()), ErroreFin.SOGGETTO_ANNULLATO.getErrore());
		checkCondition(!StatoOperativoAnagrafica.BLOCCATO.equals(soggetto.getStatoOperativo()), ErroreFin.SOGGETTO_BLOCCATO.getErrore());
	}

	/**
	 * Effettua una validazione dell'atto amministrativo fornito in input.
	 */
	protected void validazioneAttoAmministrativo() {
		// Se non ho l'atto amministrativo sono a posto
		if(!checkProvvedimentoEsistente()) {
			return;
		}
		
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento();
		logServiceRequest(request);
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return;
		}
		
		// Il provvedimento deve esistere
		if(response.getListaAttiAmministrativi().isEmpty()) {
			addErrore(ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore());
			return;
		}
		
		// Deve esistere un unico provvedimento
		checkUnicoAttoAmministrativo(response.getListaAttiAmministrativi(), model.getStrutturaAmministrativoContabileAttoAmministrativo(), false);
		
		model.setAttoAmministrativo(response.getListaAttiAmministrativi().get(0));
	}
	
	
	/**
	 * Se l'atto amministrativo collegato al preDocumento &eacute; presente, ne carica i dati.
	 * 
	 * @return l'atto amministrativo, se presente; <code>null</code> nel caso non vi siano atto da caricare
	 * 
	 * @throws GenericFrontEndMessagesException nel caso in cui la ricerca dell'Atto non vada a buon fine
	 */
	protected AttoAmministrativo caricaAttoAmministrativoSePresente() {
		final String methodName = "caricaAttoAmministrativoSePresente";
		AttoAmministrativo aa = model.getAttoAmministrativo();
		// Se non ho l'atto, non ho alcun problema
		if(aa == null || aa.getUid() == 0) {
			return null;
		}
		
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento();
		logServiceRequest(request);
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.error(methodName, "Errore nell'invocazione del servizio di Ricerca Provvedimento");
			addErrori(response);
			throwExceptionFromErrori(response.getErrori());
		}
		
		return response.getListaAttiAmministrativi().get(0);
	}
	
	/**
	 * Carica la lista dei tipi di finanziamento del capitolo.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	protected void caricaListaTipoFinanziamento() throws WebServiceInvocationFailureException {
		List<TipoFinanziamento> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO);
		if(listaInSessione == null) {
			LeggiClassificatoriGenericiByTipoElementoBil request = 
					model.creaRequestLeggiClassificatoriGenericiByTipoElementoBil(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
			LeggiClassificatoriGenericiByTipoElementoBilResponse response = classificatoreBilService.leggiClassificatoriGenericiByTipoElementoBil(request);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaTipoFinanziamento");
			}
			
			listaInSessione = response.getClassificatoriTipoFinanziamento();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_FINANZIAMENTO, listaInSessione);
		}
		
		model.setListaTipiFinanziamento(listaInSessione);
	}

	/**
	 * Controlla se il metodo prepare si sia concluso senza alcun errore. In caso contrario, rilancia un'eccezione per uscire dalla pagina.
	 * 
	 * @throws GenericFrontEndMessagesException in caso di errori nei servizii
	 */
	protected void checkPrepareConclusoSenzaErrori() {
		if(hasErrori()) {
			StringBuilder erroriRiscontrati = new StringBuilder();
			for(Errore errore : model.getErrori()) {
				erroriRiscontrati.append(errore.getTesto() + "<br>");
			}
			
			throw new GenericFrontEndMessagesException(ErroreCore.ERRORE_DI_SISTEMA.getErrore(erroriRiscontrati.toString()).getTesto(),
					GenericFrontEndMessagesException.Level.ERROR);
		}
	}
	
	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);
		
		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioCompatibile = 
				FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio) ||
				FaseBilancio.GESTIONE.equals(faseBilancio) ||
				FaseBilancio.ASSESTAMENTO.equals(faseBilancio) ||
				FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO.equals(faseBilancio);
		
		if(!faseDiBilancioCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
	/**
	 * Controlla se il provvedimento sia esistente, e pertanto da cercare.
	 * 
	 * @return <code>true</code> se il provvedimento &eacute; esistente; <code>false</code> in caso contrario
	 */
	protected boolean checkProvvedimentoEsistente() {
		return checkIntegerNotZero(model.getUidProvvedimento())
				|| checkAttoAmministrativoValido(model.getAttoAmministrativo(), model.getTipoAtto(), model.getStrutturaAmministrativoContabileAttoAmministrativo());
	}
	
	/**
	 * Controlla che l'atto sia valido.
	 * 
	 * @param attoAmministrativo               l'atto da controllare
	 * @param tipoAtto                         il tipo atto
	 * @param strutturaAmministrativoContabile la struttura
	 * 
	 * @return <code>true</code> se l'atto &eacute; valido; <code>false</code> altrimenti
	 */
	private boolean checkAttoAmministrativoValido(AttoAmministrativo attoAmministrativo, TipoAtto tipoAtto, StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		return attoAmministrativo != null
				&& (attoAmministrativo.getAnno() != 0 || attoAmministrativo.getNumero() != 0 || isValidoEntita(tipoAtto) || isValidoEntita(strutturaAmministrativoContabile));
	}

	/**
	 * Controlla se l'entita sia valida.
	 * 
	 * @param entita l'entita da controllare
	 * @return se l'entita sia valida
	 */
	private boolean isValidoEntita(Entita entita) {
		return entita != null && entita.getUid() != 0;
	}

	/**
	 * Controlla che l'Integer non sia zero.
	 * 
	 * @param integer l'integer da controllare
	 * @return se l'integer non sia zero
	 */
	private boolean checkIntegerNotZero(Integer integer) {
		return integer != null && integer.intValue() != 0;
	}
	
	/**
	 * Controlla se il capitolo sia presente, e pertanto da cercare.
	 * @param <C> la tipizzazione del capitolo
	 * @param capitolo il capitolo da controllare
	 * 
	 * @return <code>true</code> se il capitolo &eacute; esistente; <code>false</code> in caso contrario
	 */
	protected <C extends Capitolo<?, ?>> boolean checkCapitoloEsistente(C capitolo) {
		return capitolo != null && 
				capitolo.getAnnoCapitolo() != null &&
				capitolo.getNumeroCapitolo() != null &&
				capitolo.getNumeroArticolo() != null &&
				capitolo.getNumeroUEB() != null;
	}
	
	/**
	 * Controlla se il movimento di gestione sia presente.
	 * 
	 * @param movimentoGestione il movimento da controllare
	 * 
	 * @return <code>true</code> se il movimento &eacute; presente; <code>false</code> in caso contrario
	 */
	protected boolean checkMovimentoGestioneEsistente(MovimentoGestione movimentoGestione) {
		return movimentoGestione != null && (movimentoGestione.getAnnoMovimento() != 0 || movimentoGestione.getNumeroBigDecimal() != null);
	}
	
	/**
	 * Controlla se il soggetto sia presente.
	 * 
	 * @return <code>true</code> se il soggetto &eacute; presente; <code>false</code> in caso contrario
	 */
	protected boolean checkSoggettoEsistente() {
		return model.getSoggetto() != null && StringUtils.isNotBlank(model.getSoggetto().getCodiceSoggetto());
	}
	
	/**
	 * Controlla che la struttura sia presente.
	 * 
	 * @param strutturaAmministrativoContabile la struttura da validare
	 * 
	 * @return <code>true</code> se la struttura &eacute; presente; <code>false</code> in caso contrario
	 */
	protected boolean checkStrutturaEsistente(StrutturaAmministrativoContabile strutturaAmministrativoContabile){
		return model.getStrutturaAmministrativoContabile() != null &&
				model.getStrutturaAmministrativoContabile().getUid() != 0;
	}
	
}
