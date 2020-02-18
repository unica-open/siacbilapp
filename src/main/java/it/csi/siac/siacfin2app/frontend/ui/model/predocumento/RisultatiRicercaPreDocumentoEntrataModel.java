/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.predocumento;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AggiornaDataTrasmissionePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AnnullaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.AssociaImputazioniContabiliVariatePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.DefiniscePreDocumentoEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaPreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;

/**
 * Model per la visualizzazione dei risultati di ricerca per il PreDocumento di Entrata.
 * 
 * @author Alessandro Marchino
 * @version 1.0.1 - 06/05/2014
 * 
 */
public class RisultatiRicercaPreDocumentoEntrataModel extends RisultatiRicercaPreDocumentoGenericModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -5914869517078484450L;
	
	private BigDecimal importoTotale;
	// SIAC-4280
	private CausaleEntrata causaleEntrata;
	private CapitoloEntrataGestione capitolo;
	private Accertamento movimentoGestione;
	private SubAccertamento subMovimentoGestione;
	
	// SIAC-4382
	private boolean utenteDecentrato;
	// SIAC-4383
	private Date dataTrasmissione;
	private Boolean dataTrasmissioneAbilitato;
	
	//4310
	private boolean forzaDisponibilitaAccertamento;
	// SIAC-5041
	private boolean modificaAccertamentoDisponibile;

	/** Costruttore vuoto di default */
	public RisultatiRicercaPreDocumentoEntrataModel() {
		super();
		setTitolo("Risultati ricerca Predisposizione di Incasso");
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
	 * @return the causaleEntrata
	 */
	public CausaleEntrata getCausaleEntrata() {
		return causaleEntrata;
	}

	/**
	 * @param causaleEntrata the causaleEntrata to set
	 */
	public void setCausaleEntrata(CausaleEntrata causaleEntrata) {
		this.causaleEntrata = causaleEntrata;
	}
	
	/**
	 * @return the capitolo
	 */
	public CapitoloEntrataGestione getCapitolo() {
		return capitolo;
	}

	/**
	 * @param capitolo the capitolo to set
	 */
	public void setCapitolo(CapitoloEntrataGestione capitolo) {
		this.capitolo = capitolo;
	}

	/**
	 * @return the movimentoGestione
	 */
	public Accertamento getMovimentoGestione() {
		return movimentoGestione;
	}

	/**
	 * @param movimentoGestione the movimentoGestione to set
	 */
	public void setMovimentoGestione(Accertamento movimentoGestione) {
		this.movimentoGestione = movimentoGestione;
	}

	/**
	 * @return the subMovimentoGestione
	 */
	public SubAccertamento getSubMovimentoGestione() {
		return subMovimentoGestione;
	}

	/**
	 * @param subMovimentoGestione the subMovimentoGestione to set
	 */
	public void setSubMovimentoGestione(SubAccertamento subMovimentoGestione) {
		this.subMovimentoGestione = subMovimentoGestione;
	}

	/**
	 * @return the dataTrasmissione
	 */
	public Date getDataTrasmissione() {
		return dataTrasmissione != null ? new Date(dataTrasmissione.getTime()) : null;
	}

	/**
	 * @param dataTrasmissione the dataTrasmissione to set
	 */
	public void setDataTrasmissione(Date dataTrasmissione) {
		this.dataTrasmissione = dataTrasmissione != null ? new Date(dataTrasmissione.getTime()) : null;
	}

	/**
	 * @return the dataTrasmissioneAbilitato
	 */
	public Boolean getDataTrasmissioneAbilitato() {
		return dataTrasmissioneAbilitato;
	}

	/**
	 * @param dataTrasmissioneAbilitato the dataTrasmissioneAbilitato to set
	 */
	public void setDataTrasmissioneAbilitato(Boolean dataTrasmissioneAbilitato) {
		this.dataTrasmissioneAbilitato = dataTrasmissioneAbilitato;
	}

	/**
	 * @return the forzaDisponibilitaAccertamento
	 */
	public boolean isForzaDisponibilitaAccertamento() {
		return forzaDisponibilitaAccertamento;
	}

	/**
	 * @param forzaDisponibilitaAccertamento the forzaDisponibilitaAccertamento to set
	 */
	public void setForzaDisponibilitaAccertamento(boolean forzaDisponibilitaAccertamento) {
		this.forzaDisponibilitaAccertamento = forzaDisponibilitaAccertamento;
	}

	/**
	 * @return the modificaAccertamentoDisponibile
	 */
	public boolean isModificaAccertamentoDisponibile() {
		return modificaAccertamentoDisponibile;
	}

	/**
	 * @param modificaAccertamentoDisponibile the modificaAccertamentoDisponibile to set
	 */
	public void setModificaAccertamentoDisponibile(boolean modificaAccertamentoDisponibile) {
		this.modificaAccertamentoDisponibile = modificaAccertamentoDisponibile;
	}

	/**
	 * @return the utenteDecentrato
	 */
	public boolean isUtenteDecentrato() {
		return utenteDecentrato;
	}

	/**
	 * @param utenteDecentrato the utenteDecentrato to set
	 */
	public void setUtenteDecentrato(boolean utenteDecentrato) {
		this.utenteDecentrato = utenteDecentrato;
	}

	/**
	 * @return the riepilogoImputazioniContabili
	 */
	public String getRiepilogoImputazioniContabili() {
		if(getCausaleEntrata() == null) {
			return "";
		}
		return new StringBuilder()
				.append(calcolaRiepilogoImputazioniContabiliCapitolo(getCausaleEntrata().getCapitoloEntrataGestione()))
				.append(calcolaRiepilogoImputazioniContabiliMovimentoGestione(getCausaleEntrata().getAccertamento(), getCausaleEntrata().getSubAccertamento(), "Accertamento"))
				.append(calcolaRiepilogoImputazioniContabiliSoggetto(getCausaleEntrata().getSoggetto()))
				.append(calcolaRiepilogoImputazioniContabiliAttoAmministrativo(getCausaleEntrata().getAttoAmministrativo()))
				.toString();
	}
	
	/**
	 * @return the spanClassOperazioni
	 */
	public String getSpanClassOperazioni() {
		int j = Math.max(1, booleanToInt(getInserisciAbilitato()) + booleanToInt(getAssociaAbilitato()) + booleanToInt(getDefinisciAbilitato()) + booleanToInt(getDataTrasmissioneAbilitato()));
		return "span" + (12 / j);
	}
	
	/**
	 * Trasforma il boolean in un intero
	 * @param value il boolean
	 * @return l'intero corrispondente
	 */
	private int booleanToInt(Boolean value) {
		return Boolean.TRUE.equals(value) ? 1 : 0;
	}
	
	/* Requests */

	/**
	 * Crea una request per il servizio di {@link AnnullaPreDocumentoEntrata}.
	 * 
	 * @return la request creata
	 */
	public AnnullaPreDocumentoEntrata creaRequestAnnullaPreDocumentoEntrata() {
		AnnullaPreDocumentoEntrata request = new AnnullaPreDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		PreDocumentoEntrata preDocumento = new PreDocumentoEntrata();
		preDocumento.setUid(getUidDaAnnullare());
		preDocumento.setEnte(getEnte());
		
		request.setPreDocumentoEntrata(preDocumento);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AssociaImputazioniContabiliPreDocumentoEntrata}.
	 * 
	 * @param idOperazioneAsincrona               l'id dell'operazione asincrona
	 * @param ricercaSinteticaPreDocumentoEntrata la request della ricerca sintetica, nel caso in cui siano da inviare tutti i preDocumenti
	 * 
	 * @return la request creata
	 */
	public AssociaImputazioniContabiliPreDocumentoEntrata creaRequestAssociaImputazioniContabiliPreDocumentoEntrata(Integer idOperazioneAsincrona, 
			RicercaSinteticaPreDocumentoEntrata ricercaSinteticaPreDocumentoEntrata) {
		AssociaImputazioniContabiliPreDocumentoEntrata request = new AssociaImputazioniContabiliPreDocumentoEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		request.setPreDocumentiEntrata(creaPreDocumentiEntrataDaListaUid(getListaUid()));
		request.setBilancio(getBilancio());
		request.setEnte(getEnte());
		request.setIdOperazioneAsincrona(idOperazioneAsincrona);
		request.setGestisciModificaImportoAccertamento(forzaDisponibilitaAccertamento);
		
		if(Boolean.TRUE.equals(getInviaTutti())) {
			request.setRicercaSinteticaPreDocumentoEntrata(ricercaSinteticaPreDocumentoEntrata);
		}
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link AssociaImputazioniContabiliVariatePreDocumentoEntrata}.
	 * 
	 * @param ricercaSinteticaPreDocumentoEntrata la request della ricerca sintetica, nel caso in cui siano da inviare tutti i preDocumenti
	 * 
	 * @return la request creata
	 */
	public AssociaImputazioniContabiliVariatePreDocumentoEntrata creaRequestAssociaImputazioniContabiliVariatePreDocumentoEntrata(RicercaSinteticaPreDocumentoEntrata ricercaSinteticaPreDocumentoEntrata) {
		AssociaImputazioniContabiliVariatePreDocumentoEntrata request = creaRequest(AssociaImputazioniContabiliVariatePreDocumentoEntrata.class);
		
		request.setPreDocumentiEntrata(creaPreDocumentiEntrataDaListaUid(getListaUid()));
		request.setBilancio(getBilancio());
		
		if(Boolean.TRUE.equals(getInviaTutti())) {
			request.setRicercaSinteticaPreDocumentoEntrata(ricercaSinteticaPreDocumentoEntrata);
		}
		
		request.setCapitoloEntrataGestione(getCapitolo());
		request.setAccertamento(getMovimentoGestione());
		request.setSubAccertamento(getSubMovimentoGestione());
		request.setSoggetto(getSoggetto());
		request.setAttoAmministrativo(getAttoAmministrativo());
		request.setGestisciModificaImportoAccertamento(forzaDisponibilitaAccertamento);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link DefiniscePreDocumentoEntrata}.
	 * @param ricercaSinteticaPreDocumentoEntrata la request della ricerca sintetica, nel caso in cui siano da inviare tutti i preDocumenti
	 * @return la request creata
	 */
	public DefiniscePreDocumentoEntrata creaRequestDefiniscePreDocumentoEntrata(RicercaSinteticaPreDocumentoEntrata ricercaSinteticaPreDocumentoEntrata) {
		DefiniscePreDocumentoEntrata request = creaRequest(DefiniscePreDocumentoEntrata.class);
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		request.setPreDocumentiEntrata(creaPreDocumentiEntrataDaListaUid(getListaUid()));
		request.setBilancio(getBilancio());
		
		if(Boolean.TRUE.equals(getInviaTutti())) {
			request.setRicercaSinteticaPreDocumentoEntrata(ricercaSinteticaPreDocumentoEntrata);
		}
		
		return request;
	}
	
	/**
	 * Crea una lista di preDocumenti di entrata a partire dagli uid.
	 * 
	 * @param listaUid la lista degli uid
	 * 
	 * @return la lista dei prodocumenti relativi agli uid
	 */
	private List<PreDocumentoEntrata> creaPreDocumentiEntrataDaListaUid(List<Integer> listaUid) {
		return creaPreDocumentiDaListaUid(listaUid, PreDocumentoEntrata.class);
	}

	/**
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiaveOttimizzato}.
	 * 
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato() {
		RicercaAccertamentoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaAccertamentoPerChiaveOttimizzato.class);
		
		request.setEnte(getEnte());
		request.setCaricaSub(getSubMovimentoGestione() != null && getSubMovimentoGestione().getNumero() != null);
		request.setSubPaginati(true);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		RicercaAccertamentoK pRicercaAccertamentoK = new RicercaAccertamentoK();
		pRicercaAccertamentoK.setAnnoEsercizio(getAnnoEsercizioInt());
		pRicercaAccertamentoK.setAnnoAccertamento(getMovimentoGestione().getAnnoMovimento());
		pRicercaAccertamentoK.setNumeroAccertamento(getMovimentoGestione().getNumero());
		pRicercaAccertamentoK.setNumeroSubDaCercare((getSubMovimentoGestione() != null && getSubMovimentoGestione().getNumero() != null) ? getSubMovimentoGestione().getNumero() : null);
		request.setpRicercaAccertamentoK(pRicercaAccertamentoK);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaCapitoloEntrataGestione}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaCapitoloEntrataGestione creaRequestRicercaSinteticaCapitoloEntrataGestione() {
		RicercaSinteticaCapitoloEntrataGestione request = creaRequest(RicercaSinteticaCapitoloEntrataGestione.class);
		
		request.setEnte(getEnte());
		request.setParametriPaginazione(creaParametriPaginazione(10000));
		
		RicercaSinteticaCapitoloEGest ricercaSinteticaCapitoloEGest = new RicercaSinteticaCapitoloEGest();
		ricercaSinteticaCapitoloEGest.setAnnoEsercizio(getAnnoEsercizioInt());
		ricercaSinteticaCapitoloEGest.setAnnoCapitolo(getCapitolo().getAnnoCapitolo());
		ricercaSinteticaCapitoloEGest.setNumeroCapitolo(getCapitolo().getNumeroCapitolo());
		ricercaSinteticaCapitoloEGest.setNumeroArticolo(getCapitolo().getNumeroArticolo());
		ricercaSinteticaCapitoloEGest.setNumeroUEB(getCapitolo().getNumeroUEB());
		request.setRicercaSinteticaCapitoloEntrata(ricercaSinteticaCapitoloEGest);
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link AggiornaDataTrasmissionePreDocumentoEntrata}.
	 * @param ricercaSinteticaPreDocumentoEntrata la ricerca sintetica da utilizzare nella creazione della request
	 * @return la request creata
	 */
	public AggiornaDataTrasmissionePreDocumentoEntrata creaRequestAggiornaDataTrasmissionePreDocumentoEntrata(RicercaSinteticaPreDocumentoEntrata ricercaSinteticaPreDocumentoEntrata) {
		AggiornaDataTrasmissionePreDocumentoEntrata request = creaRequest(AggiornaDataTrasmissionePreDocumentoEntrata.class);
		
		request.setPreDocumentiEntrata(creaPreDocumentiEntrataDaListaUid(getListaUid()));
		request.setDataTrasmissione(getDataTrasmissione());
		
		if(Boolean.TRUE.equals(getInviaTutti())) {
			request.setRicercaSinteticaPreDocumentoEntrata(ricercaSinteticaPreDocumentoEntrata);
		}
		
		return request;
	}

}
