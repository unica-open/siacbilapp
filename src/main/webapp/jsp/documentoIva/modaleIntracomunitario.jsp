<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:set var="docId">${param.docId}</s:set>
<s:set var="docName">${param.docName}</s:set>
<s:set var="docDisabled">${param.docDisabled}</s:set>
<div aria-hidden="false" aria-labelledby="msgAnnullaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleDocumentoIntracomunitario${param.docId}">
	<div class="modal-body">
		<div class="alert alert-info alert-persistent">
			<button data-dismiss="modal" class="close" type="button">&times;</button>
			<p>
				<strong>Al salvataggio del documento iva intracomunitario verr&agrave; <s:if test="%{#docDisabled}">inserita</s:if><s:else>aggiornata</s:else>
				anche la corrispondente controregistrazione INTRASTAT nel registro vendite iva immediata</strong>
			</p>
			
			<fieldset>
				<h5 class="step-pane pull-padding">Compila i campi:</h5>
				<s:hidden name="uidIntracomunitario%{#docName}" />
				<br>
				<div class="control-group">
					<label for="tipoRegistroIvaIntracomunitario${param.docId}" class="control-label">Tipo registro</label>
					<div class="controls">
						<s:textfield id="tipoRegistroIvaIntracomunitario%{#docId}" name="tipoRegistroIvaIntracomunitario%{#docName}"
							disabled="true" cssClass="span9" data-maintain="" />
						<s:hidden name="tipoRegistroIvaIntracomunitario%{#docName}" data-maintain="" />
					</div>
				</div>
				<%--
				<div class="control-group">
					<label for="tipoDocumentoIntracomunitario${param.docId}" class="control-label">Tipo documento *</label>
					<div class="controls">
						<s:select list="listaTipoDocumentoIntracomunitario" name="tipoDocumentoIntracomunitario%{#docName}.uid"
							id="tipoDocumentoIntracomunitario%{#docId}" cssClass="span9" headerKey="0" headerValue=""
							listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" />
					</div>
				</div>
				--%>
				<div class="control-group">
					<label for="attivitaIvaIntracomunitario${param.docId}" class="control-label">Attivit&agrave;</label>
					<div class="controls">
						
						<select id="attivitaIvaIntracomunitario${param.docId}" name="attivitaIvaIntracomunitario${param.docName}.uid" class="span9"
								<s:if test="%{!#docDisabled}">disabled</s:if> data-maintain="">
							<option value="0"></option>
							<s:iterator value="listaAttivitaIvaIntracomunitario" var="ai">
								<option value="<s:property value="%{#ai.uid}" />" data-gruppo-attivita-iva="<s:property value="%{#ai.gruppoAttivitaIva.uid}" />"
									data-flag-rilevante-irap="<s:property value="%{#ai.flagRilevanteIRAP}" />"
									<s:if test="%{#ai.uid == attivitaIva.uid}">selected</s:if>>
									<s:property value="%{#ai.codice}" /> - <s:property value="%{#ai.descrizione}" />
								</option>
							</s:iterator>
						</select>
					</div>
				</div>
				<div class="control-group">
					<label for="registroIvaSubdocumentoIvaIntracomunitario${param.docId}" class="control-label">Registro *</label>
					<div class="controls">
						<s:select list="listaRegistroIvaIntracomunitario" name="registroIvaIntracomunitario%{#docName}.uid"
							id="registroIvaSubdocumentoIvaIntracomunitario%{#docId}" cssClass="span9" headerKey="0" headerValue=""
							listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="%{#docDisabled}" data-maintain="" />
					</div>
				</div>
			</fieldset>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" aria-hidden="true" data-dismiss="modal" class="btn">annulla</button>
		<button type="button" class="btn btn-primary submit" id="pulsanteConfermaIntracomunitario${param.docId}">conferma</button>
	</div>
</div>