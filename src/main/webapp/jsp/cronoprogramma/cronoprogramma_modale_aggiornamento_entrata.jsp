<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Inserito nel modal-body --%>
<div class="alert alert-error hide" id="ERRORI_ENTRATA">
	<button type="button" class="close" data-hide="alert">&times;</button>
	<strong>Attenzione!!</strong><br>
	<ul></ul>
</div>
<fieldset class="form-horizontal" id="fieldsetDettaglioEntrataAggiornamento">
	<div class="alert alert-success hide" id="alertAssociazioneCapitoloEntrataAggiornamento">
		<button type="button" class="close" data-hide="alert">&times;</button>
		<ul></ul>
	</div>
	<h5 class="step-pane">Accertamento previsto </h5>
	<s:hidden name="dettaglioEntrataCronoprogramma.uid" data-maintain="" />
	<div class="control-group">
		<label class="control-label" for="descrizioneEntrataAggiornamento">Descrizione Entrata *</label>
		<div class="controls">
			<s:textfield id="descrizioneEntrataAggiornamento" cssClass="span10" name="dettaglioEntrataCronoprogramma.descrizioneCapitolo" placeholder="finanziamento" required="required" maxlength="500" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="annoEntrataAggiornamento">Anno *</label>
		<div class="controls">
			<s:textfield id="annoEntrataAggiornamento" cssClass="span10 soloNumeri" name="dettaglioEntrataCronoprogramma.annoCompetenza" placeholder="anno" required="required" maxlength="4" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="valorePrevistoEntrataAggiornamento">Valore previsto *</label>
		<div class="controls">
			<s:textfield id="valorePrevistoEntrataAggiornamento" cssClass="span3 soloNumeri decimale" name="dettaglioEntrataCronoprogramma.stanziamento" placeholder="valore" required="required" />
		</div>
	</div>
	<div class="control-group">
		<label for="numeroCapitoloEntrataAggiornamento" class="control-label">Capitolo</label>
		<div class="controls">
			<s:textfield id="numeroCapitoloEntrataAggiornamento" cssClass="span3 soloNumeri" name="dettaglioEntrataCronoprogramma.numeroCapitolo" placeholder="%{'capitolo'}" maxlength="7" />
			<span class="alRight">
				<label class="radio inline">Da esistente</label>
				<label class="radio inline">
					<input type="radio" value="si" id="daEsistenteEntrataSiAggiornamento" name="daEsistenteEntrataAggiornamento" />
					s&iacute;
				</label>
				<label class="radio inline">
					<input type="radio" value="no" id="daEsistenteEntrataNoAggiornamento" name="daEsistenteEntrataAggiornamento" />
					no
				</label>
			</span>
		</div>
	</div>
		
	<div id="campiArticoloUEBEntrataAggiornamento" class="control-group hide">
		<label for="numeroArticoloEntrataAggiornamento" class="control-label">Articolo</label>
		<div class="controls">
			<s:textfield id="numeroArticoloEntrataAggiornamento" cssClass="span2 soloNumeri" name="dettaglioEntrataCronoprogramma.numeroArticolo" placeholder="articolo" maxlength="7" />
			<s:if test="%{gestioneUEB}">
				<span class="alRight">
					 <label class="radio inline" for="numeroUEBEntrataAggiornamento">UEB</label>
				</span>
				<s:textfield id="numeroUEBEntrataAggiornamento" cssClass="span2 soloNumeri" name="dettaglioEntrataCronoprogramma.numeroUEB" placeholder="UEB" />
			</s:if><s:else>
				<input type="hidden" name="dettaglioEntrataCronoprogramma.numeroUEB" id="numeroUEBEntrataAggiornamento" value="1" data-maintain=""/>
			</s:else>
			<s:hidden name="dettaglioEntrataCronoprogramma.capitoloEntrataPrevisione.uid" id="uidCapitoloAssociatoEntrataAggiornamento" data-maintain="" />
			<span class="alLeft">
				<a class="btn btn-primary" id="pulsanteRicercaCapitoloEntrataAggiornamento">
					<i class="icon-search icon"></i>&nbsp;cerca
				</a>
			</span>
		</div>
	</div>
	
	<h5 class="step-pane">Classificazione di bilancio</h5>
	<div class="control-group">
		<label for="titoloEntrataEntrataAggiornamento" class="control-label">Titolo *</label>
		<div class="controls">
			<s:select id="titoloEntrataEntrataAggiornamento" list="listaTitoloEntrata" cssClass="span10" name="dettaglioEntrataCronoprogramma.titoloEntrata.uid" headerKey="" headerValue="Selezionare il Titolo" listKey="uid" listValue="%{codice + '-' + descrizione}" required="required" />
		</div>
	</div>
	<div class="control-group">
		<label for="tipologiaTitoloEntrataAggiornamento" class="control-label">Tipologia *</label>
		<div class="controls">
			<s:select id="tipologiaTitoloEntrataAggiornamento" list="listaTipologiaTitolo" cssClass="span10" name="dettaglioEntrataCronoprogramma.tipologiaTitolo.uid" headerKey="" headerValue="Selezionare la Tipologia" listKey="uid" listValue="%{codice + '-' + descrizione}" required="required" />
		</div>
	</div>
</fieldset>