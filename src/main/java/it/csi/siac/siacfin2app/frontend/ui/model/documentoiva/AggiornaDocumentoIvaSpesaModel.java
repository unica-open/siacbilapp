/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.documentoiva;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaIvaDifferitaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ContaDatiCollegatiSubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoIvaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaStampaIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaSpesa;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfin2ser.model.TipoChiusura;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoEsigibilitaIva;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.TipoRelazione;
import it.csi.siac.siacfin2ser.model.TipoStampaIva;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Classe di model per l'aggiornamento del Documento Iva Spesa.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 12/06/2014
 *
 */
public class AggiornaDocumentoIvaSpesaModel extends GenericDocumentoIvaSpesaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3423541831021025104L;
	
	private Boolean flagIvaImmediata = Boolean.FALSE;
	private Boolean flagAperturaSecondaPagina = Boolean.FALSE;
	private Boolean flagDocumentoBaseNota = Boolean.FALSE;
	private Boolean flagQuotaIvaDifferita = Boolean.FALSE;
	
	private Boolean flagNotaCreditoIvaDisponibile = Boolean.FALSE;
	private Boolean flagNotaCreditoIvaPresente = Boolean.FALSE;
	private Boolean aperturaTabNotaCredito = Boolean.FALSE;
	
	private List<TipoRegistrazioneIva> listaTipoRegistrazioneIvaAggiornamento = new ArrayList<TipoRegistrazioneIva>();
	private List<TipoRegistroIva> listaTipoRegistroIvaAggiornamento = new ArrayList<TipoRegistroIva>();
	private List<AttivitaIva> listaAttivitaIvaAggiornamento = new ArrayList<AttivitaIva>();
	private List<RegistroIva> listaRegistroIvaAggiornamento = new ArrayList<RegistroIva>();
	
	// Per le note di credito
	private SubdocumentoIvaSpesa nota;
	private Boolean flagRilevanteIRAPNota = Boolean.FALSE;
	private AttivitaIva attivitaIvaNota;
	private TipoRegistroIva tipoRegistroIvaNota;
	private AliquotaSubdocumentoIva aliquotaSubdocumentoIvaNota;
	private BigDecimal percentualeAliquotaIvaNota = BigDecimal.ZERO;
	private BigDecimal percentualeIndetraibilitaAliquotaIvaNota = BigDecimal.ZERO;
	
