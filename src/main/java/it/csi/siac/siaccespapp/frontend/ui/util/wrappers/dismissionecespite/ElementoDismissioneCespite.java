/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.util.wrappers.dismissionecespite;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccespser.model.StatoDismissioneCespite;

/**
 * The Class ElementoDismissioneCespite.
 * @author elisa
 * @version 1.0.0 - 12-06-2018
 */
public class ElementoDismissioneCespite implements Serializable, ModelWrapper {

	/** Per la serializzazione*/
	private static final long serialVersionUID = 2349189995712324534L;
	
	private DismissioneCespite dismissioneCespite;
	private String azioni;
	
	
	/**
	 * Instantiates a new elemento categoria cespiti.
	 *
	 * @param dismissioneCespite the tipo bene cespite
	 */
	public ElementoDismissioneCespite(DismissioneCespite dismissioneCespite) {
		this.dismissioneCespite = dismissioneCespite;
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

	@Override
	public int getUid() {
		if(dismissioneCespite == null) {
			return 0;
		}
		return dismissioneCespite.getUid();
	}
	
	/**
	 * Gets the descrizione categoria.
	 *
	 * @return the descrizione categoria
	 */
	public String getDescrizione() {
		if(dismissioneCespite == null) {
			return "";
		}
		return dismissioneCespite.getDescrizione();
	}
	
	/**
	 * Gets the elenco.
	 *
	 * @return the elenco
	 */
	public String getElenco() {
		if(dismissioneCespite == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
				.append(dismissioneCespite.getAnnoElenco() != null? dismissioneCespite.getAnnoElenco()  : "" )
				.append("/")
				.append(dismissioneCespite.getNumeroElenco() != null? dismissioneCespite.getNumeroElenco()  : "" );
		return sb.toString();
		
	}
	
	/**
	 * Gets the provvedimento.
	 *
	 * @return the provvedimento
	 */
	public String getProvvedimento() {
		if(dismissioneCespite == null || dismissioneCespite.getAttoAmministrativo() == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
			.append(Integer.toString(dismissioneCespite.getAttoAmministrativo().getAnno()))
			.append("/")
			.append(Integer.toString(dismissioneCespite.getAttoAmministrativo().getNumero()));
		if(dismissioneCespite.getAttoAmministrativo().getTipoAtto() != null) {
			sb.append("/")
				.append(dismissioneCespite.getAttoAmministrativo().getTipoAtto().getDescrizione());
		}
		if(dismissioneCespite.getAttoAmministrativo().getStrutturaAmmContabile() != null) {
			sb.append("/")
				.append(dismissioneCespite.getAttoAmministrativo().getStrutturaAmmContabile().getCodice());
		}
		
		return sb.toString();
	}
	
	/**
	 * Gets the data cessazione.
	 *
	 * @return the data cessazione
	 */
	public String getDataCessazione() {
		if(dismissioneCespite == null) {
			return "";
		}
		return FormatUtils.formatDate(dismissioneCespite.getDataCessazione());
	}
	
	/**
	 * Gets the causale dismissione.
	 *
	 * @return the causale dismissione
	 */
	public String getCausaleDismissione(){
		if(dismissioneCespite == null || dismissioneCespite.getCausaleEP() == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<a data-original-title=\" Informazioni\" data-trigger=\"hover\" rel=\"popover\" data-content=\" ")
		.append(StringUtils.defaultString(dismissioneCespite.getCausaleEP().getDescrizione(), ""))
			.append(" \"");
		
		sb.append(" data-html=\"true\">")
			.append(StringUtils.defaultString(dismissioneCespite.getCausaleEP().getCodice(), ""))
			.append("&nbsp;")
			.append("*")
			.append("</a>");
		return sb.toString();
	}
	
	/**
	 * Gets the numero cespiti collegati.
	 *
	 * @return the numero cespiti collegati
	 */
	public String getNumeroCespitiCollegati() {
		return dismissioneCespite.getNumeroCespitiCollegati() != null? dismissioneCespite.getNumeroCespitiCollegati().toString() : "";
	}
	
	/**
	 * Gets the numero cespiti collegati long val.
	 *
	 * @return the numero cespiti collegati long val
	 */
	public Long getNumeroCespitiCollegatiLongVal() {
		return dismissioneCespite.getNumeroCespitiCollegati();
	}
	
	/**
	 * Gets the numero cespiti collegati.
	 *
	 * @return the numero cespiti collegati
	 */
	public String getStatoMovimento() {
		if( dismissioneCespite.getStatoDismissioneCespite() == null) {
			return "";
		}
		return dismissioneCespite.getStatoDismissioneCespite().getCodice() + " - " +  dismissioneCespite.getStatoDismissioneCespite().getDescrizione();
	}
	
	/**
	 * Gets the stato dismissione cespite.
	 *
	 * @return the stato dismissione cespite
	 */
	public StatoDismissioneCespite getStatoDismissioneCespite() {
		if( dismissioneCespite.getStatoDismissioneCespite() == null) {
			return null;
		}
		return dismissioneCespite.getStatoDismissioneCespite();
	}

}
