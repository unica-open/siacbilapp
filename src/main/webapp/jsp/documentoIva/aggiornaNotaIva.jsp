<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>

<%-- Inclusione head e CSS --%>
<h4 class="step-pane">Dati iva</h4>
<fieldset>
	<%--
	<s:if test="flagIntracomunitarioUtilizzabile">
		<div class="control-group">
			<label for="flagDocumentoIntracomunitario_nota" class="control-label">Documento intracomunitario</label>
			<div class="controls">
				<s:checkbox id="flagDocumentoIntracomunitario_nota" name="flagDocumentoIntracomunitarioNota" cssClass="flagDocumentoIntracomunitario" />
				<span id="campiDocumentoIntracomunitario_nota" class="fade">
					<span class="alRight">
						<label for="valuta_nota" class="radio inline">valuta estera</label>
					</span>
					<s:select list="listaValuta" name="valutaNota.uid" cssClass="lbTextSmall span2" id="valuta_nota" headerKey="0" headerValue=""
						listKey="uid" listValue="descrizione" />
					<span class="alRight">
						<label for="importoInValuta_nota" class="radio inline">importo in valuta</label>
					</span>
					<s:textfield id="importoInValuta_nota" name="importoInValutaNota" cssClass="lbTextSmall span2 soloNumeri decimale" />
				</span>
			</div>
		</div>
	</s:if>
	--%>
	<div class="control-group">
		<label for="nota_progressivoIvaSubdocumentoIVA" class="control-label">N. registrazione *</label>
		<div class="controls">
			<s:textfield id="nota_progressivoIVASubdocumentoIVA" name="nota.progressivoIVA" cssClass="lbTextSmall span2"
				required="true" readonly="true" data-maintain="" />
			<span class="alRight">
				<label for="nota_dataRegistrazioneSubdocumentoIva" class="radio inline">Data registrazione *</label>
			</span>
			<s:textfield id="nota_dataRegistrazioneSubdocumentoIva" name="nota.dataRegistrazione" cssClass="lbTextSmall span2"
				required="true" readonly="true" data-maintain="" />
		</div>
	</div>
	<div class="control-group">
		<label for="nota_tipoRegistrazioneIvaSubdocumentoIva" class="control-label">Tipo registrazione *</label>
		<div class="controls">
			<s:select list="listaTipoRegistrazioneIva" name="nota.tipoRegistrazioneIva.uid" id="nota_tipoRegistrazioneIvaSubdocumentoIva" headerKey="0"
				headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" cssClass="span6" disabled="true" data-maintain="" />
			<s:hidden name="nota.tipoRegistrazioneIva.uid" />
		</div>
	</div>
	<div class="control-group">
		<label for="nota_tipoRegistroIva" class="control-label">Tipo registro iva *</label>
		<div class="controls">
			<s:select list="listaTipoRegistroIva" name="tipoRegistroIvaNota" id="nota_tipoRegistroIva" headerKey="" headerValue=""
				listValue="%{codice + ' - ' + descrizione}" required="true" cssClass="span6" disabled="true" data-maintain="" />
			<s:hidden name="tipoRegistroIvaNota" />
		</div>
	</div>
	<div class="control-group">
		<label for="nota_attivitaIva" class="control-label">Attivit&agrave;</label>
		<div class="controls">
			<s:select list="listaAttivitaIva" name="attivitaIvaNota.uid" id="nota_attivitaIva" headerKey="0" headerValue=""
				listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" cssClass="span6" disabled="true" data-maintain="" />
			<s:hidden name="attivitaIvaNota.uid" />
			<span class="alRight">
				<label for="nota_flagRilevanteIRAPSubdocumentoIva" class="radio inline">Rilevante IRAP</label>
			</span>
			<s:checkbox id="nota_flagRilevanteIRAPSubdocumentoIva" name="nota.flagRilevanteIRAP" />
		</div>
	</div>
	<div class="control-group">
		<label for="nota_registroIvaSubdocumentoIva" class="control-label">Registro *</label>
		<div class="controls">
			<s:select list="listaRegistroIva" name="nota.registroIva.uid" cssClass="span6" id="nota_registroIvaSubdocumentoIva"
				headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" disabled="true" data-maintain="" />
			<s:hidden name="nota.registroIva.uid" />
		</div>
		
	</div>
	<div class="control-group hide" id="nota_gruppoProtocolloProvvisorio">
		<label for="nota_numeroProtocolloProvvisorioSubdocumentoIva" class="control-label">Protocollo provvisorio</label>
		<div class="controls">
			<s:textfield id="nota_numeroProtocolloProvvisorioSubdocumentoIva" name="nota.numeroProtocolloProvvisorio"
				cssClass="lbTextSmall span2 soloNumeri" placeholder="%{'numero'}" readonly="true" data-maintain="" />
			<span class="alRight">
				<label for="nota_dataProtocolloProvvisorioSubdocumentoIva" class="radio inline">In data</label>
			</span>
			<s:textfield id="nota_dataProtocolloProvvisorioSubdocumentoIva" name="nota.dataProtocolloProvvisorio"
				data-original-value="%{nota.dataProtocolloProvvisorio.getTime()}" data-date=""
				cssClass="lbTextSmall span2 datepicker" placeholder="%{'data'}" />
		</div>
	</div>
	<div class="control-group hide" id="nota_gruppoProtocolloDefinitivo">
		<label for="nota_numeroProtocolloDefinitivoSubdocumentoIva" class="control-label">Protocollo definitivo </label>
		<div class="controls">
			<s:textfield id="nota_numeroProtocolloDefinitivoSubdocumentoIva" name="nota.numeroProtocolloDefinitivo"
				cssClass="lbTextSmall span2 soloNumeri" placeholder="%{'numero'}" readonly="true" data-maintain="" />
			<span class="alRight">
				<label for="nota_dataProtocolloDefinitivoSubdocumentoIva" class="radio inline">In data</label>
			</span>
			<s:textfield id="nota_dataProtocolloDefinitivoSubdocumentoIva" name="nota.dataProtocolloDefinitivo"
				data-original-value="%{nota.dataProtocolloDefinitivo.getTime()}" data-date=""
				cssClass="lbTextSmall span2 datepicker" placeholder="%{'data'}" />
		</div>
	</div>
	<s:if test="tipoSubdocumentoIvaQuota">
		<div class="control-group">
			<label for="nota_numeroOrdinativoDocumentoSubdocumentoIva" class="control-label">N. ordinativo</label>
			<div class="controls">
				<s:textfield id="nota_numeroOrdinativoDocumentoSubdocumentoIva" name="nota.numeroOrdinativoDocumento"
					cssClass="lbTextSmall span2" readonly="true" data-maintain="" />
				<span class="alRight">
					<label for="nota_dataOrdinativoDocumentoSubdocumentoIva" class="radio inline">Data ordinativo</label>
				</span>
				<s:textfield id="nota_dataOrdinativoDocumentoSubdocumentoIva" name="nota.dataOrdinativoDocumento"
					cssClass="lbTextSmall span2" readonly="true" data-maintain="" />
				<span class="alRight">
					<label for="nota_dataCassaDocumentoSubdocumentoIva" class="radio inline">Data pagamento</label>
				</span>
				<s:textfield id="nota_dataCassaDocumentoSubdocumentoIva" name="nota.dataCassaDocumento"
					cssClass="lbTextSmall span2" readonly="true" data-maintain="" />
			</div>
		</div>
	</s:if>
	
	<div class="control-group">
		<label for="nota_descrizioneIvaSubdocumentoIva" class="control-label">Descrizione</label>
		<div class="controls">
			<s:textarea id="nota_descrizioneIvaSubdocumentoIva" name="nota.descrizioneIva"
				cssClass="input-medium span9" cols="15" rows="2" maxlength="500"></s:textarea>
		</div>
	</div>
	<p>
		<a data-target="#nota_collapseMovimentiIva" data-toggle="collapse" class="btn btn-primary">movimenti iva</a>
	</p>
	
	<div class="collapse" id="nota_collapseMovimentiIva">
		<s:include value="/jsp/documentoIva/movimentiIva.jsp">
			<s:param name="prefix">nota_</s:param>
			<s:param name="suffix"></s:param>
		</s:include>
	</div>
