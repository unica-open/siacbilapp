<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<fieldset class="form-horizontal" id="formRicercaProvvedimento">
	<div class="control-group">
		<label class="control-label" for="annoProvvedimento">Anno *</label>
		<div class="controls">
			<input type="text" id="annoProvvedimento" class="lbTextSmall span1" disabled="disabled" />
			<span class="al">
				<label class="radio inline" for="numeroProvvedimento">Numero</label>
			</span>
			<input type="text" id="numeroProvvedimento" class="lbTextSmall span1" disabled="disabled" />
			<span class="al">
				<label class="radio inline" for="tipoAttoProvvedimento">Tipo</label>
			</span>
			<select id="tipoAttoProvvedimento" class="lbTextSmall span2" disabled="disabled"></select>
			<span class="al">
				<label class="radio inline">Struttura Amministrativa</label>
			</span>
			<a href="#" role="button" class="btn" data-toggle="modal">
				Seleziona la Struttura amministrativa&nbsp;
				<i class="icon-spin icon-refresh spinner" id="SPINNER_StrutturaAmministrativoContabile"></i>
			</a>
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="oggettoProvvedimento">Oggetto</label>
		<div class="controls">
			<input type="text" class="lbTextSmall span9" id="oggettoProvvedimento" disabled="disabled"/>
		</div>
	</div>
</fieldset>

<s:hidden id="HIDDEN_uidProvvedimento" name="uidProvvedimento" />
<s:hidden id="HIDDEN_uidProvvedimentoInjettato" name="attoAmministrativo.uid" />
<s:hidden id="HIDDEN_numeroProvvedimento" name="numeroProvvedimento" />
<s:hidden id="HIDDEN_annoProvvedimento" name="annoProvvedimento" />
<s:hidden id="HIDDEN_statoProvvedimento" name="statoProvvedimento" />