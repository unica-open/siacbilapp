/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilser.model.CategoriaCapitoloEnum;
import it.csi.siac.siacbilser.model.ImportiCapitoloUG;
import it.csi.siac.siacbilser.model.ImportiCapitoloUP;
import it.csi.siac.siacbilser.model.TipoCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;


/**
 * The Class TabellaImportiConComponentiCapitoloFactory.
 * 
 * @author elisa
 * 
 * @version 1.0.0
 */
public class TabellaImportiConComponentiCapitoloFactory extends BaseTabellaImportiConComponentiCapitoloFactory implements Serializable {
	
	/**Per la serializzazione */
	private static final long serialVersionUID = 6673913266720127844L;
	
	private List<RigaImportoTabellaImportiCapitolo> righeImportoTabellaElaborate;
	
	public List<RigaImportoTabellaImportiCapitolo> getRigheImportoTabellaElaborate() {
		return righeImportoTabellaElaborate;
	}
	
	public List<RigaComponenteTabellaImportiCapitolo> getRigheComponentiElaborate() {
		return componentiFactory.getRigheComponentiElaborate();
		
	} 
	

//	public void elaboraRigheComponentiTabellaDisponibilitaVariareImpegnareComponenti() {
//		componentiFactory.elaboraRigheComponentiTabellaDisponibilitaVariareImpegnareComponenti();
//	}

	public void elaboraRigheTabella(){
		 elaboraRigheImporti(false);
		 componentiFactory.elaboraRigheComponentiTabella();
	}
	
	public void elaboraRigheTabellaConComponentiDefault(){
		 elaboraRigheImporti(false);
		 componentiFactory.elaboraRigheComponentiTabellaConComponentiDefault();
	}
	
	public void elaboraRigheConImportoIniziale(){
		 elaboraRigheImporti(true);
		 componentiFactory.elaboraRigheComponentiTabella();
		 
	}
	
