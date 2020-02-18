/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativo;

/**
 * Helper per la consultazione dei dati dell'ordinativo
 * @author Marchino Alessandro
 * @param <O> la tipizzazione dell'ordinativo
 * @param <SO> la tipizzazione del subordinativo
 *
 */
public abstract class ConsultaRegistrazioneMovFinOrdinativoHelper<O extends Ordinativo, SO extends SubOrdinativo> extends ConsultaRegistrazioneMovFinTransazioneElementareHelper<O> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4331679149495872856L;

	/** L'ordinativo */
	protected final O ordinativo;
	private final List<SO> listaSubOrdinativo;
	
	/**
	 * Costruttore di wrap
	 * @param ordinativo l'ordinativo
	 * @param listaSubOrdinativo la lista dei subordinativi sub ordinativo
	 * @param isGestioneUEB se la gestione UEB sia attiva
	 */
	protected ConsultaRegistrazioneMovFinOrdinativoHelper(O ordinativo, List<SO> listaSubOrdinativo, boolean isGestioneUEB) {
		super(isGestioneUEB);
		this.ordinativo = ordinativo;
		this.listaSubOrdinativo = listaSubOrdinativo != null ? listaSubOrdinativo : new ArrayList<SO>();
	}
	
	/**
	 * @return the ordinativo
	 */
	public O getOrdinativo() {
		return this.ordinativo;
	}

	/**
	 * @return the listaSubOrdinativo
	 */
	public List<SO> getListaSubOrdinativo() {
		return this.listaSubOrdinativo;
	}

	@Override
	protected AttoAmministrativo ottieniAttoAmministrativo() {
		return ordinativo != null ? ordinativo.getAttoAmministrativo() : null;
	}
	
	@Override
	public String getDatiCreazioneModifica() {
		if(ordinativo == null) {
			return "";
		}
		return calcolaDatiCreazioneModifica(ordinativo.getDataEmissione(), ordinativo.getLoginCreazione(), ordinativo.getDataModifica(), ordinativo.getLoginModifica());
	}
	
}
