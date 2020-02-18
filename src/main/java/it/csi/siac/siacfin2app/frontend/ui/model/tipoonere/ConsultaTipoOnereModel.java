/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.tipoonere;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.DettaglioStoricoTipoOnere;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.TipoOnere;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di model per la consultazione del tipoOnere.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/11/2014
 *
 */
public class ConsultaTipoOnereModel extends GenericTipoOnereModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5881177759077151552L;
	
	private Integer uidTipoOnere;
	
	// Liste di storico
	private List<TipoOnere> listaTipoOnere = new ArrayList<TipoOnere>();
	private List<CausaleSpesa> listaCausaleSpesa = new ArrayList<CausaleSpesa>();
	private List<CausaleEntrata> listaCausaleEntrata = new ArrayList<CausaleEntrata>();
	private List<Soggetto> listaSoggetto = new ArrayList<Soggetto>();
	
	private boolean tipoOnereSplitReverse;
	
	/** Costruttore vuoto di default */
	public ConsultaTipoOnereModel() {
		super();
		setTitolo("Consulta onere");
	}

	/**
	 * @return the uidTipoOnere
	 */
	public Integer getUidTipoOnere() {
		return uidTipoOnere;
	}

	/**
	 * @param uidTipoOnere the uidTipoOnere to set
	 */
	public void setUidTipoOnere(Integer uidTipoOnere) {
		this.uidTipoOnere = uidTipoOnere;
	}

	/**
	 * @return the listaTipoOnere
	 */
	public List<TipoOnere> getListaTipoOnere() {
		return listaTipoOnere;
	}

	/**
	 * @param listaTipoOnere the listaTipoOnere to set
	 */
	public void setListaTipoOnere(List<TipoOnere> listaTipoOnere) {
		this.listaTipoOnere = listaTipoOnere != null ? listaTipoOnere : new ArrayList<TipoOnere>();
	}
	
	/**
	 * @return the listaCausaleSpesa
	 */
	public List<CausaleSpesa> getListaCausaleSpesa() {
		return listaCausaleSpesa;
	}

	/**
	 * @param listaCausaleSpesa the listaCausaleSpesa to set
	 */
	public void setListaCausaleSpesa(List<CausaleSpesa> listaCausaleSpesa) {
		this.listaCausaleSpesa = listaCausaleSpesa != null ? listaCausaleSpesa : new ArrayList<CausaleSpesa>();
	}

	/**
	 * @return the listaCausaleEntrata
	 */
	public List<CausaleEntrata> getListaCausaleEntrata() {
		return listaCausaleEntrata;
	}

	/**
	 * @param listaCausaleEntrata the listaCausaleEntrata to set
	 */
	public void setListaCausaleEntrata(List<CausaleEntrata> listaCausaleEntrata) {
		this.listaCausaleEntrata = listaCausaleEntrata != null ? listaCausaleEntrata : new ArrayList<CausaleEntrata>();
	}
	
	/**
	 * @return the listaSoggetto
	 */
	public List<Soggetto> getListaSoggetto() {
		return listaSoggetto;
	}

	/**
	 * @param listaSoggetto the listaSoggetto to set
	 */
	public void setListaSoggetto(List<Soggetto> listaSoggetto) {
		this.listaSoggetto = listaSoggetto != null ? listaSoggetto : new ArrayList<Soggetto>();
	}
	
	/**
	 * @return the tipoOnereSplitReverse
	 */
	public boolean isTipoOnereSplitReverse() {
		return tipoOnereSplitReverse;
	}

	/**
	 * @param tipoOnereSplitReverse the tipoOnereSplitReverse to set
	 */
	public void setTipoOnereSplitReverse(boolean tipoOnereSplitReverse) {
		this.tipoOnereSplitReverse = tipoOnereSplitReverse;
	}
	
	/**
	 * @return the titoloConsultazione
	 */
	public String getTitoloConsultazione() {
		StringBuilder sb = new StringBuilder();
		if(getTipoOnere() != null) {
			// codice – descrizione (NaturaOnere codice-descrizione)
			sb.append(getTipoOnere().getCodice())
				.append(" - ")
				.append(getTipoOnere().getDescrizione());
			if(getTipoOnere().getNaturaOnere() != null) {
				sb.append(" (")
					.append(getTipoOnere().getNaturaOnere().getCodice())
					.append(" - ")
					.append(getTipoOnere().getNaturaOnere().getDescrizione())
					.append(")");
			}
		}
		return sb.toString();
	}
	
	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link DettaglioStoricoTipoOnere}.
	 * 
	 * @return la request creata
	 */
	public DettaglioStoricoTipoOnere creaRequestDettaglioStoricoTipoOnere() {
		DettaglioStoricoTipoOnere request = creaRequest(DettaglioStoricoTipoOnere.class);
		
		TipoOnere tipoOnere = new TipoOnere();
		tipoOnere.setUid(getUidTipoOnere());
		request.setTipoOnere(tipoOnere);
		
		return request;
	}
	
}
