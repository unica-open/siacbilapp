/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.util.wrappers.variazionecespite;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siaccespser.model.StatoVariazioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespite;

/**
 * Wrapper per la variazione cespite
 * @author Marchino Alessandro
 * @version 1.0.0 - 10/08/2018
 *
 */
public class ElementoVariazioneCespite implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6221816405285215539L;
	
	private final VariazioneCespite variazioneCespite;
	private String azioni;
	
	/**
	 * Costruttore di wrap
	 * @param variazioneCespite la variazione cespite
	 */
	public ElementoVariazioneCespite(VariazioneCespite variazioneCespite) {
		this.variazioneCespite = variazioneCespite;
	}

	/**
	 * @return the anno
	 */
	public String getAnno() {
		return variazioneCespite == null ? "" : variazioneCespite.getAnnoVariazione();
	}
	
	/**
	 * @return the dataInserimento
	 */
	public String getDataInserimento() {
		return variazioneCespite == null ? "" : FormatUtils.formatDate(variazioneCespite.getDataVariazione());
	}
	
	/**
	 * @return the tipoVariazione
	 */
	public String getTipoVariazione() {
		return variazioneCespite == null
			? ""
			: Boolean.TRUE.equals(variazioneCespite.getFlagTipoVariazioneIncremento())
				? "I - incremento"
				: "D - decremento";
	}
	
	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return variazioneCespite == null ? "" : variazioneCespite.getDescrizione();
	}
	
	/**
	 * @return the importo
	 */
	public String getImporto() {
		return variazioneCespite == null ? "" : FormatUtils.formatCurrency(variazioneCespite.getImporto());
	}
	
	/**
	 * @return the stato
	 */
	public String getStato() {
		StatoVariazioneCespite stato = getStatoVariazioneCespite();
		return stato == null
			? ""
			: (stato.getCodice() + " - " + stato.getDescrizione());
	}
	
	/**
	 * Gets the stato variazione cespite.
	 *
	 * @return the stato variazione cespite
	 */
	public StatoVariazioneCespite getStatoVariazioneCespite() {
		return variazioneCespite != null ? variazioneCespite.getStatoVariazioneCespite() : null;
	}
	
	/**
	 * @return the cespite
	 */
	public String getCespite() {
		if(variazioneCespite == null || variazioneCespite.getCespite() == null) {
			return "";
		}
		return new StringBuilder()
				.append("<a data-original-title=\"Descrizione\" href=\"#\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
				.append(FormatUtils.formatHtmlAttributeString(variazioneCespite.getCespite().getDescrizione()))
				.append("\">")
				.append(variazioneCespite.getCespite().getNumeroInventario())
				.append("</a>")
				.toString();
	}
	
	/**
	 * @return the tipoBene
	 */
	public String getTipoBene() {
		if(variazioneCespite == null || variazioneCespite.getCespite() == null || variazioneCespite.getCespite().getTipoBeneCespite() == null) {
			return "";
		}
		return new StringBuilder()
				.append(variazioneCespite.getCespite().getTipoBeneCespite().getCodice())
				.append(" - ")
				.append(variazioneCespite.getCespite().getTipoBeneCespite().getDescrizione())
				.toString();
	}
	
	/**
	 * @return the azioni
	 */
	public String getAzioni() {
		return this.azioni;
	}


	/**
	 * @param azioni the azioni to set
	 */
	public void setAzioni(String azioni) {
		this.azioni = azioni;
	}


	@Override
	public int getUid() {
		return variazioneCespite != null ? variazioneCespite.getUid() : 0;
	}

}
