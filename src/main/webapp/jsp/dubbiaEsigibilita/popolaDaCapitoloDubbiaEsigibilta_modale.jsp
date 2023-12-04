<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<c:if test="${param.baseActionName != null}">
	<s:set var="baseActionName">${param.baseActionName}</s:set>
</c:if>

<div id="modale-capitolo-popola-da" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="guidaCapLabel" aria-hidden="true">
	<div class="row-fluid">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 data-titolo-popola-da class="nostep-pane"></h4>
		</div>

		<div class="modal-body">
			<div class="alert alert-error hide"	id="errori-modale-capitolo-popola-da">
				<button type="button" class="close" data-hide="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul></ul>
			</div>

		<div class="" id="div-risultati-capitolo-popola-da">
			<span class="messaggio-popola-da"></span>
			<p/>
			<span data-elaborazione-riferimento-popola-da></span>
			<div>
				<table class="table table-hover" id="risultati-capitolo-popola-da">
					<thead>
						<tr>
							<th scope="col">Capitolo</th>
							<th scope="col">Descrizione</th>
							<th scope="col">Titolo</th>
							<th scope="col">Tipologia</th>
							<th scope="col">Categoria</th>
							<th scope="col"><abbr title="Struttura Amministrativa Responsabile">SAC</abbr></th>
							<th scope="col">
								<input type="checkbox" class="tooltip-test check-all" data-referred-table="#risultatiRicercaCapitolo">
							</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
			<p class="margin-large">
				<button type="button" class="btn" data-dismiss="modal" aria-hidden="true">indietro</button>
				<button type="button" id="pulsanteEstraiCapitoliPopolaDa" class="btn secondary">estrai in foglio di calcolo</button>
				<button type="button" id="pulsanteConfermaCapitoliPopolaDa" class="btn btn-primary">conferma</button>
			</p>
			<s:form id="formCapitoliPopolaDa_hidden" cssClass="hide" novalidate="novalidate" action="%{#baseActionName + '_confermaCapitoliModale'}" method="post" />
		
		</div>
	</div>
</div>
</div>