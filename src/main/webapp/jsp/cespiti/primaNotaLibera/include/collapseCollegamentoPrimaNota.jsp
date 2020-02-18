<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div class="clear"></div> 
<br>
<div class="collapse" id="collapseCollegamentoPrimaNota">

	<div class="step-content">
		<div class="step-pane active" id="step1">
			<h4 class="step-pane">Collegamento prima nota</h4>
			<fieldset id="fieldsetCollegamentoPrimaNota" class="form-horizontal">
				<div class="control-group">
					<label class="control-label" >Prima nota </label>
					<div class="controls">
						<span class="al">
							<label class="radio inline" for="annoPrimaNota">Anno *</label>
						</span>
						<s:textfield id="annoPrimaNota" cssClass="lbTextSmall span2" name="annoPrimaNota" maxlength="4" placeholder="anno" />
						<span class="al">
							<label class="radio inline" for="numeroPrimaNota">Numero definitivo *</label>
						</span>
						<s:textfield id="numeroPrimaNota" cssClass="lbTextSmall span2" name="primaNotaDaCollegare.numeroRegistrazioneLibroGiornale" placeholder="numero" />
 						<span class="al"> 
							<label class="radio inline" for="selectTipoPrimaNota">Tipo *</label> 
 						</span>
 						<s:select id="selectTipoPrimaNota" list="listaTipoPrimaNota" name="primaNotaDaCollegare.tipoCausale" headerKey="" headerValue="" 
 							listValue="%{codice + ' - ' + descrizione}" cssClass="span2"/>
 							
						<button type="button" class="btn btn-primary pull-right" id="pulsanteCompilazioneGuidataPrimaNota" >compilazione guidata</button>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label" for="selectMotivazione">Motivazione</label>
					<div class="controls">
							<s:select list="listaMotivazioni" id="selectMotivazione"  cssClass="span4"
								name="motivazione.uid" headerKey="" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" />
					</div> 
				</div>
				
				<div class="control-group">
					<label class="control-label" for="noteCollegamento">Note</label>
					<div class="controls">
						<s:textfield id="noteCollegamento" name="noteCollegamento" cssClass="span4"/>
					</div>
				</div>
			</fieldset>
			<div class="Border_line"></div>
			<p>
				<button type="button" aria-controls="collapseCollegamentoPrimaNota" aria-expanded="false" data-toggle="collapse" class="btn btn-secondary" data-target="#collapseCollegamentoPrimaNota">annulla</button>
				<span class="pull-right">
					<button type="button" class="btn btn-primary" id="pulsanteSalvaCollegamento">salva collegamento</button>
				</span>
			</p>
		</div>
	</div>
</div>