/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.causali;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ContoTipoOperazione;
import it.csi.siac.siacgenser.model.StatoOperativoCausaleEP;

/**
 * Wrapper per la causale EP.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 31/03/2015
 *
 */
public class ElementoCausaleEP implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4211692892934927840L;
	
	private final CausaleEP causaleEP;
	private String azioni;
	
	/**
	 * Costruttore di wrap.
	 * 
	 * @param causaleEP la causale da wrappare
	 */
	public ElementoCausaleEP(CausaleEP causaleEP) {
		this.causaleEP = causaleEP;
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
	 * @return the tipo
	 */
	public String getTipo() {
		return causaleEP != null && causaleEP.getTipoCausale() != null ? causaleEP.getTipoCausale().getDescrizione() : "";
	}
	
	/**
	 * @return the codice
	 */
	public String getCodice() {
		return causaleEP != null ? causaleEP.getCodice() : "";
	}
	
	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return causaleEP != null ? causaleEP.getDescrizione() : "";
	}
	
	/**
	 * @return the conti
	 */
	public String getConti() {
		StringBuilder sb = new StringBuilder();
		if(causaleEP != null && causaleEP.getContiTipoOperazione() != null) {
			sb.append("<a data-original-title=\"Conti collegati\" data-trigger=\"hover\" rel=\"popover\" data-content=\"");
			
			for (ContoTipoOperazione cto : causaleEP.getContiTipoOperazione()) {
				sb.append(cto != null && cto.getConto() != null && cto.getConto().getCodice() != null
					? FormatUtils.formatHtmlAttributeString(cto.getConto().getCodice()) + "<br/>"
					: "");
			}
			
			sb.append("\" data-html=\"true\">")
				.append(causaleEP.getContiTipoOperazione().size())
				.append("</a>");
		}
		return sb.toString();
	}
	
	/**
	 * @return the stato
	 */
	public String getStato() {
		return causaleEP != null && causaleEP.getStatoOperativoCausaleEP() != null ? causaleEP.getStatoOperativoCausaleEP().getDescrizione() : "";
	}

	@Override
	public int getUid() {
		return causaleEP != null ? causaleEP.getUid() : -1;
	}
	
	/**
	 * Ritorna lo stato operativo della causale EP
	 * @return lo stato
	 */
	public StatoOperativoCausaleEP retrieveStato() {
		return causaleEP != null ? causaleEP.getStatoOperativoCausaleEP() : null;
	}
	
}
