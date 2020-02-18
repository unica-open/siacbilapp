/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.aggiornamento;

import it.csi.siac.siacbilser.model.ClassificazioneCofog;
import it.csi.siac.siacbilser.model.Macroaggregato;
import it.csi.siac.siacbilser.model.Missione;
import it.csi.siac.siacbilser.model.PerimetroSanitarioSpesa;
import it.csi.siac.siacbilser.model.PoliticheRegionaliUnitarie;
import it.csi.siac.siacbilser.model.Programma;
import it.csi.siac.siacbilser.model.RicorrenteSpesa;
import it.csi.siac.siacbilser.model.SiopeSpesa;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacbilser.model.TransazioneUnioneEuropeaSpesa;

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
	public void setPoliticheRegionaliUnitarie(
			PoliticheRegionaliUnitarie politicheRegionaliUnitarie) {
		this.politicheRegionaliUnitarie = politicheRegionaliUnitarie;
	}

}
