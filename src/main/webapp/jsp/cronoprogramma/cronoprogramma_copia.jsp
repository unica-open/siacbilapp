<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Copia dati da Cronoprogramma --%>
<h5 class="subTitle"><i>Copia dati (opzionale)</i></h5>

<div class="control-group">
	<span class="al">
		<label for="checkCopiaDaCronoprogramma" class="control-label">Copia da cronoprogramma</label>
	</span>  
	<div class="controls">
		<input id="checkCopiaDaCronoprogramma" type="checkbox">
	</div>
</div>

<div class="control-group hide" id="campiRicercaCronoprogramma">
	<div class="controls">
		<span class="al">
			<label class="radio inline" for="annoRicercaCrono">Anno </label>
		</span>
		<s:textfield id="annoRicercaCrono" cssClass="lbTextSmall span2 soloNumeri" value="%{annoEsercizioInt}" disabled="true"/>
		<span class="al">
			<label class="radio inline" for="codiceProgettoRicercaCrono">Codice * </label>
		</span>
		<s:textfield id="codiceProgettoRicercaCrono" cssClass="lbTextSmall span2" value="%{progetto.codice}" required="true" maxlength="9" disabled="true"/>
		<span class="al">
			<label class="radio inline" for="tipoProgettoRicercaCrono">Tipo * </label>
		</span>
		<!-- task-170 -->
		<s:select list="listaTipiProgetto" id="tipoProgettoRicercaCrono" name="tipoProgettoStr" cssClass="span4" headerKey="" headerValue="" 
			listValue="%{descrizione}"  listKey="%{codice}" disabled="true"/>	
		<button type="button" class="pull-right btn-secondary" id="caricaCronoprogrammiDaCopiare"> carica cronoprogrammi</button>
	</div>
</div>

<div id="divSelezionaCronoprogramma" class="control-group hide">	
	
	<div class="controls">
		<span class="al">
			<label for="listaCronoprogrammaDaCopiare" class="control-label ">Seleziona da </label>
		</span>
		<select id="listaCronoprogrammaDaCopiare"></select>
		<span class="alLeft">
			<a class="btn btn-secondary" href="#" id="pulsanteCopiaDaCronoprogramma">
				Copia&nbsp;<i class="icon-spin icon-refresh spinner"></i>
			</a>
		</span> 
	</div>	
</div>