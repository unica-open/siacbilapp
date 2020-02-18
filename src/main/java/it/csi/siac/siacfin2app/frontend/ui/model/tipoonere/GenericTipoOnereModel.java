/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.tipoonere;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAttivitaOnere;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCausale770;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaNaturaOnere;
import it.csi.siac.siacfin2ser.model.AttivitaOnere;
import it.csi.siac.siacfin2ser.model.Causale770;
import it.csi.siac.siacfin2ser.model.CodiceSommaNonSoggetta;
import it.csi.siac.siacfin2ser.model.NaturaOnere;
import it.csi.siac.siacfin2ser.model.TipoIvaSplitReverse;
import it.csi.siac.siacfin2ser.model.TipoOnere;

/**
 * Classe di model generica per il Tipo Onere.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/11/2014
 *
 */
public class GenericTipoOnereModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 477340164498111590L;
	
	private TipoOnere tipoOnere;
	private List<NaturaOnere> listaNaturaOnere = new ArrayList<NaturaOnere>();
	private List<Causale770> listaCausale770 = new ArrayList<Causale770>();
	private List<AttivitaOnere> listaAttivitaOnere = new ArrayList<AttivitaOnere>();
	// Lotto L
	private List<TipoIvaSplitReverse> listaTipoIvaSplitReverse = new ArrayList<TipoIvaSplitReverse>();
	private List<CodiceSommaNonSoggetta> listaSommeNonSoggette = new ArrayList<CodiceSommaNonSoggetta>();
	
	/**
	 * @return the tipoOnere
	 */
	public TipoOnere getTipoOnere() {
		return tipoOnere;
	}

	/**
	 * @param tipoOnere the tipoOnere to set
	 */
	public void setTipoOnere(TipoOnere tipoOnere) {
		this.tipoOnere = tipoOnere;
	}

	/**
	 * @return the listaNaturaOnere
	 */
	public List<NaturaOnere> getListaNaturaOnere() {
		return listaNaturaOnere;
	}

	/**
	 * @param listaNaturaOnere the listaNaturaOnere to set
	 */
	public void setListaNaturaOnere(List<NaturaOnere> listaNaturaOnere) {
		this.listaNaturaOnere = listaNaturaOnere != null ? listaNaturaOnere : new ArrayList<NaturaOnere>();
	}

	/**
	 * @return the listaCausale770
	 */
	public List<Causale770> getListaCausale770() {
		return listaCausale770;
	}

	/**
	 * @param listaCausale770 the listaCausale770 to set
	 */
	public void setListaCausale770(List<Causale770> listaCausale770) {
		this.listaCausale770 = listaCausale770 != null ? listaCausale770 : new ArrayList<Causale770>();
	}

	/**
	 * @return the listaAttivitaOnere
	 */
	public List<AttivitaOnere> getListaAttivitaOnere() {
		return listaAttivitaOnere;
	}

	/**
	 * @param listaAttivitaOnere the listaAttivitaOnere to set
	 */
	public void setListaAttivitaOnere(List<AttivitaOnere> listaAttivitaOnere) {
		this.listaAttivitaOnere = listaAttivitaOnere != null ? listaAttivitaOnere : new ArrayList<AttivitaOnere>();
	}
	
	/**
	 * @return the listaTipoIvaSplitReverse
	 */
	public List<TipoIvaSplitReverse> getListaTipoIvaSplitReverse() {
		return listaTipoIvaSplitReverse;
	}

	/**
	 * @param listaTipoIvaSplitReverse the listaTipoIvaSplitReverse to set
	 */
	public void setListaTipoIvaSplitReverse(List<TipoIvaSplitReverse> listaTipoIvaSplitReverse) {
		this.listaTipoIvaSplitReverse = listaTipoIvaSplitReverse != null ? listaTipoIvaSplitReverse : new ArrayList<TipoIvaSplitReverse>();
	}
	
	
	/**
	 * @return the listaSommeNonSoggette
	 */
	public List<CodiceSommaNonSoggetta> getListaSommeNonSoggette() {
		return listaSommeNonSoggette;
	}

	/**
	 * @param listaSommeNonSoggette the listaSommeNonSoggette to set
	 */
	public void setListaSommeNonSoggette(
			List<CodiceSommaNonSoggetta> listaSommeNonSoggette) {
		this.listaSommeNonSoggette = listaSommeNonSoggette;
	}

	/**
	 * @return the descrizioneCompletaTipoOnere
	 */
	public String getDescrizioneCompletaTipoOnere() {
		StringBuilder sb = new StringBuilder();
		sb.append(getTipoOnere().getCodice())
			.append(" - ")
			.append(getTipoOnere().getDescrizione())
			.append(" (")
			.append(getTipoOnere().getNaturaOnere().getCodice())
			.append(")");
		return sb.toString();
	}
	
	/**
	 * @return the codiceEsenzione
	 */
	public String getCodiceEsenzione() {
		return BilConstants.CODICE_NATURA_ONERE_ESENZIONE.getConstant();
	}
	
	/**
	 * @return the codiceSplitReverse
	 */
	public String getCodiceSplitReverse() {
		return BilConstants.CODICE_NATURA_ONERE_SPLIT_REVERSE.getConstant();
	}
	
	/**
	 * @return the codiceReverseChange
	 */
	public String getCodiceReverseChange() {
		return TipoIvaSplitReverse.REVERSE_CHANGE.getCodice();
	}
	
	/**
	 * @return the naturaOnereEsenzione
	 */
	public boolean isNaturaOnereEsenzione() {
		return getTipoOnere() != null && getTipoOnere().getNaturaOnere() != null && BilConstants.CODICE_NATURA_ONERE_ESENZIONE.getConstant().equals(getTipoOnere().getNaturaOnere().getCodice());
	}
	
	/**
	 * @return the naturaOnereSplitReverse
	 */
	public boolean isNaturaOnereSplitReverse() {
		return getTipoOnere() != null && getTipoOnere().getNaturaOnere() != null && BilConstants.CODICE_NATURA_ONERE_SPLIT_REVERSE.getConstant().equals(getTipoOnere().getNaturaOnere().getCodice());
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaNaturaOnere}.
	 * 
	 * @return la request creata
	 */
	public RicercaNaturaOnere creaRequestRicercaNaturaOnere() {
		RicercaNaturaOnere request = creaRequest(RicercaNaturaOnere.class);
		
		request.setEnte(getEnte());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaAttivitaOnere}.
	 * 
	 * @return la request creata
	 */
	public RicercaAttivitaOnere creaRequestRicercaAttivitaOnere() {
		RicercaAttivitaOnere request = creaRequest(RicercaAttivitaOnere.class);
		
		request.setEnte(getEnte());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaCausale770}.
	 * 
	 * @return la request creata
	 */
	public RicercaCausale770 creaRequestRicercaCausale770() {
		RicercaCausale770 request = creaRequest(RicercaCausale770.class);
		
		request.setEnte(getEnte());
		
		return request;
	}
	
}
