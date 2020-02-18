/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * 
 */
package it.csi.siac.siaccespapp.frontend.ui.model.cespite;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera.ElementoConsultazioneScritturaPrimaNotaLibera;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.ModelDetail;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaScrittureInventarioByEntitaCollegata;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDismissioneCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaScrittureRegistroAByCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaVariazioneCespite;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespiteModelDetail;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccespser.model.DismissioneCespiteModelDetail;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespiteModelDetail;
import it.csi.siac.siacfin2ser.model.LiquidazioneModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrataModelDetail;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesaModelDetail;
import it.csi.siac.siacgenapp.frontend.ui.util.ElementoMovimentoConsultazionePrimaNotaIntegrata;
import it.csi.siac.siacgenser.frontend.webservice.msg.OttieniEntitaCollegatePrimaNota;
import it.csi.siac.siacgenser.model.MovimentoDettaglioModelDetail;
import it.csi.siac.siacgenser.model.MovimentoEPModelDetail;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.PrimaNotaModelDetail;
import it.csi.siac.siacgenser.model.RegistrazioneMovFinModelDetail;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * The Class InserisciTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class ConsultaCespiteModel extends GenericCespiteModel {

	/** Per la serializzazione*/
	private static final long serialVersionUID = 2239866877794530430L;
	
private static final Map<TipoCollegamento, ModelDetail[]> MAPPING_DATI_FINANZIARI_DETAILS;
	
	static {
		Map<TipoCollegamento, ModelDetail[]> tmp = new HashMap<TipoCollegamento, ModelDetail[]>();
		
		tmp.put(TipoCollegamento.LIQUIDAZIONE, new ModelDetail[] {LiquidazioneModelDetail.Impegno, LiquidazioneModelDetail.Soggetto, LiquidazioneModelDetail.PianoDeiConti});
		
		tmp.put(TipoCollegamento.SUBDOCUMENTO_SPESA, new ModelDetail[] {SubdocumentoSpesaModelDetail.TestataDocumento, SubdocumentoSpesaModelDetail.ImpegnoSubimpegnoPdc, SubdocumentoSpesaModelDetail.Liquidazione, SubdocumentoSpesaModelDetail.Ordinativo});
		tmp.put(TipoCollegamento.SUBDOCUMENTO_ENTRATA, new ModelDetail[] {SubdocumentoEntrataModelDetail.TestataDocumento,SubdocumentoEntrataModelDetail.AccertamentoSubaccertamentoPdc, SubdocumentoEntrataModelDetail.Ordinativo});
		
		MAPPING_DATI_FINANZIARI_DETAILS = Collections.unmodifiableMap(tmp);
	}

	
	private static final String STRINGA_SI = "S&igrave;";
	private static final String STRINGA_NO = "No";
	private static final String DEFAULT_FOR_NULL_VALUE = "";
	
	private List<ElementoConsultazioneScritturaPrimaNotaLibera> listaPrimeNote = new ArrayList<ElementoConsultazioneScritturaPrimaNotaLibera>();
	private int uidEntitaCollegata;
	
	private BigDecimal totaleImportoAmmortato = BigDecimal.ZERO;
	
	private String tipoMovimento;
	private TipoCollegamento tipoCollegamento;
	private List<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>> listaDatiFinanziari = new ArrayList<ElementoMovimentoConsultazionePrimaNotaIntegrata<?>>();
	private String datiAccessoriiMovimentoFinanziario;
	private PrimaNota primaNotaContabilitaFinanziaria;
	
	/**
	 * Instantiates a new consulta tipo bene model.
	 */
	public ConsultaCespiteModel() {
		setTitolo("consulta cespite");
	}
	
	/**
	 * @return the listaPrimeNote
	 */
	public List<ElementoConsultazioneScritturaPrimaNotaLibera> getListaPrimeNote() {
		return listaPrimeNote;
	}



	/**
	 * @param listaPrimeNote the listaPrimeNote to set
	 */
	public void setListaPrimeNote(List<ElementoConsultazioneScritturaPrimaNotaLibera> listaPrimeNote) {
		this.listaPrimeNote = listaPrimeNote != null? listaPrimeNote : new ArrayList<ElementoConsultazioneScritturaPrimaNotaLibera>();
	}

	
	/**
	 * @return the uidEntitaCollegata
	 */
	public int getUidEntitaCollegata() {
		return uidEntitaCollegata;
	}

	/**
	 * @param uidEntitaCollegata the uidEntitaCollegata to set
	 */
	public void setUidEntitaCollegata(int uidEntitaCollegata) {
		this.uidEntitaCollegata = uidEntitaCollegata;
	}

	/**
	 * @return the totaleImportoAmmortato
	 */
	public BigDecimal getTotaleImportoAmmortato() {
		return totaleImportoAmmortato;
	}

	/**
	 * @param totaleImportoAmmortato the totaleImportoAmmortato to set
	 */
	public void setTotaleImportoAmmortato(BigDecimal totaleImportoAmmortato) {
		this.totaleImportoAmmortato = totaleImportoAmmortato != null? totaleImportoAmmortato : BigDecimal.ZERO;
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
	 * @return the primaNotaContabilitaFinanziaria
	 */
	public PrimaNota getPrimaNotaContabilitaFinanziaria() {
		return primaNotaContabilitaFinanziaria;
	}

	/**
	 * @param primaNotaContabilitaFinanziaria the primaNotaContabilitaFinanziaria to set
	 */
	public void setPrimaNotaContabilitaFinanziaria(PrimaNota primaNotaContabilitaFinanziaria) {
		this.primaNotaContabilitaFinanziaria = primaNotaContabilitaFinanziaria;
	}

	/**
	 * Gets the codice.
	 *
	 * @return the codice
	 */
	public String getCodice() {
		return StringUtils.defaultString(getCespite().getCodice(), DEFAULT_FOR_NULL_VALUE);
	}
	
	/**
	 * Gets the descrizione.
	 *
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return StringUtils.defaultString(getCespite().getDescrizione(), DEFAULT_FOR_NULL_VALUE);
	}
	
	/**
	 * Gets the classificazione.
	 *
	 * @return the classificazione
	 */
	public String getClassificazione() {
		if(getCespite().getClassificazioneGiuridicaCespite() == null){
			return "";
		}
		return StringUtils.defaultString(getCespite().getClassificazioneGiuridicaCespite().getCodice(), DEFAULT_FOR_NULL_VALUE) + "-" + StringUtils.defaultString(getCespite().getClassificazioneGiuridicaCespite().getDescrizione(), DEFAULT_FOR_NULL_VALUE);
	}
	
	/**
	 * Gets the tipo bene.
	 *
	 * @return the tipo bene
	 */
	public String getTipoBene() {
		if(getCespite().getTipoBeneCespite() == null){
			return "";
		}
		return StringUtils.defaultString(getCespite().getTipoBeneCespite().getCodice(), DEFAULT_FOR_NULL_VALUE) + "-" + StringUtils.defaultString(getCespite().getTipoBeneCespite().getDescrizione(), DEFAULT_FOR_NULL_VALUE);
	}
	
	/**
	 * Gets the inventario.
	 *
	 * @return the inventario
	 */
	public String getInventario() {
		return  StringUtils.defaultString(getCespite().getNumeroInventario(), DEFAULT_FOR_NULL_VALUE);
	}
	/**
	 * Gets the attivo.
	 *
	 * @return the attivo
	 */
	public String getAttivo() {
		if(getCespite().getFlagStatoBene() == null) {
			return "";
		}
		return FormatUtils.formatBoolean(getCespite().getFlagStatoBene().booleanValue(), STRINGA_SI, STRINGA_NO);
	}
	
	/**
	 * Gets the donazione rinvenimento.
	 *
	 * @return the donazione rinvenimento
	 */
	public String getDonazioneRinvenimento() {
		if(getCespite().getFlgDonazioneRinvenimento() == null) {
			return "";
		}
		return FormatUtils.formatBoolean(getCespite().getFlgDonazioneRinvenimento().booleanValue(), STRINGA_SI, STRINGA_NO);
	}
	
	/**
	 * Gets the beni culturali.
	 *
	 * @return the beni culturali
	 */
	public String getBeniCulturali() {
		if(getCespite().getFlagSoggettoTutelaBeniCulturali() == null) {
			return "";
		}
		return FormatUtils.formatBoolean(getCespite().getFlagSoggettoTutelaBeniCulturali().booleanValue(), STRINGA_SI, STRINGA_NO);
	}
	
	/**
	 * Gets the ubicazione.
	 *
	 * @return the ubicazione
	 */
	public String getUbicazione() {
		return StringUtils.defaultString(getCespite().getUbicazione(), DEFAULT_FOR_NULL_VALUE);
	}
	
	/**
	 * Gets the data ingresso inventario.
	 *
	 * @return the data ingresso inventario
	 */
	public String getDataIngressoInventario() {
		return FormatUtils.formatDate(getCespite().getDataAccessoInventario());
	}
	
	/**
	 * Gets the data ingresso inventario.
	 *
	 * @return the data ingresso inventario
	 */
	public String getDataCessazione() {
		return FormatUtils.formatDate(getCespite().getDataCessazione());
	}
	
	/**
	 * Gets the valore iniziale.
	 *
	 * @return the valore iniziale
	 */
	public String getValoreIniziale() {
		return FormatUtils.formatCurrency(getCespite().getValoreIniziale());
	}
	
	/**
	 * Gets the valore iniziale.
	 *
	 * @return the valore iniziale
	 */
	public String getValoreAttuale() {
		return FormatUtils.formatCurrency(getCespite().getValoreAttuale());
	}
	
	
	/**
	 * Gets the note.
	 *
	 * @return the note
	 */
	public String getNote() {
		return StringUtils.defaultString(getCespite().getNote(),DEFAULT_FOR_NULL_VALUE);
	}
	
	/**
	 * Crea request ricerca sintetica variazione cespite.
	 *
	 * @param isIncremento the is incremento
	 * @return the ricerca sintetica variazione cespite
	 */
	public RicercaSinteticaVariazioneCespite creaRequestRicercaSinteticaVariazioneCespite(Boolean isIncremento) {
		RicercaSinteticaVariazioneCespite req = creaRequest(RicercaSinteticaVariazioneCespite.class);
		VariazioneCespite var = new VariazioneCespite();
		var.setFlagTipoVariazioneIncremento(isIncremento);
		Cespite tc = new Cespite();
		tc.setUid(getUidCespite());
		var.setCespite(tc);
		req.setVariazioneCespite(var);
		req.setParametriPaginazione(creaParametriPaginazione());
		req.setModelDetails(VariazioneCespiteModelDetail.StatoVariazioneCespite,
				VariazioneCespiteModelDetail.CespiteModelDetail,
				CespiteModelDetail.TipoBeneCespiteModelDetail);
		return req;
	}

	/**
	 * Crea ricerca sintetica dismissione cespite.
	 *
	 * @return the ricerca sintetica dismissione cespite
	 */
	public RicercaSinteticaDismissioneCespite creaRicercaSinteticaDismissioneCespite() {
		RicercaSinteticaDismissioneCespite req = creaRequest(RicercaSinteticaDismissioneCespite.class);
		Cespite tc = new Cespite();
		tc.setUid(getUidCespite());
		req.setCespite(tc);
		req.setModelDetails(DismissioneCespiteModelDetail.AttoAmministrativo, DismissioneCespiteModelDetail.CausaleEP, DismissioneCespiteModelDetail.NumeroCespitiCollegati, DismissioneCespiteModelDetail.Stato);
		req.setParametriPaginazione(creaParametriPaginazione());
		return req;
	}

	/**
	 * Crea request ricerca scritture cespite.
	 *
	 * @return the ricerca scritture cespite
	 */
	public RicercaScrittureInventarioByEntitaCollegata creaRequestRicercaScrittureInventarioByEntitaCollegataCespite() {
		RicercaScrittureInventarioByEntitaCollegata req =  baseCreaRequestRicercaScrittureByEntitaCollegata();
		Cespite tc = new Cespite();
		tc.setUid(getUidCespite());
		req.setEntitaGeneranteScritture(tc);
		return req;
	}

	/**
	 * Crea request ricerca scritture inventario by entita collegata variazione.
	 *
	 * @return the ricerca scritture inventario by entita collegata
	 */
	public RicercaScrittureInventarioByEntitaCollegata creaRequestRicercaScrittureInventarioByEntitaCollegataVariazione() {
		RicercaScrittureInventarioByEntitaCollegata req = baseCreaRequestRicercaScrittureByEntitaCollegata();
		VariazioneCespite tc = new VariazioneCespite();
		tc.setUid(getUidEntitaCollegata());
		req.setEntitaGeneranteScritture(tc);
		return req;
	}
	
	/**
	 * Crea request ricerca scritture inventario by entita collegata dismissione.
	 *
	 * @return the ricerca scritture inventario by entita collegata
	 */
	public RicercaScrittureInventarioByEntitaCollegata creaRequestRicercaScrittureInventarioByEntitaCollegataDismissione() {
		RicercaScrittureInventarioByEntitaCollegata req = baseCreaRequestRicercaScrittureByEntitaCollegata();
		DismissioneCespite tc = new DismissioneCespite();
		tc.setUid(getUidEntitaCollegata());
		req.setEntitaGeneranteScritture(tc);
		Cespite cespite = new Cespite();
		cespite.setUid(getUidCespite());
		req.setCespiteCollegatoAdEntitaGenerante(cespite);
		
		return req;
	}

	/**
	 * Base crea request ricerca scritture by entita collegata.
	 *
	 * @return the ricerca scritture inventario by entita collegata
	 */
	private RicercaScrittureInventarioByEntitaCollegata baseCreaRequestRicercaScrittureByEntitaCollegata() {
		RicercaScrittureInventarioByEntitaCollegata req = creaRequest(RicercaScrittureInventarioByEntitaCollegata.class);
		req.setModelDetails(new ModelDetail[] {
				PrimaNotaModelDetail.MovimentiEpModelDetail, 
				PrimaNotaModelDetail.StatoOperativo, 
				PrimaNotaModelDetail.StatoAccettazionePrimaNotaProvvisoria,
				PrimaNotaModelDetail.StatoAccettazionePrimaNotaDefinitiva,
				PrimaNotaModelDetail.MovimentiEpModelDetail,
				MovimentoEPModelDetail.MovimentoDettaglioModelDetail,
				MovimentoDettaglioModelDetail.Conto, 
				MovimentoDettaglioModelDetail.Segno				
		});
		return req;
	}
	
	/**
	 * Crea request ricerca scritture inventario by entita collegata dettaglio ammortamento annuo.
	 *
	 * @return the ricerca scritture inventario by entita collegata
	 */
	public RicercaScrittureInventarioByEntitaCollegata creaRequestRicercaScrittureInventarioByEntitaCollegataDettaglioAmmortamentoAnnuo() {
		RicercaScrittureInventarioByEntitaCollegata req =  baseCreaRequestRicercaScrittureByEntitaCollegata();
		DettaglioAmmortamentoAnnuoCespite tc = new DettaglioAmmortamentoAnnuoCespite();
		tc.setUid(getUidEntitaCollegata());
		req.setEntitaGeneranteScritture(tc);
		return req;
	}

	/**
	 * Crea request ricerca sintetica dettaglio ammortamento annuo cespite.
	 *
	 * @return the ricerca sintetica dettaglio ammortamento annuo cespite
	 */
	public RicercaSinteticaDettaglioAmmortamentoAnnuoCespite creaRequestRicercaSinteticaDettaglioAmmortamentoAnnuoCespite() {
		RicercaSinteticaDettaglioAmmortamentoAnnuoCespite req = creaRequest(RicercaSinteticaDettaglioAmmortamentoAnnuoCespite.class);
		Cespite tc = new Cespite();
		tc.setUid(getUidCespite());
		req.setCespite(tc);
		req.setModelDetails(new ModelDetail[] {DettaglioAmmortamentoAnnuoCespiteModelDetail.PrimaNotaModelDetail});
		req.setParametriPaginazione(creaParametriPaginazione());
		return req;
	}

	/**
	 * Crea request ricerca sintetica scritture registro A by cespite.
	 *
	 * @return the ricerca sintetica scritture registro A by cespite
	 */
	public RicercaSinteticaScrittureRegistroAByCespite creaRequestRicercaSinteticaScrittureRegistroAByCespite() {
		RicercaSinteticaScrittureRegistroAByCespite req = creaRequest(RicercaSinteticaScrittureRegistroAByCespite.class);
		Cespite tc = new Cespite();
		tc.setUid(getUidCespite());
		req.setCespite(tc);
		req.setParametriPaginazione(creaParametriPaginazione());
		req.setModelDetails(
				PrimaNotaModelDetail.StatoOperativo,
				PrimaNotaModelDetail.ContoInventario,
				PrimaNotaModelDetail.PrimaNotaInventario,
				PrimaNotaModelDetail.TipoCausale,
				//questo serve solo per avere il tipo collegamento per i dati finanziari collegati
				PrimaNotaModelDetail.MovimentiEpModelDetail,
				MovimentoEPModelDetail.RegistrazioneMovFinModelDetail,
				RegistrazioneMovFinModelDetail.EventoMovimento
		);
		return req;
	}

	/**
	 * Crea request ricerca scritture inventario by entita collegata prima nota.
	 *
	 * @return the ricerca scritture inventario by entita collegata
	 */
	public RicercaScrittureInventarioByEntitaCollegata creaRequestRicercaScrittureInventarioByEntitaCollegataPrimaNota() {
		RicercaScrittureInventarioByEntitaCollegata req =  baseCreaRequestRicercaScrittureByEntitaCollegata();
		PrimaNota tc = new PrimaNota();
		tc.setUid(getUidEntitaCollegata());
		req.setEntitaGeneranteScritture(tc);
		return req;
	}
	
	/**
	 * Crea request ottieni entita collegate prima nota.
	 *
	 * @return the ottieni entita collegate prima nota
	 */
	public OttieniEntitaCollegatePrimaNota creaRequestOttieniEntitaCollegatePrimaNota() {
		OttieniEntitaCollegatePrimaNota req = creaRequest(OttieniEntitaCollegatePrimaNota.class);
		
		req.setPrimaNota(getPrimaNotaContabilitaFinanziaria());
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
		return MAPPING_DATI_FINANZIARI_DETAILS.get(getTipoCollegamento());
	}
}
