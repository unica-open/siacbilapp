/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.action.registrazionemovfin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.siac.siacbasegengsaapp.frontend.ui.model.registrazionemovfin.RisultatiRicercaRegistrazioneMovFinBaseAjaxModel;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.risultatiricerca.ElementoRegistrazioneMovFin;
import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.risultatiricerca.ElementoRegistrazioneMovFinFactory;
import it.csi.siac.siacbilapp.frontend.ui.action.ajax.generic.GenericRisultatiRicercaAjaxAction;
import it.csi.siac.siacbilapp.frontend.ui.handler.session.BilSessionParameter;
import it.csi.siac.siacbilapp.frontend.ui.util.BilConstants;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.azioni.AzioniConsentiteFactory;
import it.csi.siac.siacbilser.business.utility.AzioniConsentite;
import it.csi.siac.siacbilser.business.utility.StringUtilities;
import it.csi.siac.siaccorser.model.AzioneConsentita;
import it.csi.siac.siaccorser.model.paginazione.ListaPaginata;
import it.csi.siac.siaccorser.model.paginazione.ParametriPaginazione;
import it.csi.siac.siacfin2ser.model.SubdocumentoEntrata;
import it.csi.siac.siacfin2ser.model.SubdocumentoSpesa;
import it.csi.siac.siacfinser.model.Accertamento;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubAccertamento;
import it.csi.siac.siacfinser.model.SubImpegno;
import it.csi.siac.siacfinser.model.liquidazione.Liquidazione;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneEntrata;
import it.csi.siac.siacfinser.model.movgest.ModificaMovimentoGestioneSpesa;
import it.csi.siac.siacfinser.model.ordinativo.Ordinativo;
import it.csi.siac.siacgenser.frontend.webservice.RegistrazioneMovFinService;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFin;
import it.csi.siac.siacgenser.frontend.webservice.msg.RicercaSinteticaRegistrazioneMovFinResponse;
import it.csi.siac.siacgenser.model.RegistrazioneMovFin;
import it.csi.siac.siacgenser.model.StatoOperativoRegistrazioneMovFin;
import it.csi.siac.siacgenser.model.TipoCollegamento;

/**
 * Action base per i risultati di ricerca delle registrazioniMovFIn.
 *
 * @author Marchino Alessandro
 * @version 1.0.0 - 05/10/2015
 *
 */
