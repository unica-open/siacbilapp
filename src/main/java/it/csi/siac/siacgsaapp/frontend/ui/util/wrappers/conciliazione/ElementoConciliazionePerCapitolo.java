/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.util.wrappers.conciliazione;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacgenser.model.ConciliazionePerCapitolo;

/**
 * Wrapper per la conciliazione per capitolo.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/11/2015
 */
public class ElementoConciliazionePerCapitolo implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3602774424944575488L;
	
	private final ConciliazionePerCapitolo conciliazionePerCapitolo;
	private String azioni;
	
	/**
	 * Costruttore di wrap.
	 * 
	 * @param conciliazionePerCapitolo la conciliazione da wrappare
	 */
	public ElementoConciliazionePerCapitolo(ConciliazionePerCapitolo conciliazionePerCapitolo) {
		this.conciliazionePerCapitolo = conciliazionePerCapitolo;
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
		return conciliazionePerCapitolo != null ? conciliazionePerCapitolo.getUid() : -1;
	}
	
	/**
	 * @return the stringaClasseDiConciliazione
	 */
	public String getStringaClasseDiConciliazione() {
		return conciliazionePerCapitolo != null && conciliazionePerCapitolo.getClasseDiConciliazione() != null ? conciliazionePerCapitolo.getClasseDiConciliazione().getDescrizione() : "";
	}
	
	/**
	 * @return the stringaClasse
	 */
	public String getStringaClasse() {
		return conciliazionePerCapitolo != null && conciliazionePerCapitolo.getConto() != null
			&& conciliazionePerCapitolo.getConto().getPianoDeiConti() != null
			&& conciliazionePerCapitolo.getConto().getPianoDeiConti().getClassePiano() != null
				? conciliazionePerCapitolo.getConto().getPianoDeiConti().getClassePiano().getCodice()
				: "";
	}
	
	/**
	 * @return the stringaConto
	 */
	public String getStringaConto() {
		return conciliazionePerCapitolo != null && conciliazionePerCapitolo.getConto() != null
				? conciliazionePerCapitolo.getConto().getCodice()
				: "";
	}
	
	/**
	 * @return the stringaDescrizioneConto
	 */
	public String getStringaDescrizioneConto() {
		return conciliazionePerCapitolo != null && conciliazionePerCapitolo.getConto() != null
				? conciliazionePerCapitolo.getConto().getDescrizione()
				: "";
	}

	/**
	 * @return the entrata
	 */
	public boolean isEntrata() {
		return conciliazionePerCapitolo != null && conciliazionePerCapitolo.getCapitolo() instanceof CapitoloEntrataGestione;
	}
	
	/**
	 * @return the spesa
	 */
	public boolean isSpesa() {
		return conciliazionePerCapitolo != null && conciliazionePerCapitolo.getCapitolo() instanceof CapitoloUscitaGestione;
	}
	
	/**
	 * @return the uidClassificatore
	 */
	public int getUidClassificatore() {
		if(conciliazionePerCapitolo == null) {
			return -1;
		}
		if(isSpesa()) {
			return conciliazionePerCapitolo.getCapitoloUscitaGestione().getMacroaggregato() != null
					? conciliazionePerCapitolo.getCapitoloUscitaGestione().getMacroaggregato().getUid()
					: -1;
		}
		if(isEntrata()) {
			return conciliazionePerCapitolo.getCapitoloEntrataGestione().getCategoriaTipologiaTitolo() != null
					? conciliazionePerCapitolo.getCapitoloEntrataGestione().getCategoriaTipologiaTitolo().getUid()
					: -1;
		}
		return -1;
	}
	
	/**
	 * @return the descrizioneClasseDiConciliazione
	 */
	public String getDescrizioneClasseDiConciliazione() {
		return conciliazionePerCapitolo != null && conciliazionePerCapitolo.getClasseDiConciliazione() != null ? conciliazionePerCapitolo.getClasseDiConciliazione().getDescrizione() : "";
	}
	
	/**
	 * @return the nameClasseDiConciliazione
	 */
	public String getNameClasseDiConciliazione() {
		return conciliazionePerCapitolo != null && conciliazionePerCapitolo.getClasseDiConciliazione() != null ? conciliazionePerCapitolo.getClasseDiConciliazione().name() : "";
	}
	
	/**
	 * @return the uidConto
	 */
	public Integer getUidConto() {
		return conciliazionePerCapitolo != null && conciliazionePerCapitolo.getConto() != null ? conciliazionePerCapitolo.getConto().getUid() : null;
	}
	
	/**
	 * @return the uidCapitolo
	 */
	public Integer getUidCapitolo() {
		return conciliazionePerCapitolo != null && conciliazionePerCapitolo.getCapitolo() != null ? conciliazionePerCapitolo.getCapitolo().getUid() : null;
	}
	
	/**
	 * @return the numeroCapitolo
	 */
	public Integer getNumeroCapitolo() {
		return conciliazionePerCapitolo != null && conciliazionePerCapitolo.getCapitolo() != null ? conciliazionePerCapitolo.getCapitolo().getNumeroCapitolo() : null;
	}
	
	/**
	 * @return the numeroArticolo
	 */
	public Integer getNumeroArticolo() {
		return conciliazionePerCapitolo != null && conciliazionePerCapitolo.getCapitolo() != null ? conciliazionePerCapitolo.getCapitolo().getNumeroArticolo() : null;
	}
	
	/**
	 * @return the numeroUEB
	 */
	public Integer getNumeroUEB() {
		return conciliazionePerCapitolo != null && conciliazionePerCapitolo.getCapitolo() != null ? conciliazionePerCapitolo.getCapitolo().getNumeroUEB() : null;
	}
}
