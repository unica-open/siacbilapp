<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="datiAnagraficaDocumento_aggiornamento" class="step-pane active">
	<div class="accordion">
		<div class="accordion-group">
			<div class="accordion-heading">
				<a href="#collapseDatiAnagraficaDocumento_aggiornamento" data-parent="#datiAnagraficaDocumento_aggiornamento"
						data-toggle="collapse" class="accordion-toggle collapsed">
					Dati principali<span class="icon">&nbsp;</span>
				</a>
			</div>
			<div class="accordion-body collapse" id="collapseDatiAnagraficaDocumento_aggiornamento">
				<div class="accordion-inner">
					<fieldset class="form-horizontal">
						<div class="boxOrInLeft">
							<p>Dati anagrafica</p>
							<ul class="htmlelt">
								<li>
									<dfn>Data emissione</dfn>
									<dl><s:property value="documento.dataEmissione" />&nbsp;</dl>
								</li>
								<li>
									<dfn>Data scadenza</dfn>
									<dl><s:property value="documento.dataScadenza" />&nbsp;</dl>
								</li>
								<li>
									<dfn>Codice fiscale</dfn>
									<dl><s:property value="soggetto.codiceFiscale"/>&nbsp;</dl>
								</li>
								<li>
									<dfn>Partita iva</dfn>
									<dl><s:property value="soggetto.partitaIva"/>&nbsp;</dl>
								</li>
								<li>
									<dfn>Descrizione</dfn>
									<dl><s:property value="documento.descrizione" />&nbsp;</dl>
								</li>
								<li>
									<dfn>Sede secondaria</dfn>
									<dl><s:property value="sedeSecondariaSoggetto.denominazione" />&nbsp;</dl>
								</li>
							</ul>
						</div>
						<div class="boxOrInRight">
							<p>Importi</p>
							<ul class="htmlelt">
								<li>
									<dfn>Importo totale documento</dfn>
									<dl><s:property value="documento.importo" />&nbsp;</dl>
								</li>
								<li>
									<dfn>Importo non rilevante iva</dfn>
									<dl><s:property value="importoNonRilevanteIva" />&nbsp;</dl>
								</li>
								<li>
									<dfn>Importo rilevante iva</dfn>
									<dl><s:property value="importoRilevanteIva" />&nbsp;</dl>
								</li>
								<s:if test="tipoSubdocumentoIvaQuota">
									<li>
										<dfn>Importo quota</dfn>
										<dl><s:property value="subdocumento.importo" />&nbsp;</dl>
									</li>
									
									<li>
										<dfn>Numero quota</dfn>
										<dl><s:property value="subdocumento.numero" />&nbsp;</dl>
									</li>
								</s:if>
							</ul>
						</div>
					</fieldset>
				</div>
			</div>
		</div>
	</div>
