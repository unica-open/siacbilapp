/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitoloResponse;
import it.csi.siac.siacbilser.model.CategoriaCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.wrapper.ImportiImpegnatoPerComponenteAnniSuccNoStanz;
import it.csi.siac.siacbilser.model.wrapper.ImportiImpegnatoPerComponenteTriennioNoStanz;


/**
 * The Class TabellaImportiConComponentiCapitoloFactory.
 * 
 * @author elisa
 * 
 * @version 1.0.0
 */
public abstract class BaseTabellaImportiConComponentiCapitoloFactory implements Serializable {
	
	/**Per la serializzazione */
	private static final long serialVersionUID = -868302060667409479L;
	
	
	protected static final List<PeriodoTabellaComponentiImportiCapitolo> PERIODI_COMPETENZA_STANZIAMENTO_INIZIALE_SENZA_SIGNIFICATO_CONTABILE 
		//SIAC-8469
		= Arrays.asList(PeriodoTabellaComponentiImportiCapitolo.RESIDUO_ANNO_BILANCIO, PeriodoTabellaComponentiImportiCapitolo.ANNI_SUCCESSIVI);
	protected static final List<PeriodoTabellaComponentiImportiCapitolo> PERIODI_COMPETENZA_STANZIAMENTO_FINALE_SENZA_SIGNIFICATO_CONTABILE 
		//SIAC-8469	
	= Arrays.asList(PeriodoTabellaComponentiImportiCapitolo.RESIDUO_ANNO_BILANCIO,PeriodoTabellaComponentiImportiCapitolo.ANNI_SUCCESSIVI);	
	
	
	protected static final List<PeriodoTabellaComponentiImportiCapitolo> PERIODI_COMPETENZA_IMPEGNATO_INIZIALE_SENZA_SIGNIFICATO_CONTABILE 
		= Arrays.asList(PeriodoTabellaComponentiImportiCapitolo.values());
	protected static final List<PeriodoTabellaComponentiImportiCapitolo> PERIODI_COMPETENZA_IMPEGNATO_FINALE_UG_SENZA_SIGNIFICATO_CONTABILE 
		//solo per il capitolo UG
		= Arrays.asList(PeriodoTabellaComponentiImportiCapitolo.RESIDUO_ANNO_BILANCIO);	
	protected static final List<PeriodoTabellaComponentiImportiCapitolo> PERIODI_COMPETENZA_IMPEGNATO_FINALE_UP_SENZA_SIGNIFICATO_CONTABILE 
		= new ArrayList<PeriodoTabellaComponentiImportiCapitolo>();
	
	protected static final List<PeriodoTabellaComponentiImportiCapitolo> PERIODI_COMPETENZA_DISP_IMPEGNARE_INIZIALE_SENZA_SIGNIFICATO_CONTABILE 
		= Arrays.asList(PeriodoTabellaComponentiImportiCapitolo.values());	
	protected static final List<PeriodoTabellaComponentiImportiCapitolo> PERIODI_COMPETENZA_DISP_IMPEGNARE_FINALE_SENZA_SIGNIFICATO_CONTABILE 
		= Arrays.asList(PeriodoTabellaComponentiImportiCapitolo.ANNI_PRECEDENTI, PeriodoTabellaComponentiImportiCapitolo.RESIDUO_ANNO_BILANCIO,
						PeriodoTabellaComponentiImportiCapitolo.ANNI_SUCCESSIVI);
	
	protected static final List<PeriodoTabellaComponentiImportiCapitolo> PERIODI_RESIDUO_PRESUNTO_INIZIALE_SENZA_SIGNIFICATO_CONTABILE 
		= Arrays.asList(PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO, PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_UNO, 
						PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_DUE, PeriodoTabellaComponentiImportiCapitolo.ANNI_SUCCESSIVI);
	
	protected static final List<PeriodoTabellaComponentiImportiCapitolo> PERIODI_RESIDUO_PRESUNTO_FINALE_SENZA_SIGNIFICATO_CONTABILE 
		= Arrays.asList(PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO, PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_UNO, 
						PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_DUE, PeriodoTabellaComponentiImportiCapitolo.ANNI_SUCCESSIVI);
	
	
	//SIAC-8469
	protected static final List<PeriodoTabellaComponentiImportiCapitolo> PERIODI_RESIDUO_EFFETTIVO_INIZIALE_UP_SENZA_SIGNIFICATO_CONTABILE 
		= Arrays.asList(PeriodoTabellaComponentiImportiCapitolo.RESIDUO_ANNO_BILANCIO, PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO, PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_UNO, 
					PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_DUE, PeriodoTabellaComponentiImportiCapitolo.ANNI_SUCCESSIVI);
	protected static final List<PeriodoTabellaComponentiImportiCapitolo> PERIODI_RESIDUO_EFFETTIVO_INIZIALE_UG_SENZA_SIGNIFICATO_CONTABILE 
		= Arrays.asList(PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO, PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_UNO, 
				PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_DUE, PeriodoTabellaComponentiImportiCapitolo.ANNI_SUCCESSIVI);
	
