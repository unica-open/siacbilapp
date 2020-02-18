/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.primanotalibera;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.RisultatiRicercaPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccespser.frontend.webservice.msg.RifiutaPrimaNotaCespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.ValidaPrimaNotaCespite;

/**
 * Classe di model per la ricerca della prima nota libera
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 5/5/2015
 * @author Elisa Chiari
 * @version 1.0.1 - 14/10/2015
 *
 */
public class RisultatiRicercaPrimaNotaLiberaINVModel extends RisultatiRicercaPrimaNotaLiberaBaseModel {
	

	
	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -8853479390854228236L;

	/** Costruttore vuoto di default */
	public RisultatiRicercaPrimaNotaLiberaINVModel() {
		setTitolo("Risultati ricerca Prima Nota Libera");
	}

	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_INV;
	}
	
	@Override
	public String getAmbitoSuffix() {
		return "INV";
	}

	/**
	 * Crea una request per il servizio di {@link ValidaPrimaNotaCespite}.
	 * 
	 * @return la request creata	 */

	public ValidaPrimaNotaCespite creaRequestValidaPrimaNotaCespite() {
			ValidaPrimaNotaCespite request = creaRequest(ValidaPrimaNotaCespite.class);
			getPrimaNotaLibera().setBilancio(getBilancio());
			getPrimaNotaLibera().setAmbito(getAmbito());
			request.setPrimaNota(getPrimaNotaLibera());			
			return request;
	}
	
	/**
	 * Crea una request per il servizio di {@link ValidaPrimaNotaCespite}.
	 * 
	 * @return la request creata	 */

	public RifiutaPrimaNotaCespite creaRequestRifiutaPrimaNotaCespite() {
			RifiutaPrimaNotaCespite request = creaRequest(RifiutaPrimaNotaCespite.class);
			getPrimaNotaLibera().setBilancio(getBilancio());
			getPrimaNotaLibera().setAmbito(getAmbito());
			request.setPrimaNota(getPrimaNotaLibera());			
			return request;
	}

}