//	private List<AliquotaSubdocumentoIva> listaAliquotaSubdocumentoIvaNota = new SortedSetList<AliquotaSubdocumentoIva>(ComparatorAliquotaSubdocumentoIva.INSTANCE);
	private BigDecimal importoTotaleNote = BigDecimal.ZERO;
	private BigDecimal importoDaDedurre = BigDecimal.ZERO;
	private List<RegistroIva> listaRegistroIvaNota = new ArrayList<RegistroIva>();
	
	// Intracomunitario
	private Boolean flagDocumentoIntracomunitarioNota = Boolean.FALSE;
	private Valuta valutaNota;
	private BigDecimal importoInValutaNota = BigDecimal.ZERO;
	private TipoRegistroIva tipoRegistroIvaIntracomunitarioNota;
	private TipoDocumento tipoDocumentoIntracomunitarioNota;
	private AttivitaIva attivitaIvaIntracomunitarioNota;
	private RegistroIva registroIvaIntracomunitarioNota;
	
	//CR-3791
	private boolean modificheARegistroAbilitate;
	private RegistroIva registroIvaAggiornamento;
	
	/** Costruttore vuoto di default */
	public AggiornaDocumentoIvaSpesaModel() {
		super();
		setTitolo("Aggiorna documenti iva spesa");
		setFlagIntracomunitarioUtilizzabile(Boolean.TRUE);
	}
	
	/**
	 * @return the flagIvaImmediata
	 */
	public Boolean getFlagIvaImmediata() {
		return flagIvaImmediata;
	}

	/**
	 * @param flagIvaImmediata the flagIvaImmediata to set
	 */
	public void setFlagIvaImmediata(Boolean flagIvaImmediata) {
		this.flagIvaImmediata = flagIvaImmediata;
	}

	/**
	 * @return the flagAperturaSecondaPagina
	 */
	public Boolean getFlagAperturaSecondaPagina() {
		return flagAperturaSecondaPagina;
	}

	/**
	 * @param flagAperturaSecondaPagina the flagAperturaSecondaPagina to set
	 */
	public void setFlagAperturaSecondaPagina(Boolean flagAperturaSecondaPagina) {
		this.flagAperturaSecondaPagina = flagAperturaSecondaPagina != null ? flagAperturaSecondaPagina : Boolean.FALSE;
	}
	
	/**
	 * @return the flagDocumentoBaseNota
	 */
	public Boolean getFlagDocumentoBaseNota() {
		return flagDocumentoBaseNota;
	}

	/**
	 * @param flagDocumentoBaseNota the flagDocumentoBaseNota to set
	 */
	public void setFlagDocumentoBaseNota(Boolean flagDocumentoBaseNota) {
		this.flagDocumentoBaseNota = flagDocumentoBaseNota != null ? flagDocumentoBaseNota : Boolean.FALSE;
	}

	/**
	 * @return the flagQuotaIvaDifferita
	 */
	public Boolean getFlagQuotaIvaDifferita() {
		return flagQuotaIvaDifferita;
	}

	/**
	 * @param flagQuotaIvaDifferita the flagQuotaIvaDifferita to set
	 */
	public void setFlagQuotaIvaDifferita(Boolean flagQuotaIvaDifferita) {
		this.flagQuotaIvaDifferita = flagQuotaIvaDifferita != null ? flagQuotaIvaDifferita : Boolean.FALSE;
	}

	/**
	 * @return the flagNotaCreditoIvaDisponibile
	 */
	public Boolean getFlagNotaCreditoIvaDisponibile() {
		return flagNotaCreditoIvaDisponibile;
	}

	/**
	 * @param flagNotaCreditoIvaDisponibile the flagNotaCreditoIvaDisponibile to set
	 */
	public void setFlagNotaCreditoIvaDisponibile(Boolean flagNotaCreditoIvaDisponibile) {
		this.flagNotaCreditoIvaDisponibile = flagNotaCreditoIvaDisponibile != null ? flagNotaCreditoIvaDisponibile : Boolean.FALSE;
	}

	/**
	 * @return the flagNotaCreditoIvaPresente
	 */
	public Boolean getFlagNotaCreditoIvaPresente() {
		return flagNotaCreditoIvaPresente;
	}

	/**
	 * @param flagNotaCreditoIvaPresente the flagNotaCreditoIvaPresente to set
	 */
	public void setFlagNotaCreditoIvaPresente(Boolean flagNotaCreditoIvaPresente) {
		this.flagNotaCreditoIvaPresente = flagNotaCreditoIvaPresente != null ? flagNotaCreditoIvaPresente : Boolean.FALSE;
	}
	
	/**
	 * @return the aperturaTabNotaCredito
	 */
	public Boolean getAperturaTabNotaCredito() {
		return aperturaTabNotaCredito;
	}

	/**
	 * @param aperturaTabNotaCredito the aperturaTabNotaCredito to set
	 */
	public void setAperturaTabNotaCredito(Boolean aperturaTabNotaCredito) {
		this.aperturaTabNotaCredito = aperturaTabNotaCredito != null ? aperturaTabNotaCredito : Boolean.FALSE;
	}

	/**
	 * @return the listaTipoRegistrazioneIvaAggiornamento
	 */
	public List<TipoRegistrazioneIva> getListaTipoRegistrazioneIvaAggiornamento() {
		return listaTipoRegistrazioneIvaAggiornamento;
	}

	/**
	 * @param listaTipoRegistrazioneIvaAggiornamento the listaTipoRegistrazioneIvaAggiornamento to set
	 */
	public void setListaTipoRegistrazioneIvaAggiornamento(List<TipoRegistrazioneIva> listaTipoRegistrazioneIvaAggiornamento) {
		this.listaTipoRegistrazioneIvaAggiornamento = listaTipoRegistrazioneIvaAggiornamento != null ? listaTipoRegistrazioneIvaAggiornamento : new ArrayList<TipoRegistrazioneIva>();
	}

	/**
	 * @return the listaTipoRegistroIvaAggiornamento
	 */
	public List<TipoRegistroIva> getListaTipoRegistroIvaAggiornamento() {
		return listaTipoRegistroIvaAggiornamento;
	}

	/**
	 * @param listaTipoRegistroIvaAggiornamento the listaTipoRegistroIvaAggiornamento to set
	 */
	public void setListaTipoRegistroIvaAggiornamento(List<TipoRegistroIva> listaTipoRegistroIvaAggiornamento) {
		this.listaTipoRegistroIvaAggiornamento = listaTipoRegistroIvaAggiornamento != null ? listaTipoRegistroIvaAggiornamento : new ArrayList<TipoRegistroIva>();
	}

	/**
	 * @return the listaAttivitaIvaAggiornamento
	 */
	public List<AttivitaIva> getListaAttivitaIvaAggiornamento() {
		return listaAttivitaIvaAggiornamento;
	}

	/**
	 * @param listaAttivitaIvaAggiornamento the listaAttivitaIvaAggiornamento to set
	 */
	public void setListaAttivitaIvaAggiornamento(List<AttivitaIva> listaAttivitaIvaAggiornamento) {
		this.listaAttivitaIvaAggiornamento = listaAttivitaIvaAggiornamento != null ? listaAttivitaIvaAggiornamento : new ArrayList<AttivitaIva>();
	}

	/**
	 * @return the listaRegistroIvaAggiornamento
	 */
	public List<RegistroIva> getListaRegistroIvaAggiornamento() {
		return listaRegistroIvaAggiornamento;
	}

	/**
	 * @param listaRegistroIvaAggiornamento the listaRegistroIvaAggiornamento to set
	 */
	public void setListaRegistroIvaAggiornamento(List<RegistroIva> listaRegistroIvaAggiornamento) {
		this.listaRegistroIvaAggiornamento = listaRegistroIvaAggiornamento != null ? listaRegistroIvaAggiornamento : new ArrayList<RegistroIva>();
	}

	/**
	 * @return the nota
	 */
	public SubdocumentoIvaSpesa getNota() {
		return nota;
	}

	/**
	 * @param nota the nota to set
	 */
	public void setNota(SubdocumentoIvaSpesa nota) {
		this.nota = nota;
	}

	/**
	 * @return the flagRilevanteIRAPNota
	 */
	public Boolean getFlagRilevanteIRAPNota() {
		return flagRilevanteIRAPNota;
	}

	/**
	 * @param flagRilevanteIRAPNota the flagRilevanteIRAPNota to set
	 */
	public void setFlagRilevanteIRAPNota(Boolean flagRilevanteIRAPNota) {
		this.flagRilevanteIRAPNota = flagRilevanteIRAPNota != null ? flagRilevanteIRAPNota : Boolean.FALSE;
	}

	/**
	 * @return the attivitaIvaNota
	 */
	public AttivitaIva getAttivitaIvaNota() {
		return attivitaIvaNota;
	}

	/**
	 * @param attivitaIvaNota the attivitaIvaNota to set
	 */
	public void setAttivitaIvaNota(AttivitaIva attivitaIvaNota) {
		this.attivitaIvaNota = attivitaIvaNota;
	}

	/**
	 * @return the tipoRegistroIvaNota
	 */
	public TipoRegistroIva getTipoRegistroIvaNota() {
		return tipoRegistroIvaNota;
	}

	/**
	 * @param tipoRegistroIvaNota the tipoRegistroIvaNota to set
	 */
	public void setTipoRegistroIvaNota(TipoRegistroIva tipoRegistroIvaNota) {
		this.tipoRegistroIvaNota = tipoRegistroIvaNota;
	}
	
	/**
	 * @return the aliquotaSubdocumentoIvaNota
	 */
	public AliquotaSubdocumentoIva getAliquotaSubdocumentoIvaNota() {
		return aliquotaSubdocumentoIvaNota;
	}

	/**
	 * @param aliquotaSubdocumentoIvaNota the aliquotaSubdocumentoIvaNota to set
	 */
	public void setAliquotaSubdocumentoIvaNota(AliquotaSubdocumentoIva aliquotaSubdocumentoIvaNota) {
		this.aliquotaSubdocumentoIvaNota = aliquotaSubdocumentoIvaNota;
	}

	/**
	 * @return the percentualeAliquotaIvaNota
	 */
	public BigDecimal getPercentualeAliquotaIvaNota() {
		return percentualeAliquotaIvaNota;
	}

	/**
	 * @param percentualeAliquotaIvaNota the percentualeAliquotaIvaNota to set
	 */
	public void setPercentualeAliquotaIvaNota(BigDecimal percentualeAliquotaIvaNota) {
		this.percentualeAliquotaIvaNota = percentualeAliquotaIvaNota;
	}

	/**
	 * @return the percentualeIndetraibilitaAliquotaIvaNota
	 */
	public BigDecimal getPercentualeIndetraibilitaAliquotaIvaNota() {
		return percentualeIndetraibilitaAliquotaIvaNota;
	}

	/**
	 * @param percentualeIndetraibilitaAliquotaIvaNota the percentualeIndetraibilitaAliquotaIvaNota to set
	 */
	public void setPercentualeIndetraibilitaAliquotaIvaNota(BigDecimal percentualeIndetraibilitaAliquotaIvaNota) {
		this.percentualeIndetraibilitaAliquotaIvaNota = percentualeIndetraibilitaAliquotaIvaNota != null ? percentualeIndetraibilitaAliquotaIvaNota : BigDecimal.ZERO;
	}

	/**
	 * @return the listaRegistroIvaNota
	 */
	public List<RegistroIva> getListaRegistroIvaNota() {
		return listaRegistroIvaNota;
	}

	/**
	 * @param listaRegistroIvaNota the listaRegistroIvaNota to set
	 */
	public void setListaRegistroIvaNota(List<RegistroIva> listaRegistroIvaNota) {
		this.listaRegistroIvaNota = listaRegistroIvaNota != null ? listaRegistroIvaNota : new ArrayList<RegistroIva>();
	}

	

	/**
	 * @return the listaNote
	 */
	public List<DocumentoSpesa> getListaNote() {
		if(getDocumento().getListaNoteCreditoSpesaFiglio() == null) {
			return new ArrayList<DocumentoSpesa>();
		}
		return getDocumento().getListaNoteCreditoSpesaFiglio();
	}

	/**
	 * @return the importoTotaleNote
	 */
	public BigDecimal getImportoTotaleNote() {
		return importoTotaleNote;
	}

	/**
	 * @param importoTotaleNote the importoTotaleNote to set
	 */
	public void setImportoTotaleNote(BigDecimal importoTotaleNote) {
		this.importoTotaleNote = importoTotaleNote != null ? importoTotaleNote : BigDecimal.ZERO;
	}

	/**
	 * @return the importoDaDedurre
	 */
	public BigDecimal getImportoDaDedurre() {
		return importoDaDedurre;
	}

	/**
	 * @param importoDaDedurre the importoDaDedurre to set
	 */
	public void setImportoDaDedurre(BigDecimal importoDaDedurre) {
		this.importoDaDedurre = importoDaDedurre != null ? importoDaDedurre : BigDecimal.ZERO;
	}
	
	/**
	 * @return the flagDocumentoIntracomunitarioNota
	 */
	public Boolean getFlagDocumentoIntracomunitarioNota() {
		return flagDocumentoIntracomunitarioNota;
	}

	/**
	 * @param flagDocumentoIntracomunitarioNota the flagDocumentoIntracomunitarioNota to set
	 */
	public void setFlagDocumentoIntracomunitarioNota(Boolean flagDocumentoIntracomunitarioNota) {
		this.flagDocumentoIntracomunitarioNota = flagDocumentoIntracomunitarioNota != null ? flagDocumentoIntracomunitarioNota : Boolean.FALSE;
	}

	/**
	 * @return the valutaNota
	 */
	public Valuta getValutaNota() {
		return valutaNota;
	}

	/**
	 * @param valutaNota the valutaNota to set
	 */
	public void setValutaNota(Valuta valutaNota) {
		this.valutaNota = valutaNota;
	}

	/**
	 * @return the importoInValutaNota
	 */
	public BigDecimal getImportoInValutaNota() {
		return importoInValutaNota;
	}

	/**
	 * @param importoInValutaNota the importoInValutaNota to set
	 */
	public void setImportoInValutaNota(BigDecimal importoInValutaNota) {
		this.importoInValutaNota = importoInValutaNota != null ? importoInValutaNota : BigDecimal.ZERO;
	}

	/**
	 * @return the tipoRegistroIvaIntracomunitarioNota
	 */
	public TipoRegistroIva getTipoRegistroIvaIntracomunitarioNota() {
		return tipoRegistroIvaIntracomunitarioNota;
	}

	/**
	 * @param tipoRegistroIvaIntracomunitarioNota the tipoRegistroIvaIntracomunitarioNota to set
	 */
	public void setTipoRegistroIvaIntracomunitarioNota(TipoRegistroIva tipoRegistroIvaIntracomunitarioNota) {
		this.tipoRegistroIvaIntracomunitarioNota = tipoRegistroIvaIntracomunitarioNota;
	}

	/**
	 * @return the tipoDocumentoIntracomunitarioNota
	 */
	public TipoDocumento getTipoDocumentoIntracomunitarioNota() {
		return tipoDocumentoIntracomunitarioNota;
	}

	/**
	 * @param tipoDocumentoIntracomunitarioNota the tipoDocumentoIntracomunitarioNota to set
	 */
	public void setTipoDocumentoIntracomunitarioNota(TipoDocumento tipoDocumentoIntracomunitarioNota) {
		this.tipoDocumentoIntracomunitarioNota = tipoDocumentoIntracomunitarioNota;
	}

	/**
	 * @return the attivitaIvaIntracomunitarioNota
	 */
	public AttivitaIva getAttivitaIvaIntracomunitarioNota() {
		return attivitaIvaIntracomunitarioNota;
	}

	/**
	 * @param attivitaIvaIntracomunitarioNota the attivitaIvaIntracomunitarioNota to set
	 */
	public void setAttivitaIvaIntracomunitarioNota(AttivitaIva attivitaIvaIntracomunitarioNota) {
		this.attivitaIvaIntracomunitarioNota = attivitaIvaIntracomunitarioNota;
	}

	/**
	 * @return the registroIvaIntracomunitarioNota
	 */
	public RegistroIva getRegistroIvaIntracomunitarioNota() {
		return registroIvaIntracomunitarioNota;
	}

	/**
	 * @param registroIvaIntracomunitarioNota the registroIvaIntracomunitarioNota to set
	 */
	public void setRegistroIvaIntracomunitarioNota(RegistroIva registroIvaIntracomunitarioNota) {
		this.registroIvaIntracomunitarioNota = registroIvaIntracomunitarioNota;
	}
	
	/**
	 * @return the modificheARegistroAbilitate
	 */
	public boolean isModificheARegistroAbilitate() {
		return modificheARegistroAbilitate;
	}

	/**
	 * @param modificheARegistroAbilitate the modificheARegistroAbilitateToSet
	 */
	public void setModificheARegistroAbilitate(boolean modificheARegistroAbilitate) {
		this.modificheARegistroAbilitate = modificheARegistroAbilitate;
	}
	
	/**
	 * @return the registroIvaAggiornamento
	 */
	public RegistroIva getRegistroIvaAggiornamento() {
		return registroIvaAggiornamento;
	}

	/**
	 * @param registroIvaAggiornamento the registroIvaAggiornamento to set 
	 */
	public void setRegistroIvaAggiornamento(RegistroIva registroIvaAggiornamento) {
		this.registroIvaAggiornamento = registroIvaAggiornamento;
	}
	
	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link AggiornaSubdocumentoIvaSpesa}.
	 * 
	 * @return la request creata
	 */
	public AggiornaSubdocumentoIvaSpesa creaRequestAggiornaSubdocumentoIvaSpesa() {
		AggiornaSubdocumentoIvaSpesa request = creaRequest(AggiornaSubdocumentoIvaSpesa.class);
		
		request.setBilancio(getBilancio());
		SubdocumentoIvaSpesa sis = popolaSubdocumentoIvaSpesa();
		request.setSubdocumentoIvaSpesa(sis);
		
		if(Boolean.TRUE.equals(getSubdocumentoIva().getFlagIntracomunitario())) {
			SubdocumentoIvaEntrata sie = getSubdocumentoIva().getSubdocumentoIvaEntrata() == null ?
				new SubdocumentoIvaEntrata() : getSubdocumentoIva().getSubdocumentoIvaEntrata();
			impostaSubdocumentoIvaPerIntracomunitario(sis, sie);
		}
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisceNotaCreditoIvaSpesa} per le note.
	 * 
	 * @return la request creata
	 */
	public InserisceNotaCreditoIvaSpesa creaRequestInserisceNotaCreditoIvaSpesa() {
		InserisceNotaCreditoIvaSpesa request = creaRequest(InserisceNotaCreditoIvaSpesa.class);
		
		request.setBilancio(getBilancio());
		SubdocumentoIvaSpesa sis = creaSubdocumentoIvaSpesaNota();
		request.setSubdocumentoIvaSpesa(sis);
		
		if(Boolean.TRUE.equals(getNota().getFlagIntracomunitario())) {
			impostaSubdocumentoIvaPerIntracomunitario(sis, new SubdocumentoIvaEntrata());
		}
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaNotaCreditoIvaSpesa} per le note.
	 * 
	 * @return la request creata
	 */
	public AggiornaNotaCreditoIvaSpesa creaRequestAggiornaNotaCreditoIvaSpesa() {
		AggiornaNotaCreditoIvaSpesa request = creaRequest(AggiornaNotaCreditoIvaSpesa.class);
		
		SubdocumentoIvaSpesa sis = getNota();
		sis.setAttivitaIva(impostaEntitaFacoltativa(getAttivitaIva()));
		sis.setListaAliquotaSubdocumentoIva(getListaAliquotaSubdocumentoIva());
		TipoRegistroIva tri = getTipoRegistroIva();
		sis.getRegistroIva().setTipoRegistroIva(tri);
		sis.setSubdocumentoIvaPadre(creaSubdocumentoIvaSpesa(getSubdocumentoIva().getUid()));
		
		request.setSubdocumentoIvaSpesa(sis);
		request.setBilancio(getBilancio());
		
		if(Boolean.TRUE.equals(getNota().getFlagIntracomunitario())) {
			SubdocumentoIvaEntrata sie = getNota().getSubdocumentoIvaEntrata() == null ?
				new SubdocumentoIvaEntrata() : getNota().getSubdocumentoIvaEntrata();
			impostaSubdocumentoIvaPerIntracomunitario(sis, sie);
		}
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaQuotaIvaDifferitaSpesa}.
	 * 
	 * @return la request creata
	 */
	public AggiornaQuotaIvaDifferitaSpesa creaRequestAggiornaQuotaIvaDifferitaSpesa() {
		AggiornaQuotaIvaDifferitaSpesa request = creaRequest(AggiornaQuotaIvaDifferitaSpesa.class);
		
		request.setBilancio(getBilancio());
		request.setSubdocumentoIvaSpesa(popolaSubdocumentoIvaSpesa());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaStampaIva}.
	 * @param dataProtocollo la data di protocollo
	 * 
	 * @return la request creata
	 */
	public RicercaStampaIva creaRequestRicercaStampaIva(Date dataProtocollo) {
		RicercaStampaIva request = creaRequest(RicercaStampaIva.class);
		
		RegistroIva ri = getSubdocumentoIva().getRegistroIva();
		GruppoAttivitaIva gai = ri.getGruppoAttivitaIva();
		TipoChiusura tc = gai.getTipoChiusura();

		Calendar cal = Calendar.getInstance();
		cal.setTime(dataProtocollo);
		int anno = cal.get(Calendar.YEAR);
		
		StampaIva stampaIva = new StampaIva();
		stampaIva.setAnnoEsercizio(Integer.valueOf(anno));
		stampaIva.setEnte(getEnte());
		stampaIva.setListaRegistroIva(Arrays.asList(ri));
		
		stampaIva.setPeriodo(Periodo.byDateAndTipoChiusura(dataProtocollo, tc));
		stampaIva.setTipoStampaIva(TipoStampaIva.REGISTRO);
		
		request.setStampaIva(stampaIva);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link ContaDatiCollegatiSubdocumentoIvaSpesa}.
	 * @return la request creata
	 */
	public ContaDatiCollegatiSubdocumentoIvaSpesa creaRequestContaDatiCollegatiSubdocumentoIvaSpesa() {
		ContaDatiCollegatiSubdocumentoIvaSpesa request = creaRequest(ContaDatiCollegatiSubdocumentoIvaSpesa.class);
		
		SubdocumentoIvaSpesa sis = new SubdocumentoIvaSpesa();
		sis.setUid(getSubdocumentoIva().getUid());
		request.setSubdocumentoIvaSpesa(sis);
		
		return request;
	}
	
	/**
	 * Crea il subdocumento Iva spesa.
	 * 
	 * @return il subdocumento creato
	 */
	private SubdocumentoIvaSpesa creaSubdocumentoIvaSpesaNota() {
		SubdocumentoIvaSpesa sis = getNota();
		
		sis.setEnte(getEnte());
		sis.setAnnoEsercizio(getAnnoEsercizioInt());
		sis.setAttivitaIva(impostaEntitaFacoltativa(getAttivitaIvaNota()));
		sis.setListaAliquotaSubdocumentoIva(getListaAliquotaSubdocumentoIvaNota());
		
		// Se non ho lo stato, allora lo creo a PROVVISORIO
		if(sis.getStatoSubdocumentoIva() == null) {
			sis.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO);
		}
		
		TipoRegistroIva tri = getTipoRegistroIvaNota();
		// Imposto il TipoRegistroIva nel Registro IVA (richiesta del servizio)
		sis.getRegistroIva().setTipoRegistroIva(tri);
		
		TipoEsigibilitaIva tei = getTipoRegistroIvaNota().getTipoEsigibilitaIva();
		if(TipoEsigibilitaIva.IMMEDIATA == tei) {
			// Se l'esigibiltà è immediata, svuoto il campo del provvisorio
			sis.setDataProtocolloProvvisorio(null);
		} else if(TipoEsigibilitaIva.DIFFERITA == tei) {
			// Se l'esigibiltà è differita, svuoto il campo del definitivo
			sis.setDataProtocolloDefinitivo(null);
		}
		
		// Imposto il padre (ne creo uno ex-novo impostando soltanto l'uid, per alleggerire la request ed evitare eventuali loop infiniti)
		sis.setSubdocumentoIvaPadre(creaSubdocumentoIvaSpesa(getSubdocumentoIva().getUid()));
		
		getSubdocumentoIva().setTipoRelazione(null);
		sis.setTipoRelazione(TipoRelazione.NOTA_CREDITO_IVA);
		
		return sis;
	}
	
	/**
	 * Crea il subdocumento Iva spesa.
	 * 
	 * @return il subdocumento creato
	 */
	private SubdocumentoIvaSpesa popolaSubdocumentoIvaSpesa() {
		SubdocumentoIvaSpesa sis = getSubdocumentoIva();
		
		sis.setAttivitaIva(impostaEntitaFacoltativa(getAttivitaIva()));
		sis.setListaAliquotaSubdocumentoIva(getListaAliquotaSubdocumentoIva());
		
		TipoRegistroIva tri = getTipoRegistroIva();
		// Imposto il TipoRegistroIva nel Registro IVA (richiesta del servizio)
		sis.getRegistroIva().setTipoRegistroIva(tri);
		
		// Intracomunitario
		if(Boolean.TRUE.equals(sis.getFlagIntracomunitario())) {
			// Imposto valuta e importo in valuta
			sis.setValuta(impostaEntitaFacoltativa(getValuta()));
			sis.setImportoInValuta(getImportoInValuta());
		}
		
//		// Imposto documento o subdocumento
//		if(Boolean.TRUE.equals(getTipoSubdocumentoIvaQuota())) {
//			sis.setSubdocumento(getSubdocumento());
//			sis.setDocumento(null);
//		} else {
//			sis.setDocumento(getDocumento());
//			sis.setSubdocumento(null);
//		}
//		
//		// Se non ho lo stato, allora lo creo a PROVVISORIO
//		if(sis.getStatoSubdocumentoIva() == null) {
//			sis.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO);
//		}
//		
//		TipoEsigibilitaIva tei = tri.getTipoEsigibilitaIva();
//		if(isRegistrazioneSenzaProtocollo()){
//			sis.setDataProtocolloProvvisorio(null);
//			sis.setNumeroProtocolloProvvisorio(null);
//			sis.setDataProtocolloDefinitivo(null);
//			sis.setNumeroProtocolloDefinitivo(null);
//		}else if(tei == TipoEsigibilitaIva.IMMEDIATA) {
//			// Se l'esigibiltà è immediata, svuoto il campo del provvisorio
//			sis.setDataProtocolloProvvisorio(null);
//			sis.setNumeroProtocolloProvvisorio(null);
//		} else if(tei == TipoEsigibilitaIva.DIFFERITA) {
//			// Se l'esigibiltà è differita, svuoto il campo del definitivo
//			sis.setDataProtocolloDefinitivo(null);
//			sis.setNumeroProtocolloDefinitivo(null);
//		}
		
		return sis;
	}

	/**
	 * Calcola il totale degli importi da dedurre. Tiene conto solo delle quote rilevanti iva.
	 * 
	 * @return il totale da dedurre
	 */
	public BigDecimal calcolaTotaleImportoDaDedurre() {
		BigDecimal totale = BigDecimal.ZERO;
		for(SubdocumentoSpesa sp : getDocumento().getListaSubdocumenti()){
			if(Boolean.TRUE.equals(sp.getFlagRilevanteIVA())){
				totale = totale.add(sp.getImportoDaDedurre());
			}
		}
		return totale;
	}

}
