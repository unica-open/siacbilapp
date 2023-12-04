/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.causale;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.causale.GenericCausaleSpesaModel;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoSpesaService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleSpesaResponse;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Action astratta per la gestione della Causale di spesa.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 04/02/2014
 *
 * @param <M> la parametrizzazione del Model
 */
public class GenericCausaleSpesaAction<M extends GenericCausaleSpesaModel> extends GenericCausaleAction<M> {
	/** Per la serializzazione */
	private static final long serialVersionUID = 2251780291217335107L;
	
	/** Serviz&icirc; del predocumento di spesa */
	@Autowired protected transient PreDocumentoSpesaService preDocumentoSpesaService;
	/** Serviz&icirc; del movimento di gestione */
	@Autowired protected transient MovimentoGestioneService movimentoGestioneService;
	/** Serviz&icirc; del capitolo di uscita gestione */
	@Autowired private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	
	@Override
	public String ottieniListaSedeSecondariaEModalitaPagamento() {
		caricaListeSedeSecondariaModalitaPagamentoDaSessione();
		return SUCCESS;
	}
	
	/**
	 * Carica le liste necessarie per il completamento delle pagine di inserimento e ricerca.
	 * 
	 * @throws WebServiceInvocationFailureException nel caso in cui le invocazioni dei servizi ritornino un fallimento
	 * 
	 */
	public void caricaListeCodifiche() throws WebServiceInvocationFailureException {
		final String methodName = "caricaListeCodifiche";
		log.debugStart(methodName, "caricaListaTipiAtto e caricaListaTipiCausale");
		caricaListaTipiAtto();
		caricaListaTipiCausale();
		caricaListaClasseSoggetto();
		caricaListaTipoFinanziamento();
		log.debugEnd(methodName, "caricaListaTipiAtto e caricaListaTipiCausale");
	}
	
	/* Response */
	
	/**
	 * @method 		 getLeggiTipiCausaleSpesaResponse
	 * @return		 la response corrispondente
	 */
	protected LeggiTipiCausaleSpesaResponse getLeggiTipiCausaleSpesaResponse() {
		LeggiTipiCausaleSpesa request = model.creaRequestLeggiTipiCausaleSpesa();
		logServiceRequest(request);
		LeggiTipiCausaleSpesaResponse response = preDocumentoSpesaService.leggiTipiCausaleSpesa(request);
		logServiceResponse(response);
		return response;
	}
	
	
	/**
	 * Effettua una ricerca di dettaglio della causale
	 */
	protected void ricercaDettaglioCausale() {
		final String methodName = "ricercaDettaglioCausale";
		RicercaDettaglioCausaleSpesa request = model.creaRequestRicercaDettaglioCausaleSpesa();
		logServiceRequest(request);
		RicercaDettaglioCausaleSpesaResponse response = preDocumentoSpesaService.ricercaDettaglioCausaleSpesa(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.error(methodName, "Errore nell'invocazione del servizio di ricerca dettaglio causale spesa");
			addErrori(response);
			
			throwExceptionFromErrori(response.getErrori());
		}
		
		log.debug(methodName, "Causale caricata. Imposto i dati nel model");
		model.impostaCausale(response.getCausaleSpesa());
	}

	/* Metodi di utilita' */

	
	/**
	 * Carica la lista dei tipi di causale
	 * 
	 */
	private void caricaListaTipiCausale() {
		final String methodName = "caricaListaTipiCausale";
		log.debug(methodName, "Caricamento della lista dei tipi di causale");
		
		List<TipoCausale> listaTipiCausaleSpesa = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_CAUSALE_SPESA);
		
