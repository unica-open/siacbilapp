/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.causale;

import java.util.Date;
import java.util.EnumSet;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaSinteticaCapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.CapitoloEntrataGestione;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siacbilser.model.ric.RicercaSinteticaCapitoloEGest;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.InserisceCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrata;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaDettaglioCausaleEntrataResponse;
import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaSinteticaCausaleEntrata;
import it.csi.siac.siacfin2ser.model.CausaleEntrata;
import it.csi.siac.siacfin2ser.model.StatoOperativoCausale;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;

/**
 * Classe di model per la specializzazione della Causale di entrata.
 * 
 * @author Osorio Alessandra
 * @version 1.0.0 - 16/04/2014
 *
 */
public class GenericCausaleEntrataModel extends GenericCausaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4948730122493670234L;
	
	private CausaleEntrata causale;
	
	private Integer idCausale;
	
	private Accertamento movimentoGestione;
	private SubAccertamento subMovimentoGestione;
	
	private CapitoloEntrataGestione capitolo;
	
	private Boolean soggettoPresente;
	private Boolean movimentoPresente;
	private Boolean provvedimentoPresente;
	private Boolean capitoloPresente;
	
	/** Costruttore vuoto di default */
	public GenericCausaleEntrataModel() {
		super();
		setNomeAzioneDecentrata(BilConstants.GESTIONE_CAUSALI_PREDOCUMENTO_ENTRATA_DECENTRATO.getConstant());
	}
	
	/**
	 * @return the causale
	 */
	public CausaleEntrata getCausale() {
		return causale;
	}

	/**
	 * @param causale the causale to set
	 */
	public void setCausale(CausaleEntrata causale) {
		this.causale = causale;
	}

	/**
	 * @return the idCausale
	 */
	public Integer getIdCausale() {
		return idCausale;
	}

	/**
	 * @param idCausale the idCausale to set
	 */
	public void setIdCausale(Integer idCausale) {
		this.idCausale = idCausale;
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
	 * @return the soggettoPresente
	 */
	public Boolean getSoggettoPresente() {
		return soggettoPresente;
	}

	/**
	 * @param soggettoPresente the soggettoPresente to set
	 */
	public void setSoggettoPresente(Boolean soggettoPresente) {
		this.soggettoPresente = soggettoPresente;
	}

	/**
	 * @return the movimentoPresente
	 */
	public Boolean getMovimentoPresente() {
		return movimentoPresente;
	}

	/**
	 * @param movimentoPresente the movimentoPresente to set
	 */
	public void setMovimentoPresente(Boolean movimentoPresente) {
		this.movimentoPresente = movimentoPresente;
	}

	/**
	 * @return the provvedimentoPresente
	 */
	public Boolean getProvvedimentoPresente() {
		return provvedimentoPresente;
	}

	/**
	 * @param provvedimentoPresente the provvedimentoPresente to set
	 */
	public void setProvvedimentoPresente(Boolean provvedimentoPresente) {
		this.provvedimentoPresente = provvedimentoPresente;
	}

	/**
	 * @return the capitoloPresente
	 */
	public Boolean getCapitoloPresente() {
		return capitoloPresente;
	}

	/**
	 * @param capitoloPresente the capitoloPresente to set
	 */
	public void setCapitoloPresente(Boolean capitoloPresente) {
		this.capitoloPresente = capitoloPresente;
	}


	/* Request */
	
	/**
	 * Crea una request per il servizio di Ricerca di dettaglio della Causale.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioCausaleEntrata creaRequestRicercaDettaglioCausaleEntrata() {

		 RicercaDettaglioCausaleEntrata request = new RicercaDettaglioCausaleEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		request.setCausaleEntrata(causale);
		
		return request;
	}
	
	/**
	  * Crea una request per il servizio di Ricerca Sintetica Causale Entrata
	  * 
	  * @return la request creata
	  */
	public RicercaSinteticaCausaleEntrata creaRequestRicercaSinteticaCausaleEntrata() {
		RicercaSinteticaCausaleEntrata request = new RicercaSinteticaCausaleEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		causale.setEnte(getEnte());
		causale.setTipoCausale(getTipoCausale());
		causale.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		injettaSoggetto();
				
		if(checkPresenzaIdEntita(getAttoAmministrativo())) {
			causale.setAttoAmministrativo(getAttoAmministrativo());
		}
		
		if(checkPresenzaIdEntita(getMovimentoGestione())) {
			causale.setAccertamento(getMovimentoGestione());
		}
		
		
		request.setCausaleEntrata(causale);
		
		request.setParametriPaginazione(creaParametriPaginazione());
		return request;
	}
		
				
	/**
	  * Crea una request per il servizio di Inserimento Causale Entrata
	  * 
	  * @return la request creata
	  */
	public InserisceCausaleEntrata creaRequestInserimentoCausaleEntrata() {
		InserisceCausaleEntrata request = new InserisceCausaleEntrata();
		
		request.setDataOra(new Date());
		request.setRichiedente(getRichiedente());
		
		causale.setEnte(getEnte());
		
		causale.setStatoOperativoCausale(StatoOperativoCausale.VALIDA);
		causale.setStrutturaAmministrativoContabile(impostaEntitaFacoltativa(getStrutturaAmministrativoContabile()));
		causale.setCapitoloEntrataGestione(impostaEntitaFacoltativa(getCapitolo()));
		causale.setAccertamento(impostaEntitaFacoltativa(getMovimentoGestione()));
		causale.setSubAccertamento(impostaEntitaFacoltativa(getSubMovimentoGestione()));
		causale.setSoggetto(impostaEntitaFacoltativa(getSoggetto()));
		causale.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		
		request.setCausaleEntrata(causale);
		
		return request;
	}


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
		request.setParametriPaginazione(creaParametriPaginazione(10000));
		request.setRicercaSinteticaCapitoloEntrata(creaRicercaSinteticaCapitoloEGest());
		
		return request;
	}
	
	/**
	 * Crea un'utilit&agrave; per la ricerca sintetica Capitolo Entrata.
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
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiaveOttimizzato}.
	 * 
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato() {
		RicercaAccertamentoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaAccertamentoPerChiaveOttimizzato.class, 1);
		request.setEnte(getEnte());
		request.setpRicercaAccertamentoK(creaPRicercaAccertamentoK());
		//carico i sub solo se ho il numero del sub valorizzato
		request.setCaricaSub(getSubMovimentoGestione() != null && getSubMovimentoGestione().getNumero() != null);
		request.setSubPaginati(true);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		//Non richiedo NESSUN importo derivato del capitolo
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class)); 
		
		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
		
		return request;
	}
	
	
	/**
	 * Imposta i dati per l'aggiornamento della causale al model.
	 * 
	 * @param response     la response da cui popolare il model
	 */
	public void impostaDati(RicercaDettaglioCausaleEntrataResponse response) {
		causale = response.getCausaleEntrata();
		
		//l'atto, il movimento e il soggetto per la pagina di aggiornamento
		setAttoAmministrativo(causale.getAttoAmministrativo());
		setMovimentoGestione(causale.getAccertamento());
		setSoggetto(causale.getSoggetto());
	}
	
	
	/**
	 * Imposta i dati del Causale di Entrata all'interno del model.
	 *  
	 * @param causaleEntrata il predocumento i cui dati devono essere impostati
	 */
	public void impostaCausale(CausaleEntrata causaleEntrata) {
		setCausale(causaleEntrata);
		setCapitolo(causaleEntrata.getCapitoloEntrataGestione());
		setSoggetto(causaleEntrata.getSoggetto());
		setMovimentoGestione(causaleEntrata.getAccertamento());
		setSubMovimentoGestione(causaleEntrata.getSubAccertamento());
	
		setStrutturaAmministrativoContabile(causaleEntrata.getStrutturaAmministrativoContabile());
		setTipoCausale(causaleEntrata.getTipoCausale());
		
		impostaAttoAmministrativo(causaleEntrata.getAttoAmministrativo());
	}
	
	/**
	 * Crea un'utilit&agrave; per la ricerca impegno.
	 * 
	 * @return l'utility creata
	 */
	private RicercaAccertamentoK creaPRicercaAccertamentoK() {
		RicercaAccertamentoK utility = new RicercaAccertamentoK();
		
		if(getMovimentoGestione() != null) {
			utility.setAnnoEsercizio(getAnnoEsercizioInt());
			utility.setAnnoAccertamento(getMovimentoGestione().getAnnoMovimento());
			utility.setNumeroAccertamento(getMovimentoGestione().getNumero());
		}
		if(getSubMovimentoGestione() != null) {
			utility.setNumeroSubDaCercare(getSubMovimentoGestione().getNumero());
		}
		
		return utility;
	}
	
	/* Metodi di utilita' */

	/**
	 * Metodo di utilit&agrave; per l'iniezione del soggetto nel documento.
	 */
	private void injettaSoggetto() {
		if(!getSoggettoPresente()) {
			return;
		}
		causale.setSoggetto(getSoggetto());
	}
	
}
