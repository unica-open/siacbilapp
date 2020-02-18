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
			<s:hidden id="uidGiustificativo" name="giustificativo.uid" />
			<s:hidden id="dataEmissioneGiustificativo" name="giustificativo.dataEmissione" data-date="true" />
			<div class="control-group">
				<label class="control-label" for="tipoGiustificativoGiustificativo">Tipo *</label>
				<div class="controls">
					<select id="tipoGiustificativoGiustificativo" name="giustificativo.tipoGiustificativo.uid" class="span12" required data-calcolo-spettante="" data-tipo-giustificativo="">
						<option></option>
						<s:iterator value="listaTipoGiustificativo" var="tg">
							<option value="<s:property value="#tg.uid"/>"
									data-spettante="<s:property value="#tg.percentualeAnticipoMissione"/>"
									data-importo="<s:property value="#tg.importo"/>"
									<s:if test="%{#tg.uid == giustificativo.tipoGiustificativo.uid}">selected</s:if>>
								<s:property value="#tg.codice" /> - <s:property value="#tg.descrizione" />
							</option>
						</s:iterator>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="valutaGiustificativo">Valuta *</label>
				<div class="controls">
					<s:select list="listaValuta" id="valutaGiustificativo" name="giustificativo.valuta.uid" cssClass="span12" required="true" headerKey="" headerValue=""
						listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-valuta="" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="cambioGiustificativo">Cambio</label>
				<div class="controls">
					<s:textfield id="cambioGiustificativo" name="giustificativo.cambio" cssClass="span3 soloNumeri decimale" disabled="true" data-euro="" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="importoGiustificativoInValutaGiustificativo">Importo valuta estera</label>
				<div class="controls">
					<s:textfield id="importoGiustificativoInValutaGiustificativo" name="giustificativo.importoGiustificativoInValuta" cssClass="span3 soloNumeri decimale" disabled="true" data-euro="" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="quantitaGiustificativo">Quantit&agrave;</label>
				<div class="controls">
					<s:textfield id="quantitaGiustificativo" name="giustificativo.quantita" cssClass="span3 soloNumeri" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="importoGiustificativoGiustificativo">Importo</label>
				<div class="controls">
					<s:textfield id="importoGiustificativoGiustificativo" name="giustificativo.importoGiustificativo" cssClass="span5 soloNumeri decimale" data-calcolo-spettante="" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="importoSpettanteGiustificativoGiustificativo">Importo spettante</label>
				<div class="controls">
					<s:textfield id="importoSpettanteGiustificativoGiustificativo" name="giustificativo.importoSpettanteGiustificativo" cssClass="span5 soloNumeri decimale" readonly="true" data-importo-spettante="" />
				</div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button type="button" aria-hidden="true" data-dismiss="modal" class="btn btn-secondary">annulla</button>
		<button type="button" class="btn btn-primary" id="confermaModaleGiustificativo">conferma</button>
	</div>
</div>