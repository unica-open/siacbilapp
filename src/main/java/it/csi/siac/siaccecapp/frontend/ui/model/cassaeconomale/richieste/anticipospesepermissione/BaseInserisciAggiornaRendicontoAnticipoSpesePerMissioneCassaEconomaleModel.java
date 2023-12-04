/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospesepermissione;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siaccommon.util.collections.CollectionUtil;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativo;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipologiaGiustificativo;
import it.csi.siac.siacfinser.frontend.webservice.msg.RicercaSoggettoPerChiave;
import it.csi.siac.siacfinser.model.ric.ParametroRicercaSoggettoK;
import it.csi.siac.siacfinser.model.ric.SorgenteDatiSoggetto;


/**
 * Classe base di model per l'inserimento e l'aggiornamento del rendiconto per l'anticipo spese per missione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/02/2015
 *
 */
public abstract class BaseInserisciAggiornaRendicontoAnticipoSpesePerMissioneCassaEconomaleModel extends BaseInserisciAggiornaAnticipoSpesePerMissioneCassaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7483459526219489546L;
	
	private RendicontoRichiesta rendicontoRichiesta;
	private RendicontoRichiesta rendicontoRichiestaCopia;
	
	private Boolean restituzioneTotale = Boolean.FALSE;
	private Boolean restituzioneAltroUfficio = Boolean.FALSE;
	private Integer uidFattura;
	
	/**
	 * @return the rendicontoRichiesta
	 */
	public RendicontoRichiesta getRendicontoRichiesta() {
		return rendicontoRichiesta;
	}

	/**
	 * @param rendicontoRichiesta the rendicontoRichiesta to set
	 */
	public void setRendicontoRichiesta(RendicontoRichiesta rendicontoRichiesta) {
		this.rendicontoRichiesta = rendicontoRichiesta;
	}

	/**
	 * @return the rendicontoRichiestaCopia
	 */
	public RendicontoRichiesta getRendicontoRichiestaCopia() {
	
		return rendicontoRichiestaCopia;
	}

	/**
	 * @param rendicontoRichiestaCopia the rendicontoRichiestaCopia to set
	 */
	public void setRendicontoRichiestaCopia(RendicontoRichiesta rendicontoRichiestaCopia) {
		this.rendicontoRichiestaCopia = rendicontoRichiestaCopia;
	}

	/**
	 * @return the restituzioneTotale
	 */
	public Boolean getRestituzioneTotale() {
		return restituzioneTotale;
	}

	/**
	 * @param restituzioneTotale the restituzioneTotale to set
	 */
	public void setRestituzioneTotale(Boolean restituzioneTotale) {
		this.restituzioneTotale = restituzioneTotale != null ? restituzioneTotale : Boolean.FALSE;
	}

	/**
	 * @return the restituzioneAltroUfficio
	 */
	public Boolean getRestituzioneAltroUfficio() {
		return restituzioneAltroUfficio;
	}

	/**
	 * @param restituzioneAltroUfficio the restituzioneAltroUfficio to set
	 */
	public void setRestituzioneAltroUfficio(Boolean restituzioneAltroUfficio) {
		this.restituzioneAltroUfficio =  restituzioneAltroUfficio != null ? restituzioneAltroUfficio : Boolean.FALSE;
		setAltroUfficio(this.restituzioneAltroUfficio.booleanValue());
	}

	/**
	 * @return the uidFattura
	 */
	public Integer getUidFattura() {
		return uidFattura;
	}

	/**
	 * @param uidFattura the uidFattura to set
	 */
	public void setUidFattura(Integer uidFattura) {
		this.uidFattura = uidFattura;
	}

	/**
	 * @return the datiEconomoCompilati
	 */
	public boolean isDatiEconomoCompilati() {
		return getRendicontoRichiesta() != null && getRendicontoRichiesta().getMovimento() != null && getRendicontoRichiesta().getMovimento().getDataMovimento() != null
				&& getRendicontoRichiesta().getMovimento().getModalitaPagamentoCassa() != null && getRendicontoRichiesta().getMovimento().getModalitaPagamentoCassa().getUid() != 0;
	}
	
	@Override
	protected RichiestaEconomale ottieniRichiestaEconomalePerRiepilogo() {
		return getRendicontoRichiesta().getRichiestaEconomale();
	}
	
	/**
	 * @return the importoRichiestaAnticipato
	 */
	public BigDecimal getImportoRichiestaAnticipato() {
		return getRendicontoRichiesta() != null && getRendicontoRichiesta().getRichiestaEconomale() != null && getRendicontoRichiesta().getRichiestaEconomale().getImporto() != null
				? getRendicontoRichiesta().getRichiestaEconomale().getImporto()
				: BigDecimal.ZERO;
	}
	
	/**
	 * @return the importoDaRestituire
	 */
	public BigDecimal getImportoDaRestituire() {
//		if(/*(getListaGiustificativo() == null || getListaGiustificativo().isEmpty()) &&*/ !Boolean.TRUE.equals(getRestituzioneAltroUfficio()) && !Boolean.TRUE.equals(getRestituzioneTotale())) {
//			return BigDecimal.ZERO;
//		}
		
		BigDecimal importo = Boolean.TRUE.equals(getRestituzioneTotale()) || Boolean.TRUE.equals(getRestituzioneAltroUfficio())
				? getImportoRichiestaAnticipato()
				: getImportoRichiestaAnticipato().subtract(getTotaleImportiGiustificativi());
		return importo.max(BigDecimal.ZERO);
	}
	
	/**
	 * @return the importoDaIntegrare
	 */
	public BigDecimal getImportoDaIntegrare() {
		return getTotaleImportiGiustificativi().subtract(getImportoRichiestaAnticipato()).max(BigDecimal.ZERO);
	}
	
	/**
	 * @return the capitoloMovimentoRichiesta
	 */
	public CapitoloUscitaGestione getCapitoloMovimentoRichiesta() {
		return getRendicontoRichiesta() != null && getRendicontoRichiesta().getRichiestaEconomale() != null && getRendicontoRichiesta().getRichiestaEconomale().getImpegno() != null
				? getRendicontoRichiesta().getRichiestaEconomale().getImpegno().getCapitoloUscitaGestione()
				: null;
	}
	
	/**
	 * @return the descrizioneCapitoloMovimentoRichiesta
	 */
	public String getDescrizioneCapitoloMovimentoRichiesta() {
		CapitoloUscitaGestione cug = getCapitoloMovimentoRichiesta();
		return computeDescrizioneCapitolo(cug);
	}
	
	@Override
	public boolean isCopied() {
		return getRendicontoRichiestaCopia() != null && getRendicontoRichiestaCopia().getUid() != 0;
	}
	
	@Override
	public String getDenominazioneRichiestaPerRendiconto() {
		return "Dati della richiesta di anticipo per missione";
	}
	
	@Override
	public String getStringaRiepilogoRichiestaEconomale() {
		StringBuilder sb = new StringBuilder();
		
		RichiestaEconomale re = ottieniRichiestaEconomalePerRiepilogo();
		
		if(re != null && re.getUid() != 0) {
			// Richiesta numero
			sb.append(computaStringaNumeroRichiesta(re))
				// Richiesta del
				.append(computaStringaDataRichiesta(re))
				// Stato richiesta
				.append(computaStringaStatoRichiesta(re))
				// Sospeso
				.append(computaStringaSospeso(re))
				// Movimento
				.append(computaStringaMovimento(getRendicontoRichiesta()));
		}
		
		return sb.toString();
	}
	
	@Override
	protected String computaStringaSospeso(RichiestaEconomale richiestaEconomale) {
		if(richiestaEconomale == null || richiestaEconomale.getSospeso() == null) {
			return "";
		}
		return " - Sospeso N. " + richiestaEconomale.getSospeso().getNumeroSospeso();
	}
	
	
	/**
	 * Ottiene il movimento da considerare.
	 * 
	 * @return il movimento
	 */
	@Override
	protected Movimento ottieniMovimento() {
		// To override in subclasses for Rendiconto
		return getRendicontoRichiesta().getMovimento();
	}
	
	@Override
	public String getDatiMatricola() {
		if(getRendicontoRichiesta() == null || getRendicontoRichiesta().getRichiestaEconomale() == null) {
			return "";
		}
		if(getRendicontoRichiesta().getRichiestaEconomale().getSoggetto() != null) {
			List<String> chunks = new ArrayList<String>();
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRendicontoRichiesta().getRichiestaEconomale().getSoggetto().getMatricola());
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRendicontoRichiesta().getRichiestaEconomale().getSoggetto().getDenominazione());
			return StringUtils.join(chunks, " - ");
		}
		List<String> chunks = new ArrayList<String>();
		CollectionUtil.addIfNotNullNorEmpty(chunks, getRendicontoRichiesta().getRichiestaEconomale().getMatricola());
		CollectionUtil.addIfNotNullNorEmpty(chunks, getRendicontoRichiesta().getRichiestaEconomale().getNome());
		if (!isGestioneHR()) {
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRendicontoRichiesta().getRichiestaEconomale().getCognome());
		}
		return StringUtils.join(chunks, " - ");
	}
	/* **** Requests **** */
	
	@Override
	public RicercaTipoGiustificativo creaRequestRicercaTipoGiustificativo() {
		return creaRequestRicercaTipoGiustificativo(TipologiaGiustificativo.RIMBORSO);
	}
	
	/**
	 * Crea una request per il servizio di {@link RicercaSoggettoPerChiave}, a partire dalla Richiesta economale (per integrazione HR).
	 * 
	 * @param richiestaEconomale la richiesta economale per cui effettuare la richiesta
	 * @return la request creata
	 */
	@Override
	public RicercaSoggettoPerChiave creaRequestRicercaSoggettoPerChiave(RichiestaEconomale richiestaEconomale) {
		if(richiestaEconomale.getSoggetto() != null && richiestaEconomale.getSoggetto().getCodiceSoggetto() != null) {
			return creaRequestRicercaSoggettoPerChiave(richiestaEconomale.getSoggetto());
		}
		
		RicercaSoggettoPerChiave request = creaRequest(RicercaSoggettoPerChiave.class);
		
		request.setEnte(getEnte());
		
		ParametroRicercaSoggettoK parametroSoggettoK = new ParametroRicercaSoggettoK();
		parametroSoggettoK.setMatricola(richiestaEconomale.getMatricola());
		request.setParametroSoggettoK(parametroSoggettoK);
		
		// Ambito CEC
		request.setCodificaAmbito(getCodiceAmbito());
		// Lotto P
		request.setSorgenteDatiSoggetto(Boolean.TRUE.equals(getMaySearchHR()) && isGestioneHR() ? SorgenteDatiSoggetto.HR : SorgenteDatiSoggetto.SIAC);
				
		return request;
	}
}
