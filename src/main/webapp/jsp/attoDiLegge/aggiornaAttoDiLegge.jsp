<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<%-- Messaggio di ERROR --%>

<div id="ERRORI_AGG_ATTO" class="alert alert-error hide">
		<button type="button" class="close" data-hide="alert">&times;</button>
		<strong>Attenzione!!</strong><br>
		<ul>
		</ul>
</div>	

<%-- Messaggio di INFORMAZIONI --%>
<div id="INFORMAZIONI_AGG_ATTO" class="alert alert-success hide">
		<button type="button" class="close" data-hide="alert">&times;</button>
		<strong>Attenzione!!</strong><br>
		<ul>
		</ul>
</div>		
<fieldset class="form-horizontal">
	<s:hidden name="aggAttoDiLegge.uid" id="aggAttoDiLegge.uid" value="" data-maintain="" />
	<div class="control-group">
		<label class="control-label" for="annoda24">Anno</label>
		<div class="controls">
			<s:textfield id="aggAttoDiLegge.anno" cssClass="lbTextSmall span2 soloNumeri" name="aggAttoDiLegge.anno" maxlength="4" required="true" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="annoa4">Numero</label>
		<div class="controls">
			<s:textfield id="aggAttoDiLegge.numero" cssClass="lbTextSmall span2 soloNumeri" name="aggAttoDiLegge.numero" maxlenght="9" required="true" />
		</div>
	</div>
	
	<div class="control-group">
		<label for="tipoAtto" class="control-label">Tipo Atto</label>
		<div class="controls">
			<s:select id="aggAttoDiLegge.tipoAtto.uid" list="listaTipoAtto" name="aggAttoDiLegge.tipoAtto.uid"
				required="false" headerKey="" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
		</div>
	</div>
		
	<div class="control-group">
		<label class="control-label" for="articolo4">Articolo</label>
		<div class="controls">
			<s:textfield id="aggAttoDiLegge.articolo" cssClass="lbTextSmall span2" name="aggAttoDiLegge.articolo" maxlength="500" />
			<span class="al">
				<label class="radio inline" for="comma4">Comma</label>
			</span>
			<s:textfield id="aggAttoDiLegge.comma" cssClass="lbTextSmall span2" name="aggAttoDiLegge.comma" maxlength="500" />
			<span class="al">
				<label class="radio inline" for="Punto4">Punto</label>
			</span>
			<s:textfield id="aggAttoDiLegge.punto" cssClass="lbTextSmall span2" name="aggAttoDiLegge.punto" maxlength="500" />
		</div>
	</div>
</fieldset>