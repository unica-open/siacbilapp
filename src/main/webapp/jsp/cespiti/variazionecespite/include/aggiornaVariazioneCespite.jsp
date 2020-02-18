<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="guidaVariazioneLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleAggiornaVariazioneCespite">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane" id="guidaVariazioneLabel">Aggiorna variazione</h4>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="erroriVariazioneModale">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<h4 id="intestazioneDatiVariazione"></h4>
		<fieldset class="form-horizontal" id="fieldsetDatiVariazione">
			<div class="control-group">
				<input type="hidden" id="uidVariazioneCespite" name="variazioneCespite.uid" />
				<label for="annoVariazioneCespite" class="control-label">Anno *</label>
				<div class="controls">
					<input id="annoVariazioneCespite" class="span2" readonly disabled="disabled" required name="variazioneCespite.annoVariazione" type="text" />
					<s:hidden id="HIDDEN_annoVariazioneCespite" name="variazioneCespite.annoVariazione"/>
					<span class="alRight">
						<label for="dataInserimentoVariazioneCespite" class="radio inline">Data inserimento *</label>
					</span>
					<s:textfield id="dataInserimentoVariazioneCespite" name="variazioneCespite.dataVariazione" class="datepicker span2"  disabled="true" readonly="true" type="text" data-date="" />
					<s:hidden id="HIDDEN_dataVariazioneCespite" name="variazioneCespite.dataVariazione"/>
				</div>
			</div>
			<div class="control-group">
				<label for="descrizioneVariazione" class="control-label">Descrizione *</label>
				<div class="controls">
					<textarea id="descrizioneVariazione" name="variazioneCespite.descrizione" rows="2" cols="20" class="span6" maxlength="500" required></textarea>
				</div>
			</div>
			<div class="control-group">
				<label for="tipoVariazioneCespite" class="control-label">Tipo Variazione</label>
				<div class="controls">
					<select id="tipoVariazioneCespite" name="variazioneCespite.tipoVariazione" class="span6" required disabled>
						<option selected><s:property value="testoSelectTipoVariazione" /></option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label for="importoVariazioneCespite" class="control-label">Importo *</label>
				<div class="controls">
					<input id="importoVariazioneCespite" class="span2 soloNumeri decimale text-right" required name="variazioneCespite.importo" type="text" data-importo />
					<span class="alRight">
						<label for="nuovoValoreAttualeVariazioneCespite" class="radio inline">Nuovo valore attuale</label>
					</span>
					<input id="nuovoValoreAttualeVariazioneCespite" disabled class="text-right span2" required value="" type="text" />
				</div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button class="btn btn-primary" id="pulsanteAggiornaVariazioneModale">
			conferma
			&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteAggiornaVariazioneModale"></i>
		</button>
	</div>
</div>
