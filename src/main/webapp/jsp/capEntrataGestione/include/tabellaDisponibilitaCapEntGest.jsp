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
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneAnno0.disponibilitaVariare"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneAnno1.disponibilitaVariare"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneAnno2.disponibilitaVariare"/></td>
			<td></td>
		</tr>
		<tr>
			<td colspan="5" class="row-small"></td>
		</tr>

		<tr>
			<th colspan="5">Accertamenti</th>
		</tr>
		<tr>
			<td><strong><abbr title="Numero">N.</abbr> totale</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneAnno0.numeroAccertamenti"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneAnno1.numeroAccertamenti"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneAnno2.numeroAccertamenti"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneResiduo.numeroAccertamenti"/></td>
		</tr>
		<tr>
			<td><strong>Accertato</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneAnno0.accertato"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneAnno1.accertato"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneAnno2.accertato"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneResiduo.accertato"/></td>
		</tr>
		<tr>
			<td><strong>Disponibilit&agrave; ad accertare</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneAnno0.disponibilitaAccertare"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneAnno1.disponibilitaAccertare"/></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneAnno2.disponibilitaAccertare"/></td>
			<td></td>
		</tr>
		<tr>
			<td colspan="5" class="row-small"></td>
		</tr>

		<tr>
			<th colspan="5">Ordinativi</th>
		</tr>
		<tr>
			<td><strong><abbr title="Numero">N.</abbr> totale</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneAnno0.numeroOrdinativi"/></td>
			<td></td>
			<td></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneResiduo.numeroOrdinativi"/></td>
		</tr>
		<tr>
			<td><strong>Incassato</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneAnno0.incassato"/></td>
			<td></td>
			<td></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneResiduo.incassato"/></td>
		</tr>
		<tr>
			<td><strong>Disponibilit&agrave; ad incassare</strong></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneAnno0.disponibilitaIncassare"/></td>
			<td></td>
			<td></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloEntrataGestioneResiduo.disponibilitaIncassare"/></td>
		</tr>

	</tbody>
</table>