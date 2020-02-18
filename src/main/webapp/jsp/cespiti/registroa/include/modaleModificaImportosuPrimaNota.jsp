<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="editCespiteRegistroA" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="row-fluid">
		<div class="overlay-modale">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
				<h3> Aggiornamento importo su prima nota del cespite:  <span id="titoloModaleCespiteRegistroA"></span></h3>
			</div>
			<div class="modal-body">
				<div class="alert alert-error alert-persistent hide" id="ERRORI_modaleEditCespiteRegistroA">
					<button type="button" class="close" data-hide="alert">&times;</button>
					<strong>Attenzione!!</strong><br>
						<ul>
						</ul>
				</div>
				<fieldset class="form-horizontal">
					<div class="control-group">
						<label class="control-label" for="importoSuPrimaNotaModale">Importo</label>
						<div class="controls">
							<input type="text" class="span2 soloNumeri decimale" id="importoSuPrimaNotaModale">
						</div>
					</div>
				</fieldset>
			</div>
			<div class="modal-footer">
				<button class="btn" data-dismiss="modal" aria-hidden="true">chiudi</button>
				<button type="button" id="confermaAggiornaCespiteRegistroAModale" class="btn btn-primary">salva</button>			
			</div>
		</div>
	</div>
</div>