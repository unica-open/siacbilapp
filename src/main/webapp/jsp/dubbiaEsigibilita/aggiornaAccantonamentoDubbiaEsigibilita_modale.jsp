<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div id="modaleAggiornamentoAccantonamentoDubbiaEsigibilita" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="intestazione_modaleAggiornamentoAccantonamentoDubbiaEsigibilita" aria-hidden="true">
	<div class="row-fluid">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			<h4 class="nostep-pane" id="intestazione_modaleAggiornamentoAccantonamentoDubbiaEsigibilita">Aggiornamento dati per capitolo <span id="infoCapitolo_modaleAggiornamentoAccantonamentoDubbiaEsigibilita"></span></h4>
		</div>

		<div class="modal-body">
			<div class="alert alert-error hide"	id="ERRORI_modaleAggiornamentoAccantonamentoDubbiaEsigibilita">
				<button type="button" class="close" data-hide="alert">&times;</button>
				<strong>Attenzione!!</strong><br>
				<ul></ul>
			</div>
			<form>
				<fieldset class="form-horizontal" id="fieldset_modaleAggiornamentoAccantonamentoDubbiaEsigibilita">
					<div class="accordion-body">
						<div class="control-group">
							<label class="control-label" for="anno4_modaleAggiornamentoAccantonamentoDubbiaEsigibilita">&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 4}" /></label>
							<div class="controls">
								<input type="text" class="input-small soloNumeri decimale text-right" name="percentualeAccantonamentoFondi4" data-uba="4"
									id="anno4_modaleAggiornamentoAccantonamentoDubbiaEsigibilita" data-format-money <s:if test="riscossioneVirtuosa">readonly</s:if> />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="anno3_modaleAggiornamentoAccantonamentoDubbiaEsigibilita">&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 3}" /></label>
							<div class="controls">
								<input type="text" class="input-small soloNumeri decimale text-right" name="percentualeAccantonamentoFondi3" data-uba="3"
									id="anno3_modaleAggiornamentoAccantonamentoDubbiaEsigibilita" data-format-money />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="anno2_modaleAggiornamentoAccantonamentoDubbiaEsigibilita">&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 2}" /></label>
							<div class="controls">
								<input type="text" class="input-small soloNumeri decimale text-right" name="percentualeAccantonamentoFondi2" data-uba="2"
									id="anno2_modaleAggiornamentoAccantonamentoDubbiaEsigibilita" data-format-money />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="anno1_modaleAggiornamentoAccantonamentoDubbiaEsigibilita">&#37; <s:property value="%{attributiBilancio.ultimoAnnoApprovato - 1}" /></label>
							<div class="controls">
								<input type="text" class="input-small soloNumeri decimale text-right" name="percentualeAccantonamentoFondi1" data-uba="1"
									id="anno1_modaleAggiornamentoAccantonamentoDubbiaEsigibilita" data-format-money />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="anno_modaleAggiornamentoAccantonamentoDubbiaEsigibilita">&#37; <s:property value="attributiBilancio.ultimoAnnoApprovato" /></label>
							<div class="controls">
								<input type="text" class="input-small soloNumeri decimale text-right" name="percentualeAccantonamentoFondi" data-uba="0"
									id="anno_modaleAggiornamentoAccantonamentoDubbiaEsigibilita" data-format-money <s:if test="riscossioneVirtuosa">readonly</s:if> />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="media_modaleAggiornamentoAccantonamentoDubbiaEsigibilita">&#37; MEDIA</label>
							<div class="controls">
								<input type="text" class="input-small soloNumeri decimale text-right" name="percentualeMediaAccantonamento" data-media readonly
									id="media_modaleAggiornamentoAccantonamentoDubbiaEsigibilita" data-format-money />
							</div>
						</div>
						<div class="control-group">
							<label class="control-label" for="delta_modaleAggiornamentoAccantonamentoDubbiaEsigibilita">&#37; con delta</label>
							<div class="controls">
								<input type="text" class="input-small soloNumeri decimale text-right" name="percentualeDelta" data-delta
									id="delta_modaleAggiornamentoAccantonamentoDubbiaEsigibilita" data-format-money />
							</div>
						</div>
						<input type="hidden" name="uid"/>
						<input type="hidden" name="capitolo.uid"/>
					</div>
			</fieldset>
		</form>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn">no, indietro</button>
		<button type="button" class="btn btn-primary" id="pulsanteConferma_modaleAggiornamentoAccantonamentoDubbiaEsigibilita">si, prosegui</button>
	</div>
</div>
</div>