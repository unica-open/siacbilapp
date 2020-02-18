<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div aria-hidden="true" aria-labelledby="msgAnnullaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleConfermaProsecuzione">
	<div class="modal-body">
		<div class="alert alert-warning alert-persistent">
			<button data-hide="alert" class="close" type="button">&times;</button>
			<p>
				<strong>Attenzione!</strong>
			</p>
			<p>Sono presenti delle modifiche non salvate: sei sicuro di voler proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<button class="btn btn-secondary" id="modaleConfermaPulsanteNo">no, indietro</button>
		<button class="btn btn-secondary" id="modaleConfermaPulsanteSi">s&iacute;, prosegui</button>
	</div>
</div>