	protected static final List<PeriodoTabellaComponentiImportiCapitolo> PERIODI_RESIDUO_EFFETTIVO_FINALE_SENZA_SIGNIFICATO_CONTABILE 
		= Arrays.asList(PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO, PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_UNO, 
						PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_DUE, PeriodoTabellaComponentiImportiCapitolo.ANNI_SUCCESSIVI);
	
	
	protected static final List<PeriodoTabellaComponentiImportiCapitolo> PERIODI_CASSA_STANZIAMENTO_INIZIALE_SENZA_SIGNIFICATO_CONTABILE 
	//SIAC-8469
		= Arrays.asList(PeriodoTabellaComponentiImportiCapitolo.RESIDUO_ANNO_BILANCIO,PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_UNO, PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_DUE,
						PeriodoTabellaComponentiImportiCapitolo.ANNI_SUCCESSIVI);
	protected static final List<PeriodoTabellaComponentiImportiCapitolo> PERIODI_CASSA_STANZIAMENTO_FINALE_SENZA_SIGNIFICATO_CONTABILE 
		//SIAC-8469
		= Arrays.asList(PeriodoTabellaComponentiImportiCapitolo.RESIDUO_ANNO_BILANCIO,PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_UNO,PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_DUE, 
						PeriodoTabellaComponentiImportiCapitolo.ANNI_SUCCESSIVI);
	
	
	protected static final List<PeriodoTabellaComponentiImportiCapitolo> PERIODI_CASSA_PAGATO_FINALE_UG_SENZA_SIGNIFICATO_CONTABILE
		//SIAC-8570
		= Arrays.asList(PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_UNO, PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_DUE,
						PeriodoTabellaComponentiImportiCapitolo.ANNI_SUCCESSIVI);
	protected static final List<PeriodoTabellaComponentiImportiCapitolo> PERIODI_CASSA_PAGATO_FINALE_UP_SENZA_SIGNIFICATO_CONTABILE
	//SIAC-8570 e SIAC-8469
	= Arrays.asList(PeriodoTabellaComponentiImportiCapitolo.RESIDUO_ANNO_BILANCIO,PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO, PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_UNO, PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_DUE,
					PeriodoTabellaComponentiImportiCapitolo.ANNI_SUCCESSIVI);
	protected static final List<PeriodoTabellaComponentiImportiCapitolo> PERIODI_CASSA_PAGATO_INIZIALE_SENZA_SIGNIFICATO_CONTABILE 
		//SIAC-8464
		= Arrays.asList(PeriodoTabellaComponentiImportiCapitolo.values());

	

	//campi di appoggio
	protected Integer annoBilancio =null;
	
	protected TipoCapitolo tipoCapitolo;
	
	protected CategoriaCapitolo categoriaCapitolo;
	
	/*
	 * IMPORTI DEL CAPITOLO EQUIVALENTE
	 * */
	//importi per anno N -1 del capitolo collegato attraverso siac_r_bil_elem_rel_tempo, oppure se il cpaitolo in ogggetto e' previsione, il capitolo di gestione equivalente di annoBilancio-1  
	//(elem_code,elem_code2,elem_code3 uguali a quello del capitolo di previsione )
	protected ImportiCapitolo importiCapitoloAnnoMenoUno;
	
	/*
	 * IMPORTI DEL CAPITOLO IN CONSULTAZIONE/AGGIORNAMENTO
	 * */
	// importi capitolo anno1 (anno = anno bilancio)
	protected ImportiCapitolo importiCapitoloAnnoBilancio;
	// importi capitolo anno2 (anno = anno bilancio +1)
	protected ImportiCapitolo importiCapitoloAnnoBilancioPiuUno;
	// importi capitolo anno3 (anno = anno bilancio +1)
	protected ImportiCapitolo importiCapitoloAnnoBilancioPiuDue;
	
