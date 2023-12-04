/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax;

import java.math.BigDecimal;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.CalcoloDisponibilitaDiUnCapitolo;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siaccorser.model.StatoBilancio;

/**
 * Classe di model per le azioni generiche del capitolo. Contiene una mappatura dei campi necessar&icirc; alle invocazioni.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 19/09/2013
 *
 */
public class CapitoloAjaxModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6683123014201191204L;
	
	// Campi di input
	private Integer annoCapitolo;
	private FaseBilancio faseBilancio;
	private StatoBilancio statoBilancio;
	private Integer numeroCapitolo;
	private String tipoDisponibilitaRichiesta;
	
	// Campi di output
	private BigDecimal disponibilitaVariare;
	
	/** Costruttore vuoto di default */
	public CapitoloAjaxModel() {
		super();
	}
	
	/**
	 * @return the annoCapitolo
	 */
	public Integer getAnnoCapitolo() {
		return annoCapitolo;
	}

	/**
	 * @param annoCapitolo the annoCapitolo to set
	 */
	public void setAnnoCapitolo(Integer annoCapitolo) {
		this.annoCapitolo = annoCapitolo;
	}

	/**
	 * @return the faseBilancio
	 */
	public FaseBilancio getFaseBilancio() {
		return faseBilancio;
	}

	/**
	 * @param faseBilancio the faseBilancio to set
	 */
	public void setFaseBilancio(FaseBilancio faseBilancio) {
		this.faseBilancio = faseBilancio;
	}

	/**
	 * @return the statoBilancio
	 */
	public StatoBilancio getStatoBilancio() {
		return statoBilancio;
	}

	/**
	 * @param statoBilancio the statoBilancio to set
	 */
	public void setStatoBilancio(StatoBilancio statoBilancio) {
		this.statoBilancio = statoBilancio;
	}

	/**
	 * @return the numeroCapitolo
	 */
	public Integer getNumeroCapitolo() {
		return numeroCapitolo;
	}

	/**
	 * @param numeroCapitolo the numeroCapitolo to set
	 */
	public void setNumeroCapitolo(Integer numeroCapitolo) {
		this.numeroCapitolo = numeroCapitolo;
	}

	/**
	 * @return the tipoDisponibilitaRichiesta
	 */
	public String getTipoDisponibilitaRichiesta() {
		return tipoDisponibilitaRichiesta;
	}

	/**
	 * @param tipoDisponibilitaRichiesta the tipoDisponibilitaRichiesta to set
	 */
	public void setTipoDisponibilitaRichiesta(String tipoDisponibilitaRichiesta) {
		this.tipoDisponibilitaRichiesta = tipoDisponibilitaRichiesta;
	}
	
	/**
	 * @return the disponibilitaVariare
	 */
	public BigDecimal getDisponibilitaVariare() {
		return disponibilitaVariare;
	}

	/**
	 * @param disponibilitaVariare the disponibilitaVariare to set
	 */
	public void setDisponibilitaVariare(BigDecimal disponibilitaVariare) {
		this.disponibilitaVariare = disponibilitaVariare;
	}

	/* Requests */
	
	/**
	 * Crea una request per il servizio di {@link CalcoloDisponibilitaDiUnCapitolo} a partire dal model.
	 * 
	 * @return la request creata
	 */
	public CalcoloDisponibilitaDiUnCapitolo creaRequestCalcoloDisponibilitaDiUnCapitolo() {
		CalcoloDisponibilitaDiUnCapitolo request = creaRequest(CalcoloDisponibilitaDiUnCapitolo.class);
		
		request.setAnnoCapitolo(annoCapitolo);
		request.setBilancio(getBilancio());
		request.setEnte(getEnte());
		request.setFase(getFaseEStatoAttualeBilancio());
		request.setNumroCapitolo(numeroCapitolo);
		request.setRichiedente(getRichiedente());
		request.setTipoDisponibilitaRichiesta(tipoDisponibilitaRichiesta);
		
		return request;
	}
	
	/**
	 * Classe di utilit&agrave; per la creazione di una {@link FaseEStatoAttualeBilancio}.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private FaseEStatoAttualeBilancio getFaseEStatoAttualeBilancio() {
		FaseEStatoAttualeBilancio utility = new FaseEStatoAttualeBilancio();
		
		utility.setFaseBilancio(faseBilancio);
		utility.setStatoBilancio(statoBilancio);
		
		return utility;
	}
	
}
