/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.provvedimento;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacattser.frontend.webservice.msg.InserisceProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfinser.Constanti;

/**
 * Classe di model per l'inserimento di un Provvedimento. Contiene una mappatura del model.
 * 
 * @author Pro-Logic
 * @version 1.0.0 - 09/09/2013
 *
 */
public class InserisciProvvedimentoModel extends GenericBilancioModel {
	/** Per la serializzazione */
	private static final long serialVersionUID = -2421335043160910036L;
	
	private AttoAmministrativo attoAmministrativo;
	private List<TipoAtto> tipiAtti = new ArrayList<TipoAtto>();
	private List<StatoOperativoAtti> statiOperativi = new ArrayList<StatoOperativoAtti>();
	
	/** Costruttore vuoto di default */
	public InserisciProvvedimentoModel() {
		super();
		setTitolo("Inserisci Provvedimento");
	}

	/**
	 * @return the attoAmministrativo
	 */
	public AttoAmministrativo getAttoAmministrativo() {
		return attoAmministrativo;
	}

	/**
	 * @param attoAmministrativo the attoAmministrativo to set
	 */
	public void setAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		this.attoAmministrativo = attoAmministrativo;
	}

	/**
	 * @return the tipiAtti
	 */
	public List<TipoAtto> getTipiAtti() {
		return tipiAtti;
	}

	/**
	 * @param tipiAtti the tipiAtti to set
	 */
	public void setTipiAtti(List<TipoAtto> tipiAtti) {
		this.tipiAtti = tipiAtti != null ? tipiAtti : new ArrayList<TipoAtto>();
	}

	/**
	 * @return the statiOperativi
	 */
	public List<StatoOperativoAtti> getStatiOperativi() {
		return statiOperativi;
	}

	/**
	 * @param statiOperativi the statiOperativi to set
	 */
	public void setStatiOperativi(List<StatoOperativoAtti> statiOperativi) {
		this.statiOperativi = statiOperativi != null ? statiOperativi : new ArrayList<StatoOperativoAtti>();
	}

	/**
	 * Crea una request per il servizio di {@link InserisceProvvedimento}.
	 * 
	 * @return la request creata
	 */
	public InserisceProvvedimento creaRequestInserisceProvvedimento() {
		InserisceProvvedimento request = creaRequest(InserisceProvvedimento.class);
		
		request.setEnte(getEnte());
		
		// SIAC-4285: in inserimento il flag e' forzato a false
		getAttoAmministrativo().setParereRegolaritaContabile(Boolean.FALSE);
		//SIAC-6929
		getAttoAmministrativo().setBloccoRagioneria(Boolean.FALSE);
		getAttoAmministrativo().setProvenienza(Constanti.ATTO_AMMINISTRATIVO_PROVENIENZA_MANUALE);
		request.setAttoAmministrativo(getAttoAmministrativo());
		StrutturaAmministrativoContabile sac = getAttoAmministrativo().getStrutturaAmmContabile();
		request.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(sac));
		request.setTipoAtto(getAttoAmministrativo().getTipoAtto());
		
		return request;
	}

}
