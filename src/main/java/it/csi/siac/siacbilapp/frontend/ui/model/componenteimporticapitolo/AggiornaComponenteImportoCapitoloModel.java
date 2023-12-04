/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.componenteimporticapitolo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.CapitoloUscitaPrevisioneModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.AnnullaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaComponenteImportiCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitolo;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloUscitaPrevisione;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.DettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.wrapper.ImportiCapitoloPerComponente;

/**
 * The Class AggiornaComponenteImportoCapitoloModel.
 * @deprecated in base alla segnalazione SIAC-7799. Usare GestioneComponenteImportoCapitoloNelCapitoloModel.
 * Viene lasciata perche' viene ancora usata  nella pagina di aggiornamento (perche'?)
 */
@Deprecated
public class AggiornaComponenteImportoCapitoloModel extends CapitoloUscitaPrevisioneModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2653471479823874609L;
	
	public AggiornaComponenteImportoCapitoloModel() {
		super();
		setTitolo("Aggiornamento Stanziamenti");
	}
	
	private int uidCapitolo;
	private List<ImportiCapitolo> listaImportiCapitolo = new ArrayList<ImportiCapitolo>();
	private List<ImportiCapitoloPerComponente> importiComponentiCapitolo = new ArrayList<ImportiCapitoloPerComponente>();
	private List<ImportiCapitoloPerComponente> competenzaComponenti = new ArrayList<ImportiCapitoloPerComponente>();
	private List<ImportiCapitoloPerComponente> residuoComponenti = new ArrayList<ImportiCapitoloPerComponente>();
	private List<ImportiCapitoloPerComponente> cassaComponenti = new ArrayList<ImportiCapitoloPerComponente>();
	private List<TipoComponenteImportiCapitolo> listaTipoComponenti = new ArrayList<TipoComponenteImportiCapitolo>();
	private TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo = new TipoComponenteImportiCapitolo();
	//AGGIORNAMENTO INSERIEMNTO DATI
	private BigDecimal importoStanziamentoAnno0 = BigDecimal.ZERO;
	private BigDecimal importoStanziamentoAnno1 = BigDecimal.ZERO;
	private BigDecimal importoStanziamentoAnno2 = BigDecimal.ZERO;
	//RESIDUI E CASSA
	private BigDecimal importoResiduoPresunto;
	private BigDecimal importoCassaStanziamento;
	
	private int indexElemento;
	private int uidTipoComponenteImportiCapitolo;
	private String fromPage;
	//SIAC-6884 per componenti di default
	private boolean isCapitoloFondino;
	
	
	
	
	public boolean getIsCapitoloFondino() {
		return isCapitoloFondino;
	}

	public void setIsCapitoloFondino(boolean isCapitoloFondino) {
		this.isCapitoloFondino = isCapitoloFondino;
	}

	private Integer uidTipoComponenteCapitolo = 0;

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
	 * @return the uidTipoComponenteImportiCapitolo
	 */
	public int getUidTipoComponenteImportiCapitolo() {
		return uidTipoComponenteImportiCapitolo;
	}

	/**
	 * @param uidTipoComponenteImportiCapitolo the uidTipoComponenteImportiCapitolo to set
	 */
	public void setUidTipoComponenteImportiCapitolo(int uidTipoComponenteImportiCapitolo) {
		this.uidTipoComponenteImportiCapitolo = uidTipoComponenteImportiCapitolo;
	}

	/**
	 * @return the indexElemento
	 */
	public int getIndexElemento() {
		return indexElemento;
	}

	/**
	 * @param indexElemento the indexElemento to set
	 */
	public void setIndexElemento(int indexElemento) {
		this.indexElemento = indexElemento;
	}

	/**
	 * @return the importoResiduoPresunto
	 */
	public BigDecimal getImportoResiduoPresunto() {
		return importoResiduoPresunto;
	}

	/**
	 * @param importoResiduoPresunto the importoResiduoPresunto to set
	 */
	public void setImportoResiduoPresunto(BigDecimal importoResiduoPresunto) {
		this.importoResiduoPresunto = importoResiduoPresunto;
	}

	/**
	 * @return the importoCassaStanziamento
	 */
	public BigDecimal getImportoCassaStanziamento() {
		return importoCassaStanziamento;
	}

	/**
	 * @param importoCassaStanziamento the importoCassaStanziamento to set
	 */
	public void setImportoCassaStanziamento(BigDecimal importoCassaStanziamento) {
		this.importoCassaStanziamento = importoCassaStanziamento;
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

	
	
	/**
	 * @return the tipoComponenteImportiCapitolo
	 */
	public TipoComponenteImportiCapitolo getTipoComponenteImportiCapitolo() {
		return tipoComponenteImportiCapitolo;
	}

	/**
	 * @param tipoComponenteImportiCapitolo the tipoComponenteImportiCapitolo to set
	 */
	public void setTipoComponenteImportiCapitolo(TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo) {
		this.tipoComponenteImportiCapitolo = tipoComponenteImportiCapitolo;
	}

	/**
	 * @return the listaTipoComponenti
	 */
	public List<TipoComponenteImportiCapitolo> getListaTipoComponenti() {
		return listaTipoComponenti;
	}

	/**
	 * @param listaTipoComponenti the listaTipoComponenti to set
	 */
	public void setListaTipoComponenti(List<TipoComponenteImportiCapitolo> listaTipoComponenti) {
		this.listaTipoComponenti = listaTipoComponenti;
	}

	/**
	 * @return the competenzaComponenti
	 */
	public List<ImportiCapitoloPerComponente> getCompetenzaComponenti() {
		return competenzaComponenti;
	}

	/**
	 * @param competenzaComponenti the competenzaComponenti to set
	 */
	public void setCompetenzaComponenti(List<ImportiCapitoloPerComponente> competenzaComponenti) {
		this.competenzaComponenti = competenzaComponenti;
	}

	/**
	 * @return the residuoComponenti
	 */
	public List<ImportiCapitoloPerComponente> getResiduoComponenti() {
		return residuoComponenti;
	}

	/**
	 * @param residuoComponenti the residuoComponenti to set
	 */
	public void setResiduoComponenti(List<ImportiCapitoloPerComponente> residuoComponenti) {
		this.residuoComponenti = residuoComponenti;
	}

	/**
	 * @return the cassaComponenti
	 */
	public List<ImportiCapitoloPerComponente> getCassaComponenti() {
		return cassaComponenti;
	}

	/**
	 * @param cassaComponenti the cassaComponenti to set
	 */
	public void setCassaComponenti(List<ImportiCapitoloPerComponente> cassaComponenti) {
		this.cassaComponenti = cassaComponenti;
	}

	/**
	 * @return the importiComponentiCapitolo
	 */
	public List<ImportiCapitoloPerComponente> getImportiComponentiCapitolo() {
		return importiComponentiCapitolo;
	}

	/**
	 * @param importiComponentiCapitolo the importiComponentiCapitolo to set
	 */
	public void setImportiComponentiCapitolo(List<ImportiCapitoloPerComponente> importiComponentiCapitolo) {
		this.importiComponentiCapitolo = importiComponentiCapitolo;
	}

	/**
	 * @return the listaImportiCapitolo
	 */
	public List<ImportiCapitolo> getListaImportiCapitolo() {
		return listaImportiCapitolo;
	}

	/**
	 * @param listaImportiCapitolo the listaImportiCapitolo to set
	 */
	public void setListaImportiCapitolo(List<ImportiCapitolo> listaImportiCapitolo) {
		this.listaImportiCapitolo = listaImportiCapitolo;
	}

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
	
	/**
	 * 
	 * @return
	 */
	public AnnullaComponenteImportiCapitolo creaAnnullaComponenteImportiCapitolo() {
		AnnullaComponenteImportiCapitolo request = creaRequest(AnnullaComponenteImportiCapitolo.class);
		request.setCapitolo(new CapitoloUscitaPrevisione());
		request.getCapitolo().setUid(uidCapitolo);
		request.setTipoComponenteImportiCapitolo(tipoComponenteImportiCapitolo);
		return request;
	}
	
	/**
	 * 
	 * @return
	 */
	public AnnullaComponenteImportiCapitolo creaAnnullaComponenteImportiCapitolo(Integer uidTipoComponenteCapitolo) {
		AnnullaComponenteImportiCapitolo request = creaRequest(AnnullaComponenteImportiCapitolo.class);
		request.setCapitolo(new CapitoloUscitaPrevisione());
		request.getCapitolo().setUid(uidCapitolo);
		request.setTipoComponenteImportiCapitolo(tipoComponenteImportiCapitolo);
		request.getTipoComponenteImportiCapitolo().setUid(uidTipoComponenteCapitolo);
		return request;
	}
	
	/**
	 * 
	 * @return
	 */
	public AggiornaComponenteImportiCapitolo creaAggiornaComponenteImportiCapitolo() {
		AggiornaComponenteImportiCapitolo request = creaRequest(AggiornaComponenteImportiCapitolo.class);
		request.setCapitolo(new CapitoloUscitaPrevisione());
		request.getCapitolo().setUid(uidCapitolo);
		List<ComponenteImportiCapitolo> listaresultimportiCapitolo = new ArrayList<ComponenteImportiCapitolo>();
		if(importiComponentiCapitolo!= null && !importiComponentiCapitolo.isEmpty()){
			
			//OCCHIO ALL INDEX
			
			//ANNO 0
			ComponenteImportiCapitolo cic0 = new ComponenteImportiCapitolo();
			cic0.setUid(importiComponentiCapitolo.get(indexElemento).getDettaglioAnno0().getComponenteImportiCapitolo().getUid());
			List<DettaglioComponenteImportiCapitolo> listDett0 = new ArrayList<DettaglioComponenteImportiCapitolo>();
			importiComponentiCapitolo.get(indexElemento).getDettaglioAnno0().setImporto(importoStanziamentoAnno0);
			listDett0.add(importiComponentiCapitolo.get(indexElemento).getDettaglioAnno0());
			cic0.setListaDettaglioComponenteImportiCapitolo(listDett0);
			listaresultimportiCapitolo.add(cic0);
			//ANNO 1
			ComponenteImportiCapitolo cic1 = new ComponenteImportiCapitolo();
			cic1.setUid(importiComponentiCapitolo.get(indexElemento).getDettaglioAnno1().getComponenteImportiCapitolo().getUid());
			List<DettaglioComponenteImportiCapitolo> listDett1 = new ArrayList<DettaglioComponenteImportiCapitolo>();
			importiComponentiCapitolo.get(indexElemento).getDettaglioAnno1().setImporto(importoStanziamentoAnno1);
			listDett1.add(importiComponentiCapitolo.get(indexElemento).getDettaglioAnno1());
			cic1.setListaDettaglioComponenteImportiCapitolo(listDett1);
			listaresultimportiCapitolo.add(cic1);
			//ANNO 2
			ComponenteImportiCapitolo cic2 = new ComponenteImportiCapitolo();
			cic2.setUid(importiComponentiCapitolo.get(indexElemento).getDettaglioAnno2().getComponenteImportiCapitolo().getUid());
			List<DettaglioComponenteImportiCapitolo> listDett2 = new ArrayList<DettaglioComponenteImportiCapitolo>();
			importiComponentiCapitolo.get(indexElemento).getDettaglioAnno2().setImporto(importoStanziamentoAnno2);
			listDett2.add(importiComponentiCapitolo.get(indexElemento).getDettaglioAnno2());
			cic2.setListaDettaglioComponenteImportiCapitolo(listDett2);
			listaresultimportiCapitolo.add(cic2);
		}
		request.setListaComponenteImportiCapitolo(listaresultimportiCapitolo);
		return request;
	}
	
	
	
	public InserisceComponenteImportiCapitolo creaInserisceComponenteImportiCapitolo() {
		InserisceComponenteImportiCapitolo req = creaRequest(InserisceComponenteImportiCapitolo.class);
//		request.setCapitolo(new CapitoloUscitaPrevisione());
//		request.getCapitolo().setUid(uidCapitolo);
		
		TipoComponenteImportiCapitolo tipoComponenteCapitolo= new TipoComponenteImportiCapitolo(); 
		tipoComponenteCapitolo.setUid(uidTipoComponenteImportiCapitolo);
		req.setDataOra(new Date());
		req.setCapitolo(new CapitoloUscitaPrevisione());
		req.getCapitolo().setUid(uidCapitolo);
		
		//ANNO 0
		ComponenteImportiCapitolo cic = new ComponenteImportiCapitolo();
		cic.setImportiCapitolo(new ImportiCapitoloUP());
		cic.getImportiCapitolo().setAnnoCompetenza(getAnnoEsercizioInt());
		cic.setTipoComponenteImportiCapitolo(tipoComponenteCapitolo);
		
		DettaglioComponenteImportiCapitolo dcic = new DettaglioComponenteImportiCapitolo();
//		dcic = new DettaglioComponenteImportiCapitolo();
//		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
//		dcic.setImporto(new BigDecimal("250.01"));
//		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		dcic.setImporto(importoStanziamentoAnno0);
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		req.getListaComponenteImportiCapitolo().add(cic);
		
		// Anno1
		cic = new ComponenteImportiCapitolo();
		cic.setImportiCapitolo(new ImportiCapitoloUP());
		cic.getImportiCapitolo().setAnnoCompetenza(getAnnoEsercizioInt()+1);
		cic.setTipoComponenteImportiCapitolo(tipoComponenteCapitolo);
		
//		dcic = new DettaglioComponenteImportiCapitolo();
//		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
//		dcic.setImporto(new BigDecimal("250.01"));
//		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		dcic.setImporto(importoStanziamentoAnno1);
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		req.getListaComponenteImportiCapitolo().add(cic);

		// ANNO 2
		cic = new ComponenteImportiCapitolo();
		cic.setImportiCapitolo(new ImportiCapitoloUP());
		cic.getImportiCapitolo().setAnnoCompetenza(getAnnoEsercizioInt()+2);
		cic.setTipoComponenteImportiCapitolo(tipoComponenteCapitolo);
		
//		dcic = new DettaglioComponenteImportiCapitolo();
//		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
//		dcic.setImporto(new BigDecimal("250.01"));
//		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		dcic.setImporto(importoStanziamentoAnno2);
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		req.getListaComponenteImportiCapitolo().add(cic);
		
		return req;
	}
	
	public InserisceComponenteImportiCapitolo creaInserisceComponenteImportiCapitolo(Integer uidTipoComponenteCapitolo) {
		InserisceComponenteImportiCapitolo req = creaRequest(InserisceComponenteImportiCapitolo.class);
		
		TipoComponenteImportiCapitolo tipoComponenteCapitolo= new TipoComponenteImportiCapitolo(); 
		tipoComponenteCapitolo.setUid(uidTipoComponenteCapitolo);
		req.setDataOra(new Date());
		req.setCapitolo(new CapitoloUscitaPrevisione());
		req.getCapitolo().setUid(uidCapitolo);
		
		//ANNO 0
		ComponenteImportiCapitolo cic = new ComponenteImportiCapitolo();
		cic.setImportiCapitolo(new ImportiCapitoloUP());
		cic.getImportiCapitolo().setAnnoCompetenza(getAnnoEsercizioInt());
		cic.setTipoComponenteImportiCapitolo(tipoComponenteCapitolo);
		
		DettaglioComponenteImportiCapitolo dcic = new DettaglioComponenteImportiCapitolo();
		
		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		dcic.setImporto(importoStanziamentoAnno0);
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		req.getListaComponenteImportiCapitolo().add(cic);
		
		// Anno1
		cic = new ComponenteImportiCapitolo();
		cic.setImportiCapitolo(new ImportiCapitoloUP());
		cic.getImportiCapitolo().setAnnoCompetenza(getAnnoEsercizioInt()+1);
		cic.setTipoComponenteImportiCapitolo(tipoComponenteCapitolo);

		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		dcic.setImporto(importoStanziamentoAnno1);
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		req.getListaComponenteImportiCapitolo().add(cic);

		// ANNO 2
		cic = new ComponenteImportiCapitolo();
		cic.setImportiCapitolo(new ImportiCapitoloUP());
		cic.getImportiCapitolo().setAnnoCompetenza(getAnnoEsercizioInt()+2);
		cic.setTipoComponenteImportiCapitolo(tipoComponenteCapitolo);

		
		dcic = new DettaglioComponenteImportiCapitolo();
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		dcic.setImporto(importoStanziamentoAnno2);
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		
		req.getListaComponenteImportiCapitolo().add(cic);
		
		return req;
	}
	
	
	
	public AggiornaImportiCapitolo creaAggiornaResiduo() {
		AggiornaImportiCapitolo request = creaRequest(AggiornaImportiCapitolo.class);
		
		//AGGIORNAMENTO RESIDUO
		Capitolo<ImportiCapitolo, ?> capitolo = new Capitolo();
		capitolo.setUid(uidCapitolo);
		List<ImportiCapitolo> importiCapitoli = buildImportiResiduoCassa(importoResiduoPresunto, null);
		capitolo.setListaImportiCapitolo(importiCapitoli);
		request.setCapitolo(capitolo);
		return request;
	}
	
	
	public AggiornaImportiCapitolo creaAggiornaCassa() {
		AggiornaImportiCapitolo request = creaRequest(AggiornaImportiCapitolo.class);
		Capitolo capitolo = new Capitolo();
		capitolo.setUid(uidCapitolo);
		List<ImportiCapitolo> importiCapitoli = buildImportiResiduoCassa(null, importoCassaStanziamento);
		capitolo.setListaImportiCapitolo(importiCapitoli);
		request.setCapitolo(capitolo);
		return request;
	}
	
	
	
	
	private List<ImportiCapitolo> buildImportiResiduoCassa(BigDecimal importoResiduo, BigDecimal importoCassa){
		
		List<ImportiCapitolo> importiCapitoli = new ArrayList<ImportiCapitolo>();
		
		//ANNO PRECEDENTI
//		ImportiCapitolo ic5 = new ImportiCapitolo();
//		ic5.setAnnoCompetenza(getAnnoEsercizioInt()-1);
//		ic5.setStanziamentoResiduo(residuoComponenti.get(0).getDettaglioAnnoPrec().getImporto());//RESIDUI
//		ic5.setStanziamentoCassa(cassaComponenti.get(0).getDettaglioAnnoPrec().getImporto());
//		importiCapitoli.add(ic5);
		
		
		//ANNO 0		
		ImportiCapitolo ic0 = new ImportiCapitolo();
		ic0.setAnnoCompetenza(getAnnoEsercizioInt());
		
		ic0.setStanziamentoResiduo(importoResiduo == null ? residuoComponenti.get(0).getDettaglioAnno0().getImporto() : importoResiduo); //2020
		ic0.setStanziamentoCassa(importoCassa == null ? cassaComponenti.get(0).getDettaglioAnno0().getImporto() : importoCassa);
		importiCapitoli.add(ic0);
		
		//ANNO 1
		ImportiCapitolo ic1 = new ImportiCapitolo();
		ic1.setAnnoCompetenza(getAnnoEsercizioInt()+1);
		ic1.setStanziamentoResiduo(residuoComponenti.get(0).getDettaglioAnno1().getImporto()); //2021
		ic1.setStanziamentoCassa(cassaComponenti.get(0).getDettaglioAnno1().getImporto());
		importiCapitoli.add(ic1);
		
		//ANNO 2
		ImportiCapitolo ic2 = new ImportiCapitolo();
		ic2.setAnnoCompetenza(getAnnoEsercizioInt()+2);
		ic2.setStanziamentoResiduo(residuoComponenti.get(0).getDettaglioAnno2().getImporto()); //2022
		ic2.setStanziamentoCassa(cassaComponenti.get(0).getDettaglioAnno2().getImporto());
		importiCapitoli.add(ic2);
		
		//ANNO SUCCESSIVI
//		ImportiCapitolo ic3 = new ImportiCapitolo();
//		ic3.setStanziamentoResiduo(residuoComponenti.get(0).getDettaglioAnniSucc().getImporto()); //SUCCC
//		ic3.setStanziamentoCassa(cassaComponenti.get(0).getDettaglioAnniSucc().getImporto());
//		importiCapitoli.add(ic3);
		
		
		//ANNO RESIDUI
//		ImportiCapitolo ic4 = new ImportiCapitolo();
//		ic4.setStanziamentoResiduo(residuoComponenti.get(0).getDettaglioResidui().getImporto());//RESIDUI
//		ic4.setStanziamentoCassa(cassaComponenti.get(0).getDettaglioResidui().getImporto());
//		importiCapitoli.add(ic4);
		
		
		
		return importiCapitoli;
		
	}
	
	
}
