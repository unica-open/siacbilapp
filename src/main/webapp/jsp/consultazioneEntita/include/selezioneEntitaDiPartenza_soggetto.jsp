<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<s:form id="formSelezionaEntitaDiPartenza_soggetto" action="" cssClass="form-horizontal hide" novalidate="novalidate">
<div  id="buttonSliding"  data-toggle="slidewidth" class=" fieldset-heading button-sliding" data-target="#selezioneConsultazioneEntitaCollegate">								
	<h4>
		<i id ="buttonSlidingIcon" data-original-title="" class="icon-double-angle-left sliding-icon icon-large tooltip-test defaultcolor"></i>&nbsp;									
		Selezione soggetto
	</h4>								
</div>
<p>Specificare l'elemento da cui iniziare la consultazione: </p>

	<h3>&nbsp;</h3>
	<div class="control-group">
		<label class="control-label" for="codiceSoggetto">Codice</label>
		<div class="controls">
			<s:textfield id="codiceSoggetto" cssClass="span3" name="codiceSoggetto" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="denominazioneSoggetto">Denominazione</label>
		<div class="controls">
			<s:textfield id="denominazioneSoggetto" cssClass="span4" name="denominazioneSoggetto" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="codiceFiscaleSoggetto">Codice Fiscale</label>
		<div class="controls">
			<s:textfield id="codiceFiscaleSoggetto" cssClass="span4 uppercase" name="codiceFiscaleSoggetto" maxlength="16" />
		</div>
	</div>
	<div class="control-group">
		<label class="control-label" for="partitaIvaSoggetto">Partita IVA </label>
		<div class="controls">
			<s:textfield id="partitaIvaSoggetto" cssClass="span3" name="partitaIvaSoggetto" />
		</div>
	</div>
	<div class=control-group>
			<s:submit id = "pulsanteRicercaEntitaDiPartenzaSoggetto" cssClass="btn btn-primary btn-block" value="cerca" />
		</div>
</s:form>
