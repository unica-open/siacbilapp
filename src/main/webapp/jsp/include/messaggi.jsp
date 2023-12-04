<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div class="alert alert-error<s:if test='!hasErrori()'> hide</s:if>" id="ERRORI">
	<button type="button" class="close" data-hide="alert">&times;</button>
	<strong>Attenzione!!</strong><br>
	<ul>
		<s:if test="hasErrori()">
			<s:iterator value="errori">
				<li>  <s:property value="testo" escapeHtml="false" /> 
				<s:if test="hasDettagliTecnici()">(rif. <s:property value='dettagliTecnici'/>)</s:if>
				</li>
			</s:iterator>
		</s:if>
	</ul>
</div>
<div class="alert alert-warning<s:if test='!hasMessaggi()'> hide</s:if>" id="MESSAGGI">
	<button type="button" class="close" data-hide="alert">&times;</button>
	<strong>Attenzione!</strong><br>
	<ul>
		<s:if test="hasMessaggi()">
			<s:iterator value="messaggi">
				<li><s:property value="codice"/> - <s:property value="descrizione" escapeHtml="false" /> </li>
			</s:iterator>
		</s:if>
	</ul>
</div>
<div class="alert alert-success<s:if test='!hasInformazioni()'> hide</s:if>" id="INFORMAZIONI">
	<button type="button" class="close" data-hide="alert">&times;</button>
	<strong>Informazioni</strong><br>
	<ul>
		<s:if test="hasInformazioni()">
			<s:iterator value="informazioni">
				<li><s:property value="testo" escapeHtml="false" /></li>
			</s:iterator>
		</s:if>
	</ul>
</div>