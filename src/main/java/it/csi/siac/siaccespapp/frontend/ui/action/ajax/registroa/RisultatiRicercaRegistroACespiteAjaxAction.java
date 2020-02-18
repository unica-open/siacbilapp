/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.action.ajax.registroa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.business.utility.StringUtilities;
import it.csi.siac.siaccespapp.frontend.ui.model.ajax.registroa.RisultatiRicercaRegistroACespiteAjaxModel;
import it.csi.siac.siaccespapp.frontend.ui.util.wrappers.registroa.ElementoPrimaNotaRegistroA;
import it.csi.siac.siaccespser.frontend.webservice.CespiteService;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaRegistroACespite;
import it.csi.siac.siaccespser.frontend.webservice.msg.RicercaSinteticaRegistroACespiteResponse;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaDefinitiva;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Classe di Action per i risultati ricerca del registro A (prime note verso inventario contabile)del cespite, gestione AJAX.
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/10/2018
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaRegistroACespiteAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoPrimaNotaRegistroA, RisultatiRicercaRegistroACespiteAjaxModel,
		PrimaNota, RicercaSinteticaRegistroACespite, RicercaSinteticaRegistroACespiteResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = -7775372158496507839L;

	private static final Pattern PATTERN = Pattern.compile("%UID%");

	@Autowired private transient CespiteService cespiteService;
	
	// Azioni
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class=\"btn-group\"><button data-toggle=\"dropdown\" class=\"btn dropdown-toggle\">Azioni<span class=\"caret\"></span></button><ul class=\"dropdown-menu pull-right\">";

	private static final String AZIONI_CONSENTITE_RIFIUTA = "<li><a data-operazione=\"rifiutaRegistroA\" href=\"#\">Rifiuta</a></li>";
	private static final String AZIONI_CONSENTITE_CONSULTA = "<li><a data-operazione=\"consultaRegistroA\" href=\"#\">Consulta</a></li>";
	private static final String AZIONI_CONSENTITE_INIZIALIZZA_INTEGRAZIONE = "<li><a data-operazione=\"integraRegistroA\" href=\"#\">Integra con Inventario Contabile</a></li>";
	private static final String AZIONI_CONSENTITE_AGGIORNA = "<li><a data-operazione=\"aggiornaRegistroA\" href=\"#\">Integra con inventario</a></li>";
	
	private static final String AZIONI_CONSENTITE_END = "</ul></div>";
		
	private FaseBilancio faseBilancio;
	private List<AzioneConsentita> listaAzioniConsentite;

	/** Costruttore vuoto di default */
	public RisultatiRicercaRegistroACespiteAjaxAction() {
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_REGISTRO_A_CESPITE);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_REGISTRO_A_CESPITE);
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaRegistroACespite req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaRegistroACespite req, ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoPrimaNotaRegistroA ottieniIstanza(PrimaNota e) throws FrontEndBusinessException {
		return new ElementoPrimaNotaRegistroA(e);
	}

	@Override
	protected RicercaSinteticaRegistroACespiteResponse ottieniResponse(RicercaSinteticaRegistroACespite req) {
		return cespiteService.ricercaSinteticaRegistroACespite(req);
	}

	@Override
	protected ListaPaginata<PrimaNota> ottieniListaRisultati(RicercaSinteticaRegistroACespiteResponse response) {
		return response.getPrimeNote();
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Lo imposto qui e non nel costruttore in quanto non ho ancora il wiring del sessionHandler
		faseBilancio = sessionHandler.getParametro(BilSessionParameter.FASE_BILANCIO);
		listaAzioniConsentite = sessionHandler.getAzioniConsentite();
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoPrimaNotaRegistroA instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		
		final boolean gestioneRifiuta = isGestisciRifiuta(instance);
		final boolean gestioneConsulta = isGestisciConsulta();
		final boolean gestioneInizializzaIntegrazione = isGestisciInizializzaIntegrazione(instance);
		final boolean gestioneAggiorna = isGestisciAggiorna(instance);
		
		StringBuilder strAzioni = new StringBuilder();
		appendIfTrue(strAzioni, true, AZIONI_CONSENTITE_BEGIN);
		appendIfTrue(strAzioni, gestioneRifiuta, AZIONI_CONSENTITE_RIFIUTA);
		appendIfTrue(strAzioni, gestioneConsulta, AZIONI_CONSENTITE_CONSULTA);
		appendIfTrue(strAzioni, gestioneInizializzaIntegrazione, AZIONI_CONSENTITE_INIZIALIZZA_INTEGRAZIONE);
		appendIfTrue(strAzioni, gestioneAggiorna, AZIONI_CONSENTITE_AGGIORNA);
		appendIfTrue(strAzioni, true, AZIONI_CONSENTITE_END);
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("%UID%", instance.getUid() + "");

		String azioni = StringUtilities.replacePlaceholders(PATTERN, strAzioni.toString(), map, false);

		instance.setAzioni(azioni);
	}

	/**
	 * Controlla se la gestione del rifiuto sia consentita
	 * @param instance l'istanza
	 * @return <code>true</code> se il rifiuto &eacute; consentito; <code>false</code> altrimenti
	 */
	private boolean isGestisciRifiuta(ElementoPrimaNotaRegistroA instance) {
		PrimaNota pn = instance.unwrap();
		return
			faseBilancioInValues(faseBilancio, FaseBilancio.ESERCIZIO_PROVVISORIO, FaseBilancio.GESTIONE, FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO)
			&& AzioniConsentiteFactory.isConsentito(AzioniConsentite.REGISTRO_A_CESPITE_GESTIONE, listaAzioniConsentite)
			&& StatoOperativoPrimaNota.DEFINITIVO.equals(pn.getStatoOperativoPrimaNota())
			//si puo' rifiutare una prima nota gia' integrata, purche' non sia collegata a cespiti (controllo presente sul servizio)
			&& (pn.getPrimaNotaInventario() == null || StatoAccettazionePrimaNotaDefinitiva.DA_ACCETTARE.equals(pn.getPrimaNotaInventario().getStatoAccettazionePrimaNotaDefinitiva()));
	}

	/**
	 * Controlla se la gestione del rifiuto sia consentita
	 * @return <code>true</code> se la consultazione &eacute; consentita; <code>false</code> altrimenti
	 */
	private boolean isGestisciConsulta() {
		return AzioniConsentiteFactory.isConsentito(AzioniConsentite.REGISTRO_A_CESPITE_RICERCA, listaAzioniConsentite);

	}
	
	/**
	 * Controlla se la gestione dell'integrazione sia consentita
	 * @param instance l'istanza
	 * @return <code>true</code> se l'integrazione &eacute; consentita; <code>false</code> altrimenti
	 */
	private boolean isGestisciInizializzaIntegrazione(ElementoPrimaNotaRegistroA instance) {
		PrimaNota pn = instance.unwrap();
		return
			faseBilancioInValues(faseBilancio, FaseBilancio.ESERCIZIO_PROVVISORIO, FaseBilancio.GESTIONE, FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO)
			&& AzioniConsentiteFactory.isConsentito(AzioniConsentite.REGISTRO_A_CESPITE_GESTIONE, listaAzioniConsentite)
			&& StatoOperativoPrimaNota.DEFINITIVO.equals(pn.getStatoOperativoPrimaNota())
			&& pn.getPrimaNotaInventario() == null;
	}
	
	
	/**
	 * Controlla se la gestione dell'aggiornamento sia consentita
	 * @param instance l'istanza
	 * @return <code>true</code> se l'aggiornamento &eacute; consentita; <code>false</code> altrimenti
	 */
	private boolean isGestisciAggiorna(ElementoPrimaNotaRegistroA instance) {
		PrimaNota pn = instance.unwrap();
		return
			faseBilancioInValues(faseBilancio, FaseBilancio.ESERCIZIO_PROVVISORIO, FaseBilancio.GESTIONE, FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO)
			&& AzioniConsentiteFactory.isConsentito(AzioniConsentite.REGISTRO_A_CESPITE_GESTIONE, listaAzioniConsentite)
			&& StatoOperativoPrimaNota.DEFINITIVO.equals(pn.getStatoOperativoPrimaNota())
			&& pn.getPrimaNotaInventario() != null
			&& StatoAccettazionePrimaNotaDefinitiva.DA_ACCETTARE.equals(pn.getPrimaNotaInventario().getStatoAccettazionePrimaNotaDefinitiva());
	}
}
