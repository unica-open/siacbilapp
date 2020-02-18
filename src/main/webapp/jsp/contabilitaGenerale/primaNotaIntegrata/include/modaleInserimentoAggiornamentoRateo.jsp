<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="modaleRateoLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleInserimentoAggiornamentoRateo">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h3 id="modaleRateoLabel">Rateo</h3>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modaleRateo">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br/>
			<ul>
			</ul>
		</div>
		<div class="alert alert-success hide" id="INFORMAZIONI_modaleRateo">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<ul></ul>
		</div>
		<fieldset class="form-horizontal" id="fieldsetModaleRateo">
		
				<s:hidden id="HIDDEN_primaNotaRateo" name ="rateo.primaNota.uid"/>
				<s:hidden id="HIDDEN_suffixRateo"/>
				<s:hidden id="HIDDEN_uidRateo" name ="rateo.uid"/>
			
				<div class="control-group">
					<label class="control-label" for="annoRateo_modale">Anno</label>
					<div class="controls">
						<s:textfield id="annoRateo_modale" name="rateo.anno" cssClass="span2"/>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="importoRateo_modale">Importo rinviabile</label>
					<div class="controls">
						<s:textfield id="importoRateo_modale" name="rateo.importo" cssClass="lbTextSmall span2 soloNumeri decimale"/>
					</div>
				</div>
			<span class="pull-right">
				<button type="button" id="pulsanteSalvaRateo" class="btn btn-primary">salva&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteSalvaRateo"></i>
				</button>
			</span>
		</fieldset>
	</div>
</div>