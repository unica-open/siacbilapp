/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipospese;

import java.math.BigDecimal;

import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.BaseConsultaRichiestaEconomaleModel;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaDettaglioRendicontoRichiesta;
import it.csi.siac.siaccecser.model.Giustificativo;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;


/**
 * Classe di model per la consultazione del rendiconto dell'anticipo spese.
 * 
 * @author Marchino Alessandro - Valentina Triolo
 * @version 1.0.0 - 18/02/2015
 */
public class ConsultaRendicontoAnticipoSpeseCassaEconomaleModel extends BaseConsultaRichiestaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 4012088632376630999L;
	
	private RendicontoRichiesta rendicontoRichiesta;

	/** Costruttore vuoto di default */
	public ConsultaRendicontoAnticipoSpeseCassaEconomaleModel() {
		setTitolo("Anticipo spese - Consultazione Rendiconto");
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
	
	@Override
	protected RichiestaEconomale ottieniRichiestaEconomalePerRiepilogo() {
		return getRendicontoRichiesta().getRichiestaEconomale();
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
