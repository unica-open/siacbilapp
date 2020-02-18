/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.util.wrappers.registrocomunicazionipcc;

import java.io.Serializable;
import java.math.BigDecimal;

import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacfin2ser.model.RegistroComunicazioniPCC;

/**
 * Classe di wrap per il {@link RegistroComunicazioniPCC}.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 19/06/2015
 * 
 * @author Elisa Chiari
 * @version 1.1.0 - 13/11/2015
 */
public class ElementoRegistroComunicazioniPCC implements ModelWrapper, Serializable {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -9015760701812349826L;
	
	
	private final RegistroComunicazioniPCC registroComunicazioniPCC;
	/**
	 * Costruttore di wrap.
	 * 
	 * @param registroComunicazioniPCC il registro da wrappare
	 */
	public ElementoRegistroComunicazioniPCC(RegistroComunicazioniPCC registroComunicazioniPCC) {
		this.registroComunicazioniPCC = registroComunicazioniPCC;
	}
	
	/**
	 * @return the statoDebito
	 */
	public String getStatoDebito() {
		return registroComunicazioniPCC != null && registroComunicazioniPCC.getStatoDebito() != null
			? registroComunicazioniPCC.getStatoDebito().getCodice() + "/" + registroComunicazioniPCC.getStatoDebito().getDescrizione()
			: "";
	}
	
	/**
	 * @return the uidStatoDebito
	 */
	public Integer getUidStatoDebito() {
		return registroComunicazioniPCC != null && registroComunicazioniPCC.getStatoDebito() != null ? Integer.valueOf(registroComunicazioniPCC.getStatoDebito().getUid()) : null;
	}
	
	/**
	 * @return the causalePCC
	 */
	public String getCausalePCC() {
		return registroComunicazioniPCC != null && registroComunicazioniPCC.getCausalePCC() != null
			? registroComunicazioniPCC.getCausalePCC().getCodice() + "/" + registroComunicazioniPCC.getCausalePCC().getDescrizione()
			: "";
	}
	
	/**
	 * @return the uidCausalePCC
	 */
	public Integer getUidCausalePCC() {
		return registroComunicazioniPCC != null && registroComunicazioniPCC.getCausalePCC() != null ? Integer.valueOf(registroComunicazioniPCC.getCausalePCC().getUid()) : null;
	}
	
	/**
	 * @return the trasmesso
	 */
	public String getTrasmesso() {
		return registroComunicazioniPCC != null && registroComunicazioniPCC.getDataInvio() != null ? "SI" : "NO";
	}
	
	/**
	 * @return the dataInvio
	 */
	public String getDataInvio() {
		return registroComunicazioniPCC != null ? FormatUtils.formatDate(registroComunicazioniPCC.getDataInvio()) : "";
	}
	
	/**
	 * @return the esito
	 */
	public String getCodiceEsito() {		 
		return registroComunicazioniPCC != null && registroComunicazioniPCC.getCodiceEsito() != null ? registroComunicazioniPCC.getCodiceEsito() : "";
	}
	
	/**
	 * @return the dataEsito
	 */
	public String getDataEsito() {
		return registroComunicazioniPCC != null ? FormatUtils.formatDate(registroComunicazioniPCC.getDataEsito()) : "";
	}
	
	/**
	 * @return the descrtizioneEsito
	 */
	public String getDescrizioneEsito() {
		return registroComunicazioniPCC != null && registroComunicazioniPCC.getDescrizioneEsito() != null ? registroComunicazioniPCC.getDescrizioneEsito() : "";
	}
	
	
	/**
	 * @return the dataScadenza
	 */
	public String getDataScadenza() {
		return registroComunicazioniPCC == null
			? ""
			: registroComunicazioniPCC.getTipoOperazionePCC() == null || !BilConstants.TIPO_OPERAZIONE_PCC_CANCELLAZIONE_COMUNICAZIONI_SCADENZA.getConstant().equals(registroComunicazioniPCC.getTipoOperazionePCC().getCodice())
				? FormatUtils.formatDate(registroComunicazioniPCC.getDataScadenza())
				: "Cancellazione comunicazioni scadenza";
	}
	
	/**
	 * @return the dataEmissioneOrdinativo
	 */
	public String getDataEmissioneOrdinativo() {
		return registroComunicazioniPCC != null ? FormatUtils.formatDate(registroComunicazioniPCC.getDataEmissioneOrdinativo()) : "";
	}
	
	/**
	 * @return the numeroOrdinativo
	 */
	public String getNumeroOrdinativo() {
		return registroComunicazioniPCC != null && registroComunicazioniPCC.getNumeroOrdinativo() != null
				? registroComunicazioniPCC.getNumeroOrdinativo().toString()
				: "";
	}
	
	/**
	 * @return the annoProvvisorioCassa
	 */
	public String getAnnoProvvisorioCassa() {
		return registroComunicazioniPCC != null && registroComunicazioniPCC.getAnnoProvvisorioCassa() != null
				? registroComunicazioniPCC.getAnnoProvvisorioCassa().toString()
				: "";
	}
	
	
	/**
	 * @return the numeroProvvisorioCassa
	 */
	public String getNumeroProvvisorioCassa() {
		return registroComunicazioniPCC != null && registroComunicazioniPCC.getNumeroProvvisorioCassa() != null
				? registroComunicazioniPCC.getNumeroProvvisorioCassa().toString()
				: "";
	}
	
	/**
	 * @return the dataQuietanza
	 */
	public String getDataQuietanza() {
		return registroComunicazioniPCC != null && registroComunicazioniPCC.getDataQuietanza() != null
				? FormatUtils.formatDate(registroComunicazioniPCC.getDataQuietanza()): "";
	}
	
	/**
	 * 
	 * @return the numeroQuietanza
	 */
	
	public String getNumeroQuietanza() {
		return registroComunicazioniPCC != null && registroComunicazioniPCC.getNumeroQuietanza() != null
				? registroComunicazioniPCC.getNumeroQuietanza().toString()
				: "";
	}
	
	/**
	 * 
	 * @return the importoQuietanza
	 */
	
	public BigDecimal getImportoQuietanza() {
		return registroComunicazioniPCC != null && registroComunicazioniPCC.getImportoQuietanza() != null
				? registroComunicazioniPCC.getImportoQuietanza()
				: BigDecimal.ZERO;
	}

	@Override
	public int getUid() {
		return registroComunicazioniPCC != null ? registroComunicazioniPCC.getUid() : -1;
	}

}
