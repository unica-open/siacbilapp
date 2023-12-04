<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<div id="modaleDettaglioOrdini" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="dettOrdiniLabel" aria-hidden="true">
	<div class="row-fluid">
		<div class="modal-header" id="dettOrdiniLabel">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4>Dettaglio ordini</h4>
		</div>
		<div class="modal-body">
			<div class="alert alert-error hide" id="ERRORI_MODALE_ORDINI">
				<button type="button" class="close" data-hide="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul></ul>
			</div>
			<div class="alert alert-success hide" id="INFORMAZIONI_MODALE_ORDINI">
				<button type="button" class="close" data-hide="alert">&times;</button>
				<strong>Informazioni</strong><br>
				<ul></ul>
			</div>
			<input type="hidden" id="HIDDEN_uidDocumento_modaleDettaglioOrdini" />
			<table class="table table-hover tab_left" id="tabella_modaleDettaglioOrdini">
				<thead>
					<tr id="txt_Left">
						<th scope="col">Numero</th>
						<th class="tab_Right span1" scope="col"></th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
			<div class="clearfix">
				<button type="button" class="btn btn-primary pull-right" id="buttonNuovo_modaleDettaglioOrdini">nuovo</button>
			</div>
			<div class="hide" id="divOrdine_modaleDettaglioOrdini">
				<fieldset id="fieldset_modaleDettaglioOrdini" class="form-horizontal">
					<input type="hidden" name="modale.ordine.documento.uid" id="uidDocumentoOrdine_modaleDettaglioOrdini" />
					<input type="hidden" name="modale.ordine.uid" id="uidOrdine_modaleDettaglioOrdini" />
					<div class="control-group">
						<label class="control-label" for="numeroOrdineOrdine_modaleDettaglioOrdini">Numero ordine</label>
						<div class="controls">
							<input type="text" id="numeroOrdineOrdine_modaleDettaglioOrdini" name="modale.ordine.numeroOrdine" class="span9" maxlength="20" />
						</div>
					</div>
					<p class="margin-large">
						<button type="button" class="btn btn-secondary" id="buttonAnnulla_modaleDettaglioOrdini">annulla</button>
						<button type="button" class="btn btn-primary" id="buttonSalva_modaleDettaglioOrdini" data-op="">salva</button>
					</p>
				</fieldset>
			</div>
			
		</div>
		<div class="modal-footer">
			<button class="btn btn-secondary" type="button" id="pulsanteChiusura_modaleDettaglioOrdini">chiudi</button>
		</div>
	</div>
</div>