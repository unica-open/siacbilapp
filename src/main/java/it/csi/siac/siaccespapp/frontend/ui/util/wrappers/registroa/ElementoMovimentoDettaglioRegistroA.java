/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.util.wrappers.registroa;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.registrazionemovfin.movimento.RegistrazioneMovFinMovimentoCollegatoHelper;
import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siacgenser.model.Conto;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.MovimentoDettaglio;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * Wrapper per la la prima nota del registro A.
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/10/2018
 */
public class ElementoMovimentoDettaglioRegistroA implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -6418145409531201569L;

	private final MovimentoEP movimentoEP;
	private final MovimentoDettaglio movimentoDettaglio;
	
	
	private final Conto contoCespite;
	private final Evento eventoCespite;
	private final Boolean hasContoCespite;
	private final Boolean hasContoImposta;
	private final List<Cespite> listaBeniCollegati;
	private String azioni;
	
	
	/**
	 * Instantiates a new elemento movimento EP registro A.
	 *
	 * @param movimentoDettaglio the movimento dettaglio
	 * @param movimentoEP the movimento EP
	 * @param contoCespite the conto cespite
	 * @param eventoCespite the evento cespite
	 * @param hasContoCespite the has conto cespite
	 * @param hasContoImposta the has conto imposta
	 */
	public ElementoMovimentoDettaglioRegistroA(MovimentoDettaglio movimentoDettaglio, MovimentoEP movimentoEP,
			Conto contoCespite, Evento eventoCespite, Boolean hasContoCespite, Boolean hasContoImposta
			) {
		this.movimentoEP = movimentoEP;
		this.movimentoDettaglio = movimentoDettaglio;
		this.contoCespite = contoCespite;
		this.eventoCespite = eventoCespite;
		this.hasContoCespite = hasContoCespite;
		this.hasContoImposta = hasContoImposta;
		this.listaBeniCollegati = movimentoDettaglio.getCespiti();
	}


	@Override
	public int getUid() {
		return this.movimentoDettaglio.getUid();
	}
	
	/**
	 * Gets the conto cespite.
	 *
	 * @return the conto cespite
	 */
	public Conto getContoCespite() {
		return this.contoCespite;
	}
	
	/**
	 * @return the listaBeniCollegati
	 */
	public List<Cespite> getListaBeniCollegati() {
		return listaBeniCollegati;
	}

	/**
	 * @return the hasContoCespite
	 */
	public Boolean getHasContoCespite() {
		return hasContoCespite;
	}


	/**
	 * @return the hasContoImposta
	 */
	public Boolean getHasContoImposta() {
		return hasContoImposta;
	}


	/**
	 * @return the importoMovimento
	 */
	public BigDecimal getImportoTotaleCespitiCollegati() {
		return movimentoDettaglio.getImportoInventariato() != null? movimentoDettaglio.getImportoInventariato() : BigDecimal.ZERO;
	}

	/**
	 * Gets the azioni.
	 *
	 * @return the azioni
	 */
	public String getAzioni() {
		return azioni;
	}

	/**
	 * Sets the azioni.
	 *
	 * @param azioni the azioni to set
	 */
	public void setAzioni(String azioni) {
		this.azioni = azioni;
	}

	/**
	 * Gets the movimento EP.
	 *
	 * @return the movimentoEP
	 */
	public MovimentoEP getMovimentoEP() {
		return movimentoEP;
	}

	/**
	 * Gets the movimento finanziario.
	 *
	 * @return the movimento finanziario
	 */
	public String getMovimentoFinanziario() {
		if(movimentoEP.getPrimaNota() == null || movimentoEP.getPrimaNota().getTipoCausale() == null) {
			return "";
		}
		return TipoCausale.Integrata.equals(movimentoEP.getPrimaNota().getTipoCausale()) && movimentoEP.getRegistrazioneMovFin() != null ? getNumeroMovimentoFinanziarioIntegrato() : getNumeroMovimentoFinanziarioLibero();
	}
	
	private String getNumeroMovimentoFinanziarioIntegrato() {
		return RegistrazioneMovFinMovimentoCollegatoHelper.getNumeroMovimentoFromRegistrazione(movimentoEP.getRegistrazioneMovFin());
	}

	private String  getNumeroMovimentoFinanziarioLibero() {
		return "Prima Nota libera";
	}

	/**
	 * Valorizzato a Si solo se nelle scritture di prima nota è presente un conto patrimoniale con tipo importo = Imposta 
	 *
	 * @return the iva commerciale
	 */
	public String getIvaCommerciale() {
		return Boolean.TRUE.equals(hasContoImposta) ?  "S&igrave;" : "No";
	}
	
	private BigDecimal getImportoMovimentoDettaglio() {
		return movimentoDettaglio != null && movimentoDettaglio.getImporto() != null? movimentoDettaglio.getImporto() : BigDecimal.ZERO;
	}
	
	/**
	 * Gets the importo finanziario.
	 *
	 * @return the importo finanziario
	 */
	public String getImportoFinanziario() {
		return FormatUtils.formatCurrency(getImportoMovimentoDettaglio());
	}
	
	/**
	 * Gets the codice conto cespite.
	 *
	 * @return the codice conto cespite
	 */
	public String getCodiceContoCespite() {
		return this.contoCespite != null ? StringUtils.defaultIfBlank(this.contoCespite.getCodice(), "") : "";
	}
	
	/**
	 * Gets the descrizione conto cespite.
	 *
	 * @return the descrizione conto cespite
	 */
	public String getDescrizioneContoCespite() {
		return this.contoCespite != null ? StringUtils.defaultIfBlank(this.contoCespite.getDescrizione(), "") : "";
	}
	
	/**
	 * Gets the importo da inventariare alienare.
	 *
	 * @return the importo da inventariare alienare
	 */
	public String getImportoDaInventariareAlienare() {
		BigDecimal importoDaInventariare = getImportoMovimentoDettaglio().subtract(getImportoTotaleCespitiCollegati());
		return FormatUtils.formatCurrency(importoDaInventariare);
	}
	

	/**
	 * Gets the importo da inventariato.
	 *
	 * @return the importo da inventariato
	 */
	public String getImportoInventariatoString() {
		return FormatUtils.formatCurrency(getImportoTotaleCespitiCollegati());
	}
	
	/**
	 * Gets the importo da inventariato.
	 *
	 * @return the importo da inventariato
	 */
	public String getNumeroBeni() {
		return this.listaBeniCollegati != null? "" + this.listaBeniCollegati.size() : "0";
	}
	
	/**
	 * Gets the codice evento cespite.
	 *
	 * @return the codice evento cespite
	 */
	public String getCodiceEventoCespite() {
		String defaultCodiceEvento = movimentoEP.getCausaleEP() != null? movimentoEP.getCausaleEP().getCodice() : "";
		return StringUtils.defaultIfBlank((this.eventoCespite != null? this.eventoCespite.getCodice() : ""), defaultCodiceEvento);
	}
	
	/**
	 * Gets the tipo causale prima nota.
	 *
	 * @return the tipo causale prima nota
	 */
	public TipoCausale getTipoCausalePrimaNota() {
		return movimentoEP.getPrimaNota().getTipoCausale();
	}
}
