/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;


/**
 * The Class RigaComponenteImportoCapitolo.
 * @
 */
public abstract class RigaDettaglioTabellaImportiCapitolo implements Serializable {

	/** Per la serializzazione  */
	private static final long serialVersionUID = 1790781558332746672L;
	
	private TipoDettaglioComponenteImportiCapitolo tipoDettaglioComponenteImportiCapitolo;
	
	private BigDecimal importoAnniPrecedenti = BigDecimal.ZERO;
	private BigDecimal importoAnno0 = BigDecimal.ZERO;
	private BigDecimal importoResiduoAnno0 = BigDecimal.ZERO;
	private BigDecimal importoAnno1 = BigDecimal.ZERO;
	private BigDecimal importoAnno2 = BigDecimal.ZERO;
	private BigDecimal importoAnniSuccessivi = BigDecimal.ZERO;
	
	//importi Iniziali
	private BigDecimal importoInizialeAnniPrecedenti = BigDecimal.ZERO;
	private BigDecimal importoInizialeAnno0 = BigDecimal.ZERO;
	private BigDecimal importoResiduoInizialeAnno0 = BigDecimal.ZERO;
	private BigDecimal importoInizialeAnno1 = BigDecimal.ZERO;
	private BigDecimal importoInizialeAnno2 = BigDecimal.ZERO;
	private BigDecimal importoInizialeAnniSuccessivi = BigDecimal.ZERO;
	
	private List<String> periodiImportoSenzaSignificatoContabile = new ArrayList<String>();
	private List<String> periodiImportoInizialeSenzaSignificatoContabile = new ArrayList<String>();
	
	public abstract boolean isTipoDettaglioEditabile();
	
	public abstract boolean isTipoDettaglioEliminabile();
	public abstract String getTrCssClass();
	
	public void aggiungiPeriodiImportoSenzaSignificatoContabile(PeriodoTabellaComponentiImportiCapitolo...periodiDaAggiungere) {
		if(periodiDaAggiungere == null) {
			return;
		}
		
		if(this.periodiImportoSenzaSignificatoContabile == null) {
			this.periodiImportoSenzaSignificatoContabile = new ArrayList<String>();
		}
		
		for (PeriodoTabellaComponentiImportiCapitolo daAggiungere : periodiDaAggiungere) {
			if(daAggiungere == null) {
				continue;
			}
			this.periodiImportoSenzaSignificatoContabile.add(daAggiungere.name());
		}
	}
	
	public void aggiungiPeriodiImportoSenzaSignificatoContabile(List<PeriodoTabellaComponentiImportiCapitolo> periodiDaAggiungere) {
		if(periodiDaAggiungere == null) {
			return;
		}
		
		if(this.periodiImportoSenzaSignificatoContabile == null) {
			this.periodiImportoSenzaSignificatoContabile = new ArrayList<String>();
		}
		
		for (PeriodoTabellaComponentiImportiCapitolo daAggiungere : periodiDaAggiungere) {
			if(daAggiungere == null) {
				continue;
			}
			this.periodiImportoSenzaSignificatoContabile.add(daAggiungere.name());
		}
		
	}
	
	public void aggiungiPeriodiImportoInizialeSenzaSignificatoContabile(List<PeriodoTabellaComponentiImportiCapitolo> periodiDaAggiungere) {
		if(periodiDaAggiungere == null) {
			return;
		}
		
		if(this.periodiImportoInizialeSenzaSignificatoContabile == null) {
			this.periodiImportoInizialeSenzaSignificatoContabile = new ArrayList<String>();
		}
		
		for (PeriodoTabellaComponentiImportiCapitolo daAggiungere : periodiDaAggiungere) {
			if(daAggiungere == null) {
				continue;
			}
			this.periodiImportoInizialeSenzaSignificatoContabile.add(daAggiungere.name());
		}
	}
	
