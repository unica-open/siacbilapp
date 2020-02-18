/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.model.cassaeconomale.richieste.anticipopertrasfertadipendenti;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.collections.CollectionUtil;
import it.csi.siac.siacbilser.model.CapitoloUscitaGestione;
import it.csi.siac.siaccecser.frontend.webservice.msg.RicercaTipoGiustificativo;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.TipologiaGiustificativo;


/**
 * Classe base di model per l'inserimento e l'aggiornamento del rendiconto per l'anticipo per trasferta dipendente.
 * 
 * @author Domenico Lisi
 * @version 1.0.0 - 16/02/2015
 *
 */
public abstract class BaseInserisciAggiornaRendicontoAnticipoPerTrasfertaDipendentiCassaEconomaleModel extends BaseInserisciAggiornaAnticipoPerTrasfertaDipendentiCassaEconomaleModel {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7483459526219489546L;
	
	private RendicontoRichiesta rendicontoRichiesta;
	private RendicontoRichiesta rendicontoRichiestaCopia;
	
	private Boolean restituzioneTotale = Boolean.FALSE;
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
	public void setRendicontoRichiestaCopia(
			RendicontoRichiesta rendicontoRichiestaCopia) {
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

	@Override
	public boolean getIsAggiornamento() {
		return false;
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
//		if((getListaGiustificativo() == null || getListaGiustificativo().isEmpty()) && !Boolean.TRUE.equals(getRestituzioneTotale())) {
//			return BigDecimal.ZERO;
//		}
		BigDecimal importo = Boolean.TRUE.equals(getRestituzioneTotale())
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
		return "Dati della richiesta di anticipo per trasferta dipendenti";
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
		CollectionUtil.addIfNotNullNorEmpty(chunks, getRendicontoRichiesta().getRichiestaEconomale().getCognome());
		return StringUtils.join(chunks, " - ");
	}
	
	/* **** Requests **** */
	
	@Override
	public RicercaTipoGiustificativo creaRequestRicercaTipoGiustificativo() {
		return creaRequestRicercaTipoGiustificativo(TipologiaGiustificativo.RIMBORSO);
	}
	
}
