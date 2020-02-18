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

import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaNotaCreditoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaQuotaIvaDifferitaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ContaDatiCollegatiSubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceNotaCreditoIvaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaStampaIva;
import it.csi.siac.siacfin2ser.model.AliquotaSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.AttivitaIva;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.Periodo;
import it.csi.siac.siacfin2ser.model.RegistroIva;
import it.csi.siac.siacfin2ser.model.StampaIva;
import it.csi.siac.siacfin2ser.model.StatoSubdocumentoIva;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoIvaEntrata;
import it.csi.siac.siacfin2ser.model.TipoChiusura;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfin2ser.model.TipoEsigibilitaIva;
import it.csi.siac.siacfin2ser.model.TipoRegistrazioneIva;
import it.csi.siac.siacfin2ser.model.TipoRegistroIva;
import it.csi.siac.siacfin2ser.model.TipoRelazione;
import it.csi.siac.siacfin2ser.model.TipoStampaIva;
import it.csi.siac.siacfin2ser.model.Valuta;

/**
 * Classe di model per l'aggiornamento del Documento Iva Entrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/06/2014
 *
 */
public class AggiornaDocumentoIvaEntrataModel extends GenericDocumentoIvaEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8855756794363637535L;
	
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
	private SubdocumentoIvaEntrata nota;
	private Boolean flagRilevanteIRAPNota = Boolean.FALSE;
	private AttivitaIva attivitaIvaNota;
	private TipoRegistroIva tipoRegistroIvaNota;
	private AliquotaSubdocumentoIva aliquotaSubdocumentoIvaNota;
	private BigDecimal percentualeAliquotaIvaNota = BigDecimal.ZERO;
	private BigDecimal percentualeIndetraibilitaAliquotaIvaNota = BigDecimal.ZERO;
	
	private List<RegistroIva> listaRegistroIvaNota = new ArrayList<RegistroIva>();
