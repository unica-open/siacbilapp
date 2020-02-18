/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.util.wrappers.ammortamento;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;

/**
 * The Class ElementoCespite.
 * @author elisa
 * @version 1.0.0 - 12-06-2018
 */
public class ElementoDettaglioAmmortamentoAnnuoCespite implements Serializable, ModelWrapper {

	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = -3306596502586578314L;
	//FIELDS
	private final DettaglioAmmortamentoAnnuoCespite dettaglioAmmortamentoAnnuoCespite;
	
	
	
	/**
	 * Instantiates a new elemento categoria cespiti.
	 *
	 * @param dettaglioAmmortamentoAnnuoCespite the dettaglio ammortamento annuo cespite
	 */
	public ElementoDettaglioAmmortamentoAnnuoCespite(DettaglioAmmortamentoAnnuoCespite dettaglioAmmortamentoAnnuoCespite) {
		this.dettaglioAmmortamentoAnnuoCespite = dettaglioAmmortamentoAnnuoCespite;
	}



	@Override
	public int getUid() {
		return dettaglioAmmortamentoAnnuoCespite.getUid();
	}
	
	/**
	 * Gets the anno.
	 *
	 * @return the anno
	 */
	public String getAnno() {
		return dettaglioAmmortamentoAnnuoCespite.getAnno().toString();
	}
	
	/**
	 * Gets the importo.
	 *
	 * @return the importo
	 */
	public String getImporto() {
		return FormatUtils.formatCurrency(dettaglioAmmortamentoAnnuoCespite.getQuotaAnnuale());
	}
	
	/**
	 * Gets the prima nota definitiva.
	 *
	 * @return the prima nota definitiva
	 */
	public String getStringaPrimaNotaDefinitiva() {
		if( dettaglioAmmortamentoAnnuoCespite == null) {
			return "";
		}
		return new StringBuilder()
				.append(StringUtils.defaultIfBlank(dettaglioAmmortamentoAnnuoCespite.getRegistrazioneDefinitivaAmmortamento(),""))
				.toString();
	}
	
}
