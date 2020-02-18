<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="hide" data-pn="LIB">
	<div class="control-group">
		<label class="control-label">Numero</label>
		<div class="controls">
			<span class="al">
				<label class="radio inline" for="LIB_primaNotaNumero">Provvisorio</label>
			</span>
			<s:textfield name="primaNota.numero" id="LIB_primaNotaNumero" cssClass="span2 soloNumeri" />
			<span class="al">
				<label class="radio inline" for="LIB_primaNotaNumeroRegistrazione">Definitivo</label>
			</span>
			<s:textfield name="primaNota.numeroRegistrazioneLibroGiornale" id="LIB_primaNotaNumeroRegistrazione" cssClass="span2 soloNumeri" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="LIB_evento">Evento</label>
		<div class="controls">
			<s:select name="evento.uid" id="LIB_evento" cssClass="span6" list="listaEvento" headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-evento="" />
		</div>
	</div>
	<div class="control-group"> 
		<label class="control-label" for="LIB_causaleEP">Causale</label>
		<div class="controls">
			<s:select name="causaleEP.uid" id="LIB_causaleEP" cssClass="span6" list="listaCausaleEP" headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" data-causale-ep="" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="LIB_contoCodice">Conto</label>
		<div class="controls">
			<s:textfield name="conto.codice" id="LIB_contoCodice" cssClass="span3" />
			<span id="LIB_descrizioneConto"></span>
			<span class="radio guidata">
				<button type="button" class="btn btn-primary" id="LIB_pulsanteCompilazioneGuidataConto" data-toggle="modal">compilazione guidata</button>
			</span>
		</div>
	</div>
	<div class="control-group"> 
		<label class="control-label" for="LIB_primaNotaStatoOperativoPrimaNota">Stato Coge</label>
		<div class="controls">
			<s:select name="primaNota.statoOperativoPrimaNota" id="LIB_primaNotaStatoOperativoPrimaNota" cssClass="span6" data-causale-ep="" list="listaStatoOperativoPrimaNota"
				headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" />
		</div>
	</div>

	<div class="control-group"> 
		<label class="control-label" for="LIB_primaNotaStatoAccettazionePrimaNotaDefinitiva">Stato Inv</label>
		<div class="controls">
			<s:select name="primaNota.statoAccettazionePrimaNotaDefinitiva" id="LIB_primaNotaStatoAccettazionePrimaNotaDefinitiva" cssClass="span6" data-causale-ep="" list="listaStatoAccettazionePrimaNotaDefinitiva"
				headerKey="" headerValue="" listValue="%{codice + ' - ' + descrizione}" />
		</div>
	</div>


	<div class="control-group">
		<label class="control-label" for="LIB_primaNotaDescrizione">Descrizione</label>
		<div class="controls">
			<s:textfield name="primaNota.descrizione" value="" id="LIB_primaNotaDescrizione" cssClass="span9" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">Data registrazione definitiva</label>
		<div class="controls">
			<span class="al">
				<label class="radio inline" for="LIB_dataRegistrazioneDa">Da</label>
			</span>
			<s:textfield name="dataRegistrazioneDefinitivaDa" maxLength="10" id="LIB_dataRegistrazioneDa" cssClass="span2 datepicker" tabindex="-1" />
			<span class="al">
				<label class="radio inline" for="LIB_dataRegistrazioneA">A</label>
			</span>
			<s:textfield name="dataRegistrazioneDefinitivaA" maxLength="10" id="LIB_dataRegistrazioneA" cssClass="span2 datepicker" tabindex="-1" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label">Data registrazione provvisoria</label>
		<div class="controls">
			<span class="al">
				<label class="radio inline" for="LIB_dataRegistrazioneProvvisoriaDa">Da</label>
			</span>
			<s:textfield name="dataRegistrazioneProvvisoriaDa" maxLength="10" id="LIB_dataRegistrazioneProvvisoriaDa" cssClass="span2 datepicker" tabindex="-1" />
			<span class="al">
				<label class="radio inline" for="LIB_dataRegistrazioneProvvisoriaA">A</label>
			</span>
			<s:textfield name="dataRegistrazioneProvvisoriaA" maxLength="10" id="LIB_dataRegistrazioneProvvisoriaA" cssClass="span2 datepicker" tabindex="-1" />
		</div>
	</div>
</div>