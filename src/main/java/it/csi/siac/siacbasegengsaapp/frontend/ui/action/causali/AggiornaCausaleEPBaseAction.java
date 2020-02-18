/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.causali;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.causali.AggiornaCausaleEPBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.ReflectionUtil;
import it.csi.siac.siaccommonapp.util.exception.ParamValidationException;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.Soggetto.StatoOperativoAnagrafica;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaCausaleResponse;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioCausale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioCausaleResponse;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassificatoreEP;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.Evento;

/**
 * Classe di action per l'aggiornamento della causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 01/04/2015
 * @param <M> la tipizzazione del model
 *
 */
public abstract class AggiornaCausaleEPBaseAction <M extends AggiornaCausaleEPBaseModel>extends BaseInserisciAggiornaCausaleEPBaseAction<M> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 175479691581335856L;


	@Override
	public String annullaStep1() {
		// Riprendo la causale originale
		CausaleEP causaleEPOriginale = model.getCausaleEPOriginale();
		// Clono per effettuare l'aggiornamento
		CausaleEP causaleEP = ReflectionUtil.deepClone(causaleEPOriginale);
		// Imposto i dati nel model
		impostaDatiNelModel(causaleEPOriginale, causaleEP);
		
		return SUCCESS;
	}
	
	@Override
	public String annullaStep2() {
		// Riprendo la causale originale
		CausaleEP causaleEPOriginale = model.getCausaleEPOriginale();
		// Clono la lista
		List<ContoTipoOperazione> listaContoTipoOperazione = ReflectionUtil.deepClone(causaleEPOriginale.getContiTipoOperazione());
		// Rimposta la lista dei conti
		model.setListaContoTipoOperazione(listaContoTipoOperazione);
		
		return SUCCESS;
	}
	
	/**
	 * Caricamento della causale EP.
	 * 
	 * @throws WebServiceInvocationFailureException in caso di fallimento nell'invocazione del servizio
	 */
	public void caricaCausaleEP() throws WebServiceInvocationFailureException {
		final String methodName = "caricaCausaleEP";
		
		// Ricerco il dettaglio della causale
		log.debug(methodName, "Caricamento della causale");
		RicercaDettaglioCausale req = model.creaRequestRicercaDettaglioCausale();
		logServiceRequest(req);
		RicercaDettaglioCausaleResponse res = causaleService.ricercaDettaglioCausale(req);
		logServiceResponse(res);
		
		if(res.hasErrori()) {
			// Errore nel caricamento della causale: importo gli errori ed esco
			String errorMsg = createErrorInServiceInvocationString(req, res);
			log.info(methodName, errorMsg);
			addErrori(res);
			// Lancio l'eccezione
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		if(res.getCausaleEP() == null) {
			// Nessuna causale trovata per id
			String errorMsg = "Nessuna causale corrispondente all'uid " + model.getCausaleEP().getUid();
			log.info(methodName, errorMsg);
			addErrore(ErroreCore.ENTITA_INESISTENTE.getErrore("Causale", model.getCausaleEP().getUid()));
			// Lancio l'eccezione
			throw new WebServiceInvocationFailureException(errorMsg);
		}
		
		// Ho la causale
		CausaleEP causaleEPDaServizio = res.getCausaleEP();
		// Carico i dati del soggetto se necessario
		caricaSoggetto(causaleEPDaServizio);
		
		// Clono per effettuare l'aggiornamento
		CausaleEP causaleEP = ReflectionUtil.deepClone(causaleEPDaServizio);
		// Imposto i dati nel model
		impostaDatiNelModel(causaleEPDaServizio, causaleEP);
	}

	/**
	 * Caricamento dei dati del soggetto se necessario
	 * 
	 * @param causaleEP la causale i cui dati devono essere caricati
	 */
	private void caricaSoggetto(CausaleEP causaleEP) {
		final String methodName = "caricaSoggetto";
		if(causaleEP.getSoggetto() == null || StringUtils.isBlank(causaleEP.getSoggetto().getCodiceSoggetto())) {
			// Non ho necessita' di caricare alcunche'
			return;
		}
		
		Soggetto soggetto = sessionHandler.getParametro(BilSessionParameter.SOGGETTO);
		
		if(soggetto == null || !causaleEP.getSoggetto().getCodiceSoggetto().equals(soggetto.getCodiceSoggetto())) {
			// I dati in sessione non sono presenti o sono non coerenti: ricerco nuovamente il soggetto via servizio
			log.debug(methodName, "Caricamento dei dati da servizio");
			RicercaSoggettoPerChiave req = model.creaRequestRicercaSoggettoPerChiave(causaleEP.getSoggetto());
			logServiceRequest(req);
			RicercaSoggettoPerChiaveResponse res = soggettoService.ricercaSoggettoPerChiave(req);
			logServiceResponse(res);
			
			if(res.hasErrori()) {
				// Errore nella chiamata al servizio: imposto gli errori ed esco
				String errorMsg = createErrorInServiceInvocationString(req, res);
				log.debug(methodName, errorMsg);
				addErrori(res);
				// Lancio l'eccezione
				throw new ParamValidationException(errorMsg);
			}
			soggetto = res.getSoggetto();
			
			// Le liste possono tranquillamente essere null. Pertanto, per sicurezza fornisco come default una lista vuota
			List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = defaultingList(res.getListaSecondariaSoggetto());
			List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = defaultingList(res.getListaModalitaPagamentoSoggetto());
			
			// Eventuale filtro ulteriore
			listaModalitaPagamentoSoggetto = impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
			
			// Imposto in sessione
			sessionHandler.setParametro(BilSessionParameter.SOGGETTO, soggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_SEDE_SECONDARIA_SOGGETTO, listaSedeSecondariaSoggetto);
			sessionHandler.setParametro(BilSessionParameter.LISTA_MODALITA_PAGAMENTO_SOGGETTO, listaModalitaPagamentoSoggetto);
		}
		
		// Controllo l'esistenza del soggetto
		checkCondition(soggetto != null, ErroreCore.ENTITA_INESISTENTE.getErrore("Soggetto", causaleEP.getSoggetto().getCodiceSoggetto()), true);
		checkCondition(!StatoOperativoAnagrafica.PROVVISORIO.equals(soggetto.getStatoOperativo()) && !StatoOperativoAnagrafica.ANNULLATO.equals(soggetto.getStatoOperativo()),
			ErroreCore.OPERAZIONE_INCOMPATIBILE_CON_STATO_ENTITA.getErrore("il soggetto", soggetto.getStatoOperativo()));
		causaleEP.setSoggetto(soggetto);
	}

	/**
	 * Impostazione dei dati nel model.
	 * 
	 * @param causaleEPOriginale la causale originale
	 * @param causaleEP          la causale da aggiornare
	 */
	private void impostaDatiNelModel(CausaleEP causaleEPOriginale, CausaleEP causaleEP) {
		// Ottengo il tipo evento
		impostaTipoEvento(causaleEP);
		model.setCausaleEPOriginale(causaleEPOriginale);
		model.setCausaleEP(causaleEP);
		
		
		// Imposto i campi esterni e cancello dalla causale
		model.setListaContoTipoOperazione(causaleEP.getContiTipoOperazione());
		causaleEP.setContiTipoOperazione(null);
		
		// Piano dei caonti
		model.setElementoPianoDeiConti(causaleEP.getElementoPianoDeiConti());
		causaleEP.setElementoPianoDeiConti(null);
		
		// Soggetto
		model.setSoggetto(causaleEP.getSoggetto());
		causaleEP.setSoggetto(null);
		
		// Classificatori
		impostaClassificatoriEP(causaleEP.getClassificatoriEP());
		causaleEP.setClassificatoriEP(null);
	}
	
	/**
	 * Ricerca e impostazione del tipo evento della causaleEP nel model.
	 * 
	 * @param causaleEP la causale da impostare
	 */
	private void impostaTipoEvento(CausaleEP causaleEP) {
		for (Evento evento : causaleEP.getEventi()) {
			if(evento.getTipoEvento() != null) {
				// Ho trovato il tipo evento: lo imposto ed esco
				model.setTipoEvento(evento.getTipoEvento());
				return;
			}
		}
	}
	
	/**
	 * Impostazione dei classificatori EP.
	 * 
	 * @param classificatoriEP i classificatori da impostare
	 */
	private void impostaClassificatoriEP(List<ClassificatoreEP> classificatoriEP) {
		for(ClassificatoreEP cep : classificatoriEP) {
			// Per ogni tipo di classificatore coerente, vado a impostare il campo
			if(BilConstants.CODICE_CLASSIFICATORE_EP1.getConstant().equals(cep.getTipoClassificatore().getCodice())) {
				// Classificatore 1
				model.setClassificatoreEP1(cep);
			} else if(BilConstants.CODICE_CLASSIFICATORE_EP2.getConstant().equals(cep.getTipoClassificatore().getCodice())) {
				// Classificatore 2
				model.setClassificatoreEP2(cep);
			} else if(BilConstants.CODICE_CLASSIFICATORE_EP3.getConstant().equals(cep.getTipoClassificatore().getCodice())) {
				// Classificatore 3
				model.setClassificatoreEP3(cep);
			} else if(BilConstants.CODICE_CLASSIFICATORE_EP4.getConstant().equals(cep.getTipoClassificatore().getCodice())) {
				// Classificatore 4
				model.setClassificatoreEP4(cep);
			} else if(BilConstants.CODICE_CLASSIFICATORE_EP5.getConstant().equals(cep.getTipoClassificatore().getCodice())) {
				// Classificatore 5
				model.setClassificatoreEP5(cep);
			}
		}
	}
	
	
	
	@Override
	public String completeStep2() {
		final String methodName = "completeStep2";
		// Inserimento della causale
		AggiornaCausale req = model.creaRequestAggiornaCausale();
		logServiceRequest(req);
		AggiornaCausaleResponse res = causaleService.aggiornaCausale(req);
		logServiceResponse(res);
		
		if(res.hasErrori()) {
			// Errore nell-invocazione del servizio: aggiungo gli errori ed esto
			log.info(methodName, "Errore nell'aggiornamento della causale EP");
			addErrori(res);
			return INPUT;
		}
		log.debug(methodName, "Aggiornata correttamente causale EP con uid " + res.getCausaleEP().getUid());
		// Imposto i dati della causale restituitimi dal servizio
		model.setCausaleEP(res.getCausaleEP());
		impostaInformazioneSuccessoAzioneInSessionePerRedirezione();
		
		// Pulisco la sessione
		sessionHandler.setParametro(BilSessionParameter.LISTA_CAUSALE_EP, null);
		sessionHandler.setParametro(BilSessionParameter.LISTA_EVENTO, null);
		return SUCCESS;
	}
	
}
