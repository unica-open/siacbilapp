/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * 
 */
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoQuotaPrimaNotaIntegrata;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrata;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ModelDetail;
import it.csi.siac.siacfin2ser.model.AccertamentoModelDetail;
import it.csi.siac.siacfin2ser.model.ImpegnoModelDetail;
import it.csi.siac.siacfin2ser.model.LiquidazioneModelDetail;
import it.csi.siac.siacfin2ser.model.SubAccertamentoModelDetail;
import it.csi.siac.siacfin2ser.model.SubImpegnoModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacgenapp.frontend.ui.util.ElementoMovimentoConsultazionePrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.Rateo;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * Classe di model per la gestione di Ratei e Risconti della prima nota integrata. Modulo GEN
 * 
 * @author Elisa
 * @version 1.0.0 - 15/09/2017
 */
public abstract class GestioneRateiERiscontiPrimaNotaIntegrataDocumentoBaseModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 5865329519773992664L;
	
	private PrimaNota primaNota;
	private BigDecimal totaleDare = BigDecimal.ZERO;
	private BigDecimal totaleAvere = BigDecimal.ZERO;
	
	private List<MovimentoDettaglio> listaMovimentoDettaglio = new ArrayList<MovimentoDettaglio>();
	
	//fields per la consultazione dei movimenti finanziari 
	private String tipoMovimento;
	private TipoCollegamento tipoCollegamento;
	private List<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>> listaDatiFinanziari = new ArrayList<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>>();
	private String datiAccessoriiMovimentoFinanziario;
	
	private List<ElementoScritturaPrimaNotaIntegrata> listaElementoScritturaPerElaborazione = new ArrayList<ElementoScritturaPrimaNotaIntegrata>();
	
	// Liste
	private List<ElementoQuotaPrimaNotaIntegrata> listaElementoQuota = new ArrayList<ElementoQuotaPrimaNotaIntegrata>();
	private Map<Integer,List<ElementoScritturaPrimaNotaIntegrata>> mappaMovimentoEPScritture = new HashMap<Integer, List<ElementoScritturaPrimaNotaIntegrata>>();

	
	private int uidMovimentoEPPerScritture;
	
	private String etichettaAzione;
	private String selettoreAzione;
	
	private Rateo rateoPrimaNota;
	
	private static final String ETICHETTA_INSERIMENTO_RATEO   = "inserisci rateo";
	private static final String ETICHETTA_AGGIORNAMENTO_RATEO = "aggiorna rateo";
	private static final String ATTIVITA_INSERIMENTO_RATEO    = "inserisciRateo";
	private static final String ATTIVITA_AGGIORNAMENTO_RATEO  = "aggiornaRateo";
	private static final String ETICHETTA_RISCONTO            = "risconti";
	
	
	/**
	 * @return the primaNota
	 */
	public PrimaNota getPrimaNota() {
		return primaNota;
	}

	/**
	 * @param primaNota the primaNota to set
	 */
	public void setPrimaNota(PrimaNota primaNota) {
		this.primaNota = primaNota;
	}

	/**
	 * @return the totaleDare
	 */
	public BigDecimal getTotaleDare() {
		return totaleDare;
	}

	/**
	 * @param totaleDare the totaleDare to set
	 */
	public void setTotaleDare(BigDecimal totaleDare) {
		this.totaleDare = totaleDare;
	}

	/**
	 * @return the totaleAvere
	 */
	public BigDecimal getTotaleAvere() {
		return totaleAvere;
	}

	/**
	 * @param totaleAvere the totaleAvere to set
	 */
	public void setTotaleAvere(BigDecimal totaleAvere) {
		this.totaleAvere = totaleAvere;
	}

	/**
	 * @return the listaMovimentoDettaglio
	 */
	public List<MovimentoDettaglio> getListaMovimentoDettaglio() {
		return listaMovimentoDettaglio;
	}

	/**
	 * @param listaMovimentoDettaglio the listaMovimentoDettaglio to set
	 */
	public void setListaMovimentoDettaglio(List<MovimentoDettaglio> listaMovimentoDettaglio) {
		this.listaMovimentoDettaglio = listaMovimentoDettaglio;
	}

	/**
	 * @return the tipoMovimento
	 */
	public String getTipoMovimento() {
		return tipoMovimento;
	}

	/**
	 * @param tipoMovimento the tipoMovimento to set
	 */
	public void setTipoMovimento(String tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	/**
	 * @return the tipoCollegamento
	 */
	public TipoCollegamento getTipoCollegamento() {
		return tipoCollegamento;
	}

	/**
	 * @param tipoCollegamento the tipoCollegamento to set
	 */
	public void setTipoCollegamento(TipoCollegamento tipoCollegamento) {
		this.tipoCollegamento = tipoCollegamento;
	}

	/**
	 * @return the listaDatiFinanziari
	 */
	public List<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>> getListaDatiFinanziari() {
		return listaDatiFinanziari;
	}

	/**
	 * @param listaDatiFinanziari the listaDatiFinanziari to set
	 */
	public void setListaDatiFinanziari(List<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>> listaDatiFinanziari) {
		this.listaDatiFinanziari = listaDatiFinanziari;
	}

	/**
	 * @return the datiAccessoriiMovimentoFinanziario
	 */
	public String getDatiAccessoriiMovimentoFinanziario() {
		return datiAccessoriiMovimentoFinanziario;
	}

	/**
	 * @param datiAccessoriiMovimentoFinanziario the datiAccessoriiMovimentoFinanziario to set
	 */
	public void setDatiAccessoriiMovimentoFinanziario(String datiAccessoriiMovimentoFinanziario) {
		this.datiAccessoriiMovimentoFinanziario = datiAccessoriiMovimentoFinanziario;
	}

	/**
	 * @return the listaElementoScritturaPerElaborazione
	 */
	public List<ElementoScritturaPrimaNotaIntegrata> getListaElementoScritturaPerElaborazione() {
		return listaElementoScritturaPerElaborazione;
	}

	/**
	 * @param listaElementoScritturaPerElaborazione the listaElementoScritturaPerElaborazione to set
	 */
	public void setListaElementoScritturaPerElaborazione(List<ElementoScritturaPrimaNotaIntegrata> listaElementoScritturaPerElaborazione) {
		this.listaElementoScritturaPerElaborazione = listaElementoScritturaPerElaborazione;
	}
	
	/**
	 * @return the listaElementoQuota
	 */
	public List<ElementoQuotaPrimaNotaIntegrata> getListaElementoQuota() {
		return listaElementoQuota;
	}

	/**
	 * @param listaElementoQuota the listaElementoQuota to set
	 */
	public void setListaElementoQuota(List<ElementoQuotaPrimaNotaIntegrata> listaElementoQuota) {
		this.listaElementoQuota = listaElementoQuota;
	}

	/**
	 * @return the uidMovimentoEPPerScritture
	 */
	public int getUidMovimentoEPPerScritture() {
		return uidMovimentoEPPerScritture;
	}

	/**
	 * @param uidMovimentoEPPerScritture the uidMovimentoEPPerScritture to set
	 */
	public void setUidMovimentoEPPerScritture(int uidMovimentoEPPerScritture) {
		this.uidMovimentoEPPerScritture = uidMovimentoEPPerScritture;
	}

	/**
	 * @return the mappaMovimentoEPScritture
	 */
	public Map<Integer, List<ElementoScritturaPrimaNotaIntegrata>> getMappaMovimentoEPScritture() {
		return mappaMovimentoEPScritture;
	}

	/**
	 * @param mappaMovimentoEPScritture the mappaMovimentoEPScritture to set
	 */
	public void setMappaMovimentoEPScritture(
			Map<Integer, List<ElementoScritturaPrimaNotaIntegrata>> mappaMovimentoEPScritture) {
		this.mappaMovimentoEPScritture = mappaMovimentoEPScritture;
	}


	/**
	 * @return the azione
	 */
	public String getEtichettaAzione() {
		return etichettaAzione;
	}

	/**
	 * @param azione the azione to set
	 */
	public void setEtichettaAzione(String azione) {
		this.etichettaAzione = azione;
	}

	/**
	 * @return the selettoreAzione
	 */
	public String getSelettoreAzione() {
		return selettoreAzione;
	}

	/**
	 * @param selettoreAzione the selettoreAzione to set
	 */
	public void setSelettoreAzione(String selettoreAzione) {
		this.selettoreAzione = selettoreAzione;
	}
	
	/**
	 * @return the rateoPrimaNota
	 */
	public Rateo getRateoPrimaNota() {
		return rateoPrimaNota;
	}

	/**
	 * @param rateoPrimaNota the rateoPrimaNota to set
	 */
	public void setRateoPrimaNota(Rateo rateoPrimaNota) {
		this.rateoPrimaNota = rateoPrimaNota;
	}

	//UTILITY
	/**
	 * Prima Nota Libera ha solo un movimentoEP
	 * 
	 * @return the causaleEP
	 */
	private CausaleEP getCausaleEP() {
		return getPrimaNota() != null 
			&& getPrimaNota().getListaMovimentiEP() != null 
			&& !getPrimaNota().getListaMovimentiEP().isEmpty()
			&& getPrimaNota().getListaMovimentiEP().get(0) != null
				? getPrimaNota().getListaMovimentiEP().get(0).getCausaleEP()
				: null;
		
	}
	/**
	 * @return the descrizioneCausale
	 */
	public String getDescrizioneCausale() {
		CausaleEP causaleEP = getCausaleEP();
		StringBuilder sb = new StringBuilder();
		if(causaleEP != null) {
			sb.append(causaleEP.getCodice())
				.append(" - ")
				.append(causaleEP.getDescrizione());
		}
		return sb.toString();
	}
	
	/**
	 * @return the numeroPrimaNota
	 */
	public String getNumeroPrimaNota() {
		if(getPrimaNota() == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
			.append(getPrimaNota().getNumero());
		if(getPrimaNota().getNumeroRegistrazioneLibroGiornale() != null) {
			sb.append(" (definitivo: ")
				.append(getPrimaNota().getNumeroRegistrazioneLibroGiornale())
				.append(")");
		}
		return sb.toString();
	}
	

	/**
	 * Controlla se il movimento sia di tipo specificato
	 * @param movimentoEP il movimento
	 * @param tipiCollegamento i tipi di collegamento
	 * @return <code>true</code> se il movimento &eacute; di tipo specificato; <code>false</code> altrimenti
	 */
	private boolean isMovimentoOfType(MovimentoEP movimentoEP, Iterable<TipoCollegamento> tipiCollegamento) {
		if(movimentoEP == null || movimentoEP.getRegistrazioneMovFin() == null || movimentoEP.getRegistrazioneMovFin().getEvento() == null) {
			return false;
		}
		for(TipoCollegamento tc : tipiCollegamento) {
			if(tc.equals(movimentoEP.getRegistrazioneMovFin().getEvento().getTipoCollegamento())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return the tipoCollegamentoDatiFinanziari
	 */
	public TipoCollegamento getTipoCollegamentoDatiFinanziari() {
		if(getPrimaNota() == null || getPrimaNota().getListaMovimentiEP() == null) {
			return null;
		}
		for(MovimentoEP mep : getPrimaNota().getListaMovimentiEP()) {
			if(mep != null && mep.getRegistrazioneMovFin() != null && mep.getRegistrazioneMovFin().getEvento() != null && mep.getRegistrazioneMovFin().getEvento().getTipoCollegamento() != null) {
				return mep.getRegistrazioneMovFin().getEvento().getTipoCollegamento();
			}
		}
		return null;
	}
	
	
	
	/**
	 * Controlla se i dati finanziari siano abilitati alla consultazione.
	 * <br/>
	 * Tali dati sono attivi solo per tipo evento Accertamento, Documento Entrata, Impegno, Liquidazione, Documento Spesa.
	 * @return the consultazioneDatiFinanziariAbilitata
	 */
	public boolean isConsultazioneDatiFinanziariAbilitata() {
		//getPrimaNota().getListaMovimentiEP().get(0).getRegistrazioneMovFin().getEvento()
		if(getPrimaNota() == null || getPrimaNota().getListaMovimentiEP() == null) {
			return false;
		}
		// SIAC-4467: agigunte le modifiche al movimento di gestione
		Iterable<TipoCollegamento> tipiCollegamento = Arrays.asList(TipoCollegamento.ACCERTAMENTO, TipoCollegamento.SUBACCERTAMENTO,
				TipoCollegamento.IMPEGNO, TipoCollegamento.SUBIMPEGNO, TipoCollegamento.LIQUIDAZIONE,
				TipoCollegamento.DOCUMENTO_ENTRATA, TipoCollegamento.DOCUMENTO_SPESA, TipoCollegamento.SUBDOCUMENTO_ENTRATA, TipoCollegamento.SUBDOCUMENTO_SPESA,
				TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_SPESA, TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA);
		for(MovimentoEP mep : getPrimaNota().getListaMovimentiEP()) {
			if(isMovimentoOfType(mep, tipiCollegamento)) {
				return true;
			}
		}
		
		return false;
	}
	
	//REQUESTS
	
	/**
	 * Gets the ambito.
	 *
	 * @return the ambito
	 */
	protected abstract Ambito getAmbito();
	
	/**
	 * Popola etichetta azione rateo.
	 */
	public void popolaEtichettaAzioneRateo() {
		if(getRateoPrimaNota() != null && getRateoPrimaNota().getUid() != 0) {
			setEtichettaAzione( ETICHETTA_AGGIORNAMENTO_RATEO);
			setSelettoreAzione(ATTIVITA_AGGIORNAMENTO_RATEO);
			return;
		}
		setEtichettaAzione(ETICHETTA_INSERIMENTO_RATEO);
		setSelettoreAzione(ATTIVITA_INSERIMENTO_RATEO);
	}
	
	/**
	 * Popola etichetta azione rateo.
	 */
	public void popolaEtichettaAzioneRisconti() {
		setEtichettaAzione(ETICHETTA_RISCONTO);
		setSelettoreAzione(null);
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioPrimaNota}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioPrimaNota creaRequestRicercaDettaglioPrimaNotaLibera() {
		RicercaDettaglioPrimaNota request = creaRequest(RicercaDettaglioPrimaNota.class);
		
		getPrimaNota().setAmbito(getAmbito());
		request.setPrimaNota(getPrimaNota());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link OttieniEntitaCollegatePrimaNota}.
	 * @return la request creata
	 */
	public OttieniEntitaCollegatePrimaNota creaRequestOttieniEntitaCollegatePrimaNota() {
		OttieniEntitaCollegatePrimaNota req = creaRequest(OttieniEntitaCollegatePrimaNota.class);
		
		req.setPrimaNota(getPrimaNota());
		req.setTipoCollegamento(getTipoCollegamento());
		req.setModelDetails(ottieniModelDetailPerTipoCollegamento());
		req.setParametriPaginazione(creaParametriPaginazione());
		
		return req;
	}

	/**
	 * Calcola i model detail per il tipo di collegamento selezionato
	 * @return i model detail da utilizzare per il tipo di collegamento
	 */
	private ModelDetail[] ottieniModelDetailPerTipoCollegamento() {
		Map<TipoCollegamento, ModelDetail[]> details = new HashMap<TipoCollegamento, ModelDetail[]>();
		details.put(TipoCollegamento.IMPEGNO, new ModelDetail[] {ImpegnoModelDetail.Soggetto, ImpegnoModelDetail.PianoDeiConti});
		details.put(TipoCollegamento.SUBIMPEGNO, new ModelDetail[] {SubImpegnoModelDetail.Soggetto, SubImpegnoModelDetail.PianoDeiConti, SubImpegnoModelDetail.Padre});
		details.put(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_SPESA, new ModelDetail[0]);
		
		details.put(TipoCollegamento.ACCERTAMENTO, new ModelDetail[] {AccertamentoModelDetail.Soggetto, AccertamentoModelDetail.PianoDeiConti});
		details.put(TipoCollegamento.SUBACCERTAMENTO, new ModelDetail[] {SubAccertamentoModelDetail.Soggetto, SubAccertamentoModelDetail.PianoDeiConti, SubAccertamentoModelDetail.Padre});
		details.put(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA, new ModelDetail[0]);
		
		details.put(TipoCollegamento.LIQUIDAZIONE, new ModelDetail[] {LiquidazioneModelDetail.Impegno, LiquidazioneModelDetail.Soggetto, LiquidazioneModelDetail.PianoDeiConti});
		
		details.put(TipoCollegamento.SUBDOCUMENTO_SPESA, new ModelDetail[] {SubdocumentoSpesaModelDetail.TestataDocumento, SubdocumentoSpesaModelDetail.ImpegnoSubimpegnoPdc, SubdocumentoSpesaModelDetail.Liquidazione, SubdocumentoSpesaModelDetail.Ordinativo});
		details.put(TipoCollegamento.SUBDOCUMENTO_ENTRATA, new ModelDetail[] {SubdocumentoEntrataModelDetail.TestataDocumento,SubdocumentoEntrataModelDetail.AccertamentoSubaccertamentoPdc, SubdocumentoEntrataModelDetail.Ordinativo});
		
		return details.get(getTipoCollegamento());
	}

	
	
}
