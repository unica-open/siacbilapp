<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="aggiornaPdC" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="aggiornaPdCLabel" aria-hidden="true">
	<%-- <s:hidden id="HIDDEN_UidDaAggiornare" name="uidDaAnnullare" /> --%>
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h3 id="labelModaleAggiornaPdC">Aggiorna Piano dei conti finanziario</h3>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modaleAggiornaPdC">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<input type="hidden" id="HIDDEN_RegistrazioneUidFin" name="" value="" />
		<div id ="campiSpesa">
			<div class="control-group">
				<label for="titoloSpesa" class="control-label">Titolo</label>
				<div class="controls">
					<s:select id="titoloSpesa" list="listaTitoloSpesa" name="titoloSpesa.uid" cssClass="span10" headerKey=""
						headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
				</div>
			</div>
			<div class="control-group">
				<label for="macroaggregato" class="control-label">Macroaggregato
					<a class="tooltip-test" title="selezionare prima il Titolo" href="#"> <i class="icon-info-sign">&nbsp; <span
							class="nascosto">selezionare prima il Titolo spesa</span>
					</i>
				</a>
				</label>
				<div class="controls">
					<div class="controls">
							<select id="macroaggregato" name="macroaggregato.uid" class="span10" data-noclassepdc=""  data-campospesa="" disabled></select>
						</div>
				</div>
			</div>
		</div>
		<div id ="campiEntrata">
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
		<%-- zTree --%>
			<!-- <div class="control-group margin-medium form-horizontal" id="divRisultatiContoFIN"> -->
				<label class="control-label">V Livello P.d.C. finanziario</label>
				<input type="hidden" id="HIDDEN_ElementoPianoDeiContiUidFIN" name="conto.pianoDeiConti.uid" value="${elementoPianoDeiConti.uid}" />
				<input type="hidden" id="HIDDEN_ElementoPianoDeiContiCodiceFIN" name="conto.pianoDeiConti.codice" value="${elementoPianoDeiConti.codice}" />
				<s:hidden id="HIDDEN_ElementoPianoDeiContiStringaFIN" name="pdcFinanziario" />
				<div class="controls">
					<div class="accordion span10" class="CodCCFinanziario">
						<div class="accordion-group">
							<div class="accordion-heading">
								<a data-noclassepdc="" id="bottonePdCFIN" class="accordion-toggle" data-toggle="collapse" data-parent="#CodCCFinanziario" href="#PDCfinResult" >
									<span id="SPAN_ElementoPianoDeiContiFIN">
										Aggiorna il piano dei Conti
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
			<!-- </div> -->
			<%-- zTree --%>
		<!-- </fieldset> -->
	</div>
	<div class="modal-footer">
		<button type="button" id="chiudiModaleAggiornaPdc" aria-hidden="true" data-dismiss="modal" class="btn btn-secondary">chiudi</button>
		<button type="button" class="btn btn-primary" id="confermaModaleAggiornaPdC">
			conferma&nbsp;<i id ="spinner" class="icon-spin icon-refresh spinner"></i>
		</button>
	</div>
</div>