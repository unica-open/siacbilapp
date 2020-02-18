/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.attivitaiva;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.TipoChiusura;

/**
 * Wrapper per i dati annualizzati del GruppoAttivitaIva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/06/2015
 *
 */
public class ElementoAnnualizzazioneGruppoAttivitaIva implements ModelWrapper, Serializable {
	
	/** Per la seriazzazione */
	private static final long serialVersionUID = 4697651510920891278L;
	
	private final GruppoAttivitaIva gruppoAttivitaIva;
	
	/**
	 * Costruttore di wrap.
	 * 
	 * @param gruppoAttivitaIva il gruppo da wrappare
	 */
	public ElementoAnnualizzazioneGruppoAttivitaIva(GruppoAttivitaIva gruppoAttivitaIva) {
		this.gruppoAttivitaIva = gruppoAttivitaIva;
	}
	
	/**
	 * @return the annoEsercizio
	 */
	public Integer getAnnoEsercizio() {
		return gruppoAttivitaIva != null ? gruppoAttivitaIva.getAnnualizzazione() : null;
	}
	
	/**
	 * @return the tipoChiusura
	 */
	public TipoChiusura getTipoChiusura() {
		return gruppoAttivitaIva != null ? gruppoAttivitaIva.getTipoChiusura() : null;
	}
	
	/**
	 * @return the percentualeProRata
	 */
	public BigDecimal getPercentualeProRata() {
		return gruppoAttivitaIva != null && !gruppoAttivitaIva.getListaProRataEChiusuraGruppoIva().isEmpty() && gruppoAttivitaIva.getListaProRataEChiusuraGruppoIva().get(0) != null
				? gruppoAttivitaIva.getListaProRataEChiusuraGruppoIva().get(0).getPercentualeProRata()
				: null;
	}
	
	/**
	 * @return the ivaPrecedente
	 */
	public BigDecimal getIvaPrecedente() {
		return gruppoAttivitaIva != null && !gruppoAttivitaIva.getListaProRataEChiusuraGruppoIva().isEmpty() && gruppoAttivitaIva.getListaProRataEChiusuraGruppoIva().get(0) != null
				? gruppoAttivitaIva.getListaProRataEChiusuraGruppoIva().get(0).getIvaPrecedente()
				: null;
	}
	
	@Override
	public int getUid() {
		return gruppoAttivitaIva != null ? gruppoAttivitaIva.getUid() : 0;
	}
	
}
