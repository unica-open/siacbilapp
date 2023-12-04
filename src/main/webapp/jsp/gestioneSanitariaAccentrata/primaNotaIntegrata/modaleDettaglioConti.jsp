<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="modaleDettaglioContiLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleDettaglioConti">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h3 id="modaleDettaglioContiLabel">Selezionare <span class="hide info-aggiuntive-digitazione-selezione"> o digitare</span> il conto da sostituire alla classe di conciliazione</h3>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modaleDettaglio">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br/>
			<ul>
			</ul>
		</div>
		<fieldset id="fieldsetModaleSelezioneConto" class="form-horizontal">
			<s:hidden id="HIDDEN_indiceModale"/>
			 
			<div class="control-group" id ="selezioneContoGuidata" >
				<label class="control-label" for="contiConciliazionePerTitoloModale">Conto </label>
				<div class="controls">
					<select id="contiConciliazionePerTitoloModale" class="span6"></select>
				</div>
			</div>
			<div class="control-group" id ="selezioneContoTestuale" >			
				<label class="control-label" for="contoConciliazione">Conto </label>
				<div class="controls">
					<s:textfield id="contoConciliazione" name="conto.codice" cssClass="span6"/>
				</div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn btn-secondary">annulla</button>
		<button type="button" class="btn btn-primary" id="pulsanteSelezionaConto">
			conferma&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteSelezionaConto"></i>
		</button>
	</div>
</div>