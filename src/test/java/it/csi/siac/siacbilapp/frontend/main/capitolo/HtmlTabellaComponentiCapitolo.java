/*
*SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
*SPDX-License-Identifier: EUPL-1.2
*/
package it.csi.siac.siacbilapp.frontend.main.capitolo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaComponenteTabellaImportiCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaDettaglioComponenteTabellaImportiCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaDettaglioImportoTabellaImportiCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaDettaglioTabellaImportiCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaImportoTabellaImportiCapitolo;
import it.csi.siac.siacbilapp.frontend.ui.util.wrappers.capitolo.aggiornamento.RigaTabellaImportiCapitolo;

public class HtmlTabellaComponentiCapitolo {
	
	
	/**
	 * utility
	 * 
	 * */
	
	
	public static String getReadonlyAttr(boolean condition) {
		return condition? "readonly" : "";
	}
	
	private static String getFirstPartTable(Integer annoBilancio, String idTabella, String header, boolean matitina) {
		 // BuildMyString.com generated code. Please enjoy your string responsibly.

		StringBuilder sb = new StringBuilder();

		sb.append("<table class=\"table table-condensed table-bordered\"").append(StringUtils.isEmpty(idTabella)? "" : ("id=\"" + idTabella+"\"")).append(">");
		sb.append("	<tbody>");
		sb.append("		<tr>");
		sb.append("			<th>" + StringUtils.defaultString(header) + "</th>");
		sb.append("			<th class=\"text-right\">&nbsp;</th>");
		sb.append("			<th class=\"text-right\">" + Integer.valueOf(annoBilancio.intValue() - 1).toString() + " </th>");
		sb.append("			<th class=\"text-right\">Residui " + annoBilancio.toString() +"</th>");
		sb.append("			<th class=\"text-right\">" + annoBilancio.toString() +"</th>");
		sb.append("			<th class=\"text-right\">"+ Integer.valueOf(annoBilancio.intValue() + 1).toString() +"</th>");
		sb.append("			<th class=\"text-right\">"+ Integer.valueOf(annoBilancio.intValue() + 2).toString() +"</th>");
		sb.append("			<th class=\"text-right\"> &gt;" +Integer.valueOf(annoBilancio.intValue() +2).toString() + "</th>");
		if(matitina) {
			sb.append("<th>&nbsp;</th>");
		}
		sb.append("		</tr>");

		return sb.toString();
	}
	
	public static String getLastPartTable() {
	 // BuildMyString.com generated code. Please enjoy your string responsibly.

	StringBuilder sb = new StringBuilder();

	sb.append("    </tbody>");
	sb.append("</table>");

	return sb.toString();
}
	
	private static StringBuilder getHtmlRiga(RigaTabellaImportiCapitolo riga, boolean editabileResiduo,
			boolean editabileAnnoBilancio, boolean editabileAnnoPiuUnoPiuDue, boolean firstCell,
			RigaDettaglioTabellaImportiCapitolo rd) {
		StringBuilder rigaDettaglio = new StringBuilder();
		rigaDettaglio.append("<tr>");
		if(firstCell) {
			rigaDettaglio.append("<td class=\"componenti-competenza\" id=\"\" rowspan=\"" + riga.getSottoRigheSize() +  "\">");
			rigaDettaglio.append(riga.getDescrizioneImporto());
			rigaDettaglio.append("</td>");
//					firstCell = false;
		}

		rigaDettaglio.append("    <td style=\"text-align:center;\">");
		rigaDettaglio.append(rd.getTipoDettaglioComponenteDesc());
		rigaDettaglio.append("    </td>");
		rigaDettaglio.append("		<td style=\"position:relative\">");
		rigaDettaglio.append("			<input type=\"text\" readonly=\"true\" name=\"\" value=\"" + rd.getFormattedImportoAnniPrecedenti() +"\" class=\"custom-new-component soloNumeri text-right\" required=\"\"></td>");
		rigaDettaglio.append("		<td style=\"position:relative;width: 30px;\">");
		rigaDettaglio.append("			<input type=\"text\" "+ getReadonlyAttr( !editabileResiduo || !rd.isTipoDettaglioEditabile()) +  " name=\"\" value=\" " + rd.getFormattedImportoResiduoAnno0() +"\" class=\"custom-new-component soloNumeri text-right\" required=\"\">");
		rigaDettaglio.append("		</td>");
		rigaDettaglio.append("		<td style=\"position:relative\">");
		rigaDettaglio.append("			<input type=\"text\" "+ getReadonlyAttr(!editabileAnnoBilancio || !rd.isTipoDettaglioEditabile()) +  " name=\"\" value=\"" + rd.getFormattedImportoAnno0() + "\" class=\"custom-new-component soloNumeri text-right\" required=\"\">");
		rigaDettaglio.append("		</td>");
		rigaDettaglio.append("		<td style=\"position:relative\">");
		rigaDettaglio.append("			<input type=\"text\" " + getReadonlyAttr(!editabileAnnoPiuUnoPiuDue || !rd.isTipoDettaglioEditabile()) +" name=\"\" value=\""+ rd.getFormattedImportoAnno1() + "\" class=\"custom-new-component soloNumeri text-right\" required=\"\">");
		rigaDettaglio.append("		</td>");
		rigaDettaglio.append("		<td style=\"position:relative\">");
		rigaDettaglio.append("			<input type=\"text\" "+ getReadonlyAttr(!editabileAnnoPiuUnoPiuDue || !rd.isTipoDettaglioEditabile()) +" name=\"\" value=\""+ rd.getFormattedImportoAnno2()+  "\" class=\"custom-new-component soloNumeri text-right\" required=\"\">");
		rigaDettaglio.append("		</td>");
		rigaDettaglio.append("		<td style=\"position:relative\">");
		rigaDettaglio.append("			<input type=\"text\" readonly=\"true\" name=\"\" value=\""+ rd.getFormattedImportoAnniSuccessivi() + "\" class=\"custom-new-component soloNumeri text-right\" required=\"\">");
		rigaDettaglio.append("		</td>");
		rigaDettaglio.append("</tr>");
		return rigaDettaglio;
	}

	
	
