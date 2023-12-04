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

import it.csi.siac.siaccommon.util.collections.list.SortedSetList;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorElementoElencoDocumentiAllegato;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorElencoDocumentiAllegato;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RiCompletaAllegatoAttoPerElenchi;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;

/**
 * Classe di model per riportare l'AllegatoAtto allo stato completato.
 * 
 * @author Nazha Ahmad
 * @version 1.0.0 - 10/03/2016
 */
public class RiCompletaAllegatoAttoModel extends GenericAllegatoAttoModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -8690633760980886401L;
	
	private Boolean disabledButtons = Boolean.FALSE;
	
	// Elenchi
	private BigDecimal totaleElenchi = BigDecimal.ZERO;
	private BigDecimal totaleSpeseRiCompletabili = BigDecimal.ZERO;
	private BigDecimal totaleEntrateRiCompletabili = BigDecimal.ZERO;
	private BigDecimal validatoSpeseRiCompletabili = BigDecimal.ZERO;
	private BigDecimal validatoEntrateRiCompletabili = BigDecimal.ZERO;
	
	private BigDecimal totaleSpeseNonElaborabili = BigDecimal.ZERO;
	private BigDecimal totaleEntrateNonElaborabili = BigDecimal.ZERO;
	private BigDecimal validatoSpeseNonElaborabili = BigDecimal.ZERO;
	private BigDecimal validatoEntrateNonElaborabili = BigDecimal.ZERO;
	
	// Subdocumento
	private BigDecimal totaleSpesaDaConvalidareSubdocumenti = BigDecimal.ZERO;
	private BigDecimal totaleSpesaConvalidateSubdocumenti = BigDecimal.ZERO;
	private BigDecimal totaleSpesaACoperturaSubdocumenti = BigDecimal.ZERO;
	
	private BigDecimal totaleEntrataDaConvalidareSubdocumenti = BigDecimal.ZERO;
	private BigDecimal totaleEntrataConvalidateSubdocumenti = BigDecimal.ZERO;
	private BigDecimal totaleEntrataACoperturaSubdocumenti = BigDecimal.ZERO;
	
	private List<ElencoDocumentiAllegato> listaElencoDocumentiAllegato = new SortedSetList<ElencoDocumentiAllegato>(ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
	private List<ElencoDocumentiAllegato> listaRiCompletabili = new SortedSetList<ElencoDocumentiAllegato>(ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
	private List<ElencoDocumentiAllegato> listaNonElaborabili = new SortedSetList<ElencoDocumentiAllegato>(ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
	
	private List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaSubdocumentiRiCompletabili =
			new SortedSetList<ElementoElencoDocumentiAllegato<?,?,?,?,?>>(ComparatorElementoElencoDocumentiAllegato.INSTANCE_ASC);
	private List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaSubdocumentiConvalidati =
			new SortedSetList<ElementoElencoDocumentiAllegato<?,?,?,?,?>>(ComparatorElementoElencoDocumentiAllegato.INSTANCE_ASC);
	private List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaSubdocumentiACopertura =
			new SortedSetList<ElementoElencoDocumentiAllegato<?,?,?,?,?>>(ComparatorElementoElencoDocumentiAllegato.INSTANCE_ASC);
	
	private List<Integer> listaUid = new ArrayList<Integer>();
	
	/** Costruttore vuoto di default */
	public RiCompletaAllegatoAttoModel() {
		setTitolo("Valutazione allegati agli atti");
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
	 * @return the totaleSpeseRiCompletabili
	 */
	public BigDecimal getTotaleSpeseRiCompletabili() {
		return totaleSpeseRiCompletabili;
	}

	/**
	 * @param totaleSpeseRiCompletabili the totaleSpeseRiCompletabili to set
	 */
	public void setTotaleSpeseRiCompletabili(BigDecimal totaleSpeseRiCompletabili) {
		this.totaleSpeseRiCompletabili = totaleSpeseRiCompletabili != null ? totaleSpeseRiCompletabili : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleEntrateRiCompletabili
	 */
	public BigDecimal getTotaleEntrateRiCompletabili() {
		return totaleEntrateRiCompletabili;
	}

	/**
	 * @param totaleEntrateRiCompletabili the totaleEntrateRiCompletabili to set
	 */
	public void setTotaleEntrateRiCompletabili(BigDecimal totaleEntrateRiCompletabili) {
		this.totaleEntrateRiCompletabili = totaleEntrateRiCompletabili != null ? totaleEntrateRiCompletabili : BigDecimal.ZERO;
	}

	/**
	 * @return the validatoSpeseRiCompletabili
	 */
	public BigDecimal getValidatoSpeseRiCompletabili() {
		return validatoSpeseRiCompletabili;
	}

	/**
	 * @param validatoSpeseRiCompletabili the validatoSpeseRiCompletabili to set
	 */
	public void setValidatoSpeseRiCompletabili(
			BigDecimal validatoSpeseRiCompletabili) {
		this.validatoSpeseRiCompletabili = validatoSpeseRiCompletabili;
	}

	/**
	 * @return the validatoEntrateRiCompletabili
	 */
	public BigDecimal getValidatoEntrateRiCompletabili() {
		return validatoEntrateRiCompletabili;
	}

	/**
	 * @param validatoEntrateRiCompletabili the validatoEntrateRiCompletabili to set
	 */
	public void setValidatoEntrateRiCompletabili(
			BigDecimal validatoEntrateRiCompletabili) {
		this.validatoEntrateRiCompletabili = validatoEntrateRiCompletabili;
	}

	/**
	 * @return the totaleSpeseNonElaborabili
	 */
	public BigDecimal getTotaleSpeseNonElaborabili() {
		return totaleSpeseNonElaborabili;
	}

	/**
	 * @param totaleSpeseNonElaborabili the totaleSpeseNonElaborabili to set
	 */
	public void setTotaleSpeseNonElaborabili(BigDecimal totaleSpeseNonElaborabili) {
		this.totaleSpeseNonElaborabili = totaleSpeseNonElaborabili != null ? totaleSpeseNonElaborabili : BigDecimal.ZERO;
	}

	/**
	 * @return the totaleEntrateNonElaborabili
	 */
	public BigDecimal getTotaleEntrateNonElaborabili() {
		return totaleEntrateNonElaborabili;
	}

	/**
	 * @param totaleEntrateNonElaborabili the totaleEntrateNonElaborabili to set
	 */
	public void setTotaleEntrateNonElaborabili(BigDecimal totaleEntrateNonElaborabili) {
		this.totaleEntrateNonElaborabili = totaleEntrateNonElaborabili != null ? totaleEntrateNonElaborabili : BigDecimal.ZERO;
	}

	/**
	 * @return the validatoSpeseNonElaborabili
	 */
	public BigDecimal getValidatoSpeseNonElaborabili() {
		return validatoSpeseNonElaborabili;
	}

	/**
	 * @param validatoSpeseNonElaborabili the validatoSpeseNonElaborabili to set
	 */
	public void setValidatoSpeseNonElaborabili(
			BigDecimal validatoSpeseNonElaborabili) {
		this.validatoSpeseNonElaborabili = validatoSpeseNonElaborabili;
	}

	/**
	 * @return the validatoEntrateNonElaborabili
	 */
	public BigDecimal getValidatoEntrateNonElaborabili() {
		return validatoEntrateNonElaborabili;
	}

	/**
	 * @param validatoEntrateNonElaborabili the validatoEntrateNonElaborabili to set
	 */
	public void setValidatoEntrateNonElaborabili(
			BigDecimal validatoEntrateNonElaborabili) {
		this.validatoEntrateNonElaborabili = validatoEntrateNonElaborabili;
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
	 * @return the listaRiCompletabili
	 */
	public List<ElencoDocumentiAllegato> getListaRiCompletabili() {
		return listaRiCompletabili;
	}

	/**
	 * @param listaRiCompletabili the listaRiCompletabili to set
	 */
	public void setListaRiCompletabili(List<ElencoDocumentiAllegato> listaRiCompletabili) {
		this.listaRiCompletabili = new SortedSetList<ElencoDocumentiAllegato>(
			listaRiCompletabili != null ? listaRiCompletabili : new ArrayList<ElencoDocumentiAllegato>(),
			ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
	}

	/**
	 * @return the listaNonElaborabili
	 */
	public List<ElencoDocumentiAllegato> getListaNonElaborabili() {
		return listaNonElaborabili;
	}

	/**
	 * @param listaNonElaborabili the listaNonElaborabili to set
	 */
	public void setListaNonElaborabili(List<ElencoDocumentiAllegato> listaNonElaborabili) {
		this.listaNonElaborabili = new SortedSetList<ElencoDocumentiAllegato>(
			listaNonElaborabili != null ? listaNonElaborabili : new ArrayList<ElencoDocumentiAllegato>(),
			ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
	}

	/**
	 * @return the listaSubdocumentiRiCompletabili
	 */
	public List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> getListaSubdocumentiRiCompletabili() {
		return listaSubdocumentiRiCompletabili;
	}

	/**
	 * @param listaSubdocumentiRiCompletabili the listaSubdocumentiRiCompletabili to set
	 */
	public void setListaSubdocumentiRiCompletabili(List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaSubdocumentiRiCompletabili) {
		this.listaSubdocumentiRiCompletabili = new SortedSetList<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>>(
			listaSubdocumentiRiCompletabili != null ? listaSubdocumentiRiCompletabili : new ArrayList<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>>(),
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
	 * @return the listaSubdocumentiNonElaborabili
	 */
	public List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> getListaSubdocumentiNonElaborabili() {
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
		this.listaUid = listaUid != null ? listaUid : new ArrayList<Integer>();
	}

	/**
	 * @return the descrizioneRiCompletaAttoAmministrativo
	 */
	public String getDescrizioneRiCompletaAttoAmministrativo() {
		return getAttoAmministrativo() == null ? "" : (": " + computeDescrizioneCompletaAttoAmministrativo(getAttoAmministrativo()));
	}
	
	/**
	 * @return the descrizioneCompletaAllegatoAtto
	 * 
	 * @version 1.1 04/04/2016 CR-3206 AHMAD
	 */
	public String getDescrizioneRiCompletaAllegatoAtto() {
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
	 * @return the totaleSpesaNonElaborabiliSubdocumenti
	 */
	public BigDecimal getTotaleSpesaNonElaborabiliSubdocumenti() {
		return getTotaleSpesaConvalidateSubdocumenti().add(getTotaleSpesaACoperturaSubdocumenti());
	}
	
	/**
	 * @return the totaleEntrataNonElaborabiliSubdocumenti
	 */
	public BigDecimal getTotaleEntrataNonElaborabiliSubdocumenti() {
		return getTotaleEntrataConvalidateSubdocumenti().add(getTotaleEntrataACoperturaSubdocumenti());
	}
	
	/**
	 * @return the totaleSpesaSubdocumenti
	 */
	public BigDecimal getTotaleSpesaSubdocumenti() {
		return getTotaleSpesaDaConvalidareSubdocumenti().add(getTotaleSpesaNonElaborabiliSubdocumenti());
	}
	
	/**
	 * @return the totaleEntrataSubdocumenti
	 */
	public BigDecimal getTotaleEntrataSubdocumenti() {
		return getTotaleEntrataDaConvalidareSubdocumenti().add(getTotaleEntrataNonElaborabiliSubdocumenti());
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
		
		request.setAllegatoAtto(allegatoAtto);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio {@link RiCompletaAllegatoAttoPerElenchi}.
	 * 
	 * @return la request creata
	 */
	public RiCompletaAllegatoAttoPerElenchi creaRequestRiCompletaAllegatoAttoPerElenchi() {
		RiCompletaAllegatoAttoPerElenchi request = creaRequest(RiCompletaAllegatoAttoPerElenchi.class);
		
		// Creo l'allegato per la request
		AllegatoAtto aa = new AllegatoAtto();

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
		request.setAllegatoAtto(aa);

		return request;
	}

	
	/* **** Utilities **** */
	
	/**
	 * Calcola i totali degli elenchi.
	 */
	public void computeTotali() {
		Map<String, BigDecimal> totaliRiCompletabili = computeTotali(getListaRiCompletabili());
		Map<String, BigDecimal> totaliNonElaborabili = computeTotali(getListaNonElaborabili());
		
		setTotaleSpeseRiCompletabili(totaliRiCompletabili.get("totaleSpese"));
		setTotaleEntrateRiCompletabili(totaliRiCompletabili.get("totaleEntrate"));
		setValidatoSpeseRiCompletabili(totaliRiCompletabili.get("validatoSpese"));
		setValidatoEntrateRiCompletabili(totaliRiCompletabili.get("validatoEntrate"));
		
		setTotaleSpeseNonElaborabili(totaliNonElaborabili.get("totaleSpese"));
		setTotaleEntrateNonElaborabili(totaliNonElaborabili.get("totaleEntrate"));
		setValidatoSpeseNonElaborabili(totaliNonElaborabili.get("validatoSpese"));
		setValidatoEntrateNonElaborabili(totaliNonElaborabili.get("validatoEntrate"));
		
		BigDecimal totaleDegliElenchi = getTotaleSpeseRiCompletabili().add(getTotaleEntrateRiCompletabili())
				.add(getTotaleSpeseNonElaborabili()).add(getTotaleEntrateNonElaborabili());
		
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
		BigDecimal validatoSpese = BigDecimal.ZERO;
		BigDecimal validatoEntrate = BigDecimal.ZERO;
		
		for(ElencoDocumentiAllegato eda : list) {
			totaleSpese = totaleSpese.add(eda.getTotaleQuoteSpese());
			totaleEntrate = totaleEntrate.add(eda.getTotaleQuoteEntrate());
			validatoSpese = validatoSpese.add(eda.getTotaleDaPagareNotNull());
			validatoEntrate = validatoEntrate.add(eda.getTotaleDaIncassareNotNull());
		}
		
		result.put("totaleSpese", totaleSpese);
		result.put("totaleEntrate", totaleEntrate);
		result.put("validatoSpese", validatoSpese);
		result.put("validatoEntrate", validatoEntrate);
		
		return result;
	}

}
