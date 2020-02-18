<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<span id="tipoProgettoAssociato"class="hide"><s:property value="tipoProgettoCollegato"/></span>

<div class="control-group">
	<label class="control-label" for="versioneCronoprogramma">Versione *</label>
	<div class="controls">
		<s:textfield id="versioneCronoprogramma" cssClass="lbTextSmall span3" name="cronoprogramma.codice" maxlength="200" placeholder="versione" required="required" />
		<span class="al">
		<label class="radio inline">Stato</label>
		</span>
		<span>
			<input type="text" disabled class="lbTextSmall span3" maxlength="30" <s:if test="%{cronoprogramma.statoOperativoCronoprogramma != null}"> value="<s:property value="cronoprogramma.statoOperativoCronoprogramma.descrizione"/>" </s:if> />
		</span>
		<s:hidden name="cronoprogramma.statoOperativoCronoprogramma"/>
	</div>
	
</div>

<div class="control-group">
	<span class="al">
		<label for="descrizioneCronoprogramma" class="control-label">Descrizione *</label>
	</span>
	<div class="controls">
		<s:textarea id="descrizioneCronoprogramma" rows="1" cols="15" cssClass="span9" name="cronoprogramma.descrizione" maxlength="500" required="required" placeholder="descrizione"></s:textarea>
	</div>
</div>

<div class="control-group">
	<span class="al">
		<label for="noteCronoprogramma" class="control-label">Note</label>
	</span>
	<div class="controls">
		<s:textarea id="noteCronoprogramma" rows="2" cols="15" cssClass="span8" name="cronoprogramma.note" maxlength="500" required="required"></s:textarea>
	</div>
</div>

<div class="control-group">
	<span class="al">
		<label class="control-label">Cronoprogramma da definire&nbsp;*</label>
	</span>
	<div class="controls">
		<label class="radio inline">
			<input type="radio" value="true" name="cronoprogramma.cronoprogrammaDaDefinire" <s:if test="cronoprogramma.cronoprogrammaDaDefinire">checked</s:if>>S&iacute;
		</label>
		<label class="radio inline">
			<input type="radio" value="false" name="cronoprogramma.cronoprogrammaDaDefinire" <s:if test="!cronoprogramma.cronoprogrammaDaDefinire">checked</s:if>>No
		</label>
	</div>
</div>

<div class="control-group">
	<label for="dataApprovazioneFattibilita" class="control-label">Data approvazione fattibilit&agrave;</label>
	<div class="controls">
		<s:textfield id="dataApprovazioneFattibilita" name="cronoprogramma.dataApprovazioneFattibilita" cssClass="span2 datepicker" />
	</div>
</div>
<div class="control-group">
	<label for="dataApprovazioneProgettoDefinitivo" class="control-label">Data approvazione progetto definitivo</label>
	<div class="controls">
		<s:textfield id="dataApprovazioneProgettoDefinitivo" name="cronoprogramma.dataApprovazioneProgettoDefinitivo" cssClass="span2 datepicker" />
	</div>
</div>
<div class="control-group">
	<label for="dataApprovazioneProgettoEsecutivo" class="control-label">Data approvazione progetto esecutivo</label>
	<div class="controls">
		<s:textfield id="dataApprovazioneProgettoEsecutivo" name="cronoprogramma.dataApprovazioneProgettoEsecutivo" cssClass="span2 datepicker" />
	</div>
</div>
<div class="control-group">
	<label for="dataAvvioProcedura" class="control-label">Data avvio procedura</label>
	<div class="controls">
		<s:textfield id="dataAvvioProcedura" name="cronoprogramma.dataAvvioProcedura" cssClass="span2 datepicker" />
	</div>
</div>
<div class="control-group">
	<label for="dataAggiudicazioneLavori" class="control-label">Data aggiudicazione lavori</label>
	<div class="controls">
		<s:textfield id="dataAggiudicazioneLavori" name="cronoprogramma.dataAggiudicazioneLavori" cssClass="span2 datepicker" />
	</div>
</div>
<div class="control-group">
	<label for="dataInizioLavori" class="control-label">Data inizio lavori</label>
	<div class="controls">
		<s:textfield id="dataInizioLavori" name="cronoprogramma.dataInizioLavori" cssClass="span2 datepicker" />
	</div>
</div>
<div class="control-group">
	<label for="dataFineLavori" class="control-label">Data fine lavori</label>
	<div class="controls">
		<s:textfield id="dataFineLavori" name="cronoprogramma.dataFineLavori" cssClass="span2" disabled="true" />
		<s:hidden id="HIDDEN_dataFineLavori" name="cronoprogramma.dataFineLavori" cssClass="span2 datepicker"/>
	</div>
</div>
<div class="control-group">
	<label class="control-label" for="giorniDurata">Durata (gg)</label>
	<div class="controls">
		<s:textfield id="giorniDurata" cssClass="lbTextSmall span1 text right" name="cronoprogramma.durataInGiorni" maxlength="200" placeholder=""/>
	</div>
</div>
<div class="control-group">
	<label for="dataCollaudo" class="control-label">Data collaudo</label>
	<div class="controls">
		<s:textfield id="dataCollaudo" name="cronoprogramma.dataCollaudo" cssClass="span2 datepicker" />
	</div>
</div>
<div class="control-group">
	<span class="al">
		<label class="control-label">Gestione quadro economico</label>
	</span>
	<div class="controls">
		<s:if test ="collegatoAProgettoDiGestione">
			<label class="radio inline">
				<input type="radio" value="true" name="cronoprogramma.gestioneQuadroEconomico" <s:if test="cronoprogramma.gestioneQuadroEconomico">checked</s:if>>S&iacute;
			</label>
			<label class="radio inline">
				<input type="radio" value="false" name="cronoprogramma.gestioneQuadroEconomico" <s:if test="cronoprogramma.uid == 0 || !cronoprogramma.gestioneQuadroEconomico">checked</s:if>>No
			</label>
		</s:if><s:else>
			<label class="radio inline">
				<input type="radio" value="true" name="cronoprogramma.gestioneQuadroEconomico" disabled>S&iacute;
			</label>
			<label class="radio inline">
				<input type="radio" value="false" name="cronoprogramma.gestioneQuadroEconomico"  checked disabled>No
			</label>
			<s:hidden name="cronoprogramma.gestioneQuadroEconomico" value="false"/>
		</s:else>
	</div>
</div>
<s:include value="/jsp/provvedimento/ricercaProvvedimentoCollapse.jsp" />