<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion_info">
	<fieldset class="form-horizontal" id="fieldsetAliquotaSubdocumentoIva<s:property value='suffisso'/>">
		<h4 class="step-pane">Dati principali</h4>
		<div class="control-group">
			<label for="aliquotaIvaAliquotaSubdocumentoIva<s:property value='suffisso'/>" class="control-label">Aliquota iva *</label>
			<div class="controls">
				<select name="aliquotaSubdocumentoIva.aliquotaIva.uid" class="span2" required
						id="aliquotaIvaAliquotaSubdocumentoIva<s:property value='suffisso'/>">
					<option value="0" data-percentuale-aliquota="" data-percentuale-indetraibilita=""></option>
					<s:iterator value="listaAliquotaIva" var="al">
						<option value="<s:property value="%{#al.uid}" />" data-percentuale-aliquota="<s:property value="%{#al.percentualeAliquota}" />"
							data-percentuale-indetraibilita="<s:property value="%{#al.percentualeIndetraibilita}" />"
							<s:if test="%{#al.uid == aliquotaSubdocumentoIva.aliquotaIva.uid}">selected</s:if>>
								<s:property value="%{#al.codice}"/> - <s:property value="%{#al.descrizione}"/>
							</option>
					</s:iterator>
				</select>
				<span class="alRight">
					<label for="percentualeAliquotaIva<s:property value='suffisso'/>" class="radio inline">Percentuale</label>
				</span>
				<s:textfield id="percentualeAliquotaIva%{suffisso}" name="percentualeAliquotaIva" cssClass="lbTextSmall span1" readonly="true" />
				<s:hidden id="percentualeIndetraibilitaAliquotaIva%{suffisso}" />
			</div>
		</div>
		
		<div class="control-group">
			<label for="imponibileAliquotaSubdocumentoIva<s:property value='suffisso'/>" class="control-label">Imponibile *</label>
			<div class="controls">
				<s:textfield id="imponibileAliquotaSubdocumentoIva%{suffisso}" name="aliquotaSubdocumentoIva.imponibile"
					cssClass="lbTextSmall span2 soloNumeri decimale" required="true"
					disabled="%{aliquotaSubdocumentoIva.aliquotaIva == null || aliquotaSubdocumentoIva.aliquotaIva.uid == 0}" />
				<span class="alRight">
					<label for="impostaAliquotaSubdocumentoIva<s:property value='suffisso'/>" class="radio inline">Imposta *</label>
				</span>
				<s:textfield id="impostaAliquotaSubdocumentoIva%{suffisso}" name="aliquotaSubdocumentoIva.imposta"
					cssClass="lbTextSmall span2 soloNumeri decimale" required="true"
					disabled="%{aliquotaSubdocumentoIva.aliquotaIva == null || aliquotaSubdocumentoIva.aliquotaIva.uid == 0}" />
				<span class="alRight">
					<label for="totaleAliquotaSubdocumentoIva<s:property value='suffisso'/>" class="radio inline">Totale *</label>
				</span>
				<s:textfield id="totaleAliquotaSubdocumentoIva%{suffisso}" name="aliquotaSubdocumentoIva.totale"
					cssClass="lbTextSmall span2 soloNumeri decimale" required="true"
					disabled="%{aliquotaSubdocumentoIva.aliquotaIva == null || aliquotaSubdocumentoIva.aliquotaIva.uid == 0}" />
				<span class="alRight">
					<button type="button" class="btn btn-secondary" id="pulsanteGestioneManualeAliquotaSubdocumentoIva<s:property value='suffisso'/>">
						Togli calcolo automatico
					</button>
				</span>
			</div>
		</div>
		<div class="control-group">
			<label for="impostaDetraibileAliquotaSubdocumentoIva<s:property value='suffisso'/>"
				class="control-label">Imposta detraibile</label>
			<div class="controls">
				<s:textfield id="impostaDetraibileAliquotaSubdocumentoIva%{suffisso}" name="aliquotaSubdocumentoIva.impostaDetraibile"
					cssClass="lbTextSmall span2 soloNumeri decimale" required="true" readonly="true" />
				<span class="alRight">
					<label for="impostaIndetraibileAliquotaSubdocumentoIva<s:property value='suffisso'/>"
						class="radio inline">Imposta indetraibile</label>
				</span>
				<s:textfield id="impostaIndetraibileAliquotaSubdocumentoIva%{suffisso}" name="aliquotaSubdocumentoIva.impostaIndetraibile"
					cssClass="lbTextSmall span2 soloNumeri decimale" required="true" readonly="true" />
			</div>
		</div>
	</fieldset>
	<p>
		<button type="button" class="btn btn-secondary"
				id="pulsanteAnnullaInserisciMovimentiIva<s:property value='suffisso'/>">annulla</button>
		<button type="button" class="btn btn-primary pull-right"
				id="pulsanteConfermaInserisciMovimentiIva<s:property value='suffisso'/>">
			aggiungi movimento&nbsp;<i class="icon-spin icon-refresh spinner"
				id="SPINNER_pulsanteConfermaInserisciMovimentiIva<s:property value='suffisso'/>"></i>
		</button>
	</p>
</div>