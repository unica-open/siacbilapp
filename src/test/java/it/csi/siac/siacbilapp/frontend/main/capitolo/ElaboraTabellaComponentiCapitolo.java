/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.main.capitolo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.PeriodoTabellaComponentiImportiCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaComponenteTabellaImportiCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaDettaglioComponenteTabellaImportiCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaDettaglioImportoTabellaImportiCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaImportoTabellaImportiCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.TipologiaImportoTabellaImportoCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.WrapperComponenteImportoCapitoloAnno;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoComponenteImportiCapitoloPerCapitoloResponse;
import it.csi.siac.siacbilser.frontend.webservice.msg.base.BaseComponenteImportiCapitoloResponse;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.DettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.PropostaDefaultComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;
import it.csi.siac.siaccommon.util.log.LogUtil;

public class ElaboraTabellaComponentiCapitolo {
	


	public static void elaboraComponenti(LogUtil log,
			BaseComponenteImportiCapitoloResponse responseComponente, Integer annoBilancio, RicercaTipoComponenteImportiCapitoloPerCapitoloResponse responseTipoComponente ) {
		final String methodName = "elaboraComponenti";
		List<RigaComponenteTabellaImportiCapitolo> righeTabella = elaboraRigheComponentiTabellaDaResponse(responseComponente);
		
		
		String tableTextString =  HtmlTabellaComponentiCapitolo.getTableAggiornamentoComponenteText(righeTabella, annoBilancio);
		log.info(methodName,"aggiornamento componente: \n" + tableTextString);
		
		List<TipoComponenteImportiCapitolo> listaTipiComponente = responseTipoComponente.getListaTipoComponenteImportiCapitolo();
		List<TipoComponenteImportiCapitolo> tipoComponenteDefault = new ArrayList<TipoComponenteImportiCapitolo>();
		List<TipoComponenteImportiCapitolo> tipoComponenteNonDefault = new ArrayList<TipoComponenteImportiCapitolo>();
		List<PropostaDefaultComponenteImportiCapitolo> proposteDefault = Arrays.asList(PropostaDefaultComponenteImportiCapitolo.SI, PropostaDefaultComponenteImportiCapitolo.SOLO_PREVISIONE);
		
		for(TipoComponenteImportiCapitolo  tipo : listaTipiComponente) {
			if(proposteDefault.contains(tipo.getPropostaDefaultComponenteImportiCapitolo())) {
				tipoComponenteDefault.add(tipo);
			}else {
				tipoComponenteNonDefault.add(tipo);
			}
		}
		
		
		for (TipoComponenteImportiCapitolo def : tipoComponenteDefault) {
			RigaComponenteTabellaImportiCapitolo riga = new RigaComponenteTabellaImportiCapitolo();
			//lo forzo per sicurezza, non e' necessario
			riga.setUidTipoComponenteImportiCapitolo(0);
			riga.setDescrizioneTipoComponente(def.getDescrizione());
			//importante: se non e' collegata al capitolo, non la posso scollegare dal capitolo
			riga.setPropostaDefault(true);
			
			riga.setSottoRighe(new ArrayList<RigaDettaglioComponenteTabellaImportiCapitolo>());
			for (DettaglioComponenteImportiCapitolo detdef : def.getListaDettaglioComponenteImportiCapitolo()) {
				RigaDettaglioComponenteTabellaImportiCapitolo sottoRiga = new RigaDettaglioComponenteTabellaImportiCapitolo();
				TipoDettaglioComponenteImportiCapitolo tp = detdef.getTipoDettaglioComponenteImportiCapitolo();
				sottoRiga.setTipoDettaglioComponenteImportiCapitolo(tp);
				riga.getSottoRighe().add(sottoRiga);
			}
			
			righeTabella.add(riga);
		}
		
		String tableTextConsultazioneString =  HtmlTabellaComponentiCapitolo.getTableConsultazioneComponenteText(righeTabella, annoBilancio);
		log.info(methodName,"consultazione componenti: \n" +  tableTextConsultazioneString);
		
		log.info(methodName, "<h4>...</h4>" + tableTextString + "<h4>...</h4>" + tableTextConsultazioneString);
	}

	
	public static RigaImportoTabellaImportiCapitolo calcolaRigaImportoCassa(Integer annoBilancio,List<ImportiCapitolo> listaImportiCapitolo) {
		
		RigaImportoTabellaImportiCapitolo rigaResiduo = new RigaImportoTabellaImportiCapitolo();
		rigaResiduo.setTipoImportiCapitoloTabella(TipologiaImportoTabellaImportoCapitolo.CASSA);
		RigaDettaglioImportoTabellaImportiCapitolo rigaImportoCassaStanziamento = new RigaDettaglioImportoTabellaImportiCapitolo();
		rigaImportoCassaStanziamento.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		
		RigaDettaglioImportoTabellaImportiCapitolo rigaImportoCassaPagato = new RigaDettaglioImportoTabellaImportiCapitolo();
		rigaImportoCassaPagato.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.PAGATO);
		
		
		for (ImportiCapitolo importiCapitolo : listaImportiCapitolo) {
			if(annoBilancio.equals(importiCapitolo.getAnnoCompetenza())){
				rigaImportoCassaStanziamento.setImportoResiduoAnno0(importiCapitolo.getStanziamentoCassa());
			}
			
			if((annoBilancio.intValue() -1 ) == importiCapitolo.getAnnoCompetenza()) {
				rigaImportoCassaStanziamento.setImportoAnniPrecedenti(importiCapitolo.getStanziamentoCassa());
				//controllo per sicurezza
				if(importiCapitolo instanceof ImportiCapitoloUG) {
					rigaImportoCassaPagato.setImportoAnniPrecedenti(((ImportiCapitoloUG)importiCapitolo).getTotalePagato());
				}
				
			}
			
			
		}
		
