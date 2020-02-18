<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div id="editQuadroEconomico" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="row-fluid">
		<div class="overlay-modale">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h3 id="titoloModaleEditQuadroEconomico"></h3><%--Modifica Stanziamenti Capitolo/UEB xxxxxxxxxxx</h3>--%>
			</div>
			<div class="modal-body">
				<div class="alert alert-error alert-persistent hide conceal-on-show" id="ERRORI_modaleEditQuadroEconomico">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<strong>Attenzione!!</strong><br>
						<ul>
						</ul>
				</div>
				<div class="alert alert-success hide conceal-on-show" id="INFORMAZIONI_modaleEditQuadroEconomico">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<strong>Informazioni</strong><br>
					<ul>
					</ul>
				</div>
				
				<fieldset class="form-horizontal" id="fieldsetInserimentoAggiornamento">
					<h4 class="step-pane">Dati Quadro Economico</h4>
					
					<div class="control-group">
						<label class="control-label" for="codiceQuadroEconomicoModal">Codice</label>
						<div class="controls">
							<input type="text" id="codiceQuadroEconomicoModal" name="quadroEconomico.codice" class="span6"/>
						</div>
					</div>
					
					<div class="control-group">
						<label class="control-label" for="descrizioneQuadroEconomicoModal">Descrizione </label>
						<div class="controls">
							<input type="text" id="descrizioneQuadroEconomicoModal" name="quadroEconomico.descrizione"/>

								<span class="alRight">
									<label for="parteQuadroEconomico" class="radio inline">Parte </label>
								</span>
								<%-- 
									<s:select id="parteQuadroEconomico" list="parteQuadroEconomico" name="quadroEconomico.parteQuadroEconomico" required="false" headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" />
								--%>
								<s:select id="parteQuadroEconomico" list="parteQuadroEconomico" name="quadroEconomico.parteQuadroEconomico" required="false" listValue="%{codice + ' - ' + descrizione}" />

								<span class="alRight">
									<label for="statoOperativiQuadroEconomicoModal" class="radio inline">Stato </label>
								</span>
								
								<input id ="statoOperativiQuadroEconomicoModal" type="text" disabled="disabled" />
								<input id ="HIDDEN_statoOperativiQuadroEconomicoModal" name="descrizioneStatoOperativoQuadroEconomico" type="hidden" value="" />
						</div>
					</div>
					
					
					<div class="clear"></div> 
				</fieldset>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">chiudi</button>
				<button  type = "button" class="btn btn-primary hide conceal-on-show" id="EDIT_inserisci">inserisci</button>
				<button  type = "button" class="btn btn-primary hide conceal-on-show" id="EDIT_aggiorna">aggiorna</button>
			</div>
		</div>
	</div>
</div>