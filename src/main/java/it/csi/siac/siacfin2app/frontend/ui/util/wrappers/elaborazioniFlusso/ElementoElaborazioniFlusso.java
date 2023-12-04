/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.elaborazioniFlusso;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import it.csi.siac.pagopa.model.Elaborazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;

/**
 * Wrapper per Elaborazione Flusso.
 * 
 * @author Gambino Vincenzo
 * @version 1.0.0 - 09/07/2020
 *
 */
public class ElementoElaborazioniFlusso implements ModelWrapper, Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2835230601000217374L;
	DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	DateFormat formatterDay = new SimpleDateFormat("dd/MM/yyyy");
	private Elaborazione elaborazione;
	/**
	 * @return the elaborazione
	 */
	public Elaborazione getElaborazione() {
		return elaborazione;
	}

	/**
	 * @param elaborazione the elaborazione to set
	 */
	public void setElaborazione(Elaborazione elaborazione) {
		this.elaborazione = elaborazione;
	}

	private String azioni;
	
	/**
	 * Costruttore a partire dalla superclasse.
	 * 
	 * @param allegatoAtto l'oggetto da wrappare
	 */
	public ElementoElaborazioniFlusso(Elaborazione elaborazione) {
		this.elaborazione = elaborazione;
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
		return elaborazione == null ? 0 : elaborazione.getUid();
	}

	
	
	// Utilita' per il Javascript
	private String dataEmissioneStr;
	private String dataElaborazioneStr;
	private String esitoElaborazioneStato;
	
	/**
	 * @return the esitoElaborazioneStato
	 * if(stato == ELABORATO_OK)
		esito = OK
		else if( stato in(ELABORATO_KO, ELABORATO_ERRATO, ELABORATO_SCARTATO, ELABORATO_IN_CORSO_*))
		esito = KO
		--RIFIUTATO, ANNULLATO, TRASMESSO e IN_ACQUISIZIONE non dovremmo mai trovarli perchè questi flussi non sono mai, o non ancora, stati elaborati
	 */
	public String getEsitoElaborazioneStato() {
		if(this.elaborazione!= null && this.elaborazione.getEsitoElaborazione() != null){
			this.esitoElaborazioneStato = this.elaborazione.getEsitoElaborazione();
		}
		/* SIAC-7975 - CONTABILIA-333 CM 02/03/2021 commentato codice per riallineamento a deploy-coll-4.29 per risoluzioni segnalazioni
		 * 
		 * if(this.elaborazione!= null && this.elaborazione.getStatoElaborazione()!= null){
			if(StatoPagoPAEnum.ELABORATO_OK.name().equals(this.elaborazione.getStatoElaborazione().getCodice())){
				this.esitoElaborazioneStato = "OK";
			}else if(StatoPagoPAEnum.ELABORATO_KO.name().equals(this.elaborazione.getStatoElaborazione().getCodice())||
					StatoPagoPAEnum.ELABORATO_ERRATO.name().equals(this.elaborazione.getStatoElaborazione().getCodice())||
					StatoPagoPAEnum.ELABORATO_SCARTATO.name().equals(this.elaborazione.getStatoElaborazione().getCodice())||
					StatoPagoPAEnum.ELABORATO_IN_CORSO.name().equals(this.elaborazione.getStatoElaborazione().getCodice())||
					StatoPagoPAEnum.ELABORATO_IN_CORSO_ER.name().equals(this.elaborazione.getStatoElaborazione().getCodice())||
					StatoPagoPAEnum.ELABORATO_IN_CORSO_SC.name().equals(this.elaborazione.getStatoElaborazione().getCodice())){
				this.esitoElaborazioneStato = "KO";
			}else{
				this.esitoElaborazioneStato = " ";
			}
		}*/else{
			this.esitoElaborazioneStato = " ";
		}
		return esitoElaborazioneStato;
	}

	/**
	 * @return the dataElaborazioneStr
	 */
	public String getDataElaborazioneStr() {
		if(this.elaborazione!= null && this.elaborazione.getDataElaborazione()!= null){
			dataElaborazioneStr = formatter.format(this.elaborazione.getDataElaborazione());
		}
		return dataElaborazioneStr;
	}

	/**
	 * @return the dataEmissioneStr
	 */
	public String getDataEmissioneStr() {
		if(this.elaborazione!= null && this.elaborazione.getDataEmissioneProvvisorio()!= null){
			dataEmissioneStr = formatterDay.format(this.elaborazione.getDataEmissioneProvvisorio());
		}else dataEmissioneStr = "";
		
//		dataEmissioneStr = "";
//		if(this.elaborazione!= null && this.elaborazione.getDataEmissioneProvvisorioStr()!= null &&
//				this.elaborazione.getDataEmissioneProvvisorioStr().length()>19){
//			StringBuilder sb = new StringBuilder();
//			String[] dayTime = this.elaborazione.getDataEmissioneProvvisorioStr().split(" ");
//			if(dayTime.length==2){
//				//DAY
//				String day = dayTime[0];
//				String[] dayArr = day.split("-");
//				if(dayArr.length==3){
//					sb.append(dayArr[2]).append("/").append(dayArr[1]).append("/").append(dayArr[0]);
//					sb.append(" ");
//				}
//				
//				
//				//TIME
//				String time = dayTime[1];
//				String[] timeArr = time.split("\\.");
//				if(timeArr.length==2){
//					sb.append(timeArr[0]);
//				}
//				dataEmissioneStr = sb.toString();
//			}
//		}
		
		return dataEmissioneStr;
	}
	
	
	

	
}
