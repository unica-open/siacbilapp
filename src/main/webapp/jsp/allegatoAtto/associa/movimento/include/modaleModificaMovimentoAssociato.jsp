<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="editMovimento" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="row-fluid">
		<div class="overlay-modale">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h3> Aggionramento dati movimento: <span id="titoloModaleMovimento"></span></h3>
			</div>
			<div class="modal-body">
				<div class="alert alert-error alert-persistent hide" id="ERRORI_modaleEditMovimento">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<strong>Attenzione!!</strong><br>
						<ul>
						</ul>
				</div>
				<fieldset class="form-horizontal">
					<div class="control-group">
						<label class="control-label" for="cigMovimentoGestioneModale">CIG</label>
						<div class="controls">
							<input type="text" class="span3" data-force-uppercase="" data-allowed-chars="[A-Za-z0-9]" id="cigMovimentoGestioneModale" maxlength="10">
							<span class="al" data-assenza-cig>
								<label for="siopeAssenzaMotivazioneModale" class="radio inline">Motivo di assenza CIG</label>
							</span>
							<s:select data-assenza-cig="" list="listaSiopeAssenzaMotivazione" id="siopeAssenzaMotivazioneModale" cssClass="span4"
								headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="false"/>
						</div>
					</div> 
					<div class="control-group">
						<label class="control-label" for="cupMovimentoGestioneModale">CUP</label>
						<div class="controls">
							<input type="text" class="span3" data-force-uppercase="" data-allowed-chars="[A-Za-z0-9]" id="cupMovimentoGestioneModale" maxlength="15" value="">
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="importoInAttoModale">Importo</label>
						<div class="controls">
							<input type="text" class="span2 soloNumeri decimale" id="importoInAttoModale">
						</div>
					</div>
				</fieldset>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">chiudi</button>
				<button type="button" id="confermaAggiornaMovimentoModale" class="btn btn-primary">salva</button>			
			</div>
		</div>
	</div>
</div>