	public void aggiungiPeriodiImportoInizialeSenzaSignificatoContabile(PeriodoTabellaComponentiImportiCapitolo...periodiDaAggiungere) {
		if(periodiDaAggiungere == null) {
			return;
		}
		
		if(this.periodiImportoInizialeSenzaSignificatoContabile == null) {
			this.periodiImportoInizialeSenzaSignificatoContabile = new ArrayList<String>();
		}
		
		for (PeriodoTabellaComponentiImportiCapitolo daAggiungere : periodiDaAggiungere) {
			if(daAggiungere == null) {
				continue;
			}
			this.periodiImportoInizialeSenzaSignificatoContabile.add(daAggiungere.name());
		}
	}
	
	//dovra' diventare abstract? suppongo di si, per ora lascio perdere
//	public String getTrClass() {
//		String cssClass="componentiCompetenzaRow";
//		if(isStanziamento()) {
//			cssClass += " previsione-default-stanziamento";
//		}
//		return cssClass;
//	}
	public BigDecimal getImportoAnniPrecedenti() {
		return importoAnniPrecedenti;
	}

	public void setImportoAnniPrecedenti(BigDecimal importoAnniPrecedenti) {
		this.importoAnniPrecedenti = importoAnniPrecedenti;
	}

	public BigDecimal getImportoAnno0() {
		return importoAnno0;
	}

	public void setImportoAnno0(BigDecimal importoAnno0) {
		this.importoAnno0 = importoAnno0;
	}

	
	public BigDecimal getImportoResiduoAnno0() {
		return importoResiduoAnno0;
	}

	public void setImportoResiduoAnno0(BigDecimal importoResiduoAnno0) {
		this.importoResiduoAnno0 = importoResiduoAnno0;
	}

	public BigDecimal getImportoAnno1() {
		return importoAnno1;
	}

	public void setImportoAnno1(BigDecimal importoAnno1) {
		this.importoAnno1 = importoAnno1;
	}

	public BigDecimal getImportoAnno2() {
		return importoAnno2;
	}

	public void setImportoAnno2(BigDecimal importoAnno2) {
		this.importoAnno2 = importoAnno2;
	}

	public BigDecimal getImportoAnniSuccessivi() {
		return importoAnniSuccessivi;
	}

	public void setImportoAnniSuccessivi(BigDecimal importoAnniSuccessivi) {
		this.importoAnniSuccessivi = importoAnniSuccessivi;
	}	
	
	public BigDecimal getImportoInizialeAnniPrecedenti() {
		return importoInizialeAnniPrecedenti;
	}

	public void setImportoInizialeAnniPrecedenti(BigDecimal importoInizialeAnniPrecedenti) {
		this.importoInizialeAnniPrecedenti = importoInizialeAnniPrecedenti;
	}

	public BigDecimal getImportoInizialeAnno0() {
		return importoInizialeAnno0;
	}

	public void setImportoInizialeAnno0(BigDecimal importoInizialeAnno0) {
		this.importoInizialeAnno0 = importoInizialeAnno0;
	}

	public BigDecimal getImportoResiduoInizialeAnno0() {
		return importoResiduoInizialeAnno0;
	}

	public void setImportoResiduoInizialeAnno0(BigDecimal importoResiduoInizialeAnno0) {
		this.importoResiduoInizialeAnno0 = importoResiduoInizialeAnno0;
	}

	public BigDecimal getImportoInizialeAnno1() {
		return importoInizialeAnno1;
	}

	public void setImportoInizialeAnno1(BigDecimal importoInizialeAnno1) {
		this.importoInizialeAnno1 = importoInizialeAnno1;
	}

	public BigDecimal getImportoInizialeAnno2() {
		return importoInizialeAnno2;
	}

	public void setImportoInizialeAnno2(BigDecimal importoInizialeAnno2) {
		this.importoInizialeAnno2 = importoInizialeAnno2;
	}

	public BigDecimal getImportoInizialeAnniSuccessivi() {
		return importoInizialeAnniSuccessivi;
	}

	public void setImportoInizialeAnniSuccessivi(BigDecimal importoInizialeAnniSuccessivi) {
		this.importoInizialeAnniSuccessivi = importoInizialeAnniSuccessivi;
	}

	public TipoDettaglioComponenteImportiCapitolo getTipoDettaglioComponenteImportiCapitolo() {
		return tipoDettaglioComponenteImportiCapitolo;
	}

