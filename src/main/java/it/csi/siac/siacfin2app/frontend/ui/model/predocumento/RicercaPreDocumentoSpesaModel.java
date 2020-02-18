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
import it.csi.siac.siaccorser.model.StrutturaAmministrativoContabile;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.DocumentoSpesa;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfin2ser.model.TipoCausale;
import it.csi.siac.siacfin2ser.model.TipoDocumento;
import it.csi.siac.siacfinser.model.ordinativo.OrdinativoPagamento;

/**
 * Classe di model per la ricerca del PreDocumento di Spesa
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/04/2014
 *
 */
public class RicercaPreDocumentoSpesaModel extends GenericPreDocumentoSpesaModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -5080609141854299761L;
	
	private static final int ELEMENTI_PER_PAGINA_RICERCA_SINTETICA = 50;
	
	private Date dataCompetenzaDa;
	private Date dataCompetenzaA;
	private StatoOperativoPreDocumento statoOperativoPreDocumento;
	private TipoDocumento tipoDocumento;
	private DocumentoSpesa documento;
	
	private Boolean flagCausaleSpesaMancante = Boolean.FALSE;
	private Boolean flagContoTesoreriaMancante = Boolean.FALSE;
	private Boolean flagSoggettoMancante = Boolean.FALSE;
	private Boolean flagAttoAmministrativoMancante = Boolean.FALSE;
	private Boolean flagEstraiNonPagato = Boolean.FALSE;
	// SIAC-4620
	private Boolean flagNonAnnullati = Boolean.FALSE;
	
	private List<StatoOperativoPreDocumento> listaStatoOperativoPreDocumento = new ArrayList<StatoOperativoPreDocumento>();
	private List<TipoDocumento> listaTipoDocumento = new ArrayList<TipoDocumento>();
	
	// SIAC-4722
	private OrdinativoPagamento ordinativo;
	
	/** Costruttore vuoto di default */
	public RicercaPreDocumentoSpesaModel() {
		setTitolo("Ricerca Predisposizione di Pagamento");
		setNomeAzioneDecentrata(BilConstants.RICERCA_PREDOCUMENTO_SPESA_DECENTRATO.getConstant());
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
	public DocumentoSpesa getDocumento() {
		return documento;
	}

	/**
	 * @param documento the documento to set
	 */
	public void setDocumento(DocumentoSpesa documento) {
		this.documento = documento;
	}

	/**
	 * @return the flagCausaleSpesaMancante
	 */
	public Boolean getFlagCausaleSpesaMancante() {
		return flagCausaleSpesaMancante;
	}

	/**
	 * @param flagCausaleSpesaMancante the flagCausaleSpesaMancante to set
	 */
	public void setFlagCausaleSpesaMancante(Boolean flagCausaleSpesaMancante) {
		this.flagCausaleSpesaMancante = flagCausaleSpesaMancante != null ? flagCausaleSpesaMancante : Boolean.FALSE;
	}

	/**
	 * @return the flagContoTesoreriaMancante
	 */
	public Boolean getFlagContoTesoreriaMancante() {
		return flagContoTesoreriaMancante;
	}

	/**
	 * @param flagContoTesoreriaMancante the flagContoTesoreriaMancante to set
	 */
	public void setFlagContoTesoreriaMancante(Boolean flagContoTesoreriaMancante) {
		this.flagContoTesoreriaMancante = flagContoTesoreriaMancante != null ? flagContoTesoreriaMancante : Boolean.FALSE;
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
	 * @return the flagEstraiNonPagato
	 */
	public Boolean getFlagEstraiNonPagato() {
		return flagEstraiNonPagato;
	}

	/**
	 * @param flagEstraiNonPagato the flagEstraiNonPagato to set
	 */
	public void setFlagEstraiNonPagato(Boolean flagEstraiNonPagato) {
		this.flagEstraiNonPagato = flagEstraiNonPagato != null ? flagEstraiNonPagato : Boolean.FALSE;
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
	public OrdinativoPagamento getOrdinativo() {
		return ordinativo;
	}

	/**
	 * @param ordinativo the ordinativo to set
	 */
	public void setOrdinativo(OrdinativoPagamento ordinativo) {
		this.ordinativo = ordinativo;
	}

	/* ***** Requests ***** */

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaPreDocumentoSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaPreDocumentoSpesa creaRequestRicercaSinteticaPreDocumentoSpesa() {
		RicercaSinteticaPreDocumentoSpesa request = new RicercaSinteticaPreDocumentoSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		request.setParametriPaginazione(creaParametriPaginazione());
		
		// Flags
		request.setDataCompetenzaA(getDataCompetenzaA());
		request.setDataCompetenzaDa(getDataCompetenzaDa());
		request.setCausaleSpesaMancante(getFlagCausaleSpesaMancante());
		request.setContoTesoreriaMancante(getFlagContoTesoreriaMancante());
		request.setSoggettoMancante(getFlagSoggettoMancante());
		request.setProvvedimentoMancante(getFlagAttoAmministrativoMancante());
		request.setEstraiNonPagato(getFlagEstraiNonPagato());
		request.setTipoCausale(getTipoCausale());
		
		request.setPreDocumentoSpesa(popolaPreDocumentoSpesa());
		
		request.setDocumento(popolaDocumentoSeDatiPresenti(getDocumento(), getTipoDocumento()));
		
		// SIAC-4620
		request.setNonAnnullati(getFlagNonAnnullati());
		// SIAC-4772
		request.setOrdinativoPagamento(getOrdinativo());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaDocumentoSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaDocumentoSpesa creaRequestRicercaSinteticaDocumentoSpesa() {
		RicercaSinteticaDocumentoSpesa request = new RicercaSinteticaDocumentoSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setParametriPaginazione(super.creaParametriPaginazione());
		request.setDocumentoSpesa(popolaDocumentoSpesa());
		
		return request;
	}
	
	/**
	 * Popola il preDocumento di spesa per l'injezione della request di ricerca sintetica.
	 * 
	 * @return il preDocumento creato
	 */
	private PreDocumentoSpesa popolaPreDocumentoSpesa() {
		PreDocumentoSpesa preDocumento = getPreDocumento();
		preDocumento.setEnte(getEnte());
		preDocumento.setStatoOperativoPreDocumento(getStatoOperativoPreDocumento());
		preDocumento.setDatiAnagraficiPreDocumento(getDatiAnagraficiPreDocumento());
		
		preDocumento.setCausaleSpesa(impostaEntitaFacoltativa(getCausaleSpesa()));
		preDocumento.setContoTesoreria(impostaEntitaFacoltativa(getContoTesoreria()));
		
		preDocumento.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		preDocumento.setCapitoloUscitaGestione(impostaEntitaFacoltativa(getCapitolo()));
		preDocumento.setImpegno(impostaEntitaFacoltativa(getMovimentoGestione()));
		preDocumento.setSubImpegno(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		preDocumento.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		preDocumento.setSedeSecondariaSoggetto(impostaEntitaFacoltativa(getSedeSecondariaSoggetto()));
		preDocumento.setModalitaPagamentoSoggetto(impostaEntitaFacoltativa(getModalitaPagamentoSoggetto()));
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
	private DocumentoSpesa popolaDocumentoSpesa() {
		DocumentoSpesa documentoSpesa = getDocumento();
		
		documentoSpesa.setEnte(getEnte());
		documentoSpesa.setTipoDocumento(getTipoDocumento());
		
		return documentoSpesa;
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
		if(getStrutturaAmministrativoContabile() != null && getStrutturaAmministrativoContabile().getUid() != 0) {
			StrutturaAmministrativoContabile sac = ComparatorUtils.searchByUidWithChildren(listaStrutturaAmministrativoContabile, getStrutturaAmministrativoContabile());
			sb.append("Struttura Amministrativa: ").append(sac.getCodice()).append(" - ");
		}
		if(getTipoCausale() != null && getTipoCausale().getUid() != 0) {
			TipoCausale tipoCausale = ComparatorUtils.searchByUid(getListaTipoCausale(), getTipoCausale());
			sb.append("Tipo causale: ").append(tipoCausale.getCodice()).append(" - ");
		}
		if(getCausaleSpesa() != null && getCausaleSpesa().getUid() != 0) {
			CausaleSpesa causaleSpesa = ComparatorUtils.searchByUid(getListaCausaleSpesa(), getCausaleSpesa());
			sb.append("Causale: ").append(causaleSpesa.getCodice()).append(" - ");
		} else if(Boolean.TRUE.equals(getFlagCausaleSpesaMancante())) {
			sb.append("Causale: Mancante - ");
		}
		if(getContoTesoreria() != null && getContoTesoreria().getUid() != 0) {
			ContoTesoreria contoTesoreria = ComparatorUtils.searchByUid(getListaContoTesoreria(), getContoTesoreria());
			sb.append("Conto del tesoriere: ").append(contoTesoreria.getCodice()).append(" - ");
		}else if(Boolean.TRUE.equals(getFlagContoTesoreriaMancante())) {
			sb.append("Conto del tesoriere: Mancante - ");
		}
		if(getStatoOperativoPreDocumento() != null){
			sb.append("Stato operativo: ").append(getStatoOperativoPreDocumento().getDescrizione()).append(" - ");
		}
		if(getPreDocumento() != null && getPreDocumento().getImporto() != null){
			sb.append("Importo: ").append(FormatUtils.formatCurrency(getPreDocumento().getImporto())).append(" - ");
		}
		if(getDatiAnagraficiPreDocumento() != null) {
			DatiAnagraficiPreDocumentoSpesa dapds = getDatiAnagraficiPreDocumento();
			if(StringUtils.isNotBlank(dapds.getRagioneSociale())) {
				sb.append("Ragione sociale: ").append(dapds.getRagioneSociale()).append(" - ");
			}
			if(StringUtils.isNotBlank(dapds.getCognome())) {
				sb.append("Cognome: ").append(dapds.getCognome()).append(" - ");
			}
			if(StringUtils.isNotBlank(dapds.getNome())) {
				sb.append("Nome: ").append(dapds.getNome()).append(" - ");
			}
			if(StringUtils.isNotBlank(dapds.getCodiceFiscale())) {
				sb.append("Codice fiscale: ").append(dapds.getCodiceFiscale()).append(" - ");
			}
			if(StringUtils.isNotBlank(dapds.getPartitaIva())) {
				sb.append("Partita IVA: ").append(dapds.getPartitaIva()).append(" - ");
			}
		}
		sb.append(componiStringaCapitolo(getCapitolo()));
		sb.append(componiStringaMovimentoGestione(BilConstants.IMPEGNO, getMovimentoGestione(), getSubMovimentoGestione()));
		sb.append(componiStringaSoggetto(getSoggetto(), getFlagSoggettoMancante()));
		sb.append(componiStringaAttoAmministrativo(getAttoAmministrativo(), getTipoAtto(), getStrutturaAmministrativoContabileAttoAmministrativo(),
				getFlagAttoAmministrativoMancante(), listaStrutturaAmministrativoContabile, getListaTipoAtto()));
		sb.append(componiStringaDocumento(getDocumento(), getTipoDocumento(), getListaTipoDocumento()));
		sb.append(componiStringaOrdinativo());
		if(Boolean.TRUE.equals(getFlagEstraiNonPagato())) {
			sb.append("Non pagato - ");
		}
		if(Boolean.TRUE.equals(getFlagNonAnnullati())) {
			sb.append("Non annullati - ");
		}
		
		return StringUtils.substringBeforeLast(sb.toString(), "-");
	}
	
}
