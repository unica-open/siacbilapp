/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera;

import java.util.Date;
import java.util.List;

import it.csi.siac.siacgenser.frontend.webservice.msg.InseriscePrimaNota;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.TipoCausale;
/**
 * Classe di Model per l'inserimento della prima nota libera (comune tra ambito FIN e GSA)
 *  
 *  @author Elisa Chiari
 *  @version 1.0.0 - 08/10/2015
 */
public abstract class InserisciPrimaNotaLiberaBaseModel  extends BaseInserisciAggiornaPrimaNotaLiberaBaseModel{


	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 6557648475061433908L;
	
	private List<MovimentoEP> listaMovimentiEP;
	
	@Override
	public String getBaseUrl() {
		return "inserisciPrimaNotaLibera" + getAmbitoSuffix();
	}
	
	@Override
	public String getDenominazioneWizard() {
		return "Inserisci Prima Nota Libera";
	}
	
	@Override
	public boolean isAggiornamento() {
		return false;
	}
	
	@Override
	public boolean isInserisciNuoviContiAbilitato() {
		return isSingoloContoCausale();
	}
	
	
	/**
	 * Crea una request per il servizio di {@link InseriscePrimaNota}.
	 * 
	 * @return la request creata
	 */
	public InseriscePrimaNota creaRequestInseriscePrimaNota( ) {
		InseriscePrimaNota request = creaRequest(InseriscePrimaNota.class);
		getPrimaNotaLibera().setBilancio(getBilancio());
		getPrimaNotaLibera().setTipoCausale(TipoCausale.Libera);
		/* imposto la lista dei movimenti*/
		listaMovimentiEP = getListaMovimentoEP();
		for(MovimentoEP mov : listaMovimentiEP){
			mov.setAmbito(getAmbito());
		}
		getPrimaNotaLibera().setListaMovimentiEP(listaMovimentiEP);
		getPrimaNotaLibera().setAmbito(getAmbito());
		getPrimaNotaLibera().setListaPrimaNotaFiglia(getListaPrimeNoteDaCollegare());
		//SIAC-8134
		popolaStrutturaCompetente();
		request.setPrimaNota(getPrimaNotaLibera());

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
		getPrimaNotaLibera().setTipoCausale(TipoCausale.Libera);
	
	}
	
	/**
	 * SIAC-8134
	 */
	protected void popolaStrutturaCompetente() {
		getPrimaNotaLibera().setStrutturaCompetente(getStrutturaCompetentePrimaNotaLibera());
	}

}
