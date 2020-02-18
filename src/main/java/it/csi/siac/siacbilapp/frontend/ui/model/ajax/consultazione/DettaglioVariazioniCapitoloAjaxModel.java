/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.model.ajax.consultazione;

import it.csi.siac.siacbilapp.frontend.ui.model.ajax.GenericRisultatiRicercaAjaxModel;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.consultazione.ElementoVariazioneImportoSingoloCapitolo;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaDettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siacbilser.model.ComponenteImportiCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitoloModelDetail;
import it.csi.siac.siacbilser.model.DettaglioVariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoSingoloCapitolo;

/**
 * Classe di Model per le chiamate AJAX relative al DataTable.
 * 
 * @author Alessandro Marchino
 * @version 1.0.0 - 01/09/2016
 *
 */
public class DettaglioVariazioniCapitoloAjaxModel extends GenericRisultatiRicercaAjaxModel<ElementoVariazioneImportoSingoloCapitolo> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 6331261716894909156L;

	/** Costruttore vuoto di default */
	public DettaglioVariazioniCapitoloAjaxModel() {
		super();
		setTitolo("Risultati di ricerca dettaglio variazione - AJAX");
	}
	
	public RicercaDettaglioVariazioneComponenteImportoCapitolo creaRequestRicercaDettaglioVariazioneComponenteImportoCapitolo(VariazioneImportoSingoloCapitolo instance, Integer uid) {
		RicercaDettaglioVariazioneComponenteImportoCapitolo request = creaRequest(RicercaDettaglioVariazioneComponenteImportoCapitolo.class);
		DettaglioVariazioneImportoCapitolo dv = new DettaglioVariazioneImportoCapitolo();
		// Fisso un tipo di capitolo
		Capitolo<?, ?> capitolo = new CapitoloUscitaGestione();
		capitolo.setUid(uid);
		dv.setCapitolo(capitolo);
		
		VariazioneImportoCapitolo vip = new VariazioneImportoCapitolo();
		vip.setUid(instance.getUid());
		dv.setVariazioneImportoCapitolo(vip);
		
		request.setDettaglioVariazioneImportoCapitolo(dv);
		request.setModelDetails(DettaglioVariazioneComponenteImportoCapitoloModelDetail.Flag, DettaglioVariazioneComponenteImportoCapitoloModelDetail.ComponenteImportiCapitolo, ComponenteImportiCapitoloModelDetail.TipoComponenteImportiCapitolo);
		return request;
	}
}
