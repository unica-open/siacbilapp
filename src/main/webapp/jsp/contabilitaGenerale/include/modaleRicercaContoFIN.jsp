<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="comp-codContoFIN" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h3 id="myModalLabel">Codice Conto Finanziario</h3>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_MODALE_CONTOFIN">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<fieldset class="form-horizontal" id="fieldsetModaleRicercaContoFIN">
			<div id="campiCollegaConto" class="accordion-body collapse in">
				<div class="control-group">
					<div class="controls">
						<label class="radio inline">
							<input type="radio" name="optionContoFIN" value="entrata" data-noclassepdc="" data-entrata=""> Entrata
						</label>
						<label class="radio inline">
							<input type="radio" name="optionContoFIN" value="spesa"  data-noclassepdc="" data-spesa=""> Spesa
						</label>
					</div>
				</div>
				<div class="campiEntrata" id="campiEntrataFIN">
					<div class="control-group">
						<label class="control-label" for="titoloEntrataFIN">Titolo</label>
						<div class="controls">
							<s:select list="listaTitoloEntrata" id="titoloEntrataFIN" cssClass="span10"
								name="titoloEntrata.uid" headerKey="" headerValue=""
								listKey="uid" listValue="%{codice + '-' + descrizione}" data-noclassepdc="" data-campoentrata="" />
						</div>
					</div>

					<div class="control-group">
						<label class="control-label" for="tipologiaTitoloFIN">Tipologia &nbsp;<a class="tooltip-test" data-original-title="Selezionare prima il Titolo"><i class="icon-info-sign">&nbsp;<span class="nascosto">Selezionare prima il Titolo</span></i></a></label>
						<div class="controls">
							<select id="tipologiaTitoloFIN" class="span10" data-noclassepdc="" data-campoentrata="" disabled></select>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="categoriaTipologiaTitoloFIN">Categoria &nbsp;<a class="tooltip-test" data-original-title="Selezionare prima la Tipologia"><i class="icon-info-sign">&nbsp;<span class="nascosto">Selezionare prima la Tipologia</span></i></a></label>
						<div class="controls">
							<select id="categoriaTipologiaTitoloFIN" name="categoriaTipologiaTitolo.uid" class="span10" data-noclassepdc="" data-campoentrata="" disabled></select>
						</div>
					</div>
				</div>
				
				<div class="campiSpesa" id="campiSpesaFIN">
					<div class="control-group">
						<label class="control-label" for="titoloSpesaFIN">Titolo</label>
						<div class="controls">
							<s:select list="listaTitoloSpesa" id="titoloSpesaFIN" cssClass="span10"
									name="titoloSpesa.uid" headerKey="" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" data-noclassepdc="" data-campospesa="" />
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label" for="macroaggregatoFIN">Macroaggregato &nbsp;<a class="tooltip-test" data-original-title="Selezionare prima il Titolo"><i class="icon-info-sign">&nbsp;<span class="nascosto">Selezionare prima il Titolo</span></i></a></label>
						<div class="controls">
							<select id="macroaggregatoFIN" name="macroaggregato.uid" class="span10" data-noclassepdc=""  data-campospesa="" disabled></select>
						</div>
					</div>
				</div>
			</div>
			<button type="button" class="btn btn-primary pull-right" id="bottoneCercaModaleRicercaPDCFIN">
				<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="spinnerModaleRicercaPDCFIN"></i>
			</button>
		</fieldset>

		<div class="clear"></div>
		<div class="control-group margin-medium form-horizontal" id="divRisultatiContoFIN">
			<label class="control-label">V Livello P.d.C. finanziario</label>
			<input type="hidden" id="HIDDEN_ElementoPianoDeiContiUidFIN" name="conto.pianoDeiConti.uid" value="${elementoPianoDeiConti.uid}" />
			<input type="hidden" id="HIDDEN_ElementoPianoDeiContiCodiceFIN" name="conto.pianoDeiConti.codice" value="${elementoPianoDeiConti.codice}" />
			<s:hidden id="HIDDEN_ElementoPianoDeiContiStringaFIN" name="pdcFinanziario" />
			<div class="controls">
				<div class="accordion span12" class="CodCCFinanziario">
					<div class="accordion-group">
						<div class="accordion-heading">
							<a data-noclassepdc="" id="bottonePdCFIN" class="accordion-toggle" data-toggle="collapse" data-parent="#CodCCFinanziario" href="#PDCfinResult" >
								<span id="SPAN_ElementoPianoDeiContiFIN">
									<s:if test='%{pdcFinanziario != null && pdcFinanziario neq ""}'>
										<s:property value="pdcFinanziario"/>
									</s:if><s:else>
										Seleziona il conto finanziario
									</s:else>
								</span>
							</a>
						</div>
						<div id="PDCfinResult" class="accordion-body collapse">
							<div class="accordion-inner">
								<ul id="treePDCFIN" class="ztree"></ul>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<div class="modal-footer">
		<button class="btn btn-secondary" id="pulsanteDeselezionaModaleRicercaContoFIN">deseleziona</button>
		<button class="btn btn-primary" aria-hidden="true" id="pulsanteConfermaModaleRicercaContoFIN">conferma</button>
	</div>

</div>
