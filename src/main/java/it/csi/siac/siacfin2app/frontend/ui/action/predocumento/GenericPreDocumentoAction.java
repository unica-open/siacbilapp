/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorCodificaPerDescrizione;
import it.csi.siac.siacbilser.frontend.webservice.ClassificatoreBilService;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBil;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriGenericiByTipoElementoBilResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.model.TipoFinanziamento;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.GenericPreDocumentoModel;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.GenericService;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuni;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListaComuniResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.Liste;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.codifiche.TipiLista;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.Sesso;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe di action generica per il PreDocumento.
 *  
 * @author Marchino Alessandro,Nazha Ahmad
 * @version 1.0.0 - 28/04/2014
 * @version 1.0.1 - 28/04/2014
 * 
 * @param <M> la parametrizzazione del model
 *
 */
public class GenericPreDocumentoAction<M extends GenericPreDocumentoModel> extends GenericBilancioAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5753785789350319842L;
	
	/** Serviz&icirc; dei classificatori bil */
	@Autowired protected transient ClassificatoreBilService classificatoreBilService;
	/** Serviz&icirc; generici */
	@Autowired protected transient GenericService genericService;
	/** Serviz&icirc; del movimento di gestione */
	@Autowired protected transient MovimentoGestioneService movimentoGestioneService;
	/** Serviz&icirc; del soggetto */
	@Autowired protected transient SoggettoService soggettoService;
	/** Serviz&icirc; del provvedimento */
	@Autowired protected transient ProvvedimentoService provvedimentoService;
	
	/** Risultato corrispondente all'aggiornamento */
	protected static final String AGGIORNA = "aggiorna";
	/** Risultato corrispondente alla ricerca */
	protected static final String RICERCA = "ricerca";
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		
		// Imposto la lista dei comuni nel model
		List<ComuneNascita> listaComuni = sessionHandler.getParametro(BilSessionParameter.LISTA_COMUNE);
		model.setListaComuni(listaComuni);
	}

	/**
	 * Effettua una validazione del Soggetto fornito in input.
	 * 
	 * @return <code>true</code> se la validazione &eacute; andata a buon fine; <code>false</code> in caso contrario
	 */
	protected boolean validazioneSoggetto() {
		final String methodName = "validazioneSoggetto";
		Soggetto soggetto = model.getSoggetto();
		// Se non ho il soggetto, sono a posto
		if(soggetto == null || StringUtils.isBlank(soggetto.getCodiceSoggetto())) {
			return false;
		}
		
		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave(soggetto);
		logServiceRequest(request);
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		logServiceResponse(response);
		
		// Se ho errori, esco subito dopo aver caricato gli errori
		if(response.hasErrori()) {
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errori riscontrati nel servizio RicercaSoggettoPerChiave"));
			addErrori(response);
			return false;
		}
		
		if(response.isFallimento() || response.getSoggetto() == null) {
			log.info(methodName, "Nessun soggetto fornito dal servizio");
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Il soggetto non e' presente"));
			return false;
		}
		
		soggetto = response.getSoggetto();
		// Aggiorno i dati del soggetto
		model.setSoggetto(soggetto);
		
		// Imposto anche le liste di MPS e SSS
		List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = defaultingList(response.getListaSecondariaSoggetto());
				
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
		listaModalitaPagamentoSoggetto = impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
		
		impostazioneModalitaPagamentoESedeSecondaria(listaSedeSecondariaSoggetto, listaModalitaPagamentoSoggetto);
		sessionHandler.setParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO, listaSedeSecondariaSoggetto);
		sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO, listaModalitaPagamentoSoggetto);
		
		checkCondition(!StatoOperativoAnagrafica.ANNULLATO.equals(soggetto.getStatoOperativo()), ErroreFin.SOGGETTO_ANNULLATO.getErrore());
		checkCondition(!StatoOperativoAnagrafica.BLOCCATO.equals(soggetto.getStatoOperativo()), ErroreFin.SOGGETTO_BLOCCATO.getErrore());
		return true;
	}
	
	
	/**
	 * carica il Soggetto fornito in input per la ricerca sintetica.
	 * 
	 * @return <code>true</code> se la validazione &eacute; andata a buon fine; <code>false</code> in caso contrario
	 */
	protected boolean validazioneSoggettoPerRicerca() {
		final String methodName = "validazioneSoggettoPerRicerca";
		Soggetto soggetto = model.getSoggetto();
		// Se non ho il soggetto, sono a posto
		if(soggetto == null || StringUtils.isBlank(soggetto.getCodiceSoggetto())) {
			return false;
		}
		
		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave(soggetto);
		logServiceRequest(request);
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		logServiceResponse(response);
		
		// Se ho errori, esco subito dopo aver caricato gli errori
		if(response.hasErrori()) {
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Errori riscontrati nel servizio RicercaSoggettoPerChiave"));
			addErrori(response);
			return false;
		}
		
		if(response.isFallimento() || response.getSoggetto() == null) {
			log.info(methodName, "Nessun soggetto fornito dal servizio");
			addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Il soggetto non e' presente"));
			return false;
		}
		
		soggetto = response.getSoggetto();
		// Aggiorno i dati del soggetto
		model.setSoggetto(soggetto);
		
		// Imposto anche le liste di MPS e SSS
		List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = defaultingList(response.getListaSecondariaSoggetto());
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
		listaModalitaPagamentoSoggetto = impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
		
		impostazioneModalitaPagamentoESedeSecondaria(listaSedeSecondariaSoggetto, listaModalitaPagamentoSoggetto);
		sessionHandler.setParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO, listaSedeSecondariaSoggetto);
		sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO, listaModalitaPagamentoSoggetto);
		
		return true;
	}
	
	/**
	 * Metodo per l'impostazione della modalit&agrave; di pagamento e della sede secondaria.
	 * 
	 * @param listaSedeSecondariaSoggetto    la lista delle sedi secondarie
	 * @param listaModalitaPagamentoSoggetto la lista delle modalita di pagamento
	 */
	protected void impostazioneModalitaPagamentoESedeSecondaria(List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto, List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto) {
		// Implementazione vuota
	}

	/**
	 * Effettua una validazione dell'atto amministrativo fornito in input.
	 * 
	 * @return <code>true</code> se la validazione &eacute; andata a buon fine; <code>false</code> in caso contrario
	 */
	protected boolean validazioneAttoAmministrativo() {
		// Se non ho l'atto amministrativo sono a posto
		if(!checkProvvedimentoEsistente()) {
			return false;
		}
		
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento();
		logServiceRequest(request);
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return false;
		}
		
		// Il provvedimento deve esistere
		if(response.getListaAttiAmministrativi().isEmpty()) {
			addErrore(ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore());
			return false;
		}
		
		// Deve esistere un unico provvedimento
		checkUnicoAttoAmministrativo(response.getListaAttiAmministrativi(), model.getStrutturaAmministrativoContabileAttoAmministrativo(), false);
		
		model.setAttoAmministrativo(response.getListaAttiAmministrativi().get(0));
		return true;
	}
	
	/**
	 * Controlla la congruenza tra il soggetto e l'impegno.
	 * 
	 * @param soggetto             il soggetto
	 * @param movimentoGestione    il movimento di gestione
	 * @param subMovimentoGestione il submovimento di gestione
	 * @param nomePredocumento     il nome del predocumento
	 * @param nomeMovimento        il nome del movimento
	 */
	protected void controlloConguenzaSoggettoMovimentoGestione(Soggetto soggetto, MovimentoGestione movimentoGestione, MovimentoGestione subMovimentoGestione,
			String nomePredocumento, String nomeMovimento) {
		final String methodName = "controlloConguenzaSoggettoMovimentoGestione";
		
		log.debug(methodName, " soggetto : " + (soggetto == null ? "NULL" : soggetto.getUid()));
		log.debug(methodName, " impegno : " + (movimentoGestione == null ? "NULL" : movimentoGestione.getUid()));
		// Se non ho soggetto o impegno, va tutto bene
		if(soggetto == null || movimentoGestione == null || soggetto.getUid() == 0 || movimentoGestione.getUid() == 0) {
			return;
		}
		
		if(subMovimentoGestione != null && subMovimentoGestione.getUid() != 0) {
			log.debug(methodName, "controllo la coerenza del soggetto subaccertamento");
			Soggetto soggettoAssociato = subMovimentoGestione.getSoggetto();
			checkCondition(soggettoAssociato == null || soggettoAssociato.getUid() == 0 || soggettoAssociato.getCodiceSoggetto().equals(soggetto.getCodiceSoggetto()), 
					ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto", "sub" + nomeMovimento, "per la " + nomePredocumento,
							"il soggetto deve essere lo stesso del sub" + nomeMovimento));
		} else {
			Soggetto soggettoAssociato = movimentoGestione.getSoggetto();
			
			if(soggettoAssociato != null && soggettoAssociato.getUid() != 0 ){
				log.debug(methodName,  "controllo la coerenza del soggetto accertamento");
				checkCondition( soggettoAssociato.getCodiceSoggetto().equals(soggetto.getCodiceSoggetto()), 
						ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto", nomeMovimento, "per la " + nomePredocumento,
								"il soggetto deve essere lo stesso dell'" + nomeMovimento));
			}else{
				log.debug(methodName,  "controllo la coerenza della classe soggetto accertamento");
				checkClasseSoggetto(movimentoGestione, soggetto, "soggetto", nomeMovimento , "per la " + nomePredocumento, "il soggetto deve appartenere alla classificazione", false);
			}
		}
		
	}
	
	/**
	 * Carica la lista delle nazioni.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	protected void caricaListaNazioni() throws WebServiceInvocationFailureException {
		List<? extends CodificaFin> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_NAZIONE);
		if(listaInSessione == null) {
			Liste request = model.creaRequestListe(TipiLista.NAZIONI);
			logServiceRequest(request);
			ListeResponse response = genericService.liste(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaNazioni");
			}
			
			listaInSessione = response.getNazioni();
			sessionHandler.setParametro(BilSessionParameter.LISTA_NAZIONE, listaInSessione);
		}
		
		model.setListaNazioniFromExtended(listaInSessione);
	}
	
	/**
	 * Carica la lista dei comuni.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	protected void caricaListaComuni() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListaComuni";
		List<ComuneNascita> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_COMUNE);
		if(listaInSessione == null) {
			String idNazione = findIdNazioneByDescrizione(BilConstants.DESCRIZIONE_ITALIA.getConstant());
			ListaComuni request = model.creaRequestListaComuni(idNazione);
			logServiceRequest(request);
			ListaComuniResponse response = genericService.cercaComuni(request);
			// Non loggo la response in quanto effettivamente troppo grande (in Italia ci sono 8057 comuni al 30/06/2014 cfr. http://www.istat.it/it/archivio/6789 )
			if(response.hasErrori()) {
				// Se ho errori, invece, loggo
				logServiceResponse(response);
				log.info(methodName, createErrorInServiceInvocationString(request, response));
				addErrori(response);
				throw new WebServiceInvocationFailureException("ListaComuni");
			}
			
			listaInSessione = response.getListaComuni();
			// Sort della lista
			Collections.sort(listaInSessione, ComparatorCodificaPerDescrizione.INSTANCE);
			sessionHandler.setParametro(BilSessionParameter.LISTA_COMUNE, listaInSessione);
		}
		
		model.setListaComuni(listaInSessione);
	}

	/**
	 * Ottiene l'id della nazione di data descrizione.
	 * 
	 * @param descrizioneNazione la descrizione della nazione
	 * 
	 * @return l'id della nazione di data descrizione, se presente; una stringa vuota in caso contrario
	 */
	private String findIdNazioneByDescrizione(String descrizioneNazione) {
		List<CodificaFin> listaNazioni = model.getListaNazioni();
		for(CodificaFin nazione : listaNazioni) {
			if(nazione.getDescrizione() != null && descrizioneNazione.equalsIgnoreCase(nazione.getDescrizione())) {
				return nazione.getId().toString();
			}
		}
		return "";
	}
	
	/**
	 * Carica la lista dei sessi.
	 */
	protected void caricaListaSesso() {
		model.setListaSesso(Arrays.asList(Sesso.values()));
	}
	

	/**
	 * Carica la lista dei tipi di atto.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	protected void caricaListaTipoAtto() throws WebServiceInvocationFailureException {
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
				throw new WebServiceInvocationFailureException("caricaListaTipoAtto");
			}
			
			listaInSessione = response.getElencoTipi();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO, listaInSessione);
		}
		
		model.setListaTipoAtto(listaInSessione);
	}
	
	/**
	 * Carica la lista delle classi del soggetto.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	protected void caricaListaClasseSoggetto() throws WebServiceInvocationFailureException {
		List<CodificaFin> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaInSessione == null) {
			ListeGestioneSoggetto request = model.creaRequestListeGestioneSoggetto();
			ListeGestioneSoggettoResponse response = soggettoService.listeGestioneSoggetto(request);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaClasseSoggetto");
			}
			
			listaInSessione = response.getListaClasseSoggetto();
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaInSessione);
		}
		
		model.setListaClasseSoggetto(listaInSessione);
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
		
		model.setStrutturaAmministrativoContabileAttoAmministrativo(aa.getStrutturaAmmContabile());
		
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
		
		if(response.getListaAttiAmministrativi().isEmpty()) {
			throw new GenericFrontEndMessagesException(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Provvedimento [uid=" + aa.getUid() + "] collegato al predocumento ma nessun provvedimento corrispondente ai filtri di ricerca disponibile").getTesto(),
					GenericFrontEndMessagesException.Level.ERROR);
		}
		
		return response.getListaAttiAmministrativi().get(0);
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
		return (model.getUidProvvedimento() != null && model.getUidProvvedimento() != 0) ||
			(
				model.getAttoAmministrativo() != null &&
				(
					model.getAttoAmministrativo().getAnno() != 0 ||
					model.getAttoAmministrativo().getNumero() != 0 ||
					(model.getTipoAtto() != null && model.getTipoAtto().getUid() != 0) ||
					(model.getAttoAmministrativo().getStrutturaAmmContabile() != null && model.getAttoAmministrativo().getStrutturaAmmContabile().getUid() != 0)
				)
			);
	}
	
	/**
	 * controlla l'importo da regolarizzare del provvisorio di cassa che sia &ge; importo documento
	 * 
	 * @param importoDocumento l'importo del documento
	 * @return true se l'importo da regolarizzare &ge; importo predocumento &rarr; provvisorio di cassa regolarizzabile
	 */
	protected boolean isProvvisorioDiCassaRegolarizzabile(BigDecimal importoDocumento){
		boolean res = false;
		
		if(model.getProvvisorioCassa().getImportoDaRegolarizzare() == null || model.getProvvisorioCassa().getImportoDaRegolarizzare().compareTo(BigDecimal.ZERO) == 0){
			return res;
		}
		
		int ris = model.getProvvisorioCassa().getImportoDaRegolarizzare().compareTo(importoDocumento);
		return ris >= 0;
	}
	
	/**
	 * Devono essere visualizzate solo modalit&agrave; di pagamento relative ai TipoAccredito = <code>CB</code> e <code>CCP</code>
	 * in stato <code>VALIDO</code> e con data scadenza delle modalit&agrave; di pagamento &ge; alla data corrente
	 * 
	 * @param listaModalitaPagamentoSoggetto la lista delle modalita da filtrare
	 * @return la lista filtrata
	 */
	protected List<ModalitaPagamentoSoggetto> filtraListaModalitaPagamentoSoggetto(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto){
		List<ModalitaPagamentoSoggetto> listaFiltrata =new ArrayList<ModalitaPagamentoSoggetto>();
		//TODO aggiungere il filtro sulla data di scadenza
		for(ModalitaPagamentoSoggetto mps : listaModalitaPagamentoSoggetto){
			if((TipoAccredito.CCP.equals(mps.getTipoAccredito()) && "VALIDO".equals(mps.getCodiceStatoModalitaPagamento())) || (TipoAccredito.CB.equals(mps.getTipoAccredito()) && "VALIDO".equals(mps.getCodiceStatoModalitaPagamento()))){
				listaFiltrata.add(mps);
			}
		}
		return defaultingList(listaFiltrata);
		
	}

}
