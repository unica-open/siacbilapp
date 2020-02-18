/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.SerializationUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.business.utility.StringUtilities;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.model.TipoComponenteImportiCapitolo;
import it.csi.siac.siacbilser.model.TipoDettaglioComponenteImportiCapitolo;

/**
 * Classe di wrap per il capitolo durante le fasi di variazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 24/10/2013
 * 
 */
public class ElementoComponenteVariazione implements Serializable, Cloneable, ModelWrapper {
	
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5395189332682842286L;
	
	private static final Pattern PATTERN = Pattern.compile("%(ANNO|IMPORTO)%");
	private static final String INPUT_AGGIORNAMENTO_IMPORTO_COMPONENTE = "<input type=\"text\" class=\"input-small soloNumeri decimale text-right %ANNO%\" disabled value=\"%IMPORTO%\"/>";
	
	private DettaglioVariazioneComponenteImportoCapitolo dettaglioAnno0;
	private DettaglioVariazioneComponenteImportoCapitolo dettaglioAnno1;
	private DettaglioVariazioneComponenteImportoCapitolo dettaglioAnno2;
	
	private Boolean eliminaSuTuttiGliAnni;
	private Boolean nuovaComponente;
	private Boolean importoModificabileAnno0;
	private Boolean importoModificabileAnno1;
	private Boolean importoModificabileAnno2;
	
	//per la gestione dell'importo sul flag elimina
	private BigDecimal importoComponenteOriginaleCapitoloAnno0 = BigDecimal.ZERO;
	private BigDecimal importoComponenteOriginaleCapitoloAnno1 = BigDecimal.ZERO;
	private BigDecimal importoComponenteOriginaleCapitoloAnno2 = BigDecimal.ZERO;
	

	/** Per la serializzazione */
	
	/** Costruttore vuoto di default */
	public ElementoComponenteVariazione() {
		super();
	}
	
	/**
	 * Gets the dettaglio variazione componente importo capitolo.
	 *
	 * @return the dettaglio variazione componente importo capitolo
	 */
	public DettaglioVariazioneComponenteImportoCapitolo getDettaglioVariazioneComponenteImportoCapitolo() {
		return getDettaglioAnno0();
	}
	
/**
	 * @return the dettaglioAnno0
	 */
	public DettaglioVariazioneComponenteImportoCapitolo getDettaglioAnno0() {
		return dettaglioAnno0;
	}

	
	/**
	 * @param dettaglioAnno0 the dettaglioAnno0 to set
	 */
	public void setDettaglioAnno0(
			DettaglioVariazioneComponenteImportoCapitolo dettaglioAnno0) {
		this.dettaglioAnno0 = dettaglioAnno0;
	}


	/**
	 * @return the dettaglioAnno1
	 */
	public DettaglioVariazioneComponenteImportoCapitolo getDettaglioAnno1() {
		return dettaglioAnno1;
	}


	/**
	 * @param dettaglioAnno1 the dettaglioAnno1 to set
	 */
	public void setDettaglioAnno1(
			DettaglioVariazioneComponenteImportoCapitolo dettaglioAnno1) {
		this.dettaglioAnno1 = dettaglioAnno1;
	}


	/**
	 * @return the dettaglioAnno2
	 */
	public DettaglioVariazioneComponenteImportoCapitolo getDettaglioAnno2() {
		return dettaglioAnno2;
	}


	/**
	 * @param dettaglioAnno2 the dettaglioAnno2 to set
	 */
	public void setDettaglioAnno2(
			DettaglioVariazioneComponenteImportoCapitolo dettaglioAnno2) {
		this.dettaglioAnno2 = dettaglioAnno2;
	}


	/**
	 * @return the importoModificabileAnno0
	 */
	public Boolean getImportoModificabileAnno0() {
		return importoModificabileAnno0;
	}


	/**
	 * @param importoModificabileAnno0 the importoModificabileAnno0 to set
	 */
	public void setImportoModificabileAnno0(Boolean importoModificabileAnno0) {
		this.importoModificabileAnno0 = importoModificabileAnno0;
	}


	/**
	 * @return the importoModificabileAnno1
	 */
	public Boolean getImportoModificabileAnno1() {
		return importoModificabileAnno1;
	}


	/**
	 * @param importoModificabileAnno1 the importoModificabileAnno1 to set
	 */
	public void setImportoModificabileAnno1(Boolean importoModificabileAnno1) {
		this.importoModificabileAnno1 = importoModificabileAnno1;
	}


	/**
	 * @return the importoModificabileAnno2
	 */
	public Boolean getImportoModificabileAnno2() {
		return importoModificabileAnno2;
	}


