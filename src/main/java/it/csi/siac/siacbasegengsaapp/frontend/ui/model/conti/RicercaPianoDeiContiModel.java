/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.TitoloEntrata;
import it.csi.siac.siacbilser.model.TitoloSpesa;
import it.csi.siac.siacgenser.frontend.webservice.msg.AnnullaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.EliminaConto;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaContoFigli;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;

/**
 * Classe di model per la ricerca del piano dei conti.
 * 
 * @author Valentina Triolo
 * @version 1.0.0
 *
 */
public class RicercaPianoDeiContiModel extends GenericBilancioModel{

	private static final long serialVersionUID = 1565888666025892640L;
	
	private Ambito ambito;
	
	private Conto conto;
	private List<ClassePiano> listaClassi = new ArrayList<ClassePiano>();
	private Boolean figliPresenti = Boolean.FALSE;
	private Boolean isGestioneConsentita = Boolean.FALSE;
	private Integer uidDaAnnullare;
	private Integer uidConto;
	private Boolean isTabellaVisibile = Boolean.FALSE;
	private List<String> gerarchiaConti = new ArrayList<String>();
	
	/* modale*/
	private List<TitoloEntrata> listaTitoloEntrata = new ArrayList<TitoloEntrata>();
	private List<TitoloSpesa> listaTitoloSpesa = new ArrayList<TitoloSpesa>();
	
	/** Costruttore vuoto di default */
	public RicercaPianoDeiContiModel(){
		setTitolo("Ricerca Piano Dei Conti");
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
	 * @return the ambitoFIN
	 */
	public Ambito getAmbitoFIN() {
		return Ambito.AMBITO_FIN;
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
	 * @return the figliPresenti
	 */
	public Boolean getFigliPresenti() {
		return figliPresenti;
	}
	/**
	 * @param figliPresenti the figliPresenti to set
	 */
	public void setFigliPresenti(Boolean figliPresenti) {
		this.figliPresenti = figliPresenti;
	}
	/**
	
	/**
	 * @return the uidDaAnnullare
	 */
	public Integer getUidDaAnnullare() {
		return uidDaAnnullare;
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
	 * @return the isGestioneConsentita
	 */
	public Boolean getIsGestioneConsentita() {
		return isGestioneConsentita;
	}
	/**
	 * @param isGestioneConsentita the isGestioneConsentita to set
	 */
	public void setIsGestioneConsentita(Boolean isGestioneConsentita) {
		this.isGestioneConsentita = isGestioneConsentita;
	}

	/**
	 * @param uidDaAnnullare the uidDaAnnullare to set
	 */
	public void setUidDaAnnullare(Integer uidDaAnnullare) {
		this.uidDaAnnullare = uidDaAnnullare;
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
	 * @return the gerarchiaConti
	 */
	public List<String> getGerarchiaConti() {
		return gerarchiaConti;
	}

	/**
	 * @param gerarchiaConti the gerarchiaConti to set
	 */
	public void setGerarchiaConti(List<String> gerarchiaConti) {
		this.gerarchiaConti = gerarchiaConti;
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
	 * @return the FIN
	 */
	public Ambito getFIN() {
		return Ambito.AMBITO_FIN;
	}

	/**
	 * Crea la request per la ricerca sintetica di un conto e di tutti i suoi figli
	 * @return la request
	 */
	public RicercaSinteticaContoFigli creaRequestRicercaSinteticaContoFigli(){
		RicercaSinteticaContoFigli request = creaRequest(RicercaSinteticaContoFigli.class);
		Conto c = new Conto();
		c.setPianoDeiConti(conto.getPianoDeiConti());
		c.setCodice(conto.getCodice().trim());
		c.setCodiceInterno(conto.getCodiceInterno());
		c.setAmbito(getAmbito());
		request.setConto(c);
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setBilancio(getBilancio());
		return request;
	}
	
	/**
	 * Crea la request per l'annullamento di un conto
	 * @return la request
	 */
	public AnnullaConto creaRequestAnnullaConto(){
		AnnullaConto request = creaRequest(AnnullaConto.class);
		Conto contoDaAnnullare = new Conto();
		contoDaAnnullare.setUid(uidDaAnnullare);
		request.setConto(contoDaAnnullare);
		request.setBilancio(getBilancio());
		return request;
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
	 * Crea la request per l'eliminazione del conto.
	 * @return la equest
	 */
	public EliminaConto creaRequestEliminaConto() {
		EliminaConto request = creaRequest(EliminaConto.class);
		Conto contoDaEliminare = new Conto();
		contoDaEliminare.setUid(uidDaAnnullare);
		request.setConto(contoDaEliminare);
		return request;
	}

}
