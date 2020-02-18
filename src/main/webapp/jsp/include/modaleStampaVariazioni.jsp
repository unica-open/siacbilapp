<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%-- Modale STAMPA --%>

<div aria-hidden="true" aria-labelledby="msgConsultaGruppoLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleStampa">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
	</div>
	<div class="modal-body">
		<div class="boxOrSpan2">
			<div id="frame-div" class="boxOrInline">
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" class="btn" id="chiudiModale" aria-hidden="true" data-dismiss="modal">chiudi</button>
	</div>
</div>

