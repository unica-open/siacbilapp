<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div aria-hidden="true" aria-labelledby="labelModaleConfermaValidazioneMassiva" role="dialog" tabindex="-1" class="modal hide fade" id="modaleConfermaValidazioneMassiva">
	<div class="modal-header">
		<button aria-hidden="true" data-dismiss="modal" class="close" type="button">&times;</button>
		<h3 id="labelModaleConfermaValidazioneMassiva">Valida Prima Nota</h3>
	</div>
	<div class="modal-body">
		<div class="alert alert-error alert-persistent">
			<p><strong>Attenzione!</strong></p>
			<p>
				Stai per validare <span id="spanDatiModaleConfermaValidazioneMassiva"></span>
				<br/>
				Sei sicuro di voler proseguire?
			</p>
		</div>
		<fieldset>
			<div class="control-group">
				<div class="controls">
					<div id="classGSAParent" class="accordion span12 classGSA">
						<div class="accordion-group">
							<div class="accordion-heading">
								<a href="#classGSA" data-toggle="collapse" data-parent="#classGSAParent" class="accordion-toggle collapsed">
									<span id="SPAN_classificatoreGSA">Seleziona il classificatore</span>
								</a>
							</div>
							<div id="classGSA" class="accordion-body collapse">
								<div class="accordion-inner">
									<ul id="classGSATree" class="ztree"></ul>
									<button type="button" class="btn pull-right" data-deseleziona-ztree="classGSATree">Deseleziona</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				<s:hidden id="HIDDEN_classificatoreGSAUid" name="classificatoreGSA.uid" />
			</div>
			<div class="form-horizontal">
				<div class="control-group">
					<label class="control-label" for="dataRegistrazioneLibroGiornale">Data registrazione definitiva *</label>
					<div class="controls">
						<s:textfield id="dataRegistrazioneLibroGiornale" name="dataRegistrazioneLibroGiornale" cssClass="lbTextSmall span2 datepicker" size="10" required="true" />
					</div>
				</div>
			</div>
		</fieldset>
	</div>
	<div class="modal-footer">
		<button aria-hidden="true" data-dismiss="modal" class="btn">no, indietro</button>
		<button type="button" class="btn btn-primary" id="confermaModaleConfermaValidazioneMassiva">
			si, prosegui&nbsp;<i class="icon-spin icon-refresh spinner" id="SPINNER_confermaModaleConfermaValidazioneMassiva"></i>
		</button>
	</div>
</div>