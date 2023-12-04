/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.componenteimporticapitolo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.componenteimporticapitolo.AggiornaComponenteImportoCapitoloModel;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilser.business.utility.capitolo.ComponenteImportiCapitoloPerAnnoHelper;
import it.csi.siac.siacbilser.frontend.webservice.ComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.TipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.base.BaseComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.DettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.PropostaDefaultComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.StatoTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

/**
 * The Class AggiornaComponenteImportoCapitoloAction.
 * @deprecated in base alla segnalazione SIAC-7799. Usare GestioneComponenteImportoCapitoloNelCapitoloAction
 * Viene lasciata perche' viene ancora usata  nella pagina di aggiornamento (perche'?)
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
@Deprecated
public class AggiornaComponenteImportoCapitoloAction
		extends GenericBilancioAction<AggiornaComponenteImportoCapitoloModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2758811778233687091L;
	@Autowired
	private transient ComponenteImportiCapitoloService componenteImportiCapitoloService;
	@Autowired
	private transient TipoComponenteImportiCapitoloService tipoComponenteImportiCapitoloService;

	@Override
	public void prepare() throws Exception {
		super.prepare();
		cleanErroriMessaggiInformazioni();
	}

	@Override
	public void prepareExecute() throws Exception {
		setModel(null);
		super.prepare();
	}

	@Override
	public String execute() throws Exception {
		
		return SUCCESS;
	}

	// restituisce tutte le componenti di default

	/**
	 * Caricamento della tabella stanziamento
	 * 
	 * @return
	 */
	public String ricerca() {

		List<TipoComponenteImportiCapitolo> listaDefaultPrevisione = new ArrayList<TipoComponenteImportiCapitolo>();
		// per la response del capitolo
		RicercaComponenteImportiCapitolo req = model.creaRequestRicercaComponenteImportiCapitolo();
		//test!!!
		BaseComponenteImportiCapitoloResponse response = componenteImportiCapitoloService
				.ricercaComponenteImportiCapitolo(req);

		if (response.getErrori().isEmpty()) {
			//SIAC-7349 - Start - MR - 22/04/2020 - Patch Nullpointer Exception per modifica stanziamenti anni precedenti
//			response = inibisciEditabilitaAnniPrecedenti(response);
			//SIAC-7349 End
			listaDefaultPrevisione = allComponentiDefaultPrevisione(response);
			response = (BaseComponenteImportiCapitoloResponse) addDefaultToResponseNew(response, listaDefaultPrevisione);
			buildStanziamentoTabellaModel(response);
			
		} else {
			model.setErrori(response.getErrori());
		}

		return SUCCESS;
	}
	
	/**
	 * Elimina componente capitolo
	 * 
	 * @return
	 */
	public String elimina() {
		List<TipoComponenteImportiCapitolo> listaDefaultPrevisione = new ArrayList<TipoComponenteImportiCapitolo>();
		AnnullaComponenteImportiCapitolo req = model.creaAnnullaComponenteImportiCapitolo();
		BaseComponenteImportiCapitoloResponse response = componenteImportiCapitoloService
				.annullaComponenteImportiCapitolo(req);

		if (response.getErrori().isEmpty()) {
			listaDefaultPrevisione = allComponentiDefaultPrevisione(response);
			response = (BaseComponenteImportiCapitoloResponse) addDefaultToResponseNew(response, listaDefaultPrevisione);
			buildStanziamentoTabellaModel(response);
		} else {
			model.setErrori(response.getErrori());
		}
		return SUCCESS;
	}

	/**
	 * Elimina componente capitolo con uid
	 * 
	 * @return
	 */
	public String elimina(Integer uidTipoComponenteCapitolo) {
		List<TipoComponenteImportiCapitolo> listaDefaultPrevisione = new ArrayList<TipoComponenteImportiCapitolo>();
		AnnullaComponenteImportiCapitolo req = model.creaAnnullaComponenteImportiCapitolo(uidTipoComponenteCapitolo);
		BaseComponenteImportiCapitoloResponse response = componenteImportiCapitoloService
				.annullaComponenteImportiCapitolo(req);

		if (response.getErrori().isEmpty()) {
			listaDefaultPrevisione = allComponentiDefaultPrevisione(response);
			response = (BaseComponenteImportiCapitoloResponse) addDefaultToResponseNew(response, listaDefaultPrevisione);
			buildStanziamentoTabellaModel(response);
			/* Aggiungo l'informazione di completamento */
			model.addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		} else {
			model.setErrori(response.getErrori());
		}
		return SUCCESS;
	}

	/**
	 * 
	 * @return
	 */
	public String aggiornaComponenti() {
		validateImportiComponenti();
		List<TipoComponenteImportiCapitolo> listaDefaultPrevisione = new ArrayList<TipoComponenteImportiCapitolo>();
		boolean esitoOperazione = true;
		BaseComponenteImportiCapitoloResponse response = null;

		if (model.getErrori() == null || model.getErrori().isEmpty()) {
			if (model.isPropostaDiDefault()) {
				InserisceComponenteImportiCapitolo req = model
						.creaInserisceComponenteImportiCapitolo(model.getUidTipoComponenteCapitolo());
				boolean areZeros = zeroAllImport(req.getListaComponenteImportiCapitolo());
				if (areZeros == false) {
					response = componenteImportiCapitoloService.inserisceComponenteImportiCapitolo(req);
				}

			} else {

				AggiornaComponenteImportiCapitolo req = model.creaAggiornaComponenteImportiCapitolo();
				// se importi tutti zero, elimino la componente associata
				response = componenteImportiCapitoloService.aggiornaComponenteImportiCapitolo(req);

				boolean areZerosImport = zeroAllImport(req.getListaComponenteImportiCapitolo());
				if (areZerosImport == true) {
					return elimina(model.getUidTipoComponenteCapitolo());
				}

			}

			if (response.getErrori().isEmpty()) {
				listaDefaultPrevisione = allComponentiDefaultPrevisione(response);
				response = (BaseComponenteImportiCapitoloResponse) addDefaultToResponseNew(response, listaDefaultPrevisione);
				buildStanziamentoTabellaModel(response);
			} else {
				model.setErrori(response.getErrori());
				esitoOperazione = false;

			}

			if (esitoOperazione) {
				/* Aggiungo l'informazione di completamento */
				addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
			}
		}
		return SUCCESS;

	}

	/**
	 * 
	 * @return
	 */
	public String inserisciComponente() {
		validateImportiComponenti();
		List<TipoComponenteImportiCapitolo> listaDefaultPrevisione = new ArrayList<TipoComponenteImportiCapitolo>();
		if (model.getErrori() == null || model.getErrori().isEmpty()) {
			InserisceComponenteImportiCapitolo req = model.creaInserisceComponenteImportiCapitolo();
			// 7156
			boolean oneNotZeros = zeroAllImport(req.getListaComponenteImportiCapitolo());

			if (oneNotZeros == false) {
				BaseComponenteImportiCapitoloResponse response = componenteImportiCapitoloService
						.inserisceComponenteImportiCapitolo(req);
				if (response.getErrori().isEmpty()) {
					listaDefaultPrevisione = allComponentiDefaultPrevisione(response);
					response = (BaseComponenteImportiCapitoloResponse) addDefaultToResponseNew(response, listaDefaultPrevisione);
					buildStanziamentoTabellaModel(response);
				} else {
					model.setErrori(response.getErrori());
				}
			}

		}
		return SUCCESS;
	}

	
	/**
	 * Aggiornamento valorin residuo
	 * 
	 * @return
	 */
	public String aggiornaResiduo() {
		//SIAC-7349 - Start - MR - 22/04/2020
		/*
		 * Patch che permette di visualizzare le componenti di defualto all'aggiornamento della cassa e residui
		 * Parte mancante a differenza dell'aggiornamento delle componenti
		 * */
		List<TipoComponenteImportiCapitolo> listaDefaultPrevisione = new ArrayList<TipoComponenteImportiCapitolo>();
		validateImportiResiduo();
		if (model.getErrori() == null || model.getErrori().isEmpty()) {
			AggiornaImportiCapitolo req = model.creaAggiornaResiduo();
			AggiornaImportiCapitoloResponse response = componenteImportiCapitoloService.aggiornaImportiCapitolo(req);
			if (response.getErrori().isEmpty()) {
				//SIAC-7349 - Start - MR - 22/04/2020
				listaDefaultPrevisione = allComponentiDefaultPrevisione(response);
				response = (AggiornaImportiCapitoloResponse) addDefaultToResponseNew(response, listaDefaultPrevisione);
				//SIAC-7349 - End
				buildStanziamentoTabellaModel(response);
			} else {
				model.setErrori(response.getErrori());
			}
		}
		return SUCCESS;
	}

	/**
	 * Aggiornamento valori cassa
	 */
	public String aggiornaCassa() {
		//SIAC-7349 - Start - MR - 22/04/2020
		/*
		* Patch che permette di visualizzare le componenti di defualto all'aggiornamento della cassa e residui
		* Parte mancante a differenza dell'aggiornamento delle componenti
		* */
		List<TipoComponenteImportiCapitolo> listaDefaultPrevisione = new ArrayList<TipoComponenteImportiCapitolo>();
		validateImportiCassa();
		if (model.getErrori() == null || model.getErrori().isEmpty()) {
			AggiornaImportiCapitolo req = model.creaAggiornaCassa();
			AggiornaImportiCapitoloResponse response = componenteImportiCapitoloService.aggiornaImportiCapitolo(req);
			if (response.getErrori().isEmpty()) {
				//SIAC-7349 - Start - MR - 22/04/2020
				listaDefaultPrevisione = allComponentiDefaultPrevisione(response);
				response = (AggiornaImportiCapitoloResponse) addDefaultToResponseNew(response, listaDefaultPrevisione);
				//SIAC-7349 - End
				buildStanziamentoTabellaModel(response);
			} else {
				model.setErrori(response.getErrori());
			}
		}
		return SUCCESS;
	}

	/**
	 * Ricerca tipo Componente
	 * 
	 * @return
	 */
	public String ricercaTipoComponenteCapitolo() {

		/*
		 * Chiamata per avere le componenti collegate da NON inserire nella
		 * combo
		 */
		model.getListaTipoComponenti().clear(); // pulisco subito il model

		// tutte le componenti
		RicercaTipoComponenteImportiCapitoloPerCapitolo reqTipoCompCap = model
				.creaRicercaTipoComponenteImportiCapitoloPerCapitolo();
		RicercaTipoComponenteImportiCapitoloPerCapitoloResponse resTipoComCapitol = tipoComponenteImportiCapitoloService
				.ricercaTipoComponenteImportiCapitoloPerCapitolo(reqTipoCompCap);

		// filtro
		if (resTipoComCapitol != null) {

			// componenti collegate
			RicercaComponenteImportiCapitolo req = model.creaRequestRicercaComponenteImportiCapitolo();
			BaseComponenteImportiCapitoloResponse response = componenteImportiCapitoloService
					.ricercaComponenteImportiCapitolo(req);

			List<Integer> listaUidAssociati = componentiAssociati(response);

			if (response.getErrori().isEmpty()) {
				List<TipoComponenteImportiCapitolo> listaTipoComponenteImportiCapitolo = resTipoComCapitol
						.getListaTipoComponenteImportiCapitolo();
				// Escludo dalla combo le componenti già Associate e quelle di
				// default
				for (TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo : listaTipoComponenteImportiCapitolo) {
					//SIAC-6884-FRESCHI PER FONDINO
					if (tipoComponenteImportiCapitolo.getStatoTipoComponenteImportiCapitolo() != null && tipoComponenteImportiCapitolo.getStatoTipoComponenteImportiCapitolo().equals(StatoTipoComponenteImportiCapitolo.VALIDO) && model.getIsCapitoloFondino()//SE E' FONDINO
							&& tipoComponenteImportiCapitolo.getMacrotipoComponenteImportiCapitolo().equals(MacrotipoComponenteImportiCapitolo.FRESCO)
							&& !PropostaDefaultComponenteImportiCapitolo.SI.equals(tipoComponenteImportiCapitolo.getPropostaDefaultComponenteImportiCapitolo())
							&& !PropostaDefaultComponenteImportiCapitolo.SOLO_PREVISIONE.equals(tipoComponenteImportiCapitolo.getPropostaDefaultComponenteImportiCapitolo())
							&& !listaUidAssociati.contains(tipoComponenteImportiCapitolo.getUid())) {
						model.getListaTipoComponenti().add(tipoComponenteImportiCapitolo);
					}else if (tipoComponenteImportiCapitolo.getStatoTipoComponenteImportiCapitolo() != null && tipoComponenteImportiCapitolo.getStatoTipoComponenteImportiCapitolo().equals(StatoTipoComponenteImportiCapitolo.VALIDO) && !model.getIsCapitoloFondino()  
							&& !PropostaDefaultComponenteImportiCapitolo.SI.equals(tipoComponenteImportiCapitolo.getPropostaDefaultComponenteImportiCapitolo())
							&& !PropostaDefaultComponenteImportiCapitolo.SOLO_PREVISIONE.equals(tipoComponenteImportiCapitolo.getPropostaDefaultComponenteImportiCapitolo())
							&& !listaUidAssociati.contains(tipoComponenteImportiCapitolo.getUid())) {
						model.getListaTipoComponenti().add(tipoComponenteImportiCapitolo);
					}

				}

			}

		}
		return SUCCESS;
	}

	private void buildStanziamentoTabellaModel(BaseComponenteImportiCapitoloResponse response) {
		
		List<ImportiCapitoloPerComponente> importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper
				.toComponentiImportiCapitoloPerAnno(response.getListaImportiCapitolo(),
						response.getImportiCapitoloResiduo(), response.getImportiCapitoloAnniSuccessivi());
		
		//SIAC-7227
		//passo la nuova lista al model con il match delle varie componenti
//		if(response.getListaImportiCapitolo().get(0) != null) {
//			importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnnoPrecedente(importiComponentiCapitolo, response.getListaImportiCapitolo().get(0));
//		}
		
		//SIAC-7349 - Start - MR - 22/04/2020 - Commento quello sopra e utilizzo nuovo metodo in helper per mostrare le componenti
		//solo se hanno un impegnato nell'anno precedente.
		if(response.getListaImportiCapitolo().get(0) != null) {
			importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnnoPrecedenteNew(importiComponentiCapitolo, response.getListaImportiCapitolo().get(0));
		}
		//SIAC-7349 - Start - MR - 07/05/2020 - Metodo per mostrare le componenti senza stanziamento anni succ
		if(response.getListaImportiCapitoloAnniSuccessiviNoStanz() != null &&  !response.getListaImportiCapitoloAnniSuccessiviNoStanz().isEmpty()) {
			importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnniSuccNoStanz(importiComponentiCapitolo, response.getListaImportiCapitoloAnniSuccessiviNoStanz());
		}
		//SIAC-7349 - END
		
		

		//SIAC-7349 - Start - GS - 21/07/2020 - Metodo per mostrare le componenti senza stanziamento triennio

		if(response.getListaImportiCapitoloTriennioNoStanz() != null &&  !response.getListaImportiCapitoloTriennioNoStanz().isEmpty()) {
			int countDefault = 0;
			for (ImportiCapitoloPerComponente i : importiComponentiCapitolo) {
				if (i.isPropostaDefault()) 
					countDefault++;
			}  
			int addIndex = importiComponentiCapitolo.size() - countDefault;
			Integer annoEsercizio = Integer.valueOf(sessionHandler.getAnnoEsercizio());
			importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerTriennioNoStanz(importiComponentiCapitolo, response.getListaImportiCapitoloTriennioNoStanz(), annoEsercizio, addIndex);
		}
		//SIAC-7349 - END
		
		//
		//passo la lista al frontend sotto formato JSON (tramite action)    
		model.setImportiComponentiCapitolo(importiComponentiCapitolo);
		// IMPOSTARE OGGETTI COMPETENZA, CASSA, RESIDUO da mettere nel model
		// (anche in un metodo privato qui dentro)
		// COMPETENZA
		List<ImportiCapitoloPerComponente> competenzaComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildCompetenzaRowUP(response.getListaImportiCapitolo(),response.getImportiCapitoloResiduo(),response.getImportiCapitoloAnniSuccessivi(),competenzaComponenti);
		model.setCompetenzaComponenti(competenzaComponenti);
		// RESIDUI
		List<ImportiCapitoloPerComponente> residuiComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildResiduiRow(response.getListaImportiCapitolo(),
				response.getImportiCapitoloResiduo(), response.getImportiCapitoloAnniSuccessivi(), residuiComponenti);
		model.setResiduoComponenti(residuiComponenti);
		// CASSA
		List<ImportiCapitoloPerComponente> cassaComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildCassaRow(response.getListaImportiCapitolo(),
				response.getImportiCapitoloResiduo(), response.getImportiCapitoloAnniSuccessivi(), cassaComponenti);
		model.setCassaComponenti(cassaComponenti);
	}

	private void validateImportiComponenti() {
		boolean isValid = true;
		//SIAC-6884
		if(model.getIsCapitoloFondino()){
			if (model.getImportoStanziamentoAnno0() == null					
			|| model.getImportoStanziamentoAnno1() == null						
			|| model.getImportoStanziamentoAnno2() == null){
				isValid = false;
			}	
		}else if (!model.getIsCapitoloFondino()){
			if ((model.getImportoStanziamentoAnno0() != null
					&& model.getImportoStanziamentoAnno0().compareTo(BigDecimal.ZERO) < 0)
					|| (model.getImportoStanziamentoAnno1() != null
							&& model.getImportoStanziamentoAnno1().compareTo(BigDecimal.ZERO) < 0)
					|| (model.getImportoStanziamentoAnno2() != null
							&& model.getImportoStanziamentoAnno2().compareTo(BigDecimal.ZERO) < 0)) {
				isValid = false;
			}
		}
		
		checkCondition(isValid, ErroreCore.FORMATO_NON_VALIDO.getErrore());
	}

	private void validateImportiResiduo() {
		boolean isValid = true;
		if ((model.getImportoResiduoPresunto() != null
				&& model.getImportoResiduoPresunto().compareTo(BigDecimal.ZERO) < 0)) {
			isValid = false;
		}
		checkCondition(isValid, ErroreCore.FORMATO_NON_VALIDO.getErrore());
	}

	private void validateImportiCassa() {
		boolean isValid = true;
		if ((model.getImportoCassaStanziamento() != null
				&& model.getImportoCassaStanziamento().compareTo(BigDecimal.ZERO) < 0)) {
			isValid = false;
		}
		checkCondition(isValid, ErroreCore.FORMATO_NON_VALIDO.getErrore());
	}

	/**
	 * Controlla se l'uid della componente default, si trova gia associato al
	 * capitolo
	 * 
	 */

	// controllo se l'uid delle componenti previsione, sono gia state associate
	// al capitolo
	protected boolean isInLista(Integer uidComponente, List<Integer> listaUid) {
		if (uidComponente == null && (listaUid == null || listaUid.isEmpty())) {
			return false;
		}
		for (Integer uid : listaUid) {
			if (uid.equals(uidComponente))
				return true;
		}
		return false;
	}

	// Restituisce tutti i componenti di default previsione, non associati al
	// capitolo
	protected List<TipoComponenteImportiCapitolo> allComponentiDefaultPrevisione(
			BaseComponenteImportiCapitoloResponse response) {

		List<Integer> listaUidAssociati = new ArrayList<Integer>();
		// lista componenti di default previsione
		List<TipoComponenteImportiCapitolo> listaDefaultPrevisione = new ArrayList<TipoComponenteImportiCapitolo>();
		List<TipoComponenteImportiCapitolo> listaTipoComponenteImportiCapitolo = new ArrayList<TipoComponenteImportiCapitolo>();
		RicercaTipoComponenteImportiCapitoloPerCapitolo reqTipoCompCap = model
				.creaRicercaTipoComponenteImportiCapitoloPerCapitolo();
		RicercaTipoComponenteImportiCapitoloPerCapitoloResponse resTipoComCapitol = tipoComponenteImportiCapitoloService
				.ricercaTipoComponenteImportiCapitoloPerCapitolo(reqTipoCompCap);

		listaUidAssociati = componentiAssociati(response);
		if (resTipoComCapitol != null && !resTipoComCapitol.getListaTipoComponenteImportiCapitolo().isEmpty()) {
			listaTipoComponenteImportiCapitolo = resTipoComCapitol.getListaTipoComponenteImportiCapitolo();
			//SIAC-6884
			for (TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo : listaTipoComponenteImportiCapitolo) {
				
				if (tipoComponenteImportiCapitolo.getStatoTipoComponenteImportiCapitolo() != null && tipoComponenteImportiCapitolo.getStatoTipoComponenteImportiCapitolo().equals(StatoTipoComponenteImportiCapitolo.VALIDO) && !model.getIsCapitoloFondino() && !isInLista(tipoComponenteImportiCapitolo.getUid(), listaUidAssociati)
						&& tipoComponenteImportiCapitolo.getPropostaDefaultComponenteImportiCapitolo() != null
						&& (tipoComponenteImportiCapitolo.getPropostaDefaultComponenteImportiCapitolo()
								.equals(PropostaDefaultComponenteImportiCapitolo.SI)
								|| tipoComponenteImportiCapitolo.getPropostaDefaultComponenteImportiCapitolo()
										.equals(PropostaDefaultComponenteImportiCapitolo.SOLO_PREVISIONE))) {
					listaDefaultPrevisione.add(tipoComponenteImportiCapitolo);
 
				}
				//SIAC-SOLO COMPONENTE FRESCO PER CAPITOLO FONDINO
				else if (tipoComponenteImportiCapitolo.getStatoTipoComponenteImportiCapitolo() != null && tipoComponenteImportiCapitolo.getStatoTipoComponenteImportiCapitolo().equals(StatoTipoComponenteImportiCapitolo.VALIDO) && model.getIsCapitoloFondino() && !isInLista(tipoComponenteImportiCapitolo.getUid(), listaUidAssociati)
						&& tipoComponenteImportiCapitolo.getMacrotipoComponenteImportiCapitolo() != null
						&& tipoComponenteImportiCapitolo.getMacrotipoComponenteImportiCapitolo().equals(MacrotipoComponenteImportiCapitolo.FRESCO)						
						&& tipoComponenteImportiCapitolo.getPropostaDefaultComponenteImportiCapitolo() != null
						&& (tipoComponenteImportiCapitolo.getPropostaDefaultComponenteImportiCapitolo()
								.equals(PropostaDefaultComponenteImportiCapitolo.SI)
								|| tipoComponenteImportiCapitolo.getPropostaDefaultComponenteImportiCapitolo()
										.equals(PropostaDefaultComponenteImportiCapitolo.SOLO_PREVISIONE))) {
					listaDefaultPrevisione.add(tipoComponenteImportiCapitolo);

				}
			}

		}

		return listaDefaultPrevisione;
	}

	// restituisce lista delle componenti associate
	protected List<Integer> componentiAssociati(BaseComponenteImportiCapitoloResponse response) {
		List<Integer> listaUidAssociati = new ArrayList<Integer>();
		if (response.getListaImportiCapitolo().get(1) != null) {
			for (int k = 0; k < response.getListaImportiCapitolo().get(1).getListaComponenteImportiCapitolo()
					.size(); k++) {
				listaUidAssociati.add(response.getListaImportiCapitolo().get(1).getListaComponenteImportiCapitolo()
						.get(k).getTipoComponenteImportiCapitolo().getUid());
			}
		}	
	
		//SIAC-7349 - GS - 23/07/2020 - INIZIO -  Associo anche le componenti che non hanno stanziato ma hanno impegnato nel triennio
		if (response.getListaImportiCapitoloTriennioNoStanz() != null) {
			for (int k = 0; k < response.getListaImportiCapitoloTriennioNoStanz()
					.size(); k++) {
				listaUidAssociati.add(response.getListaImportiCapitoloTriennioNoStanz().get(k).getIdComp());
			}
		}
		//SIAC-7349 - GS - 23/07/2020 - FINE


		return listaUidAssociati;
	}

	// aggiunge le componenti filtrate alla response
	//SIAC-7349 - Start - MR - 22/04/2020
	/*
	 * 
	 * Commentato per non duplicare codice
	 * Se presenta problemi in futuro decommentare e riutilizzare questo*/
	/*protected BaseComponenteImportiCapitoloResponse addDefaultToResponse(BaseComponenteImportiCapitoloResponse response,
			List<TipoComponenteImportiCapitolo> listaDefaultPrevisione) {
		if (listaDefaultPrevisione != null && !listaDefaultPrevisione.isEmpty()) {
			for (int j = 0; j < listaDefaultPrevisione.size(); j++) {

				// aggiunta componenti default nella response
				for (int i = 1; i < response.getListaImportiCapitolo().size(); i++) {

					ComponenteImportiCapitolo cip = new ComponenteImportiCapitolo();
					cip.setTipoComponenteImportiCapitolo(listaDefaultPrevisione.get(j));

					DettaglioComponenteImportiCapitolo dcic = new DettaglioComponenteImportiCapitolo();
					dcic.setImporto(BigDecimal.ZERO);
					dcic.setPropostaDefault(true);
					dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
					// deve essere editabile??
					dcic.setEditabile(true);
					cip.getListaDettaglioComponenteImportiCapitolo().add(dcic);

					DettaglioComponenteImportiCapitolo dcic1 = new DettaglioComponenteImportiCapitolo();
					dcic1.setImporto(BigDecimal.ZERO);
					dcic1.setPropostaDefault(true);
					dcic1.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
					cip.getListaDettaglioComponenteImportiCapitolo().add(dcic1);

					// aggiungo il nuovo componente, alla lista dell'item
					// i-esimo
					response.getListaImportiCapitolo().get(i).getListaComponenteImportiCapitolo().add(cip);
				}

			}

		}

		return response;

	}*/

	// Controlla se gli importi sono a zero.
	protected boolean zeroAllImport(List<ComponenteImportiCapitolo> listaComponenteImportiCapitolo) {
		int i = 0;
		BigDecimal toCompare = new BigDecimal(0.00);

		for (ComponenteImportiCapitolo componente : listaComponenteImportiCapitolo) {
			for (DettaglioComponenteImportiCapitolo dettaglioComponente : componente
					.getListaDettaglioComponenteImportiCapitolo()) {
				if (dettaglioComponente.getImporto().compareTo(toCompare) != 0) {
					i++;
				}
			}
		}
		return i == 0;
	}
	//SIAC-7349 - Start - MR - 22/04/2020
	/*
	* Patch che permette di visualizzare le componenti di defualt all'aggiornamento della cassa e residui
	* Parte mancante a differenza dell'aggiornamento delle componenti
	* Metodo per diverse tipologie di parametri input, e tipo di ritorno
	* 
	* */
	private Object addDefaultToResponseNew(Object response,
			List<TipoComponenteImportiCapitolo> listaDefaultPrevisione) {
		List<ImportiCapitolo> listaPerResponse = new ArrayList<ImportiCapitolo>();
		if(response instanceof BaseComponenteImportiCapitoloResponse){			 
			 listaPerResponse = ((BaseComponenteImportiCapitoloResponse) response).getListaImportiCapitolo();
		}else if(response instanceof AggiornaImportiCapitoloResponse){
			 listaPerResponse = ((AggiornaImportiCapitoloResponse) response).getListaImportiCapitolo();
		}
		
		if (listaDefaultPrevisione != null && !listaDefaultPrevisione.isEmpty()) {
			for (int j = 0; j < listaDefaultPrevisione.size(); j++) {

				// aggiunta componenti default nella response
				for (int i = 1; i < listaPerResponse.size(); i++) {

					ComponenteImportiCapitolo cip = new ComponenteImportiCapitolo();
					cip.setTipoComponenteImportiCapitolo(listaDefaultPrevisione.get(j));

					DettaglioComponenteImportiCapitolo dcic = new DettaglioComponenteImportiCapitolo();
					dcic.setImporto(BigDecimal.ZERO);
					dcic.setPropostaDefault(true);
					dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
					// deve essere editabile??
					dcic.setEditabile(true);
					cip.getListaDettaglioComponenteImportiCapitolo().add(dcic);

					DettaglioComponenteImportiCapitolo dcic1 = new DettaglioComponenteImportiCapitolo();
					dcic1.setImporto(BigDecimal.ZERO);
					dcic1.setPropostaDefault(true);
					dcic1.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
					cip.getListaDettaglioComponenteImportiCapitolo().add(dcic1);

					// aggiungo il nuovo componente, alla lista dell'item
					// i-esimo
					listaPerResponse.get(i).getListaComponenteImportiCapitolo().add(cip);
				}

			}

		}

		return response;
	}


}
