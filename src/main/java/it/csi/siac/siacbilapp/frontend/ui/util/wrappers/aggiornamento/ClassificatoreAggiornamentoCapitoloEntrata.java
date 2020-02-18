/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import it.csi.siac.siacbilser.model.CategoriaTipologiaTitolo;
import it.csi.siac.siacbilser.model.PerimetroSanitarioEntrata;
import it.csi.siac.siacbilser.model.RicorrenteEntrata;
import it.csi.siac.siacbilser.model.SiopeEntrata;
import it.csi.siac.siacbilser.model.TipologiaTitolo;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaEntrata;

/**
 * Wrapper astratto per porre in sessione gli elementi da verificare durante l'aggiornamento del Capitolo di Entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0
 *
 */
public class ClassificatoreAggiornamentoCapitoloEntrata extends ClassificatoreAggiornamento {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -2637787184461368748L;
	
	private TitoloEntrata titoloEntrata;
	private TipologiaTitolo tipologiaTitolo;
	private CategoriaTipologiaTitolo categoriaTipologiaTitolo;
	
	private SiopeEntrata siopeEntrata;
	private RicorrenteEntrata ricorrenteEntrata;
	private PerimetroSanitarioEntrata perimetroSanitarioEntrata;
	private TransazioneUnioneEuropeaEntrata transazioneUnioneEuropeaEntrata;
	
	/** Costruttore vuoto di default */
	public ClassificatoreAggiornamentoCapitoloEntrata() {
		super();
	}
	
	/**
	 * @return the titoloEntrata
	 */
	public TitoloEntrata getTitoloEntrata() {
		return titoloEntrata;
	}

	/**
	 * @param titoloEntrata the titoloEntrata to set
	 */
	public void setTitoloEntrata(TitoloEntrata titoloEntrata) {
		this.titoloEntrata = titoloEntrata;
	}

	/**
	 * @return the tipologiaTitolo
	 */
	public TipologiaTitolo getTipologiaTitolo() {
		return tipologiaTitolo;
	}

	/**
	 * @param tipologiaTitolo the tipologiaTitolo to set
	 */
	public void setTipologiaTitolo(TipologiaTitolo tipologiaTitolo) {
		this.tipologiaTitolo = tipologiaTitolo;
	}

	/**
	 * @return the categoriaTipologiaTitolo
	 */
	public CategoriaTipologiaTitolo getCategoriaTipologiaTitolo() {
		return categoriaTipologiaTitolo;
	}

	/**
	 * @param categoriaTipologiaTitolo the categoriaTipologiaTitolo to set
	 */
	public void setCategoriaTipologiaTitolo(
			CategoriaTipologiaTitolo categoriaTipologiaTitolo) {
		this.categoriaTipologiaTitolo = categoriaTipologiaTitolo;
	}
	
	/**
	 * @return the siopeEntrata
	 */
	public SiopeEntrata getSiopeEntrata() {
		return siopeEntrata;
	}

	/**
	 * @param siopeEntrata the siopeEntrata to set
	 */
	public void setSiopeEntrata(SiopeEntrata siopeEntrata) {
		this.siopeEntrata = siopeEntrata;
	}

	/**
	 * @return the ricorrenteEntrata
	 */
	public RicorrenteEntrata getRicorrenteEntrata() {
		return ricorrenteEntrata;
	}

	/**
	 * @param ricorrenteEntrata the ricorrenteEntrata to set
	 */
	public void setRicorrenteEntrata(RicorrenteEntrata ricorrenteEntrata) {
		this.ricorrenteEntrata = ricorrenteEntrata;
	}

	/**
	 * @return the perimetroSanitarioEntrata
	 */
	public PerimetroSanitarioEntrata getPerimetroSanitarioEntrata() {
		return perimetroSanitarioEntrata;
	}

	/**
	 * @param perimetroSanitarioEntrata the perimetroSanitarioEntrata to set
	 */
	public void setPerimetroSanitarioEntrata(
			PerimetroSanitarioEntrata perimetroSanitarioEntrata) {
		this.perimetroSanitarioEntrata = perimetroSanitarioEntrata;
	}

	/**
	 * @return the transazioneUnioneEuropeaEntrata
	 */
	public TransazioneUnioneEuropeaEntrata getTransazioneUnioneEuropeaEntrata() {
		return transazioneUnioneEuropeaEntrata;
	}

	/**
	 * @param transazioneUnioneEuropeaEntrata the transazioneUnioneEuropeaEntrata to set
	 */
	public void setTransazioneUnioneEuropeaEntrata(
			TransazioneUnioneEuropeaEntrata transazioneUnioneEuropeaEntrata) {
		this.transazioneUnioneEuropeaEntrata = transazioneUnioneEuropeaEntrata;
	}

	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
		toStringBuilder.appendSuper(super.toString())
			.append("titolo entrata", titoloEntrata)
			.append("tipologia titolo", tipologiaTitolo)
			.append("categoria tipologia titolo", categoriaTipologiaTitolo)
			.append("siope", siopeEntrata)
			.append("ricorrente", ricorrenteEntrata)
			.append("perimetro sanitario", perimetroSanitarioEntrata)
			.append("transazioni dell'unione europea", transazioneUnioneEuropeaEntrata);
		return toStringBuilder.toString();
	}
	
}