		rigaResiduo.addSottoRiga(rigaImportoCassaStanziamento);
		rigaResiduo.addSottoRiga(rigaImportoCassaPagato);
		return rigaResiduo;
	}
	
	
	public static RigaImportoTabellaImportiCapitolo calcolaRigaImportoCompetenza(Integer annoBilancio,List<ImportiCapitolo> listaImportiCapitolo, 
			ImportiCapitolo residuo, ImportiCapitolo annoSuccessivoImporto ) {
		RigaImportoTabellaImportiCapitolo rigaCompetenza = new RigaImportoTabellaImportiCapitolo();
		rigaCompetenza.setTipoImportiCapitoloTabella(TipologiaImportoTabellaImportoCapitolo.COMPETENZA);
		
		RigaDettaglioImportoTabellaImportiCapitolo rigaDettaglioCompetenzaStanziamento = new RigaDettaglioImportoTabellaImportiCapitolo();
		rigaDettaglioCompetenzaStanziamento.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		
		RigaDettaglioImportoTabellaImportiCapitolo rigaDettaglioCompetenzaImpegnato = new RigaDettaglioImportoTabellaImportiCapitolo();
		rigaDettaglioCompetenzaImpegnato.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
		
		RigaDettaglioImportoTabellaImportiCapitolo rigaDettaglioCompetenzaDispImpegnare = new RigaDettaglioImportoTabellaImportiCapitolo();
		rigaDettaglioCompetenzaDispImpegnare.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAIMPEGNARE);
		
		
		for (ImportiCapitolo importiCapitolo : listaImportiCapitolo) {
			
			if((annoBilancio.intValue() -1 ) == importiCapitolo.getAnnoCompetenza()) {
				rigaDettaglioCompetenzaStanziamento.setImportoAnniPrecedenti(importiCapitolo.getStanziamento());
			}
			
			if(annoBilancio.equals(importiCapitolo.getAnnoCompetenza())){
				rigaDettaglioCompetenzaStanziamento.setImportoAnno0(importiCapitolo.getStanziamento());
				rigaDettaglioCompetenzaImpegnato.setImportoAnniPrecedenti(((ImportiCapitoloUP)importiCapitolo).getDiCuiImpegnatoAnnoPrec());
				rigaDettaglioCompetenzaImpegnato.setImportoAnno0(((ImportiCapitoloUP)importiCapitolo).getImpegnatoEffettivoUPAnno1());
				rigaDettaglioCompetenzaImpegnato.setImportoAnno1(((ImportiCapitoloUP)importiCapitolo).getImpegnatoEffettivoUPAnno2());
				rigaDettaglioCompetenzaImpegnato.setImportoAnno2(((ImportiCapitoloUP)importiCapitolo).getImpegnatoEffettivoUPAnno3());
				rigaDettaglioCompetenzaImpegnato.setImportoAnniSuccessivi(((ImportiCapitoloUP)importiCapitolo).getDiCuiImpegnatoAnniSucc());
				
				rigaDettaglioCompetenzaDispImpegnare.setImportoAnno0(((ImportiCapitoloUP)importiCapitolo).getDisponibilitaImpegnareUPAnno1());
				rigaDettaglioCompetenzaDispImpegnare.setImportoAnno1(((ImportiCapitoloUP)importiCapitolo).getDisponibilitaImpegnareUPAnno2());
				rigaDettaglioCompetenzaDispImpegnare.setImportoAnno2(((ImportiCapitoloUP)importiCapitolo).getDisponibilitaImpegnareUPAnno3());
			}
			
			if((annoBilancio.intValue() +1 ) == importiCapitolo.getAnnoCompetenza()) {
				rigaDettaglioCompetenzaStanziamento.setImportoAnno1(importiCapitolo.getStanziamento());
			}
			
			if((annoBilancio.intValue() +2 ) == importiCapitolo.getAnnoCompetenza()) {
				rigaDettaglioCompetenzaStanziamento.setImportoAnno2(importiCapitolo.getStanziamento());
			}
			
			
			
		}
		
		
		rigaDettaglioCompetenzaStanziamento.setImportoResiduoAnno0(residuo!= null ? residuo.getStanziamento() : BigDecimal.ZERO);
		rigaDettaglioCompetenzaStanziamento.setImportoAnniSuccessivi(annoSuccessivoImporto!= null ? annoSuccessivoImporto.getStanziamento() : BigDecimal.ZERO);
		
		
		rigaCompetenza.addSottoRiga(rigaDettaglioCompetenzaStanziamento);
		rigaCompetenza.addSottoRiga(rigaDettaglioCompetenzaImpegnato);
		rigaCompetenza.addSottoRiga(rigaDettaglioCompetenzaDispImpegnare);
		
		return rigaCompetenza;
		
	}

	public static RigaImportoTabellaImportiCapitolo calcolaRigaImportoResiduo(Integer annoBilancio,List<ImportiCapitolo> listaImportiCapitolo, ImportiCapitolo importiAnniSuccessivi) {
		
		RigaImportoTabellaImportiCapitolo rigaResiduo = new RigaImportoTabellaImportiCapitolo();
		rigaResiduo.setTipoImportiCapitoloTabella(TipologiaImportoTabellaImportoCapitolo.RESIDUO);
		RigaDettaglioImportoTabellaImportiCapitolo rigaImportoResiduoPresunto = new RigaDettaglioImportoTabellaImportiCapitolo();
		rigaImportoResiduoPresunto.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.PRESUNTO);
		rigaImportoResiduoPresunto.setImportoAnniSuccessivi(importiAnniSuccessivi.getStanziamentoResiduo());
		
		RigaDettaglioImportoTabellaImportiCapitolo rigaImportoResiduoEffettivo = new RigaDettaglioImportoTabellaImportiCapitolo();
		rigaImportoResiduoEffettivo.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.EFFETTIVO);
		rigaImportoResiduoEffettivo.setImportoAnniSuccessivi(importiAnniSuccessivi.getStanziamentoResiduo());
		
		BigDecimal importoResiduiCalcolato = calcolaResiduoDaComponenti();
		
		for (ImportiCapitolo importiCapitolo : listaImportiCapitolo) {
			if(annoBilancio.equals(importiCapitolo.getAnnoCompetenza())){
				rigaImportoResiduoPresunto.setImportoResiduoAnno0(importiCapitolo.getStanziamentoResiduo());
			}
			
			if((annoBilancio.intValue() -1 ) == importiCapitolo.getAnnoCompetenza()) {
				rigaImportoResiduoEffettivo.setImportoAnniPrecedenti(importiCapitolo.getStanziamentoResiduo());
			}
			
			
		}
		
		rigaResiduo.addSottoRiga(rigaImportoResiduoPresunto);
		rigaResiduo.addSottoRiga(rigaImportoResiduoEffettivo);
		return rigaResiduo;
	}


	private static BigDecimal calcolaResiduoDaComponenti() {
		BigDecimal importoResiduiCalcolato = BigDecimal.ZERO;
		ComponenteImportiCapitolo cic = new ComponenteImportiCapitolo();
		BigDecimal residuoIniziale = cic.getImpegnatoResiduoIniziale() != null ? cic.getImpegnatoResiduoIniziale() : BigDecimal.ZERO;
		importoResiduiCalcolato = importoResiduiCalcolato.add(residuoIniziale);
		return importoResiduiCalcolato;
	}


	public static List<RigaComponenteTabellaImportiCapitolo> elaboraRigheComponentiTabellaDaResponse(BaseComponenteImportiCapitoloResponse response) {
		final String methodName = "ottieniRigheTabellaDaResponse";
		LogUtil log = new LogUtil(ElaboraTabellaComponentiCapitolo.class);
		
		List<ImportiCapitolo> listaImportiCapitolo = response.getListaImportiCapitolo();
		
		ImportiCapitolo importoCapitoloAnnoMenoUno = listaImportiCapitolo.get(0);
		List<ComponenteImportiCapitolo> listaComponentiAnnoMenoUno = importoCapitoloAnnoMenoUno != null? 
				importoCapitoloAnnoMenoUno.getListaComponenteImportiCapitolo() : new ArrayList<ComponenteImportiCapitolo>();
		
		ImportiCapitolo importiCapitoloAnnoBilancio = listaImportiCapitolo.get(1);
		List<ComponenteImportiCapitolo> listaComponentiAnnoBilancio =  importiCapitoloAnnoBilancio != null? 
				importiCapitoloAnnoBilancio.getListaComponenteImportiCapitolo() : new ArrayList<ComponenteImportiCapitolo>();
		
		ImportiCapitolo importiCapitoloAnnoBilancioPiuUno = listaImportiCapitolo.get(2);
		List<ComponenteImportiCapitolo> listaComponentiAnnoBilancioPiuUno =  importiCapitoloAnnoBilancioPiuUno != null? 
				importiCapitoloAnnoBilancioPiuUno.getListaComponenteImportiCapitolo() : new ArrayList<ComponenteImportiCapitolo>();
		
		ImportiCapitolo importiCapitoloAnnoBilancioPiuDue = listaImportiCapitolo.get(3);
		List<ComponenteImportiCapitolo> listaComponentiAnnoBilancioPiuDue =  importiCapitoloAnnoBilancioPiuDue != null? 
				importiCapitoloAnnoBilancioPiuDue.getListaComponenteImportiCapitolo() : new ArrayList<ComponenteImportiCapitolo>();
		
				
		ImportiCapitolo importiCapitoloResiduo = response.getImportiCapitoloResiduo();
		List<ComponenteImportiCapitolo> listaImportiCapitoloResiduo =  importiCapitoloResiduo != null? 
				importiCapitoloResiduo.getListaComponenteImportiCapitolo() : new ArrayList<ComponenteImportiCapitolo>();

		ImportiCapitolo importiCapitoloSuccessivi = response.getImportiCapitoloAnniSuccessivi();
		List<ComponenteImportiCapitolo> listaImportiCapitoloSuccessivi =  importiCapitoloSuccessivi != null? 
				importiCapitoloResiduo.getListaComponenteImportiCapitolo() : new ArrayList<ComponenteImportiCapitolo>();
		
		//caso standard
		boolean componentiPresentiAnnoPrecedente = listaComponentiAnnoMenoUno != null && !listaComponentiAnnoMenoUno.isEmpty();
		boolean componentiPresentiTriennio =  listaComponentiAnnoBilancio != null && !listaComponentiAnnoBilancio.isEmpty()
				&&  listaComponentiAnnoBilancioPiuUno != null && !listaComponentiAnnoBilancioPiuUno.isEmpty()
				&&  listaComponentiAnnoBilancioPiuDue != null && !listaComponentiAnnoBilancioPiuDue.isEmpty();
		
		log.info(methodName, "Caso standard: " +  (componentiPresentiAnnoPrecedente && componentiPresentiTriennio) 
				+ ", presenti componenti anno N: "  + componentiPresentiTriennio + ", presenti componenti anno N -1: "  + componentiPresentiAnnoPrecedente);
		
		
		Map<Integer, List<WrapperComponenteImportoCapitoloAnno>> mappaW = new HashMap<Integer, List<WrapperComponenteImportoCapitoloAnno>>();
		popolaMappaWrapper(mappaW, listaComponentiAnnoMenoUno, PeriodoTabellaComponentiImportiCapitolo.ANNI_PRECEDENTI);
		popolaMappaWrapper(mappaW, listaImportiCapitoloResiduo, PeriodoTabellaComponentiImportiCapitolo.RESIDUO_ANNO_BILANCIO);
		popolaMappaWrapper(mappaW, listaComponentiAnnoBilancio, PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO);
		popolaMappaWrapper(mappaW, listaComponentiAnnoBilancioPiuUno, PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_UNO);
		popolaMappaWrapper(mappaW, listaComponentiAnnoBilancioPiuDue, PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_DUE);
		popolaMappaWrapper(mappaW, listaImportiCapitoloSuccessivi, PeriodoTabellaComponentiImportiCapitolo.ANNI_SUCCESSIVI);
		
		List<RigaComponenteTabellaImportiCapitolo> righeTabella = new ArrayList<RigaComponenteTabellaImportiCapitolo>();
		
		for (Integer key : mappaW.keySet()) {
			List<WrapperComponenteImportoCapitoloAnno> cic = mappaW.get(key);
			RigaComponenteTabellaImportiCapitolo riga = new RigaComponenteTabellaImportiCapitolo();
			riga.setUidTipoComponenteImportiCapitolo(key);
			riga.setDescrizioneTipoComponente(cic.get(0).getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo().getDescrizione());
			//questo e' importante invece: se e' collegata al capitolo, la posso eliminare, altrimenti no.
			riga.setPropostaDefault(false);
			riga.setSottoRighe(elaboraSottoRigheW(cic));
//			log.logXmlTypeObject(riga, "wawa");
//			String tableTextSingle = getTableTextSingle(riga, 2020);
//			log.info(methodName,tableTextSingle);
			righeTabella.add(riga);
		}
		return righeTabella;
	}

	
	public static List<RigaDettaglioComponenteTabellaImportiCapitolo> elaboraSottoRigheW(List<WrapperComponenteImportoCapitoloAnno> cics) {
		Map<TipoDettaglioComponenteImportiCapitolo, RigaDettaglioComponenteTabellaImportiCapitolo> mp = new HashMap<TipoDettaglioComponenteImportiCapitolo, RigaDettaglioComponenteTabellaImportiCapitolo>();
		for (WrapperComponenteImportoCapitoloAnno wrp : cics) {
			PeriodoTabellaComponentiImportiCapitolo periodo = wrp.getPeriodoTabellaComponentiImportiCapitolo();
			
			for (DettaglioComponenteImportiCapitolo dt : wrp.getComponenteImportiCapitolo().getListaDettaglioComponenteImportiCapitolo()) {
				TipoDettaglioComponenteImportiCapitolo tp = dt.getTipoDettaglioComponenteImportiCapitolo();
				if(mp.get(tp) == null) {
					mp.put(tp, new RigaDettaglioComponenteTabellaImportiCapitolo());
				}
				RigaDettaglioComponenteTabellaImportiCapitolo riga = mp.get(tp);
				riga.setTipoDettaglioComponenteImportiCapitolo(tp);
				
				
				BeanWrapper bwic = PropertyAccessorFactory.forBeanPropertyAccess(riga);
				bwic.setPropertyValue(periodo.getFieldNameRiga(), dt.getImporto());
				
			}
			
		}
		//probabilmente si puo' migliorare
		List<RigaDettaglioComponenteTabellaImportiCapitolo> listResult = new ArrayList<RigaDettaglioComponenteTabellaImportiCapitolo>();
		
		List<RigaDettaglioComponenteTabellaImportiCapitolo> tmp = new ArrayList<RigaDettaglioComponenteTabellaImportiCapitolo>();
		RigaDettaglioComponenteTabellaImportiCapitolo rigaDettagliostanziamento = new RigaDettaglioComponenteTabellaImportiCapitolo();;
		for (RigaDettaglioComponenteTabellaImportiCapitolo rd : mp.values()) {
			if(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO.equals(rd.getTipoDettaglioComponenteImportiCapitolo())) {
				rigaDettagliostanziamento = rd;
			}else {
				tmp.add(rd);
			}
			
		}
		//voglio che lo stanziamento sia il primo, sempre.
		listResult.add(rigaDettagliostanziamento);
		listResult.addAll(tmp);
		// ne avro' 3 ognuna con due cosi
		return listResult;
	}
	
	private static void popolaMappaWrapper(Map<Integer, List<WrapperComponenteImportoCapitoloAnno>> mappa,
			List<ComponenteImportiCapitolo> listaComponentiAnnoMenoUno, PeriodoTabellaComponentiImportiCapitolo periodoTabellaComponentiImportiCapitolo) {
		
		for (ComponenteImportiCapitolo cic : listaComponentiAnnoMenoUno) {
			WrapperComponenteImportoCapitoloAnno wrapper = new WrapperComponenteImportoCapitoloAnno();
			wrapper.setComponenteImportiCapitolo(cic);
			wrapper.setPeriodoTabellaComponentiImportiCapitolo(periodoTabellaComponentiImportiCapitolo);
//			wrapper.setAnnoCompetenza(anniPrecedenti);
			Integer uidTipoComponente = Integer.valueOf(cic.getTipoComponenteImportiCapitolo().getUid());
			if(mappa.get(uidTipoComponente) == null) {
				mappa.put(uidTipoComponente, new ArrayList<WrapperComponenteImportoCapitoloAnno>());
			}
			mappa.get(uidTipoComponente).add(wrapper);
		}
	}
}
