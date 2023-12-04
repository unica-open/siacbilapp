/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfelapp.frontend.ui.model.fatturaelettronica;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocFEL;
import it.csi.siac.siacbilser.model.TipoDocFEL;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodicePCC;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaCodiceUfficioDestinatarioPCC;
import it.csi.siac.siacfin2ser.model.CodicePCC;
import it.csi.siac.siacfin2ser.model.CodiceUfficioDestinatarioPCC;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaSinteticaFatturaElettronica;
import it.csi.siac.sirfelser.model.FatturaFEL;
import it.csi.siac.sirfelser.model.PrestatoreFEL;
import it.csi.siac.sirfelser.model.StatoAcquisizioneFEL;
import it.csi.siac.sirfelser.model.TipoDocumentoFEL;

/**
 * Classe di model per la ricerca della fattura elettronica.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/06/2015
 */
public class RicercaFatturaElettronicaModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3986335559990073341L;
	
	private FatturaFEL fatturaFEL;
	private String codiceFiscale;
	private String partitaIva;
	private Date dataFatturaDa;
	private Date dataFatturaA;
	private CodicePCC codicePCC;
	
	private List<TipoDocumentoFEL> listaTipoDocumentoFEL = new ArrayList<TipoDocumentoFEL>();
	//SIAC-7557
	private List<TipoDocFEL> listaTipoDocumentoFELDB = new ArrayList<TipoDocFEL>();
	
 
	
	
	
	private List<CodiceUfficioDestinatarioPCC> listaCodiceUfficioDestinatarioPCC = new ArrayList<CodiceUfficioDestinatarioPCC>();
	private List<CodicePCC> listaCodicePCC = new ArrayList<CodicePCC>();
	private List<StatoAcquisizioneFEL> listaStatoAcquisizioneFEL = new ArrayList<StatoAcquisizioneFEL>();
	
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioTipoDocumentoFEL}.
	 * 
	 * @return la request creata
	 */
	public RicercaTipoDocFEL creaRequestRicercaTipoDocFEL() {
		RicercaTipoDocFEL request = creaRequest(RicercaTipoDocFEL.class);
		return request;
	}
	
	/** Costruttore vuoto di default */
	public RicercaFatturaElettronicaModel() {
		setTitolo("Ricerca FEL");
	}

	/**
	 * @return the fatturaFEL
	 */
	public FatturaFEL getFatturaFEL() {
		return fatturaFEL;
	}

	/**
	 * @param fatturaFEL the fatturaFEL to set
	 */
	public void setFatturaFEL(FatturaFEL fatturaFEL) {
		this.fatturaFEL = fatturaFEL;
	}

	/**
	 * @return the codiceFiscale
	 */
	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	/**
	 * @param codiceFiscale the codiceFiscale to set
	 */
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	/**
	 * @return the partitaIva
	 */
	public String getPartitaIva() {
		return partitaIva;
	}

	/**
	 * @param partitaIva the partitaIva to set
	 */
	public void setPartitaIva(String partitaIva) {
		this.partitaIva = partitaIva;
	}

	/**
	 * @return the dataFatturaDa
	 */
	public Date getDataFatturaDa() {
		return dataFatturaDa == null ? null : new Date(dataFatturaDa.getTime());
	}

	/**
	 * @param dataFatturaDa the dataFatturaDa to set
	 */
	public void setDataFatturaDa(Date dataFatturaDa) {
		this.dataFatturaDa = dataFatturaDa == null ? null : new Date(dataFatturaDa.getTime());
	}

	/**
	 * @return the dataFatturaA
	 */
	public Date getDataFatturaA() {
		return dataFatturaA == null ? null : new Date(dataFatturaA.getTime());
	}

	/**
	 * @param dataFatturaA the dataFatturaA to set
	 */
	public void setDataFatturaA(Date dataFatturaA) {
		this.dataFatturaA = dataFatturaA == null ? null : new Date(dataFatturaA.getTime());
	}

	/**
	 * @return the codicePCC
	 */
	public CodicePCC getCodicePCC() {
		return codicePCC;
	}

	/**
	 * @param codicePCC the codicePCC to set
	 */
	public void setCodicePCC(CodicePCC codicePCC) {
		this.codicePCC = codicePCC;
	}

	/**
	 * @return the listaTipoDocumentoFEL
	 */
	public List<TipoDocumentoFEL> getListaTipoDocumentoFEL() {
		return listaTipoDocumentoFEL;
	}

	/**
	 * @param listaTipoDocumentoFEL the listaTipoDocumentoFEL to set
	 */
	public void setListaTipoDocumentoFEL(List<TipoDocumentoFEL> listaTipoDocumentoFEL) {
		this.listaTipoDocumentoFEL = listaTipoDocumentoFEL != null ? listaTipoDocumentoFEL : new ArrayList<TipoDocumentoFEL>();
	}
	
	
	
	
	
	
	/**
	 * @return the listaTipoDocumentoFELDB
	 */
	public List<TipoDocFEL> getListaTipoDocumentoFELDB()
	{
		return listaTipoDocumentoFELDB;
	}

	/**
	 * @param listaTipoDocumentoFELDB the listaTipoDocumentoFELDB to set
	 */
	public void setListaTipoDocumentoFELDB(List<TipoDocFEL> listaTipoDocumentoFELDB)
	{
		this.listaTipoDocumentoFELDB = listaTipoDocumentoFELDB;
	}

	/**
	 * @return the listaCodiceUfficioDestinatarioPCC
	 */
	public List<CodiceUfficioDestinatarioPCC> getListaCodiceUfficioDestinatarioPCC() {
		return listaCodiceUfficioDestinatarioPCC;
	}

	/**
	 * @param listaCodiceUfficioDestinatarioPCC the listaCodiceUfficioDestinatarioPCC to set
	 */
	public void setListaCodiceUfficioDestinatarioPCC(List<CodiceUfficioDestinatarioPCC> listaCodiceUfficioDestinatarioPCC) {
		this.listaCodiceUfficioDestinatarioPCC = listaCodiceUfficioDestinatarioPCC != null ? listaCodiceUfficioDestinatarioPCC : new ArrayList<CodiceUfficioDestinatarioPCC>();
	}

	/**
	 * @return the listaCodicePCC
	 */
	public List<CodicePCC> getListaCodicePCC() {
		return listaCodicePCC;
	}

	/**
	 * @param listaCodicePCC the listaCodicePCC to set
	 */
	public void setListaCodicePCC(List<CodicePCC> listaCodicePCC) {
		this.listaCodicePCC = listaCodicePCC != null ? listaCodicePCC : new ArrayList<CodicePCC>();
	}

	/**
	 * @return the listaStatoAcquisizioneFEL
	 */
	public List<StatoAcquisizioneFEL> getListaStatoAcquisizioneFEL() {
		return listaStatoAcquisizioneFEL;
	}

	/**
	 * @param listaStatoAcquisizioneFEL the listaStatoAcquisizioneFEL to set
	 */
	public void setListaStatoAcquisizioneFEL(List<StatoAcquisizioneFEL> listaStatoAcquisizioneFEL) {
		this.listaStatoAcquisizioneFEL = listaStatoAcquisizioneFEL != null ? listaStatoAcquisizioneFEL : new ArrayList<StatoAcquisizioneFEL>();
	}
	
	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link RicercaCodicePCC}.
	 * 
	 * @param listaStrutturaAmministrativoContabile la lista delle strutture in sessione
	 * @return la request creata
	 */
	public RicercaCodicePCC creaRequestRicercaCodicePCC(List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile) {
		// CR-3667
//		request.setStruttureAmministrativoContabili(listaStrutturaAmministrativoContabile);
		return creaRequest(RicercaCodicePCC.class);
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaCodiceUfficioDestinatarioPCC}.
	 * 
	 * @param listaStrutturaAmministrativoContabile la lista delle strutture in sessione
	 * @return la request creata
	 */
	public RicercaCodiceUfficioDestinatarioPCC creaRequestRicercaUfficioPCC(List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile) {
		// CR-3667
//		request.setStruttureAmministrativoContabili(listaStrutturaAmministrativoContabile);
		return creaRequest(RicercaCodiceUfficioDestinatarioPCC.class);
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaFatturaElettronica}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaFatturaElettronica creaRequestRicercaSinteticaFatturaElettronica() {
		RicercaSinteticaFatturaElettronica request = creaRequest(RicercaSinteticaFatturaElettronica.class);
		
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setDataFatturaA(getDataFatturaA());
		request.setDataFatturaDa(getDataFatturaDa());
		
		impostaPrestatoreSePresente();
		getFatturaFEL().setEnte(getEnte());
		request.setFatturaFEL(getFatturaFEL());
		
		return request;
	}

	/**
	 * Impostazione del prestatore all'interno della fattura se presente
	 */
	private void impostaPrestatoreSePresente() {
		boolean isBlankCodiceFiscale = StringUtils.isBlank(getCodiceFiscale());
		boolean isBlankPartitaIva = StringUtils.isBlank(getPartitaIva());
		if(isBlankCodiceFiscale && isBlankPartitaIva) {
			// I due campi non sono valorizzati. Esco
			return;
		}
		
		PrestatoreFEL prestatore = new PrestatoreFEL();
		// Imposto il codice fiscale o la partita iva a seconda di quale dei due sia impostato
		String codicePrestatore = !isBlankCodiceFiscale ? getCodiceFiscale() : getPartitaIva();
		prestatore.setCodicePrestatore(codicePrestatore);
		
		// Imposto il prestatore nella fattura
		getFatturaFEL().setPrestatore(prestatore);
	}

}
