<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion-inner">
	<table class="table table-hover tab_left" id="tabellaOrdini">
		<thead>
			<tr id="txt_Left">
				<th scope="col">Numero</th>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="listaOrdine" var="o">
				<tr>
					<td><s:property value="#o.numeroOrdine"/></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</div>