/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera;

import java.math.BigDecimal;

import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Classe di Model perla consultazione della prima nota integrata manuale (comune tra ambito FIN e GSA)
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/12/2017
 */
public abstract class ConsultaPrimaNotaLiberaBaseModel extends BasePrimaNotaLiberaModel {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 5089683112299121010L;
	
	private PrimaNota primaNotaLibera;
	private BigDecimal totaleDare  = BigDecimal.ZERO;
	private BigDecimal totaleAvere = BigDecimal.ZERO;
	
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
	
	
}
