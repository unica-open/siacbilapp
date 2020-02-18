/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.cespite;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import it.csi.siac.siaccespser.frontend.webservice.msg.CollegaCespiteRegistroACespite;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;

/**
 * The Class InserisciCespiteModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class InserisciCespiteModel extends BaseInserisciCespiteModel {

	/** Per la serializzazione */
	private static final long serialVersionUID = -4238127399042041307L;

	private Integer uidMovimentoDettaglio;
	
	private Integer uidPrimaNota;
	
	private BigDecimal importoMassimoCespite;
	
	/**
	 * Instantiates a new inserisci tipo bene model.
	 */
	public InserisciCespiteModel() {
		setTitolo("anagrafica cespite");
	}

	@Override
	public Boolean getFlagDonazioneRinvenimento() {
		return Boolean.FALSE;
	}
	
	@Override
	public String getBaseUrl() {
		return "inserisciCespite";
	}

	@Override
	public String getFormTitle() {
		return "Anagrafica scheda cespite";
	}

	/**
	 * @return the uidMovimentoEP
	 */
	public Integer getUidMovimentoDettaglio() {
		return uidMovimentoDettaglio;
	}

	/**
	 * Sets the uid movimento dettaglio.
	 *
	 * @param uidMovimentoDettaglio the new uid movimento dettaglio
	 */
	public void setUidMovimentoDettaglio(Integer uidMovimentoDettaglio) {
		this.uidMovimentoDettaglio = uidMovimentoDettaglio;
	}
	/**
	 * @return the uidPrimaNota
	 */
	public Integer getUidPrimaNota() {
		return uidPrimaNota;
	}

	/**
	 * @param uidPrimaNota the uidPrimaNota to set
	 */
	public void setUidPrimaNota(Integer uidPrimaNota) {
		this.uidPrimaNota = uidPrimaNota;
	}
	
	/**
	 * @return the importoMassimoCespite
	 */
	public BigDecimal getImportoMassimoCespite() {
		return importoMassimoCespite;
	}

	/**
	 * @param importoMassimoCespite the importoMassimoCespite to set
	 */
	public void setImportoMassimoCespite(BigDecimal importoMassimoCespite) {
		this.importoMassimoCespite = importoMassimoCespite;
	}

	/**
	 * Crea request collega cespite registro A cespite.
	 *
	 * @return the collega cespite registro A cespite
	 */
	public CollegaCespiteRegistroACespite creaRequestCollegaCespiteRegistroACespite() {
		CollegaCespiteRegistroACespite request = creaRequest(CollegaCespiteRegistroACespite.class);
		MovimentoDettaglio movimentoDettaglioDaCollegare = new MovimentoDettaglio();
		movimentoDettaglioDaCollegare.setUid(getUidMovimentoDettaglio());
		request.setMovimentoDettaglio(movimentoDettaglioDaCollegare);
		request.setInserimentoContestuale(Boolean.TRUE);
		List<Cespite> listaCespiti = Arrays.asList(getCespite());
		request.setListaCespiti(listaCespiti);
		return request;
	}

	
	
}
