<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div aria-hidden="true" aria-labelledby="modaleDettaglioCespitiLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleDettaglioCespiti">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h3 id="modaleDettaglioCespitiLabel">Cespiti collegati <span id="modaleDettaglioCespitiSpan"></span></h3>
	</div>
	<div class="modal-body">
		<h4 id="modaleDettaglioCespitiDescrizione"></h4>
		<table class="table table-hover tab_left" id="modaleDettaglioCespitiTabella">
			<thead>
				<tr>
					<th>Cespite</th>
					<th>Tipo Bene</th>
					<th>Valore Iniziale</th>
					<th>Valore Attuale</th>
					<th>Stato Coge</th>				
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn btn-secondary">annulla</button>
	</div>
</div>