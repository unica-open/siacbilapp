<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<h4 class="step-pane">Dati importi</h4>
<div class="control-group">
	<label class="control-label" for="importoDocumento">Importo *</label>
	<div class="controls">
		<s:textfield id="importoDocumento" name="documento.importo" cssClass="lbTextSmall span2 soloNumeri decimale" placeholder="importo" required="required" />
		<span class="alRight">
			<label for="arrotondamentoImportoDocumento" class="radio inline">Arrotondamento</label>
		</span>
		<s:textfield id="arrotondamentoImportoDocumento" name="documento.arrotondamento" cssClass="lbTextSmall span2 soloNumeri decimale" placehoder="arrotondamento" />
		<span class="alRight">
			<label for="nettoImportoDocumento" class="radio inline">Netto</label>
		</span>
		<input type="text" disabled="disabled" class="lbTextSmall span2" id="nettoImportoDocumento">
	</div>
</div>
<div class="control-group">
	<label class="control-label" for="descrizioneDocumento">Descrizione *</label>
	<div class="controls">
		<s:textarea id="descrizioneDocumento" name="documento.descrizione" cssClass="span10" cols="15" rows="2" required="required" placeholder="descrizione"></s:textarea>
	</div>
</div>