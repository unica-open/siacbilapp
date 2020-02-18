<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="modaleAnnullamentoLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleAnnullamento">
	<div class="modal-body">
		<div class="alert alert-error alert-persistent">
			<%-- SIAC-6311 --%>
			<input type="hidden" id="HIDDEN_UidDaAnnullare" name="uidDaAnnullare" />
			<p><strong>Attenzione!</strong></p>
			<p><strong>Elemento selezionato:<span id="modaleAnnullamentoElementoSelezionato"></span></strong></p>
			<p>Stai per annullare l'elemento selezionato: sei sicuro di voler proseguire?</p>
			<p>L'annullamento sar&agrave; valido a partire dall'anno selezionato</p>
			<div class="control-group form-horizontal" id ="divDataAnnullamento">
				<label class="control-label" for="annoAnnullamento">Anno annullamento</label>
				<div class="controls">
					<input type="text" id="annoAnnullamento" name="annoAnnullamento" class="span3 soloNumeri text-right" maxlength="4" required />
				</div>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn">no, indietro</button>
		<button type="button" class="btn btn-primary" id="modaleAnnullamentoPulsanteSalvataggio">
			si, prosegui&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_modaleAnnullamentoPulsanteSalvataggio"></i>
		</button>
	</div>
</div>