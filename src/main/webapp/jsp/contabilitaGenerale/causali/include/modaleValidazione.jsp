<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="modaleValidazioneLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleValidazione">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h3 id="modaleValidazioneLabel">Valida Causale</h3>
	</div>
	<div class="modal-body">
		<div class="alert alert-info alert-persistent">
			<p><strong>Elemento selezionato:<span id="modaleValidazioneElementoSelezionato"></span></strong></p>
			<p>Stai per validare l'elemento selezionato: sei sicuro di voler proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn">no, indietro</button>
		<button type="button" class="btn btn-primary" id="modaleValidazionePulsanteSalvataggio">
			si, prosegui&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_modaleValidazionePulsanteSalvataggio"></i>
		</button>
	</div>
</div>