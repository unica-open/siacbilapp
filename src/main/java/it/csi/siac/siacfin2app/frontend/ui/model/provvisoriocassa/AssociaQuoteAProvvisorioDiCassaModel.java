/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.provvisoriocassa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.RicercaProvvisorioDiCassaK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;

/**
 * Classe di model per la ricerca sintetica del provvisorio di cassa.
 * 
 * @author Valentina
 * @version 1.0.0 - 13/05/2016
 */
public class AssociaQuoteAProvvisorioDiCassaModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -434598592093072894L;
	
	/** Elementi per pagina nella ricerca paginata */
	public static final int ELEMENTI_PER_PAGINA_QUOTA = 50;
	
	private ProvvisorioDiCassa provvisorio;
	private Integer uidProvvisorioDaAssociare;
	
	//Filtri ricerca quote
	private TipoDocumento tipoDocumento;
	// Documento 
	private Integer annoDocumento;
	private String numeroDocumento;
	private Integer numeroSubdocumento;
	private Date dataEmissioneDocumento;
	// Movimente gestione
	private Integer annoMovimento;
	private BigDecimal numeroMovimento;
	//Elenco
	private Integer annoElenco;
	private Integer numeroElenco;
	//Soggetto
	private Soggetto soggetto;
	
	private List<TipoDocumento> listaTipoDocumento = new ArrayList<TipoDocumento>();
	private List<Integer> listaUidSubdocumenti = new ArrayList<Integer>();
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	
	private boolean pulsanteAggiornaNascosto;
	
	
	/** Cotruttore vuoto di default */
	public AssociaQuoteAProvvisorioDiCassaModel() {
		super();
		setTitolo("Associa quote");
	}
	
	/**
	 * @return the provvisorio
	 */
	public ProvvisorioDiCassa getProvvisorio() {
		return provvisorio;
	}

	/**
	 * @param provvisorio the provvisorio to set
	 */
	public void setProvvisorio(ProvvisorioDiCassa provvisorio) {
		this.provvisorio = provvisorio;
	}

	/**
	 * @return the uidProvvisorioDaAssociare
	 */
	public Integer getUidProvvisorioDaAssociare() {
		return uidProvvisorioDaAssociare;
	}

	/**
	 * @param uidProvvisorioDaAssociare the uidProvvisorioDaAssociare to set
	 */
	public void setUidProvvisorioDaAssociare(Integer uidProvvisorioDaAssociare) {
		this.uidProvvisorioDaAssociare = uidProvvisorioDaAssociare;
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
	 * @return the tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return the annoDocumento
	 */
	public Integer getAnnoDocumento() {
		return annoDocumento;
	}

	/**
	 * @param annoDocumento the annoDocumento to set
	 */
	public void setAnnoDocumento(Integer annoDocumento) {
		this.annoDocumento = annoDocumento;
	}

	/**
	 * @return the numeroDocumento
	 */
	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	/**
	 * @param numeroDocumento the numeroDocumento to set
	 */
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	/**
	 * @return the numeroSubdocumento
	 */
	public Integer getNumeroSubdocumento() {
		return numeroSubdocumento;
	}

	/**
	 * @param numeroSubdocumento the numeroSubdocumento to set
	 */
	public void setNumeroSubdocumento(Integer numeroSubdocumento) {
		this.numeroSubdocumento = numeroSubdocumento;
	}

	/**
	 * @return the dataEmissioneDocumento
	 */
	public Date getDataEmissioneDocumento() {
		return dataEmissioneDocumento != null ? new Date(dataEmissioneDocumento.getTime()) : null;
	}

	/**
	 * @param dataEmissioneDocumento the dataEmissioneDocumento to set
	 */
	public void setDataEmissioneDocumento(Date dataEmissioneDocumento) {
		this.dataEmissioneDocumento = dataEmissioneDocumento != null ? new Date(dataEmissioneDocumento.getTime()) : null;
	}

	/**
	 * @return the annoMovimento
	 */
	public Integer getAnnoMovimento() {
		return annoMovimento;
	}

	/**
	 * @param annoMovimento the annoMovimento to set
	 */
	public void setAnnoMovimento(Integer annoMovimento) {
		this.annoMovimento = annoMovimento;
	}

	/**
	 * @return the numeroMovimento
	 */
	public BigDecimal getNumeroMovimento() {
		return numeroMovimento;
	}

	/**
	 * @param numeroMovimento the numeroMovimento to set
	 */
	public void setNumeroMovimento(BigDecimal numeroMovimento) {
		this.numeroMovimento = numeroMovimento;
	}
	
	/**
	 * @return the annoElenco
	 */
	public Integer getAnnoElenco() {
		return annoElenco;
	}

	/**
	 * @param annoElenco the annoElenco to set
	 */
	public void setAnnoElenco(Integer annoElenco) {
		this.annoElenco = annoElenco;
	}

	/**
	 * @return the numeroElenco
	 */
	public Integer getNumeroElenco() {
		return numeroElenco;
	}

	/**
	 * @param numeroElenco the numeroElenco to set
	 */
	public void setNumeroElenco(Integer numeroElenco) {
		this.numeroElenco = numeroElenco;
	}

	/**
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return soggetto;
	}

	/**
	 * @param soggetto the soggetto to set
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
	}

	/**
	 * @return the listaUidSubdocumenti
	 */
	public List<Integer> getListaUidSubdocumenti() {
		return listaUidSubdocumenti;
	}

	/**
	 * @param listaUidSubdocumenti the listaUidSubdocumenti to set
	 */
	public void setListaUidSubdocumenti(List<Integer> listaUidSubdocumenti) {
		this.listaUidSubdocumenti = listaUidSubdocumenti;
	}
	
	/**
	 * @return the listaClasseSoggetto
	 */
	public List<CodificaFin> getListaClasseSoggetto() {
		return listaClasseSoggetto;
	}

	/**
	 * @param listaClasseSoggetto the listaClasseSoggetto to set
	 */
	public void setListaClasseSoggetto(List<CodificaFin> listaClasseSoggetto) {
		this.listaClasseSoggetto = listaClasseSoggetto != null ? listaClasseSoggetto : new ArrayList<CodificaFin>();
	}

	/**
	 * @return the pulsanteAggiornaNascosto
	 */
	public boolean isPulsanteAggiornaNascosto() {
		return pulsanteAggiornaNascosto;
	}

	/**
	 * @param pulsanteAggiornaNascosto the pulsanteAggiornaNascosto to set
	 */
	public void setPulsanteAggiornaNascosto(boolean pulsanteAggiornaNascosto) {
		this.pulsanteAggiornaNascosto = pulsanteAggiornaNascosto;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaProvvisorioDiCassaPerChiave}.
	 * @return la request creata
	 */
	public RicercaProvvisorioDiCassaPerChiave creaRequestRicercaProvvisorioDiCassaPerChiave() {
		RicercaProvvisorioDiCassaPerChiave req = creaRequest(RicercaProvvisorioDiCassaPerChiave.class);
		
		req.setBilancio(getBilancio());
		req.setEnte(getEnte());
		
		req.setpRicercaProvvisorioK(new RicercaProvvisorioDiCassaK());
		req.getpRicercaProvvisorioK().setAnnoProvvisorioDiCassa(getProvvisorio().getAnno());
		req.getpRicercaProvvisorioK().setNumeroProvvisorioDiCassa(getProvvisorio().getNumero());
		req.getpRicercaProvvisorioK().setTipoProvvisorioDiCassa(getProvvisorio().getTipoProvvisorioDiCassa());
		
		return req;
	}

}
