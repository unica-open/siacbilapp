/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.fake;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.DettaglioComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.MacrotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.MomentoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.SottotipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;

public final class CapitoloUscitaPrevisioneFakeModel {

	public static List<ImportiCapitolo> populateModelToPresent() {

		List<ImportiCapitolo> listaImportiCapitolo = new ArrayList<ImportiCapitolo>();

		listaImportiCapitolo.add(initImportiCapitolo(Integer.valueOf(2019)));
		listaImportiCapitolo.add(initImportiCapitolo(Integer.valueOf(2020)));
		listaImportiCapitolo.add(initImportiCapitolo(Integer.valueOf(2021)));
		listaImportiCapitolo.add(initImportiCapitolo(Integer.valueOf(2022)));
		return listaImportiCapitolo;

	}

	public static List<BigDecimal> getStanziamentoCompetenza() {

		List<BigDecimal> stanzComp = new ArrayList<BigDecimal>();
		stanzComp.add(new BigDecimal(150));
		stanzComp.add(new BigDecimal(300));
		stanzComp.add(new BigDecimal(400));

		return stanzComp;

	}

	public static List<BigDecimal> getImpegnatoCompetenza() {

		List<BigDecimal> stanzComp = new ArrayList<BigDecimal>();
		stanzComp.add(new BigDecimal(500));
		stanzComp.add(new BigDecimal(555));
		stanzComp.add(new BigDecimal(590));

		return stanzComp;

	}
	
	public static List<BigDecimal> listaImporti() {

		List<BigDecimal> stanzComp = new ArrayList<BigDecimal>();
		stanzComp.add(new BigDecimal(1000));
		stanzComp.add(new BigDecimal(2000));
		stanzComp.add(new BigDecimal(3000));

		return stanzComp;

	}

	private static ImportiCapitoloUP initImportiCapitolo(Integer anno) {
		ImportiCapitoloUP icug = new ImportiCapitoloUP();
		icug.setAnnoCompetenza(anno);
		icug.setStanziamento(new BigDecimal(anno.toString()));
		icug.setStanziamentoResiduo(new BigDecimal(anno.toString()));
		icug.setStanziamentoCassa(new BigDecimal(anno.toString()));
		
		// Componente 1
		ComponenteImportiCapitolo cic = new ComponenteImportiCapitolo();
		icug.getListaComponenteImportiCapitolo().add(cic);
		
		TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo = new TipoComponenteImportiCapitolo();
		cic.setTipoComponenteImportiCapitolo(tipoComponenteImportiCapitolo);
		tipoComponenteImportiCapitolo.setCodice("01");
		tipoComponenteImportiCapitolo.setDescrizione("FRESCO I");
		tipoComponenteImportiCapitolo.setAnno(anno);
		tipoComponenteImportiCapitolo.setMacrotipoComponenteImportiCapitolo(MacrotipoComponenteImportiCapitolo.FRESCO);
		tipoComponenteImportiCapitolo.setSottotipoComponenteImportiCapitolo(SottotipoComponenteImportiCapitolo.APPLICATO);
		
		DettaglioComponenteImportiCapitolo dcic = new DettaglioComponenteImportiCapitolo();
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		dcic.setEditabile(true);
		dcic.setImporto(new BigDecimal(160));
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		
		dcic = new DettaglioComponenteImportiCapitolo();
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		dcic.setEditabile(false);
		dcic.setImporto(new BigDecimal(12));
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
		
		
		// Componente 2
		cic = new ComponenteImportiCapitolo();
		icug.getListaComponenteImportiCapitolo().add(cic);
		
		tipoComponenteImportiCapitolo = new TipoComponenteImportiCapitolo();
		cic.setTipoComponenteImportiCapitolo(tipoComponenteImportiCapitolo);
		tipoComponenteImportiCapitolo.setCodice("02");
		tipoComponenteImportiCapitolo.setDescrizione("FPV application #1");
		tipoComponenteImportiCapitolo.setAnno(anno);
		tipoComponenteImportiCapitolo.setMacrotipoComponenteImportiCapitolo(MacrotipoComponenteImportiCapitolo.FPV);
		tipoComponenteImportiCapitolo.setSottotipoComponenteImportiCapitolo(SottotipoComponenteImportiCapitolo.CUMULATO);
		tipoComponenteImportiCapitolo.setMomentoComponenteImportiCapitolo(MomentoComponenteImportiCapitolo.GESTIONE);
		
		dcic = new DettaglioComponenteImportiCapitolo();
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		dcic.setEditabile(true);
		dcic.setImporto(new BigDecimal(88));
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		
		dcic = new DettaglioComponenteImportiCapitolo();
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		dcic.setEditabile(false);
		dcic.setImporto(new BigDecimal(0));
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
		
		// Componente 3
		cic = new ComponenteImportiCapitolo();
		icug.getListaComponenteImportiCapitolo().add(cic);
		
		tipoComponenteImportiCapitolo = new TipoComponenteImportiCapitolo();
		cic.setTipoComponenteImportiCapitolo(tipoComponenteImportiCapitolo);
		tipoComponenteImportiCapitolo.setCodice("03");
		tipoComponenteImportiCapitolo.setDescrizione("FPV application #2");
		tipoComponenteImportiCapitolo.setAnno(anno);
		tipoComponenteImportiCapitolo.setMacrotipoComponenteImportiCapitolo(MacrotipoComponenteImportiCapitolo.FPV);
		tipoComponenteImportiCapitolo.setSottotipoComponenteImportiCapitolo(SottotipoComponenteImportiCapitolo.APPLICATO);
		
		dcic = new DettaglioComponenteImportiCapitolo();
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		dcic.setEditabile(true);
		dcic.setImporto(new BigDecimal(53));
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		
		dcic = new DettaglioComponenteImportiCapitolo();
		cic.getListaDettaglioComponenteImportiCapitolo().add(dcic);
		dcic.setEditabile(false);
		dcic.setImporto(new BigDecimal(0));
		dcic.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
		
		return icug;
	}
	
//	public boolean compareTwoImport(ImportiCapitoloUP obj1){
//		
//		List<ComponenteImportiCapitolo> lista1 = obj1.getListaComponenteImportiCapitolo();
//		
//		int i=0;
//		while(i<lista1.size()){
//			
//			
//			
//			
//			i+=1;
//		}
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		return true;
//	}

}
