/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.dismissionecespite;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioDismissioneCespite;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * The Class GenericDismissioneCespiteModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class GenericDismissioneCespiteModel extends GenericBilancioModel {

	/** PEr la serializzazione*/
	private static final long serialVersionUID = 6072408526929691036L;

	private DismissioneCespite dismissioneCespite;
	private int uidDismissioneCespite;
	
	private AttoAmministrativo attoAmministrativo;
	private TipoAtto tipoAtto;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	
	private List<CausaleEP> listaCausaleEP = new ArrayList<CausaleEP>();
	private List<Evento> listaEvento = new ArrayList<Evento>();
	
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	
	/**
	 * @return the tipoBeneDismissioneCespite
	 */
	public DismissioneCespite getDismissioneCespite() {
		return dismissioneCespite;
	}

	/**
	 * @param tipoBeneDismissioneCespite the tipoBeneDismissioneCespite to set
	 */
	public void setDismissioneCespite(DismissioneCespite tipoBeneDismissioneCespite) {
		this.dismissioneCespite = tipoBeneDismissioneCespite;
	}
	
	/**
	 * @return the uidDismissioneCespite
	 */
	public int getUidDismissioneCespite() {
		return uidDismissioneCespite;
	}

	/**
	 * @param uidDismissioneCespite the uidDismissioneCespite to set
	 */
	public void setUidDismissioneCespite(int uidDismissioneCespite) {
		this.uidDismissioneCespite = uidDismissioneCespite;
	}
	
	/**
	 * @return the tipoBeneDismissioneCespite
	 */
	public DismissioneCespite getTipoBeneDismissioneCespite() {
		return dismissioneCespite;
	}

	/**
	 * @param tipoBeneDismissioneCespite the tipoBeneDismissioneCespite to set
	 */
	public void setTipoBeneDismissioneCespite(DismissioneCespite tipoBeneDismissioneCespite) {
		this.dismissioneCespite = tipoBeneDismissioneCespite;
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
	 * @return the tipoAtto
	 */
	public TipoAtto getTipoAtto() {
		return tipoAtto;
	}



	/**
	 * @param tipoAtto the tipoAtto to set
	 */
	public void setTipoAtto(TipoAtto tipoAtto) {
		this.tipoAtto = tipoAtto;
	}



	/**
	 * @return the strutturaAmministrativoContabile
	 */
	public StrutturaAmministrativoContabile getStrutturaAmministrativoContabile() {
		return strutturaAmministrativoContabile;
	}



	/**
	 * @param strutturaAmministrativoContabile the strutturaAmministrativoContabile to set
	 */
	public void setStrutturaAmministrativoContabile(StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}
	
	
	/**
	 * @return the listaCausaleEP
	 */
	public List<CausaleEP> getListaCausaleEP() {
		return listaCausaleEP;
	}

	/**
	 * @param listaCausaleEP the listaCausaleEP to set
	 */
	public void setListaCausaleEP(List<CausaleEP> listaCausaleEP) {
		this.listaCausaleEP = listaCausaleEP;
	}

	/**
	 * @return the listaEvento
	 */
	public List<Evento> getListaEvento() {
		return listaEvento;
	}

	/**
	 * @param listaEvento the listaEvento to set
	 */
	public void setListaEvento(List<Evento> listaEvento) {
		this.listaEvento = listaEvento;
	}

	//UTILITIES
	
	/**
	 * @return the listaTipoAtto
	 */
	public List<TipoAtto> getListaTipoAtto() {
		return listaTipoAtto;
	}

	/**
	 * @param listaTipoAtto the listaTipoAtto to set
	 */
	public void setListaTipoAtto(List<TipoAtto> listaTipoAtto) {
		this.listaTipoAtto = listaTipoAtto;
	}

	/**
	 * Gets the ambito.
	 *
	 * @return the ambito
	 */
	public Ambito getAmbito(){
		return getAmbitoFIN();
	}
	
	/**
	 * Gets the ambito FIN.
	 *
	 * @return the ambito FIN
	 */
	public Ambito getAmbitoFIN(){
		return Ambito.AMBITO_FIN;
	}
	
	/**
	 * Gets the ambito FIN.
	 *
	 * @return the ambito FIN
	 */
	public Ambito getAmbitoCausaleInventario(){
		return Ambito.AMBITO_INV;
	}
	
	/**
	 * Crea request ricerca sintetica modulare causale.
	 *
	 * @return the ricerca sintetica modulare causale
	 */
	public RicercaSinteticaModulareCausale creaRequestRicercaSinteticaModulareCausale() {
		RicercaSinteticaModulareCausale request = creaRequest(RicercaSinteticaModulareCausale.class);
		CausaleEP causEpPerRequest = new CausaleEP();
		
		request.setBilancio(getBilancio());
		request.setParametriPaginazione(creaParametriPaginazione(Integer.MAX_VALUE));
				
		causEpPerRequest.setTipoCausale(TipoCausale.Libera);
		causEpPerRequest.setAmbito(Ambito.AMBITO_INV);
		request.setCausaleEP(causEpPerRequest);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaProvvedimento}.
	 * 
	 * @return la request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento() {
		RicercaProvvedimento request = creaRequest(RicercaProvvedimento.class);
		
		request.setEnte(getEnte());
		
		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setAnnoAtto(getAttoAmministrativo().getAnno());
		ricercaAtti.setNumeroAtto(getAttoAmministrativo().getNumero());
		ricercaAtti.setTipoAtto(impostaEntitaFacoltativa(getTipoAtto()));
		ricercaAtti.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		request.setRicercaAtti(ricercaAtti);
		
		return request;
	}
	
	/**
	 * Crea request ricerca dettaglio tipo bene cespite.
	 *
	 * @return the ricerca dettaglio tipo bene cespite
	 */
	public RicercaDettaglioDismissioneCespite creaRequestRicercaDettaglioDismissioneCespite() {
		RicercaDettaglioDismissioneCespite req = creaRequest(RicercaDettaglioDismissioneCespite.class);
		DismissioneCespite tc = creaDismissioneDaUid();
		req.setDismissioneCespite(tc);
		return req;
	}

	/**
	 * Crea una dismissione cespite dato un uid
	 * @return la dismissione cespite
	 */
	protected DismissioneCespite creaDismissioneDaUid() {
		DismissioneCespite tc = new DismissioneCespite();
		tc.setUid(getUidDismissioneCespite());
		return tc;
	}
	

}
