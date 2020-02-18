/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.util.wrappers.cespite;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.TipoBeneCespite;

/**
 * The Class ElementoCespite.
 * @author elisa
 * @version 1.0.0 - 12-06-2018
 */
public class ElementoCespite implements Serializable, ModelWrapper {

	/** Per la serializzazione*/
	
	private static final long serialVersionUID = -8165292320492509621L;
	
	private static final String SI = "S&igrave;";
	private static final String NO = "No";
	private static final String AZIONE_SCOLLEGA ="<a href='#msgScollega' title='scollega' class='scollegaCespite' role='button' data-toggle='modal'> <i class='icon-trash icon-2x'><span class='nascosto'>scollega</span></i></a>";
	
	
	//FIELDS
	private final Cespite cespite;
	private String azioni;
	
	
	
	/**
	 * Instantiates a new elemento categoria cespiti.
	 *
	 * @param cespite the categoria cespiti
	 */
	public ElementoCespite(Cespite cespite) {
		this.cespite = cespite;
	}

	/**
	 * @return the azioni
	 */
	public String getAzioni() {
		return azioni;
	}

	/**
	 * @param azioni the azioni to set
	 */
	public void setAzioni(String azioni) {
		this.azioni = azioni;
	}

	@Override
	public int getUid() {
		return cespite.getUid();
	}
	
	/**
	 * Gets the codice.
	 *
	 * @return the codice
	 */
	public String getCodice() {
		return StringUtils.defaultIfEmpty(cespite.getCodice(),"");
	}
	
	/**
	 * Gets the descrizione.
	 *
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return StringUtils.defaultIfEmpty(cespite.getDescrizione(),"");
	}
	
	/**
	 * Gets the classificazione.
	 *
	 * @return the classificazione
	 */
	public String getClassificazione() {
		if(cespite.getClassificazioneGiuridicaCespite() == null){
			return "";
		}
		return cespite.getClassificazioneGiuridicaCespite().getCodice() + " - " + cespite.getClassificazioneGiuridicaCespite().getDescrizione();
	}
	
	/**
	 * Gets the tipo bene.
	 *
	 * @return the tipo bene
	 */
	public String getTipoBene() {
		TipoBeneCespite tbc = cespite.getTipoBeneCespite();
		if(tbc == null){
			return "";
		}
		if(!Boolean.TRUE.equals(tbc.getAnnullato())) {
			return tbc.getCodice()  + " - "  + tbc.getDescrizione();
		}
		
		return new StringBuilder()
			.append("<a data-original-title=\"Informazioni\" data-trigger=\"hover\" rel=\"popover\" data-content=\"Annullato\">")
			.append(FormatUtils.formatCodificaCodiceDescrizione(tbc))
			.append("&nbsp;")
			.append("*")
			.append("</a>")
			.toString();
	}
	
	/**
	 * Gets the inventario.
	 *
	 * @return the inventario
	 */
	public String getInventario() {
		return cespite.getNumeroInventario();
	}
	
	/**
	 * Gets the attivo.
	 *
	 * @return the attivo
	 */
	public String getAttivo() {
		return FormatUtils.formatBoolean(cespite.getFlagStatoBene() ,SI, NO, "");
	}
	
	/**
	 * Gets the donazione rinvenimento.
	 *
	 * @return the donazione rinvenimento
	 */
	public String getDonazioneRinvenimento() {
		return FormatUtils.formatBoolean(cespite.getFlgDonazioneRinvenimento() ,SI, NO, "");
	}
	
	/**
	 * Gets the fondo ammortamento.
	 *
	 * @return the fondo ammortamento
	 */
	public String getFondoAmmortamento() {
		return FormatUtils.formatCurrency(cespite.getFondoAmmortamento());
	}
	
	/**
	 * Gets the residuo ammortamento.
	 *
	 * @return the residuo ammortamento
	 */
	public String getResiduoAmmortamento() {
		return FormatUtils.formatCurrency(cespite.getResiduoAmmortamento());
	}
	
	/**
	 * Gets the azione scollegamento.
	 *
	 * @return the azione scollegamento
	 */
	public String getAzioneScollegamento() {
		return AZIONE_SCOLLEGA;
	}
	
