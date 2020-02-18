<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="editStanziamenti" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="row-fluid">
		<div class="overlay-modale">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h3 id="titoloModaleVariazioneStanziamenti"></h3><%--Modifica Stanziamenti Capitolo/UEB xxxxxxxxxxx</h3>--%>
			</div>
			<div class="modal-body">
				<div class="alert alert-error alert-persistent hide" id="ERRORI_modaleEditStanziamenti">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<strong>Attenzione!!</strong><br>
						<ul>
						</ul>
				</div>
				<fieldset class="form-horizontal">
					<div class="control-group">
						<label class="control-label" for="competenzaVariazioneModal">Competenza</label>
						<div class="controls">							
							<s:textfield id="competenzaVariazioneModal" disabled="true" cssClass="lbTextSmall span6 text-right decimale soloNumeri" name="competenzaVariazione" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="residuoVariazioneModal">Residuo</label>
						<div class="controls">
							<s:textfield id="residuoVariazioneModal" disabled="true" cssClass="lbTextSmall span6 text-right decimale soloNumeri" name="residuoVariazione" />
							
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" for="cassaVariazioneModal">Cassa</label>
						<div class="controls">
							<s:textfield id="cassaVariazioneModal" disabled="true" cssClass="lbTextSmall span6 text-right decimale soloNumeri" name="cassaVariazione" />							
						</div>
					</div>
				</fieldset>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">chiudi</button>				
			</div>
		</div>
	</div>
</div>