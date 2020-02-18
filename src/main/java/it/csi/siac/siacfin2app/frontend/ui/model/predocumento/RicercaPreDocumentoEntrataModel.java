/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.comparator.ComparatorUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.ContoCorrentePredocumentoEntrata;
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.OrdinamentoPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumento;
import it.csi.siac.siacfin2ser.model.DocumentoEntrata;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoIncasso;

/**
 * Classe di model per la ricerca del PreDocumento di Entrata
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/04/2014
 *
 */
public class RicercaPreDocumentoEntrataModel extends GenericPreDocumentoEntrataModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5080609141854299761L;
	
	private static final int ELEMENTI_PER_PAGINA_RICERCA_SINTETICA = 50;
	
	private Date dataCompetenzaDa;
	private Date dataCompetenzaA;
	private StatoOperativoPreDocumento statoOperativoPreDocumento;
	private TipoDocumento tipoDocumento;
	private DocumentoEntrata documento;
	
	private Boolean flagCausaleEntrataMancante = Boolean.FALSE;
	private Boolean flagContoCorrenteMancante = Boolean.FALSE;
	private Boolean flagSoggettoMancante = Boolean.FALSE;
	private Boolean flagAttoAmministrativoMancante = Boolean.FALSE;
	private Boolean flagEstraiNonIncassato = Boolean.FALSE;
	
	// SIAC-4383
	private Date dataTrasmissioneDa;
	private Date dataTrasmissioneA;
	// SIAC-4620
	private Boolean flagNonAnnullati = Boolean.FALSE;
	
	private List<StatoOperativoPreDocumento> listaStatoOperativoPreDocumento = new ArrayList<StatoOperativoPreDocumento>();
	private List<TipoDocumento> listaTipoDocumento = new ArrayList<TipoDocumento>();
	
	// SIAC-4772
	private OrdinativoIncasso ordinativo;
	
	// SIAC-5250
	private List<OrdinamentoPreDocumentoEntrata> listaOrdinamentoPreDocumentoEntrata = new ArrayList<OrdinamentoPreDocumentoEntrata>();
	private OrdinamentoPreDocumentoEntrata ordinamentoPreDocumentoEntrata;
	
	/** Costruttore vuoto di default */
	public RicercaPreDocumentoEntrataModel() {
		setTitolo("Ricerca Predisposizione di Incasso");
		setNomeAzioneDecentrata(BilConstants.RICERCA_PREDOCUMENTO_ENTRATA_DECENTRATO.getConstant());
	}
	
	/**
	 * @return the dataCompetenzaDa
	 */
	public Date getDataCompetenzaDa() {
		if(dataCompetenzaDa == null) {
			return null;
		}
		return new Date(dataCompetenzaDa.getTime());
	}

	/**
	 * @param dataCompetenzaDa the dataCompetenzaDa to set
	 */
	public void setDataCompetenzaDa(Date dataCompetenzaDa) {
		if(dataCompetenzaDa != null) {
			this.dataCompetenzaDa = new Date(dataCompetenzaDa.getTime());
		} else {
			this.dataCompetenzaDa = null;
		}
	}

	/**
	 * @return the dataCompetenzaA
	 */
	public Date getDataCompetenzaA() {
		if(dataCompetenzaA == null) {
			return null;
		}
		return new Date(dataCompetenzaA.getTime());
	}

	/**
	 * @param dataCompetenzaA the dataCompetenzaA to set
	 */
	public void setDataCompetenzaA(Date dataCompetenzaA) {
		if(dataCompetenzaA != null) {
			this.dataCompetenzaA = new Date(dataCompetenzaA.getTime());
		} else {
			this.dataCompetenzaA = null;
		}
	}
	
	/**
	 * @return the statoOperativoPreDocumento
	 */
	public StatoOperativoPreDocumento getStatoOperativoPreDocumento() {
		return statoOperativoPreDocumento;
	}

	/**
	 * @param statoOperativoPreDocumento the statoOperativoPreDocumento to set
	 */
	public void setStatoOperativoPreDocumento(StatoOperativoPreDocumento statoOperativoPreDocumento) {
		this.statoOperativoPreDocumento = statoOperativoPreDocumento;
	}

	/**
	 * @return the tipoDocumento
	 */
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return the documento
	 */
	public DocumentoEntrata getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(DocumentoEntrata documento) {
		this.documento = documento;
	}

	/**
	 * @return the flagCausaleEntrataMancante
	 */
	public Boolean getFlagCausaleEntrataMancante() {
		return flagCausaleEntrataMancante;
	}

	/**
	 * @param flagCausaleEntrataMancante the flagCausaleEntrataMancante to set
	 */
	public void setFlagCausaleEntrataMancante(Boolean flagCausaleEntrataMancante) {
		this.flagCausaleEntrataMancante = flagCausaleEntrataMancante != null ? flagCausaleEntrataMancante : Boolean.FALSE;
	}

	/**
	 * @return the flagContoCorrenteMancante
	 */
	public Boolean getFlagContoCorrenteMancante() {
		return flagContoCorrenteMancante;
	}

	/**
	 * @param flagContoCorrenteMancante the flagContoCorrenteMancante to set
	 */
	public void setFlagContoCorrenteMancante(Boolean flagContoCorrenteMancante) {
		this.flagContoCorrenteMancante = flagContoCorrenteMancante != null ? flagContoCorrenteMancante : Boolean.FALSE;
	}

	/**
	 * @return the flagSoggettoMancante
	 */
	public Boolean getFlagSoggettoMancante() {
		return flagSoggettoMancante;
	}

	/**
	 * @param flagSoggettoMancante the flagSoggettoMancante to set
	 */
	public void setFlagSoggettoMancante(Boolean flagSoggettoMancante) {
		this.flagSoggettoMancante = flagSoggettoMancante != null ? flagSoggettoMancante : Boolean.FALSE;
	}

	/**
	 * @return the flagAttoAmministrativoMancante
	 */
	public Boolean getFlagAttoAmministrativoMancante() {
		return flagAttoAmministrativoMancante;
	}

	/**
	 * @param flagAttoAmministrativoMancante the flagAttoAmministrativoMancante to set
	 */
	public void setFlagAttoAmministrativoMancante(Boolean flagAttoAmministrativoMancante) {
		this.flagAttoAmministrativoMancante = flagAttoAmministrativoMancante != null ? flagAttoAmministrativoMancante : Boolean.FALSE;
	}
	
	/**
	 * @return the flagEstraiNonIncassato
	 */
	public Boolean getFlagEstraiNonIncassato() {
		return flagEstraiNonIncassato;
	}

	/**
	 * @return the dataTrasmissioneDa
	 */
	public Date getDataTrasmissioneDa() {
		return dataTrasmissioneDa != null ? new Date(dataTrasmissioneDa.getTime()) : null;
	}

	/**
	 * @param dataTrasmissioneDa the dataTrasmissioneDa to set
	 */
	public void setDataTrasmissioneDa(Date dataTrasmissioneDa) {
		this.dataTrasmissioneDa = dataTrasmissioneDa != null ? new Date(dataTrasmissioneDa.getTime()) : null;
	}

	/**
	 * @return the dataTrasmissioneA
	 */
	public Date getDataTrasmissioneA() {
		return dataTrasmissioneA != null ? new Date(dataTrasmissioneA.getTime()) : null;
	}

	/**
	 * @param dataTrasmissioneA the dataTrasmissioneA to set
	 */
	public void setDataTrasmissioneA(Date dataTrasmissioneA) {
		this.dataTrasmissioneA = dataTrasmissioneA != null ? new Date(dataTrasmissioneA.getTime()) : null;
	}

	/**
	 * @return the flagNonAnnullati
	 */
	public Boolean getFlagNonAnnullati() {
		return flagNonAnnullati;
	}

	/**
	 * @param flagNonAnnullati the flagNonAnnullati to set
	 */
	public void setFlagNonAnnullati(Boolean flagNonAnnullati) {
		this.flagNonAnnullati = flagNonAnnullati != null ? flagNonAnnullati : Boolean.FALSE;
	}

	/**
	 * @param flagEstraiNonIncassato the flagEstraiNonIncassato to set
	 */
	public void setFlagEstraiNonIncassato(Boolean flagEstraiNonIncassato) {
		this.flagEstraiNonIncassato = flagEstraiNonIncassato != null ? flagEstraiNonIncassato : Boolean.FALSE;
	}

	/**
	 * @return the listaStatoOperativoPreDocumento
	 */
	public List<StatoOperativoPreDocumento> getListaStatoOperativoPreDocumento() {
		return listaStatoOperativoPreDocumento;
	}

	/**
	 * @param listaStatoOperativoPreDocumento the listaStatoOperativoPreDocumento to set
	 */
	public void setListaStatoOperativoPreDocumento(List<StatoOperativoPreDocumento> listaStatoOperativoPreDocumento) {
		this.listaStatoOperativoPreDocumento = listaStatoOperativoPreDocumento != null ? listaStatoOperativoPreDocumento : new ArrayList<StatoOperativoPreDocumento>();
	}
	
	/**
	 * @return the listaTipoDocumento
	 */
	public List<TipoDocumento> getListaTipoDocumento() {
		return listaTipoDocumento;
	}

	/**
	 * @param listaTipoDocumento the listaTipoDocumento to set
	 */
	public void setListaTipoDocumento(List<TipoDocumento> listaTipoDocumento) {
		this.listaTipoDocumento = listaTipoDocumento != null ? listaTipoDocumento : new ArrayList<TipoDocumento>();
	}

	/**
	 * @return the ordinativo
	 */
	public OrdinativoIncasso getOrdinativo() {
		return ordinativo;
	}

	/**
	 * @param ordinativo the ordinativo to set
	 */
	public void setOrdinativo(OrdinativoIncasso ordinativo) {
		this.ordinativo = ordinativo;
	}

	/**
	 * @return the listaOrdinamentoPreDocumentoEntrata
	 */
	public List<OrdinamentoPreDocumentoEntrata> getListaOrdinamentoPreDocumentoEntrata() {
		return listaOrdinamentoPreDocumentoEntrata;
	}

	/**
	 * @param listaOrdinamentoPreDocumentoEntrata the listaOrdinamentoPreDocumentoEntrata to set
	 */
	public void setListaOrdinamentoPreDocumentoEntrata(List<OrdinamentoPreDocumentoEntrata> listaOrdinamentoPreDocumentoEntrata) {
		this.listaOrdinamentoPreDocumentoEntrata = listaOrdinamentoPreDocumentoEntrata != null ? listaOrdinamentoPreDocumentoEntrata : new ArrayList<OrdinamentoPreDocumentoEntrata>();
	}

	/**
	 * @return the ordinamentoPreDocumentoEntrata
	 */
	public OrdinamentoPreDocumentoEntrata getOrdinamentoPreDocumentoEntrata() {
		return ordinamentoPreDocumentoEntrata;
	}

	/**
	 * @param ordinamentoPreDocumentoEntrata the ordinamentoPreDocumentoEntrata to set
	 */
	public void setOrdinamentoPreDocumentoEntrata(OrdinamentoPreDocumentoEntrata ordinamentoPreDocumentoEntrata) {
		this.ordinamentoPreDocumentoEntrata = ordinamentoPreDocumentoEntrata;
	}

	/* ***** Requests ***** */

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaPreDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaPreDocumentoEntrata creaRequestRicercaSinteticaPreDocumentoEntrata() {
		RicercaSinteticaPreDocumentoEntrata request = creaRequest(RicercaSinteticaPreDocumentoEntrata.class);
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		// Flags
		request.setDataCompetenzaA(getDataCompetenzaA());
		request.setDataCompetenzaDa(getDataCompetenzaDa());
		request.setCausaleEntrataMancante(getFlagCausaleEntrataMancante());
		request.setContoCorrenteMancante(getFlagContoCorrenteMancante());
		request.setSoggettoMancante(getFlagSoggettoMancante());
		request.setProvvedimentoMancante(getFlagAttoAmministrativoMancante());
		request.setEstraiNonIncassato(getFlagEstraiNonIncassato());
		request.setTipoCausale(getTipoCausale());
		
		request.setPreDocumentoEntrata(popolaPreDocumentoEntrata());
		
		request.setDocumento(popolaDocumentoSeDatiPresenti(getDocumento(), getTipoDocumento()));
		
		// SIAC-4383
		request.setDataTrasmissioneDa(getDataTrasmissioneDa());
		request.setDataTrasmissioneA(getDataTrasmissioneA());
		
		// SIAC-4620
		request.setNonAnnullati(getFlagNonAnnullati());
		// SIAC-4772
		request.setOrdinativoIncasso(getOrdinativo());
		// SIAC-5250
		request.setOrdinamentoPreDocumentoEntrata(getOrdinamentoPreDocumentoEntrata());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaDocumentoEntrata creaRequestRicercaSinteticaDocumentoEntrata() {
		RicercaSinteticaDocumentoEntrata request = new RicercaSinteticaDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setParametriPaginazione(super.creaParametriPaginazione());
		request.setDocumentoEntrata(popolaDocumentoEntrata());
		
		return request;
	}
	
	/**
	 * Popola il preDocumento di spesa per l'injezione della request di ricerca sintetica.
	 * 
	 * @return il preDocumento creato
	 */
	private PreDocumentoEntrata popolaPreDocumentoEntrata() {
		PreDocumentoEntrata preDocumento = getPreDocumento();
		preDocumento.setEnte(getEnte());
		preDocumento.setStatoOperativoPreDocumento(getStatoOperativoPreDocumento());
		preDocumento.setDatiAnagraficiPreDocumento(getDatiAnagraficiPreDocumento());
		
		preDocumento.setCausaleEntrata(impostaEntitaFacoltativa(getCausaleEntrata()));
		preDocumento.setContoCorrente(impostaEntitaFacoltativa(getContoCorrente()));
		
		preDocumento.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		preDocumento.setCapitoloEntrataGestione(impostaEntitaFacoltativa(getCapitolo()));
		preDocumento.setAccertamento(impostaEntitaFacoltativa(getMovimentoGestione()));
		preDocumento.setSubAccertamento(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		preDocumento.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		preDocumento.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		
		return preDocumento;
	}
	
	@Override
	protected ParametriPaginazione creaParametriPaginazione() {
		ParametriPaginazione parametriPaginazione = new ParametriPaginazione();
		
		parametriPaginazione.setElementiPerPagina(ELEMENTI_PER_PAGINA_RICERCA_SINTETICA);
		parametriPaginazione.setNumeroPagina(0);
		
		return parametriPaginazione;
	}
	
	/**
	 * Popola il documento di spesa per l'injezione della request di ricerca sintetica.
	 * 
	 * @return il documento creato
	 */
	private DocumentoEntrata popolaDocumentoEntrata() {
		DocumentoEntrata documentoEntrata = getDocumento();
		
		documentoEntrata.setEnte(getEnte());
		documentoEntrata.setTipoDocumento(getTipoDocumento());
		
		return documentoEntrata;
	}

	/**
	 * Compone la stringa di riepilogo per la ricerca.
	 * 
	 * @param listaStrutturaAmministrativoContabile la lista delle Strutture Amministrativo Contabili
	 * 
	 * @return la stringa riepilogativa
	 */
	public String componiStringaRiepilogo(List<StrutturaAmministrativoContabile> listaStrutturaAmministrativoContabile) {
		StringBuilder sb = new StringBuilder();
		
		if(StringUtils.isNotBlank(getPreDocumento().getPeriodoCompetenza())) {
			sb.append("Competenza: ").append(getPreDocumento().getPeriodoCompetenza()).append(" - ");
		}
		if(getDataCompetenzaDa() != null) {
			sb.append("Data da: ").append(FormatUtils.formatDate(getDataCompetenzaDa())).append(" - ");
		}
		if(getDataCompetenzaA() != null) {
			sb.append("Data a: ").append(FormatUtils.formatDate(getDataCompetenzaA())).append(" - ");
		}
		if(getDataTrasmissioneDa() != null) {
			sb.append("Data trasmissione da: ").append(FormatUtils.formatDate(getDataTrasmissioneDa())).append(" - ");
		}
		if(getDataTrasmissioneA() != null) {
			sb.append("Data trasmissione a: ").append(FormatUtils.formatDate(getDataTrasmissioneA())).append(" - ");
		}
		if(getStrutturaAmministrativoContabile() != null && getStrutturaAmministrativoContabile().getUid() != 0) {
			StrutturaAmministrativoContabile sac = ComparatorUtils.searchByUidWithChildren(listaStrutturaAmministrativoContabile, getStrutturaAmministrativoContabile());
			sb.append("Struttura Amministrativa: ").append(sac.getCodice()).append(" - ");
		}
		if(getTipoCausale() != null && getTipoCausale().getUid() != 0) {
			TipoCausale tipoCausale = ComparatorUtils.searchByUid(getListaTipoCausale(), getTipoCausale());
			sb.append("Tipo causale: ").append(tipoCausale.getCodice()).append(" - ");
		}
		if(getCausaleEntrata() != null && getCausaleEntrata().getUid() != 0) {
			CausaleEntrata causaleEntrata = ComparatorUtils.searchByUid(getListaCausaleEntrata(), getCausaleEntrata());
			sb.append("Causale: ").append(causaleEntrata.getCodice()).append(" - ");
		} else if(Boolean.TRUE.equals(getFlagCausaleEntrataMancante())) {
			sb.append("Causale: Mancante - ");
		}
		if(getContoCorrente() != null && getContoCorrente().getUid() != 0) {
			ContoCorrentePredocumentoEntrata contoCorrente = ComparatorUtils.searchByUid(getListaContoCorrente(), getContoCorrente());
			sb.append("Conto corrente: ").append(contoCorrente.getDescrizione()).append(" - ");
		}else if(Boolean.TRUE.equals(getFlagContoCorrenteMancante())) {
			sb.append("Conto corrente: Mancante - ");
		}
		if(getStatoOperativoPreDocumento() != null){
			sb.append("Stato operativo: ").append(getStatoOperativoPreDocumento().getDescrizione()).append(" - ");
		}
		if(getPreDocumento() != null && getPreDocumento().getImporto() != null){
			sb.append("Importo: ").append(FormatUtils.formatCurrency(getPreDocumento().getImporto())).append(" - ");
		}
		if(getDatiAnagraficiPreDocumento() != null) {
			DatiAnagraficiPreDocumento dapd = getDatiAnagraficiPreDocumento();
			if(StringUtils.isNotBlank(dapd.getRagioneSociale())) {
				sb.append("Ragione sociale: ").append(dapd.getRagioneSociale()).append(" - ");
			}
			if(StringUtils.isNotBlank(dapd.getCognome())) {
				sb.append("Cognome: ").append(dapd.getCognome()).append(" - ");
			}
			if(StringUtils.isNotBlank(dapd.getNome())) {
				sb.append("Nome: ").append(dapd.getNome()).append(" - ");
			}
			if(StringUtils.isNotBlank(dapd.getCodiceFiscale())) {
				sb.append("Codice fiscale: ").append(dapd.getCodiceFiscale()).append(" - ");
			}
			if(StringUtils.isNotBlank(dapd.getPartitaIva())) {
				sb.append("Partita IVA: ").append(dapd.getPartitaIva()).append(" - ");
			}
		}
		sb.append(componiStringaCapitolo(getCapitolo()));
		sb.append(componiStringaMovimentoGestione(BilConstants.ACCERTAMENTO, getMovimentoGestione(), getSubMovimentoGestione()));
		sb.append(componiStringaSoggetto(getSoggetto(), getFlagSoggettoMancante()));
		sb.append(componiStringaAttoAmministrativo(getAttoAmministrativo(), getTipoAtto(), getStrutturaAmministrativoContabileAttoAmministrativo(),
				getFlagAttoAmministrativoMancante(), listaStrutturaAmministrativoContabile, getListaTipoAtto()));
		sb.append(componiStringaDocumento(getDocumento(), getTipoDocumento(), getListaTipoDocumento()));
		sb.append(componiStringaOrdinativo());
		if(Boolean.TRUE.equals(getFlagEstraiNonIncassato())) {
			sb.append("Non incassato - ");
		}
		if(Boolean.TRUE.equals(getFlagNonAnnullati())) {
			sb.append("Non annullati - ");
		}
		
		return StringUtils.substringBeforeLast(sb.toString(), "-");
	}

}
