<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="msgAggMovimentoIVALabel" role="dialog" tabindex="-1" class="modal hide fade" id="nota_modaleAggiornamentoMovimentiIva">
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
		<fieldset class="form-horizontal" id="nota_fieldsetAliquotaSubdocumentoIva_modale">
			<div class="control-group">
				<label for="nota_aliquotaIvaAliquotaSubdocumentoIva_modale" class="control-label">Aliquota iva * </label>
				<div class="controls">
					<select id="nota_aliquotaIvaAliquotaSubdocumentoIva_modale<s:property value='suffisso'/>" class="span3" required
							name="aliquotaSubdocumentoIvaNota.aliquotaIva.uid">
						<option value="0"></option>
						<s:iterator value="listaAliquotaIva" var="ai">
							<option value="<s:property value='%{#ai.uid}' />" <s:if test="%{#ai.uid == aliquotaSubdocumentoIvaNota.aliquotaIva.uid}">selected</s:if>
									data-percentuale-aliquota="<s:property value='%{#ai.percentualeAliquota}' />"
									data-percentuale-indetraibilita="<s:property value='%{#ai.percentualeIndetraibilita}' />">
								<s:property value="%{#ai.codice + ' - ' + #ai.descrizione}" />
							</option>
						</s:iterator>
					</select>
					<span class="alRight">
						<label for="nota_percentualeAliquotaIva_modale" class="radio inline">Percentuale</label>
					</span>
					<s:textfield id="nota_percentualeAliquotaIva_modale" name="percentualeAliquotaIvaNota" cssClass="lbTextSmall span2" readonly="true" />
					<s:hidden id="nota_percentualeIndetraibilitaAliquotaIva_modale" name="percentualeIndetraibilitaAliquotaIvaNota" />
				</div>
			</div>
			<div class="control-group">
				<label for="nota_imponibileAliquotaSubdocumentoIva_modale" class="control-label">Imponibile *</label>
				<div class="controls">
					<s:textfield id="nota_imponibileAliquotaSubdocumentoIva_modale" name="aliquotaSubdocumentoIvaNota.imponibile"
						cssClass="lbTextSmall span3 soloNumeri decimale" required="true"
						disabled="%{aliquotaSubdocumentoIva.aliquotaIva == null || aliquotaSubdocumentoIva.aliquotaIva.uid == 0}" />
					<span class="alRight">
						<label for="nota_impostaAliquotaSubdocumentoIva_modale" class="radio inline">Imposta *</label>
					</span>
					<s:textfield id="nota_impostaAliquotaSubdocumentoIva_modale" name="aliquotaSubdocumentoIvaNota.imposta"
						cssClass="lbTextSmall span3 soloNumeri decimale" required="true"
						disabled="%{aliquotaSubdocumentoIva.aliquotaIva == null || aliquotaSubdocumentoIva.aliquotaIva.uid == 0}" />
				</div>
			</div>
			<div class="control-group">
				<label for="nota_totaleAliquotaSubdocumentoIva_modale" class="control-label">Totale *</label>
				<div class="controls">
					<s:textfield id="nota_totaleAliquotaSubdocumentoIva_modale" name="aliquotaSubdocumentoIvaNota.totale"
						cssClass="lbTextSmall span3 soloNumeri decimale" required="true"
						disabled="%{aliquotaSubdocumentoIva.aliquotaIva == null || aliquotaSubdocumentoIva.aliquotaIva.uid == 0}" />
					<span class="alRight">
						<button type="button" class="btn btn-secondary" id="nota_pulsanteGestioneManualeAliquotaSubdocumentoIva_modale">
							Togli calcolo automatico
						</button>
					</span>
				</div>
			</div>
			<div class="control-group">
				<label for="nota_impostaDetraibileAliquotaSubdocumentoIva_modale" class="control-label">Imposta detraibile</label>
				<div class="controls">
					<s:textfield id="nota_impostaDetraibileAliquotaSubdocumentoIva_modale" name="aliquotaSubdocumentoIvaNota.impostaDetraibile"
						cssClass="lbTextSmall span2 soloNumeri decimale" required="true" />
					<span class="alRight">
						<label for="nota_impostaIndetraibileAliquotaSubdocumentoIva_modale" class="radio inline">Imposta indetraibile</label>
					</span>
					<s:textfield id="nota_impostaIndetraibileAliquotaSubdocumentoIva_modale" name="aliquotaSubdocumentoIvaNota.impostaIndetraibile"
						cssClass="lbTextSmall span2 soloNumeri decimale" required="true" />
				</div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button type="button" aria-hidden="true" data-dismiss="modal" class="btn btn-secondary" id="nota_pulsanteAnnullaAliquota_modale">
			annulla
		</button>
		<button type="button" class="btn btn-primary" id="nota_pulsanteConfermaAliquota_modale">
			conferma&nbsp;<i class="icon-spin icon-refresh spinner" id="nota_SPINNER_pulsanteConfermaAliquota_modale"></i>
		</button>
	</div>
</div>