	//oggetto che serve solo ad avere la lista delle componenti presenti sul del capitolo collegato attraverso siac_r_bil_elem_rel_tempo, oppure il capitolo di gestione di annoBilancio-1 equivalente 
	//con (elem_code,elem_code2,elem_code3 uguali a quello del capitolo di previsione ) per ottenere da questi due valori:
	//impegnatoAnniPrecedenti impegnato definitivo per annualita' 5 (verificare)
	//impegnatoResiduoFinale impegnato definitivo per annualita = 6 (verificare)
	protected ImportiCapitolo importiCapitoloResiduo;
	protected ImportiCapitolo importiCapitoloAnniSuccessivi;
	
	
	
	//componenti del capitolo
//	protected List<ComponenteImportiCapitolo> listaComponentiAnnoMenoUno;
	/*
	 * COMPONENTI SUL CAPITOLO 
	 * */	
//	//componenti che non hanno stanziamento ma hanno impegnato nel triennio
	protected ComponentiInTabellaFactory componentiFactory = new ComponentiInTabellaFactory();
	
	
	
	
	
	//creo una mappa tipo capitolo . proposta per definire quali siano le componenti di default

	
	

	public void init(Integer annoBilancio, TipoCapitolo tipoCapitolo, RicercaComponenteImportiCapitoloResponse response) {

		//imposto i dati 
		this.annoBilancio = annoBilancio;
		this.tipoCapitolo = tipoCapitolo;
		this.componentiFactory.init(annoBilancio, tipoCapitolo);
		initImporti(annoBilancio, response.getImportiCapitoloResiduo(), response.getListaImportiCapitolo(), 
				response.getImportiCapitoloAnniSuccessivi(),response.getListaImportiCapitoloTriennioNoStanz(), response.getListaImportiCapitoloAnniSuccessiviNoStanz());
		
	}

	
	public void init(Integer annoBilancio, TipoCapitolo tipoCapitolo, RicercaComponenteImportiCapitoloResponse response, RicercaTipoComponenteImportiCapitoloPerCapitoloResponse responseTipoComponente) {

		//imposto i dati 
		this.annoBilancio = annoBilancio;
		this.tipoCapitolo = tipoCapitolo;
		this.componentiFactory.init(annoBilancio,tipoCapitolo,responseTipoComponente.getListaTipoComponenteImportiCapitolo());
		
		initImporti(annoBilancio, response.getImportiCapitoloResiduo(), response.getListaImportiCapitolo(), 
				response.getImportiCapitoloAnniSuccessivi(),response.getListaImportiCapitoloTriennioNoStanz(), response.getListaImportiCapitoloAnniSuccessiviNoStanz());
		
	}
	
		
	public void init(Integer annoBilancio, TipoCapitolo tipoCapitolo, InserisceComponenteImportiCapitoloResponse response, List<TipoComponenteImportiCapitolo> listaTipiComponenteDefault) {

		this.annoBilancio = annoBilancio;
		this.tipoCapitolo = tipoCapitolo;
		//imposto i dati 
		this.componentiFactory.init(annoBilancio,tipoCapitolo, listaTipiComponenteDefault);

		initImporti(annoBilancio, response.getImportiCapitoloResiduo(), response.getListaImportiCapitolo(), 
					response.getImportiCapitoloAnniSuccessivi(),response.getListaImportiCapitoloTriennioNoStanz(), response.getListaImportiCapitoloAnniSuccessiviNoStanz());
			
	}
	
	public void init(Integer annoBilancio, TipoCapitolo tipoCapitolo, AggiornaComponenteImportiCapitoloResponse response, List<TipoComponenteImportiCapitolo> listaTipiComponenteDefault) {

		//imposto i dati 
		this.annoBilancio = annoBilancio;
		this.tipoCapitolo = tipoCapitolo;
		this.componentiFactory.init(annoBilancio,tipoCapitolo, listaTipiComponenteDefault);
		initImporti(annoBilancio, response.getImportiCapitoloResiduo(), response.getListaImportiCapitolo(), 
				response.getImportiCapitoloAnniSuccessivi(),response.getListaImportiCapitoloTriennioNoStanz(), response.getListaImportiCapitoloAnniSuccessiviNoStanz());
		
	}
	
	public void init(Integer annoBilancio, TipoCapitolo tipoCapitolo, AnnullaComponenteImportiCapitoloResponse response, List<TipoComponenteImportiCapitolo> listaTipiComponenteDefault) {

		//imposto i dati 
		this.annoBilancio = annoBilancio;
		this.tipoCapitolo = tipoCapitolo;
		
		this.componentiFactory.init(annoBilancio,tipoCapitolo, listaTipiComponenteDefault);
		initImporti(annoBilancio, response.getImportiCapitoloResiduo(), response.getListaImportiCapitolo(), 
				response.getImportiCapitoloAnniSuccessivi(),response.getListaImportiCapitoloTriennioNoStanz(), response.getListaImportiCapitoloAnniSuccessiviNoStanz());
		
	}
	

	
	
