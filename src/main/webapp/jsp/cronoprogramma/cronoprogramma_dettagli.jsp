<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<s:hidden id="HIDDEN_annoEsercizio" value="%{annoEsercizioInt}" data-maintain="" />
<h4 class="step-pane">Entrate</h4>
<table id="tabellaEntrata" class="table table-hover tab_left dataTable" summary="....">
	<thead>
		<tr>
			<th scope="col">Entrata prevista</th>
			<th scope="col">Anno di competenza</th>
			<th scope="col">Classificazione di bilancio</th>
			<th scope="col">Capitolo/Articolo<s:if test="%{gestioneUEB}">/UEB</s:if></th>
			<th scope="col" class="tab_Right">Valore</th>
			<th scope="col" class="tab_Right">&nbsp;</th>
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
<p>
	<s:if test="%{cronoAggiornabile}">
		<a class="btn btn-secondary" id="pulsanteApriInserisciDettaglioEntrata" title="visualizza dettaglio">inserisci nuova entrata</a>
	</s:if>
	<a class="btn btn-secondary" id="pulsanteApriConsultaTotaliEntrata" title="visualizza totali">
		Consulta Totali&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_consultaTotaliEntrata"></i>
	</a>
</p>

<div class="collapse" id="collapseDettaglioEntrata"></div>
<br>

<h4 class="step-pane">Spese</h4>
<table id="tabellaUscita" class="table table-hover tab_left dataTable" summary="....">
	<thead>
		<tr>
			<th scope="col">Attivit&agrave; prevista</th>
			<th scope="col">Anno spesa</th>
			<th scope="col">Anno entrata</th>
			<th scope="col">Classificazione di bilancio</th>
			<th scope="col">Capitolo/Articolo<s:if test="%{gestioneUEB}">/UEB</s:if></th>
			<th scope="col" class="tab_Right">Valore Previsto</th>
			<th scope="col" class="tab_Right">&nbsp;</th>
		</tr>
	</thead>
	<tbody>
	</tbody>
</table>
<p>
	<s:if test="%{cronoAggiornabile}">
		<a class="btn btn-secondary" id="pulsanteApriInserisciDettaglioUscita" title="visualizza dettaglio">inserisci nuova spesa</a>
	</s:if>
	<a class="btn btn-secondary" id="pulsanteApriConsultaTotaliUscita" title="visualizza totali">
		Consulta Totali&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_consultaTotaliUscita"></i>
	</a>
	<%-- data-toggle="modal" href="#TotaliUscita" --%>
</p>

<div class="collapse" id="collapseDettaglioUscita"></div>