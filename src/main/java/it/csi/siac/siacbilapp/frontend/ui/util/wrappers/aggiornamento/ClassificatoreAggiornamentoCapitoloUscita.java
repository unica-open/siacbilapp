/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.RisorsaAccantonata;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;
import it.csi.siac.siaccorser.model.ClassificatoreGenerico;

/**
 * Wrapper astratto per porre in sessione gli elementi da verificare durante l'aggiornamento del Capitolo di Uscita.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0
 *
 */
public class ClassificatoreAggiornamentoCapitoloUscita extends ClassificatoreAggiornamento {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2844290070068249248L;
	
	private Missione missione;
	private Programma programma;
	private TitoloSpesa titoloSpesa;
	private Macroaggregato macroaggregato;
	private ClassificazioneCofog classificazioneCofog;
	
	private SiopeSpesa siopeSpesa;
	private RicorrenteSpesa ricorrenteSpesa;
	private PerimetroSanitarioSpesa perimetroSanitarioSpesa;
	private TransazioneUnioneEuropeaSpesa transazioneUnioneEuropeaSpesa;
	private PoliticheRegionaliUnitarie politicheRegionaliUnitarie;
	//SIAC-7192
	private RisorsaAccantonata risorsaAccantonata;
	
	private ClassificatoreGenerico classificatoreGenerico1;
	private ClassificatoreGenerico classificatoreGenerico2;
	private ClassificatoreGenerico classificatoreGenerico3;
	private ClassificatoreGenerico classificatoreGenerico4;
	private ClassificatoreGenerico classificatoreGenerico5;
	private ClassificatoreGenerico classificatoreGenerico6;
	private ClassificatoreGenerico classificatoreGenerico7;
	private ClassificatoreGenerico classificatoreGenerico8;
	private ClassificatoreGenerico classificatoreGenerico9;
	private ClassificatoreGenerico classificatoreGenerico10;
	private ClassificatoreGenerico classificatoreGenerico11;
	private ClassificatoreGenerico classificatoreGenerico12;
	private ClassificatoreGenerico classificatoreGenerico13;
	private ClassificatoreGenerico classificatoreGenerico14;
	private ClassificatoreGenerico classificatoreGenerico15;

	
	/** Costruttore vuoto di default */
	public ClassificatoreAggiornamentoCapitoloUscita() {
		super();
	}

	/**
	 * @return the programma
	 */
	public Programma getProgramma() {
		return programma;
	}

	/**
	 * @param programma the programma to set
	 */
	public void setProgramma(Programma programma) {
		this.programma = programma;
	}

	/**
	 * @return the macroaggregato
	 */
	public Macroaggregato getMacroaggregato() {
		return macroaggregato;
	}

	/**
	 * @param macroaggregato the macroaggregato to set
	 */
	public void setMacroaggregato(Macroaggregato macroaggregato) {
		this.macroaggregato = macroaggregato;
	}

	/**
	 * @return the classificazioneCofog
	 */
	public ClassificazioneCofog getClassificazioneCofog() {
		return classificazioneCofog;
	}

	/**
	 * @param classificazioneCofog the classificazioneCofog to set
	 */
	public void setClassificazioneCofog(ClassificazioneCofog classificazioneCofog) {
		this.classificazioneCofog = classificazioneCofog;
	}
	
	/**
	 * @return the missione
	 */
	public Missione getMissione() {
		return missione;
	}

	/**
	 * @param missione the missione to set
	 */
	public void setMissione(Missione missione) {
		this.missione = missione;
	}

	/**
	 * @return the titoloSpesa
	 */
	public TitoloSpesa getTitoloSpesa() {
		return titoloSpesa;
	}

	/**
	 * @param titoloSpesa the titoloEntrata to set
	 */
	public void setTitoloSpesa(TitoloSpesa titoloSpesa) {
		this.titoloSpesa = titoloSpesa;
	}

	/**
	 * @return the ricorrenteSpesa
	 */
	public RicorrenteSpesa getRicorrenteSpesa() {
		return ricorrenteSpesa;
	}

	/**
	 * @param ricorrenteSpesa the ricorrenteSpesa to set
	 */
	public void setRicorrenteSpesa(RicorrenteSpesa ricorrenteSpesa) {
		this.ricorrenteSpesa = ricorrenteSpesa;
	}

	/**
	 * @return the siopeSpesa
	 */
	public SiopeSpesa getSiopeSpesa() {
		return siopeSpesa;
	}

	/**
	 * @param siopeSpesa the siopeSpesa to set
	 */
	public void setSiopeSpesa(SiopeSpesa siopeSpesa) {
		this.siopeSpesa = siopeSpesa;
	}

	/**
	 * @return the perimetroSanitarioSpesa
	 */
	public PerimetroSanitarioSpesa getPerimetroSanitarioSpesa() {
		return perimetroSanitarioSpesa;
	}

	/**
	 * @param perimetroSanitarioSpesa the perimetroSanitarioSpesa to set
	 */
	public void setPerimetroSanitarioSpesa(
			PerimetroSanitarioSpesa perimetroSanitarioSpesa) {
		this.perimetroSanitarioSpesa = perimetroSanitarioSpesa;
	}

