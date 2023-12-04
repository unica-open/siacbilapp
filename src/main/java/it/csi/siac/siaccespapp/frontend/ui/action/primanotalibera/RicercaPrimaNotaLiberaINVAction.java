/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.primanotalibera;

import java.util.ArrayList;
import java.util.List;

import xyz.timedrain.arianna.plugin.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotalibera.RicercaPrimaNotaLiberaBaseAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siaccespapp.frontend.ui.model.primanotalibera.RicercaPrimaNotaLiberaINVModel;
import it.csi.siac.siaccommonapp.util.exception.WebServiceInvocationFailureException;
import it.csi.siac.siacgenser.model.Evento;

/**
 * Classe di action per la ricerca della prima nota libera
 * 
 * @author Elisa Chiari
 * @version 1.0.1 - 02/10/2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaPrimaNotaLiberaINVAction extends RicercaPrimaNotaLiberaBaseAction<RicercaPrimaNotaLiberaINVModel> {
	
	/**
	 * Per la serializzazione
	 */
	private static final long serialVersionUID = 8839454792052684074L;


	@Override
	@BreadCrumb("%{model.titolo}")
	public String execute() throws Exception {
		// Uso il metodo solo per forzare il breadcrumb
		return super.execute();
	}
	
	@Override
	protected BilSessionParameter getBilSessionParameterRequest() {
		return BilSessionParameter.REQUEST_RICERCA_PRIMANOTALIBERA_INV;
	}
	
	
	@Override
	protected BilSessionParameter getBilSessionParameterRisultati() {
		return BilSessionParameter.RISULTATI_RICERCA_PRIMANOTALIBERA_INV;
	}

	@Override
	protected BilSessionParameter getBilSessionParameterListeCausali() {
		return BilSessionParameter.LISTA_CAUSALE_EP_LIBERA_INV;
	}

	@Override
	protected List<Evento> caricaListaEvento() throws WebServiceInvocationFailureException {
		List<Evento> listaEvento = super.caricaListaEvento();		
		List<Evento> listaRis    = new ArrayList<Evento>();		
		//filtro i soli eventi con EVENTO TIPO  INV-COGE
		for (Evento e : listaEvento) {
			//log.info("tipo evento --> ", e.getTipoEvento().getCodice());			
			if (e != null && e.getTipoEvento() != null && e.getTipoEvento().getCodice().equals(BilConstants.CODICE_EVENTO_INVENTARIO_CONTABILITA.getConstant())) {
				listaRis.add(e);
				model.setTipoEvento(e.getTipoEvento());
			}
		}
		model.setListaEvento(listaRis);	
		return listaEvento;
		
	}
	
}