	public void init(Integer annoBilancio, TipoCapitolo tipoCapitolo, AggiornaImportiCapitoloResponse response, List<TipoComponenteImportiCapitolo> listaTipiComponenteDefault) {

		//imposto i dati 
		this.annoBilancio = annoBilancio;
		this.tipoCapitolo = tipoCapitolo;
		this.componentiFactory.init(annoBilancio, tipoCapitolo, listaTipiComponenteDefault);
		initImporti(annoBilancio, response.getImportiCapitoloResiduo(), response.getListaImportiCapitolo(), 
				response.getImportiCapitoloAnniSuccessivi(),response.getListaImportiCapitoloTriennioNoStanz(), response.getListaImportiCapitoloAnniSuccessiviNoStanz());
		
	}
	
	public void init(Integer annoBilancio, TipoCapitolo tipoCapitolo, RicercaComponenteImportiCapitoloResponse response, List<TipoComponenteImportiCapitolo> listaTipiComponenteDefault) {

		//imposto i dati 
		this.annoBilancio = annoBilancio;
		this.tipoCapitolo = tipoCapitolo;
		this.componentiFactory.init(annoBilancio,tipoCapitolo, listaTipiComponenteDefault);
		initImporti(annoBilancio, response.getImportiCapitoloResiduo(), response.getListaImportiCapitolo(), 
				response.getImportiCapitoloAnniSuccessivi(),response.getListaImportiCapitoloTriennioNoStanz(), response.getListaImportiCapitoloAnniSuccessiviNoStanz());
		
	}
	
	//task-236
	public void init(Integer annoBilancio, CategoriaCapitolo categoriaCapitolo, TipoCapitolo tipoCapitolo, InserisceComponenteImportiCapitoloResponse response, List<TipoComponenteImportiCapitolo> listaTipiComponenteDefault) {
		this.annoBilancio = annoBilancio;
		this.tipoCapitolo = tipoCapitolo;
		this.categoriaCapitolo = categoriaCapitolo;
		//imposto i dati 
		this.componentiFactory.init(annoBilancio,tipoCapitolo, listaTipiComponenteDefault);

		initImporti(annoBilancio, response.getImportiCapitoloResiduo(), response.getListaImportiCapitolo(), 
					response.getImportiCapitoloAnniSuccessivi(),response.getListaImportiCapitoloTriennioNoStanz(), response.getListaImportiCapitoloAnniSuccessiviNoStanz());
			
	}
	
	//task-236
	public void init(Integer annoBilancio, CategoriaCapitolo categoriaCapitolo, TipoCapitolo tipoCapitolo, AggiornaComponenteImportiCapitoloResponse response, List<TipoComponenteImportiCapitolo> listaTipiComponenteDefault) {
		//imposto i dati 
		this.annoBilancio = annoBilancio;
		this.tipoCapitolo = tipoCapitolo;
		this.categoriaCapitolo = categoriaCapitolo;
		this.componentiFactory.init(annoBilancio,tipoCapitolo, listaTipiComponenteDefault);
		initImporti(annoBilancio, response.getImportiCapitoloResiduo(), response.getListaImportiCapitolo(), 
				response.getImportiCapitoloAnniSuccessivi(),response.getListaImportiCapitoloTriennioNoStanz(), response.getListaImportiCapitoloAnniSuccessiviNoStanz());
		
	}
	
	//task-236
	public void init(Integer annoBilancio, CategoriaCapitolo categoriaCapitolo, TipoCapitolo tipoCapitolo, AnnullaComponenteImportiCapitoloResponse response, List<TipoComponenteImportiCapitolo> listaTipiComponenteDefault) {
		//imposto i dati 
		this.annoBilancio = annoBilancio;
		this.tipoCapitolo = tipoCapitolo;
		this.categoriaCapitolo = categoriaCapitolo;
		this.componentiFactory.init(annoBilancio,tipoCapitolo, listaTipiComponenteDefault);
		initImporti(annoBilancio, response.getImportiCapitoloResiduo(), response.getListaImportiCapitolo(), 
				response.getImportiCapitoloAnniSuccessivi(),response.getListaImportiCapitoloTriennioNoStanz(), response.getListaImportiCapitoloAnniSuccessiviNoStanz());
		
	}
	
