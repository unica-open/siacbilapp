/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.primanotalibera;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.primanotalibera.ConsultaPrimaNotaLiberaBaseModel;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccespser.frontend.webservice.msg.ValidaPrimaNotaCespite;
import it.csi.siac.siacgenser.model.Evento;

/**
 * Classe di model per la consultazione della prima nota libera.
 * 
 * @author Paggio Simona
 * @version 1.0.0 - 06/05/2015
 * @author Elisa Chiari
 * @version 1.0.1 - 14/10/2015
 *
 */
public class ConsultaPrimaNotaLiberaINVModel extends ConsultaPrimaNotaLiberaBaseModel {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -4555496844989703043L;

	/** Costruttore di default*/
	public ConsultaPrimaNotaLiberaINVModel() {
		setTitolo("Consulta Prime Note Elaborate Dall'Inventario");
	}
	
	@Override
	public Ambito getAmbito() {
		return Ambito.AMBITO_INV;
	}

	//TODO da riscrivere
	/**
	 * Gets the evento
	 * @return the evento
	 */
	public String getEvento() {
		Evento ev = null;
		try{
			ev = getPrimaNotaLibera().getListaMovimentiEP().get(0).getCausaleEP().getEventi().get(0);
		}catch(Exception e){
			return "";
		}
		return ev != null ?  ev.getCodice().toString() + "-" + ev.getDescrizione().toString() : ""; 
	}
	
	/**
	 * Gets the prima nota provvisoria.
	 *
	 * @return the prima nota provvisoria
	 */
	public String getPrimaNotaProvvisoria() {		
		String pnp = getPrimaNotaLibera() != null && getPrimaNotaLibera().getNumero() != null ? getPrimaNotaLibera().getNumero().toString() : ""; 
		String dpnp = getPrimaNotaLibera() != null && getPrimaNotaLibera().getDataRegistrazione() != null ? FormatUtils.formatDate(getPrimaNotaLibera().getDataRegistrazione()).toString() : ""; 
		return pnp + "   " + dpnp; 
	}

	/**
	 * Gets the prima nota definitiva.
	 *
	 * @return the prima nota definitiva
	 */
	public String getPrimaNotaDefinitiva() {		
		String pnd = getPrimaNotaLibera() != null && getPrimaNotaLibera().getNumeroRegistrazioneLibroGiornale() != null ? getPrimaNotaLibera().getNumeroRegistrazioneLibroGiornale().toString() : ""; 
		String dpnd = getPrimaNotaLibera() != null && getPrimaNotaLibera().getDataRegistrazioneLibroGiornale() != null ? FormatUtils.formatDate(getPrimaNotaLibera().getDataRegistrazioneLibroGiornale()).toString() : ""; 
		return pnd + "   " + dpnd; 

	}

	/**
	 * Gets the stato operativo.
	 *
	 * @return the stato operativo
	 */
	public String getStatoOperativo () {
		return getPrimaNotaLibera() != null && getPrimaNotaLibera().getStatoOperativoPrimaNota() != null ? getPrimaNotaLibera().getStatoOperativoPrimaNota().getDescrizione() : "";
	}

	/**
	 * Crea request valida prima nota cespite.
	 *
	 * @return the valida prima nota cespite
	 */
	public ValidaPrimaNotaCespite creaRequestValidaPrimaNotaCespite() {
			ValidaPrimaNotaCespite request = creaRequest(ValidaPrimaNotaCespite.class);
			getPrimaNotaLibera().setBilancio(getBilancio());
			getPrimaNotaLibera().setAmbito(getAmbito());
			request.setPrimaNota(getPrimaNotaLibera());			
			return request;
	}

}