	private static StringBuilder getHtmlRowCons(
			RigaTabellaImportiCapitolo riga, boolean firstTd, RigaDettaglioTabellaImportiCapitolo rd) {
		StringBuilder rigaDettaglio = new StringBuilder();
		rigaDettaglio.append("		<tr class=\"" + rd.getTrCssClass()).append("\">");
		if(firstTd) {
			rigaDettaglio.append("			<th class=\"stanziamenti-titoli\"  ")
			.append("rowspan=\"" + riga.getSottoRigheSize() + "\">")
			.append(riga.getDescrizioneImporto() + "</th>");
		}
		
		rigaDettaglio.append("<td style=\"text-align:center;\">" + rd.getTipoDettaglioComponenteImportiCapitolo().getDescrizione() + "</td>");
		rigaDettaglio.append("<td style=\"text-align:right;\">");
		rigaDettaglio.append(rd.getFormattedImportoAnniPrecedenti());
		rigaDettaglio.append("</td>");
		rigaDettaglio.append("<td style=\"text-align:right;\">");
		rigaDettaglio.append(rd.getFormattedImportoResiduoAnno0());
		rigaDettaglio.append("</td>");
		rigaDettaglio.append("<td style=\"text-align:right;\">");
		rigaDettaglio.append(rd.getFormattedImportoAnno0());
		rigaDettaglio.append("</td>");
		rigaDettaglio.append("<td style=\"text-align:right;\">");
		rigaDettaglio.append(rd.getFormattedImportoAnno1());
		rigaDettaglio.append("</td>");
		rigaDettaglio.append("<td style=\"text-align:right;\">");
		rigaDettaglio.append(rd.getFormattedImportoAnno2());
		rigaDettaglio.append("</td>");
		rigaDettaglio.append("<td style=\"text-align:right;\">");
		rigaDettaglio.append(rd.getFormattedImportoAnniSuccessivi());
		rigaDettaglio.append("</td>");
		//questa cosa non mi piace, ci sar� di sicuro un modo migliore!!!!
		if(firstTd) {
			rigaDettaglio.append("<td style=\"text-align:left;\"").append(firstTd? (" rowspan=\""+ riga.getSottoRigheSize() + " \"") : "" ).append(">");
			if(riga.getRigaModificabilePerTipoImporto() && rd.isTipoDettaglioEditabile()) {
				rigaDettaglio.append("<a href=\"#editStanziamenti\" title=\"modifica gli importi\" role=\"button\" onclick=\"VariazioniImporti.apriModaleComponenti(0,2020, 2);\" data-toggle=\"modal\">" + 
						"	<i class=\"icon-pencil icon-2x\">" + 
						"		<span class=\"nascosto\">modifica</span>" + 
						"	</i>" + 
						" </a>");
			}
			if(riga.getRigaEliminabile() && rd.isTipoDettaglioEliminabile()) {
				rigaDettaglio.append("<a href=\"#msgElimina\" title=\"elimina\" role=\"button\" onclick=\"VariazioniImporti.eliminaComponentiImportiCapitolo(0);\" data-toggle=\"modal\" style=\"padding-left:15px;\">"
						+ "     <i class=\"icon-trash icon-2x\">"
						+ "       <span class=\"nascosto\">elimina</span>"
						+ "     </i>"
						+ "</a>");
				
			}
			rigaDettaglio.append("</td>");
		}
		
		rigaDettaglio.append("</tr>");
		return rigaDettaglio;
	}
	