	public void elaboraRigheConImportoInizialeEDisponibilita(){
		elaboraRigheConImportoIniziale();
		componentiFactory.elaboraRigheComponentiTabellaDisponibilitaVariareImpegnareComponenti();
		 
	}
	public boolean presenteAlmenoUnImportoNegativo() {
		if(this.righeImportoTabellaElaborate == null) {
			return false;
		}
		for (RigaImportoTabellaImportiCapitolo elabRiga : this.righeImportoTabellaElaborate) {
			for (RigaDettaglioImportoTabellaImportiCapitolo elabRigaDettaglio : elabRiga.getSottoRighe()) {
				if(elabRigaDettaglio.isStanziamento() && elabRigaDettaglio.isAlmenoUnImportoTriennioMinoreDiZero()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean esistonoComponentiNonFrescoCollegateAlCapitolo() {
		return componentiFactory.isEsistonoComponentiNonFrescoSulCapitolo();
	}

	private void elaboraRigheImporti(boolean elaboraImportoIniziale) {
		
		righeImportoTabellaElaborate = new ArrayList<RigaImportoTabellaImportiCapitolo>();
		 //calcolo la riga "Competenza" della tabella (importi relativi a STA)
		calcolaRigaImportoCompetenza(elaboraImportoIniziale);
		 //calcolo la riga "Residuo" della tabella (importi relativi a STR)
		calcolaRigaImportoResiduo(elaboraImportoIniziale);
//		//calcolo la riga "Residuo" della tabella (importi relativi a SCA)
		calcolaRigaImportoCassa(elaboraImportoIniziale);
	}
	
	/**
	 * Calcola la riga con titolo "Competenza" per un capitolo di uscita previsione.
	 *
	 * @return the riga importo tabella importi capitolo
	 */
	private void calcolaRigaImportoCompetenza(boolean popolaImportoIniziale) {
		RigaImportoTabellaImportiCapitolo rigaCompetenza = new RigaImportoTabellaImportiCapitolo();
		rigaCompetenza.setTipoImportiCapitoloTabella(TipologiaImportoTabellaImportoCapitolo.COMPETENZA);
        
		RigaDettaglioImportoTabellaImportiCapitolo rigaDettaglioCompetenzaStanziamento = popolaImportiStanziamentoRigaCompetenza(popolaImportoIniziale);
		
		RigaDettaglioImportoTabellaImportiCapitolo rigaDettaglioCompetenzaImpegnato = popolaImportiImpegnatoRigaCompetenza(popolaImportoIniziale);
		
		rigaCompetenza.addSottoRiga(rigaDettaglioCompetenzaStanziamento);
		rigaCompetenza.addSottoRiga(rigaDettaglioCompetenzaImpegnato);
		
		if(this.tipoCapitolo != null && TipoCapitolo.isTipoCapitoloPrevisione(this.tipoCapitolo)) {
			RigaDettaglioImportoTabellaImportiCapitolo rigaDettaglioCompetenzaDispImpegnare = popolaImportiDisponibilitaRigaCompetenza(popolaImportoIniziale);
			rigaCompetenza.addSottoRiga(rigaDettaglioCompetenzaDispImpegnare);
		}
		
		this.righeImportoTabellaElaborate.add(rigaCompetenza);
		
	}
	
	private void calcolaRigaImportoResiduo(boolean popolaImportoIniziale) {
		
		RigaImportoTabellaImportiCapitolo rigaResiduo = new RigaImportoTabellaImportiCapitolo();
		rigaResiduo.setTipoImportiCapitoloTabella(TipologiaImportoTabellaImportoCapitolo.RESIDUO);

		RigaDettaglioImportoTabellaImportiCapitolo rigaImportoResiduoPresunto = popolaImportiPresuntiRigaResiduo(popolaImportoIniziale);
		
		RigaDettaglioImportoTabellaImportiCapitolo rigaImportoResiduoEffettivo = popolaImportiEffettiviRigaResiduo(popolaImportoIniziale);
		
		rigaResiduo.addSottoRiga(rigaImportoResiduoPresunto);
		rigaResiduo.addSottoRiga(rigaImportoResiduoEffettivo);
		this.righeImportoTabellaElaborate.add(rigaResiduo);
	}
	
	private void calcolaRigaImportoCassa(boolean popolaImportoIniziale) {
		
		RigaImportoTabellaImportiCapitolo rigaCassa = new RigaImportoTabellaImportiCapitolo();
		rigaCassa.setTipoImportiCapitoloTabella(TipologiaImportoTabellaImportoCapitolo.CASSA);
		
		RigaDettaglioImportoTabellaImportiCapitolo rigaImportoCassaStanziamento = null;
		
		//task-236 uscita previsione & FPV / DAM rigaImportoCassaStanziamento = 0
		if(this.tipoCapitolo != null && TipoCapitolo.isTipoCapitoloPrevisione(this.tipoCapitolo) &&
				this.categoriaCapitolo != null && (CategoriaCapitoloEnum.FPV.getCodice().equals(this.categoriaCapitolo.getCodice()) || CategoriaCapitoloEnum.DAM.getCodice().equals(this.categoriaCapitolo.getCodice()))) {
			rigaImportoCassaStanziamento = new RigaDettaglioImportoTabellaImportiCapitolo();
			rigaImportoCassaStanziamento.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
			rigaImportoCassaStanziamento.setImportoAnno0(new BigDecimal(0));
		} else {
			rigaImportoCassaStanziamento = popolaImportiStanziamentoRigaCassa(popolaImportoIniziale);
		}
		
		RigaDettaglioImportoTabellaImportiCapitolo rigaImportoCassaPagato = popolaImportiPagatoRigaCassa(popolaImportoIniziale);
		
		rigaCassa.addSottoRiga(rigaImportoCassaStanziamento);
		rigaCassa.addSottoRiga(rigaImportoCassaPagato);
		this.righeImportoTabellaElaborate.add(rigaCassa);
	}


	private RigaDettaglioImportoTabellaImportiCapitolo popolaImportiStanziamentoRigaCompetenza(boolean popolaImportoIniziale) {
		RigaDettaglioImportoTabellaImportiCapitolo rigaDettaglioCompetenzaStanziamento = new RigaDettaglioImportoTabellaImportiCapitolo();
		rigaDettaglioCompetenzaStanziamento.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		
		//stanziamento di tipo 'STA' per anno N -1 del capitolo collegato attraverso siac_r_bil_elem_rel_tempo, oppure il capitolo di gestione di annoBilancio-1 equivalente 
		//con (elem_code,elem_code2,elem_code3 uguali a quello del capitolo di previsione )
		rigaDettaglioCompetenzaStanziamento.setImportoAnniPrecedenti(importiCapitoloAnnoMenoUno != null? importiCapitoloAnnoMenoUno.getStanziamento() : BigDecimal.ZERO);
		//stanziamento di tipo 'STA' per anno N del capitolo
		rigaDettaglioCompetenzaStanziamento.setImportoAnno0(importiCapitoloAnnoBilancio.getStanziamento());
		//stanziamento di tipo 'STA' per anno N + 1  del capitolo
		rigaDettaglioCompetenzaStanziamento.setImportoAnno1(importiCapitoloAnnoBilancioPiuUno.getStanziamento());
		//stanziamento di tipo 'STA' per anno N + 2  del capitolo
		rigaDettaglioCompetenzaStanziamento.setImportoAnno2(importiCapitoloAnnoBilancioPiuDue.getStanziamento());
		//SIAC-8461 per la riga della competenza il concetto di residuo non esiste a livello contabile
		rigaDettaglioCompetenzaStanziamento.setImportoResiduoAnno0(null);
		//TODO:  da verificare 
		rigaDettaglioCompetenzaStanziamento.setImportoAnniSuccessivi(importiCapitoloAnniSuccessivi!= null ? importiCapitoloAnniSuccessivi.getStanziamento() : BigDecimal.ZERO);
		
		rigaDettaglioCompetenzaStanziamento.aggiungiPeriodiImportoSenzaSignificatoContabile(PERIODI_COMPETENZA_STANZIAMENTO_FINALE_SENZA_SIGNIFICATO_CONTABILE);
		
		if(!popolaImportoIniziale) {
			return rigaDettaglioCompetenzaStanziamento;
		}
		
		//stanziamento di tipo 'STI' per anno N -1 del capitolo collegato attraverso siac_r_bil_elem_rel_tempo, oppure il capitolo di gestione di annoBilancio-1 equivalente 
		//con (elem_code,elem_code2,elem_code3 uguali a quello del capitolo di previsione )
		rigaDettaglioCompetenzaStanziamento.setImportoInizialeAnniPrecedenti(importiCapitoloAnnoMenoUno != null? importiCapitoloAnnoMenoUno.getStanziamentoIniziale() : BigDecimal.ZERO);
		//stanziamento di tipo 'STI' per anno N del capitolo
		rigaDettaglioCompetenzaStanziamento.setImportoInizialeAnno0(importiCapitoloAnnoBilancio.getStanziamentoIniziale());
		//stanziamento di tipo 'STI' per anno N + 1  del capitolo
		rigaDettaglioCompetenzaStanziamento.setImportoInizialeAnno1(importiCapitoloAnnoBilancioPiuUno.getStanziamentoIniziale());
		//stanziamento di tipo 'STI' per anno N + 2  del capitolo
		rigaDettaglioCompetenzaStanziamento.setImportoInizialeAnno2(importiCapitoloAnnoBilancioPiuDue.getStanziamentoIniziale());
		//SIAC-8461 per la riga della competenza il concetto di residuo non esiste a livello contabile
		rigaDettaglioCompetenzaStanziamento.setImportoResiduoInizialeAnno0(null);
		//TODO:  da verificare
		rigaDettaglioCompetenzaStanziamento.setImportoInizialeAnniSuccessivi(importiCapitoloAnniSuccessivi!= null ? importiCapitoloAnniSuccessivi.getStanziamentoIniziale() : BigDecimal.ZERO);
		
		rigaDettaglioCompetenzaStanziamento.aggiungiPeriodiImportoInizialeSenzaSignificatoContabile(PERIODI_COMPETENZA_STANZIAMENTO_INIZIALE_SENZA_SIGNIFICATO_CONTABILE);
		
		return rigaDettaglioCompetenzaStanziamento;
	}
	


	private RigaDettaglioImportoTabellaImportiCapitolo popolaImportiImpegnatoRigaCompetenza(boolean popolaImportoIniziale) {
		RigaDettaglioImportoTabellaImportiCapitolo rigaDettaglioCompetenzaImpegnato = new RigaDettaglioImportoTabellaImportiCapitolo();
		rigaDettaglioCompetenzaImpegnato.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.IMPEGNATO);
		//TODO:  da verificare
		rigaDettaglioCompetenzaImpegnato.setImportoAnniPrecedenti(estraiDiCuiImpegnatoAnniPrec());
		// sommatoria dell'impegnato finale di tutte le componenti del capitolo (fnc_siac_impegnatodefinitivoup_comp) e del capitolo di gestione euivalente ((fnc_siac_impegnatodefinitivoug_comp)
		//ma fatto a livello di capitolo
		if(importiCapitoloAnnoBilancio instanceof ImportiCapitoloUP) {
			//SIAC-8469
			rigaDettaglioCompetenzaImpegnato.setImportoResiduoAnno0(((ImportiCapitoloUP)importiCapitoloAnnoBilancio).getResiduoEffettivoFinaleUP());
			
		}
		// sommatoria importo attuale degli impegni di competenza (movgest_tipo_code='I', movgest_ts_tipo_code='T', movgest_anno=annoBilancio, movgest_ts_det_tipo_code='A' ) non annullati
		// sul capitolo di gestione equivalente ( elem_code,elem_code2,elem_code3 uguali ) dello stesso annoBilancio in esercizio provvisorio, annoBilancio=annoBilancio-1 in previsione
		rigaDettaglioCompetenzaImpegnato.setImportoAnno0(estraiDiCuiImpegnatoAnno1());
		// sommatoria importo attuale degli impegni di anno2 (movgest_tipo_code='I', movgest_ts_tipo_code='T', movgest_anno=annoBilancio + 1 , movgest_ts_det_tipo_code='A' ) non annullati
		// sul capitolo di gestione equivalente ( elem_code,elem_code2,elem_code3 uguali ) dello stesso annoBilancio in esercizio provvisorio, annoBilancio=annoBilancio-1 in previsione
		rigaDettaglioCompetenzaImpegnato.setImportoAnno1(estraiDiCuiImpegnatoAnno2());
		// sommatoria importo attuale degli impegni di anno3 (movgest_tipo_code='I', movgest_ts_tipo_code='T', movgest_anno=annoBilancio + 2 , movgest_ts_det_tipo_code='A' ) non annullati
		// sul capitolo di gestione equivalente ( elem_code,elem_code2,elem_code3 uguali ) dello stesso annoBilancio in esercizio provvisorio, annoBilancio=annoBilancio-1 in previsione
		rigaDettaglioCompetenzaImpegnato.setImportoAnno2(estraiDiCuiImpegnatoAnno3());
		// sommatoria importo attuale degli impegni di anni successivi(movgest_tipo_code='I', movgest_ts_tipo_code='T', movgest_anno>annoBilancio + 2 , movgest_ts_det_tipo_code='A' ) non annullati
		// sul capitolo di gestione equivalente ( elem_code,elem_code2,elem_code3 uguali ) dello stesso annoBilancio in esercizio provvisorio, annoBilancio=annoBilancio-1 in previsione
		rigaDettaglioCompetenzaImpegnato.setImportoAnniSuccessivi(estraiDiCuiImpegnatoAnniSucc());
		
		//se il capitolo e' di gestione
		if(this.tipoCapitolo != null && TipoCapitolo.isTipoCapitoloGestione(this.tipoCapitolo)) {
			rigaDettaglioCompetenzaImpegnato.aggiungiPeriodiImportoSenzaSignificatoContabile(PERIODI_COMPETENZA_IMPEGNATO_FINALE_UG_SENZA_SIGNIFICATO_CONTABILE);
		}else {
			rigaDettaglioCompetenzaImpegnato.aggiungiPeriodiImportoSenzaSignificatoContabile(PERIODI_COMPETENZA_IMPEGNATO_FINALE_UP_SENZA_SIGNIFICATO_CONTABILE);
		}
		
		if(!popolaImportoIniziale) {
			return rigaDettaglioCompetenzaImpegnato;
		}
		//l'impegnato non presenta importi iniziali
		rigaDettaglioCompetenzaImpegnato.aggiungiPeriodiImportoInizialeSenzaSignificatoContabile(PERIODI_COMPETENZA_IMPEGNATO_INIZIALE_SENZA_SIGNIFICATO_CONTABILE);

		
		return rigaDettaglioCompetenzaImpegnato;
	}

	
	private RigaDettaglioImportoTabellaImportiCapitolo popolaImportiDisponibilitaRigaCompetenza(boolean popolaImportoIniziale) {
		RigaDettaglioImportoTabellaImportiCapitolo rigaDettaglioCompetenzaDispImpegnare = new RigaDettaglioImportoTabellaImportiCapitolo();
		rigaDettaglioCompetenzaDispImpegnare.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.DISPONIBILITAIMPEGNARE);
		// disponibilita' impegnare annoCompentenza=annoBilancio calcolata rispetto al di gestione equivalente (elem_code,elem_code2,elem_code3 uguali ) 
		// dello stesso annoBilancio in esercizio provvisorio, annoBilancio=annoBilancio-1 in previsione 
		// (calcolo effettuato attraverso function come stanziamento effettivo annoCompetenza=annoBilancio del capitolo di previsione in annoBilancio - totale impegnato calcolato sopra)
		rigaDettaglioCompetenzaDispImpegnare.setImportoAnno0(estraiDiponibilitaImpegnareAnno1());
		// disponibilita' impegnare annoCompentenza=annoBilancio +1 calcolata rispetto al di gestione equivalente (elem_code,elem_code2,elem_code3 uguali ) 
		// dello stesso annoBilancio in esercizio provvisorio, annoBilancio=annoBilancio-1 in previsione 
		// (calcolo effettuato attraverso function come stanziamento effettivo annoCompetenza=annoBilancio +1 del capitolo di previsione in annoBilancio - totale impegnato calcolato sopra)
		rigaDettaglioCompetenzaDispImpegnare.setImportoAnno1(estraiDiponibilitaImpegnareAnno2());
		// disponibilita' impegnare annoCompentenza=annoBilancio +2 calcolata rispetto al di gestione equivalente (elem_code,elem_code2,elem_code3 uguali ) 
		// dello stesso annoBilancio in esercizio provvisorio, annoBilancio=annoBilancio-1 in previsione 
		// (calcolo effettuato attraverso function come stanziamento effettivo annoCompetenza=annoBilancio +2 del capitolo di previsione in annoBilancio - totale impegnato calcolato sopra)
		rigaDettaglioCompetenzaDispImpegnare.setImportoAnno2(estraiDiponibilitaImpegnareAnno3());
		
		rigaDettaglioCompetenzaDispImpegnare.aggiungiPeriodiImportoSenzaSignificatoContabile(PERIODI_COMPETENZA_DISP_IMPEGNARE_FINALE_SENZA_SIGNIFICATO_CONTABILE);
		if(!popolaImportoIniziale) {
			return rigaDettaglioCompetenzaDispImpegnare;
		}
		//la disponibilita non presenta importi inziali
		rigaDettaglioCompetenzaDispImpegnare.aggiungiPeriodiImportoInizialeSenzaSignificatoContabile(PERIODI_COMPETENZA_DISP_IMPEGNARE_INIZIALE_SENZA_SIGNIFICATO_CONTABILE);
		return rigaDettaglioCompetenzaDispImpegnare;
	}
	
	
	private RigaDettaglioImportoTabellaImportiCapitolo popolaImportiPresuntiRigaResiduo(boolean popolaImportoIniziale) {
		
		RigaDettaglioImportoTabellaImportiCapitolo rigaImportoResiduoPresunto = new RigaDettaglioImportoTabellaImportiCapitolo();
		rigaImportoResiduoPresunto.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.PRESUNTO);
		
		//SIAC-8464: stanziamento di tipo 'STR' per anno N del capitolo
		if(importiCapitoloAnnoMenoUno != null) {
			rigaImportoResiduoPresunto.setImportoAnniPrecedenti(importiCapitoloAnnoMenoUno.getStanziamentoResiduo());
		}
		//stanziamento di tipo 'STR' per anno N del capitolo
		rigaImportoResiduoPresunto.setImportoResiduoAnno0(importiCapitoloAnnoBilancio.getStanziamentoResiduo());
		
		//stanziamento di tipo 'STR' per anno N +1 del capitolo
		rigaImportoResiduoPresunto.setImportoAnno1(importiCapitoloAnnoBilancioPiuUno.getStanziamentoResiduo());
		
		//stanziamento di tipo 'STR' per anno N +2 del capitolo
		rigaImportoResiduoPresunto.setImportoAnno2(importiCapitoloAnnoBilancioPiuDue.getStanziamentoResiduo());
		
		//stanziamento di tipo 'STR' per anno N> anno bilancio +2 del capitolo
		rigaImportoResiduoPresunto.setImportoAnniSuccessivi(importiCapitoloAnniSuccessivi.getStanziamentoResiduo());
		
		rigaImportoResiduoPresunto.aggiungiPeriodiImportoSenzaSignificatoContabile(PERIODI_RESIDUO_PRESUNTO_FINALE_SENZA_SIGNIFICATO_CONTABILE);
		
		if(!popolaImportoIniziale) {
			return rigaImportoResiduoPresunto; 
		}
		
		
		//stanziamento di tipo 'STR' per anno N> anno bilancio +2 del capitolo
		rigaImportoResiduoPresunto.setImportoInizialeAnniPrecedenti(importiCapitoloAnnoMenoUno != null? importiCapitoloAnnoMenoUno.getStanziamentoResiduoIniziale() : BigDecimal.ZERO );
		//stanziamento di tipo 'STR' per anno N del capitolo
		rigaImportoResiduoPresunto.setImportoResiduoInizialeAnno0(importiCapitoloAnnoBilancio.getStanziamentoResiduoIniziale());
		
		//stanziamento di tipo 'STR' per anno N +1 del capitolo, lo popolo comunque, anche se e' segnato come senza significato contabile, perche' ce l'ho
		rigaImportoResiduoPresunto.setImportoInizialeAnno1(importiCapitoloAnnoBilancioPiuUno.getStanziamentoResiduoIniziale());
				
		//stanziamento di tipo 'STR' per anno N +2 del capitolo, lo popolo comunque, anche se e' segnato come senza significato contabile, perche' ce l'ho
		rigaImportoResiduoPresunto.setImportoInizialeAnno2(importiCapitoloAnnoBilancioPiuDue.getStanziamentoResiduoIniziale());
		
		rigaImportoResiduoPresunto.aggiungiPeriodiImportoInizialeSenzaSignificatoContabile(PERIODI_RESIDUO_PRESUNTO_INIZIALE_SENZA_SIGNIFICATO_CONTABILE);
		
		return rigaImportoResiduoPresunto;
	}
	
	private RigaDettaglioImportoTabellaImportiCapitolo popolaImportiEffettiviRigaResiduo(boolean popolaImportoIniziale) {
		
		boolean isCapitoloGestione = this.tipoCapitolo != null && TipoCapitolo.isTipoCapitoloGestione(this.tipoCapitolo);
		boolean isCapitoloPrevisione = this.tipoCapitolo != null && TipoCapitolo.isTipoCapitoloPrevisione(this.tipoCapitolo);
		boolean isImportiCapitoloAnnoMenoUnoGestione = importiCapitoloAnnoMenoUno != null && importiCapitoloAnnoMenoUno instanceof ImportiCapitoloUG;
		boolean isImportiCapitoloAnnoBilancioGestione = importiCapitoloAnnoBilancio instanceof ImportiCapitoloUG;
		boolean isImportiCapitoloAnnoBilancioPrevisione = !isImportiCapitoloAnnoBilancioGestione && importiCapitoloAnnoBilancio instanceof ImportiCapitoloUP;
		
		RigaDettaglioImportoTabellaImportiCapitolo rigaImportoResiduoEffettivo = new RigaDettaglioImportoTabellaImportiCapitolo();
		rigaImportoResiduoEffettivo.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.EFFETTIVO);
		//stanziamento di tipo 'STR' per anno N -1 del capitolo collegato attraverso siac_r_bil_elem_rel_tempo, oppure il capitolo di gestione di annoBilancio-1 equivalente 
		//con (elem_code,elem_code2,elem_code3 uguali a quello del capitolo di previsione )
		//SIAC-8464: e' stato spostato questo valore in altra cella (quella residui presunti), ma non si toglie per ora in quanto verra' poi modificato tutto il calcolo sulla riga
//		rigaImportoResiduoEffettivo.setImportoAnniPrecedenti(importiCapitoloAnnoMenoUno.getStanziamentoResiduo());
		
		if( isImportiCapitoloAnnoMenoUnoGestione) {
			
			BigDecimal importoResiduoEffettivoFinaleAnnoPrec = isCapitoloPrevisione?
					((ImportiCapitoloUG)importiCapitoloAnnoMenoUno).getResiduoEffettivoFinaleUG()
					: ((ImportiCapitoloUG)importiCapitoloAnnoBilancio).getResiduoEffettivoFinaleUGAnnoPrec();
			//SIAC-8469
			rigaImportoResiduoEffettivo.setImportoAnniPrecedenti(importoResiduoEffettivoFinaleAnnoPrec);
			
		}
		// sommatoria dell'impegnato finale di tutte le componenti del capitolo (fnc_siac_impegnatodefinitivoup_comp) e del capitolo di gestione euivalente ((fnc_siac_impegnatodefinitivoug_comp)
		
		if(isImportiCapitoloAnnoBilancioGestione) {
			//SIAC-8469
			rigaImportoResiduoEffettivo.setImportoResiduoAnno0(((ImportiCapitoloUG)importiCapitoloAnnoBilancio).getResiduoEffettivoFinaleUG());
		}else if(isImportiCapitoloAnnoBilancioPrevisione) {
			//SIAC-8469
			rigaImportoResiduoEffettivo.setImportoResiduoAnno0(((ImportiCapitoloUP)importiCapitoloAnnoBilancio).getResiduoEffettivoFinaleUP());
			
		}
		
		
		rigaImportoResiduoEffettivo.aggiungiPeriodiImportoSenzaSignificatoContabile(PERIODI_RESIDUO_EFFETTIVO_FINALE_SENZA_SIGNIFICATO_CONTABILE);
		
		if(!popolaImportoIniziale) {
			return rigaImportoResiduoEffettivo;
		}
		//SIAC-8469
		if(isCapitoloGestione && isImportiCapitoloAnnoBilancioGestione) {
			//SIAC-8469
			rigaImportoResiduoEffettivo.setImportoResiduoInizialeAnno0(((ImportiCapitoloUG)importiCapitoloAnnoBilancio).getResiduoEffettivoInizialeUG());
			//SIAC-8469
			rigaImportoResiduoEffettivo.setImportoInizialeAnniPrecedenti(((ImportiCapitoloUG)importiCapitoloAnnoBilancio).getResiduoEffettivoInizialeUGAnnoPrec());
		}
		
		if(isCapitoloPrevisione && isImportiCapitoloAnnoMenoUnoGestione) {
			//SIAC-8469
			rigaImportoResiduoEffettivo.setImportoInizialeAnniPrecedenti(((ImportiCapitoloUG)importiCapitoloAnnoMenoUno).getResiduoEffettivoInizialeUG());
			
		}
		
		rigaImportoResiduoEffettivo.aggiungiPeriodiImportoInizialeSenzaSignificatoContabile(isCapitoloGestione? 
				PERIODI_RESIDUO_EFFETTIVO_INIZIALE_UG_SENZA_SIGNIFICATO_CONTABILE
				: PERIODI_RESIDUO_EFFETTIVO_INIZIALE_UP_SENZA_SIGNIFICATO_CONTABILE
				);
		
		return rigaImportoResiduoEffettivo;
	}
	
