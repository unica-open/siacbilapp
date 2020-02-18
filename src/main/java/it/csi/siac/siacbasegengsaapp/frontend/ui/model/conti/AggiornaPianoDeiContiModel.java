/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti;

import it.csi.siac.siacgenser.frontend.webservice.msg.AggiornaConto;

/**
 * Classe di model per l'aggiornamento del piano dei conti.
 * 
 * @author Valentina Triolo
 * @version 1.0.0
 *
 */
public class AggiornaPianoDeiContiModel extends BaseInserisciAggiornaPianoDeiContiModel{

	private static final long serialVersionUID = 1475325577751418329L;

	private boolean validitaNellAnnoCorrente;
	private boolean figliPresenti;
	
	/** Costruttore vuoto di default */
	public AggiornaPianoDeiContiModel(){
		setTitolo("Aggiorna Piano Dei Conti");
	}
	
	
	/**
	 * @return the validitaNellAnnoCorrente
	 */
	public boolean isValiditaNellAnnoCorrente() {
		return validitaNellAnnoCorrente;
	}
	/**
	 * @param validitaNellAnnoCorrente the validitaNellAnnoCorrente to set
	 */
	public void setValiditaNellAnnoCorrente(boolean validitaNellAnnoCorrente) {
		this.validitaNellAnnoCorrente = validitaNellAnnoCorrente;
	}
	/**
	 * @return the figliPresenti
	 */
	public boolean isFigliPresenti() {
		return figliPresenti;
	}
	/**
	 * @param figliPresenti the figliPresenti to set
	 */
	public void setFigliPresenti(boolean figliPresenti) {
		this.figliPresenti = figliPresenti;
	}

	/**Crea una request per il servizio di AggiornaConto
	 * 
	 * @return la request
	 */
	public AggiornaConto creaRequestAggiornaConto(){
		AggiornaConto reqAC = creaRequest(AggiornaConto.class);
		popolaContoPerAggiornamento();
		reqAC.setConto(getConto());
		reqAC.setBilancio(getBilancio());		
		return reqAC;
	}
	
	/**
	 * Popolamento del conto per l'aggiornamento
	 */
	private void popolaContoPerAggiornamento() {
		getConto().setUid(getUidConto());
		getConto().setCodice((getConto().getContoPadre().getCodice() != null && getConto().getLivello() != 1 ? getConto().getContoPadre().getCodice() + "." : "") + getCodiceContoEditato());
		getConto().setContoAPartite(Boolean.TRUE.equals(getConto().getContoAPartite()));
		getConto().setContoFoglia(Boolean.TRUE.equals(getConto().getContoFoglia()));
		getConto().setContoDiLegge(Boolean.TRUE.equals(getConto().getContoDiLegge()));
		getConto().setAttivo(Boolean.TRUE.equals(getConto().getAttivo()));
		getConto().setCodiceBilancio(getCodiceBilancio());
		impostaElementoDellaLista();
		
		impostaElementoDellaLista();
		ripulisciCampiPerContoFoglia();
		
	}
	
//	@Override
//	public RicercaDettaglioConto creaRequestRicercaDettaglioConto(){
//		RicercaDettaglioConto reqRDC = creaRequest(RicercaDettaglioConto.class);
//		Conto c = new Conto();
//		c.setUid(getUidConto());
//		reqRDC.setConto(c);
//		reqRDC.setRichiedente(getRichiedente());
//		reqRDC.setBilancio(getBilancio());
//		return reqRDC;
//	}
	
	
	

}
