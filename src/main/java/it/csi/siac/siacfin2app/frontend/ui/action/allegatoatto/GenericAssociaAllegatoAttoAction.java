/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimentoResponse;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.errore.ErroreAtt;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.GenericAssociaAllegatoAttoModel;
import it.csi.siac.siacfin2ser.model.errore.ErroreFin;
import it.csi.siac.siacfinser.frontend.webservice.SoggettoService;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.ListeGestioneSoggettoResponse;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiaveResponse;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe di Action per l'associazione con l'allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 30/set/2014
 * 
 * @param <M> la tipizzazione del model
 *
 */
public class GenericAssociaAllegatoAttoAction<M extends GenericAssociaAllegatoAttoModel> extends GenericAllegatoAttoAction<M> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 477517089945792473L;
	
	/** Serviz&icirc; del soggetto */
	@Autowired protected transient SoggettoService soggettoService;
	
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
	 * Controlla che il soggetto sia valido.
	 * 
	 * @param soggetto     il soggetto da controllare
	 * @param statiAmmessi gli stati del soggetto ammessi per la ricerca
	 */
	protected void checkSoggettoValido(Soggetto soggetto, List<Soggetto.StatoOperativoAnagrafica> statiAmmessi) {
		final String methodName = "checkSoggettoValido";
		if(soggetto == null || StringUtils.isBlank(soggetto.getCodiceSoggetto())) {
			log.debug(methodName, "Nessun soggetto da validare");
			return;
		}
		log.debug(methodName, "Codice soggetto da validare: " + soggetto.getCodiceSoggetto());
		RicercaSoggettoPerChiave request = model.creaRequestRicercaSoggettoPerChiave();
		logServiceRequest(request);
		RicercaSoggettoPerChiaveResponse response = soggettoService.ricercaSoggettoPerChiave(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			// Errore nell'invocazione del servizio: loggo, aggiungo al model ed esco
			log.info(methodName, createErrorInServiceInvocationString(RicercaSoggettoPerChiave.class, response));
			addErrori(response);
			return;
		}
		Soggetto soggettoDaResponse = response.getSoggetto();
		if(soggettoDaResponse == null) {
			// Nessun soggetto trovato: loggo, segnalo ed esco
			log.debug(methodName, "Nessun soggetto disponibile per codice " + soggetto.getCodiceSoggetto());
			addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("Soggetto", "codice " + soggetto.getCodiceSoggetto()));
			return;
		}
		// Soggetto trovato
		log.debug(methodName, "Soggetto con codice " + soggetto.getCodiceSoggetto() + " trovato con uid " + soggettoDaResponse.getUid()
				+ ". StatoOperativoAnagrafica: " + soggetto.getStatoOperativo());
		
		// Controllo stati
		checkCondition(statiAmmessi.contains(soggettoDaResponse.getStatoOperativo()), ErroreFin.SOGGETTO_NON_VALIDO.getErrore());
		model.setSoggetto(soggettoDaResponse);
		// Imposto le liste di Sede Secondaria e di Modalita Pagamento
		
		List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = defaultingList(response.getListaSecondariaSoggetto());
		List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = defaultingList(response.getListaModalitaPagamentoSoggetto());
		listaModalitaPagamentoSoggetto = impostaListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
		model.setListaSedeSecondariaSoggetto(listaSedeSecondariaSoggetto);
		model.setListaModalitaPagamentoSoggetto(listaModalitaPagamentoSoggetto);
	}

	/**
	 * Controlla che l'atto amministrativo sia valido.
	 * 
	 * @param attoAmministrativo l'atto da controllare.
	 */
	protected void checkAttoAmministrativoValido(AttoAmministrativo attoAmministrativo) {
		final String methodName = "checkAttoAmministrativoValido";
		if(attoAmministrativo == null) {
			log.debug(methodName, "Nessun atto amministrativo da validare");
			return;
		}
		TipoAtto tipoAtto = model.getTipoAtto();
		// Se non ho ne' anno, ne' numero, ne' tipo, allora non ho nulla da cercare
		if(attoAmministrativo.getAnno() == 0 && attoAmministrativo.getNumero() == 0 && (tipoAtto == null || tipoAtto.getUid() == 0)) {
			log.debug(methodName, "Nessun dato dell'amministrativo da validare");
			return;
		}
		
		checkCondition(isValidAttoAmministrativo(attoAmministrativo, tipoAtto),
			ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("quando valorizzato anno atto bisogna valorizzare o il tipo o il numero atto"), true);
		
		log.debug(methodName, "Atto amministrativo da validare: anno" + attoAmministrativo.getAnno() + ", numero " + attoAmministrativo.getNumero() +
			(model.getTipoAtto() == null ? "" : ", tipoAtto " + model.getTipoAtto().getUid()));
		RicercaProvvedimento request = model.creaRequestRicercaProvvedimento();
		logServiceRequest(request);
		RicercaProvvedimentoResponse response = provvedimentoService.ricercaProvvedimento(request);
		logServiceResponse(response);
		
		if(response.hasErrori()) {
			// Errore nell'invocazione del servizio: loggo, aggiungo al model ed esco
			log.info(methodName, createErrorInServiceInvocationString(RicercaProvvedimento.class, response));
			addErrori(response);
			return;
		}
		
		// Controllo di aver almeno un provvedimento
		checkCondition(!response.getListaAttiAmministrativi().isEmpty(), ErroreAtt.PROVVEDIMENTO_INESISTENTE.getErrore(), true);
		// Controllo di avere al piu' un provvedimento
		checkUnicoAttoAmministrativo(response.getListaAttiAmministrativi(), model.getAttoAmministrativo().getStrutturaAmmContabile(), true);
		
		AttoAmministrativo aa = response.getListaAttiAmministrativi().get(0);
		// Imposto l'atto nel model
		model.setAttoAmministrativo(aa);
		model.setStrutturaAmministrativoContabile(aa.getStrutturaAmmContabile());
	}

	/**
	 * Controlla che l'atto amministrativo sia valido.
	 * 
	 * @param attoAmministrativo l'atto da controllare
	 * @param tipoAtto           il tipo dell'atto
	 * 
	 * @return <code>true</code> se l'atto &eacute; valido; <code>false</code> in caso contrario
	 */
	private boolean isValidAttoAmministrativo(AttoAmministrativo attoAmministrativo, TipoAtto tipoAtto) {
		return attoAmministrativo.getAnno() != 0 && (attoAmministrativo.getNumero() != 0 || (tipoAtto != null && tipoAtto.getUid() != 0));
	}

}