	/**
	 * @return the transazioneUnioneEuropeaSpesa
	 */
	public TransazioneUnioneEuropeaSpesa getTransazioneUnioneEuropeaSpesa() {
		return transazioneUnioneEuropeaSpesa;
	}

	/**
	 * @param transazioneUnioneEuropeaSpesa the transazioneUnioneEuropeaSpesa to set
	 */
	public void setTransazioneUnioneEuropeaSpesa(
			TransazioneUnioneEuropeaSpesa transazioneUnioneEuropeaSpesa) {
		this.transazioneUnioneEuropeaSpesa = transazioneUnioneEuropeaSpesa;
	}

	/**
	 * @return the politicheRegionaliUnitarie
	 */
	public PoliticheRegionaliUnitarie getPoliticheRegionaliUnitarie() {
		return politicheRegionaliUnitarie;
	}

	/**
	 * @param politicheRegionaliUnitarie the politicheRegionaliUnitarie to set
	 */
	public void setPoliticheRegionaliUnitarie(PoliticheRegionaliUnitarie politicheRegionaliUnitarie) {
		this.politicheRegionaliUnitarie = politicheRegionaliUnitarie;
	}

	/**
	 * @return the risorsaAccantonata
	 */
	public RisorsaAccantonata getRisorsaAccantonata() {
		return risorsaAccantonata;
	}

	/**
	 * @param risorsaAccantonata the risorsaAccantonata to set
	 */
	public void setRisorsaAccantonata(RisorsaAccantonata risorsaAccantonata) {
		this.risorsaAccantonata = risorsaAccantonata;
	}
	
	/**
	 * @return the classificatoreGenerico1
	 */
	public ClassificatoreGenerico getClassificatoreGenerico1() {
		return classificatoreGenerico1;
	}

	/**
	 * @param classificatoreGenerico1 the classificatoreGenerico1 to set
	 */
	public void setClassificatoreGenerico1(ClassificatoreGenerico classificatoreGenerico1) {
		this.classificatoreGenerico1 = classificatoreGenerico1;
	}

	/**
	 * @return the classificatoreGenerico2
	 */
	public ClassificatoreGenerico getClassificatoreGenerico2() {
		return classificatoreGenerico2;
	}

	/**
	 * @param classificatoreGenerico2 the classificatoreGenerico2 to set
	 */
	public void setClassificatoreGenerico2(ClassificatoreGenerico classificatoreGenerico2) {
		this.classificatoreGenerico2 = classificatoreGenerico2;
	}

	/**
	 * @return the classificatoreGenerico3
	 */
	public ClassificatoreGenerico getClassificatoreGenerico3() {
		return classificatoreGenerico3;
	}

	/**
	 * @param classificatoreGenerico3 the classificatoreGenerico3 to set
	 */
	public void setClassificatoreGenerico3(ClassificatoreGenerico classificatoreGenerico3) {
		this.classificatoreGenerico3 = classificatoreGenerico3;
	}

	/**
	 * @return the classificatoreGenerico4
	 */
	public ClassificatoreGenerico getClassificatoreGenerico4() {
		return classificatoreGenerico4;
	}

	/**
	 * @param classificatoreGenerico4 the classificatoreGenerico4 to set
	 */
	public void setClassificatoreGenerico4(ClassificatoreGenerico classificatoreGenerico4) {
		this.classificatoreGenerico4 = classificatoreGenerico4;
	}

	/**
	 * @return the classificatoreGenerico5
	 */
	public ClassificatoreGenerico getClassificatoreGenerico5() {
		return classificatoreGenerico5;
	}

	/**
	 * @param classificatoreGenerico5 the classificatoreGenerico5 to set
	 */
	public void setClassificatoreGenerico5(ClassificatoreGenerico classificatoreGenerico5) {
		this.classificatoreGenerico5 = classificatoreGenerico5;
	}

	/**
	 * @return the classificatoreGenerico6
	 */
	public ClassificatoreGenerico getClassificatoreGenerico6() {
		return classificatoreGenerico6;
	}

	/**
	 * @param classificatoreGenerico6 the classificatoreGenerico6 to set
	 */
	public void setClassificatoreGenerico6(ClassificatoreGenerico classificatoreGenerico6) {
		this.classificatoreGenerico6 = classificatoreGenerico6;
	}

	/**
	 * @return the classificatoreGenerico7
	 */
	public ClassificatoreGenerico getClassificatoreGenerico7() {
		return classificatoreGenerico7;
	}

	/**
	 * @param classificatoreGenerico7 the classificatoreGenerico7 to set
	 */
	public void setClassificatoreGenerico7(ClassificatoreGenerico classificatoreGenerico7) {
		this.classificatoreGenerico7 = classificatoreGenerico7;
	}

	/**
	 * @return the classificatoreGenerico8
	 */
	public ClassificatoreGenerico getClassificatoreGenerico8() {
		return classificatoreGenerico8;
	}

