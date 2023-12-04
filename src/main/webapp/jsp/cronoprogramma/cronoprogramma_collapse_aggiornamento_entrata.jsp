<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div class="accordion_info">
	<fieldset id="fieldsetDettaglioEntrata" class="form-horizontal">
		<div class="alert alert-success hide" id="alertAssociazioneCapitoloEntrata">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<ul></ul>
		</div>
		<h5 class="step-pane">Accertamento previsto</h5>
		<div class="control-group">
			<label for="descrizioneEntrata" class="control-label">Descrizione Entrata *</label>
			<div class="controls">
				<s:textfield id="descrizioneEntrata" name="dettaglioEntrataCronoprogramma.descrizioneCapitolo" placeholder="finanziamento" cssClass="lbTextSmall span9" required="required" maxlength="500" />
			</div>
		</div>
		<div class="control-group">
			<label for="annoEntrata" class="control-label">Anno *</label>
			<div class="controls">
				<s:textfield id="annoEntrata" name="dettaglioEntrataCronoprogramma.annoCompetenza" placeholder="anno" required="required" cssClass="lbTextSmall span2 soloNumeri" maxlength="4" />
			</div>
		</div>
		<div class="control-group">
			<label for="valorePrevistoEntrata" class="control-label">Valore previsto *</label>
			<div class="controls">
				<s:textfield id="valorePrevistoEntrata" name="dettaglioEntrataCronoprogramma.stanziamento" placeholder="valore" required="required" cssClass="lbTextSmall span3 soloNumeri decimale" />
			</div>
		</div>
		<div class="control-group">
			<label for="numeroCapitoloEntrata" class="control-label">Capitolo</label>
			<div class="controls">
				<s:textfield id="numeroCapitoloEntrata" cssClass="span2 soloNumeri" name="dettaglioEntrataCronoprogramma.numeroCapitolo" placeholder="%{'capitolo'}" maxlength="9"
					readonly="dettaglioEntrataFromEsistente" />
				<s:textfield id="numeroArticoloEntrata" cssClass="span2 soloNumeri" name="dettaglioEntrataCronoprogramma.numeroArticolo" placeholder="%{'articolo'}" maxlength="9"
					readonly="dettaglioEntrataFromEsistente" />
				<s:if test="gestioneUEB">
					<s:textfield id="numeroUEBEntrata" cssClass="span2 soloNumeri" name="dettaglioEntrataCronoprogramma.numeroUEB" placeholder="%{'UEB'}" maxlength="9"
						readonly="dettaglioEntrataFromEsistente" />
				</s:if><s:else>
					<s:hidden id="numeroUEBEntrata" name="dettaglioEntrataCronoprogramma.numeroUEB" />
				</s:else>
				<!-- SIAC-8791 -->
				<!-- s:hidden id="uidCapitoloEntrata" name="dettaglioEntrataCronoprogramma.capitoloEntrata.uid" />-->
				<s:hidden id="uidCapitoloEntrata" name="dettaglioEntrataCronoprogramma.capitolo.uid" />
				<span class="alRight">
					<label class="radio inline">Da esistente</label>
					<label class="radio inline">
						<input type="radio" value="si" id="daEsistenteEntrataSi" name="daEsistenteEntrata" <s:if test="dettaglioEntrataFromEsistente">checked</s:if> />
						s&iacute;
					</label>
					<label class="radio inline">
						<input type="radio" value="no" id="daEsistenteEntrataNo" name="daEsistenteEntrata" <s:if test="!dettaglioEntrataFromEsistente">checked</s:if> />
						no
					</label>
				</span>
				<span id="datiRiferimentoCapitoloEntrataSpan" class="radio inline"></span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Avanzo di amministrazione *</label>
			<div class="controls">
				<label class="radio inline">
					<input type="radio" value="true" name="dettaglioEntrataCronoprogramma.isAvanzoAmministrazione" <s:if test="dettaglioEntrataCronoprogramma.isAvanzoAmministrazione">checked</s:if> <s:if test="dettaglioEntrataFromEsistente">disabled</s:if>>S&iacute;
				</label>
				<label class="radio inline">
					<input type="radio" value="false" name="dettaglioEntrataCronoprogramma.isAvanzoAmministrazione" <s:if test="!dettaglioEntrataCronoprogramma.isAvanzoAmministrazione">checked</s:if> <s:if test="dettaglioEntrataFromEsistente">disabled</s:if>>No
				</label>
			</div>
		</div>
		<h5 class="step-pane">Classificazione di bilancio</h5>
		<div class="control-group">
			<label class="control-label" for="titoloEntrataEntrata">Titolo *</label>
			<div class="controls">
				<s:select id="titoloEntrataEntrata" list="listaTitoloEntrata" name="dettaglioEntrataCronoprogramma.titoloEntrata.uid" cssClass="span10"
					required="required" listKey="uid" listValue="%{codice + '-' + descrizione}" headerKey="" headerValue="Seleziona il titolo"
					disabled="%{dettaglioEntrataCronoprogramma.numeroArticolo != null}" />
				<s:hidden id="HIDDEN_titoloEntrataEntrata" name="dettaglioEntrataCronoprogramma.titoloEntrata.uid" disabled="%{dettaglioEntrataCronoprogramma.numeroArticolo == null}" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="tipologiaTitoloEntrata">Tipologia *</label>
			<div class="controls">
				<s:select id="tipologiaTitoloEntrata" list="listaTipologiaTitolo" name="dettaglioEntrataCronoprogramma.tipologiaTitolo.uid" cssClass="span10"
					required="required" listKey="uid" listValue="%{codice + '-' + descrizione}" headerKey="" headerValue="Seleziona la tipologia"
					disabled="%{dettaglioEntrataCronoprogramma.numeroArticolo != null}" />
				<s:hidden id="HIDDEN_tipologiaTitoloEntrata" name="dettaglioEntrataCronoprogramma.tipologiaTitolo.uid" disabled="%{dettaglioEntrataCronoprogramma.numeroArticolo == null}" />
			</div>
		</div>
	</fieldset>

	<p>
		<a data-close-collapse="#collapseDettaglioEntrata" class="btn btn-secondary" href="#">annulla aggiornamento</a>
		<a data-url-fragment="AggiornaDettaglioCronoprogrammaEntrata" data-close-collapse="#collapseDettaglioEntrata" class="btn btn-secondary" href="#" data-save-button>
			inserisci&nbsp;<i class="icon-spin icon-refresh spinner"></i>
		</a>
	</p>
</div>