<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div aria-hidden="true" aria-labelledby="msgAnnullaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleConfermaProsecuzioneSuAzione">
	<div class="modal-body">
		<div class="alert alert-warning alert-persistent" id="alertModaleConfermaProsecuzioneSuAzione">
			<button data-hide="alert" class="close" type="button">&times;</button>
			<p>
				<strong>Attenzione!</strong>
			</p>
			<ul>
			</ul>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn btn-secondary" data-dismiss="modal" aria-hidden="true">no, indietro</button>
		<button type="button" class="btn btn-secondary" id="modaleConfermaProsecuzioneSuAzionePulsanteSi">
			s&iacute;, prosegui&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_modaleConfermaProsecuzioneSuAzione"></i>
		</button>
	</div>
</div>