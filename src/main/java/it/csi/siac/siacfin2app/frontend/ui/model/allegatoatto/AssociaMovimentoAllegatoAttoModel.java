/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siaccorser.util.comparator.ComparatorUtils;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiTesoreria;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoElencoDocumenti;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.mutuo.VoceMutuo;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.ric.RicercaProvvisorioDiCassaK;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;

/**
 * Classe di model per l'associazione tra movimento di gestione ed allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/set/2014
 * @version 1.0.1 - 30/set/2014 - aggiunta classe base
 */
	
public class AssociaMovimentoAllegatoAttoModel extends GenericAssociaAllegatoAttoModel {
	/** Per la serializzazione */
	private static final long serialVersionUID = 5706397707187809937L;
	
	private List<SubdocumentoSpesa> listaSubdocumentoSpesa = new ArrayList<SubdocumentoSpesa>();
	private List<SubdocumentoEntrata> listaSubdocumentoEntrata = new ArrayList<SubdocumentoEntrata>();
	private List<ContoTesoreria> listaContoTesoreria = new ArrayList<ContoTesoreria>();
	
	private SubdocumentoSpesa subdocumentoSpesa;
	private SubdocumentoEntrata subdocumentoEntrata;
	private ContoTesoreria contoTesoreria;

	private VoceMutuo voceMutuo;
	
	private Integer row;
	
	private Integer uidMovimento;
	private BigDecimal totaleGiaImpegnato;
	private BigDecimal numeroSubmovimento;
	
	// SIAC-5311 SIOPE+
	private List<SiopeAssenzaMotivazione> listaSiopeAssenzaMotivazione = new ArrayList<SiopeAssenzaMotivazione>();

	/** Costruttore vuoto di default */
	public AssociaMovimentoAllegatoAttoModel() {
		super();
		setTitolo("Associa movimenti allegato atto");
	}

	/**
	 * @return the listaSubdocumentoSpesa
	 */
	public List<SubdocumentoSpesa> getListaSubdocumentoSpesa() {
		return listaSubdocumentoSpesa != null ? listaSubdocumentoSpesa: new ArrayList<SubdocumentoSpesa>();
	}

	/**
	 * @param listaSubdocumentoSpesa the listaSubdocumentoSpesa to set
	 */
	public void setListaSubdocumentoSpesa(List<SubdocumentoSpesa> listaSubdocumentoSpesa) {
		this.listaSubdocumentoSpesa = listaSubdocumentoSpesa;
	}

	/**
	 * @return the listaSubdocumentoEntrata
	 */
	public List<SubdocumentoEntrata> getListaSubdocumentoEntrata() {
		return listaSubdocumentoEntrata != null ? listaSubdocumentoEntrata: new ArrayList<SubdocumentoEntrata>();
	}

	/**
	 * @param listaSubdocumentoEntrata the listaSubdocumentoEntrata to set
	 */
	public void setListaSubdocumentoEntrata(List<SubdocumentoEntrata> listaSubdocumentoEntrata) {
		this.listaSubdocumentoEntrata = listaSubdocumentoEntrata;
	}
	
	/**
	 * @return listaContoTesoreria the listaContoTesoreria to set
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
	 * @return the subdocumentoSpesa
	 */
	public SubdocumentoSpesa getSubdocumentoSpesa() {
		return subdocumentoSpesa;
	}

	/**
	 * @param subdocumentoSpesa the subdocumentoSpesa to set
	 */
	public void setSubdocumentoSpesa(SubdocumentoSpesa subdocumentoSpesa) {
		this.subdocumentoSpesa = subdocumentoSpesa;
	}

	/**
	 * @return the subdocumentoEntrata
	 */
	public SubdocumentoEntrata getSubdocumentoEntrata() {
		return subdocumentoEntrata;
	}

