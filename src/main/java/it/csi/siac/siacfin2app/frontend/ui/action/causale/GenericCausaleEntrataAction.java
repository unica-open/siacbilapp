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
import it.csi.siac.siacbilser.frontend.webservice.CapitoloEntrataGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestioneResponse;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.Errore;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.causale.GenericCausaleEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.PreDocumentoEntrataService;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzatoResponse;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;

/**
 * Action astratta per la gestione della Causale di entrata.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 04/02/2014
 *
 * @param <M> la parametrizzazione del Model
 */
public class GenericCausaleEntrataAction<M extends GenericCausaleEntrataModel> extends GenericCausaleAction<M> {
	/** Per la serializzazione */
	private static final long serialVersionUID = 2251780291217335107L;
	
	/** Serviz&icirc; del predocumento di entrata */
	@Autowired protected transient PreDocumentoEntrataService preDocumentoEntrataService;
	/** Serviz&icirc; del movimento di gestione */
	@Autowired protected transient MovimentoGestioneService movimentoGestioneService;
	/** Serviz&icirc; del capitolo di entrata gestione */
	@Autowired private transient CapitoloEntrataGestioneService capitoloEntrataGestioneService;
	
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
	 * @method 		 getLeggiTipiCausaleEntrataResponse
	 * @return		 la response corrispondente
	 */
	protected LeggiTipiCausaleEntrataResponse getLeggiTipiCausaleEntrataResponse() {
		LeggiTipiCausaleEntrata request = model.creaRequestLeggiTipiCausaleEntrata();
		logServiceRequest(request);
		LeggiTipiCausaleEntrataResponse response = preDocumentoEntrataService.leggiTipiCausaleEntrata(request);
		logServiceResponse(response);
		return response;
	}
	
	
	/**
	 * Effettua una ricerca di dettaglio della causale.
	 */
	protected void ricercaDettaglioCausale() {
		final String methodName = "ricercaDettaglioCausale";
		RicercaDettaglioCausaleEntrata request = model.creaRequestRicercaDettaglioCausaleEntrata();
		logServiceRequest(request);
		RicercaDettaglioCausaleEntrataResponse response = preDocumentoEntrataService.ricercaDettaglioCausaleEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.error(methodName, "Errore nell'invocazione del servizio di ricerca dettaglio causale entrata");
			addErrori(response);
			
			throwExceptionFromErrori(response.getErrori());
		}
		
