/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.stampe.stamparendiconto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccecapp.frontend.ui.action.cassaeconomale.stampe.GenericStampaCECAction;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.stampe.StampaCECRendicontoModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRendicontoCassaDaStampare;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaRendicontoCassaDaStampareResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimoRendicontoCassaStampato;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimoRendicontoCassaStampatoResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRendicontoCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRendicontoCassaResponse;
import it.csi.siac.siaccecser.frontend.webservice.msg.VerificaStampaRendicontoCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.VerificaStampaRendicontoCassaResponse;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.StampaRendiconto;
import it.csi.siac.siaccecser.model.errore.ErroreCEC;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
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

/**
 * Classe di action per le stampa del rendiconto CEC.
 * 
 * @author Nazha Ahmad
 * @author Marchino Alessandro
 * @version 1.0.0 - 31/03/2015
 * @version 1.1.0 - 15/06/2017
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class StampaCECRendicontoAction extends GenericStampaCECAction<StampaCECRendicontoModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4742928877046561702L;
	
	@Autowired private transient SoggettoService soggettoService;
	
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
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		super.execute();
		// Controllo se l'azione precedente sia stata un successo
		leggiEventualiInformazioniAzionePrecedente();
		// Controllo se vi siano stati messaggi dall'azione precedente
		leggiEventualiMessaggiAzionePrecedente();
		// Controllo se vi siano stati errori dall'azione precedente
		leggiEventualiErroriAzionePrecedente();

		model.impostaDatiNelModel();
		return SUCCESS;
	}

	/**
	 * Preparazione per il metodo {@link #enterStep2()}
	 */
	public void prepareEnterStep2() {
		model.setCassaEconomale(null);
		model.setStampaRendiconto(null);
		model.setTipoStampa(null);
		model.setPeriodoDaRendicontareDataFine(null);
		model.setPeriodoDaRendicontareDataInizio(null);
	}
	
	/**
	 * Effettua i controlli neccessari richiesti dall'analisi prima di effettuare la stampa
	 */
	public void validateEnterStep2() {
		final String methodName = "validateStampaRendiconto";
		log.debug(methodName, " verifico parametri in corso");

		checkNotNullNorInvalidUid(model.getCassaEconomale(), "Cassa economale");
		// Impostazione della cassa selezionata
		impostaCassa();
		verificaRendicontoStampabile();
		checkNotNull(model.getPeriodoDaRendicontareDataInizio(), "Periodo da rendicontare inizio");
		checkNotNull(model.getPeriodoDaRendicontareDataFine(), "Periodo da rendicontare fine");
		checkNotNull(model.getTipoStampa(), "Tipo stampa");
		
		checkCondition(!hasErrori(), ErroreCEC.CEC_ERR_0024.getErrore());
	}

	/**
	 * Verifica se la stampa &eacute; in stato definitivo ed in base al periodo scelto
	 */
	private void verificaRendicontoStampabile() {
		VerificaStampaRendicontoCassa req = model.creaRequestVerificaStampaRendicontoCassa();
		VerificaStampaRendicontoCassaResponse res = stampaCassaEconomaleService.verificaStampaRendicontoCassa(req);
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(res);
		}
		if(!res.isStampabile()) {
			addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("non e' possibile effettuare la stampa per i parametri forniti"));
		}
	}
	
	/**
	 * Impostazione della cassa selezionata
	 */
	private void impostaCassa() {
		final String methodName = "impostaCassaEconomale";
		if(model.getCassaEconomale() == null || model.getCassaEconomale().getUid() == 0) {
			// Non ho la cassa economale, esco subito
			log.debug(methodName, "Cassa economale non fornita");
			return;
		}
		CassaEconomale cassaEconomale = ComparatorUtils.searchByUidEventuallyNull(model.getListaCasseEconomali(), model.getCassaEconomale());
		checkNotNullNorInvalidUid(cassaEconomale, "Cassa economale");
		
		model.setCassaEconomale(cassaEconomale);
	}
	
	/**
	 * Ingresso nello step2 dell'elaborazione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enterStep2() {
		final String methodName = "enterStep2";
		
		RicercaSinteticaRendicontoCassaDaStampare req = model.creaRequestRicercaSinteticaRendicontoCassaDaStampare();
		RicercaSinteticaRendicontoCassaDaStampareResponse res = stampaCassaEconomaleService.ricercaSinteticaRendicontoCassaDaStampare(req);
		
		// Controllo gli errori
		if(res.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errori nell'invocazione della chiamata al servizio");
			addErrori(res);
			return INPUT;
		}
		
		log.info(methodName, "res.getNumeroOperazioniDiCassa() "+res.getNumeroOperazioniDiCassa()); 
		
		if(res.getTotaleElementi() == 0 && res.getNumeroOperazioniDiCassa() ==0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}
		
		
		//SIAC-5890
		model.setNumeroOperazioniDiCassa(res.getNumeroOperazioniDiCassa());
		
		log.debug(methodName, "Totale: " + res.getTotaleElementi());
		
		// Imposto in sessione i dati
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_SINTETICA_RENDICONTO_CASSA_DA_STAMPARE, req);
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_RENDICONTO_CASSA_DA_STAMPARE, res.getListaMovimenti());
		
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		// Imposto i totali nel model
		model.setNumeroTotaleMovimenti(Long.valueOf(res.getTotaleElementi()));
		model.setImportoTotaleMovimenti(res.getImportoTotale());
		//SIAC-6450
		model.setImportoTotaleSenzaMovimentiAnnullati(res.getImportoTotaleSenzaMovimentiAnnullati());
		
		// Imposto la pagina 0
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, Integer.valueOf(0));
		
		return SUCCESS;
	}
	
	/**
	 * Step2 dell'esecuzione. Metodo di appoggio per il refresh della pagina
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String step2() {
		Integer startPage = sessionHandler.getParametro(BilSessionParameter.RIENTRO_POSIZIONE_START);
		model.setStartPage(startPage);
		return SUCCESS;
	}
	
	/**
	 * Redirezione allo step 1
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String backToStep1() {
		// Pulisco il form
		model.setStampaRendiconto(new StampaRendiconto());
		model.setTipoStampa(null);
		model.setPeriodoDaRendicontareDataFine(null);
		model.setPeriodoDaRendicontareDataInizio(null);
		model.setNumeroRendiconto(null);
		
		model.setSoggetto(null);
		model.setModalitaPagamentoSoggetto(null);
		model.setCausale(null);
		model.setStrutturaAmministrativoContabile(null);
		model.setAnticipiSpesaDaInserire(null);
		
		model.impostaDatiNelModel();
		return SUCCESS;
	}
	
	/**
	 * Redirezione allo step 2
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String backToStep2() {
		// Pulisco il form
		model.setSoggetto(null);
		model.setModalitaPagamentoSoggetto(null);
		model.setCausale(null);
		model.setStrutturaAmministrativoContabile(null);
		model.setAnticipiSpesaDaInserire(null);
		
		return SUCCESS;
	}
	
	/**
	 * Calcola il suffisso per il result
	 * @return il suffisso da apporre al result
	 */
	private String resultSuffix() {
		return model.isStampaDefinitiva() ? "_definitiva" : "_bozza";
	}
	
	/**
	 * Ingresso nello step 3.
	 * <br/>
	 * Nel caso di stampa in BOZZA, redirigo subito allo step 4.
	 * <br/>
	 * Nel caso di stampa in DEFINITIVA, redirigo allo step 3.
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enterStep3() {
		final String methodName = "enterStep3";
		try {
			caricaListaClasseSoggetto();
		} catch(WebServiceInvocationFailureException wsife) {
			log.info(methodName, wsife.getMessage());
			return INPUT;
		}
		model.setSoggetto(null);
		model.setModalitaPagamentoSoggetto(null);
		model.setStrutturaAmministrativoContabile(null);
		model.setAnticipiSpesaDaInserire(Boolean.TRUE);
		
		prepopolaCausale();
		
		return SUCCESS + resultSuffix();
	}
	
	/**
	 * Prepopolamento della causale.
	 * <br/>
	 * Causale Atto: Il campo va preimpostato con il seguente testo: "Reintegro fondo economale – Rendiconto N."
	 * + numero rendiconto + "cassa" + descrizione cassa + "da " + intervallo date + "a" + date
	 */
	private void prepopolaCausale() {
		StringBuilder sb = new StringBuilder()
				.append("Reintegro fondo economale – Rendiconto N. ")
				.append(model.getNumeroRendiconto())
				.append(" cassa ")
				.append(model.getCassaEconomale().getDescrizione())
				.append(" da ")
				.append(FormatUtils.formatDate(model.getPeriodoDaRendicontareDataInizio()))
				.append(" a ")
				.append(FormatUtils.formatDate(model.getPeriodoDaRendicontareDataFine()));
		model.setCausale(sb.toString());
	}

	/**
	 * Carica la lista delle classi del soggetto.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	private void caricaListaClasseSoggetto() throws WebServiceInvocationFailureException {
		List<CodificaFin> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO);
		if(listaInSessione == null) {
			ListeGestioneSoggetto req = model.creaRequestListeGestioneSoggetto();
			ListeGestioneSoggettoResponse res = soggettoService.listeGestioneSoggetto(req);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(res);
				throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(req, res));
			}
			
			listaInSessione = res.getListaClasseSoggetto();
			sessionHandler.setParametro(BilSessionParameter.LISTA_CLASSI_SOGGETTO, listaInSessione);
		}
		
		model.setListaClasseSoggetto(listaInSessione);
	}
	/**
	 * Step3 dell'esecuzione. Metodo di appoggio per il refresh della pagina
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String step3() {
		return SUCCESS;
	}
	/**
	 * Preparazione per il metodo {@link #enterStep4()}
	 */
	public void prepareEnterStep4() {
		model.setSoggetto(null);
		model.setModalitaPagamentoSoggetto(null);
		model.setCausale(null);
		model.setStrutturaAmministrativoContabile(null);
		model.setAnticipiSpesaDaInserire(null);
	}
	/**
	 * Ingresso nello step 4
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String enterStep4() {
		final String methodName = "enterStep4";
		// Validazione: richiamo qua per dividere il result in due
		validazioneEnterStep4();
		if(hasErrori()) {
			log.info(methodName, "Errori di validazione della action");
			return INPUT + resultSuffix();
		}
		
		// Stampa
		log.debug(methodName, " STAMPA IN CORSO....");

		StampaRendicontoCassa req = model.creaRequestStampaRendicontoCassa();
		StampaRendicontoCassaResponse res = stampaCassaEconomaleService.stampaRendicontoCassa(req);

		if (res.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(req, res));
			addErrori(res);
			return INPUT + resultSuffix();
		}

		log.debug(methodName, "Invocazione del servizio Stampa rendicontoCassa avvenuta con successo");
		impostaMessaggioStampaPresaInCarico();
		setInformazioniMessaggiErroriInSessionePerActionSuccessiva();

		return SUCCESS;
	}
	/**
	 * Validazione per il metodo {@link #enterStep4()}
	 */
	private void validazioneEnterStep4() {
		String methodName = "validateEnterStep4";
		if(!model.isStampaDefinitiva()) {
			// Nulla da validare
			log.debug(methodName, "Bozza: non vi e' alcunche' da validare");
			return;
		}
		
		checkCondition(model.getSoggetto() != null && StringUtils.isNotBlank(model.getSoggetto().getCodiceSoggetto()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Soggetto"));
		checkNotNullNorInvalidUid(model.getModalitaPagamentoSoggetto(), "Modalita' di pagamento soggetto");
		checkNotNullNorInvalidUid(model.getStrutturaAmministrativoContabile(), "Struttura amministrativo contabile");
		checkNotNullNorEmpty(model.getCausale(), "Causale atto");
		checkNotNull(model.getAnticipiSpesaDaInserire(), "Richieste di anticipi missione presenti in allegato atto");
		
		try {
			validateSoggetto();
		} catch(ParamValidationException pve) {
			log.info(methodName, "Errore di validazione del soggetto: " + pve.getMessage());
		}
	}
	/**
	 * Validazione del soggetto
	 */
	private void validateSoggetto() {
		final String methodName = "validateSoggetto";

		checkNotNull(model.getSoggetto(), "Soggetto", true);
		checkNotNullNorEmpty(model.getSoggetto().getCodiceSoggetto(), "Soggetto", true);
		
		Soggetto soggetto = sessionHandler.getParametro(BilSessionParameter.SOGGETTO);
		if(soggetto == null || !model.getSoggetto().getCodiceSoggetto().equals(soggetto.getCodiceSoggetto())) {
			log.debug(methodName, "Caricamento dei dati da servizio");
			RicercaSoggettoPerChiave req = model.creaRequestRicercaSoggettoPerChiave();
			RicercaSoggettoPerChiaveResponse res = soggettoService.ricercaSoggettoPerChiave(req);
			
			// Controllo gli errori
			if(res.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(res);
				throw new ParamValidationException(createErrorInServiceInvocationString(req, res));
			}
			soggetto = res.getSoggetto();
			
			// Le liste possono tranquillamente essere null. Pertanto, per sicurezza fornisco come default una lista vuota
			List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = defaultingList(res.getListaSecondariaSoggetto());
			List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = defaultingList(res.getListaModalitaPagamentoSoggetto());
			
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
	 * Step4 dell'esecuzione. Metodo di appoggio per il refresh della pagina
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String step4() {
		leggiEventualiErroriMessaggiInformazioniAzionePrecedente();
		return SUCCESS;
	}
	
	/**
	 * Caricamento dell'ultima stampa definitiva.
	 * 
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	public String caricaUltimaDefinitiva() {
		String methodName="caricaUltimaDefinitiva";
		RicercaUltimoRendicontoCassaStampato request = model.creaRequestRicercaUltimoRendicontoCassaStampato();
		logServiceRequest(request);
		RicercaUltimoRendicontoCassaStampatoResponse response = stampaCassaEconomaleService.ricercaUltimoRendicontoCassaStampato(request);
		logServiceResponse(response);
		if (response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(request, response));
			addErrori(response);
			return SUCCESS;
		}

		if (response.getStampeCassaFile() != null && response.getStampeCassaFile().getStampaRendiconto() != null) {
			model.setStampaRendiconto(response.getStampeCassaFile().getStampaRendiconto());
			model.setNumeroRendiconto(Integer.valueOf(response.getStampeCassaFile().getStampaRendiconto().getNumeroRendiconto().intValue() + 1));
		} else {
			model.impostaStampaRendiconto();
			model.setNumeroRendiconto(Integer.valueOf(1));
		}
		return SUCCESS;
		
	}

}
