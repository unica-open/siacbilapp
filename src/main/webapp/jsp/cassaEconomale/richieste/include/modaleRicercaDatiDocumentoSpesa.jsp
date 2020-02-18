<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="labelModaleRicercaDocumentoSpesa" role="dialog" tabindex="-1" class="modal hide fade" id="modaleRicercaDocumentoSpesa">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h4 class="nostep-pane" id="labelModaleGiustificativo">Ricerca Fattura</h4>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modaleRicercaDocumentoSpesa">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<fieldset id="fieldsetModaleRicercaDocumentoSpesa" class="form-horizontal">
				<s:hidden id="HIDDEN_anno_datepicker" value="%{documentoSpesa.anno}" />
				<div class="control-group">
					<label class="control-label" for="tipoDocumentoDocumentoSpesa_modale">Tipo</label>
					<div class="controls">
						<s:select list="listaTipoDocumento" id="tipoDocumentoDocumentoSpesa_modale" name="modale.documentoSpesa.tipoDocumento.uid" cssClass="span4"
							headerKey="" headerValue="" listKey="uid" listValue="descrizione" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="statoOperativoDocumento_modale">Stato</label>
					<div class="controls">
						<s:select id="statoOperativoDocumento_modale" list="listaStatoOperativoDocumento" name="modale.documentoSpesa.statoOperativoDocumento"
							required="false" headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="anno_modale">Anno</label>
					<div class="controls">
						<s:textfield id="anno_modale" cssClass="lbTextSmall span2 soloNumeri" name="modale.documentoSpesa.anno" maxlength="4" placeholder="anno" />
						<span class="al">
							<label class="radio inline" for="numero_modale">Numero</label>
						</span>
						<s:textfield id="numero_modale" cssClass="lbTextSmall span2" name="modale.documentoSpesa.numero" placeholder="numero" />
					</div>
				</div>
				<div class="control-group" >
					<label class="control-label" for="dataEmissione_modale">Data documento</label>
					<div class="controls">
						<s:textfield id="dataEmissione_modale" cssClass="lbTextSmall span2 datepicker" name="modale.documentoSpesa.dataEmissione" placeholder="dataEmissione" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">Elenco</label>
					<div class="controls">
						<span class="al">
							<label class="radio inline" for="elenco.ANNO_modale">Anno</label>
						</span>
						<s:textfield id="elenco.anno_modale" cssClass="lbTextSmall span2" name="modale.elencoDocumenti.anno" maxlength="4" placeholder="anno" />
						<span class="al">
							<label class="radio inline" for="elenco.numero_modale">Numero</label>
						</span>
						<s:textfield id="elenco.numero_modale" cssClass="lbTextSmall span2" name="modale.elencoDocumenti.numero" placeholder="numero" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">Movimento</label>
					<div class="controls">
						<span class="al">
							<label class="radio inline" for="movimentoGestione.anno_modale">Anno</label>
						</span>
						<s:textfield id="movimentoGestione.anno_modale" cssClass="lbTextSmall span2" name="modale.impegno.annoMovimento" maxlength="4" placeholder="anno" />
						<span class="al">
							<label class="radio inline" for="movimentoGestione.numero_modale">Numero</label>
						</span>
						<s:textfield id="movimentoGestione.numero_modale" cssClass="lbTextSmall span2" name="modale.impegno.numero" placeholder="numero" value="%{impegno.numero.toString()}" />
					</div>
				</div>
				<button type="button" class="btn btn-primary pull-right" id="pulsanteCercaModaleRicercaDocumentoSpesa">
				<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="spinnerModaleCercaDocumentoSpesa"></i>
			</button>
		</fieldset>
		<div id="risultatiRicercaModaleDocumentoSpesa">
			<h4>Elenco Dati Fatture</h4>
			<table class="table table-hover tab_left" id="tabellaModaleRicercaDocumentoSpesa">
				<thead>
					<tr>
						<th class="span1">&nbsp;</th>
						<th>Tipo</th>
						<th>Anno</th>
						<th>Numero</th>
						<th>Stato</th>
						<th>Soggetto</th>
						<th>Importo</th>
						<th>Importo da pagare</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
			<div id="divModaleRicercaDocumentoSpesaSubdocumento" class="hide">
				<h4>Elenco Subdocumenti</h4>
				<table class="table table-hover tab_left" id="tabellaModaleRicercaDocumentoSpesaSubdocumento">
					<thead>
						<tr>
							<th class="span1">&nbsp;</th>
							<th><abbr title="Numero">N.</abbr> quota</th>
							<th>Importo quota</th>
							<th>Pagato </th>
							<th>Data pagamento in <abbr title="cassa economale">cec</abbr></th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>
		</div>
	</div>
	<div class="modal-footer">
		<a aria-hidden="true" data-dismiss="modal" class="btn btn-secondary">annulla</a>
		<button type="button" class="btn btn-primary" id="pulsanteConfermaModaleRicercaDocumentoSpesa">conferma</button>
	</div>
</div>