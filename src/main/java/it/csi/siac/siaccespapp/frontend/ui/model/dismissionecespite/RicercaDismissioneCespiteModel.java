/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.model.dismissionecespite;

import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaDismissioneCespite;
import it.csi.siac.siaccespser.model.DismissioneCespiteModelDetail;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Evento;

/**
 * The Class InserisciTipoBeneModel.
 * @author elisa
 * @version 1.0.0 - 01-06-2018
 */
public class RicercaDismissioneCespiteModel extends GenericDismissioneCespiteModel {


	/** Per la serializzazione*/
	private static final long serialVersionUID = 5637175348438175265L;
	
	private Evento evento;
	private CausaleEP causaleEP;
	
	/**
	 * Instantiates a new ricerca dismissione cespite model.
	 */
	public RicercaDismissioneCespiteModel() {
		setTitolo("ricerca dismissione");
	}
	
	/**
	 * @return the evento
	 */
	public Evento getEvento() {
		return evento;
	}

	/**
	 * @param evento the evento to set
	 */
	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	/**
	 * @return the causaleEP
	 */
	public CausaleEP getCausaleEP() {
		return causaleEP;
	}

	/**
	 * @param causaleEP the causaleEP to set
	 */
	public void setCausaleEP(CausaleEP causaleEP) {
		this.causaleEP = causaleEP;
	}



	/**
	 * Crea request ricerca sintetica tipo bene.
	 *
	 * @return the ricerca sintetica tipo bene cespite
	 */
	public RicercaSinteticaDismissioneCespite creaRequestRicercaSinteticaDismissioneCespite() {
		RicercaSinteticaDismissioneCespite req = creaRequest(RicercaSinteticaDismissioneCespite.class);
		req.setDismissioneCespite(getDismissioneCespite());
		req.setAttoAmministrativo(impostaEntitaFacoltativa(getAttoAmministrativo()));
		req.setEvento(impostaEntitaFacoltativa(getEvento()));
		req.setCausaleEP(impostaEntitaFacoltativa(getCausaleEP()));
		req.setModelDetails(DismissioneCespiteModelDetail.AttoAmministrativo, DismissioneCespiteModelDetail.CausaleEP, DismissioneCespiteModelDetail.NumeroCespitiCollegati, DismissioneCespiteModelDetail.Stato);
		req.setParametriPaginazione(creaParametriPaginazione());
		return req;
	}
	
}