	private RigaDettaglioImportoTabellaImportiCapitolo popolaImportiStanziamentoRigaCassa(boolean popolaImportiIniziali) {
		RigaDettaglioImportoTabellaImportiCapitolo rigaImportoCassaStanziamento = new RigaDettaglioImportoTabellaImportiCapitolo();
		
		rigaImportoCassaStanziamento.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.STANZIAMENTO);
		//SIAC-8464
		//stanziamento di tipo 'SCA' per anno N -1 del capitolo collegato attraverso siac_r_bil_elem_rel_tempo, oppure il capitolo di gestione di annoBilancio-1 equivalente 
		//con (elem_code,elem_code2,elem_code3 uguali a quello del capitolo di previsione )
		rigaImportoCassaStanziamento.setImportoAnniPrecedenti(importiCapitoloAnnoMenoUno != null? importiCapitoloAnnoMenoUno.getStanziamentoCassa() : BigDecimal.ZERO);
		//stanziamento di tipo 'SCA' per anno N del capitolo
		rigaImportoCassaStanziamento.setImportoAnno0(importiCapitoloAnnoBilancio.getStanziamentoCassa());
		rigaImportoCassaStanziamento.aggiungiPeriodiImportoSenzaSignificatoContabile(PERIODI_CASSA_STANZIAMENTO_FINALE_SENZA_SIGNIFICATO_CONTABILE);
		
