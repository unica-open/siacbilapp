<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="comp-CodCespite" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h3 id="myModalLabel">Collega Cespite</h3>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modaleRicercaCespite">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<fieldset class="form-horizontal" id="fieldsetModaleRicercaCespite">
			<div id="campiRicercaCespite" class="accordion-body collapse in">
			
				<s:hidden id="HIDDEN_ambitoCespite" data-maintain="" value="%{ambito}" name="modaleRicercaCespite.ambito"/>
				<s:hidden id="HIDDEN_ambitoCespiteINV" data-maintain="" value="%{ambitoINV}" name="modaleRicercaCespite.ambitoINV"/>
			
				
				<div class="control-group">
					<label class="control-label"  for="codiceCespiteRicerca_modale">Numero Cespite</label>
					<div class="controls">
						<s:textfield id="codiceCespiteRicerca_modale" name="modaleRicercaCespite.cespite.codice" cssClass="span6" />
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="numeroInventarioCespite_modale">Numero Inventario</label>
					<div class="controls">
						<s:textfield id="numeroInventarioCespite_modale" name="modaleRicercaCespite.cespite.numeroInventario" cssClass="span6 soloNumeri" />
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="descrizioneRicerca_modale">Descrizione</label>
					<div class="controls">
						<s:textfield id="descrizioneRicerca_modale" name="modaleRicercaCespite.cespite.descrizione" cssClass="span6" />
					</div>
				</div>
								
				<div class="control-group">
					<label class="control-label" for="classeTipoBene_modale">Tipo Bene</label>
					<div class="controls">
						<s:select id="classeTipoBene_modale" name="modaleRicercaCespite.cespite.tipoBeneCespite.uid" list="listaTipoBeneCespite" cssClass="span6"
							headerKey="" headerValue="" listKey="uid" listValue="%{codice + '-' + descrizione}" />
					</div>
				</div>
			</div>
			<button type="button" class="btn btn-primary pull-right" id="bottoneCercaModaleRicercaTipoBeneCespite">
				<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="spinnerModaleRicercaCespite"></i>
			</button>
		</fieldset>
		
		<div id="risultatiRicercaCespite" class="hide">
			<h4>Elenco Conti</h4>
			<table class="table table-hover tab_left" id="tabellaRisultatiRicercaCespite">
				<thead>
					<tr>
						<th></th>
						<th>Codice</th>
						<th>Descrizione</th>
						<th>Tipo bene</th>
						<th>Classificazione</th>
						<th>Inventario</th>
						<th>Stato</th>
						<th>Stato iter</th>						
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
		
	</div>

	<div class="modal-footer">
		<button class="btn btn-primary"  aria-hidden="true" id="pulsanteConfermaModaleRicercaCespite">conferma</button>
	</div>
 
</div>