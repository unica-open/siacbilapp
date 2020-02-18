/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegratamanuale;

import java.math.BigDecimal;
import java.util.Date;
import java.util.EnumSet;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.BaseInserisciAggiornaPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.ImportiCapitoloEnum;
import it.csi.siac.siaccorser.model.TipologiaClassificatore;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliCapitoli;
import it.csi.siac.siacfinser.frontend.webservice.msg.DatiOpzionaliElencoSubTuttiConSoloGliIds;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaAccertamentoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaImpegnoPerChiaveOttimizzato;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.MovimentoGestione;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.ric.RicercaAccertamentoK;
import it.csi.siac.siacfinser.model.ric.RicercaImpegnoK;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNotaIntegrataManuale;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
/**
 * Classe di Model per l'inserimento della prima nota integrata manuale (comune tra ambito FIN e GSA)
 *  
 *  @author Marchino Alessandro
 *  @version 1.0.0 - 11/12/2017
 */
public abstract class InserisciPrimaNotaIntegrataManualeBaseModel extends BaseInserisciAggiornaPrimaNotaLiberaBaseModel{


	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 6557648475061433908L;
	
	private String tipoMovimento;
	private Integer annoMovimentoGestione;
	private Integer numeroMovimentoGestione;
	private Integer numeroSubmovimentoGestione;
	
	private Impegno impegno;
	private Accertamento accertamento;
	private SubImpegno subImpegno;
	private SubAccertamento subAccertamento;
	
	/**
	 * @return the tipoMovimento
	 */
	public String getTipoMovimento() {
		return this.tipoMovimento;
	}

	/**
	 * @param tipoMovimento the tipoMovimento to set
	 */
	public void setTipoMovimento(String tipoMovimento) {
		this.tipoMovimento = tipoMovimento;
	}

	/**
	 * @return the annoMovimentoGestione
	 */
	public Integer getAnnoMovimentoGestione() {
		return this.annoMovimentoGestione;
	}

	/**
	 * @param annoMovimentoGestione the annoMovimentoGestione to set
	 */
	public void setAnnoMovimentoGestione(Integer annoMovimentoGestione) {
		this.annoMovimentoGestione = annoMovimentoGestione;
	}

	/**
	 * @return the numeroMovimentoGestione
	 */
	public Integer getNumeroMovimentoGestione() {
		return this.numeroMovimentoGestione;
	}

	/**
	 * @param numeroMovimentoGestione the numeroMovimentoGestione to set
	 */
	public void setNumeroMovimentoGestione(Integer numeroMovimentoGestione) {
		this.numeroMovimentoGestione = numeroMovimentoGestione;
	}

	/**
	 * @return the numeroSubmovimentoGestione
	 */
	public Integer getNumeroSubmovimentoGestione() {
		return this.numeroSubmovimentoGestione;
	}

	/**
	 * @param numeroSubmovimentoGestione the numeroSubmovimentoGestione to set
	 */
	public void setNumeroSubmovimentoGestione(Integer numeroSubmovimentoGestione) {
		this.numeroSubmovimentoGestione = numeroSubmovimentoGestione;
	}

	/**
	 * @return the impegno
	 */
	public Impegno getImpegno() {
		return this.impegno;
	}

	/**
	 * @param impegno the impegno to set
	 */
	public void setImpegno(Impegno impegno) {
		this.impegno = impegno;
	}

	/**
	 * @return the accertamento
	 */
	public Accertamento getAccertamento() {
		return this.accertamento;
	}

	/**
	 * @param accertamento the accertamento to set
	 */
	public void setAccertamento(Accertamento accertamento) {
		this.accertamento = accertamento;
	}

	/**
	 * @return the subImpegno
	 */
	public SubImpegno getSubImpegno() {
		return this.subImpegno;
	}

	/**
	 * @param subImpegno the subImpegno to set
	 */
	public void setSubImpegno(SubImpegno subImpegno) {
		this.subImpegno = subImpegno;
	}

	/**
	 * @return the subAccertamento
	 */
	public SubAccertamento getSubAccertamento() {
		return this.subAccertamento;
	}

	/**
	 * @param subAccertamento the subAccertamento to set
	 */
	public void setSubAccertamento(SubAccertamento subAccertamento) {
		this.subAccertamento = subAccertamento;
	}
	
	/**
	 * @return descrizioneMovimentoGestione
	 */
	public String getDescrizioneMovimentoGestione() {
		MovimentoGestione mg = retrieveMovimentoGestione();
		return mg != null ? mg.getDescrizione() : "";
	}
	