	/***
	 * BUSINESS
	 * 
	 * */
	
	public static String getTableAggiornamentoComponenteText(List<RigaComponenteTabellaImportiCapitolo> rigas, Integer annoBilancio) {
		String idTabella="";
		String titoloPrimaCella ="Componente";
		StringBuilder table = new StringBuilder().append(getFirstPartTable(annoBilancio, idTabella, titoloPrimaCella, false));
		for(RigaComponenteTabellaImportiCapitolo riga: rigas){
			
			
			boolean editabileResiduo = riga.getImportoEditabileCellaResiduo();
			boolean editabileAnnoBilancio = riga.getImportoEditabileCellaAnnoBilancio();
			boolean editabileAnnoPiuUnoPiuDue = riga.getImportoEditabileCelleAnnoBilancioPiuUnoPiuDue();
			boolean firstCell = true;
			
			for (RigaDettaglioComponenteTabellaImportiCapitolo rd : riga.getSottoRighe()) {
				StringBuilder rigaDettaglio = getHtmlRiga(riga, editabileResiduo, editabileAnnoBilancio,
												editabileAnnoPiuUnoPiuDue, firstCell, rd);
				firstCell=false;

				table.append(rigaDettaglio.toString());
			}
		}
		
		
		table.append(getLastPartTable());
		return table.toString();
	}

	
	
	/**
	 * BUSINESS
	 * 
	 * */	
	public static String getTableAggiornamentoImportoText(List<RigaImportoTabellaImportiCapitolo> rigas, Integer annoBilancio) {
		String idTabella="";
		String titoloPrimaCella ="Componente";
		StringBuilder table = new StringBuilder().append(getFirstPartTable(annoBilancio, idTabella, titoloPrimaCella, false));
		for(RigaImportoTabellaImportiCapitolo riga: rigas){
			
			
			boolean editabileResiduo = riga.getImportoEditabileCellaResiduo();
			boolean editabileAnnoBilancio = riga.getImportoEditabileCellaAnnoBilancio();
			boolean editabileAnnoPiuUnoPiuDue = riga.getImportoEditabileCelleAnnoBilancioPiuUnoPiuDue();
			boolean firstCell = true;
			
			for (RigaDettaglioImportoTabellaImportiCapitolo rd : riga.getSottoRighe()) {
				StringBuilder rigaDettaglio = getHtmlRiga(riga, editabileResiduo, editabileAnnoBilancio,
												editabileAnnoPiuUnoPiuDue, firstCell, rd);
				firstCell=false;

				table.append(rigaDettaglio.toString());
			}
		}
		
		
		table.append(getLastPartTable());
		return table.toString();
	}
	
