/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti;

import it.csi.siac.siacgenser.frontend.webservice.msg.InserisceConto;
import it.csi.siac.siacgenser.model.Conto;

/**
 * Classe di model per l'inserimento del figlio del piano dei conti.
 * 
 * @author Valentina Triolo
 * @version 1.0.0
 *
 */
public class InserisciFiglioPianoDeiContiModel extends BaseInserisciAggiornaPianoDeiContiModel{

	private static final long serialVersionUID = 1565888666025892640L;
	
	private Conto contoPadre;
	
	
	/** Costruttore vuoto di default */
	public InserisciFiglioPianoDeiContiModel(){
		setTitolo("Inserisci Figlio Piano Dei Conti");
	}
	/**
	 * @return the contoPadre
	 */
	public Conto getContoPadre() {
		return contoPadre;
	}
	/**
	 * @param contoPadre the contoPadre to set
	 */
	public void setContoPadre(Conto contoPadre) {
		this.contoPadre = contoPadre;
	}

	/**Crea una request per il servizio di inserimento
	 * 
	 * @return la request
	 */
	public InserisceConto creaRequestInserisceConto(){
		InserisceConto reqIC = creaRequest(InserisceConto.class);
		reqIC.setBilancio(getBilancio());
		popolaContoPerInserimento();
		reqIC.setConto(getConto());
		return reqIC;
	}
	
	/**
	 * Popolamento del conto per l'inserimento.
	 */
	private void popolaContoPerInserimento() {
		
		getConto().setCodice(( contoPadre.getCodice()!= null && contoPadre.getLivello() != 0 ? contoPadre.getCodice()+ "." : "") + getCodiceContoEditato());
		getConto().setContoPadre(contoPadre);
		getConto().setPianoDeiConti(contoPadre.getPianoDeiConti());
		getConto().setContoAPartite(Boolean.TRUE.equals(getConto().getContoAPartite()));
		getConto().setContoFoglia(Boolean.TRUE.equals(getConto().getContoFoglia()));
		getConto().setContoDiLegge(Boolean.TRUE.equals(getConto().getContoDiLegge()));
		getConto().setCodiceBilancio(getCodiceBilancio());
		getConto().setLivello(contoPadre.getLivello()+1);
		getConto().setAmbito(getAmbito());
		
		impostaElementoDellaLista();
		ripulisciCampiPerContoFoglia();
		
	}
	
	
	
	
	
	
	
	
}
