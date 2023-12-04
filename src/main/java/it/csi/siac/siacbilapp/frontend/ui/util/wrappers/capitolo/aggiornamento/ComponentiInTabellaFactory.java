/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.DettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.wrapper.ImportiImpegnatoPerComponenteAnniSuccNoStanz;
import it.csi.siac.siacbilser.model.wrapper.ImportiImpegnatoPerComponenteTriennioNoStanz;


/**
 * The Class TabellaImportiConComponentiCapitoloFactory.
 * 
 * @author elisa
 * 
 * @version 1.0.0
 */
public class ComponentiInTabellaFactory implements Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2905825925884214007L;
	//campi di appoggio
	private List<TipoComponenteImportiCapitolo> listaTipiComponente = new ArrayList<TipoComponenteImportiCapitolo>();
	private List<Integer> uidTipoComponentiAssociateAlCapitolo = new ArrayList<Integer>();
	private List<Integer> uidTipoComponentiAssociateAlCapitoloPersoloImpegnato = new ArrayList<Integer>();
	private Integer annoBilancio =null;
	
	private List<RigaComponenteTabellaImportiCapitolo> righeComponentiElaborate = new ArrayList<RigaComponenteTabellaImportiCapitolo>();
	private List<RigaComponenteTabellaImportiCapitolo> righeDisponibilitaImpegnare = new ArrayList<RigaComponenteTabellaImportiCapitolo>();
	private List<RigaComponenteTabellaImportiCapitolo> righeDisponibilitaVariare = new ArrayList<RigaComponenteTabellaImportiCapitolo>();
	
	//componenti del capitolo
	private List<ComponenteImportiCapitolo> listaComponentiAnnoMenoUno;
	/*
	 * COMPONENTI SUL CAPITOLO 
	 * */	
	private List<ComponenteImportiCapitolo> listaComponentiAnnoBilancio;
	private List<ComponenteImportiCapitolo> listaComponentiAnnoBilancioPiuUno;
	private List<ComponenteImportiCapitolo> listaComponentiAnnoBilancioPiuDue;
//	private List<ComponenteImportiCapitolo> listaImportiCapitoloResiduo;
	private List<ComponenteImportiCapitolo> listaImportiCapitoloSuccessivi;
	//componenti che non hanno stanziamento ma hanno impegnato nel triennio
	private List<ImportiImpegnatoPerComponenteTriennioNoStanz> listaImportiCapitoloTriennioNoStanz;
	private List<ImportiImpegnatoPerComponenteAnniSuccNoStanz> listaImportiCapitoloAnniSuccessiviNoStanz;
	
	private Map<Integer, List<WrapperComponenteImportoCapitoloAnno>> mappaTipoComponenteComponentiPerAnno;
	private Map<Integer, RigaComponenteTabellaImportiCapitolo> mappaRigheComponentiPresentiSoloSuAnnoMenoUno = new HashMap<Integer, RigaComponenteTabellaImportiCapitolo>();
	private Map<Integer, RigaDettaglioComponenteTabellaImportiCapitolo> mappaDettaglioImpegnatoSenzaStanziato = new HashMap<Integer, RigaDettaglioComponenteTabellaImportiCapitolo>();
	private Map<Integer, String> mappaidTipoCompDescrizionePerImpegnatoSenzaStanziato = new HashMap<Integer, String>();
	
	private boolean esistonoComponentiNonFrescoSulCapitolo;
	
	private TipoCapitolo tipoCapitolo;
	
	public ComponentiInTabellaFactory(){
		
	}
	
	