	/**
	 * @param subdocumentoEntrata the subdocumentoEntrata to set
	 */
	public void setSubdocumentoEntrata(SubdocumentoEntrata subdocumentoEntrata) {
		this.subdocumentoEntrata = subdocumentoEntrata;
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
	 * @return the voceMutuo
	 */
	public VoceMutuo getVoceMutuo() {
		return voceMutuo;
	}

	/**
	 * @param voceMutuo the voceMutuo to set
	 */
	public void setVoceMutuo(VoceMutuo voceMutuo) {
		this.voceMutuo = voceMutuo;
	}

	/**
	 * @return the row
	 */
	public Integer getRow() {
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(Integer row) {
		this.row = row;
	}
	
	/**
	 * @return the uidMovimento
	 */
	public Integer getUidMovimento() {
		return uidMovimento;
	}

	/**
	 * @param uidMovimento the uidMovimento to set
	 */
	public void setUidMovimento(Integer uidMovimento) {
		this.uidMovimento = uidMovimento;
	}
	
	/**
	 * @return the totaleGiaImpegnato
	 */
	public BigDecimal getTotaleGiaImpegnato() {
		return totaleGiaImpegnato;
	}

	/**
	 * @param totaleGiaImpegnato the totaleGiaImpegnato to set
	 */
	public void setTotaleGiaImpegnato(BigDecimal totaleGiaImpegnato) {
		this.totaleGiaImpegnato = totaleGiaImpegnato;
	}
	
	/**
	 * @return the numeroSubmovimento
	 */
	public BigDecimal getNumeroSubmovimento() {
		return numeroSubmovimento;
	}

	/**
	 * @param numeroSubmovimento the numeroSubmovimento to set
	 */
	public void setNumeroSubmovimento(BigDecimal numeroSubmovimento) {
		this.numeroSubmovimento = numeroSubmovimento;
	}

	/**
	 * @return the listaSiopeAssenzaMotivazione
	 */
	public List<SiopeAssenzaMotivazione> getListaSiopeAssenzaMotivazione() {
		return listaSiopeAssenzaMotivazione;
	}

	/**
	 * @param listaSiopeAssenzaMotivazione the listaSiopeAssenzaMotivazione to set
	 */
	public void setListaSiopeAssenzaMotivazione(List<SiopeAssenzaMotivazione> listaSiopeAssenzaMotivazione) {
		this.listaSiopeAssenzaMotivazione = listaSiopeAssenzaMotivazione != null ? listaSiopeAssenzaMotivazione : new ArrayList<SiopeAssenzaMotivazione>();
	}

	/**
	 * Restituisce il totale degli impegni da associare.
	 * 
	 * @return the totaleSpesa
	 */
	public BigDecimal getTotaleSpesa() {
		BigDecimal total = BigDecimal.ZERO;
		for(SubdocumentoSpesa ss : getListaSubdocumentoSpesa()) {
			total = total.add(ss.getImporto());
		}
		return total;
	}
	
	/**
	 * Restituisce il totale degli accertamenti da associare.
	 * 
	 * @return the totaleEntrata
	 */
	public BigDecimal getTotaleEntrata() {
		BigDecimal total = BigDecimal.ZERO;
		for(SubdocumentoEntrata se : getListaSubdocumentoEntrata()) {
			total = total.add(se.getImporto());
		}
		return total;
	}
	
	/**
	 * Ottiene la descrizione completa del soggetto per la visualizzazione in pagina.
	 * 
	 * @return the descrizioneCompletaSoggetto
	 */
	public String getDescrizioneCompletaSoggetto() {
		return computeDescrizioneCompletaSoggetto(getSoggetto());
	}
	
	/**
	 * Aggiungi il subdocumento spesa presente nelmode alla lista dei subdocumenti spesa del model.s
	 */
	public void aggiungiSubdocumentoSpesaAllaLista() {
		Impegno movimentoGestione = getSubdocumentoSpesa().getSubImpegno() != null && getSubdocumentoSpesa().getSubImpegno().getNumero() != null ? getSubdocumentoSpesa().getSubImpegno() : getSubdocumentoSpesa().getImpegno();
		getSubdocumentoSpesa().setSiopeTipoDebito(movimentoGestione.getSiopeTipoDebito());
		SiopeAssenzaMotivazione sam = ComparatorUtils.searchByUid(getListaSiopeAssenzaMotivazione(), getSubdocumentoSpesa().getSiopeAssenzaMotivazione());
		getSubdocumentoSpesa().setSiopeAssenzaMotivazione(sam);
		getListaSubdocumentoSpesa().add(0, getSubdocumentoSpesa());
	}
	
	/* **** Request **** */
	
	@Override
	protected void popolaElencoDocumentiAllegato(ElencoDocumentiAllegato elencoDocumentiAllegato) {
		elencoDocumentiAllegato.setAnno(getAnnoEsercizioInt());
		elencoDocumentiAllegato.setEnte(getEnte());
		elencoDocumentiAllegato.setStatoOperativoElencoDocumenti(StatoOperativoElencoDocumenti.BOZZA);
		// Subdocumenti relativi all'elenco
		popolamentoSoggettoMovimentiGestione();
		elencoDocumentiAllegato.getSubdocumenti().addAll(getListaSubdocumentoSpesa());
		elencoDocumentiAllegato.getSubdocumenti().addAll(getListaSubdocumentoEntrata());
	}

	/**
	 * Popolamento del soggetto negli eventuali movimenti di gestione che non hanno il dato.
	 * <br/>
	 * Questo perch&eacute; il servizio deve inserire dei documenti e recupera il soggetto a partire dal movimento di gestione.
	 */
	private void popolamentoSoggettoMovimentiGestione() {
		// Spesa
		for(SubdocumentoSpesa ss : getListaSubdocumentoSpesa()) {
			if(ss.getImpegno().getSoggetto() == null || ss.getImpegno().getSoggetto().getUid() == 0) {
				ss.getImpegno().setSoggetto(getSoggetto());
				ss.setModalitaPagamentoSoggetto(getModalitaPagamentoSoggetto());
			}
		}
		// Entrata
		for(SubdocumentoEntrata se : getListaSubdocumentoEntrata()) {
			if(se.getAccertamento().getSoggetto() == null || se.getAccertamento().getSoggetto().getUid() == 0) {
				se.getAccertamento().setSoggetto(getSoggetto());
			}
		}
	}

	/**
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiaveOttimizzato}.
	 * 
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato() {
		RicercaAccertamentoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaAccertamentoPerChiaveOttimizzato.class, 1);
		request.setEnte(getEnte());
		//carico i sub solo se ho il numero del sub valorizzato
		request.setCaricaSub(getSubdocumentoEntrata() != null && getSubdocumentoEntrata().getSubAccertamento() != null && getSubdocumentoEntrata().getSubAccertamento().getNumero() != null);
		request.setSubPaginati(true);
		
		if(getSubdocumentoEntrata() != null && getSubdocumentoEntrata().getAccertamento() != null) {
			RicercaAccertamentoK prak = new RicercaAccertamentoK();
			prak.setAnnoEsercizio(getAnnoEsercizioInt());
			prak.setAnnoAccertamento(Integer.valueOf(getSubdocumentoEntrata().getAccertamento().getAnnoMovimento()));
			prak.setNumeroAccertamento(getSubdocumentoEntrata().getAccertamento().getNumero());
			prak.setNumeroSubDaCercare(getSubdocumentoEntrata().getSubAccertamento() != null ? getSubdocumentoEntrata().getSubAccertamento().getNumero() : null);
			request.setpRicercaAccertamentoK(prak);
		}
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		//Non richiedo NESSUN importo derivato del capitolo
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class)); 

		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiaveOttimizzato}.
	 * 
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato() {
		RicercaImpegnoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaImpegnoPerChiaveOttimizzato.class, 1);
		request.setEnte(getEnte());
		//carico i sub solo se ho il numero del sub valorizzato
		request.setCaricaSub(getSubdocumentoSpesa() != null && getSubdocumentoSpesa().getSubImpegno() != null && getSubdocumentoSpesa().getSubImpegno().getNumero() != null);
		request.setSubPaginati(true);
		
		if(getSubdocumentoSpesa() != null && getSubdocumentoSpesa().getImpegno() != null) {
			RicercaImpegnoK prik = new RicercaImpegnoK();
			prik.setAnnoEsercizio(getAnnoEsercizioInt());
			prik.setAnnoImpegno(Integer.valueOf(getSubdocumentoSpesa().getImpegno().getAnnoMovimento()));
			prik.setNumeroImpegno(getSubdocumentoSpesa().getImpegno().getNumero());
			prik.setNumeroSubDaCercare(getSubdocumentoSpesa().getSubImpegno() != null ? getSubdocumentoSpesa().getSubImpegno().getNumero() : null);
			request.setpRicercaImpegnoK(prik);
		}
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		// Non richiedo NESSUN importo derivato.
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		datiOpzionaliCapitoli.setTipologieClassificatoriRichiesti(EnumSet.noneOf(TipologiaClassificatore.class));
		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaProvvisorioDiCassaPerChiave}.
	 * @param provvisorio il provvisorio da cercare
	 * 
	 * @return la request creata
	 */
	public RicercaProvvisorioDiCassaPerChiave creaRequestProvvisorioCassa(ProvvisorioDiCassa provvisorio) {
		RicercaProvvisorioDiCassaPerChiave request = new RicercaProvvisorioDiCassaPerChiave();
		
		request.setBilancio(getBilancio());
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		RicercaProvvisorioDiCassaK pRicercaProvvisorioK = new RicercaProvvisorioDiCassaK();
		pRicercaProvvisorioK.setAnnoProvvisorioDiCassa(provvisorio.getAnno());
		pRicercaProvvisorioK.setNumeroProvvisorioDiCassa(provvisorio.getNumero());
		pRicercaProvvisorioK.setTipoProvvisorioDiCassa(provvisorio.getTipoProvvisorioDiCassa());
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
