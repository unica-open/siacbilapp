/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacfelapp.frontend.ui.action.fatturaelettronica;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.exception.FrontEndBusinessException;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.FaseEStatoAttualeBilancio.FaseBilancio;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfelapp.frontend.ui.model.fatturaelettronica.RisultatiRicercaFatturaElettronicaAjaxModel;
import it.csi.siac.siacfelapp.frontend.ui.util.wrapper.ElementoFatturaFEL;
import it.csi.siac.sirfelser.frontend.webservice.FatturaElettronicaService;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaSinteticaFatturaElettronica;
import it.csi.siac.sirfelser.frontend.webservice.msg.RicercaSinteticaFatturaElettronicaResponse;
import it.csi.siac.sirfelser.model.FatturaFEL;

/**
 * Classe di action per i risultati di ricerca della fattura elettronica, gestione AJAX.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 09/06/2015
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaFatturaElettronicaAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoFatturaFEL, RisultatiRicercaFatturaElettronicaAjaxModel,
		FatturaFEL, RicercaSinteticaFatturaElettronica, RicercaSinteticaFatturaElettronicaResponse> {

	/** Per la serializzazione */
	private static final long serialVersionUID = 6583391285142980674L;

	@Autowired private transient FatturaElettronicaService fatturaElettronicaService;
	
	// Azioni
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class=\"btn-group\">"
			+ "<button data-toggle=\"dropdown\" class=\"btn dropdown-toggle\">Azioni<span class=\"caret\"></span></button>"
			+ "<ul class=\"dropdown-menu pull-right\">";
	private static final String AZIONI_CONSENTITE_CONSULTA = "<li><a class=\"consultaFatturaElettronica\" data-href=\"risultatiRicercaFatturaElettronica_consulta.do?fatturaFEL.idFattura=%source.uid%\">consulta</a></li>";
	private static final String AZIONI_CONSENTITE_IMPORTA = "<li><a class=\"importaFatturaElettronica\" data-href=\"risultatiRicercaFatturaElettronica_importa.do?fatturaFEL.idFattura=%source.uid%\">importa</a></li>";
	private static final String AZIONI_CONSENTITE_SOSPENDI = "<li><a class=\"sospendiFatturaElettronica\" data-href=\"risultatiRicercaFatturaElettronica_sospendi.do?fatturaFEL.idFattura=%source.uid%\">sospendi</a></li>";
	private static final String AZIONI_CONSENTITE_END = "</ul>"
			+ "</div>";
		
	private FaseBilancio faseBilancio;
	
	/** Costruttore vuoto di default */
	public RisultatiRicercaFatturaElettronicaAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_FATTURA_FEL);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_FATTURA_FEL);
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaFatturaElettronica req) {
		return req.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaFatturaElettronica req, ParametriPaginazione parametriPaginazione) {
		req.setParametriPaginazione(parametriPaginazione);
	}

	@Override
	protected ElementoFatturaFEL ottieniIstanza(FatturaFEL e) throws FrontEndBusinessException {
		return new ElementoFatturaFEL(e);
	}

	@Override
	protected RicercaSinteticaFatturaElettronicaResponse ottieniResponse(RicercaSinteticaFatturaElettronica req) {
		return fatturaElettronicaService.ricercaSinteticaFatturaElettronica(req);
	}

	@Override
	protected ListaPaginata<FatturaFEL> ottieniListaRisultati(RicercaSinteticaFatturaElettronicaResponse response) {
		return response.getFattureFEL();
	}
	
	@Override
	public void prepare() throws Exception {
		super.prepare();
		// Lo imposto qui e non nel costruttore in quanto non ho ancora il wiring del sessionHandler
		faseBilancio = sessionHandler.getParametro(BilSessionParameter.FASE_BILANCIO);
	}
	
	@Override
	protected boolean controllaDaRientro() {
		boolean result = Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO));
		sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		return result;
	}
	
	@Override
	protected void gestisciAzioniConsentite(ElementoFatturaFEL instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		
		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();
		
		final boolean gestioneConsulta = gestisciConsultazione(listaAzioniConsentite);
		final boolean gestioneImporta = gestisciImportazione(listaAzioniConsentite);
		final boolean gestioneSospendi = gestisciSospensione(listaAzioniConsentite, instance);
		
		StringBuilder strAzioni = new StringBuilder(AZIONI_CONSENTITE_BEGIN);
		
		if (gestioneConsulta) {
			strAzioni.append(AZIONI_CONSENTITE_CONSULTA);
		}
		if(gestioneImporta) {
			strAzioni.append(AZIONI_CONSENTITE_IMPORTA);
		}
		if (gestioneSospendi) {
			strAzioni.append(AZIONI_CONSENTITE_SOSPENDI);
		}
		
		strAzioni.append(AZIONI_CONSENTITE_END);
		
		String azioni = strAzioni.toString().replaceAll("%source.uid%", instance.getUid() + "");
		instance.setAzioni(azioni);
	}
	
	/**
	 * Controlla che la consultazione sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se la consultazione &eacute; consentita; <code>false</code> altrimenti
	 */
	private boolean gestisciConsultazione(List<AzioneConsentita> listaAzioniConsentite) {
		return AzioniConsentiteFactory.isConsentito(AzioniConsentite.FATTURA_ELETTRONICA_RICERCA, listaAzioniConsentite);

	}

	/**
	 * Controlla che l'importazione sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * 
	 * @return <code>true</code> se l'annullamento &eacute; consentita; <code>false</code> altrimenti
	 */
	private boolean gestisciImportazione(List<AzioneConsentita> listaAzioniConsentite) {
		return faseBilancioInValues(faseBilancio, FaseBilancio.ESERCIZIO_PROVVISORIO, FaseBilancio.GESTIONE, FaseBilancio.ASSESTAMENTO, FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO)
				&& AzioniConsentiteFactory.isConsentito(AzioniConsentite.FATTURA_ELETTRONICA_GESTISCI, listaAzioniConsentite);
	}
	
	/**
	 * Controlla che la sospensione sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance              l'istanza da controllare
	 * 
	 * @return <code>true</code> se l'annullamento &eacute; consentita; <code>false</code> altrimenti
	 */
	private boolean gestisciSospensione(List<AzioneConsentita> listaAzioniConsentite, ElementoFatturaFEL instance) {
		return instance.isStatoOperativoDaImportare()
				&& faseBilancioInValues(faseBilancio, FaseBilancio.ESERCIZIO_PROVVISORIO, FaseBilancio.GESTIONE, FaseBilancio.ASSESTAMENTO, FaseBilancio.PREDISPOSIZIONE_CONSUNTIVO)
				&& AzioniConsentiteFactory.isConsentito(AzioniConsentite.FATTURA_ELETTRONICA_GESTISCI, listaAzioniConsentite);
	}

}
