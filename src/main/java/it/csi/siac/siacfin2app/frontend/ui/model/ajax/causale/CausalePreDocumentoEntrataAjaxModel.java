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
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrata;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;
import it.csi.siac.siacfin2ser.model.TipoCausale;

/**
 * Classe di model per il caricamento via AJAX della Causale di entrata per il PreDocumento.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 28/04/2014
 *
 */
public class CausalePreDocumentoEntrataAjaxModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4564025336813906291L;
	
	private Integer uidTipoCausale;
	private Integer uidStrutturaAmministrativoContabile;
	private CausaleEntrata causaleEntrata;
	
	// SIAC-4492
	private CausaleEntrata causaleOriginale;
	
	private List<CausaleEntrata> listaCausale = new ArrayList<CausaleEntrata>();
	
	/** Costruttore vuoto di default */
	public CausalePreDocumentoEntrataAjaxModel() {
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
	 * @return the causaleEntrata
	 */
	public CausaleEntrata getCausaleEntrata() {
		return causaleEntrata;
	}

	/**
	 * @param causaleEntrata the causaleEntrata to set
	 */
	public void setCausaleEntrata(CausaleEntrata causaleEntrata) {
		this.causaleEntrata = causaleEntrata;
	}

	/**
	 * @return the causaleOriginale
	 */
	public CausaleEntrata getCausaleOriginale() {
		return causaleOriginale;
	}

	/**
	 * @param causaleOriginale the causaleOriginale to set
	 */
	public void setCausaleOriginale(CausaleEntrata causaleOriginale) {
		this.causaleOriginale = causaleOriginale;
	}

	/**
	 * @return the listaCausale
	 */
	public List<CausaleEntrata> getListaCausale() {
		return listaCausale;
	}

	/**
	 * @param listaCausale the listaCausale to set
	 */
	public void setListaCausale(List<CausaleEntrata> listaCausale) {
		this.listaCausale = listaCausale != null ? listaCausale : new ArrayList<CausaleEntrata>();
	}
	
	/* ******** Requests ******** */

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaCausaleEntrata}.
	 * @param statoOperativoCausale lo stato della causale
	 * @return la request creata
	 */
	public RicercaSinteticaCausaleEntrata creaRequestRicercaSinteticaCausaleEntrata(StatoOperativoCausale statoOperativoCausale) {
		RicercaSinteticaCausaleEntrata request = new RicercaSinteticaCausaleEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setParametriPaginazione(creaParametriPaginazione(10000));
		request.setCausaleEntrata(creaCausaleEntrata(statoOperativoCausale));
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioCausaleEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioCausaleEntrata creaRequestRicercaDettaglioCausaleEntrata() {
		RicercaDettaglioCausaleEntrata request = creaRequest(RicercaDettaglioCausaleEntrata.class);
		
		request.setCausaleEntrata(getCausaleEntrata());
		
		return request;
	}

	/**
	 * Crea una causale entrata per la ricerca sintetica.
	 * @param statoOperativoCausale lo stato della causale
	 * @return la causale creata
	 */
	private CausaleEntrata creaCausaleEntrata(StatoOperativoCausale statoOperativoCausale) {
		CausaleEntrata causale = new CausaleEntrata();
		
		causale.setTipoCausale(creaTipoCausale(getUidTipoCausale()));
		causale.setStrutturaAmministrativoContabile(creaStrutturaAmministrativoContabile(getUidStrutturaAmministrativoContabile()));
		causale.setEnte(getEnte());
		causale.setStatoOperativoCausale(statoOperativoCausale);
		
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
		// Il campo è opzionale: se non e' passato dal chiamante, non valorizzo nulla
		if(uid == null || uid.intValue() == 0) {
			return null;
		}
		
		StrutturaAmministrativoContabile strutturaAmministrativoContabile = new StrutturaAmministrativoContabile();
		strutturaAmministrativoContabile.setUid(uid);
		return strutturaAmministrativoContabile;
	}
	
}
