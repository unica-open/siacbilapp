/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotaintegratamanuale;

import java.math.BigDecimal;
import java.util.EnumSet;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.Ambito;
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
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Classe di Model perla consultazione della prima nota libera (comune tra ambito FIN e GSA)
 *  
 *  @author Elisa Chiari
 *  @version 1.0.0 - 08/10/2015
 */
public abstract class ConsultaPrimaNotaIntegrataManualeBaseModel extends GenericBilancioModel {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 5089683112299121010L;
	
	private PrimaNota primaNotaLibera;
	private BigDecimal totaleDare  = BigDecimal.ZERO;
	private BigDecimal totaleAvere = BigDecimal.ZERO;
	
	private Impegno impegno;
	private Accertamento accertamento;
	private SubImpegno subImpegno;
	private SubAccertamento subAccertamento;
	
	// SIAC-5853
	private boolean dataRegistrazioneDefinitivaVisibile;
	
	/**
	 * @return the ambito
	 */
	public abstract Ambito getAmbito();
	
	/**
	 * @return the ambitoSuffix
	 */
	public String getAmbitoSuffix() {
		return getAmbito().getSuffix();
	}

	/**
	 * @return the primaNotaLibera
	 */
	public PrimaNota getPrimaNotaLibera() {
		return primaNotaLibera;
	}

	/**
	 * @param primaNotaLibera the primaNotaLibera to set
	 */
	public void setPrimaNotaLibera(PrimaNota primaNotaLibera) {
		this.primaNotaLibera = primaNotaLibera;
	}
	
	/**
	 * @return the totaleDare
	 */
	public BigDecimal getTotaleDare() {
		return totaleDare;
	}

	/**
	 * @param totaleDare the totaleDare to set
	 */
	public void setTotaleDare(BigDecimal totaleDare) {
		this.totaleDare = totaleDare;
	}

	/**
	 * @return the totaleAvere
	 */
	public BigDecimal getTotaleAvere() {
		return totaleAvere;
	}

	/**
	 * @param totaleAvere the totaleAvere to set
	 */
	public void setTotaleAvere(BigDecimal totaleAvere) {
		this.totaleAvere = totaleAvere;
	}

	/**
	 * @return the tipoMovimento
	 */
	public String getTipoMovimento() {
		MovimentoGestione mg = retrieveMovimentoGestione();
		return mg instanceof Impegno
			? Impegno.class.getSimpleName()
			: mg instanceof Accertamento
				? Accertamento.class.getSimpleName()
				: null;
	}

	/**
	 * @return the annoMovimentoGestione
	 */
	public Integer getAnnoMovimentoGestione() {
		MovimentoGestione mg = retrieveMovimentoGestione();
		return mg != null ? Integer.valueOf(mg.getAnnoMovimento()) : null;
	}

	/**
	 * @return the numeroMovimentoGestione
	 */
	public Integer getNumeroMovimentoGestione() {
		MovimentoGestione mg = retrieveMovimentoGestione();
		
		if(mg instanceof SubImpegno) {
			return toInteger(((SubImpegno) mg).getNumeroImpegnoPadre());
		}
		if(mg instanceof SubAccertamento) {
			return toInteger(((SubAccertamento) mg).getNumeroAccertamentoPadre());
		}
		return mg != null ? toInteger(mg.getNumero()) : null;
	}
	
	/**
	 * Converts a BigDecimal to integer
	 * @param bd the big decimal to convert
	 * @return the corresponding integer
	 */
	private Integer toInteger(BigDecimal bd) {
		return bd != null ? Integer.valueOf(bd.intValue()) : null;
	}

	/**
	 * @return the numeroSubmovimentoGestione
	 */
	public Integer getNumeroSubmovimentoGestione() {
		MovimentoGestione smg = retrieveMovimentoGestioneSub();
		return smg != null ? toInteger(smg.getNumero()) : null;
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
	 * @return the dataRegistrazioneDefinitivaVisibile
	 */
	public boolean isDataRegistrazioneDefinitivaVisibile() {
		return this.dataRegistrazioneDefinitivaVisibile;
	}

	/**
	 * @param dataRegistrazioneDefinitivaVisibile the dataRegistrazioneDefinitivaVisibile to set
	 */
	public void setDataRegistrazioneDefinitivaVisibile(boolean dataRegistrazioneDefinitivaVisibile) {
		this.dataRegistrazioneDefinitivaVisibile = dataRegistrazioneDefinitivaVisibile;
	}

	/**
	 * Prima Nota Libera ha solo un movimentoEP
	 * @return the causaleEP
	 */
	public CausaleEP getCausaleEP() {
		
		return primaNotaLibera != null 
				&& primaNotaLibera.getListaMovimentiEP() != null 
				&& !primaNotaLibera.getListaMovimentiEP().isEmpty()
				&& primaNotaLibera.getListaMovimentiEP().get(0) != null ?
						primaNotaLibera.getListaMovimentiEP().get(0).getCausaleEP() : null;
		
	}
	/**
	 * @return the descrizioneCausale
	 */
	public String getDescrizioneCausale() {
		
		return getCausaleEP().getCodice() + " - " + getCausaleEP().getDescrizione();
	}

	/**
	 * @return the TitoloRiepilogoPrimaNotaStep3
	 */
	public String getTitoloRiepilogoPrimaNotaStep3() {
		return computaTitoloStep3PrimaNota();
	}
	
	/**
	 * Calcolo della stringa  titolo della pagina di riepilogo/consultazione
	 * 
	 * @return la stringa della data richiesta
	 */
	protected String computaTitoloStep3PrimaNota () {
		StringBuilder sb = new StringBuilder();
		sb.append("Prima Nota ");
		if(primaNotaLibera!=null) {
			if (StatoOperativoPrimaNota.PROVVISORIO.equals(primaNotaLibera.getStatoOperativoPrimaNota())){
				sb.append(" provvisoria N.")
				.append(primaNotaLibera.getNumero());
			} else if (StatoOperativoPrimaNota.DEFINITIVO.equals(primaNotaLibera.getStatoOperativoPrimaNota())){
				sb.append(" definitiva N.")
				.append(primaNotaLibera.getNumeroRegistrazioneLibroGiornale());
			}
		}
		return sb.toString();
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
