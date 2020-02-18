/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siaccecser.model.ContoTipoBeneSelector;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.TipoConto;

/**
 * Classe di model per la ricerca del piano dei conti.
 * 
 * @author Valentina Triolo
 * @version 1.0.0
 *
 */
public class RicercaContoModel extends GenericBilancioModel{

	private static final long serialVersionUID = 1565888666025892640L;
	
	private Conto conto;
	private List<ClassePiano> listaClassi = new ArrayList<ClassePiano>();
	private Ambito ambito;

	private Integer uidConto;
	private Boolean isTabellaVisibile = Boolean.FALSE;

	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	
	private ContoTipoBeneSelector contoTipoBeneSelector;
	

	/** Costruttore vuoto di default */
	public RicercaContoModel(){
		setTitolo("Ricerca Piano Dei Conti");
	}
	/**
	 * @return the conto
	 */
	public Conto getConto() {
		return conto;
	}
	/**
	 * @param conto the conto to set
	 */
	public void setConto(Conto conto) {
		this.conto = conto;
	}
	/**
	 * @return the listaClassi
	 */
	public List<ClassePiano> getListaClassi() {
		return listaClassi;
	}
	/**
	 * @param listaClassi the listaClassi to set
	 */
	public void setListaClassi(List<ClassePiano> listaClassi) {
		this.listaClassi = listaClassi;
	}

	/**
	 * @return the uidContoPadre
	 */
	public Integer getUidConto() {
		return uidConto;
	}
	/**
	 * @param uidConto the uidConto to set
	 */
	public void setUidConto(Integer uidConto) {
		this.uidConto = uidConto;
	}
	
	
	/**
	 * @return the isTabellaVisibile
	 */
	public Boolean getIsTabellaVisibile() {
		return isTabellaVisibile;
	}

	/**
	 * @param isTabellaVisibile the isTabellaVisibile to set
	 */
	public void setIsTabellaVisibile(Boolean isTabellaVisibile) {
		this.isTabellaVisibile = isTabellaVisibile;
	}
	

	
	/**
	 * @return the listaTitoloEntrata
	 */
	public final List<TitoloEntrata> getListaTitoloEntrata() {
		return listaTitoloEntrata;
	}

	/**
	 * @param listaTitoloEntrata the listaTitoloEntrata to set
	 */
	public final void setListaTitoloEntrata(List<TitoloEntrata> listaTitoloEntrata) {
		this.listaTitoloEntrata = listaTitoloEntrata;
	}

	/**
	 * @return the listaTitoloSpesa
	 */
	public final List<TitoloSpesa> getListaTitoloSpesa() {
		return listaTitoloSpesa;
	}

	/**
	 * @param listaTitoloSpesa the listaTitoloSpesa to set
	 */
	public final void setListaTitoloSpesa(List<TitoloSpesa> listaTitoloSpesa) {
		this.listaTitoloSpesa = listaTitoloSpesa;
	}
	
	/**
	 * @return the ambito
	 */
	public Ambito getAmbito() {
		return ambito;
	}
	/**
	 * @param ambito the ambito to set
	 */
	public void setAmbito(Ambito ambito) {
		this.ambito = ambito;
	}	
	
	/**
	 * @return the contoTipoBeneSelector
	 */
	public ContoTipoBeneSelector getContoTipoBeneSelector() {
		return contoTipoBeneSelector;
	}
	/**
	 * @param contoTipoBeneSelector the contoTipoBeneSelector to set
	 */
	public void setContoTipoBeneSelector(ContoTipoBeneSelector contoTipoBeneSelector) {
		this.contoTipoBeneSelector = contoTipoBeneSelector;
	}
	/**
	 * Crea la request per la ricerca sintetica di un conto e di tutti i suoi figli
	 * @return la request
	 */
	public RicercaSinteticaConto creaRequestRicercaSinteticaConto(){
		RicercaSinteticaConto request = creaRequest(RicercaSinteticaConto.class);
		Conto c = new Conto();
		c.setPianoDeiConti(conto.getPianoDeiConti());
		c.setCodice(conto.getCodice());
		c.setLivello(conto.getLivello());
		c.setAmbito(getAmbito());
		log.info("creaRequestRicercaSinteticaConto", "ambito passato " + getAmbito());
		
		c.setElementoPianoDeiConti(conto.getElementoPianoDeiConti());
		if(Boolean.TRUE.equals(conto.getAmmortamento())){
			c.setAmmortamento(Boolean.TRUE);
		}
		impostaValoriTramiteSelector(c);
		request.setConto(c);
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setBilancio(getBilancio());
		return request;
	}
	
	
	
	private void impostaValoriTramiteSelector(Conto c) {
		if(contoTipoBeneSelector == null) {
			return;
		}
		c.setAmmortamento(contoTipoBeneSelector.getAmmortamento());
		c.setAttivo(contoTipoBeneSelector.getAttivo());
		c.setContoFoglia(contoTipoBeneSelector.getContoFoglia());
		String codiceTipoConto = contoTipoBeneSelector.getCodiceTipoConto();
		if(StringUtils.isBlank(codiceTipoConto)) {
			return;
		}
		TipoConto tc = new TipoConto();
		tc.setCodice(codiceTipoConto);
		c.setTipoConto(tc);		
	}
	/**
	 * Crea la request per il servizio ricerca codifiche per tipoCodifica uguale a ClassePiano
	 * @return la request
	 */
	public RicercaCodifiche creaRequestRicercaClassi(){
		String ambitoSuffix = getAmbito().name().split("_")[1];
		return creaRequestRicercaCodifiche("ClassePiano" + ambitoSuffix);
	}
	
	/**
	 * Crea la request per il servizio ricerca codifiche per tipoCodifica uguale a TitoloEntrata
	 * @return la request
	 */
	public RicercaCodifiche creaRequestRicercaTitoliEntrata(){
		return creaRequestRicercaCodifiche(TitoloEntrata.class);
	}
	/**
	 * Crea la request per il servizio ricerca codifiche per tipoCodifica uguale a TitoloSpesa
	 * @return la request
	 */
	public RicercaCodifiche creaRequestRicercaTitoliSpesa(){
		return creaRequestRicercaCodifiche(TitoloSpesa.class);
	}

}