	public void setTipoDettaglioComponenteImportiCapitolo(
			TipoDettaglioComponenteImportiCapitolo tipoDettaglioComponenteImportiCapitolo) {
		this.tipoDettaglioComponenteImportiCapitolo = tipoDettaglioComponenteImportiCapitolo;
	}
	
	
	public String getTipoDettaglioComponenteDesc() {
		return getTipoDettaglioComponenteImportiCapitolo()!= null? getTipoDettaglioComponenteImportiCapitolo().getDescrizione() : "";
	}
	
	protected boolean isImportoVisualizzabileByPeriodoName(String periodoName) {
		return !periodiImportoSenzaSignificatoContabile.contains(periodoName);
	}
	
	protected boolean isImportoInizialeVisualizzabileByPeriodoName(String periodoName) {
		return !periodiImportoInizialeSenzaSignificatoContabile.contains(periodoName);
	}
	
	public String getFormattedImportoAnniPrecedenti() {
		return isImportoVisualizzabileByPeriodoName(PeriodoTabellaComponentiImportiCapitolo.ANNI_PRECEDENTI.name())? FormatUtils.formatCurrency(importoAnniPrecedenti) : "";
	}

	public String getFormattedImportoAnno0() {
		return isImportoVisualizzabileByPeriodoName(PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO.name())? FormatUtils.formatCurrency(importoAnno0) : "";
	}
	

	public String getFormattedImportoResiduoAnno0() {
		return isImportoVisualizzabileByPeriodoName(PeriodoTabellaComponentiImportiCapitolo.RESIDUO_ANNO_BILANCIO.name())? FormatUtils.formatCurrency(importoResiduoAnno0) : "";
	}
	
	
	public String getFormattedImportoAnno1() {
		return isImportoVisualizzabileByPeriodoName(PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_UNO.name())? FormatUtils.formatCurrency(importoAnno1) : "";
	}
	
	public String getFormattedImportoAnno2() {
		return isImportoVisualizzabileByPeriodoName(PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_DUE.name())? FormatUtils.formatCurrency(importoAnno2) : "";
	}
	
	
	public String getFormattedImportoAnniSuccessivi() {
		return isImportoVisualizzabileByPeriodoName(PeriodoTabellaComponentiImportiCapitolo.ANNI_SUCCESSIVI.name())? FormatUtils.formatCurrency(importoAnniSuccessivi) : "";
	}
	
	public String getFormattedImportoInizialeAnniPrecedenti() {
		return isImportoInizialeVisualizzabileByPeriodoName(PeriodoTabellaComponentiImportiCapitolo.ANNI_PRECEDENTI.name())? FormatUtils.formatCurrency(importoInizialeAnniPrecedenti): "";
	}

	public String getFormattedImportoInizialeAnno0() {
		return isImportoInizialeVisualizzabileByPeriodoName(PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO.name())? FormatUtils.formatCurrency(importoInizialeAnno0): "";
	}
	

	public String getFormattedImportoResiduoInizialeAnno0() {
		return isImportoInizialeVisualizzabileByPeriodoName(PeriodoTabellaComponentiImportiCapitolo.RESIDUO_ANNO_BILANCIO.name())? FormatUtils.formatCurrency(importoResiduoInizialeAnno0): "";
	}
	
	
	public String getFormattedImportoInizialeAnno1() {
		return isImportoInizialeVisualizzabileByPeriodoName(PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_UNO.name())? FormatUtils.formatCurrency(importoInizialeAnno1): "";
	}
	
	public String getFormattedImportoInizialeAnno2() {
		return isImportoInizialeVisualizzabileByPeriodoName(PeriodoTabellaComponentiImportiCapitolo.ANNO_BILANCIO_PIU_DUE.name())? FormatUtils.formatCurrency(importoInizialeAnno2): "";
	}
	
	
	public String getFormattedImportoInizialeAnniSuccessivi() {
		return isImportoInizialeVisualizzabileByPeriodoName(PeriodoTabellaComponentiImportiCapitolo.ANNI_SUCCESSIVI.name())? FormatUtils.formatCurrency(importoInizialeAnniSuccessivi): "";
	}
	
}

