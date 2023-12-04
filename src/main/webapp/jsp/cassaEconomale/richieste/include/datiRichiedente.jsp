<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:set var="asteriskAbsent"><c:out value="${param.asteriskAbsent}" default="false"/></s:set>
<s:set var="shouldDisable"><c:out value="${param.shouldDisable}" default="false"/></s:set>
<s:if test="%{#asteriskAbsent}">
	<s:set var="asterisk">&#8203;</s:set>
</s:if><s:else>
	<s:set var="asterisk">&nbsp;*</s:set>
</s:else>
<s:hidden name="maySearchHR" id="HIDDEN_maySearchHR"  />
<h4>Dati del richiedente<span id="descrizioneMatricola"><s:property value="denominazioneMatricola" /></span></h4>
<div class="control-group">
	<label class="control-label" for="matricolaSoggettoRichiestaEconomale">Matricola<s:property value="#asterisk" escapeHtml="false"/></label>
	<div class="controls">
		<s:textfield id="matricolaSoggettoRichiestaEconomale" name="richiestaEconomale.soggetto.matricola" cssClass="span2" required="true" disabled="%{isAggiornamento || #shouldDisable}" />
		<s:if test="%{isAggiornamento || #shouldDisable}">
			<s:hidden name="richiestaEconomale.soggetto.matricola" />
		</s:if><s:else>
			<button type="button" class="btn btn-primary pull-right" id="pulsanteCompilazioneGuidataMatricola">compilazione guidata</button>
		</s:else>
	</div>
</div>
<div class="control-group">
	<label class="control-label" for="strutturaDiAppartenenzaRichiestaEconomale">Unit&agrave; organizzativa</label>
	<div class="controls">
		<s:textfield id="strutturaDiAppartenenzaRichiestaEconomale" name="richiestaEconomale.strutturaDiAppartenenza" cssClass="span9" disabled="%{isAggiornamento || #shouldDisable}" />
		<s:if test="%{isAggiornamento}">
			<s:hidden id="HIDDEN_strutturaDiAppartenenzaRichiestaEconomale" name="richiestaEconomale.strutturaDiAppartenenza" />
		</s:if>
	</div>
</div>