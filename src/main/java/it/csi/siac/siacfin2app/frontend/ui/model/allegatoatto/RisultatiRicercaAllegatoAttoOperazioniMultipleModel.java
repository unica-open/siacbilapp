/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.CompletaAllegatoAttoMultiplo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ControlloImportiImpegniVincolati;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchiMultiplo;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAtto;

/**
 * Model per la visualizzazione dei risultati di ricerca per l'Allegato Atto.
 * 
 * @author elisa
 * @version 1.0.0 - 26/feb/2018
 * 
 */
public class RisultatiRicercaAllegatoAttoOperazioniMultipleModel extends RisultatiRicercaAllegatoAttoBaseModel {

	/** Per la serializzazione */

	private static final long serialVersionUID = -7054975590452665030L;
	
	private List <Integer> uidsAllegatiAtto = new ArrayList<Integer>();
	private boolean convalida;
	private boolean convalidaManuale = true;

	/** Costruttore vuoto di default */
	public RisultatiRicercaAllegatoAttoOperazioniMultipleModel() {
		super();
		setTitolo("Risultati di ricerca allegati atto");
//		"effettuaRicercaAllegatoAtto"
	}
	
	/**
	 * Gets the uids allegati atto.
	 *
	 * @return the uidsAllegatiAtto
	 */
	public List<Integer> getUidsAllegatiAtto() {
		return uidsAllegatiAtto;
	}

	/**
	 * Sets the uids allegati atto.
	 *
	 * @param uidsAllegatiAtto the uidsAllegatiAtto to set
	 */
	public void setUidsAllegatiAtto(List<Integer> uidsAllegatiAtto) {
		this.uidsAllegatiAtto = uidsAllegatiAtto != null ? uidsAllegatiAtto : new ArrayList<Integer>();
	}
	
	/**
	 * @return the convalida
	 */
	public boolean isConvalida() {
		return convalida;
	}

	/**
	 * @param convalida the convalida to set
	 */
	public void setConvalida(boolean convalida) {
		this.convalida = convalida;
	}
	
	/**
	 * @return the convalidaManuale
	 */
	public boolean isConvalidaManuale() {
		return convalidaManuale;
	}

	/**
	 * @param convalidaManuale the convalidaManuale to set
	 */
	public void setConvalidaManuale(boolean convalidaManuale) {
		this.convalidaManuale = convalidaManuale;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link CompletaAllegatoAtto}.
	 * 
	 * @return la request creata
	 */
	private CompletaAllegatoAttoMultiplo creaRequestBaseCompletaAllegatoAttoMultiplo() {
		CompletaAllegatoAttoMultiplo request = creaRequest(CompletaAllegatoAttoMultiplo.class);
		request.setBilancio(getBilancio());		
		return request;
	}
	
	/**
	 * Crea request completa allegato atto multiplo per selezionati.
	 *
	 * @return the completa allegato atto multiplo
	 */
	public CompletaAllegatoAttoMultiplo creaRequestCompletaAllegatoAttoMultiploPerSelezionati() {
		CompletaAllegatoAttoMultiplo request = creaRequestBaseCompletaAllegatoAttoMultiplo();
		request.setUidsAllegatiAtto(uidsAllegatiAtto);		
		return request;
	}
	
	/**
	 * Crea request completa allegato atto multiplo per selezionati.
	 *
	 * @param reqInterna the req interna
	 * @return the completa allegato atto multiplo
	 */
	public CompletaAllegatoAttoMultiplo creaRequestCompletaAllegatoAttoMultiploTutti(RicercaAllegatoAtto reqInterna) {
		CompletaAllegatoAttoMultiplo request = creaRequestBaseCompletaAllegatoAttoMultiplo();
		request.setRicercaAllegatoAtto(reqInterna);		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link CompletaAllegatoAtto}.
	 * 
	 * @return la request creata
	 */
	private ConvalidaAllegatoAttoPerElenchiMultiplo creaRequestBaseConvalidaAllegatoAttoPerElenchiMultiplo() {
		ConvalidaAllegatoAttoPerElenchiMultiplo request = creaRequest(ConvalidaAllegatoAttoPerElenchiMultiplo.class);
		request.setFlagConvalidaManuale(Boolean.valueOf(isConvalidaManuale()));
		return request;
	}

	/**
	 * Crea request convalida allegato atto multiplo per selezionati.
	 *
	 * @return the convalida allegato atto per elenchi multiplo
	 */
	public ConvalidaAllegatoAttoPerElenchiMultiplo creaRequestConvalidaAllegatoAttoMultiploPerSelezionati() {
		ConvalidaAllegatoAttoPerElenchiMultiplo req = creaRequestBaseConvalidaAllegatoAttoPerElenchiMultiplo();
		req.setUidsAllegatiAtto(uidsAllegatiAtto);
		return req;
	}

	/**
	 * Crea request convalida allegato atto per elenchi multiplo tutti.
	 *
	 * @param reqInterna the req interna
	 * @return the convalida allegato atto per elenchi multiplo
	 */
	public ConvalidaAllegatoAttoPerElenchiMultiplo creaRequestConvalidaAllegatoAttoPerElenchiMultiploTutti(RicercaAllegatoAtto reqInterna) {
		ConvalidaAllegatoAttoPerElenchiMultiplo req = creaRequestBaseConvalidaAllegatoAttoPerElenchiMultiplo();
		req.setRicercaAllegatoAtto(reqInterna);	
		return req;
	}

	/**
	 * 
	 * @return ControlloImportiImpegniVincolati
	 */
	//SIAC-6688
	public ControlloImportiImpegniVincolati creaRequestControlloImportiImpegniVincolati() {
		ControlloImportiImpegniVincolati request = creaRequest(ControlloImportiImpegniVincolati.class);
		List<Integer> listaAllegatoAttoId = new ArrayList<Integer>();
		List<Integer> lista = getUidsAllegatiAtto();
		for(Integer id : lista){
			listaAllegatoAttoId.add(id);
		}
		request.setListaAllegatoAttoId(listaAllegatoAttoId );		
		return request;
	}
	
}
