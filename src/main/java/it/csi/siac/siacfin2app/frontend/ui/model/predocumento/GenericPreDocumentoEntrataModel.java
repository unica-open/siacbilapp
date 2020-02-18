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
import it.csi.siac.siacbilser.frontend.webservice.msg.LeggiClassificatoriByTipologieClassificatori;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ContoCorrentePredocumentoEntrata;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siaccorser.model.Messaggio;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiContiCorrente;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.LeggiTipiCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrata;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.DatiAnagraficiPreDocumento;
import it.csi.siac.siacfin2ser.model.PreDocumentoEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;
import it.csi.siac.siacfin2ser.model.StatoOperativoPreDocumento;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.soggetto.ComuneNascita;

/**
 * Classe di model generica per il PreDocumento di Spesa
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 22/04/2014
 *
 */
public class GenericPreDocumentoEntrataModel extends GenericPreDocumentoModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 8224877594681380300L;
	
	private PreDocumentoEntrata preDocumento;
	private DatiAnagraficiPreDocumento datiAnagraficiPreDocumento;
	private CausaleEntrata causaleEntrata;
	private ContoCorrentePredocumentoEntrata contoCorrente;
	private CapitoloEntrataGestione capitolo;
	private Accertamento movimentoGestione;
	private Accertamento movimentoGestioneOrdinativo;
	
	private SubAccertamento subMovimentoGestione;
	// SIAC-4492
	private CausaleEntrata causaleOriginale;
	
	private List<CausaleEntrata> listaCausaleEntrata = new ArrayList<CausaleEntrata>();
	//CR-4483, il conto corrente diventa classificatore
	private List<ContoCorrentePredocumentoEntrata> listaContoCorrente = new ArrayList<ContoCorrentePredocumentoEntrata>();

	//CR-4310: prevedo la possibilita' di aggiornare l'accertamento previa conferma dell'operatore
	private boolean richiediConfermaUtente;
	private Boolean forzaDisponibilitaAccertamento;
	private String messaggioRichiestaConfermaProsecuzione = "";
	
	/**
	 * @return the preDocumento
	 */
	public PreDocumentoEntrata getPreDocumento() {
		return preDocumento;
	}

	/**
	 * @param preDocumento the preDocumento to set
	 */
	public void setPreDocumento(PreDocumentoEntrata preDocumento) {
		this.preDocumento = preDocumento;
	}

	/**
	 * @return the datiAnagraficiPreDocumento
	 */
	public DatiAnagraficiPreDocumento getDatiAnagraficiPreDocumento() {
		return datiAnagraficiPreDocumento;
	}

	/**
	 * @param datiAnagraficiPreDocumento the datiAnagraficiPreDocumento to set
	 */
	public void setDatiAnagraficiPreDocumento(DatiAnagraficiPreDocumento datiAnagraficiPreDocumento) {
		this.datiAnagraficiPreDocumento = datiAnagraficiPreDocumento;
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
	 * @return the contoCorrente
	 */
	public ContoCorrentePredocumentoEntrata getContoCorrente() {
		return contoCorrente;
	}

	/**
	 * @param contoCorrente the contoCorrente to set
	 */
	public void setContoCorrente(ContoCorrentePredocumentoEntrata contoCorrente) {
		this.contoCorrente = contoCorrente;
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
	 * @return the causaleOriginale
	 */
	public CausaleEntrata getCausaleOriginale() {
		return causaleOriginale;
	}

	/**
	 * @param causaleOriginale the causaleOriginale to set
	 */
	public void setCausaleOriginale(CausaleEntrata causaleOriginale) {
		this.causaleOriginale = causaleOriginale;
	}

	/**
	 * @return the listaCausaleEntrata
	 */
	public List<CausaleEntrata> getListaCausaleEntrata() {
		return listaCausaleEntrata;
	}

	/**
	 * @param listaCausaleEntrata the listaCausaleEntrata to set
	 */
	public void setListaCausaleEntrata(List<CausaleEntrata> listaCausaleEntrata) {
		this.listaCausaleEntrata = listaCausaleEntrata != null ? listaCausaleEntrata : new ArrayList<CausaleEntrata>();
	}

	/**
	 * @return the listaContoCorrente
	 */
	public List<ContoCorrentePredocumentoEntrata> getListaContoCorrente() {
		return listaContoCorrente;
	}

	/**
	 * @param listaContoCorrente the listaContoCorrente to set
	 */
	public void setListaContoCorrente(List<ContoCorrentePredocumentoEntrata> listaContoCorrente) {
		this.listaContoCorrente = listaContoCorrente != null ? listaContoCorrente : new ArrayList<ContoCorrentePredocumentoEntrata>();
	}
	
	
	/**
	 * @return the richiediConfermaUtente
	 */
	public boolean getRichiediConfermaUtente() {
		return richiediConfermaUtente;
	}

	/**
	 * @param richiediConfermaUtente the richiediConfermaUtente to set
	 */
	public void setRichiediConfermaUtente(boolean richiediConfermaUtente) {
		this.richiediConfermaUtente = richiediConfermaUtente;
	}

	/**
	 * @return the forzaDisponibilitaAccertamento
	 */
	public Boolean getForzaDisponibilitaAccertamento() {
		return forzaDisponibilitaAccertamento;
	}

	/**
	 * @param forzaDisponibilitaAccertamento the forzaDisponibilitaAccertamento to set
	 */
	public void setForzaDisponibilitaAccertamento(Boolean forzaDisponibilitaAccertamento) {
		this.forzaDisponibilitaAccertamento = forzaDisponibilitaAccertamento;
	}

	/**
	 * @return the messaggioRichiestaConfermaProsecuzione
	 */
	public String getMessaggioRichiestaConfermaProsecuzione() {
		return messaggioRichiestaConfermaProsecuzione;
	}

	/**
	 * @param messaggioRichiestaConfermaProsecuzione the messaggioRichiestaConfermaProsecuzione to set
	 */
	public void setMessaggioRichiestaConfermaProsecuzione(String messaggioRichiestaConfermaProsecuzione) {
		this.messaggioRichiestaConfermaProsecuzione = messaggioRichiestaConfermaProsecuzione;
	}
	
	/**
	 * Imposta il messaggio da mostrare per richiedere la conferma di prosecuzione dell'azione a partire dai messaggi presenti nel modello
	 */
	public void impostaMessaggioRichiestaConfermaProsecuzioneFromMessaggi(){
		StringBuilder sb = new StringBuilder();
		for (Messaggio msg : getMessaggi()) {
			sb.append(msg.getDescrizione())
			.append("<br>");
			
		}
		setMessaggioRichiestaConfermaProsecuzione(sb.toString());
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
		sb.append("Predisposizione di Incasso Numero ")
			.append(preDocumento.getNumero())
			.append(" Stato Operativo ")
			.append(preDocumento.getStatoOperativoPreDocumento().getDescrizione());
		
		// Se lo stato è completo, segno la data di completamento
		if(StatoOperativoPreDocumento.COMPLETO.equals(preDocumento.getStatoOperativoPreDocumento())) {
			sb.append("<br/>")
				.append("Completata il ")
				.append(FormatUtils.formatDate(preDocumento.getDataCompletamento()));
		}
		
		// Segno l'utenza di creazione
		sb.append("<br/>")
			.append("Creata da ")
			.append(preDocumento.getLoginCreazione())
			.append(" ")
			.append(Boolean.TRUE.equals(preDocumento.getFlagManuale()) ? "Manuale" : "Automatico");
		
		// Se è stato aggiornato, segno l'utenza di aggiornamento
		if(!preDocumento.getDataCreazione().equals(preDocumento.getDataModifica())) {
			sb.append("<br/>")
				.append("Aggiornata da ")
				.append(preDocumento.getLoginModifica());
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
	 * Crea una request per il servizio di {@link RicercaSinteticaCapitoloEntrataGestione}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaCapitoloEntrataGestione creaRequestRicercaSinteticaCapitoloEntrataGestione() {
		RicercaSinteticaCapitoloEntrataGestione request = new RicercaSinteticaCapitoloEntrataGestione();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		request.setParametriPaginazione(impostaParametriPaginazione());
		request.setRicercaSinteticaCapitoloEntrata(creaRicercaSinteticaCapitoloEGest());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiaveOttimizzato}.
	 * 
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato() {
		RicercaAccertamentoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaAccertamentoPerChiaveOttimizzato.class);
		
		request.setEnte(getEnte());
		request.setpRicercaAccertamentoK(creaPRicercaAccertamentoK());
		request.setCaricaSub(getSubMovimentoGestione() != null && getSubMovimentoGestione().getNumero() != null);
		request.setSubPaginati(true);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link LeggiTipiCausaleEntrata}.
	 * 
	 * @return la request creata
	 */
	public LeggiTipiCausaleEntrata creaRequestLeggiTipiCausaleEntrata() {
		LeggiTipiCausaleEntrata request = new LeggiTipiCausaleEntrata();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link LeggiContiCorrente} a partire dal model.
	 * 
	 * @return la request creata
	 */
	public LeggiContiCorrente creaRequestLeggiContiCorrente() {
		LeggiContiCorrente request = new LeggiContiCorrente();
		
		request.setDataOra(new Date());
		request.setEnte(getEnte());
		request.setRichiedente(getRichiedente());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link LeggiClassificatoriByTipologieClassificatori} a partire dal model.
	 * 
	 * @return la request creata
	 */
	public LeggiClassificatoriByTipologieClassificatori	creaRequestLeggiClassificatoriByTipologieClassificatori(){
		//CR-4483
		LeggiClassificatoriByTipologieClassificatori request = creaRequest(LeggiClassificatoriByTipologieClassificatori.class);
		request.setEnte(getEnte());
		request.setBilancio(getBilancio());
		
		List<TipologiaClassificatore> listaTipologieClassificatori = new ArrayList<TipologiaClassificatore>();
		listaTipologieClassificatori.add(TipologiaClassificatore.CONTO_CORRENTE_PREDISPOSIZIONE_INCASSO);
		
		request.setListaTipologieClassificatori(listaTipologieClassificatori);
		
		return request;
	}
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaCausaleEntrata}.
	 * @return la request creata
	 */
	public RicercaSinteticaCausaleEntrata creaRequestRicercaSinteticaCausaleEntrata() {
		RicercaSinteticaCausaleEntrata request = new RicercaSinteticaCausaleEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		request.setCausaleEntrata(creaCausaleEntrata());
		request.setParametriPaginazione(impostaParametriPaginazione());
		
		return request;
	}
	
	/**
	 * Crea un'utilit&agrave; per la ricerca sintetica.
	 * 
	 * @return l'utility creata
	 */
	private RicercaSinteticaCapitoloEGest creaRicercaSinteticaCapitoloEGest() {
		RicercaSinteticaCapitoloEGest utility = new RicercaSinteticaCapitoloEGest();
		
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
	private RicercaAccertamentoK creaPRicercaAccertamentoK() {
		RicercaAccertamentoK utility = new RicercaAccertamentoK();
		
		utility.setAnnoEsercizio(getAnnoEsercizioInt());
		utility.setAnnoAccertamento(getMovimentoGestione().getAnnoMovimento());
		utility.setNumeroAccertamento(getMovimentoGestione().getNumero());
		
		//riga in piu rispetto alla vecchia versione 
		utility.setNumeroSubDaCercare((getSubMovimentoGestione() != null && getSubMovimentoGestione().getNumero() != null) ? getSubMovimentoGestione().getNumero() : null);

		return utility;
	}
	
	/**
	 * Imposta la causale.
	 * @return la causale per la ricerca
	 */
	private CausaleEntrata creaCausaleEntrata() {
		CausaleEntrata causale = new CausaleEntrata();
		
		causale.setTipoCausale(getTipoCausale());
		causale.setStrutturaAmministrativoContabile(impostaStrutturaAmministrativoContabile());
		causale.setEnte(getEnte());
		causale.setStatoOperativoCausale(StatoOperativoCausale.VALIDA);
		
		return causale;
	}

	/**
	 * @return the movimentoGestioneOrdinativo
	 */
	public Accertamento getMovimentoGestioneOrdinativo() {
		return movimentoGestioneOrdinativo;
	}

	/**
	 * @param movimentoGestioneOrdinativo the movimentoGestioneOrdinativo to set
	 */
	public void setMovimentoGestioneOrdinativo(Accertamento movimentoGestioneOrdinativo) {
		this.movimentoGestioneOrdinativo = movimentoGestioneOrdinativo;
	}

	
}
