/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto;

import java.util.ArrayList;
import java.util.List;

import it.csi.siac.siacfin2ser.frontend.webservice.msg.RicercaAllegatoAtto;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;

/**
 * Classe di model per la ricerca dell'allegato atto.
 * 
 * @author elisa
 * @version 1.0.0 - 26/feb/2018
 *
 */
public class RicercaAllegatoAttoConvalidaMultiploModel extends RicercaAllegatoAttoBaseModel{
	
	/**Per la serializzazione */
	private static final long serialVersionUID = 4476639350620063514L;

	/** Costruttore vuoto di default */
	public RicercaAllegatoAttoConvalidaMultiploModel() {
		super();
		setTitolo("Ricerca allegato atto per convalida multiplo");
		setNomeAzioneRicerca("ricercaAllegatoAttoConvalidaMultiplo_effettuaRicercaAllegatoAtto");
		setDisabilitaSelezioneStato(true);
		setConvalida(true);
	}
	
	/**
	 * Gets the descrizioni stati operativi.
	 *
	 * @return the descrizioni stati operativi
	 */
	public List<String> getDescrizioniListaStatiOperativiAllegatoAtto() {
		List<String> codici  = new ArrayList<String>();
		for (StatoOperativoAllegatoAtto statoOperativoAllegatoAtto : getListaStatoOperativoAllegatoAtto()) {
			codici.add(statoOperativoAllegatoAtto.getDescrizione());
		}
		return codici;
	}
	
	
	@Override
	public RicercaAllegatoAtto creaRequestRicercaAllegatoAtto() {
		RicercaAllegatoAtto request= super.creaRequestRicercaAllegatoAtto();
		//se io non ho impostato nessun filtro per stato, devo comunque tirare su solo quelli potenzialmente convalidabili
		request.setStatiOperativiFiltri(getListaStatoOperativoAllegatoAtto());
		return request;
	}

}
