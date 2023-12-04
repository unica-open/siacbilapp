/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgenapp.frontend.ui.action.primanotaintegrata;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.siac.siacbasegengsaapp.frontend.ui.action.primanotaintegrata.RisultatiRicercaPrimaNotaIntegrataBaseAjaxAction;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotaintegrata.ElementoPrimaNotaIntegrata;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siaccorser.util.AzioneConsentitaEnum;
import it.csi.siac.siacbilser.model.Ambito;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.FaseBilancio;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Classe di action per i risultati di ricerca della prima nota integrata, gestione AJAX. Modulo GEN
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 14/05/2015
 * @version 1.0.1 - 16/06/2015
 * @version 1.1.0 - 08/10/2015 - gestione GEN/GSA
 */
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RisultatiRicercaPrimaNotaIntegrataFINAjaxAction extends RisultatiRicercaPrimaNotaIntegrataBaseAjaxAction {

	/** Per la serializzazione */
	private static final long serialVersionUID = -2151157094226181748L;
	
	private static final String[] EVENTI_RATEI_RISCONTI = new String[] {
			BilConstants.DESCRIZIONE_TIPO_EVENTO_IMPEGNO.getConstant(),
			BilConstants.DESCRIZIONE_TIPO_EVENTO_ACCERTAMENTO.getConstant(),
			BilConstants.DESCRIZIONE_TIPO_EVENTO_DOCUMENTO_ENTRATA.getConstant(),
			BilConstants.DESCRIZIONE_TIPO_EVENTO_DOCUMENTO_SPESA.getConstant(),
			BilConstants.DESCRIZIONE_TIPO_EVENTO_LIQUIDAZIONE.getConstant(),
		}; 

	/** Costruttore vuoto di default */
	public RisultatiRicercaPrimaNotaIntegrataFINAjaxAction() {
		super();
		setParametroSessioneLista(BilSessionParameter.RISULTATI_RICERCA_PRIMANOTAINTEGRATA_GEN);
		setParametroSessioneRequest(BilSessionParameter.REQUEST_RICERCA_PRIMANOTAINTEGRATA_GEN);
	}

	@Override
	protected String getCodiceAmbito() {
		return Ambito.AMBITO_FIN.getSuffix();
	}

	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaValidaPrimaNotaIntegrata() {
		return AzioneConsentitaEnum.PRIMA_NOTA_INTEGRATA_VALIDA_GEN;
	}

	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaRicercaPrimaNotaIntegrata() {
		return AzioneConsentitaEnum.PRIMA_NOTA_INTEGRATA_RICERCA_GEN;
	}

	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaGestisciPrimaNotaIntegrata() {
		return AzioneConsentitaEnum.PRIMA_NOTA_INTEGRATA_GESTISCI_GEN;
	}
	
	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaRateiRisconti() {
		return AzioneConsentitaEnum.RATEI_RISCONTI;
	}

	@Override
	protected AzioneConsentitaEnum getAzioneConsentitaAggiornaPrimaNotaIntegrata() {
		return AzioneConsentitaEnum.PRIMA_NOTA_INTEGRATA_AGGIORNA_GEN;
	}

	@Override
	protected boolean isStatoCoerenteConAggiornamento(ElementoPrimaNotaIntegrata instance) {
		return StatoOperativoPrimaNota.PROVVISORIO.equals(instance.getStatoOperativoPrimaNota());
	}
	
	//SIAC-6208
	/**
	 * Controlla che l'aggiornamento dei risconti sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance              l'istanza da controllare
	 * 
	 * @return <code>true</code> se l'aggiornamento &eacute; consentito; <code>false</code> altrimenti
	 */
	@Override
	protected boolean gestisciAggiornaRisconti(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaIntegrata instance) {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE, FaseBilancio.CHIUSO)
				&& AzioniConsentiteFactory.isConsentito(getAzioneConsentitaRateiRisconti(), listaAzioniConsentite) 
				&& (instance.getRisconti() != null && !instance.getRisconti().isEmpty())
				&& tipoEventoLecitoPerRateiRisconti(instance)
				&& StatoOperativoPrimaNota.DEFINITIVO.equals(instance.getStatoOperativoPrimaNota());
	}

	/**
	 * Controlla che l'aggiornamento del rateo sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance              l'istanza da controllare
	 * 
	 * @return <code>true</code> se l'aggiornamento &eacute; consentito; <code>false</code> altrimenti
	 */
	@Override
	protected boolean gestisciAggiornaRateo(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaIntegrata instance) {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE, FaseBilancio.CHIUSO)
				&& AzioniConsentiteFactory.isConsentito(getAzioneConsentitaRateiRisconti(), listaAzioniConsentite)
				&& (instance.getRateo() != null && instance.getRateo().getUid() != 0)
				&& bilancioPrecedenteNonChiuso()
				&& tipoEventoLecitoPerRateiRisconti(instance)
				&& StatoOperativoPrimaNota.DEFINITIVO.equals(instance.getStatoOperativoPrimaNota());
	}

	/**
	 * Controlla che l'inserimento di un risconto sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance              l'istanza da controllare
	 * 
	 * @return <code>true</code> se l'inserimento &eacute; consentito; <code>false</code> altrimenti
	 */
	@Override
	protected boolean gestisciInserisciRisconti(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaIntegrata instance) {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE, FaseBilancio.CHIUSO)
				&& AzioniConsentiteFactory.isConsentito(getAzioneConsentitaRateiRisconti(), listaAzioniConsentite) 
				&& (instance.getRisconti() == null || instance.getRisconti().isEmpty())
				&& tipoEventoLecitoPerRateiRisconti(instance)
				&& StatoOperativoPrimaNota.DEFINITIVO.equals(instance.getStatoOperativoPrimaNota());
	}

	/**
	 * Controlla che l'inserimento di un rateo sia eseguibile.
	 * 
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance              l'istanza da controllare
	 * 
	 * @return <code>true</code> se l'inserimento &eacute; consentito; <code>false</code> altrimenti
	 */
	@Override
	protected boolean gestisciInserisciRateo(List<AzioneConsentita> listaAzioniConsentite, ElementoPrimaNotaIntegrata instance) {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE, FaseBilancio.CHIUSO)
				&& AzioniConsentiteFactory.isConsentito(getAzioneConsentitaRateiRisconti(), listaAzioniConsentite) 
				&& (instance.getRateo() == null || instance.getRateo().getUid() == 0)
				&& bilancioPrecedenteNonChiuso()
				&& tipoEventoLecitoPerRateiRisconti(instance)
				&& StatoOperativoPrimaNota.DEFINITIVO.equals(instance.getStatoOperativoPrimaNota());
	}

	
	/**
	 * Controlla che la prima nota sia relativa ad evento a cui sia lecito collegare un rateo oppure un riconto.
	 *
	 * @param instance l'istanza da controllare
	 * @return <code>true</code> se risulta possibile il collegamento con un rateo oppure un risconto, <code>false</code> altrimenti
	 */
	private boolean tipoEventoLecitoPerRateiRisconti(ElementoPrimaNotaIntegrata instance){
		List<String> eventiLeciti = Arrays.asList(EVENTI_RATEI_RISCONTI);
		return eventiLeciti.contains(instance.getTipoEvento()) && (!instance.isPrimaNotaDaNCD());
	}
	
	/**
	 * Controlla che il bilancio precedente non sia chiuso
	 * 
	 * @return  <code>true</code> se il bilancio non &eacute; chiuso; <code>false</code> altrimenti
	 */
	private boolean bilancioPrecedenteNonChiuso(){
		FaseBilancio faseBilancioPrecedente = sessionHandler.getParametro(BilSessionParameter.FASE_BILANCIO_PRECEDENTE);
		return !faseBilancioInValues(faseBilancioPrecedente, FaseBilancio.CHIUSO);
	}
	
	//SIAC-8323
	protected boolean isFaseBilancioCompatibileConAggiornamento() {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE, FaseBilancio.CHIUSO);
	}

	protected boolean isFaseBilancioCompatibileConValidazione() {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.CHIUSO);
	}

	protected boolean isFaseBilancioCompatibileConAnnullamento() {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE, FaseBilancio.CHIUSO);
	}
	
	protected boolean isFaseBilancioCompatibileConCollegamento() {
		return !faseBilancioInValues(faseBilancio, FaseBilancio.CHIUSO);
	}
}
