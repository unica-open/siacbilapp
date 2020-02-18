/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfin2app.frontend.ui.action.allegatoatto;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.softwareforge.struts2.breadcrumb.BreadCrumb;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.GenericBilancioAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siaccorser.model.errore.ErroreCore;
import it.csi.siac.siacfin2app.frontend.ui.model.allegatoatto.RicercaAllegatoAttoConvalidaMultiploModel;
import it.csi.siac.siacfin2ser.model.StatoOperativoAllegatoAtto;

/**
 * Classe di Action per la ricerca dell'allegato atto.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 11/set/2014
 * @version 1.0.1 - 15/set/2014 - Aggiunta della classe base
 * @version 1.0.2 - 26/feb/2018 aggiunta ulteriore classe base
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RicercaAllegatoAttoConvalidaMultiploAction extends RicercaAllegatoAttoBaseAction<RicercaAllegatoAttoConvalidaMultiploModel> {
	
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 9151430302065643477L;


	@Override
	@BreadCrumb(GenericBilancioAction.MODEL_TITOLO)
	public String execute() throws Exception {
		return SUCCESS;
	}

	@Override
	protected void caricaListaStatoOperativoAllegatoAtto() {
		//CR-5584, carico gli atti che possono essere completati
		model.setListaStatoOperativoAllegatoAtto(Arrays.asList(StatoOperativoAllegatoAtto.COMPLETATO, StatoOperativoAllegatoAtto.PARZIALMENTE_CONVALIDATO));
	}
	
	@Override
	protected BilSessionParameter ottieniParametroSessioneRequest() {
		return BilSessionParameter.REQUEST_RICERCA_ALLEGATO_ATTO_MULT;
	}

	@Override
	protected BilSessionParameter ottieniParametroSessioneRisultatiRicerca() {
		return BilSessionParameter.RISULTATI_RICERCA_ALLEGATO_ATTO_MULT;
	}
	
	/**
	 * 
	 */
	@Override
	protected void impostaMessaggioNessunDatoTrovato() {
		List<String> descrizioniStati = model.getDescrizioniListaStatiOperativiAllegatoAtto();
		if(model.getAllegatoAtto().getStatoOperativoAllegatoAtto() != null || descrizioniStati == null || descrizioniStati.isEmpty()) {
			//ho filtrato per stato operativo dell'allegato atto, l'errore puo' essere generico
			addErrore(ErroreCore.NESSUN_DATO_REPERITO.getErrore());
		}
		//non ho filtrato per stato, ma solo per gli stati convalidabili: aggiungo informazione per l'utente 
		addErrore(ErroreCore.ENTITA_NON_TROVATA.getErrore("allegato atto", "in uno dei seguenti stati: " + StringUtils.join(descrizioniStati, ", ")));
	}
	
}

