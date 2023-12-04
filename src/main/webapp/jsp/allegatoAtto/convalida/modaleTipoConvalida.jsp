<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<div aria-hidden="true" aria-labelledby="labelModaleTipoConvalida" role="dialog" tabindex="-1" class="modal hide fade" id="modaleTipoConvalida">
	<div class="modal-header" id="labelModaleTipoConvalida">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<!-- task-160 -->
		<h4 class="nostep-pane">Numero oggetti in convalida <span id="numeroDocumentiCollegati"></span> </h4>
								
	</div>
	<div class="modal-body">
		<fieldset class="form-horizontal">
			<div class="control-group">
				<label class="control-label">Convalida per Emissione </label>
				<div class="controls">
					<label class="radio inline">
						<input type="radio" value="false" name="convalidaManuale" <s:if test="!convalidaManuale">checked</s:if>>Automatica
					</label>
					<label class="radio inline">
						<input type="radio" id="convalidaManuale_modal" value="true" name="convalidaManuale" <s:if test="convalidaManuale">checked</s:if>>Manuale
					</label>
				</div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn btn-secondary">annulla</button>
		<button type="button" class="btn btn-primary" id="pulsanteConfermaModaleTipoConvalida">conferma</button>
	</div>
</div>