	/**
	 * @return the importoAttualeMovimentoGestione
	 */
	public BigDecimal getImportoAttualeMovimentoGestione() {
		MovimentoGestione mg = retrieveMovimentoGestione();
		return mg != null ? mg.getImportoAttuale() : null;
	}
	
	/**
	 * @return soggettoMovimentoGestione
	 */
	public String getSoggettoMovimentoGestione() {
		MovimentoGestione smg = retrieveMovimentoGestioneSub();
		MovimentoGestione mg = retrieveMovimentoGestioneTestata();
		return smg != null && smg.getSoggetto() != null
			? computeDataSoggetto(smg.getSoggetto())
			: mg != null
				? computeDataSoggetto(mg.getSoggetto())
				: null;
	}
	
	/**
	 * @return capitoloMovimentoGestione
	 */
	public String getCapitoloMovimentoGestione() {
		// XXX: Ci saranno modi migliori?
		if(getSubImpegno() != null && getSubImpegno().getCapitoloUscitaGestione() != null) {
			return computeDataCapitolo(getSubImpegno().getCapitoloUscitaGestione());
		}
		if(getImpegno() != null && getImpegno().getCapitoloUscitaGestione() != null) {
			return computeDataCapitolo(getImpegno().getCapitoloUscitaGestione());
		}
		if(getSubAccertamento() != null && getSubAccertamento().getCapitoloEntrataGestione() != null) {
			return computeDataCapitolo(getSubAccertamento().getCapitoloEntrataGestione());
		}
		if(getAccertamento() != null && getAccertamento().getCapitoloEntrataGestione() != null) {
			return computeDataCapitolo(getAccertamento().getCapitoloEntrataGestione());
		}
		return "";
	}
	
	/**
	 * Recupera il movimento di gestione
	 * @return il movimento di gestione
	 */
	public MovimentoGestione retrieveMovimentoGestione() {
		// Ordine SUBIMPEGNO -> IMPEGNO -> SUBACCERTAMENTO -> ACCERTAMENTO
		return ObjectUtils.firstNonNull(getSubImpegno(), getImpegno(), getSubAccertamento(), getAccertamento());
	}
	
	/**
	 * Recupera il movimento di gestione testata
	 * @return il movimento di gestione
	 */
	private MovimentoGestione retrieveMovimentoGestioneTestata() {
		// Ordine IMPEGNO -> ACCERTAMENTO
		return ObjectUtils.firstNonNull(getImpegno(), getAccertamento());
	}
	
	/**
	 * Recupera il movimento di gestione sub
	 * @return il movimento di gestione sub
	 */
	private MovimentoGestione retrieveMovimentoGestioneSub() {
		// Ordine SUBIMPEGNO -> SUBACCERTAMENTO
		return ObjectUtils.firstNonNull(getSubImpegno(), getSubAccertamento());
	}
	
	/**
	 * Calcola i dati del soggetto (codice e denominazione)
	 * @param s il soggetto
	 * @return i dati del soggetto
	 */
	private String computeDataSoggetto(Soggetto s) {
		if(s == null || StringUtils.isBlank(s.getCodiceSoggetto())) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
				.append(s.getCodiceSoggetto());
		if(StringUtils.isNotBlank(s.getDenominazione())) {
			sb.append(" - ")
				.append(s.getDenominazione());
		}
		return sb.toString();
	}
	
