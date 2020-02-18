/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.stampe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.CassaEconomaleBaseModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaStampeCassaEconomale;
import it.csi.siac.siaccecser.model.CassaEconomale;
import it.csi.siac.siaccecser.model.StampeCassaFile;
import it.csi.siac.siaccecser.model.TipoDocumento;
import it.csi.siac.siaccecser.model.TipoStampa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaStampaIva;

/**
 * Classe di model per la ricerca delle StampeCassaFile.
 * 
 * @author Valentina Triolo
 * @version 1.0.0 - 31/03/2015
 *
 */
public class RicercaStampeCassaEconomaleModel extends CassaEconomaleBaseModel {
	
	private static final long serialVersionUID = -8732614137703827938L;
	
	private StampeCassaFile stampeCassaFile;
	
	private List<TipoDocumento> listaTipoDocumento = new ArrayList<TipoDocumento>();
	private List<TipoStampa> listaTipoStampa = new ArrayList<TipoStampa>();
	private List<CassaEconomale> listaCasseEconomali = new ArrayList<CassaEconomale>();
	
	private Date dataGiornaleUltimaStampaDa;
	private Date dataGiornaleUltimaStampaA;
	
	
	/** Costruttore vuoto di default */
	public RicercaStampeCassaEconomaleModel() {
		super();
		setTitolo("Ricerca stampe cassa economale");
	}

	
	/**
	 * @return the stampeCassaFile
	 */
	public StampeCassaFile getStampeCassaFile() {
		return stampeCassaFile;
	}

	/**
	 * @param stampeCassaFile the stampeCassaFile to set
	 */
	public void setStampeCassaFile(StampeCassaFile stampeCassaFile) {
		this.stampeCassaFile = stampeCassaFile;
	}
	
	/**
	 * @return the unicaCassa
	 */
	public Boolean getUnicaCassa() {
		return getListaCasseEconomali() != null && getListaCasseEconomali().size() == 1;
	}

	/**
	 * @return the listaTipoDocumento
	 */
	public List<TipoDocumento> getListaTipoDocumento() {
		return listaTipoDocumento;
	}

	/**
	 * @param listaTipoDocumento the listaTipoDocumento to set
	 */
	public void setListaTipoDocumento(List<TipoDocumento> listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento;
	}

	/**
	 * @return the listaTipoStampa
	 */
	public List<TipoStampa> getListaTipoStampa() {
		return listaTipoStampa;
	}

	/**
	 * @param listaTipoStampa the listaTipoStampa to set
	 */
	public void setListaTipoStampa(List<TipoStampa> listaTipoStampa) {
		this.listaTipoStampa = listaTipoStampa;
	}

	/**
	 * @return the listaCasseEconomali
	 */
	public List<CassaEconomale> getListaCasseEconomali() {
		return listaCasseEconomali;
	}

	/**
	 * @param listaCasseEconomali the listaCasseEconomali to set
	 */
	public void setListaCasseEconomali(List<CassaEconomale> listaCasseEconomali) {
		this.listaCasseEconomali = listaCasseEconomali;
	}

	/**
	 * @return the dataGiornaleUltimaStampaDa
	 */
	public Date getDataGiornaleUltimaStampaDa() {
		return dataGiornaleUltimaStampaDa != null ? new Date(dataGiornaleUltimaStampaDa.getTime()) : null;
	}

	/**
	 * @param dataGiornaleUltimaStampaDa the dataGiornaleUltimaStampaDa to set
	 */
	public void setDataGiornaleUltimaStampaDa(Date dataGiornaleUltimaStampaDa) {
		this.dataGiornaleUltimaStampaDa = dataGiornaleUltimaStampaDa != null ? new Date(dataGiornaleUltimaStampaDa.getTime()) : null;
	}

	/**
	 * @return the dataGiornaleUltimaStampaA
	 */
	public Date getDataGiornaleUltimaStampaA() {
		return dataGiornaleUltimaStampaA != null ? new Date(dataGiornaleUltimaStampaA.getTime()) : null;
	}

	/**
	 * @param dataGiornaleUltimaStampaA the dataGiornaleUltimaStampaA to set
	 */
	public void setDataGiornaleUltimaStampaA(Date dataGiornaleUltimaStampaA) {
		this.dataGiornaleUltimaStampaA = dataGiornaleUltimaStampaA != null ? new Date(dataGiornaleUltimaStampaA.getTime()) : null;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaStampaIva}.
	 * 
	 * @return la request creata
	 */
	public RicercaStampeCassaEconomale creaRequestRicercaStampeCassaEconomale() {
		RicercaStampeCassaEconomale request = creaRequest(RicercaStampeCassaEconomale.class);
		
		stampeCassaFile.setBilancio(getBilancio());
		stampeCassaFile.setCassaEconomale(getCassaEconomale());
		
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setStampa(stampeCassaFile);

		request.setDataUltimaStampaGiornaleDa(dataGiornaleUltimaStampaDa);

		request.setDataUltimaStampaGiornaleA(dataGiornaleUltimaStampaA);
		return request;
	}

	
	@Override
	public RicercaSinteticaCassaEconomale creaRequestRicercaSinteticaCassaEconomale() {
		
		RicercaSinteticaCassaEconomale request = creaRequest(RicercaSinteticaCassaEconomale.class);
		request.setBilancio(getBilancio());
		return request;
	}

}
