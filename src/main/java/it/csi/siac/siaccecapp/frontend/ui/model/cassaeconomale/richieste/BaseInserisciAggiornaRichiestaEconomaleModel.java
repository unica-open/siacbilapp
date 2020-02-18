/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.collections.CollectionUtil;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaModalitaPagamentoCassa;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativo;
import it.csi.siac.siaccecser.model.ModalitaPagamentoCassa;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoDiCassa;
import it.csi.siac.siaccecser.model.TipologiaGiustificativo;
import it.csi.siac.siacfinser.model.soggetto.Soggetto;
import it.csi.siac.siacfinser.model.soggetto.modpag.ModalitaPagamentoSoggetto;

/**
 * Classe base di model per l'inserimento e l'aggiornamento della richiesta economale.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 02/02/2015
 *
 */
public abstract class BaseInserisciAggiornaRichiestaEconomaleModel extends BaseRiepilogoRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -8055372501604349107L;
	
	// Step 1
	private RichiestaEconomale richiestaEconomaleCopia;
	private Integer uidRichiesta;
	private Integer uidRendiconto;
	
	private List<ModalitaPagamentoCassa> listaModalitaPagamentoCassa = new ArrayList<ModalitaPagamentoCassa>();
	
	private ModalitaPagamentoSoggetto modalitaPagamentoSoggetto;
	private List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggettoDifferenteIban = new ArrayList<ModalitaPagamentoSoggetto>();
	
	/**
	 * @return the richiestaEconomaleCopia
	 */
	public RichiestaEconomale getRichiestaEconomaleCopia() {
		return richiestaEconomaleCopia;
	}

	/**
	 * @param richiestaEconomaleCopia the richiestaEconomaleCopia to set
	 */
	public void setRichiestaEconomaleCopia(RichiestaEconomale richiestaEconomaleCopia) {
		this.richiestaEconomaleCopia = richiestaEconomaleCopia;
	}

	/**
	 * @return the listaModalitaPagamentoCassa
	 */
	public List<ModalitaPagamentoCassa> getListaModalitaPagamentoCassa() {
		return listaModalitaPagamentoCassa;
	}

	/**
	 * @param listaModalitaPagamentoCassa the listaModalitaPagamentoCassa to set
	 */
	public void setListaModalitaPagamentoCassa(List<ModalitaPagamentoCassa> listaModalitaPagamentoCassa) {
		this.listaModalitaPagamentoCassa = listaModalitaPagamentoCassa != null ? listaModalitaPagamentoCassa : new ArrayList<ModalitaPagamentoCassa>();
	}
	
	/**
	 * @return the modalitaPagamentoSoggetto
	 */
	public ModalitaPagamentoSoggetto getModalitaPagamentoSoggetto() {
		return modalitaPagamentoSoggetto;
	}

	/**
	 * @param modalitaPagamentoSoggetto the modalitaPagamentoSoggetto to set
	 */
	public void setModalitaPagamentoSoggetto(ModalitaPagamentoSoggetto modalitaPagamentoSoggetto) {
		this.modalitaPagamentoSoggetto = modalitaPagamentoSoggetto;
	}

	/**
	 * @return the listaModalitaPagamentoSoggettoDifferenteIban
	 */
	public List<ModalitaPagamentoSoggetto> getListaModalitaPagamentoSoggettoDifferenteIban() {
		return listaModalitaPagamentoSoggettoDifferenteIban;
	}

	/**
	 * @param listaModalitaPagamentoSoggettoDifferenteIban the listaModalitaPagamentoSoggettoDifferenteIban to set
	 */
	public void setListaModalitaPagamentoSoggettoDifferenteIban(List<ModalitaPagamentoSoggetto> listaModalitaPagamentoSoggettoDifferenteIban) {
		this.listaModalitaPagamentoSoggettoDifferenteIban = listaModalitaPagamentoSoggettoDifferenteIban != null ? listaModalitaPagamentoSoggettoDifferenteIban : new ArrayList<ModalitaPagamentoSoggetto>();
	}

	/**
	 * @return the maySearchHR
	 */
	public Boolean getMaySearchHR() {
		return Boolean.TRUE;
	}
	
	
	/**
	 * @return the isAggiornamento
	 */
	public abstract boolean getIsAggiornamento();
	
	/**
	 * @return the baseUrl
	 */
	public abstract String getBaseUrl();
	
	/**
	 * @return the urlAnnullaStep1
	 */
	public String getUrlAnnullaStep1(){
		return getBaseUrl() + "_annullaStep1.do";
	}
	
	/**
	 * @return the urlAnnullaStep2
	 */
	public String getUrlAnnullaStep2(){
		return getBaseUrl() + "_annullaStep2.do";
	}
	
	/**
	 * @return the urlStep1
	 */
	public String getUrlStep1() {
		return getBaseUrl() + "_completeStep1";
	}
	
	/**
	 * @return the urlBackToStep1
	 */
	public String getUrlBackToStep1() {
		return getBaseUrl() + "_backToStep1";
	}
	
	/**
	 * @return the urlStep2
	 */
	public String getUrlStep2() {
		return getBaseUrl() + "_completeStep2";
	}
	
	/**
	 * @return the urlStep3
	 */
	public String getUrlStep3() {
		return getBaseUrl() + "_completeStep3";
	}
	
	/**
	 * @return the urlCopiaRichiestaEconomale
	 */
	public String getUrlCopiaRichiestaEconomale() {
		return getBaseUrl() + "_copiaRichiesta.do";
	}
	
	/**
	 * @return the urlVisualizzaImporti
	 */
	public String getUrlVisualizzaImporti() {
		return getBaseUrl() + "_visualizzaImporti.do";
	}
	
	/**
	 * @return the urlRestituzioneTotale
	 */
	public String getUrlRestituzioneTotale() {
		return getBaseUrl() + "_restituzioneTotale.do";
	}
	
	/**
	 * @return the urlRestituzioneAltroUfficio
	 */
	public String getUrlRestituzioneAltroUfficio() {
		return getBaseUrl() + "_restituzioneAltroUfficio.do";
	}
	
	/**
	 * @return the uidRichiesta
	 */
	public Integer getUidRichiesta() {
		return uidRichiesta;
	}

	/**
	 * @param uidRichiesta the uidRichiesta to set
	 */
	public void setUidRichiesta(Integer uidRichiesta) {
		this.uidRichiesta = uidRichiesta;
	}
	
	/**
	 * @return the uidRendiconto
	 */
	public Integer getUidRendiconto() {
		return uidRendiconto;
	}

	/**
	 * @param uidRendiconto the uidRendiconto to set
	 */
	public void setUidRendiconto(Integer uidRendiconto) {
		this.uidRendiconto = uidRendiconto;
	}

	/**
	 * @return the richiestaEconomalePrenotata
	 */
	public boolean isRichiestaEconomalePrenotata() {
		return getRichiestaEconomale() != null && StatoOperativoRichiestaEconomale.PRENOTATA.equals(getRichiestaEconomale().getStatoOperativoRichiestaEconomale());
	}
	
	/**
	 * @return the denominazioneMatricola
	 */
	public String getDenominazioneMatricola() {
		StringBuilder sb = new StringBuilder();
		if(getRichiestaEconomale() != null && getRichiestaEconomale().getSoggetto() != null && StringUtils.isNotBlank(getRichiestaEconomale().getSoggetto().getMatricola())
				&& StringUtils.isNotBlank(getRichiestaEconomale().getSoggetto().getCognome()) && StringUtils.isNotBlank(getRichiestaEconomale().getSoggetto().getNome())) {
			sb.append(": ")
				.append(getRichiestaEconomale().getSoggetto().getCognome())
				.append(" ")
				.append(getRichiestaEconomale().getSoggetto().getNome());
		}
		return sb.toString();
	}
	
	/**
	 * @return the denominazioneMatricola
	 */
	public String getDenominazioneSoggetto() {
		StringBuilder sb = new StringBuilder();
		if(getRichiestaEconomale() != null && getRichiestaEconomale().getSoggetto() != null && StringUtils.isNotBlank(getRichiestaEconomale().getSoggetto().getCodiceSoggetto())
				&& StringUtils.isNotBlank(getRichiestaEconomale().getSoggetto().getCognome()) && StringUtils.isNotBlank(getRichiestaEconomale().getSoggetto().getNome())) {
			sb.append(": ")
				.append(getRichiestaEconomale().getSoggetto().getCognome())
				.append(" ")
				.append(getRichiestaEconomale().getSoggetto().getNome());
		}
		return sb.toString();
	}
	/**
	 * @return the cassaContanti
	 */
	public boolean isCassaContanti() {
		return getCassaEconomale() != null && TipoDiCassa.CONTANTI.equals(getCassaEconomale().getTipoDiCassa());
	}
	
	/**
	 * @return the copied
	 */
	public boolean isCopied() {
		return getRichiestaEconomaleCopia() != null && getRichiestaEconomaleCopia().getUid() != 0;
	}
	
	/**
	 * @return the denominazioneRichiestaPerRendiconto
	 */
	public String getDenominazioneRichiestaPerRendiconto() {
		return "";
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaTipoGiustificativo}.
	 * 
	 * @param tipologiaGiustificativo la tipologia di giustificativo da ricercare
	 * 
	 * @return la request creata
	 */
	protected RicercaTipoGiustificativo creaRequestRicercaTipoGiustificativo(TipologiaGiustificativo tipologiaGiustificativo) {
		RicercaTipoGiustificativo request = creaRequest(RicercaTipoGiustificativo.class);
		
		request.setTipologiaGiustificativo(tipologiaGiustificativo);
		request.setCassaEconomale(getCassaEconomale());
		
		return request;
	}

	/**
	 * Crea una request per il servizio di {@link RicercaModalitaPagamentoCassa}.
	 * 
	 * @return la request creata
	 */
	public RicercaModalitaPagamentoCassa creaRequestRicercaModalitaPagamentoCassa() {
		return creaRequest(RicercaModalitaPagamentoCassa.class);
	}
	
	// LOTTO M
	
	/**
	 * Controlla se i campi dell'impegno siano disabilitati.
	 * <br/>
	 * I campi sono sempre disabilitati tranne che in inserimento di pagamento, rimborso spese e gli anticipi.
	 * 
	 * @return the disabledImpegnoFields
	 */
	public boolean isDisabledImpegnoFields() {
		// May be overridden
		return true;
	}
	
	// Lotto P
	
	/**
	 * @return the datiMatricola
	 */
	@Override
	public String getDatiMatricola() {
		if(getRichiestaEconomale() == null) {
			return "";
		}
		if(getRichiestaEconomale().getSoggetto() != null) {
			List<String> chunks = new ArrayList<String>();
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getSoggetto().getMatricola());
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getSoggetto().getDenominazione());
			return StringUtils.join(chunks, " - ");
		}
		List<String> chunks = new ArrayList<String>();
		CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getMatricola());
		CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getNome());
		if (!isGestioneHR()) {
			//hr mette tutti i dati in un solo campo
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRichiestaEconomale().getCognome());
		}
		return StringUtils.join(chunks, " - ");
	}
	
	/**
	 * Popolamento dei dati da HR;
	 */
	protected void popolaDatiHR() {
		if(!isGestioneHR() || getRichiestaEconomale() == null || getRichiestaEconomale().getSoggetto() == null) {
			return;
		}
		Soggetto soggetto = getRichiestaEconomale().getSoggetto();
		getRichiestaEconomale().setMatricola(soggetto.getMatricola());
		getRichiestaEconomale().setCognome(soggetto.getDenominazione());
		getRichiestaEconomale().setSoggetto(null);
		//JIRA 3382 COMMENTO #1
		getRichiestaEconomale().setStrutturaDiAppartenenza(getRichiestaEconomale().getDatiTrasfertaMissione() !=null ? getRichiestaEconomale().getDatiTrasfertaMissione().getUnitaOrganizzativa():""  );
		// TODO: Copio altri dati?		
	}
}
