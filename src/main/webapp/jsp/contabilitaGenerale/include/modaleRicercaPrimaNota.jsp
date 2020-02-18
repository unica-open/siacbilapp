<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="comp-PrimaNota" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h3 id="myModalLabel">Prima nota da collegare</h3>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modaleRicercaPrimaNota">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br>
			<ul>
			</ul>
		</div>
		<fieldset class="form-horizontal" id="fieldsetModaleRicercaPrimaNota">
			<div id="campiRicercaPrimaNota" class="accordion-body collapse in">
				<s:hidden id="HIDDEN_ambitoModale" name ="ambito"/>
			
				<div class="control-group">
					<label class="control-label" for="annoPrimaNota_modale">Anno</label>
					<div class="controls">
						<s:textfield id="annoPrimaNota_modale" name="annoPrimaNota" cssClass="span2"/>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="numeroPrimaNota_modale">Numero definitivo</label>
					<div class="controls">
						<s:textfield id="numeroPrimaNota_modale" name="primaNota.numeroRegistrazioneLibroGiornale" cssClass="span2"/>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="tipoPrimaNota_modale">Tipo</label>
					<div class="controls">
					<s:select list="listaTipoPrimaNota" name="primaNota.tipoCausale" 
  						id="tipoPrimaNota_modale" cssClass="span6" headerKey="" headerValue=""
 						 listValue="%{codice + ' - ' + descrizione}" />
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="tipoEvento_modale">Tipo Evento</label>
					<div class="controls">
					<s:select list="listaTipiEvento" name="tipoEvento.uid"
 							id="tipoEvento_modale" cssClass="span6" headerKey="" headerValue=""  
						listKey="uid" listValue="%{codice + ' - ' + descrizione}" /> 
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" >Movimento finanziario
						<a href="#" class="tooltip-test" data-original-title="selezionare il tipo evento">
							<i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare il tipo evento</span></i>
						</a>
					</label>
					<div class="controls">
						<span class="al">
							<label class="radio inline" for="annoMovimento_modale">Anno</label>
						</span>
						<s:textfield id="annoMovimento_modale" name="annoMovimento" cssClass="span2"/>
						<span class="al">
							<label class="radio inline" for="numeroMovimento_modale">Numero</label>
						</span>
						<s:textfield id="numeroMovimento_modale" name="numeroMovimento" cssClass="span2"/>
 						<span class="al"> 
							<label class="radio inline" for="numeroSubmovimento_modale">Submovimento</label> 
 						</span>
 						<s:textfield id="numeroSubmovimento_modale" name="numeroSubmovimento" cssClass="span2"/>
					</div>
				</div>
				
				<div class="control-group">
					<label class="control-label" for="codiceContoFinanziario_modale">Codice conto finanziario
						<a href="#" class="tooltip-test" data-original-title="selezionare il tipo evento">
							<i class="icon-info-sign">&nbsp;<span class="nascosto">selezionare il tipo evento</span></i>
						</a>
					</label>
					<div class="controls">
						<s:textfield id="codiceContoFinanziario_modale" name="elementoPianoDeiConti.codice" cssClass="span6"/>
					</div>
				</div>
				
			</div>
			<button type="button" class="btn btn-primary pull-right" id="bottoneCercaModaleRicercaPrimaNota">
				<i class="icon-search icon"></i>&nbsp;cerca&nbsp;<i class="icon-spin icon-refresh spinner" id="spinnerModaleRicercaPrimaNota"></i>
			</button>
		</fieldset>
		<div id="risultatiRicercaPrimaNota" class="hide">
			<h4>Elenco Prime Note</h4>
			<table class="table table-hover tab_left" id="tabellaRisultatiRicercaPrimaNota">
				<thead>
					<tr>
						<th></th>
						<th>Tipo</th>
						<th>Anno</th>
						<th>Numero definitivo</th>
						<th>Tipo evento</th>
						<th>Movimento finanziario</th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
	</div>

	<div class="modal-footer">
		<button class="btn btn-primary"  aria-hidden="true" id="pulsanteConfermaModaleRicercaPrimaNota">conferma</button>
	</div>
 
</div>