</div>
<h4 class="step-pane">Dati iva</h4>
<fieldset>
	<s:if test="flagIntracomunitarioUtilizzabile">
		<div class="control-group">
			<label for="flagIntracomunitario_aggiornamento" class="control-label">Documento intracomunitario</label>
			<div class="controls">
				<s:checkbox id="flagIntracomunitario_aggiornamento" name="subdocumentoIva.flagIntracomunitario" cssClass="flagIntracomunitario" disabled="true"
						data-maintain="" />
				<span id="campiDocumentoIntracomunitario" class="fade<s:if test="subdocumentoIva.flagIntracomunitario"> in</s:if>">
					<span class="alRight">
						<label for="valuta_aggiornamento" class="radio inline">valuta estera</label>
					</span>
					<s:select list="listaValuta" name="valuta.uid" cssClass="lbTextSmall span2" id="valuta_aggiornamento" headerKey="0" headerValue=""
							listKey="uid" listValue="descrizione" disabled="true" data-maintain="" />
					<span class="alRight">
						<label for="importoInValuta_aggiornamento" class="radio inline">importo in valuta</label>
					</span>
					<s:textfield id="importoInValuta_aggiornamento" name="importoInValuta" cssClass="lbTextSmall span2 soloNumeri decimale"
							disabled="true" data-maintain="" />
				</span>
			</div>
		</div>
	</s:if>
	<div class="control-group">
		<label for="progressivoIVASubdocumentoIva_aggiornamento" class="control-label">N. registrazione *</label>
		<div class="controls">
			<s:textfield id="progressivoIVASubdocumentoIva_aggiornamento" name="subdocumentoIva.progressivoIVA" cssClass="lbTextSmall span2" 
				required="true" disabled="true" data-maintain="" />
			<span class="alRight">
				<label for="dataRegistrazioneSubdocumentoIva_aggiornamento" class="radio inline">Data registrazione *</label>
			</span>
			<s:textfield id="dataRegistrazioneSubdocumentoIva_aggiornamento" name="subdocumentoIva.dataRegistrazione" cssClass="lbTextSmall span2"
				required="true" disabled="true" data-maintain="" />
		</div>
	</div>
	<div class="control-group">
		<label for="tipoRegistrazioneIvaSubdocumentoIva_aggiornamento" class="control-label">Tipo registrazione *</label>
		<div class="controls">
			<s:select list="listaTipoRegistrazioneIva" name="subdocumentoIva.tipoRegistrazioneIva.uid" id="tipoRegistrazioneIvaSubdocumentoIva_aggiornamento"
				headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" cssClass="span6" disabled="true" data-maintain="" />
			<s:hidden name="subdocumentoIva.tipoRegistrazioneIva.uid" />
		</div>
	</div>
	<div class="control-group">
		<label for="tipoRegistroIva_aggiornamento" class="control-label">Tipo registro iva *</label>
		<div class="controls">
			<s:select list="listaTipoRegistroIva" name="tipoRegistroIva" id="tipoRegistroIva_aggiornamento" headerKey="" headerValue=""
				listValue="%{codice + ' - ' + descrizione}" required="true" cssClass="span6" disabled="true" data-maintain="" />
			<s:hidden name="tipoRegistroIva" />
		</div>
	</div>
	<div class="control-group">
		<label for="attivitaIva_aggiornamento" class="control-label">Attivit&agrave;</label>
		<div class="controls">
			<s:select list="listaAttivitaIva" name="attivitaIva.uid" id="attivitaIva_aggiornamento" headerKey="0" headerValue=""
				listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" cssClass="span6" disabled="true" data-maintain="" />
			<s:hidden name="attivitaIva.uid" />
			<span class="alRight">
				<label for="flagRilevanteIRAPSubdocumentoIva_aggiornamento" class="radio inline">Rilevante IRAP</label>
			</span>
			<s:checkbox name="subdocumentoIva.flagRilevanteIRAP" id="flagRilevanteIRAPSubdocumentoIva_aggiornamento" disabled="true" />
		</div>
	</div>
	<div class="control-group">
		<label for="registroIvaSubdocumentoIva_aggiornamento" class="control-label">Registro *</label>
		<div class="controls">
			<s:select list="listaRegistroIva" name="subdocumentoIva.registroIva.uid" cssClass="span6" id="registroIvaSubdocumentoIva_aggiornamento" 
				headerKey="0" headerValue="" listKey="uid" listValue="%{codice + ' - ' + descrizione}" required="true" disabled="true" data-maintain="" />
			<s:hidden name="subdocumentoIva.registroIva.uid" />
		</div>
	</div>
	<div class="control-group <s:if test='!flagProtocolloProvvisorio'>hide</s:if>" id="gruppoProtocolloProvvisorio_aggiornamento">
		<label for="numeroProtocolloProvvisorioSubdocumentoIva_aggiornamento" class="control-label">Protocollo provvisorio</label>
		<div class="controls">
			<s:textfield id="numeroProtocolloProvvisorioSubdocumentoIva_aggiornamento" name="subdocumentoIva.numeroProtocolloProvvisorio" 
				cssClass="lbTextSmall span2 soloNumeri" placeholder="%{'numero'}" disabled="true" data-maintain="" />
			<span class="alRight">
				<label for="dataProtocolloProvvisorioSubdocumentoIva_aggiornamento" class="radio inline">In data</label>
			</span>
			<s:textfield id="dataProtocolloProvvisorioSubdocumentoIva_aggiornamento" name="subdocumentoIva.dataProtocolloProvvisorio" disabled="true"
				cssClass="lbTextSmall span2 datepicker" placeholder="%{'data'}" data-original-value="%{subdocumentoIva.dataProtocolloProvvisorio}" />
		</div>
	</div>
	<div class="control-group <s:if test='flagProtocolloProvvisorio'>hide</s:if>" id="gruppoProtocolloDefinitivo_aggiornamento">
		<label for="numeroProtocolloDefinitivoSubdocumentoIva_aggiornamento" class="control-label">Protocollo definitivo </label>
		<div class="controls">
			<s:textfield id="numeroProtocolloDefinitivoSubdocumentoIva_aggiornamento" name="subdocumentoIva.numeroProtocolloDefinitivo"
				cssClass="lbTextSmall span2 soloNumeri" placeholder="%{'numero'}" disabled="true" data-maintain="" />
			<span class="alRight">
				<label for="dataProtocolloDefinitivoSubdocumentoIva_aggiornamento" class="radio inline">In data</label>
			</span>
			<s:textfield id="dataProtocolloDefinitivoSubdocumentoIva_aggiornamento" name="subdocumentoIva.dataProtocolloDefinitivo" disabled="true"
				cssClass="lbTextSmall span2 datepicker" placeholder="%{'data'}" data-original-value="%{subdocumentoIva.dataProtocolloDefinitivo}" />
		</div>
	</div>
	<s:if test="tipoSubdocumentoIvaQuota">
		<div class="control-group">
			<label for="numeroOrdinativoDocumentoSubdocumentoIva_aggiornamento" class="control-label">N. ordinativo</label>
			<div class="controls">
				<s:textfield id="numeroOrdinativoDocumentoSubdocumentoIva_aggiornamento" name="subdocumentoIva.numeroOrdinativoDocumento"
					cssClass="lbTextSmall span2" disabled="true" data-maintain="" />
				<span class="alRight">
					<label for="dataOrdinativoDocumentoSubdocumentoIva_aggiornamento" class="radio inline">Data ordinativo</label>
				</span>
				<s:textfield id="dataOrdinativoDocumentoSubdocumentoIva_aggiornamento" name="subdocumentoIva.dataOrdinativoDocumento"
					cssClass="lbTextSmall span2" disabled="true" data-maintain="" />
				<span class="alRight">
					<label for="dataCassaDocumentoSubdocumentoIva_aggiornamento" class="radio inline">Data pagamento</label>
				</span>
				<s:textfield id="dataCassaDocumentoSubdocumentoIva_aggiornamento" name="subdocumentoIva.dataCassaDocumento"
					cssClass="lbTextSmall span2" disabled="true" data-maintain="" />
			</div>
		</div>
	</s:if>
	
	<div class="control-group">
		<label for="descrizioneIvaSubdocumentoIva_aggiornamento" class="control-label">Descrizione</label>
		<div class="controls">
			<s:textarea id="descrizioneIvaSubdocumentoIva_aggiornamento" name="subdocumentoIva.descrizioneIva" cssClass="input-medium span9"
				cols="15" rows="2" maxlength="500" data-original-value="%{subdocumentoIva.descrizioneIva}" disabled="true"></s:textarea>
		</div>
	</div>
	<p>
		<a data-target="#collapseMovimentiIva_aggiornamento" class="btn btn-primary disabled">movimenti iva</a>
	</p>
	
	<div class="collapse" id="collapseMovimentiIva_aggiornamento">
	</div>
</fieldset>
<div class="Border_line"></div>
<p class="margin-medium">
	<s:include value="/jsp/include/indietro.jsp" />
</p>