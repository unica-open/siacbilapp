<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="msgAggMovimentoIVALabel" role="dialog" tabindex="-1"
		class="modal hide fade" id="modaleAggiornamentoMovimentiIva<s:property value='suffisso'/>">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<div class="alert alert-error hide" id="ERRORI_modaleAggiornamentoMovimentiIva<s:property value='suffisso'/>">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<h4 class="nostep-pane">Aggiorna movimento iva</h4>
	</div>
	<div class="modal-body">
		<fieldset class="form-horizontal" id="fieldsetAliquotaSubdocumentoIva_modale<s:property value='suffisso'/>">
			<div class="control-group">
				<label for="aliquotaIvaAliquotaSubdocumentoIva_modale<s:property value='suffisso'/>" class="control-label">Aliquota iva *</label>
				<div class="controls">
					<select id="aliquotaIvaAliquotaSubdocumentoIva_modale<s:property value='suffisso'/>" class="span3" required
							name="aliquotaSubdocumentoIva.aliquotaIva.uid">
						<option value="0"></option>
						<s:iterator value="listaAliquotaIva" var="ai">
							<option value="<s:property value='%{#ai.uid}' />" <s:if test="%{#ai.uid == aliquotaSubdocumentoIva.aliquotaIva.uid}">selected</s:if>
									data-percentuale-aliquota="<s:property value='%{#ai.percentualeAliquota}' />"
									data-percentuale-indetraibilita="<s:property value='%{#ai.percentualeIndetraibilita}' />">
								<s:property value="%{#ai.codice + ' - ' + #ai.descrizione}" />
							</option>
						</s:iterator>
					</select>
					<span class="alRight">
						<label for="percentualeAliquotaIva_modale<s:property value='suffisso'/>" class="radio inline">Percentuale
					</label>
					</span>
					<s:textfield id="percentualeAliquotaIva_modale%{suffisso}" name="percentualeAliquotaIva" cssClass="lbTextSmall span2"
						readonly="true" />
					<s:hidden id="percentualeIndetraibilitaAliquotaIva_modale%{suffisso}" name="percentualeIndetraibilitaAliquotaIva" />
				</div>
			</div>
			<div class="control-group">
				<label for="imponibileAliquotaSubdocumentoIva_modale<s:property value='suffisso'/>" class="control-label">Imponibile *</label>
				<div class="controls">
					<s:textfield id="imponibileAliquotaSubdocumentoIva_modale%{suffisso}" name="aliquotaSubdocumentoIva.imponibile"
						cssClass="lbTextSmall span3 soloNumeri decimale" required="true"
						disabled="%{aliquotaSubdocumentoIva.aliquotaIva == null || aliquotaSubdocumentoIva.aliquotaIva.uid == 0}" />
					<span class="alRight">
						<label for="impostaAliquotaSubdocumentoIva_modale<s:property value='suffisso'/>" class="radio inline">Imposta *</label>
					</span>
					<s:textfield id="impostaAliquotaSubdocumentoIva_modale%{suffisso}" name="aliquotaSubdocumentoIva.imposta"
						cssClass="lbTextSmall span3 soloNumeri decimale" required="true"
						disabled="%{aliquotaSubdocumentoIva.aliquotaIva == null || aliquotaSubdocumentoIva.aliquotaIva.uid == 0}" />
				</div>
			</div>
			<div class="control-group">
				<label for="totaleAliquotaSubdocumentoIva_modale<s:property value='suffisso'/>" class="control-label">Totale *</label>
				<div class="controls">
					<s:textfield id="totaleAliquotaSubdocumentoIva_modale%{suffisso}" name="aliquotaSubdocumentoIva.totale"
						cssClass="lbTextSmall span3 soloNumeri decimale" required="true"
						disabled="%{aliquotaSubdocumentoIva.aliquotaIva == null || aliquotaSubdocumentoIva.aliquotaIva.uid == 0}" />
					<span class="alRight">
						<button type="button" class="btn btn-secondary" id="pulsanteGestioneManualeAliquotaSubdocumentoIva_modale<s:property value='suffisso'/>">
							Togli calcolo automatico
						</button>
					</span>
				</div>
			</div>
			<div class="control-group">
				<label for="impostaDetraibileAliquotaSubdocumentoIva_modale<s:property value='suffisso'/>" class="control-label">Imposta detraibile</label>
				<div class="controls">
					<s:textfield id="impostaDetraibileAliquotaSubdocumentoIva_modale%{suffisso}" name="aliquotaSubdocumentoIva.impostaDetraibile"
						cssClass="lbTextSmall span2 soloNumeri decimale" required="true" readonly="true" />
					<span class="alRight">
						<label for="impostaIndetraibileAliquotaSubdocumentoIva_modale<s:property value='suffisso'/>" class="radio inline">Imposta indetraibile</label>
					</span>
					<s:textfield id="impostaIndetraibileAliquotaSubdocumentoIva_modale%{suffisso}" name="aliquotaSubdocumentoIva.impostaIndetraibile"
						cssClass="lbTextSmall span2 soloNumeri decimale" required="true" readonly="true" />
				</div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button type="button" aria-hidden="true" data-dismiss="modal" class="btn btn-secondary" 
				id="pulsanteAnnullaAliquota_modale<s:property value='suffisso'/>">
			annulla
		</button>
		<button type="button" class="btn btn-primary" id="pulsanteConfermaAliquota_modale<s:property value='suffisso'/>">
			conferma&nbsp;
			<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteConfermaAliquota_modale<s:property value='suffisso'/>"></i>
		</button>
	</div>
</div>