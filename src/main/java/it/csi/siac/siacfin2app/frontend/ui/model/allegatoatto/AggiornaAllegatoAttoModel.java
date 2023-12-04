/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorElencoDocumentiAllegato;
import it.csi.siac.siacbilser.business.utility.ElaborazioneEnum;
import it.csi.siac.siacbilser.frontend.webservice.msg.ElaborazioneWrapper;
import it.csi.siac.siacbilser.frontend.webservice.msg.EsisteElaborazioneAttiva;
import it.csi.siac.siacbilser.model.TipologiaAttributo;
import it.csi.siac.siaccecser.frontend.webservice.msg.InviaAllegatoAtto;
import it.csi.siac.siaccecser.frontend.webservice.msg.StampaAllegatoAtto;
import it.csi.siac.siaccommon.util.collections.list.SortedSetList;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegato;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegatoEntrata;
import it.csi.siac.siacfin2app.frontend.ui.util.wrappers.allegatoatto.ElementoElencoDocumentiAllegatoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaAttributiQuotaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDatiSoggettoAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaImportiQuoteDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaMassivaDatiSoggettoAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DisassociaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.EliminaQuotaDaElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDatiSospensioneAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioQuotaSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaElenchiPerAllegatoAtto;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaQuoteElenco;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.SpezzaQuotaSpesa;
import it.csi.siac.siacfin2ser.model.AllegatoAtto;
import it.csi.siac.siacfin2ser.model.AllegatoAttoModelDetail;
import it.csi.siac.siacfin2ser.model.DatiSoggettoAllegato;
import it.csi.siac.siacfin2ser.model.Documento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.ElencoDocumentiAllegato;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;
import it.csi.siac.siacfin2ser.model.Subdocumento;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.AggiornaDatiDurcSoggetto;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaProvvisorioDiCassaPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.codifiche.CodificaFin;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.provvisoriDiCassa.ProvvisorioDiCassa.TipoProvvisorioDiCassa;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.RicercaProvvisorioDiCassaK;
import it.csi.siac.siacfinser.model.ric.SorgenteDatiSoggetto;
import it.csi.siac.siacfinser.model.siopeplus.SiopeAssenzaMotivazione;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * Classe di model per l'aggiornamento dell'allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/set/2014
 *
 */
