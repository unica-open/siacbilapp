/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegratamanuale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

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
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNotaIntegrataManuale;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * Classe di model per l'aggiornamento della prima nota libera
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 */
public abstract class AggiornaPrimaNotaIntegrataManualeBaseModel extends BaseInserisciAggiornaPrimaNotaLiberaBaseModel {

	
	/**
	 * Per la serializzazione 
	 */
	private static final long serialVersionUID = 9072823660486723869L;
	
	private PrimaNota primaNotaLiberaOriginale;
	private List<MovimentoEP> listaMovimentiEP = new ArrayList<MovimentoEP>();
	
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
	 * @return the primaNotaLiberaOriginale
	 */
	public PrimaNota getPrimaNotaLiberaOriginale() {
		return primaNotaLiberaOriginale;
	}

	/**
	 * @param primaNotaLiberaOriginale the primaNotaLiberaOriginale to set
	 */
	public void setPrimaNotaLiberaOriginale(PrimaNota primaNotaLiberaOriginale) {
		this.primaNotaLiberaOriginale = primaNotaLiberaOriginale;
	}
	
	/**
	 * @return the listaMovimentiEP
	 */
	public List<MovimentoEP> getListaMovimentiEP() {
		return this.listaMovimentiEP;
	}

	/**
	 * @param listaMovimentiEP the listaMovimentiEP to set
	 */
	public void setListaMovimentiEP(List<MovimentoEP> listaMovimentiEP) {
		this.listaMovimentiEP = listaMovimentiEP != null ? listaMovimentiEP : new ArrayList<MovimentoEP>();
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
		return "aggiornaPrimaNotaIntegrataManuale" + getAmbitoSuffix();
	}



	@Override
	public boolean isAggiornamento() {
		return true;
	}
	
	@Override
	public boolean isInserisciNuoviContiAbilitato() {
		//SIAC-6195: posso aggiornare e modificare le scritture anche delle prime note definitive
		return true;
	}
	
	/**
	 * Crea una request per il servizio di {@link AggiornaPrimaNota}.
	 * 
	 * @return la request creata
	 */
	public AggiornaPrimaNotaIntegrataManuale creaRequestAggiornaPrimaNotaIntegrataManuale( ) {
		AggiornaPrimaNotaIntegrataManuale request = creaRequest(AggiornaPrimaNotaIntegrataManuale.class);
		request.setPrimaNota(getPrimaNotaLibera());
		
		getPrimaNotaLibera().setBilancio(getBilancio());
		getPrimaNotaLibera().setTipoCausale(TipoCausale.Integrata);
		getPrimaNotaLibera().setAmbito(getAmbito());
		getPrimaNotaLibera().setListaPrimaNotaFiglia(getListaPrimeNoteDaCollegare());
		
		RegistrazioneMovFin rmf = recuperaRegistrazioneMovFinOriginale();
		
		// imposto la lista dei movimenti
		for(MovimentoEP mov : getListaMovimentoEP()){
			mov.setAmbito(getAmbito());
			mov.setRegistrazioneMovFin(rmf);
		}
		getPrimaNotaLibera().setListaMovimentiEP(getListaMovimentoEP());

		return request;
	}

	/**
	 * Recupera la registrazione movfin originale della prima nota
	 * @return
	 */
	private RegistrazioneMovFin recuperaRegistrazioneMovFinOriginale() {
		if(getPrimaNotaLiberaOriginale() == null || getPrimaNotaLiberaOriginale().getListaMovimentiEP() == null) {
			return null;
		}
		for(MovimentoEP mep : getPrimaNotaLiberaOriginale().getListaMovimentiEP()) {
			if(mep.getRegistrazioneMovFin() != null && mep.getRegistrazioneMovFin().getUid() != 0) {
				return mep.getRegistrazioneMovFin();
			}
		}
		return null;
	}

	@Override
	public String getDenominazioneWizard() {
		StringBuilder sb = new StringBuilder();
		sb.append("Aggiorna Prima Nota");
		if(getPrimaNotaLibera() != null && getPrimaNotaLibera().getNumero() != null) {
			sb.append(" ")
				.append(getPrimaNotaLibera().getNumero());
		}
		return sb.toString();
	}
	
	@Override
	public TipoCausale getTipoCausale() {
		return TipoCausale.Integrata;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioPrimaNota}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioPrimaNota creaRequestRicercaDettaglioPrimaNotaLibera() {
		RicercaDettaglioPrimaNota request = creaRequest(RicercaDettaglioPrimaNota.class);
		
		request.setPrimaNota(getPrimaNotaLibera());
		request.getPrimaNota().setAmbito(getAmbito());
		
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaAccertamentoPerChiaveOttimizzato}.
	 * @param acc l'accertamento
	 * @return la request creata
	 */
	public RicercaAccertamentoPerChiaveOttimizzato creaRequestRicercaAccertamentoPerChiaveOttimizzato(Accertamento acc) {
		RicercaAccertamentoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaAccertamentoPerChiaveOttimizzato.class, 1);
		request.setEnte(getEnte());
		//carico i sub solo se ho un sub passato
		request.setCaricaSub(acc instanceof SubAccertamento);
		request.setSubPaginati(true);
		
		RicercaAccertamentoK prak = new RicercaAccertamentoK();
		prak.setAnnoEsercizio(getAnnoEsercizioInt());
		request.setpRicercaAccertamentoK(prak);
		
		if(acc instanceof SubAccertamento) {
			SubAccertamento sa = (SubAccertamento) acc;
			prak.setAnnoAccertamento(Integer.valueOf(sa.getAnnoAccertamentoPadre()));
			prak.setNumeroAccertamento(sa.getNumeroAccertamentoPadre());
			prak.setNumeroSubDaCercare(sa.getNumero());
		} else {
			prak.setAnnoAccertamento(Integer.valueOf(acc.getAnnoMovimento()));
			prak.setNumeroAccertamento(acc.getNumero());
		}
		
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
	 * @param imp l'impegno
	 * @return la request creata
	 */
	public RicercaImpegnoPerChiaveOttimizzato creaRequestRicercaImpegnoPerChiaveOttimizzato(Impegno imp) {
		RicercaImpegnoPerChiaveOttimizzato request = creaPaginazioneRequest(RicercaImpegnoPerChiaveOttimizzato.class, 1);
		request.setEnte(getEnte());
		//carico i sub solo se ho il numero del sub valorizzato
		request.setCaricaSub(imp instanceof SubImpegno);
		request.setSubPaginati(true);
		
		RicercaImpegnoK prik = new RicercaImpegnoK();
		prik.setAnnoEsercizio(getAnnoEsercizioInt());
		request.setpRicercaImpegnoK(prik);
		
		if(imp instanceof SubImpegno) {
			SubImpegno si = (SubImpegno) imp;
			prik.setAnnoImpegno(Integer.valueOf(si.getAnnoImpegnoPadre()));
			prik.setNumeroImpegno(si.getNumeroImpegnoPadre());
			prik.setNumeroSubDaCercare(si.getNumero());
		} else {
			prik.setAnnoImpegno(Integer.valueOf(imp.getAnnoMovimento()));
			prik.setNumeroImpegno(imp.getNumero());
		}
		
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
