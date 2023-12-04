<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<s:set var="template">${param.template}</s:set>
<div aria-hidden="true" aria-labelledby="msgAnnullaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleConfermaStampaIva">
	<div class="modal-header">
		<button data-dismiss="modal" class="close" type="button">&times;</button>
	</div>
	<div class="modal-body">
		<div class="alert alert-info alert-persistent">
			<p>
				<strong id="templateStampa"></strong>
				<s:hidden id="hiddenTemplateStampa" value="%{#template}" />
			</p>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" aria-hidden="true" data-dismiss="modal" class="btn">annulla</button>
		<button type="submit" class="btn btn-primary" id="pulsanteConfermaStampaIva">conferma</button>
	</div>
</div>