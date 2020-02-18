<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<s:set name="prefix"><c:out value="${param.prefix}" default="" /></s:set>
<s:set name="title"><c:out value="${param.title}" default="" /></s:set>
<s:set name="aggiornamento"><c:out value="${param.aggiornamento}" default="false" /></s:set>

<div class="accordion_info">
	<fieldset class="form-horizontal" id="<s:property value="#prefix"/>_fieldsetConciliazioni">
		<h4 class="titleTxt nostep-pane"><s:property value="#title"/></h4>
		<h4 class="step-pane">Dati</h4>
		<s:if test="#aggiornamento">
			<s:hidden name="conciliazionePerTitolo.uid" id="%{#prefix}_uidConciliazionePerTitolo_hidden" />
		</s:if>
		<div class="control-group">
			<label class="control-label" for="<s:property value="#prefix"/>_classeDiConciliazioneConciliazionePerTitolo">Classe di conciliazione *</label>
			<div class="controls">
				<s:select list="listaClasseDiConciliazione" name="conciliazionePerTitolo.classeDiConciliazione" id="%{#prefix}_classeDiConciliazioneConciliazionePerTitolo"
					cssClass="span6" headerKey="" headerValue="" listValue="descrizione" required="true" disabled="#aggiornamento" />
				<s:if test="#aggiornamento">
					<s:hidden id="%{#prefix}_classeDiConciliazioneConciliazionePerTitolo_hidden" name="conciliazionePerTitolo.classeDiConciliazione" />
				</s:if>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Tipo *</label>
			<div class="controls">
				<span class="al">
					<label class="radio inline">
						<input type="radio" value="S" name="entrataSpesa" <s:if test="#aggiornamento">disabled</s:if> data-maintain>Spese
					</label>
				</span>
				<span class="al">
					<label class="radio inline">
						<input type="radio" value="E" name="entrataSpesa" <s:if test="#aggiornamento">disabled</s:if> data-maintain>Entrate
					</label>
				</span>
				<s:if test="#aggiornamento">
					<s:hidden id="%{#prefix}_entrataSpesa_hidden" name="entrataSpesa" />
				</s:if>
			</div>
		</div>
		<div data-tipo="S" class="hide">
			<div class="control-group">
				<label class="control-label" for="<s:property value="#prefix"/>_titoloSpesa">Titolo *</label>
				<div class="controls">
					<s:select list="listaTitoloSpesa" name="titoloSpesa.uid" id="%{#prefix}_titoloSpesa" headerKey="" headerValue=""
						listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="true" cssClass="span6" required="true" />
					<s:if test="#aggiornamento">
						<s:hidden id="%{#prefix}_titoloSpesa_hidden" name="titoloSpesa.uid" />
					</s:if>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="<s:property value="#prefix"/>_macroaggregato">Macroaggregato *</label>
				<div class="controls">
					<s:select list="listaMacroaggregato" name="macroaggregato.uid" id="%{#prefix}_macroaggregato"
						headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="true" cssClass="span6" required="true" />
					<s:if test="#aggiornamento">
						<s:hidden id="%{#prefix}_macroaggregato_hidden" name="macroaggregato.uid" />
					</s:if>
				</div>
			</div>
		</div>
		<div data-tipo="E" class="hide">
			<div class="control-group">
				<label class="control-label" for="<s:property value="#prefix"/>_titoloEntrata">Titolo *</label>
				<div class="controls">
					<s:select list="listaTitoloEntrata" name="titoloEntrata.uid" id="%{#prefix}_titoloEntrata" headerKey="" headerValue=""
						listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="true" cssClass="span6" required="true" />
					<s:if test="#aggiornamento">
						<s:hidden id="%{#prefix}_titoloEntrata_hidden" name="titoloEntrata.uid" />
					</s:if>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="<s:property value="#prefix"/>_tipologiaTitolo">Tipologia *</label>
				<div class="controls">
					<s:select list="listaTipologiaTitolo" name="tipologiaTitolo.uid" id="%{#prefix}_tipologiaTitolo" headerKey="" headerValue=""
						listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="true" cssClass="span6" required="true" />
					<s:if test="#aggiornamento">
						<s:hidden id="%{#prefix}_tipologiaTitolo_hidden" name="tipologiaTitolo.uid" />
					</s:if>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="<s:property value="#prefix"/>_categoriaTipologiaTitolo">Categoria *</label>
				<div class="controls">
					<s:select list="listaCategoriaTipologiaTitolo" name="categoriaTipologiaTitolo.uid" id="%{#prefix}_categoriaTipologiaTitolo"
						headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" disabled="true" cssClass="span6" required="true" />
					<s:if test="#aggiornamento">
						<s:hidden id="%{#prefix}_categoriaTipologiaTitolo_hidden" name="categoriaTipologiaTitolo.uid" />
					</s:if>
				</div>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="<s:property value="#prefix"/>_classePiano">Classe *</label>
			<div class="controls">
				<s:select list="listaClassePiano" name="classePiano.uid" id="%{#prefix}_classePiano" headerKey="" headerValue=""
					listKey="uid" listValue="%{codice + ' - ' + descrizione}" cssClass="span6" required="true" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="<s:property value="#prefix"/>_contoConciliazionePerTitolo">Conto *</label>
			<div class="controls">
				<s:textfield name="conciliazionePerTitolo.conto.codice" id="%{#prefix}_contoConciliazionePerTitolo" cssClass="span6" required="true" />
				&nbsp;<span id="<s:property value="#prefix"/>_descrizioneConto" data-descrizione-conto></span>
				<button type="button" class="btn btn-primary pull-right" id="<s:property value="#prefix"/>_pulsanteCompilazioneGuidataConto">compilazione guidata</button>
			</div>
		</div>
		<p> 
			<button type="button" class="btn btn-secondary" id="<s:property value="#prefix"/>_buttonAnnulla">annulla</button>
			<span class="pull-right">
				<button type="button" class="btn btn-primary" id="<s:property value="#prefix"/>_buttonSalva">salva</button>
			</span>
		</p>
	</fieldset>
</div>