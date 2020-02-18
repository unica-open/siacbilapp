/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale;

import java.util.EnumSet;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.model.ImportiCassaEconomaleEnum;
import it.csi.siac.siaccecser.frontend.webservice.msg.CalcolaDisponibilitaCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioCassaEconomale;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaSinteticaCassaEconomale;
import it.csi.siac.siaccecser.model.CassaEconomale;

/**
 * Classe di model di base per la cassa economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 03/12/2014
 *
 */
public class CassaEconomaleBaseModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6213320062441887193L;
	
	private CassaEconomale cassaEconomale;
	
	/**
	 * @return the cassaEconomale
	 */
	public CassaEconomale getCassaEconomale() {
		return cassaEconomale;
	}

	/**
	 * @param cassaEconomale the cassaEconomale to set
	 */
	public void setCassaEconomale(CassaEconomale cassaEconomale) {
		this.cassaEconomale = cassaEconomale;
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioCassaEconomale}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioCassaEconomale creaRequestRicercaDettaglioCassaEconomale() {
		RicercaDettaglioCassaEconomale request = creaRequest(RicercaDettaglioCassaEconomale.class);
		
		request.setCassaEconomale(getCassaEconomale());
		
		return request;
	}
	/**
	 * Crea una request per il servizio di {@link RicercaSinteticaCassaEconomale}.
	 * 
	 * @return la request creata
	 */
	public RicercaSinteticaCassaEconomale creaRequestRicercaSinteticaCassaEconomale() {
		
		RicercaSinteticaCassaEconomale request = creaRequest(RicercaSinteticaCassaEconomale.class);
		request.setBilancio(getBilancio());
		return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link CalcolaDisponibilitaCassaEconomale}.
	 * 
	 * @param cassaEconomale la cassaEconomale per cui effettuare la richiesta
	 * @return la request creata
	 */
	public CalcolaDisponibilitaCassaEconomale creaRequestCalcolaDisponibilitaCassaEconomale(CassaEconomale cassaEconomale) {
		CalcolaDisponibilitaCassaEconomale request = creaRequest(CalcolaDisponibilitaCassaEconomale.class);
		
		// Copio solo l'uid
		CassaEconomale ce = new CassaEconomale();
		ce.setUid(cassaEconomale.getUid());
		request.setBilancio(getBilancio());
		request.setCassaEconomale(ce);
		
		request.setImportiDerivatiRichiesti(EnumSet.allOf(ImportiCassaEconomaleEnum.class));
		return request;
	}

}