		log.debug(methodName, "Causale caricata. Imposto i dati nel model");
		model.impostaCausale(response.getCausaleEntrata());
	}

	/* Metodi di utilita' */

	
	/**
	 * Carica la lista dei tipi di causale
	 * 
	 */
	private void caricaListaTipiCausale() {
		final String methodName = "caricaListaTipiCausale";
		log.debug(methodName, "Caricamento della lista dei tipi di causale");
		
		List<TipoCausale> listaTipiCausaleEntrata = sessionHandler.getParametro(BilSessionParameter.LISTA_TIPO_CAUSALE_ENTRATA);
		
		if (listaTipiCausaleEntrata == null) {
			log.debug(methodName, "Caricamento lista tipi di causale");
			LeggiTipiCausaleEntrataResponse responseAL = getLeggiTipiCausaleEntrataResponse();
			
			// aggiungere  nel response
			listaTipiCausaleEntrata = responseAL.getTipiCausale();
			sessionHandler.setParametro(BilSessionParameter.LISTA_TIPO_CAUSALE_ENTRATA, listaTipiCausaleEntrata);
		}
		model.setListaTipoCausale(listaTipiCausaleEntrata);
	}
	
	


	
	/**
	 * Effettua una validazione del Capitolo fornito in input.
	 */
	protected void validazioneCapitolo() {
		CapitoloEntrataGestione capitoloEntrataGestione = model.getCapitolo();
		// Se non ho il capitolo, sono a posto
		if(!checkCapitoloEsistente(capitoloEntrataGestione)) {
			return;
		}
		
		RicercaSinteticaCapitoloEntrataGestione request = model.creaRequestRicercaSinteticaCapitoloEntrataGestione();
		logServiceRequest(request);
		RicercaSinteticaCapitoloEntrataGestioneResponse response = capitoloEntrataGestioneService.ricercaSinteticaCapitoloEntrataGestione(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return;
		}
		
		StringBuilder classificazioneCapitolo = new StringBuilder()
				.append(capitoloEntrataGestione.getAnnoCapitolo())
				.append("/")
				.append(capitoloEntrataGestione.getNumeroCapitolo())
				.append("/")
				.append(capitoloEntrataGestione.getNumeroArticolo());
		if(model.isGestioneUEB()) {
			classificazioneCapitolo
				.append("/")
				.append(capitoloEntrataGestione.getNumeroUEB());
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
	 * Effettua una validazione dell'impegno e del subimpegno forniti in input
	 */
	protected void validazioneAccertamentoSubAccertamento() {
		Accertamento accertamento = model.getMovimentoGestione();
		SubAccertamento subAccertamento = model.getSubMovimentoGestione();
		
		if(accertamento == null || (accertamento.getAnnoMovimento() == 0 || accertamento.getNumero() == null)) {
			return;
		}
		
		RicercaAccertamentoPerChiaveOttimizzato request = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato();
		RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(request);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
			return;
		}
		if(response.isFallimento() || response.getAccertamento() == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Accertamento", accertamento.getAnnoMovimento() + "/" + accertamento.getNumero().toPlainString()));
			return;
		}
		
		accertamento = response.getAccertamento();
		
		model.setMovimentoGestione(accertamento);
		
		if(subAccertamento != null && subAccertamento.getNumero() != null) {
			BigDecimal numero = subAccertamento.getNumero();
			// Controlli di validità sull'impegno
			subAccertamento = findSubAccertamentoLegatoAccertamentoByNumero(response.getAccertamento(), subAccertamento);
			if(subAccertamento == null) {
				addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("SubAccertamento", accertamento.getAnnoMovimento() + "/" + accertamento.getNumero().toPlainString() + "-" + numero.toPlainString()));
				return;
			}
			model.setSubMovimentoGestione(subAccertamento);
			// Controllo stato CAMBIARE;: ho un meraviglioso enum StatoOperativoMovimentoGestione.DEFINITIVO.getCodice() 
			checkCondition("D".equals(subAccertamento.getStatoOperativoMovimentoGestioneEntrata()), ErroreFin.SUBACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore(""));
			
		} else {
			// Controlli di validità sul subimpegno
			checkCondition("D".equals(accertamento.getStatoOperativoMovimentoGestioneEntrata()), ErroreFin.ACCERTAMENTO_NON_IN_STATO_DEFINITIVO.getErrore());
		}
		
		// Controllo anno
		checkCondition(accertamento.getAnnoMovimento() <= model.getAnnoEsercizioInt().intValue(), 
				ErroreCore.FORMATO_NON_VALIDO.getErrore("subaccertamento", "l'anno deve essere non superiore all'anno di esercizio"));
	}
	
	

	/**
	 * Trova il subAccertamento nell'elenco degli subimpegni dell'impegno, se presente.
	 * 
	 * @param impegno    l'impegno tra i cui subImpegni trovare quello fornito
	 * @param subAccertamento il subimpegno da cercare
	 * 
	 * @return il subimpegno legato, se presente; <code>null</code> in caso contrario
	 */
	private SubAccertamento findSubAccertamentoLegatoAccertamentoByNumero(Accertamento impegno, SubAccertamento subAccertamento) {
		SubAccertamento result = null;
		if(impegno.getElencoSubAccertamenti() != null) {
			for(SubAccertamento s : impegno.getElencoSubAccertamenti()) {
				if(s.getNumero().compareTo(subAccertamento.getNumero()) == 0) {
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
	protected void controlloConguenzaSoggettoAccertamento() {
		Soggetto soggetto = model.getSoggetto();
		Accertamento accertamento = model.getMovimentoGestione();
		SubAccertamento subAccertamento = model.getSubMovimentoGestione();
		
		// Se non ho soggetto o impegno, va tutto bene
		boolean soggettoPresente = soggetto != null && StringUtils.isNotBlank(soggetto.getCodiceSoggetto());
		if(!soggettoPresente || accertamento == null) {
			return;
		}
		if(StatoOperativoAnagrafica.ANNULLATO.equals(soggetto.getStatoOperativo()) || StatoOperativoAnagrafica.BLOCCATO.equals(soggetto.getStatoOperativo())){
			return;
		}
		
		if(subAccertamento != null && subAccertamento.getUid() != 0) {
			Soggetto soggettoAssociato = subAccertamento.getSoggetto();
			checkCondition(soggettoAssociato == null || soggettoAssociato.getUid() == 0|| soggettoAssociato.getCodiceSoggetto().equals(soggetto.getCodiceSoggetto()), 
					ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto di incasso", " il subaccertamento", "per la causale di incasso", 
							"subaccertamento con soggetto specifico : il soggetto di pagamento deve essere lo stesso del subaccertamento "));
		} else {
			Soggetto soggettoAssociato = accertamento.getSoggetto();
			checkCondition(soggettoAssociato == null || soggettoAssociato.getUid() == 0|| soggettoAssociato.getCodiceSoggetto().equals(soggetto.getCodiceSoggetto()), 
					ErroreFin.SOGGETTO_MOVIMENTO_GESTIONE_NON_VALIDO_PER_INCONGRUENZA.getErrore("soggetto di incasso", " l'accertamento", "per la causale di incasso", 
							"accertamento con soggetto specifico : il soggetto di incasso deve essere lo stesso dell'accertamento "));
			checkClasseSoggetto(accertamento, soggetto, "soggetto di incasso", " l'accertamento", "per la causale di incasso", 
					" accertamento con classificazione : il soggetto dell' incasso deve appartenere alla classificazione ");
		}
		
	}
	
	/**
	 * Controlla la congruenza tra il capitolo e l'impegno.
	 */
	protected void controlloCongruenzaCapitoloAccertamento() {
		String methodName = "controlloCongruenzaCapitoloAccertamento";
		CapitoloEntrataGestione capitolo = model.getCapitolo();
		Accertamento accertamento = model.getMovimentoGestione();
		// Se non ho capitolo o impegno, sono a posto
		boolean capitoloPresente = capitolo != null && capitolo.getNumeroCapitolo() != null &&
				capitolo.getNumeroArticolo() != null && capitolo.getNumeroUEB() != null;
		if(!capitoloPresente || accertamento == null) {
			return;
		}
		
		CapitoloEntrataGestione capitoloAccertamento = accertamento.getCapitoloEntrataGestione();
		log.debug(methodName, "Capitolo nell'accertamento != null? " + (capitoloAccertamento != null));
		
		checkCondition(capitoloAccertamento == null || (
				capitolo.getNumeroCapitolo().equals(capitoloAccertamento.getNumeroCapitolo()) &&
				capitolo.getNumeroArticolo().equals(capitoloAccertamento.getNumeroArticolo()) &&
				capitolo.getNumeroUEB().equals(capitoloAccertamento.getNumeroUEB())
			), ErroreCore.FORMATO_NON_VALIDO.getErrore("accertamento", "il capitolo e il capitolo associato all'accertamento devono essere gli stessi"));
	}

	
	
	/**
	 * Validazione di unicit&agrave; per l'impegno.
	 *
	 * @param listaErrori La lista degli errori da alimentare se necessario
	 */
	protected void verificaUnicitaAccertamento(List<Errore> listaErrori) {
		//TODO: questo non sembra essere mai usato...
		RicercaAccertamentoPerChiaveOttimizzato request = model.creaRequestRicercaAccertamentoPerChiaveOttimizzato();
		RicercaAccertamentoPerChiaveOttimizzatoResponse response = movimentoGestioneService.ricercaAccertamentoPerChiaveOttimizzato(request);
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			addErrori(response);
		} else {
			if(response.getAccertamento() == null) {
				listaErrori.add(ErroreCore.ENTITA_NON_TROVATA.getErrore("Movimento Anno e numero","L'accertamento indicato"));
			}
			logServiceResponse(response);
			model.setMovimentoGestione(response.getAccertamento());
		}
	}

}
