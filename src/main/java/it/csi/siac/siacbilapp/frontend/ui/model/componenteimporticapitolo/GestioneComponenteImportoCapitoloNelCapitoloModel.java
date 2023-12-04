/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.componenteimporticapitolo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaComponenteTabellaImportiCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaImportoTabellaImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioCapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitolo;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.DettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ric.RicercaDettaglioCapitoloUPrev;

public class GestioneComponenteImportoCapitoloNelCapitoloModel extends GenericBilancioModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2653471479823874609L;
	
	public GestioneComponenteImportoCapitoloNelCapitoloModel() {
		super();
		setTitolo("Aggiornamento Stanziamenti");
	}
	
	private int uidCapitolo;
	//SIAC-8003
	private CapitoloUscitaPrevisione capitolo;
	
	public CapitoloUscitaPrevisione getCapitolo() {
		return capitolo;
	}

	public void setCapitolo(CapitoloUscitaPrevisione capitolo) {
		this.capitolo = capitolo;
	}

	private String fromPage;
	//SIAC-6884 per componenti di default
	private boolean isCapitoloFondino;
	private Integer uidTipoComponenteCapitolo = 0;
	
	private List<RigaComponenteTabellaImportiCapitolo> righeComponentiTabellaImportiCapitolo = new ArrayList<RigaComponenteTabellaImportiCapitolo>();
	private List<RigaImportoTabellaImportiCapitolo> righeImportiTabellaImportiCapitolo = new ArrayList<RigaImportoTabellaImportiCapitolo>();
	
	private List<TipoComponenteImportiCapitolo> listaTipoComponentiDefault = new ArrayList<TipoComponenteImportiCapitolo>();
	private List<TipoComponenteImportiCapitolo> listaTipoComponentiPerNuovaComponente = new ArrayList<TipoComponenteImportiCapitolo>();

	
	//AGGIORNAMENTO INSERIEMNTO DATI
	private BigDecimal importoStanziamentoAnno0 = BigDecimal.ZERO;
	private BigDecimal importoStanziamentoAnno1 = BigDecimal.ZERO;
	private BigDecimal importoStanziamentoAnno2 = BigDecimal.ZERO;
	
	
	private BigDecimal importoStanziamentoModificato = BigDecimal.ZERO;
	
	
	
	
	
	public List<TipoComponenteImportiCapitolo> getListaTipoComponentiDefault() {
		return listaTipoComponentiDefault;
	}

	public void setListaTipoComponentiDefault(List<TipoComponenteImportiCapitolo> listaTipoComponentiDefault) {
		this.listaTipoComponentiDefault = listaTipoComponentiDefault;
	}

	public List<TipoComponenteImportiCapitolo> getListaTipoComponentiPerNuovaComponente() {
		return listaTipoComponentiPerNuovaComponente;
	}

	public void setListaTipoComponentiPerNuovaComponente(
			List<TipoComponenteImportiCapitolo> listaTipoComponentiPerNuovaComponente) {
		this.listaTipoComponentiPerNuovaComponente = listaTipoComponentiPerNuovaComponente != null? listaTipoComponentiPerNuovaComponente : new ArrayList<TipoComponenteImportiCapitolo>();
	}
	
	
	public List<RigaComponenteTabellaImportiCapitolo> getRigheComponentiTabellaImportiCapitolo() {
		return righeComponentiTabellaImportiCapitolo;
	}

	public void setRigheComponentiTabellaImportiCapitolo(
			List<RigaComponenteTabellaImportiCapitolo> righeComponentiTabellaImportiCapitolo) {
		this.righeComponentiTabellaImportiCapitolo = righeComponentiTabellaImportiCapitolo;
	}

	
	

	public List<RigaImportoTabellaImportiCapitolo> getRigheImportiTabellaImportiCapitolo() {
		return righeImportiTabellaImportiCapitolo;
	}

	public void setRigheImportiTabellaImportiCapitolo(
			List<RigaImportoTabellaImportiCapitolo> righeImportiTabellaImportiCapitolo) {
		this.righeImportiTabellaImportiCapitolo = righeImportiTabellaImportiCapitolo;
	}

	public boolean getIsCapitoloFondino() {
		return isCapitoloFondino;
	}

	public void setIsCapitoloFondino(boolean isCapitoloFondino) {
		this.isCapitoloFondino = isCapitoloFondino;
	}

	

	/* Tiene traccia dei componenti proposti non associati */
	private boolean propostaDiDefault;

	public boolean isPropostaDiDefault() {
		return propostaDiDefault;
	}

	public void setPropostaDiDefault(boolean isPropostaDiDefault) {
		this.propostaDiDefault = isPropostaDiDefault;
	}

	public Integer getUidTipoComponenteCapitolo() {
		return uidTipoComponenteCapitolo;
	}

	public void setUidTipoComponenteCapitolo(Integer uidTipoComponenteCapitolo) {
		this.uidTipoComponenteCapitolo = uidTipoComponenteCapitolo;
	}
	

	/**
	 * @return the fromPage
	 */
	public String getFromPage() {
		return fromPage;
	}

	/**
	 * @param fromPage the fromPage to set
	 */
	public void setFromPage(String fromPage) {
		this.fromPage = fromPage;
	}


	/**
	 * @return the importoStanziamentoAnno0
	 */
	public BigDecimal getImportoStanziamentoAnno0() {
		return importoStanziamentoAnno0;
	}

	/**
	 * @param importoStanziamentoAnno0 the importoStanziamentoAnno0 to set
	 */
	public void setImportoStanziamentoAnno0(BigDecimal importoStanziamentoAnno0) {
		this.importoStanziamentoAnno0 = importoStanziamentoAnno0;
	}

	/**
	 * @return the importoStanziamentoAnno1
	 */
	public BigDecimal getImportoStanziamentoAnno1() {
		return importoStanziamentoAnno1;
	}

	/**
	 * @param importoStanziamentoAnno1 the importoStanziamentoAnno1 to set
	 */
	public void setImportoStanziamentoAnno1(BigDecimal importoStanziamentoAnno1) {
		this.importoStanziamentoAnno1 = importoStanziamentoAnno1;
	}

	/**
	 * @return the importoStanziamentoAnno2
	 */
	public BigDecimal getImportoStanziamentoAnno2() {
		return importoStanziamentoAnno2;
	}

	/**
	 * @param importoStanziamentoAnno2 the importoStanziamentoAnno2 to set
	 */
	public void setImportoStanziamentoAnno2(BigDecimal importoStanziamentoAnno2) {
		this.importoStanziamentoAnno2 = importoStanziamentoAnno2;
	}

	public BigDecimal getImportoStanziamentoModificato() {
		return importoStanziamentoModificato;
	}

	public void setImportoStanziamentoModificato(BigDecimal importoStanziamentoModificato) {
		this.importoStanziamentoModificato = importoStanziamentoModificato;
	}

