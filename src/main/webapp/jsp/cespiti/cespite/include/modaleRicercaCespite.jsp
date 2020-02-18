<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="guidaCespiteLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleRicercaCespite">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane" id="guidaCespiteLabel">Collega cespite</h4>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="erroriCespiteModale">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<fieldset class="form-horizontal" id="fieldset_modaleCespite">
			<s:hidden name="flagStatoBene"/>
			<div class="control-group">
				<label for="codice" class="control-label">Codice </label>
				<div class="controls">
					<s:textfield id="codice" name="cespite.codice" cssClass="span6" maxlength="500"/>
				</div>
			</div>
			<div class="control-group">
				<label for="descrizione" class="control-label">Descrizione </label>
				<div class="controls">
					<s:textfield id="descrizione" name="cespite.descrizione" cssClass="span6" placeholder="descrizione" maxlength="500"/>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="cespiteTipoBene">Tipo Bene </label>
				<div class="controls">
					<s:select id="cespiteTipoBene" cssClass="span6" list="listaTipoBeneCespite" listKey="uid" name="cespite.tipoBeneCespite.uid"
														headerKey="" headerValue="" listValue="%{codice + ' - '+ descrizione}" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"><abbr title="Numero">Num.</abbr> inventario</label>
				<div class="controls">
					<span class="al">
						<label class="radio inline" for="numeroInventarioDa">Da</label>
					</span>
					<s:textfield id="numeroInventarioDa" name="numeroInventarioDa" cssClass="lbTextSmall soloNumeri" maxlength="8"/>
					<span class="al">
						<label class="radio inline" for="numeroInventarioA">A</label>
					</span>
					<s:textfield id="numeroInventarioA" name="numeroInventarioA" cssClass="lbTextSmall soloNumeri" maxlength="8"/>
				</div>
			</div>
			<p>
				<button type="button" id="pulsanteRicercaCespiteModale" class="btn btn-primary pull-right">
					<i class="icon-search icon"></i>cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteRicercaCespiteModale"></i>
				</button>
			</p>
		</fieldset>
		
		<div id="divCespitiTrovati" class="hide">
			<h4>Elenco cespiti trovati</h4>
				
				<table class="table table-hover tab_left dataTable" id="tabellaCespiteModale">
					<thead>
						<tr>
							<th class="span1"></th>
							<th scope="col">Cespite</th>
							<th scope="col">Tipo Bene</th>
							<th scope="col">Inventario</th>
							<th scope="col">Attivo</th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			<div class="Border_line"></div>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-primary" id="pulsanteConfermaCespiteModale">
			conferma
			&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteConfermaCespiteModale"></i>
		</button>
	</div>
</div>