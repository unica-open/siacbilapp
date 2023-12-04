/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.attivitaiva;

import java.math.BigDecimal;

import it.csi.siac.siaccommon.util.ReflectionUtil;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacfin2ser.model.GruppoAttivitaIva;
import it.csi.siac.siacfin2ser.model.ProRataEChiusuraGruppoIva;

/**
 * Wrapper per il GruppoAttivitaIva.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 29/05/2014
 *
 */
public class ElementoGruppoAttivitaIva extends GruppoAttivitaIva implements ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4859871816921684817L;
	
	private String azioni;
	private Integer annoEsercizio;
	private BigDecimal percentualeProRata;
	
	/**
	 * Costruttore a partire dalla superclasse.
	 * 
	 * @param gruppoAttivitaIva la superclasse
	 * @param annoProRata       l'anno della ProRata
	 */
	public ElementoGruppoAttivitaIva(GruppoAttivitaIva gruppoAttivitaIva, Integer annoProRata) {
		ReflectionUtil.downcastByReflection(gruppoAttivitaIva, this);
		
		// Ottengo i dati di anno e percentuale ProRata dalla lista del proRata
		if(!this.getListaProRataEChiusuraGruppoIva().isEmpty()) {
			ProRataEChiusuraGruppoIva precgi = null;
			// Cerco la prorata relativa all'anno
			for(ProRataEChiusuraGruppoIva p : this.getListaProRataEChiusuraGruppoIva()) {
				if(annoProRata.equals(p.getAnnoEsercizio())) {
					// Imposto il proRata ed esco dal ciclo
					precgi = p;
					break;
				}
			}
			if(precgi != null) {
				this.annoEsercizio = precgi.getAnnoEsercizio();
				this.percentualeProRata = precgi.getPercentualeProRata();
			}
		}
	}

	/**
	 * @return the azioni
	 */
	public String getAzioni() {
		return azioni;
	}

	/**
	 * @param azioni the azioni to set
	 */
	public void setAzioni(String azioni) {
		this.azioni = azioni;
	}

	/**
	 * @return the annoEsercizio
	 */
	public Integer getAnnoEsercizio() {
		return annoEsercizio;
	}

	/**
	 * @param annoEsercizio the annoEsercizio to set
	 */
	public void setAnnoEsercizio(Integer annoEsercizio) {
		this.annoEsercizio = annoEsercizio;
	}

	/**
	 * @return the percentualeProRata
	 */
	public BigDecimal getPercentualeProRata() {
		return percentualeProRata;
	}

	/**
	 * @param percentualeProRata the percentualeProRata to set
	 */
	public void setPercentualeProRata(BigDecimal percentualeProRata) {
		this.percentualeProRata = percentualeProRata;
	}
	
}