//	private List<AliquotaSubdocumentoIva> listaAliquotaSubdocumentoIvaNota = new SortedSetList<AliquotaSubdocumentoIva>(ComparatorAliquotaSubdocumentoIva.INSTANCE);
	private BigDecimal importoTotaleNote = BigDecimal.ZERO;
	private BigDecimal importoDaDedurre = BigDecimal.ZERO;
	
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
	public AggiornaDocumentoIvaEntrataModel() {
		super();
		setTitolo("Aggiorna documenti iva entrata");
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
	 * @return the listaTipoRegistrazioneIvaAggiornamento
	 */
	public List<TipoRegistrazioneIva> getListaTipoRegistrazioneIvaAggiornamento() {
		return listaTipoRegistrazioneIvaAggiornamento;
	}

	/**
	 * @param listaTipoRegistrazioneIvaAggiornamento the listaTipoRegistrazioneIvaAggiornamento to set
	 */
	public void setListaTipoRegistrazioneIvaAggiornamento(
			List<TipoRegistrazioneIva> listaTipoRegistrazioneIvaAggiornamento) {
		this.listaTipoRegistrazioneIvaAggiornamento = listaTipoRegistrazioneIvaAggiornamento;
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
	 * @return the listaTipoRegistroIvaAggiornamento
	 */
	public List<TipoRegistroIva> getListaTipoRegistroIvaAggiornamento() {
		return listaTipoRegistroIvaAggiornamento;
	}

	/**
	 * @param listaTipoRegistroIvaAggiornamento the listaTipoRegistroIvaAggiornamento to set
	 */
	public void setListaTipoRegistroIvaAggiornamento(List<TipoRegistroIva> listaTipoRegistroIvaAggiornamento) {
		this.listaTipoRegistroIvaAggiornamento = listaTipoRegistroIvaAggiornamento;
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
		this.listaRegistroIvaAggiornamento = listaRegistroIvaAggiornamento;
	}

	/**
	 * @param aperturaTabNotaCredito the aperturaTabNotaCredito to set
	 */
	public void setAperturaTabNotaCredito(Boolean aperturaTabNotaCredito) {
		this.aperturaTabNotaCredito = aperturaTabNotaCredito != null ? aperturaTabNotaCredito : Boolean.FALSE;
	}

	/**
	 * @return the nota
	 */
	public SubdocumentoIvaEntrata getNota() {
		return nota;
	}

	/**
	 * @param nota the nota to set
	 */
	public void setNota(SubdocumentoIvaEntrata nota) {
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
		this.flagRilevanteIRAPNota = flagRilevanteIRAPNota;
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
		this.percentualeIndetraibilitaAliquotaIvaNota = percentualeIndetraibilitaAliquotaIvaNota;
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

//	/**
//	 * @return the listaAliquotaSubdocumentoIvaNota
//	 */
//	public List<AliquotaSubdocumentoIva> getListaAliquotaSubdocumentoIvaNota() {
//		return listaAliquotaSubdocumentoIvaNota;
//	}
//
//	/**
//	 * @param listaAliquotaSubdocumentoIvaNota the listaAliquotaSubdocumentoIvaNota to set
//	 */
//	public void setListaAliquotaSubdocumentoIvaNota(List<AliquotaSubdocumentoIva> listaAliquotaSubdocumentoIvaNota) {
//		this.listaAliquotaSubdocumentoIvaNota = new SortedSetList<AliquotaSubdocumentoIva>(
//			listaAliquotaSubdocumentoIvaNota != null ? listaAliquotaSubdocumentoIvaNota : new ArrayList<AliquotaSubdocumentoIva>(),
//			ComparatorAliquotaSubdocumentoIva.INSTANCE);
//	}
//	
//	/**
//	 * Ottiene il totale degli imponibili dei MovimentiIva.
//	 * 
//	 * @return il totale degli imponibili
//	 */
//	public BigDecimal getTotaleImponibileMovimentiIvaNota() {
//		BigDecimal result = BigDecimal.ZERO;
//		for (AliquotaSubdocumentoIva asi : listaAliquotaSubdocumentoIvaNota) {
//			result = result.add(asi.getImponibile());
//		}
//		return result;
//	}
	
//	/**
//	 * Ottiene il totale delle imposte dei MovimentiIva.
//	 * 
//	 * @return il totale delle imposte
//	 */
//	public BigDecimal getTotaleImpostaMovimentiIvaNota() {
//		BigDecimal result = BigDecimal.ZERO;
//		for (AliquotaSubdocumentoIva asi : listaAliquotaSubdocumentoIvaNota) {
//			result = result.add(asi.getImposta());
//		}
//		return result;
//	}
//	
//	/**
//	 * Ottiene il totale dei totali dei MovimentiIva.
//	 * 
//	 * @return il totale dei totali
//	 */
//	public BigDecimal getTotaleTotaleMovimentiIvaNota() {
//		BigDecimal result = BigDecimal.ZERO;
//		for (AliquotaSubdocumentoIva asi : listaAliquotaSubdocumentoIvaNota) {
//			result = result.add(asi.getTotale());
//		}
//		return result;
//	}

	/**
	 * @return the listaNote
	 */
	public List<DocumentoEntrata> getListaNote() {
		if(getDocumento().getListaNoteCreditoEntrataFiglio() == null) {
			return new ArrayList<DocumentoEntrata>();
		}
		return getDocumento().getListaNoteCreditoEntrataFiglio();
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
	 * @return the registroIvaAggiornamento
	 */
	public RegistroIva getRegistroIvaAggiornamento() {
		return registroIvaAggiornamento;
	}

	/**
	 * @param modificheARegistroAbilitate the modificheARegistroAbilitate to set
	 */
	public void setModificheARegistroAbilitate(boolean modificheARegistroAbilitate) {
		this.modificheARegistroAbilitate = modificheARegistroAbilitate;
	}

	/**
	 * @param registroIvaAggiornamento the registroIvaAggiornamento to set
	 */
	public void setRegistroIvaAggiornamento(RegistroIva registroIvaAggiornamento) {
		this.registroIvaAggiornamento = registroIvaAggiornamento;
	}
	
	/* **** Requests **** */

	/**
	 * Crea una request per il servizio di {@link AggiornaSubdocumentoIvaEntrata}.
	 * 
	 * @return la request creata
	 */
	public AggiornaSubdocumentoIvaEntrata creaRequestAggiornaSubdocumentoIvaEntrata() {
		AggiornaSubdocumentoIvaEntrata request = creaRequest(AggiornaSubdocumentoIvaEntrata.class);
		
		request.setBilancio(getBilancio());
		request.setSubdocumentoIvaEntrata(popolaSubdocumentoIvaEntrata());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InserisceNotaCreditoIvaEntrata} per le note.
	 * 
	 * @return la request creata
	 */
	public InserisceNotaCreditoIvaEntrata creaRequestInserisceNotaCreditoIvaEntrata() {
		InserisceNotaCreditoIvaEntrata request = creaRequest(InserisceNotaCreditoIvaEntrata.class);
		
		request.setBilancio(getBilancio());
		
		SubdocumentoIvaEntrata sis = getNota();
		sis.setAttivitaIva(impostaEntitaFacoltativa(getAttivitaIva()));
		sis.setListaAliquotaSubdocumentoIva(getListaAliquotaSubdocumentoIva());
		TipoRegistroIva tri = getTipoRegistroIva();
		sis.getRegistroIva().setTipoRegistroIva(tri);
		sis.setSubdocumentoIvaPadre(creaSubdocumentoIvaEntrata(getSubdocumentoIva().getUid()));
		
		request.setSubdocumentoIvaEntrata(sis);
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaNotaCreditoIvaEntrata} per le note.
	 * 
	 * @return la request creata
	 */
	public AggiornaNotaCreditoIvaEntrata creaRequestAggiornaNotaCreditoIvaEntrata() {
		AggiornaNotaCreditoIvaEntrata request = creaRequest(AggiornaNotaCreditoIvaEntrata.class);
		
		request.setBilancio(getBilancio());
		request.setSubdocumentoIvaEntrata(creaSubdocumentoIvaEntrataNota());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaQuotaIvaDifferitaEntrata}.
	 * 
	 * @return la request creata
	 */
	public AggiornaQuotaIvaDifferitaEntrata creaRequestAggiornaQuotaIvaDifferitaEntrata() {
		AggiornaQuotaIvaDifferitaEntrata request = creaRequest(AggiornaQuotaIvaDifferitaEntrata.class);
		
		request.setBilancio(getBilancio());
		request.setSubdocumentoIvaEntrata(popolaSubdocumentoIvaEntrata());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaStampaIva}.
	 * @param dataProtocollo la data di protocollo
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
	 * Crea una request per il servizio di {@link ContaDatiCollegatiSubdocumentoIvaEntrata}.
	 * @return la request creata
	 */
	public ContaDatiCollegatiSubdocumentoIvaEntrata creaRequestContaDatiCollegatiSubdocumentoIvaEntrata() {
		ContaDatiCollegatiSubdocumentoIvaEntrata request = creaRequest(ContaDatiCollegatiSubdocumentoIvaEntrata.class);
		
		SubdocumentoIvaEntrata sis = new SubdocumentoIvaEntrata();
		sis.setUid(getSubdocumentoIva().getUid());
		request.setSubdocumentoIvaEntrata(sis);
		
		return request;
	}
	
	/**
	 * Crea il subdocumento Iva entrata.
	 * 
	 * @return il subdocumento creato
	 */
	private SubdocumentoIvaEntrata popolaSubdocumentoIvaEntrata() {
		SubdocumentoIvaEntrata sie = getSubdocumentoIva();
		
		sie.setAttivitaIva(impostaEntitaFacoltativa(getAttivitaIva()));
		sie.setListaAliquotaSubdocumentoIva(getListaAliquotaSubdocumentoIva());
		TipoRegistroIva tri = getTipoRegistroIva();
		sie.getRegistroIva().setTipoRegistroIva(tri);
		
		//Tutto questo blocco serviva solo in inserimento, non in aggiornamento
		// Imposto documento o subdocumento
//		if(Boolean.TRUE.equals(getTipoSubdocumentoIvaQuota())) {
//			sie.setSubdocumento(getSubdocumento());
//		} else {
//			sie.setDocumento(getDocumento());
//		}
//		
//		TipoEsigibilitaIva tei = tri.getTipoEsigibilitaIva();
//		if(isRegistrazioneSenzaProtocollo()){
//			sie.setDataProtocolloProvvisorio(null);
//			sie.setNumeroProtocolloProvvisorio(null);
//			sie.setDataProtocolloDefinitivo(null);
//			sie.setNumeroProtocolloDefinitivo(null);
//		} else if(tei == TipoEsigibilitaIva.IMMEDIATA) {
//			// Se l'esigibiltà è immediata, svuoto il campo del provvisorio
//			sie.setDataProtocolloProvvisorio(null);
//		} else if(tei == TipoEsigibilitaIva.DIFFERITA) {
//			// Se l'esigibiltà è differita, svuoto il campo del definitivo
//			sie.setDataProtocolloDefinitivo(null);
//		}
		
		return sie;
	}
	
	/**
	 * Crea il subdocumento Iva spesa.
	 * 
	 * @return il subdocumento creato
	 */
	private SubdocumentoIvaEntrata creaSubdocumentoIvaEntrataNota() {
		SubdocumentoIvaEntrata sie = getNota();
		
		sie.setAttivitaIva(impostaEntitaFacoltativa(getAttivitaIva()));
		sie.setListaAliquotaSubdocumentoIva(getListaAliquotaSubdocumentoIva());
		
		sie.setEnte(getEnte());
		sie.setAnnoEsercizio(getAnnoEsercizioInt());
		sie.setAttivitaIva(impostaEntitaFacoltativa(getAttivitaIvaNota()));
		sie.setListaAliquotaSubdocumentoIva(getListaAliquotaSubdocumentoIvaNota());
		
		// Se non ho lo stato, allora lo creo a PROVVISORIO
		if(sie.getStatoSubdocumentoIva() == null) {
			sie.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO);
		}
		
		TipoRegistroIva tri = getTipoRegistroIvaNota();
		// Imposto il TipoRegistroIva nel Registro IVA (richiesta del servizio)
		sie.getRegistroIva().setTipoRegistroIva(tri);
		
		TipoEsigibilitaIva tei = getTipoRegistroIvaNota().getTipoEsigibilitaIva();
		if(TipoEsigibilitaIva.IMMEDIATA == tei) {
			// Se l'esigibiltà è immediata, svuoto il campo del provvisorio
			sie.setDataProtocolloProvvisorio(null);
		} else if(TipoEsigibilitaIva.DIFFERITA == tei) {
			// Se l'esigibiltà è differita, svuoto il campo del definitivo
			sie.setDataProtocolloDefinitivo(null);
		}
		
		// Imposto il padre (ne creo uno ex-novo impostando soltanto l'uid, per alleggerire la request ed evitare eventuali loop infiniti)
		sie.setSubdocumentoIvaPadre(creaSubdocumentoIvaEntrata(getSubdocumentoIva().getUid()));
		
		getSubdocumentoIva().setTipoRelazione(null);
		sie.setTipoRelazione(TipoRelazione.NOTA_CREDITO_IVA);
		
		return sie;
	}
	
//	/**
//	 * Crea il subdocumento Iva entrata.
//	 * 
//	 * @return il subdocumento creato
//	 */
//	private SubdocumentoIvaEntrata creaSubdocumentoIvaEntrataQuotaIvaDifferita() {
//		SubdocumentoIvaEntrata sis = getSubdocumentoIva();
//		
//		sis.setEnte(getEnte());
////		sis.setAnnoEsercizio(getAnnoEsercizioInt());
//		sis.setAttivitaIva(impostaEntitaFacoltativa(getAttivitaIva()));
//		sis.setListaAliquotaSubdocumentoIva(getListaAliquotaSubdocumentoIva());
//		
//		// Se non ho lo stato, allora lo creo a PROVVISORIO
//		if(sis.getStatoSubdocumentoIva() == null) {
//			sis.setStatoSubdocumentoIva(StatoSubdocumentoIva.PROVVISORIO);
//		}
//		
//		TipoRegistroIva tri = getTipoRegistroIva();
//		// Imposto il TipoRegistroIva nel Registro IVA (richiesta del servizio)
//		sis.getRegistroIva().setTipoRegistroIva(tri);
//		
//		TipoEsigibilitaIva tei = getTipoRegistroIva().getTipoEsigibilitaIva();
//		if(TipoEsigibilitaIva.IMMEDIATA == tei) {
//			// Se l'esigibiltà è immediata, svuoto il campo del provvisorio
//			sis.setDataProtocolloProvvisorio(null);
//		} else if(TipoEsigibilitaIva.DIFFERITA == tei) {
//			// Se l'esigibiltà è differita, svuoto il campo del definitivo
//			sis.setDataProtocolloDefinitivo(null);
//		}
//		
//		return sis;
//	}

	/**
	 * Calcola il totale degli importi da dedurre. Tiene conto solo delle quote rilevanti iva.
	 * 
	 * @return il totale da dedurre
	 */
	public BigDecimal calcolaTotaleImportoDaDedurre() {
		BigDecimal totale = BigDecimal.ZERO;
		for(SubdocumentoEntrata se : getDocumento().getListaSubdocumenti()){
			if(Boolean.TRUE.equals(se.getFlagRilevanteIVA())){
				totale = totale.add(se.getImportoDaDedurre());
			}
		}
		return totale;
	}

}
