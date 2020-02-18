<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<h4>Disponibilit&agrave;</h4>
<table class="table table-hover table-bordered">
	<thead>
		<tr class="row-slim-bottom">
			<th></th>
			<th class="text-center" colspan="3">Competenza</th>
			<th class="text-center">Residui</th>
		</tr>
		<tr class="row-slim-top">
			<th class="span2">Capitolo</th>
			<th class="span2p5 text-center">${annoEsercizioInt + 0}</th>
			<th class="span2p5 text-center">${annoEsercizioInt + 1}</th>
			<th class="span2p5 text-center">${annoEsercizioInt + 2}</th>
			<th class="span2p5"></th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td><strong>Disponibilit&agrave; a variare</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.disponibilitaVariare"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno1.disponibilitaVariare"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno2.disponibilitaVariare"/></td>
			<td></td>
		</tr>
		<tr>
			<td colspan="5" class="row-small"></td>
		</tr>

		<tr>
			<th colspan="5">Impegni</th>
		</tr>
		<tr>
			<td><strong><abbr title="Numero">N.</abbr> totale</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.numeroImpegni"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno1.numeroImpegni"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno2.numeroImpegni"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneResiduo.numeroImpegni"/></td>
		</tr>
		<tr>
			<td><strong>Impegnato</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.impegnato"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno1.impegnato"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno2.impegnato"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneResiduo.impegnato"/></td>
		</tr>
		<tr>
			<td><strong>- di cui da riaccertamento</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.impegnatoDaRiaccertamento"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno1.impegnatoDaRiaccertamento"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno2.impegnatoDaRiaccertamento"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneResiduo.impegnatoDaRiaccertamento"/></td>
		</tr>
		<tr>
			<td><strong>- di cui da esercizi prec.</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.impegnatoDaEserciziPrec"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno1.impegnatoDaEserciziPrec"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno2.impegnatoDaEserciziPrec"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneResiduo.impegnatoDaEserciziPrec"/></td>
		</tr>
		<!-- INIZIO FILIPPO SIAC-6899 -->
		<tr>
			<td><strong>- finanziato da FPV</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.finanziatoDaFPV"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno1.finanziatoDaFPV"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno2.finanziatoDaFPV"/></td>
			<td></td>
		</tr>
		
		<tr>
			<td><strong>- finanziato da Avanzo</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.finanziatoDaAvanzo"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno1.finanziatoDaAvanzo"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno2.finanziatoDaAvanzo"/></td>
			<td></td>
		</tr>
		<!-- FINE FILIPPO SIAC-6899 -->
		<s:if test="prenotazioneAbilitato">
			<tr>
				<td><strong>- di cui quota di prenotazione</strong>
				<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.impegnatoDaPrenotazione"/></td>
				<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno1.impegnatoDaPrenotazione"/></td>
				<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno2.impegnatoDaPrenotazione"/></td>
				<td></td>
			</tr>
		</s:if>
		<tr>
			<td><strong>Disponibilit&agrave; ad impegnare</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.disponibilitaImpegnare"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno1.disponibilitaImpegnare"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno2.disponibilitaImpegnare"/></td>
			<td></td>
		</tr>
		<s:if test="faseProvvisoria">
			<tr>
				<td><strong>12° in regime provvisorio</strong>
				<td class="text-right"><s:property value="regimeProvvisorio0"/></td>
				<td class="text-right"><s:property value="regimeProvvisorio1"/></td>
				<td class="text-right"><s:property value="regimeProvvisorio2"/></td>
				<td></td>
			</tr>
		</s:if>
		<tr>
			<td colspan="5" class="row-small"></td>
		</tr>

		<tr>
			<th colspan="5">Liquidazioni</th>
		</tr>
		<tr>
			<td><strong><abbr title="Numero">N.</abbr> totale</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.numeroLiquidazioni"/></td>
			<td></td>
			<td></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneResiduo.numeroLiquidazioni"/></td>
		</tr>
		<tr>
			<td><strong>Liquidato</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.liquidato"/></td>
			<td></td>
			<td></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneResiduo.liquidato"/></td>
		</tr>
		<s:if test="prenotazioneAbilitato">
			<tr>
				<td><strong>- di cui da prenotazioni</strong>
				<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.liquidatoDaPrenotazioni"/></td>
				<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno1.liquidatoDaPrenotazioni"/></td>
				<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno2.liquidatoDaPrenotazioni"/></td>
				<td></td>
			</tr>
		</s:if>
		<tr>
			<td colspan="5" class="row-small"></td>
		</tr>

		<tr>
			<th colspan="5">Ordinativi</th>
		</tr>
		<tr>
			<td><strong><abbr title="Numero">N.</abbr> totale</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.numeroOrdinativi"/></td>
			<td></td>
			<td></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneResiduo.numeroOrdinativi"/></td>
		</tr>
		<tr>
			<td><strong>Pagato</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.pagato"/></td>
			<td></td>
			<td></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneResiduo.pagato"/></td>
		</tr>
		<tr>
			<td><strong>Disponibilit&agrave; di cassa</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.disponibilitaPagare"/></td>
			<td></td>
			<td></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneResiduo.disponibilitaPagare"/></td>
		</tr>

	</tbody>
</table>