public abstract class RisultatiRicercaRegistrazioneMovFinBaseAjaxAction extends GenericRisultatiRicercaAjaxAction<ElementoRegistrazioneMovFin,
	RisultatiRicercaRegistrazioneMovFinBaseAjaxModel, RegistrazioneMovFin, RicercaSinteticaRegistrazioneMovFin, RicercaSinteticaRegistrazioneMovFinResponse> {

	/** Per la serialiazzazione */
	private static final long serialVersionUID = -5765791974403538285L;

	private static final Pattern PATTERN = Pattern.compile("%(TYPE|UIDRICHIESTA|UID|MOV|NUM|ANNO|NUMSUB|UIDPRIMANOTA)%");

	/** Stringhe HTML per azioni consentite */
	private static final String AZIONI_CONSENTITE_BEGIN = "<div class='btn-group'> " +
			"<button class='btn dropdown-toggle' data-placement='left' data-toggle='dropdown' href='#'>Azioni <span class='caret'></span></button>" +
			"<ul class='dropdown-menu pull-right' >";

	private static final String AZIONI_CONSENTITE_COMPLETA =
			"<li><a href='risultatiRicercaRegistrazioneMovFin%TYPE%_completa.do?uidDaCompletare=%UIDRICHIESTA%&nomeAzione=gestionePrimaNotaIntegrata%TYPE%&validazione=false'>completa</a></li>";

	private static final String AZIONI_CONSENTITE_COMPLETA_VALIDA =
			"<li><a href='risultatiRicercaRegistrazioneMovFin%TYPE%_completaEValida.do?uidDaCompletare=%UIDRICHIESTA%&nomeAzione=gestionePrimaNotaIntegrata%TYPE%&validazione=true'>completa e valida</a></li>";

	private static final String AZIONI_CONSENTITE_VALIDA =
			"<li><a href='risultatiRicercaRegistrazioneMovFin%TYPE%_valida.do?uidDaValidare=%UIDRICHIESTA%'>valida</a></li>";

	private static final String AZIONI_CONSENTITE_CONSULTA_UID =
			"<li><a href='risultatiRicercaRegistrazioneMovFin%TYPE%_consulta%MOV%.do?uidDaConsultare=%UID%'>consulta</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA_UID_PRIMA_NOTA =
			"<li><a href='risultatiRicercaRegistrazioneMovFin%TYPE%_consulta%MOV%.do?uidPrimaNotaRegistrazioneDaConsultare=%UIDPRIMANOTA%&uidDaConsultare=%UID%'>consulta</a></li>";

	private static final String AZIONI_CONSENTITE_CONSULTA_ANNO_NUMERO =
			"<li><a href='risultatiRicercaRegistrazioneMovFin%TYPE%_consulta%MOV%.do?numeroMovimentoDaConsultare=%NUM%&annoMovimentoDaConsultare=%ANNO%'>consulta</a></li>";

	private static final String AZIONI_CONSENTITE_CONSULTA_ANNO_NUMERO_SUB =
			"<li><a href='risultatiRicercaRegistrazioneMovFin%TYPE%_consulta%MOV%.do?numeroMovimentoDaConsultare=%NUM%&annoMovimentoDaConsultare=%ANNO%&numeroSubMovimentoDaConsultare=%NUMSUB%'>consulta</a></li>";

	private static final String AZIONI_CONSENTITE_CONSULTA_ANNO_NUMERO_UID =
			"<li><a href='risultatiRicercaRegistrazioneMovFin%TYPE%_consulta%MOV%.do?uidDaConsultare=%UID%&numeroMovimentoDaConsultare=%NUM%&annoMovimentoDaConsultare=%ANNO%'>consulta</a></li>";
	
	private static final String AZIONI_CONSENTITE_CONSULTA_ANNO_NUMERO_SUB_UID =
			"<li><a href='risultatiRicercaRegistrazioneMovFin%TYPE%_consulta%MOV%.do?uidDaConsultare=%UID%&numeroMovimentoDaConsultare=%NUM%&annoMovimentoDaConsultare=%ANNO%&numeroSubMovimentoDaConsultare=%NUMSUB%''>consulta</a></li>";

//	private static final String AZIONI_CONSENTITE_ANNULLA =
//			"<li><a href='#msgAnnulla' data-toggle='modal'>annulla</a></li>";
	private static final String AZIONI_CONSENTITE_AGGIORNA_PDC =
			"<li><a href='#aggiornaPdC' >aggiorna PdC Finanziario</a></li>";

	private static final String AZIONI_CONSENTITE_END = "</ul></div>";

	@Autowired private transient RegistrazioneMovFinService registrazioneMovFinService;

	@Override
	protected boolean controllaDaRientro() {
		boolean result = false;
		if(Boolean.TRUE.equals(sessionHandler.getParametro(BilSessionParameter.RIENTRO, Boolean.class))) {
			result = true;
			sessionHandler.setParametro(BilSessionParameter.RIENTRO, null);
		}
		return result;
	}

	@Override
	protected ParametriPaginazione ottieniParametriDiPaginazione(RicercaSinteticaRegistrazioneMovFin request) {
		return request.getParametriPaginazione();
	}

	@Override
	protected void impostaParametriPaginazione(RicercaSinteticaRegistrazioneMovFin request, ParametriPaginazione parametriPaginazione) {
		request.setParametriPaginazione(parametriPaginazione);

	}

	@Override
	protected ElementoRegistrazioneMovFin ottieniIstanza(RegistrazioneMovFin e) {
		return ElementoRegistrazioneMovFinFactory.getInstance(e);
	}

	@Override
	protected RicercaSinteticaRegistrazioneMovFinResponse ottieniResponse(RicercaSinteticaRegistrazioneMovFin request) {
		return registrazioneMovFinService.ricercaSinteticaRegistrazioneMovFin(request);
	}

	@Override
	protected ListaPaginata<RegistrazioneMovFin> ottieniListaRisultati(RicercaSinteticaRegistrazioneMovFinResponse response) {
		return response.getRegistrazioniMovFin();
	}

	@Override
	protected void gestisciAzioniConsentite(ElementoRegistrazioneMovFin instance, boolean daRientro, boolean isAggiornaAbilitato,
			boolean isAnnullaAbilitato, boolean isConsultaAbilitato, boolean isEliminaAbilitato) {
		if(!isValidRegistrazione(instance)) {
			instance.setAzioni("");
			return;
		}

		List<AzioneConsentita> listaAzioniConsentite = sessionHandler.getAzioniConsentite();

		log.logXmlTypeObject(listaAzioniConsentite, "azioni consentite");
		
		boolean gestionePrimaNotaConsentita = isGestionePrimaNotaConsentita(listaAzioniConsentite, instance);
//		boolean annullaConsentita = isAnnullaConsentita(listaAzioniConsentite);
		boolean consultaConsentita = isConsultaConsentita(listaAzioniConsentite);
		boolean aggiornaPdCConsentita = isAggiornaPdCConsentita(listaAzioniConsentite) && instance.getUidPianoDeiContiFIN()!=0;

		TipoCollegamento tipoCollegamento = instance.getTipoCollegamento();
		boolean isImpPrg = isImpPrg(instance);

		Integer anno = null;
		Integer numero = null;
		Integer numeroSub = null;

		// Gestione delle azioni consentite
		StringBuilder azioniBuilder = new StringBuilder();
		azioniBuilder.append(AZIONI_CONSENTITE_BEGIN);

		if(gestionePrimaNotaConsentita) {
			if(isImpPrg) {
				azioniBuilder.append(AZIONI_CONSENTITE_VALIDA);
			} else {
				azioniBuilder.append(AZIONI_CONSENTITE_COMPLETA);
				azioniBuilder.append(AZIONI_CONSENTITE_COMPLETA_VALIDA);
			}
		}

//		if(annullaConsentita) {
//			azioniBuilder.append(AZIONI_CONSENTITE_ANNULLA);
//		}

		if(consultaConsentita) {
			if(TipoCollegamento.LIQUIDAZIONE.equals(tipoCollegamento)){
				azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA_ANNO_NUMERO);
				Liquidazione liquidazione = instance.obtainMovimentoCast();
				anno = liquidazione.getAnnoLiquidazione();
				numero = liquidazione.getNumeroLiquidazione().intValue();
			} else if(TipoCollegamento.ORDINATIVO_INCASSO.equals(tipoCollegamento) || TipoCollegamento.ORDINATIVO_PAGAMENTO.equals(tipoCollegamento)){
				azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA_ANNO_NUMERO);
				Ordinativo ordinativo = instance.obtainMovimentoCast();
				anno = ordinativo.getAnno();
				numero = ordinativo.getNumero();
			} else if(TipoCollegamento.IMPEGNO.equals(tipoCollegamento)){
				azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA_ANNO_NUMERO);
				Impegno impegno = instance.obtainMovimentoCast();
				anno = impegno.getAnnoMovimento();
				numero = impegno.getNumero().intValue();
			} else if(TipoCollegamento.ACCERTAMENTO.equals(tipoCollegamento)){
				azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA_ANNO_NUMERO);
				Accertamento accertamento = instance.obtainMovimentoCast();
				anno = accertamento.getAnnoMovimento();
				numero = accertamento.getNumero().intValue();
			} else if(TipoCollegamento.SUBIMPEGNO.equals(tipoCollegamento)){
				azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA_ANNO_NUMERO_SUB);
				SubImpegno subImpegno = instance.obtainMovimentoCast();
				anno = subImpegno.getAnnoMovimento();
				numero = subImpegno.getNumeroImpegnoPadre().intValue();
				numeroSub = subImpegno.getNumero().intValue();
			} else if(TipoCollegamento.SUBACCERTAMENTO.equals(tipoCollegamento)){
				azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA_ANNO_NUMERO_SUB);
				SubAccertamento subAccertamento = instance.obtainMovimentoCast();
				anno = subAccertamento.getAnnoMovimento();
				numero = subAccertamento.getNumeroAccertamentoPadre().intValue();
				numeroSub = subAccertamento.getNumero().intValue();
			} else if(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_SPESA.equals(tipoCollegamento)){
				ModificaMovimentoGestioneSpesa modificaMovimentoGestioneSpesa = instance.obtainMovimentoCast();
				if(modificaMovimentoGestioneSpesa != null && modificaMovimentoGestioneSpesa.getSubImpegno() != null) {
					azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA_ANNO_NUMERO_SUB_UID);
					SubImpegno subImpegno = modificaMovimentoGestioneSpesa.getSubImpegno();
					anno = subImpegno.getAnnoImpegnoPadre();
					numero = subImpegno.getNumeroImpegnoPadre().intValue();
					numeroSub = subImpegno.getNumero().intValue();
				} else if(modificaMovimentoGestioneSpesa != null && modificaMovimentoGestioneSpesa.getImpegno() != null) {
					azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA_ANNO_NUMERO_UID);
					Impegno impegno = modificaMovimentoGestioneSpesa.getImpegno();
					anno = impegno.getAnnoMovimento();
					numero = impegno.getNumero().intValue();
				}
			} else if(TipoCollegamento.MODIFICA_MOVIMENTO_GESTIONE_ENTRATA.equals(tipoCollegamento)){
				ModificaMovimentoGestioneEntrata modificaMovimentoGestioneSpesa = instance.obtainMovimentoCast();
				if(modificaMovimentoGestioneSpesa != null && modificaMovimentoGestioneSpesa.getSubAccertamento() != null) {
					azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA_ANNO_NUMERO_SUB_UID);
					SubAccertamento subAccertamento = modificaMovimentoGestioneSpesa.getSubAccertamento();
					anno = subAccertamento.getAnnoAccertamentoPadre();
					numero = subAccertamento.getNumeroAccertamentoPadre().intValue();
					numeroSub = subAccertamento.getNumero().intValue();
				} else if(modificaMovimentoGestioneSpesa != null && modificaMovimentoGestioneSpesa.getAccertamento() != null) {
					azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA_ANNO_NUMERO_UID);
					Accertamento accertamento = modificaMovimentoGestioneSpesa.getAccertamento();
					anno = accertamento.getAnnoMovimento();
					numero = accertamento.getNumero().intValue();
				}
			} else if(TipoCollegamento.RATEO.equals(tipoCollegamento) || TipoCollegamento.RISCONTO.equals(tipoCollegamento)){
				azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA_UID_PRIMA_NOTA);
			} else {
				azioniBuilder.append(AZIONI_CONSENTITE_CONSULTA_UID);
			}
		}
		if(aggiornaPdCConsentita){
			azioniBuilder.append(AZIONI_CONSENTITE_AGGIORNA_PDC);
		}
		
		azioniBuilder.append(AZIONI_CONSENTITE_END);

		Integer uidMovimento = instance.getUidMovimento();
		String codiceCollegamento = instance.getCodiceTipoCollegamento();
		if(TipoCollegamento.SUBDOCUMENTO_ENTRATA.equals(tipoCollegamento)){
			SubdocumentoEntrata subdoc = instance.obtainMovimentoCast();
			uidMovimento = subdoc.getDocumento().getUid();
			if(instance.isEventoNotaCredito()){
				codiceCollegamento = BilConstants.CODICE_NOTE_ACCREDITO.getConstant();
			} else {
				codiceCollegamento = TipoCollegamento.DOCUMENTO_ENTRATA.getCodice();
			}
		}
		if(TipoCollegamento.SUBDOCUMENTO_SPESA.equals(tipoCollegamento)){
			SubdocumentoSpesa subdoc = instance.obtainMovimentoCast();
			uidMovimento = subdoc.getDocumento().getUid();
			if(instance.isEventoNotaCredito()){
				codiceCollegamento = BilConstants.CODICE_NOTE_CREDITO.getConstant();
			} else {
				codiceCollegamento = TipoCollegamento.DOCUMENTO_SPESA.getCodice();
			}
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("%TYPE%", getCodiceAmbito());
		map.put("%UIDRICHIESTA%", instance.getUid() + "");
		map.put("%MOV%", codiceCollegamento);
		map.put("%NUM%", numero != null ? numero.toString() : "");
		map.put("%ANNO%", anno != null ? anno.toString() : "");
		map.put("%UID%", uidMovimento.toString());
		map.put("%NUMSUB%", numeroSub != null ? numeroSub.toString() : "");
		map.put("%UIDPRIMANOTA%", instance.getUidPrimaNota());

		String azioni = StringUtilities.replacePlaceholders(PATTERN, azioniBuilder.toString(), map, false);

		instance.setAzioni(azioni);
	}

	/**
	 * Controlla se la registrazione sia valida
	 *
	 * @param instance l'istanza la cui validit&agrave; &eacute; da controllare
	 * @return <code>true</code> se l'istanza &eacute; valida; <code>false</code> altrimenti
	 */
	private boolean isValidRegistrazione(ElementoRegistrazioneMovFin instance) {
		return instance.getUidMovimento() != 0 || instance.isMovimentoSubdocumentoWithDocumento();
	}

	/**
	 * Controlla se la registrazione sia relativa all'evento <code>IMP-PRG</code>.
	 *
	 * @param instance il wrapper da controllare
	 * @return <code>true</code> se l'istanza &eacute; riferentesi all'evento <code>IMP-PRG</code>; <code>false</code> altrimenti.
	 */
	private boolean isImpPrg(ElementoRegistrazioneMovFin instance) {
		return BilConstants.CODICE_EVENTO_IMP_PRG.getConstant().equals(instance.getCodiceEvento());
	}

	/**
	 * Controlla se la gestione della prima nota sia consentita.
	 *
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @param instance              l'istanza da controllare
	 * @return <code>true</code> se la gestione della prima nota per l'istanza &eacute; consentita; <code>false</code> altrimenti
	 */
	private boolean isGestionePrimaNotaConsentita(List<AzioneConsentita> listaAzioniConsentite, ElementoRegistrazioneMovFin instance) {
		StatoOperativoRegistrazioneMovFin stato = instance.getStatoOperativoRegistrazioneMovFin();
		return Boolean.TRUE.equals(AzioniConsentiteFactory.isConsentito(getAzioneConsentitaGestionePrimaNotaIntegrata(), listaAzioniConsentite))
				&& (StatoOperativoRegistrazioneMovFin.NOTIFICATO.equals(stato) || StatoOperativoRegistrazioneMovFin.ELABORATO.equals(stato));
	}

	/**
	 * Controlla se l'annullamento sia consentito.
	 *
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @return <code>true</code> se l'annullamento dell'istanza &eacute; consentita; <code>false</code> altrimenti
	 */
