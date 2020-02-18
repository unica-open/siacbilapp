/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacbilser.model.ModelDetail;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
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
import it.csi.siac.siacgenser.frontend.webservice.msg.ValidaPrimaNota;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoCollegamento;
/**
 * Classe base di model per la consultazione della prima nota integrata.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 08/10/2015
 *
 */
public abstract class ConsultaPrimaNotaIntegrataBaseModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2321894975699517036L;
	
	private PrimaNota primaNota;
	private BigDecimal totaleDare = BigDecimal.ZERO;
	private BigDecimal totaleAvere = BigDecimal.ZERO;
	private List<MovimentoDettaglio> listaMovimentoDettaglio = new ArrayList<MovimentoDettaglio>();
	
	private String tipoMovimento;
	private TipoCollegamento tipoCollegamento;
	private List<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>> listaDatiFinanziari = new ArrayList<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>>();
	private boolean validazioneAbilitata;
	private String datiAccessoriiMovimentoFinanziario;
	
	// SIAC-5242
	private FaseBilancio faseBilancio;
	// SIAC-5853
	private boolean dataRegistrazioneDefinitivaVisibile;

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
		this.totaleDare = totaleDare != null ? totaleDare : BigDecimal.ZERO;
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
		this.totaleAvere = totaleAvere != null ? totaleAvere : BigDecimal.ZERO;
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
		this.listaMovimentoDettaglio = listaMovimentoDettaglio != null ? listaMovimentoDettaglio : new ArrayList<MovimentoDettaglio>();
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
		this.listaDatiFinanziari = listaDatiFinanziari != null ? listaDatiFinanziari : new ArrayList<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>>();
	}

	/**
	 * @return the validazioneAbilitata
	 */
	public boolean isValidazioneAbilitata() {
		return validazioneAbilitata;
	}

	/**
	 * @param validazioneAbilitata the validazioneAbilitata to set
	 */
	public void setValidazioneAbilitata(boolean validazioneAbilitata) {
		this.validazioneAbilitata = validazioneAbilitata;
	}

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
	 * @return the faseBilancio
	 */
	public FaseBilancio getFaseBilancio() {
		return faseBilancio;
	}

	/**
	 * @param faseBilancio the faseBilancio to set
	 */
	public void setFaseBilancio(FaseBilancio faseBilancio) {
		this.faseBilancio = faseBilancio;
	}

	/**
	 * @return the dataRegistrazioneDefinitivaVisibile
	 */
	public boolean isDataRegistrazioneDefinitivaVisibile() {
		return this.dataRegistrazioneDefinitivaVisibile;
	}

	/**
	 * @param dataRegistrazioneDefinitivaVisibile the dataRegistrazioneDefinitivaVisibile to set
	 */
	public void setDataRegistrazioneDefinitivaVisibile(boolean dataRegistrazioneDefinitivaVisibile) {
		this.dataRegistrazioneDefinitivaVisibile = dataRegistrazioneDefinitivaVisibile;
	}

	/**
	 * @return the ambito
	 */
	public abstract Ambito getAmbito();
	
	// CR-4046
	
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
	
	/* **** Requests **** */

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
	 * Crea una request per il servizio di {@link ValidaPrimaNota}.
	 * 
	 * @return la request creata
	 */
	public ValidaPrimaNota creaRequestValidaPrimaNota() {
		ValidaPrimaNota request = creaRequest(ValidaPrimaNota.class);
		getPrimaNota().setAmbito(getAmbito());
		getPrimaNota().setBilancio(getBilancio());
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
