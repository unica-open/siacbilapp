/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.cespite;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaCespite;
import it.csi.siac.siaccespser.model.CespiteModelDetail;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;

/**
 * The Class InserisciTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class RicercaCespiteModel extends GenericCespiteModel {

	/** PEr la serializzazione*/
	private static final long serialVersionUID = -7464901976179916368L;
	
	private DismissioneCespite dismissioneCespite;
	// SIAC-6374
	private Integer numeroInventarioDa;
	private Integer numeroInventarioA;
	// SIAC-6375
	private String flagSoggettoTutelaBeniCulturali;
	private String flgDonazioneRinvenimento;
	private String flagStatoBene;

	/**
	 * Instantiates a new ricerca tipo bene model.
	 */
	public RicercaCespiteModel() {
		setTitolo("Ricerca cespite");
	}
	
	/**
	 * @return the dismissioneCespite
	 */
	public DismissioneCespite getDismissioniCespite() {
		return dismissioneCespite;
	}

	/**
	 * @param dismissioneCespite the dismissioneCespite to set
	 */
	public void setDismissioniCespite(DismissioneCespite dismissioneCespite) {
		this.dismissioneCespite = dismissioneCespite;
	}
	

	/**
	 * @return the numeroInventarioDa
	 */
	public Integer getNumeroInventarioDa() {
		return this.numeroInventarioDa;
	}

	/**
	 * @param numeroInventarioDa the numeroInventarioDa to set
	 */
	public void setNumeroInventarioDa(Integer numeroInventarioDa) {
		this.numeroInventarioDa = numeroInventarioDa;
	}

	/**
	 * @return the numeroInventarioA
	 */
	public Integer getNumeroInventarioA() {
		return this.numeroInventarioA;
	}

	/**
	 * @param numeroInventarioA the numeroInventarioA to set
	 */
	public void setNumeroInventarioA(Integer numeroInventarioA) {
		this.numeroInventarioA = numeroInventarioA;
	}

	/**
	 * @return the flagSoggettoTutelaBeniCulturali
	 */
	public String getFlagSoggettoTutelaBeniCulturali() {
		return this.flagSoggettoTutelaBeniCulturali;
	}

	/**
	 * @param flagSoggettoTutelaBeniCulturali the flagSoggettoTutelaBeniCulturali to set
	 */
	public void setFlagSoggettoTutelaBeniCulturali(String flagSoggettoTutelaBeniCulturali) {
		this.flagSoggettoTutelaBeniCulturali = flagSoggettoTutelaBeniCulturali;
	}

	/**
	 * @return the flgDonazioneRinvenimento
	 */
	public String getFlgDonazioneRinvenimento() {
		return this.flgDonazioneRinvenimento;
	}

	/**
	 * @param flgDonazioneRinvenimento the flgDonazioneRinvenimento to set
	 */
	public void setFlgDonazioneRinvenimento(String flgDonazioneRinvenimento) {
		this.flgDonazioneRinvenimento = flgDonazioneRinvenimento;
	}

	/**
	 * @return the flagStatoBene
	 */
	public String getFlagStatoBene() {
		return this.flagStatoBene;
	}

	/**
	 * @param flagStatoBene the flagStatoBene to set
	 */
	public void setFlagStatoBene(String flagStatoBene) {
		this.flagStatoBene = flagStatoBene;
	}

	/**
	 * Crea request ricerca sintetica tipo bene.
	 *
	 * @return the ricerca sintetica tipo bene cespite
	 */
	public RicercaSinteticaCespite creaRequestRicercaSinteticaCespite() {
		RicercaSinteticaCespite req = creaRequest(RicercaSinteticaCespite.class);
		req.setCespite(getCespite());
		req.setTipoBeneCespite(getCespite().getTipoBeneCespite());
		req.setClassificazioneGiuridicaCespite(getCespite().getClassificazioneGiuridicaCespite());
		req.setDismissioneCespite(getDismissioniCespite());
		// tiro su solo la categoria
		req.setModelDetails(CespiteModelDetail.TipoBeneCespiteModelDetail,
				CespiteModelDetail.ClassificazioneGiuridicaCespite,
				CespiteModelDetail.DismissioneCespite,
				CespiteModelDetail.IsCollegatoAPrimeNote,
				TipoBeneCespiteModelDetail.Annullato);
		// SIAC-6374
		req.setNumeroInventarioDa(getNumeroInventarioDa());
		req.setNumeroInventarioA(getNumeroInventarioA());
		
		// SIAC-6375
		req.getCespite().setFlagSoggettoTutelaBeniCulturali(FormatUtils.parseBoolean(getFlagSoggettoTutelaBeniCulturali(), "S", "N"));
		req.getCespite().setFlgDonazioneRinvenimento(FormatUtils.parseBoolean(getFlgDonazioneRinvenimento(), "S", "N"));
		req.getCespite().setFlagStatoBene(FormatUtils.parseBoolean(getFlagStatoBene(), "S", "N"));
		
		req.setParametriPaginazione(creaParametriPaginazione());
		return req;
	}
	
}
