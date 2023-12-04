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
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseConsultaRichiestaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiesta;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;


/**
 * Classe di model per la consultazione del rendiconto dell'anticipo spese per missione.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 16/02/2015
 */
public class ConsultaRendicontoAnticipoSpesePerMissioneCassaEconomaleModel extends BaseConsultaRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4012088632376630999L;
	
	private RendicontoRichiesta rendicontoRichiesta;

	/** Costruttore vuoto di default */
	public ConsultaRendicontoAnticipoSpesePerMissioneCassaEconomaleModel() {
		setTitolo("Anticipo spese per missione - Consultazione Rendiconto");
	}

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
		return getRendicontoRichiesta().getImportoRestituito().max(BigDecimal.ZERO);
	}
	
	/**
	 * @return the importoDaIntegrare
	 */
	public BigDecimal getImportoDaIntegrare() {
		return getRendicontoRichiesta().getImportoIntegrato().max(BigDecimal.ZERO);
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
	
	/**
	 * @return the totaleImportiGiustificativi
	 */
	public BigDecimal getTotaleImportiGiustificativi() {
		BigDecimal totale = BigDecimal.ZERO;
		for(Giustificativo g : getRendicontoRichiesta().getGiustificativi()) {
			if(g.getImportoGiustificativo() != null) {
				totale = totale.add(g.getImportoGiustificativo());
			}
		}
		return totale;
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
	 * Ottiene la richiesta per la composizione del riepilogo.
	 * 
	 * @return la richiesta di cui ottenere il riepilogo per il rendiconto
	 */
	@Override
	protected RichiestaEconomale ottieniRichiestaEconomalePerRiepilogo() {
		return getRendicontoRichiesta().getRichiestaEconomale();
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
			//hr mette tutti i dati in un solo campo
			CollectionUtil.addIfNotNullNorEmpty(chunks, getRendicontoRichiesta().getRichiestaEconomale().getCognome());
		}
		return StringUtils.join(chunks, " - ");
	}
	
	/* **** Requests **** */
	
	/**
	 * Crea una request per il servizio di {@link RicercaDettaglioRendicontoRichiesta}.
	 * 
	 * @return la request creata
	 */
	public RicercaDettaglioRendicontoRichiesta creaRequestRicercaDettaglioRendicontoRichiesta() {
		RicercaDettaglioRendicontoRichiesta request = creaRequest(RicercaDettaglioRendicontoRichiesta.class);
		
		request.setRendicontoRichiesta(getRendicontoRichiesta());
		
		return request;
	}

}
