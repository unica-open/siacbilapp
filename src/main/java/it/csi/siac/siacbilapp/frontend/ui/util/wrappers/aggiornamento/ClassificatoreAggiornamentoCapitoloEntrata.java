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
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;

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
	private ClassificatoreGenerico classificatoreGenerico36;
	private ClassificatoreGenerico classificatoreGenerico37;
	private ClassificatoreGenerico classificatoreGenerico38;
	private ClassificatoreGenerico classificatoreGenerico39;
	private ClassificatoreGenerico classificatoreGenerico40;
	private ClassificatoreGenerico classificatoreGenerico41;
	private ClassificatoreGenerico classificatoreGenerico42;
	private ClassificatoreGenerico classificatoreGenerico43;
	private ClassificatoreGenerico classificatoreGenerico44;
	private ClassificatoreGenerico classificatoreGenerico45;
	private ClassificatoreGenerico classificatoreGenerico46;
	private ClassificatoreGenerico classificatoreGenerico47;
	private ClassificatoreGenerico classificatoreGenerico48;
	private ClassificatoreGenerico classificatoreGenerico49;
	private ClassificatoreGenerico classificatoreGenerico50;

	
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
	public void setPerimetroSanitarioEntrata(PerimetroSanitarioEntrata perimetroSanitarioEntrata) {
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
	

	public ClassificatoreGenerico getClassificatoreGenerico36() {
		return classificatoreGenerico36;
	}

	public void setClassificatoreGenerico36(ClassificatoreGenerico classificatoreGenerico36) {
		this.classificatoreGenerico36 = classificatoreGenerico36;
	}

	public ClassificatoreGenerico getClassificatoreGenerico37() {
		return classificatoreGenerico37;
	}

	public void setClassificatoreGenerico37(ClassificatoreGenerico classificatoreGenerico37) {
		this.classificatoreGenerico37 = classificatoreGenerico37;
	}

	public ClassificatoreGenerico getClassificatoreGenerico38() {
		return classificatoreGenerico38;
	}

	public void setClassificatoreGenerico38(ClassificatoreGenerico classificatoreGenerico38) {
		this.classificatoreGenerico38 = classificatoreGenerico38;
	}

	public ClassificatoreGenerico getClassificatoreGenerico39() {
		return classificatoreGenerico39;
	}

	public void setClassificatoreGenerico39(ClassificatoreGenerico classificatoreGenerico39) {
		this.classificatoreGenerico39 = classificatoreGenerico39;
	}

	public ClassificatoreGenerico getClassificatoreGenerico40() {
		return classificatoreGenerico40;
	}

	public void setClassificatoreGenerico40(ClassificatoreGenerico classificatoreGenerico40) {
		this.classificatoreGenerico40 = classificatoreGenerico40;
	}

	public ClassificatoreGenerico getClassificatoreGenerico41() {
		return classificatoreGenerico41;
	}

	public void setClassificatoreGenerico41(ClassificatoreGenerico classificatoreGenerico41) {
		this.classificatoreGenerico41 = classificatoreGenerico41;
	}

	public ClassificatoreGenerico getClassificatoreGenerico42() {
		return classificatoreGenerico42;
	}

	public void setClassificatoreGenerico42(ClassificatoreGenerico classificatoreGenerico42) {
		this.classificatoreGenerico42 = classificatoreGenerico42;
	}

	public ClassificatoreGenerico getClassificatoreGenerico43() {
		return classificatoreGenerico43;
	}

	public void setClassificatoreGenerico43(ClassificatoreGenerico classificatoreGenerico43) {
		this.classificatoreGenerico43 = classificatoreGenerico43;
	}

	public ClassificatoreGenerico getClassificatoreGenerico44() {
		return classificatoreGenerico44;
	}

	public void setClassificatoreGenerico44(ClassificatoreGenerico classificatoreGenerico44) {
		this.classificatoreGenerico44 = classificatoreGenerico44;
	}

	public ClassificatoreGenerico getClassificatoreGenerico45() {
		return classificatoreGenerico45;
	}

	public void setClassificatoreGenerico45(ClassificatoreGenerico classificatoreGenerico45) {
		this.classificatoreGenerico45 = classificatoreGenerico45;
	}

	public ClassificatoreGenerico getClassificatoreGenerico46() {
		return classificatoreGenerico46;
	}

	public void setClassificatoreGenerico46(ClassificatoreGenerico classificatoreGenerico46) {
		this.classificatoreGenerico46 = classificatoreGenerico46;
	}

	public ClassificatoreGenerico getClassificatoreGenerico47() {
		return classificatoreGenerico47;
	}

	public void setClassificatoreGenerico47(ClassificatoreGenerico classificatoreGenerico47) {
		this.classificatoreGenerico47 = classificatoreGenerico47;
	}

	public ClassificatoreGenerico getClassificatoreGenerico48() {
		return classificatoreGenerico48;
	}

	public void setClassificatoreGenerico48(ClassificatoreGenerico classificatoreGenerico48) {
		this.classificatoreGenerico48 = classificatoreGenerico48;
	}

	public ClassificatoreGenerico getClassificatoreGenerico49() {
		return classificatoreGenerico49;
	}

	public void setClassificatoreGenerico49(ClassificatoreGenerico classificatoreGenerico49) {
		this.classificatoreGenerico49 = classificatoreGenerico49;
	}

	public ClassificatoreGenerico getClassificatoreGenerico50() {
		return classificatoreGenerico50;
	}

	public void setClassificatoreGenerico50(ClassificatoreGenerico classificatoreGenerico50) {
		this.classificatoreGenerico50 = classificatoreGenerico50;
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
			.append("transazioni dell'unione europea", transazioneUnioneEuropeaEntrata)
			.append("classificatore generico 36 ", classificatoreGenerico36)  
			.append("classificatore generico 37 ", classificatoreGenerico37)  
			.append("classificatore generico 38 ", classificatoreGenerico38)  
			.append("classificatore generico 39 ", classificatoreGenerico39)  
			.append("classificatore generico 40 ", classificatoreGenerico40)  
			.append("classificatore generico 41 ", classificatoreGenerico41)  
			.append("classificatore generico 42 ", classificatoreGenerico42)  
			.append("classificatore generico 43 ", classificatoreGenerico43)  
			.append("classificatore generico 44 ", classificatoreGenerico44)  
			.append("classificatore generico 45 ", classificatoreGenerico45)
			.append("classificatore generico 46 ", classificatoreGenerico46)
			.append("classificatore generico 47 ", classificatoreGenerico47)  
			.append("classificatore generico 48 ", classificatoreGenerico48)  
			.append("classificatore generico 49 ", classificatoreGenerico49)  
			.append("classificatore generico 50 ", classificatoreGenerico50)
			;
		return toStringBuilder.toString();
	}
	
}