	/**
	 * @param importoModificabileAnno2 the importoModificabileAnno2 to set
	 */
	public void setImportoModificabileAnno2(Boolean importoModificabileAnno2) {
		this.importoModificabileAnno2 = importoModificabileAnno2;
	}

	/**
	 * @return the eliminaSuTuttiGliAnni
	 */
	public Boolean getEliminaSuTuttiGliAnni() {
		return eliminaSuTuttiGliAnni;
	}


	/**
	 * @param eliminaSuTuttiGliAnni the eliminaSuTuttiGliAnni to set
	 */
	public void setEliminaSuTuttiGliAnni(Boolean eliminaSuTuttiGliAnni) {
		this.eliminaSuTuttiGliAnni = eliminaSuTuttiGliAnni;
	}

	/**
	 * @return the nuovaComponente
	 */
	public Boolean getNuovaComponente() {
		return nuovaComponente;
	}


	/**
	 * @param nuovaComponente the nuovaComponente to set
	 */
	public void setNuovaComponente(Boolean nuovaComponente) {
		this.nuovaComponente = nuovaComponente;
	}
	

	/**
	 * @return the importoComponenteOriginaleCapitoloAnno0
	 */
	public BigDecimal getImportoComponenteOriginaleCapitoloAnno0() {
		return importoComponenteOriginaleCapitoloAnno0;
	}

	/**
	 * @param importoComponenteOriginaleCapitoloAnno0 the importoComponenteOriginaleCapitoloAnno0 to set
	 */
	public void setImportoComponenteOriginaleCapitoloAnno0(BigDecimal importoComponenteOriginaleCapitoloAnno0) {
		this.importoComponenteOriginaleCapitoloAnno0 = importoComponenteOriginaleCapitoloAnno0 != null? importoComponenteOriginaleCapitoloAnno0 : BigDecimal.ZERO;
	}

	/**
	 * @return the importoComponenteOriginaleCapitoloAnno1
	 */
	public BigDecimal getImportoComponenteOriginaleCapitoloAnno1() {
		return importoComponenteOriginaleCapitoloAnno1;
	}

	/**
	 * @param importoComponenteOriginaleCapitoloAnno1 the importoComponenteOriginaleCapitoloAnno1 to set
	 */
	public void setImportoComponenteOriginaleCapitoloAnno1(BigDecimal importoComponenteOriginaleCapitoloAnno1) {
		this.importoComponenteOriginaleCapitoloAnno1 = importoComponenteOriginaleCapitoloAnno1 != null? importoComponenteOriginaleCapitoloAnno1 : BigDecimal.ZERO;
	}

	/**
	 * @return the importoComponenteOriginaleAnno2
	 */
	public BigDecimal getImportoComponenteOriginaleCapitoloAnno2() {
		return importoComponenteOriginaleCapitoloAnno2;
	}

	/**
	 * @param importoComponenteOriginaleAnno2 the importoComponenteOriginaleAnno2 to set
	 */
	public void setImportoComponenteOriginaleCapitoloAnno2(BigDecimal importoComponenteOriginaleAnno2) {
		this.importoComponenteOriginaleCapitoloAnno2 = importoComponenteOriginaleAnno2 != null? importoComponenteOriginaleAnno2 : BigDecimal.ZERO;
	}

	/**
	 * Gets the tipo componente importi capitolo.
	 *
	 * @return the tipo componente importi capitolo
	 */
	public TipoComponenteImportiCapitolo getTipoComponenteImportiCapitolo() {
		return getComponenteImportiCapitolo() != null ? getComponenteImportiCapitolo().getTipoComponenteImportiCapitolo() : null; 
	}

	/**
	 * Gets the descrizione tipo componente capitolo.
	 *
	 * @return the descrizione tipo componente capitolo
	 */
	public String getDescrizioneTipoComponenteCapitolo() {
		TipoComponenteImportiCapitolo tipoComponenteImportiCapitolo = getTipoComponenteImportiCapitolo();
		return tipoComponenteImportiCapitolo != null? StringUtils.defaultIfBlank(tipoComponenteImportiCapitolo.getDescrizione(), "") : "";
	}
	
	/**
	 * Gets the componente importi capitolo.
	 *
	 * @return the componente importi capitolo
	 */
	public ComponenteImportiCapitolo getComponenteImportiCapitolo() {
		return getDettaglioVariazioneComponenteImportoCapitolo() != null? getDettaglioVariazioneComponenteImportoCapitolo().getComponenteImportiCapitolo() : null;
	}
	
