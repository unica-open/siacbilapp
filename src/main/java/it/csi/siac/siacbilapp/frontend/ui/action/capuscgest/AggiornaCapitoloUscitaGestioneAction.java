/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capuscgest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.interceptor.validation.SkipValidation;
import xyz.timedrain.arianna.plugin.BreadCrumb;
import xyz.timedrain.arianna.plugin.BreadCrumbTrail;
import xyz.timedrain.arianna.plugin.Crumb;

/*
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.softwareforge.struts2.breadcrumb.BreadCrumbTrail;
import org.softwareforge.struts2.breadcrumb.Crumb;
*/
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloUscitaAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscgest.AggiornaCapitoloUscitaGestioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoCapitoloUscita;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento.ClassificatoreAggiornamentoFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.TabellaImportiConComponentiCapitoloFactory;
import it.csi.siac.siacbilser.business.utility.capitolo.ComponenteImportiCapitoloPerAnnoHelper;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.ComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaCapitoloDiUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaAttributiModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.ControllaClassificatoriModificabiliCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancio;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioBilancioResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.StatoOperativoElementoDiBilancio;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.errore.ErroreBil;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;
import it.csi.siac.siaccommonapp.handler.session.CommonSessionParameter;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfinser.frontend.webservice.MovimentoGestioneService;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubImpegni;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSinteticaImpegniSubimpegniResponse;

