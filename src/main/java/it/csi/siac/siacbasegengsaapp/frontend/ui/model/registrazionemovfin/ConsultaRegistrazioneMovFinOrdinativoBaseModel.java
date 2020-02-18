/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.text.WordUtils;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.consultazione.ConsultaRegistrazioneMovFinOrdinativoHelper;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacfinser.model.ordinativo.SubOrdinativo;

/**
 * Classe base di model per la consultazione dell'Ordinativo di incasso della registrazione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 07/10/2015
 * @param <O>  la tipizzazione dell'ordinativo
 * @param <SO> la tipizzazione del subordinativo
 * @param <H> la tipizzazione dell'helper
 *
 */
public abstract class ConsultaRegistrazioneMovFinOrdinativoBaseModel<O extends Ordinativo, SO extends SubOrdinativo, H extends ConsultaRegistrazioneMovFinOrdinativoHelper<O, SO>>
		extends ConsultaRegistrazioneMovFinTransazioneElementareBaseModel<O, H> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5051685640869828677L;
	
	private O ordinativo;
	private List<SO> listaSubOrdinativo;
	private Integer numero;
	private Integer anno;

	/**
	 * @return the ordinativo
	 */
	public O getOrdinativo() {
		return ordinativo;
	}

	/**
	 * @param ordinativo the ordinativo to set
	 */
	public void setOrdinativo(O ordinativo) {
		this.ordinativo = ordinativo;
	}

	/**
	 * @return the listaSubOrdinativo
	 */
	public List<SO> getListaSubOrdinativo() {
		return listaSubOrdinativo;
	}

	/**
	 * @param listaSubOrdinativo the listaSubOrdinativo to set
	 */
	public void setListaSubOrdinativo(List<SO> listaSubOrdinativo) {
		this.listaSubOrdinativo = listaSubOrdinativo != null ? listaSubOrdinativo : new ArrayList<SO>();
	}

	/**
	 * @return the numero
	 */
	public Integer getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	/**
	 * @return the anno
	 */
	public Integer getAnno() {
		return anno;
	}

	/**
	 * @param anno the anno to set
	 */
	public void setAnno(Integer anno) {
		this.anno = anno;
	}

	@Override
	public String getIntestazione() {
		if(getOrdinativo() == null) {
			return "";
		}
		return new StringBuilder()
			.append("Ordinativo ")
			.append(getOrdinativo().getAnno())
			.append(" / ")
			.append(getOrdinativo().getNumero())
			.toString();
	}
	
	@Override
	public String getStato(){
		if(getOrdinativo() == null || getOrdinativo().getStatoOperativoOrdinativo() == null) {
			return "";
		}
		return new StringBuilder()
			.append("Stato: ")
			.append(WordUtils.capitalizeFully(getOrdinativo().getStatoOperativoOrdinativo().toString()))
			.append(" dal ")
			// TODO: individuare il campo
			.append("")
			.toString();
	}
	
}
