<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="labelModaleAggiornaContabilizzazione" role="dialog" tabindex="-1" class="modal hide fade in" id="modaleAggiornaContabilizzazione">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<div class="alert alert-error hide" id="ERRORI_modaleAggiornaContabilizzazione">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<h3 id="labelModaleAggiornaContabilizzazione">Aggiorna contabilizzazione</h3>
	</div>
	<div class="modal-body">
		<fieldset class="form-horizontal" id="fieldsetModaleAggiornaContabilizzazione">
			<input type="hidden" name="registroComunicazioniPCC.uid" id="registroComunicazioniPCCModaleAggiornaContabilizzazione" />
			<div class="control-group">
				<label class="control-label" for="statoDebitoRegistroComunicazioniPCCModaleAggiornaContabilizzazione">Stato del debito *</label>
				<div class="controls">
					<s:select list="listaStatoDebito" id="statoDebitoRegistroComunicazioniPCCModaleAggiornaContabilizzazione" name="registroComunicazioniPCC.statoDebito.uid" cssClass="span9"
						listKey="uid" listValue="%{codice + ' - ' + descrizione}" headerKey="" headerValue="" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="causalePCCRegistroComunicazioniPCCModaleAggiornaContabilizzazione">Causale</label>
				<div class="controls">
					<select id="causalePCCRegistroComunicazioniPCCModaleAggiornaContabilizzazione" name="registroComunicazioniPCC.causalePCC.uid" class="span9">
					</select>
				</div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button type="button" aria-hidden="true" data-dismiss="modal" class="btn btn-secondary">chiudi</button>
		<button type="button" class="btn btn-primary" id="confermaModaleAggiornaContabilizzazione">
			conferma&nbsp;<i class="icon-spin icon-refresh spinner"></i>
		</button>
	</div>
</div>