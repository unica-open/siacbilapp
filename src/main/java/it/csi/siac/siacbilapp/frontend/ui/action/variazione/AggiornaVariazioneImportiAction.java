/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * 
 */
package it.csi.siac.siacbilapp.frontend.ui.action.variazione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.variazione.step4.RiepilogoVariazioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.helper.DettaglioVariazioneComponenteImportoCapitoloHelper;
import it.csi.siac.siacbilapp.frontend.ui.util.helper.ImportiCapitoloComponenteVariazioneModificabile;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoCapitoloVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoComponenteVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoComponenteVariazioneFactory;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.GestisciDettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.GestisciDettaglioVariazioneComponenteImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisciDettaglioVariazioneImportoCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitoloResponse;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.StatoOperativoVariazioneBilancio;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siaccorser.util.comparator.ComparatorUtil;

/**
 * Classe per la gestione dell'aggiornamento della variazione degli importi.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 20/11/2013
 * 
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class AggiornaVariazioneImportiAction extends AggiornaVariazioneImportiBaseAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2252533479814374690L;
	
	/**
	 * Metodo per aggiornare i capitoli nella variazione.
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String aggiornaCapitoliNellaVariazione() {
		final String methodName = "aggiornaCapitoliNellaVariazione";
		log.debug(methodName, "Aggiorno la lista");
		
		ElementoCapitoloVariazione elementoCapitoloVariazioneDaAggiornare = model.getSpecificaImporti().getElementoCapitoloVariazione();
		if (!validaInserimentoCapitoloNellaVariazione(elementoCapitoloVariazioneDaAggiornare, model.getSpecificaImporti().getIgnoraValidazione(), model.getSpecificaImporti().getIgnoraValidazioneImportiDopoDefinizione())) {
			model.getSpecificaImporti().setIgnoraValidazione(Boolean.FALSE);
			model.getSpecificaImporti().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
			return SUCCESS;
		}

		// 3403
		AggiornaDettaglioVariazioneImportoCapitolo req = model.creaRequestAggiornaDettaglioVariazioneImportoCapitolo();
		
		///*
		AggiornaDettaglioVariazioneImportoCapitoloResponse res = variazioneDiBilancioService.aggiornaDettaglioVariazioneImportoCapitolo(req);
		
		if (res.hasErrori()) {
			log.debug(methodName, "Invocazione terminata con fallimento");
			addErrori(res);
			return SUCCESS;
		}///
		log.debug(methodName, "richiamato il webService di aggiornamento capitoli nella variazione");
		model.getSpecificaImporti().setIgnoraValidazione(Boolean.FALSE);
		model.getSpecificaImporti().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
		
		return SUCCESS;
	}
	
	/**
	 * Metodo per collegare i capitoli alla variazione
	 * 
	 * @return il risultato dell'invocazione
	 */
	@SkipValidation
	public String aggiungiCapitoliNellaVariazione() {
		final String methodName = "aggiungiCapitoliNellaVariazione";
		
		ElementoCapitoloVariazione elementoCapitoloVariazioneDaAggiungere = model.getSpecificaImporti().getElementoCapitoloVariazione();
		if (!validaInserimentoCapitoloNellaVariazione(elementoCapitoloVariazioneDaAggiungere, model.getSpecificaImporti().getIgnoraValidazione(), model.getSpecificaImporti().getIgnoraValidazioneImportiDopoDefinizione())) {
			model.getSpecificaImporti().setIgnoraValidazione(Boolean.FALSE);
			return SUCCESS;
		}
		
		InserisciDettaglioVariazioneImportoCapitolo req = model.creaRequestInserisciDettaglioVariazioneImportoCapitolo();
		
		log.debug(methodName, "richiamo il servizio di inserimento");
		///*
		 InserisciDettaglioVariazioneImportoCapitoloResponse res = variazioneDiBilancioService.inserisciDettaglioVariazioneImportoCapitolo(req);
		
		if(res.hasErrori()){
			log.debug(methodName, "invocazione terminata con esito fallimento");
			addErrori(res);
			
		} //*/
		model.getSpecificaImporti().setIgnoraValidazione(Boolean.FALSE);
		model.getSpecificaImporti().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
		return SUCCESS;
	}

	@Override
	protected void impostaFlagCassaIncongruente(boolean isCassaIncongruente) {
		model.getSpecificaImporti().setCassaIncongruente(isCassaIncongruente);
		
	}

	@Override
	protected void impostaFlagCassaIncongruenteDopoDefinizione(boolean isCassaDopoDefinizioneIncongruente) {
		model.getSpecificaImporti().setCassaDopoDefinizioneIncongruente(isCassaDopoDefinizioneIncongruente); 
		
	}

	@Override
	protected void impostaFlagIgnoraValidazione(boolean ignoraValidazione) {
		model.getSpecificaImporti().setIgnoraValidazione(Boolean.valueOf(ignoraValidazione));		
	}

	@Override
	protected void impostaFlagIgnoraValidazioneImportiDopoDefinizione(boolean ignoraValidazioneCapitoloDopoDefinizione) {
		model.getSpecificaImporti().setIgnoraValidazioneImportiDopoDefinizione(Boolean.valueOf(ignoraValidazioneCapitoloDopoDefinizione));	
	}
	
	
	/**
	 * Prepare carica componenti importo per nuovo dettaglio variazione.
	 */
	public void prepareCaricaComponentiImportoPerNuovoDettaglioVariazione() {
		model.getSpecificaImporti().setComponentiCapitoloNuovoDettaglio(null);
	}
	
	/**
	 * Validate carica componenti importo per nuovo dettaglio variazione.
	 */
	public void validateCaricaComponentiImportoPerNuovoDettaglioVariazione() {
		checkCondition(model.getSpecificaImporti().getUidCapitoloComponenti() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("chiave capitolo per caricamento componenti"));
	}
	/**
	 * Carica componenti importo per nuovo dettaglio variazione.
	 *
	 * @return the string
	 */
	//SIAC-6881
	public String caricaComponentiImportoPerNuovoDettaglioVariazione() {
		List<ImportiCapitoloPerComponente> importiComponentiCapitolo = sessionHandler.getParametro(BilSessionParameter.LISTA_IMPORTI_COMPONENTE_CAPITOLO_DA_RICERCA);
		
		List<ElementoComponenteVariazione> listaComponentiCapitoloNuovoDettaglio = getListaComponentiCapitoloNuovoDettaglio(importiComponentiCapitolo);
		
		model.getSpecificaImporti().setComponentiCapitoloNuovoDettaglio(listaComponentiCapitoloNuovoDettaglio);
		model.getSpecificaImporti().ricalcolaStanziamentiNuovoDettaglio();
		return SUCCESS;
	}

	
	/**
	 * Gets the lista componenti capitolo modificabili in variazione.
	 *
	 * @param importiComponentiCapitoloDaFiltrare the importi componenti capitolo da filtrare
	 * @param listaTipoComponentiImportiCapitolo the lista tipo componenti importi capitolo
	 * @return the lista componenti capitolo modificabili in variazione
	 */
	private List<ElementoComponenteVariazione> getListaComponentiCapitoloNuovoDettaglio(List<ImportiCapitoloPerComponente> importiComponentiCapitoloDaFiltrare) {

		List<ElementoComponenteVariazione> filtered = new ArrayList<ElementoComponenteVariazione>();
		
		if(isGestioneCapitoloFondinoPerDecentrato()) {
			return getListaComponentiCapitoloDefaultFondino();
		}
		
		List<Integer> uidTipiComponentiDelCapitolo  = new ArrayList<Integer>();
		if(importiComponentiCapitoloDaFiltrare != null && !importiComponentiCapitoloDaFiltrare.isEmpty()) {
			//aggiungo le componenti presenti sul capitolo
			uidTipiComponentiDelCapitolo = addComponentiDaCapitolo(importiComponentiCapitoloDaFiltrare, filtered);
		}
		
		//SIAC-7169
		//aggiungo le componenti in base al default
		addComponentiDaDefault(filtered, uidTipiComponentiDelCapitolo);
		
		return filtered;
	}
	
	/**
	 * Gets the lista componenti capitolo default fondino.
	 *
	 * @return the lista componenti capitolo default fondino
	 */
	private List<ElementoComponenteVariazione> getListaComponentiCapitoloDefaultFondino(){
		List<ElementoComponenteVariazione> filtered = new ArrayList<ElementoComponenteVariazione>();
		List<TipoComponenteImportiCapitolo> tcByMacro = model.getTipoComponenteByMacrotipo(MacrotipoComponenteImportiCapitolo.FRESCO);
		for (TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo : tcByMacro) {
			ElementoComponenteVariazione componenteDefault = getComponenteDefault(tipoComponenteImportiCapitolo);
			if(componenteDefault == null) {
				continue;
			}
			filtered.add(componenteDefault);
		}
		return filtered;
	}
	
	/**
	 * Checks if is gestione capitolo fondino per decentrato.
	 *
	 * @return true, if is gestione capitolo fondino per decentrato
	 */
	private boolean isGestioneCapitoloFondinoPerDecentrato() {
		return model.getSpecificaImporti().isCapitoloFondino() 
				//SIAC-7217eliminata dipendenza dall'azione in seguito a comunicazioni con Sil default viene impostato a fresco solo se l'utente ha l'azione oinserisci variazione decentrata.. c'è poi anche un errore in fase di salvataggio se l'utente inserisce dele componenti di tipo diverso da fresco, ed anche questo viene lanciato solo se l'utente ha abilitata l'azione decentrata "InsVarDec"
				/*
				Giuseppe, 10:29
				Nell'analisi non e' subordinato all'azione.
				
				10:29
				ok, lo modifico in giornata e lo metto su allora
				
				
				Giuseppe, 10:29
				(intendo dire, nel documento CDU, non si cita l'azione...)
				
				Ok.
				*/

				//&& AzioniConsentiteFactory.isConsentito(AzioniConsentite.INSERISCI_VARIAZIONE_DECENTRATA, sessionHandler.getAzioniConsentite())
				;
	}

	/**
	 * @param filtered
	 * @param uidTipiComponentiDelCapitolo
	 */
	private void addComponentiDaDefault(List<ElementoComponenteVariazione> filtered, List<Integer> uidTipiComponentiDelCapitolo) {
		for (TipoComponenteImportiCapitolo tipoDefault : model.getListaTipoComponenteDefault()) {
			if(uidTipiComponentiDelCapitolo.contains(tipoDefault.getUid())) {
				continue;
			}
			ElementoComponenteVariazione elementoComponenteSuiTreAnni = getComponenteDefault(tipoDefault);
			if(elementoComponenteSuiTreAnni == null) {
				continue;
			}
			filtered.add(elementoComponenteSuiTreAnni);
		}
	}
	
	/**
	 * Gets the componente default.
	 *
	 * @param tipoDefault the tipo default
	 * @return the componente default
	 */
	private ElementoComponenteVariazione getComponenteDefault(TipoComponenteImportiCapitolo tipoDefault) {
		ImportiCapitoloComponenteVariazioneModificabile modificabile = ImportiCapitoloComponenteVariazioneModificabile.getImportiCapitoloModificabile(model.getApplicazione(), tipoDefault.getMacrotipoComponenteImportiCapitolo(), tipoDefault.getSottotipoComponenteImportiCapitolo(), TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		if(modificabile == null || !modificabile.isAlmenoUnAnnualitaModificabile()) {
			return null;
		}
		return ElementoComponenteVariazioneFactory.getInstanceComponenteDefault(tipoDefault, modificabile);
	}

	/**
	 * @param importiComponentiCapitoloDaFiltrare
	 * @param listaTipoComponentiImportiCapitolo
	 * @param filtered
	 * @param uidTipiComponentiDelCapitolo
	 * @return 
	 */
	private List<Integer> addComponentiDaCapitolo(List<ImportiCapitoloPerComponente> importiComponentiCapitoloDaFiltrare, 	List<ElementoComponenteVariazione> filtered) {
		
		List<Integer> uidTipiComponentiDelCapitolo = new ArrayList<Integer>();
		List<TipoComponenteImportiCapitolo> listaTipoComponentiImportiCapitolo = model.getListaTipoComponente();
		
		for (ImportiCapitoloPerComponente importiByComponente : importiComponentiCapitoloDaFiltrare) {
			
			TipoComponenteImportiCapitolo tipoComponente = ComparatorUtil.searchByUid(listaTipoComponentiImportiCapitolo, importiByComponente.getTipoComponenteImportiCapitolo());
			if(model.isPropostaDefaultCompatibile(tipoComponente.getPropostaDefaultComponenteImportiCapitolo())) {
				uidTipiComponentiDelCapitolo.add(tipoComponente.getUid());
			}
			
			ImportiCapitoloComponenteVariazioneModificabile importiCapitoloModificabile = ImportiCapitoloComponenteVariazioneModificabile.getImportiCapitoloModificabile(model.getApplicazione(), tipoComponente.getMacrotipoComponenteImportiCapitolo(), tipoComponente.getSottotipoComponenteImportiCapitolo(), importiByComponente.getTipoDettaglioComponenteImportiCapitolo());
			if(importiCapitoloModificabile == null || !importiCapitoloModificabile.isAlmenoUnAnnualitaModificabile()) {
				continue;
			}
			
			ElementoComponenteVariazione elementoComponenteSuiTreAnni = ElementoComponenteVariazioneFactory.getInstanceFromDatiCapitolo(tipoComponente, importiByComponente.getDettaglioAnno0(), BigDecimal.ZERO, importiByComponente.getDettaglioAnno1(), BigDecimal.ZERO, importiByComponente.getDettaglioAnno2(), BigDecimal.ZERO, importiCapitoloModificabile, false, false );
			filtered.add(elementoComponenteSuiTreAnni);
		}
		return uidTipiComponentiDelCapitolo;
	}

	/**
	 * Aggiungi capitoli con componenti nella variazione.
	 *
	 * @return the string
	 */
	public String aggiungiCapitoliConComponentiNellaVariazione() {
		final String methodName ="aggiungiCapitoliConComponentiNellaVariazione";
		
		Map<String, List<DettaglioVariazioneComponenteImportoCapitolo>> unwrapped = ElementoComponenteVariazioneFactory.unWrapAllByAnno(model.getSpecificaImporti().getComponentiCapitoloNuovoDettaglio(), true);
		
		if (!validaComponenti(unwrapped)) {
			return SUCCESS;
		}
		
		
		ElementoCapitoloVariazione elementoCapitoloVariazioneDaAggiungere = model.getSpecificaImporti().getElementoCapitoloVariazione();
		
		
		/*SIAC-6884 controlli per inserimento Regione Piemonte Decentrato e Capitoli*/
//		int uidVariazione = model.getUidVariazione(); //UID VARIAZIONE
//		StrutturaAmministrativoContabile sc = model.getDirezioneProponente(); //direzione proponente della variazione
//		
//		
//		boolean decentrato = model.isDecentrato();
//		boolean regionePiemonte = model.isRegionePiemonte();
//		
//		boolean isCapitoloSpesa = ((elementoCapitoloVariazioneDaAggiungere.getTipoCapitolo() == TipoCapitolo.CAPITOLO_USCITA_GESTIONE) || elementoCapitoloVariazioneDaAggiungere.getTipoCapitolo() == TipoCapitolo.CAPITOLO_USCITA_PREVISIONE)? true : false;
//		int uidCapitolo = elementoCapitoloVariazioneDaAggiungere.getUid();
		
		if (!validaInserimentoCapitoloNellaVariazione(elementoCapitoloVariazioneDaAggiungere, model.getSpecificaImporti().getIgnoraValidazione(), model.getSpecificaImporti().getIgnoraValidazioneImportiDopoDefinizione())) {
			model.getSpecificaImporti().setIgnoraValidazione(Boolean.FALSE);
			return SUCCESS;
		}
		List<DettaglioVariazioneComponenteImportoCapitolo> detts0 = unwrapped.get(ElementoComponenteVariazioneFactory.KEY_ANNO_0);
		List<DettaglioVariazioneComponenteImportoCapitolo> detts1 = unwrapped.get(ElementoComponenteVariazioneFactory.KEY_ANNO_1);
		List<DettaglioVariazioneComponenteImportoCapitolo> detts2 = unwrapped.get(ElementoComponenteVariazioneFactory.KEY_ANNO_2);
		
		GestisciDettaglioVariazioneComponenteImportoCapitolo req = model.creaRequestGestisciDettaglioVariazioneComponenteImportoCapitolo(detts0, detts1, detts2, Boolean.TRUE);
		GestisciDettaglioVariazioneComponenteImportoCapitoloResponse response = variazioneDiBilancioService.gestisciDettaglioVariazioneComponenteImportoCapitolo(req);
		
		if(response.hasErrori()){
			log.debug(methodName, "invocazione terminata con esito fallimento");
			addErrori(response);
			return SUCCESS;
		} //*/
		
		
		model.getSpecificaImporti().setIgnoraValidazione(Boolean.FALSE);
		model.getSpecificaImporti().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
		
		return SUCCESS;
	}
	
	/**
	 * Aggiorna capitoli con componenti nella variazione.
	 *
	 * @return the string
	 */
	public String aggiornaCapitoliConComponentiNellaVariazione() {
		final String methodName = "aggiornaCapitoliNellaVariazione";
		log.debug(methodName, "Aggiorno la lista");
		
		Map<String, List<DettaglioVariazioneComponenteImportoCapitolo>> unwrapped = ElementoComponenteVariazioneFactory.unWrapAllByAnno(model.getSpecificaImporti().getComponentiCapitoloDettaglio(), false);
		
		if (!validaComponenti(unwrapped)) {
			return SUCCESS;
		}
		
		ElementoCapitoloVariazione elementoCapitoloVariazioneDaAggiornare = model.getSpecificaImporti().getElementoCapitoloVariazione();
		if (!validaInserimentoCapitoloNellaVariazione(elementoCapitoloVariazioneDaAggiornare, model.getSpecificaImporti().getIgnoraValidazione(), model.getSpecificaImporti().getIgnoraValidazioneImportiDopoDefinizione())) {
			model.getSpecificaImporti().setIgnoraValidazione(Boolean.FALSE);
			model.getSpecificaImporti().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
			return SUCCESS;
		}

		List<DettaglioVariazioneComponenteImportoCapitolo> detts0 = unwrapped.get(ElementoComponenteVariazioneFactory.KEY_ANNO_0);
		List<DettaglioVariazioneComponenteImportoCapitolo> detts1 = unwrapped.get(ElementoComponenteVariazioneFactory.KEY_ANNO_1);
		List<DettaglioVariazioneComponenteImportoCapitolo> detts2 = unwrapped.get(ElementoComponenteVariazioneFactory.KEY_ANNO_2);
		
		GestisciDettaglioVariazioneComponenteImportoCapitolo req = model.creaRequestGestisciDettaglioVariazioneComponenteImportoCapitolo(detts0, detts1, detts2, Boolean.FALSE);
		GestisciDettaglioVariazioneComponenteImportoCapitoloResponse response = variazioneDiBilancioService.gestisciDettaglioVariazioneComponenteImportoCapitolo(req);
		
		if(response.hasErrori()){
			log.debug(methodName, "invocazione terminata con esito fallimento");
			addErrori(response);
			return SUCCESS;
		}
		
		log.debug(methodName, "richiamato il webService di aggiornamento capitoli nella variazione");
		model.getSpecificaImporti().setIgnoraValidazione(Boolean.FALSE);
		model.getSpecificaImporti().setIgnoraValidazioneImportiDopoDefinizione(Boolean.FALSE);
		
		return SUCCESS;
	}
	
	private boolean validaComponenti(Map<String, List<DettaglioVariazioneComponenteImportoCapitolo>> unwrapped) {
		//non so ancora bene che controlli dovrei fare, per il momento ritorno true
		return true;
	}
	
	/**
	 * Check dettaglio popolato correttamente.
	 */
	private void checkDettaglioPopolatoCorrettamente() {
		ElementoComponenteVariazione elementoComponenteModificata = model.getSpecificaImporti().getElementoComponenteModificata();
		checkNotNull(elementoComponenteModificata, "elemento componente");
		DettaglioVariazioneComponenteImportoCapitolo dettaglio = elementoComponenteModificata.getDettaglioVariazioneComponenteImportoCapitolo();
		checkNotNull(dettaglio, "dettaglio", true);
		checkNotNull(dettaglio.getImporto(), "importo");
		checkNotNull(dettaglio.getComponenteImportiCapitolo(), "componente importo", true);
		checkNotNullNorInvalidUid(dettaglio.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo(), "tipo componente", true);
		checkNotNull(dettaglio.getTipoDettaglioComponenteImportiCapitolo(), "tipo dettaglio componente");
		//controllo i dati degli anni successivi
		checkNotNull(elementoComponenteModificata.getDettaglioAnno1(), "dettaglio per l'anno di bilancio + 1");
		checkNotNull(elementoComponenteModificata.getImportoAnno1(), "importo anno di bilancio + 1");
		//controllo i dati degli anni successivi
		checkNotNull(elementoComponenteModificata.getDettaglioAnno2(), "dettaglio per l'anno di bilancio + 2");
		checkNotNull(elementoComponenteModificata.getImportoAnno2(), "importo anno di bilancio + 2");
	}
	
	/**
	 * Aggiungi componente A lista.
	 *
	 * @param componentiCapitoloNuovoDettaglio the componenti capitolo nuovo dettaglio
	 * @param elemento the elemento
	 * @return true, if successful
	 */
	private boolean aggiungiComponenteALista(List<ElementoComponenteVariazione> componentiCapitoloNuovoDettaglio, ElementoComponenteVariazione elemento) {
		if(componentiCapitoloNuovoDettaglio == null) {
			componentiCapitoloNuovoDettaglio = new ArrayList<ElementoComponenteVariazione>();
		}
		int index = DettaglioVariazioneComponenteImportoCapitoloHelper.getIndexDettaglioInListaElemento(componentiCapitoloNuovoDettaglio, elemento);
		if(index != -1) {
			addErrore(ErroreCore.OPERAZIONE_NON_CONSENTITA.getErrore("componente gia' presente in variazione"));
			return false;
		}
		TipoComponenteImportiCapitolo tipoComponenteConValori = ComparatorUtil.searchByUid(model.getListaTipoComponente(), elemento.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo());
		ElementoComponenteVariazione instanceNuovaComponente = ElementoComponenteVariazioneFactory.getInstanceNuovaComponente(tipoComponenteConValori, elemento, model.getApplicazione());
		
		componentiCapitoloNuovoDettaglio.add(instanceNuovaComponente);
		return true;
	}
	
	/**
	 * Modifica componente in lista.
	 *
	 * @param componentiCapitoloNuovoDettaglio the componenti capitolo nuovo dettaglio
	 * @param componenteModificata the componente modificata
	 */
	private void modificaComponenteInLista(List<ElementoComponenteVariazione> componentiCapitoloNuovoDettaglio, ElementoComponenteVariazione componenteModificata) {
		if(componentiCapitoloNuovoDettaglio == null) {
			componentiCapitoloNuovoDettaglio = new ArrayList<ElementoComponenteVariazione>();
		}
		int index = DettaglioVariazioneComponenteImportoCapitoloHelper.getIndexDettaglioInListaElemento(componentiCapitoloNuovoDettaglio, componenteModificata);
		if(index == -1) {
			//non ho trovato il dettaglio nella lista, cosa faccio?
			componentiCapitoloNuovoDettaglio.add(componenteModificata);
			return;
		}
		
		ElementoComponenteVariazione componenteOld = componentiCapitoloNuovoDettaglio.get(index);
		//forse cent5ralizzarlo e' un po' eccessivo, lascio questo come futura memoria che potrebbe aver senso farlo
//		ElementoComponenteVariazioneFactory.copiaDatiModificabiliSuVecchiaComponente(componenteOld, componenteModificata);
		Boolean eliminaSuTuttiGliAnni = componenteModificata.getEliminaSuTuttiGliAnni();
		componenteOld.setNuovaComponente(componenteModificata.getNuovaComponente());
		
		componenteOld.setEliminaSuTuttiGliAnni(componenteModificata.getEliminaSuTuttiGliAnni());
		componenteOld.getDettaglioAnno0().setImporto(Boolean.TRUE.equals(eliminaSuTuttiGliAnni) ? componenteOld.getImportoComponenteOriginaleCapitoloAnno0().negate() : model.arrotondaImporto(componenteModificata.getDettaglioAnno0().getImporto()));
		componenteOld.getDettaglioAnno1().setImporto(Boolean.TRUE.equals(eliminaSuTuttiGliAnni) ? componenteOld.getImportoComponenteOriginaleCapitoloAnno1().negate() : model.arrotondaImporto(componenteModificata.getDettaglioAnno1().getImporto()));
		componenteOld.getDettaglioAnno2().setImporto(Boolean.TRUE.equals(eliminaSuTuttiGliAnni) ? componenteOld.getImportoComponenteOriginaleCapitoloAnno2().negate() : model.arrotondaImporto(componenteModificata.getDettaglioAnno2().getImporto()));
		
		componentiCapitoloNuovoDettaglio.set(index, componenteOld);
	}
	
	/**
	 * @param componentiCapitoloNuovoDettaglio
	 * @param dettaglio
	 * @return
	 */
	private boolean eliminaComponenteDaLista(List<ElementoComponenteVariazione> componentiCapitoloNuovoDettaglio, ElementoComponenteVariazione componenteDaEliminare) {
		if(componentiCapitoloNuovoDettaglio == null) {
			componentiCapitoloNuovoDettaglio = new ArrayList<ElementoComponenteVariazione>();
		}
		int index = DettaglioVariazioneComponenteImportoCapitoloHelper.getIndexDettaglioInListaElemento(componentiCapitoloNuovoDettaglio, componenteDaEliminare);
		if(index == -1) {
			//non ho trovato il dettaglio nella lista, cosa faccio?
			addErrore(ErroreBil.OPERAZIONE_NON_POSSIBILE.getErrore("dettaglio componente non presente"));
			return false;
		}
		componentiCapitoloNuovoDettaglio.remove(index);
		return true;
	}
	
	
	/**
	 * Validate modifica componente nuovo dettaglio.
	 */
	public void validateInserisciNuovaComponenteNuovoDettaglio() {
		ElementoComponenteVariazione elementoComponenteModificata = model.getSpecificaImporti().getElementoComponenteModificata();
		checkNotNull(elementoComponenteModificata, "elemento componente");
		//controllo i dati del dettaglio dell'anno 0 (che "comanda")
		DettaglioVariazioneComponenteImportoCapitolo dettaglio = elementoComponenteModificata.getDettaglioVariazioneComponenteImportoCapitolo();
		checkNotNull(dettaglio, "dettaglio", true);
		checkNotNull(dettaglio.getImporto(), "importo");
		checkNotNull(dettaglio.getTipoDettaglioComponenteImportiCapitolo(), "tipo dettaglio componente");
		checkNotNull(dettaglio.getComponenteImportiCapitolo(), "componente importo", true);
		checkNotNullNorInvalidUid(dettaglio.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo(), "tipo componente", true);
		//controllo i dati degli anni successivi
		checkNotNull(elementoComponenteModificata.getDettaglioAnno1(), "dettaglio per l'anno di bilancio + 1");
		checkNotNull(elementoComponenteModificata.getImportoAnno1(), "importo anno di bilancio + 1");
		//controllo i dati degli anni successivi
		checkNotNull(elementoComponenteModificata.getDettaglioAnno2(), "dettaglio per l'anno di bilancio + 2");
		checkNotNull(elementoComponenteModificata.getImportoAnno2(), "importo anno di bilancio + 2");
	}
	
	/**
	 * Prepare inserisci nuova componente nuovo dettaglio.
	 */
	public void prepareInserisciNuovaComponenteDettaglio() {
		model.getSpecificaImporti().setElementoComponenteModificata(null);
	}
	
	/**
	 * Prepare inserisci nuova componente nuovo dettaglio.
	 */
	public void prepareInserisciNuovaComponenteNuovoDettaglio() {
		model.getSpecificaImporti().setElementoComponenteModificata(null);
	}
	
	/**
	 * Inserisci nuova componente dettaglio.
	 *
	 * @return the string
	 */
	public String inserisciNuovaComponenteNuovoDettaglio() {
		List<ElementoComponenteVariazione> componentiCapitoloNuovoDettaglio = model.getSpecificaImporti().getComponentiCapitoloNuovoDettaglio();
		ElementoComponenteVariazione dettaglio = model.getSpecificaImporti().getElementoComponenteModificata();
		boolean componenteAggiuntaConSuccesso = aggiungiComponenteALista(componentiCapitoloNuovoDettaglio, dettaglio);
		if(!componenteAggiuntaConSuccesso) {
			return INPUT;
		}
		model.getSpecificaImporti().ricalcolaStanziamentiNuovoDettaglio();
		return SUCCESS;
	}
	
	/**
	 * Validate modifica componente nuovo dettaglio.
	 */
	public void validateAggiornaComponenteNuovoDettaglioVariazione() {
		checkDettaglioPopolatoCorrettamente();
	}
	
	
	/**
	 * Prepare modifica componente nuovo dettaglio.
	 */
	public void prepareAggiornaComponenteNuovoDettaglioVariazione() {
		model.getSpecificaImporti().setElementoComponenteModificata(null);
	}
	
	
	/**
	 * Modifica componente nuovo dettaglio.
	 *
	 * @return the string
	 */
	public String aggiornaComponenteNuovoDettaglioVariazione() {
		List<ElementoComponenteVariazione> componentiCapitoloNuovoDettaglio = model.getSpecificaImporti().getComponentiCapitoloNuovoDettaglio();
		ElementoComponenteVariazione componenteModificata = model.getSpecificaImporti().getElementoComponenteModificata();
		modificaComponenteInLista(componentiCapitoloNuovoDettaglio, componenteModificata);
		model.getSpecificaImporti().ricalcolaStanziamentiNuovoDettaglio();
		return SUCCESS;
	}
	
	/**
	 * Validate modifica componente nuovo dettaglio.
	 */
	public void validateEliminaComponenteNuovoDettaglioVariazione() {
		checkDettaglioPopolatoCorrettamente();
	}
	
	
	/**
	 * Prepare modifica componente nuovo dettaglio.
	 */
	public void prepareEliminaComponenteNuovoDettaglioVariazione() {
		model.getSpecificaImporti().setElementoComponenteModificata(null);
	}
	
	/**
	 * Elimina componente nuovo dettaglio.
	 *
	 * @return the string
	 */
	public String eliminaComponenteNuovoDettaglioVariazione() {
		List<ElementoComponenteVariazione> componentiCapitoloNuovoDettaglio = model.getSpecificaImporti().getComponentiCapitoloNuovoDettaglio();
		ElementoComponenteVariazione componenteModificata = model.getSpecificaImporti().getElementoComponenteModificata();
		boolean eliminazioneSuccesso = eliminaComponenteDaLista(componentiCapitoloNuovoDettaglio, componenteModificata);
		if(!eliminazioneSuccesso) {
			return INPUT;
		}
		model.getSpecificaImporti().ricalcolaStanziamentiNuovoDettaglio();
		return SUCCESS;
	}
	
	/**
	 * Validate modifica componente nuovo dettaglio.
	 */
	public void validateInserisciNuovaComponenteDettaglio() {
		ElementoComponenteVariazione elementoComponenteModificata = model.getSpecificaImporti().getElementoComponenteModificata();
		checkNotNull(elementoComponenteModificata, "elemento componente");
		DettaglioVariazioneComponenteImportoCapitolo dettaglio = elementoComponenteModificata.getDettaglioVariazioneComponenteImportoCapitolo();
		checkNotNull(dettaglio, "dettaglio", true);
		checkNotNull(dettaglio.getImporto(), "importo");
		checkNotNull(dettaglio.getTipoDettaglioComponenteImportiCapitolo(), "tipo dettaglio componente");
		checkNotNull(dettaglio.getComponenteImportiCapitolo(), "componente importo", true);
		checkNotNullNorInvalidUid(dettaglio.getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo(), "tipo componente", true);
		
		//controllo i dati degli anni successivi
		checkNotNull(elementoComponenteModificata.getDettaglioAnno1(), "dettaglio per l'anno di bilancio + 1");
		checkNotNull(elementoComponenteModificata.getImportoAnno1(), "importo anno di bilancio + 1");
		//controllo i dati degli anni successivi
		checkNotNull(elementoComponenteModificata.getDettaglioAnno2(), "dettaglio per l'anno di bilancio + 2");
		checkNotNull(elementoComponenteModificata.getImportoAnno2(), "importo anno di bilancio + 2");
		
	}
	
	/**
	 * Inserisci nuova componente dettaglio.
	 *
	 * @return the string
	 */
	public String inserisciNuovaComponenteDettaglio() {
		List<ElementoComponenteVariazione> componentiCapitoloNuovoDettaglio = model.getSpecificaImporti().getComponentiCapitoloDettaglio();
		ElementoComponenteVariazione dettaglio = model.getSpecificaImporti().getElementoComponenteModificata();
		boolean componenteAggiuntaConSuccesso = aggiungiComponenteALista(componentiCapitoloNuovoDettaglio, dettaglio);
		if(!componenteAggiuntaConSuccesso) {
			return INPUT;
		}
		model.getSpecificaImporti().ricalcolaStanziamentiDettaglio();
		return SUCCESS;
	}
	
	/**
	 * Validate modifica componente nuovo dettaglio.
	 */
	public void validateAggiornaComponenteDettaglioVariazione() {
		checkDettaglioPopolatoCorrettamente();
	}
	
	
	/**
	 * Prepare modifica componente nuovo dettaglio.
	 */
	public void prepareAggiornaComponenteDettaglioVariazione() {
		model.getSpecificaImporti().setElementoComponenteModificata(null);
	}
	
	
	/**
	 * Modifica componente nuovo dettaglio.
	 *
	 * @return the string
	 */
	public String aggiornaComponenteDettaglioVariazione() {
		List<ElementoComponenteVariazione> componentiCapitoloNuovoDettaglio = model.getSpecificaImporti().getComponentiCapitoloDettaglio();
		ElementoComponenteVariazione componenteModificata = model.getSpecificaImporti().getElementoComponenteModificata();
		modificaComponenteInLista(componentiCapitoloNuovoDettaglio, componenteModificata);
		model.getSpecificaImporti().ricalcolaStanziamentiDettaglio();
		return SUCCESS;
	}
	
	/**
	 * Validate modifica componente nuovo dettaglio.
	 */
	public void validateEliminaComponenteDettaglioVariazione() {
		checkDettaglioPopolatoCorrettamente();
	}
	
	
	/**
	 * Prepare modifica componente nuovo dettaglio.
	 */
	public void prepareEliminaComponenteDettaglioVariazione() {
		model.getSpecificaImporti().setElementoComponenteModificata(null);
	}
	
	/**
	 * Elimina componente nuovo dettaglio.
	 *
	 * @return the string
	 */
	public String eliminaComponenteDettaglioVariazione() {
		List<ElementoComponenteVariazione> componentiCapitoloNuovoDettaglio = model.getSpecificaImporti().getComponentiCapitoloDettaglio();
		ElementoComponenteVariazione componenteModificata = model.getSpecificaImporti().getElementoComponenteModificata();
		boolean eliminazioneSuccesso = eliminaComponenteDaLista(componentiCapitoloNuovoDettaglio, componenteModificata);
		if(!eliminazioneSuccesso) {
			return INPUT;
		}
		model.getSpecificaImporti().ricalcolaStanziamentiDettaglio();
		return SUCCESS;
	}
	
	/**
	 * Prepare ricerca componenti importo capitolo in variazione.
	 */
	public void prepareRicercaComponentiImportoCapitoloInVariazione() {
		model.getSpecificaImporti().setComponentiCapitoloDettaglio(null);
	}
	
	/**
	 * Validate ricerca componenti importo capitolo in variazione.
	 */
	public void validateRicercaComponentiImportoCapitoloInVariazione() {
		checkCondition(model.getSpecificaImporti().getUidCapitoloAssociatoComponenti() != 0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("capitolo"));
	}
	
	/**
	 * Ricerca componenti importo capitolo in variazione.
	 *
	 * @return the string
	 */
	public String ricercaComponentiImportoCapitoloInVariazione() {
		RicercaDettaglioVariazioneComponenteImportoCapitolo req = model.creaRequestRicercaDettaglioVariazioneComponenteImportoCapitolo();
		RicercaDettaglioVariazioneComponenteImportoCapitoloResponse response = variazioneDiBilancioService.ricercaDettaglioVariazioneComponenteImportoCapitolo(req);
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		List<ElementoComponenteVariazione> componentiDelCapitolo = ElementoComponenteVariazioneFactory.getInstancesFromListeDettagliSui3Anni(response.getListaDettaglioVariazioneComponenteImportoCapitolo(),  response.getListaDettaglioVariazioneComponenteImportoCapitolo1(), response.getListaDettaglioVariazioneComponenteImportoCapitolo2(), model.getApplicazione());
		model.getSpecificaImporti().setComponentiCapitoloDettaglio(componentiDelCapitolo);
		return SUCCESS;
	}
	
	/**
	 * Ottieni editabilita nuova componente.
	 *
	 * @return the string
	 */
	public String ottieniEditabilitaByTipoComponente() {
		
		TipoComponenteImportiCapitolo tipoComponente = ComparatorUtil.searchByUid(model.getListaTipoComponente(), model.getSpecificaImporti().getTipoComponenteSelezionata());
		if(tipoComponente == null) {
			addErrore(ErroreCore.ENTITA_NON_TROVATA_SINGOLO_MSG.getErrore("tipo componente selezionata"));
			return INPUT;
		}
		
		ImportiCapitoloComponenteVariazioneModificabile importi = ImportiCapitoloComponenteVariazioneModificabile.getImportiCapitoloModificabile(model.getApplicazione(), tipoComponente.getMacrotipoComponenteImportiCapitolo(), tipoComponente.getSottotipoComponenteImportiCapitolo(), model.getSpecificaImporti().getTipoDettaglioSelezionato());
		
		model.getSpecificaImporti().setEditabileAnno0(Boolean.TRUE.equals(importi.getInserimentoAnno0()));
		model.getSpecificaImporti().setEditabileAnno1(Boolean.TRUE.equals(importi.getInserimentoAnniSuccessivi()));
		model.getSpecificaImporti().setEditabileAnno2(Boolean.TRUE.equals(importi.getInserimentoAnniSuccessivi()));
		return SUCCESS;
	}
	
	/**
	 * Redirezione verso riepilogo.
	 *
	 * @return the string
	 */
	public String redirezioneVersoRiepilogo() {
		RiepilogoVariazioneModel riepilogo = model.getRiepilogo();
		riepilogo.setNumeroVariazione(model.getNumeroVariazione());
		riepilogo.setStatoVariazione(getStatoOperativoVariazioneDiBilancioByUtente());
		riepilogo.setApplicazioneVariazione(model.getApplicazione().getDescrizione());
		riepilogo.setDescrizioneVariazione(model.getDescrizione());
		riepilogo.setTipoVariazione(model.getTipoVariazione());
		riepilogo.setNoteVariazione(model.getNote());
		impostaInformazioneSuccesso();
		leggiEventualiMessaggiAzionePrecedente();
		return SUCCESS;
	}
	
	private StatoOperativoVariazioneBilancio getStatoOperativoVariazioneDiBilancioByUtente() {
		return isAzioneConsentita(AzioneConsentitaEnum.RICERCA_VARIAZIONI_LIMITATA) ? 
				StatoOperativoVariazioneBilancio.PRE_BOZZA : StatoOperativoVariazioneBilancio.BOZZA;
	}
	
	/*
	 * SIAC-6884 restituisce le informazioni relative al Capitolo da aggiungere alla variazione*/
	protected Map<String, String> infoChapterToAdd(int uidCapitolo){
		RicercaDettaglioCapitoloUscitaGestione req = model.creaRequestRicercaDettaglioCapitoloUscitaGestione(uidCapitolo);
		
		
		
		
		return null;
		
	}
		
}
