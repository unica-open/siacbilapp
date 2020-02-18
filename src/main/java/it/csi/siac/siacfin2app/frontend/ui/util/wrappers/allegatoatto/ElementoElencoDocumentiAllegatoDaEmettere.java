/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

/**
 * Wrapper per l'Allegato Atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/set/2014
 *
 */
public class ElementoElencoDocumentiAllegatoDaEmettere implements ModelWrapper, Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2096757269195747448L;

	
	private final ElencoDocumentiAllegato elencoDocumentiAllegato;
	
	/**
	 * Costruttore a partire dalla superclasse.
	 *
	 * @param elencoDocumentiAllegato the elenco documenti allegato
	 */
	public ElementoElencoDocumentiAllegatoDaEmettere(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		this.elencoDocumentiAllegato = elencoDocumentiAllegato;
	}
	
	/**
	 * Unwrap.
	 *
	 * @return the elenco documenti allegato
	 */
	public ElencoDocumentiAllegato getElencoDocumentiAllegato() {
		return this.elencoDocumentiAllegato;
	}

	/**
	 * Gets the totale da pagare.
	 *
	 * @return the totale da pagare
	 */
	public BigDecimal getTotaleDaPagare() {
		return this.elencoDocumentiAllegato.getTotaleDaPagare();
	}
	
	/**
	 * Gets the anno.
	 *
	 * @return the anno
	 */
	public String getAnno() {
		return this.elencoDocumentiAllegato.getAnno() != null? this.elencoDocumentiAllegato.getAnno().toString() : "";
	}
	
	/**
	 * Gets the numero.
	 *
	 * @return the numero
	 */
	public String getNumero() {
		return this.elencoDocumentiAllegato.getNumero() != null? this.elencoDocumentiAllegato.getNumero().toString() : "";
	}
	
	/**
	 * Gets the stato.
	 *
	 * @return the stato
	 */
	public String getStato() {
		return this.elencoDocumentiAllegato.getStatoOperativoElencoDocumenti() != null? this.elencoDocumentiAllegato.getStatoOperativoElencoDocumenti().getCodice() : "";
	}
	
	/**
	 * Gets the data trasmissione.
	 *
	 * @return the data trasmissione
	 */
	public String getDataTrasmissione() {
		StringBuilder sb = new StringBuilder();
		if(this.elencoDocumentiAllegato.getDataTrasmissione() == null) {
			return "";
		}
		sb.append("<a data-original-title='Fonte' data-trigger='hover' rel='popover' data-content='")
		.append(FormatUtils.formatHtmlAttributeString(StringUtils.defaultIfBlank(this.elencoDocumentiAllegato.getSysEsterno(), "")))
		.append(" - ")
		.append(this.elencoDocumentiAllegato.getAnnoSysEsterno()!= null? this.elencoDocumentiAllegato.getAnnoSysEsterno() : "")
		.append("/")
		.append(StringUtils.defaultIfBlank(this.elencoDocumentiAllegato.getNumeroSysEsterno(), ""))
		.append("'>")
		.append(this.elencoDocumentiAllegato.getDataTrasmissione()!= null? FormatUtils.formatDate(this.elencoDocumentiAllegato.getDataTrasmissione()) : "")
		.append("</a>");
		return sb.toString();
	}

	/**
	 * Gets the checks for impegno con conferma durc.
	 *
	 * @return the checks for impegno con conferma durc
	 */
	public String getHasImpegnoConConfermaDurcString() {
		return hasImpegnoConfermaDurc()? "S&igrave;" : "No";
	}
	
	private boolean hasImpegnoConfermaDurc() {
		return Boolean.TRUE.equals(this.elencoDocumentiAllegato.getHasImpegnoConfermaDurc());
	}
	
	/**
	 * Gets the data fine validita durc.
	 *
	 * @return the data fine validita durc
	 */
	public String getDataFineValiditaDurc() {
		if(!hasImpegnoConfermaDurc()) {
			return "";
		}
		return this.elencoDocumentiAllegato.getDataFineValiditaDurc() != null? FormatUtils.formatDate(this.elencoDocumentiAllegato.getDataFineValiditaDurc()) : "Durc da richiedere";
	}
	
	/**
	 * Inibisci selezione.
	 *
	 * @return true, if successful
	 */
	public boolean getInibisciSelezioneSpesa() {
		if(BigDecimal.ZERO.compareTo(this.elencoDocumentiAllegato.getTotaleDaPagareNotNull())==0) {
			return true;
		}		
		return hasImpegnoConfermaDurc() && isDurcScadutoONull();
	}
	
	/**
	 * Inibisci selezione.
	 *
	 * @return true, if successful
	 */
	public boolean getInibisciSelezioneEntrata() {
		return BigDecimal.ZERO.compareTo(this.elencoDocumentiAllegato.getTotaleDaIncassareNotNull()) == 0;
	}
	
	/**
	 * Checks if is durc scaduto O null.
	 *
	 * @return true, if is durc scaduto O null
	 */
	private boolean isDurcScadutoONull() {
		Date dataFineValiditaDurc = this.elencoDocumentiAllegato.getDataFineValiditaDurc();
		if(dataFineValiditaDurc == null) {
			return true;
		}
		Date now = new Date();
		
		//SIAC-7143
		String dateNow = new SimpleDateFormat("yyyy-MM-dd").format(now);
		String dateFineDurc = new SimpleDateFormat("yyyy-MM-dd").format(dataFineValiditaDurc);
		
		return dateNow.compareTo(dateFineDurc)>0;
	}
	
	/**
	 * Gets the totale quote spese.
	 *
	 * @return the totale quote spese
	 */
	public String getTotaleQuoteSpese() {
		return this.elencoDocumentiAllegato.getTotaleQuoteSpese() != null? FormatUtils.formatCurrency(this.elencoDocumentiAllegato.getTotaleQuoteSpese()) : "";
	}
	
	/**
	 * Gets the totale da pagare string.
	 *
	 * @return the totale da pagare string
	 */
	public String getTotaleDaPagareString() {
		return this.elencoDocumentiAllegato.getTotaleDaPagare() != null? FormatUtils.formatCurrency(this.elencoDocumentiAllegato.getTotaleDaPagare()) : "";
	}
	
	/**
	 * Gets the totale quote spese.
	 *
	 * @return the totale quote spese
	 */
	public String getTotaleQuoteEntrate() {
		return this.elencoDocumentiAllegato.getTotaleQuoteEntrate() != null? FormatUtils.formatCurrency(this.elencoDocumentiAllegato.getTotaleQuoteEntrate()) : "";
	}
	
	/**
	 * Gets the totale da pagare string.
	 *
	 * @return the totale da pagare string
	 */
	public String getTotaleDaIncassareString() {
		return this.elencoDocumentiAllegato.getTotaleDaIncassare() != null? FormatUtils.formatCurrency(this.elencoDocumentiAllegato.getTotaleDaIncassare()) : "";
	}
	

	@Override
	public int getUid() {
		return this.elencoDocumentiAllegato.getUid();
	}
}
