/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste;

import java.io.InputStream;
import java.util.Date;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilapp.frontend.ui.util.converter.BooleanIntegerConverter;
import it.csi.siac.siaccecser.frontend.webservice.msg.AnnullaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.ControllaAggiornabilitaRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRendicontoRichiestaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaRicevutaRichiestaEconomale;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;


/**
 * Classe base di model per i risultati della ricerca della richiesta economale.
 * 
 * @author Marchino Alessandro,Nazha Ahmad
 * @version 1.0.0 - 02/02/2015
 * @version 1.0.1 - 17/04/2015
 */
public abstract class BaseRisultatiRicercaRichiestaEconomaleModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7746110979597905485L;
	
	private Integer uidRichiesta;
	private Integer savedDisplayStart;
	
	//campi aggiunti il 17/04/2014 ahmad
	private Integer uidRichiestaDaStampare;
	private String contentType;
	private Long contentLength;
	private String fileName;
	private transient InputStream inputStream;
	//fine parte aggiunta 
	/**
	 * @return the uidRichiesta
	 */
	public Integer getUidRichiesta() {
		return uidRichiesta;
	}

	/**
	 * @param uidRichiesta the uidRichiesta to set
	 */
	public void setUidRichiesta(Integer uidRichiesta) {
		this.uidRichiesta = uidRichiesta;
	}

	/**
	 * @return the savedDisplayStart
	 */
	public Integer getSavedDisplayStart() {
		return savedDisplayStart;
	}

	/**
	 * @param savedDisplayStart the savedDisplayStart to set
	 */
	public void setSavedDisplayStart(Integer savedDisplayStart) {
		this.savedDisplayStart = savedDisplayStart;
	}
	
	/**
	 * @return the denominazioneRisultatiRicerca
	 */
	public abstract String getDenominazioneRisultatiRicerca();
	
	/**
	 * @return the pathTipoRichiestaEconomale
	 */
	public abstract String getPathTipoRichiestaEconomale();
	
	
	/* **** Requests **** */
	
	/**
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the contentLength
	 */
	public Long getContentLength() {
		return contentLength;
	}

	/**
	 * @param contentLength the contentLength to set
	 */
	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the inputStream
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	/**
	 * @param inputStream the inputStream to set
	 */
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}



	/**
	 * @return the uidRichiestaDaStampare
	 */
	public Integer getUidRichiestaDaStampare() {
		return uidRichiestaDaStampare;
	}

	/**
	 * @param uidRichiestaDaStampare the uidRichiestaDaStampare to set
	 */
	public void setUidRichiestaDaStampare(Integer uidRichiestaDaStampare) {
		this.uidRichiestaDaStampare = uidRichiestaDaStampare;
	}
	
	// SIAC-4618
	
	/**
	 * @return the hasNumeroRichiesta
	 */
	public boolean isHasNumeroRichiesta() {
		return true;
	}
	/**
	 * @return the hasData
	 */
	public boolean isHasData() {
		return true;
	}
	/**
	 * @return the hasStato
	 */
	public boolean isHasStato() {
		return true;
	}
	/**
	 * @return the hasNumeroSospeso
	 */
	public boolean isHasNumeroSospeso() {
		return true;
	}
	/**
	 * @return the hasNumeroMovimento
	 */
	public boolean isHasNumeroMovimento() {
		return true;
	}
	/**
	 * @return the hasRichiedente
	 */
	public boolean isHasRichiedente() {
		return true;
	}
	/**
	 * @return the hasImporto
	 */
	public boolean isHasImporto() {
		return true;
	}
	/**
	 * @return the hasNumeroMovimentoRendiconto
	 */
	public boolean isHasNumeroMovimentoRendiconto() {
		return false;
	}
	
	/**
	 * @return the intestazioneImporto
	 */
	public String getIntestazioneImporto() {
		return "Importo";
	}
	
	/**
	 * @return the intestazioneTotaleImporto
	 */
	public String getIntestazioneTotaleImporto() {
		return "Totale";
	}
	
	/**
	 * @return the colspanTotaleImporto
	 */
	public int getColspanTotaleImporto() {
		return BooleanIntegerConverter.toInteger(isHasNumeroRichiesta())
				+ BooleanIntegerConverter.toInteger(isHasData())
				+ BooleanIntegerConverter.toInteger(isHasStato())
				+ BooleanIntegerConverter.toInteger(isHasNumeroSospeso())
				+ BooleanIntegerConverter.toInteger(isHasNumeroMovimento())
				+ BooleanIntegerConverter.toInteger(isHasRichiedente())
				+ BooleanIntegerConverter.toInteger(isHasNumeroMovimentoRendiconto());
	}

	/**
	 * Crea una request per il servizio di {@link AnnullaRichiestaEconomale}.
	 * 
	 * @return la request creata
	 */
	public AnnullaRichiestaEconomale creaRequestAnnullaRichiestaEconomale() {
		AnnullaRichiestaEconomale request = creaRequest(AnnullaRichiestaEconomale.class);
		
		RichiestaEconomale richiestaEconomale = new RichiestaEconomale();
		richiestaEconomale.setUid(getUidRichiesta());
		request.setRichiestaEconomale(richiestaEconomale);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link StampaRicevutaRichiestaEconomale}.
	 * 
	 * @return la request creata
	 */
	public StampaRicevutaRichiestaEconomale creaRequestStampaRicevutaRichiestaEconomale() {
		StampaRicevutaRichiestaEconomale request = creaRequest(StampaRicevutaRichiestaEconomale.class);
		request.setBilancio(getBilancio());
		request.setRichiedente(getRichiedente());
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		RichiestaEconomale richiestaEconomale = new RichiestaEconomale();
		richiestaEconomale.setUid(getUidRichiestaDaStampare());
		request.setRichiestaEconomale(richiestaEconomale);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link StampaRicevutaRendicontoRichiestaEconomale}.
	 * 
	 * @return la request creata
	 */
	public StampaRicevutaRendicontoRichiestaEconomale creaRequestStampaRicevutaRendicontoRichiestaEconomale() {
		StampaRicevutaRendicontoRichiestaEconomale req = creaRequest(StampaRicevutaRendicontoRichiestaEconomale.class);
		req.setBilancio(getBilancio());
		req.setEnte(getEnte());
		
		RendicontoRichiesta rendicontoRichiesta = new RendicontoRichiesta();
		rendicontoRichiesta.setUid(getUidRichiestaDaStampare());
		req.setRendicontoRichiesta(rendicontoRichiesta);
		
		return req;
	}
	
	/**
	 * Crea una request per il servizio di {@link ControllaAggiornabilitaRichiestaEconomale}.
	 * 
	 * @return la request creata
	 */
	public ControllaAggiornabilitaRichiestaEconomale creaRequestControllaAggiornabilitaRichiestaEconomale() {
		ControllaAggiornabilitaRichiestaEconomale request = creaRequest(ControllaAggiornabilitaRichiestaEconomale.class);
		
		RichiestaEconomale richiestaEconomale = new RichiestaEconomale();
		richiestaEconomale.setUid(getUidRichiesta());
		request.setRichiestaEconomale(richiestaEconomale);
		
		return request;
	}
}
