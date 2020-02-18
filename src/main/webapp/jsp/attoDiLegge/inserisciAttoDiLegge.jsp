<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>

<fieldset class="form-horizontal">
	<div class="control-group">
		<label class="control-label" for="annoda24">Anno</label>
		<div class="controls">
			<s:textfield id="attoDiLegge.anno" cssClass="lbTextSmall span2 soloNumeri" name="attoDiLegge.anno" maxlength="4" required="true" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="annoa4">Numero</label>
		<div class="controls">
			<s:textfield id="attoDiLegge.numero" cssClass="lbTextSmall span2 soloNumeri" name="attoDiLegge.numero" maxlenght="9" required="true" />
		</div>
	</div>

	<div class="control-group">
		<label for="tipoAtto" class="control-label">Tipo Atto</label>
		<div class="controls">
			<s:select id="attoDiLegge.tipoAtto" list="listaTipoAtto" name="attoDiLegge.tipoAtto.uid"
				required="false" headerKey="" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
		</div>
	</div>

	<div class="control-group">
		<label class="control-label" for="articolo4">Articolo</label>
		<div class="controls">
			<s:textfield id="attoDiLegge.articolo" cssClass="lbTextSmall span2" name="attoDiLegge.articolo" maxlength="500" />
			<span class="al">
				<label class="radio inline" for="comma4">Comma</label>
			</span>
			<s:textfield id="attoDiLegge.comma" cssClass="lbTextSmall span2" name="attoDiLegge.comma" maxlength="500" />
			<span class="al">
				<label class="radio inline" for="Punto4">Punto</label>
			</span>
			<s:textfield id="attoDiLegge.punto" cssClass="lbTextSmall span2" name="attoDiLegge.punto" maxlength="500" />
		</div>
	</div>
</fieldset>