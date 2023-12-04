<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:hidden id="HIDDEN_UidCapitolo" name="uidCapitolo" />
<s:hidden id="HIDDEN_UidPrevisioneImpegnatoAccertato" name="previsioneImpegnatoAccertato.uid" />
<table class="table table-hover table-bordered">
	<thead>
		<tr class="row-slim-bottom">
			<th scope="col" colspan="3">&nbsp;</th>
			<th scope="col" colspan="3" class="text-center">${annoEsercizioInt + 0}</th>
			<th scope="col" colspan="3" class="text-center">${annoEsercizioInt + 1}</th>
			<th scope="col" colspan="3" class="text-center">${annoEsercizioInt + 2}</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<th colspan="3">Competenza</th>
			<td colspan="3" class="text-right"><s:property value="importiCapitolo0.stanziamento" /></td>
			<td colspan="3" class="text-right"><s:property value="importiCapitolo1.stanziamento" /></td>
			<td colspan="3" class="text-right"><s:property value="importiCapitolo2.stanziamento" /></td>
		</tr>
		<tr>
			<th colspan="3"><s:property value="descrizioneImportoDerivato" /></th>
			<td colspan="3" class="text-right"><s:property value="importoDerivatoAnno0" /></td>
			<td colspan="3" class="text-right"><s:property value="importoDerivatoAnno1" /></td>
			<td colspan="3" class="text-right"><s:property value="importoDerivatoAnno2" /></td>
		</tr>
		<tr>
			<th colspan="3">Previsione al 31/12</th>
			<td colspan="3">
				<s:if test='%{previsioneEditabile}'>
					<input type="text" id="previsioneAnno1" class="lbTextSmall decimale soloNumeri span12 text-right" style="margin-bottom: 0px;" value="<s:property value="previsioneImpegnatoAccertato.importoPrevAnno1"/>">
				</s:if><s:else>
					<span class="text-right pull-right"> <s:property value="previsioneImpegnatoAccertato.importoPrevAnno1"/></span>
				</s:else>
			</td>
			<td colspan="3">
				<s:if test="%{previsioneEditabile}">
					<input type="text" id="previsioneAnno2" class="lbTextSmall decimale soloNumeri span12 text-right" style="margin-bottom: 0px;" value="<s:property value="previsioneImpegnatoAccertato.importoPrevAnno2"/>">
				</s:if><s:else>
					<span class="text-right pull-right"><s:property value="previsioneImpegnatoAccertato.importoPrevAnno2"/></span>
				</s:else>
			</td>
			<td colspan="3">
				<s:if test="%{previsioneEditabile}">
					<input type="text" id="previsioneAnno3" class="lbTextSmall decimale soloNumeri span12 text-right" style="margin-bottom: 0px;" value="<s:property value="previsioneImpegnatoAccertato.importoPrevAnno3"/>">
				</s:if><s:else>
					<span class="text-right pull-right"><s:property value="previsioneImpegnatoAccertato.importoPrevAnno3"/></span>
				</s:else>
			</td>
		</tr>
		<tr>
			<th colspan="3">Note</th>
				
				<s:if test="%{previsioneEditabile}">
					<td colspan="9">
						<textarea id="previsioneNote" rows="4" maxlength="500" placeholder="note eventuali previsione accertato" class="desc-prev-acc-0 span12" name="previsioneImpegnatoAccertato.note"><s:property value="previsioneImpegnatoAccertato.note" /></textarea>
					</td>
				</s:if><s:else>
					<td colspan="9">
						<span class="whitespace-pre"><s:property value="previsioneImpegnatoAccertato.note" /></span>
					</td>
				</s:else>
			
		</tr>
	</tbody>
</table>
					