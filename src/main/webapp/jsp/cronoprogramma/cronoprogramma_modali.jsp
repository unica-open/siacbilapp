<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Modali --%>
<%--div id="modaleAggiornamentoUscita" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAggiornaPLabel" aria-hidden="true">
	<div class="row-fluid">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true"data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="nostep-pane">Aggiornare l'attivit&agrave;</h4>
		</div>
		<div class="modal-body">
		</div>
		<div class="modal-footer">
			<button class="btn btn-secondary" aria-hidden="true" id="pulsanteAnnullaAggiornamentoDettaglioUscita">annulla</button>
			<button class="btn btn-primary" aria-hidden="true" id="pulsanteSalvaAggiornamentoDettaglioUscita">
				salva&nbsp;<i class="icon-spin icon-refresh spinner"></i>
			</button>
		</div>
	</div>
</div>

<div id="modaleAggiornamentoEntrata" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgAggiornaELabel" aria-hidden="true">
	<div class="row-fluid">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="nostep-pane">Aggiornare l'attivit&agrave;</h4>
		</div>
		<div class="modal-body">
		</div>
		<div class="modal-footer">
			<button class="btn btn-secondary" aria-hidden="true" id="pulsanteAnnullaAggiornamentoDettaglioEntrata">annulla</button>
			<button class="btn btn-primary" aria-hidden="true" id="pulsanteSalvaAggiornamentoDettaglioEntrata">
				salva&nbsp;<i class="icon-spin icon-refresh spinner"></i>
			</button>
		</div>
	</div>
</div>
--%>
<div id="modaleElimina" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="msgEliminaLabel" aria-hidden="true">
	<div class="modal-body">
		<div class="alert alert-error alert-persistent">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<p>
				<strong>Attenzione!!!</strong>
			</p>
			<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<a class="btn" data-dismiss="modal" aria-hidden="true">no, indietro</a>
		<a class="btn btn-primary" aria-hidden="true" id="pulsanteProseguiElimina">
			s&iacute;, prosegui&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_eliminaDettaglio"></i>
		</a>
	</div>
</div>

<div id="modaleConsultaTotali" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="TotaliEntrataLabel" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h4 id="titoloTotali"><%--Totali Entrata--%></h4>
	</div>
	<div class="modal-body">
		<table class="table table-hover tab_centered" id="tabellaTotali">
			<thead>
			</thead>
		</table>
	</div>
	<div class="modal-footer">
		<button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">chiudi</button>
	</div>
</div>

<div id="modaleVerificaQuadratura" aria-hidden="true" aria-labelledby="verQuadraturaLabel" role="dialog" tabindex="-1" class="modal hide fade">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane">Quadratura</h4>
	</div>
	<div class="modal-body">
		<fieldset class="form-horizontal">
			<table summary="...." class="table tab_left table-hover">
				<thead>
				</thead>
				<tbody>
					<tr>
						<th scope="col" class="tab_Right borderBottomLight">Entrate</th>
						<td scope="col" class="tab_Right" id="quadraturaEntrata"></td>
					</tr>
					<tr>
						<th scope="col" class="tab_Right borderBottomLight">Spese</th>
						<td scope="col" class="tab_Right" id="quadraturaUscita"></td>
					</tr>
					<tr>
						<th scope="col" class="tab_Right borderBottomLight">Differenza</th>
						<td scope="col" class="tab_Right" id="quadraturaDifferenza"></td>
					</tr>
				</tbody>
			</table>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn btn-primary">chiudi</button>
	</div>
</div>