<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="labelModaleNuovaContabilizzazione" role="dialog" tabindex="-1" class="modal hide fade in" id="modaleNuovaContabilizzazione">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<div class="alert alert-error hide" id="ERRORI_modaleNuovaContabilizzazione">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<h3 id="labelModaleNuovaContabilizzazione">Nuova contabilizzazione</h3>
	</div>
	<div class="modal-body">
		<fieldset class="form-horizontal" id="fieldsetModaleNuovaContabilizzazione">
			<div class="control-group">
				<label class="control-label" for="statoDebitoRegistroComunicazioniPCCModaleNuovaContabilizzazione">Stato del debito * </label>
				<div class="controls">
					<s:select list="listaStatoDebito" id="statoDebitoRegistroComunicazioniPCCModaleNuovaContabilizzazione" name="registroComunicazioniPCC.statoDebito.uid" cssClass="span9"
						listKey="uid" listValue="%{codice + ' - ' + descrizione}" headerKey="" headerValue="" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="causalePCCRegistroComunicazioniPCCModaleNuovaContabilizzazione">Causale</label>
				<div class="controls">
					<select id="causalePCCRegistroComunicazioniPCCModaleNuovaContabilizzazione" name="registroComunicazioniPCC.causalePCC.uid" disabled="disabled" class="span9">
					</select>
				</div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button type="button" aria-hidden="true" data-dismiss="modal" class="btn btn-secondary">chiudi</button>
		<button type="button" class="btn btn-primary" id="confermaModaleNuovaContabilizzazione">
			conferma&nbsp;<i class="icon-spin icon-refresh spinner"></i>
		</button>
	</div>
</div>