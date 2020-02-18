/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.tipobenecespite;

import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaTipoBeneCespite;
import it.csi.siac.siaccespser.model.CategoriaCespitiModelDetail;
import it.csi.siac.siaccespser.model.TipoBeneCespiteModelDetail;

/**
 * The Class InserisciTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class RicercaTipoBeneModel extends GenericTipoBeneModel {

	/** PEr la serializzazione*/
	private static final long serialVersionUID = 2091856715874511583L;

	/**
	 * Instantiates a new ricerca tipo bene model.
	 */
	public RicercaTipoBeneModel() {
		setTitolo("Ricerca tipo bene");
	}

	/**
	 * Crea request ricerca sintetica tipo bene.
	 *
	 * @return the ricerca sintetica tipo bene cespite
	 */
	public RicercaSinteticaTipoBeneCespite creaRequestRicercaSinteticaTipoBene() {
		RicercaSinteticaTipoBeneCespite req = creaRequest(RicercaSinteticaTipoBeneCespite.class);
		req.setCategoriaCespiti(impostaEntitaFacoltativa(getTipoBeneCespite().getCategoriaCespiti()));
		req.setTipoBeneCespite(getTipoBeneCespite());
		req.setContoPatrimoniale(getTipoBeneCespite().getContoPatrimoniale());
		//tiro su solo la categoria
		req.setModelDetails(TipoBeneCespiteModelDetail.CategoriaCespitiModelDetail, TipoBeneCespiteModelDetail.Annullato, CategoriaCespitiModelDetail.Annullato);
		req.setParametriPaginazione(creaParametriPaginazione());
		return req;
	}

	

}
