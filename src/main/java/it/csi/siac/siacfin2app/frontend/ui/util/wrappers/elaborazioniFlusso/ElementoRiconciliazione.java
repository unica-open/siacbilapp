/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.elaborazioniFlusso;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import it.csi.siac.pagopa.model.Riconciliazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;

/**
 * Wrapper per Elaborazione Flusso.
 * 
 * @author Gambino Vincenzo
 * @version 1.0.0 - 09/07/2020
 *
 */
public class ElementoRiconciliazione implements ModelWrapper, Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2835230601000217374L;
	DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private Riconciliazione riconciliazione;
	
	private String azioni;
	private String esito;
	private String infoAccertamento;
	

	/**
	 * @return the riconciliazione
	 */
	public Riconciliazione getRiconciliazione() {
		return riconciliazione;
	}

	/**
	 * @param riconciliazione the riconciliazione to set
	 */
	public void setRiconciliazione(Riconciliazione riconciliazione) {
		this.riconciliazione = riconciliazione;
	}
	
	/**
	 * Costruttore a partire dalla superclasse.
	 * 
	 * @param allegatoAtto l'oggetto da wrappare
	 */
	public ElementoRiconciliazione(Riconciliazione riconciliazione) {
		this.riconciliazione = riconciliazione;
	}

	/**
	 * @return the azioni
	 */
	public String getAzioni() {
		return azioni;
	}

	/**
	 * @param azioni the azioni to set
	 */
	public void setAzioni(String azioni) {
		this.azioni = azioni;
	}

	@Override
	public int getUid() {
		return riconciliazione == null ? 0 : riconciliazione.getUid();
	}

	
	
	// Utilita' per il Javascript
	/**
	 * @return the esito
	 */
	public String getEsito() {
		if(this.riconciliazione!= null &&  this.riconciliazione.getErrore()!= null){
			esito = "<a   id='esitoErrore'"
					+ " href='#erroriRiconciliazioniModal' class='erroriRiconciliazione'>"
							+ "<i class='icon-info-sign'></i></a> KO - descrizione dettagliata errore";
		}else{
			esito = "OK";
		}
		
		
		return esito;
	}

	/**
	 * @return the infoAccertamento
	 */
	public String getInfoAccertamento() {
		
		if(this.riconciliazione!= null && this.riconciliazione.getAnno()!= null &&
				this.riconciliazione.getNumeroAccertamento()!= null){
			infoAccertamento = this.riconciliazione.getAnno() +  " " + this.riconciliazione.getNumeroAccertamento();
		}else{
			infoAccertamento = "";
		}
		
		
		
		
		
		return infoAccertamento;
	}
	
	

	
}
