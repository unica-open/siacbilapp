/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera;

import java.util.List;

import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaPrimaNota;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioPrimaNota;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;

/**
 * Classe di model per l'aggiornamento della prima nota libera
 *  @author Elisa Chiari
 * @version 1.0.0 - 14/10/2015
 */

public abstract class AggiornaPrimaNotaLiberaBaseModel extends BaseInserisciAggiornaPrimaNotaLiberaBaseModel {

	
	/**
	 * Per la serializzazione 
	 */
	private static final long serialVersionUID = 9072823660486723869L;
	
	private PrimaNota primaNotaLiberaOriginale;
	private List<MovimentoEP> listaMovimentiEP;
	

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

	@Override
	public String getBaseUrl() {
		return "aggiornaPrimaNotaLibera" + getAmbitoSuffix();
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
	public AggiornaPrimaNota creaRequestAggiornaPrimaNota() {
		AggiornaPrimaNota request = creaRequest(AggiornaPrimaNota.class);
		/* imposto la lista dei movimenti*/
		listaMovimentiEP= getListaMovimentoEP();
				
		for(MovimentoEP mov : listaMovimentiEP){
			mov.setAmbito(getAmbito());
		}
		getPrimaNotaLibera().setListaMovimentiEP(listaMovimentiEP);
		getPrimaNotaLibera().setBilancio(getBilancio());
		getPrimaNotaLibera().setAmbito(getAmbito());
		getPrimaNotaLibera().setListaPrimaNotaFiglia(getListaPrimeNoteDaCollegare());
		//SIAC-8134
		popolaStrutturaCompetente();
		request.setPrimaNota(getPrimaNotaLibera());

		return request;
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
	 * SIAC-8134
	 */
	protected void popolaStrutturaCompetente() {
		getPrimaNotaLibera().setStrutturaCompetente(getStrutturaCompetentePrimaNotaLibera());
	}
	
}
