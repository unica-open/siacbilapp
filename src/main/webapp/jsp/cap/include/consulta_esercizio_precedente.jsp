<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<h4>Stanziamenti esercizio precedente</h4>

<table class="table table-hover table-bordered">
	<tr>
		<th>&nbsp;</th>
		<th scope="col" class="text-center">${annoEsercizioInt - 1}</th>
	</tr>
	<tr>
		<th>&nbsp;</th>
		<th class="text-center" scope="col">Finale</th>
	</tr>
	<tr>
		<th>Competenza</th>
		<td><s:property value="importiEsercizioPrecedente.stanziamento" /></td>
	</tr>
	<tr>
		<th>Residuo</th>
		<td scope="row"><s:property value="importiEsercizioPrecedente.stanziamentoResiduo" /></td>
	</tr>
	<tr>
		<th>Cassa</th>
		<td scope="row"><s:property value="importiEsercizioPrecedente.stanziamentoCassa" /></td>
	</tr>
	<%--<tr>
		<th><abbr title="fondo pluriennale vincolato">FPV</abbr></th>
		<td scope="row"><s:property value="importiEsercizioPrecedente.fondoPluriennaleVinc" /></td>
	</tr>--%>
</table>
