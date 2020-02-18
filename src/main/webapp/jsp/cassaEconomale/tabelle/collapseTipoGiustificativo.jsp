<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion_info">
	<fieldset id="fieldsetGiustificativo" class="form-horizontal">
		<h4 class="titleTxt nostep-pane"><s:property value="operazioneTipiGiustificativo.descrizione" /></h4>
		<s:hidden id="uidTipoGiustificativo" name="tipoGiustificativo.uid" />
		<h4 class="step-pane">Dati</h4>
		<div class="control-group">
			<label class="control-label" for="tipologiaGiustificativoTipoGiustificativo">Tipo giustificativo *</label>
			<div class="controls">
				<s:select list="listaTipologiaGiustificativo" name="tipoGiustificativo.tipologiaGiustificativo" id="tipologiaGiustificativoTipoGiustificativo"
					cssClass="span9" headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" disabled="%{!operazioneTipiGiustificativo.editabile}" />
				<s:if test="%{!operazioneTipiGiustificativo.editabile}">
					<s:hidden id="tipologiaGiustificativoTipoGiustificativoHidden" name="tipoGiustificativo.tipologiaGiustificativo" />
				</s:if>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="codiceTipoGiustificativo">Codice *</label>
			<div class="controls">
				<s:textfield id="codiceTipoGiustificativo" name="tipoGiustificativo.codice" cssClass="span3" maxlength="200" required="required"
					readonly="%{!operazioneTipiGiustificativo.editabile}" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="descrizioneTipoGiustificativo">Descrizione *</label>
			<div class="controls">
				<s:textfield id="descrizioneTipoGiustificativo" name="tipoGiustificativo.descrizione" cssClass="span9" maxlength="500" required="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="importoTipoGiustificativo">Importo</label>
			<div class="controls">
				<s:textfield id="importoTipoGiustificativo" name="tipoGiustificativo.importo" cssClass="span9 decimale soloNumeri" required="required" disabled="!tipoGiustificativoAnticipoMissione" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="percentualeAnticipoTrasfertaTipoGiustificativo">Percentuale anticipo trasferte</label>
			<div class="controls">
				<s:textfield id="percentualeAnticipoTrasfertaTipoGiustificativo" name="tipoGiustificativo.percentualeAnticipoTrasferta"
					cssClass="span2 soloNumeri decimale" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="percentualeAnticipoMissioneTipoGiustificativo">Percentuale anticipo missione</label>
			<div class="controls">
				<s:textfield id="percentualeAnticipoMissioneTipoGiustificativo" name="tipoGiustificativo.percentualeAnticipoMissione"
					cssClass="span2 soloNumeri decimale" />
			</div>
		</div>
	</fieldset>
	<p>
		<button type="button" id="annullaGiustificativo" class="btn btn-secondary">annulla</button>
		<span class="pull-right">
			<button type="button" id="salvaGiustificativo" class="btn btn-primary">salva</button>
		</span>
	</p>
</div>