//	/**
//	 * @return the tipoComponenteImportiCapitolo
//	 */
//	public TipoComponenteImportiCapitolo getTipoComponenteImportiCapitolo() {
//		return tipoComponenteImportiCapitolo;
//	}
//
//	/**
//	 * @param tipoComponenteImportiCapitolo the tipoComponenteImportiCapitolo to set
//	 */
//	public void setTipoComponenteImportiCapitolo(TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo) {
//		this.tipoComponenteImportiCapitolo = tipoComponenteImportiCapitolo;
//	}
//
//	/**
//	 * @return the competenzaComponenti
//	 */
//	public List<ImportiCapitoloPerComponente> getCompetenzaComponenti() {
//		return competenzaComponenti;
//	}
//
//	/**
//	 * @param competenzaComponenti the competenzaComponenti to set
//	 */
//	public void setCompetenzaComponenti(List<ImportiCapitoloPerComponente> competenzaComponenti) {
//		this.competenzaComponenti = competenzaComponenti;
//	}

//	/**
//	 * @return the residuoComponenti
//	 */
//	public List<ImportiCapitoloPerComponente> getResiduoComponenti() {
//		return residuoComponenti;
//	}
//
//	/**
//	 * @param residuoComponenti the residuoComponenti to set
//	 */
//	public void setResiduoComponenti(List<ImportiCapitoloPerComponente> residuoComponenti) {
//		this.residuoComponenti = residuoComponenti;
//	}
//
//	/**
//	 * @return the cassaComponenti
//	 */
//	public List<ImportiCapitoloPerComponente> getCassaComponenti() {
//		return cassaComponenti;
//	}
//
//	/**
//	 * @param cassaComponenti the cassaComponenti to set
//	 */
//	public void setCassaComponenti(List<ImportiCapitoloPerComponente> cassaComponenti) {
//		this.cassaComponenti = cassaComponenti;
//	}
//
//	/**
//	 * @return the importiComponentiCapitolo
//	 */
//	public List<ImportiCapitoloPerComponente> getImportiComponentiCapitolo() {
//		return importiComponentiCapitolo;
//	}
//
//	/**
//	 * @param importiComponentiCapitolo the importiComponentiCapitolo to set
//	 */
//	public void setImportiComponentiCapitolo(List<ImportiCapitoloPerComponente> importiComponentiCapitolo) {
//		this.importiComponentiCapitolo = importiComponentiCapitolo;
//	}
//
//	/**
//	 * @return the listaImportiCapitolo
//	 */
//	public List<ImportiCapitolo> getListaImportiCapitolo() {
//		return listaImportiCapitolo;
//	}

