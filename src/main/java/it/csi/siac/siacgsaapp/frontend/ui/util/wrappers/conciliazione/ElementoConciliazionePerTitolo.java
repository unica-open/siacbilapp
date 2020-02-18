/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.util.wrappers.conciliazione;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacgenser.model.ConciliazionePerTitolo;

/**
 * Wrapper per la conciliazione per titolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 28/10/2015
 */
public class ElementoConciliazionePerTitolo implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2356750534014510120L;
	
	private final ConciliazionePerTitolo conciliazionePerTitolo;
	private String azioni;
	
	/**
	 * Costruttore di wrap.
	 * 
	 * @param conciliazionePerTitolo la conciliazione da wrappare
	 */
	public ElementoConciliazionePerTitolo(ConciliazionePerTitolo conciliazionePerTitolo) {
		this.conciliazionePerTitolo = conciliazionePerTitolo;
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
		return conciliazionePerTitolo != null ? conciliazionePerTitolo.getUid() : -1;
	}
	
	/**
	 * @return the stringaClassificatore
	 */
	public String getStringaClassificatore() {
		if(conciliazionePerTitolo == null || conciliazionePerTitolo.getClassificatoreGerarchico() == null) {
			return "";
		}
		
		StringBuilder sb = new StringBuilder()
			.append("<a href=\"#\" rel=\"popover\" data-original-title=\"Descrizione\" data-trigger=\"hover\" data-content=\"")
			.append(FormatUtils.formatHtmlAttributeString(conciliazionePerTitolo.getClassificatoreGerarchico().getDescrizione()))
			.append("\">");
		if(conciliazionePerTitolo.getClassificatoreGerarchico() instanceof Macroaggregato) {
			sb.append(conciliazionePerTitolo.getClassificatoreGerarchico().getCodice());
		}
		if(conciliazionePerTitolo.getClassificatoreGerarchico() instanceof CategoriaTipologiaTitolo) {
			List<String> chunks = new ArrayList<String>();
			if(conciliazionePerTitolo.getTipologiaTitolo() != null) {
				chunks.add(conciliazionePerTitolo.getTipologiaTitolo().getCodice());
			}
			chunks.add(conciliazionePerTitolo.getClassificatoreGerarchico().getCodice());
			sb.append(StringUtils.join(chunks, "-"));
		}
		return sb.append("</a>")
				.toString();
	}
	
	/**
	 * @return the stringaClasse
	 */
	public String getStringaClasse() {
		return conciliazionePerTitolo != null && conciliazionePerTitolo.getConto() != null
			&& conciliazionePerTitolo.getConto().getPianoDeiConti() != null
			&& conciliazionePerTitolo.getConto().getPianoDeiConti().getClassePiano() != null
				? conciliazionePerTitolo.getConto().getPianoDeiConti().getClassePiano().getCodice()
				: "";
	}
	
	/**
	 * @return the stringaConto
	 */
	public String getStringaConto() {
		return conciliazionePerTitolo != null && conciliazionePerTitolo.getConto() != null
				? conciliazionePerTitolo.getConto().getCodice()
				: "";
	}
	
	/**
	 * @return the stringaDescrizioneConto
	 */
	public String getStringaDescrizioneConto() {
		return conciliazionePerTitolo != null && conciliazionePerTitolo.getConto() != null
				? conciliazionePerTitolo.getConto().getDescrizione()
				: "";
	}
}
