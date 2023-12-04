/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.componenteimporticapitolo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.componenteimporticapitolo.GestioneComponenteImportoCapitoloNelCapitoloModel;
import it.csi.siac.siacbilapp.frontend.ui.util.annotation.PutModelInSession;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaComponenteTabellaImportiCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.TabellaImportiConComponentiCapitoloFactory;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaDettaglioImportoTabellaImportiCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaImportoTabellaImportiCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.TipologiaImportoTabellaImportoCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloUscitaPrevisioneService;
import it.csi.siac.siacbilser.frontend.webservice.ComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.TipoComponenteImportiCapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisioneResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitoloResponse;
import it.csi.siac.siacbilser.model.CategoriaCapitoloEnum;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.PropostaDefaultComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.StatoTipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siaccorser.model.Informazione;
import it.csi.siac.siaccorser.model.errore.ErroreCore;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@PutModelInSession
public class GestioneComponenteImportoCapitoloNelCapitoloAction extends GenericBilancioAction<GestioneComponenteImportoCapitoloNelCapitoloModel> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2758811778233687091L;
	@Autowired
	private transient ComponenteImportiCapitoloService componenteImportiCapitoloService;
	@Autowired
	private transient TipoComponenteImportiCapitoloService tipoComponenteImportiCapitoloService;
	
	@Autowired private transient CapitoloUscitaPrevisioneService capitoloUscitaPrevisioneService;

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
		//qui dovrei cercare i dati minimal del capitolo, non lo faccio per ora, mi faccio passare dalla pagina prima se si tratta di fondino, e considero come tipo capitolo = tipo_capitolo previsione
		caricaTipoComponente();
		
		caricaCapitolo();
		
		return SUCCESS;
	}
	
	private void caricaCapitolo() {
		String methodName="caricaCapitolo";
		RicercaDettaglioCapitoloUscitaPrevisione requestRicercaDettaglio = model.creaRequestRicercaDettaglioCapitoloUscitaPrevisione();
		logServiceRequest(requestRicercaDettaglio);
		
		log.debug(methodName, "Richiamo il WebService di ricerca dettaglio");
		RicercaDettaglioCapitoloUscitaPrevisioneResponse responseRicercaDettaglio = capitoloUscitaPrevisioneService.ricercaDettaglioCapitoloUscitaPrevisione(requestRicercaDettaglio);
		log.debug(methodName, "Richiamato il WebService di ricerca dettaglio");
		logServiceResponse(responseRicercaDettaglio);
		
		model.setCapitolo(responseRicercaDettaglio.getCapitoloUscitaPrevisione());
	}

	//SIAC-8003
	public boolean checkCapitoloCategoria() {
		 if(CategoriaCapitoloEnum.AAM.getCodice().equals(model.getCapitolo().getCategoriaCapitolo().getCodice())
			|| CategoriaCapitoloEnum.categoriaIsFPV(model.getCapitolo().getCategoriaCapitolo().getCodice())
			|| CategoriaCapitoloEnum.DAM.getCodice().equals(model.getCapitolo().getCategoriaCapitolo().getCodice())){
			 return true;
		 }else {
			 return false;
		 }
	}
	
	public String caricaListaTipoComponentiPerNuovaComponente() {
		return SUCCESS;
	}
	
	
	
	
	/**
	 * Ricerca tipo Componente.
	 */
	private void caricaTipoComponente() {

		/*
		 * Chiamata per avere le componenti collegate da NON inserire nella
		 * combo
		 */
		// tutte le componenti
		RicercaTipoComponenteImportiCapitoloPerCapitolo req = model.creaRicercaTipoComponenteImportiCapitoloPerCapitolo();
		RicercaTipoComponenteImportiCapitoloPerCapitoloResponse res = tipoComponenteImportiCapitoloService.ricercaTipoComponenteImportiCapitoloPerCapitolo(req);
		
		if(res.hasErrori()) {
			addErrori(res);
			return;
		}
		
		List<TipoComponenteImportiCapitolo> tipoComponenteDefault = new ArrayList<TipoComponenteImportiCapitolo>();
		List<TipoComponenteImportiCapitolo> tipoComponentePerNuovaComponente = new ArrayList<TipoComponenteImportiCapitolo>();
		
		for (TipoComponenteImportiCapitolo tipo : res.getListaTipoComponenteImportiCapitolo()) {
			if(!isTipoComponenteValida(tipo) || isCapitoloFondinoENonTipoFresco(tipo)) {
				continue;
			}
			
			if(isPropostaDefault(tipo)) {
				tipoComponenteDefault.add(tipo);
			}else{
				tipoComponentePerNuovaComponente.add(tipo);
			}
			
		}
		
		model.setListaTipoComponentiDefault(tipoComponenteDefault);
		model.setListaTipoComponentiPerNuovaComponente(tipoComponentePerNuovaComponente);

	}


	// restituisce tutte le componenti di default

	/**
	 * Caricamento della tabella stanziamento
	 * 
	 * @return
	 */
	public String caricaImporti() {

		// per la response del capitolo
		RicercaComponenteImportiCapitolo req = model.creaRequestRicercaComponenteImportiCapitolo();
		RicercaComponenteImportiCapitoloResponse response = componenteImportiCapitoloService.ricercaComponenteImportiCapitolo(req);

		if(response.hasErrori()) {
			addErrori(response);
			return SUCCESS;
		}
		//faccio tutti i calcoli
		TabellaImportiConComponentiCapitoloFactory factory = new TabellaImportiConComponentiCapitoloFactory();
		//task-236
		//factory.init(model.getAnnoEsercizioInt(), TipoCapitolo.CAPITOLO_USCITA_PREVISIONE, response, model.getListaTipoComponentiDefault());
		factory.init(model.getAnnoEsercizioInt(), model.getCapitolo().getCategoriaCapitolo(),TipoCapitolo.CAPITOLO_USCITA_PREVISIONE, response, model.getListaTipoComponentiDefault());		
		factory.elaboraRigheTabella();
		model.setRigheImportiTabellaImportiCapitolo(factory.getRigheImportoTabellaElaborate());
		
		factory.elaboraRigheTabellaConComponentiDefault();
		model.setRigheImportiTabellaImportiCapitolo(factory.getRigheImportoTabellaElaborate());
		model.setRigheComponentiTabellaImportiCapitolo(factory.getRigheComponentiElaborate());
		
		return SUCCESS;
	}
	
	public void prepareAggiornaResiduo() {
		model.setImportoStanziamentoModificato(BigDecimal.ZERO);
	}
	
	public void validateAggiornaResiduo() {
		checkCondition(model.getImportoStanziamentoModificato() != null && BigDecimal.ZERO.compareTo(model.getImportoStanziamentoModificato())<=0, ErroreCore.FORMATO_NON_VALIDO.getErrore());
	}
	
	public String aggiornaResiduo() {
		BigDecimal importoCassa = extractImportoCassa();
		
		AggiornaImportiCapitolo req = model.creaRequestAggiornaImportiCapitolo(model.getImportoStanziamentoModificato(), importoCassa);
		AggiornaImportiCapitoloResponse response = componenteImportiCapitoloService.aggiornaImportiCapitolo(req);
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		//faccio tutti i calcoli
		TabellaImportiConComponentiCapitoloFactory factory = new TabellaImportiConComponentiCapitoloFactory();
		factory.init(model.getAnnoEsercizioInt(), TipoCapitolo.CAPITOLO_USCITA_PREVISIONE, response, model.getListaTipoComponentiDefault());
		
		factory.elaboraRigheTabellaConComponentiDefault();
		model.setRigheImportiTabellaImportiCapitolo(factory.getRigheImportoTabellaElaborate());
		model.setRigheComponentiTabellaImportiCapitolo(factory.getRigheComponentiElaborate());
				
		return SUCCESS;
	}
	
	public void prepareAggiornaCassa() {
		model.setImportoStanziamentoModificato(BigDecimal.ZERO);
	}
	
	public void validateAggiornaCassa() {
		checkCondition(model.getImportoStanziamentoModificato() != null && BigDecimal.ZERO.compareTo(model.getImportoStanziamentoModificato())<=0, ErroreCore.FORMATO_NON_VALIDO.getErrore());
	}
	
	public String aggiornaCassa() {
		BigDecimal importoResiduo = extractImportoResiduo(); model.getRigheImportiTabellaImportiCapitolo();
		AggiornaImportiCapitolo req = model.creaRequestAggiornaImportiCapitolo(importoResiduo, model.getImportoStanziamentoModificato());
		AggiornaImportiCapitoloResponse response = componenteImportiCapitoloService.aggiornaImportiCapitolo(req);
		
		//faccio tutti i calcoli
		TabellaImportiConComponentiCapitoloFactory factory = new TabellaImportiConComponentiCapitoloFactory();
		//task-236
		//factory.init(model.getAnnoEsercizioInt(), TipoCapitolo.CAPITOLO_USCITA_PREVISIONE, response, model.getListaTipoComponentiDefault());
		factory.init(model.getAnnoEsercizioInt(), model.getCapitolo().getCategoriaCapitolo(), TipoCapitolo.CAPITOLO_USCITA_PREVISIONE, response, model.getListaTipoComponentiDefault());
		
		factory.elaboraRigheTabellaConComponentiDefault();
		model.setRigheImportiTabellaImportiCapitolo(factory.getRigheImportoTabellaElaborate());
		model.setRigheComponentiTabellaImportiCapitolo(factory.getRigheComponentiElaborate());
				
		return SUCCESS;
	}
	
	
	
	public void validateAggiornaComponenti() {
		checkCondition(model.getUidTipoComponenteCapitolo() != null && model.getUidTipoComponenteCapitolo().intValue() !=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("identificativo del tipo componente da inserire"));
		validateImportiComponenti();
	}
	
	public String aggiornaComponenti() {
		//gestione componenti default
		boolean importiTuttiAZero = BigDecimal.ZERO.compareTo(model.getImportoStanziamentoAnno0()) == 0 && BigDecimal.ZERO.compareTo(model.getImportoStanziamentoAnno1()) ==0  && BigDecimal.ZERO.compareTo(model.getImportoStanziamentoAnno2()) == 0;
		boolean daInserire = isAggiornaSuComponenteDaInserire();

		if(importiTuttiAZero && daInserire) {
			return SUCCESS;
		}
		
		if(daInserire) {
			return inserisciComponenteSuCapitolo();
		}
		
		return aggiornaComponenteSulCapitolo(importiTuttiAZero);

	}

	
	private String aggiornaComponenteSulCapitolo(boolean importiTuttiAZero) {
		AggiornaComponenteImportiCapitolo reqAgg = model.creaRequestAggiornaComponenteImportiCapitolo();
		// se importi tutti zero, elimino la componente associata
		AggiornaComponenteImportiCapitoloResponse responseAgg = componenteImportiCapitoloService.aggiornaComponenteImportiCapitolo(reqAgg);
		
		if(responseAgg.hasErrori()) {
			addErrori(responseAgg);
			return INPUT;
		}
		
		if(importiTuttiAZero) {
			AnnullaComponenteImportiCapitolo reqAnn = model.creaRequestAnnullaComponenteImportiCapitolo();
			AnnullaComponenteImportiCapitoloResponse responseAnn = componenteImportiCapitoloService.annullaComponenteImportiCapitolo(reqAnn);
			if(responseAnn.hasErrori()) {
				addErrori(responseAnn);
				return INPUT;
			}
		}
		
		TabellaImportiConComponentiCapitoloFactory factory = new TabellaImportiConComponentiCapitoloFactory();
		//task-236
		//factory.init(model.getAnnoEsercizioInt(), TipoCapitolo.CAPITOLO_USCITA_PREVISIONE, responseAgg, model.getListaTipoComponentiDefault());
		factory.init(model.getAnnoEsercizioInt(),  model.getCapitolo().getCategoriaCapitolo(), TipoCapitolo.CAPITOLO_USCITA_PREVISIONE, responseAgg, model.getListaTipoComponentiDefault());
		
		factory.elaboraRigheTabellaConComponentiDefault();
		model.setRigheImportiTabellaImportiCapitolo(factory.getRigheImportoTabellaElaborate());
		model.setRigheComponentiTabellaImportiCapitolo(factory.getRigheComponentiElaborate());
		
		/* Aggiungo l'informazione di completamento */
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		return SUCCESS;
	}

	private String inserisciComponenteSuCapitolo() {
		InserisceComponenteImportiCapitolo req = model.creaRequestInserisceComponenteImportiCapitolo();
		InserisceComponenteImportiCapitoloResponse response = componenteImportiCapitoloService.inserisceComponenteImportiCapitolo(req);
		if(response.hasErrori()) {
			addErrori(response);
			return INPUT;
		}
		TabellaImportiConComponentiCapitoloFactory factory = new TabellaImportiConComponentiCapitoloFactory();
		//task-236
		//factory.init(model.getAnnoEsercizioInt(), TipoCapitolo.CAPITOLO_USCITA_PREVISIONE, response, model.getListaTipoComponentiDefault());
		factory.init(model.getAnnoEsercizioInt(), model.getCapitolo().getCategoriaCapitolo(), TipoCapitolo.CAPITOLO_USCITA_PREVISIONE, response, model.getListaTipoComponentiDefault());
		
		factory.elaboraRigheTabellaConComponentiDefault();
		model.setRigheImportiTabellaImportiCapitolo(factory.getRigheImportoTabellaElaborate());
		model.setRigheComponentiTabellaImportiCapitolo(factory.getRigheComponentiElaborate());
		
		/* Aggiungo l'informazione di completamento */
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		return SUCCESS;
	}

	
	public String elimina() {
		AnnullaComponenteImportiCapitolo reqAnn = model.creaRequestAnnullaComponenteImportiCapitolo();
		AnnullaComponenteImportiCapitoloResponse responseAnn = componenteImportiCapitoloService.annullaComponenteImportiCapitolo(reqAnn);
		if(responseAnn.hasErrori()) {
			addErrori(responseAnn);
			return INPUT;
		}
		
		TabellaImportiConComponentiCapitoloFactory factory = new TabellaImportiConComponentiCapitoloFactory();
		//task-236
		//factory.init(model.getAnnoEsercizioInt(), TipoCapitolo.CAPITOLO_USCITA_PREVISIONE, responseAnn, model.getListaTipoComponentiDefault());
		factory.init(model.getAnnoEsercizioInt(),  model.getCapitolo().getCategoriaCapitolo(), TipoCapitolo.CAPITOLO_USCITA_PREVISIONE, responseAnn, model.getListaTipoComponentiDefault());
				
		
		factory.elaboraRigheTabellaConComponentiDefault();
		model.setRigheImportiTabellaImportiCapitolo(factory.getRigheImportoTabellaElaborate());
		model.setRigheComponentiTabellaImportiCapitolo(factory.getRigheComponentiElaborate());
		
		addInformazione(new Informazione("CRU_CON_2001", "L'operazione e' stata completata con successo"));
		return SUCCESS;
	}

	
	public void validateInserisciNuovaComponente() {
		checkCondition(model.getUidTipoComponenteCapitolo() !=0, ErroreCore.PARAMETRO_NON_INIZIALIZZATO.getErrore("identificativo del tipo componente da inserire"));
		validateImportiComponenti();
	}

	/**
	 * 
	 * @return
//	 */
	public String inserisciNuovaComponente() {
		if(BigDecimal.ZERO.compareTo(model.getImportoStanziamentoAnno0()) == 0 && BigDecimal.ZERO.compareTo(model.getImportoStanziamentoAnno1()) ==0  && BigDecimal.ZERO.compareTo(model.getImportoStanziamentoAnno2()) == 0) {
			return SUCCESS;
		}
		
		return inserisciComponenteSuCapitolo();
		
	}
	

	private boolean isTipoComponenteValida(TipoComponenteImportiCapitolo tp) {
		StatoTipoComponenteImportiCapitolo stato = tp.getStatoTipoComponenteImportiCapitolo() ;
		return stato != null & StatoTipoComponenteImportiCapitolo.VALIDO.equals(stato);
	}
	
	private boolean isPropostaDefault(TipoComponenteImportiCapitolo tp) {
		PropostaDefaultComponenteImportiCapitolo proposta = tp.getPropostaDefaultComponenteImportiCapitolo();
		return PropostaDefaultComponenteImportiCapitolo.SI.equals(proposta) 
				|| PropostaDefaultComponenteImportiCapitolo.SOLO_PREVISIONE.equals(proposta);
	}

	private boolean isCapitoloFondinoENonTipoFresco(TipoComponenteImportiCapitolo tp) {
		return model.getIsCapitoloFondino() && !MacrotipoComponenteImportiCapitolo.FRESCO.equals(tp.getMacrotipoComponenteImportiCapitolo());
	}
	
	private BigDecimal extractImportoCassa() {
		for (RigaImportoTabellaImportiCapitolo imp : model.getRigheImportiTabellaImportiCapitolo()) {
			if(TipologiaImportoTabellaImportoCapitolo.CASSA.equals(imp.getTipoImportiCapitoloTabella())) {
				for (RigaDettaglioImportoTabellaImportiCapitolo rd : imp.getSottoRighe()) {
					if(rd.isStanziamento()) {
						return rd.getImportoAnno0();
					}
				}
			}
			
		}
		return BigDecimal.ZERO;
	}
	
	private BigDecimal extractImportoResiduo() {
		for (RigaImportoTabellaImportiCapitolo imp : model.getRigheImportiTabellaImportiCapitolo()) {
			if(TipologiaImportoTabellaImportoCapitolo.RESIDUO.equals(imp.getTipoImportiCapitoloTabella())) {
				for (RigaDettaglioImportoTabellaImportiCapitolo rd : imp.getSottoRighe()) {
					if(rd.isPresunto()) {
						return rd.getImportoResiduoAnno0();
					}
				}
			}
			
		}
		return BigDecimal.ZERO;
	}

	
	private boolean isAggiornaSuComponenteDaInserire() {
		List<Integer> lista = model.extractListaUidComponenti();
		return lista == null || lista.isEmpty() || lista.size() != 3;
	}
	

	private void validateImportiComponenti() {
		
		checkCondition(model.getImportoStanziamentoAnno0() != null && model.getImportoStanziamentoAnno1() != null && model.getImportoStanziamentoAnno2() != null, ErroreCore.FORMATO_NON_VALIDO.getErrore("importo", "deve essere valorizzato su tutti e 3 gli anni"), true);
		
		checkCondition(model.getIsCapitoloFondino() || (BigDecimal.ZERO.compareTo(model.getImportoStanziamentoAnno0()) <=0 && BigDecimal.ZERO.compareTo(model.getImportoStanziamentoAnno1()) <=0  && BigDecimal.ZERO.compareTo(model.getImportoStanziamentoAnno2()) <=0),
				ErroreCore.FORMATO_NON_VALIDO.getErrore("importi", ", sono ammessi solamente importi positivi su tutti e 3 gli anni"));
	}


}