		if(!popolaImportiIniziali) {
			return rigaImportoCassaStanziamento;
		}
		//stanziamento di tipo 'SCA' per anno N -1 del capitolo collegato attraverso siac_r_bil_elem_rel_tempo, oppure il capitolo di gestione di annoBilancio-1 equivalente 
		//con (elem_code,elem_code2,elem_code3 uguali a quello del capitolo di previsione )
		rigaImportoCassaStanziamento.setImportoInizialeAnniPrecedenti(importiCapitoloAnnoMenoUno.getStanziamentoCassaIniziale());
		rigaImportoCassaStanziamento.setImportoInizialeAnno0(importiCapitoloAnnoBilancio.getStanziamentoCassaIniziale());
		rigaImportoCassaStanziamento.aggiungiPeriodiImportoInizialeSenzaSignificatoContabile(PERIODI_CASSA_STANZIAMENTO_INIZIALE_SENZA_SIGNIFICATO_CONTABILE);
		return rigaImportoCassaStanziamento;
	}


	private RigaDettaglioImportoTabellaImportiCapitolo popolaImportiPagatoRigaCassa(boolean popolaImportiRigaCassa) {
		RigaDettaglioImportoTabellaImportiCapitolo rigaImportoCassaPagato = new RigaDettaglioImportoTabellaImportiCapitolo();
		rigaImportoCassaPagato.setTipoDettaglioComponenteImportiCapitolo(TipoDettaglioComponenteImportiCapitolo.PAGATO);
		
		if(importiCapitoloAnnoMenoUno != null && importiCapitoloAnnoMenoUno instanceof ImportiCapitoloUG) {
			// sommatoria importo attuale degli ordinativi di pagamento non annullati sul capitolo di gestione equivalente ( elem_code,elem_code2,elem_code3 uguali ) 
			//dello stesso annoBilancio in esercizio provvisorio, annoBilancio=annoBilancio-1 in previsione
			rigaImportoCassaPagato.setImportoAnniPrecedenti(((ImportiCapitoloUG)importiCapitoloAnnoMenoUno).getTotalePagato());
		}
		//dall'excel, dovrebbe essere:
		//Totale pagamenti validi sul capitolo di gestione siac_t_ordinativo_ts_det.ord_ts_det_importo per ordinativi di pagamento non annullati con ord_tipo_code='P' 
//		su impegni residui movgest_anno<annoBilancio elem_id=<capitolo_id> del capitolo di gestione bil_id=siac_t_bil.bil_id (annoBilancio) 
		//MA ON RTROVO CORRISPONDENZE SUL CODICE
		//rigaImportoCassaPagato.setImportoResiduoAnno0(??? -> disponibilitaCapitoloUscitaGestioneResiduo.pagato);
		
		if(importiCapitoloAnnoBilancio instanceof ImportiCapitoloUG) {
			ImportiCapitoloUG importiCapitoloUG = (ImportiCapitoloUG)importiCapitoloAnnoBilancio;
			BigDecimal importoPagatoSuResidui = importiCapitoloUG.getTotalePagatoSuResidui() != null? importiCapitoloUG.getTotalePagatoSuResidui() : BigDecimal.ZERO;
			BigDecimal importoPagato = importiCapitoloUG.getTotalePagato() != null? importiCapitoloUG.getTotalePagato() : BigDecimal.ZERO;
			
			// sommatoria importo attuale degli ordinativi di pagamento non annullati collegati amovgest residui sul capitolo di gestione
			rigaImportoCassaPagato.setImportoResiduoAnno0(importoPagatoSuResidui);
			// sommatoria importo attuale degli ordinativi di pagamento non annullati collegati amovgest di competenza sul capitolo di gestione
			rigaImportoCassaPagato.setImportoAnno0(importoPagato.subtract(importoPagatoSuResidui));
		}
		//se il capitolo e' di gestione, SIAC-8469
		if(this.tipoCapitolo != null && TipoCapitolo.isTipoCapitoloGestione(this.tipoCapitolo)) {
			rigaImportoCassaPagato.aggiungiPeriodiImportoSenzaSignificatoContabile(PERIODI_CASSA_PAGATO_FINALE_UG_SENZA_SIGNIFICATO_CONTABILE);
		}else {
			rigaImportoCassaPagato.aggiungiPeriodiImportoSenzaSignificatoContabile(PERIODI_CASSA_PAGATO_FINALE_UP_SENZA_SIGNIFICATO_CONTABILE);
		}
		
		if(!popolaImportiRigaCassa) {
			return rigaImportoCassaPagato;
		}
		rigaImportoCassaPagato.aggiungiPeriodiImportoInizialeSenzaSignificatoContabile(PERIODI_CASSA_PAGATO_INIZIALE_SENZA_SIGNIFICATO_CONTABILE);
		return rigaImportoCassaPagato;
	}
	
	private BigDecimal estraiDiCuiImpegnatoAnniPrec() {
		if(importiCapitoloAnnoBilancio instanceof ImportiCapitoloUP ) {
			return ((ImportiCapitoloUP)importiCapitoloAnnoBilancio).getDiCuiImpegnatoAnnoPrec(); 
		}
		return importiCapitoloAnnoBilancio instanceof ImportiCapitoloUG ? ((ImportiCapitoloUG)importiCapitoloAnnoBilancio).getDiCuiImpegnatoUGAnnoPrec(): BigDecimal.ZERO;
	}
	
	private BigDecimal estraiDiCuiImpegnatoAnno1() {
		if(importiCapitoloAnnoBilancio instanceof ImportiCapitoloUP ) {
			return ((ImportiCapitoloUP)importiCapitoloAnnoBilancio).getImpegnatoEffettivoUPAnno1(); 
		}
		return importiCapitoloAnnoBilancio instanceof ImportiCapitoloUG ? ((ImportiCapitoloUG)importiCapitoloAnnoBilancio).getImpegnatoEffettivoUGAnno1(): BigDecimal.ZERO;
	}
	
	private BigDecimal estraiDiCuiImpegnatoAnno2() {
		if(importiCapitoloAnnoBilancio instanceof ImportiCapitoloUP ) {
			return ((ImportiCapitoloUP)importiCapitoloAnnoBilancio).getImpegnatoEffettivoUPAnno2(); 
		}
		return importiCapitoloAnnoBilancio instanceof ImportiCapitoloUG ? ((ImportiCapitoloUG)importiCapitoloAnnoBilancio).getImpegnatoEffettivoUGAnno2(): BigDecimal.ZERO;
	}
	
	private BigDecimal estraiDiCuiImpegnatoAnno3() {
		if(importiCapitoloAnnoBilancio instanceof ImportiCapitoloUP ) {
			return ((ImportiCapitoloUP)importiCapitoloAnnoBilancio).getImpegnatoEffettivoUPAnno3(); 
		}
		return importiCapitoloAnnoBilancio instanceof ImportiCapitoloUG ? ((ImportiCapitoloUG)importiCapitoloAnnoBilancio).getImpegnatoEffettivoUGAnno3(): BigDecimal.ZERO;
	}
	
	private BigDecimal estraiDiCuiImpegnatoAnniSucc() {
		if(importiCapitoloAnnoBilancio instanceof ImportiCapitoloUP ) {
			return ((ImportiCapitoloUP)importiCapitoloAnnoBilancio).getDiCuiImpegnatoAnniSucc(); 
		}
		return importiCapitoloAnnoBilancio instanceof ImportiCapitoloUG ? ((ImportiCapitoloUG)importiCapitoloAnnoBilancio).getDiCuiImpegnatoUGAnniSucc(): BigDecimal.ZERO;
	}
	
	
	
	private BigDecimal estraiDiponibilitaImpegnareAnno1() {
		if(importiCapitoloAnnoBilancio instanceof ImportiCapitoloUP ) {
			return ((ImportiCapitoloUP)importiCapitoloAnnoBilancio).getDisponibilitaImpegnareUPAnno1(); 
		}
		//per ora non si visualizza la disp ad impegnare per capitolo UG
//		return importiCapitoloAnnoBilancio instanceof ImportiCapitoloUG ? ((ImportiCapitoloUG)importiCapitoloAnnoBilancio).getDisponibilitaImpegnareAnno3(): BigDecimal.ZERO;
		return BigDecimal.ZERO;
	}
	
	private BigDecimal estraiDiponibilitaImpegnareAnno2() {
		if(importiCapitoloAnnoBilancio instanceof ImportiCapitoloUP ) {
			return ((ImportiCapitoloUP)importiCapitoloAnnoBilancio).getDisponibilitaImpegnareUPAnno2(); 
		}
		//per ora non si visualizza la disp ad impegnare per capitolo UG
//		return importiCapitoloAnnoBilancio instanceof ImportiCapitoloUG ? ((ImportiCapitoloUG)importiCapitoloAnnoBilancio).getDisponibilitaImpegnareAnno2(): BigDecimal.ZERO;
		return BigDecimal.ZERO;
	}
	
	private BigDecimal estraiDiponibilitaImpegnareAnno3() {
		if(importiCapitoloAnnoBilancio instanceof ImportiCapitoloUP ) {
			return ((ImportiCapitoloUP)importiCapitoloAnnoBilancio).getDisponibilitaImpegnareUPAnno3(); 
		}
		//per ora non si visualizza la disp ad impegnare per capitolo UG
//		return importiCapitoloAnnoBilancio instanceof ImportiCapitoloUG ? ((ImportiCapitoloUG)importiCapitoloAnnoBilancio).getDisponibilitaImpegnareAnno3(): BigDecimal.ZERO;
		return BigDecimal.ZERO;
	}
	

}