//	/**
//	 * @param listaImportiCapitolo the listaImportiCapitolo to set
//	 */
//	public void setListaImportiCapitolo(List<ImportiCapitolo> listaImportiCapitolo) {
//		this.listaImportiCapitolo = listaImportiCapitolo;
//	}

	/**
	 * @return the uidCapitolo
	 */
	public int getUidCapitolo() {
		return uidCapitolo;
	}

	/**
	 * @param uidCapitolo the uidCapitolo to set
	 */
	public void setUidCapitolo(int uidCapitolo) {
		this.uidCapitolo = uidCapitolo;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public RicercaComponenteImportiCapitolo creaRequestRicercaComponenteImportiCapitolo() {
		RicercaComponenteImportiCapitolo request = creaRequest(RicercaComponenteImportiCapitolo.class);
		request.setCapitolo(new CapitoloUscitaPrevisione());
		request.getCapitolo().setUid(uidCapitolo);
		return request;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public RicercaTipoComponenteImportiCapitoloPerCapitolo creaRicercaTipoComponenteImportiCapitoloPerCapitolo() {
		RicercaTipoComponenteImportiCapitoloPerCapitolo request = creaRequest(RicercaTipoComponenteImportiCapitoloPerCapitolo.class);
		request.setCapitolo(new CapitoloUscitaPrevisione());
		request.getCapitolo().setUid(uidCapitolo);
		request.setSoloValidiPerBilancio(true);
		return request;
	}
	
	private ComponenteImportiCapitolo creaComponenteImportiCapitolo(Integer uidComponente, TipoComponenteImportiCapitolo tipo, Integer annoCompetenza, BigDecimal importo) {
		
		ComponenteImportiCapitolo cic = creaComponenteImportiCapitolo(tipo, annoCompetenza, importo);
		cic.setUid(uidComponente);
		return cic;
		
	}
	
	
	private ComponenteImportiCapitolo creaComponenteImportiCapitolo(TipoComponenteImportiCapitolo tipo, Integer annoCompetenza, BigDecimal importo) {
		
		ComponenteImportiCapitolo cic = new ComponenteImportiCapitolo();
		cic.setImportiCapitolo(new ImportiCapitoloUP());
		
		cic.setTipoComponenteImportiCapitolo(tipo);
		cic.getImportiCapitolo().setAnnoCompetenza(annoCompetenza);
		
		DettaglioComponenteImportiCapitolo dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		dcic.setImporto(importo);
		
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		return cic;
		
	}
	
	public AggiornaComponenteImportiCapitolo creaRequestAggiornaComponenteImportiCapitolo() {
		AggiornaComponenteImportiCapitolo request = creaRequest(AggiornaComponenteImportiCapitolo.class);
		request.setCapitolo(new CapitoloUscitaPrevisione());
		request.getCapitolo().setUid(uidCapitolo);
		
		TipoComponenteImportiCapitolo tcic = new TipoComponenteImportiCapitolo();
		tcic.setUid(getUidTipoComponenteCapitolo());

		
		List<Integer> extractListaUidComponenti = extractListaUidComponenti();
		List<ComponenteImportiCapitolo> lista = new ArrayList<ComponenteImportiCapitolo>();
		
		lista.add(creaComponenteImportiCapitolo(extractListaUidComponenti.get(0), tcic,Integer.valueOf(getAnnoEsercizioInt()), getImportoStanziamentoAnno0()));
		lista.add(creaComponenteImportiCapitolo(extractListaUidComponenti.get(1),tcic,Integer.valueOf(getAnnoEsercizioInt() + 1), getImportoStanziamentoAnno1()));
		lista.add(creaComponenteImportiCapitolo(extractListaUidComponenti.get(2),tcic,Integer.valueOf(getAnnoEsercizioInt() + 2), getImportoStanziamentoAnno2()));
		
		request.setListaComponenteImportiCapitolo(lista);
		
		return request;
	}
	
	public InserisceComponenteImportiCapitolo creaRequestInserisceComponenteImportiCapitolo() {
		InserisceComponenteImportiCapitolo request = creaRequest(InserisceComponenteImportiCapitolo.class);
		request.setCapitolo(new CapitoloUscitaPrevisione());
		request.getCapitolo().setUid(uidCapitolo);
		
		TipoComponenteImportiCapitolo tcic = new TipoComponenteImportiCapitolo();
		tcic.setUid(getUidTipoComponenteCapitolo());
		
		List<ComponenteImportiCapitolo> lista = new ArrayList<ComponenteImportiCapitolo>();
		
		lista.add(creaComponenteImportiCapitolo(tcic,Integer.valueOf(getAnnoEsercizioInt()), getImportoStanziamentoAnno0()));
		lista.add(creaComponenteImportiCapitolo(tcic,Integer.valueOf(getAnnoEsercizioInt() + 1), getImportoStanziamentoAnno1()));
		lista.add(creaComponenteImportiCapitolo(tcic,Integer.valueOf(getAnnoEsercizioInt() + 2), getImportoStanziamentoAnno2()));
		
		request.setListaComponenteImportiCapitolo(lista);
		return request;
	}
	
	public AnnullaComponenteImportiCapitolo creaRequestAnnullaComponenteImportiCapitolo() {
		AnnullaComponenteImportiCapitolo request = creaRequest(AnnullaComponenteImportiCapitolo.class);
		request.setCapitolo(new CapitoloUscitaPrevisione());
		request.getCapitolo().setUid(uidCapitolo);
		TipoComponenteImportiCapitolo tcic = new TipoComponenteImportiCapitolo();
		tcic.setUid(getUidTipoComponenteCapitolo());
		request.setTipoComponenteImportiCapitolo(tcic);
		return request;
	}

	
	
	public List<Integer> extractListaUidComponenti() {
		for (RigaComponenteTabellaImportiCapitolo rg : getRigheComponentiTabellaImportiCapitolo()) {
			if(rg.getUidTipoComponenteImportiCapitolo() != getUidTipoComponenteCapitolo() || !rg.hasUidComponentesuiTreAnni()) {
				continue;
			}
			List<Integer> uids = new ArrayList<Integer>();
			uids.add(rg.getUidComponenteAnno0());
			uids.add(rg.getUidComponenteAnno1());
			uids.add(rg.getUidComponenteAnno2());
			return uids;
			
		}
		return null;
	}
		
	
	/**
	 * Crea request aggiorna importi capitolo.
	 *
	 * @param importoResiduo the importo residuo
	 * @param importoCassa the importo cassa
	 * @return the aggiorna importi capitolo
	 */
	public AggiornaImportiCapitolo creaRequestAggiornaImportiCapitolo(BigDecimal importoResiduo, BigDecimal importoCassa) {
		AggiornaImportiCapitolo request = creaRequest(AggiornaImportiCapitolo.class);
		
		//AGGIORNAMENTO RESIDUO
		Capitolo<ImportiCapitoloUP, ImportiCapitoloUG> capitolo = new CapitoloUscitaPrevisione();
		capitolo.setUid(uidCapitolo);
		List<ImportiCapitoloUP> importiCapitolo = new ArrayList<ImportiCapitoloUP>();
		
		ImportiCapitoloUP importo = new ImportiCapitoloUP();//buildImportiResiduoCassa(importoResiduoPresunto, null);
		importo.setAnnoCompetenza(getAnnoEsercizioInt());
		importo.setStanziamentoResiduo(importoResiduo);
		importo.setStanziamentoCassa(importoCassa);
		importiCapitolo.add(importo);
	
		capitolo.setListaImportiCapitolo(importiCapitolo);
		request.setCapitolo(capitolo);
		return request;
	}
	
	//SIAC-8003
	public RicercaDettaglioCapitoloUscitaPrevisione creaRequestRicercaDettaglioCapitoloUscitaPrevisione() {
		RicercaDettaglioCapitoloUscitaPrevisione request = creaRequest(RicercaDettaglioCapitoloUscitaPrevisione.class);
		
		request.setEnte(getEnte());

		RicercaDettaglioCapitoloUPrev ricercaDettaglioCapitoloUPrev = new RicercaDettaglioCapitoloUPrev();
		ricercaDettaglioCapitoloUPrev.setChiaveCapitolo(uidCapitolo);

		request.setRicercaDettaglioCapitoloUPrev(ricercaDettaglioCapitoloUPrev);
		
		return request;
	}
	
}
