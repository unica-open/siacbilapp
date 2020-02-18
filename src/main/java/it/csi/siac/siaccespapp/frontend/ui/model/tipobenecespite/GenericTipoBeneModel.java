/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.tipobenecespite;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccecser.model.ContoTipoBeneSelector;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaDettaglioTipoBeneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCategoriaCespiti;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.model.CategoriaCespiti;
import it.csi.siac.siaccespser.model.TipoBeneCespite;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaModulareCausale;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * The Class GenericTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class GenericTipoBeneModel extends GenericBilancioModel {

	/** PEr la serializzazione*/
	private static final long serialVersionUID = -2929698129945781385L;
	
	private List<CategoriaCespiti> listaCategoriaCespiti = new ArrayList<CategoriaCespiti>();
	private TipoBeneCespite tipoBeneCespite;
	private int uidTipoBeneCespite;
	
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	private List<ClassePiano> listaClassi = new ArrayList<ClassePiano>();
	
	private List<CausaleEP> listaCausaleEP = new ArrayList<CausaleEP>();
	private List<Evento> listaEvento = new ArrayList<Evento>();
	
	/**
	 * @return the listaCategoriaCespiti
	 */
	public List<CategoriaCespiti> getListaCategoriaCespiti() {
		return listaCategoriaCespiti;
	}

	/**
	 * @param listaCategoriaCespiti the listaCategoriaCespiti to set
	 */
	public void setListaCategoriaCespiti(List<CategoriaCespiti> listaCategoriaCespiti) {
		this.listaCategoriaCespiti = listaCategoriaCespiti;
	}

	/**
	 * @return the tipoBeneCespite
	 */
	public TipoBeneCespite getTipoBeneCespite() {
		return tipoBeneCespite;
	}

	/**
	 * @param tipoBeneCespite the tipoBeneCespite to set
	 */
	public void setTipoBeneCespite(TipoBeneCespite tipoBeneCespite) {
		this.tipoBeneCespite = tipoBeneCespite;
	}
	
	/**
	 * @return the uidTipoBeneCespite
	 */
	public int getUidTipoBeneCespite() {
		return uidTipoBeneCespite;
	}

	/**
	 * @param uidTipoBeneCespite the uidTipoBeneCespite to set
	 */
	public void setUidTipoBeneCespite(int uidTipoBeneCespite) {
		this.uidTipoBeneCespite = uidTipoBeneCespite;
	}

	/**
	 * @return the listaTitoloEntrata
	 */
	public List<TitoloEntrata> getListaTitoloEntrata() {
		return listaTitoloEntrata;
	}

	/**
	 * @param listaTitoloEntrata the listaTitoloEntrata to set
	 */
	public void setListaTitoloEntrata(List<TitoloEntrata> listaTitoloEntrata) {
		this.listaTitoloEntrata = listaTitoloEntrata;
	}

	/**
	 * @return the listaTitoloSpesa
	 */
	public List<TitoloSpesa> getListaTitoloSpesa() {
		return listaTitoloSpesa;
	}

	/**
	 * @param listaTitoloSpesa the listaTitoloSpesa to set
	 */
	public void setListaTitoloSpesa(List<TitoloSpesa> listaTitoloSpesa) {
		this.listaTitoloSpesa = listaTitoloSpesa;
	}

	/**
	 * Gets the lista classi.
	 *
	 * @return the listaClassi
	 */
	public List<ClassePiano> getListaClassi() {
		return listaClassi;
	}

	/**
	 * Sets the lista classi.
	 *
	 * @param listaClassi the listaClassi to set
	 */
	public void setListaClassi(List<ClassePiano> listaClassi) {
		this.listaClassi = listaClassi;
	}
	
	/**
	 * Gets the lista causale EP.
	 *
	 * @return the listaCausaleEP
	 */
	public List<CausaleEP> getListaCausaleEP() {
		return listaCausaleEP;
	}

	/**
	 * Sets the lista causale EP.
	 *
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
	 * Gets the conto patrimoniale tipo bene enum.
	 *
	 * @return the conto patrimoniale tipo bene enum
	 */
	public ContoTipoBeneSelector getContoPatrimonialeTipoBeneSelector(){
		return ContoTipoBeneSelector.Patrimoniale;
	}
	
	/**
	 * Gets the conto ammortamento tipo bene enum.
	 *
	 * @return the conto ammortamento tipo bene enum
	 */
	public ContoTipoBeneSelector getContoAmmortamentoTipoBeneSelector(){
		return ContoTipoBeneSelector.Ammortamento;
	}
	
	/**
	 * Gets the conto alienazione tipo bene enum.
	 *
	 * @return the conto alienazione tipo bene enum
	 */
	public ContoTipoBeneSelector getContoAlienazioneTipoBeneSelector(){
		return ContoTipoBeneSelector.Alienazione;
	}
	
	/**
	 * Gets the conto decremento tipo bene enum.
	 *
	 * @return the conto decremento tipo bene enum
	 */
	public ContoTipoBeneSelector getContoDecrementoTipoBeneSelector(){
		return ContoTipoBeneSelector.Decremento;
	}
	
	/**
	 * Gets the conto donazione tipo bene enum.
	 *
	 * @return the conto donazione tipo bene enum
	 */
	public ContoTipoBeneSelector getContoDonazioneTipoBeneSelector(){
		return ContoTipoBeneSelector.Donazione;
	}
	
	/**
	 * Gets the conto fondo ammortamento tipo bene enum.
	 *
	 * @return the conto fondo ammortamento tipo bene enum
	 */
	public ContoTipoBeneSelector getContoFondoAmmortamentoTipoBeneSelector(){
		return ContoTipoBeneSelector.Fondo_Ammortamento;
	}
	
	/**
	 * Gets the conto incremento tipo bene enum.
	 *
	 * @return the conto incremento tipo bene enum
	 */
	public ContoTipoBeneSelector getContoIncrementoTipoBeneSelector(){
		return ContoTipoBeneSelector.Incremento;
	}
	
	/**
	 * Gets the conto minus valenza tipo bene enum.
	 *
	 * @return the conto minus valenza tipo bene enum
	 */
	public ContoTipoBeneSelector getContoMinusValenzaTipoBeneSelector(){
		return ContoTipoBeneSelector.MinusValenza;
	}
	
	/**
	 * Gets the conto plus valenza tipo bene enum.
	 *
	 * @return the conto plus valenza tipo bene enum
	 */
	public ContoTipoBeneSelector getContoPlusValenzaTipoBeneSelector(){
		return ContoTipoBeneSelector.PlusValenza;
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
		request.setCausaleEPModelDetails();
				
		causEpPerRequest.setTipoCausale(TipoCausale.Libera);
		causEpPerRequest.setAmbito(Ambito.AMBITO_INV);
		request.setCausaleEP(causEpPerRequest);
		
		return request;
	}
	
	/**
	 * Crea request ricerca sintetica conto.
	 *
	 * @param conto the conto
	 * @return the ricerca sintetica conto
	 */
	public RicercaSinteticaConto creaRequestRicercaSinteticaConto(Conto conto) {
		RicercaSinteticaConto request = creaRequest(RicercaSinteticaConto.class);
		request.setBilancio(getBilancio());
		request.setConto(conto);
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}
	
	/**
	 * Crea conto ricerca selector.
	 * @param uid the uuid
	 * @param codice the codice
	 * @param contoSelector the conto selector
	 * @return the conto
	 */
	public Conto creaContoRicercaSelector(int uid, String codice, ContoTipoBeneSelector contoSelector) {
		Conto c = new Conto();
		c.setUid(uid);
		c.setAmbito(Ambito.AMBITO_FIN);
		c.setCodice(codice);
		c.setAmmortamento(contoSelector.getAmmortamento());
		c.setAttivo(contoSelector.getAttivo());
		c.setContoFoglia(contoSelector.getContoFoglia());
		return c;
	}
	
	/**
	 * Crea request ricerca sintetica tipo bene cespite by codice conto patrimoniale.
	 *
	 * @param contoPatrimoniale the conto patrimoniale
	 * @return the ricerca sintetica tipo bene cespite
	 */
	public RicercaSinteticaTipoBeneCespite creaRequestRicercaSinteticaTipoBeneCespiteByCodiceContoPatrimoniale(Conto contoPatrimoniale) {
		RicercaSinteticaTipoBeneCespite req = creaRequest(RicercaSinteticaTipoBeneCespite.class);
		req.setContoPatrimoniale(contoPatrimoniale);
		req.setParametriPaginazione(creaParametriPaginazione(1));
		req.setModelDetails();
		return req;
	}
	
	/**
	 * Crea request ricerca dettaglio tipo bene cespite.
	 *
	 * @return the ricerca dettaglio tipo bene cespite
	 */
	public RicercaDettaglioTipoBeneCespite creaRequestRicercaDettaglioTipoBeneCespite() {
		RicercaDettaglioTipoBeneCespite req = creaRequest(RicercaDettaglioTipoBeneCespite.class);
		TipoBeneCespite tc = new TipoBeneCespite();
		tc.setUid(getUidTipoBeneCespite());
		req.setTipoBeneCespite(tc);
		return req;
	}
	

	/**
	 * Crea request ricerca sintetica categoria cespiti.
	 * @param escludiAnnullati se sia necessario escludere gli annullati
	 *
	 * @return the ricerca sintetica categoria cespiti
	 */
	public RicercaSinteticaCategoriaCespiti creaRequestRicercaSinteticaCategoriaCespiti(Boolean escludiAnnullati){
		RicercaSinteticaCategoriaCespiti req = creaRequest(RicercaSinteticaCategoriaCespiti.class);
		req.setEscludiAnnullati(escludiAnnullati);
		req.setParametriPaginazione(creaParametriPaginazione());
		req.getParametriPaginazione().setElementiPerPagina(Integer.MAX_VALUE);
		return req;
	}
}
