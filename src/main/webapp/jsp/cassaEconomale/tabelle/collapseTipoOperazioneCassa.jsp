<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="accordion_info">
	<fieldset id="fieldsetOperazioneCassa" class="form-horizontal">
		<h4 class="titleTxt nostep-pane"><s:property value="operazioneTipiOperazioneDiCassa.descrizione" /></h4>
		<s:hidden id="uidTipoOperazioneCassa" name="tipoOperazioneCassa.uid" />
		<h4 class="step-pane">Dati</h4>
		<div class="control-group">
			<label class="control-label" for="codiceTipoOperazioneCassa">Codice *</label>
			<div class="controls">
				<s:textfield id="codiceTipoOperazioneCassa" name="tipoOperazioneCassa.codice" cssClass="span3" maxlength="200" required="required"
					readonly="%{!operazioneTipiOperazioneDiCassa.editabile}" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">Tipo *</label>
			<div class="controls">
				<s:iterator value="listaTipologiaOperazioneCassa" var="toc">
					<label class="radio inline">
						<input type="radio" name="tipoOperazioneCassa.tipologiaOperazioneCassa" value="<s:property value="%{#toc}" />"
							<s:if test="%{tipoOperazioneCassa.tipologiaOperazioneCassa == #toc}">checked="checked"</s:if>
							<s:if test="%{!operazioneTipiOperazioneDiCassa.editabile}">disabled</s:if>>
						&nbsp;<s:property value="%{#toc.descrizione}" />
					</label>
				</s:iterator>
				<s:if test="%{!operazioneTipiOperazioneDiCassa.editabile}">
					<s:hidden name="tipoOperazioneCassa.tipologiaOperazioneCassa" />
				</s:if>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="descrizioneTipoOperazioneCassa">Descrizione *</label>
			<div class="controls">
				<s:textfield id="descrizioneTipoOperazioneCassa" name="tipoOperazioneCassa.descrizione" cssClass="span9" maxlength="500" required="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="inclusoInGiornaleTipoOperazioneCassa">Incluso in giornale</label>
			<div class="controls">
				<s:checkbox id="inclusoInGiornaleTipoOperazioneCassa" name="tipoOperazioneCassa.inclusoInGiornale" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label" for="inclusoInRendicontoTipoOperazioneCassa">Incluso in rendiconto</label>
			<div class="controls">
				<s:checkbox id="inclusoInRendicontoTipoOperazioneCassa" name="tipoOperazioneCassa.inclusoInRendiconto" />
			</div>
		</div>
	</fieldset>
	<p>
		<button type="button" id="annullaOperazioneCassa" class="btn btn-secondary">annulla</button>
		<span class="pull-right">
			<button type="button" id="salvaOperazioneCassa" class="btn btn-primary">salva</button>
		</span>
	</p>
</div>