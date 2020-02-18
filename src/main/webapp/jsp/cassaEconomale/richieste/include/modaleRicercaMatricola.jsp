<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="labelModaleRicercaMatricola" role="dialog" tabindex="-1" class="modal hide fade" id="modaleRicercaMatricola">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane" id="labelModaleRicercaMatricola">Seleziona dipendente</h4>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modaleRicercaMatricola">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<fieldset class="form-horizontal" id="fieldsetModaleRicercaMatricola">
		
			
			<div class="control-group">
				<label class="control-label" for="matricolaSoggettoModaleRicercaMatricola">Matricola</label>
				<div class="controls">
					<input type="text" class="span3" name="modale.soggetto.matricola" id="matricolaSoggettoModaleRicercaMatricola">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="denominazioneSoggettoModaleRicercaMatricola">Denominazione</label>
				<div class="controls">
					<input type="text" class="span9" id="denominazioneSoggettoModaleRicercaMatricola" name="modale.soggetto.denominazione" />
				</div>
			</div>
			<button type="button" class="btn btn-primary pull-right" id="bottoneCercaModaleRicercaMatricola">
				<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="spinnerModaleRicercaMatricola"></i>
			</button>
		</fieldset>
		<div id="risultatiRicercaModaleRicercaMatricola">
			<h4>Elenco dipendenti</h4>
			<table class="table table-hover tab_left" id="tabellaModaleRicercaMatricola">
				<thead>
					<tr>
						<th></th>
						<th>Matricola</th>
						<th>Denominazione</th>
						<th>Stato</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-primary" id="pulsanteConfermaModaleRicercaMatricola">conferma</button>
	</div>
</div>