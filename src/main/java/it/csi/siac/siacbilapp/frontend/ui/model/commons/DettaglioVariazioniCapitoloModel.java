/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.commons;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaVariazioniSingoloCapitolo;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ric.SegnoImporti;

/**
 * Classe di model per la consultazione del Capitolo di Uscita Previsione. Contiene una mappatura dei form per il
 * Capitolo di Uscita Previsione.
 * 
 * @author Daniele Argiolas
 * @version 1.1.2 27/06/2013
 *
 */

public class DettaglioVariazioniCapitoloModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8593581741059902904L;
	
	private Capitolo<?, ?> capitolo;
	private Boolean variazioneInAumento;
	
	/** Costruttore vuoto di default */
	public DettaglioVariazioniCapitoloModel() {
		super();
	}
	
	/**
	 * @return the capitolo
	 */
	public Capitolo<?, ?> getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo the capitolo to set
	 */
	public void setCapitolo(Capitolo<?, ?> capitolo) {
		this.capitolo = capitolo;
	}

	/**
	 * @return the variazioneInAumento
	 */
	public Boolean getVariazioneInAumento() {
		return variazioneInAumento;
	}

	/**
	 * @param variazioneInAumento the variazioneInAumento to set
	 */
	public void setVariazioneInAumento(Boolean variazioneInAumento) {
		this.variazioneInAumento = variazioneInAumento;
	}
	
	/**
	 * @return the testoZeroVariazioni
	 */
	public String getTestoZeroVariazioni() {
		return Boolean.TRUE.equals(getVariazioneInAumento()) ? "Non sono presenti variazioni in aumento associate al capitolo" : "Non sono presenti variazioni in diminuzione associate al capitolo";
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaVariazioniSingoloCapitolo}.
	 * @return la request creata
	 */
	public RicercaSinteticaVariazioniSingoloCapitolo creaRequestRicercaSinteticaVariazioniSingoloCapitolo() {
		RicercaSinteticaVariazioniSingoloCapitolo req = creaRequest(RicercaSinteticaVariazioniSingoloCapitolo.class);
		
		req.setBilancio(getBilancio());
		req.setCapitolo(getCapitolo());
		req.setEnte(getEnte());
		//CONTABILIA-285
		if(getVariazioneInAumento()== null){
			req.setSegnoImportiVariazione(SegnoImporti.NULLO);
		}else{
			req.setSegnoImportiVariazione(Boolean.TRUE.equals(getVariazioneInAumento()) ? SegnoImporti.POSITIVO : SegnoImporti.NEGATIVO);
		}
		
		
		
		
		req.setParametriPaginazione(creaParametriPaginazione(5));
		
		return req;
	}
	
}