	/**
	 * @param classificatoreGenerico8 the classificatoreGenerico8 to set
	 */
	public void setClassificatoreGenerico8(ClassificatoreGenerico classificatoreGenerico8) {
		this.classificatoreGenerico8 = classificatoreGenerico8;
	}

	/**
	 * @return the classificatoreGenerico9
	 */
	public ClassificatoreGenerico getClassificatoreGenerico9() {
		return classificatoreGenerico9;
	}

	/**
	 * @param classificatoreGenerico9 the classificatoreGenerico9 to set
	 */
	public void setClassificatoreGenerico9(ClassificatoreGenerico classificatoreGenerico9) {
		this.classificatoreGenerico9 = classificatoreGenerico9;
	}

	/**
	 * @return the classificatoreGenerico10
	 */
	public ClassificatoreGenerico getClassificatoreGenerico10() {
		return classificatoreGenerico10;
	}

	/**
	 * @param classificatoreGenerico10 the classificatoreGenerico10 to set
	 */
	public void setClassificatoreGenerico10(ClassificatoreGenerico classificatoreGenerico10) {
		this.classificatoreGenerico10 = classificatoreGenerico10;
	}
	
	/**
	 * @return the classificatoreGenerico11
	 */
	public ClassificatoreGenerico getClassificatoreGenerico11() {
		return classificatoreGenerico11;
	}

	/**
	 * @param classificatoreGenerico11 the classificatoreGenerico11 to set
	 */
	public void setClassificatoreGenerico11(ClassificatoreGenerico classificatoreGenerico11) {
		this.classificatoreGenerico11 = classificatoreGenerico11;
	}

	/**
	 * @return the classificatoreGenerico12
	 */
	public ClassificatoreGenerico getClassificatoreGenerico12() {
		return classificatoreGenerico12;
	}

	/**
	 * @param classificatoreGenerico12 the classificatoreGenerico12 to set
	 */
	public void setClassificatoreGenerico12(ClassificatoreGenerico classificatoreGenerico12) {
		this.classificatoreGenerico12 = classificatoreGenerico12;
	}

	/**
	 * @return the classificatoreGenerico13
	 */
	public ClassificatoreGenerico getClassificatoreGenerico13() {
		return classificatoreGenerico13;
	}

	/**
	 * @param classificatoreGenerico13 the classificatoreGenerico13 to set
	 */
	public void setClassificatoreGenerico13(ClassificatoreGenerico classificatoreGenerico13) {
		this.classificatoreGenerico13 = classificatoreGenerico13;
	}

	/**
	 * @return the classificatoreGenerico14
	 */
	public ClassificatoreGenerico getClassificatoreGenerico14() {
		return classificatoreGenerico14;
	}

	/**
	 * @param classificatoreGenerico14 the classificatoreGenerico14 to set
	 */
	public void setClassificatoreGenerico14(ClassificatoreGenerico classificatoreGenerico14) {
		this.classificatoreGenerico14 = classificatoreGenerico14;
	}

	/**
	 * @return the classificatoreGenerico15
	 */
	public ClassificatoreGenerico getClassificatoreGenerico15() {
		return classificatoreGenerico15;
	}

	/**
	 * @param classificatoreGenerico15 the classificatoreGenerico15 to set
	 */
	public void setClassificatoreGenerico15(ClassificatoreGenerico classificatoreGenerico15) {
		this.classificatoreGenerico15 = classificatoreGenerico15;
	}
	
	@Override
	public String toString() {
		ToStringBuilder toStringBuilder = new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE);
		toStringBuilder.appendSuper(super.toString())
			.append("missione", missione)
			.append("programma", programma)
			.append("titolo spesa", titoloSpesa)
			.append("macroaggregato", macroaggregato)
			.append("classificazioneCofog", classificazioneCofog)
			.append("siope Spesa", siopeSpesa)
			.append("ricorrente spesa", ricorrenteSpesa)
			.append("classificatore generico 1 ",  classificatoreGenerico1 )  
			.append("classificatore generico 2 ",  classificatoreGenerico2 )  
			.append("classificatore generico 3 ",  classificatoreGenerico3 )  
			.append("classificatore generico 4 ",  classificatoreGenerico4 )  
			.append("classificatore generico 5 ",  classificatoreGenerico5 )  
			.append("classificatore generico 6 ",  classificatoreGenerico6 )  
			.append("classificatore generico 7 ",  classificatoreGenerico7 )  
			.append("classificatore generico 8 ",  classificatoreGenerico8 )  
			.append("classificatore generico 9 ",  classificatoreGenerico9 )  
			.append("classificatore generico 10 ", classificatoreGenerico10)
			.append("classificatore generico 11 ", classificatoreGenerico11)
			.append("classificatore generico 12 ", classificatoreGenerico12)  
			.append("classificatore generico 13 ", classificatoreGenerico13)  
			.append("classificatore generico 14 ", classificatoreGenerico14)  
			.append("classificatore generico 15 ", classificatoreGenerico15)
			;
		return toStringBuilder.toString();
	}

	

}