/**
 * Classe di Action per la gestione dell'aggiornamento del Capitolo di Uscita
 * gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 30/07/2013
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class AggiornaCapitoloUscitaGestioneAction extends CapitoloUscitaAction<AggiornaCapitoloUscitaGestioneModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6484169745084755813L;

	@Autowired
	private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;
	@Autowired
	private transient ComponenteImportiCapitoloService componenteImportiCapitoloService;

	@Autowired 
	private transient MovimentoGestioneService movimentoGestioneService;
	/**
	 * Indica se sia necessario tornare indietro di due crumbs a termine
	 * dell'aggiornamento
	 */
	private boolean tornareDiDueIndietroDopoAggiornamento;

	/*
	 * Per portarsi dietro le componenti del capitolo una volta effettuato
	 * l'aggiornamento
	 */
	// private List<ImportiCapitoloPerComponente> importiComponentiCapitolo =
	// new ArrayList<ImportiCapitoloPerComponente>();
	//
	//
	// public List<ImportiCapitoloPerComponente> getImportiComponentiCapitolo()
	// {
	// return importiComponentiCapitolo;
	// }
	//
	// public void
	// setImportiComponentiCapitolo(List<ImportiCapitoloPerComponente>
	// importiComponentiCapitolo) {
	// this.importiComponentiCapitolo = importiComponentiCapitolo;
	// }

	@Override
	public void prepare() throws Exception {
		final String methodName = "prepare";
		log.debugStart(methodName, "Preparazione della action");
		super.prepare();

		caricaListaCodifiche(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE);
		ControllaClassificatoriModificabiliCapitoloResponse responseControlloClassificatoriEditabili = sessionHandler
				.getParametroXmlType(BilSessionParameter.EDITABILITA_CLASSIFICATORI,
						ControllaClassificatoriModificabiliCapitoloResponse.class);

		// Il controllo è necessario in caso di errore
		if (responseControlloClassificatoriEditabili != null) {
			log.debug(methodName, "Valuto l'editabilita dei campi");
			model.valutaModificabilitaClassificatori(responseControlloClassificatoriEditabili, false);
		}

		ControllaAttributiModificabiliCapitoloResponse responseControlloAttributiEditabili = sessionHandler
				.getParametroXmlType(BilSessionParameter.EDITABILITA_ATTRIBUTI,
						ControllaAttributiModificabiliCapitoloResponse.class);

		// Il controllo è necessario in caso di errore
		if (responseControlloAttributiEditabili != null) {
			log.debug(methodName, "Valuto l'editabilita dei campi");
			model.valutaModificabilitaAttributi(responseControlloAttributiEditabili, false);
		}

		log.debugEnd(methodName, "");
	}

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		String methodName = "execute";

		checkCasoDUsoApplicabile(model.getTitolo());
		log.debug(methodName, "Capitolo da aggiornare - uid: " + model.getUidDaAggiornare());

		/* Ricerca del capitolo */
		RicercaDettaglioCapitoloUscitaGestione request = model.creaRequestRicercaDettaglioCapitoloUscitaGestione();
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
		RicercaDettaglioCapitoloUscitaGestioneResponse response = capitoloUscitaGestioneService.ricercaDettaglioCapitoloUscitaGestione(request);
		
		// Controllo gli errori
		if (response.hasErrori()) {
			// si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio di ricerca dettaglio del capitolo.");
			addErrori(methodName, response);
			return INPUT;
		}

		// GESC012
		RicercaComponenteImportiCapitolo req = model.creaRequestRicercaComponenteImportiCapitolo();
		logServiceRequest(req);
		//SIAC-7349 - MR - SR210 - 12.05.2020 Abilito esecuzione nuovo servizio per le componenti
		req.setAbilitaCalcoloDisponibilita(true);
		RicercaComponenteImportiCapitoloResponse responseComponentiImportiCapitolo = componenteImportiCapitoloService.ricercaComponenteImportiCapitolo(req);
		// Controllo gli errori
		if (responseComponentiImportiCapitolo.hasErrori()) {
			// si sono verificati degli errori: esco.
			log.debug(methodName, "Errore nella risposta del servizio di ricercaComponenteImportiCapitol");
			addErrori("Componente Importi Capitolo ricercaComponenteImportiCapitolo()", responseComponentiImportiCapitolo);
			return INPUT;
		}
		
		
		
		impostaDatiComponentiNelModel(responseComponentiImportiCapitolo);
		
		ricercaDisponibilitaCapitolo();

        List<ImportiCapitoloPerComponente> importiComponentiCapitolo = getListaImporticomponente(responseComponentiImportiCapitolo);
		//
		
		log.debug(methodName, "Impostazione dei dati nel model");
		model.impostaDatiDaResponse(response, importiComponentiCapitolo);		
		
		boolean stanziamentiNegativiPresenti = checkStanziamentiNegativi(importiComponentiCapitolo);
		boolean freschiNonPresenti = checkComponentiNonFresco(importiComponentiCapitolo);
		model.setStanziamentiNegativiPresenti(stanziamentiNegativiPresenti);
		model.setComponentiDiversiDaFresco(freschiNonPresenti);
		
		log.debug(methodName, "Caricamento delle ulteriori liste");
		caricaListaCodificheAggiornamento();

		StatoOperativoElementoDiBilancio stato = response.getCapitoloUscita().getStatoOperativoElementoDiBilancio();
		if (!StatoOperativoElementoDiBilancio.VALIDO.equals(stato)
				&& !StatoOperativoElementoDiBilancio.PROVVISORIO.equals(stato)) {
			addErrore(ErroreBil.CAPITOLO_NON_AGGIORNABILE_PERCHE_STATO_INCONGRUENTE.getErrore());
			return INPUT;
		}
		// TODO: martellata, per favore mettere a posto
		if (StatoOperativoElementoDiBilancio.VALIDO.equals(stato)) {

			RicercaVariazioniCapitoloPerAggiornamentoCapitolo requestRicercaVariazioni = model
					.creaRequestRicercaVariazioniCapitoloPerAggiornamentoCapitolo(model.getUidDaAggiornare());
			logServiceRequest(requestRicercaVariazioni);
			RicercaVariazioniCapitoloPerAggiornamentoCapitoloResponse responseRicercaVariazioni = capitoloService
					.ricercaVariazioniCapitoloPerAggiornamentoCapitolo(requestRicercaVariazioni);
			logServiceResponse(responseRicercaVariazioni);

			if (responseRicercaVariazioni.hasErrori()) {
				log.debug(methodName,
						createErrorInServiceInvocationString(RicercaVariazioniCapitoloPerAggiornamentoCapitolo.class, responseRicercaVariazioni));
				addErrori(responseRicercaVariazioni);
				return INPUT;
			}

			if (responseRicercaVariazioni.getNumeroVariazioniImporti() > 0
					|| responseRicercaVariazioni.getNumeroVariazioniCodifiche() > 0) {
				String str = getStringaErroreVariazioniNonDefinitiveCollegate(
						responseRicercaVariazioni.getVariazioneImportiNumero(),
						responseRicercaVariazioni.getVariazioneCodificheNumero());
				addMessaggio(ErroreBil.CAPITOLO_CON_VARIAZIONI_NON_DEFINITIVE_COLLEGATE.getErrore(str));
				// return INPUT;
			}
		}

		log.debug(methodName, "Metto in sessione gli elementi già presenti nel model");
		sessionHandler.setParametro(BilSessionParameter.CLASSIFICATORI_AGGIORNAMENTO,
				ClassificatoreAggiornamentoFactory.getInstance(model));
		// Salvo in sessione la lista delle componenti con relativi importi,
		// della competenza
		sessionHandler.setParametro(BilSessionParameter.LISTA_IMPORTI_COMPONENTE_CAPITOLO, importiComponentiCapitolo);
		ControllaClassificatoriModificabiliCapitoloResponse responseClassificatoriModificabili = ottieniResponseControllaClassificatoriModificabiliCapitolo(
				model.getCapitoloUscitaGestione());

		log.debug(methodName, "Valuto l'editabilita dei campi");
		model.valutaModificabilitaClassificatori(responseClassificatoriModificabili, false);

		ControllaAttributiModificabiliCapitoloResponse responseAttributiModificabili = ottieniResponseControllaAttributiModificabiliCapitolo(
				model.getCapitoloUscitaGestione());
		log.debug(methodName, "Valuto l'editabilita degli attributi");
		model.valutaModificabilitaAttributi(responseAttributiModificabili, false);

		// Imposto la response per i classificatori modificabili in sessione
		sessionHandler.setParametroXmlType(BilSessionParameter.EDITABILITA_CLASSIFICATORI,
				responseClassificatoriModificabili);

		return SUCCESS;
	}

	/**
	 * @param responseComponentiImportiCapitolo
	 * @return
	 */
	private List<ImportiCapitoloPerComponente> getListaImporticomponente(RicercaComponenteImportiCapitoloResponse responseComponentiImportiCapitolo) {
		
		List<ImportiCapitoloPerComponente> importiComponentiCapitolo = new ArrayList<ImportiCapitoloPerComponente>();
//		importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnno(responseComponentiImportiCapitolo.getListaImportiCapitolo(),
//		responseComponentiImportiCapitolo.getImportiCapitoloResiduo(), responseComponentiImportiCapitolo.getImportiCapitoloAnniSuccessivi());
		importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper
				.toComponentiImportiCapitoloPerAnno(responseComponentiImportiCapitolo.getListaImportiCapitolo(), responseComponentiImportiCapitolo.getImportiCapitoloAnniSuccessivi());


		//SIAC-7227
		if(responseComponentiImportiCapitolo.getListaImportiCapitolo().get(0) != null) {
			importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnnoPrecedenteNew(importiComponentiCapitolo, responseComponentiImportiCapitolo.getListaImportiCapitolo().get(0));
		}
		
		//SIAC-7349 - SR210 - MR - 12.05.2020 Mostro anche la componente senza stanziamento
		//SIAC-7349 - SR210 - MR - Start - 12/05/2020 - Nuovo metodo per mostrare componenti negli anni successivi senza stanziamento
		if(responseComponentiImportiCapitolo.getListaImportiCapitoloAnniSuccessiviNoStanz() != null &&  !responseComponentiImportiCapitolo.getListaImportiCapitoloAnniSuccessiviNoStanz().isEmpty()) {
			importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnniSuccNoStanz(importiComponentiCapitolo, responseComponentiImportiCapitolo.getListaImportiCapitoloAnniSuccessiviNoStanz());
		}

		//SIAC-7349 - GS- Start - 21/07/2020 - Nuovo metodo per mostrare componenti nel triennio senza stanziamento
		if(responseComponentiImportiCapitolo.getListaImportiCapitoloTriennioNoStanz() != null &&  !responseComponentiImportiCapitolo.getListaImportiCapitoloTriennioNoStanz().isEmpty()) {
			int countDefault = 0;
			for (ImportiCapitoloPerComponente i : importiComponentiCapitolo) {
				if (i.isPropostaDefault()) 
					countDefault++;
			}  
			int addIndex = importiComponentiCapitolo.size() - countDefault;
			Integer annoEsercizio = Integer.valueOf(sessionHandler.getAnnoEsercizio());
			importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerTriennioNoStanz(importiComponentiCapitolo, responseComponentiImportiCapitolo.getListaImportiCapitoloTriennioNoStanz(), annoEsercizio, addIndex);
		}
		
		//SIAC-7349 - Start - SR210 - MR - 16/04/2020 
				//Per non introdurre regressione, viene fatta una deepCopy dell'array, e successivamente
				//vengono aggiunte le Disponibilita in un nuovo array, e rimosso la disponibilita dall'arrey master.
		int sizeImporti = importiComponentiCapitolo.size();				
		List<Integer> indiciDettagliDispImpVar= new ArrayList<Integer>();
		for(int i=0; i<sizeImporti; i++){
			TipoDettaglioComponenteImportiCapitolo dettaglioComponente = importiComponentiCapitolo.get(i).getTipoDettaglioComponenteImportiCapitolo();
			if(dettaglioComponente.equals(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAIMPEGNARE)
							||dettaglioComponente.equals(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAVARIARE)){
				indiciDettagliDispImpVar.add(i);							
			}
		}
		int j=indiciDettagliDispImpVar.size()-1;
		while (j>=0) {
			importiComponentiCapitolo.remove(importiComponentiCapitolo.get(indiciDettagliDispImpVar.get(j)));
			j--;			
		}	
		return importiComponentiCapitolo;
	}

	/**
	 * @param res
	 */
	private void impostaDatiComponentiNelModel(RicercaComponenteImportiCapitoloResponse res) {
		model.setImportiAnniSucc(res.getImportiCapitoloAnniSuccessivi());
		model.setImportiResidui(res.getImportiCapitoloResiduo());
				

		// FIXME aggiungere i controlli di errore exception call w

		// NUOVA GESTIONE - TODO MIGLIORARE IL CODICE
		// COMPETENZA
		List<ImportiCapitoloPerComponente> competenzaComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildCompetenzaRowUG(res.getListaImportiCapitolo(),
				res.getImportiCapitoloResiduo(), res.getImportiCapitoloAnniSuccessivi(), competenzaComponenti);

		model.setCompetenzaStanziamento((competenzaComponenti.get(0) != null) ? competenzaComponenti.get(0) : null);
		model.setCompetenzaImpegnato((competenzaComponenti.get(1) != null) ? competenzaComponenti.get(1) : null);
		model.setDisponibilita((competenzaComponenti.get(2) != null) ? competenzaComponenti.get(2) : null);

		/// model.setCompetenza
		// RESIDUI
		List<ImportiCapitoloPerComponente> residuiComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildResiduiRow(res.getListaImportiCapitolo(),
				res.getImportiCapitoloResiduo(), res.getImportiCapitoloAnniSuccessivi(), residuiComponenti);

		model.setResiduiPresunti((residuiComponenti.get(0) != null) ? residuiComponenti.get(0) : null);
		model.setResiduiEffettivi((residuiComponenti.get(1) != null) ? residuiComponenti.get(1) : null);

		// CASSA
		List<ImportiCapitoloPerComponente> cassaComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildCassaRow(res.getListaImportiCapitolo(),
				res.getImportiCapitoloResiduo(), res.getImportiCapitoloAnniSuccessivi(), cassaComponenti);

		model.setCassaStanziato((cassaComponenti.get(0) != null) ? cassaComponenti.get(0) : null);
		model.setCassaPagato((cassaComponenti.get(1) != null) ? cassaComponenti.get(1) : null);
	}
	
	//SIAC-7290
	private boolean checkStanziamentiNegativi(List<ImportiCapitoloPerComponente> importiComponentiCapitolo) {
		for(ImportiCapitoloPerComponente icp : importiComponentiCapitolo){
			if((icp.getDettaglioAnniSucc()!=null && icp.getDettaglioAnniSucc().getImporto().compareTo(BigDecimal.ZERO)<0) || 
					(icp.getDettaglioAnno0()!=null && icp.getDettaglioAnno0().getImporto().compareTo(BigDecimal.ZERO)<0) || 
					(icp.getDettaglioAnno1()!=null && icp.getDettaglioAnno1().getImporto().compareTo(BigDecimal.ZERO)<0) || 
					(icp.getDettaglioAnno2()!=null && icp.getDettaglioAnno2().getImporto().compareTo(BigDecimal.ZERO)<0) || 
					(icp.getDettaglioAnnoPrec()!=null && icp.getDettaglioAnnoPrec().getImporto().compareTo(BigDecimal.ZERO)<0) || 
					(icp.getDettaglioAnnoPrec()!=null && icp.getDettaglioAnnoPrec().getImporto().compareTo(BigDecimal.ZERO)<0)){					
				return true;	
			}
		}
		return false;
	}
	
	//SIAC-7290
	private boolean checkComponentiNonFresco(List<ImportiCapitoloPerComponente> importiComponentiCapitolo) {
		for(ImportiCapitoloPerComponente icp : importiComponentiCapitolo){
			if(icp.getTipoComponenteImportiCapitolo() != null && icp.getTipoComponenteImportiCapitolo().getMacrotipoComponenteImportiCapitolo() !=null &&
					icp.getTipoComponenteImportiCapitolo().getMacrotipoComponenteImportiCapitolo().getCodice() != null && 
					!icp.getTipoComponenteImportiCapitolo().getMacrotipoComponenteImportiCapitolo().getCodice().equals(MacrotipoComponenteImportiCapitolo.FRESCO.getCodice())){					
				return true;	
			}
		}
		return false;
	}

	/**
	 * Aggiorna il Capitolo di Uscita Gestione.
	 * 
	 * @return la String corrispondente al risultato della Action
	 * @throws WebServiceInvocationFailureException
	 */
	public String aggiornaCDU() throws WebServiceInvocationFailureException {
		final String methodName = "aggiornaCDU";
		/* Valorizzo i classificatori a partire dalla sessione */
		log.debug(methodName, "Valorizzo i classificatori");
		model.caricaClassificatoriDaSessione(sessionHandler);

		// Carico i dati dalla sessione
		log.debug(methodName, "Caricamento dei dati originali dalla sessione");
		ClassificatoreAggiornamentoCapitoloUscita classificatoreAggiornamento = sessionHandler.getParametro(
				BilSessionParameter.CLASSIFICATORI_AGGIORNAMENTO, ClassificatoreAggiornamentoCapitoloUscita.class);

		log.debug(methodName, "Creazione della request");
		AggiornaCapitoloDiUscitaGestione request = model
				.creaRequestAggiornaCapitoloDiUscitaGestione(classificatoreAggiornamento);
		logServiceRequest(request);

		log.debug(methodName, "Richiamo il WebService di aggiornamento");
		AggiornaCapitoloDiUscitaGestioneResponse response = capitoloUscitaGestioneService
				.aggiornaCapitoloDiUscitaGestione(request);
		log.debug(methodName, "Richiamato il WebService di aggiornamento");
		logServiceResponse(response);

		// Controllo gli errori
		if (response.hasErrori()) {
			// si sono verificati degli errori: esco.
			log.error(methodName, "Errore nella risposta del servizio");
			addErrori(methodName, response);
			caricaListaCodificheAggiornamento();
			return INPUT;
		}

		log.debug(methodName, "Capitolo di Uscita di Gestione aggiornato correttamente");
		/* Ripulisco la sessione */
		// Mantengo i risultati di ricerca per tornare alla pagina dei risultati
		// Mantengo la posizione di rientro
		sessionHandler.cleanSafelyExcluding(BilSessionParameter.RISULTATI_RICERCA_SINTETICA_CAPITOLO_USCITA_GESTIONE,
				BilSessionParameter.RIENTRO_POSIZIONE_START);

		/*
		 * Imposto in sessione il dato di rientro dall'aggiornamento, utile nel
		 * caso in cui si debba rientrare sui risultati di ricerca
		 */
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, "S");

		// Ripopolo gli eventuali campi che sono disabilitati
		model.setParametriDisabilitati(classificatoreAggiornamento);

		/*
		 * Imposta la necessita' di tornare indietro due volte a termine
		 * dell'esecuzione
		 */
		if (!model.isDaAggiornamento()) {
			tornareDiDueIndietroDopoAggiornamento = true;
		}
		List<ImportiCapitoloPerComponente> listImportiCompCap = (List<ImportiCapitoloPerComponente>) sessionHandler
				.getParametro(BilSessionParameter.LISTA_IMPORTI_COMPONENTE_CAPITOLO);
		// Carico la lista dei componenti dalla sessione. Se null, effettuo la
		// chiamata
		log.debug("Ricerca Lista Componenti", "Ricerca Lista componenti del capitolo");
		//
		RicercaComponenteImportiCapitolo req = model.creaRequestRicercaComponenteImportiCapitolo();
		//SIAC-7349 - MR - SR210 - 12.05.2020 Dopo aver aggiornato, richiama il servizio nuovo
		req.setAbilitaCalcoloDisponibilita(true);
		RicercaComponenteImportiCapitoloResponse res = componenteImportiCapitoloService
				.ricercaComponenteImportiCapitolo(req);
		// Controllo gli errori
		if (res.hasErrori()) {
			// si sono verificati degli errori: esco.
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaComponenteImportiCapitolo.class, res));
		}
		log.debug(methodName, "Impostazione dei dati nel model da ricerca componenti");
		
		List<ImportiCapitoloPerComponente> importiComponentiCapitolo = getListaImporticomponente(res);
		
		model.setImportiComponentiCapitolo(importiComponentiCapitolo);
		impostaDatiComponentiNelModel(res);

		ricercaDisponibilitaCapitolo();
		
		//SIAC-7600 mi assicuro che il controllo venga effettuato solo per il fondino
		ClassificatoreGenerico classGen3 =  model.getClassificatoreGenerico3();
		
		if((classGen3 != null && "SI".equals(classGen3.getDescrizione())) 
				&& (classGen3.getTipoClassificatore() != null && "Capitolo Budget".equals(classGen3.getTipoClassificatore().getDescrizione()))) {
		
			RicercaSinteticaImpegniSubImpegni request1 = model.creaRequestRicercaImpegniSubImpegni();
			RicercaSinteticaImpegniSubimpegniResponse responseTotaleImpegniCapitolo = movimentoGestioneService.ricercaSinteticaImpegniSubimpegni(request1);
			//qui bisognerebbe reindirizzare all'input in quanto non è possibile validare la JSP se il servizio fallisce.
			
			//SIAC-6884 controllo se ci sono impegni. Per ultimo in quanto altrimenti non viene popolata la tabella al redirect sull'INPUT
			if (responseTotaleImpegniCapitolo.getListaImpegni() != null && responseTotaleImpegniCapitolo.getListaImpegni().size() > 0) {
				
				String str = "" + responseTotaleImpegniCapitolo.getListaImpegni().size();
				//SIAC-7600 cambio l'errore
				addMessaggio(ErroreBil.CAPITOLO_BUDGET_CON_IMPEGNI_COLLEGATI.getErrore(str));
				caricaListaCodificheAggiornamento();
				return INPUT;
			}
			
		}
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		return SUCCESS;
	}

	public void validateAggiornaCDU() {
		CapitoloUscitaGestione cug = model.getCapitoloUscitaGestione();
		if (cug != null && !model.isGestioneUEB()) {
			cug.setNumeroUEB(Integer.valueOf(1));
		}
		// Controllo che il capitolo ci sia
		checkNotNull(cug, "Capitolo", true);

		checkNotNull(cug.getNumeroCapitolo(), "Capitolo");
		checkNotNull(cug.getNumeroArticolo(), "Articolo");
		checkNotNull(cug.getNumeroUEB(), "UEB");
		checkNotNullNorEmpty(cug.getDescrizione(), "Descrizione");
		
		//task-55
		Missione missioneConDati = ComparatorUtils.searchByUid(model.getListaMissione(), model.getMissione());
		if("20".equals(missioneConDati.getCodice())) {
			checkNotNull(cug.getFlagNonInserireAllegatoA1(), "Capitolo da non inserire nell'allegato A1");
		}else {
			model.getCapitoloUscitaGestione().setFlagNonInserireAllegatoA1(null);
		}
				
		checkCondition(
				!model.isCategoriaCapitoloEditabile()
						|| (cug.getCategoriaCapitolo() != null && cug.getCategoriaCapitolo().getUid() != 0),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Tipo capitolo"));

		// CR-2204: controllo la classificazione di bilancio solo per i tipi
		// standard
		if (isCategoriaCapitoloStandard(cug.getCategoriaCapitolo())) {
			checkCondition(
					!model.isMissioneEditabile() || (model.getMissione() != null && model.getMissione().getUid() != 0),
					ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Missione"));
			checkCondition(
					!model.isProgrammaEditabile()
							|| (model.getProgramma() != null && model.getProgramma().getUid() != 0),
					ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Programma"));
			checkCondition(
					!model.isTitoloSpesaEditabile()
							|| (model.getTitoloSpesa() != null && model.getTitoloSpesa().getUid() != 0),
					ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Titolo"));
			checkCondition(
					!model.isMacroaggregatoEditabile()
							|| (model.getMacroaggregato() != null && model.getMacroaggregato().getUid() != 0),
					ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Macroaggregato"));
			checkCondition(!model.isElementoPianoDeiContiEditabile()
					|| (model.getElementoPianoDeiConti() != null && model.getElementoPianoDeiConti().getUid() != 0),
					ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Elemento del Piano dei Conti"));
			//task-9
			checkCondition(!model.isClassificazioneCofogEditabile()
					|| (model.getClassificazioneCofog() != null && model.getClassificazioneCofog().getUid() != 0),
					ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Cofog"));
		}

		checkCondition(
				!model.isStrutturaAmministrativoContabileEditabile()
						|| (model.getStrutturaAmministrativoContabile() != null
								&& model.getStrutturaAmministrativoContabile().getUid() != 0),
				ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("Struttura Amministrativa Responsabile"));
		
		checkCondition(!isMissioneWithCodice(CODICE_MISSIONE_20) || model.idEntitaPresente(model.getRisorsaAccantonata()), ErroreCore.DATO_OBBLIGATORIO_OMESSO.getErrore("risorsa accantonata"));
	
		
	}

	/**
	 * Ottiene il crumb precedente a partire dallo stack dei breadcrumbs.
	 * 
	 * @return the previousCrumb
	 */
	@Override
	public Crumb getPreviousCrumb() {
		// Fallback in caso di mancata necessità
		if (!tornareDiDueIndietroDopoAggiornamento) {
			return super.getPreviousCrumb();
		}

		final String methodName = "getPreviousAction";
		// Il valore di default quando non vi sono precedenti actions è null
		// Qualora sulla pagina si trovi un null, l'indietro deve redirigere
		// verso il cruscotto
		Crumb previousCrumb = null;
		BreadCrumbTrail trail = sessionHandler.getParametro(CommonSessionParameter.BREADCRUMB_TRAIL,
				BreadCrumbTrail.class);
		try {
			int numeroCrumbs = trail.getCrumbs().size();
			// Ottengo DUE crumb precedenti rispetto a quello attuale
			previousCrumb = trail.getCrumbs().get(numeroCrumbs - 3);
		} catch (Exception e) {
			// Non dovrebbe succedere
			log.debug(methodName, "Il trail delle azioni precedenti non contiene sufficienti crumbs");
		}
		return previousCrumb;
	}
	
	/**
	 * Ricerca della disponibilit&agrave; del capitolo
	 * 
	 * @throws WebServiceInvocationFailureException
	 *             in caso di fallimento nell'invocazione del servizio
	 */
	private void ricercaDisponibilitaCapitolo() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDisponibilitaCapitolo";

		RicercaDisponibilitaCapitoloUscitaGestione req = model.creaRequestRicercaDisponibilitaCapitoloUscitaGestione();
		RicercaDisponibilitaCapitoloUscitaGestioneResponse res = capitoloUscitaGestioneService
				.ricercaDisponibilitaCapitoloUscitaGestione(req);

		// Controllo gli errori
		if (res.hasErrori()) {
			// si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDisponibilitaCapitoloUscitaGestione.class, res));
		}

		log.debug(methodName, "Impostazione delle disponibilita");
		model.setDisponibilitaCapitoloUscitaGestioneAnno0(res.getDisponibilitaCapitoloUscitaGestioneAnno0());
		model.setDisponibilitaCapitoloUscitaGestioneAnno1(res.getDisponibilitaCapitoloUscitaGestioneAnno1());
		model.setDisponibilitaCapitoloUscitaGestioneAnno2(res.getDisponibilitaCapitoloUscitaGestioneAnno2());
		model.setDisponibilitaCapitoloUscitaGestioneResiduo(res.getDisponibilitaCapitoloUscitaGestioneResiduo());
	}

	@Override
	protected void checkCasoDUsoApplicabile(String cdu) {
		RicercaDettaglioBilancio request = model.creaRequestRicercaDettaglioBilancio();
		RicercaDettaglioBilancioResponse response = bilancioService.ricercaDettaglioBilancio(request);

		FaseBilancio faseBilancio = response.getBilancio().getFaseEStatoAttualeBilancio().getFaseBilancio();
		boolean faseDiBilancioCompatibile = FaseBilancio.GESTIONE.equals(faseBilancio)
				|| FaseBilancio.ESERCIZIO_PROVVISORIO.equals(faseBilancio);

		if (!faseDiBilancioCompatibile) {
			throwOperazioneIncompatibileFaseBilancio(faseBilancio);
		}
	}
	
	public void validateCaricaImporti() {
		checkNotNullNorInvalidUid(model.getCapitoloUscitaGestione(), "identificativo univoco del capitolo per caricamento degli importi");
	}
	
	public String caricaImporti() {
		RicercaComponenteImportiCapitolo reqComponenti = model.creaRequestRicercaComponenteImportiCapitolo();
		RicercaComponenteImportiCapitoloResponse resComponenti = componenteImportiCapitoloService
				.ricercaComponenteImportiCapitolo(reqComponenti);

		// Controllo gli errori
		if (resComponenti.hasErrori()) {
			addErrori(resComponenti);
			return INPUT;
		}
		TabellaImportiConComponentiCapitoloFactory factory = new TabellaImportiConComponentiCapitoloFactory();
		factory.init(model.getAnnoEsercizioInt(), TipoCapitolo.CAPITOLO_USCITA_GESTIONE, resComponenti);
		factory.elaboraRigheConImportoIniziale();

		model.setRigheImportiTabellaImportiCapitolo(factory.getRigheImportoTabellaElaborate());
		model.setRigheComponentiTabellaImportiCapitolo(factory.getRigheComponentiElaborate());

		return SUCCESS;

	}

}
