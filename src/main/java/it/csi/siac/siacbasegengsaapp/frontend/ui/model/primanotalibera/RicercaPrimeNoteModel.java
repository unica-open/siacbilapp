/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ElementoPianoDeiConti;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaPrimeNote;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoEvento;

/**
 * Classe di model per la ricerca della prima nota libera
 * 
 * @author Valentina
 * @version 1.0.0 - 20/06/2016
 * @version 1.0.1 - 08/10/2015
 *
 */
public class RicercaPrimeNoteModel extends GenericBilancioModel {

	private static final long serialVersionUID = -5246607431760922936L;
	
	private PrimaNota primaNota;
	private Integer annoPrimaNota;
	private TipoEvento tipoEvento;
	private ElementoPianoDeiConti elementoPianoDeiConti;
	private Integer annoMovimento;
	private String numeroMovimento;
	private Integer numeroSubmovimento;
	private Ambito ambito;
	
	/**
	 * @return the primaNota
	 */
	public PrimaNota getPrimaNota() {
		return primaNota;
	}

	/**
	 * @param primaNota the primaNota to set
	 */
	public void setPrimaNota(PrimaNota primaNota) {
		this.primaNota = primaNota;
	}

	/**
	 * @return the annoPrimaNota
	 */
	public Integer getAnnoPrimaNota() {
		return annoPrimaNota;
	}

	/**
	 * @param annoPrimaNota the annoPrimaNota to set
	 */
	public void setAnnoPrimaNota(Integer annoPrimaNota) {
		this.annoPrimaNota = annoPrimaNota;
	}

	/**
	 * @return the tipoEvento
	 */
	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	/**
	 * @param tipoEvento the tipoEvento to set
	 */
	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	/**
	 * @return the elementoPianoDeiConti
	 */
	public ElementoPianoDeiConti getElementoPianoDeiConti() {
		return elementoPianoDeiConti;
	}

	/**
	 * @param elementoPianoDeiConti the elementoPianoDeiConti to set
	 */
	public void setElementoPianoDeiConti(ElementoPianoDeiConti elementoPianoDeiConti) {
		this.elementoPianoDeiConti = elementoPianoDeiConti;
	}

	/**
	 * @return the annoMovimento
	 */
	public Integer getAnnoMovimento() {
		return annoMovimento;
	}

	/**
	 * @param annoMovimento the annoMovimento to set
	 */
	public void setAnnoMovimento(Integer annoMovimento) {
		this.annoMovimento = annoMovimento;
	}
	/**
	 * @return the numeroMovimento
	 */
	public String getNumeroMovimento() {
		return numeroMovimento;
	}
	/**
	 * @param numeroMovimento the numeroMovimento to set
	 */
	public void setNumeroMovimento(String numeroMovimento) {
		this.numeroMovimento = numeroMovimento;
	}

	/**
	 * @return the numeroSubmovimento
	 */
	public Integer getNumeroSubmovimento() {
		return numeroSubmovimento;
	}

	/**
	 * @param numeroSubmovimento the numeroSubmovimento to set
	 */
	public void setNumeroSubmovimento(Integer numeroSubmovimento) {
		this.numeroSubmovimento = numeroSubmovimento;
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
	 * Crea una request per il servizio di {@link RicercaPrimeNote}.
	 * @return la request creta
	 */
	public RicercaPrimeNote creaRequestRicercaPrimeNote() {
		RicercaPrimeNote req = creaRequest(RicercaPrimeNote.class);
		primaNota.setAmbito(getAmbito());
		req.setAnnoPrimaNota(annoPrimaNota);
		req.setPrimaNota(primaNota);
		req.setTipoEvento(tipoEvento);
		req.setElementoPianoDeiConti(elementoPianoDeiConti);
		req.setAnnoMovimento(annoMovimento);
		req.setNumeroMoviemento(numeroMovimento);
		req.setNumeroSubmovimento(numeroSubmovimento);
		req.setParametriPaginazione(creaParametriPaginazione());
		return req;
	}

	/**
	 * Crea una request per il servizio di {@link LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno}.
	 * @return la request creta
	 */
	public LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno creaRequestLeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno() {
		LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno req = creaRequest(LeggiClassificatoreGerarchicoByCodiceAndTipoAndAnno.class);
		req.setTipologiaClassificatore(TipologiaClassificatore.PCE_V);
		req.setClassificatore(getElementoPianoDeiConti());
		if(getAnnoPrimaNota() != null){
			req.setAnno(getAnnoPrimaNota());
		}else{
			req.setAnno(getAnnoEsercizioInt());
		}
		return req;
	}
	
}