//	private boolean isAnnullaConsentita(List<AzioneConsentita> listaAzioniConsentite) {
//		return Boolean.TRUE.equals(AzioniConsentiteFactory.isConsentito(getAzioneConsentitaGestioneRegistrazioneMovFin(), listaAzioniConsentite))
//				&& !faseBilancioInValues(faseBilancio, FaseBilancio.PLURIENNALE, FaseBilancio.PREVISIONE, FaseBilancio.CHIUSO);
//	}

	/**
	 * Controlla se la consultazione sia consentita.
	 *
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @return <code>true</code> se la consultazione dell'istanza &eacute; consentita; <code>false</code> altrimenti
	 */
	private boolean isConsultaConsentita(List<AzioneConsentita> listaAzioniConsentite) {
		return Boolean.TRUE.equals(AzioniConsentiteFactory.isConsentito(getAzioneConsentitaRicercaMovFin(), listaAzioniConsentite));
	}
	
	/**
	 * Controlla se l'aggiornamento del piano dei conti sia consentito.
	 *
	 * @param listaAzioniConsentite la lista delle azioni consentite
	 * @return <code>true</code> se l'aggiornamento dell'istanza &eacute; consentita; <code>false</code> altrimenti
	 */
	protected boolean isAggiornaPdCConsentita(List<AzioneConsentita> listaAzioniConsentite) {
		return false;
	}
	
	/**
	 * @return il codice dell'ambito di riferimento ('FIN/GSA')
	 */
	protected abstract String getCodiceAmbito();
	/**
	 * @return l'azione consentita corrispondente alla gestione della prima nota integrata
	 */
	protected abstract AzioniConsentite getAzioneConsentitaGestionePrimaNotaIntegrata();
	/**
	 * @return l'azione consentita corrispondente alla gestione della registrazione movfin
	 */
	protected abstract AzioniConsentite getAzioneConsentitaGestioneRegistrazioneMovFin();
	/**
	 * @return l'azione consentita corrispondente alla ricerca della registrazione movfin
	 */
	protected abstract AzioniConsentite getAzioneConsentitaRicercaMovFin();
}
