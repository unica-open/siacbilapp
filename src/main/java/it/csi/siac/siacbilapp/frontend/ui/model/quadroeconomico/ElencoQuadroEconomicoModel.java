/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
/*
 * 
 */
package it.csi.siac.siacbilapp.frontend.ui.model.quadroeconomico;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacbilapp.frontend.ui.model.GenericBilancioModel;
import it.csi.siac.siacbilser.frontend.webservice.msg.quadroeconomico.RicercaQuadroEconomicoValido;
import it.csi.siac.siacbilser.model.QuadroEconomico;

/**
 * Classe di model per l'elenco dei classificatori GSA
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/01/2018
 */
public class ElencoQuadroEconomicoModel extends GenericBilancioModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4160293014212386902L;
	
	private List<QuadroEconomico> listaQuadroEconomico = new ArrayList<QuadroEconomico>();

	/** Costruttore vuoto di default */
	public ElencoQuadroEconomicoModel() {
		setTitolo("Elenco Classificatore GSA");
	}

	/**
	 * @return the listaQuadroEconomico
	 */
	public List<QuadroEconomico> getListaQuadroEconomico() {
		return this.listaQuadroEconomico;
	}

	/**
	 * @param listaQuadroEconomico the listaQuadroEconomicoto set
	 */
	public void setListaQuadroEconomico(List<QuadroEconomico> listaQuadroEconomico) {
		this.listaQuadroEconomico = listaQuadroEconomico;
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaQuadroEconomicoValido}.
	 * @return la request creata
	 */
	public RicercaQuadroEconomicoValido creaRequestRicercaQuadroEconomicoValido() {
		RicercaQuadroEconomicoValido req = creaRequest(RicercaQuadroEconomicoValido.class);
		
		req.setBilancio(getBilancio());
		
		return req;
	}
}
