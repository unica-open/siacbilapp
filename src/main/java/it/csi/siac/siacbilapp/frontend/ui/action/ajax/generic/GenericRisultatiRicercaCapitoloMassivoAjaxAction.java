/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic;

import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.ricerca.ElementoCapitoloFactory;
import it.csi.siac.siacbilser.model.Capitolo;
import it.csi.siac.siaccorser.model.ServiceRequest;
import it.csi.siac.siaccorser.model.ServiceResponse;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;

/**
 * Action generica per i risultati di ricerca massiva del capitolo.
 * 
 * @author LG, AM
 * 
 * @param <CAP> la parametrizzazione del Capitolo 
 * @param <REQ> la parametrizzazione del la Request
 * @param <RES> la parametrizzazione della Response
 * 
 */
public abstract class GenericRisultatiRicercaCapitoloMassivoAjaxAction<CAP extends Capitolo<?, ?>, REQ extends ServiceRequest, RES extends ServiceResponse> 
	extends GenericRisultatiRicercaCapitoloAjaxAction<CAP, REQ, RES> {	
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -4860803788823522637L;

	@Override
	protected int ottieniTotaleRecords(ListaPaginata<CAP> listaRisultatiDaCuiOttenereIlTotale) {
		return listaRisultatiDaCuiOttenereIlTotale.getTotaleElementi();
	}
	
	@Override
	protected ElementoCapitolo getInstance(CAP cap) throws FrontEndBusinessException {
		return ElementoCapitoloFactory.getInstance(cap, true, model.isGestioneUEB());
	}
	
	@Override
	protected boolean gestisciAnnullamento(boolean bIsRientro, boolean isAnnullaAbilitato, ElementoCapitolo el) {
		return false;
	}
	
	@Override
	protected boolean gestisciEliminazione(boolean isEliminaAbilitato) {
		return false;
	}
	
	@Override
	protected String gestisciAzioneAggiorna(String azioneAggiorna, ElementoCapitolo el) {
		return azioneAggiorna.replaceAll("%altriParametri%", "&annoCapitoloDaAggiornare=" + el.getAnnoCapitolo() + 
				"&numeroCapitoloDaAggiornare=" + el.getNumeroCapitolo() + 
				"&numeroArticoloDaAggiornare=" + el.getNumeroArticolo())
			.replace("%Massiva%", "Massiva");
	}
	
	@Override
	protected String gestisciAzioneConsulta(String azioneConsulta, ElementoCapitolo el) {
		return azioneConsulta.replaceAll("%altriParametri%", "&annoCapitoloDaConsultare=" + el.getAnnoCapitolo() + 
				"&numeroCapitoloDaConsultare=" + el.getNumeroCapitolo() + 
				"&numeroArticoloDaConsultare=" + el.getNumeroArticolo())
			.replace("%Massiva%", "Massiva");
	}
	
	@Override
	protected boolean controllaDatiReperiti(ListaPaginata<CAP> lista) {
		return !lista.isEmpty();
	}
	
}