	/**
	 * Gets the tipo dettaglio componente importi capitolo.
	 *
	 * @return the tipo dettaglio componente importi capitolo
	 */
	public TipoDettaglioComponenteImportiCapitolo getTipoDettaglioComponenteImportiCapitolo() {
		return getDettaglioVariazioneComponenteImportoCapitolo() != null? getDettaglioVariazioneComponenteImportoCapitolo().getTipoDettaglioComponenteImportiCapitolo() : null;
	}
	
	
	/**
	 * Gets the descrizione componente.
	 *
	 * @return the descrizione componente
	 */
	public String getDescrizioneTipoComponente() {
		TipoComponenteImportiCapitolo tipo = getTipoComponenteImportiCapitolo();
		
		return tipo != null &&  StringUtils.isNotEmpty(tipo.getDescrizione()) ?
				tipo.getDescrizione() : "N.D.";
	}
	
	/**
	 * Gets the importo.
	 *
	 * @return the importo
	 */
	public String getImporto() {
		return getDettaglioVariazioneComponenteImportoCapitolo() != null? FormatUtils.formatCurrency(getDettaglioVariazioneComponenteImportoCapitolo().getImporto()) : "0,00";
	}
	
	/**
	 * Gets the descrizione tipo dettaglio componente capitolo.
	 *
	 * @return the descrizione tipo dettaglio componente capitolo
	 */
	public String getDescrizioneTipoDettaglioComponenteCapitolo() {
		TipoDettaglioComponenteImportiCapitolo tipo = getTipoDettaglioComponenteImportiCapitolo();
		return tipo != null? StringUtils.defaultIfBlank(tipo.getDescrizione(), "N.D.") : "";
	}
	
	/**
	 * Gets the descrizione dettaglio in variazione.
	 *
	 * @return the descrizione dettaglio in variazione
	 */
	public String getDescrizioneDettaglioInVariazione() {
		
		return new StringBuilder()
				.append(getDescrizioneTipoComponente())
				.append(Boolean.TRUE.equals(getNuovaComponente()) ? " ( NUOVA )" : "")
				.append(Boolean.TRUE.equals(getEliminaSuTuttiGliAnni()) == true ? " ( DA ELIMINARE )" : "")
				.toString();
	}
	
	
	/**
	 * Gets the html importo anno 0.
	 *
	 * @return the html importo anno 0
	 */
	public String getHtmlImportoAnno0() {
		String importoFormattato = FormatUtils.formatCurrency(getImportoAnno0());
		return Boolean.TRUE.equals(getImportoModificabileAnno0())? getInputImportoModificabile(importoFormattato, "anno0") : importoFormattato;
	}
	
	/**
	 * Gets the html importo anno 1.
	 *
	 * @return the html importo anno 1
	 */
	public String getHtmlImportoAnno1() {
		String importoFormattato = FormatUtils.formatCurrency(getImportoAnno1());
		return Boolean.TRUE.equals(getImportoModificabileAnno1())? getInputImportoModificabile(importoFormattato, "anno1") : importoFormattato;
	}
	
	/**
	 * Gets the html importo anno 2.
	 *
	 * @return the html importo anno 2
	 */
	public String getHtmlImportoAnno2() {
		String importoFormattato = FormatUtils.formatCurrency(getImportoAnno2());
		return Boolean.TRUE.equals(getImportoModificabileAnno2())? getInputImportoModificabile(importoFormattato, "anno2") : importoFormattato;
	}


	/**
	 * @param importoFormattato
	 * @return
	 */
	private String getInputImportoModificabile(String importoFormattato, String anno) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("%IMPORTO%", importoFormattato);
		map.put("%ANNO%", anno);
		return StringUtilities.replacePlaceholders(PATTERN, INPUT_AGGIORNAMENTO_IMPORTO_COMPONENTE.toString(), map, false);
	}
	
	
	
	/**
	 * @return
	 */
	public BigDecimal getImportoAnno0() {
		return getDettaglioAnno0().getImporto();
	}
	
	/**
	 * Gets the importo anno 1.
	 *
	 * @return the importo anno 1
	 */
	public BigDecimal getImportoAnno1() {
		return getDettaglioAnno1().getImporto();
	}
	
	/**
	 * Gets the importo anno 2.
	 *
	 * @return the importo anno 2
	 */
	public BigDecimal getImportoAnno2() {
		return getDettaglioAnno2().getImporto();
	}
	
	

	@Override
	public Object clone() throws CloneNotSupportedException {
		super.clone();
		return SerializationUtils.clone(this);
	}



	@Override
	public int getUid() {
		return 0;
	}

}
