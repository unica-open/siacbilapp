/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siaccecapp.frontend.ui.util.wrapper.quadroeconomico;

import java.io.Serializable;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.ModelWrapper;
import it.csi.siac.siacbilser.model.QuadroEconomico;
import it.csi.siac.siacbilser.model.StatoOperativoQuadroEconomico;

/**
 * Wrapper per il quadroEconomico.
 * 
 * @author Marchino Alessandro
 * @version 1.0.0 - 04/01/2018
 */
public class ElementoQuadroEconomico implements Serializable, ModelWrapper {
	
	/** Per la serializzazione */
	private static final long serialVersionUID = 2340845913038225555L;
	
	private final QuadroEconomico quadroEconomico;
	
	/**
	 * Costruttore di wrap
	 * @param quadroEconomico il quadroEconomico da wrappare
	 */
	public ElementoQuadroEconomico(QuadroEconomico quadroEconomico) {
		this.quadroEconomico = quadroEconomico;
	}

	/**
	 * @return the domString
	 */
	public String getDomString() {
		if(quadroEconomico == null) {
			return "";
		}
		return new StringBuilder()
			.append("<div class=\"accordion-group\">")
				.append("<div class=\"accordion-heading\">")
					.append(computeIntestazioneAccordion())
				.append("</div>")
				.append("<div id=\"quadroEconomico").append(quadroEconomico.getUid()).append("\" class=\"accordion-body collapse\">")
					.append("<div class=\"accordion-inner\">")
						.append(computeBodyAccordionSingoloQuadroEconomico())
						.append(computeButtonInserimentoQuadroEconomicoLivello2())
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
		return new StringBuilder()
				.append("<a class=\"accordion-toggle\" data-parent=\"#accordionQuadroEconomico\" href=\"#QuadroEconomico")
				.append(quadroEconomico.getUid())
				.append("\" data-toggle=\"collapse\" data-uid=\"")
				.append(quadroEconomico.getUid())
				.append("\" data-target=\"#quadroEconomico")
				.append(quadroEconomico.getUid())
				.append("\" data-loaded=\"false\">")
					.append(quadroEconomico.getCodice())
					.append(" - ")
					.append(quadroEconomico.getDescrizione())
					.append(" - ")
					.append(" Stato: ")
					.append(quadroEconomico.getStatoOperativoQuadroEconomico().getDescrizione())
					.append("<span class=\"icon\"></span>")
				.append("</a>")
				.toString();
	}
	
