/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.previmpacc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.model.previmpacc.ConsultaPrevisioneImpegnatoAccertatoModel;
import it.csi.siac.siacbilser.frontend.webservice.CapitoloService;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStanziamentiCapitoloGestione;
import it.csi.siac.siacbilser.frontend.webservice.msg.RicercaStanziamentiCapitoloGestioneResponse;
import it.csi.siac.siacbilser.model.ImportiCapitolo;


/**
 * The Class RisultatiRicercaPrevisioneImpegnatoAccertatoBaseAction.
 *
 * @author elisa
 * @version 1.0.0 15 ott 2021
 * @param <MODEL> the generic type
 */
public abstract class ConsultaPrevisioneImpegnatoAccertatoBaseAction<MODEL extends ConsultaPrevisioneImpegnatoAccertatoModel> extends GenericBilancioAction<MODEL> {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 1621845443571811020L;
	
	
	@Autowired private CapitoloService capitoloService;
	

	
	
	protected void caricaImporti() {
		RicercaStanziamentiCapitoloGestione request = model.creaRequestRicercaStanziamentiCapitolo();
		RicercaStanziamentiCapitoloGestioneResponse response = capitoloService.ricercaStanziamentiCapitoloGestione(request);
		if(response.hasErrori()) {
			addErrori(response);
			return;
		}
		impostaImportiCapitolo(response.getListaImportiCapitolo());
		model.setPrevisioneImpegnatoAccertato(response.getPrevisioneImpegnatoAccertato());
	}
	
	private void impostaImportiCapitolo(List<ImportiCapitolo> listaImportiCapitolo) {
		int annoEsercizio = model.getAnnoEsercizioInt().intValue();
		for (ImportiCapitolo ic : listaImportiCapitolo) {
			
			if(ic.getAnnoCompetenza().intValue() == annoEsercizio) {
				model.setImportiCapitolo0(ic);
			}else if(ic.getAnnoCompetenza().intValue() == (annoEsercizio +1)) {
				model.setImportiCapitolo1(ic);
			}else if(ic.getAnnoCompetenza().intValue() == (annoEsercizio +2)) {
				model.setImportiCapitolo2(ic);
			}
		}
	}
	
	public String backToRicerca() {
		return SUCCESS;
	}
	
	

}
