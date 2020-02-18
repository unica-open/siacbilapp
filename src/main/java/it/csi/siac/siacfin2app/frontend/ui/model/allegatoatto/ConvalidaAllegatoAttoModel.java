/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacbilapp.frontend.ui.util.collections.list.SortedSetList;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorElementoElencoDocumentiAllegato;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorElencoDocumentiAllegato;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegato;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegatoEntrata;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegatoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.ConvalidaAllegatoAttoPerElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RifiutaElenchi;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAttoModelDetail;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegatoModelDetail;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;

/**
 * Classe di model per la convalida dell'AllegatoAtto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/10/2014
 */
public class ConvalidaAllegatoAttoModel extends GenericAllegatoAttoModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -8690633760980886401L;
	
	private Boolean convalidaManuale = Boolean.TRUE;
	private Boolean disabledButtons = Boolean.FALSE;
	
	// Elenchi
	private BigDecimal totaleElenchi = BigDecimal.ZERO;
	private BigDecimal totaleSpeseConvalidabili = BigDecimal.ZERO;
	private BigDecimal totaleEntrateConvalidabili = BigDecimal.ZERO;
	private BigDecimal nonValidatoSpeseConvalidabili = BigDecimal.ZERO;
	private BigDecimal nonValidatoEntrateConvalidabili = BigDecimal.ZERO;
	
	private BigDecimal totaleSpeseNonConvalidabili = BigDecimal.ZERO;
	private BigDecimal totaleEntrateNonConvalidabili = BigDecimal.ZERO;
	private BigDecimal nonValidatoSpeseNonConvalidabili = BigDecimal.ZERO;
	private BigDecimal nonValidatoEntrateNonConvalidabili = BigDecimal.ZERO;
	
	// Subdocumento
	private BigDecimal totaleSpesaDaConvalidareSubdocumenti = BigDecimal.ZERO;
	private BigDecimal totaleSpesaConvalidateSubdocumenti = BigDecimal.ZERO;
	private BigDecimal totaleSpesaACoperturaSubdocumenti = BigDecimal.ZERO;
	
	private BigDecimal totaleEntrataDaConvalidareSubdocumenti = BigDecimal.ZERO;
	private BigDecimal totaleEntrataConvalidateSubdocumenti = BigDecimal.ZERO;
	private BigDecimal totaleEntrataACoperturaSubdocumenti = BigDecimal.ZERO;
	
	private List<ElencoDocumentiAllegato> listaElencoDocumentiAllegato = new SortedSetList<ElencoDocumentiAllegato>(ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
	private List<ElencoDocumentiAllegato> listaConvalidabili = new SortedSetList<ElencoDocumentiAllegato>(ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
	private List<ElencoDocumentiAllegato> listaNonConvalidabili = new SortedSetList<ElencoDocumentiAllegato>(ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
	
//	private List<ElementoElencoDocumentiAllegatoDaEmettere> listaElementiConvalidabili = new SortedSetList<ElencoDocumentiAllegato>(ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
//	private List<ElementoElencoDocumentiAllegatoDaEmettere> listaElementiNonConvalidabili = new SortedSetList<ElencoDocumentiAllegato>(ComparatorElencoDocumentiAllegato.INSTANCE_DES
	
	
	private List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaSubdocumentiConvalidabili =
			new SortedSetList<ElementoElencoDocumentiAllegato<?,?,?,?,?>>(ComparatorElementoElencoDocumentiAllegato.INSTANCE_ASC);
	private List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaSubdocumentiConvalidati =
			new SortedSetList<ElementoElencoDocumentiAllegato<?,?,?,?,?>>(ComparatorElementoElencoDocumentiAllegato.INSTANCE_ASC);
	private List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaSubdocumentiACopertura =
			new SortedSetList<ElementoElencoDocumentiAllegato<?,?,?,?,?>>(ComparatorElementoElencoDocumentiAllegato.INSTANCE_ASC);
	
	private List<Integer> listaUid = new ArrayList<Integer>();
	// SIAC-5170
	private Integer uidAllegatoAtto;
	
	/** Costruttore vuoto di default */
	public ConvalidaAllegatoAttoModel() {
		setTitolo("Valutazione allegati agli atti");
	}
	
	/**
	 * @return the convalidaManuale
	 */
	public Boolean getConvalidaManuale() {
		return convalidaManuale;
	}

	/**
	 * @param convalidaManuale the convalidaManuale to set
	 */
	public void setConvalidaManuale(Boolean convalidaManuale) {
		this.convalidaManuale = convalidaManuale;
	}
	
	/**
	 * @return the disabledButtons
	 */
	public Boolean getDisabledButtons() {
		return disabledButtons;
	}

	/**
	 * @param disabledButtons the disabledButtons to set
	 */
	public void setDisabledButtons(Boolean disabledButtons) {
		this.disabledButtons = disabledButtons != null ? disabledButtons : Boolean.FALSE;
	}

	/**
	 * @return the totaleElenchi
	 */
	public BigDecimal getTotaleElenchi() {
		return totaleElenchi;
	}

	/**
	 * @param totaleElenchi the totaleElenchi to set
	 */
	public void setTotaleElenchi(BigDecimal totaleElenchi) {
		this.totaleElenchi = totaleElenchi != null ? totaleElenchi : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleSpeseConvalidabili
	 */
	public BigDecimal getTotaleSpeseConvalidabili() {
		return totaleSpeseConvalidabili;
	}

	/**
	 * @param totaleSpeseConvalidabili the totaleSpeseConvalidabili to set
	 */
	public void setTotaleSpeseConvalidabili(BigDecimal totaleSpeseConvalidabili) {
		this.totaleSpeseConvalidabili = totaleSpeseConvalidabili != null ? totaleSpeseConvalidabili : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleEntrateConvalidabili
	 */
	public BigDecimal getTotaleEntrateConvalidabili() {
		return totaleEntrateConvalidabili;
	}

	/**
	 * @param totaleEntrateConvalidabili the totaleEntrateConvalidabili to set
	 */
	public void setTotaleEntrateConvalidabili(BigDecimal totaleEntrateConvalidabili) {
		this.totaleEntrateConvalidabili = totaleEntrateConvalidabili != null ? totaleEntrateConvalidabili : BigDecimal.ZERO;
	}

	/**
	 * @return the nonValidatoSpeseConvalidabili
	 */
	public BigDecimal getNonValidatoSpeseConvalidabili() {
		return nonValidatoSpeseConvalidabili;
	}

	/**
	 * @param nonValidatoSpeseConvalidabili the nonValidatoSpeseConvalidabili to set
	 */
	public void setNonValidatoSpeseConvalidabili(BigDecimal nonValidatoSpeseConvalidabili) {
		this.nonValidatoSpeseConvalidabili = nonValidatoSpeseConvalidabili != null ? nonValidatoSpeseConvalidabili : BigDecimal.ZERO;
	}

	/**
	 * @return the nonValidatoEntrateConvalidabili
	 */
	public BigDecimal getNonValidatoEntrateConvalidabili() {
		return nonValidatoEntrateConvalidabili;
	}

	/**
	 * @param nonValidatoEntrateConvalidabili the nonValidatoEntrateConvalidabili to set
	 */
	public void setNonValidatoEntrateConvalidabili(BigDecimal nonValidatoEntrateConvalidabili) {
		this.nonValidatoEntrateConvalidabili = nonValidatoEntrateConvalidabili != null ? nonValidatoEntrateConvalidabili : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleSpeseNonConvalidabili
	 */
	public BigDecimal getTotaleSpeseNonConvalidabili() {
		return totaleSpeseNonConvalidabili;
	}

	/**
	 * @param totaleSpeseNonConvalidabili the totaleSpeseNonConvalidabili to set
	 */
	public void setTotaleSpeseNonConvalidabili(BigDecimal totaleSpeseNonConvalidabili) {
		this.totaleSpeseNonConvalidabili = totaleSpeseNonConvalidabili != null ? totaleSpeseNonConvalidabili : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleEntrateNonConvalidabili
	 */
	public BigDecimal getTotaleEntrateNonConvalidabili() {
		return totaleEntrateNonConvalidabili;
	}

	/**
	 * @param totaleEntrateNonConvalidabili the totaleEntrateNonConvalidabili to set
	 */
	public void setTotaleEntrateNonConvalidabili(BigDecimal totaleEntrateNonConvalidabili) {
		this.totaleEntrateNonConvalidabili = totaleEntrateNonConvalidabili != null ? totaleEntrateNonConvalidabili : BigDecimal.ZERO;
	}

	/**
	 * @return the nonValidatoSpeseNonConvalidabili
	 */
	public BigDecimal getNonValidatoSpeseNonConvalidabili() {
		return nonValidatoSpeseNonConvalidabili;
	}

	/**
	 * @param nonValidatoSpeseNonConvalidabili the nonValidatoSpeseNonConvalidabili to set
	 */
	public void setNonValidatoSpeseNonConvalidabili(BigDecimal nonValidatoSpeseNonConvalidabili) {
		this.nonValidatoSpeseNonConvalidabili = nonValidatoSpeseNonConvalidabili != null ? nonValidatoSpeseNonConvalidabili : BigDecimal.ZERO;
	}

	/**
	 * @return the nonValidatoEntrateNonConvalidabili
	 */
	public BigDecimal getNonValidatoEntrateNonConvalidabili() {
		return nonValidatoEntrateNonConvalidabili;
	}

	/**
	 * @param nonValidatoEntrateNonConvalidabili the nonValidatoEntrateNonConvalidabili to set
	 */
	public void setNonValidatoEntrateNonConvalidabili(BigDecimal nonValidatoEntrateNonConvalidabili) {
		this.nonValidatoEntrateNonConvalidabili = nonValidatoEntrateNonConvalidabili != null ? nonValidatoEntrateNonConvalidabili : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleSpesaDaConvalidareSubdocumenti
	 */
	public BigDecimal getTotaleSpesaDaConvalidareSubdocumenti() {
		return totaleSpesaDaConvalidareSubdocumenti;
	}

	/**
	 * @param totaleSpesaDaConvalidareSubdocumenti the totaleSpesaDaConvalidareSubdocumenti to set
	 */
	public void setTotaleSpesaDaConvalidareSubdocumenti(BigDecimal totaleSpesaDaConvalidareSubdocumenti) {
		this.totaleSpesaDaConvalidareSubdocumenti = totaleSpesaDaConvalidareSubdocumenti != null ? totaleSpesaDaConvalidareSubdocumenti : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleSpesaConvalidateSubdocumenti
	 */
	public BigDecimal getTotaleSpesaConvalidateSubdocumenti() {
		return totaleSpesaConvalidateSubdocumenti;
	}

	/**
	 * @param totaleSpesaConvalidateSubdocumenti the totaleSpesaConvalidateSubdocumenti to set
	 */
	public void setTotaleSpesaConvalidateSubdocumenti(BigDecimal totaleSpesaConvalidateSubdocumenti) {
		this.totaleSpesaConvalidateSubdocumenti = totaleSpesaConvalidateSubdocumenti != null ? totaleSpesaConvalidateSubdocumenti : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleSpesaACoperturaSubdocumenti
	 */
	public BigDecimal getTotaleSpesaACoperturaSubdocumenti() {
		return totaleSpesaACoperturaSubdocumenti;
	}

	/**
	 * @param totaleSpesaACoperturaSubdocumenti the totaleSpesaACoperturaSubdocumenti to set
	 */
	public void setTotaleSpesaACoperturaSubdocumenti(BigDecimal totaleSpesaACoperturaSubdocumenti) {
		this.totaleSpesaACoperturaSubdocumenti = totaleSpesaACoperturaSubdocumenti != null ? totaleSpesaACoperturaSubdocumenti : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleEntrataDaConvalidareSubdocumenti
	 */
	public BigDecimal getTotaleEntrataDaConvalidareSubdocumenti() {
		return totaleEntrataDaConvalidareSubdocumenti;
	}

	/**
	 * @param totaleEntrataDaConvalidareSubdocumenti the totaleEntrataDaConvalidareSubdocumenti to set
	 */
	public void setTotaleEntrataDaConvalidareSubdocumenti(BigDecimal totaleEntrataDaConvalidareSubdocumenti) {
		this.totaleEntrataDaConvalidareSubdocumenti = totaleEntrataDaConvalidareSubdocumenti != null ? totaleEntrataDaConvalidareSubdocumenti : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleEntrataConvalidateSubdocumenti
	 */
	public BigDecimal getTotaleEntrataConvalidateSubdocumenti() {
		return totaleEntrataConvalidateSubdocumenti;
	}

	/**
	 * @param totaleEntrataConvalidateSubdocumenti the totaleEntrataConvalidateSubdocumenti to set
	 */
	public void setTotaleEntrataConvalidateSubdocumenti(BigDecimal totaleEntrataConvalidateSubdocumenti) {
		this.totaleEntrataConvalidateSubdocumenti = totaleEntrataConvalidateSubdocumenti != null ? totaleEntrataConvalidateSubdocumenti : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleEntrataACoperturaSubdocumenti
	 */
	public BigDecimal getTotaleEntrataACoperturaSubdocumenti() {
		return totaleEntrataACoperturaSubdocumenti;
	}

	/**
	 * @param totaleEntrataACoperturaSubdocumenti the totaleEntrataACoperturaSubdocumenti to set
	 */
	public void setTotaleEntrataACoperturaSubdocumenti(BigDecimal totaleEntrataACoperturaSubdocumenti) {
		this.totaleEntrataACoperturaSubdocumenti = totaleEntrataACoperturaSubdocumenti != null ? totaleEntrataACoperturaSubdocumenti : BigDecimal.ZERO;
	}

	/**
	 * @return the listaElencoDocumentiAllegato
	 */
	public List<ElencoDocumentiAllegato> getListaElencoDocumentiAllegato() {
		return listaElencoDocumentiAllegato;
	}

	/**
	 * @param listaElencoDocumentiAllegato the listaElencoDocumentiAllegato to set
	 */
	public void setListaElencoDocumentiAllegato(List<ElencoDocumentiAllegato> listaElencoDocumentiAllegato) {
		this.listaElencoDocumentiAllegato = new SortedSetList<ElencoDocumentiAllegato>(
			listaElencoDocumentiAllegato != null ? listaElencoDocumentiAllegato : new ArrayList<ElencoDocumentiAllegato>(),
			ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
	}

	/**
	 * @return the listaConvalidabili
	 */
	public List<ElencoDocumentiAllegato> getListaConvalidabili() {
		return listaConvalidabili;
	}

	/**
	 * @param listaConvalidabili the listaConvalidabili to set
	 */
	public void setListaConvalidabili(List<ElencoDocumentiAllegato> listaConvalidabili) {
		this.listaConvalidabili = new SortedSetList<ElencoDocumentiAllegato>(
			listaConvalidabili != null ? listaConvalidabili : new ArrayList<ElencoDocumentiAllegato>(),
			ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
	}

	/**
	 * @return the listaNonConvalidabili
	 */
	public List<ElencoDocumentiAllegato> getListaNonConvalidabili() {
		return listaNonConvalidabili;
	}

	/**
	 * @param listaNonConvalidabili the listaNonConvalidabili to set
	 */
	public void setListaNonConvalidabili(List<ElencoDocumentiAllegato> listaNonConvalidabili) {
		this.listaNonConvalidabili = new SortedSetList<ElencoDocumentiAllegato>(
			listaNonConvalidabili != null ? listaNonConvalidabili : new ArrayList<ElencoDocumentiAllegato>(),
			ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
	}

	/**
	 * @return the listaSubdocumentiConvalidabili
	 */
	public List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> getListaSubdocumentiConvalidabili() {
		return listaSubdocumentiConvalidabili;
	}

	/**
	 * @param listaSubdocumentiConvalidabili the listaSubdocumentiConvalidabili to set
	 */
	public void setListaSubdocumentiConvalidabili(List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaSubdocumentiConvalidabili) {
		this.listaSubdocumentiConvalidabili = new SortedSetList<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>>(
			listaSubdocumentiConvalidabili != null ? listaSubdocumentiConvalidabili : new ArrayList<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>>(),
			ComparatorElementoElencoDocumentiAllegato.INSTANCE_ASC);
	}

	/**
	 * @return the listaSubdocumentiConvalidati
	 */
	public List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> getListaSubdocumentiConvalidati() {
		return listaSubdocumentiConvalidati;
	}

	/**
	 * @param listaSubdocumentiConvalidati the listaSubdocumentiConvalidati to set
	 */
	public void setListaSubdocumentiConvalidati(List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaSubdocumentiConvalidati) {
		this.listaSubdocumentiConvalidati = new SortedSetList<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>>(
			listaSubdocumentiConvalidati != null ? listaSubdocumentiConvalidati : new ArrayList<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>>(),
			ComparatorElementoElencoDocumentiAllegato.INSTANCE_ASC);
	}

	/**
	 * @return the listaSubdocumentiACopertura
	 */
	public List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> getListaSubdocumentiACopertura() {
		return listaSubdocumentiACopertura;
	}

	/**
	 * @param listaSubdocumentiACopertura the listaSubdocumentiACopertura to set
	 */
	public void setListaSubdocumentiACopertura(List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaSubdocumentiACopertura) {
		this.listaSubdocumentiACopertura = new SortedSetList<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>>(
			listaSubdocumentiACopertura != null ? listaSubdocumentiACopertura : new ArrayList<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>>(),
			ComparatorElementoElencoDocumentiAllegato.INSTANCE_ASC);
	}
	
	/**
	 * @return the listaSubdocumentiNonConvalidabili
	 */
	public List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> getListaSubdocumentiNonConvalidabili() {
		List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> result =
				new SortedSetList<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>>(ComparatorElementoElencoDocumentiAllegato.INSTANCE_ASC);
		result.addAll(getListaSubdocumentiConvalidati());
		result.addAll(getListaSubdocumentiACopertura());
		return result;
	}

	/**
	 * @return the listaUid
	 */
	public List<Integer> getListaUid() {
		return listaUid;
	}

	/**
	 * @param listaUid the listaUid to set
	 */
	public void setListaUid(List<Integer> listaUid) {
		this.listaUid = listaUid != null ? new ArrayList<Integer>(listaUid) : new ArrayList<Integer>();
	}

	/**
	 * @return the uidAllegatoAtto
	 */
	public Integer getUidAllegatoAtto() {
		return uidAllegatoAtto;
	}

	/**
	 * @param uidAllegatoAtto the uidAllegatoAtto to set
	 */
	public void setUidAllegatoAtto(Integer uidAllegatoAtto) {
		this.uidAllegatoAtto = uidAllegatoAtto;
	}

	/**
	 * @return the descrizioneCompletaAttoAmministrativo
	 */
	public String getDescrizioneCompletaAttoAmministrativo() {
		return getAttoAmministrativo() == null ? "" : (": " + computeDescrizioneCompletaAttoAmministrativo(getAttoAmministrativo()));
	}
	
	/**
	 * @return the descrizioneCompletaAllegatoAtto
	 * 
	 * @version 1.1 04/04/2016 CR-3206 AHMAD
	 */
	public String getDescrizioneCompletaAllegatoAtto() {
		StringBuilder sb = new StringBuilder();
		sb.append(getAllegatoAtto().getAttoAmministrativo().getAnno());
		sb.append("/");
		sb.append(getAllegatoAtto().getAttoAmministrativo().getNumero());
		sb.append(" - Versione Invio Firma: ");
		sb.append(getAllegatoAtto().getVersioneInvioFirmaNotNull());
		sb.append(getAllegatoAtto().getDataVersioneInvioFirma()!=null ? " Del "+FormatUtils.formatDate(getAllegatoAtto().getDataVersioneInvioFirma()):" ");
		sb.append(getAllegatoAtto().getUtenteVersioneInvioFirma() !=null ? " Da "+getAllegatoAtto().getUtenteVersioneInvioFirma():" ");

		return sb.toString();
	}
	
	/**
	 * @return the totaleElenchiCollegati
	 */
	public Integer getTotaleElenchiCollegati() {
		return getListaElencoDocumentiAllegato().size();
	}
	
	/**
	 * @return the totaleSpesaNonConvalidabiliSubdocumenti
	 */
	public BigDecimal getTotaleSpesaNonConvalidabiliSubdocumenti() {
		return getTotaleSpesaConvalidateSubdocumenti().add(getTotaleSpesaACoperturaSubdocumenti());
	}
	
	/**
	 * @return the totaleEntrataNonConvalidabiliSubdocumenti
	 */
	public BigDecimal getTotaleEntrataNonConvalidabiliSubdocumenti() {
		return getTotaleEntrataConvalidateSubdocumenti().add(getTotaleEntrataACoperturaSubdocumenti());
	}
	
	/**
	 * @return the totaleSpesaSubdocumenti
	 */
	public BigDecimal getTotaleSpesaSubdocumenti() {
		return getTotaleSpesaDaConvalidareSubdocumenti().add(getTotaleSpesaNonConvalidabiliSubdocumenti());
	}
	
	/**
	 * @return the totaleEntrataSubdocumenti
	 */
	public BigDecimal getTotaleEntrataSubdocumenti() {
		return getTotaleEntrataDaConvalidareSubdocumenti().add(getTotaleEntrataNonConvalidabiliSubdocumenti());
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio {@link RicercaDettaglioAllegatoAtto}.
	 * 
	 * @param allegatoAtto l'allegato atto rispetto cui generare la request
	 * @return la request creata
	 */
	public RicercaDettaglioAllegatoAtto creaRequestRicercaDettaglioAllegatoAtto(AllegatoAtto allegatoAtto) {
		RicercaDettaglioAllegatoAtto request = creaRequest(RicercaDettaglioAllegatoAtto.class);
		request.setAllegatoAttoModelDetails(AllegatoAttoModelDetail.DataInizioValiditaStato, AllegatoAttoModelDetail.DatiSoggetto, AllegatoAttoModelDetail.ElencoDocumentiModelDetail);
		request.setModelDetailsEntitaCollegate(ElencoDocumentiAllegatoModelDetail.TotaleDaPagareIncassare,ElencoDocumentiAllegatoModelDetail.TotaleQuoteSpesaEntrata, ElencoDocumentiAllegatoModelDetail.TotaleDaConvalidareSpesaEntrata,
				ElencoDocumentiAllegatoModelDetail.ContieneQuoteACopertura,ElencoDocumentiAllegatoModelDetail.SubdocumentiTotale,				
				ElencoDocumentiAllegatoModelDetail.HasImpegnoConfermaDataFineValiditaDurc
				);
		request.setAllegatoAtto(allegatoAtto);
		
		return request;
	}

	/**
	 * Crea una request per il servizio {@link RicercaElenco}.
	 * 
	 * @param numeroPagina la pagina rispetto cui ricercare l'allegato
	 * 
	 * @return la request creata
	 */
	public RicercaElenco creaRequestRicercaElenco(int numeroPagina) {
		RicercaElenco request = creaRequest(RicercaElenco.class);
		
		ElencoDocumentiAllegato elencoDocumentiAllegato = new ElencoDocumentiAllegato();
		elencoDocumentiAllegato.setAllegatoAtto(getAllegatoAtto());
		request.setElencoDocumentiAllegato(elencoDocumentiAllegato);
		
		request.setParametriPaginazione(new ParametriPaginazione(numeroPagina, ELEMENTI_PER_PAGINA_RICERCA));
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio {@link ConvalidaAllegatoAttoPerElenchi}.
	 * 
	 * @return la request creata
	 */
	public ConvalidaAllegatoAttoPerElenchi creaRequestConvalidaAllegatoAttoPerElenchi() {
		ConvalidaAllegatoAttoPerElenchi request = creaRequest(ConvalidaAllegatoAttoPerElenchi.class);
		
		request.setFlagConvalidaManuale(getConvalidaManuale());
		
		// Creo l'allegato per la request
		AllegatoAtto aa = new AllegatoAtto();
		request.setAllegatoAtto(aa);
		
		// Popolo l'allegato
		aa.setUid(getAllegatoAtto().getUid());
		aa.setEnte(getEnte());
		// Creo gli elenchi a partire dagli uid
		for(Integer uid : getListaUid()) {
			if(uid != null) {
				ElencoDocumentiAllegato eda = new ElencoDocumentiAllegato();
				eda.setUid(uid);
				aa.getElenchiDocumentiAllegato().add(eda);
			}
		}
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio {@link ConvalidaAllegatoAttoPerElenchi} per i subdocumenti.
	 * 
	 * @return la request creata
	 */
	public ConvalidaAllegatoAttoPerElenchi creaRequestConvalidaAllegatoAttoPerElenchiPerSubdocumenti() {
		ConvalidaAllegatoAttoPerElenchi request = creaRequest(ConvalidaAllegatoAttoPerElenchi.class);
		
		// Creo l'allegato per la request
		AllegatoAtto aa = new AllegatoAtto();
		request.setAllegatoAtto(aa);
		
		// Popolo l'allegato
		aa.setUid(getAllegatoAtto().getUid());
		aa.setEnte(getEnte());
		ElencoDocumentiAllegato eda = new ElencoDocumentiAllegato();
		aa.getElenchiDocumentiAllegato().add(eda);
		
		eda.setUid(getElencoDocumentiAllegato().getUid());
		// Creo i subdocumenti
		for(Integer uid : getListaUid()) {
			if(uid != null) {
				// Creo di entrata. E' alquanto inutile il tipo, dunque scelgo quello piu' leggero
				Subdocumento<?, ?> sub = instantiateSubdocumentoByUid(uid);
				if(sub == null) {
					// Ignoro il caso
					continue;
				}
				sub.setFlagConvalidaManuale(getConvalidaManuale());
				eda.getSubdocumenti().add(sub);
			}
		}
		
		return request;
	}
	
	/**
	 * Instanzia un subdocumento a partire dall'uid.
	 * 
	 * @param uid l'uid del subdocumento
	 * 
	 * @return un subdocumento di dato uid
	 */
	private Subdocumento<?, ?> instantiateSubdocumentoByUid(Integer uid) {
		// Vado a considerare solo subdocumenti nella lista dei convalidabili
		List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> convalidabili = getListaSubdocumentiConvalidabili();
		for(ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?> eeda : convalidabili) {
			Subdocumento<?, ?> subdocumento = eeda.extractSubdocumento();
			if(subdocumento != null && subdocumento.getUid() == uid.intValue()) {
				return subdocumento;
			}
		}
		return null;
	}

	/**
	 * Crea una request per il servizio {@link RifiutaElenchi}.
	 * 
	 * @param uidElenchiDaInserire       gli uid degli elenchi da inserire
	 * @param statoOperativoAllegatoAtto lo stato dell'allegato
	 * 
	 * @return la request creata
	 */
	public RifiutaElenchi creaRequestRifiutaElenchi(List<Integer> uidElenchiDaInserire, StatoOperativoAllegatoAtto statoOperativoAllegatoAtto) {
		RifiutaElenchi request = creaRequest(RifiutaElenchi.class);
		
		AllegatoAtto aa = new AllegatoAtto();
		request.setAllegatoAtto(aa);
		request.setBilancio(getBilancio());
		
		// Popolo l'allegato
		aa.setEnte(getEnte());
		aa.setUid(getAllegatoAtto().getUid());
		aa.setStatoOperativoAllegatoAtto(statoOperativoAllegatoAtto);
		if(uidElenchiDaInserire != null && !uidElenchiDaInserire.isEmpty()) {
			for(Integer uid : uidElenchiDaInserire) {
				if(uid != null) {
					ElencoDocumentiAllegato eda = new ElencoDocumentiAllegato();
					eda.setUid(uid);
					aa.getElenchiDocumentiAllegato().add(eda);
				}
			}
		}
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio {@link RicercaDettaglioElenco}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioElenco creaRequestRicercaDettaglioElenco() {
		RicercaDettaglioElenco request = creaRequest(RicercaDettaglioElenco.class);
		request.setElencoDocumentiAllegato(getElencoDocumentiAllegato());
		
		return request;
	}
	
	/* **** Utilities **** */
	
	/**
	 * Calcola i totali degli elenchi.
	 */
	public void computeTotali() {
		Map<String, BigDecimal> totaliConvalidabili = computeTotali(getListaConvalidabili());
		Map<String, BigDecimal> totaliNonConvalidabili = computeTotali(getListaNonConvalidabili());
		
		setTotaleSpeseConvalidabili(totaliConvalidabili.get("totaleSpese"));
		setTotaleEntrateConvalidabili(totaliConvalidabili.get("totaleEntrate"));
		setNonValidatoSpeseConvalidabili(totaliConvalidabili.get("nonValidatoSpese"));
		setNonValidatoEntrateConvalidabili(totaliConvalidabili.get("nonValidatoEntrate"));
		
		setTotaleSpeseNonConvalidabili(totaliNonConvalidabili.get("totaleSpese"));
		setTotaleEntrateNonConvalidabili(totaliNonConvalidabili.get("totaleEntrate"));
		setNonValidatoSpeseNonConvalidabili(totaliNonConvalidabili.get("nonValidatoSpese"));
		setNonValidatoEntrateNonConvalidabili(totaliNonConvalidabili.get("nonValidatoEntrate"));
		
		BigDecimal totaleDegliElenchi = getTotaleSpeseConvalidabili().add(getTotaleEntrateConvalidabili())
				.add(getTotaleSpeseNonConvalidabili()).add(getTotaleEntrateNonConvalidabili());
		
		setTotaleElenchi(totaleDegliElenchi);
	}

	/**
	 * Computa i totali per gli elenchi forniti.
	 * 
	 * @param list la liste degli elenchi
	 * @return il totale
	 */
	private Map<String, BigDecimal> computeTotali(List<ElencoDocumentiAllegato> list) {
		Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
		
		BigDecimal totaleSpese = BigDecimal.ZERO;
		BigDecimal totaleEntrate = BigDecimal.ZERO;
		BigDecimal nonValidatoSpese = BigDecimal.ZERO;
		BigDecimal nonValidatoEntrate = BigDecimal.ZERO;
		
		for(ElencoDocumentiAllegato eda : list) {
			totaleSpese = totaleSpese.add(eda.getTotaleQuoteSpese());
			totaleEntrate = totaleEntrate.add(eda.getTotaleQuoteEntrate());
			nonValidatoSpese = nonValidatoSpese.add(eda.getTotaleDaConvalidareSpesaNoCoperturaNotNull());
			nonValidatoEntrate = nonValidatoEntrate.add(eda.getTotaleDaConvalidareEntrataNoCoperturaNotNull());
		}
		
		result.put("totaleSpese", totaleSpese);
		result.put("totaleEntrate", totaleEntrate);
		result.put("nonValidatoSpese", nonValidatoSpese);
		result.put("nonValidatoEntrate", nonValidatoEntrate);
		
		return result;
	}

	/**
	 * Calcola i totali dei subdocumenti.
	 */
	public void computeTotaliSubdocumenti() {
		Map<String, BigDecimal> totaliConvalidabili = computeTotaliSubdocumenti(getListaSubdocumentiConvalidabili());
		setTotaleSpesaDaConvalidareSubdocumenti(totaliConvalidabili.get("spesa"));
		setTotaleEntrataDaConvalidareSubdocumenti(totaliConvalidabili.get("entrata"));
		
		Map<String, BigDecimal> totaliConvalidati = computeTotaliSubdocumenti(getListaSubdocumentiConvalidati());
		setTotaleSpesaConvalidateSubdocumenti(totaliConvalidati.get("spesa"));
		setTotaleEntrataConvalidateSubdocumenti(totaliConvalidati.get("entrata"));
		
		Map<String, BigDecimal> totaliACopertura = computeTotaliSubdocumenti(getListaSubdocumentiACopertura());
		setTotaleSpesaACoperturaSubdocumenti(totaliACopertura.get("spesa"));
		setTotaleEntrataACoperturaSubdocumenti(totaliACopertura.get("entrata"));
	}
	
	/**
	 * Calcola i totali dei subdocumenti.
	 * @param list la lista dei subdocumenti
	 * @return il totale
	 */
	private Map<String, BigDecimal> computeTotaliSubdocumenti(List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> list) {
		Map<String, BigDecimal> result = new HashMap<String, BigDecimal>();
		
		BigDecimal spesa = BigDecimal.ZERO;
		BigDecimal entrata = BigDecimal.ZERO;
		
		for(ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?> eeda : list) {
			if(eeda instanceof ElementoElencoDocumentiAllegatoSpesa) {
				spesa = spesa.add(((SubdocumentoSpesa)eeda.extractSubdocumento()).getImportoDaPagare());
			} else if (eeda instanceof ElementoElencoDocumentiAllegatoEntrata) {
				entrata = entrata.add(((SubdocumentoEntrata)eeda.extractSubdocumento()).getImportoDaIncassare());
			}
		}
		
		result.put("spesa", spesa);
		result.put("entrata", entrata);
		return result;
	}

}
