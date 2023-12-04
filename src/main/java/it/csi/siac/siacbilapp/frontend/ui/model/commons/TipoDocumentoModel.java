/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.commons;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioTipoDocumentoFEL;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaTipoDocumentoFEL;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaTipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoFamigliaDocumento;

public class TipoDocumentoModel extends GenericBilancioModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String codice;
	private String descrizione;
	 
	 
	
	//Prevedo la possibilita' di inserire una coppia FEL - Contabilia già esistente
	private boolean richiediConfermaUtente;
	private Boolean forzaTipoDocumento = false;
	private String messaggioRichiestaConfermaProsecuzione = "";
	
	
	private  TipoDocumento tipoDocContabiliaEntrata;
	private  TipoDocumento tipoDocContabiliaSpesa;
	
 
	
	private String uidTipoDocumento;
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoDocumento}.
	 * 
	 * @param tipoFamigliaDocumento la famiglia del documento (Entrata / Spesa)
	 * @param flagSubordinato       se il documento &eacute; subordinato
	 * @param flagRegolarizzazione  se il documento &eacute; una regolarizzazione
	 * 
	 * @return la request creata
	 */
	public RicercaTipoDocumento creaRequestRicercaTipoDocumento(TipoFamigliaDocumento tipoFamigliaDocumento, Boolean flagSubordinato, Boolean flagRegolarizzazione) {
		RicercaTipoDocumento request = new RicercaTipoDocumento();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setFlagRegolarizzazione(flagRegolarizzazione);
		request.setFlagSubordinato(flagSubordinato);
		request.setRichiedente(getRichiedente());
		request.setTipoFamDoc(tipoFamigliaDocumento);
		
		return request;
	}
	
	
	
	private List<TipoDocumento> listaTipoDocContabiliaEntrata = new ArrayList<TipoDocumento>();
	private List<TipoDocumento> listaTipoDocContabiliaSpesa = new ArrayList<TipoDocumento>();
	
	
	private List<TipoDocumento> listaTipoDocContabiliaEntrataClone = new ArrayList<TipoDocumento>();
	private List<TipoDocumento> listaTipoDocContabiliaSpesaClone = new ArrayList<TipoDocumento>();
	 
	
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioTipoDocumentoFEL}.
	 * 
	 * @return la request creata
	 */
	public RicercaTipoDocumentoFEL creaRequestRicercaTipoDocumentoFEL() {
		RicercaTipoDocumentoFEL request = creaRequest(RicercaTipoDocumentoFEL.class);
	
		 
		return request;
	}
	 
	/**
	 * @return the uidTipoDocumento
	 */
	public String getUidTipoDocumento()
	{
		return uidTipoDocumento;
	}
	/**
	 * @param uidTipoDocumento the uidTipoDocumento to set
	 */
	public void setUidTipoDocumento(String uidTipoDocumento)
	{
		this.uidTipoDocumento = uidTipoDocumento;
	}
	/**
	 * @return the codice
	 */
	public String getCodice()
	{
		return codice;
	}
	/**
	 * @param codice the codice to set
	 */
	public void setCodice(String codice)
	{
		this.codice = codice;
	}
	/**
	 * @return the descrizione
	 */
	public String getDescrizione()
	{
		return descrizione;
	}
	/**
	 * @param descrizione the descrizione to set
	 */
	public void setDescrizione(String descrizione)
	{
		this.descrizione = descrizione;
	}
	/**
	 * @return the tipoDocContabiliaEntrata
	 */
	public TipoDocumento getTipoDocContabiliaEntrata()
	{
		return tipoDocContabiliaEntrata;
	}
	/**
	 * @param tipoDocContabiliaEntrata the tipoDocContabiliaEntrata to set
	 */
	public void setTipoDocContabiliaEntrata(TipoDocumento tipoDocContabiliaEntrata)
	{
		this.tipoDocContabiliaEntrata = tipoDocContabiliaEntrata;
	}
	/**
	 * @return the tipoDocContabiliaSpesa
	 */
	public TipoDocumento getTipoDocContabiliaSpesa()
	{
		return tipoDocContabiliaSpesa;
	}
	/**
	 * @param tipoDocContabiliaSpesa the tipoDocContabiliaSpesa to set
	 */
	public void setTipoDocContabiliaSpesa(TipoDocumento tipoDocContabiliaSpesa)
	{
		this.tipoDocContabiliaSpesa = tipoDocContabiliaSpesa;
	}
	/**
	 * @return the listaTipoDocContabiliaEntrata
	 */
	public List<TipoDocumento> getListaTipoDocContabiliaEntrata()
	{
		return listaTipoDocContabiliaEntrata;
	}
	/**
	 * @param listaTipoDocContabiliaEntrata the listaTipoDocContabiliaEntrata to set
	 */
	public void setListaTipoDocContabiliaEntrata(List<TipoDocumento> listaTipoDocContabiliaEntrata)
	{
		this.listaTipoDocContabiliaEntrata = listaTipoDocContabiliaEntrata;
	}
	/**
	 * @return the listaTipoDocContabiliaSpesa
	 */
	public List<TipoDocumento> getListaTipoDocContabiliaSpesa()
	{
		return listaTipoDocContabiliaSpesa;
	}
	/**
	 * @param listaTipoDocContabiliaSpesa the listaTipoDocContabiliaSpesa to set
	 */
	public void setListaTipoDocContabiliaSpesa(List<TipoDocumento> listaTipoDocContabiliaSpesa)
	{
		this.listaTipoDocContabiliaSpesa = listaTipoDocContabiliaSpesa;
	}
	/**
	 * @return the listaTipoDocContabiliaEntrataClone
	 */
	public List<TipoDocumento> getListaTipoDocContabiliaEntrataClone()
	{
		return listaTipoDocContabiliaEntrataClone;
	}
	/**
	 * @param listaTipoDocContabiliaEntrataClone the listaTipoDocContabiliaEntrataClone to set
	 */
	public void setListaTipoDocContabiliaEntrataClone(List<TipoDocumento> listaTipoDocContabiliaEntrataClone)
	{
		this.listaTipoDocContabiliaEntrataClone = listaTipoDocContabiliaEntrataClone;
	}
	/**
	 * @return the listaTipoDocContabiliaSpesaClone
	 */
	public List<TipoDocumento> getListaTipoDocContabiliaSpesaClone()
	{
		return listaTipoDocContabiliaSpesaClone;
	}
	/**
	 * @param listaTipoDocContabiliaSpesaClone the listaTipoDocContabiliaSpesaClone to set
	 */
	public void setListaTipoDocContabiliaSpesaClone(List<TipoDocumento> listaTipoDocContabiliaSpesaClone)
	{
		this.listaTipoDocContabiliaSpesaClone = listaTipoDocContabiliaSpesaClone;
	}

	/**
	 * @return the richiediConfermaUtente
	 */
	public boolean getRichiediConfermaUtente() {
		return richiediConfermaUtente;
	}

	/**
	 * @param richiediConfermaUtente the richiediConfermaUtente to set
	 */
	public void setRichiediConfermaUtente(boolean richiediConfermaUtente) {
		this.richiediConfermaUtente = richiediConfermaUtente;
	}

	
	/**
	 * @return the messaggioRichiestaConfermaProsecuzione
	 */
	public String getMessaggioRichiestaConfermaProsecuzione()
	{
		return messaggioRichiestaConfermaProsecuzione;
	}


	/**
	 * @param messaggioRichiestaConfermaProsecuzione the messaggioRichiestaConfermaProsecuzione to set
	 */
	public void setMessaggioRichiestaConfermaProsecuzione(String messaggioRichiestaConfermaProsecuzione)
	{
		this.messaggioRichiestaConfermaProsecuzione = messaggioRichiestaConfermaProsecuzione;
	}
	/**
	 * @return the forzaTipoDocumento
	 */
	public Boolean getForzaTipoDocumento()
	{
		return forzaTipoDocumento;
	}
	/**
	 * @param forzaTipoDocumento the forzaTipoDocumento to set
	 */
	public void setForzaTipoDocumento(Boolean forzaTipoDocumento)
	{
		this.forzaTipoDocumento = forzaTipoDocumento;
	}	


	
	
}
