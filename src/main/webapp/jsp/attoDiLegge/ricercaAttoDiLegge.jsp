<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Messaggio di ERROR --%>	
<div id="ERRORI_CERCA_ATTO" class="alert alert-error hide">
		<button type="button" class="close" data-hide="alert">&times;</button>
		<strong>Attenzione!!</strong><br>
		<ul>
		</ul>
</div>

<%-- Messaggio di MESSAGGI --%>
<div id="MESSAGGI_CERCA_ATTO" class="alert alert-warning hide">
		<button type="button" class="close" data-hide="alert">&times;</button>
		<strong>Attenzione!!</strong><br>
		<ul>
		</ul>
</div>

<%-- Messaggio di INFORMAZIONI --%>
<div id="INFORMAZIONI_CERCA_ATTO" class="alert alert-success hide">
		<button type="button" class="close" data-hide="alert">&times;</button>
		<strong>Attenzione!!</strong><br>
		<ul>
		</ul>
</div>

<fieldset class="form-horizontal">
	<div class="control-group">
		<label class="control-label" for="annoda2">Anno</label>
		<div class="controls">
			<s:textfield id="annoda2" cssClass="lbTextSmall span2 soloNumeri" name="annoLegge" required="true" maxlength="4" />
			<span class="al">
				<label class="radio inline" for="annoa">Numero</label>
			</span>
			<s:textfield id="annoa" cssClass="lbTextSmall span2 soloNumeri" required="true" name="numeroLegge" maxlength="9" />
			<span class="al">
				<label class="radio inline" for="tipoAtto">Tipo Atto</label>
			</span>
			<s:select id="tipoAtto" list="listaTipoAtto" name="tipoAtto.uid" cssClass="span2"
				required="false" headerKey="" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
		</div>
	</div>
	
	<div class="control-group">
		<label class="control-label" for="articolo">Articolo</label>
		<div class="controls">
			<s:textfield id="articolo" cssClass="lbTextSmall span2" name="articolo" maxlength="500" />
			<span class="al">
				<label class="radio inline" for="comma">Comma</label>
			</span>
			<s:textfield id="comma" cssClass="lbTextSmall span2" name="comma" maxlength="500" />
			<span class="al">
				<label class="radio inline" for="Punto">Punto</label>
			</span>
			<s:textfield id="Punto" cssClass="lbTextSmall span2" name="punto" maxlength="500" />
		</div>
	</div>
</fieldset>
<p>
	<s:reset cssClass="btn btn-link" value="annulla" />
	&nbsp;
	<a id="btnCercaAtto" class="btn btn-primary" href="#">
	cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_AttoDiLegge"></i>
	</a>
</p>