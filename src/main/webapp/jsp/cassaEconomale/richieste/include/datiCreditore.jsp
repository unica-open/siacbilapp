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

<s:hidden id="HIDDEN_soggettoDenominazione" name="richiestaEconomale.soggetto.denominazione" />
<s:hidden id="HIDDEN_soggettoCodiceFiscale" name="richiestaEconomale.soggetto.codiceFiscale" />
<s:hidden id="HIDDEN_soggettoUid" name="richiestaEconomale.soggetto.uid" />

<h4>Dati del creditore<span id="descrizioneSoggetto"><s:property value="denominazioneSoggetto" /></span></h4>
<div class="control-group">
	<label class="control-label" for="codiceSoggettoRichiestaEconomale">Soggetto<s:property value="#asterisk" escapeHtml="false"/></label>
	<div class="controls">
		<s:textfield id="codiceSoggettoRichiestaEconomale" name="richiestaEconomale.soggetto.codiceSoggetto" cssClass="span2" required="true" disabled="%{isAggiornamento || #shouldDisable}" />
		<s:if test="%{isAggiornamento || #shouldDisable}">
			<s:hidden name="richiestaEconomale.soggetto.codiceSoggetto" />
		</s:if><s:else>
			<button type="button" class="btn btn-primary pull-right" id="pulsanteAperturaCompilazioneGuidataSoggetto">compilazione guidata</button>
		</s:else>
	</div>
</div>
