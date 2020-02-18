<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="comp-SpesaNotaCredito" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h3 id="myModalLabel">Note credito ricerca</h3>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modaleRicercaNCD">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<fieldset class="form-horizontal" id="fieldsetModaleRicercaSpesaNCD">
			<div id="campiRicercaNCD" class="accordion-body collapse in">

				<s:hidden id="HIDDEN_tipoDocumentoUIDNotaCreditoEsistente" name="modale.documentoSpesa.tipoDocumento.uid" value="%{tipoDocumentoNotaCredito.uid}" data-maintain="" />
				<s:hidden id="HIDDEN_soggettoCodiceNotaCreditoEsistente" name="modale.soggetto.codiceSoggetto" value="%{soggetto.codiceSoggetto}" data-maintain="" />
				<s:hidden id="HIDDEN_soggettoUidNotaCreditoEsistente" name="modale.soggetto.uid" value="%{soggetto.uid}" data-maintain="" />
				
				<div class="control-group">
					<label class="control-label" for="annoNotaCreditoEsistente">Anno</label>
					<div class="controls">
						<s:textfield id="annoNotaCreditoEsistente" cssClass="lbTextSmall span2 soloNumeri" name="modale.documentoSpesa.anno" maxlength="4" placeholder="anno" />
						<span class="al">
							<label class="radio inline" for="numeroDocumentoSpesaNcd">Numero</label>
						</span>
						<s:textfield id="numeroNotaCreditoEsistente" cssClass="lbTextSmall span2" name="modale.documentoSpesa.numero" placeholder="numero" />
						<span class="al">
							<label class="radio inline" for="dataDocumentoSpesaNcd">Data</label>
						</span>
						<s:textfield id="dataNotaCreditoEsistente" cssClass="lbTextSmall span2 datepicker" name="modale.documentoSpesa.dataEmissione" placeholder="dataEmissione" />
					</div>
				</div>
			</div>
			<p class="margin-medium">
					<button type="button" class="btn btn-primary pull-right" id="pulsanteCercaSpesaNCDModale" >
						<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteCercaSpesaNCDModale"></i>
					</button>
				</p>
		</fieldset>
		<div id="risultatiRicercaSpesaNCD" class="hide">
			<h4>Elenco Note di Credito</h4>
			<table class="table table-hover tab_left" id="tabellaRisultatiRicercaSpesaNCD">
				<thead>
					<tr>
						<th></th>
						<th>Documento</th>
						<th>Data</th>
						<th>Stato</th>
						<th>Importo</th>
					</tr>
				</thead>
				<tbody></tbody>
				<tfoot>
					<tr>
						<th colspan="4">Totale</th>
						<th class="tab_Right" id="importoTotaleDoc"></th>
					</tr>
				</tfoot>
			</table>
		</div>
	</div>

	<div class="modal-footer">
		<button type="button" class="btn btn-primary pull-right" id="pulsanteConfermaSpesaNCDModale" >
			conferma&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteConfermaSpesaNCDModale"></i>
		</button>
	</div>
 
</div>