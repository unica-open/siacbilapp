/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.util.wrappers.conciliazione;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacgenser.model.ConciliazionePerBeneficiario;

/**
 * Wrapper per la conciliazione per capitolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/11/2015
 */
public class ElementoConciliazionePerBeneficiario implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3602774424944575488L;
	
	private final ConciliazionePerBeneficiario conciliazionePerBeneficiario;
	private final boolean isGestioneUEB;
	private String azioni;
	
	/**
	 * Costruttore di wrap.
	 * 
	 * @param conciliazionePerBeneficiario la conciliazione da wrappare
	 * @param isGestioneUEB se la gestione comprenda le UEB
	 */
	public ElementoConciliazionePerBeneficiario(ConciliazionePerBeneficiario conciliazionePerBeneficiario, boolean isGestioneUEB) {
		this.conciliazionePerBeneficiario = conciliazionePerBeneficiario;
		this.isGestioneUEB = isGestioneUEB;
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
		return conciliazionePerBeneficiario != null ? conciliazionePerBeneficiario.getUid() : -1;
	}
	
	/**
	 * @return the stringaEntSpe
	 */
	public String getStringaEntSpe() {
		return isSpesa() ? "S" : isEntrata() ? "E" : "";
	}
	
	/**
	 * @return the stringaCapitolo
	 */
	public String getStringaCapitolo() {
		if(conciliazionePerBeneficiario == null || conciliazionePerBeneficiario.getCapitolo() == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
			.append(conciliazionePerBeneficiario.getCapitolo().getNumeroCapitolo())
			.append("/")
			.append(conciliazionePerBeneficiario.getCapitolo().getNumeroArticolo());
		if(isGestioneUEB) {
			sb.append("/")
				.append(conciliazionePerBeneficiario.getCapitolo().getNumeroUEB());
		}
		return sb.toString();
	}
	
	/**
	 * @return the stringaClasseDiConciliazione
	 */
	public String getStringaClasseDiConciliazione() {
		return conciliazionePerBeneficiario != null && conciliazionePerBeneficiario.getClasseDiConciliazione() != null ? conciliazionePerBeneficiario.getClasseDiConciliazione().getDescrizione() : "";
	}
	
	/**
	 * @return the stringaClasse
	 */
	public String getStringaClasse() {
		return conciliazionePerBeneficiario != null && conciliazionePerBeneficiario.getConto() != null
			&& conciliazionePerBeneficiario.getConto().getPianoDeiConti() != null
			&& conciliazionePerBeneficiario.getConto().getPianoDeiConti().getClassePiano() != null
				? conciliazionePerBeneficiario.getConto().getPianoDeiConti().getClassePiano().getCodice()
				: "";
	}
	
	/**
	 * @return the stringaConto
	 */
	public String getStringaConto() {
		if(conciliazionePerBeneficiario == null || conciliazionePerBeneficiario.getConto() == null) {
			return "";
		}
		return new StringBuilder("<a href=\"#\" rel=\"popover\" data-trigger=\"hover\" data-original-title=\"Descrizione\" data-content=\"")
			.append(FormatUtils.formatHtmlAttributeString(conciliazionePerBeneficiario.getConto().getDescrizione()))
			.append("\">")
			.append(conciliazionePerBeneficiario.getConto().getCodice())
			.append("</a>")
			.toString();
	}
	
	/**
	 * @return the entrata
	 */
	public boolean isEntrata() {
		return conciliazionePerBeneficiario != null && conciliazionePerBeneficiario.getCapitolo() instanceof CapitoloEntrataGestione;
	}
	
	/**
	 * @return the spesa
	 */
	public boolean isSpesa() {
		return conciliazionePerBeneficiario != null && conciliazionePerBeneficiario.getCapitolo() instanceof CapitoloUscitaGestione;
	}
	
	/**
	 * @return the uidClassificatore
	 */
	public int getUidClassificatore() {
		if(isSpesa()) {
			CapitoloUscitaGestione capitoloUscitaGestione = (CapitoloUscitaGestione) conciliazionePerBeneficiario.getCapitolo();
			return capitoloUscitaGestione.getMacroaggregato() != null ? capitoloUscitaGestione.getMacroaggregato().getUid() : -1;
		}
		if(isEntrata()) {
			CapitoloEntrataGestione capitoloEntrataGestione = (CapitoloEntrataGestione) conciliazionePerBeneficiario.getCapitolo();
			return capitoloEntrataGestione.getCategoriaTipologiaTitolo() != null ? capitoloEntrataGestione.getCategoriaTipologiaTitolo().getUid() : -1;
		}
		return -1;
	}
	
	/**
	 * @return the descrizioneClasseDiConciliazione
	 */
	public String getDescrizioneClasseDiConciliazione() {
		return conciliazionePerBeneficiario != null && conciliazionePerBeneficiario.getClasseDiConciliazione() != null ? conciliazionePerBeneficiario.getClasseDiConciliazione().getDescrizione() : "";
	}
	
	/**
	 * @return the nameClasseDiConciliazione
	 */
	public String getNameClasseDiConciliazione() {
		return conciliazionePerBeneficiario != null && conciliazionePerBeneficiario.getClasseDiConciliazione() != null ? conciliazionePerBeneficiario.getClasseDiConciliazione().name() : "";
	}
	
	/**
	 * @return the codiceSoggetto
	 */
	public String getCodiceSoggetto() {
		return conciliazionePerBeneficiario != null && conciliazionePerBeneficiario.getSoggetto() != null ? conciliazionePerBeneficiario.getSoggetto().getCodiceSoggetto() : "";
	}
	
	/**
	 * @return the denominazioneSoggetto
	 */
	public String getDenominazioneSoggetto() {
		return conciliazionePerBeneficiario != null && conciliazionePerBeneficiario.getSoggetto() != null ? conciliazionePerBeneficiario.getSoggetto().getDenominazione() : "";
	}
	
	/**
	 * @return the uidSoggetto
	 */
	public Integer getUidSoggetto() {
		return conciliazionePerBeneficiario != null && conciliazionePerBeneficiario.getSoggetto() != null ? conciliazionePerBeneficiario.getSoggetto().getUid() : null;
	}
	
	/**
	 * @return the uidConto
	 */
	public Integer getUidConto() {
		return conciliazionePerBeneficiario != null && conciliazionePerBeneficiario.getConto() != null ? conciliazionePerBeneficiario.getConto().getUid() : null;
	}
	
	/**
	 * @return the uidCapitolo
	 */
	public Integer getUidCapitolo() {
		return conciliazionePerBeneficiario != null && conciliazionePerBeneficiario.getCapitolo() != null ? conciliazionePerBeneficiario.getCapitolo().getUid() : null;
	}
	
	/**
	 * @return the numeroCapitolo
	 */
	public Integer getNumeroCapitolo() {
		return conciliazionePerBeneficiario != null && conciliazionePerBeneficiario.getCapitolo() != null ? conciliazionePerBeneficiario.getCapitolo().getNumeroCapitolo() : null;
	}
	
	/**
	 * @return the numeroArticolo
	 */
	public Integer getNumeroArticolo() {
		return conciliazionePerBeneficiario != null && conciliazionePerBeneficiario.getCapitolo() != null ? conciliazionePerBeneficiario.getCapitolo().getNumeroArticolo() : null;
	}
	
	/**
	 * @return the numeroUEB
	 */
	public Integer getNumeroUEB() {
		return conciliazionePerBeneficiario != null && conciliazionePerBeneficiario.getCapitolo() != null ? conciliazionePerBeneficiario.getCapitolo().getNumeroUEB() : null;
	}
}
