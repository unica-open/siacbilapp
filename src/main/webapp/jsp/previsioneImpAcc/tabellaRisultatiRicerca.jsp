<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%-- <%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%> --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- TABELLE RIEPILOGO con azioni -->
<c:if test="${param.suffix != null}">
	<s:set var="suffix">${param.suffix}</s:set>
</c:if>
<table class="table table-striped table-bordered table-hover dataTable" id="risultatiRicercaCapitolo<s:property value="%{#suffix}" />"summary="...." >
	<thead>
		<tr>
			<%-- <th scope="col">Tipologia</th> --%>
			<th scope="col">Capitolo</th>
			<th scope="col">Stato</th>
			<th scope="col">Classificazione</th>
			<th scope="col">Competenza</th>
			<th scope="col">Residuo</th>
			<th scope="col">Cassa</th>
			<th scope="col"><abbr title="Struttura Amministrativa Responsabile">Strutt. Amm. Resp.</abbr></th>
			<th scope="col"><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario</th>
			<th scope="col">Azioni</th>
		</tr>
	</thead>
	<tbody>
	</tbody>
	<tfoot>
		<tr>
			<%-- <th scope="col">Tipologia</th> --%>
			<th scope="col">Capitolo</th>
			<th scope="col">Stato</th>
			<th scope="col">Classificazione</th>
				<th scope="col" class="text-right">
					<s:property value="totaleImporti.stanziamento"/>
				<th scope="col" class="text-right">
					<s:property value="totaleImporti.stanziamentoResiduo"/>
				</th>
				<th scope="col" class="text-right">
					<s:property value="totaleImporti.stanziamentoCassa"/>
				</th>
			<th scope="col"><abbr title="Struttura Amministrativa Responsabile">Strutt. Amm. Resp.</abbr></th>
			<th scope="col"><abbr title="Piano dei Conti">P.d.C.</abbr> finanziario</th>
			<th scope="col">Azioni</th>
		</tr>
	</tfoot>
</table>
