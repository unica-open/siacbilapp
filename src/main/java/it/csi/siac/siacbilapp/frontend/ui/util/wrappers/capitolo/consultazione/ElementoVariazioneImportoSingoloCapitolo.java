/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.consultazione;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import it.csi.siac.siacattser.model.AttoAmministrativo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoComponenteVariazione;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.variazione.importi.ElementoComponenteVariazioneFactory;
import it.csi.siac.siacbilser.model.DettaglioVariazioneComponenteImportoCapitolo;
import it.csi.siac.siacbilser.model.ImportiCapitolo;
import it.csi.siac.siacbilser.model.VariazioneImportoSingoloCapitolo;
import it.csi.siac.siaccommon.util.number.NumberUtils;

/**
 * Wrapper per i dati di variazione importo del singolo capitolo.
 *
 * @author Marchino Alessandro
 */
public class ElementoVariazioneImportoSingoloCapitolo implements ModelWrapper, Serializable {

	/** Per la serializzazione. */
	private static final long serialVersionUID = 9102352651390625462L;

	/** The Constant IDX_PLACEHOLDER. */
	private static final String IDX_PLACEHOLDER = "%%CURRENT_IDX%%";

	/** The variazione importo singolo capitolo. */
	private final VariazioneImportoSingoloCapitolo variazioneImportoSingoloCapitolo;
	private final Integer annoEsercizio;
	private final List<DettaglioVariazioneComponenteImportoCapitolo> listaDettaglioVariazioneComponenteImportoCapitolo;
	private final List<DettaglioVariazioneComponenteImportoCapitolo> listaDettaglioVariazioneComponenteImportoCapitolo1;
	private final List<DettaglioVariazioneComponenteImportoCapitolo> listaDettaglioVariazioneComponenteImportoCapitolo2;
	private List<ImportiCapitolo> listImportiCapitolo;
	
	/**
	 * Costruttore di wrap.
	 *
	 * @param variazioneImportoSingoloCapitolo i dati di variazione da wrappare
	 * @param annoEsercizio l'anno esercizio
	 * @param listaDettaglioVariazioneComponenteImportoCapitolo la lista dei dettagli per l'anno 0
	 * @param listaDettaglioVariazioneComponenteImportoCapitolo1 la lista dei dettagli per l'anno 1
	 * @param listaDettaglioVariazioneComponenteImportoCapitolo2 la lista dei dettagli per l'anno 2
	 */
	public ElementoVariazioneImportoSingoloCapitolo(VariazioneImportoSingoloCapitolo variazioneImportoSingoloCapitolo, Integer annoEsercizio,
			List<DettaglioVariazioneComponenteImportoCapitolo> listaDettaglioVariazioneComponenteImportoCapitolo,
			List<DettaglioVariazioneComponenteImportoCapitolo> listaDettaglioVariazioneComponenteImportoCapitolo1,
			List<DettaglioVariazioneComponenteImportoCapitolo> listaDettaglioVariazioneComponenteImportoCapitolo2) {
		this.listaDettaglioVariazioneComponenteImportoCapitolo = listaDettaglioVariazioneComponenteImportoCapitolo;
		this.listaDettaglioVariazioneComponenteImportoCapitolo1 = listaDettaglioVariazioneComponenteImportoCapitolo1;
		this.listaDettaglioVariazioneComponenteImportoCapitolo2 = listaDettaglioVariazioneComponenteImportoCapitolo2;
		this.variazioneImportoSingoloCapitolo = variazioneImportoSingoloCapitolo;
		this.annoEsercizio = annoEsercizio;
	}

	/**
	 * @return the listImportiCapitolo
	 */
	public List<ImportiCapitolo> getListImportiCapitolo() {
		return this.listImportiCapitolo;
	}

	/**
	 * @param listImportiCapitolo the listImportiCapitolo to set
	 */
	public void setListImportiCapitolo(List<ImportiCapitolo> listImportiCapitolo) {
		this.listImportiCapitolo = listImportiCapitolo;
	}

	/**
	 * Gets the accordion html.
	 *
	 * @return the accordionHtml
	 */
	public String getAccordionHtml() {
		return new StringBuilder()
				.append("<div class=\"accordion-group\">")
				.append("<div class=\"accordion-heading\">")
				.append("<a class=\"accordion-toggle collapsed\" data-toggle=\"collapse\" data-parent=\"#accordionPadre\" href=\"#collapse")
				.append(IDX_PLACEHOLDER).append("\">")
				.append("Variazione ")
				.append(variazioneImportoSingoloCapitolo.getNumero()).append("<span class=\"icon\">&nbsp;</span>")
				.append("</a>")
				.append("</div>")
				.append("<div id=\"collapse").append(IDX_PLACEHOLDER)
				.append("\" class=\"accordion-body collapse\">")
				.append("<div class=\"accordion-inner\">")
				.append(computeDettaglio())
				.append(computeAttoAmministrativo(variazioneImportoSingoloCapitolo.getAttoAmministrativo(), "Provvedimento variazione di PEG"))
				.append(computeAttoAmministrativo(variazioneImportoSingoloCapitolo.getAttoAmministrativoVariazioneDiBilancio(), "Provvedimento variazione di bilancio"))
				.append(computeImporti()).append("</div>").append("</div>").append("</div>").toString();
	}

