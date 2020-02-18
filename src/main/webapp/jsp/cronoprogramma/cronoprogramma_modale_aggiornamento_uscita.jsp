<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Inserito nel modal-body --%>
<div class="alert alert-error hide" id="ERRORI_USCITA">
	<button type="button" class="close" data-hide="alert">&times;</button>
	<strong>Attenzione!!</strong><br>
	<ul></ul>
</div>
<fieldset class="form-horizontal" id="fieldsetDettaglioUscitaAggiornamento">
	<div class="alert alert-success hide" id="alertAssociazioneCapitoloUscitaAggiornamento">
		<button type="button" class="close" data-hide="alert">&times;</button>
		<ul></ul>
	</div>
	<div class="control-group">
		<h5 class="step-pane">Impegno previsto</h5>
		<label class="control-label" for="descrizioneUscitaAggiornamento">Descrizione spesa *</label>
		<div class="controls">
			<s:textfield id="descrizioneUscitaAggiornamento" cssClass="span10" name="dettaglioUscitaCronoprogramma.descrizioneCapitolo" placeholder="attività" required="required" maxlength="500" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="annoUscitaAggiornamento">Anno spesa *</label>
		<div class="controls">
			<s:textfield id="annoUscitaAggiornamento" cssClass="span2 soloNumeri" name="dettaglioUscitaCronoprogramma.annoCompetenza" placeholder="anno" required="required" maxlength="4" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="annoUscitaEntrataAggiornamento">Anno entrata *</label>
		<div class="controls">
			<s:textfield id="annoUscitaEntrataAggiornamento" cssClass="span2 soloNumeri" name="dettaglioUscitaCronoprogramma.annoEntrata" placeholder="anno" required="required" maxlength="4" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="valorePrevistoUscitaAggiornamento">Valore previsto *</label>
		<div class="controls">
			<s:textfield id="valorePrevistoUscitaAggiornamento" cssClass="span3 soloNumeri decimale" name="dettaglioUscitaCronoprogramma.stanziamento" placeholder="valore" required="required" />
		</div>
	</div>
	<div class="control-group">
		<label for="numeroCapitoloUscitaAggiornamento" class="control-label">Capitolo</label>
		<div class="controls">
			<s:textfield id="numeroCapitoloUscitaAggiornamento" cssClass="span3 soloNumeri" name="dettaglioUscitaCronoprogramma.numeroCapitolo" placeholder="%{'capitolo'}" maxlength="7" />
			<span class="alRight">
				<label class="radio inline">Da esistente</label>
				<label class="radio inline">
					<input type="radio" value="si" id="daEsistenteUscitaSiAggiornamento" name="daEsistenteUscitaAggiornamento" <s:if test="%{dettaglioUscitaCronoprogramma.capitoloUscitaPrevisione != null && dettaglioUscitaCronoprogramma.capitoloUscitaPrevisione.uid > 0}">checked="checked"</s:if> />
					s&iacute;
				</label>
				<label class="radio inline">
					<input type="radio" value="no" id="daEsistenteUscitaNoAggiornamento" name="daEsistenteUscitaAggiornamento">
					no
				</label>
			</span>
		</div>
	</div>
		
	<div id="campiArticoloUEBUscitaAggiornamento" class="control-group <s:if test="%{dettaglioUscitaCronoprogramma.capitoloUscitaPrevisione == null || dettaglioUscitaCronoprogramma.capitoloUscitaPrevisione.uid == 0}">hide</s:if>">
		<label for="numeroArticoloUscitaAggiornamento" class="control-label">Articolo</label>
		<div class="controls">
			<s:textfield id="numeroArticoloUscitaAggiornamento" cssClass="span2 soloNumeri" name="dettaglioUscitaCronoprogramma.numeroArticolo" placeholder="articolo" maxlength="7" />
			<s:if test="%{gestioneUEB}">
				<span class="alRight">
					 <label class="radio inline" for="numeroUEBUscitaAggiornamento">UEB</label>
				</span>
				<s:textfield id="numeroUEBUscitaAggiornamento" cssClass="span2 soloNumeri" name="dettaglioUscitaCronoprogramma.numeroUEB" placeholder="UEB" />
			</s:if><s:else>
				<input type="hidden" name="dettaglioUscitaCronoprogramma.numeroUEB" id="numeroUEBUscitaAggiornamento" value="1" data-maintain=""/>
			</s:else>
			<s:hidden name="dettaglioUscitaCronoprogramma.capitoloUscitaPrevisione.uid" id="uidCapitoloAssociatoUscitaAggiornamento" data-maintain="" />
			<span class="alLeft">
				<a class="btn btn-primary" id="pulsanteRicercaCapitoloUscitaAggiornamento">
					<i class="icon-search icon"></i>&nbsp;cerca
				</a>
			</span>
		</div>
	</div>
	
	<h5 class="step-pane">Classificazione di bilancio</h5>     
	<div class="control-group">
		<label for="missioneUscitaAggiornamento" class="control-label">Missione *</label>
		<div class="controls">
			<s:select id="missioneUscitaAggiornamento" list="listaMissione" cssClass="span10" name="dettaglioUscitaCronoprogramma.missione.uid" headerKey="" headerValue="Selezionare la Missione" listKey="uid" listValue="%{codice + '-' + descrizione}" required="required" />
		</div>
	</div>
	<div class="control-group">
		<label for="programmaUscitaAggiornamento" class="control-label">Programma *</label>
		<div class="controls">
			<s:select id="programmaUscitaAggiornamento" list="listaProgramma" cssClass="span10" name="dettaglioUscitaCronoprogramma.programma.uid" headerKey="" headerValue="Selezionare il Programma" listKey="uid" listValue="%{codice + '-' + descrizione}" required="required" />
		</div>
	</div>
	<div class="control-group">
		<label for="titoloSpesaUscitaAggiornamento" class="control-label">Titolo *</label>
		<div class="controls">
			<s:select id="titoloSpesaUscitaAggiornamento" list="listaTitoloSpesa" cssClass="span10" name="dettaglioUscitaCronoprogramma.titoloSpesa.uid" headerKey="" headerValue="Selezionare il Titolo" listKey="uid" listValue="%{codice + '-' + descrizione}" required="required" />
		</div>
	</div>
</fieldset>