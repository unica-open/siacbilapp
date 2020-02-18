<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div id="modaleCompilazioneSIOPEEntrata" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="modaleCompilazioneSIOPELabel" aria-hidden="true">
	<div class="row-fluid">
		<div class="modal-header" id="modaleCompilazioneSIOPELabel">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="nostep-pane">Seleziona SIOPE</h4>
		</div>
		<div class="modal-body">
			<div class="alert alert-error hide" id="ERRORI_modaleCompilazioneSIOPEEntrata">
				<button type="button" class="close" data-hide="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul></ul>
			</div>
			<fieldset class="form-horizontal" id="modaleCompilazioneSIOPEFieldsetEntrata">
				<div class="control-group">
					<label class="control-label" for="modaleCompilazioneSIOPEEntrata_codice">Codice</label>
					<div class="controls">
						<s:textfield id="modaleCompilazioneSIOPEEntrata_codice" cssClass="span5" name="modale.classificatore.codice" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="modaleCompilazioneSIOPEntrata_descrizione">Descrizione</label>
					<div class="controls">
						<s:textfield id="modaleCompilazioneSIOPEEntrata_descrizione" cssClass="span5" name="modale.classificatore.descrizione" />
					</div>
				</div>
				<button type="button" class="btn btn-primary pull-right" data-ricerca-url="ricercaClassificatoreGerarchico_siopeEntrata.do" data-ajax-url="risultatiRicercaSiopeEntrataAjax.do">
					<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner"></i>
				</button>
			</fieldset>
			<div id="modaleCompilazioneSIOPEEntrataDivTabella" class="hide">
				<h4>Elenco SIOPE</h4>
				<table class="table table-hover tab_left" id="modaleCompilazioneSIOPEEntrataTabella" summary="....">
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
			<button class="btn btn-primary" id="modaleCompilazioneSIOPEEntrataPulsanteConferma">conferma</button>
		</div>
	</div>
</div>