	/**
	 * Aggiunge i dati del dettaglio.
	 *
	 * @return la stringa del dettaglio
	 */
	private String computeDettaglio() {
		return new StringBuilder().append("<dl class=\"dl-horizontal\">")
				.append("<dt>Stato</dt>").append("<dd>").append(getDescrizioneStatoOperativoVariazioneBilancio()).append("&nbsp;</dd>")
				.append("<dt>Applicazione</dt>").append("<dd>").append(getNameApplicazioneVariazione()).append("&nbsp;</dd>")
				.append("<dt>Descrizione</dt>").append("<dd>").append(variazioneImportoSingoloCapitolo.getDescrizione()).append("&nbsp;</dd>")
				.append("<dt>Note</dt>").append("<dd>").append(variazioneImportoSingoloCapitolo.getNote()).append("&nbsp;</dd>")
				.append("<dt>Tipo Variazione</dt>").append("<dd>").append(getDescrizioneTipoVariazione()).append("&nbsp;</dd>")
				.append("</dl>")
				.toString();
	}

	/**
	 * Gets the descrizione tipo variazione.
	 *
	 * @return the descrizioneTipoVariazione
	 */
	private String getDescrizioneTipoVariazione() {
		return variazioneImportoSingoloCapitolo.getTipoVariazione() != null ? variazioneImportoSingoloCapitolo.getTipoVariazione().getDescrizione() : "";
	}

	/**
	 * Gets the name applicazione variazione.
	 *
	 * @return the nameApplicazioneVariazione
	 */
	private String getNameApplicazioneVariazione() {
		return variazioneImportoSingoloCapitolo.getApplicazioneVariazione() != null ? variazioneImportoSingoloCapitolo.getApplicazioneVariazione().name() : "";
	}

	/**
	 * Gets the descrizione stato operativo variazione bilancio.
	 *
	 * @return the descrizioneStatoOperativoVariaizoneBilancio
	 */
	private String getDescrizioneStatoOperativoVariazioneBilancio() {
		return variazioneImportoSingoloCapitolo.getStatoOperativoVariazioneDiBilancio() != null ? variazioneImportoSingoloCapitolo.getStatoOperativoVariazioneDiBilancio().getDescrizione() : "";
	}

	/**
	 * Aggiunge i dati dell'atto amministrativo.
	 *
	 * @param attoAmministrativo the atto amministrativo
	 * @return la stringa dell'atto amministrativo
	 */
	private String computeAttoAmministrativo(AttoAmministrativo attoAmministrativo, String titoloAttoAmministrativo) {
		return new StringBuilder().append("<h5>")
				.append(titoloAttoAmministrativo != null ? titoloAttoAmministrativo : "Provvedimento").append("</h5>")
				.append("<dl class=\"dl-horizontal\">").append("<dt>Tipo</dt>").append("<dd>")
				.append(getCodiceAttoAttoAmministrativo(attoAmministrativo)).append(" - ")
				.append(getDescrizioneTipoAttoAttoAmministrativo(attoAmministrativo)).append("&nbsp;</dd>")
				.append("<dt>Anno</dt>").append("<dd>").append(getAnnoAttoAmministrativo(attoAmministrativo))
				.append("&nbsp;</dd>").append("<dt>Numero</dt>").append("<dd>")
				.append(getNumeroAttoAmministrativo(attoAmministrativo)).append("&nbsp;</dd>")
				.append("<dt>Struttura</dt>").append("<dd>")
				.append(getDescrizioneStrutturaAmministrativoContabileAttoAmministrativo(attoAmministrativo))
				.append("&nbsp;</dd>").append("<dt>Oggetto</dt>").append("<dd>")
				.append(getOggettoAttoAmministrativo(attoAmministrativo)).append("&nbsp;</dd>").append("</dl>")
				.toString();
	}

