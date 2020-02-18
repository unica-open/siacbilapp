<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="well margin-medium">
	<div>
		<label class="control-label" for="numeroRichiestaRichestaEconomaleCopia">Copia dati da</label>
		<div class="controls">
			<s:textfield id="numeroRichiestaRichestaEconomaleCopia" name="richiestaEconomaleCopia.numeroRichiesta" cssClass="span3" placeholder="numero richiesta" />
			<span class="pull-right">
				<a data-href="<s:property value="urlCopiaRichiestaEconomale"/>" class="btn btn-primary" data-element-overlay="body" id="pulsanteCopiaRichiestaEconomale">copia</a>
			</span>
		</div>
	</div>
</div>