<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="modaleRiscontiLabel" role="dialog" tabindex="-1" class="modal hide fade" id="modaleInserimentoAggiornamentoRisconti">
	<div class="modal-header">
		<button type="button" class="close" id="buttonCloseModal" data-dismiss="modal" aria-hidden="true">&times;</button>
		<h3 id="modaleRiscontiLabel">Risconti</h3>
	</div>
	<div class="modal-body">
		<div class="alert alert-error hide" id="ERRORI_modaleRisconti">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<strong>Attenzione!!</strong><br/>
			<ul>
			</ul>
		</div>
		<div class="alert alert-success hide" id="INFORMAZIONI_modaleRisconti">
			<button type="button" class="close" data-hide="alert">&times;</button>
			<ul></ul>
		</div>
		
		<h4>Risconti inseriti</h4>
		<table class="table table-hover tab_left" id="tabellaRiscontiInseriti">
			<thead>
				<tr>
					<th>Anno</th>
					<th>Importo</th>
					<th class="tab_Right span2">&nbsp;</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<p>
			<button type="button" id="pulsanteInserisciRisconto" class="btn btn-secondary">
				inserisci risconto&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteInserisciRisconto"></i>
			</button>
		</p>
		
		<div class="hide" id="collapseInserimentoAggiornamentoRisconto">
			<div class="step-content">
				<div class="step-pane active" id="step1">
					<h4 class="step-pane">Risconto</h4>
					<fieldset class="form-horizontal" id="fieldsetModaleRisconti">
		
						<s:hidden id="HIDDEN_primaNotaRisconto" name ="risconto.primaNota.uid"/>
						<s:hidden id="HIDDEN_suffixRisconto"/>
						<s:hidden id="HIDDEN_idxRisconto" name="idxRisconto"/>
					
						<div class="control-group">
							<label class="control-label" for="annoRisconto_modale">Anno</label>
							<div class="controls">
								<s:textfield id="annoRisconto_modale" name="risconto.anno" cssClass="span2"/>
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="importoRisconto_modale">Importo rinviabile</label>
							<div class="controls">
								<s:textfield id="importoRisconto_modale" name="risconto.importo" cssClass="lbTextSmall span2 soloNumeri decimale"/>
							</div>
						</div>
						<p>
						<button type="button"  class="btn btn-secondary" id="pulsanteAnnullaRisconti">annulla</button>
							<span class="pull-right">
								<button type="button" class="btn btn-primary" id="pulsanteSalvaRisconti">salva risconto</button>
							</span>
						</p>
					</fieldset>
				</div>
			</div>
		</div>
		<div>	
			<p>
				<span class="pull-right">
					<button type="button" class="btn btn-primary" id="pulsanteChiudiModaleRisconti">chiudi
						&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_pulsanteChiudiModaleRisconti"></i>
					</button>
				</span>
			</p>
		</div>
		<br>
		<br>
	</div>
</div>