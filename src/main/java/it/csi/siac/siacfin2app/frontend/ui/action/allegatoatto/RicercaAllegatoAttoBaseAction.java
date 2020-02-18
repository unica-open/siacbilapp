/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.RicercaAllegatoAttoBaseModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAttoResponse;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di Action per la ricerca dell'allegato atto.
 *
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/set/2014
 * @version 1.0.1 - 15/set/2014 - Aggiunta della classe base
 * @param <M> the generic type
 */
public abstract class RicercaAllegatoAttoBaseAction<M extends RicercaAllegatoAttoBaseModel> extends GenericAllegatoAttoAction<M> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8269920632698578563L;

	/** Per la serializzazione */
	
	@Autowired private transient SoggettoService soggettoService;

	@Autowired private transient MovimentoGestioneService impegnoService;

	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		super.prepare();
		// Caricamento liste
		try {
			log.debug(methodName, "Caricamento liste classificatori");
			caricaListaTipoAtto();
			caricaListaStatoOperativoAllegatoAtto();
			caricaListaStatoOperativoElencoDocumenti();
			caricaListaClassiSoggetto();			
		} catch (WebServiceInvocationFailureException e) {
			log.error(methodName, "Errore nell'invocazione del caricamento di una lista: " + e.getMessage());
		}
	}
	
	/**
	 * Effettua la ricerca per Allegato Atto.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String effettuaRicerca() {
		final String methodName = "effettuaRicerca";
		
		RicercaAllegatoAtto request = model.creaRequestRicercaAllegatoAtto();
		logServiceRequest(request);
		RicercaAllegatoAttoResponse response = allegatoAttoService.ricercaAllegatoAtto(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return INPUT;
		}
		if(response.getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato di ricerca per i dati forniti");
			impostaMessaggioNessunDatoTrovato();
			return INPUT;
		}
		
		log.debug(methodName, "Ricerca effettuata con successo. Totale elementi trovati: " + response.getTotaleElementi());
		
		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(ottieniParametroSessioneRequest(), request);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(ottieniParametroSessioneRisultatiRicerca(), response.getAllegatoAtto());
		
		log.debug(methodName, "Imposto la stringa da visualizzare nei risultati ricerca");
		List<TipoAtto> listaTipoAtto = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_ATTO_AMMINISTRATIVO);
		List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile =sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		
		
		
		
		String stringaRiepilogo = model.componiStringaRiepilogo(listaTipoAtto, listaStrutturaAmministrativoContabile);
		
		sessionHandler.setParametro(BilSessionParameter.RIEPILOGO_RICERCA_ALLEGATO_ATTO, stringaRiepilogo);
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		sessionHandler.setParametro(BilSessionParameter.TAB_VISUALIZZAZIONE_ALLEGATO_ATTO, null);
		return SUCCESS;
	}

	/**
	 * 
	 */
	protected void impostaMessaggioNessunDatoTrovato() {
		addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
	}

	/**
	 * Filtra e imposta gli errori
	 * @param response la response del servizio
	 */
	protected void filtraEdImpostaErrori(RicercaAllegatoAttoResponse response) {
		addErrori(response);
	}
	
	/**
	 * Validazione per il metodo {@link #effettuaRicerca()}.
	 */
	public void validateEffettuaRicerca() {
		final String methodName = "validateEffettuaRicerca";
		
		// L'allegato
		final AllegatoAtto allegatoAtto = model.getAllegatoAtto();
		
		final boolean isCausaleNonValorizzataOValida = StringUtils.isBlank(allegatoAtto.getCausale()) || StringUtils.trimToEmpty(allegatoAtto.getCausale()).length() >= 3;
		// La causale e' valida se valorizzata
		final boolean isCausaleValida = StringUtils.isNotBlank(allegatoAtto.getCausale()) && StringUtils.trimToEmpty(allegatoAtto.getCausale()).length() >= 3;
		// La causale deve avere almeno 3 caratteri se presente
		checkCondition(isCausaleNonValorizzataOValida, ErroreCore.FORMATO_NON_VALIDO.getErrore("Causale", "deve avere almeno tre caratteri"));

		// Controllo provvedimento
		final boolean isAttoAmministrativoValido = checkAttoAmministrativo();
		
		// Controllo soggetto
		final boolean isSoggettoValido = checkSoggetto();
		
		// Controllo impegno
		final boolean isImpegnoValido = checkImpegno();

		// Controllo Elenco
		final boolean isElencoDocumentiAllegatoValido = checkElencoDocumentiAllegato();
		
		checkCondition(StringUtils.isBlank(model.getFlagRitenute()) || "S".equals(model.getFlagRitenute()) || "N".equals(model.getFlagRitenute()),
			ErroreCore.FORMATO_NON_VALIDO.getErrore("Flag ritenute", "se impostati, sono ammessi solo i valori S e N"));

		checkCondition(StringUtils.isBlank(model.getFlagSoggettoDurc()) || "S".equals(model.getFlagSoggettoDurc()) || "N".equals(model.getFlagSoggettoDurc()),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("Flag ritenute", "se impostati, sono ammessi solo i valori S e N"));
		
		// Controllo che ci sia almeno un criterio valido di ricerca
		final boolean almenoUnCriterioValido = isCausaleValida
			|| allegatoAtto.getStatoOperativoAllegatoAtto() != null
			|| model.getDataScadenzaDa() != null
			|| model.getDataScadenzaA() != null
			|| StringUtils.isNotBlank(model.getFlagRitenute())
			|| isAttoAmministrativoValido
			|| isSoggettoValido
			|| isImpegnoValido
			|| isElencoDocumentiAllegatoValido
			// SIAC-6166
			|| model.getAnnoBilancio() != null
			//SIAC-6162
			|| StringUtils.isNotBlank(model.getFlagSoggettoDurc())
			;
		
		log.debug(methodName, "La ricerca e' valida? " + almenoUnCriterioValido);
		checkCondition(almenoUnCriterioValido, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
	}

	/**
	 * Controllo per il provvedimento.
	 * 
	 * @return <code>true</code> se il provvedimento &eacute; valido per la ricerca; <code>false</code> altrimenti
	 */
	private boolean checkAttoAmministrativo() {
		final String methodName = "checkAttoAmministrativo";
		AttoAmministrativo aa = model.getAttoAmministrativo();
		checkCondition(aa == null || !(aa.getAnno() != 0 ^ aa.getNumero() != 0), ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno e numero atto", "devono essere entrambi valorizzati o entrambi non valorizzati"));
		
		if(aa == null || aa.getAnno() == 0 || aa.getNumero() == 0) {
			return false;
		}
		
		if(model.getStrutturaAmministrativoContabile() != null && model.getStrutturaAmministrativoContabile().getUid() != 0) {
			model.getAttoAmministrativo().setStrutturaAmmContabile(model.getStrutturaAmministrativoContabile());
		}
		log.debug(methodName, "Ricerca provvedimento con anno " + aa.getAnno() + " e numero " + aa.getNumero());
		// Se ho i dati dell'atto amministrativo, controllo che siano corretti
		try {
			//controlloEsistenzaEUnicitaAttoAmministrativo();
			//ANTO SIAC-5660
			controlloEsistenzaAttoAmministrativo();
			return true;
		} catch(ParamValidationException pve) {
			log.info(methodName, "Errore di validazione dell'Atto Amministrativo: " + pve.getMessage());
		}
		return false;
	}
	
	/**
	 * Controllo per il soggetto.
	 * 
	 * @return <code>true</code> se il soggetto &eacute; valido per la ricerca; <code>false</code> altrimenti
	 */
	private boolean checkSoggetto() {
		final String methodName = "checkSoggetto";
		Soggetto soggetto = model.getSoggetto();
		final boolean isSoggettoValido = soggetto != null && StringUtils.isNotBlank(soggetto.getCodiceSoggetto());
		
		if(!isSoggettoValido) {
			return false;
		}
		
		log.debug(methodName, "Ricerca soggetto con codice " + soggetto.getCodiceSoggetto());
		// Se ho i dati del soggetto, controllo che siano corretti
		try {
			controlloSoggetto();
		} catch(ParamValidationException pve) {
			log.info(methodName, "Errore di validazione del Soggetto: " + pve.getMessage());
		}
		return isSoggettoValido;
	}
	
	/**
	 * Controllo per l impegno.
	 * 
	 * @return <code>true</code> se il soggetto &eacute; valido per la ricerca; <code>false</code> altrimenti
	 */
	private boolean checkImpegno() {
		final String methodName = "checkImpegno";
		Impegno impegno = model.getImpegno();
		final boolean isImpegnoValido = impegno != null && impegno.getNumero() != null && impegno.getAnnoMovimento() != 0;
		
		if(!isImpegnoValido) {
			return false;
		}
		
		log.debug(methodName, "Ricerca impegno con numero: " + impegno.getNumero() +" anno: " + impegno.getAnnoMovimento());
		// Se ho i dati del soggetto, controllo che siano corretti
		try {
			controlloImpegno();			
		} catch(ParamValidationException pve) {
			log.info(methodName, "Errore di validazione del Soggetto: " + pve.getMessage());
		}
		return isImpegnoValido;
	}
	
	/**
	 * Controlla la presenza del soggetto.
	 */
	private void controlloSoggetto() {
		final String methodName = "controlloSoggetto";
		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave();
		logServiceRequest(request);
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMessage = createErrorInServiceInvocationString(request, response);
			log.info(methodName, errorMessage);
			addErrori(response);
			throw new ParamValidationException(errorMessage);
		}
		if(response.getSoggetto() == null) {
			log.info(methodName, "Nessun soggetto ottenuto con codice" + model.getSoggetto().getCodiceSoggetto());
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", model.getSoggetto().getCodiceSoggetto()));
			return;
		}
		model.setSoggetto(response.getSoggetto());
	}

	/**
	 * Controlla l impegno.
	 */
	private void controlloImpegno() {
		final String methodName = "controlloImpegno";

		RicercaImpegnoPerChiaveOttimizzato request = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();
		logServiceRequest(request);
		RicercaImpegnoPerChiaveOttimizzatoResponse response = impegnoService.ricercaImpegnoPerChiaveOttimizzato(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			String errorMessage = createErrorInServiceInvocationString(request, response);
			log.info(methodName, errorMessage);
			addErrori(response);
			throw new ParamValidationException(errorMessage);
		}
		if(response.getImpegno() == null) {
			log.info(methodName, "Nessun impegno ottenuto con codice" + model.getImpegno().getAnnoMovimento());
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", model.getImpegno().getNumero()));
			return;
		}
		model.setImpegno(response.getImpegno());
		if(model.getSubImpegno() != null && model.getSubImpegno().getNumero() != null) {
			SubImpegno subImpegno = findSubImpegno(response.getImpegno(), model.getSubImpegno());
			model.setSubImpegno(subImpegno);
		}
	}
	
	/**
	 * Trova il subImpegno dall'impegno restituito dal servizio di ricerca.
	 * 
	 * @param impegno    l'impegno del servizio di ricerca
	 * @param subImpegno il subimpegno con uid da trovare
	 * @return il subimpegno
	 */
	private SubImpegno findSubImpegno(Impegno impegno, SubImpegno subImpegno) {
		final String methodName = "findSubImpegno";
		if(impegno == null || impegno.getElencoSubImpegni() == null) {
			// TODO: lanciare eccezione: ho selezionato il subimpegno ma non ho subimpegni
			//throw new Exception("test");
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("SubImpegno non reperibile dalla response"));
			log.warn(methodName, "SubImpegno non reperibile dalla response");
			return null;
		}
		for(SubImpegno si : impegno.getElencoSubImpegni()) {
			if(si != null && subImpegno.getNumero().compareTo(si.getNumero()) == 0) {
				return si;
			}
		}
		// TODO: lanciare eccezione: ho selezionato il subimpegno ma non l'ho trovato
		log.warn(methodName, "Nessun subimpegno con uid " + subImpegno.getNumero() + " reperibile nell'impegno con uid " + impegno.getUid());
		addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Nessun subimpegno con uid " + subImpegno.getNumero() + " reperibile nell'impegno con uid " + impegno.getUid()));
		return null;
	}
	
	
	/**
	 * Controllo per l'elenco documenti allegato.
	 * 
	 * @return <code>true</code> se l'elenco &eacute; valido per la ricerca; <code>false</code> altrimenti
	 */
	private boolean checkElencoDocumentiAllegato() {
		final String methodName = "checkElencoDocumentiAllegato";
		ElencoDocumentiAllegato eda = model.getElencoDocumentiAllegato();
		final boolean isElencoDocumentiAllegatoValido = eda == null || !(eda.getAnno() != null ^ eda.getNumero() != null);
		checkCondition(isElencoDocumentiAllegatoValido, ErroreCore.FORMATO_NON_VALIDO.getErrore("Anno e numero elenco", "devono essere entrambi valorizzati o entrambi non valorizzati"));
		
		if(eda == null || eda.getAnno() == null || eda.getNumero() == null) {
			return false;
		}
		
		// Effettuo la ricerca
		log.debug(methodName, "Ricerca elenco con anno " + eda.getAnno() + " e numero " + eda.getNumero());
		// Se ho i dati dell'atto amministrativo, controllo che siano corretti
		try {
			controlloEsistenzaEUnicitaElencoDocumentiAllegato();
		} catch(ParamValidationException pve) {
			log.info(methodName, "Errore di validazione dell'Elenco: " + pve.getMessage());
		}
		return isElencoDocumentiAllegatoValido;
	}

	/**
	 * Carico la lista degli stati operativo dell'allegato atto.
	 */
	protected abstract void caricaListaStatoOperativoAllegatoAtto();
	
	/**
	 * Ottieni parametro in base al quale mettere in sessione la request.
	 *
	 * @return the bil session parameter
	 */
	protected abstract BilSessionParameter ottieniParametroSessioneRequest();
	
	/**
	 * Ottieni parametro sessione in base al quale mettere in sessione i risultati ricerca.
	 *
	 * @return the bil session parameter
	 */
	protected abstract BilSessionParameter ottieniParametroSessioneRisultatiRicerca();
	/**
	 * Controlla se la lista delle Classi Soggetto sia presente valorizzata nel model.
	 * <br>
	 * In caso contrario, effettua una ricerca per ottenere i tipi di documento.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso di errore nell'invocazione del servizio
	 */
	private void caricaListaClassiSoggetto() throws WebServiceInvocationFailureException {
		List<CodificaFin> listaClassiSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaClassiSoggetto == null) {
			ListeGestioneSoggetto request = model.creaRequestListeGestioneSoggetto();
			logServiceRequest(request);
			ListeGestioneSoggettoResponse response = soggettoService.listeGestioneSoggetto(request);
			logServiceResponse(response);
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaTipoAtto");
			}			
			listaClassiSoggetto = response.getListaClasseSoggetto();
			ComparatorUtils.sortByCodiceFin(listaClassiSoggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaClassiSoggetto);
		}
		model.setListaClasseSoggetto(listaClassiSoggetto);
	}
	
}

