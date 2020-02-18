<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div class="accordion_info">
	<fieldset id="fieldsetDettaglioUscita" class="form-horizontal">
		<div class="alert alert-success hide" id="alertAssociazioneCapitoloUscita">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<ul></ul>
		</div>
		<s:if test ="collegatoAProgettoDiGestione">
			<h5 class="step-pane">Quadro economico </h5>
			<div class="control-group">
				<label class="control-label">Quadro </label>
				<div class="controls">
					<div class="accordion span8 quadroEconomico">
						<div class="accordion-group">
							<div class="accordion-heading">
									<a class="accordion-toggle" id="accordionPadreQuadroEconomico" href="#">
										<span id="SPAN_QuadroEconomico">Seleziona il quadro economico</span>
									</a>
							</div>
							<div id="quadroEconomico" class="accordion-body collapse">
								<div class="accordion-inner">
									<ul id="treeQuadroEconomico" class="ztree"></ul>
								</div>
							</div>
						</div>
					</div>
					<s:hidden id="HIDDEN_QuadroEconomicoUid" name="dettaglioUscitaCronoprogramma.quadroEconomico.uid" />
				</div>
			</div>
			<%-- <div class="control-group">
				<label for="importoQuadroEconomicoUscita" class="control-label">Importo *</label>
				<div class="controls">
					<s:textfield id="importoQuadroEconomicoUscita" name="dettaglioUscitaCronoprogramma.importoQuadroEconomico" placeholder="valore" disabled="true" required="required" cssClass="lbTextSmall span3 soloNumeri decimale" />
				</div>
			</div> --%>
		</s:if>
		
		<h5 class="step-pane">Impegno previsto </h5>
		<div class="control-group">
			<label for="descrizioneUscita" class="control-label">Descrizione Spesa *</label>
			<div class="controls">
				<s:textfield id="descrizioneUscita" name="dettaglioUscitaCronoprogramma.descrizioneCapitolo" placeholder="attività" cssClass="lbTextSmall span9" required="required" maxlength="500" />
			</div>
		</div>
		<div class="control-group">
			<label for="annoUscita" class="control-label">Anno spesa *</label>
			<div class="controls">
				<s:textfield id="annoUscita" name="dettaglioUscitaCronoprogramma.annoCompetenza" placeholder="anno" required="required" cssClass="lbTextSmall span2 soloNumeri" maxlength="4" />
			</div>
		</div>
		<div class="control-group">
			<label for="annoUscitaEntrata" class="control-label">Anno entrata *</label>
			<div class="controls">
				<s:textfield id="annoUscitaEntrata" name="dettaglioUscitaCronoprogramma.annoEntrata" placeholder="anno" required="required" cssClass="lbTextSmall span2 soloNumeri" maxlength="4" />
			</div>
		</div>
		<div class="control-group">
			<label for="valorePrevistoUscita" class="control-label">Valore previsto *</label>
			<div class="controls">
				<s:textfield id="valorePrevistoUscita" name="dettaglioUscitaCronoprogramma.stanziamento" placeholder="valore" required="required" cssClass="lbTextSmall span3 soloNumeri decimale" />
			</div>
		</div>
		<div class="control-group">
			<label for="numeroCapitoloUscita" class="control-label">Capitolo</label>
			<div class="controls">
				<s:hidden id="uidCapitoloUscita" name="dettaglioUscitaCronoprogramma.capitolo.uid" />
				<s:textfield id="numeroCapitoloUscita" cssClass="span2 soloNumeri" name="dettaglioUscitaCronoprogramma.numeroCapitolo" placeholder="%{'capitolo'}" maxlength="9"
					readonly="%{dettaglioUscitaCronoprogramma.numeroArticolo != null}" />
				<s:textfield id="numeroArticoloUscita" cssClass="span2 soloNumeri" name="dettaglioUscitaCronoprogramma.numeroArticolo" placeholder="%{'articolo'}" maxlength="9"
					readonly="%{dettaglioUscitaCronoprogramma.numeroArticolo != null}" />
				<s:if test="gestioneUEB">
					<s:textfield id="numeroUEBUscita" cssClass="span2 soloNumeri" name="dettaglioUscitaCronoprogramma.numeroUEB" placeholder="%{'UEB'}" maxlength="9"
						readonly="%{dettaglioUscitaCronoprogramma.numeroArticolo != null}" />
				</s:if><s:else>
					<s:hidden id="numeroUEBUscita" name="dettaglioUscitaCronoprogramma.numeroUEB" />
				</s:else>
				<span class="alRight">
					<label class="radio inline">Da esistente</label>
					<label class="radio inline">
						<input type="radio" value="si" id="daEsistenteUscitaSi" name="daEsistenteUscita" />
						s&iacute;
					</label>
					<label class="radio inline">
						<input type="radio" value="no" id="daEsistenteUscitaNo" name="daEsistenteUscita" checked />
						no
					</label>
				</span>
				<span id="datiRiferimentoCapitoloUscitaSpan" class="radio inline"></span>
			</div>
		</div>

		<h5 class="step-pane">Classificazione di bilancio</h5>
		<div class="control-group">
			<label class="control-label" for="missioneUscita">Missione *</label>
			<div class="controls">
				<s:select id="missioneUscita" list="listaMissione" name="dettaglioUscitaCronoprogramma.missione.uid" cssClass="span10"
					required="required" listKey="uid" listValue="%{codice + '-' + descrizione}" headerKey="" headerValue="Seleziona la missione" />
				<s:hidden id="HIDDEN_missioneUscita" name="dettaglioUscitaCronoprogramma.missione.uid" disabled="true" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="programmaUscita">Programma *</label>
			<div class="controls">
				<s:select id="programmaUscita" list="listaProgramma" name="dettaglioUscitaCronoprogramma.programma.uid" cssClass="span10"
					required="required" listKey="uid" listValue="%{codice + '-' + descrizione}" headerKey="" headerValue="Seleziona il programma" disabled="true" />
				<s:hidden id="HIDDEN_programmaUscita" name="dettaglioUscitaCronoprogramma.programma.uid" disabled="true" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="titoloSpesaUscita">Titolo *</label>
			<div class="controls">
				<s:select id="titoloSpesaUscita" list="listaTitoloSpesa" name="dettaglioUscitaCronoprogramma.titoloSpesa.uid" cssClass="span10"
					required="required" listKey="uid" listValue="%{codice + '-' + descrizione}" headerKey="" headerValue="Seleziona il titolo" />
				<s:hidden id="HIDDEN_titoloSpesaUscita" name="dettaglioUscitaCronoprogramma.titoloSpesa.uid" disabled="true" />
			</div>
		</div>
	</fieldset>

	<p>
		<a data-close-collapse="#collapseDettaglioUscita" class="btn btn-secondary" href="#">annulla inserimento</a>
		<a data-url-fragment="InserisciDettaglioCronoprogrammaUscita" data-close-collapse="#collapseDettaglioUscita" class="btn btn-secondary" href="#" data-save-button>
			inserisci&nbsp;<i class="icon-spin icon-refresh spinner"></i>
		</a>
	</p>
</div>