<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="labelModaleMissioneEsterna" role="dialog" tabindex="-1" class="modal hide fade" id="modaleMissioneEsterna">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane" id="labelModaleMissioneEsterna">Seleziona la missione</h4>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modaleMissioneEsterna">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<div id="risultatiRicercaModaleMissioneEsterna">
			<h4>Elenco missioni</h4>
			<table class="table table-hover tab_left" id="tabellaModaleMissioneEsterna">
				<thead>
					<tr>
						<th></th>
						<th>Codice missione</th>
						<th>Matricola</th>
						<th>Cognome Nome dipendente</th>
						<th>Data missione</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-primary" id="pulsanteConfermaModaleMissioneEsterna">
			conferma&nbsp;<i class="icon-spin icon-refresh spinner"></i>
		</button>
	</div>
</div>