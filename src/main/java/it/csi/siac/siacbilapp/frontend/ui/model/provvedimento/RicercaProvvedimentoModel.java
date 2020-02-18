/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.provvedimento;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacattser.model.StatoOperativoAtti;
import it.csi.siac.siacattser.model.TipoAtto;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.provvedimento.ElementoProvvedimento;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.provvedimento.ElementoProvvedimentoFactory;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;

/**
 * Classe di model per la ricerca del Provvedimento. Contiene una mappatura dei campi della ricerca.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 26/09/2013
 *
 */
public class RicercaProvvedimentoModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5408731638066392689L;
	
	// Campi da ottenere
	private AttoAmministrativo attoAmministrativo;
	private TipoAtto tipoAtto;
	private StrutturaAmministrativoContabile strutturaAmministrativoContabile;
	private StatoOperativoAtti statoOperativoAtti;
	
	//SIAC 6929 
	private String bloccoRagioneria;
	private String provenienza;
	private String inseritoManualmente;
	
	// Liste
	private List<TipoAtto> listaTipoAtto = new ArrayList<TipoAtto>();
	private List<StatoOperativoAtti> listaStatoOperativo = new ArrayList<StatoOperativoAtti>();
	
	// Risultato
	private List<ElementoProvvedimento> listaElementoProvvedimento = new ArrayList<ElementoProvvedimento>();
	
	// Strighe di utilita'
	private String strutturaAmministrativoResponsabile;
	
	/** Costruttore vuoto di default */
	public RicercaProvvedimentoModel() {
		super();
		setTitolo("Ricerca provvedimento");
	}
	
	/* Getter e Setter */
	
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
	public void setStrutturaAmministrativoContabile(
			StrutturaAmministrativoContabile strutturaAmministrativoContabile) {
		this.strutturaAmministrativoContabile = strutturaAmministrativoContabile;
	}

	/**
	 * @return the statoOperativoAtti
	 */
	public StatoOperativoAtti getStatoOperativoAtti() {
		return statoOperativoAtti;
	}

	/**
	 * @param statoOperativoAtti the statoOperativoAtti to set
	 */
	public void setStatoOperativoAtti(StatoOperativoAtti statoOperativoAtti) {
		this.statoOperativoAtti = statoOperativoAtti;
	}

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
	 * @return the listaStatoOperativo
	 */
	public List<StatoOperativoAtti> getListaStatoOperativo() {
		return listaStatoOperativo;
	}

	/**
	 * @param listaStatoOperativo the listaStatoOperativo to set
	 */
	public void setListaStatoOperativo(List<StatoOperativoAtti> listaStatoOperativo) {
		this.listaStatoOperativo = listaStatoOperativo;
	}
	
	/**
	 * @return the listaElementoProvvedimento
	 */
	public List<ElementoProvvedimento> getListaElementoProvvedimento() {
		return listaElementoProvvedimento;
	}

	/**
	 * @param listaElementoProvvedimento the listaElementoProvvedimento to set
	 */
	public void setListaElementoProvvedimento(
			List<ElementoProvvedimento> listaElementoProvvedimento) {
		this.listaElementoProvvedimento = listaElementoProvvedimento;
	}
	
	/**
	 * @return the strutturaAmministrativoResponsabile
	 */
	public String getStrutturaAmministrativoResponsabile() {
		return strutturaAmministrativoResponsabile;
	}

	/**
	 * @param strutturaAmministrativoResponsabile the strutturaAmministrativoResponsabile to set
	 */
	public void setStrutturaAmministrativoResponsabile(
			String strutturaAmministrativoResponsabile) {
		this.strutturaAmministrativoResponsabile = strutturaAmministrativoResponsabile;
	}
	
	/* Request */

	/**
	 * Crea una request per il servizio di {@link RicercaProvvedimento} a partire dal model.
	 * 
	 * @return la request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento() {
		RicercaProvvedimento request = creaRequest(RicercaProvvedimento.class);
		
		request.setEnte(getEnte());
		
		// Injezione della utility di ricerca
		request.setRicercaAtti(creaUtilityRicercaAtti());
		
		return request;
	}
	
	/* Utilities */
	
	/**
	 * Metodo di utilit&agrave; per la ricerca del provvedimento.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private RicercaAtti creaUtilityRicercaAtti() {
		RicercaAtti utility = new RicercaAtti();
		
		// Controllo che il provvedimento sia stato inizializzato
		if(attoAmministrativo != null) {
			// L'anno e' obbligatorio, dunque lo injetto sempre
			// SIAC-5586: l'anno non e' piu' obbligatorio
			utility.setAnnoAtto(attoAmministrativo.getAnno() != 0 ? Integer.valueOf(attoAmministrativo.getAnno()) : null);
			// Injetto il numero dell'atto se e' stato inizializzato
			utility.setNumeroAtto(attoAmministrativo.getNumero() != 0 ? Integer.valueOf(attoAmministrativo.getNumero()) : null);
			// Injetto le note se sono state inizializzate
			utility.setNote(StringUtils.isNotBlank(attoAmministrativo.getNote()) ? attoAmministrativo.getNote() : null);
			// Injetto l'oggetto se e' stato inizializzato
			utility.setOggetto(StringUtils.isNotBlank(attoAmministrativo.getOggetto()) ? attoAmministrativo.getOggetto() : null);
			
			
			//SIAC 6929 Injetto il blocco ragioneria e inserito manualmente - commentato in quanto non definiti nell'xml della request soap per richiamare il servizio.
			//Discuterne per quanto riguarda i valori dell'inserimento manuale
			utility.setBloccoRagioneria(getBloccoRagioneria()==null || getBloccoRagioneria().equals("TUTTI") ? null : (getBloccoRagioneria().equals("SI") ? true : false));
			//Deciso di mantenere inseritoManualmente ma di utilizzare provenienza
			utility.setProvenienza((getInseritoManualmente() == null || getInseritoManualmente().equals("TUTTI"))  ?  null : ((getInseritoManualmente().equals("SI")) ? "MANUALE" : String.valueOf(-1)));

		}
		 //devo buildare
		utility.setTipoAtto(impostaEntitaFacoltativa(getTipoAtto()));
		utility.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		
		// Injetto lo stato operativo se e solo se e' stato inizializzato
		utility.setStatoOperativo(statoOperativoAtti != null ? statoOperativoAtti.name() : null);
		
		return utility;
	}
	
	/**
	 * @param listaAttoAmministrativo the listaAttoAmministrativo to set
	 */
	public void impostaListaElementoProvvedimento(List<AttoAmministrativo> listaAttoAmministrativo) {
		if(listaAttoAmministrativo != null) {
			if(listaElementoProvvedimento == null) {
				listaElementoProvvedimento = new ArrayList<ElementoProvvedimento>();
			}
			for(AttoAmministrativo provvedimento : listaAttoAmministrativo) {
				listaElementoProvvedimento.add(ElementoProvvedimentoFactory.getInstance(provvedimento));
			}
		}
	}
	
	//SIAC 6929 filtri di ricerca in radio button
	public String getBloccoRagioneria() {
		return bloccoRagioneria;
	}

	public void setBloccoRagioneria(String bloccoRagioneria) {
		this.bloccoRagioneria = bloccoRagioneria;
	}

	public String getInseritoManualmente() {
		return inseritoManualmente;
	}

	public void setInseritoManualmente(String inseritoManualmente) {
		this.inseritoManualmente = inseritoManualmente;
	}

	public String getProvenienza() {
		return provenienza;
	}

	public void setProvenienza(String provenienza) {
		this.provenienza = provenienza;
	}
	
}
