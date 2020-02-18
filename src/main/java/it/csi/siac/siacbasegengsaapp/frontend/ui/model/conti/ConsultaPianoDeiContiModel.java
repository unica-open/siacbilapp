/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.model.conti;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacgenser.frontend.webservice.msg.LeggiTreeCodiceBilancio;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaDettaglioConto;
import it.csi.siac.siacgenser.model.ClassePiano;
import it.csi.siac.siacgenser.model.Conto;

/**
 * Classe di model per la consultazione del piano dei conti.
 * 
 * @author Valentina Triolo
 * @version 1.0.0
 *
 */
public class ConsultaPianoDeiContiModel extends GenericBilancioModel{

	
	private static final long serialVersionUID = 1565888666025892640L;
	
	private Conto conto;
	private Integer uidConto;
	
	/** Costruttore vuoto di default */
	public ConsultaPianoDeiContiModel(){
		setTitolo("Consulta Piano Dei Conti");
	}
	
	/**
	 * @return the conto
	 */
	public Conto getConto() {
		return conto;
	}
	/**
	 * @param conto the conto to set
	 */
	public void setConto(Conto conto) {
		this.conto = conto;
	}
	/**
	 * @return the uidConto
	 */
	public Integer getUidConto() {
		return uidConto;
	}
	/**
	 * @param uidConto the uidConto to set
	 */
	public void setUidConto(Integer uidConto) {
		this.uidConto = uidConto;
	}
	
	/**Crea una request per il servizio di RicercaDettaglioConto
	 * 
	 * @return la request
	 */
	public RicercaDettaglioConto creaRequestRicercaDettaglioConto(){
		RicercaDettaglioConto request = new RicercaDettaglioConto();
		Conto c = new Conto();
		c.setUid(uidConto);
		request.setConto(c);
		request.setRichiedente(getRichiedente());
		request.setBilancio(getBilancio());
		return request;
		
	}
	
	/**Crea una request per il servizio di RicercaDettaglioConto
	 * @param uidClasse l'uid classe
	 * 
	 * @return la request
	 */
	public LeggiTreeCodiceBilancio creaRequestLeggiTreeCodiceBilancio(Integer uidClasse){
		LeggiTreeCodiceBilancio reqLTCB = creaRequest(LeggiTreeCodiceBilancio.class);
		reqLTCB.setAnno(getBilancio().getAnno());
		ClassePiano classePiano = new ClassePiano();
		classePiano.setUid(uidClasse);
		reqLTCB.setClassePiano(classePiano);
		return reqLTCB;
	}
	
	
	
}
