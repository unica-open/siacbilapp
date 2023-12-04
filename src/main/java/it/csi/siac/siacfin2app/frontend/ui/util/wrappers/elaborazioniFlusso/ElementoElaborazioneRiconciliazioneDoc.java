/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.elaborazioniFlusso;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import it.csi.siac.pagopa.model.Errore;
import it.csi.siac.pagopa.model.Riconciliazione;
import it.csi.siac.pagopa.model.RiconciliazioneDoc;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;

/**
 * Wrapper per Elaborazione Flusso.s
 * 
 * @author Gambino Vincenzo
 * @version 1.0.0 - 09/07/2020
 *
 */
public class ElementoElaborazioneRiconciliazioneDoc implements ModelWrapper, Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2835230601000217374L;
	DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private RiconciliazioneDoc riconciliazioneDoc;
	

	private String azioni;
//	private String descrizione;
//	private String tipoOperazioneDocumento;
//	private String codiceFiscale;
//	private String iuv;
//	private String ragioneSociale;
//	private BigDecimal importo;
//	
//	private String codiceVoce;
//	private String descrizioneVoce;
//	private String codiceSottovoce;
	
	private String esito;
	private String infoAccertamento;
	//SIAC-8046 CM 16/03/2021 Task 2.0 e 18/03/2021 Task 2.1 Inizio 
	private String infoAccertamentoNuovo;

	public String getInfoAccertamentoNuovo() {		
		if(this.riconciliazioneDoc!=null && this.riconciliazioneDoc.getRiconciliazione()!= null && 
				this.riconciliazioneDoc.getRiconciliazione().getAnno()!= null &&
				this.riconciliazioneDoc.getRiconciliazione().getNumeroAccertamento()!= null){

			// SIAC-8046 CM 19/03/2021 Task 2.1 Aggiunta condizione per stato file
			if((this.riconciliazioneDoc.getRiconciliazione().getFile()!=null && this.riconciliazioneDoc.getRiconciliazione().getFile().getStatoFile()!=null
					&& (this.riconciliazioneDoc.getRiconciliazione().getFile().getStatoFile().getFilePagopaStatoCode().equals(StatoFileEnum.ELABORATO_ERRATO.getConstant())
							|| this.riconciliazioneDoc.getRiconciliazione().getFile().getStatoFile().getFilePagopaStatoCode().equals(StatoFileEnum.ELABORATO_IN_CORSO.getConstant())
							|| this.riconciliazioneDoc.getRiconciliazione().getFile().getStatoFile().getFilePagopaStatoCode().equals(StatoFileEnum.ELABORATO_IN_CORSO_ER.getConstant())
							|| this.riconciliazioneDoc.getRiconciliazione().getFile().getStatoFile().getFilePagopaStatoCode().equals(StatoFileEnum.ELABORATO_IN_CORSO_SC.getConstant())
							|| this.riconciliazioneDoc.getRiconciliazione().getFile().getStatoFile().getFilePagopaStatoCode().equals(StatoFileEnum.ELABORATO_SCARTATO.getConstant())
							)) 
					&& (this.riconciliazioneDoc.isAzioneAggiornaAttiva()) && (this.riconciliazioneDoc.getErrore()!=null && 
					(this.riconciliazioneDoc.getErrore().getCodice().equals(ErroreRiconciliazioneDocEnum.DATI_DI_RICONCILIAZIONE_SENZA_ESTREMI_ACCERTAMENTO.getConstant()) || 
							this.riconciliazioneDoc.getErrore().getCodice().equals(ErroreRiconciliazioneDocEnum.ERRORE_IN_INSERIMENTO_DATI_DI_DETTAGLIO_ELABORAZIONE_FLUSSO_DI_RICONCILIAZIONE.getConstant()) || 
							this.riconciliazioneDoc.getErrore().getCodice().equals(ErroreRiconciliazioneDocEnum.DATI_RICONCILIAZIONE_CON_ACCERTAMENTO_PRIVO_DI_SOGGETTO_O_INESISTENTE.getConstant())))) {
				
				infoAccertamentoNuovo = " <a   id='aggiornaAccertamento'"
						+ " href='#aggiornaAccertamentoModal' class='aggiornaAccertamento'>"
						+ "<i class='icon-fixed-width icon-pencil'></i></a>  ";
			}else {
				infoAccertamentoNuovo="";
			}
			if(this.riconciliazioneDoc.getAnno().equals(this.riconciliazioneDoc.getRiconciliazione().getAnno()) && this.riconciliazioneDoc.getNumeroAccertamento().equals(this.riconciliazioneDoc.getRiconciliazione().getNumeroAccertamento()) ) {
				infoAccertamentoNuovo += "  ";
			}else {
				infoAccertamentoNuovo += this.riconciliazioneDoc.getRiconciliazione().getAnno() +  "/" + this.riconciliazioneDoc.getRiconciliazione().getNumeroAccertamento();
			}
		}else{
			infoAccertamentoNuovo = "";
		}
		return infoAccertamentoNuovo;
	}
	//SIAC-8046 CM 16-18/03/2021 Fine

	/**
	 * Costruttore a partire dalla superclasse.
	 * 
	 * @param allegatoAtto l'oggetto da wrappare
	 */
	public ElementoElaborazioneRiconciliazioneDoc(RiconciliazioneDoc riconciliazioneDoc) {
		this.riconciliazioneDoc = riconciliazioneDoc;
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
		return riconciliazioneDoc == null ? 0 : riconciliazioneDoc.getUid();
	}


	/**
	 * @return the riconciliazioneErrore
	 */
	public RiconciliazioneDoc getRiconciliazioneDoc() {
		return riconciliazioneDoc;
	}

	/**
	 * @param riconciliazioneErrore the riconciliazioneErrore to set
	 */
	public void setRiconciliazioneDoc(RiconciliazioneDoc riconciliazioneDoc) {
		this.riconciliazioneDoc = riconciliazioneDoc;
	}
	
	// Utilita' per il Javascript
	/**
	 * @return the infoAccertamento
	 */
	public String getInfoAccertamento() {
		
		if(this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getAnno()!= null &&
				this.riconciliazioneDoc.getNumeroAccertamento()!= null){
			infoAccertamento = this.riconciliazioneDoc.getAnno() +  "/" + this.riconciliazioneDoc.getNumeroAccertamento();
		}else{
			infoAccertamento = "";
		}
		return infoAccertamento;
	}
	
	/**
	 * @return the esito
	 */
	public String getEsito() {
		esito = "OK";
		if(this.riconciliazioneDoc != null){
			if(this.riconciliazioneDoc.getSubDocId() != null){
				return esito;
			}
			String msgDefault = "dettagli non presenti";
			if(this.riconciliazioneDoc.getErrore() != null){
				if(this.riconciliazioneDoc.getErroreDettaglio() == null){
					if(this.riconciliazioneDoc.getErrore() != null && this.riconciliazioneDoc.getErrore().getDescrizione()!= null){
						msgDefault = this.riconciliazioneDoc.getErrore().getDescrizione();
						esito = "KO (" + msgDefault + ")";
					}else if(this.riconciliazioneDoc.getMessaggioErrore() != null){
						
						msgDefault = this.riconciliazioneDoc.getMessaggioErrore();						
						//SIAC-8123 CM Intervento 2 30/04/2021 Inizio
						esito = getResDettagliElaboratiDiAggregato(this.riconciliazioneDoc.getErroreDettaglio(), this.riconciliazioneDoc.getErrore(), this.riconciliazioneDoc.getRiconciliazioneDocFlagConDett(), this.riconciliazioneDoc.getRicDocDettagliElaboratiDiAggregato(), msgDefault, esito);
						//SIAC-8123 CM Intervento 2 30/04/2021 Fine
					}
				}else{
					esito = "<a   id='esitoErrore'"
							+ " href='#erroriRiconciliazioniModal' class='erroriRiconciliazione'>"
							+ "<i class='icon-info-sign'></i></a> KO - "+  this.riconciliazioneDoc.getErrore().getDescrizione();
				}
			}else{
				if(this.riconciliazioneDoc.getMessaggioErrore() != null){
					msgDefault= this.riconciliazioneDoc.getMessaggioErrore();
					//SIAC-8123 CM Intervento 2 30/04/2021 Inizio
					esito = getResDettagliElaboratiDiAggregato(this.riconciliazioneDoc.getErroreDettaglio(), this.riconciliazioneDoc.getErrore(), this.riconciliazioneDoc.getRiconciliazioneDocFlagConDett(), this.riconciliazioneDoc.getRicDocDettagliElaboratiDiAggregato(), msgDefault, esito);
					//SIAC-8123 CM Intervento 2 30/04/2021 Fine
				}
			}
		}
		return esito;
	}
	
	//SIAC-8123 CM Intervento 2 30/04/2021 Inizio
	public String getResDettagliElaboratiDiAggregato(Integer erroreDettaglio, Errore errore, Boolean riconciliazioneDocFlagConDett, Integer ricDocDettagliElaboratiDiAggregato, String msgDefault, String esito) {
		
		if(((errore != null && errore.getUid() == 0) || errore == null)
				&& erroreDettaglio == null 
				&& riconciliazioneDocFlagConDett.equals(true)) {
			
			if(ricDocDettagliElaboratiDiAggregato.equals(0)) {
				//lo setto vuoto perchè in questo caso non c'è errore perchè sono stati elaborati tutti 
				//i dettagli della riconciliazione (dato aggregato) e l'elaborzione di tutti questi
				//dettagli è andata a buon fine
				esito = "OK";
				//recordSet.setMessaggioErrore("");
			}else {
				//in questo caso i dettagli della riconciliazione (aggregato) non sono stati elaborati
				//oppure non sono andate a buon fine le elaborazioni
				esito = "KO (" + msgDefault + ")";
				//recordSet.setMessaggioErrore("ERRORE NON ELABORATO");
			}
		} else if(((errore != null && errore.getUid() == 0) || errore == null)
				&& erroreDettaglio == null 
				&& riconciliazioneDocFlagConDett.equals(false)) {
			
			esito = "KO (" + msgDefault + ")";
			//recordSet.setMessaggioErrore("NON ELABORATO PER ERRORE IN ALTRA RIGA");
			
		} else {
			//non ricadiamo nei casi della SIAC-8123
			esito = "KO (" + msgDefault + ")";
			//recordSet.setMessaggioErrore("ERRORE GENERICO NON CENSITO");
		}		
		return esito;
	}
	//SIAC-8123 CM Intervento 2 30/04/2021 Fine
	
	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		if(this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getErrore()!= null &&
				this.riconciliazioneDoc.getErrore().getDescrizione()!= null){
			return this.riconciliazioneDoc.getErrore().getDescrizione();
		}
		return "";
	}
	
	
	/**
	 * @return the tipoOperazioneDocumento
	 */
	public String getTipoOperazioneDocumento() {
		if(this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getTipoDocumento()!= null){
			return this.riconciliazioneDoc.getTipoDocumento().getDescrizione();
		}
		return "";
	}

	/**
	 * @return the codiceFiscale
	 */
	public String getCodiceFiscale() {
		return (this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getCodiceFiscale()!= null) ? this.riconciliazioneDoc.getCodiceFiscale() : "";
	}

	/**
	 * @return the iuv
	 */
	public String getIuv() {
		return (this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getIUV()!= null) ? this.riconciliazioneDoc.getIUV() : "";
	}

	/**
	 * @return the ragioneSociale
	 */
	public String getRagioneSociale() {
		return (this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getRagioneSociale()!= null) ? this.riconciliazioneDoc.getRagioneSociale() : "";
	}

	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return (this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getImporto()!= null) ? this.riconciliazioneDoc.getImporto() : BigDecimal.ZERO;
	}

	/**
	 * @return the codiceVoce
	 */
	public String getCodiceVoce() {
		return (this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getCodiceVoce()!= null) ? this.riconciliazioneDoc.getCodiceVoce() : "";
	}

	/**
	 * @return the descrizioneVoce
	 */
	public String getDescrizioneVoce() {
		return (this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getDescrizioneVoce()!= null) ? this.riconciliazioneDoc.getDescrizioneVoce() : "";
	}

	/**
	 * @return the codiceSottovoce
	 */
	public String getCodiceSottovoce() {
		return (this.riconciliazioneDoc!= null && this.riconciliazioneDoc.getCodiceSottovoce()!= null) ? this.riconciliazioneDoc.getCodiceSottovoce() : "";
	}	
}
