<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:set var="visualizzaImportoIniziale"><c:out value="${param.visualizzaImportoIniziale}" default="false"/></s:set>

<table class="table table-hover table-condensed table-bordered" id="tabellaStanziamentiPerComponenti">
	<thead>
		<tr class="row-slim-bottom">
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th <s:if test="%{#visualizzaImportoIniziale}"> class="text-center" scope="col"  colspan="2"</s:if><s:else>class="text-right"</s:else>> ${annoEsercizioInt - 1}</th>
			<th <s:if test="%{#visualizzaImportoIniziale}"> class="text-center" scope="col" colspan="2"</s:if> <s:else>class="text-right"</s:else>>Residui ${annoEsercizioInt + 0}</th>
			<th <s:if test="%{#visualizzaImportoIniziale}"> class="text-center" scope="col" colspan="2"</s:if> <s:else>class="text-right"</s:else>>${annoEsercizioInt + 0}</th>
			<th <s:if test="%{#visualizzaImportoIniziale}"> class="text-center" scope="col" colspan="2"</s:if> <s:else>class="text-right"</s:else>>${annoEsercizioInt + 1}</th>
			<th <s:if test="%{#visualizzaImportoIniziale}"> class="text-center" scope="col" colspan="2"</s:if> <s:else>class="text-right"</s:else>>${annoEsercizioInt + 2}</th>
			<th <s:if test="%{#visualizzaImportoIniziale}"> class="text-center" scope="col" colspan="2"</s:if> <s:else>class="text-right"</s:else>>>${annoEsercizioInt + 2}</th>
		</tr>
		<s:if test="%{#visualizzaImportoIniziale}">
		<tr class="row-slim-bottom">
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th class="text-right" scope="col" colspan="1" >Iniziale</th>
			<th class="text-right" scope="col" colspan="1" >Finale</th>
			<th class="text-right" scope="col" colspan="1" >Iniziale</th>
			<th class="text-right" scope="col" colspan="1" >Finale</th>
			<th class="text-right" scope="col" colspan="1" >Iniziale</th>
			<th class="text-right" scope="col" colspan="1" >Finale</th>
			<th class="text-right" scope="col" colspan="1" >Iniziale</th>
			<th class="text-right" scope="col" colspan="1" >Finale</th>
			<th class="text-right" scope="col" colspan="1" >Iniziale</th>
			<th class="text-right" scope="col" colspan="1" >Finale</th>
			<th class="text-right" scope="col" colspan="1" >Iniziale</th>
			<th class="text-right" scope="col" colspan="1" >Finale</th>
		</tr>
		</s:if>
	</thead>
	<tbody>
	</tbody>
</table>
								