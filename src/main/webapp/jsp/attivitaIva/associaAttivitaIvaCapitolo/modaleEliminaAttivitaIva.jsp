<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div aria-hidden="true" aria-labelledby="msgEliminaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleElimina">
	<div class="modal-body">
		<div class="alert alert-error">
			<button data-hide="alert" class="close" type="button">&times;</button>
			<p>
				<strong>Attenzione!</strong>
			</p>
			<p>
				<strong>Elemento selezionato: <span id="SPAN_elementoSelezionatoElimina"></span></strong>
			</p>
			<p>
				Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?
			</p>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn" id="BUTTON_noElimina" aria-hidden="true" data-dismiss="modal">no, indietro</button>
		<button type="button" class="btn btn-primary" id="BUTTON_siElimina">s&igrave;, prosegui&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_BUTTON_siElimina"></i></button>
	</div>
</div>