	public static String getTableConsultazioneComponenteText(List<RigaComponenteTabellaImportiCapitolo> rigas, Integer annoBilancio) {
		StringBuilder table = new StringBuilder().append(getFirstPartTable(annoBilancio, "tabellaStanziamentiTotaliComponenti",null, true));
		for(RigaComponenteTabellaImportiCapitolo riga: rigas){
			boolean firstTd = true;
			for (RigaDettaglioComponenteTabellaImportiCapitolo rd : riga.getSottoRighe()) {
				StringBuilder rigaDettaglio = getHtmlRowCons( riga, firstTd, rd);
				firstTd = false;
				table.append(rigaDettaglio.toString());
			}
		}
		
		
		table.append(getLastPartTable());
		return table.toString();
	}
	
	
	public static String getTableConsultazioneImportoText(List<RigaImportoTabellaImportiCapitolo> rigas, Integer annoBilancio) {
		StringBuilder table = new StringBuilder().append(getFirstPartTable(annoBilancio, "tabellaStanziamentiTotaliComponenti",null, true));
		for(RigaImportoTabellaImportiCapitolo riga: rigas){
			boolean firstTd = true;
			for (RigaDettaglioImportoTabellaImportiCapitolo rd : riga.getSottoRighe()) {
				StringBuilder rigaDettaglio =  getHtmlRowCons(riga, firstTd, rd);
				firstTd = false;
				table.append(rigaDettaglio.toString());
			}
		}
		
		
		table.append(getLastPartTable());
		return table.toString();
	}

//	
//	
//	/**********************************************
//	 * 
//	 * UTILITY CREAZIONE TABELLA (SOLO PER I TEST)
//	 * 
//	 * *********************************************/
//	
//	
//	public static String getRowagg(int sottoRigheSize, String descrizioneTipoComponente) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("		<tr>");
//		sb.append("			<td class=\"componenti-competenza\" id=\"\" rowspan=\"" + sottoRigheSize +  "\">" + descrizioneTipoComponente + "</td>");
//		return sb.toString();
//	}
//	
//	public static String getRowCons(int sottoRigheSize, String descrizioneTipoComponente) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("		<tr class=\"componentiCompetenzaRow previsione-default\" style=\"display: table-row;\">");
//		sb.append("			<td class=\"componenti-competenza\" id=\"tipoComModale0\" rowspan=\"" + sottoRigheSize +  "\">" + descrizioneTipoComponente + "</td>");
//		return sb.toString();
//	}
//	
//	
//	public static String getDettaglioTdsInput(RigaDettaglioTabellaImportiCapitolo rd, boolean componenteModificabile) {
//		boolean readonlyTriennio = !componenteModificabile || !rd.isTipoDettaglioEditabile();
//		StringBuilder sb = new StringBuilder();
//
//		sb.append("<td style=\"text-align:center;\">" + rd.getTipoDettaglioComponenteDesc() + "</td>");
//		sb.append("				<td style=\"position:relative\">");
//		sb.append("					<input id=\"detAnnoPrecImp0\" type=\"text\" readonly=\"true\" name=\"\" value=\"" + rd.getFormattedImportoAnniPrecedenti() +"\" class=\"custom-new-component soloNumeri text-right\" required=\"\"></td>");
//		sb.append("				<td style=\"position:relative;width: 30px;\">");
//		sb.append("					<input id=\"detAnnoResImp0\" type=\"text\" readonly=\"true\" name=\"\" value=\" " + rd.getFormattedImportoResiduoAnno0() +"\" class=\"custom-new-component soloNumeri text-right\" required=\"\">");
//		sb.append("				</td>");
//		sb.append("				<td style=\"position:relative\">");
//		sb.append("					<input id=\"detAnno0Imp0\" type=\"text\" "+ getReadonlyAttr(readonlyTriennio) +  " name=\"\" value=\"" + rd.getFormattedImportoAnno0() + "\" class=\"custom-new-component soloNumeri text-right\" required=\"\">");
//		sb.append("				</td>");
//		sb.append("				<td style=\"position:relative\">");
//		sb.append("					<input id=\"detAnno1Imp0\" type=\"text\" " + getReadonlyAttr(readonlyTriennio) +" name=\"\" value=\""+ rd.getFormattedImportoAnno1() + "\" class=\"custom-new-component soloNumeri text-right\" required=\"\">");
//		sb.append("				</td>");
//		sb.append("				<td style=\"position:relative\">");
//		sb.append("					<input id=\"detAnno2Imp0\" type=\"text\" "+ getReadonlyAttr(readonlyTriennio) +" name=\"\" value=\""+ rd.getFormattedImportoAnno2()+  "\" class=\"custom-new-component soloNumeri text-right\" required=\"\">");
//		sb.append("				</td>");
//		sb.append("				<td style=\"position:relative\">");
//		sb.append("					<input id=\"detAnnoSuccImp0\" type=\"text\" readonly=\"true\" name=\"\" value=\""+ rd.getFormattedImportoAnniSuccessivi() + "\" class=\"custom-new-component soloNumeri text-right\" required=\"\">");
//		sb.append("				</td>");
//		sb.append("				</tr><tr>");
//		return sb.toString();
//	}
//	
//
//	
//	
//	public static String getTableConsultazioneComponenteTextOOLD(List<RigaComponenteTabellaImportiCapitolo> rigas, Integer annoBilancio) {
//		StringBuilder table = new StringBuilder().append(getFirstPartTable(annoBilancio, "tabellaStanziamentiTotaliComponenti",null,  true));
//		for(RigaComponenteTabellaImportiCapitolo riga: rigas){
//			getRowsFromRiga(table, riga);
////			table.append(getRowCons(riga.getSottoRigheSize(), riga.getDescrizioneTipoComponente()));
////			for (RigaDettaglioComponenteImportoCapitolo rd : riga.getSottoRighe()) {
////				//TODO: controllare editabilita' componenti
////				table.append(getDettaglioTdsCons(rd, riga.isComponenteEditabile(),  riga.isComponenteEliminabile()));
////			}
//		}
//		
//		
//		table.append(getLastPartTable());
//		return table.toString();
//	}
//	
//	public static String getTableConsultazioneImportoText2(List<RigaImportoTabellaImportiCapitolo> rigas, Integer annoBilancio) {
//		StringBuilder table = new StringBuilder().append(getFirstPartTable(annoBilancio, "tabellaStanziamentiTotaliComponenti",null, true));
//		for(RigaImportoTabellaImportiCapitolo riga: rigas){
//			getRowsFromRiga(table, riga);
////			table.append(getRowCons(riga.getSottoRigheSize(), riga.getDescrizioneTipoComponente()));
////			for (RigaDettaglioComponenteImportoCapitolo rd : riga.getSottoRighe()) {
////				//TODO: controllare editabilita' componenti
////				table.append(getDettaglioTdsCons(rd, riga.isComponenteEditabile(),  riga.isComponenteEliminabile()));
////			}
//		}
//		
//		
//		table.append(getLastPartTable());
//		return table.toString();
//	}
//	
//	public static void getRowsFromRiga(StringBuilder table, RigaImportoTabellaImportiCapitolo riga) {
////		StringBuilder table = new StringBuilder();
//		boolean firstTd = true;
//		for (RigaDettaglioImportoTabellaImportiCapitolo rd : riga.getSottoRighe()) {
//			table.append("		<tr class=\"" + rd.getTrCssClass()+ " " ).append(rd.isStanziamento()? rd.getTrCssClass() : "").append("\">");
//			if(firstTd) {
//				table.append("			<th class=\"stanziamenti-titoli\"  ")
//				.append("rowspan=\"" + riga.getSottoRigheSize() + "\">")
//				.append(riga.getDescrizioneImporto() + "</th>");
//				firstTd = false;
//			}
//			
//			//TODO: controllare editabilita' componenti
//			table.append(getDettaglioTdsCons(rd, rd.isTipoDettaglioEditabile(), false));
//			table.append("</tr>");
//		}
//	}
//
//	public static void getRowsFromRiga(StringBuilder table, RigaComponenteTabellaImportiCapitolo riga) {
////		StringBuilder table = new StringBuilder();
//		boolean firstTd = true;
//		for (RigaDettaglioComponenteTabellaImportiCapitolo rd : riga.getSottoRighe()) {
//			table.append("		<tr class=\"componentiCompetenzaRow " ).append(rd.isStanziamento()? riga.getTrCssClass() : "").append("\" style=\"display: table-row;\">");
//			if(firstTd) {
//				table.append("			<td class=\"componenti-competenza\" id=\"tipoComModale0\" ")
//				.append("rowspan=\"" + riga.getSottoRigheSize() + "\">")
//				.append(riga.getDescrizioneImporto() + "</td>");
//				firstTd = false;
//			}
//			
//			//TODO: controllare editabilita' componenti
//			table.append(getDettaglioTdsCons(rd, riga.isComponenteEditabile(),  riga.isComponenteEliminabile()));
//		}
//	}
//
//	
//	
//	
//	public static String getTableAggiornamentoComponenteTextOld(List<RigaComponenteTabellaImportiCapitolo> rigas, Integer annoBilancio) {
//		StringBuilder table = new StringBuilder().append(getFirstPartTable(annoBilancio, "","Componente", false));
//		for(RigaComponenteTabellaImportiCapitolo riga: rigas){
//			table.append(getRowagg(riga.getSottoRigheSize(), riga.getDescrizioneImporto()));
//			for (RigaDettaglioComponenteTabellaImportiCapitolo rd : riga.getSottoRighe()) {
//				//TODO: controllare editabilita' componenti
//				table.append(getDettaglioTdsInput(rd, true));
//			}
//		}
//	
//	
//	table.append(getLastPartTable());
//	return table.toString();
//	}
	
}