</fieldset>
<div class="Border_line"></div>
<p class="margin-medium">
	<s:include value="/jsp/include/indietro.jsp" />
	<button type="button" class="btn reset">annulla</button>
	<button type="button" class="btn btn-primary pull-right pulsanteSalvaFormIva" id="pulsanteSalvaForm_nota">aggiorna</button>
</p>

<div aria-hidden="true" aria-labelledby="msgEliminaLabel" role="dialog" tabindex="-1" class="modal hide fade" id="nota_modaleElimina">
	<div class="modal-body">
		<div class="alert alert-error">
			<button data-dismiss="alert" class="close" type="button">&times;</button>
			<p><strong>Attenzione!</strong></p>
			<p><strong>Elemento selezionato: <span id="nota_SPAN_elementoSelezionato"></span></strong></p>
			<p>Stai per eliminare l'elemento selezionato: sei sicuro di voler proseguire?</p>
		</div>
	</div>
	<div class="modal-footer">
		<button type="button" aria-hidden="true" data-dismiss="modal" class="btn">no, indietro</button>
		<button type="button" class="btn btn-primary" id="nota_pulsanteSiElimina">
			s&iacute;, prosegui&nbsp;<i class="icon-spin icon-refresh spinner" id="nota_SPINNER_pulsanteSiElimina"></i>
		</button>
	</div>
</div>
<div id="nota_divModaleMovimentiIva"><%--<s:include value="/jsp/documentoIva/modaleMovimentiIva.jsp" />--%></div>