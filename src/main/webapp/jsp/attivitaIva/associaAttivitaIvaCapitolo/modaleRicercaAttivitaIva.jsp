<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div aria-hidden="true" aria-labelledby="CercaRelazioneIVALABEL" role="dialog" tabindex="-1" class="modal hide fade" id="modaleRisultatiAttivitaIva">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<div class="alert alert-error hide" id="ERRORI_MODALE">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<h4 class="nostep-pane">Ricerca le attivit&agrave; iva</h4>
	</div>
	<div class="modal-body">
		<fieldset class="form-horizontal">
			<table summary="...." class="table table-hover tab_left" id="tabellaAttivitaIvaRicerca">
				<thead>
					<tr>
						<th>&nbsp;</th>
						<th>Codice</th>
						<th>Descrizione</th>
						<th>Rilevante IRAP</th>
						<th>Gruppo attivit&agrave; iva</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
				<tfoot>
				</tfoot>
			</table>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-secondary" id="BUTTON_noAssocia" aria-hidden="true" data-dismiss="modal">annulla</button>
		<button type="button" class="btn btn-primary" id="BUTTON_siAssocia">associa&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_BUTTON_siAssocia"></i></button>
	</div>
</div>