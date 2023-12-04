/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.causale;

import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.causale.RicercaCausaleEntrataModel;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrataResponse;

/**
 * Classe di Action per la gestione della ricerca della Causale.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 12/05/2014
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaCausaleEntrataAction extends GenericCausaleEntrataAction<RicercaCausaleEntrataModel> {

	/** Per la serialiazzazione */
	private static final long serialVersionUID = 7033998987854718167L;
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		caricaListeCodifiche();
		caricaListaStati();
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		return SUCCESS;
	}

	/**
	 * Ricerca dei documenti sulla base dei criteri impostati.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaCausaleEntrata() {
		final String methodName = "ricercaCausaleEntrata";

		log.debug(methodName, "Effettua la ricerca");

		RicercaSinteticaCausaleEntrata request = model.creaRequestRicercaSinteticaCausaleEntrata();
		logServiceRequest(request);

		RicercaSinteticaCausaleEntrataResponse response = preDocumentoEntrataService.ricercaSinteticaCausaleEntrata(request);
		logServiceResponse(response);

		if (response.hasErrori()) {
			log.info(methodName, "Fallimento nella chiamata al servizio");
			addErrori(response);
			return INPUT;
		}
		
		if(response.getCausaliEntrata().getTotaleElementi() == 0) {
			log.debug(methodName, "Nessun risultato trovato");
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
			return INPUT;
		}

		log.debug(methodName, "Ricerca effettuata con successo. Totale Elementi: "+response.getCausaliEntrata().getTotaleElementi());

		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CAUSALI_ENTRATA, request);

		log.debug(methodName, "Imposto in sessione la lista");
		
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CAUSALI_ENTRATA, response.getCausaliEntrata());
		
		log.debug(methodName, "Imposto la stringa da visualizzare nei risultati ricerca");
		List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile = 
				sessionHandler.getParametro(BilSessionParameter.LISTA_STRUTTURA_AMMINISTRATIVO_CONTABILE);
		String stringaRiepilogo = model.componiStringaRiepilogo(listaStrutturaAmministrativoContabile);
		
		sessionHandler.setParametro(BilSessionParameter.RIEPILOGO_RICERCA_CAUSALE, stringaRiepilogo);
		sessionHandler.setParametro(BilSessionParameter.RIENTRO_POSIZIONE_START, null);
		
		return SUCCESS;
	}
	
	/**
	 * Ricerca della causale di entrata sulla base del'uid fornito.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	public String ricercaDettaglioCausaleEntrata() {
		final String methodName = "ricercaDettaglioCausaleEntrata";
		if(!checkIsValidaRicercaDettaglioCausaleSpesa()) {
			log.info(methodName, "Ricerca di dettaglio non valida");
			return SUCCESS;
		}
		
		RicercaDettaglioCausaleEntrata request = model.creaRequestRicercaDettaglioCausaleEntrata();
		logServiceRequest(request);
		RicercaDettaglioCausaleEntrataResponse response = preDocumentoEntrataService.ricercaDettaglioCausaleEntrata(request);
		logServiceResponse(response);
		
		// Controllo gli errori
		if(response.hasErrori()) {
			//si sono verificati degli errori: esco.
			log.info(methodName, "Errore nell'invocazione del servizio di ricerca dettaglio causale");
			addErrori(response);
			return SUCCESS;
		}
		
		model.setCausale(response.getCausaleEntrata());
		
		return SUCCESS;
	}

	/**
	 * Validazione per il metodo {@link RicercaCausaleEntrataAction#ricercaCausaleEntrata()}.
	 */
	public void validateRicercaCausaleEntrata() {
		final String methodName = "validateRicercaCausaleEntrata";
		log.debugStart(methodName, "Verifica campi");
		
		boolean formValido =
				(model.getCausale() != null
					&& checkStringaValorizzata(model.getCausale().getCodice(), "codice causale")
					|| checkStringaValorizzata(model.getCausale().getDescrizione(), "descrizione causale"))
				|| checkPresenzaIdEntita(model.getTipoCausale())
				|| checkCampoValorizzato(model.getStatoOperativoCausale(), "Stato");
			
		//verifica imputazioni contabili
		boolean movimentoPresente = checkMovimentoGestioneEsistente(model.getMovimentoGestione());
		boolean soggettoPresente = checkSoggettoEsistente();
		boolean provvedimentoPresente = checkProvvedimentoEsistente();
		boolean capitoloPresente = checkCapitoloEsistente(model.getCapitolo());
		boolean strutturaPresente = checkStrutturaEsistente(model.getStrutturaAmministrativoContabile());
		
		formValido = formValido || soggettoPresente || provvedimentoPresente || movimentoPresente || strutturaPresente || capitoloPresente;
		
		checkCondition(formValido, ErroreCore.NESSUN_CRITERIO_RICERCA.getErrore());
		
		// Check sul soggetto		
		if(capitoloPresente) {
			validazioneCapitolo();
		}
		 
		// Check sul soggetto		
		if(soggettoPresente) {
			validazioneSoggetto();
		}
		//Check sul provvedimento: se è stato valorizzato deve esistere e i campi indicati devono restituire un'unico provvedimento
		if(provvedimentoPresente) {
			validazioneAttoAmministrativo();
		}
		
		// Anno Impegno e numero devono essere entrambi presenti o entrambi assenti
		if(movimentoPresente) {
			validazioneAccertamentoSubAccertamento();
		}
	}
	
	/**
	 * Controllo di validazione per la ricerca di dettaglio della causale di spesa.
	 * 
	 * @return <code>true</code> se la i dati sono validi per la ricerca; <code>false</code> in caso contrario
	 */
	private boolean checkIsValidaRicercaDettaglioCausaleSpesa() {
		checkCondition(model.getCausale() != null && model.getCausale().getUid() != 0, ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("causale"));
		return !hasErrori();
	}

}
