/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloUGest;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoSpesa;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoSpesa;
import it.csi.siac.siacfin2ser.model.CausaleSpesa;
import it.csi.siac.siacfin2ser.model.PreDocumentoSpesa;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;
import it.csi.siac.siacfinser.model.soggetto.sedesecondaria.SedeSecondariaSoggetto;

/**
 * Model per la visualizzazione dei risultati di ricerca per il PreDocumento di Spesa.
 * 
 * @author Alessandro Marchino
 * @version 1.0.1 - 06/05/2014
 * 
 */
public class RisultatiRicercaPreDocumentoSpesaModel extends RisultatiRicercaPreDocumentoGenericModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -1487933932329611800L;
	
	private BigDecimal importoTotale;
	// SIAC-4280
	private CausaleSpesa causaleSpesa;
	private CapitoloUscitaGestione capitolo;
	private Impegno movimentoGestione;
	private SubImpegno subMovimentoGestione;
	private SedeSecondariaSoggetto sedeSecondariaSoggetto;
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggetto;

	/** Costruttore vuoto di default */
	public RisultatiRicercaPreDocumentoSpesaModel() {
		super();
		setTitolo("Risultati ricerca Predisposizione di Pagamento");
	}
	
	/**
	 * @return the importoTotale
	 */
	public BigDecimal getImportoTotale() {
		return importoTotale;
	}

	/**
	 * @param importoTotale the importoTotale to set
	 */
	public void setImportoTotale(BigDecimal importoTotale) {
		this.importoTotale = importoTotale;
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
	 * @return the riepilogoImputazioniContabili
	 */
	public String getRiepilogoImputazioniContabili() {
		if(getCausaleSpesa() == null) {
			return "";
		}
		return new StringBuilder()
				.append(calcolaRiepilogoImputazioniContabiliCapitolo(getCausaleSpesa().getCapitoloUscitaGestione()))
				.append(calcolaRiepilogoImputazioniContabiliMovimentoGestione(getCausaleSpesa().getImpegno(), getCausaleSpesa().getSubImpegno(), "Impegno"))
				.append(calcolaRiepilogoImputazioniContabiliSoggetto(getCausaleSpesa().getSoggetto()))
				.append(calcolaRiepilogoImputazioniContabiliAttoAmministrativo(getCausaleSpesa().getAttoAmministrativo()))
				.toString();
	}

	/* Requests */
	
	/**
	 * Crea una request per il servizio di {@link AnnullaPreDocumentoSpesa}.
	 * 
	 * @return la request creata
	 */
	public AnnullaPreDocumentoSpesa creaRequestAnnullaPreDocumentoSpesa() {
		AnnullaPreDocumentoSpesa request = new AnnullaPreDocumentoSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		PreDocumentoSpesa preDocumento = new PreDocumentoSpesa();
		preDocumento.setUid(getUidDaAnnullare());
		preDocumento.setEnte(getEnte());
		
		request.setPreDocumentoSpesa(preDocumento);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AssociaImputazioniContabiliPreDocumentoSpesa}.
	 * 
	 * @param idOperazioneAsincrona             l'id dell'operazione asincrona
	 * @param ricercaSinteticaPreDocumentoSpesa la request della ricerca sintetica, nel caso in cui siano da inviare tutti i preDocumenti
	 * 
	 * @return la request creata
	 */
	public AssociaImputazioniContabiliPreDocumentoSpesa creaRequestAssociaImputazioniContabiliPreDocumentoSpesa(Integer idOperazioneAsincrona, 
			RicercaSinteticaPreDocumentoSpesa ricercaSinteticaPreDocumentoSpesa) {
		AssociaImputazioniContabiliPreDocumentoSpesa request = new AssociaImputazioniContabiliPreDocumentoSpesa();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		request.setPreDocumentiSpesa(creaPreDocumentiSpesaDaListaUid(getListaUid()));
		request.setBilancio(getBilancio());
		request.setEnte(getEnte());
		request.setIdOperazioneAsincrona(idOperazioneAsincrona);
		
		if(Boolean.TRUE.equals(getInviaTutti())) {
			request.setRicercaSinteticaPreDocumentoSpesa(ricercaSinteticaPreDocumentoSpesa);
		}
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AssociaImputazioniContabiliVariatePreDocumentoSpesa}.
	 * 
	 * @param ricercaSinteticaPreDocumentoSpesa la request della ricerca sintetica, nel caso in cui siano da inviare tutti i preDocumenti
	 * 
	 * @return la request creata
	 */
	public AssociaImputazioniContabiliVariatePreDocumentoSpesa creaRequestAssociaImputazioniContabiliVariatePreDocumentoSpesa(RicercaSinteticaPreDocumentoSpesa ricercaSinteticaPreDocumentoSpesa) {
		AssociaImputazioniContabiliVariatePreDocumentoSpesa request = creaRequest(AssociaImputazioniContabiliVariatePreDocumentoSpesa.class);
		
		request.setPreDocumentiSpesa(creaPreDocumentiSpesaDaListaUid(getListaUid()));
		request.setBilancio(getBilancio());
		
		if(Boolean.TRUE.equals(getInviaTutti())) {
			request.setRicercaSinteticaPreDocumentoSpesa(ricercaSinteticaPreDocumentoSpesa);
		}
		
		request.setCapitoloUscitaGestione(impostaEntitaFacoltativa(getCapitolo()));
		request.setImpegno(impostaEntitaFacoltativa(getMovimentoGestione()));
		request.setSubImpegno(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		request.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		request.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		request.setSedeSecondariaSoggetto(impostaEntitaFacoltativa(getSedeSecondariaSoggetto()));
		request.setModalitaPagamentoSoggetto(impostaEntitaFacoltativa(getModalitaPagamentoSoggetto()));
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link DefiniscePreDocumentoSpesa}.
	 * @param ricercaSinteticaPreDocumentoSpesa la request della ricerca sintetica, nel caso in cui siano da inviare tutti i preDocumenti
	 * @return la request creata
	 */
	public DefiniscePreDocumentoSpesa creaRequestDefiniscePreDocumentoSpesa(RicercaSinteticaPreDocumentoSpesa ricercaSinteticaPreDocumentoSpesa) {
		DefiniscePreDocumentoSpesa request = creaRequest(DefiniscePreDocumentoSpesa.class);
		
		request.setPreDocumentiSpesa(creaPreDocumentiSpesaDaListaUid(getListaUid()));
		request.setBilancio(getBilancio());
		
		if(Boolean.TRUE.equals(getInviaTutti())) {
			request.setRicercaSinteticaPreDocumentoSpesa(ricercaSinteticaPreDocumentoSpesa);
		}
		
		return request;
	}
	
	/**
	 * Crea una lista di preDocumenti di spesa a partire dagli uid.
	 * 
	 * @param listaUid la lista degli uid
	 * 
	 * @return la lista dei prodocumenti relativi agli uid
	 */
	private List<PreDocumentoSpesa> creaPreDocumentiSpesaDaListaUid(List<Integer> listaUid) {
		return creaPreDocumentiDaListaUid(listaUid, PreDocumentoSpesa.class);
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiaveOttimizzato}.
	 * 
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato() {
		RicercaImpegnoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaImpegnoPerChiaveOttimizzato.class);
		
		request.setEnte(getEnte());
		request.setCaricaSub(getSubMovimentoGestione() != null && getSubMovimentoGestione().getNumeroBigDecimal() != null);
		request.setSubPaginati(true);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		RicercaImpegnoK pRicercaImpegnoK = new RicercaImpegnoK();
		pRicercaImpegnoK.setAnnoEsercizio(getAnnoEsercizioInt());
		pRicercaImpegnoK.setAnnoImpegno(getMovimentoGestione().getAnnoMovimento());
		pRicercaImpegnoK.setNumeroImpegno(getMovimentoGestione().getNumeroBigDecimal());
		pRicercaImpegnoK.setNumeroSubDaCercare((getSubMovimentoGestione() != null && getSubMovimentoGestione().getNumeroBigDecimal() != null) ? getSubMovimentoGestione().getNumeroBigDecimal() : null);
		request.setpRicercaImpegnoK(pRicercaImpegnoK);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaCapitoloUscitaGestione}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaCapitoloUscitaGestione creaRequestRicercaSinteticaCapitoloUscitaGestione() {
		RicercaSinteticaCapitoloUscitaGestione request = creaRequest(RicercaSinteticaCapitoloUscitaGestione.class);
		
		request.setEnte(getEnte());
		request.setParametriPaginazione(creaParametriPaginazione(10000));
		
		RicercaSinteticaCapitoloUGest ricercaSinteticaCapitoloUGest = new RicercaSinteticaCapitoloUGest();
		ricercaSinteticaCapitoloUGest.setAnnoEsercizio(getAnnoEsercizioInt());
		ricercaSinteticaCapitoloUGest.setAnnoCapitolo(getCapitolo().getAnnoCapitolo());
		ricercaSinteticaCapitoloUGest.setNumeroCapitolo(getCapitolo().getNumeroCapitolo());
		ricercaSinteticaCapitoloUGest.setNumeroArticolo(getCapitolo().getNumeroArticolo());
		ricercaSinteticaCapitoloUGest.setNumeroUEB(getCapitolo().getNumeroUEB());
		request.setRicercaSinteticaCapitoloUGest(ricercaSinteticaCapitoloUGest);
		
		return request;
	}
}
