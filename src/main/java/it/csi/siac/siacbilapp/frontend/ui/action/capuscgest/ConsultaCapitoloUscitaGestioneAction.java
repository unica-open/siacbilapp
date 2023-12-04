/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.capuscgest;

import java.util.ArrayList;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.commons.CapitoloUscitaAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.model.capuscgest.ConsultaCapitoloUscitaGestioneModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.TabellaImportiConComponentiCapitoloFactory;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaGestioneService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDisponibilitaCapitoloUscitaGestioneResponse;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;
import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.wrapper.EntitaConsultabileFilter;

/**
 * Classe di Action per la gestione della consultazione del Capitolo di Uscita
 * Gestione.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 30/07/2013
 * @version 1.1.0 22/07/2016 - spostato caricamento movimenti sull'AJAX
 *
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class ConsultaCapitoloUscitaGestioneAction extends CapitoloUscitaAction<ConsultaCapitoloUscitaGestioneModel> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -6009408563224038486L;

	@Autowired
	private transient CapitoloUscitaGestioneService capitoloUscitaGestioneService;

	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() {
		final String methodName = "execute";

		try {
			// Effettuo la ricerca di dettaglio del Capitolo
			ricercaDettaglioCapitolo();
			// CR-4324
			// TODO: valutare se caricarla via AJAX
			ricercaDisponibilitaCapitolo();
			// SIAC-4585
			gestisciMassimoImpegnabile();
			// SIAC-5169
			caricaLabelClassificatoriGenerici(BilConstants.CODICE_CAPITOLO_USCITA_GESTIONE.getConstant());
			// SIAC-6193
			gestisciFiltriEntitaConsultabili();
		} catch (WebServiceInvocationFailureException wsife) {
			log.debug(methodName, wsife.getMessage());
			return INPUT;
		}

		// Imposto model in sessione
		sessionHandler.setParametro(BilSessionParameter.MODEL_CONSULTA_CAPITOLO, model);

		return SUCCESS;
	}

	/**
	 * Ricerca di dettaglio del capitolo
	 * 
	 * @throws WebServiceInvocationFailureException
	 *             in caso di fallimento nell'invocazione del servizio
	 */
	private void ricercaDettaglioCapitolo() throws WebServiceInvocationFailureException {
		final String methodName = "ricercaDettaglioCapitolo";

		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
		RicercaDettaglioCapitoloUscitaGestione req = model.creaRequestRicercaDettaglioCapitoloUscitaGestione();
		RicercaDettaglioCapitoloUscitaGestioneResponse res = capitoloUscitaGestioneService
				.ricercaDettaglioCapitoloUscitaGestione(req);
		// Controllo gli errori
		if (res.hasErrori()) {
			// si sono verificati degli errori: esco.
			addErrori(res);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(RicercaDettaglioCapitoloUscitaGestione.class, res));
		}
		
		sessionHandler.setParametro(BilSessionParameter.CAPITOLO_PER_RICERCA_DETTAGLIO_VARIAZIONE, res.getCapitoloUscita().getUid());

		// GESC012
		/*
		log.debug("ricercaComponenteImportiCapitolo",
				"Richiamo il WebService di ricerca dettaglio componenti competenza");
		RicercaComponenteImportiCapitolo reqComponente = model.creaRequestRicercaComponenteImportiCapitolo();

		//SIAC-7349 - Start - SR210 - MR - 16/04/2020 - Abilito la chiamata per la disponibilita da mostrare nella tabella di riepilogo
		reqComponente.setAbilitaCalcoloDisponibilita(true);
		//SIAC-7349 - End
		//Salvo in sessione Richiedente e UidCapitolo
		RicercaComponenteImportiCapitoloResponse resComponente = componenteImportiCapitoloService
				.ricercaComponenteImportiCapitolo(reqComponente);
		log.debug("ricercaComponenteImportiCapitolo",
				"Richiamo il WebService di ricerca dettaglio componenti competenza");

		// Controllo gli errori
		if (resComponente.hasErrori()) {
			// si sono verificati degli errori: esco.
			addErrori(resComponente);
			throw new WebServiceInvocationFailureException(createErrorInServiceInvocationString(reqComponente, resComponente));
		}

		log.debug(methodName, "Impostazione dei dati nel model");
		
		List<ImportiCapitoloPerComponente> importiComponentiCapitolo = new ArrayList<ImportiCapitoloPerComponente>();
		log.debug("Helper toComponentiImportiCapitoloPerAnno", "Utilizzo Helper");
	
		importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper
				.toComponentiImportiCapitoloPerAnno(resComponente.getListaImportiCapitolo(), resComponente.getImportiCapitoloAnniSuccessivi());
		log.debug(methodName, "Impostazione degli importi componenti nel model");
		
		//SIAC-7227
//		if(resComponente.getListaImportiCapitolo().get(0) != null) {
//			importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnnoPrecedente(importiComponentiCapitolo, resComponente.getListaImportiCapitolo().get(0));
//		}
		if(resComponente.getListaImportiCapitolo().get(0) != null ) {
			importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnnoPrecedenteNew(importiComponentiCapitolo, resComponente.getListaImportiCapitolo().get(0));
		}
		
		
		
		//SIAC-7349 - SR210 - MR - Start - 12/05/2020 - Nuovo metodo per mostrare componenti negli anni successivi senza stanziamento
		if(resComponente.getListaImportiCapitoloAnniSuccessiviNoStanz() != null &&  !resComponente.getListaImportiCapitoloAnniSuccessiviNoStanz().isEmpty()) {
			importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerAnniSuccNoStanz(importiComponentiCapitolo, resComponente.getListaImportiCapitoloAnniSuccessiviNoStanz());
		}
		//SIAC-7349 - End
		
		//SIAC-7349 - GS - Start - 20/07/2020 - Nuovo metodo per mostrare componenti nel triennio senza stanziamento
		if(resComponente.getListaImportiCapitoloTriennioNoStanz() != null &&  !resComponente.getListaImportiCapitoloTriennioNoStanz().isEmpty()) {
			int countDefault = 0;
			for (ImportiCapitoloPerComponente i : importiComponentiCapitolo) {
				if (i.isPropostaDefault()) 
					countDefault++;
			}  
			int addIndex = importiComponentiCapitolo.size() - countDefault;
			Integer annoEsercizio = Integer.valueOf(sessionHandler.getAnnoEsercizio());
			importiComponentiCapitolo = ComponenteImportiCapitoloPerAnnoHelper.toComponentiImportiCapitoloPerTriennioNoStanz(importiComponentiCapitolo, resComponente.getListaImportiCapitoloTriennioNoStanz(), annoEsercizio, addIndex);
		}
		//SIAC-7349 - End
		
		//SIAC-7349 End
		//SIAC-7349 - Start - SR210 - MR - 16/04/2020 
		//Per non introdurre regressione, viene fatta una deepCopy dell'array, e successivamente
		//vengono aggiunte le Disponibilita in un nuovo array, e rimosso la disponibilita dall'array master.
		importiComponentiCapitolo = impostaDatiInMaschera(importiComponentiCapitolo);
		model.setImportiComponentiCapitolo(importiComponentiCapitolo);
		*/
		
		model.impostaDatiDaResponse(res);
		
		//vecchia gestione
		/*
		model.setImportiCapitoloSuccessivi(resComponente.getImportiCapitoloAnniSuccessivi());
		model.setImportiResidui(resComponente.getImportiCapitoloResiduo());
		
		
		//NUOVA GESTIONE
		// COMPETENZA
		List<ImportiCapitoloPerComponente> competenzaComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildCompetenzaRowUG(resComponente.getListaImportiCapitolo(),resComponente.getImportiCapitoloResiduo(), resComponente.getImportiCapitoloAnniSuccessivi(), competenzaComponenti);
		
		model.setCompetenzaStanziamento((competenzaComponenti.get(0) !=null) ? competenzaComponenti.get(0) : null);
		model.setCompetenzaImpegnato((competenzaComponenti.get(1) !=null) ? competenzaComponenti.get(1) : null);
		model.setDisponibilita((competenzaComponenti.get(2) !=null) ? competenzaComponenti.get(2) : null);
		
		///model.setCompetenza
		// RESIDUI
		List<ImportiCapitoloPerComponente> residuiComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildResiduiRow(resComponente.getListaImportiCapitolo(),resComponente.getImportiCapitoloResiduo(), resComponente.getImportiCapitoloAnniSuccessivi(), residuiComponenti);
		
		model.setResiduiPresunti((residuiComponenti.get(0) !=null) ? residuiComponenti.get(0) : null);
		model.setResiduiEffettivi((residuiComponenti.get(1) !=null) ? residuiComponenti.get(1) : null);

		
		//model.setResidui
		
		//CASSA
		List<ImportiCapitoloPerComponente> cassaComponenti = new ArrayList<ImportiCapitoloPerComponente>();
		ComponenteImportiCapitoloPerAnnoHelper.buildCassaRow( resComponente.getListaImportiCapitolo(),resComponente.getImportiCapitoloResiduo(),resComponente.getImportiCapitoloAnniSuccessivi(),cassaComponenti);
		
		model.setCassaStanziato((cassaComponenti.get(0) !=null) ? cassaComponenti.get(0) : null);
		model.setCassaPagato((cassaComponenti.get(1) !=null) ? cassaComponenti.get(1) : null);
		
		*/
		log.debug(methodName, "Dati impostati nel model");
		
		
	}

	
	public String caricaImporti() {
		RicercaComponenteImportiCapitolo reqComponente = model.creaRequestRicercaComponenteImportiCapitolo();

		//SIAC-7349 - Start - SR210 - MR - 16/04/2020 - Abilito la chiamata per la disponibilita da mostrare nella tabella di riepilogo
		reqComponente.setAbilitaCalcoloDisponibilita(true);
		//SIAC-7349 - End
		//Salvo in sessione Richiedente e UidCapitolo
		RicercaComponenteImportiCapitoloResponse resComponente = componenteImportiCapitoloService
				.ricercaComponenteImportiCapitolo(reqComponente);
		log.debug("ricercaComponenteImportiCapitolo",
				"Richiamo il WebService di ricerca dettaglio componenti competenza");

		// Controllo gli errori
		if (resComponente.hasErrori()) {
			// si sono verificati degli errori: esco.
			addErrori(resComponente);
			return INPUT;
		}
		
		TabellaImportiConComponentiCapitoloFactory factory = new TabellaImportiConComponentiCapitoloFactory();
		factory.init(model.getAnnoEsercizioInt(), TipoCapitolo.CAPITOLO_USCITA_GESTIONE, resComponente);
		factory.elaboraRigheConImportoInizialeEDisponibilita();
		
		model.setRigheImportiTabellaImportiCapitolo(factory.getRigheImportoTabellaElaborate());
		model.setRigheComponentiTabellaImportiCapitolo(factory.getRigheComponentiElaborate());
		
		
		model.setRigheDisponibilitaImpegnareComponenti(factory.getRigheDisponibilitaImpegnareComponenti());
		model.setRigheDisponibilitaVariareComponenti(factory.getRigheDisponibilitaVariareComponenti());
		
		
		return SUCCESS;
		
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

	/**
	 * Gestione del massimo impegnabile
	 */
	private void gestisciMassimoImpegnabile() {
		ImportiCapitoloUG icug0 = model.getImportiCapitoloUscitaGestione0();
		ImportiCapitoloUG icug1 = model.getImportiCapitoloUscitaGestione1();
		ImportiCapitoloUG icug2 = model.getImportiCapitoloUscitaGestione2();

		boolean hasMassimoImpegnabile = hasMassimoImpegnabile(icug0) || hasMassimoImpegnabile(icug1)
				|| hasMassimoImpegnabile(icug2);
		model.setHasMassimoImpegnabile(hasMassimoImpegnabile);
	}

	/**
	 * Aggiunta dei filtri pr le entit&agrave; consultabili:
	 * <ul>
	 * <li>Impegno: COMPETENZA, RESIDUO, PLURIENNALE</li>
	 * <li>Liquidazione: COMPETENZA, RESIDUO</li>
	 * <li>Orfinativo: COMPETENZA, RESIDUO</li>
	 * </ul>
	 */
	private void gestisciFiltriEntitaConsultabili() {
		// Sempre aggiungere il filtro vuoto
		model.getListaFiltroImpegno().add(new EntitaConsultabileFilter("", "Tutti", "checked='checked'"));
		model.getListaFiltroImpegno().add(new EntitaConsultabileFilter("C", "Competenza", ""));
		model.getListaFiltroImpegno().add(new EntitaConsultabileFilter("R", "Residuo", ""));
		model.getListaFiltroImpegno().add(new EntitaConsultabileFilter("P", "Pluriennale", ""));

		model.getListaFiltroLiquidazione().add(new EntitaConsultabileFilter("", "Tutti", "checked='checked'"));
		model.getListaFiltroLiquidazione().add(new EntitaConsultabileFilter("C", "Competenza", ""));
		model.getListaFiltroLiquidazione().add(new EntitaConsultabileFilter("R", "Residuo", ""));

		model.getListaFiltroOrdinativo().add(new EntitaConsultabileFilter("", "Tutti", "checked='checked'"));
		model.getListaFiltroOrdinativo().add(new EntitaConsultabileFilter("C", "Competenza", ""));
		model.getListaFiltroOrdinativo().add(new EntitaConsultabileFilter("R", "Residuo", ""));
	}

	/**
	 * Controlla se gli importi hanno il massimo impegnabile
	 * 
	 * @param icug
	 *            l'importo da controllare
	 * @return true se l'importo ha il massimo impegnabile
	 */
	private boolean hasMassimoImpegnabile(ImportiCapitoloUG icug) {
		return icug != null && icug.getMassimoImpegnabile() != null;
	}
	
	
	//SIAC-7349 - MR - SR210 - 12.05.2020 Questo metodo permette di visualizzare correttamente
	//i valori di impegnato, disp Variare e disp a Impegnare di ogni singola componente
	//nella maschera di consultazione
	private List<ImportiCapitoloPerComponente> impostaDatiInMaschera(List<ImportiCapitoloPerComponente> importiComponentiCapitolo){
		int sizeImporti = importiComponentiCapitolo.size();
		
		List<Integer> indiciDettagliDispImpVar= new ArrayList<Integer>();
		List<ImportiCapitoloPerComponente> listaDisponibilitaImpegnare = new ArrayList<ImportiCapitoloPerComponente>();
		List<ImportiCapitoloPerComponente> listaDisponibilitaVariare = new ArrayList<ImportiCapitoloPerComponente>();
		for(int i=0; i<sizeImporti; i++){
			TipoDettaglioComponenteImportiCapitolo dettaglioComponente = importiComponentiCapitolo.get(i).getTipoDettaglioComponenteImportiCapitolo();
			if(dettaglioComponente.equals(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAIMPEGNARE)
					||dettaglioComponente.equals(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAVARIARE)){
				indiciDettagliDispImpVar.add(i);							
			}
		}
		for(Integer index : indiciDettagliDispImpVar){
			TipoDettaglioComponenteImportiCapitolo tipoDettaglioComponente = importiComponentiCapitolo.get(index).getTipoDettaglioComponenteImportiCapitolo();
			
			if(tipoDettaglioComponente.equals(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAIMPEGNARE)){
				listaDisponibilitaImpegnare.add(ReflectionUtil.deepClone(importiComponentiCapitolo.get(index)));				
			} else if(tipoDettaglioComponente.equals(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAVARIARE)){
				listaDisponibilitaVariare.add(ReflectionUtil.deepClone(importiComponentiCapitolo.get(index)));				
			}
		}
		int j=indiciDettagliDispImpVar.size()-1;
		while (j>=0) {
			importiComponentiCapitolo.remove(importiComponentiCapitolo.get(indiciDettagliDispImpVar.get(j)));
			j--;			
		}
		//SIAC-7349 - Start - Sr210 MR 16/04/2020 Setting delle disponibilita impegnare nel model
		model.setDisponibilitaImpegnareComponentiCapitolo(listaDisponibilitaImpegnare);
		//SIAC-7349
				
		//SIAC-7349 - Start - Sr210 MR 16/04/2020 Setting delle disponibilita a variare nel model
		model.setDisponibilitaVariareComponentiCapitolo(listaDisponibilitaVariare);
		//SIAC-7349
		
		return importiComponentiCapitolo;

	}

}
