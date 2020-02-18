/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacgenser.model.CausaleEP;
import it.csi.siac.siacgenser.model.Evento;
import it.csi.siac.siacgenser.model.PrimaNota;
import it.csi.siac.siacgenser.model.StatoOperativoPrimaNota;

/**
 * Wrapper per la causale EP.
 * 
 * @author Paggio Simona, Alessandro Marchino
 * @version 1.0.0 - 05/05/2015
 * @version 1.0.1 - 12/05/2015
 *
 */
public class ElementoPrimaNotaLibera implements Serializable, ModelWrapper {

	/** Per la serializzazione */
	private static final long serialVersionUID = 9107056116333597659L;
	
	private PrimaNota primaNotaLibera;
	private String azioni;
	
	/**
	 * Costruttore di wrap.
	 * 
	 * @param primaNotaLibera la primaNota da wrappare
	 */
	public ElementoPrimaNotaLibera(PrimaNota primaNotaLibera) {
		this.primaNotaLibera = primaNotaLibera;
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
	
	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return primaNotaLibera != null && primaNotaLibera.getTipoCausale() != null ? primaNotaLibera.getTipoCausale().getDescrizione() : "";
	}
	
	
	/**
	 * Prima Nota Libera ha solo un movimentoEP.
	 * 
	 * @return the causaleEP
	 */
	public CausaleEP getCausaleEP() {
		return primaNotaLibera != null 
				&& primaNotaLibera.getListaMovimentiEP() != null 
				&& !primaNotaLibera.getListaMovimentiEP().isEmpty()
				&& primaNotaLibera.getListaMovimentiEP().get(0) != null
					? primaNotaLibera.getListaMovimentiEP().get(0).getCausaleEP()
					: null;
	}
	
	/**
	 * @return the codice
	 */
	public String getCodiceCausaleEP() {
		return getCausaleEP() != null ? getCausaleEP().getCodice() : "";
	}
	
	/**
	 * @return the descrizione
	 */
	public String getDescrizione() {
		return primaNotaLibera!=null ? primaNotaLibera.getDescrizione() : "";
	}
	
	/**
	 * @return the numero
	 */
	public String getNumero() {
		return primaNotaLibera != null && primaNotaLibera.getNumero() != null ? primaNotaLibera.getNumero().toString() : "";
	}
	
	/**
	 * @return the NumeroLibroGiornale
	 */
	public String getNumeroLibroGiornale() {
		return primaNotaLibera != null && primaNotaLibera.getNumeroRegistrazioneLibroGiornale() != null ? primaNotaLibera.getNumeroRegistrazioneLibroGiornale().toString() : "";
	}
	
	/**
	 * @return the info
	 */
	public String getInfoENumero() {
		StringBuilder sb = new StringBuilder();
		
		if(primaNotaLibera != null) {
			sb.append("<a class=\"tooltip-test\" data-original-title=\"Visualizza dettagli\" data-toggle=\"modal\" >")
				.append("<i class=\"icon-info-sign\">&nbsp;")
				.append("<span class=\"nascosto\">")
				.append("Visualizza dettagli")
				.append("</span>")
				.append("</i>")
				.append("</a>")
				.append(primaNotaLibera.getNumero());
		}
		
		return sb.toString();
	}
	
	/**
	 * @return the statoOperativoPrimaNota
	 */
	public StatoOperativoPrimaNota getStatoOperativoPrimaNota() {
		return primaNotaLibera != null ? primaNotaLibera.getStatoOperativoPrimaNota() : null;
	}
	
	/**
	 * @return the dataRegistrazione
	 */
	public String getDataRegistrazione() {
		return primaNotaLibera != null ? FormatUtils.formatDate(primaNotaLibera.getDataRegistrazione()) : "";
	}
	
	/**
	 * @return the dataRegistrazioneDefinitiva
	 */
	public String getDataRegistrazioneDefinitiva() {
		return primaNotaLibera != null ? FormatUtils.formatDate(primaNotaLibera.getDataRegistrazioneLibroGiornale()) : "";
	}
	
	/**
	 * @return the dataRegistrazione
	 */
	public String getNumeroDataRegistrazione() {
		return primaNotaLibera != null ? FormatUtils.formatDate(primaNotaLibera.getDataRegistrazione()) : "";
	}
	
	/**
	 * @return the statoOperativo
	 */
	public String getStatoOperativo () {
		return primaNotaLibera != null && primaNotaLibera.getStatoOperativoPrimaNota() != null ? primaNotaLibera.getStatoOperativoPrimaNota().getDescrizione() : "";
	}
	
	/**
	 * @return the causaleEP
	 */
	public String getCausaleEPWithTootip() {
		StringBuilder sb = new StringBuilder();
		CausaleEP causaleEP = getCausaleEP();
		if(causaleEP != null) {
			sb.append("<a data-original-title=\"Descrizione\" data-trigger=\"hover\" rel=\"popover\" data-content=\"")
				.append(FormatUtils.formatHtmlAttributeString(causaleEP.getDescrizione()))
				.append("\" >")
				.append(causaleEP.getCodice())
				.append("</a>");
		}
		return sb.toString();
	}
	
	@Override
	public int getUid() {
		return primaNotaLibera != null ? primaNotaLibera.getUid() : -1;
	}
	
	/**
	 * @return the evento
	 */
	public String getEvento() {
		Evento ev = null;
		try{
			ev = primaNotaLibera.getListaMovimentiEP().get(0).getCausaleEP().getEventi().get(0);
		}catch(Exception e){
			return "";
		}
		return ev != null ?  ev.getCodice().toString() + "-" + ev.getDescrizione().toString() : ""; 
	}
	
	/**
	 * @return the primaNotaProvvisoria
	 */
	public String getPrimaNotaProvvisoria() {
		return getNumero() + "   " + getDataRegistrazione();
	}
	/**
	 * @return the primaNotaDefinitiva
	 */
	public String getPrimaNotaDefinitiva() {
		return getNumeroLibroGiornale() + "   " + getDataRegistrazioneDefinitiva();
	}
	
	/**
	 * @return the infoCespite
	 */
	public String getInfoCespite() {
		return "<a class=\"tooltip-test\" href=\"#\" data-original-title=\"Cespiti collegati\"><i class=\"icon-info-sign\">&nbsp;<span class=\"nascosto\">Cespiti collegati</span></i></a>";
	}

	/*
	public String getNumeroCespite() {
		return  cespite.getCodice();
	}
	
	public String getTipoBene() {	
		String cod         = cespite != null && cespite.getTipoBeneCespite().getCodice() != null ? cespite.getTipoBeneCespite().getCodice().toString() : "";
		String descrizione = cespite != null && cespite.getTipoBeneCespite().getDescrizione() != null ? cespite.getTipoBeneCespite().getDescrizione().toString() : "";
		return  cod +'-' + descrizione;
	}
	
	public String getValoreIniziale() {
		return  cespite != null && cespite.getValoreIniziale() !=null ? cespite.getValoreIniziale().toString() : "" ;
	}

	public String getValoreAttuale() {
		return  cespite.getValoreAttualeNotNull().toString();
	}

	public String getStatoCoge() {		
		String cod         = primaNotaLibera != null && primaNotaLibera.getStatoOperativoPrimaNota().getCodice() != null ? primaNotaLibera.getStatoOperativoPrimaNota().getCodice().toString() : "";
		String descrizione = primaNotaLibera != null && primaNotaLibera.getStatoOperativoPrimaNota().getDescrizione() != null ? primaNotaLibera.getStatoOperativoPrimaNota().getDescrizione().toString() : "";
		return  cod +'-' + descrizione;
	}
	*/
}