	/**
	 * Calcolo del body dell'accordion sul singolo clasificatore
	 * @return il body dell'accordion
	 */
	private String computeBodyAccordionSingoloQuadroEconomico() {
		StringBuilder sb = new StringBuilder();
		String uidPadre = computeUidPadre();
		sb.append(computeControls(quadroEconomico, null));
		
		if(quadroEconomico.getListaQuadroEconomicoFigli() == null || quadroEconomico.getListaQuadroEconomicoFigli().isEmpty()) {
			return sb.toString();
		}
		
		for(QuadroEconomico qe : quadroEconomico.getListaQuadroEconomicoFigli()) {
			if(qe != null) {
				sb.append(computeControls(qe, uidPadre));
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
				.append(quadroEconomico.getUid())
				.append("\"")
				.toString();
	}

	/**
	 * Calcolo dei controlli
	 * @param quadroEconomico il quadroEconomicoicatore
	 * @param uidPadre il blocco dell'uid padre
	 * @return i controlli
	 */
	private String computeControls(QuadroEconomico quadroEconomico, String uidPadre) {
		if(quadroEconomico == null) {
			// null-safe
			return "";
		}
		// Variabili di utilita'
		boolean hasSpaziature = uidPadre != null;
		int    uidQuadroEconomico = quadroEconomico.getUid();
		String codiceQuadroEconomico = quadroEconomico.getCodice();
		String descrizioneQuadroEconomico = quadroEconomico.getDescrizione();
		String parteQuadroEconomicoDesc = quadroEconomico.getParteQuadroEconomico().getDescrizione();
		
		String statoQuadroEconomicoDesc = quadroEconomico.getStatoOperativoQuadroEconomico() != null ? quadroEconomico.getStatoOperativoQuadroEconomico().getDescrizione() : "";
		String controlsSpan = hasSpaziature ? "span11" : "span12";
		
		StringBuilder sb = new StringBuilder();
		
		if(hasSpaziature) {
			// Inserimento spaziatura			
			// style=\"border-width:1px;\"
			sb.append("<div class=\"box-border\"  >")
				.append("<span class=\"span1\"></span>");
		}
		
		sb
			.append("<div class=\"control-group \"").append(controlsSpan).append("\"\">")
				.append("<label class=\"control-label\" for=\"codiceQuadroEconomico_").append(uidQuadroEconomico).append("\">Codice </label>")
				.append("<div class=\"controls\">")
					
					.append("<input type=\"hidden\" name=\"quadroEconomico.uid\" value=\"").append(uidQuadroEconomico).append("\"/>")
					.append("<input id=\"codiceQuadroEconomico_").append(uidQuadroEconomico).append("\" disabled type=\"text\" name=\"quadroEconomico.codice\" class=\"span3\" value=\"").append(codiceQuadroEconomico).append("\"/>")
					
					.append("<span class=\"alRight\">")
						.append("<label for=\"descrizioneQuadroEconomico_").append(uidQuadroEconomico).append("\" class=\"radio inline\">&nbsp;Descrizione</label>")
					.append("</span>")
					.append("<input id=\"descrizioneQuadroEconomico_").append(uidQuadroEconomico).append("\" type=\"text\" cssClass=\"span4\" disabled value=\"").append(descrizioneQuadroEconomico).append("\" />")
					
					.append("<span class=\"alRight\">")
					.append("<label for=\"parteQuadroEconomico_").append(uidQuadroEconomico).append("\" class=\"radio inline\">&nbsp;Parte</label>")
					.append("</span>")
					.append("<input id=\"parteQuadroEconomico_").append(uidQuadroEconomico).append("\" type=\"text\" class=\"span1\" disabled value=\"").append(parteQuadroEconomicoDesc).append("\" />")
					
					
					.append("<span class=\"alRight\">")
						.append("<label for=\"statoQuadroEconomico_").append(uidQuadroEconomico).append("\" class=\"radio inline\">&nbsp;Stato</label>")
					.append("</span>")
					.append("<input id=\"statoQuadroEconomico_").append(uidQuadroEconomico).append("\" type=\"text\" class=\"span1\" disabled value=\"").append(statoQuadroEconomicoDesc).append("\" />");
		// Le azioni sono presenti solo se il quadroEconomico e' valido
		if(StatoOperativoQuadroEconomico.VALIDO.equals(quadroEconomico.getStatoOperativoQuadroEconomico())) {
			sb.append("<div class=\"btn-group pull-right\">")
				.append("<button data-toggle=\"dropdown\" class=\"btn dropdown-toggle pull-right\">Azioni<span class=\"caret\"></span></button>'")
				.append("<ul class=\"dropdown-menu pull-right\">")
					.append("<li><a class=\"aggiornaQuadroEconomico\" data-uid-quadro-economico=\"").append(uidQuadroEconomico).append("\" ").append(uidPadre).append(">aggiorna</a></li>")
					.append("<li><a class=\"annullaQuadroEconomico\"  data-uid-quadro-economico=\"").append(uidQuadroEconomico).append("\" ").append(uidPadre).append(">annulla</a></li>")
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
	 * Calcolo dei pulsanti di inserimento del quadroEconomico di secondo livello
	 * @return i pulsanti
	 */
	private String computeButtonInserimentoQuadroEconomicoLivello2() {
		if(!StatoOperativoQuadroEconomico.VALIDO.equals(quadroEconomico.getStatoOperativoQuadroEconomico())) {
			return "";
		}
		int uid = quadroEconomico.getUid();
		return new StringBuilder()
			.append("<p>")
				.append("<button type=\"button\" class=\"btn btn-secondary pull-right inserisciFiglio\" data-uid-padre=\"").append(uid).append("\" id=\"pulsanteInserisciQuadroEconomicoFiglio_").append(uid).append("\">")
					.append("Inserisci secondo livello&nbsp;<i class=\"icon-spin icon-refresh spinner\" id=\"SPINNER_pulsanteInserisciQuadroEconomicoFiglio_").append(uid).append("\"></i>")
				.append("</button>")
			.append("</p>")
			.append("<div class=\"clear\"></div>")
			.toString();
	}

	@Override
	public int getUid() {
		return quadroEconomico != null ? quadroEconomico.getUid() : 0;
	}
	
}





