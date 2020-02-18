/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.codifica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe per la gestione del risultato necessario per la gestione degli zTree.
 * <br>
 * La classe presenta tre soli costruttori al momento, in quanto la gestione sullo
 * zTree &eacute; riservata all'{@link ElementoPianoDeiConti}, alla 
 * {@link StrutturaAmministrativoContabile} e al SIOPE.
 * 
 * @author Marchino Alessandro
 * @version 1.0.1 - 03/12/2013
 *
 */
public class ElementoCodifica implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2917019157620631539L;
	/** Pattern per la generazione del testo */
	private static final String PATTERN = "%s-%s";
	
	private int uid;
	private String descrizione;
	private String codice;
	private String codiceTipo;
	private List<ElementoCodifica> sottoElementi = new ArrayList<ElementoCodifica>();
	
	/** Costruttore vuoto di default */
	public ElementoCodifica() {
		super();
	}
	
	@Override
	public int getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public void setUid(int uid) {
		this.uid = uid;
	}

	/**
	 * @return the testo
	 */
	public String getTesto() {
		return String.format(PATTERN, codice, descrizione);
	}

	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return descrizione;
	}

	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	/**
	 * @return the codice
	 */
	public String getCodice() {
		return codice;
	}

	/**
	 * @param codice the codice to set
	 */
	public void setCodice(String codice) {
		this.codice = codice;
	}

	/**
	 * @return the codiceTipo
	 */
	public String getCodiceTipo() {
		return codiceTipo;
	}

	/**
	 * @param codiceTipo the codiceTipo to set
	 */
	public void setCodiceTipo(String codiceTipo) {
		this.codiceTipo = codiceTipo;
	}

	/**
	 * @return the sottoElementi
	 */
	public List<ElementoCodifica> getSottoElementi() {
		return sottoElementi;
	}

	/**
	 * @param sottoElementi the sottoElementi to set
	 */
	public void setSottoElementi(List<ElementoCodifica> sottoElementi) {
		this.sottoElementi = sottoElementi;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
			.append("uid", 			  uid)
			.append("testo", 		  getTesto())
			.append("sotto-elementi", sottoElementi)
			.toString();
	}
	
}
