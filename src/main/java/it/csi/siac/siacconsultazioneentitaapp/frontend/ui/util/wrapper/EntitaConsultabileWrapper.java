/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacconsultazioneentitaapp.frontend.ui.util.wrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacconsultazioneentitaser.model.EntitaConsultabile;

/**
 * Wrapper di {@link EntitaConsultabile} per il frontend. 
 * 
 * @author Domenico
 *
 */
public class EntitaConsultabileWrapper implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7429024725101676549L;
	
	
	private EntitaConsultabile entitaConsultabile;
	
	private List<EntitaConsultabileDataWrapper> listaIntestazioneCampiTabella = new ArrayList<EntitaConsultabileDataWrapper>();
	//Mappa dei campi contenente il dato composto delle colonne (comprensivo di eventuale HTML);
	private Map<String,String> campiColonne = new HashMap<String,String>();
	//Mappa dei campi contenente il dato composto delle colonne (comprensivo di eventuale HTML);
	private Map<String,String> campiAccessori = new HashMap<String,String>();
	private String testo;
//	private Boolean isParent;

	/**
	 * Costruttore a partire dall' {@link EntitaConsultabile}
	 * @param entitaConsultabile l'entita consultabile da wrappare
	 */
	public EntitaConsultabileWrapper(EntitaConsultabile entitaConsultabile) {
		super();
		this.entitaConsultabile = entitaConsultabile;
	}

	/**
	 * @return the entitaConsultabile
	 */
	public EntitaConsultabile getEntitaConsultabile() {
		return entitaConsultabile;
	}

	/**
	 * @param entitaConsultabile the entitaConsultabile to set
	 */
	public void setEntitaConsultabile(EntitaConsultabile entitaConsultabile) {
		this.entitaConsultabile = entitaConsultabile;
	}
	
	/**
	 * @return the etichetta utilizzata nel div di selezione entita'
	 */
	public String getEtichettaEntitaConsultabile(){
		return entitaConsultabile.getTipoEntitaConsultabile().getDescrizione();
	}
		

	/**
	 * @return the nomeClasse
	 */
	public String getNomeClasseEntitaConsultabile(){
		return entitaConsultabile.getTipoEntitaConsultabile().name();
	}
	
	/**
	 * @return the listaIntestazioneCampiTabella
	 */
	public List<EntitaConsultabileDataWrapper> getListaIntestazioneCampiTabella(){
		return listaIntestazioneCampiTabella;
	}

	/**
	 * @return the testo da utilizzare nell'albero di selezione Entita
	 */
	public String getTesto(){
		return this.testo;
	}
	
//	/**
//	 * @return the isParent Boolean:
//	 * true se posso selezionare altre entita dopo questa,
//	 * false altrimenti
//	 */
//	public Boolean getIsParent(){
//		return this.isParent;
//	}
	
	/**
	 * @param listaIntestazioneCampiTabella the listaIntestazioneCampiTabella to set
	 */
	public void setListaIntestazioneCampiTabella(List<EntitaConsultabileDataWrapper> listaIntestazioneCampiTabella) {
		this.listaIntestazioneCampiTabella = listaIntestazioneCampiTabella==null?new ArrayList<EntitaConsultabileDataWrapper>():listaIntestazioneCampiTabella;
	}

	/**
	 * @param testo the testo to set
	 */
	public void setTesto(String testo) {
		this.testo = testo;
	}


	/**
	 * @return the campiColonne
	 */
	public Map<String, String> getCampiColonne() {
		return campiColonne;
	}

	/**
	 * @param campiColonne the campiColonne to set
	 */
	public void setCampiColonne(Map<String, String> campiColonne) {
		this.campiColonne = campiColonne;
	}

	/**
	 * @return the cammpiAccessori
	 */
	public Map<String, String> getCampiAccessori() {
		return campiAccessori;
	}

	/**
	 * @param cammpiAccessori the cammpiAccessori to set
	 */
	public void setCampiAccessori(Map<String, String> cammpiAccessori) {
		this.campiAccessori = cammpiAccessori;
	}

	

}
