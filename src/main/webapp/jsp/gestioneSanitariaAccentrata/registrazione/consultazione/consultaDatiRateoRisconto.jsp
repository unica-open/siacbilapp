<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<h4 class="step-pane"><s:property value="consultazioneHelper.datiCreazioneModifica" /></h4>
<div class="boxOrSpan2">
	<div class="boxOrInLeft-multi">
		<p>Dati <s:property value="consultazioneHelper.tipoRateoRisconto"/></p>
		<ul class="htmlelt">
			<li>
				<dfn>Anno</dfn>
				<dl><s:property value="consultazioneHelper.rateoRisconto.anno"/></dl>
			</li>
			<li>
				<dfn>Importo</dfn>
				<dl><s:property value="consultazioneHelper.rateoRisconto.importo"/></dl>
			</li>
		</ul>
	</div>
	<div class="boxOrInRight-multi">
		<p>Dati <s:property value="consultazioneHelper.tipoMovimentoRateoRisconto"/></p>
		<ul class="htmlelt">
			<li>
				<dfn>Anno</dfn>
				<dl><s:property value="consultazioneHelper.annoMovimentoRateoRisconto"/></dl>
			</li>
			<li>
				<dfn>Numero</dfn>
				<dl><s:property value="consultazioneHelper.numeroMovimentoRateoRisconto"/></dl>
			</li>
			<li>
				<dfn>Descrizione</dfn>
				<dl><s:property value="consultazioneHelper.descrizioneMovimentoRateoRisconto"/></dl>
			</li>
			<li>
				<dfn>Soggetto</dfn>
				<dl><s:property value="consultazioneHelper.soggettoMovimentoRateoRisconto"/></dl>
			</li>
		</ul>
	</div>
</div>
