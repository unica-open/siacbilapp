/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step3.SpecificaVariazioneImportoUEBModel;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.importi.ElementoImportiVariazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.EliminaDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siaccecser.frontend.webservice.msg.VariazioneBilancioExcelReport;
import it.csi.siac.siaccecser.frontend.webservice.msg.VariazioneBilancioExcelReportResponse;
import it.csi.siac.siaccommonapp.interceptor.anchor.annotation.AnchorAnnotation;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * Classe per la gestione dell'aggiornamento della variazione degli importi.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 06/06/2016
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class AggiornaVariazioneImportiUEBAction extends AggiornaVariazioneImportiBaseAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2252533479814374691L;
	

	
	@Override
	protected void prepareExecuteImpostaParametroInserimentoNuovaUEB() {
		// Controllo di non essere rientrato dall'inserimento di una nuova UEB
		SpecificaVariazioneImportoUEBModel modelImportiConUEB = model.getSpecificaUEB();
		Boolean nuovaUEB = sessionHandler.getParametro(BilSessionParameter.INSERIMENTO_NUOVA_UEB);
		modelImportiConUEB.setRientroDaInserimentoNuovaUEB(nuovaUEB);
		sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_NUOVA_UEB, null);
	}
	
	
	@Override
	protected void impostaFlagCassaIncongruente(boolean cassaIncongruente) {
		model.getSpecificaUEB().setCassaIncongruente(cassaIncongruente);
		
	}

	@Override
	protected void impostaFlagCassaIncongruenteDopoDefinizione(boolean isCassaDopoDefinizioneIncongruente) {
		model.getSpecificaUEB().setCassaDopoDefinizioneIncongruente(isCassaDopoDefinizioneIncongruente);
		
	}


	/* **************************************************************************
	 * **************************************************************************
	 * *** Interazioni AJAX con la pagina di specificazione della variazione ****
	 * **************************************************************************
	 * **************************************************************************/

	/* ******** Con gestione UEB ******** */

	/**
	 * Metodo per ottenere la lista dei capitoli da inserire nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String leggiCapitoliDaInserireNellaVariazioneUEB() {
		return SUCCESS;
	}

	/**
	 * Metodo per ottenere la lista dei capitoli nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String leggiCapitoliNellaVariazioneUEB() {
		model.getSpecificaUEB()
				.setElementoImportiVariazione(ElementoImportiVariazioneFactory.getInstance(model.getSpecificaUEB().getListaUEBNellaVariazione()));

		final String methodName = "leggiCapitoliNellaVariazioneUEB";

		log.debug(methodName, "Richiamo il webService di ricercaDettagliVariazioneImportoCapitoloNellaVariazione");

		RicercaDettagliVariazioneImportoCapitoloNellaVariazione request = model.creaRequestRicercaDettagliVariazioneImportoCapitoloNellaVariazione();
		RicercaDettagliVariazioneImportoCapitoloNellaVariazioneResponse response = variazioneDiBilancioService.ricercaDettagliVariazioneImportoCapitoloNellaVariazione(request);
		
		if (response.hasErrori()) {
			log.info(methodName, createErrorInServiceInvocationString(RicercaDettagliVariazioneImportoCapitoloNellaVariazione.class, response));
			addErrori(response);
			return SUCCESS;
		}
		
		
		log.debug(methodName, "Ricerca effettuata con successo. Totale elementi trovati: " + response.getTotaleElementi());
		
		// Imposto in sessione i dati
		log.debug(methodName, "Imposto in sessione la request");
		sessionHandler.setParametroXmlType(BilSessionParameter.REQUEST_RICERCA_CAPITOLI_NELLA_VARIAZIONE, request);
		
		log.debug(methodName, "Imposto in sessione la lista");
		sessionHandler.setParametro(BilSessionParameter.RISULTATI_RICERCA_CAPITOLI_NELLA_VARIAZIONE, response.getListaDettaglioVariazioneImportoCapitolo());
		
		model.setTotaleStanziamentiCassaEntrata(response.getTotaleStanziamentiCassaEntrata());
		model.setTotaleStanziamentiCassaSpesa(response.getTotaleStanziamentiCassaSpesa());
		model.setTotaleStanziamentiEntrata(response.getTotaleStanziamentiEntrata());
		model.setTotaleStanziamentiSpesa(response.getTotaleStanziamentiSpesa());
		model.setTotaleStanziamentiResiduiEntrata(response.getTotaleStanziamentiResiduiEntrata());
		model.setTotaleStanziamentiResiduiSpesa(response.getTotaleStanziamentiResiduiSpesa());
				
		return SUCCESS;		
	}

	/**
	 * Metodo per il popolamento della lista degli importi da aggiornare nella Variazione UEB.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String popolaListaAggiornamentoImportiNellaVariazioneUEB() {
		return SUCCESS;
	}

	/**
	 * Metodo per il controllo delle azioni consentite all'utente.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String controllaAzioniConsentiteAllUtenteUEB() {
		TipoCapitolo tipoCapitolo = model.getSpecificaUEB().getElementoCapitoloVariazione().getTipoCapitolo();
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		model.getSpecificaUEB().setUtenteAbilitatoAdAnnullamento(AzioniConsentiteFactory.isAnnullaConsentito(tipoCapitolo, listaAzioniConsentite));
		model.getSpecificaUEB().setUtenteAbilitatoAdInserimento(AzioniConsentiteFactory.isInserisciConsentito(tipoCapitolo, listaAzioniConsentite));
		// Pulisco la lista dei capitli da inserire (o da aggiornare) nella variazione
		pulisciLista(model.getSpecificaUEB().getListaUEBDaInserireNellaVariazione());
		return SUCCESS;
	}

	/**
	 * Pulisce la lista fornita in input.
	 * 
	 * @param lista la lista da pulire
	 */
	private void pulisciLista(List<?> lista) {
		lista.clear();
	}

	/**
	 * Metodo per aggiungere un singolo capitolo nella lista dei capitoli da inserire nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String aggiungiCapitoliDaInserireNellaVariazioneUEB() {
		final String methodName = "aggiungiCapitoliDaInserireNellaVariazioneUEB";
		ElementoCapitoloVariazione capitoloDaInserireInVariazione = model.getSpecificaUEB().getElementoCapitoloVariazione();
		List<ElementoCapitoloVariazione> listaUEBDaInserireNellaVariazione = model.getSpecificaUEB().getListaUEBDaInserireNellaVariazione();
		List<ElementoCapitoloVariazione> listaUEBNellaVariazione = model.getSpecificaUEB().getListaUEBNellaVariazione();

		log.debug(methodName, "Valido la variazione da inserire nell'elenco di quelle temporanee");
		if (!validaInserimentoCapitoloNellaVariazione(capitoloDaInserireInVariazione, /*model.getSpecificaUEB().getElementoCapitoloVariazioneOld(),*/ model.getSpecificaUEB().getIgnoraValidazione(),model.getSpecificaUEB().getIgnoraValidazioneImportiDopoDefinizione())) {
			model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);
			return SUCCESS;
		}
		model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);

		log.debug(methodName, "Gli importi sono sensati. Controllo di non aver già inserito il capitolo precedentemente");
		if (ComparatorUtils.searchByUid(listaUEBDaInserireNellaVariazione, capitoloDaInserireInVariazione) != null
				|| ComparatorUtils.searchByUid(listaUEBNellaVariazione, capitoloDaInserireInVariazione) != null) {
			log.debug(methodName, "Capitolo già presente nella variazione");
			addErrore(ErroreBil.UEB_GIA_ASSOCIATA_ALLA_VARIAZIONE.getErrore(""));
			return SUCCESS;
		}

		log.debug(methodName, "La validazione della UEB ha avuto successo. Inserire pertanto la UEB nella lista");
		listaUEBDaInserireNellaVariazione.add(capitoloDaInserireInVariazione);

		// Re-injetto la lista nel model, e reimposto il capitolo come null
		model.getSpecificaUEB().setElementoCapitoloVariazione(null);
		model.getSpecificaUEB().setListaUEBDaInserireNellaVariazione(listaUEBDaInserireNellaVariazione);

		return SUCCESS;
	}

	/**
	 * Metodo per aggiungere i capitoli da inserire nella variazione effettivamente nella variazione stessa.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String aggiungiCapitoliNellaVariazioneUEB() {
		final String methodName = "aggiungiCapitoliNellaVariazioneUEB";
		
		ElementoCapitoloVariazione elementoCapitoloVariazione = model.getSpecificaUEB().getElementoCapitoloVariazione();
		
		if(!validaInserimentoCapitoloNellaVariazione(elementoCapitoloVariazione, model.getSpecificaUEB().getIgnoraValidazione(), model.getSpecificaUEB().getIgnoraValidazioneImportiDopoDefinizione())){
			model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);
			model.getSpecificaUEB().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
			return SUCCESS;
		}
		
		InserisciDettaglioVariazioneImportoCapitolo req = model.creaRequestInserisciDettaglioVariazioneImportoCapitoloUEB();
		
		
		log.debug(methodName, "richiamo il servizio di inserimento");
		//*
		 InserisciDettaglioVariazioneImportoCapitoloResponse res = variazioneDiBilancioService.inserisciDettaglioVariazioneImportoCapitolo(req);
		
		if(res.hasErrori()){
			log.debug(methodName, "invocazione terminata con esito fallimento");
			addErrori(res);
			
		} //*/
		
		model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);
		model.getSpecificaUEB().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);

		return SUCCESS;
	
	}

	/**
	 * Metodo per aggiornare i capitoli da inserire nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String aggiornaCapitoliDaInserireNellaVariazioneUEB() {
		//FIXME: potrebbe non essere mai utilizzato
		final String methodName = "aggiornaCapitoliDaInserireNellaVariazioneUEB";
		log.debug(methodName, "Aggiorno la lista");
		ElementoCapitoloVariazione elementoDaAggiornare = model.getSpecificaUEB().getElementoCapitoloVariazione();
		List<ElementoCapitoloVariazione> lista = model.getSpecificaUEB().getListaUEBDaInserireNellaVariazione();

		if (!validaInserimentoCapitoloNellaVariazione(elementoDaAggiornare,/* model.getSpecificaUEB().getElementoCapitoloVariazioneOld(),*/ model.getSpecificaUEB().getIgnoraValidazione(), model.getSpecificaUEB().getIgnoraValidazioneImportiDopoDefinizione())) {
			// Gli importi non sono validi: ritornare
			model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);
			model.getSpecificaUEB().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
			return SUCCESS;
		}
		model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);

		// Ottengo l'indice dell'elemento da aggiornare
		int indice = ComparatorUtils.getIndexByUid(lista, elementoDaAggiornare);
		log.debug(methodName, "Indice dell'elemento da aggiornare: " + indice);
		// Controllo di sicurezza
		if (indice > -1) {
			lista.set(indice, elementoDaAggiornare);
		} else {
			lista.add(elementoDaAggiornare);
		}
		// Svuoto il valore dell'elemento
		model.getSpecificaUEB().setElementoCapitoloVariazione(null);
		// Re-injetto la lista aggiornata nel model
		model.getSpecificaUEB().setListaUEBDaInserireNellaVariazione(lista);
		
		model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);
		model.getSpecificaUEB().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
		return SUCCESS;
	}

	/**
	 * Metodo per aggiornare i capitoli nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String aggiornaCapitoliNellaVariazioneUEB() {
		final String methodName = "aggiornaCapitoliNellaVariazioneUEB";
		log.debug(methodName, "Aggiorno la lista");
		
		ElementoCapitoloVariazione elementoCapitoloVariazioneDaAggiornare = model.getSpecificaUEB().getElementoCapitoloVariazione();
		if (!validaInserimentoCapitoloNellaVariazione(elementoCapitoloVariazioneDaAggiornare, model.getSpecificaUEB().getIgnoraValidazione(), model.getSpecificaUEB().getIgnoraValidazioneImportiDopoDefinizione())) {
			model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);
			model.getSpecificaUEB().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
			return SUCCESS;
		}

		// 3403
		AggiornaDettaglioVariazioneImportoCapitolo req = model.creaRequestAggiornaDettaglioVariazioneImportoCapitoloUEB();
		
		///*
		AggiornaDettaglioVariazioneImportoCapitoloResponse res = variazioneDiBilancioService.aggiornaDettaglioVariazioneImportoCapitolo(req);
		
		if (res.hasErrori()) {
			log.debug(methodName, "Invocazione terminata con fallimento");
			addErrori(res);
			return SUCCESS;
		}//*/
		log.debug(methodName, "richiamato il webService di aggiornamento capitoli nella variazione");
		model.getSpecificaUEB().setIgnoraValidazione(Boolean.FALSE);
		model.getSpecificaUEB().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);

		return SUCCESS;
	}

	/**
	 * Metodo per eliminare i capitoli da inserire nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String eliminaCapitoliDaInserireNellaVariazioneUEB() {
		final String methodName = "eliminaCapitoliDaInserireNellaVariazioneUEB";
		log.debug(methodName, "Modifico la lista eliminando l'elemento");

		ElementoCapitoloVariazione elementoDaAggiornare = model.getSpecificaUEB().getElementoCapitoloVariazione();
		List<ElementoCapitoloVariazione> listaUEBDaInserireNellaVariazione = model.getSpecificaUEB().getListaUEBDaInserireNellaVariazione();

		// Ottengo l'indice dell'elemento da aggiornare
		int indice = ComparatorUtils.getIndexByUid(listaUEBDaInserireNellaVariazione, elementoDaAggiornare);
		log.debug(methodName, "Indice dell'elemento da eliminare: " + indice);
		// Controllo di sicurezza
		if (indice > -1) {
			listaUEBDaInserireNellaVariazione.remove(indice);
		}
		// Svuoto il valore dell'elemento
		elementoDaAggiornare = null;
		model.getSpecificaUEB().setElementoCapitoloVariazione(elementoDaAggiornare);

		// Re-injetto la lista aggiornata nel model
		model.getSpecificaUEB().setListaUEBDaInserireNellaVariazione(listaUEBDaInserireNellaVariazione);
		return SUCCESS;
	}

	/**
	 * Metodo per eliminare i capitoli nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String eliminaCapitoliNellaVariazioneUEB() {
		final String methodName = "eliminaCapitoliNellaVariazioneUEB";
		log.debug(methodName, "Elimino l'elemento");
		
		EliminaDettaglioVariazioneImportoCapitolo req = model.creaRequestEliminaDettaglioVariazioneImportoCapitoloUEB();
		
		log.debug(methodName, "richiamo il webservice di eliminazione capitolo nella variazione");
		EliminaDettaglioVariazioneImportoCapitoloResponse response = variazioneDiBilancioService.eliminaDettaglioVariazioneImportoCapitolo(req);
		if(response.hasErrori()){
			log.debug(methodName, "Si sono verificati errori durante l'invocazione del servizio di eliminazione del capitolo nella variazione");
			addErrori(response);
		}
		
		// Controllo se l'elemento è stato inserito in stato provvisorio
		if (Boolean.TRUE.equals(model.getSpecificaUEB().getElementoCapitoloVariazione().getDaInserire())) {
			log.debug(methodName, "Il capitolo è provvisorio: eliminarlo");
			ServiceResponse responseEliminazione = eliminaCapitoloProvvisorio(model.getSpecificaUEB().getElementoCapitoloVariazione().unwrap());
			if(responseEliminazione.hasErrori()){
				log.debug(methodName, "Si sono verificati errori durante l'invocazione del servizio di eliminazione del capitolo");
				addErrori(responseEliminazione);
			}
		}
		
		return SUCCESS;
	}

	/**
	 * Redirige verso la creazione di un nuovo capitolo.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	@AnchorAnnotation(name = "Redirezione nuovo capitolo UEB", value = "%{cdu}", afterAction = true)
	public String redirezioneVersoNuovoCapitoloUEB() {
		final String methodName = "redirezioneVersoNuovoCapitoloUEB";

		// Comunico all'inserimento l'arrivo dall'aggiornamento
		sessionHandler.setParametro(BilSessionParameter.INSERISCI_NUOVO_DA_AGGIORNAMENTO, Boolean.TRUE);
		sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_DA_VARIAZIONE, Boolean.TRUE);
		String tipoCapitolo = model.getSpecificaUEB().getTipoCapitolo().toLowerCase(getLocale());
		String tipoApplicazione = model.getApplicazione().getDescrizione().toLowerCase(getLocale());
		String compoundName = tipoCapitolo + "_" + tipoApplicazione;
		log.debug(methodName, "Redirezione verso inserimento: " + compoundName);
		return compoundName;
	}

	/**
	 * Redirige verso la creazione di una nuova UEB.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	@AnchorAnnotation(name = "Redirezione nuova UEB", value = "%{cdu}", afterAction = true)
	public String redirezioneVersoNuovoUEB() {
		final String methodName = "redirezioneVersoNuovoUEB";
		SpecificaVariazioneImportoUEBModel specifica = model.getSpecificaUEB();

		// Aumento il numero della UEB, se presente
		Integer numeroUEB = specifica.getNumeroUEB();
		if (numeroUEB == null) {
			numeroUEB = Integer.valueOf(0);
		}
		numeroUEB++;
		specifica.setNumeroUEB(numeroUEB);

		log.debug(methodName, "Il numero della prossima UEB è " + numeroUEB);

		// Comunico all'inserimento l'arrivo dall'aggiornamento
		sessionHandler.setParametro(BilSessionParameter.INSERISCI_NUOVO_DA_AGGIORNAMENTO, Boolean.TRUE);
		sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_NUOVA_UEB, Boolean.TRUE);
		sessionHandler.setParametro(BilSessionParameter.INSERIMENTO_DA_VARIAZIONE, Boolean.TRUE);

		String tipoCapitolo = specifica.getTipoCapitolo().toLowerCase(getLocale());
		String tipoApplicazione = model.getApplicazione().getDescrizione().toLowerCase(getLocale());
		String compoundName = tipoCapitolo + "_" + tipoApplicazione;
		log.debug(methodName, "Redirezione verso inserimento: " + compoundName);
		return compoundName;
	}

	/**
	 * Controlla l'annullabilit&agrave; di un capitolo con tutte le UEB collegate.
	 * 
	 * @return una Stringa corrispondente al risultato dell'invocazione
	 */
	@SkipValidation
	public String annullaCapitoloUEB() {
		final String methodName = "annullaCapitoloUEB";
		
		List<ElementoCapitoloVariazione> listaUEBDaAnnullare = model.getSpecificaUEB().getListaUEBDaAnnullare();
		
		//ElementoCapitoloVariazione elementoCapitoloVariazioneDaAnnullare = model.getSpecificaUEB().getElementoCapitoloVariazione();
//		// Unwrap del wrapper
		
		for (ElementoCapitoloVariazione elementoCapitoloVariazioneDaAnnullare : listaUEBDaAnnullare) {
			Capitolo<?, ?> capitoloDaAnnullare = elementoCapitoloVariazioneDaAnnullare.unwrap();
//
			log.debug(methodName, "Invocazione del servizio di verifica annullabilità");
			ServiceResponse responseAnnullabilita = verificaAnnullabilitaCapitolo(capitoloDaAnnullare);
			if (responseAnnullabilita == null) {
				addErrore(ErroreCore.ERRORE_DI_SISTEMA.getErrore("Invocazione non possibile per il capitolo"
						+ elementoCapitoloVariazioneDaAnnullare.getAnnoCapitolo() + " / " + elementoCapitoloVariazioneDaAnnullare.getNumeroCapitolo() + "/"
						+ elementoCapitoloVariazioneDaAnnullare.getNumeroArticolo()));
				return SUCCESS;
			} else if (Boolean.FALSE.equals(ReflectionUtil.getBooleanField(responseAnnullabilita, "annullabilitaCapitolo"))) {
				addErrori(responseAnnullabilita);
				return SUCCESS;
			} 

			InserisciDettaglioVariazioneImportoCapitolo req = model.creaRequestInserisciDettaglioVariazioneImportoAnnullaCapitoloUEB(elementoCapitoloVariazioneDaAnnullare);
		
		
			log.debug(methodName, "richiamo il servizio di inserimento");
			//*
			InserisciDettaglioVariazioneImportoCapitoloResponse res = variazioneDiBilancioService.inserisciDettaglioVariazioneImportoCapitolo(req);
		
			if(res.hasErrori()){
				log.debug(methodName, "invocazione terminata con esito fallimento");
				addErrori(res);
			
			}
		}
		return SUCCESS;

	}


	@Override
	protected void impostaFlagIgnoraValidazione(boolean b) {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void impostaFlagIgnoraValidazioneImportiDopoDefinizione(boolean b) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Preparazione per il metodo {@link #download()}
	 */
	@Override
	public void prepareDownload() {
		model.setIsXlsx(null);
		model.setContentType(null);
		model.setContentLength(null);
		model.setFileName(null);
		model.setInputStream(null);
	}
	
	/**
	 * Download dell'excel dei dati della variazione
	 * @return una stringa corrispondente al risultato dell'invocazione
	 */
	@Override
	public String download() {
		final String methodName = "download";
		
		VariazioneBilancioExcelReport req = model.creaRequestStampaExcelVariazioneDiBilancio();
		VariazioneBilancioExcelReportResponse res = variazioneDiBilancioService.variazioneBilancioExcelReport(req);
		
		if(res.hasErrori()) {
			addErrori(res);
			log.info(methodName, "Errori nel reperimento dell'excel della variazione");
			return INPUT;
		}
		
		byte[] bytes = res.getReport();
		model.setContentType(res.getContentType() == null ? null : res.getContentType().getMimeType());
		model.setContentLength(Long.valueOf(bytes.length));
		model.setFileName("esportazioneVariazione" + model.getAnnoEsercizio() + "_" + model.getNumeroVariazione() + "." + res.getExtension());
		model.setInputStream(new ByteArrayInputStream(bytes));
		
		return SUCCESS;
	}
	

}