	/**
	 * Gets the valore attuale.
	 *
	 * @return the valore attuale
	 */
	public String getValoreAttuale() {
		return FormatUtils.formatCurrency(cespite.getValoreAttuale());
	}
	
	/**
	 * Gets the valore iniziale.
	 *
	 * @return the valore attuale
	 */
	public String getValoreIniziale() {
		return FormatUtils.formatCurrency(cespite.getValoreIniziale());
	}
	
	/**
	 * Gets the uid dismissione cespite collegata.
	 *
	 * @return the uid dismissione cespite collegata
	 */
	public int getUidDismissioneCespiteCollegata() {
		return cespite != null && cespite.getDismissioneCespite() != null && cespite.getDismissioneCespite().getUid() != 0 ?
				cespite.getDismissioneCespite().getUid()
				: 0;
	}
	
	/**
	 * Checks if is collegato A prime note.
	 *
	 * @return true, if is collegato A prime note
	 */
	public boolean isCollegatoAPrimeNote() {
		return cespite != null && Boolean.TRUE.equals(cespite.getIsCollegatoPrimeNote());
	}
	
	/**
	 * Gets the data accesso inventario.
	 *
	 * @return the data accesso inventario
	 */
	public String getDataAccessoInventario() {
		return FormatUtils.formatDate(cespite.getDataAccessoInventario());
	}
	
	/**
	 * Gets the ammortamento by anno.
	 *
	 * @param anno the anno
	 * @return the ammortamento by anno
	 */
	public String getAmmortamentoByAnno(Integer anno) {
		if(anno == null) {
			return "anno non presente";
		}
		return "Non disponibile";
		
	}
	
	/**
	 * Gets the codice descrizione.
	 *
	 * @return the codice descrizione
	 */
	public String getCodiceDescrizione() {
		return new StringBuilder().append(getCodice()).append(" - ").append(getDescrizione()).toString();
	}

	/**
	 * @return the stato
	 */
	public String getStato() {
		return cespite.getStato().toString();
	}

	/**
	 * @return the statoIter
	 */
	public String getStatoIter() {
		return cespite.getStato().toString();
	}

	/**
	 * Gets the importo ammortamento su singolo anno.
	 *
	 * @return the importo ammortamento su singolo anno
	 */
	public String getImportoAmmortamentoSuSingoloAnno() {
		BigDecimal sum = BigDecimal.ZERO;
		if(cespite == null || cespite.getAmmortamentoAnnuoCespite() == null || cespite.getAmmortamentoAnnuoCespite().getDettagliAmmortamentoAnnuoCespite() == null || cespite.getAmmortamentoAnnuoCespite().getDettagliAmmortamentoAnnuoCespite().isEmpty()) {
			return FormatUtils.formatCurrency(sum);
		}
		Integer annoDettaglio = null;
		for (DettaglioAmmortamentoAnnuoCespite dettaglioAmmortamentoAnnuoCespite : cespite.getAmmortamentoAnnuoCespite().getDettagliAmmortamentoAnnuoCespite()) {
			if(annoDettaglio != null && !annoDettaglio.equals(dettaglioAmmortamentoAnnuoCespite.getAnno())) {
				return "<a data-original-title=\"Informazioni\" data-trigger=\"hover\" rel=\"popover\" data-content=\"Impossibile determinare un importo univoco per l'anno\">N.D.</a>";
			}
			sum = sum.add(dettaglioAmmortamentoAnnuoCespite.getQuotaAnnuale());
		}
		return FormatUtils.formatCurrency(sum);
	}
	
	/**
	 * Gets the valore attuale su registro A.
	 *
	 * @return the valore attuale su registro A
	 */
	public String getValoreAttualeSuRegistroA() {
		BigDecimal valoreSuPrimaNota = cespite.getImportoSuRegistroA() != null? cespite.getImportoSuRegistroA() : BigDecimal.ZERO;
		return FormatUtils.formatCurrency(valoreSuPrimaNota);
	}
	
	/**
	 * Gets the inserimento contestuale registro A.
	 *
	 * @return the inserimento contestuale registro A
	 */
	public boolean getInserimentoContestualeRegistroA() {
		return Boolean.TRUE.equals(cespite.getInserimentoContestualeRegistroA());
	}
	
}
