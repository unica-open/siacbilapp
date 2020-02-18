/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ajax.registroa;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilser.business.utility.StringUtilities;
import it.csi.siac.siaccespapp.frontend.ui.model.ajax.registroa.RisultatiRicercaMovimentoEPRegistroACespiteAjaxModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.registroa.ElementoMovimentoEPRegistroA;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.registroa.ElementoMovimentoEPRegistroAFactory;
import it.csi.siac.siaccespser.frontend.webservice.PrimaNotaCespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoEPRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaMovimentoEPRegistroACespiteResponse;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.model.MovimentoEP;

/**
 * Classe di Action per i risultati ricerca del registro A (prime note verso inventario contabile)del cespite, gestione AJAX.
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/10/2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaMovimentoEPRegistroACespiteAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoMovimentoEPRegistroA, RisultatiRicercaMovimentoEPRegistroACespiteAjaxModel,
		MovimentoEP, RicercaSinteticaMovimentoEPRegistroACespite, RicercaSinteticaMovimentoEPRegistroACespiteResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 3567393262488332089L;

//	private static final Pattern PATTERN = Pattern.compile("%UID%");

	@Autowired private transient PrimaNotaCespiteService primaNotaCespiteService;
	
	private static final String PULSANTE_CONSULTA =  "<button data-uid-movimento-ep=\"%UID%\" type=\"button\" class=\"btn btn-secondary\"> cespiti</button>";
	private static final Pattern PATTERN = Pattern.compile("%UID%");
	// Azioni
//	private static final String AZIONI_CONSENTITE_BEGIN = "<div class=\"btn-group\"><button data-toggle=\"dropdown\" class=\"btn dropdown-toggle\">Azioni<span class=\"caret\"></span></button><ul class=\"dropdown-menu pull-right\">";
//	private static final String AZIONI_CONSENTITE_CONSULTA = "<li><a data-operazione=\"consultaRegistroA\" href=\"risultatiRicercaRegistroACespite_consulta.do?primaNota.uid=%UID%\">Consulta</a></li>";
//	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
//		
//	private List<AzioneConsentita> listaAzioniConsentite;

	/** Costruttore vuoto di default */
	public RisultatiRicercaMovimentoEPRegistroACespiteAjaxAction() {
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_MOVIMENTI_EP_REGISTRO_A_CESPITE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_MOVIMENTI_EP_REGISTRO_A_CESPITE);
	}
	
	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaMovimentoEPRegistroACespite req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaMovimentoEPRegistroACespite req,	ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoMovimentoEPRegistroA ottieniIstanza(MovimentoEP e) throws FrontEndBusinessException {
		return ElementoMovimentoEPRegistroAFactory.getInstance(e);
	}

	@Override
	protected RicercaSinteticaMovimentoEPRegistroACespiteResponse ottieniResponse(RicercaSinteticaMovimentoEPRegistroACespite req) {
		return primaNotaCespiteService.ricercaSinteticaMovimentoEPRegistroACespite(req);
	}

	@Override
	protected ListaPaginata<MovimentoEP> ottieniListaRisultati(RicercaSinteticaMovimentoEPRegistroACespiteResponse response) {
		return response.getMovimentiEP();
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}
	@Override
	protected void gestisciAzioniConsentite(ElementoMovimentoEPRegistroA instance, boolean daRientro, boolean isAggiornaAbilitato, boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		if(Boolean.TRUE.equals(instance.getHasContoCespite())) {
			
			Map<String, String> map = new HashMap<String, String>();
			map.put("%UID%", instance.getUid() + "");

			String azioni = StringUtilities.replacePlaceholders(PATTERN, PULSANTE_CONSULTA, map, false);
			instance.setAzioni(azioni);
		}
	}
	
}