	private String getCodiceAttoAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		return attoAmministrativo != null ? Integer.toString(attoAmministrativo.getUid()) : "";
	}

	/**
	 * Gets the oggetto atto amministrativo.
	 *
	 * @param attoAmministrativo the atto amministrativo da cui ricavare l'oggetto
	 * @return l' oggetto dell' atto amministrativo
	 */
	private String getOggettoAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		return attoAmministrativo != null ? attoAmministrativo.getOggetto() : "";
	}

	/**
	 * Ottiene la descrizione struttura amministrativo contabile atto
	 * amministrativo.
	 *
	 * @param attoAmministrativo the atto amministrativo da cui ricavare la SAC
	 * @return la descrizione della SAC ottenuta
	 */
	private String getDescrizioneStrutturaAmministrativoContabileAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		return attoAmministrativo != null && attoAmministrativo.getStrutturaAmmContabile() != null ? attoAmministrativo.getStrutturaAmmContabile().getDescrizione() : "";
	}

	/**
	 * Ottiene the numero atto amministrativo.
	 *
	 * @param attoAmministrativo the atto amministrativo da cui ricavare il numero
	 * @return the numero atto amministrativo
	 */
	private Object getNumeroAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		return attoAmministrativo != null ? Integer.toString(attoAmministrativo.getNumero()) : "";
	}

	/**
	 * Ottiene the anno atto amministrativo.
	 *
	 * @param attoAmministrativo the atto amministrativo da cui ricavare l'anno
	 * @return the anno atto amministrativo dell'atto
	 */
	private String getAnnoAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		return attoAmministrativo != null ? Integer.toString(attoAmministrativo.getAnno()) : "";
	}

	/**
	 * Ottiene la descrizione tipo atto atto amministrativo.
	 *
	 * @param attoAmministrativo the atto amministrativo da cui ricavare la descrizione del tipo atto
	 * @return the descrizione tipo atto atto amministrativo
	 */
	private String getDescrizioneTipoAttoAttoAmministrativo(AttoAmministrativo attoAmministrativo) {
		return attoAmministrativo != null && attoAmministrativo.getTipoAtto() != null
				? attoAmministrativo.getTipoAtto().getDescrizione() : "";
	}

	/**
	 * Aggiunge i dati degli importi.
	 *
	 * @return la stringa degli importi
	 */
	private String computeImporti() {
		int annoN = annoEsercizio.intValue();
		int anno1 = annoN + 1;
		int anno2 = annoN + 2;
		StringBuilder sb = new StringBuilder();
		
		sb.append("<table id=\"tabellaDettaglioVariazioni\" class=\"table table-bordered table-condensed\">")
				.append("<tr>").append("<th>&nbsp;</th>").append("<th>&nbsp;</th>")
				.append("<th class=\"text-center\">").append(annoN).append("&nbsp;</th>")
				.append("<th class=\"text-center\">").append(anno1).append("&nbsp;</th>")
				.append("<th class=\"text-center\">").append(anno2).append("&nbsp;</th>")
				.append("</tr>");
		appendImportoCompetenza(sb);
		appendComponentiCompetenza(sb);
		appendImportoResidui(sb);
		appendImportoCassa(sb);
		sb.append("</table>");
		return sb.toString();
	}

	/**
	 * Crea le righe ciclando le righe delle componenti
	 * @param sb lo StringBuilder
	 */
	private void appendComponentiCompetenza(StringBuilder sb) {
		List<ElementoComponenteVariazione> listaElementoComponenteVariazione = ElementoComponenteVariazioneFactory.getInstancesFromListeDettagliSui3Anni(
				listaDettaglioVariazioneComponenteImportoCapitolo,
				listaDettaglioVariazioneComponenteImportoCapitolo1,
				listaDettaglioVariazioneComponenteImportoCapitolo2,
				variazioneImportoSingoloCapitolo.getApplicazioneVariazione());
		
		for(ElementoComponenteVariazione ecv : listaElementoComponenteVariazione) {
			sb.append("<tr class=\"componentiCompetenzaDettVar\">")
				.append("<td class=\"componenti-competenza-dett\" rowspan=\"1\">")
				.append(ecv.getTipoComponenteImportiCapitolo().getDescrizione())
				.append("</td>")
				.append("<td class=\"text-center\">")
				.append(ecv.getTipoDettaglioComponenteImportiCapitolo().getDescrizione())
				.append("</td>")
				.append("<td class=\"text-right\">").append(toImporto(ecv.getImportoAnno0())).append("</td>")
				.append("<td class=\"text-right\">").append(toImporto(ecv.getImportoAnno1())).append("</td>")
				.append("<td class=\"text-right\">").append(toImporto(ecv.getImportoAnno2())).append("</td>")
				.append("</tr>");
		}
	}

	/**
	 * Gets the importo competenza.
	 * @return the importoCompetenza
	 */
	private void appendImportoCompetenza(StringBuilder sb) {
		computeImporti(sb, "Competenza",
			toZero(variazioneImportoSingoloCapitolo.getDettaglioVariazioneImporto().getStanziamento()),
			toZero(variazioneImportoSingoloCapitolo.getDettaglioVariazioneImporto().getStanziamento1()),
			toZero(variazioneImportoSingoloCapitolo.getDettaglioVariazioneImporto().getStanziamento2())
		);
	}
	
	/**
	 * Gets the importo residui.
	 * @param sb lo StringBuilder
	 */
	private void appendImportoResidui(StringBuilder sb) {
		computeImporti(sb, "Residui",
			toZero(variazioneImportoSingoloCapitolo.getDettaglioVariazioneImporto().getStanziamentoResiduo()),
			null,
			null
		);
	}

	/**
	 * Gets the importo cassa.
	 * @param sb lo StringBuilder
	 */
	private void appendImportoCassa(StringBuilder sb) {
		computeImporti(sb, "Cassa",
			toZero(variazioneImportoSingoloCapitolo.getDettaglioVariazioneImporto().getStanziamentoCassa()),
			null,
			null
		);
	}

	
	/**
	 * Aggiunge gli importi.
	 *
	 * @param sb lo StringBuilder 
	 * @param nameil nome dell'importo
	 * @param importo0 l'importo da aggiungere per l'anno + 0
	 * @param importo1 l'importo da aggiungere per l'anno + 0
	 * @param importo2 l'importo da aggiungere per l'anno + 0
	 */
	private void computeImporti(StringBuilder sb, String nameRow, BigDecimal importo0, BigDecimal importo1, BigDecimal importo2) {
		String name = nameRow;
		String titleOne = "Stanziamento";
		String titleTwo = "Impegnato";
		
		StringBuilder stanz = new StringBuilder();
		StringBuilder imp = new StringBuilder();
		
		if (nameRow.equals("Competenza")) {
			name = "<a class=\"competenzaTotaleVariazione\">" + nameRow + "</a>";
			stanz.append("<td class=\"text-right\">").append(toImporto(importo0)).append("</td>")
				.append("<td class=\"text-right\">").append(toImporto(importo1)).append("</td>")
				.append("<td class=\"text-right\">").append(toImporto(importo2)).append("</td>");
			imp.append("<td class=\"text-right\"></td>")
				.append("<td class=\"text-right\"></td>")
				.append("<td class=\"text-right\"></td>");
			
		} else if (nameRow.equals("Residui")){
			titleOne = "Effettivi";
			titleTwo = "Presunti";
			stanz.append("<td class=\"text-right\">").append(toImporto(importo0)).append("</td>")
				.append("<td class=\"text-right\">").append(toImporto(importo1)).append("</td>")
				.append("<td class=\"text-right\">").append(toImporto(importo2)).append("</td>");
			imp.append("<td class=\"text-right\"></td>")
				.append("<td class=\"text-right\"></td>")
				.append("<td class=\"text-right\"></td>");
		
		} else if(nameRow.equals("Cassa")){
			titleTwo = "Pagato";
			stanz.append("<td class=\"text-right\">").append(toImporto(importo0)).append("</td>")
				.append("<td class=\"text-right\">").append(toImporto(importo1)).append("</td>")
				.append("<td class=\"text-right\">").append(toImporto(importo2)).append("</td>");
			imp.append("<td class=\"text-right\"></td>")
				.append("<td class=\"text-right\"></td>")
				.append("<td class=\"text-right\"></td>");
		}

		sb.append("<tr>")
			.append("<th rowspan=\"2\" class=\"text-left\" scope=\"col\">").append(name).append("</th>")
			.append("<td class=\"text-center\">").append(titleOne).append("</td>")
			.append(stanz)
			//.append("</tr></td>")
			.append("</tr>")
			.append("<tr>")
			.append("<td class=\"text-center\">").append(titleTwo).append("</td>")
			.append(imp)
			.append("</tr>");
	}
	
	/**
	 * Imposta a zero un BigDecimal se non valorizzato
	 * @param bd il big decimal
	 * @return il valore del bigdecimal ovvero zero
	 */
	private BigDecimal toZero(BigDecimal bd) {
		return bd != null ? bd : BigDecimal.ZERO;
	}
	
	/**
	 * Converte l'importo ion stringa, null-safe
	 * @param db l'importo
	 * @return il testo
	 */
	private String toImporto(BigDecimal db) {
		return db != null ? NumberUtils.toImporto(db) : "";
	}

	@Override
	public int getUid() {
		return variazioneImportoSingoloCapitolo.getUid();
	}


}
