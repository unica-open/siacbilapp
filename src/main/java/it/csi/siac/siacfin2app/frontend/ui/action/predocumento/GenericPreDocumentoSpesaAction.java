/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.predocumento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Entita.StatoEntita;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.predocumento.GenericPreDocumentoSpesaModel;
import it.csi.siac.siacfin2app.frontend.ui.util.helper.VerificaBloccoRORHelper;
import it.csi.siac.siacfin2ser.frontend.webservice.ContoTesoreriaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreriaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.ProvvisorioService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassaResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto.TipoAccredito;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe di action generica per il PreDocumento di Spesa
 * 
 * @author Marchino Alessandro,Nazha Ahmad
 * @version 1.0.0 - 15/04/2014
 * @version 1.0.1 - 11/06/2015
 * 
 * @param <M> la parametrizzazione del model
 *
 */

public class GenericPreDocumentoSpesaAction<M extends GenericPreDocumentoSpesaModel> extends GenericPreDocumentoAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5753785789350319842L;
	
	/** Serviz&icirc; del capitolo di uscita gestione */
	@Autowired protected transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	/** Serviz&icirc; del provvisorio */
	@Autowired protected transient ProvvisorioService provvisorioService;
	@Autowired protected transient ContoTesoreriaService contoTesoreriaService;

	/**
	 * Carica le liste di Sede Secondaria e Modalita Pagamento.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ottieniListaSedeSecondariaEModalitaPagamento() {
		caricaListeSedeSecondariaModalitaPagamentoDaSessione();
		return SUCCESS;
	}
	
	@Override
	protected void impostazioneModalitaPagamentoESedeSecondaria(List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto, List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto) {
		model.setListaSedeSecondariaSoggetto(listaSedeSecondariaSoggetto);
		model.setListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
	}
	
	/**
	 * Effettua una validazione del Capitolo fornito in input.
	 * 
	 * @return <code>true</code> se la validazione &eacute; andata a buon fine; <code>false</code> in caso contrario
	 */
	protected boolean validazioneCapitolo() {
		CapitoloUscitaGestione capitoloUscitaGestione = model.getCapitolo();
		// Se non ho il capitolo, sono a posto
		if(capitoloUscitaGestione == null
			|| (
				// Gestione UEB: posso avere un qualsiasi campo
				model.isGestioneUEB()
				&& capitoloUscitaGestione.getNumeroCapitolo() == null
				&& capitoloUscitaGestione.getNumeroArticolo() == null
				&& capitoloUscitaGestione.getNumeroUEB() == null
			) || (
				// Gestione NoUEB: ignoro il caso in cui ho solo l'UEB
				capitoloUscitaGestione.getNumeroCapitolo() == null
				&& capitoloUscitaGestione.getNumeroArticolo() == null
			)) {
			return false;
		}
		
		RicercaSinteticaCapitoloUscitaGestione request = model.creaRequestRicercaSinteticaCapitoloUscitaGestione();
		logServiceRequest(request);
		RicercaSinteticaCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return false;
		}
		
		StringBuilder classificazioneCapitolo = new StringBuilder()
				.append(capitoloUscitaGestione.getAnnoCapitolo())
				.append("/")
				.append(capitoloUscitaGestione.getNumeroCapitolo())
				.append("/")
				.append(capitoloUscitaGestione.getNumeroArticolo());
		if(model.isGestioneUEB()) {
			classificazioneCapitolo.append("/")
				.append(capitoloUscitaGestione.getNumeroUEB());
		}
		
		int totaleElementi = response.getTotaleElementi();
		checkCondition(totaleElementi > 0, ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo", classificazioneCapitolo.toString()));
		checkCondition(totaleElementi < 2, ErroreFin.OGGETTO_NON_UNIVOCO.getErrore("Capitolo"));
		
		if(totaleElementi == 1) {
			// Imposto i dati del capitolo
			model.setCapitolo(response.getCapitoli().get(0));
		}
		return true;
	}
	
	/**
	 * Effettua una validazione dell'impegno e del subimpegno forniti in input.
	 */
	protected void validazioneImpegnoSubImpegno(Integer flagProvenienzaCduBloccoROR) {
		Impegno impegno = model.getMovimentoGestione();
		SubImpegno subImpegno = model.getSubMovimentoGestione();
		
		boolean impegnoValorizzato = impegno != null && impegno.getAnnoMovimento() != 0 && impegno.getNumeroBigDecimal() != null;
		boolean subImpegnoValorizzato = subImpegno != null && subImpegno.getNumeroBigDecimal() != null;
		
		checkCondition(impegnoValorizzato || !subImpegnoValorizzato , ErroreCore.INCONGRUENZA_NEI_PARAMETRI.getErrore("per indicare un subimpegno e' necessario indicare anche un impegno."), true);
		
		if(!impegnoValorizzato){
			return;
		}
		
	
		RicercaImpegnoPerChiaveOttimizzato request = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();
		logServiceRequest(request);
		RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return;
		}
		
		if(response.isFallimento() || response.getImpegno() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", impegno.getAnnoMovimento()+"/"+impegno.getNumeroBigDecimal()));
			return;
		}
		
		impegno = response.getImpegno();
		
		model.setMovimentoGestione(impegno);
		
		if(subImpegno != null && subImpegno.getNumeroBigDecimal() != null) {
			BigDecimal numero = subImpegno.getNumeroBigDecimal();
			// Controlli di validità sull'impegno
			subImpegno = findSubImpegnoLegatoImpegnoByNumero(response.getImpegno(), subImpegno);
			if(subImpegno == null) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("SubImpegno", impegno.getAnnoMovimento() + "/" + impegno.getNumeroBigDecimal() + "-" + numero));
				return;
			}
			model.setSubMovimentoGestione(subImpegno);
			// Controllo stato
			checkCondition("D".equals(subImpegno.getStatoOperativoMovimentoGestioneSpesa()), ErroreFin.SUBIMPEGNO_NON_IN_STATO_DEFINITIVO.getErrore());
			
		} else {
			// Controlli di validità sul subimpegno
			checkCondition("D".equals(impegno.getStatoOperativoMovimentoGestioneSpesa()), ErroreFin.IMPEGNO_NON_IN_STATO_DEFINITIVO.getErrore());
		}
		
		// Controllo anno
		checkCondition(impegno.getAnnoMovimento() <= model.getAnnoEsercizioInt().intValue(), 
				ErroreCore.FORMATO_NON_VALIDO.getErrore("subimpegno", "l'anno deve essere non superiore all'anno di esercizio"));
		
		if(flagProvenienzaCduBloccoROR != null && flagProvenienzaCduBloccoROR.intValue() == 1){
			boolean test = VerificaBloccoRORHelper.escludiImpegnoPerBloccoROR(sessionHandler.getAzioniConsentite(), impegno,  model.getAnnoEsercizioInt());
			if(test){
				checkCondition(!test, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impegno/sub impegno residuo non utilizzabile"));
			}else if(impegno.getElencoSubImpegni() != null && !impegno.getElencoSubImpegni().isEmpty()){
				for(int k = 0; k < impegno.getElencoSubImpegni().size(); k++){
					test = VerificaBloccoRORHelper.escludiImpegnoPerBloccoROR(sessionHandler.getAzioniConsentite(), impegno.getElencoSubImpegni().get(k), model.getAnnoEsercizioInt());
					if(test)
						break;
				}
				if(test){
					checkCondition(!test, ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("Impegno/sub impegno residuo non utilizzabile"));
				}
			}
		}
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
				if(s.getNumeroBigDecimal().compareTo(subImpegno.getNumeroBigDecimal()) == 0) {
					result = s;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Controlla la congruenza tra il capitolo e l'impegno.
	 */
	protected void controlloCongruenzaCapitoloImpegno() {
		final String methodName = "controlloCongruenzaCapitoloImpegno";
		
		CapitoloUscitaGestione capitoloUscitaGestione = model.getCapitolo();
		Impegno impegno = model.getMovimentoGestione();
		// Se non ho capitolo o impegno, sono a posto
		if(capitoloUscitaGestione == null || impegno == null || capitoloUscitaGestione.getUid() == 0 || impegno.getUid() == 0) {
			return;
		}
		
		CapitoloUscitaGestione capitoloUscitaGestioneImpegno = impegno.getCapitoloUscitaGestione();
		
		checkCondition(capitoloUscitaGestioneImpegno == null || (
				capitoloUscitaGestione.getNumeroCapitolo().equals(capitoloUscitaGestioneImpegno.getNumeroCapitolo()) &&
				capitoloUscitaGestione.getNumeroArticolo().equals(capitoloUscitaGestioneImpegno.getNumeroArticolo()) &&
				capitoloUscitaGestione.getNumeroUEB().equals(capitoloUscitaGestioneImpegno.getNumeroUEB())
			), ErroreCore.VALORE_NON_CONSENTITO.getErrore("capitolo", "in quanto non corrisponde al capitolo dell'impegno"));
		// Log
		if(log.isDebugEnabled() && capitoloUscitaGestioneImpegno != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("Capitolo impegno - numero: ")
				.append(capitoloUscitaGestioneImpegno.getNumeroCapitolo())
				.append(" --- ")
				.append("Capitolo - numero: ")
				.append(capitoloUscitaGestione.getNumeroCapitolo())
				.append(" --- ---")
				.append("Capitolo impegno - numero articolo: ")
				.append(capitoloUscitaGestioneImpegno.getNumeroArticolo())
				.append(" --- ")
				.append("Capitolo - numero articolo: ")
				.append(capitoloUscitaGestione.getNumeroArticolo())
				.append(" --- ---")
				.append("Capitolo impegno - numero UEB: ")
				.append(capitoloUscitaGestioneImpegno.getNumeroUEB())
				.append(" --- ")
				.append("Capitolo - numero UEB: ")
				.append(capitoloUscitaGestione.getNumeroUEB());
			log.debug(methodName, sb.toString());
		}
	}
	
	/**
	 * Carica la lista del Tipo Causale.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	protected void caricaListaTipoCausale() throws WebServiceInvocationFailureException {
		List<TipoCausale> listaInSessione = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_CAUSALE_SPESA);
		if(listaInSessione == null) {
			LeggiTipiCausaleSpesa request = model.creaRequestLeggiTipiCausaleSpesa();
			logServiceRequest(request);
			LeggiTipiCausaleSpesaResponse response = preDocumentoSpesaService.leggiTipiCausaleSpesa(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaTipoCausale");
			}
			
			listaInSessione = response.getTipiCausale();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_CAUSALE_SPESA, listaInSessione);
		}
		
		model.setListaTipoCausale(listaInSessione);
	}
	
	/**
	 * Carica la lista della Causale Spesa.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	protected void caricaListaCausaleSpesa() throws WebServiceInvocationFailureException {
		List<CausaleSpesa> list = sessionHandler.getParametro(BilSessionParameter.LISTA_CAUSALE_SPESA);
		
		// Prima controllo se la il tipoCausale sia presente
		if(model.getTipoCausale() != null && model.getTipoCausale().getUid() != 0 && (list == null || list.isEmpty())) {
			RicercaSinteticaCausaleSpesa request = model.creaRequestRicercaSinteticaCausaleSpesa();
			logServiceRequest(request);
			RicercaSinteticaCausaleSpesaResponse response = preDocumentoSpesaService.ricercaSinteticaCausaleSpesa(request);
			logServiceResponse(response);
			
			// Controllo gli errori
			if(response.hasErrori()) {
				//si sono verificati degli errori: esco.
				addErrori(response);
				throw new WebServiceInvocationFailureException("caricaListaCausaleSpesa");
			}
			list = response.getCausaliSpesa();
		}
		sessionHandler.setParametro(BilSessionParameter.LISTA_CAUSALE_SPESA, list);
		model.setListaCausaleSpesa(list);
	}
	
	/**
	 * Carica la lista del Conto Tesoreria.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	@Override
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
				throw new WebServiceInvocationFailureException("caricaListaContoTesoreria");
			}
			
			listaInSessione = response.getContiTesoreria();
			sessionHandler.setParametro(BilSessionParameter.LISTA_CONTO_TESORERIA, listaInSessione);
		}
		
		model.setListaContoTesoreria(listaInSessione);
	}
	
	/**
	 * Carica la lista delle sedi secondarie e delle modalit&agrave; di pagamento a partire dal soggetto selezionato, se presente.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui l'invocazione del servizio fallisca
	 */
	protected void caricaListaSedeSecondariaSoggettoEModalitaPagamentoSoggetto() throws WebServiceInvocationFailureException {
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
			throw new WebServiceInvocationFailureException("caricaListaSedeSecondariaSoggettoEModalitaPagamentoSoggetto");
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
	
	@Override
	protected List<ModalitaPagamentoSoggetto> impostaListaModalitaPagamentoSoggetto(List<ModalitaPagamentoSoggetto> lista) {
		List<ModalitaPagamentoSoggetto> result = new ArrayList<ModalitaPagamentoSoggetto>();
		for(ModalitaPagamentoSoggetto mps : lista) {
			// TODO: trovare la data di scadenza della modalita di pagamento del soggetto
			if(!isTipoAccreditoCBOrCCP(mps.getTipoAccredito()) || (StatoEntita.VALIDO.equals(mps.getStato()) && isEqualsOrAfterToday(null))) {
				result.add(mps);
			}
		}
		
		return result;
	}
	
	/**
	 * Controlla se il tipo di accredito sia CB oppure CCP.
	 * 
	 * @param tipoAccredito il tipo di accredito da controllare
	 * @return <code>true</code> se la condizione &eacute; rispettata; <code>false</code> altrimenti
	 */
	private boolean isTipoAccreditoCBOrCCP(TipoAccredito tipoAccredito) {
		return TipoAccredito.CB.equals(tipoAccredito) || TipoAccredito.CCP.equals(tipoAccredito);
	}
	
	/**
	 * Controlla se la data sia pari o successiva alla data odierna.
	 * 
	 * @param date la data da controllare
	 * 
	 * @return <code>true</code> se la data &eacute; null, successiva o pari alla data odierna; <code>false</code> altrimenti
	 */
	private boolean isEqualsOrAfterToday(Date date) {
		if(date == null) {
			return true;
		}
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date today = cal.getTime();
		return !date.before(today);
	}
	
	/**
	 * Carica dalla sessione le liste della Sede Secondaria e della Modalita Pagamento.
	 */
	protected void caricaListeSedeSecondariaModalitaPagamentoDaSessione() {
		List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO);
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = sessionHandler.getParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO);
		
		model.setListaSedeSecondariaSoggetto(listaSedeSecondariaSoggetto);
		model.setListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
	}
	
	
	/**
	 * Effettua una validazione dell'impegno e del subimpegno forniti in input.
	 * 
	 * @return <code>true</code> se la validazione &eacute; andata a buon fine; <code>false</code> in caso contrario
	 */
	protected boolean validazioneImpegnoSubImpegnoPerRicerca() {
		Impegno impegno = model.getMovimentoGestione();
		SubImpegno subImpegno = model.getSubMovimentoGestione();
		
		if(impegno == null || (
				impegno.getAnnoMovimento() == 0 ||
				impegno.getNumeroBigDecimal() == null
				)) {
			return false;
		}
		
		RicercaImpegnoPerChiaveOttimizzato request = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();
		logServiceRequest(request);
		RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return false;
		}
		
		if(response.isFallimento() || response.getImpegno() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Impegno", impegno.getAnnoMovimento()+"/"+impegno.getNumeroBigDecimal()));
			return false;
		}
		
		impegno = response.getImpegno();
		
		model.setMovimentoGestione(impegno);
		
		if(subImpegno != null && subImpegno.getNumeroBigDecimal() != null) {
			BigDecimal numero = subImpegno.getNumeroBigDecimal();
			// Controlli di validita' sull'impegno
			subImpegno = findSubImpegnoLegatoImpegnoByNumero(response.getImpegno(), subImpegno);
			if(subImpegno == null) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("SubImpegno", impegno.getAnnoMovimento() + "/" + impegno.getNumeroBigDecimal() + "-" + numero));
				return true;
			}
			model.setSubMovimentoGestione(subImpegno);
		}
		
		return true;
	
	}
	
	/**
	 * richiama la ricerca 
	 */
	protected void validazioneProvvisorioDiCassaPredocumentoDiSpesa(){
		if(model.getProvvisorioCassa() == null || model.getProvvisorioCassa().getNumero() == null || model.getProvvisorioCassa().getAnno() == null) {
			return;
		}
		
		final String methodName = "validazioneProvvisorioDiCassaPredocumentoDiSpesa";
		
		model.getProvvisorioCassa().setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa.S);
		RicercaProvvisoriDiCassa request = model.creaRequestRicercaProvvisorioDiCassa();
		logServiceRequest(request);
		RicercaProvvisoriDiCassaResponse response = provvisorioService.ricercaProvvisoriDiCassa(request);
		logServiceResponse(response);

		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, createErrorInServiceInvocationString(RicercaProvvisoriDiCassa.class, response));
			addErrori(response);
			return;
		}

		checkCondition(response.getElencoProvvisoriDiCassa() != null && !response.getElencoProvvisoriDiCassa().isEmpty(), ErroreFin.DATI_PROVVISORIO_QUIETANZA_ERRATI.getErrore(), true);
		ProvvisorioDiCassa provvisorioDiCassaFromResponse = response.getElencoProvvisoriDiCassa().get(0);
		checkCondition(provvisorioDiCassaFromResponse != null, ErroreFin.DATI_PROVVISORIO_QUIETANZA_ERRATI.getErrore(),true);

		checkCondition(TipoProvvisorioDiCassa.S.equals(provvisorioDiCassaFromResponse.getTipoProvvisorioDiCassa())
				&& provvisorioDiCassaFromResponse.getDataRegolarizzazione() == null
				&& provvisorioDiCassaFromResponse.getDataAnnullamento() == null,
					ErroreFin.DATI_PROVVISORIO_QUIETANZA_ERRATI.getErrore());
		String keyProvvisorio = provvisorioDiCassaFromResponse.getAnno() + " - " + provvisorioDiCassaFromResponse.getNumero();
		model.setProvvisorioCassa(provvisorioDiCassaFromResponse);
		checkCondition(isProvvisorioDiCassaRegolarizzabile(model.getPreDocumento().getImporto()),
				ErroreFin.PROVVISORIO_NON_REGOLARIZZABILE.getErrore("inserimento", "predisposizione di pagamento", keyProvvisorio,
						"L'importo della predisposizione supera l'importo da regolarizzare del provvisorio"));
	}
	

	
	

}
