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
			<s:hidden id="HIDDEN_UidDaAnnullare" name="uidDaAnnullare" />
			<p><strong>Attenzione!</strong></p>
			<p><strong>Elemento selezionato:<span id="modaleAnnullamentoElementoSelezionato"></span></strong></p>
			<p>Stai per annullare l'elemento selezionato: sei sicuro di voler proseguire?</p>
			<div class="control-group" id ="divDataAnnullamento">
				<label class="control-label" for="dataAnnullamento">Data annullamento</label>
				<div class="controls">
					<s:textfield id="dataAnnullamento" name="dataAnnullamento" cssClass="span3 datepicker" required="true" data-date="true" />
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