/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.ajax.causale;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleSpesa;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.TipoCausale;

/**
 * Classe di model per il caricamento via AJAX della Causale di spesa per il PreDocumento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/04/2014
 *
 */
public class CausalePreDocumentoSpesaAjaxModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4564025336813906291L;
	
	private Integer uidTipoCausale;
	private Integer uidStrutturaAmministrativoContabile;
	private CausaleSpesa causaleSpesa;
	
	private List<CausaleSpesa> listaCausale = new ArrayList<CausaleSpesa>();
	
	/** Costruttore vuoto di default */
	public CausalePreDocumentoSpesaAjaxModel() {
		super();
	}

	/**
	 * @return the uidTipoCausale
	 */
	public Integer getUidTipoCausale() {
		return uidTipoCausale;
	}

	/**
	 * @param uidTipoCausale the uidTipoCausale to set
	 */
	public void setUidTipoCausale(Integer uidTipoCausale) {
		this.uidTipoCausale = uidTipoCausale;
	}

	/**
	 * @return the uidStrutturaAmministrativoContabile
	 */
	public Integer getUidStrutturaAmministrativoContabile() {
		return uidStrutturaAmministrativoContabile;
	}

	/**
	 * @param uidStrutturaAmministrativoContabile the uidStrutturaAmministrativoContabile to set
	 */
	public void setUidStrutturaAmministrativoContabile(
			Integer uidStrutturaAmministrativoContabile) {
		this.uidStrutturaAmministrativoContabile = uidStrutturaAmministrativoContabile;
	}
	
	/**
	 * @return the causaleSpesa
	 */
	public CausaleSpesa getCausaleSpesa() {
		return causaleSpesa;
	}

	/**
	 * @param causaleSpesa the causaleSpesa to set
	 */
	public void setCausaleSpesa(CausaleSpesa causaleSpesa) {
		this.causaleSpesa = causaleSpesa;
	}

	/**
	 * @return the listaCausale
	 */
	public List<CausaleSpesa> getListaCausale() {
		return listaCausale;
	}

	/**
	 * @param listaCausale the listaCausale to set
	 */
	public void setListaCausale(List<CausaleSpesa> listaCausale) {
		this.listaCausale = listaCausale;
	}
	
	/* ******** Requests ******** */

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaCausaleSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaCausaleSpesa creaRequestRicercaSinteticaCausaleSpesa() {
		RicercaSinteticaCausaleSpesa request = new RicercaSinteticaCausaleSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setParametriPaginazione(creaParametriPaginazione(10000));
		request.setCausaleSpesa(creaCausaleSpesa());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioCausaleSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioCausaleSpesa creaRequestRicercaDettaglioCausaleSpesa() {
		RicercaDettaglioCausaleSpesa request = creaRequest(RicercaDettaglioCausaleSpesa.class);
		
		request.setCausaleSpesa(getCausaleSpesa());
		
		return request;
	}

	/**
	 * Crea una causale spesa per la ricerca sintetica.
	 * 
	 * @return la causale creata
	 */
	private CausaleSpesa creaCausaleSpesa() {
		CausaleSpesa causale = new CausaleSpesa();
		
		causale.setTipoCausale(creaTipoCausale(getUidTipoCausale()));
		causale.setStrutturaAmministrativoContabile(creaStrutturaAmministrativoContabile(getUidStrutturaAmministrativoContabile()));
		causale.setEnte(getEnte());
		
		return causale;
	}

	/**
	 * Crea il tipo di causale da injettare nella request.
	 * 
	 * @param uid l'uid del tipo da creare
	 * 
	 * @return il tipo creato
	 */
	private TipoCausale creaTipoCausale(Integer uid) {
		TipoCausale tipoCausale = new TipoCausale();
		tipoCausale.setUid(uid);
		return tipoCausale;
	}
	
	/**
	 * Crea la struttura amministrativo contabile da injettare nella request.
	 * 
	 * @param uid l'uid della struttura da creare
	 * 
	 * @return il struttura creato
	 */
	private StrutturaAmministrativoContabile creaStrutturaAmministrativoContabile(Integer uid) {
		// Il campo è opzionale: se non è passato dal chiamante, non valorizzo nulla
		if(uid == null || uid.intValue() == 0) {
			return null;
		}
		
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = new StrutturaAmministrativoContabile();
		strutturaAmministrativoContabile.setUid(uid);
		return strutturaAmministrativoContabile;
	}
	
}
