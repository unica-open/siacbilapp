/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccespapp.frontend.ui.util.wrappers.registroa;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacgenser.model.MovimentoEP;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoAccettazionePrimaNotaDefinitiva;
import it.csi.siac.siacgenser.model.TipoCausale;

/**
 * Wrapper per la la prima nota del registro A.
 * @author Marchino Alessandro
 * @version 1.0.0 - 23/10/2018
 */
public class ElementoPrimaNotaRegistroA implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = -7559394896467991498L;
	private final PrimaNota primaNota;
	private String azioni;
	
	/**
	 * Costruttore di wrap
	 * @param primaNota la prima nota da wrappare
	 */
	public ElementoPrimaNotaRegistroA(PrimaNota primaNota) {
		this.primaNota = primaNota;
	}
	
	/**
	 * Unwrap del modello wrappato
	 * @return il modello wrappato
	 */
	public PrimaNota unwrap() {
		return primaNota;
	}

	/**
	 * @return the azioni
	 */
	public String getAzioni() {
		return this.azioni;
	}

	/**
	 * @param azioni the azioni to set
	 */
	public void setAzioni(String azioni) {
		this.azioni = azioni;
	}

	@Override
	public int getUid() {
		return primaNota.getUid();
	}
	
	/**
	 * @return the numeroPrimaNota
	 */
	public Integer getNumeroPrimaNota() {
		return primaNota.getNumeroRegistrazioneLibroGiornale();
	}
	
	/**
	 * @return the contoPatrimoniale
	 */
	public String getContoPatrimoniale() {
		if(primaNota.getContoInventario() == null) {
			return "";
		}
		return new StringBuilder()
			.append("<a href=\"#\" data-original-title=\"Descrizione\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
			.append(FormatUtils.formatHtmlAttributeString(primaNota.getContoInventario().getDescrizione()))
			.append("\">")
			.append(FormatUtils.formatHtmlAttributeString(primaNota.getContoInventario().getCodice()))
			.append("</a>")
			.toString();
	}
	
	/**
	 * @return the dataDefinizionePrimaNota
	 */
	public String getDataDefinizionePrimaNota() {
		return FormatUtils.formatDate(primaNota.getDataRegistrazioneLibroGiornale());
	}
	
	/**
	 * @return the numeroInventario
	 */
	public String getNumeroInventario() {
		//SIAC-6672
		if(primaNota.getPrimaNotaInventario() == null || primaNota.getPrimaNotaInventario().getNumeroRegistrazioneLibroGiornale() == null  || primaNota.getPrimaNotaInventario().getUid() == 0) {
			return "";
		}
		return new StringBuilder(primaNota.getPrimaNotaInventario().getNumeroRegistrazioneLibroGiornale().intValue())
				.append("&nbsp;<a class=\"tooltip-test\" title=\"dati Cespite\" href=\"#\" data-ricerca-cespite=\"")
				.append(primaNota.getUid())
				.append("\"><i class=\"icon-info-sign\">&nbsp;<span class=\"nascosto\">Dati Cespite</span></i></a>")
				.toString();
	}

	
	/**
	 * @return the infoCespite
	 */
	public String getInfoCespite() {
		if(primaNota.getPrimaNotaInventario() == null || primaNota.getPrimaNotaInventario().getUid() == 0) {
			return "";
		}
		return "<a class=\"tooltip-test\" href=\"#\" data-original-title=\"Cespiti collegati\"><i class=\"icon-info-sign\">&nbsp;<span class=\"nascosto\">Cespiti collegati</span></i></a>";
	}

	
	/**
	 * @return the statoInventario
	 */
	public String getStatoInventario() {
		StatoAccettazionePrimaNotaDefinitiva sapnd = StatoAccettazionePrimaNotaDefinitiva.DA_ACCETTARE;
		if(primaNota.getPrimaNotaInventario() != null && primaNota.getPrimaNotaInventario().getStatoAccettazionePrimaNotaDefinitiva() != null) {
			sapnd = primaNota.getPrimaNotaInventario().getStatoAccettazionePrimaNotaDefinitiva();
		}
		return sapnd.getCodice() + " - " +sapnd.getDescrizione();

		//
		/*
		return new StringBuilder()
			.append("<a href=\"#\" data-original-title=\"Descrizione\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
			.append(sapnd.getDescrizione())
			.append("\">")
			.append(sapnd.getCodice())
			.append("</a>")
			.toString();
		*/
	}
	
	/**
	 * @return the statoCoGe
	 */ 
	public String getStatoCoGe() {
		if(primaNota.getStatoOperativoPrimaNota() == null) {
			return "";
		}
		return new StringBuilder()
			.append("<a href=\"#\" data-original-title=\"Descrizione\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
			.append(primaNota.getStatoOperativoPrimaNota().getDescrizione())
			.append("\">")
			.append(primaNota.getStatoOperativoPrimaNota().getCodice())
			.append("</a>")
			.toString();
	}
	
	/**
	 * Gets the uid prima nota inventario.
	 *
	 * @return the uid prima nota inventario
	 */
	public Integer getUidPrimaNotaInventario() {
		return primaNota.getPrimaNotaInventario() != null? Integer.valueOf(primaNota.getPrimaNotaInventario().getUid()) : null;
	}
	
	/**
	 * Checks if is integrata.
	 *
	 * @return the boolean
	 */
	public boolean isIntegrata() {
		return TipoCausale.Integrata.equals(primaNota.getTipoCausale());
	}
	
	/**
	 * @return the tipoCollegamentoDatiFinanziari
	 */
	public String getNameTipoCollegamentoDatiFinanziari() {
		if(primaNota == null || primaNota.getListaMovimentiEP() == null) {
			return "";
		}
		for(MovimentoEP mep : primaNota.getListaMovimentiEP()) {
			if(mep != null && mep.getRegistrazioneMovFin() != null && mep.getRegistrazioneMovFin().getEvento() != null && mep.getRegistrazioneMovFin().getEvento().getTipoCollegamento() != null) {
				return mep.getRegistrazioneMovFin().getEvento().getTipoCollegamento().name();
			}
		}
		return "";
	}
	
	

}
