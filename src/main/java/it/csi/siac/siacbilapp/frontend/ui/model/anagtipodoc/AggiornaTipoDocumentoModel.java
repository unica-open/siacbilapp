/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.anagtipodoc;

import java.util.Date;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.TipoDocumentoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.AggiornaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioTipoDocumentoFEL;
import it.csi.siac.siacbilser.model.TipoDocFEL;

/**
 * @author filippo
 *
 */
public class AggiornaTipoDocumentoModel extends TipoDocumentoModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1L;
 

	private TipoDocFEL tipoDocFel;
	
	private Boolean flagSubordinato = Boolean.FALSE;
	private Boolean flagRegolarizzazione = Boolean.FALSE;


	/** Costruttore vuoto di default */
	public AggiornaTipoDocumentoModel() {
		super();
		setTitolo("Aggiorna Tipo Documento FEL - Contabilia");
	}

	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioTipoDocumentoFEL}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioTipoDocumentoFEL creaRequestRicercaDettaglioTipoDocumentoFEL() {
		RicercaDettaglioTipoDocumentoFEL request = creaRequest(RicercaDettaglioTipoDocumentoFEL.class);
		TipoDocFEL tipoDoc = new TipoDocFEL();
		tipoDoc.setCodice(getUidTipoDocumento());
		request.setTipoDocFEL(tipoDoc);
		 
		return request;
	}

	/**
	 * @return the tipoDocFel
	 */
	public TipoDocFEL getTipoDocFel()
	{
		return tipoDocFel;
	}


	/**
	 * @param tipoDocFel the tipoDocFel to set
	 */
	public void setTipoDocFel(TipoDocFEL tipoDocFel)
	{
		this.tipoDocFel = tipoDocFel;
	}


	/**
	 * @return the flagSubordinato
	 */
	public Boolean getFlagSubordinato()
	{
		return flagSubordinato;
	}


	/**
	 * @param flagSubordinato the flagSubordinato to set
	 */
	public void setFlagSubordinato(Boolean flagSubordinato)
	{
		this.flagSubordinato = flagSubordinato;
	}


	/**
	 * @return the flagRegolarizzazione
	 */
	public Boolean getFlagRegolarizzazione()
	{
		return flagRegolarizzazione;
	}


	/**
	 * @param flagRegolarizzazione the flagRegolarizzazione to set
	 */
	public void setFlagRegolarizzazione(Boolean flagRegolarizzazione)
	{
		this.flagRegolarizzazione = flagRegolarizzazione;
	}
	
	 
	/* Requests */	
	public AggiornaTipoDocumentoFEL creaRequestAggiornaTipoDocumentoFEL() {

		AggiornaTipoDocumentoFEL request = new AggiornaTipoDocumentoFEL();

		// Popolamento dei campi standard
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		if (tipoDocFel.getTipoDocContabiliaSpesa() != null && tipoDocFel.getTipoDocContabiliaSpesa().getUid()==0 ) {
			tipoDocFel.setTipoDocContabiliaSpesa(null);
		}
		
		if (tipoDocFel.getTipoDocContabiliaEntrata() != null && tipoDocFel.getTipoDocContabiliaEntrata().getUid()==0 ) {
			tipoDocFel.setTipoDocContabiliaEntrata(null);
		}
		
		request.setTipoDocumentoFEL(tipoDocFel); 
		
		return request;
	}
	
	
}
