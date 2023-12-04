/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleSpesa;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.ContoTesoreria;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiave;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Classe di model generica per il PreDocumento di Spesa
 * 
 * @author Marchino Alessandro,Nazha Ahmad
 * @version 1.0.0 - 15/04/2014
 * @version 1.0.1 - 11/06/2015
 *
 */
public class GenericPreDocumentoSpesaModel extends GenericPreDocumentoModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = 8224877594681380300L;

	private PreDocumentoSpesa preDocumento;
	private DatiAnagraficiPreDocumentoSpesa datiAnagraficiPreDocumento;
	private CausaleSpesa causaleSpesa;
	private ContoTesoreria contoTesoreria;
	private CapitoloUscitaGestione capitolo;
	private Impegno movimentoGestione;
	private Impegno movimentoGestioneOrdinativo;

	
	private SubImpegno subMovimentoGestione;
	private SedeSecondariaSoggetto sedeSecondariaSoggetto;
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggetto;
	
    
	private List<CausaleSpesa> listaCausaleSpesa = new ArrayList<CausaleSpesa>();
	private List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto = new ArrayList<SedeSecondariaSoggetto>();
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto = new ArrayList<ModalitaPagamentoSoggetto>();


	/**
	 * @return the preDocumento
	 */
	public PreDocumentoSpesa getPreDocumento() {
		return preDocumento;
	}

	/**
	 * @param preDocumento the preDocumento to set
	 */
	public void setPreDocumento(PreDocumentoSpesa preDocumento) {
		this.preDocumento = preDocumento;
	}

	/**
	 * @return the datiAnagraficiPreDocumento
	 */
	public DatiAnagraficiPreDocumentoSpesa getDatiAnagraficiPreDocumento() {
		return datiAnagraficiPreDocumento;
	}

	/**
	 * @param datiAnagraficiPreDocumento the datiAnagraficiPreDocumento to set
	 */
	public void setDatiAnagraficiPreDocumento(DatiAnagraficiPreDocumentoSpesa datiAnagraficiPreDocumento) {
		this.datiAnagraficiPreDocumento = datiAnagraficiPreDocumento;
	}

	/**
	 * @return the causaleSpesa
	 */
	public CausaleSpesa getCausaleSpesa() {
		return causaleSpesa;
	}

	/**
	 * @param causaleSpesa the causaleSpesa to set
	 */
	public void setCausaleSpesa(CausaleSpesa causaleSpesa) {
		this.causaleSpesa = causaleSpesa;
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
	 * @return the capitolo
	 */
	public CapitoloUscitaGestione getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo the capitolo to set
	 */
	public void setCapitolo(CapitoloUscitaGestione capitolo) {
		this.capitolo = capitolo;
	}

	/**
	 * @return the movimentoGestione
	 */
	public Impegno getMovimentoGestione() {
		return movimentoGestione;
	}

	/**
	 * @param movimentoGestione the movimentoGestione to set
	 */
	public void setMovimentoGestione(Impegno movimentoGestione) {
		this.movimentoGestione = movimentoGestione;
	}

	/**
	 * @return the subMovimentoGestione
	 */
	public SubImpegno getSubMovimentoGestione() {
		return subMovimentoGestione;
	}

	/**
	 * @param subMovimentoGestione the subMovimentoGestione to set
	 */
	public void setSubMovimentoGestione(SubImpegno subMovimentoGestione) {
		this.subMovimentoGestione = subMovimentoGestione;
	}

	/**
	 * @return the sedeSecondariaSoggetto
	 */
	public SedeSecondariaSoggetto getSedeSecondariaSoggetto() {
		return sedeSecondariaSoggetto;
	}

	/**
	 * @param sedeSecondariaSoggetto the sedeSecondariaSoggetto to set
	 */
	public void setSedeSecondariaSoggetto(SedeSecondariaSoggetto sedeSecondariaSoggetto) {
		this.sedeSecondariaSoggetto = sedeSecondariaSoggetto;
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
	 * @return the listaCausaleSpesa
	 */
	public List<CausaleSpesa> getListaCausaleSpesa() {
		return listaCausaleSpesa;
	}

	/**
	 * @param listaCausaleSpesa the listaCausaleSpesa to set
	 */
	public void setListaCausaleSpesa(List<CausaleSpesa> listaCausaleSpesa) {
		this.listaCausaleSpesa = listaCausaleSpesa != null ? listaCausaleSpesa : new ArrayList<CausaleSpesa>();
	}

	/**
	 * @return the listaSecondariaSoggetto
	 */
	public List<SedeSecondariaSoggetto> getListaSedeSecondariaSoggetto() {
		return listaSedeSecondariaSoggetto;
	}

	/**
	 * @param listaSedeSecondariaSoggetto the listaSecondariaSoggetto to set
	 */
	public void setListaSedeSecondariaSoggetto(List<SedeSecondariaSoggetto> listaSedeSecondariaSoggetto) {
		this.listaSedeSecondariaSoggetto = listaSedeSecondariaSoggetto != null ? listaSedeSecondariaSoggetto : new ArrayList<SedeSecondariaSoggetto>();
	}

	/**
	 * @return the listaModalitaPagamentoSoggetto
	 */
	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggetto() {
		return listaModalitaPagamentoSoggetto;
	}

	/**
	 * @param listaModalitaPagamentoSoggetto the listaModalitaPagamentoSoggetto to set
	 */
	public void setListaModalitaPagamentoSoggetto(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggetto) {
		this.listaModalitaPagamentoSoggetto = listaModalitaPagamentoSoggetto != null ? listaModalitaPagamentoSoggetto : new ArrayList<ModalitaPagamentoSoggetto>();
	}

	/**
	 * @return the provinciaIndirizzo
	 */
	public String getProvinciaIndirizzo() {
		// Se non ho il comune, non ho la provincia
		if(getDatiAnagraficiPreDocumento() == null || StringUtils.isEmpty(getDatiAnagraficiPreDocumento().getComuneIndirizzo())) {
			return "";
		}
		// Cerco il comune di indirizzo
		for(ComuneNascita cn : getListaComuni()) {
			if(getDatiAnagraficiPreDocumento().getComuneIndirizzo().equals(cn.getDescrizione())) {
				return cn.getSiglaProvincia();
			}
		}
		// Se non lo trovo, non restituisco alcunche'
		return "";
	}
	
	/**
	 * @return the provinciaNascita
	 */
	public String getProvinciaNascita() {
		// Se non ho il comune, non ho la provincia
		if(getDatiAnagraficiPreDocumento() == null || StringUtils.isEmpty(getDatiAnagraficiPreDocumento().getComuneNascita())) {
			return "";
		}
		// Cerco il comune di nascita
		for(ComuneNascita cn : getListaComuni()) {
			if(getDatiAnagraficiPreDocumento().getComuneNascita().equals(cn.getDescrizione())) {
				return cn.getSiglaProvincia();
			}
		}
		// Se non lo trovo, non restituisco alcunche'
		return "";
	}

	/**
	 * Costruisce il titolo della predisposizione.
	 * 
	 * @return il titolo
	 */
	public String getTitoloPredisposizione() {
		StringBuilder sb = new StringBuilder();
		sb.append("Predisposizione di Pagamento Numero ").append(preDocumento.getNumero()).append(" Stato Operativo ").append(preDocumento.getStatoOperativoPreDocumento().getDescrizione());

		// Se lo stato è completo, segno la data di completamento
		if (StatoOperativoPreDocumento.COMPLETO.equals(preDocumento.getStatoOperativoPreDocumento())) {
			sb.append("<br/>").append("Completata il ").append(FormatUtils.formatDate(preDocumento.getDataCompletamento()));
		}

		// Segno l'utenza di creazione
		sb.append("<br/>").append("Creata da ").append(preDocumento.getLoginCreazione()).append(" ").append(Boolean.TRUE.equals(preDocumento.getFlagManuale()) ? "Manuale" : "Automatico");

		// Se è stato aggiornato, segno l'utenza di aggiornamento
		if (!preDocumento.getDataCreazione().equals(preDocumento.getDataModifica())) {
			sb.append("<br/>").append("Aggiornata da ").append(preDocumento.getLoginModifica());
		}

		return sb.toString();
	}
	
	/**
	 * @return the datiRiferimentoMovimentoGestione
	 */
	public String getDatiRiferimentoMovimentoGestione() {
		return computeDatiRiferimentoMovimentoGestione(getMovimentoGestione(), getSubMovimentoGestione());
	}

	/* ***** Requests ***** */

	/**
	 * Crea una request per il servizio di
	 * {@link RicercaSinteticaCapitoloUscitaGestione}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaCapitoloUscitaGestione creaRequestRicercaSinteticaCapitoloUscitaGestione() {
		RicercaSinteticaCapitoloUscitaGestione request = new RicercaSinteticaCapitoloUscitaGestione();

		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setParametriPaginazione(creaParametriPaginazione());
		request.setRicercaSinteticaCapitoloUGest(creaRicercaSinteticaCapitoloUGest());

		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiave}.
	 * 
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato() {
		RicercaImpegnoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaImpegnoPerChiaveOttimizzato.class);
		request.setEnte(getEnte());
		request.setpRicercaImpegnoK(creaPRicercaImpegnoK());
		//carico i sub solo se ho il numero del sub valorizzato
		request.setCaricaSub(getSubMovimentoGestione() != null && getSubMovimentoGestione().getNumeroBigDecimal() != null);
		request.setSubPaginati(true);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link LeggiTipiCausaleSpesa}.
	 * 
	 * @return la request creata
	 */
	public LeggiTipiCausaleSpesa creaRequestLeggiTipiCausaleSpesa() {
		LeggiTipiCausaleSpesa request = new LeggiTipiCausaleSpesa();

		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());

		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaCausaleSpesa}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaCausaleSpesa creaRequestRicercaSinteticaCausaleSpesa() {
		RicercaSinteticaCausaleSpesa request = new RicercaSinteticaCausaleSpesa();

		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setCausaleSpesa(creaCausaleSpesa());
		request.setParametriPaginazione(creaParametriPaginazione());

		return request;
	}

	/**
	 * Crea un'utilit&agrave; per la ricerca sintetica.
	 * 
	 * @return l'utility creata
	 */
	private RicercaSinteticaCapitoloUGest creaRicercaSinteticaCapitoloUGest() {
		RicercaSinteticaCapitoloUGest utility = new RicercaSinteticaCapitoloUGest();

		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setAnnoCapitolo(getCapitolo().getAnnoCapitolo());
		utility.setNumeroCapitolo(getCapitolo().getNumeroCapitolo());
		utility.setNumeroArticolo(getCapitolo().getNumeroArticolo());
		utility.setNumeroUEB(getCapitolo().getNumeroUEB());

		return utility;
	}

	/**
	 * Crea un'utilit&agrave; per la ricerca impegno.
	 * 
	 * @return l'utility creata
	 */
	private RicercaImpegnoK creaPRicercaImpegnoK() {
		RicercaImpegnoK utility = new RicercaImpegnoK();

		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setAnnoImpegno(getMovimentoGestione().getAnnoMovimento());
		utility.setNumeroImpegno(getMovimentoGestione().getNumeroBigDecimal());

		//SubImpegno
		utility.setNumeroSubDaCercare((getSubMovimentoGestione() != null && getSubMovimentoGestione().getNumeroBigDecimal() != null) ? getSubMovimentoGestione().getNumeroBigDecimal() : null);

		return utility;
	}

	/**
	 * Imposta la causale.
	 * 
	 * @return la causale per la ricerca
	 */
	private CausaleSpesa creaCausaleSpesa() {
		CausaleSpesa causale = new CausaleSpesa();

		causale.setTipoCausale(getTipoCausale());
		causale.setStrutturaAmministrativoContabile(impostaStrutturaAmministrativoContabile());
		causale.setEnte(getEnte());

		return causale;
	}



	/**
	 * @return the movimentoGestioneOrdinativo
	 */
	public Impegno getMovimentoGestioneOrdinativo() {
		return movimentoGestioneOrdinativo;
	}

	/**
	 * @param movimentoGestioneOrdinativo the movimentoGestioneOrdinativo to set
	 */
	public void setMovimentoGestioneOrdinativo(Impegno movimentoGestioneOrdinativo) {
		this.movimentoGestioneOrdinativo = movimentoGestioneOrdinativo;
	}

	
	
}
