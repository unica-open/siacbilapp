/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.anagtipodoc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.commons.TipoDocumentoModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.InserisceTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocumentoFEL;
import it.csi.siac.siacbilser.model.TipoDocFEL;
import it.csi.siac.siacfin2ser.model.TipoDocumento;

/**
 * @author filippo
 *
 */
public class RicercaTipoDocumentoModel extends TipoDocumentoModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 1L;
	
	private TipoDocFEL tipoDocFel;
	
	private List<TipoDocumento> listaDocContabiliaEntrata = new ArrayList<TipoDocumento>();
	private List<TipoDocumento> listaDocContabiliaSpesa = new ArrayList<TipoDocumento>();

	
	
	private Boolean flagSubordinato = Boolean.FALSE;
	private Boolean flagRegolarizzazione = Boolean.FALSE;
	
	
	/** Costruttore vuoto di default */
	public RicercaTipoDocumentoModel() {
		super();
		setTitolo("Ricerca Tipo Documento FEL - Contabilia");
	}

	
	/* Requests */	
	public InserisceTipoDocumentoFEL creaRequestInserisceComponenteCapitolo() {

		InserisceTipoDocumentoFEL request = new InserisceTipoDocumentoFEL();
		
	 
		
		/* aggiungere gli altri? */
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		request.setTipoDocumentoFEL(tipoDocFel);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaTipoDocumentoFEL} a partire dal model.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaTipoDocumentoFEL creaRequestTipoDocumentoFEL() {
		RicercaSinteticaTipoDocumentoFEL request = creaRequest(RicercaSinteticaTipoDocumentoFEL.class);
		
		request.setParametriPaginazione(creaParametriPaginazione());
				
		// Injezione della utility di ricerca
		request.setTipoDocFEL(creaUtilityRicercaComponenteCapitolo());
		
		return request;
	}

	
	/**
	 * Metodo di utilit&agrave; per la ricerca del provvedimento.
	 * 
	 * @return l'utilit&agrave; creata
	 */
	private TipoDocFEL creaUtilityRicercaComponenteCapitolo() {
		RicercaTipoDocumentoFEL utility = new RicercaTipoDocumentoFEL();
		TipoDocFEL tipoDocumentiFEL = new TipoDocFEL();
		 
		
		utility.setTipoDocFEL(tipoDocumentiFEL);
		// Controllo che il provvedimento sia stato inizializzato
		if(tipoDocFel != null) {
			// Injetto la descrizione se sono state inizializzate
			utility.getTipoDocFEL().setCodice(StringUtils.isNotBlank(tipoDocFel.getCodice()) ? tipoDocFel.getCodice().toUpperCase() : null);
			utility.getTipoDocFEL().setDescrizione(StringUtils.isNotBlank(tipoDocFel.getDescrizione()) ? tipoDocFel.getDescrizione() : null);
			utility.getTipoDocFEL().setTipoDocContabiliaEntrata(tipoDocFel.getTipoDocContabiliaEntrata());
			utility.getTipoDocFEL().setTipoDocContabiliaSpesa(tipoDocFel.getTipoDocContabiliaSpesa());
		}
		
		

		
		return utility.getTipoDocFEL();
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
	 * @return the listaDocContabiliaEntrata
	 */
	public List<TipoDocumento> getListaDocContabiliaEntrata()
	{
		return listaDocContabiliaEntrata;
	}


	/**
	 * @param listaDocContabiliaEntrata the listaDocContabiliaEntrata to set
	 */
	public void setListaDocContabiliaEntrata(List<TipoDocumento> listaDocContabiliaEntrata)
	{
		this.listaDocContabiliaEntrata = listaDocContabiliaEntrata;
	}


	/**
	 * @return the listaDocContabiliaSpesa
	 */
	public List<TipoDocumento> getListaDocContabiliaSpesa()
	{
		return listaDocContabiliaSpesa;
	}


	/**
	 * @param listaDocContabiliaSpesa the listaDocContabiliaSpesa to set
	 */
	public void setListaDocContabiliaSpesa(List<TipoDocumento> listaDocContabiliaSpesa)
	{
		this.listaDocContabiliaSpesa = listaDocContabiliaSpesa;
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
	
	
}
