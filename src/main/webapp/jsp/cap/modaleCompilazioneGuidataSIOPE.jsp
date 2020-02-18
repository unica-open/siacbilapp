<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:set name="ricercaUrl"><c:out value="${param.ricercaUrl}"/></s:set>
<s:set name="ajaxUrl"><c:out value="${param.ajaxUrl}"/></s:set>
<div id="modaleCompilazioneSIOPE" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="modaleCompilazioneSIOPELabel" aria-hidden="true">
	<div class="row-fluid">
		<div class="modal-header" id="modaleCompilazioneSIOPELabel">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="nostep-pane">Seleziona SIOPE</h4>
		</div>
		<div class="modal-body">
			<div class="alert alert-error hide" id="ERRORI_modaleCompilazioneSIOPE">
				<button type="button" class="close" data-hide="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul></ul>
			</div>
			<fieldset class="form-horizontal" id="modaleCompilazioneSIOPEFieldset">
				<div class="control-group">
					<label class="control-label" for="modaleCompilazioneSIOPE_codice">Codice</label>
					<div class="controls">
						<s:textfield id="modaleCompilazioneSIOPE_codice" cssClass="span5" name="modale.classificatore.codice" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="modaleCompilazioneSIOPE_descrizione">Descrizione</label>
					<div class="controls">
						<s:textfield id="modaleCompilazioneSIOPE_descrizione" cssClass="span5" name="modale.classificatore.descrizione" />
					</div>
				</div>
				<button type="button" class="btn btn-primary pull-right" data-ricerca-url="<s:property value="#ricercaUrl"/>" data-ajax-url="<s:property value="#ajaxUrl"/>">
					<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner"></i>
				</button>
			</fieldset>
			<div id="modaleCompilazioneSIOPEDivTabella" class="hide">
				<h4>Elenco SIOPE</h4>
				<table class="table table-hover tab_left" id="modaleCompilazioneSIOPETabella" summary="....">
					<thead>
						<tr>
							<th scope="col"></th>
							<th scope="col">Codice</th>
							<th scope="col">Descrizione</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
		<div class="modal-footer">
			<button class="btn btn-primary" id="modaleCompilazioneSIOPEPulsanteConferma">conferma</button>
		</div>
	</div>
</div>