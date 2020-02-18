<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="labelModaleGiustificativo" role="dialog" tabindex="-1" class="modal hide fade form-horizontal" id="modaleGiustificativo">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane" id="labelModaleGiustificativo">Inserimento nuovo giustificativo</h4>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modaleGiustificativo">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<fieldset id="fieldsetModaleGiustificativo">
			<div class="control-group">
				<label class="control-label" for="tipoGiustificativoGiustificativo">Tipo *</label>
				<div class="controls">
					<s:select list="listaTipoGiustificativo" id="tipoGiustificativoGiustificativo" name="giustificativo.tipoGiustificativo.uid" cssClass="span12" required="true"
						headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-tipo-giustificativo="" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="annoProtocolloGiustificativo">Anno</label>
				<div class="controls">
					<s:textfield id="annoProtocolloGiustificativo" name="giustificativo.annoProtocollo" cssClass="span3 soloNumeri" maxlength="4" data-fattura="" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="numeroProtocolloGiustificativo"><abbr title="Numero">N.</abbr> Protocollo</label>
				<div class="controls">
					<s:textfield id="numeroProtocolloGiustificativo" name="giustificativo.numeroProtocollo" cssClass="span3" data-fattura="" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="dataEmissioneGiustificativo">Data emissione</label>
				<div class="controls">
					<s:textfield id="dataEmissioneGiustificativo" name="giustificativo.dataEmissione" cssClass="span3 datepicker" maxlength="10" data-date="true" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="numeroGiustificativoGiustificativo">Numero</label>
				<div class="controls">
					<s:textfield id="numeroGiustificativoGiustificativo" name="giustificativo.numeroGiustificativo" cssClass="span3" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="importoGiustificativoGiustificativo">Importo *</label>
				<div class="controls">
					<s:textfield id="importoGiustificativoGiustificativo" name="giustificativo.importoGiustificativo" cssClass="span5 soloNumeri decimale" required="true" />
				</div>
			</div>
			<input type="hidden" id="flagInclusoNelPagamentoGiustificativo" name="giustificativo.flagInclusoNelPagamento" value="true" data-maintain="" />
		</fieldset>
	</div>
	<div class="modal-footer">
		<button type="button" aria-hidden="true" data-dismiss="modal" class="btn btn-secondary">annulla</button>
		<button type="button" class="btn btn-primary" id="confermaModaleGiustificativo">conferma</button>
	</div>
</div>