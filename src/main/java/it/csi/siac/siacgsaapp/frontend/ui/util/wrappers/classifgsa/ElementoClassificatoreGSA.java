/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacgsaapp.frontend.ui.util.wrappers.classifgsa;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacgenser.model.ClassificatoreGSA;
import it.csi.siac.siacgenser.model.StatoOperativoClassificatoreGSA;

/**
 * Wrapper per il classificatore GSA.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/01/2018
 */
public class ElementoClassificatoreGSA implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2340845913038225555L;
	
	private final ClassificatoreGSA classificatoreGSA;
	
	/**
	 * Costruttore di wrap
	 * @param classificatoreGSA il classificatore da wrappare
	 */
	public ElementoClassificatoreGSA(ClassificatoreGSA classificatoreGSA) {
		this.classificatoreGSA = classificatoreGSA;
	}

	/**
	 * @return the domString
	 */
	public String getDomString() {
		if(classificatoreGSA == null) {
			return "";
		}
		return new StringBuilder()
			.append("<div class=\"accordion-group\">")
				.append("<div class=\"accordion-heading\">")
					.append(computeIntestazioneAccordion())
				.append("</div>")
				.append("<div id=\"classifGSA").append(classificatoreGSA.getUid()).append("\" class=\"accordion-body collapse\">")
					.append("<div class=\"accordion-inner\">")
						.append(computeBodyAccordionSingoloClassificatore())
						.append(computeButtonInserimentoClassificatoreLivello2())
					.append("</div>")
				.append("</div>")
			.append("</div>")
			.toString();
	}
	
	/**
	 * Calcolo dell'intestazione dell'accordion
	 * @return l'intestazione dell'accordion
	 */
	private String computeIntestazioneAccordion() {
		// classificatoreGSA e' non-null
		return new StringBuilder()
				.append("<a class=\"accordion-toggle\" data-parent=\"#accordionClassifGSA\" href=\"#classifGSA")
				.append(classificatoreGSA.getUid())
				.append("\" data-toggle=\"collapse\" data-uid=\"")
				.append(classificatoreGSA.getUid())
				.append("\" data-target=\"#classifGSA")
				.append(classificatoreGSA.getUid())
				.append("\" data-loaded=\"false\">")
					.append(classificatoreGSA.getCodice())
					.append(" - ")
					.append(classificatoreGSA.getDescrizione())
					.append(" - ")
					.append(" Stato: ")
					.append(classificatoreGSA.getStatoOperativoClassificatoreGSA().getDescrizione())
					.append("<span class=\"icon\"></span>")
				.append("</a>")
				.toString();
	}
	
	/**
	 * Calcolo del body dell'accordion sul singolo clasificatore
	 * @return il body dell'accordion
	 */
	private String computeBodyAccordionSingoloClassificatore() {
		// classificatoreGSA e' non-null
		StringBuilder sb = new StringBuilder();
		String uidPadre = computeUidPadre();
		sb.append(computeControls(classificatoreGSA, null));
		
		if(classificatoreGSA.getListaClassificatoriGSAFigli() == null || classificatoreGSA.getListaClassificatoriGSAFigli().isEmpty()) {
			return sb.toString();
		}
		
		for(ClassificatoreGSA classif : classificatoreGSA.getListaClassificatoriGSAFigli()) {
			if(classif != null) {
				sb.append(computeControls(classif, uidPadre));
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Calcolo dell'uid del padre
	 * @return l'uid del padre
	 */
	private String computeUidPadre() {
		return new StringBuilder()
				.append("data-uid-padre=\"")
				.append(classificatoreGSA.getUid())
				.append("\"")
				.toString();
	}

	/**
	 * Calcolo dei controlli
	 * @param classif il classificatore
	 * @param uidPadre il blocco dell'uid padre
	 * @return i controlli
	 */
	private String computeControls(ClassificatoreGSA classif, String uidPadre) {
		if(classif == null) {
			// null-safe
			return "";
		}
		// Variabili di utilita'
		boolean hasSpaziature = uidPadre != null;
		int uidClass = classif.getUid();
		String codiceClass = classif.getCodice();
		String descrizioneClass = classif.getDescrizione();
		String statoClassDesc = classif.getStatoOperativoClassificatoreGSA() != null ? classif.getStatoOperativoClassificatoreGSA().getDescrizione() : "";
		String controlsSpan = hasSpaziature ? "span11" : "span12";
		
		StringBuilder sb = new StringBuilder();
		
		if(hasSpaziature) {
			// Inserimento spaziatura
			sb.append("<div>")
				.append("<span class=\"span1\"></span>");
		}
		
		sb
			.append("<div class=\"control-group \"").append(controlsSpan).append("\"\">")
				.append("<label class=\"control-label\" for=\"codiceClassificatore_").append(uidClass).append("\">Codice </label>")
				.append("<div class=\"controls\">")
					.append("<input type=\"hidden\" name=\"classificatoreGSA.uid\" value=\"").append(uidClass).append("\"/>")
					.append("<input id=\"codiceClassificatore_").append(uidClass).append("\" disabled type=\"text\" name=\"classificatoreGSA.codice\" value=\"").append(codiceClass).append("\"/>")
					.append("<span class=\"alRight\">")
						.append("<label for=\"descrizioneClassificatore_").append(uidClass).append("\" class=\"radio inline\">Descrizione </label>")
					.append("</span>")
					.append("<input id=\"descrizioneClassificatore_").append(uidClass).append("\" type=\"text\" disabled value=\"").append(descrizioneClass).append("\" />")
					.append("<span class=\"alRight\">")
						.append("<label for=\"statoClassificatore_").append(uidClass).append("\" class=\"radio inline\">Stato </label>")
					.append("</span>")
					.append("<input id=\"statoClassificatore_").append(uidClass).append("\" type=\"text\" disabled value=\"").append(statoClassDesc).append("\" />");
		// Le azioni sono presenti solo se il classificatore e' valido
		if(StatoOperativoClassificatoreGSA.VALIDO.equals(classif.getStatoOperativoClassificatoreGSA())) {
			sb.append("<div class=\"btn-group pull-right\">")
				.append("<button data-toggle=\"dropdown\" class=\"btn dropdown-toggle pull-right\">Azioni<span class=\"caret\"></span></button>'")
				.append("<ul class=\"dropdown-menu pull-right\">")
					.append("<li><a class=\"aggiornaClassificatore\" data-uid-classificatore=\"").append(uidClass).append("\" ").append(uidPadre).append(">aggiorna</a></li>")
					.append("<li><a class=\"annullaClassificatore\" data-uid-classificatore=\"").append(uidClass).append("\" ").append(uidPadre).append(">annulla</a></li>")
				.append("</ul>")
			.append("</div>");
		}
		
		sb.append("</div>")
		.append("</div>");
		
		if(hasSpaziature) {
			sb.append("</div>");
		}
		
		return sb.toString();
	}

	/**
	 * Calcolo dei pulsanti di inserimento del classificatori di secondo livello
	 * @return i pulsanti
	 */
	private String computeButtonInserimentoClassificatoreLivello2() {
		if(!StatoOperativoClassificatoreGSA.VALIDO.equals(classificatoreGSA.getStatoOperativoClassificatoreGSA())) {
			return "";
		}
		int uid = classificatoreGSA.getUid();
		return new StringBuilder()
			.append("<p>")
				.append("<button type=\"button\" class=\"btn btn-secondary pull-right inserisciFiglio\" data-uid-padre=\"").append(uid).append("\" id=\"pulsanteInserisciClassificatoreFiglio_").append(uid).append("\">")
					.append("Inserisci secondo livello&nbsp;<i class=\"icon-spin icon-refresh spinner\" id=\"SPINNER_pulsanteInserisciClassificatoreFiglio_").append(uid).append("\"></i>")
				.append("</button>")
			.append("</p>")
			.append("<div class=\"clear\"></div>")
			.toString();
	}

	@Override
	public int getUid() {
		return classificatoreGSA != null ? classificatoreGSA.getUid() : 0;
	}
	
}
