/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.util.wrapper.cassaeconomale.stampe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siaccecser.model.Movimento;
import it.csi.siac.siaccecser.model.RendicontoRichiesta;
import it.csi.siac.siaccecser.model.RichiestaEconomale;
import it.csi.siac.siaccecser.model.StatoOperativoRichiestaEconomale;
import it.csi.siac.siaccecser.model.TipoRichiestaEconomale;
import it.csi.siac.siacfinser.model.Impegno;
import it.csi.siac.siacfinser.model.SubImpegno;

/**
 * Wrapper per il movimento, per la stampa del rendiconto
 * @author Marchino Alessandro
 *
 */
public abstract class ElementoMovimentoStampa implements ModelWrapper, Serializable {

	private static final String CODICE_ANTICIPO_TRASFERTA_DIPENDENTI = "ANTICIPO_TRASFERTA_DIPENDENTI";

	/** Il codice dell'anticipo spese per missione */
	private static final String CODICE_ANTICIPO_SPESE_MISSIONE = "ANTICIPO_SPESE_MISSIONE";

	/** Per la serializzazione */
	private static final long serialVersionUID = -5205168413052557052L;
	/** Il movimento */
	protected final Movimento movimento;
	
	/**
	 * Costruttore di wrap
	 * @param movimento il movimento da wrappare
	 */
	protected ElementoMovimentoStampa(Movimento movimento) {
		// Il movimento non e' null
		this.movimento = movimento;
	}
	
	/**
	 * @return the capitolo
	 */
	public String getCapitolo() {
		Impegno impegno = extractImpegno();
		if(impegno == null || impegno.getCapitoloUscitaGestione() == null) {
			return "";
		}
		return impegno.getCapitoloUscitaGestione().getNumeroCapitolo() + "/" + impegno.getCapitoloUscitaGestione().getNumeroArticolo();
	}
	
	/**
	 * @return the impegno
	 */
	public String getImpegno() {
		Impegno impegno = extractImpegno();
		SubImpegno subImpegno = extractSubImpegno();
		if(impegno == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder()
			.append(impegno.getAnnoMovimento())
			.append("/")
			.append(impegno.getNumero().toPlainString());
		if(subImpegno != null) {
			sb.append("/")
				.append(subImpegno.getNumero().toPlainString());
		}
		return sb.toString();
	}
	
	/**
	 * @return the numeroMovimento
	 */
	public Integer getNumeroMovimento() {
		return movimento.getNumeroMovimento();
	}
	
	/**
	 * @return the tipo
	 */
	public String getTipo() {
		TipoRichiestaEconomale tre = extractTipo();
		return tre == null
				? ""
				: tre.getCodice() + " - " + tre.getDescrizione();
	}
	
	/**
	 * @return the ataRegistrazione
	 */
	public String getDataRegistrazione() {
		return FormatUtils.formatDate(movimento.getDataMovimento());
	}
	
	/**
	 * @return the beneficiario
	 */
	public String getBeneficiario() {
		RichiestaEconomale richiestaEconomale = extractRichiesta();
		if(richiestaEconomale == null) {
			return "";
		}
		List<String> chunks = new ArrayList<String>();
		if(StringUtils.isNotBlank(richiestaEconomale.getNome())) {
			chunks.add(richiestaEconomale.getNome());
		}
		if(StringUtils.isNotBlank(richiestaEconomale.getCognome())
				&& richiestaEconomale.getCognome() != null
				&& !richiestaEconomale.getCognome().equals(richiestaEconomale.getNome())) {
			chunks.add(richiestaEconomale.getCognome());
		}
		
		return StringUtils.join(chunks, " ");
	}
	
	/**
	 * @return the oggetto
	 */
	public String getOggetto() {
		RichiestaEconomale richiestaEconomale = extractRichiesta();
		if(!checkAnticipoSpeseMissione(richiestaEconomale)) {
			return richiestaEconomale != null ? richiestaEconomale.getDescrizioneDellaRichiesta() : "";
		}
		return richiestaEconomale != null && richiestaEconomale.getDatiTrasfertaMissione() != null
			? richiestaEconomale.getDatiTrasfertaMissione().getMotivo()
			: "";
	}
	
	/**
	 * @return the importo
	 */
	public BigDecimal getImporto() {
		RichiestaEconomale richiestaEconomale = extractRichiesta();
		RendicontoRichiesta rendicontoRichiesta = movimento.getRendicontoRichiesta();
		
		// SIAC-5049 - l'importo del rendiconto e' restituito - integrato
		
		return rendicontoRichiesta != null
			? rendicontoRichiesta.getImportoMovimento().negate()
			: richiestaEconomale != null
				? richiestaEconomale.getImporto()
				: null;
	}
	
	@Override
	public int getUid() {
		return movimento.getUid();
	}
	
	/**
	 * Estrazione dell'impegno
	 * @return l'impegno
	 */
	protected abstract Impegno extractImpegno();
	/**
	 * Estrazione del subimpegno
	 * @return il subimpegno
	 */
	protected abstract SubImpegno extractSubImpegno();
	/**
	 * Estrazione del tipo di richiesta economale
	 * @return il subimpegno
	 */
	protected abstract TipoRichiestaEconomale extractTipo();
	/**
	 * Estrazione della richiesta economale
	 * @return la richiesta
	 */
	protected abstract RichiestaEconomale extractRichiesta();
	
	/**
	 * Controlla se la request sia un anticipo spesa per missione
	 * @param richiestaEconomale la richista economale
	 * @return controlla se sia un anticipo spese per missione
	 */
	protected boolean checkAnticipoSpeseMissione(RichiestaEconomale richiestaEconomale) {
		TipoRichiestaEconomale tre = extractTipo();
		return tre != null
			&& (
				CODICE_ANTICIPO_SPESE_MISSIONE.equals(tre.getCodice()) || CODICE_ANTICIPO_TRASFERTA_DIPENDENTI.equals(tre.getCodice())
			);
	}
	
	/**
	 * Gets the numero movimento con stato.
	 *
	 * @return the numero movimento con stato
	 */
	public String getNumeroMovimentoConStato() {
		//SIAC-6450
		RichiestaEconomale richiestaEconomale = extractRichiesta();
		
		if(richiestaEconomale == null || !StatoOperativoRichiestaEconomale.ANNULLATA.equals(richiestaEconomale.getStatoOperativoRichiestaEconomale())) {
			return getNumeroMovimento().toString();
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<a data-original-title=\" Stato \" data-trigger=\"hover\" rel=\"popover\" data-content=\"  ")
		.append(richiestaEconomale.getStatoOperativoRichiestaEconomale().getDescrizione())
		.append("\"");
		
		sb.append(" data-html=\"true\">")
			.append(getNumeroMovimento().toString())
			.append("&nbsp;")
			.append("*")
			.append("</a>");
		return sb.toString();
	}
	
}
