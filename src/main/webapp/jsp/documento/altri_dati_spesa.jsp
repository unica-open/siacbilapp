<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<h4 class="step-pane">Altri dati</h4>
<div class="control-group">
	<label for="terminePagamentoDocumento" class="control-label">Termine di pagamento</label>
	<div class="controls">
		<s:textfield id="terminePagamentoDocumento" name="documento.terminePagamento" cssClass="lbTextSmall span2 numeroNaturale" placeholder="xxxx" />
		<span class="alRight">
			<label for="dataScadenzaDocumento" class="radio inline">Data scadenza</label>
		</span>
		<s:textfield id="dataScadenzaDocumento" name="documento.dataScadenza" cssClass="lbTextSmall span2" placeholder="xx/xx/xxxx" />
	</div>
</div>
<div class="control-group">
	<label class="control-label">Sospensione</label>
	<div class="controls">
		<span class="al">
			<label for="dataSospensioneDocumento" class="radio inline">Data</label>
		</span>
		<s:textfield id="dataSospensioneDocumento" name="documento.dataSospensione" cssClass="lbTextSmall span2" placeholder="xx/xx/xxxx" />
		<span class="al">
			<label for="causaleSospensioneDocumento" class="radio inline">Causale</label>
		</span>
		<s:textfield id="causaleSospensioneDocumento" name="documento.causaleSospensione" cssClass="span4" />
		<span class="al">
			<label for="dataRiattivazioneDocumento" class="radio inline">Data riattivazione</label>
		</span>
		<s:textfield id="dataRiattivazione" name="documento.dataRiattivazione" cssClass="lbTextSmall span2" placeholder="xx/xx/xxxx" />
		</div>
</div>
<div class="control-group">
	<label class="control-label">Dati repertorio/protocollo</label>
	<div class="controls">
		<span class="alRight">
			<label for="numeroRepertorioDocumento" class="radio inline">Numero</label>
		</span>
		<s:textfield id="numeroRepertorioDocumento" name="documento.numeroRepertorio" cssClass="lbTextSmall span2" placeholder="numero" />
		<span class="alRight">
			<label for="dataRepertorioDocumento" class="radio inline">Data</label>
		</span>
		<s:textfield id="dataRepertorioDocumento" name="documento.dataRepertorio" cssClass="lbTextSmall span2" placeholder="data" />
		<input type="text" name="scadenza1" value="23/02/2014" class="lbTextSmall span2" id="scadenza1">
	</div>
</div>
<div class="control-group">
	<label for="codiceFiscalePignoratoDocumento" class="control-label">Soggetto pignorato</label>
	<div class="controls">
		<s:textfield id="codiceFiscalePignoratoDocumento" name="documento.codiceFiscalePignorato" cssClass="lbTextSmall span4" placeholder="codice fiscale" />
	</div>
</div>