	//task-236
	public void init(Integer annoBilancio, CategoriaCapitolo categoriaCapitolo, TipoCapitolo tipoCapitolo, AggiornaImportiCapitoloResponse response, List<TipoComponenteImportiCapitolo> listaTipiComponenteDefault) {

		//imposto i dati 
		this.annoBilancio = annoBilancio;
		this.tipoCapitolo = tipoCapitolo;
		this.categoriaCapitolo = categoriaCapitolo;
		this.componentiFactory.init(annoBilancio, tipoCapitolo, listaTipiComponenteDefault);
		initImporti(annoBilancio, response.getImportiCapitoloResiduo(), response.getListaImportiCapitolo(), 
				response.getImportiCapitoloAnniSuccessivi(),response.getListaImportiCapitoloTriennioNoStanz(), response.getListaImportiCapitoloAnniSuccessiviNoStanz());
		
	}
	
	//task-236
	public void init(Integer annoBilancio, CategoriaCapitolo categoriaCapitolo, TipoCapitolo tipoCapitolo, RicercaComponenteImportiCapitoloResponse response, List<TipoComponenteImportiCapitolo> listaTipiComponenteDefault) {

		//imposto i dati 
		this.annoBilancio = annoBilancio;
		this.tipoCapitolo = tipoCapitolo;
		this.categoriaCapitolo = categoriaCapitolo;
		this.componentiFactory.init(annoBilancio,tipoCapitolo, listaTipiComponenteDefault);
		initImporti(annoBilancio, response.getImportiCapitoloResiduo(), response.getListaImportiCapitolo(), 
				response.getImportiCapitoloAnniSuccessivi(),response.getListaImportiCapitoloTriennioNoStanz(), response.getListaImportiCapitoloAnniSuccessiviNoStanz());
		
	}
	
	private void initImporti(Integer annoBilancio, ImportiCapitolo importiCapitoloResiduo,
			List<ImportiCapitolo> listaImportiCapitolo,	ImportiCapitolo importiCapitoloAnniSuccessivi, 
			List<ImportiImpegnatoPerComponenteTriennioNoStanz> importiCapitoloTriennioNoStanziamento, List<ImportiImpegnatoPerComponenteAnniSuccNoStanz> listaImportiCapitoloAnniSuccessiviNoStanz) {
		
		this.importiCapitoloResiduo = importiCapitoloResiduo;
		this.importiCapitoloAnniSuccessivi = importiCapitoloAnniSuccessivi;
		//questi due oggetti java hanno gli stessi campi e nessuna separazione logica, non ha senso che siano diversi!!!
		
		for (ImportiCapitolo ic : listaImportiCapitolo) {
			if((annoBilancio.intValue() -1 ) == ic.getAnnoCompetenza()) {
				this.importiCapitoloAnnoMenoUno = ic;
				continue;
			}
			if((annoBilancio.intValue()) == ic.getAnnoCompetenza()) {
				this.importiCapitoloAnnoBilancio = ic;
				continue;
			}
			if((annoBilancio.intValue() +1) == ic.getAnnoCompetenza()) {
				this.importiCapitoloAnnoBilancioPiuUno = ic;
				continue;
			}
			if((annoBilancio.intValue() +2 ) == ic.getAnnoCompetenza()) {
				this.importiCapitoloAnnoBilancioPiuDue = ic;
			}
			
		}
		
		componentiFactory.initImporti(this.annoBilancio, importiCapitoloAnnoMenoUno, importiCapitoloAnnoBilancio, 
				importiCapitoloAnnoBilancioPiuUno, importiCapitoloAnnoBilancioPiuDue,importiCapitoloAnniSuccessivi,  importiCapitoloTriennioNoStanziamento, listaImportiCapitoloAnniSuccessiviNoStanz);
	}
	
	
	public List<Integer> getUidTipoComponentiAssociateAlCapitolo(){
		return componentiFactory.getUidTipoComponentiAssociateAlCapitolo();
	}
	
	public List<TipoComponenteImportiCapitolo> filtraListaImportoComponente(List<TipoComponenteImportiCapitolo> toFilter){
		return componentiFactory.filtraListaImportoComponente(toFilter);
	}

	
	public abstract void elaboraRigheTabella();
	
	public abstract void elaboraRigheConImportoIniziale();
	
	public List<RigaComponenteTabellaImportiCapitolo> getRigheDisponibilitaImpegnareComponenti() {
		return componentiFactory.getRigheDisponibilitaImpegnareComponenti();
	}

	//SIAC-8884
	public List<RigaComponenteTabellaImportiCapitolo> getRigheDisponibilitaVariareComponenti() {
		return componentiFactory.getRigheDisponibilitaVariareComponenti();
	}
	
}

