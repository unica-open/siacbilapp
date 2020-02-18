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
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisoriDiCassa;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaProvvisorio;
import it.csi.siac.siacfinser.model.ric.RicercaProvvisorioDiCassaK;

/**
 * Classe di model per la ricerca del provvisorio di cassa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/10/2014
 * @version 1.1.0 - 24/11/2015 - CR-2541 / CR-2543
 */
public class RicercaProvvisorioDiCassaModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6359144171751925763L;
	
	private ProvvisorioDiCassa provvisorioDiCassa;
	
	private Integer numeroDa;
	private Integer numeroA;
	private Date dataEmissioneDa;
	private Date dataEmissioneA;
	
	// AGGIUNTE CR-2541 / CR-2543
	private Integer annoDa;
	private Integer annoA;
	private BigDecimal importoDa;
	private BigDecimal importoA;
	private TipoProvvisorioDiCassa tipoProvvisorioDiCassa;
	private String flagDaRegolarizzare;
	private Boolean flagProvvisoriPagoPA;
	
	private String denominazioneSoggetto;
	private String descCausale;
	
	//SIAC-6981
	private Boolean escludiImportoDaEmettereZero;
	
	//SIAC-6352
	private ContoTesoreria contoTesoreria;
	private List<ContoTesoreria> listaContoTesoreria = new ArrayList<ContoTesoreria>();
	
	/** Cotruttore vuoto di default */
	public RicercaProvvisorioDiCassaModel() {
		super();
	}

	/**
	 * @return the provvisorioDiCassa
	 */
	public ProvvisorioDiCassa getProvvisorioDiCassa() {
		return provvisorioDiCassa;
	}

	/**
	 * @param provvisorioDiCassa the provvisorioDiCassa to set
	 */
	public void setProvvisorioDiCassa(ProvvisorioDiCassa provvisorioDiCassa) {
		this.provvisorioDiCassa = provvisorioDiCassa;
	}

	/**
	 * @return the numeroDa
	 */
	public Integer getNumeroDa() {
		return numeroDa;
	}

	/**
	 * @param numeroDa the numeroDa to set
	 */
	public void setNumeroDa(Integer numeroDa) {
		this.numeroDa = numeroDa;
	}

	/**
	 * @return the numeroA
	 */
	public Integer getNumeroA() {
		return numeroA;
	}

	/**
	 * @param numeroA the numeroA to set
	 */
	public void setNumeroA(Integer numeroA) {
		this.numeroA = numeroA;
	}

	/**
	 * @return the dataEmissioneDa
	 */
	public Date getDataEmissioneDa() {
		return dataEmissioneDa == null ? null : new Date(dataEmissioneDa.getTime());
	}

	/**
	 * @param dataEmissioneDa the dataEmissioneDa to set
	 */
	public void setDataEmissioneDa(Date dataEmissioneDa) {
		this.dataEmissioneDa = dataEmissioneDa == null ? null : new Date(dataEmissioneDa.getTime());
	}

	/**
	 * @return the dataEmissioneA
	 */
	public Date getDataEmissioneA() {
		return dataEmissioneA == null ? null : new Date(dataEmissioneA.getTime());
	}

	/**
	 * @param dataEmissioneA the dataEmissioneA to set
	 */
	public void setDataEmissioneA(Date dataEmissioneA) {
		this.dataEmissioneA = dataEmissioneA == null ? null : new Date(dataEmissioneA.getTime());
	}
	
	/**
	 * @return the annoDa
	 */
	public Integer getAnnoDa() {
		return annoDa;
	}

	/**
	 * @param annoDa the annoDa to set
	 */
	public void setAnnoDa(Integer annoDa) {
		this.annoDa = annoDa;
	}

	/**
	 * @return the annoA
	 */
	public Integer getAnnoA() {
		return annoA;
	}

	/**
	 * @param annoA the annoA to set
	 */
	public void setAnnoA(Integer annoA) {
		this.annoA = annoA;
	}

	/**
	 * @return the importoDa
	 */
	public BigDecimal getImportoDa() {
		return importoDa;
	}

	/**
	 * @param importoDa the importoDa to set
	 */
	public void setImportoDa(BigDecimal importoDa) {
		this.importoDa = importoDa;
	}

	/**
	 * @return the importoA
	 */
	public BigDecimal getImportoA() {
		return importoA;
	}

	/**
	 * @param importoA the importoA to set
	 */
	public void setImportoA(BigDecimal importoA) {
		this.importoA = importoA;
	}
	
	/**
	 * @return the tipoProvvisorioDiCassa
	 */
	public TipoProvvisorioDiCassa getTipoProvvisorioDiCassa() {
		return tipoProvvisorioDiCassa;
	}

	/**
	 * @param tipoProvvisorioDiCassa the tipoProvvisorioDiCassa to set
	 */
	public void setTipoProvvisorioDiCassa(TipoProvvisorioDiCassa tipoProvvisorioDiCassa) {
		this.tipoProvvisorioDiCassa = tipoProvvisorioDiCassa;
	}
	
	/**
	 * @return the flagDaRegolarizzare
	 */
	public String getFlagDaRegolarizzare() {
		return flagDaRegolarizzare;
	}

	/**
	 * @param flagDaRegolarizzare the flagDaRegolarizzare to set
	 */
	public void setFlagDaRegolarizzare(String flagDaRegolarizzare) {
		this.flagDaRegolarizzare = flagDaRegolarizzare;
	}

	/**
	 * @return the flagProvvisoriPagoPA
	 */
	public Boolean isFlagProvvisoriPagoPA() {
		return flagProvvisoriPagoPA;
	}

	/**
	 * @param flagProvvisoriPagoPA the flagProvvisoriPagoPA to set
	 */
	public void setFlagProvvisoriPagoPA(Boolean flagProvvisoriPagoPA) {
		this.flagProvvisoriPagoPA = flagProvvisoriPagoPA;
	}

	
	
	/* **** Requests **** */

	/**
	 * @return the denominazioneSoggetto
	 */
	public String getDenominazioneSoggetto() {
		return denominazioneSoggetto;
	}

	/**
	 * @param denominazioneSoggetto the denominazioneSoggetto to set
	 */
	public void setDenominazioneSoggetto(String denominazioneSoggetto) {
		this.denominazioneSoggetto = denominazioneSoggetto;
	}

	/**
	 * @return the descCausale
	 */
	public String getDescCausale() {
		return descCausale;
	}

	/**
	 * @param descCausale the descCausale to set
	 */
	public void setDescCausale(String descCausale) {
		this.descCausale = descCausale;
	}
	
	/**
	 * @return the escludiImportoDaemettereZero
	 */
	public Boolean getEscludiImportoDaEmettereZero() {
		return escludiImportoDaEmettereZero;
	}

	/**
	 * @param escludiImportoDaemettereZero the escludiImportoDaemettereZero to set
	 */
	public void setEscludiImportoDaEmettereZero(Boolean escludiImportoDaemettereZero) {
		this.escludiImportoDaEmettereZero = escludiImportoDaemettereZero;
	}

	/**
	 * @return the flagProvvisoriPagoPA
	 */
	public Boolean getFlagProvvisoriPagoPA() {
		return flagProvvisoriPagoPA;
	}

	
	/**
	 * @return the contoTesoreria
	 */
	public ContoTesoreria getContoTesoreria() {
		return contoTesoreria;
	}

	/**
	 * @param contoTesoreria the contoTesoreria to set
	 */
	public void setContoTesoreria(ContoTesoreria contoTesoreria) {
		this.contoTesoreria = contoTesoreria;
	}

	/**
	 * @return the listaContoTesoreria
	 */
	public List<ContoTesoreria> getListaContoTesoreria() {
		return listaContoTesoreria != null ? listaContoTesoreria: new ArrayList<ContoTesoreria>();
	}

	/**
	 * @param listaContoTesoreria the listaContoTesoreria to set
	 */
	public void setListaContoTesoreria(List<ContoTesoreria> listaContoTesoreria) {
		this.listaContoTesoreria = listaContoTesoreria;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaProvvisoriDiCassa}.
	 * 
	 * @return la request creata
	 */
	public RicercaProvvisoriDiCassa creaRequestRicercaProvvisoriDiCassa() {
		RicercaProvvisoriDiCassa request = creaRequest(RicercaProvvisoriDiCassa.class);
		
		request.setBilancio(getBilancio());
		request.setEnte(getEnte());
		// Le pagine sono 1-based
		request.setNumPagina(1);
		request.setNumRisultatiPerPagina(ELEMENTI_PER_PAGINA_RICERCA);
		
		ParametroRicercaProvvisorio parametroRicercaProvvisorio = new ParametroRicercaProvvisorio();
		
		parametroRicercaProvvisorio.setNumeroDa(getNumeroDa());
		parametroRicercaProvvisorio.setNumeroA(getNumeroA());
		parametroRicercaProvvisorio.setDataInizioEmissione(getDataEmissioneDa());
		parametroRicercaProvvisorio.setDataFineEmissione(getDataEmissioneA());
		
		// CR-2541 / CR-2543
		parametroRicercaProvvisorio.setAnnoDa(getAnnoDa());
		parametroRicercaProvvisorio.setAnnoA(getAnnoA());
		parametroRicercaProvvisorio.setTipoProvvisorio(getTipoProvvisorioDiCassa());
		parametroRicercaProvvisorio.setImportoDa(getImportoDa());
		parametroRicercaProvvisorio.setImportoA(getImportoA());
		parametroRicercaProvvisorio.setFlagDaRegolarizzare(getFlagDaRegolarizzare());
		
		parametroRicercaProvvisorio.setDescCausale(getDescCausale());
		parametroRicercaProvvisorio.setDenominazioneSoggetto(getDenominazioneSoggetto());
		parametroRicercaProvvisorio.setContoTesoriere(getContoTesoreria() != null ? getContoTesoreria().getCodice() : null);
		
		// L'anno deve essere quello corrente se non specificato
		if(getAnnoDa() == null && getAnnoA() == null) {
			parametroRicercaProvvisorio.setAnno(getAnnoEsercizioInt());
		}		
		parametroRicercaProvvisorio.setFlagProvvisoriPagoPA(isFlagProvvisoriPagoPA());
		
		parametroRicercaProvvisorio.setEscludiProvvisoriConImportoDaEmettereZero(getEscludiImportoDaEmettereZero());
		
		request.setParametroRicercaProvvisorio(parametroRicercaProvvisorio);
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaProvvisorioDiCassaPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaProvvisorioDiCassaPerChiave creaRequestRicercaProvvisorioDiCassaPerChiave() {
		RicercaProvvisorioDiCassaPerChiave request = creaRequest(RicercaProvvisorioDiCassaPerChiave.class);
		
		request.setBilancio(getBilancio());
		request.setEnte(getEnte());
		
		RicercaProvvisorioDiCassaK pRicercaProvvisorioK = new RicercaProvvisorioDiCassaK();
		pRicercaProvvisorioK.setAnnoProvvisorioDiCassa(getProvvisorioDiCassa().getAnno());
		pRicercaProvvisorioK.setNumeroProvvisorioDiCassa(getProvvisorioDiCassa().getNumero());
		request.setpRicercaProvvisorioK(pRicercaProvvisorioK);
		
		return request;
	}

		/**
	 * Creazione della request per la lettura dei conti di tesoreria
	 * @return la request creata
	 */
	public LeggiContiTesoreria creaRequestLeggiContiTesoreria() {
		LeggiContiTesoreria request = new LeggiContiTesoreria();
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		return request;
	}


	
}
