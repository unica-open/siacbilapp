/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegrata.aggiorna;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoScritturaPrimaNotaIntegrata;
import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaCodifiche;
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
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaConto;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.OperazioneSegnoConto;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoCollegamento;
/**
 * Classe base di model per la consultazione della prima nota integrata.
 * 
 * @author Elisa Chiari
 * @version 1.0.0 - 14/03/2017
 *
 */
public abstract class BaseAggiornaPrimaNotaIntegrataBaseModel extends GenericBilancioModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -3458635907574365086L;
	private PrimaNota primaNota;
	private BigDecimal totaleDare = BigDecimal.ZERO;
	private BigDecimal totaleAvere = BigDecimal.ZERO;
	
	//fields per la consultazione dei movimenti finanziari 
	private String tipoMovimento;
	private TipoCollegamento tipoCollegamento;
	private List<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>> listaDatiFinanziari = new ArrayList<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>>();
	private String datiAccessoriiMovimentoFinanziario;
	
	private List<ElementoScritturaPrimaNotaIntegrata> listaElementoScritturaPerElaborazione = new ArrayList<ElementoScritturaPrimaNotaIntegrata>();
	
	
	/*da modale*/
	private Integer indiceConto;
	private BigDecimal importo;
	private OperazioneSegnoConto operazioneSegnoConto;
	/*da collapse*/
	private Conto conto;
	private BigDecimal importoCollapse;
	private OperazioneSegnoConto operazioneSegnoContoCollapse;
	
	
	/* modale compilazione guidata conto*/
	private List<ClassePiano> listaClassi = new ArrayList<ClassePiano>();
	
	private boolean showPulsanteAggiornamento = true;
	private boolean proseguiImportiNonCongruenti = false;
	
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
	 * @return the indiceConto
	 */
	public Integer getIndiceConto() {
		return indiceConto;
	}

	/**
	 * @param indiceConto the indiceConto to set
	 */
	public void setIndiceConto(Integer indiceConto) {
		this.indiceConto = indiceConto;
	}

	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		return importo;
	}

	/**
	 * @param importo the importo to set
	 */
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	/**
	 * @return the operazioneSegnoConto
	 */
	public OperazioneSegnoConto getOperazioneSegnoConto() {
		return operazioneSegnoConto;
	}

	/**
	 * @param operazioneSegnoConto the operazioneSegnoConto to set
	 */
	public void setOperazioneSegnoConto(OperazioneSegnoConto operazioneSegnoConto) {
		this.operazioneSegnoConto = operazioneSegnoConto;
	}

	/**
	 * @return conto
	 */
	public Conto getConto() {
		return conto;
	}

	/**
	 * @param conto the conto to set
	 */
	public void setConto(Conto conto) {
		this.conto = conto;
	}

	/**
	 * @return the importoCollapse
	 */
	public BigDecimal getImportoCollapse() {
		return importoCollapse;
	}

	/**
	 * @param importoCollapse the importoCollapse
	 */
	public void setImportoCollapse(BigDecimal importoCollapse) {
		this.importoCollapse = importoCollapse;
	}

	/**
	 * @return the operazioneSegnoContoCollapse
	 */
	public OperazioneSegnoConto getOperazioneSegnoContoCollapse() {
		return operazioneSegnoContoCollapse;
	}

	/**
	 * @param operazioneSegnoContoCollapse the operazioneSegnoContoCollapse to set
	 */
	public void setOperazioneSegnoContoCollapse(OperazioneSegnoConto operazioneSegnoContoCollapse) {
		this.operazioneSegnoContoCollapse = operazioneSegnoContoCollapse;
	}

	
	/**
	 * @return the listaClassi
	 */
	public List<ClassePiano> getListaClassi() {
		return listaClassi;
	}

	/**
	 * @param listaClassi the listaClassi to set
	 */
	public void setListaClassi(List<ClassePiano> listaClassi) {
		this.listaClassi = listaClassi;
	}
	
	/**
	 * @return the showPulsanteAggiornamento
	 */
	public boolean isShowPulsanteAggiornamento() {
		return showPulsanteAggiornamento;
	}

	/**
	 * @param showPulsanteAggiornamento the showPulsanteAggiornamento to set
	 */
	public void setShowPulsanteAggiornamento(boolean showPulsanteAggiornamento) {
		this.showPulsanteAggiornamento = showPulsanteAggiornamento;
	}
	
	/**
	 * @return the proseguiImportiNonCongruenti
	 */
	public boolean isProseguiImportiNonCongruenti() {
		return this.proseguiImportiNonCongruenti;
	}

	/**
	 * @param proseguiImportiNonCongruenti the proseguiImportiNonCongruenti to set
	 */
	public void setProseguiImportiNonCongruenti(boolean proseguiImportiNonCongruenti) {
		this.proseguiImportiNonCongruenti = proseguiImportiNonCongruenti;
	}

	/**
	 * @return the ambito
	 */
	public abstract Ambito getAmbito();
	
	/**
	 * @return the azioneAggiornamento
	 */
	public abstract  String getAzioneAggiornamento();
	
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
	
	/**
	 * Ottiene la lista dei movimenti con l'ambito corretto
	 * @return la lista dei movimenti
	 */
	public abstract List<MovimentoEP> ottieniListaMovimentiEPConAmbito();
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioPrimaNota}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioPrimaNota creaRequestRicercaDettaglioPrimaNota() {
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
	
	//SIAC-4524
	/**
	 * Crea una request per il servizio di {@link AggiornaPrimaNota}.
	 * 
	 * @return la request creata
	 */
	public AggiornaPrimaNota creaRequestAggiornaPrimaNotaIntegrata(){
		//SIAC-4524
		AggiornaPrimaNota request = creaRequest(AggiornaPrimaNota.class);
		getPrimaNota().setListaMovimentiEP(ottieniListaMovimentiEPConAmbito());
		request.setPrimaNota(getPrimaNota());
		return request;
	}

	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaConto}.
	 * @param conto il conto per cui effettuare la ricerca
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaConto creaRequestRicercaSinteticaConto(Conto conto) {
		RicercaSinteticaConto request = creaRequest(RicercaSinteticaConto.class);
		
		conto.setAmbito(getAmbito());
		
		request.setBilancio(getBilancio());
		request.setConto(conto);
		request.setParametriPaginazione(creaParametriPaginazione());
		
		return request;
	}
	/**
	 * Crea la request per il servizio ricerca codifiche per tipoCodifica uguale a ClassePiano
	 * @return la request
	 */
	public RicercaCodifiche creaRequestRicercaClassi(){
		String suffix = "_" + getAmbito().getSuffix();
		return creaRequestRicercaCodifiche("ClassePiano" + suffix);
	}

}