//	
//	public void init(List<ComponenteImportiCapitolo> listaComponentiAnnoMenoUno,List<ComponenteImportiCapitolo> listaComponentiAnnoBilancio,
//			List<ComponenteImportiCapitolo> listaComponentiAnnoBilancioPiuUno,List<ComponenteImportiCapitolo> listaComponentiAnnoBilancioPiuDue,
//			List<ImportiImpegnatoPerComponenteTriennioNoStanz> listaImportiCapitoloTriennioNoStanz
//			,List<ImportiImpegnatoPerComponenteAnniSuccNoStanz> listaImportiCapitoloAnniSuccessiviNoStanz ) {
	

	public boolean isEsistonoComponentiNonFrescoSulCapitolo() {
		return esistonoComponentiNonFrescoSulCapitolo;
	}


	public List<RigaComponenteTabellaImportiCapitolo> getRigheComponentiElaborate() {
		return righeComponentiElaborate;
	}
	
	public void init(Integer annoBilancio, TipoCapitolo tipoCapitolo) {
		this.annoBilancio = annoBilancio;
		this.tipoCapitolo = tipoCapitolo;
	}
	
	public void init(Integer annoBilancio, TipoCapitolo tipoCapitolo, List<TipoComponenteImportiCapitolo> listaTipiComponente) {
		init(annoBilancio, tipoCapitolo);
		this.listaTipiComponente = listaTipiComponente;
	}


	public void initImporti(Integer annoBilancio, ImportiCapitolo importiCapitoloAnnoMenoUno, ImportiCapitolo importiCapitoloAnnoBilancio, ImportiCapitolo importiCapitoloAnnoBilancioPiuUno,ImportiCapitolo importiCapitoloAnnoBilancioPiuDue, 
			ImportiCapitolo importiCapitoloAnniSuccessivi,List<ImportiImpegnatoPerComponenteTriennioNoStanz> importiCapitoloTriennioNoStanziamento, List<ImportiImpegnatoPerComponenteAnniSuccNoStanz> listaImportiCapitoloAnniSuccessiviNoStanz
			) {
		this.annoBilancio = annoBilancio;
		this.listaComponentiAnnoMenoUno = importiCapitoloAnnoMenoUno != null? 
				importiCapitoloAnnoMenoUno.getListaComponenteImportiCapitolo() : new ArrayList<ComponenteImportiCapitolo>();
		
		this.listaComponentiAnnoBilancio =  importiCapitoloAnnoBilancio != null? 
				importiCapitoloAnnoBilancio.getListaComponenteImportiCapitolo() : new ArrayList<ComponenteImportiCapitolo>();
		
		this.listaComponentiAnnoBilancioPiuUno =  importiCapitoloAnnoBilancioPiuUno != null? 
				importiCapitoloAnnoBilancioPiuUno.getListaComponenteImportiCapitolo() : new ArrayList<ComponenteImportiCapitolo>();
		
		this.listaComponentiAnnoBilancioPiuDue =  importiCapitoloAnnoBilancioPiuDue != null? 
				importiCapitoloAnnoBilancioPiuDue.getListaComponenteImportiCapitolo() : new ArrayList<ComponenteImportiCapitolo>();
		
				
//		this.listaImportiCapitoloResiduo =  importiCapitoloResiduo != null? 
//				importiCapitoloResiduo.getListaComponenteImportiCapitolo() : new ArrayList<ComponenteImportiCapitolo>();

		this.listaImportiCapitoloSuccessivi =  importiCapitoloAnniSuccessivi != null? 
				importiCapitoloAnniSuccessivi.getListaComponenteImportiCapitolo() : new ArrayList<ComponenteImportiCapitolo>();
		//questi due oggetti java hanno gli stessi campi e nessuna separazione logica, non ha senso che siano diversi!!!
		this.listaImportiCapitoloTriennioNoStanz = importiCapitoloTriennioNoStanziamento != null? importiCapitoloTriennioNoStanziamento : new ArrayList<ImportiImpegnatoPerComponenteTriennioNoStanz>();
		this.listaImportiCapitoloAnniSuccessiviNoStanz = listaImportiCapitoloAnniSuccessiviNoStanz != null? listaImportiCapitoloAnniSuccessiviNoStanz : new ArrayList<ImportiImpegnatoPerComponenteAnniSuccNoStanz>();
		this.mappaTipoComponenteComponentiPerAnno = popolaMappaTipoComponenteListaImportoComponenteWrapper();
	}
	
	private void impostaSuRigaGiaPresenteSulCapitoloEquivalente(
			BigDecimal importo, Integer idTipoComponente, PeriodoTabellaComponentiImportiCapitolo periodo) {
		RigaComponenteTabellaImportiCapitolo riga = this.mappaRigheComponentiPresentiSoloSuAnnoMenoUno.get(idTipoComponente);
		
		for (int i = 0; i < riga.getSottoRighe().size(); i++) {
			RigaDettaglioComponenteTabellaImportiCapitolo rdDettaglio = riga.getSottoRighe().get(i);
			if(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO.equals(rdDettaglio.getTipoDettaglioComponenteImportiCapitolo())) {
				
				BeanWrapper bwic = PropertyAccessorFactory.forBeanPropertyAccess(riga.getSottoRighe().get(i));
				
				bwic.setPropertyValue(periodo.getFieldNameRiga(),  importo);
			}
			
		}
	}
	
	public BigDecimal calcolaSommaImpegnatoResiduoFinaleSuComponentiAnnoBilancio() {
		BigDecimal somma = BigDecimal.ZERO;
		for(ComponenteImportiCapitolo cic : listaComponentiAnnoBilancio){
			somma = somma.add(cic.getImpegnatoResiduoFinale() != null ? cic.getImpegnatoResiduoFinale() : BigDecimal.ZERO);
		}
		return somma;
	}
	
	public BigDecimal calcolaSommaImpegnatoResiduoInizialeSuComponentiAnnoBilancio() {
		BigDecimal somma = BigDecimal.ZERO;
		for(ComponenteImportiCapitolo cic : listaComponentiAnnoBilancio){
			somma = somma.add(cic.getImpegnatoResiduoIniziale() != null ? cic.getImpegnatoResiduoIniziale() : BigDecimal.ZERO);
		}
		return somma;
	}
	
	private void  impostaImpegnato(PeriodoTabellaComponentiImportiCapitolo periodoImporto,	Integer idTipoComponente, String descrizioneComponente, BigDecimal importo ) {
		
		if(this.mappaRigheComponentiPresentiSoloSuAnnoMenoUno.get(idTipoComponente) != null) {
			impostaSuRigaGiaPresenteSulCapitoloEquivalente(importo, idTipoComponente,
					periodoImporto);
			return;
		}
		
		this.mappaidTipoCompDescrizionePerImpegnatoSenzaStanziato.put(idTipoComponente, descrizioneComponente);
		RigaDettaglioComponenteTabellaImportiCapitolo rdImpegnato = this.mappaDettaglioImpegnatoSenzaStanziato.get(idTipoComponente);
		if(this.mappaDettaglioImpegnatoSenzaStanziato.get(idTipoComponente) == null) {
			rdImpegnato = new RigaDettaglioComponenteTabellaImportiCapitolo();
			rdImpegnato.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
		}
		
		BeanWrapper bwic = PropertyAccessorFactory.forBeanPropertyAccess(rdImpegnato);
		
		bwic.setPropertyValue(periodoImporto.getFieldNameRiga(),  importo);
		
		this.mappaDettaglioImpegnatoSenzaStanziato.put(idTipoComponente, rdImpegnato);
	}

	
	private Map<Integer, List<WrapperComponenteImportoCapitoloAnno>> popolaMappaTipoComponenteListaImportoComponenteWrapper() {
		Map<Integer, List<WrapperComponenteImportoCapitoloAnno>> mappaW = new HashMap<Integer, List<WrapperComponenteImportoCapitoloAnno>>();
		popolaMappaWrapper(mappaW, listaComponentiAnnoMenoUno, PeriodoTabellaComponentiImportiCapitolo.ANNI_PRECEDENTI);
//		popolaMappaWrapper(mappaW, listaImportiCapitoloResiduo, PeriodoTabellaComponentiImportiCapitolo.RESIDUO_ANNO_BILANCIO);
		popolaMappaWrapper(mappaW, listaComponentiAnnoBilancio, PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO);
		popolaMappaWrapper(mappaW, listaComponentiAnnoBilancioPiuUno, PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_UNO);
		popolaMappaWrapper(mappaW, listaComponentiAnnoBilancioPiuDue, PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_DUE);
		popolaMappaWrapper(mappaW, listaImportiCapitoloSuccessivi, PeriodoTabellaComponentiImportiCapitolo.ANNI_SUCCESSIVI);
		return mappaW;
	}
	
	private void popolaMappaWrapper(Map<Integer, List<WrapperComponenteImportoCapitoloAnno>> mappa,
			List<ComponenteImportiCapitolo> listaComponenti, PeriodoTabellaComponentiImportiCapitolo periodoTabellaComponentiImportiCapitolo) {
		
		for (ComponenteImportiCapitolo cic : listaComponenti) {
			WrapperComponenteImportoCapitoloAnno wrapper = new WrapperComponenteImportoCapitoloAnno();
			wrapper.setComponenteImportiCapitolo(cic);
			wrapper.setPeriodoTabellaComponentiImportiCapitolo(periodoTabellaComponentiImportiCapitolo);
			Integer uidTipoComponente = Integer.valueOf(cic.getTipoComponenteImportiCapitolo().getUid());
			if(mappa.get(uidTipoComponente) == null) {
				mappa.put(uidTipoComponente, new ArrayList<WrapperComponenteImportoCapitoloAnno>());
			}
			mappa.get(uidTipoComponente).add(wrapper);
		}
	}
	
	
	public List<Integer> getUidTipoComponentiAssociateAlCapitolo(){
		return this.uidTipoComponentiAssociateAlCapitolo;
	}
	
	public List<TipoComponenteImportiCapitolo> filtraListaImportoComponente(List<TipoComponenteImportiCapitolo> toFilter){
		List<TipoComponenteImportiCapitolo> filtered = new ArrayList<TipoComponenteImportiCapitolo>();
		for (TipoComponenteImportiCapitolo tp : toFilter) {
			if(this.uidTipoComponentiAssociateAlCapitolo.contains(tp.getUid())) {
				continue;
			}
			filtered.add(tp);
		}
		return filtered;
	}
	

	public List<RigaComponenteTabellaImportiCapitolo> getRigheDisponibilitaImpegnareComponenti() {
		return righeDisponibilitaImpegnare;
	}

	public List<RigaComponenteTabellaImportiCapitolo> getRigheDisponibilitaVariareComponenti() {
		return righeDisponibilitaVariare;
	}
	
	/**Per la serializzazione */

	private void elaboraRigheComponentiAssociateAlCapitolo() {
		
		//se una componente r' presente sull'anno di bialncio meno uno ma non su quello successivo, essa deve essere mostrata DOPO le componenti presenti sul
		//capitolo vero. per fare questo, uso una lista di appoggio.
		
		// 1- mi creo un mapper che per uid tipo componente mi da una lista di wrapper delle componenti presenti
		if(mappaTipoComponenteComponentiPerAnno == null) {
			elaboraRigheComponentiConImpegnatoMaSenzaStanziato();
			return;
		}
		
		
		for (Integer uidTipoComponente : mappaTipoComponenteComponentiPerAnno.keySet()) {
			
			uidTipoComponentiAssociateAlCapitolo.add(uidTipoComponente);
			
			List<WrapperComponenteImportoCapitoloAnno> listaWrapperComponentiImportoCapitoloByTipoComponente = mappaTipoComponenteComponentiPerAnno.get(uidTipoComponente);
			
			RigaComponenteTabellaImportiCapitolo riga = popolaRigaComponenteSulCapitolo(uidTipoComponente, listaWrapperComponentiImportoCapitoloByTipoComponente);
			
			if(riga.hasUidComponentesuiTreAnni()) {
				//la componente e' presente sul capitolo, va mostrata per prima
				this.righeComponentiElaborate.add(riga);
			}else {
				//la componente e' presente solo sul capitolo equivalente, forzo il fatto che sia mostrata per ultima
//				righeComponentiCollegateAlCapitoloEquivalenteMaNonAlCapitolo.add(riga);
				this.mappaRigheComponentiPresentiSoloSuAnnoMenoUno.put(uidTipoComponente, riga);
			}
			
		}
		elaboraRigheComponentiConImpegnatoMaSenzaStanziato();
	}


	private RigaComponenteTabellaImportiCapitolo popolaRigaComponenteSulCapitolo(Integer key,
			List<WrapperComponenteImportoCapitoloAnno> cic) {
		RigaComponenteTabellaImportiCapitolo riga = new RigaComponenteTabellaImportiCapitolo();
		riga.setUidTipoComponenteImportiCapitolo(key);
		
		TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo = cic.get(0).getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo();
		riga.setDescrizioneTipoComponente(tipoComponenteImportiCapitolo.getDescrizione());
		esistonoComponentiNonFrescoSulCapitolo = esistonoComponentiNonFrescoSulCapitolo || tipoComponenteImportiCapitolo.getMacrotipoComponenteImportiCapitolo() == null || !MacrotipoComponenteImportiCapitolo.FRESCO.equals(tipoComponenteImportiCapitolo.getMacrotipoComponenteImportiCapitolo());
		//questo e' importante invece: se e' collegata al capitolo, la posso eliminare, altrimenti no.
		riga.setPropostaDefault(false);
		
		impostaDatisuRigaDaWrapper(riga, cic);
		return riga;
	}


	private List<RigaComponenteTabellaImportiCapitolo> elaboraRigheComponentiDefault() {
		List<RigaComponenteTabellaImportiCapitolo> righeTabellaComponentiDefault = new ArrayList<RigaComponenteTabellaImportiCapitolo>();
		
		for (TipoComponenteImportiCapitolo def : this.listaTipiComponente) {
			if(uidTipoComponentiAssociateAlCapitolo.contains(Integer.valueOf(def.getUid())) || uidTipoComponentiAssociateAlCapitoloPersoloImpegnato.contains(def.getUid())) {
				continue;
			}
			RigaComponenteTabellaImportiCapitolo riga = new RigaComponenteTabellaImportiCapitolo();
			riga.setUidTipoComponenteImportiCapitolo(def.getUid());
			riga.setDescrizioneTipoComponente(def.getDescrizione());
			riga.setMacrotipoComponenteImportiCapitolo(def.getMacrotipoComponenteImportiCapitolo());
			//importante: se non e' collegata al capitolo, non la posso scollegare dal capitolo
			riga.setPropostaDefault(true);
			
			riga.setSottoRighe(new ArrayList<RigaDettaglioComponenteTabellaImportiCapitolo>());
			for (DettaglioComponenteImportiCapitolo detdef : def.getListaDettaglioComponenteImportiCapitolo()) {
				RigaDettaglioComponenteTabellaImportiCapitolo sottoRiga = new RigaDettaglioComponenteTabellaImportiCapitolo();
				TipoDettaglioComponenteImportiCapitolo tp = detdef.getTipoDettaglioComponenteImportiCapitolo();
				sottoRiga.setTipoDettaglioComponenteImportiCapitolo(tp);
				riga.getSottoRighe().add(sottoRiga);
			}
			
			righeTabellaComponentiDefault.add(riga);
		}
		
		return righeTabellaComponentiDefault;
	}

	
	private void elaboraRigheComponentiConImpegnatoMaSenzaStanziato() {
		
		//i dati dovrebbero essere ottenuti dal servizio in tutt'altro modo. Sono costratta ad adeguare il front-end
		for (ImportiImpegnatoPerComponenteTriennioNoStanz impegnato : this.listaImportiCapitoloTriennioNoStanz) {
			Integer idTipoComponente = impegnato.getIdComp();
			Integer annoImpegnato = impegnato.getAnnoImpegnato();
			
			if( annoImpegnato == null) {
				continue;
			}
			PeriodoTabellaComponentiImportiCapitolo periodoImporto = PeriodoTabellaComponentiImportiCapitolo.getByDeltaEscludendoResiduo(annoImpegnato.intValue() - this.annoBilancio.intValue());
			impostaImpegnato(periodoImporto, idTipoComponente,
					impegnato.getDescrizioneComponente(), impegnato.getImporto());
			
		}
		
		for (ImportiImpegnatoPerComponenteAnniSuccNoStanz impegnato : this.listaImportiCapitoloAnniSuccessiviNoStanz) {
			impostaImpegnato(PeriodoTabellaComponentiImportiCapitolo.ANNI_SUCCESSIVI, impegnato.getIdComp(),
					impegnato.getDescrizioneComponente(), impegnato.getImporto());
			
		}
		//aggiungo le componenti che abbiano impegnato e stanziato
		this.righeComponentiElaborate.addAll(this.mappaRigheComponentiPresentiSoloSuAnnoMenoUno.values());
		
		Set<Integer> uids = this.mappaDettaglioImpegnatoSenzaStanziato.keySet();
		
		for (Integer uid : uids) {
			RigaComponenteTabellaImportiCapitolo riga = new RigaComponenteTabellaImportiCapitolo();
			riga.setUidTipoComponenteImportiCapitolo(uid);
			riga.setDescrizioneTipoComponente(this.mappaidTipoCompDescrizionePerImpegnatoSenzaStanziato.get(uid));
			//importante: se non e' collegata al capitolo, non la posso scollegare dal capitolo
			riga.setPropostaDefault(true);
			
			riga.setSottoRighe(new ArrayList<RigaDettaglioComponenteTabellaImportiCapitolo>());
			
			//aggiungo il dettaglio stanziamento prima del dettaglio impegnato
			RigaDettaglioComponenteTabellaImportiCapitolo rd = new RigaDettaglioComponenteTabellaImportiCapitolo();
			rd.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
			rd.setTipoCapitolo(this.tipoCapitolo);
			riga.getSottoRighe().add(rd);
			
			//aggiungo il dettaglio stanziamento prima del dettaglio impegnato
			riga.getSottoRighe().add(mappaDettaglioImpegnatoSenzaStanziato.get(uid));
			
			this.righeComponentiElaborate.add(riga);
			
		}
		
		this.uidTipoComponentiAssociateAlCapitoloPersoloImpegnato.addAll(uids);
		
	}
	
	
	private void impostaDatisuRigaDaWrapper(RigaComponenteTabellaImportiCapitolo rigaComponente, List<WrapperComponenteImportoCapitoloAnno> cics) {
		Map<TipoDettaglioComponenteImportiCapitolo, RigaDettaglioComponenteTabellaImportiCapitolo> mappaTipoDettaglioRigaDettaglio = new HashMap<TipoDettaglioComponenteImportiCapitolo, RigaDettaglioComponenteTabellaImportiCapitolo>();
		for (WrapperComponenteImportoCapitoloAnno wrp : cics) {
			PeriodoTabellaComponentiImportiCapitolo periodo = wrp.getPeriodoTabellaComponentiImportiCapitolo();
			ComponenteImportiCapitolo comp = wrp.getComponenteImportiCapitolo();
			
			impostaUidComponenti(rigaComponente, periodo, comp);
			
			impostaImportiSullaRigaDettaglio(mappaTipoDettaglioRigaDettaglio, wrp, periodo, comp);
			
		}
		//probabilmente si puo' migliorare
		List<RigaDettaglioComponenteTabellaImportiCapitolo> listResult = extractListaFromMappa(mappaTipoDettaglioRigaDettaglio);

		rigaComponente.setSottoRighe(listResult);
	}


	private List<RigaDettaglioComponenteTabellaImportiCapitolo> extractListaFromMappa(
			Map<TipoDettaglioComponenteImportiCapitolo, RigaDettaglioComponenteTabellaImportiCapitolo> mp) {
		List<RigaDettaglioComponenteTabellaImportiCapitolo> listResult = new ArrayList<RigaDettaglioComponenteTabellaImportiCapitolo>();
		
		List<RigaDettaglioComponenteTabellaImportiCapitolo> tmp = new ArrayList<RigaDettaglioComponenteTabellaImportiCapitolo>();
		RigaDettaglioComponenteTabellaImportiCapitolo rigaDettagliostanziamento = new RigaDettaglioComponenteTabellaImportiCapitolo();;
		for (RigaDettaglioComponenteTabellaImportiCapitolo rd : mp.values()) {
			rd.setTipoCapitolo(this.tipoCapitolo);
			if(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO.equals(rd.getTipoDettaglioComponenteImportiCapitolo())) {
				//SIAC-8461 per la riga della competenza il concetto di residuo non esiste a livello contabile
				//le componenti seguono le regole della competenza
				rd.setImportoResiduoAnno0(null);
				rd.setImportoResiduoInizialeAnno0(null);
				rigaDettagliostanziamento = rd;
			}else if(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO.equals(rd.getTipoDettaglioComponenteImportiCapitolo())){
				tmp.add(rd);
			}
			
		}
		//voglio che lo stanziamento sia il primo, sempre.
		listResult.add(rigaDettagliostanziamento);
		listResult.addAll(tmp);
		return listResult;
	}


	private void impostaUidComponenti(RigaComponenteTabellaImportiCapitolo rigaComponente,
			PeriodoTabellaComponentiImportiCapitolo periodo, ComponenteImportiCapitolo comp) {
		String fieldnameUid = periodo.getFieldNameUidComponente();
		
		if(StringUtils.isNotEmpty(fieldnameUid)) {
			BeanWrapper bwic = PropertyAccessorFactory.forBeanPropertyAccess(rigaComponente);
			bwic.setPropertyValue(fieldnameUid,  comp.getUid());
		}
	}


	private void impostaImportiSullaRigaDettaglio(
			Map<TipoDettaglioComponenteImportiCapitolo, RigaDettaglioComponenteTabellaImportiCapitolo> mp,
			WrapperComponenteImportoCapitoloAnno wrp, PeriodoTabellaComponentiImportiCapitolo periodo,
			ComponenteImportiCapitolo comp) {
		for (DettaglioComponenteImportiCapitolo dt : comp.getListaDettaglioComponenteImportiCapitolo()) {
			TipoDettaglioComponenteImportiCapitolo tp = dt.getTipoDettaglioComponenteImportiCapitolo();
			if(mp.get(tp) == null) {
				RigaDettaglioComponenteTabellaImportiCapitolo rigaNew = new RigaDettaglioComponenteTabellaImportiCapitolo();
				if(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO.equals(tp)) {
					rigaNew.setImportoResiduoInizialeAnno0(comp.getImpegnatoResiduoIniziale());
					rigaNew.setImportoResiduoAnno0(comp.getImpegnatoResiduoFinale());
				}
				
				mp.put(tp, rigaNew);
			}
			RigaDettaglioComponenteTabellaImportiCapitolo riga = mp.get(tp);
			riga.setTipoDettaglioComponenteImportiCapitolo(tp);
			
			BigDecimal importo = extractImportoComponente(wrp, periodo, dt, tp);
			
			BeanWrapper bwic = PropertyAccessorFactory.forBeanPropertyAccess(riga);
			bwic.setPropertyValue(periodo.getFieldNameRiga(),  importo);
			
		}
	}


	private BigDecimal extractImportoComponente(WrapperComponenteImportoCapitoloAnno wrp,
			PeriodoTabellaComponentiImportiCapitolo periodo, DettaglioComponenteImportiCapitolo dt,
			TipoDettaglioComponenteImportiCapitolo tp) {
		BigDecimal importo; //wrp.getComponenteImportiCapitolo().getImpegnatoResiduoFinale()
		String fieldNameComponente = periodo.getFieldNameComponente(tp); 
		if( fieldNameComponente == null) {
			importo = dt.getImporto();
		}else {
			BeanWrapper bwicCompo = PropertyAccessorFactory.forBeanPropertyAccess(wrp.getComponenteImportiCapitolo());
			importo = (BigDecimal) bwicCompo.getPropertyValue(fieldNameComponente); // setPropertyValue(periodo.getFieldNameRiga(), dt.getImporto());
		}
		return importo != null? importo : BigDecimal.ZERO;
	}

	
	
	public void elaboraRigheComponentiTabellaDisponibilitaVariareImpegnareComponenti() {
		
		for (Integer uidTipoComponente : mappaTipoComponenteComponentiPerAnno.keySet()) {
			
			uidTipoComponentiAssociateAlCapitolo.add(uidTipoComponente);
			
			List<WrapperComponenteImportoCapitoloAnno> listaWrapperComponentiImportoCapitoloByTipoComponente = mappaTipoComponenteComponentiPerAnno.get(uidTipoComponente);
			
			RigaComponenteTabellaImportiCapitolo rigaDisponibilitaVariare = new RigaComponenteTabellaImportiCapitolo();
			rigaDisponibilitaVariare.setUidTipoComponenteImportiCapitolo(uidTipoComponente);
			rigaDisponibilitaVariare.setDescrizioneTipoComponente(listaWrapperComponentiImportoCapitoloByTipoComponente.get(0).getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo().getDescrizione());
			
			RigaComponenteTabellaImportiCapitolo rigaDisponibilitaImpegnare = new RigaComponenteTabellaImportiCapitolo();
			rigaDisponibilitaImpegnare.setUidTipoComponenteImportiCapitolo(uidTipoComponente);
			rigaDisponibilitaImpegnare.setDescrizioneTipoComponente(listaWrapperComponentiImportoCapitoloByTipoComponente.get(0).getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo().getDescrizione());
			
			
			RigaDettaglioComponenteTabellaImportiCapitolo rigaDettDisponibilitaVariare = new RigaDettaglioComponenteTabellaImportiCapitolo();
			RigaDettaglioComponenteTabellaImportiCapitolo rigaDettDisponibilitaImpegnare = new RigaDettaglioComponenteTabellaImportiCapitolo();
			
			for (WrapperComponenteImportoCapitoloAnno wrp : listaWrapperComponentiImportoCapitoloByTipoComponente) {
				PeriodoTabellaComponentiImportiCapitolo periodo = wrp.getPeriodoTabellaComponentiImportiCapitolo();
				if(wrp.getPeriodoTabellaComponentiImportiCapitolo().getDelta() <0 ||  wrp.getPeriodoTabellaComponentiImportiCapitolo().getDelta() >2) {
					continue;
				}
				for (DettaglioComponenteImportiCapitolo dt : wrp.getComponenteImportiCapitolo().getListaDettaglioComponenteImportiCapitolo()) {
					TipoDettaglioComponenteImportiCapitolo tp = dt.getTipoDettaglioComponenteImportiCapitolo();
					// SIAC-8884
					if(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAVARIARE.equals(tp)) {
						rigaDettDisponibilitaVariare.setTipoDettaglioComponenteImportiCapitolo(tp);
						BigDecimal importo =  dt.getImporto();
						BeanWrapper bwic = PropertyAccessorFactory.forBeanPropertyAccess(rigaDettDisponibilitaVariare);
						bwic.setPropertyValue(periodo.getFieldNameRiga(),  importo);	
					}else if(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAIMPEGNARE.equals(tp)) {
						rigaDettDisponibilitaImpegnare.setTipoDettaglioComponenteImportiCapitolo(tp);
						BigDecimal importo =  dt.getImporto();
						BeanWrapper bwic = PropertyAccessorFactory.forBeanPropertyAccess(rigaDettDisponibilitaImpegnare);
						bwic.setPropertyValue(periodo.getFieldNameRiga(),  importo);
					}
				}	
			}
			rigaDisponibilitaVariare.setSottoRighe(Arrays.asList(rigaDettDisponibilitaVariare));
			rigaDisponibilitaImpegnare.setSottoRighe(Arrays.asList(rigaDettDisponibilitaImpegnare));
					
			this.righeDisponibilitaVariare.add(rigaDisponibilitaVariare);
			this.righeDisponibilitaImpegnare.add(rigaDisponibilitaImpegnare);	
		}
		//aggiungo una riga per ogni componente associata al capitolo
	}
	
	public void elaboraRigheComponentiTabella() {
		this.righeComponentiElaborate = new ArrayList<RigaComponenteTabellaImportiCapitolo>();
		//aggiungo una riga per ogni componente associata al capitolo
		elaboraRigheComponentiAssociateAlCapitolo();
		//aggiungo le componenti che prevedono un impegnato ma non uno stanziamento
//		this.righeComponentiElaborate.addAll(elaboraRigheComponentiConImpegnatoMaSenzaStanziato());		
		
	}
	
	public void elaboraRigheComponentiTabellaConComponentiDefault() {
		this.righeComponentiElaborate = new ArrayList<RigaComponenteTabellaImportiCapitolo>();
		//aggiungo una riga per ogni componente associata al capitolo
		elaboraRigheComponentiAssociateAlCapitolo();
		//aggiungo le componenti che prevedono un impegnato ma non uno stanziamento
//		this.righeComponentiElaborate.addAll(elaboraRigheComponentiConImpegnatoMaSenzaStanziato());		
		//aggiungo una riga per ogni componente di default escludendo quelle associate al capitolo
		this.righeComponentiElaborate.addAll(elaboraRigheComponentiDefault());
		
	}

}

