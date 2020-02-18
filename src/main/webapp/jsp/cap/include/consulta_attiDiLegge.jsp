<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<h4>Atti di legge collegati</h4>
<table class="table table-hover" summary="...." id="tabellaRelazioneAttoDiLegge">
	<thead>
		<tr>
			<th scope="col">Tipo Atto</th>
			<th scope="col">Anno</th>
			<th scope="col">Numero</th>
			<th scope="col">Articolo</th>
			<th scope="col">Comma</th>
			<th scope="col">Punto</th>
			<th scope="col">Gerarchia</th>
			<th scope="col">Descrizione</th>
			<th scope="col">Data inizio <abbr title="finanziamento">finanz</abbr></th>
			<th scope="col">Data fine <abbr title="finanziamento">finanz</abbr></th>
		</tr>
	</thead>
	<tbody>
		<s:iterator value="aaData" var="adl">
			<tr>
				<td><s:property value="#adl.tipoAtto"/></td>
				<td><s:property value="#adl.anno"/></td>
				<td><s:property value="#adl.numero"/></td>
				<td><s:property value="#adl.articolo"/></td>
				<td><s:property value="#adl.comma"/></td>
				<td><s:property value="#adl.punto"/></td>
				<td><s:property value="#adl.gerarchia"/></td>
				<td><s:property value="#adl.descrizione"/></td>
				<td><s:property value="#adl.dataInizioFinanziamento"/></td>
				<td><s:property value="#adl.dataFineFinanziamento"/></td>
			</tr>
		</s:iterator>
	</tbody>
</table>