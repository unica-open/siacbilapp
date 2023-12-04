/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.previmpacc;

import java.util.Set;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaPrevisioneImpegnatoAccertato;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStanziamentiCapitoloGestione;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.PrevisioneImpegnatoAccertato;


/**
 * The Class RisultatiRicercaPrevisioneImpegnatoAccertatoModel.
 *
 * @author elisa
 * @version 1.0.0 15 ott 2021
 */
public abstract class RisultatiRicercaPrevisioneImpegnatoAccertatoModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8376657205464880807L;

	//SPESA
	private ImportiCapitolo importiCapitolo0;
	private ImportiCapitolo importiCapitolo1;
	private ImportiCapitolo importiCapitolo2;
	private PrevisioneImpegnatoAccertato previsioneImpegnatoAccertato; 
	
	
	private int savedDisplayStart;
	
	// Per le azioni da delegare all'esterno
	private int uidCapitolo;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaPrevisioneImpegnatoAccertatoModel() {
		super();
		setTitolo("Risultati di ricerca previsione impegnato accertato");
	}


	/* Getter e Setter */
	/**
	 * @return the savedDisplayStart
	 */
	public int getSavedDisplayStart() {
		return savedDisplayStart;
	}


	/**
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(int savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}


	public int getUidCapitolo() {
		return uidCapitolo;
	}


	public void setUidCapitolo(int uidCapitolo) {
		this.uidCapitolo = uidCapitolo;
	}
	
	

	public ImportiCapitolo getImportiCapitolo0() {
		return importiCapitolo0;
	}


	public void setImportiCapitolo0(ImportiCapitolo importiCapitolo0) {
		this.importiCapitolo0 = importiCapitolo0;
	}


	public ImportiCapitolo getImportiCapitolo1() {
		return importiCapitolo1;
	}


	public void setImportiCapitolo1(ImportiCapitolo importiCapitolo1) {
		this.importiCapitolo1 = importiCapitolo1;
	}


	public ImportiCapitolo getImportiCapitolo2() {
		return importiCapitolo2;
	}


	public void setImportiCapitolo2(ImportiCapitolo importiCapitolo2) {
		this.importiCapitolo2 = importiCapitolo2;
	}


	public PrevisioneImpegnatoAccertato getPrevisioneImpegnatoAccertato() {
		return previsioneImpegnatoAccertato;
	}
	
	public void setPrevisioneImpegnatoAccertato(PrevisioneImpegnatoAccertato previsioneImpegnatoAccertato) {
		this.previsioneImpegnatoAccertato = previsioneImpegnatoAccertato;
	}

	
	public abstract String getDescrizioneImportoDerivato();
	public abstract String getImportoDerivatoAnno0();
	public abstract String getImportoDerivatoAnno1();
	public abstract String getImportoDerivatoAnno2();

	public RicercaStanziamentiCapitoloGestione creaRequestRicercaStanziamentiCapitolo() {
		RicercaStanziamentiCapitoloGestione request = creaRequest(RicercaStanziamentiCapitoloGestione.class);
		Set<ImportiCapitoloEnum> importiDerivati = caricaImportiDerivatiRichiesti();
		request.setUidCapitolo(getUidCapitolo());
		request.setImportiDerivatiRichiesti(importiDerivati);
		return request;
	}


	protected abstract  Set<ImportiCapitoloEnum> caricaImportiDerivatiRichiesti() ;


	public AggiornaPrevisioneImpegnatoAccertato creaRequestAggiornaPrevisioneImpegnatoAccertato() {
		AggiornaPrevisioneImpegnatoAccertato req = creaRequest(AggiornaPrevisioneImpegnatoAccertato.class);
		req.setPrevisioneImpegnatoAccertatoSuCapitolo(previsioneImpegnatoAccertato);
		return req;
	}

	public boolean isPrevisioneEditabile() {
		return true;
	}

}
