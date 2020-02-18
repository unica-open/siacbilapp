/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbasegengsaapp.frontend.ui.util.wrapper.primanotalibera;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.format.FormatUtils;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siaccespser.model.Cespite;
import it.csi.siac.siaccespser.model.DettaglioAmmortamentoAnnuoCespite;
import it.csi.siac.siaccespser.model.DismissioneCespite;
import it.csi.siac.siaccespser.model.VariazioneCespite;
import it.csi.siac.siacgenser.model.CausaleEP;
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
public class ElementoConsultazioneScritturaPrimaNotaLibera implements Serializable, ModelWrapper {

	/** Per la serializzazione */
	private static final long serialVersionUID = -8280206550745259939L;
	private final PrimaNota primaNotaLibera;
	private List<ElementoScritturaPrimaNotaLibera> listaElementoScritturaPrimaNotaLibera;
	private String entitaGenerantePrimaNota;
	
	private static final Map<String, String> MAPPING_SUFFIX;
	
	static {
		Map<String, String> tmp = new HashMap<String, String>();
		tmp.put(Cespite.class.getSimpleName(), "DonazioniRinvenimenti");
		tmp.put(VariazioneCespite.class.getSimpleName(), "SvalutazioniRivalutazioni");
		tmp.put(DismissioneCespite.class.getSimpleName(), "Dismissioni");
		tmp.put(DettaglioAmmortamentoAnnuoCespite.class.getSimpleName(), "DettaglioAmmortamentoAnnuo");
		MAPPING_SUFFIX = Collections.unmodifiableMap(tmp);
	}
	
	
	private static final String INTESTAZIONE = "<h4 class=\"step-pane\">Elenco scritture prima nota %DESC%</h4>";
	
	private static final String TABELLA = new StringBuilder()
			.append("<table class=\"table table-hover tab_left\" id=\"tabellaScritture%SUFFIX%\">") 
			.append("    <thead>")
			.append("        <tr>")
			.append("			<th>Conto</th>")
			.append("			<th>Descrizione</th>")
			.append("			<th class=\"tab_Right\">Dare</th>")
			.append("			<th class=\"tab_Right\">Avere</th>")
			.append("		</tr>")
			.append("	</thead>")
			.append("	<tbody>")
			.append("	</tbody>")
			.append("</table>")
			.toString();
	
	/**
	 * Instantiates a new elemento consultazione scrittura prima nota libera.
	 *
	 * @param primaNotaLibera the prima nota libera
	 */
	public ElementoConsultazioneScritturaPrimaNotaLibera(PrimaNota primaNotaLibera) {
		this.primaNotaLibera = primaNotaLibera;
		this.entitaGenerantePrimaNota = "";
	}
	
	/**
	 * Costruttore di wrap.
	 *
	 * @param primaNotaLibera la primaNota da wrappare
	 * @param clazzGenerante the clazz generante
	 */
	public ElementoConsultazioneScritturaPrimaNotaLibera(PrimaNota primaNotaLibera, Class<?> clazzGenerante) {
		this.primaNotaLibera = primaNotaLibera;
		this.entitaGenerantePrimaNota = clazzGenerante.getSimpleName();
	}
	
	/**
	 * @return the listaElementoScritturaPrimaNotaLibera
	 */
	public List<ElementoScritturaPrimaNotaLibera> getListaElementoScritturaPrimaNotaLibera() {
		return listaElementoScritturaPrimaNotaLibera;
	}

	/**
	 * @param listaElementoScritturaPrimaNotaLibera the listaElementoScritturaPrimaNotaLibera to set
	 */
	public void setListaElementoScritturaPrimaNotaLibera(
			List<ElementoScritturaPrimaNotaLibera> listaElementoScritturaPrimaNotaLibera) {
		this.listaElementoScritturaPrimaNotaLibera = listaElementoScritturaPrimaNotaLibera;
	}

	/**
	 * Gets the intestazione.
	 *
	 * @return the intestazione
	 */
	public String  getIntestazione() {
		return INTESTAZIONE.replaceAll("%DESC%", getDescrizioneIntestazionePrimaNota());
	}
	
	/**
	 * Gets the tabella.
	 *
	 * @return the tabella
	 */
	public String getTabella() {
		if(primaNotaLibera == null) {
			return "";
		}
		return TABELLA.replaceAll("%SUFFIX%", getSuffixSelettoreTabella());
	}
	
	/**
	 * Gets the suffix selettore tabella.
	 *
	 * @return the suffix selettore tabella
	 */
	public String getSuffixSelettoreTabella() {
		return new StringBuilder()
				.append(StringUtils.isNotBlank(MAPPING_SUFFIX.get(this.entitaGenerantePrimaNota))? 
						MAPPING_SUFFIX.get(this.entitaGenerantePrimaNota) : "")
				.append("_")
				.append(primaNotaLibera != null && primaNotaLibera.getUid() != 0? 
						Integer.toString(primaNotaLibera.getUid()) : "")
				.toString();
	}


	private String getDescrizioneIntestazionePrimaNota() {
		if(primaNotaLibera == null) {
			return " non presente";
		}
		StringBuilder sb = new StringBuilder().append(" numero ")
		.append(primaNotaLibera.getNumero())
		.append(" ( ");
		if(primaNotaLibera.getStatoAccettazionePrimaNotaDefinitiva() != null) {
			sb.append(primaNotaLibera.getStatoAccettazionePrimaNotaDefinitiva().getDescrizione());
		}else if(primaNotaLibera.getStatoAccettazionePrimaNotaProvvisoria() != null){
			sb.append(primaNotaLibera.getStatoAccettazionePrimaNotaProvvisoria().getDescrizione());
		}
		return sb.append(" - ")
		.append(primaNotaLibera.getStatoOperativoPrimaNota() != null? primaNotaLibera.getStatoOperativoPrimaNota().getDescrizione() : "" )
		.append(" ) ").toString();
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
	
}