public class AggiornaAllegatoAttoModel extends GenericAllegatoAttoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 3509011605563225740L;
	private static final String AZIONE_DECENTRATA = "OP-COM-aggAttoAllegatoDec";
	
	// ENUM di valutazione della pagina di ingresso
	/**
	 * Enum rappresentante lo tab di ingresso.
	 * 
	 * @author Marchino Alessandro
	 * @version 1.0.0 - 16/set/2014
	 */
	public static enum TabVisualizzazione {
		/** Pagina <em>Dati Allegato</em> */
		DATI,
		/** Pagina <em>Elenchi Collegati</em> */
		ELENCO,
		/** Pagina <em>DURC</em> */
		DURC
	}
	
	private Integer uidAllegatoAtto;
	private TabVisualizzazione tabVisualizzazione;
	private Integer row;
	private List<ElencoDocumentiAllegato> listaElencoDocumentiAllegato = new SortedSetList<ElencoDocumentiAllegato>(ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
	private List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaElementoElencoDocumentiAllegato = new SortedSetList<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>>();
	
	private SubdocumentoSpesa subdocumentoSpesa;
	private SubdocumentoEntrata subdocumentoEntrata;
	// Aggiunti in quanto sembra che Struts non voglia saperne di Generics
	private DocumentoSpesa documentoSpesa;
	private DocumentoEntrata documentoEntrata;
	private DatiSoggettoAllegato datiSoggettoAllegato;
	private Integer uidElencoDaEliminare;
	
	// SIAC-5021
	private boolean stampaAbilitato;
	private boolean invioFluxAbilitato;
	private Integer uidSubdocumento;
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggetto;
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggettoFiltrate = new ArrayList<ModalitaPagamentoSoggetto>();
	
	// SIAC-5043
	private SubdocumentoSpesa nuovoSubdocumentoSpesa;
	private SubdocumentoEntrata nuovoSubdocumentoEntrata;
	
	// SIAC-5172
	private boolean datiSoggettoAllegatoDeterminatiUnivocamente;
	
	// SIAC-5311 SIOPE+
	private List<SiopeAssenzaMotivazione> listaSiopeAssenzaMotivazione = new ArrayList<SiopeAssenzaMotivazione>();
	//SIAC-5589
	private Soggetto soggetto;
	private List<CodificaFin> listaClasseSoggetto = new ArrayList<CodificaFin>();
	

	/** Costruttore vuoto di default */
	public AggiornaAllegatoAttoModel() {
		super();
		setTitolo("Aggiorna allegato atto");
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
	 * @return the tabVisualizzazione
	 */
	public TabVisualizzazione getTabVisualizzazione() {
		return tabVisualizzazione;
	}

	/**
	 * @param tabVisualizzazione the tabVisualizzazione to set
	 */
	public void setTabVisualizzazione(TabVisualizzazione tabVisualizzazione) {
		this.tabVisualizzazione = tabVisualizzazione;
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
	 * @return the listaElencoDocumentiAllegato
	 */
	public List<ElencoDocumentiAllegato> getListaElencoDocumentiAllegato() {
		return listaElencoDocumentiAllegato;
	}

	/**
	 * @param listaElencoDocumentiAllegato the listaElencoDocumentiAllegato to set
	 */
	public void setListaElencoDocumentiAllegato(List<ElencoDocumentiAllegato> listaElencoDocumentiAllegato) {
		this.listaElencoDocumentiAllegato = new SortedSetList<ElencoDocumentiAllegato>(listaElencoDocumentiAllegato, ComparatorElencoDocumentiAllegato.INSTANCE_DESC);
	}

	/**
	 * @return the listaElementoElencoDocumentiAllegato
	 */
	public List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> getListaElementoElencoDocumentiAllegato() {
		return listaElementoElencoDocumentiAllegato;
	}

	/**
	 * @param listaElementoElencoDocumentiAllegato the listaElementoElencoDocumentiAllegato to set
	 */
	public void setListaElementoElencoDocumentiAllegato(List<ElementoElencoDocumentiAllegato<?, ?, ?, ?, ?>> listaElementoElencoDocumentiAllegato) {
		this.listaElementoElencoDocumentiAllegato = new SortedSetList<ElementoElencoDocumentiAllegato<?,?,?,?,?>>(listaElementoElencoDocumentiAllegato);
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
	 * @return the documentoSpesa
	 */
	public DocumentoSpesa getDocumentoSpesa() {
		return documentoSpesa;
	}

	/**
	 * @param documentoSpesa the documentoSpesa to set
	 */
	public void setDocumentoSpesa(DocumentoSpesa documentoSpesa) {
		this.documentoSpesa = documentoSpesa;
	}

	/**
	 * @return the documentoEntrata
	 */
	public DocumentoEntrata getDocumentoEntrata() {
		return documentoEntrata;
	}

	/**
	 * @param documentoEntrata the documentoEntrata to set
	 */
	public void setDocumentoEntrata(DocumentoEntrata documentoEntrata) {
		this.documentoEntrata = documentoEntrata;
	}

	/**
	 * @return the datiSoggettoAllegato
	 */
	public DatiSoggettoAllegato getDatiSoggettoAllegato() {
		return datiSoggettoAllegato;
	}

	/**
	 * @param datiSoggettoAllegato the datiSoggettoAllegato to set
	 */
	public void setDatiSoggettoAllegato(DatiSoggettoAllegato datiSoggettoAllegato) {
		this.datiSoggettoAllegato = datiSoggettoAllegato;
	}

	/**
	 * @return the nomeAzioneDecentrata
	 */
	public String getNomeAzioneDecentrata() {
		return AZIONE_DECENTRATA;
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
	 * @return the daCompletare
	 */
	public Boolean getDaCompletare() {
		return StatoOperativoAllegatoAtto.DA_COMPLETARE.equals(getAllegatoAtto().getStatoOperativoAllegatoAtto());
	}
	
	/**
	 * @return the daCompletareOCompletato
	 */
	public Boolean getDaCompletareOCompletato() {
		return StatoOperativoAllegatoAtto.DA_COMPLETARE.equals(getAllegatoAtto().getStatoOperativoAllegatoAtto())
			|| StatoOperativoAllegatoAtto.COMPLETATO.equals(getAllegatoAtto().getStatoOperativoAllegatoAtto());
	}
	
	/**
	 * @return the parzialmenteConvalidato
	 */
	public Boolean getParzialmenteConvalidato() {
		return StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO.equals(getAllegatoAtto().getStatoOperativoAllegatoAtto());
	}
	
	/**
	 * @return the uidElencoDaEliminare
	 */
	public Integer getUidElencoDaEliminare() {
		return uidElencoDaEliminare;
	}

	/**
	 * @param uidElencoDaEliminare the uidElencoDaEliminare to set
	 */
	public void setUidElencoDaEliminare(Integer uidElencoDaEliminare) {
		this.uidElencoDaEliminare = uidElencoDaEliminare;
	}
	
	/**
	 * @return the stampaAbilitato
	 */
	public boolean isStampaAbilitato() {
		return stampaAbilitato;
	}

	/**
	 * @param stampaAbilitato the stampaAbilitato to set
	 */
	public void setStampaAbilitato(boolean stampaAbilitato) {
		this.stampaAbilitato = stampaAbilitato;
	}

	/**
	 * @return the invioFluxAbilitato
	 */
	public boolean isInvioFluxAbilitato() {
		return invioFluxAbilitato;
	}

	/**
	 * @param invioFluxAbilitato the invioFluxAbilitato to set
	 */
	public void setInvioFluxAbilitato(boolean invioFluxAbilitato) {
		this.invioFluxAbilitato = invioFluxAbilitato;
	}

	/**
	 * @return the uidSubdocumento
	 */
	public Integer getUidSubdocumento() {
		return uidSubdocumento;
	}

	/**
	 * @param uidSubdocumento the uidSubdocumento to set
	 */
	public void setUidSubdocumento(Integer uidSubdocumento) {
		this.uidSubdocumento = uidSubdocumento;
	}

	/**
	 * @return the modalitaPagamentoSoggetto
	 */
	public ModalitaPagamentoSoggetto getModalitaPagamentoSoggetto() {
		return modalitaPagamentoSoggetto;
	}

	/**
	 * @param modalitaPagamentoSoggetto the modalitaPagamentoSoggetto to set
	 */
	public void setModalitaPagamentoSoggetto(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		this.modalitaPagamentoSoggetto = modalitaPagamentoSoggetto;
	}

	/**
	 * @return the listaModalitaPagamentoSoggettoFiltrate
	 */
	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggettoFiltrate() {
		return listaModalitaPagamentoSoggettoFiltrate;
	}

	/**
	 * @param listaModalitaPagamentoSoggettoFiltrate the listaModalitaPagamentoSoggettoFiltrate to set
	 */
	public void setListaModalitaPagamentoSoggettoFiltrate(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggettoFiltrate) {
		this.listaModalitaPagamentoSoggettoFiltrate = listaModalitaPagamentoSoggettoFiltrate != null ? listaModalitaPagamentoSoggettoFiltrate : new ArrayList<ModalitaPagamentoSoggetto>();
	}

	/**
	 * @return the nuovoSubdocumentoSpesa
	 */
	public SubdocumentoSpesa getNuovoSubdocumentoSpesa() {
		return nuovoSubdocumentoSpesa;
	}

	/**
	 * @param nuovoSubdocumentoSpesa the nuovoSubdocumentoSpesa to set
	 */
	public void setNuovoSubdocumentoSpesa(SubdocumentoSpesa nuovoSubdocumentoSpesa) {
		this.nuovoSubdocumentoSpesa = nuovoSubdocumentoSpesa;
	}

	/**
	 * @return the nuovoSubdocumentoEntrata
	 */
	public SubdocumentoEntrata getNuovoSubdocumentoEntrata() {
		return nuovoSubdocumentoEntrata;
	}

	/**
	 * @param nuovoSubdocumentoEntrata the nuovoSubdocumentoEntrata to set
	 */
	public void setNuovoSubdocumentoEntrata(SubdocumentoEntrata nuovoSubdocumentoEntrata) {
		this.nuovoSubdocumentoEntrata = nuovoSubdocumentoEntrata;
	}

	/**
	 * @return the datiSoggettoAllegatoDeterminatiUnivocamente
	 */
	public boolean isDatiSoggettoAllegatoDeterminatiUnivocamente() {
		return datiSoggettoAllegatoDeterminatiUnivocamente;
	}

	/**
	 * @param datiSoggettoAllegatoDeterminatiUnivocamente the datiSoggettoAllegatoDeterminatiUnivocamente to set
	 */
	public void setDatiSoggettoAllegatoDeterminatiUnivocamente(boolean datiSoggettoAllegatoDeterminatiUnivocamente) {
		this.datiSoggettoAllegatoDeterminatiUnivocamente = datiSoggettoAllegatoDeterminatiUnivocamente;
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
	 * Gets the soggetto.
	 *
	 * @return the soggetto
	 */
	public Soggetto getSoggetto() {
		return soggetto;
	}

	/**
	 * Sets the soggetto.
	 *
	 * @param soggetto the soggetto to set
	 */
	public void setSoggetto(Soggetto soggetto) {
		this.soggetto = soggetto;
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
		this.listaClasseSoggetto = listaClasseSoggetto != null? listaClasseSoggetto : new ArrayList<CodificaFin>() ;
	}

	/**
	 * @return the flagRitenuteNonAggiornabile
	 */
	public boolean isFlagRitenuteNonAggiornabile() {
		// Lo stato deve essere COMPLETATO o ANNULLATO
		return getAllegatoAtto() != null
			&& (StatoOperativoAllegatoAtto.COMPLETATO.equals(getAllegatoAtto().getStatoOperativoAllegatoAtto())
				|| StatoOperativoAllegatoAtto.ANNULLATO.equals(getAllegatoAtto().getStatoOperativoAllegatoAtto()));
	}
	
	/**
	 * Controlla se l'ente gestisce o meno l'integrazione con Atti di liquidazione.
	 * 
	 * @return <code>true</code> se l'ente gestisce l'integrazione; <code>false</code> in caso contrario
	 */
	public boolean isIntegrazioneAttiLiquidazione() {
		return getEnte() != null && getEnte().getGestioneLivelli() != null;
			// TODO: impostare il controllo corretto
			//&& BilConstants.GESTIONE_UEB.getConstant().equals(getEnte().getGestioneLivelli().get(TipologiaGestioneLivelli.LIVELLO_GESTIONE_BILANCIO))
	}
	
	// SIAC-5172
	
	/**
	 * Controlla se la sospensione di tutto sia abilitato.
	 * <br/>
	 * La funzionalit&agrave; &eacute; abilitata solo se l'atto &eacute; DA COMPLETARE o COMPLETATO
	 * @return <code>true</code> se la sospensione di tutto sia abilitato
	 */
	public boolean isSospendiTuttoAbilitato() {
		return getAllegatoAtto() != null &&
			(StatoOperativoAllegatoAtto.DA_COMPLETARE.equals(getAllegatoAtto().getStatoOperativoAllegatoAtto())
				|| StatoOperativoAllegatoAtto.COMPLETATO.equals(getAllegatoAtto().getStatoOperativoAllegatoAtto()));
	}
	// SIAC-5410
	/**
	 * @return the impegnoTipoDebitoCommerciale
	 */
	public boolean isImpegnoTipoDebitoCommerciale() {
		if(getSubdocumentoSpesa() == null) {
			return false;
		}
		Impegno impegno = getSubdocumentoSpesa().getImpegnoOSubImpegno();
		return impegno != null
				&& impegno.getSiopeTipoDebito() != null
				&& BilConstants.CODICE_SIOPE_DEBITO_TIPO_COMMERCIALE.getConstant().equals(impegno.getSiopeTipoDebito().getCodice());
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
		
		request.setAllegatoAttoModelDetails(AllegatoAttoModelDetail.DataInizioValiditaStato, AllegatoAttoModelDetail.DatiSoggetto,
				AllegatoAttoModelDetail.ElencoDocumenti, AllegatoAttoModelDetail.IsAssociatoAdAlmenoUnaQuotaSpesa, 
				//SIAC-6840
				AllegatoAttoModelDetail.IsAssociatoAdUnDocumento,
				
				AllegatoAttoModelDetail.ElencoSoggettiDurc);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link AggiornaAllegatoAtto}.
	 * 
	 * @return la request creata
	 */
	public AggiornaAllegatoAtto creaRequestAggiornaAllegatoAtto() {
		AggiornaAllegatoAtto request = creaRequest(AggiornaAllegatoAtto.class);
		
		// Popolo l'ente
		getAllegatoAtto().setEnte(getEnte());
		
		request.setBilancio(getBilancio());
		request.setAllegatoAtto(getAllegatoAtto());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioElenco}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioElenco creaRequestRicercaDettaglioElenco() {
		RicercaDettaglioElenco request = creaRequest(RicercaDettaglioElenco.class);
		
		getElencoDocumentiAllegato().setEnte(getEnte());
		request.setElencoDocumentiAllegato(getElencoDocumentiAllegato());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link DisassociaElenco}.
	 * 
	 * @return la request creata
	 */
	public DisassociaElenco creaRequestDisassociaElenco() {
		DisassociaElenco request = creaRequest(DisassociaElenco.class);
		request.setBilancio(getBilancio());
		
		// Minimizzo i dati dell'allegato
		AllegatoAtto aa = new AllegatoAtto();
		aa.setUid(getAllegatoAtto().getUid());
		aa.setEnte(getEnte());
		getElencoDocumentiAllegato().setAllegatoAtto(aa);
		getElencoDocumentiAllegato().setEnte(getEnte());
		
		request.setElencoDocumentiAllegato(getElencoDocumentiAllegato());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AssociaElenco}.
	 * 
	 * @return la request creata
	 */
	public AssociaElenco creaRequestAssociaElenco() {
		AssociaElenco request = creaRequest(AssociaElenco.class);
		request.setBilancio(getBilancio());
		
		// Minimizzo i dati dell'allegato
		AllegatoAtto aa = new AllegatoAtto();
		aa.setUid(getAllegatoAtto().getUid());
		aa.setEnte(getEnte());
		getElencoDocumentiAllegato().setAllegatoAtto(aa);
		getElencoDocumentiAllegato().setEnte(getEnte());
		
		request.setElencoDocumentiAllegato(getElencoDocumentiAllegato());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaDatiSoggettoAllegatoAtto}.
	 * 
	 * @return la request creata
	 */
	public AggiornaDatiSoggettoAllegatoAtto creaRequestAggiornaDatiSoggettoAllegatoAtto() {
		AggiornaDatiSoggettoAllegatoAtto request = creaRequest(AggiornaDatiSoggettoAllegatoAtto.class);
		
		// Injetto solo il minimo dell'allegato atto
		AllegatoAtto aa = new AllegatoAtto();
		aa.setUid(getAllegatoAtto().getUid());
		aa.setEnte(getEnte());
		getDatiSoggettoAllegato().setAllegatoAtto(aa);
		
		// Injetto l'ente
		getDatiSoggettoAllegato().setEnte(getEnte());
		
		request.setDatiSoggettoAllegato(getDatiSoggettoAllegato());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDatiSoggettoAllegato}.
	 * 
	 * @return la request creata
	 */
	public RicercaDatiSoggettoAllegato creaRequestRicercaDatiSoggettoAllegato() {
		RicercaDatiSoggettoAllegato request = creaRequest(RicercaDatiSoggettoAllegato.class);
		
		// Injetto solo il minimo dell'allegato atto
		AllegatoAtto aa = new AllegatoAtto();
		aa.setUid(getAllegatoAtto().getUid());
		aa.setEnte(getEnte());
		request.setAllegatoAtto(aa);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link EliminaQuotaDaElenco}.
	 * 
	 * @return la request creata
	 */
	public EliminaQuotaDaElenco creaRequestEliminaQuotaDaElenco() {
		EliminaQuotaDaElenco request = creaRequest(EliminaQuotaDaElenco.class);
		
		// Popolo l'elenco
		ElencoDocumentiAllegato eda = new ElencoDocumentiAllegato();
		eda.setUid(getElencoDocumentiAllegato().getUid());
		eda.setEnte(getEnte());
		request.setElencoDocumentiAllegato(eda);
		
		request.setBilancio(getBilancio());
		
		@SuppressWarnings("unchecked")
		Subdocumento<?, ?> subdocumento = ObjectUtils.firstNonNull(getSubdocumentoSpesa(), getSubdocumentoEntrata());
		request.setSubdocumento(subdocumento);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaImportiQuoteDocumentoSpesa}.
	 * 
	 * @return la request creata
	 */
	public AggiornaImportiQuoteDocumentoSpesa creaRequestAggiornaImportiQuoteDocumentoSpesa() {
		AggiornaImportiQuoteDocumentoSpesa request = creaRequest(AggiornaImportiQuoteDocumentoSpesa.class);

		// Popolo l'ente
		getSubdocumentoSpesa().setEnte(getEnte());
		getSubdocumentoSpesa().setDocumento(getDocumentoSpesa());
		getSubdocumentoSpesa().setModalitaPagamentoSoggetto(getModalitaPagamentoSoggetto());
		
		List<SubdocumentoSpesa> list = new ArrayList<SubdocumentoSpesa>();
		list.add(getSubdocumentoSpesa());
		request.setSubdocumentiSpesa(list);
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaImportiQuoteDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public AggiornaImportiQuoteDocumentoEntrata creaRequestAggiornaImportiQuoteDocumentoEntrata() {
		AggiornaImportiQuoteDocumentoEntrata request = creaRequest(AggiornaImportiQuoteDocumentoEntrata.class);
		
		// Popolo l'ente
		getSubdocumentoEntrata().setEnte(getEnte());
		getSubdocumentoEntrata().setDocumento(getDocumentoEntrata());
		
		List<SubdocumentoEntrata> list = new ArrayList<SubdocumentoEntrata>();
		list.add(getSubdocumentoEntrata());
		request.setSubdocumentiEntrata(list);
		request.setBilancio(getBilancio());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaImportiQuoteDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public AggiornaAttributiQuotaDocumentoSpesa creaRequestAggiornaAttributiQuotaDocumentoSpesa() {
		AggiornaAttributiQuotaDocumentoSpesa request = creaRequest(AggiornaAttributiQuotaDocumentoSpesa.class);
		
		// Popolo l'ente
		getSubdocumentoSpesa().setEnte(getEnte());
		request.setSubdocumentoSpesa(getSubdocumentoSpesa());
		request.setSiopeAssenzaMotivazione(impostaEntitaFacoltativa(getSubdocumentoSpesa().getSiopeAssenzaMotivazione()));
		
		// Popolo gli attributi che mi interessano
		Map<TipologiaAttributo, Object> attributi = new EnumMap<TipologiaAttributo, Object>(TipologiaAttributo.class);
		attributi.put(TipologiaAttributo.CIG, getSubdocumentoSpesa().getCig());
		attributi.put(TipologiaAttributo.CUP, getSubdocumentoSpesa().getCup());
		
		request.setAttributi(attributi);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaElenchiPerAllegatoAtto}.
	 * 
	 * @return la request creata
	 */
	public RicercaElenchiPerAllegatoAtto creaRequestRicercaElenchiPerAllegatoAtto() {
		RicercaElenchiPerAllegatoAtto request = creaRequest(RicercaElenchiPerAllegatoAtto.class);
		
		AllegatoAtto aa = new AllegatoAtto();
		aa.setUid(getAllegatoAtto().getUid());
		request.setAllegatoAtto(aa);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link StampaAllegatoAtto}.
	 * 
	 * @return la request creata
	 */
	public StampaAllegatoAtto creaRequestStampaAllegatoAtto() {
		StampaAllegatoAtto request = creaRequest(StampaAllegatoAtto.class);
		
		// Imposto solo l'uid
		AllegatoAtto aa = new AllegatoAtto();
		aa.setUid(getAllegatoAtto().getUid());
		aa.setEnte(getEnte());
		
		request.setAllegatoAtto(aa);
		request.setEnte(getEnte());
		request.setBilancio(getBilancio());
		request.setAnnoEsercizio(getAnnoEsercizioInt());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link InviaAllegatoAtto}.
	 * 
	 * @return la request creata
	 */
	public InviaAllegatoAtto creaRequestInviaAllegatoAtto() {
		InviaAllegatoAtto request = creaRequest(InviaAllegatoAtto.class);
		
		// Imposto solo l'uid
		AllegatoAtto aa = new AllegatoAtto();
		aa.setUid(getAllegatoAtto().getUid());
		aa.setEnte(getEnte());
		request.setAllegatoAtto(aa);
		
		request.setBilancio(getBilancio());
		request.setAnnoEsercizio(getAnnoEsercizioInt());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}.
	 * @return la request creata
	 */
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave() {
		RicercaSoggettoPerChiave req = creaRequest(RicercaSoggettoPerChiave.class);
		
		req.setCodificaAmbito(BilConstants.AMBITO_FIN.getConstant());
		req.setEnte(getEnte());
		req.setSorgenteDatiSoggetto(SorgenteDatiSoggetto.SIAC);
		
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setCodice(getDocumentoSpesa().getSoggetto().getCodiceSoggetto());
		req.setParametroSoggettoK(parametroSoggettoK);
		
		return req;
	}
	
	/**
	 * Wrappa l'elenco.
	 * 
	 * @param eda l'elenco da wrappare
	 */
	public void wrapListaElementoElencoDocumentiAllegato(ElencoDocumentiAllegato eda) {
		// Pulisco la lista
		getListaElementoElencoDocumentiAllegato().clear();
		if(eda == null) {
			// Se non ho un elenco, non ho nulla da wrappare
			return;
		}
		for(Subdocumento<?, ?> s : eda.getSubdocumenti()) {
			Documento<?, ?> doc = s.getDocumento();
			if(doc == null || doc.getSoggetto() == null) {
				// Non ho i dati del documento. Strano. Continuo
				continue;
			}
			DatiSoggettoAllegato dsa = getAllegatoAtto().datiSoggettoAllegatoBySoggetto(doc.getSoggetto());
			if(s instanceof SubdocumentoSpesa) {
				ElementoElencoDocumentiAllegatoSpesa eedas = new ElementoElencoDocumentiAllegatoSpesa(eda, dsa, Boolean.valueOf(isGestioneUEB()), (SubdocumentoSpesa)s);
				getListaElementoElencoDocumentiAllegato().add(eedas);
			} else if(s instanceof SubdocumentoEntrata) {
				ElementoElencoDocumentiAllegatoEntrata eedae = new ElementoElencoDocumentiAllegatoEntrata(eda, dsa, Boolean.valueOf(isGestioneUEB()), (SubdocumentoEntrata)s);
				getListaElementoElencoDocumentiAllegato().add(eedae);
			}
		}
	}

	/**
	 * Crea unq request per il servizio di {@link SpezzaQuotaSpesa}.
	 * @return la request creata
	 */
	public SpezzaQuotaSpesa creaRequestSpezzaQuotaSpesa() {
		SpezzaQuotaSpesa req = creaRequest(SpezzaQuotaSpesa.class);
		
		req.setSubdocumentoSpesa(getNuovoSubdocumentoSpesa());
		
		getNuovoSubdocumentoSpesa().setUid(getSubdocumentoSpesa().getUid());
		req.setBilancio(getBilancio());
		
		return req;
	}

	/**
	 * Crea unq request per il servizio di {@link SpezzaQuotaEntrata}.
	 * @return la request creata
	 */
	public SpezzaQuotaEntrata creaRequestSpezzaQuotaEntrata() {
		SpezzaQuotaEntrata req = creaRequest(SpezzaQuotaEntrata.class);
		
		req.setSubdocumentoEntrata(getNuovoSubdocumentoEntrata());
		getNuovoSubdocumentoEntrata().setUid(getSubdocumentoEntrata().getUid());
		req.setBilancio(getBilancio());
		
		return req ;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaProvvisorioDiCassaPerChiave}.
	 * @param provvisorioDiCassa il provvisorio da cercare
	 * @param tipoProvvisorioDiCassa il tipo di provvisorio
	 * @return la request creata
	 */
	public RicercaProvvisorioDiCassaPerChiave creaRequestRicercaProvvisorioDiCassaPerChiave(ProvvisorioDiCassa provvisorioDiCassa, TipoProvvisorioDiCassa tipoProvvisorioDiCassa) {
		RicercaProvvisorioDiCassaPerChiave req = creaRequest(RicercaProvvisorioDiCassaPerChiave.class);
		
		req.setBilancio(getBilancio());
		req.setEnte(getEnte());
		
		RicercaProvvisorioDiCassaK pRicercaProvvisorioK = new RicercaProvvisorioDiCassaK();
		pRicercaProvvisorioK.setAnnoProvvisorioDiCassa(provvisorioDiCassa.getAnno());
		pRicercaProvvisorioK.setNumeroProvvisorioDiCassa(provvisorioDiCassa.getNumero());
		pRicercaProvvisorioK.setTipoProvvisorioDiCassa(tipoProvvisorioDiCassa);
		req.setpRicercaProvvisorioK(pRicercaProvvisorioK);
		
		return req;
	}

	/**
	 * Crea una request per il servizio di {@link AggiornaMassivaDatiSoggettoAllegatoAtto}.
	 * @param eda l'elenco da impostare
	 * @return la request creata
	 */
	public AggiornaMassivaDatiSoggettoAllegatoAtto creaRequestAggiornaMassivaDatiSoggettoAllegatoAtto(ElencoDocumentiAllegato eda) {
		AggiornaMassivaDatiSoggettoAllegatoAtto req = creaRequest(AggiornaMassivaDatiSoggettoAllegatoAtto.class);
		
		req.setDatiSoggettoAllegato(getDatiSoggettoAllegato());
		req.setElencoDocumentiAllegato(eda);
		
		AllegatoAtto aa = new AllegatoAtto();
		aa.setUid(getAllegatoAtto().getUid());
		req.setAllegatoAtto(aa);
		
		return req;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDatiSospensioneAllegatoAtto}
	 * @param eda l'elenco da impostare
	 * @return la request creata
	 */
	public RicercaDatiSospensioneAllegatoAtto creaRequestRicercaDatiSospensioneAllegatoAtto(ElencoDocumentiAllegato eda) {
		RicercaDatiSospensioneAllegatoAtto req = creaRequest(RicercaDatiSospensioneAllegatoAtto.class);
		
		req.setElencoDocumentiAllegato(eda);
		
		AllegatoAtto aa = new AllegatoAtto();
		aa.setUid(getAllegatoAtto().getUid());
		req.setAllegatoAtto(aa);
		
		return req;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioQuotaSpesa}.
	 * @param ss il subdocumento di spesa
	 * @return la request creata
	 */
	public RicercaDettaglioQuotaSpesa creaRequestRicercaDettaglioQuotaSpesa(SubdocumentoSpesa ss) {
		RicercaDettaglioQuotaSpesa req = creaRequest(RicercaDettaglioQuotaSpesa.class);
		req.setSubdocumentoSpesa(ss);
		return req;
	}

	/**
	 * Crea una request per il servizio di {@link EsisteElaborazioneAttiva}.
	 * @return la request creata
	 */
    public EsisteElaborazioneAttiva creaRequestEsisteElaborazioneAttiva() {
	EsisteElaborazioneAttiva req = creaRequest(EsisteElaborazioneAttiva.class);

	ElaborazioneWrapper elabWrapper = new ElaborazioneWrapper();
	elabWrapper.setUid(getUidAllegatoAtto()); 
	elabWrapper.setElaborazione(ElaborazioneEnum.ALLEGATO_ATTO_AGGIORNA);

	req.setElaborazioni(elabWrapper);

	return req;
    }


    /**
     * Crea request aggiorna dati durc soggetto.
     *
     * @return the aggiorna dati durc soggetto
     */
    public AggiornaDatiDurcSoggetto creaRequestAggiornaDatiDurcSoggetto() {
	AggiornaDatiDurcSoggetto req = creaRequest(AggiornaDatiDurcSoggetto.class);
	
	req.setIdSoggetto(soggetto.getUid());
	req.setDataFineValiditaDurc(soggetto.getDataFineValiditaDurc());
	req.setTipoFonteDurc(soggetto.getTipoFonteDurc());
	req.setFonteDurcClassifId(soggetto.getFonteDurcClassifId());
	req.setNoteDurc(soggetto.getNoteDurc());

	return req;
    }
    

	//SIAC-7410
	/**
	 * Crea una request per il servizio {@link RicercaSinteticaQuoteElenco}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaQuoteElenco creaRequestRicercaSinteticaQuoteElenco() {
		RicercaSinteticaQuoteElenco request = creaRequest(RicercaSinteticaQuoteElenco.class);
		request.setElencoDocumentiAllegato(getElencoDocumentiAllegato());
		request.setSoggetto(getSoggetto());
		request.setParametriPaginazione(new ParametriPaginazione(0,10));
		return request;
	}
}
