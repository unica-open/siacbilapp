<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion_info">
	<fieldset class="form-horizontal" id="nota_fieldsetAliquotaSubdocumentoIva">
		<h4 class="step-pane">Dati principali</h4>
		<div class="control-group">
			<label for="nota_aliquotaIvaAliquotaSubdocumentoIva" class="control-label">Aliquota iva *</label>
			<div class="controls">
				<select name="aliquotaSubdocumentoIvaNota.aliquotaIva.uid" class="span2" required
						id="nota_aliquotaIvaAliquotaSubdocumentoIva">
					<option value="0" data-percentuale-aliquota="" data-percentuale-indetraibilita=""></option>
					<s:iterator value="listaAliquotaIva" var="al">
						<option value="<s:property value="%{#al.uid}" />" data-percentuale-aliquota="<s:property value="%{#al.percentualeAliquota}" />"
							data-percentuale-indetraibilita="<s:property value="%{#al.percentualeIndetraibilita}" />"
							<s:if test="%{#al.uid == aliquotaSubdocumentoIvaNota.aliquotaIvaNota.uid}">selected</s:if>>
								<s:property value="%{#al.codice}"/> - <s:property value="%{#al.descrizione}"/>
							</option>
					</s:iterator>
				</select>
				<span class="alRight">
					<label for="nota_percentualeAliquotaIva" class="radio inline">Percentuale</label>
				</span>
				<s:textfield id="nota_percentualeAliquotaIva" name="percentualeAliquotaIvaNota" cssClass="lbTextSmall span1" readonly="true" />
				<s:hidden id="nota_percentualeIndetraibilitaAliquotaIva" />
			</div>
		</div>
		
		<div class="control-group">
			<label for="nota_imponibileAliquotaSubdocumentoIva" class="control-label">Imponibile *</label>
			<div class="controls">
				<s:textfield id="nota_imponibileAliquotaSubdocumentoIva" name="aliquotaSubdocumentoIvaNota.imponibile"
					cssClass="lbTextSmall span2 soloNumeri decimale" required="true"
					disabled="%{aliquotaSubdocumentoIvaNota.aliquotaIva == null || aliquotaSubdocumentoIvaNota.aliquotaIva.uid == 0}" />
				<span class="alRight">
					<label for="nota_impostaAliquotaSubdocumentoIva" class="radio inline">Imposta *</label>
				</span>
				<s:textfield id="nota_impostaAliquotaSubdocumentoIva" name="aliquotaSubdocumentoIvaNota.imposta"
					cssClass="lbTextSmall span2 soloNumeri decimale" required="true"
					disabled="%{aliquotaSubdocumentoIva.aliquotaIva == null || aliquotaSubdocumentoIva.aliquotaIva.uid == 0}" />
				<span class="alRight">
					<label for="nota_totaleAliquotaSubdocumentoIva" class="radio inline">Totale *</label>
				</span>
				<s:textfield id="nota_totaleAliquotaSubdocumentoIva" name="aliquotaSubdocumentoIvaNota.totale"
					cssClass="lbTextSmall span2 soloNumeri decimale" required="true"
					disabled="%{aliquotaSubdocumentoIva.aliquotaIva == null || aliquotaSubdocumentoIva.aliquotaIva.uid == 0}" />
				<span class="alRight">
					<button type="button" class="btn btn-secondary" id="nota_pulsanteGestioneManualeAliquotaSubdocumentoIva">
						Togli calcolo automatico
					</button>
				</span>
			</div>
		</div>
		<div class="control-group">
			<label for="nota_impostaDetraibileAliquotaSubdocumentoIva"
				class="control-label">Imposta detraibile</label>
			<div class="controls">
				<s:textfield id="nota_impostaDetraibileAliquotaSubdocumentoIva" name="aliquotaSubdocumentoIvaNota.impostaDetraibile"
					cssClass="lbTextSmall span2 soloNumeri decimale" required="true" readonly="true" />
				<span class="alRight">
					<label for="nota_impostaIndetraibileAliquotaSubdocumentoIva"
						class="radio inline">Imposta indetraibile</label>
				</span>
				<s:textfield id="nota_impostaIndetraibileAliquotaSubdocumentoIva" name="aliquotaSubdocumentoIvaNota.impostaIndetraibile"
					cssClass="lbTextSmall span2 soloNumeri decimale" required="true" readonly="true" />
			</div>
		</div>
	</fieldset>
	<p>
		<button type="button" class="btn btn-secondary" id="nota_pulsanteAnnullaInserisciMovimentiIva">annulla</button>
		<button type="button" class="btn btn-primary pull-right" id="nota_pulsanteConfermaInserisciMovimentiIva">
			aggiungi movimento&nbsp;<i class="icon-spin icon-refresh spinner" id="nota_SPINNER_pulsanteConfermaInserisciMovimentiIva"></i>
		</button>
	</p>
</div>