<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="modaleCollegaPrimaNotaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleCollegaPrimaNota">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h3 id="modaleCollegaPrimaNotaLabel">CollegaPrima Nota</h3>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modale">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br/>
			<ul>
			</ul>
		</div>
		<div class="alert alert-success hide" id="INFORMAZIONI_modale">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<ul></ul>
		</div>
		<table class="table table-hover tab_left" id="tabellaPrimeNoteCollegate">
			<thead>
				<tr>
					<th>Tipo</th>
					<th>Anno</th>
					<th>Numero</th>
					<th>Motivazione</th>
					<th>Stato</th>
					<th class="tab_Right span2">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<p>
			<button type="button" id="pulsanteCollegamentoPrimeNote" class="btn btn-secondary">
				collega prima nota&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteCollegamentoPrimeNote"></i>
			</button>
		</p>
		<s:include value="/jsp/contabilitaGenerale/primaNotaLibera/include/collapseCollegamentoPrimaNota.jsp" />
		<br>
	</div>
</div>