	/**
	 * Calcola i dati del capitolo (anno, numero, descrizione)
	 * @param c il capitolo
	 * @return i dati del capitolo
	 */
	private String computeDataCapitolo(Capitolo<?, ?> c) {
		if(c == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
				.append(c.getAnnoCapitolo())
				.append("/")
				.append(c.getNumeroCapitolo())
				.append("/")
				.append(c.getNumeroArticolo());
		if(isGestioneUEB()) {
			sb.append("/")
				.append(c.getNumeroUEB());
		}
		return sb.append(" ")
			.append(c.getDescrizione())
			.toString();
	}

	@Override
	public String getBaseUrl() {
		return "inserisciPrimaNotaIntegrataManuale" + getAmbitoSuffix();
	}
	
	@Override
	public String getDenominazioneWizard() {
		return "Inserisci Prima Nota Integrata Manuale";
	}
	
	@Override
	public boolean isAggiornamento() {
		return false;
	}
	
	@Override
	public boolean isInserisciNuoviContiAbilitato() {
		return isSingoloContoCausale();
	}
	
	@Override
	public TipoCausale getTipoCausale() {
		return TipoCausale.Integrata;
	}
	
	
	/**
	 * Crea una request per il servizio di {@link InseriscePrimaNota}.
	 * 
	 * @return la request creata
	 */
	public InseriscePrimaNotaIntegrataManuale creaRequestInseriscePrimaNotaIntegrataManuale( ) {
		InseriscePrimaNotaIntegrataManuale request = creaRequest(InseriscePrimaNotaIntegrataManuale.class);

		getPrimaNotaLibera().setBilancio(getBilancio());
		getPrimaNotaLibera().setTipoCausale(TipoCausale.Integrata);
		getPrimaNotaLibera().setAmbito(getAmbito());
		getPrimaNotaLibera().setListaPrimaNotaFiglia(getListaPrimeNoteDaCollegare());
		
		// Imposto la lista dei movimenti
		for(MovimentoEP mov : getListaMovimentoEP()){
			mov.setAmbito(getAmbito());
		}
		getPrimaNotaLibera().setListaMovimentiEP(getListaMovimentoEP());
		request.setPrimaNota(getPrimaNotaLibera());
		
		// Ordine SUBIMPEGNO -> IMPEGNO -> SUBACCERTAMENTO -> ACCERTAMENTO
		request.setEntita(ObjectUtils.firstNonNull(getSubImpegno(), getImpegno(), getSubAccertamento(), getAccertamento()));
		request.setEvento(getEvento());

		return request;
	}
	
	/**
	 * imposta i dati neccessari all'interfaccia di stampa corrsipondente
	 */
	public void impostaDatiNelModel() { 
		if (getPrimaNotaLibera()==null) {
			
			setPrimaNotaLibera(new PrimaNota());
		}
		getPrimaNotaLibera().setDataRegistrazione(new Date());
		getPrimaNotaLibera().setAmbito(getAmbito());
		getPrimaNotaLibera().setTipoCausale(TipoCausale.Integrata);
	
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiaveOttimizzato}.
	 * 
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato() {
		RicercaAccertamentoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaAccertamentoPerChiaveOttimizzato.class, 1);
		request.setEnte(getEnte());
		//carico i sub solo se ho il numero del sub valorizzato
		request.setCaricaSub(getNumeroSubmovimentoGestione() != null);
		request.setSubPaginati(true);
		
		RicercaAccertamentoK prak = new RicercaAccertamentoK();
		prak.setAnnoEsercizio(getAnnoEsercizioInt());
		prak.setAnnoAccertamento(getAnnoMovimentoGestione());
		prak.setNumeroAccertamento(new BigDecimal(getNumeroMovimentoGestione().intValue()));
		prak.setNumeroSubDaCercare(getNumeroSubmovimentoGestione() != null ? new BigDecimal(getNumeroSubmovimentoGestione().intValue()) : null);
		request.setpRicercaAccertamentoK(prak);
		
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
	 * Crea una request per il servizio di {@link RicercaImpegnoPerChiaveOttimizzato}.
	 * 
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato() {
		RicercaImpegnoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaImpegnoPerChiaveOttimizzato.class, 1);
		request.setEnte(getEnte());
		//carico i sub solo se ho il numero del sub valorizzato
		request.setCaricaSub(getNumeroSubmovimentoGestione() != null);
		request.setSubPaginati(true);
		
		RicercaImpegnoK prik = new RicercaImpegnoK();
		prik.setAnnoEsercizio(getAnnoEsercizioInt());
		prik.setAnnoImpegno(getAnnoMovimentoGestione());
		prik.setNumeroImpegno(new BigDecimal(getNumeroMovimentoGestione().intValue()));
		prik.setNumeroSubDaCercare(getNumeroSubmovimentoGestione() != null ? new BigDecimal(getNumeroSubmovimentoGestione().intValue()) : null);
		request.setpRicercaImpegnoK(prik);
		
		DatiOpzionaliElencoSubTuttiConSoloGliIds datiOpzionaliElencoSubTuttiConSoloGliIds = new DatiOpzionaliElencoSubTuttiConSoloGliIds();
		datiOpzionaliElencoSubTuttiConSoloGliIds.setEscludiAnnullati(true);
		request.setDatiOpzionaliElencoSubTuttiConSoloGliIds(datiOpzionaliElencoSubTuttiConSoloGliIds);
		
		// Non richiedo NESSUN importo derivato.
		DatiOpzionaliCapitoli datiOpzionaliCapitoli = new DatiOpzionaliCapitoli();
		datiOpzionaliCapitoli.setImportiDerivatiRichiesti(EnumSet.noneOf(ImportiCapitoloEnum.class));
		datiOpzionaliCapitoli.setTipologieClassificatoriRichiesti(EnumSet.noneOf(TipologiaClassificatore.class));
		request.setDatiOpzionaliCapitoli(datiOpzionaliCapitoli);
		
		return request;
	}
}
