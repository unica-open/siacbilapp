/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.stampe;

import java.util.Date;

import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaUltimaStampaDefinitivaGiornaleCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaGiornaleCassa;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.StampaGiornale;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccecser.model.TipoDocumento;
import it.csi.siac.siaccecser.model.TipoStampa;



/**
 * Classe di model generica per le stampa del giornale di cassa CEC.
 * 
 * @author Paggio Simona,Nazha Ahmad
 * @version 1.0.0 - 10/02/2015
 * @version 1.0.1 - 31/03/2015
 *
 */
public class StampaCECGiornaleCassaModel extends GenericStampaCECModel {

	/** per serializzazione	 */
	private static final long serialVersionUID = 1644191862625565588L;
	
	private Date dataSistema;
	private Date dataDaElaborare;
	private Date dataUltimaStampaDef;
	private Integer uidCassaEconomale;

	private StampaGiornale stampaGiornale=new StampaGiornale();
	
	private boolean flagStampaEffettuata=Boolean.FALSE;

	/** Costruttore vuoto di default */
	public StampaCECGiornaleCassaModel() {
		setTitolo("Stampa Giornale di cassa");
	}

	



	/**
	 * @return the uidCassaEconomale
	 */
	public Integer getUidCassaEconomale() {
		return uidCassaEconomale;
	}





	/**
	 * @param uidCassaEconomale the uidCassaEconomale to set
	 */
	public void setUidCassaEconomale(Integer uidCassaEconomale) {
		this.uidCassaEconomale = uidCassaEconomale;
	}





	/**
	 * @return the dataSistema
	 */
	public Date getDataSistema() {
		return dataSistema == null ? null : new Date(dataSistema.getTime());
	}


	/**
	 * @param dataSistema the dataSistema to set
	 */
	public void setDataSistema(Date dataSistema) {
		this.dataSistema = dataSistema == null ? null : new Date(dataSistema.getTime());
	}


	/**
	 * @return the dataDaElaborare
	 */
	public Date getDataDaElaborare() {
		return dataDaElaborare == null ? null : new Date(dataDaElaborare.getTime());
	}

	/**
	 * @param dataDaElaborare the dataDaElaborare to set
	 */
	public void setDataDaElaborare(Date dataDaElaborare) {
		this.dataDaElaborare = dataDaElaborare == null ? null : new Date(dataDaElaborare.getTime());
	}

	/**
	 * @return the stampaGiornale
	 */
	public StampaGiornale getStampaGiornale() {
		return stampaGiornale;
	}

	/**
	 * @param stampaGiornale the stampaGiornale to set
	 */
	public void setStampaGiornale(StampaGiornale stampaGiornale) {
		this.stampaGiornale = stampaGiornale;
	}
	
	/**
	 * url della action di stampa giornale cassa
	 * @return url
	 */
	public String getUrlStampaGiornaleDiCassa() {
		return "cassaEconomaleStampe_stampaGiornaleDiCassa.do";
	}
	
	/*
	 * request
	 */
	
	/**
	 * @return the flagStampaEffettuata
	 */
	public boolean isFlagStampaEffettuata() {
		return flagStampaEffettuata;
	}


	/**
	 * @param flagStampaEffettuata the flagStampaEffettuata to set
	 */
	public void setFlagStampaEffettuata(boolean flagStampaEffettuata) {
		this.flagStampaEffettuata = flagStampaEffettuata;
	}


	/**
	 * @return the dataUltimaStampaDef
	 */
	public Date getDataUltimaStampaDef() {
		return dataUltimaStampaDef == null ? null : new Date(dataUltimaStampaDef.getTime());
	}


	/**
	 * @param dataUltimaStampaDef the dataUltimaStampaDef to set
	 */
	public void setDataUltimaStampaDef(Date dataUltimaStampaDef) {
		this.dataUltimaStampaDef = dataUltimaStampaDef == null ? null : new Date(dataUltimaStampaDef.getTime());
	}


	/**
	 * crea la request findDatiUltimaStampaDefinitivaGiornaleCassa
	 * @return la request creata
	 */
	public RicercaUltimaStampaDefinitivaGiornaleCassa creaRequestRicercaDatiUltimaStampaDefinitivaGiornaleCassa() {
		RicercaUltimaStampaDefinitivaGiornaleCassa request = creaRequest(RicercaUltimaStampaDefinitivaGiornaleCassa.class);
		
		StampeCassaFile stampeCassaFile = new StampeCassaFile();
		stampeCassaFile.setBilancio(getBilancio());
		stampeCassaFile.setEnte(getEnte());
		stampeCassaFile.setTipoDocumento(TipoDocumento.GIORNALE_CASSA);
		stampeCassaFile.setTipoStampa(TipoStampa.DEFINITIVA);
		
		CassaEconomale cassa = new CassaEconomale();
		cassa.setUid(uidCassaEconomale);
		stampeCassaFile.setCassaEconomale(cassa);

		request.setStampeCassaFile(stampeCassaFile);
		
		return request;
		
		
	}
	/**
	 * crea la request per stampare un giornale di cassa
	 * 
	 * @return la request 
	 */
	public StampaGiornaleCassa creaRequestStampaGiornaleCassa() {
		StampaGiornaleCassa request= creaRequest(StampaGiornaleCassa.class);
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setBilancio(getBilancio());
		request.setTipoStampa(getTipoStampa());
		request.setDataStampaGiornale(dataDaElaborare);
		
		request.setProseguiCEC_INF_0017(Boolean.TRUE);
		request.setCassaEconomale(getCassaEconomale());
		
		return request;
	}

	/**
	 * imposta i dati neccessari all'interfaccia di stampa corrsipondente
	 */
	public void impostaDatiNelModel() {
		setFlagStampaEffettuata(Boolean.FALSE);
		setDataDaElaborare(new Date());
		setTipoStampa(TipoStampa.BOZZA);
		setDataSistema(new Date());
		
	}

	
}