		if (listaTipiCausaleSpesa == null) {
			log.debug(methodName, "Caricamento lista tipi di causale");
			LeggiTipiCausaleSpesaResponse responseAL = getLeggiTipiCausaleSpesaResponse();
			
			// aggiungere  nel response
			listaTipiCausaleSpesa = responseAL.getTipiCausale();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_CAUSALE_SPESA, listaTipiCausaleSpesa);
		}
		model.setListaTipoCausale(listaTipiCausaleSpesa);
	}
	
	/**
	 * Effettua una validazione del Capitolo fornito in input.
	 */
	protected void validazioneCapitolo() {
		CapitoloUscitaGestione capitoloUscitaGestione = model.getCapitolo();
		// Se non ho il capitolo, sono a posto
		if(!checkCapitoloEsistente(capitoloUscitaGestione)) {
			return;
		}
		
		RicercaSinteticaCapitoloUscitaGestione request = model.creaRequestRicercaSinteticaCapitoloUscitaGestione();
		logServiceRequest(request);
		RicercaSinteticaCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaSinteticaCapitoloUscitaGestione(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return;
		}
		
		StringBuilder classificazioneCapitolo =  new StringBuilder()
				.append(capitoloUscitaGestione.getAnnoCapitolo())
				.append("/")
				.append(capitoloUscitaGestione.getNumeroCapitolo())
				.append("/")
				.append(capitoloUscitaGestione.getNumeroArticolo());
		if(model.isGestioneUEB()) {
			classificazioneCapitolo
				.append("/")
				.append(capitoloUscitaGestione.getNumeroUEB());
		}
		
		int totaleElementi = response.getTotaleElementi();
		checkCondition(totaleElementi > 0, ErroreCore.ENTITA_NON_TROVATA.getErrore("Capitolo", classificazioneCapitolo.toString()));
		checkCondition(totaleElementi < 2, ErroreFin.OGGETTO_NON_UNIVOCO.getErrore("Capitolo"));
		
		if(totaleElementi == 1) {
			// Imposto i dati del capitolo
			model.setCapitolo(response.getCapitoli().get(0));
		}
	}
	
	/**
	 * Effettua una validazione dell'impegno e del subimpegno forniti in input.
	 */
	protected void validazioneImpegnoSubImpegno() {
		Impegno impegno = model.getMovimentoGestione();
		SubImpegno subImpegno = model.getSubMovimentoGestione();
		
		if(impegno == null || (impegno.getAnnoMovimento() == 0 || impegno.getNumeroBigDecimal() == null)) {
			return;
		}
		
		RicercaImpegnoPerChiaveOttimizzato request = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();
		RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(request);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return;
		}
		if(response.isFallimento() || response.getImpegno() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", impegno.getAnnoMovimento() + "/" + impegno.getNumeroBigDecimal().toPlainString()));
			return;
		}
		
		impegno = response.getImpegno();
		
		model.setMovimentoGestione(impegno);
		
		if(subImpegno != null && subImpegno.getNumeroBigDecimal() != null) {
			BigDecimal numero = subImpegno.getNumeroBigDecimal();
			// Controlli di validità sull'impegno
			subImpegno = findSubImpegnoLegatoImpegnoByNumero(response.getImpegno(), subImpegno);
			if(subImpegno == null) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("SubImpegno", impegno.getAnnoMovimento() + "/" + impegno.getNumeroBigDecimal().toPlainString() + "-" + numero.toPlainString()));
				return;
			}
			model.setSubMovimentoGestione(subImpegno);
			// Controllo stato
			checkCondition("D".equals(subImpegno.getStatoOperativoMovimentoGestioneSpesa()), ErroreFin.SUBIMPEGNO_NON_IN_STATO_DEFINITIVO.getErrore());
			
		} else {
			// Controlli di validità sul subimpegno
			checkCondition("D".equals(impegno.getStatoOperativoMovimentoGestioneSpesa())
					|| "N".equals(impegno.getStatoOperativoMovimentoGestioneSpesa()), ErroreFin.IMPEGNO_NON_IN_STATO_DEFINITIVO.getErrore());
		}
		
		// Controllo anno
		checkCondition(impegno.getAnnoMovimento() <= model.getAnnoEsercizioInt().intValue(), 
				ErroreCore.FORMATO_NON_VALIDO.getErrore("subimpegno", "l'anno deve essere non superiore all'anno di esercizio"));
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
	 * Controlla la congruenza tra il soggetto e l'impegno.
	 */
	protected void controlloConguenzaSoggettoImpegno() {
		Soggetto soggetto = model.getSoggetto();
		Impegno impegno = model.getMovimentoGestione();
		SubImpegno subImpegno = model.getSubMovimentoGestione();
		
		// Se non ho soggetto o impegno, va tutto bene
		boolean soggettoPresente = soggetto != null && StringUtils.isNotBlank(soggetto.getCodiceSoggetto());
		if(!soggettoPresente || impegno == null) {
			return;
		}
		if(StatoOperativoAnagrafica.ANNULLATO.equals(soggetto.getStatoOperativo()) || StatoOperativoAnagrafica.BLOCCATO.equals(soggetto.getStatoOperativo())){
			return;
		}
		
		if(subImpegno != null && subImpegno.getUid() != 0) {
			Soggetto soggettoAssociato = subImpegno.getSoggetto();
			checkCondition(soggettoAssociato == null || soggettoAssociato.getUid() == 0 || soggettoAssociato.getCodiceSoggetto().equals(soggetto.getCodiceSoggetto()), 
					ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto di pagamento", " il subimpegno", "per la causale di pagamento", 
							"subimpegno con soggetto specifico : il soggetto di pagamento deve essere lo stesso del subimpegno "));
		} else {
			Soggetto soggettoAssociato = impegno.getSoggetto();
			checkCondition(soggettoAssociato == null || soggettoAssociato.getUid() == 0 || soggettoAssociato.getCodiceSoggetto().equals(soggetto.getCodiceSoggetto()), 
					ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto di pagamento", " l'impegno", "per la causale di pagamento", 
							"impegno con soggetto specifico : il soggetto di pagamento deve essere lo stesso dell'impegno "));
			// Controllo sulla classe
			checkClasseSoggetto(impegno, soggetto, "soggetto di pagamento", " l'impegno", "per la causale di pagamento", 
					" impegno con classificazione : 'il soggetto del pagamento deve appartenere alla classificazione ");
		}
	}
	
	/**
	 * Controlla la congruenza tra il capitolo e l'impegno.
	 */
	protected void controlloCongruenzaCapitoloImpegno() {
		String methodName = "controlloCongruenzaCapitoloImpegno";
		CapitoloUscitaGestione capitolo = model.getCapitolo();
		Impegno impegno = model.getMovimentoGestione();
		// Se non ho capitolo o impegno, sono a posto
		boolean capitoloPresente = capitolo != null && capitolo.getNumeroCapitolo() != null &&
				capitolo.getNumeroArticolo() != null && capitolo.getNumeroUEB() != null;
		if(!capitoloPresente || impegno == null) {
			return;
		}
		
		CapitoloUscitaGestione capitoloImpegno = impegno.getCapitoloUscitaGestione();
		log.debug(methodName, "Capitolo nell'impegno != null? " + (capitoloImpegno != null));
		
		checkCondition(capitoloImpegno == null || (
				capitolo.getNumeroCapitolo().equals(capitoloImpegno.getNumeroCapitolo()) &&
				capitolo.getNumeroArticolo().equals(capitoloImpegno.getNumeroArticolo()) &&
				capitolo.getNumeroUEB().equals(capitoloImpegno.getNumeroUEB())
			), ErroreCore.FORMATO_NON_VALIDO.getErrore("impegno", "il capitolo e il capitolo associato all'impegno devono essere gli stessi"));
	}

	
	
	/**
	 * Validazione di unicit&agrave; per l'impegno.
	 *
	 * @param listaErrori La lista degli errori da alimentare se necessario
	 */
	protected void verificaUnicitaImpegno(List<Errore> listaErrori) {
		
		RicercaImpegnoPerChiaveOttimizzato request = model.creaRequestRicercaImpegnoPerChiaveOttimizzato();
		RicercaImpegnoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaImpegnoPerChiaveOttimizzato(request);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
		} else {
			if(response.getImpegno() == null) {
				listaErrori.add(ErroreCore.ENTITA_NON_TROVATA.getErrore("Movimento Anno e numero","L'impegno indicato"));
			}
			logServiceResponse(response);
			model.setMovimentoGestione(response.getImpegno());
		}
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

}
