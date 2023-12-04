/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacattser.frontend.webservice.msg.RicercaProvvedimento;
import it.csi.siac.siacattser.model.ric.RicercaAtti;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siaccommon.util.collections.list.SortedSetList;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAttoModelDetail;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;

/**
 * Classe di model per la consultazione dell'allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 15/set/2014
 *
 */
public class ConsultaAllegatoAttoModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -3306826547055777804L;
	
	private AllegatoAtto allegatoAtto;
	private Integer uidAllegatoAtto;
	
	private ElencoDocumentiAllegato elencoDocumentiAllegato;
	private List<ElencoDocumentiAllegato> listaElencoDocumentiAllegato = new SortedSetList<ElencoDocumentiAllegato>(ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
	
	//SIAC-5589
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	
	/** Costruttore vuoto di default */
	public ConsultaAllegatoAttoModel() {
		super();
		setTitolo("Consulta allegato atto");
	}

	/**
	 * @return the allegatoAtto
	 */
	public AllegatoAtto getAllegatoAtto() {
		return allegatoAtto;
	}

	/**
	 * @param allegatoAtto the allegatoAtto to set
	 */
	public void setAllegatoAtto(AllegatoAtto allegatoAtto) {
		this.allegatoAtto = allegatoAtto;
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
	 * @return the elencoDocumentiAllegato
	 */
	public ElencoDocumentiAllegato getElencoDocumentiAllegato() {
		return elencoDocumentiAllegato;
	}

	/**
	 * @param elencoDocumentiAllegato the elencoDocumentiAllegato to set
	 */
	public void setElencoDocumentiAllegato(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		this.elencoDocumentiAllegato = elencoDocumentiAllegato;
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
		this.listaElencoDocumentiAllegato = listaElencoDocumentiAllegato != null ? listaElencoDocumentiAllegato : new SortedSetList<ElencoDocumentiAllegato>(ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
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
		this.listaClasseSoggetto = listaClasseSoggetto;
	}

	/**
	 * @return the totaleEntrataListaElencoDocumentiAllegato
	 */
	public BigDecimal getTotaleEntrataListaElencoDocumentiAllegato() {
		BigDecimal result = BigDecimal.ZERO;
		for(ElencoDocumentiAllegato eda : listaElencoDocumentiAllegato) {
			result = result.add(eda.getTotaleQuoteEntrate());
		}
		return result;
	}
	
	/**
	 * @return the totaleSpesaListaElencoDocumentiAllegato
	 */
	public BigDecimal getTotaleSpesaListaElencoDocumentiAllegato() {
		BigDecimal result = BigDecimal.ZERO;
		for(ElencoDocumentiAllegato eda : listaElencoDocumentiAllegato) {
			result = result.add(eda.getTotaleQuoteSpese());
		}
		return result;
	}
	
	/**
	 * @return the totaleNettoListaElencoDocumentiAllegato
	 */
	public BigDecimal getTotaleNettoListaElencoDocumentiAllegato() {
		// Netto = entrata - spesa
		return getTotaleEntrataListaElencoDocumentiAllegato().subtract(getTotaleSpesaListaElencoDocumentiAllegato());
	}
	
	/**
	 * @return the totaleIncassatoListaElencoDocumentiAllegato
	 */
	public BigDecimal getTotaleIncassatoListaElencoDocumentiAllegato() {
		BigDecimal result = BigDecimal.ZERO;
		for(ElencoDocumentiAllegato eda : listaElencoDocumentiAllegato) {
			result = result.add(eda.getTotaleIncassato());
		}
		return result;
	}
	
	/**
	 * @return the totalePagatoListaElencoDocumentiAllegato
	 */
	public BigDecimal getTotalePagatoListaElencoDocumentiAllegato() {
		BigDecimal result = BigDecimal.ZERO;
		for(ElencoDocumentiAllegato eda : listaElencoDocumentiAllegato) {
			result = result.add(eda.getTotalePagato());
		}
		return result;
	}
	
	/**
	 * @return the convalidato
	 */
	public boolean isConvalidato() {
		return StatoOperativoAllegatoAtto.CONVALIDATO.equals(getAllegatoAtto().getStatoOperativoAllegatoAtto());
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioAllegatoAtto}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioAllegatoAtto creaRequestRicercaDettaglioAllegatoAtto() {
		RicercaDettaglioAllegatoAtto request = creaRequest(RicercaDettaglioAllegatoAtto.class);
		
		AllegatoAtto aa = new AllegatoAtto();
		aa.setUid(getUidAllegatoAtto());
		request.setAllegatoAtto(aa);
		
		request.setAllegatoAttoModelDetails(
			AllegatoAttoModelDetail.IsAssociatoAdUnSubdocumentoConOrdinativo,
			AllegatoAttoModelDetail.DataInizioValiditaStato,
			AllegatoAttoModelDetail.DatiSoggetto,
			AllegatoAttoModelDetail.ElencoDocumentiConPagatoIncassato,
			AllegatoAttoModelDetail.DataCompletamento);

		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaProvvedimento}.
	 * 
	 * @return la request creata
	 */
	public RicercaProvvedimento creaRequestRicercaProvvedimento() {
		RicercaProvvedimento request = creaRequest(RicercaProvvedimento.class);
		
		RicercaAtti ricercaAtti = new RicercaAtti();
		ricercaAtti.setUid(getAllegatoAtto().getAttoAmministrativo().getUid());
		
		request.setRicercaAtti(ricercaAtti);
		request.setEnte(getEnte());
		
		return request;
	}
	
}
