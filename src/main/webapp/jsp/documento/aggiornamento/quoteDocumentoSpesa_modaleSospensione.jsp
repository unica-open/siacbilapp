<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="modaleSospensioneQuota_header" role="dialog" tabindex="-1" class="modal hide fade" id="modaleSospensioneQuota">
	<div class="modal-header" id="modaleSospensioneQuota_header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4>Sospensione</h4>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modaleSospensioneQuota">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<fieldset class="form-horizontal" id="modaleSospensioneQuota_fieldset">
			<div class="control-group">
				<label class="control-label" for="modaleSospensioneQuota_dataScadenzaSubdocumento">Data scadenza</label>
				<div class="controls">
					<input type="text" id="modaleSospensioneQuota_dataScadenzaSubdocumento" name="modale.subdocumento.dataScadenza" class="lbTextSmall span2 datepicker" placeholder="data sospensione" maxlength="10" data-date="true" disabled />
					<span class="alRight">
						<label for="modaleSospensioneQuota_dataScadenzaDopoSospensioneSubdocumento" class="radio inline">Data scadenza dopo sospensione</label>
					</span>
					<input type="text" id="modaleSospensioneQuota_dataScadenzaDopoSospensioneSubdocumento" name="modale.subdocumento.dataScadenzaDopoSospensione" class="lbTextSmall span2 datepicker" placeholder="data scadenza dopo sospensione" maxlength="10" data-date="true" />
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label">Sospensione</label>
				<div class="controls">
					<span class="al">
						<label class="radio inline" for="modaleSospensioneQuota_dataSospensioneSubdocumento">Data&nbsp;</label>
					</span>
					<input type="text" id="modaleSospensioneQuota_dataSospensioneSubdocumento" name="modale.subdocumento.dataSospensione" class="lbTextSmall span2 datepicker" placeholder="data sospensione" maxlength="10" data-date="true" />
					<span class="al">
						<label class="radio inline" for="modaleSospensioneQuota_dataRiattivazioneSubdocumento">Data riattivazione</label>
					</span>
					<input type="text" id="modaleSospensioneQuota_dataRiattivazioneSubdocumento" name="modale.subdocumento.dataRiattivazione" class="lbTextSmall span2 datepicker" placeholder="data riattivazione" maxlength="10" data-date="true" />
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="modaleSospensioneQuota_causaleSospensioneSubdocumento">Causale sospensione</label>
				<div class="controls">
					<select id="modaleSospensioneQuota_causaleSospensioneSubdoc" name="modale.causaleSospensioneSubdoc" class="lbTextSmall span6">
						<option value="0"></option>
						<s:iterator value="listaCausaleSospensione" var="cs">
							<option value="<s:property value="#cs.uid"/>" data-template="<s:property value="#cs.descrizione"/>"><s:property value="#cs.codice"/> - <s:property value="#cs.descrizione"/></option>
						</s:iterator>
					</select>
					&nbsp;
					<input type="text" id="modaleSospensioneQuota_causaleSospensioneSubdocumento" name="modale.subdocumento.causaleSospensione" class="span6" placeholder="causale" />
				</div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<a class="btn btn-primary" id="modaleSospensioneQuota_conferma" href="#">
			conferma&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_modaleSospensioneQuota_conferma"></i>
		</a>
	</div>
</div>