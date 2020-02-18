/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.richiestaeconomale;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;

/**
 * Wrapper per la richiesta economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 30/01/2015
 *
 */
public class ElementoRichiestaEconomale implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5543370162601347997L;

	private final RichiestaEconomale richiestaEconomale;
	private String azioni;
	
	/** 
	 * Costruttore a partire dalla richiesta da wrappare
	 * 
	 * @param richiestaEconomale la richiesta da wrappare
	 */
	public ElementoRichiestaEconomale(RichiestaEconomale richiestaEconomale) {
		this.richiestaEconomale = richiestaEconomale;
	}
	
	/**
	 * @return the azioni
	 */
	public String getAzioni() {
		return azioni;
	}

	/**
	 * @param azioni the azioni to set
	 */
	public void setAzioni(String azioni) {
		this.azioni = azioni;
	}

	/**
	 * @return the numeroRichiesta
	 */
	public String getNumeroRichiesta() {
		return richiestaEconomale != null && richiestaEconomale.getNumeroRichiesta() != null ? richiestaEconomale.getNumeroRichiesta().toString() : "";
	}
	
	/**
	 * @return the dataCreazione
	 */
	public String getDataCreazione() {
		return richiestaEconomale != null && richiestaEconomale.getDataCreazione() != null ? FormatUtils.formatDate(richiestaEconomale.getDataCreazione()) : "";
	}
	
	/**
	 * @return the statoOperativoRichiestaEconomale
	 */
	public String getStatoOperativoRichiestaEconomale() {
		return richiestaEconomale != null && richiestaEconomale.getStatoOperativoRichiestaEconomale() != null ? richiestaEconomale.getStatoOperativoRichiestaEconomale().getDescrizione() : "";
	}
	
	/**
	 * @return the numeroSospeso
	 */
	public String getNumeroSospeso() {
		return richiestaEconomale != null && richiestaEconomale.getSospeso() != null && richiestaEconomale.getSospeso().getNumeroSospeso() != null
				? richiestaEconomale.getSospeso().getNumeroSospeso().toString()
				: "";
	}
	
	/**
	 * @return the numeroMovimento
	 */
	public String getNumeroMovimento() {
		return richiestaEconomale != null && richiestaEconomale.getMovimento() != null && richiestaEconomale.getMovimento().getNumeroMovimento() != null
				? richiestaEconomale.getMovimento().getNumeroMovimento().toString()
				: "";
	}
	
	/**
	 * @return the richiedente
	 */
	public String getRichiedente() {
		if (richiestaEconomale != null && richiestaEconomale.getSoggetto() != null && richiestaEconomale.getSoggetto().getDenominazione()!=null) {
			return richiestaEconomale.getSoggetto().getDenominazione();
		} else if (richiestaEconomale != null && richiestaEconomale.getCognome()!=null) {
			return richiestaEconomale.getCognome();
		} 
		return "";
		
		/*if (richiestaEconomale != null && richiestaEconomale.getSoggetto() != null) {
			return richiestaEconomale.getSoggetto().getDenominazione();
		} else if (richiestaEconomale != null && richiestaEconomale.getMatricola()!=null) {
			return richiestaEconomale.getCognome();
		} 
		return "";*/
	}
	
	/**
	 * @return the importoRichiesta
	 */
	public BigDecimal getImportoRichiesta() {
		return richiestaEconomale != null && richiestaEconomale.getImporto() != null ? richiestaEconomale.getImporto() : BigDecimal.ZERO;
	}
	
	/**
	 * @return the statoOperativoPrenotata
	 */
	public boolean isStatoOperativoPrenotata() {
		return richiestaEconomale != null && StatoOperativoRichiestaEconomale.PRENOTATA.equals(richiestaEconomale.getStatoOperativoRichiestaEconomale());
	}
	
	
	/**
	 * @return the statoOperativoRendicontata
	 */
	public boolean isStatoOperativoRendicontata() {
		return richiestaEconomale != null && StatoOperativoRichiestaEconomale.RENDICONTATA.equals(richiestaEconomale.getStatoOperativoRichiestaEconomale());
	}
	/**
	 * @return the statoOperativoRendicontata
	 */
	public boolean isStatoOperativoAgliAtti() {
		return richiestaEconomale != null && StatoOperativoRichiestaEconomale.AGLI_ATTI.equals(richiestaEconomale.getStatoOperativoRichiestaEconomale());
	}
	
	/**
	 * @return the statoOperativoDaRendicontare
	 */
	public boolean isStatoOperativoDaRendicontare() {
		return richiestaEconomale != null && StatoOperativoRichiestaEconomale.DA_RENDICONTARE.equals(richiestaEconomale.getStatoOperativoRichiestaEconomale());
	}
	
	/**
	 * @return the statoOperativoEvasa
	 */
	public boolean isStatoOperativoEvasa() {
		return richiestaEconomale != null && StatoOperativoRichiestaEconomale.EVASA.equals(richiestaEconomale.getStatoOperativoRichiestaEconomale());
	}
	
	/**
	 * @return the statoOperativoRendicontata
	 */
	public boolean hasRendiconto() {
		return getUidRendiconto() != 0;
	}
	
	/**
	 * @return the uidRendiconto
	 */
	public int getUidRendiconto() {
		return richiestaEconomale != null && richiestaEconomale.getRendicontoRichiesta() != null ? richiestaEconomale.getRendicontoRichiesta().getUid() : 0;
	}
	
	/**
	 * @return the numeroMovimentoRendiconto
	 */
	public Integer getNumeroMovimentoRendiconto() {
		return richiestaEconomale != null && richiestaEconomale.getRendicontoRichiesta() != null && richiestaEconomale.getRendicontoRichiesta().getMovimento() != null
			? richiestaEconomale.getRendicontoRichiesta().getMovimento().getNumeroMovimento()
			: null;
	}
	
	/**
	 * @return the fromEsterna
	 */
	public boolean isFromEsterna() {
		return richiestaEconomale != null && richiestaEconomale.getIdMissioneEsterna() != null;
	}
	
	@Override
	public int getUid() {
		return richiestaEconomale != null ? richiestaEconomale.getUid() : 0;
	}

}
