<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion-inner">
	<table class="table table-hover tab_left" id="tabellaDatiFinanziariMovGest">
		<thead>
			<tr>
				<th>Anno</th>
				<th>Numero</th>
				<th>Descrizione</th>
				<th>Soggetto</th>
				<th><abbr title="Quinto livello del Piano dei conti">V liv. Pdc</abbr></th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="listaDatiFinanziari" var="df">
				<tr>
					<td><s:property value="#df.anno" /></td>
					<td><s:property value="#df.numero" /></td>
					<td><s:property value="#df.descrizione" /></td>
					<td><s:property value="#df.soggetto" /></td>
					<td><s:property value="#df.pianoDeiConti" /></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</div>